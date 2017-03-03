package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.YuYueAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.HospitalInquiryByTopmd;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.zzu.ehome.db.DBHelper.mContext;

/**
 * Created by Mersens on 2016/8/9.
 */
public class OrdinaryYuYueActivity extends BaseActivity {
    private YuYueAdapter adapter;
    private ListView listView;
    private RequestMaker requestMaker;
    private List<HospitalBean> mList;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ordinary_yy);
        requestMaker = RequestMaker.getInstance();
        initViews();
        if(!isNetWork){
           showNetWorkErrorDialog();
            return;
        }
        initDatas();
        initEvent();
        if(!CommonUtils.isNotificationEnabled(OrdinaryYuYueActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "预约挂号", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });


    }

    public void initEvent() {

listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isNetWork){
           showNetWorkErrorDialog();
            return;
        }
        Intent i = new Intent(OrdinaryYuYueActivity.this, OfficeListActivity.class);
        i.putExtra("id", mList.get(position).getHospital_Id());
        i.putExtra("hosName", mList.get(position).getHospital_FullName());
      startActivity(i);
    }
});
    }

    public void initDatas() {
        requestMaker.HospitalInquiryByTopmd(
                new JsonAsyncTask_Info(OrdinaryYuYueActivity.this, true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {

                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("HospitalInquiryByTopmd");
                            if (array.getJSONObject(0).has("MessageCode")) {
//                                Toast.makeText(OrdinaryYuYueActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                        Toast.LENGTH_SHORT).show();
                            } else {
                                HospitalInquiryByTopmd date = JsonTools.getData(result.toString(), HospitalInquiryByTopmd.class);
                                mList = date.getDate();
                                adapter = new YuYueAdapter(OrdinaryYuYueActivity.this, mList);
                                listView.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Exception e) {

                    }
                }
                ));


    }
}
