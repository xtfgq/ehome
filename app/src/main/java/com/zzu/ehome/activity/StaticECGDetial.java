package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/6/23.
 */
public class StaticECGDetial extends BaseActivity {

    private Intent mIntent;
    private ImageView ivstatic;
    private ImageLoader mImageLoader;
    private String imurl;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_static_ecg);
        mIntent = this.getIntent();
        mImageLoader = ImageLoader.getInstance();
        mIntent = this.getIntent();
        imurl = mIntent.getStringExtra("imurl");
        initViews();
        if (!CommonUtils.isNotificationEnabled(StaticECGDetial.this)) {
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        ivstatic = (ImageView) findViewById(R.id.ivstatic);
        mImageLoader.displayImage(
                imurl, ivstatic);
        ivstatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaticECGDetial.this, ImageECGDetail.class);
                i.putExtra("imurl", imurl);
                startActivity(i);

            }
        });
    }


}

