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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.zzu.ehome.R;

import com.zzu.ehome.bean.PlatformBean;

import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/3.
 */

public class PlatformTestActivity extends BaseActivity{
    private ListView mListView;
    private LinearLayout layout_no_msg;
    private MyAdapter adapter=null;
    private List<PlatformBean> list=new ArrayList<>();
    private PullToRefreshLayout pulltorefreshlayout;
    int pageNum=1;
    private boolean isFirst,isReflash,isLoading;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.platform_test);
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
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);

    }
    private void initEvnets(){
        adapter = new MyAdapter(list);
        mListView.setAdapter(adapter);

        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.FAIL);
                    return;
                }
                pageNum=1;
                isFirst = true;
                isReflash=true;
                isLoading=false;
                list.clear();
                initDatas();

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(!isNetWork){
                   showNetWorkErrorDialog();
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    return;
                }
                pageNum++;
                isLoading=true;
                isReflash=false;
                initDatas();


            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(PlatformTestActivity.this,WebPlatmActivity.class);
                i.putExtra("flag","info");
                startActivity(i);
            }
        });
    }
    private void initDatas(){
        for(int i=0;i<10;i++){
            PlatformBean bean=new PlatformBean();
            bean.setTime("2016年4月体检报告");
            bean.setName("郑州大学第五附属医院"+i*pageNum);
            list.add(bean);
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();

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
            holder.tv_time.setText(list.get(position).getTime());
            holder.tv_name.setText(list.get(position).getName());


            return convertView;
        }
    }
    public static class ViewHolder {
        public TextView tv_time;
        public TextView tv_name;

    }
}
