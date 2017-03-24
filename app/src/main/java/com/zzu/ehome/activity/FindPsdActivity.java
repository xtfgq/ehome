package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;


import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.service.RegisterCodeTimerService;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RegisterCodeTimer;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zzu on 2016/4/8.
 */
public class FindPsdActivity extends BaseActivity implements View.OnClickListener {
    private final String mPageName = "FindPsdActivity";

    private EditText editPhone;
    private EditText edCode;

    private EditText editPass_again;
    private Button btn_save;
    private TextView tv_getCode;
    private Intent mIntent;
    private String usermobile, chkcode="", usrpwd,usermobileold="";
    private RequestMaker requestMaker;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_findpsd);
        requestMaker = RequestMaker.getInstance();
        initViews();
        RegisterCodeTimerService.setHandler(mCodeHandler);
        mIntent = new Intent(FindPsdActivity.this,
                RegisterCodeTimerService.class);
        initEvent();
        if(!CommonUtils.isNotificationEnabled(FindPsdActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        editPhone = (EditText) findViewById(R.id.editPhone);
        edCode = (EditText) findViewById(R.id.edCode);
//        editPass = (EditText) findViewById(R.id.editPass);
        editPass_again = (EditText) findViewById(R.id.editPass_again);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "找回密码",
                new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {

                        finishActivity();
                    }
                });



    }

    Handler mCodeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
                tv_getCode.setText(msg.obj.toString());
                tv_getCode.setEnabled(false);
            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
                tv_getCode.setEnabled(true);
                tv_getCode.setText(msg.obj.toString());
            }
        }

        ;
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(tv_getCode!=null) {
                        stopService(mIntent);
                        tv_getCode.setEnabled(true);
                        tv_getCode.setText("获取验证码");
                    }
                    break;


                default:
                    break;
            }
        }

    };


    public void initEvent() {
        btn_save.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
    }

    @Override
    public void onClick(View v) {
       if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if(!CommonUtils.isNotificationEnabled(FindPsdActivity.this)){
            showTitleDialog("请打开通知中心");

        }
        switch (v.getId()) {
            case R.id.tv_getCode:
                if (CommonUtils.isFastClick()) {
                    return;
                }

                if (TextUtils.isEmpty(editPhone.getText().toString().trim())) {
                   show("请输入手机号！");
                    return;
                }
                if (!IOUtils.isMobileNO(editPhone.getText().toString().trim())) {
                    show("请输入正确的手机号码!");
                    return;
                }
                if (isNetworkAvailable()) {
                    doGetCode();
                } else {
                    ToastUtils.showMessage(FindPsdActivity.this, R.string.msgUninternet);
                }
                break;
            case R.id.btn_save:
                if (CommonUtils.isFastClick()) {
                    return;
                }
                usermobile=editPhone.getText().toString().trim();

                usrpwd = editPass_again.getText().toString().trim();
                if (TextUtils.isEmpty(usermobile)) {
                    show("请输入手机号！");
                    return;
                } else if (usermobile.length() != 11) {
                    show("请输入正确的手机号码!");

                    return;
                } else if (!IOUtils.isMobileNO(editPhone.getText().toString().trim())) {
                    show("请输入正确的手机号码!");

                    return;
                } else if (!usermobile.equals(editPhone.getText().toString().trim())) {
                    show("请输入正确的手机号码!");

                    return;
                } else if(TextUtils.isEmpty(edCode.getText().toString().trim())){
                    show("请输入验证码");
                    return;
                }
                else if(!usermobileold.equals(usermobile)){
                    show("手机号与验证码不匹配");
                    return;
                }
                else if (!chkcode.equals(edCode.getText().toString().trim())) {
                    show("验证码不正确");
                    return;
                }
                else if (TextUtils.isEmpty(usrpwd)) {
                    show("请输入密码");
                    return;
                } else if(editPass_again.length()<6){
                    show( "密码长度不能小于6位");
                    return;
                }
               else if (isNetworkAvailable()) {
                    doFindPwd();
                } else {
                    ToastUtils.showMessage(FindPsdActivity.this, R.string.msgUninternet);
                }


        }

    }

    /**
     * 获取验证码
     */
    public void doGetCode() {
        CHKCodeSend();


    }

    private void CHKCodeSend() {
        tv_getCode.setEnabled(false);
        usermobile = editPhone.getText().toString().trim();
        if (TextUtils.isEmpty(usermobile)) {
            ToastUtils.showMessage(FindPsdActivity.this, R.string.mobile_register);

            return;
        }

        if (IOUtils.isMobileNO(usermobile)) {
            startService(mIntent);
            requestMaker.sendAuthCode(usermobile, new JsonAsyncTask_Info(FindPsdActivity.this, true, new JsonAsyncTaskOnComplete() {
                @Override
                public void processJsonObject(Object result) {
                    String value = result.toString();
                    usermobileold=usermobile;
                    try {
                        JSONObject mySO = (JSONObject) result;
                        JSONArray array = mySO
                                .getJSONArray("SendAuthCode");

                        if(Integer.valueOf(array.getJSONObject(0)
                                .getString("MessageCode"))==0) {

                            for (int i = 0; i < array.length(); i++) {
                                chkcode = array.getJSONObject(i)
                                        .getString("MessageContent");
//                              ToastUtils.showMessage(FindPsdActivity.this,chkcode+"");
                                ToastUtils.showMessage(FindPsdActivity.this,"验证码已发送，请注意查收。");
                            }
                        }else{
                            stopService(mIntent);
                            tv_getCode.setEnabled(true);
                            tv_getCode.setText("获取验证码");
                            showDialog(array.getJSONObject(0)
                                    .getString("MessageContent"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {
                    Message message = Message.obtain();
                    message.what = 0;
                    mHandler.sendMessage(message);
                }
            }));
        }
    }

    private void doFindPwd() {
        requestMaker.UserFindPass(usermobile, usrpwd, new JsonAsyncTask_Info(FindPsdActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String value = result.toString();


                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("UserFindPass");
                    ToastUtils.showMessage(FindPsdActivity.this, array.getJSONObject(0)
                            .getString("MessageContent"));
                    if (array.getJSONObject(0)
                            .getString("MessageCode").equals("0")) {
                        CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity.class);
                        startActivity(new Intent(FindPsdActivity.this,LoginActivity.class));
                        finishActivity();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));

    }
    private void showDialog(String message) {

        DialogTips dialog = new DialogTips(FindPsdActivity.this, message, "确定");

        dialog.show();
        dialog = null;

    }

}
