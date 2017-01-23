package com.zzu.ehome.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.utils.CommonUtils;

import static android.R.attr.type;

/**
 * Created by Mersens on 2016/8/10.
 */
public class YuYueSuccessActivity extends BaseSimpleActivity implements View.OnClickListener {
    private Button btnext;
    private TextView tvcon;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_yuyue_success);
        initViews();
        initEvent();
        initDatas();
        if (!CommonUtils.isNotificationEnabled(YuYueSuccessActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        btnext=(Button)findViewById(R.id.btnext);
        tvcon=(TextView)findViewById(R.id.tv_continue);
    }

    public void initEvent() {
        btnext.setOnClickListener(this);
        tvcon.setOnClickListener(this);
    }

    public void initDatas() {

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btnext:
               CustomApplcation.getInstance().finishSingleActivityByClass(ConfirmMsgActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(SelectPatientActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(DoctorTimeActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(OfficeListActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(DoctorListActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(OrdinaryYuYueActivity.class);

               //从网络预约跳过来






               finish();
               break;
           case R.id.tv_continue:
               CustomApplcation.getInstance().finishSingleActivityByClass(ConfirmMsgActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(SelectPatientActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(DoctorTimeActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(OfficeListActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(DoctorListActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(OrdinaryYuYueActivity.class);

               startActivity(new Intent(YuYueSuccessActivity.this,OrdinaryYuYueActivity.class));
               finish();
               break;
       }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CustomApplcation.getInstance().finishSingleActivityByClass(ConfirmMsgActivity.class);
            CustomApplcation.getInstance().finishSingleActivityByClass(SelectPatientActivity.class);
            CustomApplcation.getInstance().finishSingleActivityByClass(DoctorTimeActivity.class);
            CustomApplcation.getInstance().finishSingleActivityByClass(OfficeListActivity.class);
            CustomApplcation.getInstance().finishSingleActivityByClass(DoctorListActivity.class);
            CustomApplcation.getInstance().finishSingleActivityByClass(OrdinaryYuYueActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }
}
