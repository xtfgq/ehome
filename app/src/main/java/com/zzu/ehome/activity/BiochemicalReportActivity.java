package com.zzu.ehome.activity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.zzu.ehome.R;
import com.zzu.ehome.adapter.InspectionReportAdapter;
import com.zzu.ehome.bean.ResultContent;

import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;


import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/6.
 */

public class BiochemicalReportActivity extends BaseActivity {

    private ListView listView;
    private LinearLayout layout_none;
    private EHomeDao dao;
    private String usrid;
    private PullToRefreshLayout pulltorefreshlayout;
    private List<ResultContent> mList = new ArrayList<ResultContent>();
    private InspectionReportAdapter mAdapter;
    private boolean isFirst = true;
    private boolean isReflash = false;
    private boolean isLoading = false;
    private int page = 1;
    private User dbUser;
    private RequestMaker requestMaker;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_biochemical_report);
        dao = new EHomeDaoImpl(this);
        requestMaker = RequestMaker.getInstance();
        usrid = SharePreferenceUtil.getInstance(BiochemicalReportActivity.this).getUserId();
        initViews();

        initEvent();
        initDatas();
        if (!CommonUtils.isNotificationEnabled(BiochemicalReportActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "生化检查报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);

        listView = (ListView) findViewById(R.id.listView);

        layout_none = (LinearLayout) findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);


    }


    public void initEvent() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isNetWork) {
                    showNetWorkErrorDialog();
                    return;
                }
                Intent i = new Intent(BiochemicalReportActivity.this, BiochemistryReportDetailActivity.class);
                i.putExtra("Type", mList.get(position).getOCRType());
                i.putExtra("RecordID", mList.get(position).getID());
                i.putExtra("TypeTitle", "生化");
                startActivity(i);
            }
        });
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            page = 1;
            isFirst = true;
            isReflash = true;
            isLoading = false;
            initDatas();
        }
    }

    public void initDatas() {
        dbUser = dao.findUserInfoById(usrid);
        requestMaker.OCRRecordInquiry(dbUser.getUserno(), "04", "02", page + "", 10 + "", new JsonAsyncTask_Info(BiochemicalReportActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("OCRRecordInquiry");
                    int code = Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                    if (layout_none != null && listView != null) {
                        if (isReflash) {
                            mList.clear();
                        }
                        if (code == 0) {
                            layout_none.setVisibility(View.GONE);
                            org.json.JSONArray arraySub =
                                    array.getJSONObject(0).getJSONArray("ResultContent");
                            for (int i = 0; i < arraySub.length(); i++) {
                                ResultContent rc = new ResultContent();
                                rc.setCreatedDate(arraySub.getJSONObject(i).getString("CreatedDate"));
                                rc.setID(arraySub.getJSONObject(i).getString("ID"));
                                rc.setOCRType(arraySub.getJSONObject(i).getString("OCRType"));
                                rc.setOCRTypeName(arraySub.getJSONObject(i).getString("OCRTypeName"));
                                rc.setRownumber(arraySub.getJSONObject(i).getString("rownumber"));
//                            rc.setFromto(arraySub.getJSONObject(i).getString("Fromto"));
                                mList.add(rc);
                            }
                            if (isFirst) {
                                mAdapter = new InspectionReportAdapter(BiochemicalReportActivity.this, mList);
                                listView.setAdapter(mAdapter);
                                isFirst = false;
                            }

                            if (isReflash) {
                                isReflash = false;
                                isFirst = false;
                                mAdapter.notifyDataSetChanged();
                                pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else if (isLoading) {
                                isLoading = false;
                                isFirst = false;
                                mAdapter.notifyDataSetChanged();
                                pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            }


                        } else if (code == 2 && isLoading) {
                            isLoading = false;
                            isFirst = false;
                            pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            Toast.makeText(BiochemicalReportActivity.this, "已经没有更多数据了",
                                    Toast.LENGTH_SHORT).show();
                            layout_none.setVisibility(View.GONE);
                        } else {
                            if (mList.size() > 0) {
                                layout_none.setVisibility(View.GONE);
                            } else {
                                layout_none.setVisibility(View.VISIBLE);
                                pulltorefreshlayout.setVisibility(View.GONE);
                            }
                            isFirst = false;
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


}
