package com.zzu.ehome.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.zzu.ehome.R;
import com.zzu.ehome.fragment.DynamicFragment;
import com.zzu.ehome.fragment.StaticFragment;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by Administrator on 2016/7/25.
 */
public class InternetMap extends BaseActivity {
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;
    private Intent mIntent;
    private String  title;
    private double Latitude, Longitude;
    private Marker mMarker;
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_geocoder);
        mIntent = this.getIntent();
        title = mIntent.getStringExtra("Name");
        Latitude = mIntent.getDoubleExtra("Latitude", 0);
        Longitude = mIntent.getDoubleExtra("Longitude", 0);
        initViews();
        if(!CommonUtils.isNotificationEnabled(InternetMap.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20.0f));
        LatLng result = new LatLng(Latitude, Longitude);
        mMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().position(result)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result));
        TextView location = new TextView(InternetMap.this);
        location.setBackgroundResource(R.mipmap.popup);
        location.setPadding(30, 20, 30, 50);
        location.setTextColor(getResources().getColor(R.color.text_color1));
        location.setText(title);
        mInfoWindow = new InfoWindow(location, mMarker.getPosition(), -87);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }


}
