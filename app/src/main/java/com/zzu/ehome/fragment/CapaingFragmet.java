package com.zzu.ehome.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.yiguo.toast.Toast;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.CapaingAdapter;
import com.zzu.ehome.bean.CapaingBean;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/9/24.
 */
public class CapaingFragmet extends BaseFragment{
    private View mView;
    private ListView listView;
    private CapaingAdapter adapter;
    private RequestMaker requestMaker;
    private boolean isFirst = true;
    private boolean isReflash=false;
    private boolean isLoading=false;
    private String userid;
    private int page;
    private PullToRefreshLayout pulltorefreshlayout;
    private List<CapaingBean> list=new ArrayList<>();
    private SupperBaseActivity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.capaing_frg_layout, null);
        page=1;
        requestMaker = RequestMaker.getInstance();
        initViews();
        initEvent();
        initDatas();
        return mView;
    }

    public void initViews() {
        listView=(ListView) mView.findViewById(R.id.lilstView);
        pulltorefreshlayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);
    }

    public void initEvent() {
        adapter=new CapaingAdapter(getActivity());
        listView.setAdapter(adapter);
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.FAIL);
                    return;

                }
                page=1;
                isFirst = true;
                isReflash=true;
                isLoading=false;
                initDatas();

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    return;

                }
                page++;
                isLoading=true;
                isReflash=false;
                initDatas();


            }
        });


    }

    public void initDatas() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;

        }
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker.APPLogInquiry(userid,"01",page+"",10+"",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("APPLogInquiry");
                    if(getActivity()!=null){
                    if(isReflash){
                        list.clear();
                    }
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if(page>1){
                            Toast.makeText(getActivity(), "没有更多数据了！",
                                    Toast.LENGTH_SHORT).show();
                        }else
                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                        if (isFirst) {
                            isFirst=false;
                        }

                    } else {
                        for(int i=0;i<array.length();i++){
                            CapaingBean bean=new CapaingBean();
                            bean.setCreateDate(array.getJSONObject(i).getString("CreatedDate"));
                            bean.setName(array.getJSONObject(i).getString("Log_Content"));
                            bean.setLog_ID(array.getJSONObject(i).getString("Log_ID"));
                            list.add(bean);
                        }
                        adapter.setmList(list);
                        adapter.notifyDataSetChanged();
                    }
                    if(pulltorefreshlayout!=null) {
                        if (isReflash) {
                            isReflash = false;
                            isFirst = false;
                            pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        } else if (isLoading) {
                            isLoading = false;
                            isFirst = false;
                            pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));


    }


    public static Fragment getInstance() {
        return new CapaingFragmet();
    }

    @Override
    protected void lazyLoad() {

    }
}
