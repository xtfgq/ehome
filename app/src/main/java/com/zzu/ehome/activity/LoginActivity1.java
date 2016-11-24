package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;

import com.zzu.ehome.DemoContext;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.bean.StepCounterDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserDate;
import com.zzu.ehome.bean.UserInfoDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.NetUtils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2016/8/5.
 */
public class LoginActivity1 extends BaseActivity implements View.OnClickListener {
    /**
     * token 的主要作用是身份授权和安全，因此不能通过客户端直接访问融云服务器获取 token，
     * 您必须通过 Server API 从融云服务器 获取 token 返回给您的 App，并在之后连接时使用
     */
    private String token;
    private EHomeDao dao;
    private RequestMaker requestMaker;
    private String ClientID;
    private EditText mEditPhone, mEditPass;
    private Button login;
    private TextView tvselect, tvnewReg;
    private String tag = "",hometag="";
    private Intent mIntent;
    private String title = "登录";
    private double calories = 0;
    private int step_length = 55;
    private int minute_distance = 80;
    private String timeCount;
    private float weight;
    private String backUserid = "";
    private String userid = "",homeid="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        initViews();
        PushManager.getInstance().initialize(this.getApplicationContext());
        dao = new EHomeDaoImpl(this);
        mIntent = this.getIntent();
        requestMaker = RequestMaker.getInstance();
        if (mIntent != null) {
            if (mIntent.getStringExtra("relation") != null) {
                tag = mIntent.getStringExtra("relation");
                title = "添加家人";
                login.setText("添加");
                tvselect.setVisibility(View.GONE);
                if (mIntent.getStringExtra("usrid") != null) {
                    SharePreferenceUtil.getInstance(LoginActivity1.this).setPARENTID(SharePreferenceUtil.getInstance(LoginActivity1.this).getUserId());
                    userid = mIntent.getStringExtra("usrid");
                    getUser(userid);

                }
            }
            if(mIntent.getStringExtra("Home")!=null){
                hometag=mIntent.getStringExtra("Home");
            }
        }
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(LoginActivity1.this).getWeight())) {
            weight = 50.0f;
        } else {
            weight = Float.parseFloat(SharePreferenceUtil.getInstance(LoginActivity1.this).getWeight());
        }
        ClientID = PushManager.getInstance().getClientid(LoginActivity1.this);

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

                if (mIntent.getStringExtra("logout") != null && mIntent.getStringExtra("logout").equals("logout")) {
                    confirmExit();
                } else {
                    finishActivity();
               }
            }
        });

        login.setOnClickListener(this);
        tvselect.setOnClickListener(this);
        tvnewReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin:
                if (NetUtils.isNetworkConnected(LoginActivity1.this)) {
                    doLogin();
                } else {
                    ToastUtils.showMessage(LoginActivity1.this,"请连接网络！");

                }
                break;
            case R.id.tvselect:
                doForgetPsd();
                break;
            case R.id.tvnewReg:
                doReg();
                break;
        }

    }

    private void doReg() {
        if (TextUtils.isEmpty(tag)) {
            Intent intent = new Intent(LoginActivity1.this, RegisterActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity1.this, RegisterActivity.class);
            intent.putExtra("relation", "rela");
            startActivity(intent);
        }
    }

    private void initViews() {
        mEditPhone = (EditText) findViewById(R.id.editPhone);
        mEditPass = (EditText) findViewById(R.id.editPass);
        login = (Button) findViewById(R.id.btnlogin);
        tvselect = (TextView) findViewById(R.id.tvselect);
        tvnewReg = (TextView) findViewById(R.id.tvnewReg);
    }

    private void doLogin() {

        String name = mEditPhone.getText().toString().trim();
        String psd = mEditPass.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {

//            ToastUtils.showMessage(LoginActivity1.this,"请输入您的账号!");
            showDialog("请输入您的账号!");
            return;
        }
        if (TextUtils.isEmpty(psd)) {

//            ToastUtils.showMessage(LoginActivity1.this,"请输入您的密码!");
            showDialog("请输入您的密码!");
            return;
        }
        if (!IOUtils.isMobileNO(name)) {

//            ToastUtils.showMessage(LoginActivity1.this,"请输入正确的手机号!");
            showDialog("请输入正确的手机号!");
            return;
        }
       if(!TextUtils.isEmpty(tag)){
           homeid=SharePreferenceUtil.getInstance(LoginActivity1.this).getHomeId();
          if(name.equals(dao.findUserInfoById(homeid).getMobile())) {
              showDialog("此亲友关系已存在！");
          }else{
              login(name, psd);
          }
       }else {
           login(name, psd);
       }
    }

    public void login(final String mobile, final String psd) {
        startProgressDialog();
        requestMaker.userLogin(mobile, psd, ClientID, new JsonAsyncTask_Info(LoginActivity1.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                if (result != null) {
                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserLogin");
                        stopProgressDialog();
                        if (array.getJSONObject(0).has("MessageCode")) {
//                            Toast.makeText(LoginActivity1.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                    Toast.LENGTH_SHORT).show();
                            ToastUtils.showMessage(LoginActivity1.this,array.getJSONObject(0).getString("MessageContent").toString());

                        } else {
                            UserDate date = JsonTools.getData(result.toString(), UserDate.class);
                            List<User> list = date.getData();
                            String imgHead = list.get(0).getImgHead();
                            if (imgHead != null) {
                                if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                                    imgHead = "";
                                } else {
                                    imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");
                                }
                            } else {
                                imgHead = "";
                            }

                            list.get(0).setImgHead(imgHead);
                            list.get(0).setPassword(psd);
                            list.get(0).setMobile(mobile);

//
//                            UserRefresh(list.get(0).getUserid(), list.get(0).getUsername(), imgHead);
                            if (!dao.findUserIsExist(list.get(0).getUserid())) {
                                dao.addUserInfo(list.get(0));
                            } else {
                                User dbUser = dao.findUserInfoById(list.get(0).getUserid());
                                dbUser.setImgHead(imgHead);
                                dbUser.setPassword(psd);
                                dbUser.setMobile(mobile);
                                dbUser.setPatientId(list.get(0).getPatientId());
                                if (list.get(0).getSex() != null) {
                                    dbUser.setSex(list.get(0).getSex());
                                }
                                if (list.get(0).getUserno() != null) {
                                    dbUser.setUserno(list.get(0).getUserno());
                                }
                                if (list.get(0).getAge() != null) {
                                    dbUser.setAge(list.get(0).getAge());
                                }
                                if (list.get(0).getUsername() != null) {
                                    dbUser.setUsername(list.get(0).getUsername());
                                }
                                if (list.get(0).getUserHeight() != null) {
                                    dbUser.setUserHeight(list.get(0).getUserHeight());
                                }
                                if(list.get(0).getUserno()!=null){
                                    dbUser.setUserno(list.get(0).getUserno());
                                }

                                dao.updateUserInfo(dbUser, list.get(0).getUserid());
                            }

                            CustomApplcation.getInstance().finishSingleActivityByClass(AddSuccussAct.class);
                            if (!TextUtils.isEmpty(tag)) {
                                if (mIntent.getStringExtra("usrid") != null) {
                                    CustomApplcation.getInstance().finishSingleActivityByClass(MyHome.class);
                                    upload(SharePreferenceUtil.getInstance(LoginActivity1.this).getPARENTID(), array.getJSONObject(0).getString("UserID"));
                                    SharePreferenceUtil.getInstance(LoginActivity1.this).setUserId(array.getJSONObject(0).getString("UserID"));
                                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                                    Intent intenthealth = new Intent("refresh");
                                    sendBroadcast(intenthealth);
                                    startActivity(new Intent(LoginActivity1.this,MainActivity.class));
                                } else {
                                    SharePreferenceUtil.getInstance(LoginActivity1.this).setPARENTID(array.getJSONObject(0).getString("UserID"));
                                    Intent i = new Intent(LoginActivity1.this, RelationActivity.class);
                                    i.putExtra("relation", "rela");
                                    startActivity(i);
                                }

                            } else {
                                getToken(list.get(0).getUserid(), list.get(0).getUsername(), imgHead);
                                SharePreferenceUtil.getInstance(LoginActivity1.this).setUserId(array.getJSONObject(0).getString("UserID"));
                                if(array.getJSONObject(0).has("UserNO")){
                                    getJIbu(array.getJSONObject(0).getString("UserNO"),array.getJSONObject(0).getString("UserID"));
                                }
                                Intent intenthealth = new Intent("refresh");
                                 sendBroadcast(intenthealth);
                                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                                SharePreferenceUtil.getInstance(LoginActivity1.this).setHomeId(array.getJSONObject(0).getString("UserID"));
                            }
                            SharePreferenceUtil.getInstance(LoginActivity1.this).setIsFirst(true);
                            if(!TextUtils.isEmpty(hometag)) {
                                Intent i=new Intent(LoginActivity1.this, MainActivity.class);
                                i.putExtra("Home","Home");
                                startActivity(i);
                            }else {
                                finish();
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }));
    }


    /**
     * 执行忘记密码操作
     */
    private void doForgetPsd() {
        Intent intent = new Intent(LoginActivity1.this, FindPsdActivity.class);
        startActivity(intent);
    }



    private void getJIbu(final String cardNo,final String userid) {
        /**
         * 查询历史数据
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        requestMaker.StepCounterInquiry(cardNo,userid, sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000), new JsonAsyncTask_Info(LoginActivity1.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        StepDetector.CURRENT_SETP = 0;
                        StepBean step = new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(0);
                        step.setUserid("");
                        step.setUploadState(0);
                        dao.updateStep(step);

                    } else {

                        StepCounterDate date = JsonTools.getData(result.toString(), StepCounterDate.class);
                        List<StepCounterBean> list = date.getData();

                        StepDetector.CURRENT_SETP = Integer.valueOf(list.get(0).getTotalStep());
                        StepBean step = new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(StepDetector.CURRENT_SETP);
                        step.setUserid(userid);
                        step.setUploadState(0);
                        dao.updateStep(step);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }));
    }

    private void upload(final String oldid, final String newid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01) / 1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        String userid = SharePreferenceUtil.getInstance(LoginActivity1.this).getUserId();
        String card=dao.findUserInfoById(userid).getUserno();
        requestMaker.StepCounterInsert(card,userid, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f", calories), sdf.format(new Date()), new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInsert");
                    UserClientBind(oldid, newid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void UserClientBind(final String oldid, final String newid) {
        startProgressDialog();
        final String userid = SharePreferenceUtil.getInstance(LoginActivity1.this).getUserId();
        requestMaker.loginOut(userid, new JsonAsyncTask_Info(LoginActivity1.this, true, new JsonAsyncTaskOnComplete() {
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
                        StepBean step = new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(0);
                        step.setUserid("");
                        step.setUploadState(0);
                        dao.updateStep(step);
                        getJIbu("",newid);
                        finishActivity();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mIntent.getStringExtra("logout") != null && mIntent.getStringExtra("logout").equals("logout")) {
                confirmExit();
            } else {
                finishActivity();
          }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获得token
     */
    private void getToken(String userid, final String name, final String head) {

        requestMaker.getToken(userid, name, head, new JsonAsyncTask_Info(LoginActivity1.this, true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("GetToken");
                    if (array.getJSONObject(0).getString("MessageCode").toString().equals("0")) {
                        token = array.getJSONObject(0).getString("MessageContent").toString();
                        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                        edit.putString("DEMO_TOKEN", token);
                        edit.apply();
                        CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                            @Override
                            public void OnSuccess(String userid) {
//                                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                                RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, name, Uri.parse(head)));
                                RongIM.getInstance().setMessageAttachedUserInfo(true);
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity1.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));


    }

    private void UserRefresh(String userid, String name, String head) {
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid, name, Uri.parse(head)));
        requestMaker.UserRefresh(userid, name, head, new JsonAsyncTask_Info(LoginActivity1.this, true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("UserRefresh");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));

    }

    public void confirmExit() {

//        DialogTips dialog = new DialogTips(LoginActivity1.this, "", "是否退出软件？",
//                "确定", true, true);
//        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int userId) {
//                SharePreferenceUtil.getInstance(LoginActivity1.this).setUserId("");
//                SharePreferenceUtil.getInstance(LoginActivity1.this).setIsRemeber(false);
//                CustomApplcation.getInstance().exit();
//                finish();
//            }
//        });
//
//        dialog.show();
//        dialog = null;
        Intent i=new Intent(LoginActivity1.this, MainActivity.class);
        i.putExtra("Home","Home");
        startActivity(i);

    }

    private void getUser(String userid) {
        requestMaker.UserInquiry(userid, new JsonAsyncTask_Info(LoginActivity1.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                UserInfoDate date = JsonTools.getData(result.toString(), UserInfoDate.class);
                Log.e("psd", result.toString() + "<<<<----------->");
                List<User> list = date.getData();
                User user2 = list.get(0);
                mEditPhone.setText(user2.getMobile());
                mEditPass.setText(user2.getUserPassword());
                stopProgressDialog();


            }

        }));
    }
    private void showDialog(String message) {

        DialogTips dialog = new DialogTips(LoginActivity1.this, message, "确定");

        dialog.show();
        dialog = null;

    }


}
