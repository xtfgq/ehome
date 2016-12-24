package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.HealthFiles;
import com.zzu.ehome.fragment.HealthFilesFragment1;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesActivity extends BaseActivity {
    private String usrid;
    private String type=null;
    private HealthFiles hf=null;
    private String tag="";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_health_activity);
        Bundle bundle=getIntent().getExtras();
        usrid=bundle.getString("UserId");
        type=bundle.getString("type");
        tag=bundle.getString("TagFile");
        hf=(HealthFiles) bundle.getSerializable(HealthFilesActivity1.HEALTHFILES);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的档案", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }

    public void initEvent() {
    }

    public void initDatas() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, HealthFilesFragment1.getInstance(usrid,type,hf,tag)).commit();
    }

}
