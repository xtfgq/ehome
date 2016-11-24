package com.zzu.ehome.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;

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
               finishActivity();
               break;
           case R.id.tv_continue:
               CustomApplcation.getInstance().finishSingleActivityByClass(ConfirmMsgActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(SelectPatientActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(DoctorTimeActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(OfficeListActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(DoctorListActivity.class);
               CustomApplcation.getInstance().finishSingleActivityByClass(OrdinaryYuYueActivity.class);
               finishActivity();
               startActivity(new Intent(YuYueSuccessActivity.this,OrdinaryYuYueActivity.class));
               break;
       }
    }
}
