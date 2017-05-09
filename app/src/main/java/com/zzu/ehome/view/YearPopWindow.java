package com.zzu.ehome.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.wheel.wheelview.OnWheelScrollListener;
import com.zzu.ehome.view.wheel.wheelview.WheelView;
import com.zzu.ehome.view.wheel.wheelview.adapter.NumericWheelAdapter;

/**
 * Created by Mersens on 2016/9/28.
 */

public class YearPopWindow extends PopupWindow implements View.OnClickListener{
    private Context context;
    private int year;
    private WheelView wheelView;
    private TextView tvcancle,tvok;


    private OnYearSelectListener listener;
    public void setOnAgeSelectListener(OnYearSelectListener listener){
        this.listener=listener;
    }

    public interface OnYearSelectListener{
        void onSelect(String select);
    }
    public YearPopWindow(Context mContext, View parent,int year) {

        super(mContext);

        this.context = mContext;
        View view = View
                .inflate(mContext, R.layout.item_age_layout, null);
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
        this.year=year;
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        wheelView= (WheelView) view.findViewById(R.id.wheelView);
        initEvnets();


    }
    private void initEvnets() {
        tvok.setOnClickListener(this);
        tvcancle.setOnClickListener(this);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(context, year-5, year, "%04d");
        numericWheelAdapter3.setLabel("");
        wheelView.setViewAdapter(numericWheelAdapter3);
        wheelView.setCyclic(true);
        wheelView.addScrollingListener(scrollListener);
        wheelView.setCurrentItem(year);
    }
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            year = wheelView.getCurrentItem();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                listener.onSelect(year+"");
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
}
