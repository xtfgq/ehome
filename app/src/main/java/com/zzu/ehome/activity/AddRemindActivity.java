package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.RepatBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.TimePopWindows;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Mersens on 2016/8/18.
 */
public class AddRemindActivity extends BaseActivity implements TimePopWindows.OnGetData, View.OnClickListener {
    private RelativeLayout layout_time, layout_repeat, layout_type;
    private TimePopWindows tp;
    private LinearLayout llremind;
    private TextView tvtime;
    public static final int ADD_WEEK = 0x17;
    public static final int ADD_TYPE = 0x18;
    private List<RepatBean> listweek = null;
    private TextView tvweek;
    private TextView tvwenzhen;
    private Button btn_save;
    private RequestMaker requestMaker;
    private String userid, ClientID, Time, Type, Weekday;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_remind);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(AddRemindActivity.this).getUserId();
        ClientID = PushManager.getInstance().getClientid(AddRemindActivity.this);
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(AddRemindActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        layout_time = (RelativeLayout) findViewById(R.id.layout_time);
        layout_repeat = (RelativeLayout) findViewById(R.id.layout_repeat);
        layout_type = (RelativeLayout) findViewById(R.id.layout_type);
        llremind = (LinearLayout) findViewById(R.id.llremind);
        tvtime = (TextView) findViewById(R.id.tvtime);
        tvweek = (TextView) findViewById(R.id.tvweek);
        tvwenzhen = (TextView) findViewById(R.id.tvwenzhen);
        btn_save = (Button) findViewById(R.id.btn_save);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "添加提醒", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
               finishActivity();
            }
        });
    }


    public void initEvent() {
        layout_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }

                tp = new TimePopWindows(AddRemindActivity.this, llremind);
                tp.setmOnGetData(AddRemindActivity.this);

            }
        });
        layout_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                Intent intent = new Intent(AddRemindActivity.this, RepeatActivity.class);
                startActivityForResult(intent, ADD_WEEK);


            }
        });
        layout_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                Intent intent = new Intent(AddRemindActivity.this, RemindTypeActivity.class);
                startActivityForResult(intent, ADD_TYPE);

            }
        });
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        tvtime.setText(df.format(new Date()));
        btn_save.setOnClickListener(this);
    }


    public void initDatas() {
    }


    @Override
    public void onDataCallBack(String time) {
        tvtime.setText(time);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_WEEK && data != null) {
            listweek = (List<RepatBean>) data.getSerializableExtra("WeekList");
            Weekday = "";
            int i = 0;
            for (RepatBean bean : listweek) {
                if (bean.getSelct()) {
                    Weekday += bean.getNaem() + ",";
                    i++;
                }
            }
            if (!TextUtils.isEmpty(Weekday)) {
                Weekday = Weekday.substring(0, Weekday.length() - 1);

                if (i == 7) {
                    tvweek.setText("每天");
                } else {
                    tvweek.setText(Weekday);
                }
            } else {
                tvweek.setText("");
            }

        }
        if (requestCode == ADD_TYPE && data != null) {
            String rs2 = (String) data.getSerializableExtra("Type");
            tvwenzhen.setText(rs2);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                addRemind();
                break;
        }
    }

    private void addRemind() {
        Time = tvtime.getText().toString();
        if (TextUtils.isEmpty(tvweek.getText().toString())) {
            show("请选择提醒频次！");
            return;
        }
        if (TextUtils.isEmpty(tvwenzhen.getText().toString())) {
            show("请选择提醒类型！");
            return;
        }
        if (tvwenzhen.getText().toString().contains("用药")) {
            Type = "01";
        } else if (tvwenzhen.getText().toString().contains("测量")) {
            Type = "02";
        }
        btn_save.setEnabled(false);
        startProgressDialog();

        requestMaker.RemindInsert(userid, Time, Type, Weekday, new JsonAsyncTask_Info(AddRemindActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    btn_save.setEnabled(true);
                    JSONObject mySO = (JSONObject) result;
                    stopProgressDialog();
                    org.json.JSONArray array = mySO
                            .getJSONArray("RemindInsert");
                    if (array.getJSONObject(0).getString("MessageCode").toString().equals("0")) {
                        Toast.makeText(AddRemindActivity.this, "保存成功！",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("finish", "sucuss");
                        setResult(MyRemindActivity1.ADD_REMIND, intent);
                        finish();
                    } else {
                        Toast.makeText(AddRemindActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }));
    }
}
