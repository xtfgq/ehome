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

public class PingguTestActivity extends BaseActivity{
    private ListView listView;
    private ExaminationTestAadpter mAdapter;
    private List<String> mList;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_examination_test);
        initViews();
        if(!CommonUtils.isNotificationEnabled(PingguTestActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "健康预测", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

        mList=new ArrayList<String>();
        mList.add("高血压患病风险评估");
        mList.add("糖尿病患病风险评估");
        mAdapter = new ExaminationTestAadpter(PingguTestActivity.this, mList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent i=new Intent(PingguTestActivity.this,MyPingguActivity.class);
                        i.putExtra("url","file:///android_asset/Hypertension.html");
                        i.putExtra("title","高血压患病风险评估");
                        startActivity(i);
                        break;
                    case 1:
                        Intent i2=new Intent(PingguTestActivity.this,MyPingguActivity.class);
                        i2.putExtra("url","file:///android_asset/Diabetes.html");
                        i2.putExtra("title","糖尿病患病风险评估");
                        startActivity(i2);
                        break;
                }

            }
        });
    }





}
