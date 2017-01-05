package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zzu.ehome.R;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Conversation;


/**
 * Created by Bob on 15/8/18.
 * 聚合会话列表
 * 什么是聚合会话列表？
 */
public class SubConversationListActivtiy extends BaseActivity {


    private RelativeLayout mBack;
    /**
     * 聚合类型
     */
    private String type,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);
        SubConversationListFragment fragment = new SubConversationListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");
        if (type == null )
            return;
        getActionBarTitle();
        if (!CommonUtils.isNotificationEnabled(SubConversationListActivtiy.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    /**
     * 通过 intent 中的数据，得到当前的 targetId 和 type
     */
    private void getActionBarTitle() {

        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        type = intent.getData().getQueryParameter("type");


        if (type.equals("group")) {
            title="聚合群组";

        } else if (type.equals("private")) {
            title="聚合单聊";

        } else if (type.equals("discussion")) {
            title="聚合讨论组";

        } else if (type.equals("system")) {
            title="聚合系统会话";

        } else {
            title="聚合";

        }
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
//        if(RongIM.getInstance() != null)
//            RongIM.getInstance().startSubConversationList(this, Conversation.ConversationType.GROUP);
//        RongIM.getInstance().startSubConversationList(this, Conversation.ConversationType.PRIVATE);

    }
}

