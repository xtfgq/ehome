package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserInfoDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.wheel.wheelview.OnWheelScrollListener;
import com.zzu.ehome.view.wheel.wheelview.WheelView;
import com.zzu.ehome.view.wheel.wheelview.adapter.NumericWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.zzu.ehome.R.id.weight;

/**
 * Created by Administrator on 2016/8/8.
 */
public class WeightAndHeightAct extends BaseSimpleActivity implements View.OnClickListener {
    private RequestMaker requestMaker;
    private TextView tvGo;
    private String parentId;
    private WheelView mHeight;
    private WheelView mWeight;
    private int hei = 170, wei = 60;
    private Button btnext;
    private ImageView ivback;
    private EHomeDao dao;
    private String tag="";
    private String userid;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        setContentView(R.layout.activity_relation);
        setContentView(R.layout.activity_weight_height);
        requestMaker = RequestMaker.getInstance();
        dao = new EHomeDaoImpl(this);
        if (this.getIntent() != null) {
            if (this.getIntent().getStringExtra("relation") != null) {
                tag = this.getIntent().getStringExtra("relation");
                parentId = SharePreferenceUtil.getInstance(WeightAndHeightAct.this).getPARENTID();

            }
        }
            userid=SharePreferenceUtil.getInstance(WeightAndHeightAct.this).getUserId();

        initViews();

        initEvent();
        if (!CommonUtils.isNotificationEnabled(WeightAndHeightAct.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        switch (v.getId()) {
            case R.id.btnext:
                if(TextUtils.isEmpty(tag)) {
                    doWeight(userid);
                }else{
                    doWeight(parentId);
                }
                CustomApplcation.getInstance().isRead=false;
                Intent intenthealth1 = new Intent("userrefresh");
                sendBroadcast(intenthealth1);

                break;
            case R.id.tvGo:
                if(TextUtils.isEmpty(tag)) {
                    startActivity(new Intent(WeightAndHeightAct.this, RegisterFinishAct.class));
                }else{
                    startActivity(new Intent(WeightAndHeightAct.this, AddSuccussAct.class));

                }
                CustomApplcation.getInstance().isRead=false;
                Intent intenthealth = new Intent("userrefresh");
                sendBroadcast(intenthealth);


                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    private void initEvent() {

        tvGo.setOnClickListener(this);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(this, 40, 250, "%d");
        numericWheelAdapter3.setLabel("");
        mHeight.setViewAdapter(numericWheelAdapter3);
        mHeight.setCyclic(true);
        mHeight.addScrollingListener(scrollListener);
        mHeight.setCurrentItem(130);

        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(this, 10, 150, "%d");
        numericWheelAdapter4.setLabel("");
        mWeight.setViewAdapter(numericWheelAdapter4);
        mWeight.setCyclic(true);
        mWeight.addScrollingListener(scrollListener);
        mWeight.setCurrentItem(40);
        btnext.setOnClickListener(this);

    }

    private void initViews() {

        tvGo = (TextView) findViewById(R.id.tvGo);
        mHeight = (WheelView) findViewById(R.id.height);
        mWeight = (WheelView) findViewById(weight);
        btnext = (Button) findViewById(R.id.btnext);
        ivback = (ImageView) findViewById(R.id.iv_back);
        ivback.setOnClickListener(this);
        mHeight.setVisibleItems(7);
        mWeight.setVisibleItems(7);

    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            hei = mHeight.getCurrentItem()+40;
            wei = mWeight.getCurrentItem()+10;

        }
    };

    private void doInquery(final String id) {
        requestMaker.UserInquiry(id, new JsonAsyncTask_Info(WeightAndHeightAct.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                stopProgressDialog();
                UserInfoDate date = JsonTools.getData(result.toString(), UserInfoDate.class);
                List<User> list = date.getData();
                User user = list.get(0);
                String imgHead = user.getImgHead();

                if (imgHead != null) {
                    if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                        imgHead = "";
                    } else {
                        imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");
                    }
                } else {
                    imgHead = "";
                }
                User dbUser = dao.findUserInfoById(id);
                dbUser.setImgHead(imgHead);
                dbUser.setUserHeight(user.getUserHeight());
                dbUser.setUsername(user.getUsername());
                dbUser.setAge(user.getAge());
                dbUser.setUserno(user.getUserno());
                dbUser.setSex(user.getSex());
                dao.updateUserInfo(dbUser, id);

                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity1.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(RegisterActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(RelationActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(SecondActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(SexActivity.class);
                if(TextUtils.isEmpty(tag)) {
                    startActivity(new Intent(WeightAndHeightAct.this, RegisterFinishAct.class));
                }else{
                    startActivity(new Intent(WeightAndHeightAct.this, AddSuccussAct.class));

                }

                finishActivity();

            }

            @Override
            public void onError(Exception e) {

            }

        }));
    }
    private void doWeight(final String id){
        startProgressDialog();
        SharePreferenceUtil.getInstance(WeightAndHeightAct.this).setWeight(wei + "");
        requestMaker.userInfoWeight(id, hei + "", wei + "", new JsonAsyncTask_Info(WeightAndHeightAct.this, true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("UserInfoChange");

                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        doInquery(id);
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
