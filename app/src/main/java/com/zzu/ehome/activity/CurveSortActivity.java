package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.LymphChartFragment;
import com.zzu.ehome.fragment.GYSZFragment;
import com.zzu.ehome.fragment.HighDensityProteinFragment;
import com.zzu.ehome.fragment.LowDensityProteinFragment;
import com.zzu.ehome.fragment.MaiBoChartFragment;
import com.zzu.ehome.fragment.NEUChartFragment;
import com.zzu.ehome.fragment.RBCChartFragment;
import com.zzu.ehome.fragment.SuggarChartFragment;
import com.zzu.ehome.fragment.TCFragment;
import com.zzu.ehome.fragment.TizhongChartFragment;
import com.zzu.ehome.fragment.WBCChartFragment;
import com.zzu.ehome.fragment.UricAcidFragment;
import com.zzu.ehome.fragment.XueyaChartFragment;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/9/24.
 */
public class CurveSortActivity extends BaseActivity {
    private String title;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_log);
        mIntent = this.getIntent();
        title = mIntent.getStringExtra("title");
        initView();
        initFragment(title);
        if (!CommonUtils.isNotificationEnabled(CurveSortActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    private void initView() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }

    private void initFragment(String title) {
        if (title.equals("体重")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, TizhongChartFragment.getInstance()).commit();
        } else if (title.equals("血压")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, XueyaChartFragment.getInstance()).commit();
        } else if (title.equals("心率")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, MaiBoChartFragment.getInstance()).commit();
        } else if (title.equals("红细胞")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, RBCChartFragment.getInstance(title)).commit();
        } else if (title.equals("白细胞")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, WBCChartFragment.getInstance(title)).commit();
        } else if (title.equals("中性粒细胞")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, NEUChartFragment.getInstance(title)).commit();
        } else if (title.equals("淋巴细胞")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, LymphChartFragment.getInstance(title)).commit();
        } else if (title.equals("血清葡萄糖")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, SuggarChartFragment.getInstance(title)).commit();
        } else if (title.equals("尿酸")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, UricAcidFragment.getInstance()).commit();
        } else if (title.equals("低密度脂蛋白")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, LowDensityProteinFragment.getInstance()).commit();
        } else if (title.equals("高密度脂蛋白")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, HighDensityProteinFragment.getInstance()).commit();
        } else if (title.equals("总胆固醇")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, TCFragment.getInstance()).commit();
        } else if (title.equals("甘油三酯")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, GYSZFragment.getInstance()).commit();
        }
    }
}
