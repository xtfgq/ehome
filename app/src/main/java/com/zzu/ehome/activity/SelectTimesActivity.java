package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.wheel.wheelview.OnWheelScrollListener;
import com.zzu.ehome.view.wheel.wheelview.WheelView;
import com.zzu.ehome.view.wheel.wheelview.adapter.NumericWheelAdapter;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/8.
 */
public class SelectTimesActivity extends BaseActivity {
    private WheelView mHour;
    private WheelView mMin;
    private TextView tv_ok,tvcancle;

    private int mHours = 0;
    private int mMinus = 1;

    private String mH, mM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        initViews();
        initEvents();


    }
    private  void  initViews(){
        mHour = (WheelView) findViewById(R.id.min);
        mMin = (WheelView) findViewById(R.id.sec);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tvcancle=(TextView)findViewById(R.id.tv_cancel);
        mHour.setVisibleItems(3);
        mMin.setVisibleItems(3);
    }
    private void initEvents(){
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(this, 0, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        mHour.setViewAdapter(numericWheelAdapter3);
        mHour.setCyclic(true);
        mHour.addScrollingListener(scrollListener);


        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(this, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        mMin.setViewAdapter(numericWheelAdapter4);
        mMin.setCyclic(true);
        mMin.addScrollingListener(scrollListener);
        Date d = new Date();
        mHours = d.getHours();
        mMinus = d.getMinutes();
        mHour.setCurrentItem(mHours);
        mMin.setCurrentItem(mMinus);
        tvcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHours < 10) {
                    mH = "0" + mHours;
                } else {
                    mH = "" + mHours;
                }
                if (mMinus < 10) {
                    mM = "0" + mMinus;
                } else {
                    mM = "" + mMinus;
                }


                String time =  mH + ":" + mM;
                Intent intent = new Intent();
                intent.putExtra("time", time);
                setResult(Constants.REQUEST_CALENDAR, intent);
                finish();
            }
        });

    }
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            mHours = mHour.getCurrentItem();
            mMinus = mMin.getCurrentItem();

        }
    };



}
