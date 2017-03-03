package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.RegisterCodeTimer;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AddSuccussAct extends BaseSimpleActivity implements View.OnClickListener {

    private RequestMaker requestMaker;

    private String parentId;
    private TextView tvcontinue;
    private Button btnext;
    private TextView tvgoHome;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        setContentView(R.layout.activity_relation);
        setContentView(R.layout.layout_add_finish);
        requestMaker = RequestMaker.getInstance();

        initViews();
        parentId = SharePreferenceUtil.getInstance(AddSuccussAct.this).getPARENTID();
        initEvent();
        if(!CommonUtils.isNotificationEnabled(AddSuccussAct.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }

        switch (v.getId()) {
            case R.id.tv_continue:
                tvcontinue.setEnabled(false);
                CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
                Intent i = new Intent(this, LoginActivity1.class);
                i.putExtra("relation", "rela");
                startActivity(i);
                break;
            case R.id.btnext:
                btnext.setEnabled(false);
                CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
                startActivity(new Intent(AddSuccussAct.this,MyHome.class));
                break;
            case R.id.tv_goHome:
                tvgoHome.setEnabled(false);
                goHome();
                break;
        }

    }

    private void initEvent() {
        tvcontinue.setOnClickListener(this);
        btnext.setOnClickListener(this);
        tvgoHome.setOnClickListener(this);
    }

    private void initViews() {
        tvcontinue = (TextView) findViewById(R.id.tv_continue);
        btnext=(Button)findViewById(R.id.btnext);
        tvgoHome=(TextView)findViewById(R.id.tv_goHome);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            goHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void goHome(){
        CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
        CustomApplcation.getInstance().finishSingleActivityByClass(RegisterActivity.class);
        CustomApplcation.getInstance().finishSingleActivityByClass(SecondActivity.class);
        CustomApplcation.getInstance().finishSingleActivityByClass(SexActivity.class);
        CustomApplcation.getInstance().finishSingleActivityByClass(WeightAndHeightAct.class);
        CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
        Intent i=new Intent(AddSuccussAct.this, MainActivity.class);
        i.putExtra("Home","Home");
        startActivity(i);

    }
}
