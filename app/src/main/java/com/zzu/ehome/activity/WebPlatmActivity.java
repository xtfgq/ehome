package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.fragment.WebPlatmFramet;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.DialogUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PlatmDialogEnsureCancelView;

import org.json.JSONArray;
import org.json.JSONObject;




/**
 * Created by Administrator on 2017/1/4.
 */

public class WebPlatmActivity extends BaseActivity implements View.OnClickListener{
    private int selectColor;
    private int unSelectColor;
    private TextView tv_zongjian,tv_tijian;
    private ImageView iv_zongjian,iv_tijian;
    private RelativeLayout layout_zongjian,layout_tijian;
    private WebPlatmFramet mWebPlatmFramet;
    private Intent mIntent;
    private String tag,name,CHKCODE,UserNo,time,hosid,hosname,userid;
    private Fragment[] fragments;
    private int index;
    private int currentIndex;
    private RequestMaker requestMaker;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_fragmet_tijian);
        mIntent=this.getIntent();
        if(mIntent!=null){
            tag=mIntent.getStringExtra("flag");
            name=mIntent.getStringExtra("name");
            CHKCODE=mIntent.getStringExtra("code");
            UserNo=mIntent.getStringExtra("UserNo");
            time=mIntent.getStringExtra("time");

            hosname=mIntent.getStringExtra("hosname");
        }
        userid= SharePreferenceUtil.getInstance(WebPlatmActivity.this).getUserId();
        requestMaker=RequestMaker.getInstance();

        initViews();
        if(!CommonUtils.isNotificationEnabled(WebPlatmActivity.this)){
            showTitleDialog("请打开通知中心");
        }
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        initEvnets();
    }
    private void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, DateUtils.StringPattern(time, "yyyy/MM/dd HH:mm:ss", "yyyy年MM月")+"体检报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                save();
            }
        });
        selectColor=getResources().getColor(R.color.bottom_text_color_pressed);
        unSelectColor=getResources().getColor(R.color.bottom_text_color_normal);
        tv_zongjian=(TextView)findViewById(R.id.tv_zongjian);
        tv_tijian=(TextView)findViewById(R.id.tv_tijian);
        iv_zongjian=(ImageView)findViewById(R.id.iv_zongjian);
        iv_tijian=(ImageView) findViewById(R.id.iv_tijian);
        layout_zongjian=(RelativeLayout)findViewById(R.id.layout_zongjian);
        layout_tijian=(RelativeLayout)findViewById(R.id.layout_tijian);
        fragments=new Fragment[2];

        fragments[0]= WebPlatmFramet.getInstance(Constants.EhomeURL+"/WebServices/HealthTest.aspx?"+"Name="+name+"&CHKCODE="+CHKCODE+"&UserNo="+UserNo);
        fragments[1]= WebPlatmFramet.getInstance(Constants.EhomeURL+"/WebServices/HealthTestDetail.aspx?"+"CHKCODE="+CHKCODE);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragments[0]).commit();
        resetImgs();
        iv_zongjian.setImageResource(R.mipmap.icon_zjbg);
        tv_zongjian.setTextColor(selectColor);
    }
    private void initEvnets(){
        layout_zongjian.setOnClickListener(this);
        layout_tijian.setOnClickListener(this);
    }

    private void resetImgs() {
        tv_zongjian.setTextColor(unSelectColor);
        tv_tijian.setTextColor(unSelectColor);
        iv_zongjian.setImageResource(R.mipmap.icon_zjbg2);
        iv_tijian.setImageResource(R.mipmap.icon_tjzb2);
    }
    private void setTab(int i) {
        resetImgs();
        switch (i){
            case 0:
                iv_zongjian.setImageResource(R.mipmap.icon_zjbg);
                tv_zongjian.setTextColor(selectColor);
                index=0;
                addFragment();
                break;
            case 1:
                iv_tijian.setImageResource(R.mipmap.icon_tjzb);
                tv_tijian.setTextColor(selectColor);
                index=1;
                addFragment();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_zongjian:
                setTab(0);
                break;
            case R.id.layout_tijian:
                setTab(1);
                break;

        }
    }

    private void save(){
        if(tag.equals("add")) {
            PlatmDialogEnsureCancelView dialogEnsureCancelView = new PlatmDialogEnsureCancelView(
                    WebPlatmActivity.this).setDialogMsg("", "请对报告进行存档保存", "保存")
                    .setOnClickListenerEnsure(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            saveInfo();
                        }
                    }).setOnClickListenerCancle(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finishActivity();
                        }
                    });
            DialogUtils.showSelfDialog(WebPlatmActivity.this, dialogEnsureCancelView);
        }else{
            CustomApplcation.getInstance().finishSingleActivityByClass(SmartSearchActivity.class);
            finishActivity();
        }
    }
    private void saveInfo(){
        requestMaker.CheckupInfoInsert(CHKCODE,UserNo,name,time,hosname,userid,new JsonAsyncTask_Info(WebPlatmActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("CheckupInfoInsert");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        int flag=Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                        if(flag==0) {
                            RxBus.getInstance().send(new EventType("smart"));
                            CustomApplcation.getInstance().finishSingleActivityByClass(SmartSearchActivity.class);
                            finishActivity();
                        }else{
                            ToastUtils.showMessage(WebPlatmActivity.this, array.getJSONObject(0).getString("MessageContent"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    public void addFragment() {
        if(currentIndex!=index){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fragments[currentIndex]);
            if(!fragments[index].isAdded()){
                ft.add(R.id.fragment_container, fragments[index]);
            }
            ft.show(fragments[index]).commit();
        }
        currentIndex=index;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            save();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
