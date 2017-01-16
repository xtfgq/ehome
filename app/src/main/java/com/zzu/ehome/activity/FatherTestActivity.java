package com.zzu.ehome.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import static com.zzu.ehome.R.id.layout_search;

/**
 * Created by Administrator on 2017/1/3.
 */

public class FatherTestActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout layout_search;
    private RelativeLayout layout_add;
   private String userid;
    private EHomeDao dao;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.report_new);
        initViews();
        initEvents();
        dao = new EHomeDaoImpl(FatherTestActivity.this);
        if(!CommonUtils.isNotificationEnabled(FatherTestActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "体检报告",
                new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {

                        finishActivity();
                    }
                });
        layout_search=(RelativeLayout)findViewById(R.id.layout_search);
        layout_add=(RelativeLayout)findViewById(R.id.layout_add);
    }
    public void initEvents(){
        layout_search.setOnClickListener(this);
        layout_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_search:
                if(checkCardNo()){
                    startActivity(new Intent(FatherTestActivity.this,PlatformTestActivity.class));
                };

                break;
            case R.id.layout_add:
                startActivity(new Intent(FatherTestActivity.this,ExaminationReportActivity.class));
                break;
        }

    }
    private Boolean  checkCardNo(){
        userid= SharePreferenceUtil.getInstance(FatherTestActivity.this).getUserId();
        User dbUser=dao.findUserInfoById(userid);
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
        DialogTips dialog = new DialogTips(FatherTestActivity.this, "", "用户信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(FatherTestActivity.this, PersonalCenterInfo.class));
            }
        });

        dialog.show();
        dialog = null;
    }
}
