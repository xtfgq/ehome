package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MedicinalRecordAdapter;
import com.zzu.ehome.bean.MedicationDate;
import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Mersens on 2016/6/27.
 */
public class YYJLDataActivity extends BaseActivity {
    private RequestMaker requestMaker;
    private ListView listView;
    private String userid;
    private int page = 1;
    private MedicinalRecordAdapter mAdapter;

    private LinearLayout rlup;

    private LayoutInflater inflater;
    private PullToRefreshLayout pulltorefreshlayout;


    private List<MedicationRecord> mlist = new ArrayList<MedicationRecord>();
    private boolean isFirst = true;
    private boolean isReflash=false;
    private boolean isLoading=false;
    private EHomeDao dao;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(YYJLDataActivity.this).getUserId();
        setContentView(R.layout.layout_yyjl_data);
        initViews();
        dao=new EHomeDaoImpl(this);
        EventBus.getDefault().register(this);
        initEvent();
        initDate();

    }
    public void initEvent() {
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
                isFirst = true;
                isReflash = true;
                isLoading = false;
                initDate();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                isFirst = false;
                isLoading = true;
                isReflash = false;
                initDate();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(YYJLDataActivity.this, YYJLDesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("YYJLRecords", mlist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void initViews() {
        inflater = LayoutInflater.from(YYJLDataActivity.this);
        rlup = (LinearLayout) findViewById(R.id.ll_up);

        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new MedicinalRecordAdapter(YYJLDataActivity.this);
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        listView.setAdapter(mAdapter);
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "用药记录", R.mipmap.icon_add_zoushi
                , new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {
                        finishActivity();
                    }
                }, new HeadView.OnRightClickListener() {
                    @Override
                    public void onClick() {

                        startActivity(new Intent(YYJLDataActivity.this, YYJLActivity.class));

                    }
                });
    }

    private void initDate() {
        startProgressDialog();
        requestMaker.MedicationRecordInquiry(userid,dao.findUserInfoById(userid).getUserno(),10 + "", page + "", new JsonAsyncTask_Info(YYJLDataActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MedicationRecordInquiry");
                    stopProgressDialog();
                    if(isReflash){
                        mlist.clear();
                    }
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if (isFirst) {
                            listView.setVisibility(View.GONE);
                            rlup.setVisibility(View.VISIBLE);
                            isFirst=false;
                        }

                    } else {
                        if (isFirst) {
                            rlup.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                        MedicationDate date = JsonTools.getData(result.toString(), MedicationDate.class);
                        List<MedicationRecord> list = date.getData();

                        mlist.addAll(list);
                        mAdapter.setList(mlist);
                        mAdapter.notifyDataSetChanged();
                    }
                    if(isReflash){
                        isReflash=false;
                        isFirst=false;
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else if(isLoading){
                        isLoading=false;
                        isFirst=false;
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }));

    }



    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_manager_data) == event
                .getRefreshWhere()) {
            page = 1;
            isFirst = true;
           isReflash=false;
           isLoading=false;
            mlist.clear();
            initDate();
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

