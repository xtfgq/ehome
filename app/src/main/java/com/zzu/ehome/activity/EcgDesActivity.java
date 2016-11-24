package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;


/**
 * Created by Administrator on 2016/9/9.
 */
public class EcgDesActivity extends BaseSimpleActivity implements View.OnClickListener{
    private ImageView ivhome;
    private ImageView        ivback;
    private Button btn_ecg;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ecg_layout);
        intiView();
        intiEvnents();
    }
    private void intiView(){
        ivhome=(ImageView)findViewById(R.id.ivhome);
        ViewGroup.LayoutParams para;
        para = ivhome.getLayoutParams();

        para.width = ScreenUtils.getScreenWidth(EcgDesActivity.this);
        para.height = para.width*1600/750-ScreenUtils.dip2px(EcgDesActivity.this,48);
        ivhome.setLayoutParams(para);
        btn_ecg=(Button)findViewById(R.id.btn_des);

        ivback=(ImageView)findViewById(R.id.ivback);
    }
    private void intiEvnents(){
        btn_ecg.setOnClickListener(this);

        ivback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_des:
                startActivity(new Intent(EcgDesActivity.this,ECGActivity1.class));
                break;
            case R.id.ivback:
                finish();
                break;

        }
    }
}
