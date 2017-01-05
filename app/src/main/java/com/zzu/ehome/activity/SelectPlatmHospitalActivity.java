package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectHospitalAdapter;
import com.zzu.ehome.adapter.SelectPlatmAdapter;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.OcrBean;
import com.zzu.ehome.bean.TitleInquiryBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.activity.PrivateDoctorFragment.QBYY;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectPlatmHospitalActivity extends NetBaseActivity {
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_ok;
    private int index = 0;

    private List<TitleInquiryBean> mHList=null;
    //请求单例
    private RequestMaker requestMaker;
    private SelectPlatmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_selecthospital);
        requestMaker = RequestMaker.getInstance();
        mHList=(List<TitleInquiryBean>)this.getIntent().getSerializableExtra("HosList");
        initViews();
        mAdapter=new SelectPlatmAdapter(SelectPlatmHospitalActivity.this, mHList);
        listView.setAdapter(mAdapter);
        listView.setSelection(0);
        initEvent();
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
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
                Intent intent = new Intent();
                index=mAdapter.getIndex();
                intent.putExtra("hospital", mHList.get(index).getValue());
                intent.putExtra("hospital_id", mHList.get(index).getCode());
                intent.putExtra("index", index);
                setResult(DoctorFragment.ADD_HOSPITAL, intent);
                finish();

            }
        });

    }
}
