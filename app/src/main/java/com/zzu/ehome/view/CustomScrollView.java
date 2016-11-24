package com.zzu.ehome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zzu.ehome.view.crop.util.Log;


/**
 * Created by Administrator on 2016/11/3.
 */

public class CustomScrollView extends HorizontalScrollView {
//    private GestureDetector mGestureDetector;
//    View.OnTouchListener mGestureListener;
//    private float startX;
//    private float startY;
    private int lastX;
    private int lastY;


    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
/*        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);*/
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_UP:
//
//                Log.e("getScrollX()",getScrollX()+"");
//                Log.e("getScrollY()",getScrollY()+"");
//
//
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }

    // Return false if we're scrolling in the x direction
/*    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(Math.abs(distanceX)>20 ||Math.abs(distanceY)<400) {
                return true;
            }
            return false;
        }
    }*/
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        this.getParent().requestDisallowInterceptTouchEvent(true);
//        return super.dispatchTouchEvent(ev);
//    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getRawX();
//        int y = (int) ev.getRawY();
//        int dealtX = 0;
//        int dealtY = 0;
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                dealtX = 0;
//                dealtY = 0;
//                // 保证子View能够接收到Action_move事件
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                dealtX += Math.abs(x - lastX);
//                dealtY += Math.abs(y - lastY);
//               Log.e("vvvvxxx", "dealtX:=" + dealtX);
//               Log.e("vvvvyyy", "dealtY:=" + dealtY);
//                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
//                if (dealtX >= 20) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                lastX = x;
//                lastY = y;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int curPosition;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:

                    getParent().requestDisallowInterceptTouchEvent(true);
                break;


        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        Log.e("onScrollChangedX", "dealtX:=" + l);
        Log.e("onScrollChangedY", "dealtY:=" + t);


        super.onScrollChanged(l, t, oldl, oldt);
    }
}
