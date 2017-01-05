package com.zzu.ehome.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.zzu.ehome.R;
import com.zzu.ehome.activity.CooperationPharmacyActivity;
import com.zzu.ehome.activity.NearPharmacyActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.bean.PharmacyBean;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Mersens on 2016/8/17.
 * NearPharmacyFragment
 */
public class CooperationPharmacyFragment extends BaseFragment {
    private ListView mListView;
    private View mView;
    private PullToRefreshLayout pulltorefreshlayout;
    private RequestMaker requestMaker;
    private static int pagesize=10;
    private int pageindex=1;
    private static final double EARTH_RADIUS = 6378137;
    private BDLocation mLocation= NearPharmacyActivity.mLocation;
    private List<PharmacyBean> list=new ArrayList<>();
    private MyAdapter adapter=null;
    private boolean isReflash=false;
    private boolean isLoading=false;
    public static String ID="id";
    private LinearLayout layout_no_msg;
    private TextView  tvnodate;
    private CompositeSubscription compositeSubscription;
    private SupperBaseActivity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near_pharmacy,null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        initViews();
        initEvent();
        initDatas();
    }

    public void  initViews(){
        adapter=new MyAdapter(list);
        requestMaker = RequestMaker.getInstance();
        mListView=(ListView)mView.findViewById(R.id.lilstView);
        layout_no_msg=(LinearLayout)mView.findViewById(R.id.layout_no_msg);
        pulltorefreshlayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);
        tvnodate=(TextView)mView.findViewById(R.id.tvnodate);
        tvnodate.setText("正在签约中");
        mListView.setAdapter(adapter);
    }


    public void initEvent(){
        //监听订阅事件
        compositeSubscription=new CompositeSubscription();
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
                                if(pageindex==1) {
                                    isReflash = true;
                                    isLoading = false;
                                    initDatas();
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
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.FAIL);
                    activity.showNetWorkErrorDialog();
                    return;
                }
                pageindex=1;
                isReflash=true;
                isLoading=false;
                initDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    activity.showNetWorkErrorDialog();
                    return;
                }
                pageindex++;
                isReflash=false;
                isLoading=true;
                initDatas();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
                Intent intent=   new Intent(getActivity(), CooperationPharmacyActivity.class);
                intent.putExtra(ID,list.get(position).getId());
                startActivity(intent);

            }
        });
    }

    class MyAdapter extends BaseAdapter {
        public void setList(List<PharmacyBean> list){
            this.list=list;

        }

        private List<PharmacyBean> list;

        public MyAdapter(List<PharmacyBean> list) {
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
                holder.tv_h = (TextView) convertView.findViewById(R.id.tv_h);
                holder.tv_b = (TextView) convertView.findViewById(R.id.tv_b);
                holder.layout_b = (RelativeLayout) convertView.findViewById(R.id.layout_b);
                holder.layout_h = (RelativeLayout) convertView.findViewById(R.id.layout_h);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PharmacyBean p = list.get(position);
            holder.tv_name.setText(p.getPharmacyName());
            holder.tv_address.setText(p.getPharmacyAddress());
            String zk=p.getZhekou();
            if(TextUtils.isEmpty(zk)){
                holder.layout_h.setVisibility(View.GONE);
            }else{
                holder.tv_h.setText(zk+"折");
            }
            String yb=p.getYibaoType();
            if(TextUtils.isEmpty(yb)){
                holder.layout_b.setVisibility(View.GONE);
            }else if(yb.contains(",")){
                String ybs[]=yb.split(",");
                holder.tv_b.setText("省,市保");


            }else if("1".equals(yb)){
                holder.tv_b.setText("市保");

            }else if("0".equals(yb)){
                holder.tv_b.setText("省保");
            }

            double mLongitude = Double.valueOf(p.getLongitude());
            double mLatitude = Double.valueOf(p.getLatitude());
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

    public void initDatas(){
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;
        }
        requestMaker.PharmacyInquiry(pagesize+"",pageindex+"", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                List<PharmacyBean> mList=new ArrayList<PharmacyBean>();
                try {
                    JSONArray array = mySO.getJSONArray("PharmacyInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if(pageindex==1) {

                            layout_no_msg.setVisibility(View.VISIBLE);
                        }

                    } else {

                        for(int i=0;i<array.length();i++){
                            JSONObject json=array.getJSONObject(i);
                            PharmacyBean pb=new PharmacyBean();
                            String PharmacyPhone=json.getString("PharmacyPhone");
                            pb.setPharmacyPhone(PharmacyPhone);
                            String PharmacyName=json.getString("PharmacyName");
                            pb.setPharmacyName(PharmacyName);
                            String PharmacyAddress=json.getString("PharmacyAddress");
                            pb.setPharmacyAddress(PharmacyAddress);
                            String YibaoType=json.getString("YibaoType");
                            pb.setYibaoType(YibaoType);
                            String Zhekou=json.getString("Zhekou");
                            pb.setZhekou(Zhekou);
                            String Latitude=json.getString("Latitude");
                            pb.setLatitude(Latitude);
                            String Longitude=json.getString("Longitude");
                            pb.setLongitude(Longitude);
                            String id=json.getString("ID");
                            pb.setId(id);
                            mList.add(pb);
                        }
                        if(isReflash){
                            list.clear();
                        }
                        if(mList.size()>0){
                            list.addAll(mList);
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        layout_no_msg.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if(isReflash){
                        isReflash=false;
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else if(isLoading){
                        isLoading=false;
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }
        }));


    }

    public static class ViewHolder {
        public TextView tv_distance;
        public TextView tv_name;
        public TextView tv_address;
        public RelativeLayout layout_b;
        public RelativeLayout layout_h;
        private TextView tv_h;
        private TextView tv_b;
    }
    @Override
    protected void lazyLoad() {

    }


    public static Fragment getInstance(){

        return new CooperationPharmacyFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
