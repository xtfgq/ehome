package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SexActivity extends BaseSimpleActivity implements View.OnClickListener {
    private RequestMaker requestMaker;
    private String parentId;
    private ImageView ivman, ivwoman;
    private ImageView iv_back;
    private TextView tvGo;
    private String tag="";
    private String userid;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        setContentView(R.layout.activity_relation);
        setContentView(R.layout.activity_sex);
        requestMaker = RequestMaker.getInstance();
        if (this.getIntent() != null) {
            if (this.getIntent().getStringExtra("relation") != null) {
                tag = this.getIntent().getStringExtra("relation");
                parentId = SharePreferenceUtil.getInstance(SexActivity.this).getPARENTID();

            }
        }
            userid = SharePreferenceUtil.getInstance(SexActivity.this).getUserId();

        initViews();

        initEvent();
        if (!CommonUtils.isNotificationEnabled(SexActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    private void initEvent() {
        ivman.setOnClickListener(this);
        ivwoman.setOnClickListener(this);
        tvGo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initViews() {
        ivman = (ImageView) findViewById(R.id.iv_man);
        ivwoman = (ImageView) findViewById(R.id.iv_woman);
        tvGo = (TextView) findViewById(R.id.tvGo);
        iv_back=(ImageView)findViewById(R.id.iv_back);
    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if (!CommonUtils.isNotificationEnabled(SexActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
        switch (v.getId()) {
            case R.id.iv_man:
                if(TextUtils.isEmpty(tag)) {
                    doSex("01",userid);

                }else{
                    doSex("01",parentId);

                }

                break;
            case R.id.iv_woman:
                if(TextUtils.isEmpty(tag)) {
                    doSex("02",userid);
                }else{
                    doSex("02",parentId);
                }
                break;
            case R.id.tvGo:
                doGoHeightAndWeight();
                break;
            case R.id.iv_back:
               finish();
                break;
        }

    }

    private void doSex(String str,final String id) {
        startProgressDialog();
        requestMaker.userInfo(id, "", str, "", "", "", new JsonAsyncTask_Info(SexActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                try {
                    stopProgressDialog();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("UserInfoChange");
                    stopProgressDialog();
                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        if(TextUtils.isEmpty(tag)) {
                            startActivity(new Intent(SexActivity.this, WeightAndHeightAct.class));
                        }else{
                            Intent i=new Intent(SexActivity.this,WeightAndHeightAct.class);
                            i.putExtra("relation", "rela");
                            startActivity(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));
    }

    private void doGoHeightAndWeight() {
        if(TextUtils.isEmpty(tag)) {
            startActivity(new Intent(SexActivity.this, WeightAndHeightAct.class));
        }else{
            Intent i=new Intent(SexActivity.this,WeightAndHeightAct.class);
            i.putExtra("relation", "rela");
            startActivity(i);
        }

    }

}
