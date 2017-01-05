package com.zzu.ehome.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.DemoContext;
import com.zzu.ehome.MapLocationActivity;
import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.Tool;
import com.zzu.ehome.view.HeadView;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.LocationMessage;

/**
 * Created by Bob on 15/8/18.
 * 会话页面
 */
public class ConversationActivity extends BaseActivity implements RongIM.LocationProvider, RongIM.ConversationBehaviorListener{

    private TextView mTitle;
    private RelativeLayout mBack;
    String name;
    private String mTargetId;
    String token = null;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        Intent intent = getIntent();
        getIntentDate(intent);
        setActionBar();
        RongIM.setLocationProvider(this);
        RongIM.setConversationBehaviorListener(this);

        isReconnect(intent);
        if(!CommonUtils.isNotificationEnabled(ConversationActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");


        name = getIntent().getData().getQueryParameter("title");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();
        fragment.setUri(uri);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.replace(R.id.rong_content, fragment);
        transaction.commit();
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {




        if (DemoContext.getInstance() != null) {

            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
        }

        //push或通知过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息

            if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
                CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                    @Override
                    public void OnSuccess(String userid) {
                        enterFragment(mConversationType, mTargetId);
                    }
                });
               
            } else {

                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                        CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                            @Override
                            public void OnSuccess(String userid) {
                                enterFragment(mConversationType, mTargetId);
                            }
                        });

                    }else {

                        enterFragment(mConversationType, mTargetId);
                    }
                }
            }
        }
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBar() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, name, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });


    }



    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {

        mTitle.setText(targetid);
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(CustomApplcation.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {
                    Log.d("LoginActivity", "--onSuccess" + s);

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("errorCode", "--errorCode" + errorCode);


                }
            });
        }
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }



    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context arg0, View arg1, Message arg2) {
        // TODO Auto-generated method stub
        if (arg2.getContent() instanceof LocationMessage) {
            Intent intent = new Intent(ConversationActivity.this, MapLocationActivity.class);
            intent.putExtra("location", arg2.getContent());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }



        return false;
    }

    @Override
    public void onStartLocation(Context context, LocationCallback callback) {
        Tool.mLastLocationCallback=callback;
        Intent intent = new Intent(context, MapLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
