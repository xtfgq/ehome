package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.DemoContext;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by zzu on 2016/4/15.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //    private RelativeLayout layout_about;
    private RelativeLayout layout_xgmm;
    private RelativeLayout layout_qchc;
    private RelativeLayout layout_exit;
    private RelativeLayout layout_convation;
    private RequestMaker request;
    private String userid;
    private final String mPageName = "SettingActivity";
    private EHomeDao dao;
    private float weight;
    private double calories = 0;
    private int step_length = 55;
    private int minute_distance = 80;
    private String timeCount;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_setting);
        request = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(SettingActivity.this).getUserId();
        dao = new EHomeDaoImpl(this);
        initViews();
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(SettingActivity.this).getWeight())) {
            weight = 50.0f;
        } else {
            weight = Float.parseFloat(SharePreferenceUtil.getInstance(SettingActivity.this).getWeight());
        }
        initEvent();
        if(!CommonUtils.isNotificationEnabled(SettingActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
//        layout_about=(RelativeLayout)findViewById(R.id.layout_about);
        layout_xgmm = (RelativeLayout) findViewById(R.id.layout_xgmm);
        layout_qchc = (RelativeLayout) findViewById(R.id.layout_qchc);
        layout_exit = (RelativeLayout) findViewById(R.id.layout_exit);
        layout_convation=(RelativeLayout)findViewById(R.id.layout_convation);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "设置", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    public void initEvent() {
//        layout_about.setOnClickListener(this);
        layout_xgmm.setOnClickListener(this);
        layout_qchc.setOnClickListener(this);
        layout_exit.setOnClickListener(this);
        layout_convation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        switch (v.getId()) {
//            case R.id.layout_about:
//                intentAction(SettingActivity.this,AboutEhomeActivity.class);
//                break;
            case R.id.layout_xgmm:
                if (!TextUtils.isEmpty(userid)) {
                    intentAction(SettingActivity.this, ChangePasswordActivity.class);
                } else {
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }

                break;
            case R.id.layout_qchc:
                DialogTips dialog = new DialogTips(SettingActivity.this, "", "是否清理缓存?",
                        "确定", true, true);
                dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int userId) {
                        startProgressDialog();
                        showTips();
                    }
                });

                dialog.show();
                dialog = null;
                break;
            case R.id.layout_convation:
                DialogTips dialog2 = new DialogTips(SettingActivity.this, "", "是否清理聊天记录?",
                        "确定", true, true);
                dialog2.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int userId) {
                        clearConversation();
                    }
                });

                dialog2.show();
                dialog2 = null;
             break;
            case R.id.layout_exit:
                if (CommonUtils.isFastClick())
                    return;
                if (TextUtils.isEmpty(dao.findUserInfoById(userid).getUserno())) {
                    UserClientBind();
                } else {
                    upload();
                }
//                if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(SettingActivity.this).getUserId())){
//                    Intent i = new Intent(SettingActivity.this, LoginActivity.class);
//                    i.putExtra("logout", "logout");
//                    startActivity(i);
//                }else {
//                    upload();
//                }
                break;
        }
    }

    private void upload() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01) / 1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        String cardNo = dao.findUserInfoById(userid).getUserno();
        request.StepCounterInsert(cardNo, userid, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f", calories), sdf.format(new Date()), new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInsert");
                    SharePreferenceUtil.getInstance(SettingActivity.this).setWeight("");
                    UserClientBind();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    private void UserClientBind() {
        startProgressDialog();
        request.loginOut(userid, new JsonAsyncTask_Info(SettingActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String UserClientBind = result.toString();
                if (UserClientBind == null) {

                } else {
                    stopProgressDialog();

                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserClientIDChange");
                        StepDetector.CURRENT_SETP = 0;
                        ToastUtils.showMessage(SettingActivity.this, "退出登陆成功");

                        RongIM.getInstance().logout();
                        RongIM.getInstance().disconnect();
                        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                        edit.putString("DEMO_TOKEN", "");
                        edit.apply();

                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                        StepBean step = new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(0);
                        step.setUserid("");
                        step.setUploadState(0);
                        dao.updateStep(step);
                        SharePreferenceUtil.getInstance(SettingActivity.this).setUserId("");
                        SharePreferenceUtil.getInstance(SettingActivity.this).setHomeId("");
                        SharePreferenceUtil.getInstance(SettingActivity.this).setSmartSearchCode("");
                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                        Intent intenthealth = new Intent("userrefresh");
                        sendBroadcast(intenthealth);
                        finishActivity();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public void showTips() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopProgressDialog();
                DialogTips dialog = new DialogTips(SettingActivity.this, "清除成功!", "确定");

                dialog.show();
                dialog = null;
            }
        }, 3000);
    }
    private void clearConversation(){
        if (DemoContext.getInstance() != null) {
            String token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {

                CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                    @Override
                    public void OnSuccess(String userid) {

                        try {
                            clearMessage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else{
                clearMessage();
            }
        }
        }

    /**
     * 清除本地聊天记录
     */
    private void clearMessage(){

        RongIM.getInstance().getRongIMClient().clearConversations(new RongIMClient.ResultCallback<Boolean>(){
            @Override
            public void onSuccess(Boolean aBoolean) {
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        },Conversation.ConversationType.PRIVATE);
    }

}
