package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.RepeatAdapter;
import com.zzu.ehome.bean.RepatBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.view.HeadView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/18.
 */
public class RepeatActivity extends BaseActivity {
    private ListView listView;
    private RepeatAdapter mAdapter;

    private String [] week={"周一","周二","周三","周四","周五","周六","周日"};
    List<RepatBean> mList = new ArrayList<RepatBean>();
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_repeat);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "重复", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("WeekList", (Serializable) mList);
                intent.setClass(RepeatActivity.this, AddRemindActivity.class);
                intent.putExtras(bundle);
                setResult(AddRemindActivity.ADD_WEEK, intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mList.get(position).getSelct()){
                    mList.get(position).setSelct(false);
                }else{
                    mList.get(position).setSelct(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    public void initEvent() {



    }

    public void initDatas() {
        for(int i=0;i<week.length;i++){
            RepatBean item=new RepatBean();
            item.setNaem(week[i]);
            item.setSelct(false);
            mList.add(item);
        }
        mAdapter=new RepeatAdapter(RepeatActivity.this,mList);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("WeekList", (Serializable) mList);
            intent.setClass(RepeatActivity.this, AddRemindActivity.class);
            intent.putExtras(bundle);
            setResult(AddRemindActivity.ADD_WEEK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
