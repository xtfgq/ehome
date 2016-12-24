package com.zzu.ehome.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import android.view.ViewParent;
import android.widget.FrameLayout;

import com.zzu.ehome.R;

import java.util.List;

import static com.zzu.ehome.R.id.container;
import static com.zzu.ehome.R.id.share_gridview;


/**
 * Created by Administrator on 2016/12/12.
 */

public class DecorViewGuideHelper {
    private Activity activity;
    private View rootView;
    private List<View> coverIds;
    private int current = 0;
    private int length = 0;
    private int CONTINUE = 0x1234;


    public DecorViewGuideHelper(Activity activity, List<View> coverIds) {
        this.activity = activity;
        this.coverIds = coverIds;
        this.rootView = activity.getWindow().getDecorView().findViewById(R.id.root_container);
        SharePreferenceUtil.getInstance(activity).setGUIDId(4);
        this.length = coverIds.size();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1234:
                    current++;
//                    SharePreferenceUtil.getInstance(activity).setGUIDId(current+1);
                    display();
                    break;

                default:
                    break;
            }
        }

    };
    public void display() {
       final  ViewParent viewParent = rootView.getParent();
        if (viewParent instanceof FrameLayout) {
            if (length > 0 && current < length) {
                final FrameLayout container = (FrameLayout) viewParent;
                coverIds.get(current).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        container.removeView(coverIds.get(current));
                        if(current==3){

                            return;
                        }

                        handler.sendEmptyMessage(CONTINUE);
                    }
                });

                //current ++ ;
                container.addView(coverIds.get(current));
            }
        }

    }
}
