package com.zzu.ehome.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.BaseListAdapter;
import com.zzu.ehome.bean.AdviceBean;
import com.zzu.ehome.bean.AdviceData;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class HealthInstructionActivity extends BaseActivity implements View.OnClickListener {
    private AdviceAdapter mAadpter;
    private List<AdviceBean> mList;
    private ListView listView;
    private RequestMaker requestMaker;
    private String cardNo, date;
    private LinearLayout layout_none;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_health_instruction);
        requestMaker = RequestMaker.getInstance();
        cardNo = getDao().findUserInfoById(SharePreferenceUtil.getInstance(HealthInstructionActivity.this).getUserId()).getUserno();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        date = sdf.format(new Date());
        initViews();
        if (!isNetWork) {
            showNetWorkErrorDialog();
            return;
        }
        initData();
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "健康指导", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        listView = (ListView) findViewById(R.id.lilstView);
        layout_none = (LinearLayout) findViewById(R.id.layout_none);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
    }

    private void initData() {
        requestMaker.HealthAdviceSearchByDate(cardNo, date, new JsonAsyncTask_Info(HealthInstructionActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("HealthAdviceSearchByDate");
                    if (layout_none != null && listView != null) {
                        if (array.getJSONObject(0).has("MessageCode")) {
                            layout_none.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);

                        } else {

                            AdviceData date = JsonTools.getData(result.toString(), AdviceData.class);
                            mList = date.getData();
                            mAadpter = new AdviceAdapter(HealthInstructionActivity.this, mList);
                            listView.setAdapter(mAadpter);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                startActivity(new Intent(HealthInstructionActivity.this, CurvedShapeListActivity.class));
                break;
        }
    }

    public class AdviceAdapter extends BaseListAdapter<AdviceBean> {
        private List<AdviceBean> list;


        public AdviceAdapter(Context context, List<AdviceBean> objects) {
            super(context, objects);
            this.list = objects;

        }

        @Override
        public View getGqView(int position, View convertView, ViewGroup parent) {
            View mView = getInflater().inflate(R.layout.item_new_instruction, null);
            TextView name = (TextView) mView.findViewById(R.id.tv_content);
            TextView tvDescription = (TextView) mView.findViewById(R.id.tvLast);
            TextView tvDate = (TextView) mView.findViewById(R.id.tvDate);
            TextView tvnum = (TextView) mView.findViewById(R.id.tvnum);
            tvnum.setText("  " + (position + 1) + ".");
            tvDescription.setText("体检结论:" + list.get(position).getDescription());
            name.setText(list.get(position).getAdvice());
            return mView;
        }
    }

}
