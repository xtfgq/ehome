package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.PlatformBean;
import com.zzu.ehome.bean.PlatformHosiptalBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Administrator on 2017/1/3.
 */

public class PlatformHospitalActivity extends BaseActivity {
    private ListView mListView;
    private LinearLayout layout_no_msg;
    private MyAdapter adapter = null;
    private List<PlatformHosiptalBean> list = new ArrayList<>();
    private RefreshLayout refreshLayout;

    private boolean isRefresh = false;
    private RequestMaker requestMaker;
    private EHomeDao dao;

    private Intent mIntent;
    private String tag, name, UserNo, hosname, userid;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.platform_test);
        requestMaker = RequestMaker.getInstance();
        mIntent = this.getIntent();

        if (mIntent != null) {
            name = mIntent.getStringExtra("name");
            UserNo = mIntent.getStringExtra("UserNo");
            hosname = mIntent.getStringExtra("hosptial");
        }

        userid = SharePreferenceUtil.getInstance(PlatformHospitalActivity.this).getUserId();

        initViews();
        if (!CommonUtils.isNotificationEnabled(PlatformHospitalActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
        if (!isNetWork) {
            showNetWorkErrorDialog();
            return;
        }
        initEvnets();
        initDatas();
    }

    public void initViews() {
        mListView = (ListView) findViewById(R.id.listView);
        layout_no_msg = (LinearLayout) findViewById(R.id.layout_no_msg);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "智能查询报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

    }

    private void initEvnets() {

        adapter = new MyAdapter(list);
        mListView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {
                if (!isNetWork) {
                    if (!isNetWork) {
                        refreshLayout.refreshFinish(RefreshLayout.FAIL);
                        return;
                    }
                    return;
                }
                isRefresh = true;
                list.clear();
                initDatas();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doSave(name, UserNo, hosname, list.get(position).getCHKCODE());
            }
        });
    }

    private void initDatas() {

        requestMaker.ZDWFYRecordInfoInquiry(UserNo, name,hosname, new JsonAsyncTask_Info(PlatformHospitalActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    stopProgressDialog();
                    JSONArray array = mySO.getJSONArray("ZDWFYRecordInfoInquiry");
                    if (layout_no_msg != null && mListView != null) {
                        if (array.getJSONObject(0).has("MessageCode")) {
                            layout_no_msg.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);

                        } else {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
                                list.add(getDataFromJson(json));
                            }
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            layout_no_msg.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (isRefresh) {
                        refreshLayout.refreshFinish(RefreshLayout.SUCCEED);
                        isRefresh = false;
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));

    }

    private PlatformHosiptalBean getDataFromJson(JSONObject json) throws JSONException {
        PlatformHosiptalBean bean = new PlatformHosiptalBean();
        bean.setTime(json.getString("RecordTime"));
        bean.setCHKCODE(json.getString("MedicalCode"));
        return bean;
    }

    class MyAdapter extends BaseAdapter {
        public void setList(List<PlatformHosiptalBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        private List<PlatformHosiptalBean> list;

        public MyAdapter(List<PlatformHosiptalBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(PlatformHospitalActivity.this).inflate(R.layout.platform_hospitaltest_item, null);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_time.setText(DateUtils.StringPattern(list.get(position).getTime(), "yyyy/MM/dd HH:mm:ss", "yyyy年MM月") + "体检报告");

            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView tv_time;
        public TextView tv_name;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 查询相关平台数据
     * MessageCode:1，失败，2查不到数据含有表示已经保存过
     * 3.表示已经存过了
     */

    private void doSave(final String name, final String userno, final String hosname, final String code) {
        requestMaker.ZDWFYUserRecordJudgeInquiry(code, userno, name, hosname, new JsonAsyncTask_Info(PlatformHospitalActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("ZDWFYUserRecordJudgeInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        int flag = Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                        if (flag == 3) {
                            Intent i = new Intent(PlatformHospitalActivity.this, WebPlatmActivity.class);
                            i.putExtra("flag", "info");
                            i.putExtra("name", name);
                            i.putExtra("UserNo", userno);
                            i.putExtra("code", code);
                            i.putExtra("time", array.getJSONObject(0).getString("RecordTime"));
                            i.putExtra("hosname", hosname);
                            startActivity(i);

                        } else {
                            show(array.getJSONObject(0).getString("MessageContent"));
                        }
                    } else {
                        Intent i = new Intent(PlatformHospitalActivity.this, WebPlatmActivity.class);
                        i.putExtra("flag", "add");
                        i.putExtra("name", name);
                        i.putExtra("code", code);
                        i.putExtra("UserNo", userno);
                        i.putExtra("time", array.getJSONObject(0).getString("RecordTime"));
                        i.putExtra("hosname", hosname);
                        startActivity(i);
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
}
