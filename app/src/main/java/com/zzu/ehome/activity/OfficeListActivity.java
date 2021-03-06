package com.zzu.ehome.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.OfficeListAdapter;
import com.zzu.ehome.bean.DepDateTemp;
import com.zzu.ehome.bean.DepTempBean;
import com.zzu.ehome.bean.DepartmentPart;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.refreshLayout;

/**
 * Created by Mersens on 2016/8/9.
 */
public class OfficeListActivity extends BaseActivity {
    private ListView listView;
    private OfficeListAdapter adapter;
    private RequestMaker requestMaker;
    private String sid = "", title;

    private List<DepTempBean> depNOlist = new ArrayList<DepTempBean>();
    private List<DepTempBean> depPartList = new ArrayList<DepTempBean>();
    private List<DepartmentPart> depAllList = new ArrayList<DepartmentPart>();
    private RefreshLayout refreshLayout;
    private boolean isRefresh=false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_office_list);
        requestMaker = RequestMaker.getInstance();
        title = this.getIntent().getStringExtra("hosName");
        initViews();
        sid = this.getIntent().getStringExtra("id");
        initEvent();

        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }

        initDatas();
        if(!CommonUtils.isNotificationEnabled(OfficeListActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        refreshLayout=(RefreshLayout) findViewById(R.id.refreshLayout);
        listView = (ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }

    public void initEvent() {
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {

                    if(!isNetWork){
                        refreshLayout.refreshFinish(RefreshLayout.FAIL);
                        return;
                    }

                depNOlist.clear();
                depPartList.clear();
                isRefresh=true;
                getSub(sid);
            }
        });
    }

    private void getSub(String id) {
        startProgressDialog();
        requestMaker.HospitalDepertByTopmd(id, "All", new JsonAsyncTask_Info(
                OfficeListActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String rs = result.toString();

                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("HospitalDepertByTopmd");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(OfficeListActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                        stopProgressDialog();
                        show("暂无数据");
                    } else {
                        stopProgressDialog();
                        DepDateTemp date = JsonTools.getData(result.toString(), DepDateTemp.class);
                        List<DepTempBean> mList = date.getData();

                        for (DepTempBean bean : mList) {
                            if (bean.getParentDeprtID().equals("0")) {
                                depNOlist.add(bean);
                            } else {
                                depPartList.add(bean);
                            }

                        }
                        for (int m = 0; m < depNOlist.size(); m++) {
                            DepartmentPart dp = new DepartmentPart();
                            dp.setDepartmentID(depNOlist.get(m).getDepartmentID());
                            dp.setDepartmentName(depNOlist.get(m).getDepartmentName());
                            dp.setType(0);
                            depAllList.add(dp);
                            Boolean isHas = false;

                            for (int n = 0; n < depPartList.size(); n++) {

                                if (depNOlist.get(m).getDepartmentID().equals(depPartList.get(n).getParentDeprtID())) {
                                    DepartmentPart dpsub = new DepartmentPart();
                                    dpsub.setDepartmentID(depPartList.get(n).getDepartmentID());
                                    dpsub.setDepartmentName(depPartList.get(n).getDepartmentName());
                                    dpsub.setType(1);
                                    isHas = true;
                                    depAllList.add(dpsub);

                                } else if (depNOlist.get(m).getParentDeprtID().equals("0")) {
                                    if ((n == depPartList.size() - 1) && (!isHas))
                                        depAllList.remove(depAllList.size() - 1);
                                }
                            }
                            adapter = new OfficeListAdapter(OfficeListActivity.this, depAllList, sid);
                            listView.setAdapter(adapter);
                            if(depAllList.size()==0){
                                show("暂无数据");
                            }
                        }

                    }
                } catch (Exception e) {
                    stopProgressDialog();
                    show("暂无数据");
                    e.printStackTrace();
                }
                finally {
                    if(isRefresh){
                        refreshLayout.refreshFinish(RefreshLayout.SUCCEED);
                        isRefresh=false;
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    public void initDatas() {

        getSub(sid);


    }

}
