package com.zzu.ehome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.view.wheel.wheelview.OnWheelScrollListener;
import com.zzu.ehome.view.wheel.wheelview.WheelView;
import com.zzu.ehome.view.wheel.wheelview.adapter.NumericWheelAdapter;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/19.
 */
public class TimePopWindows extends PopupWindow implements View.OnClickListener{
    private Context context;
    private int mHours,mMinus;
    private WheelView mHour,mMin;
    private TextView tvcancle,tvok;
    public OnGetData getmOnGetData() {
        return mOnGetData;
    }

    public void setmOnGetData(OnGetData mOnGetData) {
        this.mOnGetData = mOnGetData;
    }

    private OnGetData mOnGetData;

    public interface OnGetData {
        void onDataCallBack(String time);
    }
    public TimePopWindows(Context mContext, View parent) {

        super(mContext);

        this.context = mContext;
        View view = View
                .inflate(mContext, R.layout.item_time_layout, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));
        tvcancle=(TextView) view.findViewById(R.id.tv_cancel);
        tvok=(TextView)view.findViewById(R.id.tv_ok);
        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mHour= (WheelView) view.findViewById(R.id.hour);
        mMin= (WheelView) view.findViewById(R.id.min);
        initEvnets();


    }
    private void initEvnets() {
        tvok.setOnClickListener(this);
        tvcancle.setOnClickListener(this);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(context, 0, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        mHour.setViewAdapter(numericWheelAdapter3);
        mHour.setCyclic(true);
        mHour.addScrollingListener(scrollListener);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(context, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        mMin.setViewAdapter(numericWheelAdapter4);
        mMin.setCyclic(true);
        mMin.addScrollingListener(scrollListener);
        Date d = new Date();
        mHours=d.getHours();
        mMinus=d.getMinutes();
        mHour.setCurrentItem(mHours);
        mMin.setCurrentItem(mMinus);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                String hours="";
                String minus="";
                hours=mHours<10? "0"+ mHours:""+mHours;
                minus=mMinus<10? "0"+ mMinus:""+mMinus;
                mOnGetData.onDataCallBack(hours+":"+minus);
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
}
