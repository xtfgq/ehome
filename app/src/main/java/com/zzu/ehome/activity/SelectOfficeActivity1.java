package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.DepartmentBeanAdapter;
import com.zzu.ehome.bean.DepartmentBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.FlowLayout;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class SelectOfficeActivity1 extends BaseActivity{
    private String hospital_id;
    private RequestMaker requestMaker;
    private List<DepartmentBean> mList;
    private DepartmentBeanAdapter mAdapter;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_office1);
        requestMaker = RequestMaker.getInstance();
        hospital_id = getIntent().getStringExtra("hospital_id");
        initViews();
        initDates();
        initEvent();

    }
    public void initEvent() {

    }
    public void initViews() {
        mListView=(ListView)findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "选择科室", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("department", mList.get(position).getDepartment_FullName());
                intent.putExtra("department_id", mList.get(position).getDepartment_Id());
                setResult(MyDoctorActivity.ADD_OFFICE, intent);
                finish();
            }
        });
    }
    public void initDates() {
        startProgressDialog();
        requestMaker.DepartmentInquiry(hospital_id, new JsonAsyncTask_Info(SelectOfficeActivity1.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("DepartmentInquiry");
                    mList = new ArrayList<DepartmentBean>();
                    stopProgressDialog();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        mList.add(JsonTools.getData(jsonObject.toString(), DepartmentBean.class));
                    }
                    mAdapter=new DepartmentBeanAdapter(SelectOfficeActivity1.this,mList);
                    mListView.setAdapter(mAdapter);
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
