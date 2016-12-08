package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.zzu.ehome.R;
import com.zzu.ehome.adapter.InspectionReportAdapter;
import com.zzu.ehome.bean.ResultContent;

import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.DateUtils;

import com.zzu.ehome.utils.SharePreferenceUtil;

import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class BiochemicalReportActivity extends BaseActivity{
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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_biochemical_report);
        dao = new EHomeDaoImpl(this);
        usrid = SharePreferenceUtil.getInstance(BiochemicalReportActivity.this).getUserId();
        initViews();

        initEvent();
        initDatas();
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
                Intent i = new Intent(BiochemicalReportActivity.this, InspectionReportDetailActivity.class);
                i.putExtra("Type", mList.get(position).getOCRType());
                i.putExtra("RecordID", mList.get(position).getID());
                i.putExtra("Title", DateUtils.StringPattern(mList.get(position).getCreatedDate(), "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd"));
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
        if(!isFirst){
            page = 1;
            isFirst = true;
            isReflash = true;
            isLoading = false;
            mList.clear();
            initDatas();
        }
    }

    public void initDatas() {



    }



}
