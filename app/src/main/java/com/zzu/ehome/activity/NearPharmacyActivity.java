package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zzu.ehome.R;
import com.zzu.ehome.fragment.CooperationPharmacyFragment;
import com.zzu.ehome.fragment.NearPharmacyFragment;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/17.
 * 附近药店
 */
public class NearPharmacyActivity extends BaseActivity {
    private RelativeLayout layout_near, layout_cooperation;
    private TextView tv_near, tv_cooperation;
    private int selectColor, unSelectColor;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    public  static BDLocation mLocation = null;
    public static BDLocation getLocation() {
        return mLocation;
    }
    private Fragment[] fragments;
    private int index;
    private int currentIndex;





    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_near_pharmacy);
        location();
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(NearPharmacyActivity.this)){
            showTitleDialog("请打开通知中心");
        }

    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "附近药店", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        layout_near = (RelativeLayout) findViewById(R.id.layout_near);
        layout_cooperation = (RelativeLayout) findViewById(R.id.layout_cooperation);
        tv_near = (TextView) findViewById(R.id.tv_near);
        tv_cooperation = (TextView) findViewById(R.id.tv_cooperation);

    }


    public void initEvent() {
        layout_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                if(!CommonUtils.isNotificationEnabled(NearPharmacyActivity.this)){
                    showTitleDialog("请打开通知中心");
                }
                index=0;
                setColor(Type.NEAR);
                addFragment(Type.NEAR);
            }
        });
        layout_cooperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                if(!CommonUtils.isNotificationEnabled(NearPharmacyActivity.this)){
                    showTitleDialog("请打开通知中心");
                }
                index=1;
                setColor(Type.COOPERATION);
                addFragment(Type.COOPERATION);

            }
        });

    }

    public void initDatas() {
        unSelectColor = getResources().getColor(R.color.text_color2);
        selectColor = getResources().getColor(R.color.actionbar_color);
        tv_near.setTextColor(selectColor);
        tv_cooperation.setTextColor(unSelectColor);
        fragments=new Fragment[2];
        fragments[0]=NearPharmacyFragment.getInstance();
        fragments[1]=CooperationPharmacyFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragments[0]).commit();
    }


    public void setColor(Type type) {
        resetColor();
        switch (type) {
            case NEAR:
                tv_near.setTextColor(selectColor);
                break;
            case COOPERATION:
                tv_cooperation.setTextColor(selectColor);
                break;
        }

    }


    public void resetColor() {
        tv_near.setTextColor(unSelectColor);
        tv_cooperation.setTextColor(unSelectColor);
    }

    public Fragment creatFragment(Type type) {
        Fragment fragment = null;
        switch (type) {
            case NEAR:
                fragment = fragments[0];
                break;

            case COOPERATION:
                fragment = fragments[1];
                break;
        }

        return fragment;
    }


    public void addFragment(Type type) {
        if(currentIndex!=index){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fragments[currentIndex]);
            if(!fragments[index].isAdded()){
                ft.add(R.id.fragment_container, fragments[index]);
            }
            ft.show(fragments[index]).commit();
        }
        currentIndex=index;
    }

    public enum Type {
        NEAR, COOPERATION;
    }
    private void location() {
        mLocationClient = new LocationClient(NearPharmacyActivity.this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    // 初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(3000);// 设置发起定位请求的间隔时间,单位为3000ms
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location) {
                mLocation=location;
                RxBus.getInstance().send(new EventType("location"));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.start();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mMyLocationListener);
    }
}
