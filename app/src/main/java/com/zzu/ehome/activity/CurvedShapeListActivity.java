package com.zzu.ehome.activity;


import android.os.Bundle;


import android.widget.ListView;


import com.zzu.ehome.R;
import com.zzu.ehome.adapter.CurvedShapeListAdapter;
import com.zzu.ehome.bean.CurvedPart;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/8/9.
 */
public class CurvedShapeListActivity extends BaseActivity  {

    private ListView listView;
    private List<CurvedPart> mCurvedPart;
    private CurvedShapeListAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);


        setContentView(R.layout.layout_curvershaped_list);
        mCurvedPart=new ArrayList<CurvedPart>();
        initViews();

        initEvent();
        if(!CommonUtils.isNotificationEnabled(CurvedShapeListActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }


    private void initEvent() {

        CurvedPart cpBase = new CurvedPart();
        cpBase.setType(0);
        cpBase.setName("基本特征");
        mCurvedPart.add(cpBase);
        CurvedPart cpChildjb = new CurvedPart();
        cpChildjb.setType(1);
        cpChildjb.setName("体重");
        mCurvedPart.add(cpChildjb);
        CurvedPart cpChildjb1 = new CurvedPart();
        cpChildjb1.setType(1);
        cpChildjb1.setName("血压");
        mCurvedPart.add(cpChildjb1);
        CurvedPart cpChildjb2 = new CurvedPart();
        cpChildjb2.setType(1);
        cpChildjb2.setName("心率");
        mCurvedPart.add(cpChildjb2);
        CurvedPart cpBaseNormal = new CurvedPart();
        cpBaseNormal.setType(0);
        cpBaseNormal.setName("血常规");
        mCurvedPart.add(cpBaseNormal);
        CurvedPart cpBaseNormalxc = new CurvedPart();
        cpBaseNormalxc.setType(1);
        cpBaseNormalxc.setName("红细胞");
        mCurvedPart.add(cpBaseNormalxc);
        CurvedPart cpBaseNormalxc1 = new CurvedPart();
        cpBaseNormalxc1.setType(1);
        cpBaseNormalxc1.setName("白细胞");
        mCurvedPart.add(cpBaseNormalxc1);
        CurvedPart cpBaseNormalxc2 = new CurvedPart();
        cpBaseNormalxc2.setType(1);
        cpBaseNormalxc2.setName("中性粒细胞");
        mCurvedPart.add(cpBaseNormalxc2);
        CurvedPart cpBaseNormalxc3 = new CurvedPart();
        cpBaseNormalxc3.setType(1);
        cpBaseNormalxc3.setName("淋巴细胞");
        mCurvedPart.add(cpBaseNormalxc3);
        CurvedPart cpBaseBloodSuggar = new CurvedPart();
        cpBaseBloodSuggar.setType(0);
        cpBaseBloodSuggar.setName("血糖");
        mCurvedPart.add(cpBaseBloodSuggar);
        CurvedPart cpBaseBloodSuggarxt = new CurvedPart();
        cpBaseBloodSuggarxt.setType(1);
        cpBaseBloodSuggarxt.setName("血清葡萄糖");
        mCurvedPart.add(cpBaseBloodSuggarxt);
        adapter = new CurvedShapeListAdapter(CurvedShapeListActivity.this, mCurvedPart);
        listView.setAdapter(adapter);
    }

    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "健康曲线", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        listView = (ListView) findViewById(R.id.listView);

    }

}
