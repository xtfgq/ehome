package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectHospitalAdapter;
import com.zzu.ehome.adapter.SelectRelationAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.RelationshipInBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectRelationActivity extends NetBaseActivity {
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;
    private int index = 0;
    private List<RelationshipInBean> mHList;
    //请求单例
    private RequestMaker requestMaker;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_selectrelection);
        requestMaker = RequestMaker.getInstance();
        initViews();
        initData();
        initEvent();
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
    }

    public void initData() {
        startProgressDialog();
        requestMaker.RelationshipInquiry(new JsonAsyncTask_Info(SelectRelationActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("RelationshipInquiry");
                   mHList = new ArrayList<RelationshipInBean>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        RelationshipInBean bean=new RelationshipInBean();
                        bean.setCode(jsonObject.getString("Code"));
                        bean.setValue(jsonObject.getString("Value"));
                        mHList.add(bean);
                    }
                   listView.setAdapter(new SelectRelationAdapter(SelectRelationActivity.this, mHList));
                    stopProgressDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));

    }


    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("code", mHList.get(index).getCode());
                intent.putExtra("value", mHList.get(index).getValue());
                setResult(RelationActivity.ADD_RELATION, intent);
                finish();

            }
        });

    }
}
