package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.CooperationPharmacyFragment;
import com.zzu.ehome.fragment.NearPharmacyFragment;
import com.zzu.ehome.fragment.WebPlatmFramet;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import static android.R.attr.type;


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
    private String tag;
    private Fragment[] fragments;
    private int index;
    private int currentIndex;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_fragmet_tijian);
        mIntent=this.getIntent();
        if(mIntent!=null){
            tag=mIntent.getStringExtra("flag");
        }
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
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "2016年4月体检报告", new HeadView.OnLeftClickListener() {
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
        fragments[0]= WebPlatmFramet.getInstance("http://news.baidu.com/");
        fragments[1]= WebPlatmFramet.getInstance("http://music.baidu.com/");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragments[0]).commit();
        resetImgs();
        iv_zongjian.setImageResource(R.mipmap.icon_zjbg);
        tv_zongjian.setTextColor(selectColor);
    }
    private void initEvnets(){
        layout_zongjian.setOnClickListener(this);
        layout_tijian.setOnClickListener(this);
    }
    private void initDatas(){


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
                iv_tijian.setImageResource(R.mipmap.icon_zjbg);
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
    @Override
    public void onBackPressed() {
      save();
    }
    private void save(){
        if(tag.equals("add")) {
            DialogTips dialog = new DialogTips(WebPlatmActivity.this, "请对报告进行存档保存",
                    "确定");
            dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int userId) {
                    ToastUtils.showMessage(WebPlatmActivity.this, "正在保存");
                    finishActivity();

                }
            });

            dialog.show();
            dialog = null;
        }else{
            finishActivity();
        }
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
}
