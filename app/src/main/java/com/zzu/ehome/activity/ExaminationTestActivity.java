package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ExaminationTestAadpter;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.utils.SharePreferenceUtil;
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
                        User dbUser = getDao().findUserInfoById(SharePreferenceUtil.getInstance(ExaminationTestActivity.this).getUserId());
                        if(dbUser.getUserno()==null|| TextUtils.isEmpty(dbUser.getUserno())){
                            startActivity(new Intent(ExaminationTestActivity.this,PersonalCenterInfo.class));
                            return;
                        }
                        startActivity(new Intent(ExaminationTestActivity.this, SmartWebView.class));
                        break;
                    case 1:
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




}
