package com.zzu.ehome.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
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
import com.zzu.ehome.activity.NearPharmacyActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.zzu.ehome.activity.NearPharmacyActivity.mLocation;

/**
 * Created by Mersens on 2016/8/17.
 * NearPharmacyFragment
 */
public class NearPharmacyFragment extends BaseFragment {
    private ListView mListView;
    private View mView;

    private PoiSearch mPoiSearch = null;
    int radius = 1000;
    private static String keyWorlds = "药店";
    private PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
    private boolean isFirst = true;

    private List<PoiInfo> list=new ArrayList<>();
    private static final double EARTH_RADIUS = 6378137;
    private PullToRefreshLayout pulltorefreshlayout;
    private int pageNum=0;
    private MyAdapter adapter=null;
    private boolean isReflash=false;
    private boolean isLoading=false;
    private LinearLayout layout_no_msg;
    private TextView tvnodate;
    private CompositeSubscription compositeSubscription;
    private SupperBaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity) context;
    }

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
        layout_no_msg=(LinearLayout)mView.findViewById(R.id.layout_no_msg);
        pulltorefreshlayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);

        tvnodate=(TextView)mView.findViewById(R.id.tvnodate);
        tvnodate.setText("暂无数据");

    }

    public void initEvent() {
        compositeSubscription = new CompositeSubscription();
        //监听订阅事件
        Subscription subscription = RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }

                        if (event instanceof EventType){
                            EventType type=(EventType)event;
                            if("location".equals(type.getType())){
                                if (isFirst) {

                                    searchNearbyProcess(NearPharmacyActivity.getLocation());
                                }
                            }


                        }

                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.FAIL);
                    return;
                }
                pageNum=0;
                isFirst = true;
                isReflash=true;
                isLoading=false;
                searchNearbyProcess(NearPharmacyActivity.getLocation());

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    return;
                }
                pageNum++;
                isLoading=true;
                isReflash=false;
                searchNearbyProcess(NearPharmacyActivity.getLocation());


            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
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
            holder.tv_distance.setText(distance + "m");
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
                if(isReflash){
                    isReflash=false;

                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else if(isLoading){
                    isLoading=false;
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                if(pageNum==0) {
                    layout_no_msg.setVisibility(View.VISIBLE);
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
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }






    public void searchNearbyProcess(BDLocation location) {
        if(location==null)
            return;
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
        nearbySearchOption.location(l);
        nearbySearchOption.pageNum(pageNum);
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    public static Fragment getInstance() {

        return new NearPharmacyFragment();
    }


}
