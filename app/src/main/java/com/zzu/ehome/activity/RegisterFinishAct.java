package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2016/8/9.
 */
public class RegisterFinishAct extends BaseSimpleActivity implements View.OnClickListener {
    private RequestMaker requestMaker;

    private String parentId;
    private Button btnext;
    private TextView tvno;
    private String tag="";


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        setContentView(R.layout.activity_relation);
        setContentView(R.layout.layout_finish_reg);
        requestMaker = RequestMaker.getInstance();
        if (this.getIntent() != null) {
            if (this.getIntent().getStringExtra("relation") != null) {
                tag = this.getIntent().getStringExtra("relation");
            }
        }
        initViews();
        parentId = SharePreferenceUtil.getInstance(RegisterFinishAct.this).getPARENTID();
        initEvent();
        if(!CommonUtils.isNotificationEnabled(RegisterFinishAct.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if(!CommonUtils.isNotificationEnabled(RegisterFinishAct.this)){
            showTitleDialog("请打开通知中心");
        }
        switch (v.getId()) {
            case R.id.btnext:
                btnext.setEnabled(false);
                Intent i=new Intent(RegisterFinishAct.this,HealthFilesActivity.class);
//                i.putExtra("UserId",SharePreferenceUtil.getInstance(RegisterFinishAct.this).getPARENTID());
                i.putExtra("UserId",SharePreferenceUtil.getInstance(RegisterFinishAct.this).getUserId());
                i.putExtra("TagFile","TagFile");
                i.putExtra("type","0");
                startActivity(i);
                break;
            case R.id.tvno:
                tvno.setEnabled(false);
                CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(SexActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(WeightAndHeightAct.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(SecondActivity.class);
                if(TextUtils.isEmpty(tag)) {
                    startActivity(new Intent(RegisterFinishAct.this, MainActivity.class));
                }else{
                    startActivity(new Intent(RegisterFinishAct.this, MyHome.class));
                }
                finishActivity();
                break;
        }

    }

    private void initEvent() {
        btnext.setOnClickListener(this);
        tvno.setOnClickListener(this);
    }

    private void initViews() {
        btnext = (Button) findViewById(R.id.btnext);
        tvno=(TextView)findViewById(R.id.tvno);

    }
}
