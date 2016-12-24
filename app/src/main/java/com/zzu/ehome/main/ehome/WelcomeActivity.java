package com.zzu.ehome.main.ehome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.GuideActivity;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

public class WelcomeActivity extends FragmentActivity {

    private static final long SPLASH_DELAY_MILLIS = 1000;

    private static final int GO_HOME = 0X00;
    private static final int GO_GUIDE = 0X01;
    String userid, ClientID;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    private final String mPageName = "WelcomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        PushManager.getInstance().initialize(this.getApplicationContext());
        ClientID = PushManager.getInstance().getClientid(WelcomeActivity.this);
        dao = new EHomeDaoImpl(this);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(WelcomeActivity.this).getUserId();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:

//                    if(userid.equals("")){
//                        goLogin();
//                    }else {
//                        User user = dao.findUserInfoById(userid);
//                        if(user!=null) {
//                            login(user.getMobile(), user.getPassword());
//                        }else{
//                            goLogin();
//                        }
//                    }
                    goHome();

                    break;
                case GO_GUIDE:
                    goGuide();

                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void init() {

        if (!SharePreferenceUtil.getInstance(WelcomeActivity.this).getIsFirst()) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }
    }

    private void goHome() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();

    }

    private void goGuide() {
        startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
        finish();
    }






}
