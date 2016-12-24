package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MSDoctorDetailBean;
import com.zzu.ehome.bean.MSDoctorDetailInquiry;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static com.zzu.ehome.R.id.lrtop;

/**
 * Created by Mersens on 2016/8/16.
 */
public class DoctorDetialActivity extends BaseActivity implements View.OnClickListener {
    private ImageView icon_back, icon_share;
    private LinearLayout layout_ljyy, layout_mfzx;
    private String doctorid = "";
    private RequestMaker requestMaker;
    private ImageView icon_user;
    private TextView tvtitle;
    private TextView tv_hosname;
    private TextView tvfavors, tvjianjie;
    private LinearLayout lldes;
    private ImageView ivmore;
    private TextView tv_title_sc;
    private TextView tv_title;
    private TextView tv_diagnoseCount;
    private TextView tv_sign;
    private int index = 0;
    private TextView tv_apply;
    private String title;
    private TextView tvnotie,tv_oltime,tvdes;
    private String headurl;
    private EHomeDaoImpl dao;
    private RelativeLayout rltop;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        setContentView(R.layout.activity_doctor_detial);
        doctorid = this.getIntent().getStringExtra("doctorid");
        title=this.getIntent().getStringExtra("doctorname");
        initViews();
        tv_title.setText(title);
        initEvent();
        dao = new EHomeDaoImpl(this);
    }

    public void initViews() {
        icon_back = (ImageView) findViewById(R.id.icon_back);
        icon_share = (ImageView) findViewById(R.id.icon_share);
        layout_ljyy = (LinearLayout) findViewById(R.id.layout_ljyy);
        layout_mfzx = (LinearLayout) findViewById(R.id.layout_mfzx);
        icon_user = (ImageView) findViewById(R.id.icon_user);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_hosname = (TextView) findViewById(R.id.tv_hosname);
        tvfavors = (TextView) findViewById(R.id.tvfavors);
        tvjianjie = (TextView) findViewById(R.id.tvjianjie);
        lldes = (LinearLayout) findViewById(R.id.lldes);
        ivmore = (ImageView) findViewById(R.id.ivmore);
        tvdes=(TextView)findViewById(R.id.tvdes);
        tv_title_sc = (TextView) findViewById(R.id.tv_title_sc);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_diagnoseCount = (TextView) findViewById(R.id.tv_diagnoseCount);
        tv_apply=(TextView)findViewById(R.id.tv_apply);
        tvnotie=(TextView)findViewById(R.id.tvnotie);
        tv_oltime=(TextView)findViewById(R.id.tv_oltime);
        rltop=(RelativeLayout)findViewById(R.id.rltop);
        ViewGroup.LayoutParams para;
        para =  rltop.getLayoutParams();
        para.width = ScreenUtils.getScreenWidth(DoctorDetialActivity.this);
        para.height = para.width*20/75;
        rltop.setLayoutParams(para);
    }

    public void initEvent() {
        icon_back.setOnClickListener(this);
        icon_share.setOnClickListener(this);
        layout_ljyy.setOnClickListener(this);
        layout_mfzx.setOnClickListener(this);
        lldes.setOnClickListener(this);
        tvnotie.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    public void initDatas() {
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId())) {
            noUsrid();
        } else {
            serchDoc();
        }

    }

    /**
     * 没有登录状态查询详情
     */
    private void noUsrid(){
        startProgressDialog();
        requestMaker.MSDoctorDetailInquiry(doctorid, new JsonAsyncTask_Info(DoctorDetialActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    stopProgressDialog();
                    JSONObject mySO = (JSONObject) result;

                    JSONArray array = mySO.getJSONArray("MSDoctorDetailInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(DoctorDetialActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        MSDoctorDetailInquiry date = JsonTools.getData(result.toString(), MSDoctorDetailInquiry.class);
                        List<MSDoctorDetailBean> list = date.getData();
                        MSDoctorDetailBean bean = list.get(0);
                        headurl=Constants.EhomeURL + bean.getImageURL().replace("~", "").replace("\\", "/");
                        Glide.with(DoctorDetialActivity.this)
                                .load(headurl)
                                .centerCrop().error(R.drawable.icon_doctor)
                                .into(icon_user);
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(doctorid,title , Uri.parse(headurl)));
                        tvtitle.setText(bean.getGoodAt() + "" + bean.getTitle());
                        tv_hosname.setText(bean.getHospitalName());
                        tvfavors.setText("签约量：" + bean.getSignCount());
                        tvjianjie.setText(bean.getDescription());
                        tv_title_sc.setText(bean.getSpeciaty());
                        tv_diagnoseCount.setText("问诊量：" + bean.getDiagnoseCount());


                        String strApplyTo="";
                        if (bean.getApplyTo().indexOf(",") >= 0) {
                            String str[] = bean.getApplyTo().split(",");

                            for (int i = 0; i < str.length; i++) {
                                strApplyTo += str[i] + "\n";
                            }
                        }else{
                            strApplyTo=bean.getApplyTo();
                        }

                        tv_oltime.setText(bean.getDoctorOLexplain());
                        tv_apply.setText(strApplyTo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));
    }

    /**
     * 登录状态下详情
     */
    private void serchDoc(){
        startProgressDialog();
        requestMaker.MSDoctorDetailInquiry(doctorid, SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId(), new JsonAsyncTask_Info(DoctorDetialActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    stopProgressDialog();
                    JSONObject mySO = (JSONObject) result;

                    JSONArray array = mySO.getJSONArray("MSDoctorDetailInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(DoctorDetialActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {
                        MSDoctorDetailInquiry date = JsonTools.getData(result.toString(), MSDoctorDetailInquiry.class);
                        List<MSDoctorDetailBean> list = date.getData();
                        MSDoctorDetailBean bean = list.get(0);

                        headurl=Constants.EhomeURL + bean.getImageURL().replace("~", "").replace("\\", "/");
//                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                            @Override
//                            public UserInfo getUserInfo(String s) {
//                                return new UserInfo(doctorid, title, Uri.parse(headurl));
//                            }
//                        }, true);

                        Glide.with(DoctorDetialActivity.this)
                                .load(headurl)
                                .centerCrop().error(R.drawable.icon_doctor)
                                .into(icon_user);
                        tvtitle.setText(bean.getGoodAt() + "" + bean.getTitle());
                        tv_hosname.setText(bean.getHospitalName());
                        tvfavors.setText("签约量：" + bean.getSignCount());
                        tvjianjie.setText(bean.getDescription());
                        tv_title_sc.setText(bean.getSpeciaty());
                        tv_diagnoseCount.setText("问诊量：" + bean.getDiagnoseCount());
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(doctorid,title , Uri.parse(headurl)));
                        if (Integer.valueOf(bean.getIsSign()) == 0) {
                            tv_sign.setText("立即关注");
                        } else {
                            tv_sign.setText("取消关注");
                        }
                        String strApplyTo="";
                        if (bean.getApplyTo().indexOf(",") >= 0) {
                            String str[] = bean.getApplyTo().split(",");
                            for (int i = 0; i < str.length; i++) {
                                strApplyTo += str[i] + "\n";
                            }
                        }else{
                            strApplyTo=bean.getApplyTo();
                        }

                        tv_oltime.setText(bean.getDoctorOLexplain());
                        tv_apply.setText(strApplyTo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_share:
                ToastUtils.showMessage(this, "点击分享");
                break;
            case R.id.icon_back:
                finishActivity();
                break;
            case R.id.layout_ljyy:

                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId())) {
                    startActivity(new Intent(DoctorDetialActivity.this, LoginActivity1.class));
                } else {
                    tv_sign.setEnabled(false);
                    if (CommonUtils.isFastClick()) {
                        return;
                    }
                    if (tv_sign.getText().toString().contains("立即")) {
                        requestMaker.MSDoctorSignInsert(doctorid, SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId(), new JsonAsyncTask_Info(DoctorDetialActivity.this, true, new JsonAsyncTaskOnComplete() {
                            @Override
                            public void processJsonObject(Object result) {
                                try {
                                    JSONObject mySO = (JSONObject) result;

                                    tv_sign.setText("已经关注");
                                    tv_sign.setEnabled(true);
                                    serchDoc();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }));
                    }else if(tv_sign.getText().toString().contains("取消")){
                        requestMaker.MSDoctorSignDelete(doctorid, SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId(), new JsonAsyncTask_Info(DoctorDetialActivity.this, true, new JsonAsyncTaskOnComplete() {
                            @Override
                            public void processJsonObject(Object result) {
                                try {
                                    JSONObject mySO = (JSONObject) result;

                                    tv_sign.setText("立即关注");
                                    tv_sign.setEnabled(true);
                                    serchDoc();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }));

                    }

                }

                break;
            case R.id.layout_mfzx:

                if (tv_sign.getText().toString().contains("立即")) {
                    ToastUtils.showMessage(this, "请先关注该医生");
                    return;
                }
//                if(CommonUtils.isFastClick()){
//                    return;
//                }

                if (RongIM.getInstance() != null) {
                    requestMaker.MSDoctorDiagnoseInsert(doctorid, SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId(), new JsonAsyncTask_Info(DoctorDetialActivity.this, true, new JsonAsyncTaskOnComplete() {
                        @Override
                        public void processJsonObject(Object result) {
                            try {
                                JSONObject mySO = (JSONObject) result;
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }));
                    startProgressDialog();
                    requestMaker.MSDoctorConsultationTime(doctorid,new JsonAsyncTask_Info(DoctorDetialActivity.this, true, new JsonAsyncTaskOnComplete() {
                        @Override
                        public void processJsonObject(Object result) {
                            try {
                                stopProgressDialog();
                                JSONObject mySO = (JSONObject) result;
                                JSONArray array = mySO.getJSONArray("MSDoctorConsultationTime");
//                                {"MSDoctorConsultationTime":[{"MessageCode":"3","MessageContent":"当前时间医生在线，可以咨询！"}]}
                                if(Integer.valueOf(array.getJSONObject(0).getString("MessageCode"))==3){


                                    User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId());

                                    if (TextUtils.isEmpty(dbUser.getUserno())) {
                                        completeInfoTips();
                                        return;
                                    }
                                    RongIM.getInstance().startPrivateChat(DoctorDetialActivity.this, doctorid, title);
                                }else {
                                    confirmConversation(array.getJSONObject(0).getString("MessageContent").toString());
                                }

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }));




                }

                break;
            case R.id.lldes:
                if (index % 2 == 0) {
                    tvjianjie.setMaxLines(100);
                    turnToUp(ivmore);
                    tvdes.setText("收起");
                } else {
                    tvjianjie.setMaxLines(2);
                    turnToDown(ivmore);
                    tvdes.setText("查看详情");
                }
                index++;
                break;
            case R.id.tvnotie:
                startActivity(new Intent(DoctorDetialActivity.this, MyRemindActivity1.class));
                break;
        }

    }

    private void turnToUp(ImageView iv) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_arr_g_down)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(180f);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }

    private void turnToDown(ImageView iv) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_arr_g_down)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(360f);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }
    public void confirmConversation(String str) {
        DialogTips dialog = new DialogTips(DoctorDetialActivity.this, "", str,
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId());
                if(TextUtils.isEmpty(dbUser.getAge())){
                    startActivity(new Intent(DoctorDetialActivity.this,PersonalCenterInfo.class));
                    return;
                }
                RongIM.getInstance().startPrivateChat(DoctorDetialActivity.this, doctorid, title);

            }
        });

        dialog.show();
        dialog = null;
    }
    private Boolean  checkCardNo(){
        User dbUser=dao.findUserInfoById(SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId());
        if (TextUtils.isEmpty(dbUser.getUserno())) {
            completeInfoTips();
            return false;
        }else{
            return true;
        }
    }
    /**
     * 如果用户信息不完善，显示提示框
     */
    public void completeInfoTips() {
        DialogTips dialog = new DialogTips(DoctorDetialActivity.this, "", "用户信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(DoctorDetialActivity.this, PersonalCenterInfo.class));
            }
        });

        dialog.show();
        dialog = null;
    }


}
