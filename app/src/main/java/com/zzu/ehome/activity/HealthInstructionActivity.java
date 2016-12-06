package com.zzu.ehome.activity;

import android.os.Bundle;
import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;


/**
 * Created by Administrator on 2016/12/5.
 */

public class HealthInstructionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_health_instruction);
        initViews();
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "健康指导", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

             finishActivity();
            }
        });
    }
}
