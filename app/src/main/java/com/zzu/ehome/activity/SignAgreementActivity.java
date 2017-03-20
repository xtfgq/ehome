package com.zzu.ehome.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Mersens on 2017/2/15 14:01
 * Email:626168564@qq.com
 */

public class SignAgreementActivity extends BaseActivity {
    private TextView tv_home_doctor_value;
    private TextView tv_hosptial_value;
    private TextView tv_home_fzr_value;
    private TextView tv_cardID_value;
    private TextView tv_tel;
    private Button btn_sign;
    private String doctorname, hosname, doctorid;
    private String startTime;
    private String endTime;
    private TextView tv_sertime;


    private RequestMaker requestMaker;
    private EHomeDao dao;
    private String userid;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_sign_agreement);
        doctorname = this.getIntent().getStringExtra("doctorname");
        hosname = this.getIntent().getStringExtra("hosname");
        doctorid = this.getIntent().getStringExtra("doctorid");
        startTime = this.getIntent().getStringExtra("startTime");
        endTime = this.getIntent().getStringExtra("endTime");
        requestMaker = RequestMaker.getInstance();
        init();

    }

    private void init() {
        initViews();
        initEvent();
        initDatas();
    }

    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "家庭医生签约服务协议", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tv_home_doctor_value = (TextView) findViewById(R.id.tv_home_doctor_value);
        tv_home_doctor_value.setText(doctorname);
        tv_hosptial_value = (TextView) findViewById(R.id.tv_hosptial_value);
        tv_hosptial_value.setText(hosname);
        tv_home_fzr_value = (TextView) findViewById(R.id.tv_home_fzr_value);
        tv_cardID_value = (TextView) findViewById(R.id.tv_cardID_value);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        tv_sertime=(TextView)findViewById(R.id.tv_sertime);
        if(!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)){
            startTime= DateUtils.StringPattern(startTime,"yyyy/MM/dd HH:mm:ss","yyyy.MM.dd");
            endTime=DateUtils.StringPattern(endTime,"yyyy/MM/dd HH:mm:ss","yyyy.MM.dd");
            tv_sertime.setText(startTime+"-"+endTime);
        }
    }

    private void initEvent() {
        tv_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打服务电话
                showTips();

            }
        });
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //签约
                if (checkCardNo(userid)) {
                    showTipsAgree();
                    return;
                }

            }
        });
        //创建compositeSubscription实例
        compositeSubscription = new CompositeSubscription();

        //监听订阅事件
        Subscription subscription = RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }
                        if (event instanceof EventType) {
                            EventType type = (EventType) event;
                            if ("refresh".equals(type.getType())) {
                                userid = SharePreferenceUtil.getInstance(SignAgreementActivity.this).getUserId();
                                if (!TextUtils.isEmpty(userid)) {
                                    User user = dao.findUserInfoById(userid);
                                    if (user != null) {
                                        tv_home_fzr_value.setText(user.getUsername());
                                        tv_cardID_value.setText(user.getUserno());
                                    }
                                }
                            }

                        }


                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
    }

    private void initDatas() {
        dao = new EHomeDaoImpl(this);
        userid = SharePreferenceUtil.getInstance(this).getUserId();
        if (!TextUtils.isEmpty(userid)) {
            User user = dao.findUserInfoById(userid);
            if (user != null) {
                tv_home_fzr_value.setText(user.getUsername());
                tv_cardID_value.setText(user.getUserno());
            }
        }
    }

    /**
     * 如果用户信息不完善，显示提示框
     */
    public void completeInfoTips() {
        DialogTips dialog = new DialogTips(SignAgreementActivity.this, "", "用户信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(SignAgreementActivity.this, PersonalCenterInfo.class));
            }
        });

        dialog.show();
        dialog = null;
    }

    private Boolean checkCardNo(String userid) {
        User dbUser = dao.findUserInfoById(userid);
        if (TextUtils.isEmpty(dbUser.getUserno())) {
            completeInfoTips();
            return false;
        } else {
            return true;
        }
    }


    private void showTips() {
        DialogTips dialog = new DialogTips(SignAgreementActivity.this, "0371-86505391", "拨打",
                "取消", "", false, false);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                callPhone();
            }
        });
        dialog.show();
        dialog = null;
    }

    private void callPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (SignAgreementActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intentTel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "037186505391"));
                startActivity(intentTel);
            }
        } else {
            Intent intentTel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "037186505391"));
            startActivity(intentTel);
        }


    }

    private void showTipsAgree() {

        DialogTips dialog = new DialogTips(SignAgreementActivity.this, "确定要签约当前医生" + "\n" + "作为您的家庭医生吗?", "确定签约",
                "我再想想", "", false, false);

        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                doSign();
            }
        });
        dialog.show();
        dialog = null;
    }

    private void doSign() {
        User dbUser = dao.findUserInfoById(userid);

        requestMaker.MSDoctorSignInsert(doctorid, userid, dbUser.getUserno(), doctorname, hosname, dbUser.getUsername(), dbUser.getMobile(), new JsonAsyncTask_Info(SignAgreementActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("MSDoctorSignInsert");

                    if (array.getJSONObject(0).getString("MessageCode").equals("0")) {
                        Toast.makeText(SignAgreementActivity.this, "签约成功",
                                Toast.LENGTH_SHORT).show();
                        RxBus.getInstance().send(new EventType("su"));
                        finish();
                    } else {
                        Toast.makeText(SignAgreementActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
