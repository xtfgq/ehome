package com.zzu.ehome.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/17.
 * NearPharmacyFragment
 */
public class NearPharmacyFragment extends BaseFragment {
    private ListView mListView;
    private View mView;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private PoiSearch mPoiSearch = null;
    int radius = 1000;
    private static String keyWorlds = "药店";
    private PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
    private boolean isFirst = true;
    private BDLocation mLocation;
    private List<PoiInfo> list=new ArrayList<>();
    private static final double EARTH_RADIUS = 6378137;
    private PullToRefreshLayout pulltorefreshlayout;
    private int pageNum=0;
    private MyAdapter adapter=null;
    private boolean isReflash=false;
    private boolean isLoading=false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near_pharmacy, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initViews();
        initEvent();
        initDatas();

    }

    public void initViews() {
        mListView = (ListView) mView.findViewById(R.id.lilstView);
        adapter = new MyAdapter(list);
        mListView.setAdapter(adapter);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
        pulltorefreshlayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);

    }

    public void initEvent() {
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageNum=0;
                isFirst = true;
                isReflash=true;
                isLoading=false;
                searchNearbyProcess(mLocation);

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pageNum++;
                isLoading=true;
                isReflash=false;
                searchNearbyProcess(mLocation);


            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo p = list.get(position);
                final double mLatitude = p.location.latitude;
                final double mLongitude = p.location.longitude;
                final String name = p.name;
                DialogTips dialog = new DialogTips(getActivity(), name, "到这里去");
                dialog.setCanceledOnTouchOutside(true);
                dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int userId) {
                        Uri mUri = Uri.parse("geo:" + mLatitude + "," + mLongitude + "?" + "q=" + name);
                        Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
                        startActivity(mIntent);
                    }
                });

                dialog.show();
                dialog = null;

            }
        });
    }


    public void initDatas() {
        nearbySearchOption.keyword(keyWorlds);
        nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
        nearbySearchOption.radius(radius);

        location();
    }

    class MyAdapter extends BaseAdapter {
        public void setList(List<PoiInfo> list){
            this.list=list;
            notifyDataSetChanged();
        }

        private List<PoiInfo> list;

        public MyAdapter(List<PoiInfo> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.near_pharmacy_item, null);
                holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
                holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
                holder.layout_b = (RelativeLayout) convertView.findViewById(R.id.layout_b);
                holder.layout_h = (RelativeLayout) convertView.findViewById(R.id.layout_h);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.layout_b.setVisibility(View.GONE);
            holder.layout_h.setVisibility(View.GONE);
            PoiInfo p = list.get(position);
            String tel=p.phoneNum;
//            if(TextUtils.isEmpty(tel)){
//                holder.tv_tel.setVisibility(View.GONE);
//            }else{
//                holder.tv_tel.setVisibility(View.VISIBLE);
//                holder.tv_tel.setText(tel);
//
//            }
            holder.tv_name.setText(p.name);
            holder.tv_address.setText(p.address);
            double mLongitude = p.location.longitude;
            double mLatitude = p.location.latitude;
            int distance = getDistance(mLocation.getLongitude(), mLocation.getLatitude(), mLongitude, mLatitude);
            holder.tv_distance.setText(distance + "米");
            return convertView;
        }
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static int getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return (int) Math.round(s * 10000) / 10000;
    }

    public static class ViewHolder {
        public TextView tv_distance;
        public TextView tv_name;
        public TextView tv_address;
        public RelativeLayout layout_b;
        public RelativeLayout layout_h;
        public TextView tv_tel;
    }


    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                ToastUtils.showMessage(getActivity(), "未找到结果");
                if(isReflash){
                    isReflash=false;
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else if(isLoading){
                    isLoading=false;
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                isFirst = false;
                if(isReflash){
                    list.clear();
                }
                List<PoiInfo> mList=poiResult.getAllPoi();
                if(mList!=null && mList.size()>0){
                    list.addAll(mList);
                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                if(isReflash){
                    isReflash=false;
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else if(isLoading){
                    isLoading=false;
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }

    private void location() {
        mLocationClient = new LocationClient(getActivity());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    // 初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(10000);// 设置发起定位请求的间隔时间,单位为ms
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
                if (isFirst) {
                    mLocation = location;
                    searchNearbyProcess(location);
                }
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    public void searchNearbyProcess(BDLocation location) {
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
        nearbySearchOption.location(l);
        nearbySearchOption.pageNum(pageNum);
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    public static Fragment getInstance() {

        return new NearPharmacyFragment();
    }

}
