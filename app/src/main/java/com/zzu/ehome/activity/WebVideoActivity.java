package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2016/9/9.
 */
public class WebVideoActivity extends BaseSimpleActivity implements View.OnClickListener{
    private ImageView ivhome,ivback;
    private LinearLayout layout_mzwz,layout_yyjz,llback;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_web_layout);
        intiView();
        intiEvnents();
    }
    private void intiView(){
        ivhome=(ImageView)findViewById(R.id.ivhome);
        ViewGroup.LayoutParams para;
        para = ivhome.getLayoutParams();

        para.width = ScreenUtils.getScreenWidth(WebVideoActivity.this);
        para.height = para.width*1600/750-ScreenUtils.dip2px(WebVideoActivity.this,48);
        ivhome.setLayoutParams(para);
        layout_mzwz=(LinearLayout)findViewById(R.id.layout_mzwz);
        layout_yyjz=(LinearLayout)findViewById(R.id.layout_yyjz);
        ivback=(ImageView)findViewById(R.id.ivback);
        llback=(LinearLayout)findViewById(R.id.llback);
    }
    private void intiEvnents(){
        layout_mzwz.setOnClickListener(this);
        layout_yyjz.setOnClickListener(this);
        llback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_mzwz:
                Intent i=new Intent(WebVideoActivity.this,InternetHospitalActivity.class);
                i.putExtra("Tag","web");
                startActivity(i);
                break;
            case R.id.layout_yyjz:
                if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(WebVideoActivity.this).getUserId())){
                    startActivity(new Intent(WebVideoActivity.this,LoginActivity1.class));
                }else
                startActivity(new Intent(WebVideoActivity.this,MyDoctorActivity.class));
                break;
            case R.id.llback:
                finishActivity();
                break;
        }
    }
}
