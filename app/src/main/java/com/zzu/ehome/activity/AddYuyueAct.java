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
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AddYuyueAct extends BaseSimpleActivity implements View.OnClickListener {

    private TextView tvcontinue;
    private Button btnext;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_yycg);
        initViews();
        initEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_continue:
                CustomApplcation.getInstance().finishSingleActivityByClass(MyDoctorActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(WebConfigAct.class);
                startActivity(new Intent(AddYuyueAct.this,MyDoctorActivity.class));

                break;
            case R.id.btnext:
                CustomApplcation.getInstance().finishSingleActivityByClass(MyDoctorActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(WebConfigAct.class);
                Intent i=new Intent(AddYuyueAct.this, MainActivity.class);
                i.putExtra("Home","Home");
                startActivity(i);
                break;

        }

    }

    private void initEvent() {
        tvcontinue.setOnClickListener(this);
        btnext.setOnClickListener(this);

    }

    private void initViews() {
        tvcontinue = (TextView) findViewById(R.id.tv_continue);
        btnext=(Button)findViewById(R.id.btnext);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomApplcation.getInstance().finishSingleActivityByClass(MyDoctorActivity.class);
            CustomApplcation.getInstance().finishSingleActivityByClass(WebConfigAct.class);
            Intent i=new Intent(AddYuyueAct.this, MainActivity.class);
            i.putExtra("Home","Home");
            startActivity(i);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
