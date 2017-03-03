package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MedicalRecordsAdapter;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.bean.TreatmentInquirywWithPageDate;
import com.zzu.ehome.utils.CommonUtils;
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

/**
 * Created by Administrator on 2016/9/5.
 */
public class MedicalRecordsActivity extends BaseActivity {
    private RequestMaker requestMaker;
    private String userid;
    private int page = 1;
    private RelativeLayout rlup;
    private ListView listView;
    private MedicalRecordsAdapter mAdapter;
    List<TreatmentInquirywWithPage> list = new ArrayList<TreatmentInquirywWithPage>();

    private PullToRefreshLayout pulltorefreshlayout;
    private boolean isFirst = true;
    private boolean isReflash = false;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_health_files);
        userid = SharePreferenceUtil.getInstance(MedicalRecordsActivity.this).getUserId();
        requestMaker = RequestMaker.getInstance();
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(MedicalRecordsActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "就诊记录", R.mipmap.icon_add_zoushi, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                doAdd();
            }
        });
        rlup = (RelativeLayout) findViewById(R.id.rl_up);
        mAdapter = new MedicalRecordsAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        listView.setAdapter(mAdapter);
    }

    private void doAdd() {
        startActivity(new Intent(MedicalRecordsActivity.this, CreateillnessActivity.class));
    }

    public void initEvent() {
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
                isFirst = true;
                isReflash = true;
                isLoading = false;
                initDatas();


            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                isLoading = true;
                isReflash = false;
                initDatas();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MedicalRecordsActivity.this, MedicalRecordsDesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MedicalRecords", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void initDatas() {
        startProgressDialog();
        requestMaker.TreatmentInquirywWithPage(userid, 10 + "", page + "", new JsonAsyncTask_Info(MedicalRecordsActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentInquirywWithPage");
                    stopProgressDialog();
                    if (isReflash) {
                        list.clear();
                    }

                    if (array.getJSONObject(0).has("MessageCode")) {
                        if (isFirst) {
                            rlup.setVisibility(View.VISIBLE);
                            isFirst = false;
                        }

                    } else {

                        if (isFirst) {
                            rlup.setVisibility(View.GONE);
                            isFirst=false;
                        }
                        TreatmentInquirywWithPageDate date = JsonTools.getData(result.toString(), TreatmentInquirywWithPageDate.class);
                        for (TreatmentInquirywWithPage tp : date.getData()) {
                            list.add(tp);
                        }
                        mAdapter.setmList(list);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (isReflash) {
                        isReflash = false;
                        isFirst = false;
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else if (isLoading) {
                        isLoading = false;
                        isFirst = false;
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            page = 1;
            isFirst = true;
            isReflash = true;
            isLoading = false;
            list.clear();
            initDatas();
        }
    }
}
