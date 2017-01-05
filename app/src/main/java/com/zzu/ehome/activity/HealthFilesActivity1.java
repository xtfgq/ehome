package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.HealthFiles;
import com.zzu.ehome.fragment.HealthFilesFragment2;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesActivity1 extends BaseActivity implements HealthFilesFragment2.MyListener {
    private  String usrid,tag="";
    private String type=null;
    private HealthFiles healthFiles=null;
    public static final String HEALTHFILES="healthFiles";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_health_activity1);
        usrid=this.getIntent().getStringExtra("UserId");
        type=getIntent().getStringExtra("type");
        if(this.getIntent().getStringExtra("TagFile")!=null){
            tag=this.getIntent().getStringExtra("TagFile");
        }

        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(HealthFilesActivity1.this)){
            showTitleDialog("请打开通知中心");

        }
    }

    public void initViews() {
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "我的档案", R.mipmap.icon_editor, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                if(!TextUtils.isEmpty(tag)) {
                    CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
                    CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
                    CustomApplcation.getInstance().finishSingleActivityByClass(SexActivity.class);
                    CustomApplcation.getInstance().finishSingleActivityByClass(WeightAndHeightAct.class);
                    CustomApplcation.getInstance().finishSingleActivityByClass(SecondActivity.class);
                    CustomApplcation.getInstance().finishSingleActivityByClass(RegisterFinishAct.class);
                    startActivity(new Intent(HealthFilesActivity1.this, MainActivity.class));
                }else{
                    finish();
               }
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                Intent i=new Intent(HealthFilesActivity1.this,HealthFilesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UserId", SharePreferenceUtil.getInstance(HealthFilesActivity1.this).getUserId());
                bundle.putString("type",type);
                bundle.putSerializable(HEALTHFILES,healthFiles);
                i.putExtras(bundle);
                startActivity(i);
//                startActivity(new Intent(HealthFilesActivity1.this, HealthFilesActivity.class));
            }
        });
    }

    public void initEvent() {

    }

    public void initDatas() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, HealthFilesFragment2.getInstance(usrid)).commit();
    }

    @Override
    public void getType(String type) {
        this.type=type;
    }

    @Override
    public void getHealthFiles(HealthFiles hf) {
        this.healthFiles=hf;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!TextUtils.isEmpty(tag)) {
                CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(SexActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(WeightAndHeightAct.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(SecondActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(RegisterFinishAct.class);
                startActivity(new Intent(HealthFilesActivity1.this, MainActivity.class));
            }else{
               finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
