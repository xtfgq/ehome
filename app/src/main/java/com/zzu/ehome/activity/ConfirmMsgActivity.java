package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.DoctorSchemaByTopmdBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_ECGInfo;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mersens on 2016/8/10.
 */
public class ConfirmMsgActivity extends BaseActivity {
    private Button btn_yuyue;
    private Intent mIntent;
    private DoctorSchemaByTopmdBean doctortime;
    private RequestMaker requestMaker;
    private String userid;
    private TextView tv_name,tv_departname,tv_time,tv_contactor,tvcardNum;
    private String HospitalID,DepartmentID,DoctorID,DepartmentName,UserContactorID,UserName,UserNo;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker=RequestMaker.getInstance();
        setContentView(R.layout.layout_confirm_msg);

        mIntent=this.getIntent();
        DepartmentID=mIntent.getStringExtra("DepartmentID");
        HospitalID=mIntent.getStringExtra("HospitalID");
        DoctorID=mIntent.getStringExtra("DoctorID");
        DepartmentName=mIntent.getStringExtra("DepartName");
        UserContactorID=mIntent.getStringExtra("UserContactorID");
        UserName=mIntent.getStringExtra("UserName");
        UserNo=mIntent.getStringExtra("UserNO");
        doctortime=(DoctorSchemaByTopmdBean)mIntent.getSerializableExtra("DoctorTime");
        userid= SharePreferenceUtil.getInstance(ConfirmMsgActivity.this).getUserId();
        initViews();
        initEvent();
       if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        initDatas();
        if(!CommonUtils.isNotificationEnabled(ConfirmMsgActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "确认挂号信息", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        btn_yuyue=(Button) findViewById(R.id.btn_yuyue);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_departname=(TextView)findViewById(R.id.tv_departname);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_contactor=(TextView)findViewById(R.id.tv_contactor);
        tvcardNum=(TextView)findViewById(R.id.tvcardNum);
    }


    public void initEvent(){
        tv_departname.setText(DepartmentName);
        tv_time.setText(doctortime.getScheduleTimeContent());
        tv_name.setText(doctortime.getRealName());
        tv_contactor.setText(UserName);
        tvcardNum.setText(UserNo);
        btn_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                startProgressDialog();
                btn_yuyue.setEnabled(false);
                requestMaker.GuahaoOrderInsert(userid,HospitalID,DepartmentID,DepartmentName,doctortime.getRealName(),doctortime.getSchemaID(),DoctorID,UserContactorID,doctortime.getSchemaWeek(),
                        doctortime.getNoonName(),doctortime.getBeginTime(),doctortime.getEndTime(),doctortime.getRegistType(),doctortime.getRMB(),doctortime.getSchemaDate(),
                        doctortime.getRegistId(),new JsonAsyncTask_ECGInfo(ConfirmMsgActivity.this, true, new JsonAsyncTaskOnComplete() {
                            @Override
                            public void processJsonObject(Object result) {
                                try {
                                    btn_yuyue.setEnabled(true);
                                    String value = result.toString();
                                    stopProgressDialog();
                                    JSONObject mySO = (JSONObject) result;
                                    JSONArray array = mySO.getJSONArray("Result");
                                    int code=Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
//                                    ToastUtils.showMessage(ConfirmMsgActivity.this,array.getJSONObject(0).getJSONObject("MessageContent").toString());
//                                    if(code=0){
//                                       startActivity(new Intent(ConfirmMsgActivity.this,YuYueSuccessActivity.class));
//                                    }else{
//                                        ToastUtils.showMessage(ConfirmMsgActivity.this,array.getJSONObject(0).getJSONObject("MessageContent").toString());
//                                    }
                                    if(code>=1){
                                        startActivity(new Intent(ConfirmMsgActivity.this,YuYueSuccessActivity.class));
                                    }else if(code==-1){
                                        showDialog("对不起，一天只能挂医院不同科室两个号");
                                    }else if(code==-2){
                                        showDialog("对不起，不能重复预约同一医生同一号");
                                    }else if(code==-3){
                                        showDialog("对不起，爽约超过3次，不能挂号");
                                    }else if(code==-4||code==-7){
                                            showDialog(array.getJSONObject(0).getString("MessageContent"));
                                    } else if(code==0){
                                        showDialog("对不起，挂号失败请稍后再试");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    btn_yuyue.setEnabled(true);
                                    finish();
                                }
                            }
                        }));


            }
        });

    }


    public void initDatas(){}
    private void showDialog(String message) {

        DialogTips dialog = new DialogTips(ConfirmMsgActivity.this, message, "确定");

        dialog.show();
        dialog = null;

    }

}
