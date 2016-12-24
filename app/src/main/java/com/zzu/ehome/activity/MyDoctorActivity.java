package com.zzu.ehome.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import de.greenrobot.event.EventBus;

import static com.zzu.ehome.activity.AppointmentActivity.office;

/**
 * Created by Administrator on 2016/5/30.
 */
public class MyDoctorActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layout_add_hosptial;
    private RelativeLayout layout_add_office;
    private RelativeLayout layout_add_doctor;
    private RelativeLayout layout_add_time;
    public static final int ADD_HOSPITAL = 0x00;
    public static final int ADD_OFFICE = 0x01;
    public static final int ADD_DOCTOR = 0x10;
    public static final int ADD_TIME = 0x11;
    private TextView tv_hosptial;
    private TextView tv_office;
    private TextView tv_doctor;
    private TextView tv_time;
    private Button btn_ok;
    private String hospital_id;
    private String department_id;
    private String doctor_id;
    private String DateStr;
    private String TimeSpanStr;
    private String PerTime;
    private String UserNo;
    //请求单例
    private RequestMaker requestMaker;
    public static final String ACTION_NAME = "ACTION_DOCTOR";
    private EHomeDao dao;
    String userid, PatientId;
    private boolean isPrepared;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor);
        EventBus.getDefault().register(this);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(MyDoctorActivity.this).getUserId();
        dao = new EHomeDaoImpl(MyDoctorActivity.this);
        PatientId = dao.findUserInfoById(userid).getPatientId();
        initViews();
        initEvent();

    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "预约视频就诊", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        layout_add_hosptial = (RelativeLayout) findViewById(R.id.layout_add_hosptial);
        layout_add_office = (RelativeLayout) findViewById(R.id.layout_add_office);
        layout_add_doctor = (RelativeLayout) findViewById(R.id.layout_add_doctor);
        layout_add_time = (RelativeLayout) findViewById(R.id.layout_add_time);
        tv_hosptial = (TextView) findViewById(R.id.tv_hosptial);
        tv_office = (TextView) findViewById(R.id.tv_office);
        tv_doctor = (TextView) findViewById(R.id.tv_doctor);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_ok = (Button) findViewById(R.id.btn_ok);

    }


    public void initEvent() {
        layout_add_hosptial.setOnClickListener(this);
        layout_add_office.setOnClickListener(this);
        layout_add_doctor.setOnClickListener(this);
        layout_add_time.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        registerBoradcastReceiver();

    }

    public static Fragment getInstance() {
        return new DoctorFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add_hosptial:
                Intent intent1 = new Intent(MyDoctorActivity.this, SelectHospitalActivity.class);
                startActivityForResult(intent1, ADD_HOSPITAL);
                break;
            case R.id.layout_add_office:
                if (TextUtils.isEmpty(hospital_id)) {
                    showDialog("请填写就诊医院");
                    return;
                }
                Intent intent2 = new Intent(MyDoctorActivity.this, SelectOfficeActivity1.class);
                intent2.putExtra("hospital_id", hospital_id);
                startActivityForResult(intent2, ADD_OFFICE);
                break;
            case R.id.layout_add_doctor:
                if (TextUtils.isEmpty(department_id)) {
                    showDialog("请选择科室");
                    return;
                }

                Intent intent = new Intent(MyDoctorActivity.this, AppointmentActivity.class);
                intent.putExtra("department_id", department_id);
                intent.putExtra("hosptial", tv_hosptial.getText().toString());
                intent.putExtra("office", tv_office.getText().toString());
                startActivityForResult(intent, ADD_DOCTOR);
                break;
            case R.id.layout_add_time:
                if (TextUtils.isEmpty(doctor_id)) {
                    showDialog("请选择医生");

                    return;
                }
                Intent intenttime = new Intent(MyDoctorActivity.this, SelectDateActivity_1.class);
                intenttime.putExtra("department_id", department_id);
                intenttime.putExtra("doctor_id", doctor_id);
                startActivityForResult(intenttime, ADD_TIME);
                break;
            case R.id.btn_ok:
                confirmSave();

                break;
        }
    }

    public void confirmSave() {
        String hosptial = tv_hosptial.getText().toString();
        String office = tv_office.getText().toString();
        String doctor = tv_doctor.getText().toString();
        String time = tv_time.getText().toString();
        UserNo = dao.findUserInfoById(userid).getUserno();
        if (hosptial.equals("请选择医院")) {
            showDialog("请选择医院");
            return;
        }
        if (office.equals("请选择科室")) {
            showDialog( "请选择科室");
            return;

        }
        if (doctor.equals("请选择医生")) {
            showDialog("请选择医生");
            return;

        }
        if (time.equals("请选择就诊时间")) {
            showDialog("请选择就诊时间");
            return;
        }
        if (TextUtils.isEmpty(UserNo)) {
            completeInfoTips();
            return;
        }
        Intent i = new Intent(MyDoctorActivity.this, WebConfigAct.class);
        i.putExtra("Hos", tv_hosptial.getText().toString());
        i.putExtra("Dep", tv_office.getText().toString());
        i.putExtra("Time", tv_time.getText().toString());
        i.putExtra("doctor_id", doctor_id);
        i.putExtra("PatientId",PatientId);
        i.putExtra("DateStr",DateStr);
        i.putExtra("TimeSpanStr",TimeSpanStr);
        i.putExtra("PerTime",PerTime);
        startActivity(i);
        tv_hosptial.setText("请选择医院");
        tv_office.setText("请选择科室");
        tv_doctor.setText("请选择医生");
        tv_time.setText("请选择就诊时间");
        hospital_id = "";
        department_id = "";
        doctor_id = "";

//        DialogTips dialog = new DialogTips(MyDoctorActivity.this, "", "您确定预约" + tv_hosptial.getText().toString() + tv_office.getText().toString() + tv_doctor.getText().toString() + "医生" + tv_time.getText().toString() + "的号码吗？",
//                "确定", true, true);
//        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int userId) {
//                startProgressDialog();
//                requestMaker.TreatmentInsert(doctor_id, PatientId, DateStr, TimeSpanStr, PerTime, new JsonAsyncTask_Info(MyDoctorActivity.this, true, new JsonAsyncTaskOnComplete() {
//                    @Override
//                    public void processJsonObject(Object result) {
//                        JSONObject mySO = (JSONObject) result;
//                        try {
//                            JSONArray array = mySO.getJSONArray("TreatmentInsert");
//                            JSONObject jsonObject = (JSONObject) array.get(0);
//                            String code = jsonObject.getString("MessageCode");
//                            stopProgressDialog();
//                            if ("0".equals(code)) {
//                               ToastUtils.showMessage(MyDoctorActivity.this, jsonObject.getString("MessageContent"));
//                                tv_hosptial.setText("");
//                                tv_office.setText("");
//                                tv_doctor.setText("");
//                                tv_time.setText("");
//                                hospital_id = "";
//                                department_id = "";
//                                doctor_id = "";
//                                finish();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } finally {
//                            stopProgressDialog();
//                        }
//
//                    }
//                }));
//            }
//        });
//
//        dialog.show();
//        dialog = null;
    }


    public void save() {


    }

    private void showDialog(String title) {

        DialogTips dialog = new DialogTips(MyDoctorActivity.this, title, "确定");

        dialog.show();
        dialog = null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_HOSPITAL && resultCode == ADD_HOSPITAL && data != null) {
            String hosptial = data.getStringExtra("hospital");
            hospital_id = data.getStringExtra("hospital_id");
            if (!TextUtils.isEmpty(hosptial)) {
                tv_hosptial.setText(hosptial);

                tv_office.setText("请选择科室");
                tv_doctor.setText("请选择医生");
                tv_time.setText("请选择就诊时间");
            }
        }
        if (requestCode == ADD_OFFICE && resultCode == ADD_OFFICE && data != null) {
            String office = data.getStringExtra("department");
            department_id = data.getStringExtra("department_id");
            if (!TextUtils.isEmpty(office)) {
                tv_office.setVisibility(View.VISIBLE);
                tv_office.setText(office);
                doctor_id="";
                tv_doctor.setText("请选择医生");
                tv_time.setText("请选择就诊时间");

            }
        }

        if (requestCode == ADD_DOCTOR && resultCode == ADD_DOCTOR && data != null) {
            String doctor = data.getStringExtra("doctor");
            doctor_id = data.getStringExtra("doctor_id");
            if (!TextUtils.isEmpty(doctor)) {
                tv_doctor.setVisibility(View.VISIBLE);
                tv_doctor.setText(doctor);
                tv_time.setText("请选择就诊时间");
            }
        }
        if (requestCode == ADD_TIME && resultCode == ADD_TIME && data != null) {
            DateStr = data.getStringExtra("DateStr");
            TimeSpanStr = data.getStringExtra("TimeSpanStr");
            PerTime = data.getStringExtra("PerTime");
            if (!TextUtils.isEmpty(DateStr)) {
                tv_time.setVisibility(View.VISIBLE);
                tv_time.setText(DateStr + " " + TimeSpanStr);
            }
        }
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                String doctor = intent.getStringExtra("doctor");
                doctor_id = intent.getStringExtra("doctor_id");
                department_id = intent.getStringExtra("department_id");
                String office = intent.getStringExtra("Department_FullName");

                if (!TextUtils.isEmpty(doctor)) {
                    if (!TextUtils.isEmpty(department_id) && !TextUtils.isEmpty(office)) {
                        hospital_id = intent.getStringExtra("hospitalid");
                        tv_hosptial.setText(AppointmentActivity.hosptial);
                        tv_office.setText(office);
                    }
                    tv_office.setVisibility(View.VISIBLE);
                    tv_doctor.setVisibility(View.VISIBLE);
                    tv_doctor.setText(doctor);
                    tv_time.setText("请选择就诊时间");

                }

            }
        }
    };

    public void onEventMainThread(RefreshEvent event) {

        if (getResources().getInteger(R.integer.back_detail) == event
                .getRefreshWhere()) {

            tv_hosptial.setText("");
            tv_office.setText("");
            tv_doctor.setText("");
            tv_time.setText("");
            hospital_id = "";
            department_id = "";
            doctor_id = "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        EventBus.getDefault().unregister(this);
    }
    /**
     * 如果用户信息不完善，显示提示框
     */
    public void completeInfoTips() {
        DialogTips dialog = new DialogTips(MyDoctorActivity.this, "", "用户信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(MyDoctorActivity.this, PersonalCenterInfo.class));
            }
        });

        dialog.show();
        dialog = null;
    }


}
