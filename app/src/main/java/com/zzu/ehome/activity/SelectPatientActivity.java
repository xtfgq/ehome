package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SelectPatientAdapter;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.DoctorSchemaByTopmdBean;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserContactor;
import com.zzu.ehome.bean.UserContactorData;
import com.zzu.ehome.bean.UserDate;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_ECGInfo;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class SelectPatientActivity extends BaseActivity {
    private RequestMaker requestMaker;
    private ListView listView;
    private SelectPatientAdapter adapter;

    private String userid,HospitalID,DepartmentID,DoctorID,DepartmentName;
    private DoctorSchemaByTopmdBean doctortime;
    public static final int ADD_PATIENT = 0x03;
    List<UserContactor> list;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor_list);
        requestMaker=RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(SelectPatientActivity.this).getUserId();
        Intent intent = this.getIntent();
        DepartmentID=intent.getStringExtra("DepartmentID");
        HospitalID=intent.getStringExtra("HospitalID");
        DoctorID=intent.getStringExtra("DoctorID");
        DepartmentName=intent.getStringExtra("DepartName");

        doctortime=(DoctorSchemaByTopmdBean)intent.getSerializableExtra("DoctorTime");
        initViews();
        initEvent();
        initDatas();
        if (!CommonUtils.isNotificationEnabled(SelectPatientActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "选择就诊人", "添加", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                Intent intent1 = new Intent(SelectPatientActivity.this, AddUserInfoActivity.class);
                startActivityForResult(intent1, ADD_PATIENT );

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
                Intent intent = new Intent();
                intent.setClass(SelectPatientActivity.this, ConfirmMsgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DoctorTime", doctortime);
                bundle.putString("HospitalID",HospitalID);
                bundle.putString("DepartmentID",DepartmentID);
                bundle.putString("DoctorID",DoctorID);
                bundle.putString("DepartName",DepartmentName);
                bundle.putString("UserContactorID",list.get(position).getUserContactorID());
                bundle.putString("UserName",list.get(position).getUserName());
                bundle.putString("UserNO",list.get(position).getUserNO());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    public void initDatas() {
        requestMaker.UserContactorInquiryByTopmd(userid, new JsonAsyncTask_Info(SelectPatientActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("UserContactorInquiryByTopmd");
                    if (array.getJSONObject(0).has("MessageCode")) {

                       if(Integer.valueOf(array.getJSONObject(0).getString("MessageCode"))==2) {
                           Add();
                       }

                    } else {
                        UserContactorData date = JsonTools.getData(result.toString(), UserContactorData.class);
                        list = date.getData();
                        adapter = new SelectPatientAdapter(SelectPatientActivity.this, list);
                        listView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_PATIENT && data != null) {
            initDatas();
        }
    }
    public void Add() {
       final DialogTips dialog = new DialogTips(SelectPatientActivity.this, "", "请添加就诊人",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {

                Intent intent1 = new Intent(SelectPatientActivity.this, AddUserInfoActivity.class);
                startActivityForResult(intent1, ADD_PATIENT );
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
