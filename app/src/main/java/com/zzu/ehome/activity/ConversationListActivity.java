package com.zzu.ehome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zzu.ehome.DemoContext;
import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.fragment.BaseFragment;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 15/8/18.
 * 会话列表
 */
public class ConversationListActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        setActionBarTitle();
        isReconnect();
        if(!CommonUtils.isNotificationEnabled(ConversationListActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private Fragment enterFragment() {

        ConversationListFragment fragment =  ConversationListFragment.getInstance();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();


        fragment.setUri(uri);
        return fragment;
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBarTitle() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "咨询记录", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });


    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect() {

        Intent intent = getIntent();
        String token = null;

        if (DemoContext.getInstance() != null) {
            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
        }
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push").equals("true")) {
                String id = intent.getData().getQueryParameter("pushId");

                CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                    @Override
                    public void OnSuccess(String userid) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, enterFragment())
                                .commit();

                    }
                });
            }

        } else {//通知过来
            //程序切到后台，收到消息后点击进入,会执行这里
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                    @Override
                    public void OnSuccess(String userid) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, enterFragment())
                                .commit();
                    }
                });
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, enterFragment())
                        .commit();
            }
        }
    }


}
