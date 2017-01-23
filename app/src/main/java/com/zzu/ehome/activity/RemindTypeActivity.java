package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.RemindTypeAdapter;
import com.zzu.ehome.adapter.RepeatAdapter;
import com.zzu.ehome.bean.RepatBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.crop.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/18.
 */
public class RemindTypeActivity extends BaseActivity {
    private String[] type = {"用药提醒", "测量提醒"};
    private RemindTypeAdapter mAdapter;
    private ListView listView;
    private int pos=-1;
    List<RepatBean> mList = new ArrayList<RepatBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_remind_type);
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(RemindTypeActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "提醒类型", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                if(pos!=-1) {
                    Intent intent = new Intent();
                    intent.putExtra("Type", type[pos]);
                    setResult(AddRemindActivity.ADD_TYPE, intent);
                    finish();
                }else{
                    finishActivity();
                }
            }
        });
        listView = (ListView) findViewById(R.id.listView);
    }


    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (RepatBean bean : mList) {
                    bean.setSelct(false);
                }
                pos = position;
                mList.get(position).setSelct(true);
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("Type", type[pos]);
                setResult(AddRemindActivity.ADD_TYPE, intent);
                finish();
            }
        });
    }


    public void initDatas() {

        for (int i = 0; i < type.length; i++) {

            RepatBean item = new RepatBean();
            item.setNaem(type[i]);
            item.setSelct(false);
            mList.add(item);
        }
        mAdapter = new RemindTypeAdapter(RemindTypeActivity.this, mList);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(pos!=-1) {
                Intent intent = new Intent();
                intent.putExtra("Type", type[pos]);
                setResult(AddRemindActivity.ADD_TYPE, intent);
                finish();
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
