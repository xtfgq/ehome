package com.zzu.ehome.activity;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;

import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.RelationDes;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.edRelation;
import static com.zzu.ehome.R.id.tv_hosptial;
import static com.zzu.ehome.activity.AppointmentActivity.hosptial;


/**
 * Created by Administrator on 2016/8/6.
 */
public class RelationActivity extends BaseSimpleActivity implements View.OnClickListener {
    private ImageView ivback;
    private Button btnNext;
    private Intent mIntent;
    private String userid, parentId;
    private RequestMaker requestMaker;
    private TextView edRelation;
    private LinearLayout lldot;
    String tag = "";
    private EHomeDao dao;
    private Boolean isSame=false;
private String code,value;
    public static final int ADD_RELATION= 0x11;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_relation);
        requestMaker = RequestMaker.getInstance();
        mIntent = this.getIntent();
        initViews();
        if (mIntent != null) {
            if (mIntent.getStringExtra("relation") != null) {
                tag = mIntent.getStringExtra("relation");
                lldot.setVisibility(View.VISIBLE);

            }
        } else {
            lldot.setVisibility(View.VISIBLE);
        }
        dao=new EHomeDaoImpl(this);
        userid = SharePreferenceUtil.getInstance(RelationActivity.this).getHomeId();
        parentId = SharePreferenceUtil.getInstance(RelationActivity.this).getPARENTID();


    }

    private void initViews() {
        ivback = (ImageView) findViewById(R.id.iv_back);
        btnNext = (Button) findViewById(R.id.btnext);
        edRelation = (TextView) findViewById(R.id.edRelation);
        lldot = (LinearLayout) findViewById(R.id.lldot);
        if (TextUtils.isEmpty(tag)) {
            lldot.setVisibility(View.VISIBLE);
        } else {
            lldot.setVisibility(View.GONE);
        }
        ivback.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        edRelation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if (!CommonUtils.isNotificationEnabled(RelationActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
        switch (v.getId()) {
            case R.id.iv_back:

                CustomApplcation.getInstance().finishSingleActivityByClass(RegisterActivity.class);
                finish();
                break;
            case R.id.btnext:
                if (TextUtils.isEmpty(edRelation.getText().toString().trim())||edRelation.getText().toString().trim().equals("请选择您和被添加人的关系")) {
                    ToastUtils.showMessage(RelationActivity.this, "请填写用户关系!");
                    return;
                }
                startProgressDialog();
                requestMaker.UserRelationshipInsert(userid, code, parentId, new JsonAsyncTask_Info(RelationActivity.this, true, new JsonAsyncTaskOnComplete() {

                    @Override
                    public void processJsonObject(Object result) {
                        if (result != null) {
                            stopProgressDialog();
                            try {
                                JSONObject mySO = (JSONObject) result;
                                JSONArray array = mySO
                                        .getJSONArray("UserRelationshipInsert");
                                if (array.getJSONObject(0).getString("MessageCode").toString().equals("0")) {
                                    if (!TextUtils.isEmpty(tag)) {
                                        startActivity(new Intent(RelationActivity.this, AddSuccussAct.class));
                                    } else {
                                        Intent i=new Intent(RelationActivity.this,SecondActivity.class);
                                        i.putExtra("relation", "rela");
                                        startActivity(i);
                                    }
                                    User dbUser=dao.findUserInfoById(userid);
                                    String orders=dbUser.getOrder();
                                    if(!TextUtils.isEmpty(orders)) {
                                        String[] orderstr = orders.split(",");

                                        for (int n = 0; n < orderstr.length; n++) {

                                            if (orderstr[n].equals(parentId)) {
                                                isSame = true;
                                                break;
                                            }
                                        }

                                    }
                                    if(!isSame){
                                        if(TextUtils.isEmpty(orders)){
                                            orders=parentId;
                                        }else {
                                            orders += "," + parentId;
                                        }
                                        dbUser.setOrder(orders);
                                        dao.updateUserInfo(dbUser,userid);
                                        CustomApplcation.getInstance().isRead=false;
                                        Intent intenthealth = new Intent("userrefresh");
                                        sendBroadcast(intenthealth);
                                    }




                                } else if (Integer.valueOf(array.getJSONObject(0).getString("MessageCode"))==1){
                                    ToastUtils.showMessage(RelationActivity.this, array.getJSONObject(0).getString("MessageContent").toString());
                                    Intent i=new Intent(RelationActivity.this, MainActivity.class);
                                    i.putExtra("Home","Home");
                                    startActivity(i);
                                    finishActivity();
                                }
                                else {
                                    ToastUtils.showMessage(RelationActivity.this, array.getJSONObject(0).getString("MessageContent").toString());
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }));


                break;
            case R.id.edRelation:


                Intent intent1=new Intent(RelationActivity.this,SelectRelationActivity.class);
                startActivityForResult(intent1, ADD_RELATION);
                break;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomApplcation.getInstance().finishSingleActivityByClass(RegisterActivity.class);
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RELATION && resultCode == ADD_RELATION && data != null) {
            code = data.getStringExtra("code");
            value = data.getStringExtra("value");
            if (!TextUtils.isEmpty(value)) {
                edRelation.setText(value);
            }
        }
    }

}
