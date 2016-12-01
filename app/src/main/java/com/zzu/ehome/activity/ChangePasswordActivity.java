package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.service.RegisterCodeTimerService;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RegisterCodeTimer;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

import static com.zzu.ehome.R.id.tv_getCode;

public class ChangePasswordActivity extends BaseActivity {
    private EditText edt_old_Pass;
    private EditText edt_new_Pass;
    private EditText edt_new_Pass_again;
    private Button btn_update;
    private String userid;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    private final String mPageName = "ChangePasswordActivity";
    private  Intent mIntent;
    private String chkcode="";
    private TextView tvgetcode;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        userid = SharePreferenceUtil.getInstance(ChangePasswordActivity.this).getUserId();
        requestMaker = RequestMaker.getInstance();
        dao = new EHomeDaoImpl(ChangePasswordActivity.this);
        initViews();
        user = dao.findUserInfoById(userid);
        edt_old_Pass.setText(user.getMobile());
        edt_old_Pass.setFocusable(false);
        edt_old_Pass.setEnabled(false);
        initEvent();
        RegisterCodeTimerService.setHandler(mCodeHandler);
    }
    Handler mCodeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
                tvgetcode.setText(msg.obj.toString());
                tvgetcode.setEnabled(false);
            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
                tvgetcode.setEnabled(true);
                tvgetcode.setText(msg.obj.toString());
            }
        }

        ;
    };

    private void initViews() {
        edt_old_Pass = (EditText) findViewById(R.id.edt_old_Pass);
        edt_new_Pass = (EditText) findViewById(R.id.edt_new_Pass);
        edt_new_Pass_again = (EditText) findViewById(R.id.edt_new_Pass_again);
        btn_update = (Button) findViewById(R.id.btn_update);
        tvgetcode=(TextView)findViewById(tv_getCode);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "密码修改", new HeadView.OnLeftClickListener() {
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
        mIntent = new Intent(ChangePasswordActivity.this,
                RegisterCodeTimerService.class);
        tvgetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edt_old_Pass.getText().toString().trim())){
                    ToastUtils.showMessage(ChangePasswordActivity.this,"请输入手机号！");
                    return;
                }
                doGetCode();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdate();
            }
        });
    }
    /**
     * 获取验证码
     */
    public void doGetCode() {
        CHKCodeSend();


    }
    private void CHKCodeSend() {

        String usermobile = edt_old_Pass.getText().toString().trim();


        if (IOUtils.isMobileNO(usermobile)) {
            requestMaker.sendAuthCode(usermobile, new JsonAsyncTask_Info(ChangePasswordActivity.this, true, new JsonAsyncTaskOnComplete() {
                @Override
                public void processJsonObject(Object result) {
                    String value = result.toString();
//               {"SendAuthCode":[{"MessageContent":"296283","MessageCode":"0"}]}

                    try {
                        JSONObject mySO = (JSONObject) result;
                        JSONArray array = mySO
                                .getJSONArray("SendAuthCode");
                        if(Integer.valueOf(array.getJSONObject(0)
                                .getString("MessageCode"))==0) {
                            startService(mIntent);
                            for (int i = 0; i < array.length(); i++) {
                                chkcode = array.getJSONObject(i)
                                        .getString("MessageContent");
                               ToastUtils.showMessage(ChangePasswordActivity.this,chkcode+"");



                            }
                        }else{
                            ToastUtils.showMessage(ChangePasswordActivity.this,array.getJSONObject(0)
                                    .getString("MessageContent"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    public void doUpdate() {

        String oldPsd = edt_old_Pass.getText().toString().trim();
        String newPsd = edt_new_Pass.getText().toString().trim();
        String newPsdAgain = edt_new_Pass_again.getText().toString().trim();
        if (TextUtils.isEmpty(oldPsd)) {
            ToastUtils.showMessage(ChangePasswordActivity.this, "手机号码不能为空");
            return;
        } else if (TextUtils.isEmpty(newPsd)) {
            ToastUtils.showMessage(ChangePasswordActivity.this, "验证码不能为空");
            return;
        } else if (!user.getMobile().equals(oldPsd)) {
            ToastUtils.showMessage(ChangePasswordActivity.this, R.string.checkmobile_register);
            return;
        }else if (!IOUtils.isMobileNO(oldPsd)) {
            ToastUtils.showMessage(ChangePasswordActivity.this, R.string.checkmobile_register);
            return;
        }

        else if (newPsdAgain.length() < 6||newPsdAgain.equals("设置新密码（6位以上）")) {
            ToastUtils.showMessage(ChangePasswordActivity.this, "密码长度不能小于6位");
            return;

        } else if (!newPsd.equals(chkcode)) {
            ToastUtils.showMessage(ChangePasswordActivity.this, R.string.erro_code_register);
            return;

        }

        final String nPsd = newPsdAgain;
        requestMaker.UserAuthChange(user.getMobile(), user.getPassword(), nPsd, new JsonAsyncTask_Info(ChangePasswordActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("TAG", result.toString());
                try {
                    JSONArray array = mySO.getJSONArray("UserAuthChange");
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    String msg = jsonObject.getString("MessageCode");


                    if ("0".equals(msg)) {
                        ToastUtils.showMessage(ChangePasswordActivity.this, jsonObject.getString("MessageContent"));
                        User dbUser = dao.findUserInfoById(userid);
                        dbUser.setPassword(nPsd);
                        dao.updateUserInfo(dbUser, userid);
                        UserClientBind();

                    } else {
                        ToastUtils.showMessage(ChangePasswordActivity.this, jsonObject.getString("MessageContent"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }


        protected void onDestroy() {
            super.onDestroy();
            stopService(mIntent);
        }
    private void UserClientBind() {
        startProgressDialog();
        requestMaker.loginOut(userid, new JsonAsyncTask_Info(ChangePasswordActivity.this, true, new JsonAsyncTaskOnComplete() {
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

                        RongIM.getInstance().logout();

                        SharePreferenceUtil.getInstance(ChangePasswordActivity.this).setUserId("");
                        SharePreferenceUtil.getInstance(ChangePasswordActivity.this).setHomeId("");
                        startActivity(new Intent(ChangePasswordActivity.this,LoginActivity1.class));
                        Intent intenthealth = new Intent("refresh");
                        sendBroadcast(intenthealth);
                        finishActivity();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
    }

}
