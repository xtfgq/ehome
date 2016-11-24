package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzu.ehome.R;

/**
 * Created by Mersens on 2016/9/2.
 */
public class NotMapActivity extends NetBaseActivity {
    private TextView tv_ok;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_not_map);
        initViews();

    }
    public void initViews(){
        tv_ok=(TextView)findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
