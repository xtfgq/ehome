package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzu.ehome.R;

import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/11/9.
 */

public class WebConfigAct extends BaseActivity {
    private Intent mIntent;
    private TextView tv_hosptial;
    private TextView tv_sj;
    private TextView tv_ks;
    private Button btn_ok;
    private RequestMaker requestMaker;
    private String doctor_id,PatientId,DateStr,TimeSpanStr,PerTime;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker=RequestMaker.getInstance();
        setContentView(R.layout.layout_qryyxx);
        mIntent=this.getIntent();
        initViews();


    }
    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "确定预约信息", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tv_hosptial=(TextView)findViewById(R.id.tv_hosptial);
        tv_sj=(TextView)findViewById(R.id.tv_sj);
        tv_ks=(TextView)findViewById(R.id.tv_ks);
        btn_ok=(Button)findViewById(R.id.btn_ok);
        tv_hosptial.setText(mIntent.getStringExtra("Hos"));
        tv_ks.setText(mIntent.getStringExtra("Dep"));
        tv_sj.setText(mIntent.getStringExtra("Time"));
        doctor_id=mIntent.getStringExtra("doctor_id");
        PatientId=mIntent.getStringExtra("PatientId");
        DateStr=mIntent.getStringExtra("DateStr");
        TimeSpanStr=mIntent.getStringExtra("TimeSpanStr");
        PerTime=mIntent.getStringExtra("PerTime");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgressDialog();
                requestMaker.TreatmentInsert(doctor_id, PatientId, DateStr, TimeSpanStr, PerTime, new JsonAsyncTask_Info(WebConfigAct.this, true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {
                        JSONObject mySO = (JSONObject) result;
                        try {
                            JSONArray array = mySO.getJSONArray("TreatmentInsert");
                            JSONObject jsonObject = (JSONObject) array.get(0);
                            String code = jsonObject.getString("MessageCode");
                            stopProgressDialog();
                            if ("0".equals(code)) {
                                startActivity(new Intent(WebConfigAct.this,AddYuyueAct.class));
                            }else {
                                ToastUtils.showMessage(WebConfigAct.this, jsonObject.getString("MessageContent"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            stopProgressDialog();
                        }

                    }
                }));
            }
        });


    }
}