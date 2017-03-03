package com.zzu.ehome.activity;

import android.content.DialogInterface;
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
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/23.
 * 检查报告
 */
public class InspectionReportActivity extends BaseActivity {
    private ListView listView;
    private LinearLayout layout_none;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    private User dbUser;
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
        setContentView(R.layout.layout_inspection_report);
        dao = new EHomeDaoImpl(this);
        usrid = SharePreferenceUtil.getInstance(InspectionReportActivity.this).getUserId();
        initViews();
//        if(TextUtils.isEmpty(dao.findUserInfoById(usrid).getUserno()))
//        completeInfoTips();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(InspectionReportActivity.this)){
            showTitleDialog("请打开通知中心");

        }
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "血常规检查报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                    finishActivity();

            }
        });

//        setDefaultViewMethod(R.mipmap.icon_arrow_left, "检查报告", R.mipmap.icon_ocr,new HeadView.OnLeftClickListener() {
//            @Override
//            public void onClick() {
//                finishActivity();
//            }
//        }, new HeadView.OnRightClickListener() {
//            @Override
//            public void onClick() {
////                showTabs();
//                startActivity(new Intent(InspectionReportActivity.this,SelectOCRHosActivity.class));
//
//            }
//        });
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);

        listView = (ListView) findViewById(R.id.listView);

        layout_none = (LinearLayout) findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);


    }


    public void showTabs() {
        DialogTips dialog = new DialogTips(this, "","暂支持郑大五附院血常规报告",
                "确定",true,true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(InspectionReportActivity.this, UploadOcrPicActivity.class));
            }
        });


        dialog.show();
        dialog = null;
    }

    public void initEvent() {
        requestMaker = RequestMaker.getInstance();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                Intent i = new Intent(InspectionReportActivity.this, InspectionReportDetailActivity.class);
                i.putExtra("Type", mList.get(position).getOCRType());
                i.putExtra("RecordID", mList.get(position).getID());
                i.putExtra("TypeTitle","血常规");
//                i.putExtra("Title", DateUtils.StringPattern(mList.get(position).getCreatedDate(), "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd"));
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

            initDatas();
        }
    }

    public void initDatas() {
        dbUser = dao.findUserInfoById(usrid);
        requestMaker.OCRRecordInquiry(dbUser.getUserno(), "04","01",page + "", 10 + "", new JsonAsyncTask_Info(InspectionReportActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("OCRRecordInquiry");
                    int code = Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                    if (isReflash) {
                        mList.clear();
                    }
                    if(layout_none!=null&&listView!=null&&pulltorefreshlayout!=null) {
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
                                mAdapter = new InspectionReportAdapter(InspectionReportActivity.this, mList);
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
                            mAdapter.notifyDataSetChanged();
                            pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            Toast.makeText(InspectionReportActivity.this, "已经没有更多数据了",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (mList.size() > 0) {
                                layout_none.setVisibility(View.GONE);
                            } else {
                                layout_none.setVisibility(View.VISIBLE);
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

//        final List<String> mList = new ArrayList<>();
//        mList.add("血糖");
//        mList.add("血常规");
//        mList.add("尿常规");
//        mList.add("肝功能");
//        mList.add("肾功能");
//        mList.add("生化");
//        mList.add("血常规");
//
//        listView.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return mList.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return mList.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View v = LayoutInflater.from(InspectionReportActivity.this).inflate(R.layout.dynamic_item, null);
////                TextView tvname = (TextView) findViewById(R.id.tv_title);
////                tvname.setText(mList.get(position));
//                return v;
//            }
//        });

    }



}
