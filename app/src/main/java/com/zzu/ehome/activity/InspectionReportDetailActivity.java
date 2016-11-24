package com.zzu.ehome.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.OcrDetailAdapter;
import com.zzu.ehome.bean.BloodRoutine;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.crop.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/23.
 */
public class InspectionReportDetailActivity extends BaseActivity {
    private RequestMaker requestMaker;
    private ListView listView;
    private String title,type,id;
    private EHomeDao dao;
    private User dbUser;
    private String usrid;
    private OcrDetailAdapter mAdapter;
    private List<BloodRoutine> mList;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_inspection_report_detail);
        title=this.getIntent().getStringExtra("Title");
        type=this.getIntent().getStringExtra("Type");
        id=this.getIntent().getStringExtra("RecordID");
        dao = new EHomeDaoImpl(this);
        usrid = SharePreferenceUtil.getInstance(InspectionReportDetailActivity.this).getUserId();
        initViews();
        initEvent();
        initDatas();

    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        listView=(ListView)findViewById(R.id.lilstView);
    }


    public void initEvent() {

        requestMaker=RequestMaker.getInstance();

    }


    public void initDatas() {
        dbUser=dao.findUserInfoById(usrid);
        mList=new ArrayList<>();
        requestMaker.BloodRoutineInquiry(type, dbUser.getUserno(), id, new JsonAsyncTask_Info(InspectionReportDetailActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    Log.e("tag00",result.toString());
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("BloodRoutineInquiry");
                    int code = Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                    if (code == 0) {
                        org.json.JSONArray arraySub =
                                array.getJSONObject(0).getJSONArray("ResultContent");
                        for (int i = 0; i < arraySub.length(); i++) {
                            BloodRoutine br = new BloodRoutine();
                          br.setCHK_ItemName_Z(arraySub.getJSONObject(i).getString("CHK_ItemName_Z"));
                            br.setItemRange(arraySub.getJSONObject(i).getString("ItemRange"));
                            br.setItemUnit(arraySub.getJSONObject(i).getString("ItemUnit"));
                            br.setMonitorTime(arraySub.getJSONObject(i).getString("MonitorTime"));
                            br.setItemValue(arraySub.getJSONObject(i).getString("ItemValue"));
                            mList.add(br);

                        }

                        mAdapter=new OcrDetailAdapter(InspectionReportDetailActivity.this,mList);
                        listView.setAdapter(mAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));
    }
}
