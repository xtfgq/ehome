package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectItemAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.OcrTypeBean;
import com.zzu.ehome.bean.OcrTypeData;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectItemActivity extends NetBaseActivity {
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;

    private List<HospitalBean> mHList;
    //请求单例
    private RequestMaker requestMaker;
    String id,hosname;
    private Intent mIntent;
    private  SelectItemAdapter mAdapter;
    List<OcrTypeBean> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_select_item);
        requestMaker = RequestMaker.getInstance();
        mIntent=this.getIntent();
        hosname=mIntent.getStringExtra("HospitalName");
        id=mIntent.getStringExtra("HospitalID");
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
        requestMaker.OCRTypeInquiry(id,new JsonAsyncTask_Info(SelectItemActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("OCRTypeInquiry");

                    if (array.getJSONObject(0).has("MessageCode")) {

                    }else{
                        OcrTypeData date = JsonTools.getData(result.toString(), OcrTypeData.class);
                        list = date.getData();
                        mAdapter=new SelectItemAdapter(SelectItemActivity.this, list);
                        listView.setAdapter(mAdapter);
                    }


                    stopProgressDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));

    }


    public void initEvent() {

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index=mAdapter.getIndex();

                ImageOCRSelectorActivity.start(SelectItemActivity.this, 1, ImageOCRSelectorActivity.MODE_MULTIPLE, true,
                        hosname,id,list.get(index).getCode(),list.get(index).getValue(),true, false, 0);
                finish();

            }
        });

    }
}
