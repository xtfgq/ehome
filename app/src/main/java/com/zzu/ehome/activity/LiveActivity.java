package com.zzu.ehome.activity;

import android.content.Context;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupSystemElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;

import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;

import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.BarrageView;
import com.zzu.ehome.view.HeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by guoqiang on 2017/4/18.
 */

public class LiveActivity extends SupperBaseActivity implements ITXLivePlayListener {
    private TXCloudVideoView mPlayerView;
    private TXLivePlayer mLivePlayer = null;

    private int mCurrentRenderMode;
    private int mCurrentRenderRotation;
    StringBuffer mLogMsg = new StringBuffer("");
    private TXLivePlayConfig mPlayConfig;

    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;

    private boolean mHWDecode = false;
    private final int mLogMsgLenLimit = 3000;
    //缓存策略
    private static final int CACHE_STRATEGY_FAST = 1;  //极速
    private static final int CACHE_STRATEGY_SMOOTH = 2;  //流畅
    private static final int CACHE_STRATEGY_AUTO = 3;  //自动

    private static final float CACHE_TIME_FAST = 1.0f;
    private static final float CACHE_TIME_SMOOTH = 5.0f;

    private ImageView mLoadingView;

    private int mCacheStrategy = 0;
    private String playUrl, groupId;

    private TIMConversation mGroupConversation;

    private String name = "";
    private EditText et_send;

    private LinearLayout ll_room;
    BarrageView barrageView;
    private View layout_video;
    private boolean mVideoPlay;
    private InputMethodManager imm;


    private User user;
    private EHomeDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playUrl = this.getIntent().getStringExtra("playUrl");
        groupId = this.getIntent().getStringExtra("groupId");
        setContentView(R.layout.activity_live);

        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        mPlayConfig = new TXLivePlayConfig();
        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(LiveActivity.this);
        }
        mVideoPlay = false;
        this.setCacheStrategy(CACHE_STRATEGY_AUTO);
        layout_video = findViewById(R.id.layout_video);
        et_send = (EditText) findViewById(R.id.et_send);
        mLoadingView = (ImageView) findViewById(R.id.loadingImageView);
        ll_room = (LinearLayout) findViewById(R.id.ll_room);
        barrageView = (BarrageView) findViewById(R.id.containerView);
        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);
        mPlayerView.disableLog(true);
        mLivePlayer.setConfig(mPlayConfig);

        dao = new EHomeDaoImpl(this);
        user = dao.findUserInfoById(SharePreferenceUtil.getInstance(this).getUserId());
        name = user.getUsername();
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "视频教学", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                if (isScreenOriatationPortrait(LiveActivity.this)) {
                    finish();
                } else {
                    fullScreen();
                }

            }
        });
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(mCurrentRenderMode);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        et_send.setOnKeyListener(new MyKeyListener());
        joinIMChatRoom(groupId);
        findViewById(R.id.btn_fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreen();
            }
        });
    }


    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            stopLoadingAnimation();
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            stopPlayRtmp();
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlayRtmp();
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
            startLoadingAnimation();
        }
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        appendEventLog(event, msg);
        if (event < 0) {
            Toast.makeText(this, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            stopLoadingAnimation();
        }

    }

    private void stopPlayRtmp() {
        stopLoadingAnimation();
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    public void setCacheStrategy(int nCacheStrategy) {
        if (mCacheStrategy == nCacheStrategy) return;
        mCacheStrategy = nCacheStrategy;

        switch (nCacheStrategy) {
            case CACHE_STRATEGY_FAST:
                mPlayConfig.setAutoAdjustCacheTime(true);
                mPlayConfig.setMaxAutoAdjustCacheTime(CACHE_TIME_FAST);
                mPlayConfig.setMinAutoAdjustCacheTime(CACHE_TIME_FAST);
                mLivePlayer.setConfig(mPlayConfig);
                break;

            case CACHE_STRATEGY_SMOOTH:
                mPlayConfig.setAutoAdjustCacheTime(false);
                mPlayConfig.setCacheTime(CACHE_TIME_SMOOTH);
                mLivePlayer.setConfig(mPlayConfig);
                break;

            case CACHE_STRATEGY_AUTO:
                mPlayConfig.setAutoAdjustCacheTime(true);
                mPlayConfig.setMaxAutoAdjustCacheTime(CACHE_TIME_SMOOTH);
                mPlayConfig.setMinAutoAdjustCacheTime(CACHE_TIME_FAST);
                mLivePlayer.setConfig(mPlayConfig);
                break;

            default:
                break;
        }
    }

    private boolean startPlayRtmp() {
        if (!checkPlayUrl(playUrl)) {
            return false;
        }
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setPlayListener(this);

        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(mCurrentRenderMode);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);
        int result = mLivePlayer.startPlay(playUrl, mPlayType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result == -2) {
            Toast.makeText(this, "非腾讯云链接地址，若要放开限制，请联系腾讯云商务团队", Toast.LENGTH_SHORT).show();
        }
        if (result != 0) {

            return false;
        }
        mVideoPlay = true;

        return true;
    }

    private boolean checkPlayUrl(final String playUrl) {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://") && !playUrl.startsWith("/"))) {
            Toast.makeText(this, "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式和本地播放方式（绝对路径，如\"/sdcard/test.mp4\"）!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (playUrl.startsWith("rtmp://")) {
            mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
            mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
        } else {
            Toast.makeText(this, "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoPlay) {
            if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4 || mPlayType == TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO) {
                if (mLivePlayer != null) {
                    mLivePlayer.resume();
                }
            }

        }
        if (mPlayerView != null) {
            mPlayerView.onResume();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4 || mPlayType == TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO) {
            if (mLivePlayer != null) {
                mLivePlayer.pause();
            }
        }

        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true);
        }
        if (mPlayerView != null) {
            mPlayerView.onDestroy();
        }
        perpareQuitRoom();
    }

    public void perpareQuitRoom() {

        sendGroupMessage(Constants.AVIMCMD_EXITLIVE, "", new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                notifyQuitReady();
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                notifyQuitReady();
            }
        });

    }

    /**
     * 已经发完退出消息了
     */
    private void notifyQuitReady() {
        TIMManager.getInstance().removeMessageListener(msgListener);
    }

    private void joinIMChatRoom(final String chatRoomId) {
        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatRoomId);
        TIMGroupManager.getInstance().applyJoinGroup(chatRoomId, Constants.APPLY_CHATROOM + chatRoomId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                //已经在是成员了
                if (i == Constants.IS_ALREADY_MEMBER) {
                    initTIMListener(chatRoomId);
                }
            }

            @Override
            public void onSuccess() {
                initTIMListener(chatRoomId);
            }
        });

    }

    private void startLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mLoadingView.getDrawable()).start();
        }
    }

    private void stopLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
            ((AnimationDrawable) mLoadingView.getDrawable()).stop();
        }
    }

    /**
     * 初始化聊天室  设置监听器
     */
    public void initTIMListener(String chatRoomId) {
        startPlayRtmp();
        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatRoomId);
        TIMManager.getInstance().addMessageListener(msgListener);

    }

    /**
     * 群消息回调
     */
    private TIMMessageListener msgListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {

            parseIMMessage(list);
            return false;
        }
    };

    /**
     * 解析消息回调
     *
     * @param list 消息列表
     */
    private void parseIMMessage(List<TIMMessage> list) {
        List<TIMMessage> tlist = list;

        if (tlist.size() > 0) {
            if (mGroupConversation != null)
                mGroupConversation.setReadMessage(tlist.get(0));
        }

        for (int i = tlist.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = tlist.get(i);
            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null)
                    continue;
                TIMElem elem = currMsg.getElement(j);
                TIMElemType type = elem.getType();

                //系统消息
                if (type == TIMElemType.GroupSystem) {
                    if (TIMGroupSystemElemType.TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE == ((TIMGroupSystemElem) elem).getSubtype()) {

                    }
                }

                //最后处理文本消息
                if (type == TIMElemType.Text) {
                    if (currMsg.isSelf()) {
                        handleTextMessage(elem, name);
                    } else {
                        TIMUserProfile sendUser = currMsg.getSenderProfile();
                        String nick = "";
                        if (sendUser != null) {
                            nick = sendUser.getNickName();
                        } else {
                            nick = currMsg.getSender();
                        }

                        handleTextMessage(elem, nick);
                    }
                }
            }
        }
    }

    /**
     * 处理文本消息解析
     *
     * @param elem
     * @param name
     */
    private void handleTextMessage(TIMElem elem, String name) {
        TIMTextElem textElem = (TIMTextElem) elem;
        if (textElem.getText() != null) {
            sendMsg(name + ":" + textElem.getText());
        }

    }

    private void sendMsg(String msg) {
        TextView textView = new TextView(LiveActivity.this);
        textView.setText(msg);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(2, 10, 2, 10);
        textView.setLayoutParams(lp);
        ll_room.addView(textView, 0);
        barrageView.addMessage(msg);
    }

    private void fullScreen() {
        if (isScreenOriatationPortrait(this)) {
            full(true);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            hideActionBar();
            ll_room.setVisibility(View.INVISIBLE);
            et_send.setVisibility(View.VISIBLE);
            layout_video.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        } else {
            full(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //显示其他组件
            showActionBar();
            ll_room.setVisibility(View.VISIBLE);
            et_send.setVisibility(View.VISIBLE);
            int width = getResources().getDisplayMetrics().heightPixels;
            int height = (int) (width * 9.0 / 16);
            layout_video.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            mPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
            findViewById(R.id.btn_fullscreen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreen();
                }
            });

        }
    }

    public void onBackPressed() {
        if (isScreenOriatationPortrait(this)) {
            finish();

        } else {
            fullScreen();
        }
    }

    //动态隐藏状态栏
    private void full(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
    }


    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

    }

    private class MyKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (et_send.getText().length() > 0) {
                    sendText("" + et_send.getText());
                    imm.showSoftInput(et_send, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(et_send.getWindowToken(), 0);

                } else {
                    Toast.makeText(LiveActivity.this, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }

                return true;
            }
            return false;
        }
    }

    private void sendText(String msg) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "输入内容太长!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        TIMMessage Nmsg = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();

        elem.setText(msg);
        if (Nmsg.addElement(elem) != 0) {
            return;
        }

        sendMessage(Nmsg);

    }

    public void sendMessage(TIMMessage msg) {
        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
        mGroupConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                if (code == 85) { //消息体太长
                    if (null != this) {
                        Toast.makeText(LiveActivity.this, "输入内容太长", Toast.LENGTH_SHORT).show();
                    }
                } else if (code == 6011) {//群主不存在
                    if (null != this) {
                        Toast.makeText(LiveActivity.this, "群主不存在", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onSuccess(TIMMessage timMessage) {//发送消息成功
                et_send.setText("");
                for (int j = 0; j < timMessage.getElementCount(); j++) {
                    TIMElem elem = (TIMElem) timMessage.getElement(0);
                    if (timMessage.isSelf()) {
                        handleTextMessage(elem, name);
                    } else {
                        TIMUserProfile sendUser = timMessage.getSenderProfile();
                        String name;
                        if (sendUser != null) {
                            name = sendUser.getNickName();
                        } else {
                            name = timMessage.getSender();
                        }
                        handleTextMessage(elem, name);
                    }
                }
            }
        });
    }

    public void sendGroupMessage(int cmd, String param, TIMValueCallBack<TIMMessage> callback) {
        JSONObject inviteCmd = new JSONObject();
        try {
            inviteCmd.put(Constants.CMD_KEY, cmd);
            inviteCmd.put(Constants.CMD_PARAM, param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String cmds = inviteCmd.toString();
        TIMMessage Gmsg = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(cmds.getBytes());
        elem.setDesc("");
        Gmsg.addElement(elem);

        if (mGroupConversation != null)
            mGroupConversation.sendMessage(Gmsg, callback);
    }

    //公用打印辅助函数
    protected void appendEventLog(int event, String message) {
        String str = "receive event: " + event + ", " + message;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String date = sdf.format(System.currentTimeMillis());
        while (mLogMsg.length() > mLogMsgLenLimit) {
            int idx = mLogMsg.indexOf("\n");
            if (idx == 0)
                idx = 1;
            mLogMsg = mLogMsg.delete(0, idx);
        }
        mLogMsg = mLogMsg.append("\n" + "[" + date + "]" + message);
    }


}
