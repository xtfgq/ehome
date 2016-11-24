package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.zzu.ehome.R;

import com.zzu.ehome.fragment.CapaingFragmet;
import com.zzu.ehome.view.HeadView;


/**
 * Created by Administrator on 2016/9/24.
 */
public class CampaignActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_log);
        initView();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, CapaingFragmet.getInstance()).commit();

    }
    private void initView(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "系统消息", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {


                finishActivity();

            }
        });
    }
}
