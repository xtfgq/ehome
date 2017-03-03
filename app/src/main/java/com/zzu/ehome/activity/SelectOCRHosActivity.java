package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectHospitalAdapter;
import com.zzu.ehome.adapter.SelectOcrhosAdater;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.TitleInquiryBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.parent;
import static com.zzu.ehome.R.id.view;
import static com.zzu.ehome.activity.PrivateDoctorFragment.QBYY;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectOCRHosActivity extends BaseActivity {
    private ListView listView;

    private int index = 0;
    private List<HospitalBean> mHList=new ArrayList<HospitalBean>();
    //请求单例
    private RequestMaker requestMaker;
    private List<TitleInquiryBean> yyList = new ArrayList<TitleInquiryBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_selectocr);
        requestMaker = RequestMaker.getInstance();
        initViews();
        initData();
//        initEvent();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "选择类别", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
            finishActivity();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectOCRHosActivity.this,SelectItemActivity.class);
                intent.putExtra("HospitalName", mHList.get(position).getHospital_FullName());
                intent.putExtra("HospitalID", mHList.get(position).getHospital_Id());
                startActivity(intent);

            }
        });

    }

    public void initData() {

        requestMaker.HospitalInquiryPMD(new JsonAsyncTask_Info(SelectOCRHosActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("HospitalInquiryPMD");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = (JSONObject) array.get(i);
                            HospitalBean bean = new HospitalBean();
                            bean.setHospital_Id(jsonObject.getString("HospitalID"));
                            bean.setHospital_FullName(jsonObject.getString("HospitalName"));
                            if (Integer.valueOf(jsonObject.getString("IsOCR")) == 1) {
                                mHList.add(bean);
                            }
                        }
                        listView.setAdapter(new SelectOcrhosAdater(SelectOCRHosActivity.this, mHList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }

        }));


    }


//    public void initEvent() {
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                index = position;
//            }
//        });
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//
//            }
//        });
//        tv_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageOCRSelectorActivity.start(SelectOCRHosActivity.this, 1, ImageOCRSelectorActivity.MODE_MULTIPLE, true,
//                        mHList.get(index).getHospital_FullName(),true, false, 0);
////                ImageSelectorActivity.start(SelectOCRHosActivity.this, 1, ImageSelectorActivity.MODE_MULTIPLE, true,
////                        false, false, 0);
//                finish();
//
//            }
//        });
//
//    }
}
