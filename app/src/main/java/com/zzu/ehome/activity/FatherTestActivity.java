package com.zzu.ehome.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

import static com.zzu.ehome.R.id.layout_search;

/**
 * Created by Administrator on 2017/1/3.
 */

public class FatherTestActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout layout_search;
    private RelativeLayout layout_add;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.report_new);
        initViews();
        initEvents();
        if(!CommonUtils.isNotificationEnabled(FatherTestActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "体检报告",
                new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {

                        finishActivity();
                    }
                });
        layout_search=(RelativeLayout)findViewById(R.id.layout_search);
        layout_add=(RelativeLayout)findViewById(R.id.layout_add);
    }
    public void initEvents(){
        layout_search.setOnClickListener(this);
        layout_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_search:
                startActivity(new Intent(FatherTestActivity.this,PlatformTestActivity.class));
                break;
            case R.id.layout_add:
                startActivity(new Intent(FatherTestActivity.this,ExaminationReportActivity.class));
                break;
        }

    }
}
