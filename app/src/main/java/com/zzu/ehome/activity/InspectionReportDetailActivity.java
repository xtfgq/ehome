package com.zzu.ehome.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.BaseListAdapter;
import com.zzu.ehome.adapter.OcrDetailAdapter;
import com.zzu.ehome.bean.AdviceBean;
import com.zzu.ehome.bean.BloodRoutine;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.attr.position;

/**
 * Created by Mersens on 2016/8/23.
 */
public class InspectionReportDetailActivity extends BaseActivity {
    private RequestMaker requestMaker;
    private ListView listView,listView2;
    private String title,type,id,tvtitle;
    private EHomeDao dao;
    private User dbUser;
    private String usrid;
    private OcrDetailAdapter mAdapter;
    private List<BloodRoutine> mList;
    private TextView textViewtitle;
    private List<AdviceBean> mList2;
    private LinearLayout llad,lladnone;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_inspection_report_detail);

        tvtitle=this.getIntent().getStringExtra("TypeTitle");
        type=this.getIntent().getStringExtra("Type");
        id=this.getIntent().getStringExtra("RecordID");
        mList2=new ArrayList<>();
        dao = new EHomeDaoImpl(this);
        usrid = SharePreferenceUtil.getInstance(InspectionReportDetailActivity.this).getUserId();
        initViews();
        initEvent();
        initDatas();

    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

        listView=(ListView)findViewById(R.id.lilstView);
        listView2=(ListView)findViewById(R.id.lilstView2);

        textViewtitle=(TextView)findViewById(R.id.tvtitle);
        textViewtitle.setText(tvtitle);
        llad=(LinearLayout)findViewById(R.id.llad);
        lladnone=(LinearLayout)findViewById(R.id.lladnone);
    }


    public void initEvent() {

        requestMaker=RequestMaker.getInstance();

    }


    public void initDatas() {
        dbUser=dao.findUserInfoById(usrid);
        mList=new ArrayList<>();
        requestMaker.BloodRoutineInquiry(type, dbUser.getUserno(), id, new JsonAsyncTask_Info(InspectionReportDetailActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {

                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("BloodRoutineInquiry");
                    int code = Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                    if(listView!=null&&listView2!=null&&lladnone!=null){
                    if (code == 0) {
                        org.json.JSONArray arraySub =
                                array.getJSONObject(0).getJSONArray("ResultContent");
                        java.text.DecimalFormat df = new java.text.DecimalFormat("######0.00");
                        String title = arraySub.getJSONObject(0).getString("MonitorTime");
                        title=DateUtils.StringPattern(title, "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd");
                        setHeadTitle(title);

                        for (int i = 0; i < arraySub.length(); i++) {
                            BloodRoutine br = new BloodRoutine();
                            br.setCHK_ItemName_Z(arraySub.getJSONObject(i).getString("CHK_ItemName_Z"));
                            br.setItemRange(arraySub.getJSONObject(i).getString("ItemRange"));
                            br.setItemUnit(arraySub.getJSONObject(i).getString("ItemUnit"));
                            br.setMonitorTime(arraySub.getJSONObject(i).getString("MonitorTime"));
                            double value = Double.valueOf(arraySub.getJSONObject(i).getString("ItemValue"));
                            br.setItemValue(df.format(value));
                            mList.add(br);
                            if (!TextUtils.isEmpty(arraySub.getJSONObject(i).getString("Advice"))) {
                                AdviceBean ab = new AdviceBean();
                                ab.setAdvice(arraySub.getJSONObject(i).getString("Advice"));
                                ab.setDescription(arraySub.getJSONObject(i).getString("Description"));
                                mList2.add(ab);
                            }

//                            ab.setAdvice(arraySub.getJSONObject(i).getString("ItemRange"));

                        }

                        mAdapter = new OcrDetailAdapter(InspectionReportDetailActivity.this, mList);
                        listView.setAdapter(mAdapter);
                        if (mList2.size() > 0) {
                            llad.setVisibility(View.VISIBLE);
                            lladnone.setVisibility(View.GONE);
                            AdviceDetailAdapter mAadpter2 = new AdviceDetailAdapter(InspectionReportDetailActivity.this, mList2);
                            listView2.setAdapter(mAadpter2);

                        } else {
                            llad.setVisibility(View.GONE);
                            lladnone.setVisibility(View.VISIBLE);
                        }
                    }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }
    public class AdviceDetailAdapter extends BaseListAdapter<AdviceBean> {
        private List<AdviceBean> list;



        public AdviceDetailAdapter(Context context, List<AdviceBean> objects) {
            super(context, objects);
            this.list = objects;

        }

        @Override
        public View getGqView(int position, View convertView, ViewGroup parent) {
            View mView = getInflater().inflate(R.layout.item_advice, null);
            TextView name = (TextView) mView.findViewById(R.id.tv_content);
            TextView tvDescription=(TextView)mView.findViewById(R.id.tvDescription);
            TextView tvnum=(TextView)mView.findViewById(R.id.tvnum);
            tvnum.setText("  "+(position+1)+".");
            tvDescription.setText(list.get(position).getDescription());
            name.setText(list.get(position).getAdvice());
            return mView;
        }
    }
}
