package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.zzu.ehome.R;

import com.zzu.ehome.bean.PlatformBean;

import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;
import com.zzu.ehome.view.RefreshLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.zzu.ehome.R.id.refreshLayout;


/**
 * Created by Administrator on 2017/1/3.
 */

public class PlatformTestActivity extends BaseActivity{
    private ListView mListView;
    private LinearLayout layout_no_msg;
    private MyAdapter adapter=null;
    private List<PlatformBean> list=new ArrayList<>();
    private RefreshLayout refreshLayout;

    private boolean isRefresh=false;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    private String cardNo,name,userid;
    private CompositeSubscription compositeSubscription;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.platform_test);
        requestMaker=RequestMaker.getInstance();
        dao=new EHomeDaoImpl(PlatformTestActivity.this);
        userid= SharePreferenceUtil.getInstance(PlatformTestActivity.this).getUserId();
        cardNo=dao.findUserInfoById(userid).getUserno();
        name=dao.findUserInfoById(userid).getUsername();
        initViews();
        if(!CommonUtils.isNotificationEnabled(PlatformTestActivity.this)){
            showTitleDialog("请打开通知中心");
        }
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        initEvnets();
        initDatas();
    }
    public void initViews() {
        mListView = (ListView) findViewById(R.id.listView);
        layout_no_msg=(LinearLayout)findViewById(R.id.layout_no_msg);
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "智能查询报告", R.mipmap.icon_add_zoushi, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                startActivity(new Intent(PlatformTestActivity.this,SmartSearchActivity.class));


            }
        });
        refreshLayout=(RefreshLayout) findViewById(R.id.refreshLayout);

    }
    private void initEvnets(){
        adapter = new MyAdapter(list);
        mListView.setAdapter(adapter);
        compositeSubscription = new CompositeSubscription();
        //监听订阅事件
        Subscription subscription = RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }

                        if (event instanceof EventType){
                            EventType type=(EventType)event;
                            if("smart".equals(type.getType())){
                                list.clear();
                                initDatas();
                            }
                        }




                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {
                if(!isNetWork){
                    if(!isNetWork){
                        refreshLayout.refreshFinish(RefreshLayout.FAIL);
                        return;
                    }
                    return;
                }
                isRefresh=true;
                list.clear();
               initDatas();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(PlatformTestActivity.this,WebPlatmActivity.class);
                i.putExtra("flag","info");
                i.putExtra("name",name);
                i.putExtra("UserNo",cardNo);
                i.putExtra("code",list.get(position).getCHKCODE());
                i.putExtra("time",list.get(position).getTime());
                i.putExtra("hosname",list.get(position).getName());
                startActivity(i);
            }
        });
    }
    private void initDatas(){

//        name="张三";
//        cardNo="410322198608051234";
        requestMaker.CheckupInfoInquiry(cardNo,name,new JsonAsyncTask_Info(PlatformTestActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    stopProgressDialog();
                    JSONArray array = mySO.getJSONArray("CheckupInfoInquiry");
                    if(layout_no_msg!=null&&mListView!=null) {
                        if (array.getJSONObject(0).has("MessageCode")) {
                            layout_no_msg.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);

                        } else {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
                                list.add(getDataFromJson(json));
                            }
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            layout_no_msg.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if(isRefresh){
                        refreshLayout.refreshFinish(RefreshLayout.SUCCEED);
                        isRefresh=false;
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }
        } ));

    }
    private PlatformBean getDataFromJson(JSONObject json)throws JSONException {
        PlatformBean bean = new PlatformBean();
        bean.setTime(json.getString("RecordTime"));
        bean.setName(json.getString("HOSName"));
        bean.setCHKCODE(json.getString("CHKCODE"));
        return  bean;
    }
    class MyAdapter extends BaseAdapter {
        public void setList(List<PlatformBean> list){
            this.list=list;
            notifyDataSetChanged();
        }

        private List<PlatformBean> list;

        public MyAdapter(List<PlatformBean> list) {
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
                convertView = LayoutInflater.from(PlatformTestActivity.this).inflate(R.layout.platform_test_item, null);
                holder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
                holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_time.setText(DateUtils.StringPattern(list.get(position).getTime(), "yyyy/MM/dd HH:mm:ss", "yyyy年MM月")+"体检报告");
            holder.tv_name.setText(list.get(position).getName());
            return convertView;
        }
    }
    public static class ViewHolder {
        public TextView tv_time;
        public TextView tv_name;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
