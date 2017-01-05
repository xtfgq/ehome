package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ExaminationTestAadpter;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/6.
 */

public class ExaminationTestActivity extends BaseActivity{
    private ListView listView;
    private ExaminationTestAadpter mAdapter;
    private List<String> mList;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_examination_test);
        initViews();
        if(!CommonUtils.isNotificationEnabled(ExaminationTestActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "轻体检", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

        mList=new ArrayList<String>();
        mList.add("自助体检报告");
        mList.add("血常规检查报告");
//        mList.add("生化检查体检报告");
        mAdapter = new ExaminationTestAadpter(ExaminationTestActivity.this, mList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                       if(!isNetWork){
                            showNetWorkErrorDialog();
                            return;
                        }
                        User dbUser = getDao().findUserInfoById(SharePreferenceUtil.getInstance(ExaminationTestActivity.this).getUserId());
                        if(dbUser.getUserno()==null|| TextUtils.isEmpty(dbUser.getUserno())){
                            completeInfoTips();
                            return;
                        }
                        startActivity(new Intent(ExaminationTestActivity.this, SmartWebView.class));
                        break;
                    case 1:
                       if(!isNetWork){
                            showNetWorkErrorDialog();
                            return;
                        }
                        User dbUser2 = getDao().findUserInfoById(SharePreferenceUtil.getInstance(ExaminationTestActivity.this).getUserId());
                        if(dbUser2.getUserno()==null|| TextUtils.isEmpty(dbUser2.getUserno())){
                            completeInfoTips();
                            return;
                        }
                        startActivity(new Intent(ExaminationTestActivity.this, InspectionReportActivity.class));

                        break;
//                    case 2:
//                        startActivity(new Intent(ExaminationTestActivity.this, BiochemicalReportActivity.class));
//
//                        break;

                }
            }
        });
    }
    /**
     * 如果用户信息不完善，显示提示框
     */
    public void completeInfoTips() {
        DialogTips dialog = new DialogTips(this, "", "用户信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(ExaminationTestActivity.this, PersonalCenterInfo.class));
            }
        });

        dialog.show();
        dialog = null;
    }




}
