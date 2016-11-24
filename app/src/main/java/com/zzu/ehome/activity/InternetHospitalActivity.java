package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ECGAdapter;
import com.zzu.ehome.adapter.NetHospitalAreaAdapter;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

/**
 * Created by dell on 2016/6/17.
 */
public class InternetHospitalActivity extends BaseActivity {
    private ListView listview;
    private ECGAdapter adapter;
    List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

    private RequestMaker requestMaker;
    private String tag="",title="互联网医院分布";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        setContentView(R.layout.activity_net_hospital);
        if(this.getIntent()!=null){
            tag=getIntent().getStringExtra("Tag");

        }
        if(!TextUtils.isEmpty(tag)){
          title="门诊位置";
        }
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
        listview = (ListView) findViewById(R.id.listview);
    }

    public void initEvent() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mList.get(position).get("Name").toString();
                String Id = mList.get(position).get("ID").toString();
                int type=Integer.valueOf(mList.get(position).get("HaveChild").toString());
                if(type==0){
                    Intent intent = new Intent(InternetHospitalActivity.this, NetHospitalAreaActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("Id", Id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else{
                    Intent intent = new Intent(InternetHospitalActivity.this, InternetParentHospitalActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("Id", Id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }


            }
        });
    }

    public void initDatas() {
        requestMaker.RegionInquiry("0",new JsonAsyncTask_Info(InternetHospitalActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("RegionInquiry");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode")) {

                    } else {
                        Map<String, Object> map;
                        for (int i = 0; i < array.length(); i++) {
                            map = new HashMap<String, Object>();
                            map.put("Name",
                                    array.getJSONObject(i).getString("Name"));
                            map.put("ID",
                                    array.getJSONObject(i).getString("ID"));
                            map.put("HaveChild",
                                    array.getJSONObject(i).getString("HaveChild"));

                            mList.add(map);
                        }
                        adapter = new ECGAdapter(InternetHospitalActivity.this, mList);
                        listview.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));


//        mList=new ArrayList<String>();
//        mList.add("郑州市");
//        mList.add("西平县");
//        mList.add("禹州市");
//        adapter=new ECGAdapter(this,mList);
//        listview.setAdapter(adapter);

    }

}
