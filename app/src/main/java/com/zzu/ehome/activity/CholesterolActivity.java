package com.zzu.ehome.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.CholChatAdapter;
import com.zzu.ehome.adapter.UaChatAdapter;
import com.zzu.ehome.bean.CholDate;
import com.zzu.ehome.bean.CholHistoryBean;
import com.zzu.ehome.bean.CholHistoryDate;
import com.zzu.ehome.bean.CholRes;
import com.zzu.ehome.bean.LithicAcidDate;
import com.zzu.ehome.bean.LithicAcidRes;
import com.zzu.ehome.bean.UaBean;
import com.zzu.ehome.bean.UaDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.CholestenoneView;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.UaView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xclcharts.chart.PointD;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/25.
 */

public class CholesterolActivity extends BaseActivity implements StickyListHeadersListView.OnHeaderClickListener, StickyListHeadersListView.OnLoadingMoreLinstener {
    private RequestMaker requestMaker;
    private LinearLayout heardchat;
    private String userid, cardNo;
    private int page;
    private EHomeDao dao;
    private User dbUser;
    private LayoutInflater inflater;
    private CholestenoneView mChart;
    private LinearLayout llua;
    private TextView tvnodata;
    private StickyListHeadersListView listview;
    private RelativeLayout moredata;
    private View progressBarView;
    private CholChatAdapter mAadpter;
    private TextView progressBarTextView;

    private List<CholHistoryBean> mList=new ArrayList<>();
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        setContentView(R.layout.activity_cholestenone_layout);
        initViews();
        userid = SharePreferenceUtil.getInstance(CholesterolActivity.this).getUserId();
        dao = new EHomeDaoImpl(this);
        dbUser = dao.findUserInfoById(userid);
        cardNo = dbUser.getUserno();
        page = 1;
        initEvents();
    }

    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "胆固醇", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        inflater = LayoutInflater.from(this);
        mAadpter = new CholChatAdapter(CholesterolActivity.this);
        heardchat = (LinearLayout) inflater.inflate(R.layout.layout_cholestenone_new_chat, null);
        mChart = (CholestenoneView) heardchat.findViewById(R.id.chart);
        llua = (LinearLayout) heardchat.findViewById(R.id.llca);
        tvnodata = (TextView) heardchat.findViewById(R.id.tvnodate);
        moredata = (RelativeLayout) inflater.inflate(R.layout.moredata, null);
        progressBarView = (View) moredata.findViewById(R.id.loadmore_foot_progressbar);
        progressBarTextView = (TextView) moredata.findViewById(R.id.loadmore_foot_text);
        loadingAnimation = (AnimationDrawable) progressBarView.getBackground();
        listview = (StickyListHeadersListView) findViewById(R.id.lv_ua);
        listview.addHeaderView(heardchat);
        listview.addFooterView(moredata);

        listview.setOnHeaderClickListener(this);
        listview.setLoadingMoreListener(this);
        listview.setAdapter(mAadpter);
    }

    private void initEvents() {
        getTop(7);
        getHistory();
    }


    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void OnLoadingMore() {
        progressBarView.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);
        loadingAnimation.start();
        page++;
        if (!isLoading) {
            isLoading = true;
            getHistory();
        }

    }
    /*
    统计图表数据
     */
    private void getTop(int num){
        requestMaker.CholestenoneInquiry(cardNo, "", "", "T", num + "", new JsonAsyncTask_Info(CholesterolActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue = result.toString();
                    JSONArray array = mySO
                            .getJSONArray("CholestenoneInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {

                        tvnodata.setVisibility(View.VISIBLE);
                    } else {
                        tvnodata.setVisibility(View.GONE);
                        CholDate date = JsonTools.getData(result.toString(), CholDate.class);
                        List<CholRes> list = date.getData();
                        List<PointD> linePoint2 = new ArrayList<PointD>();

                        for (int i = 0; i < list.size(); i++) {

                            linePoint2.add(new PointD(Double.valueOf(i+1), Double.valueOf(list.get(i).getCHOL())));
                        }

                        mChart.setX(list.size());
                        mChart.refresh(linePoint2);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));
    }
    private void getHistory(){
        requestMaker.HealthDataInquirywWithPageType(userid, cardNo,10 + "", page + "", "Cholestenone", new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue = result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if (page == 1) {

//                        listview.setVisibility(View.GONE);
                        } else
                            loadingFinished();
                    } else {
                        listview.setVisibility(View.VISIBLE);
                        CholHistoryDate date = JsonTools.getData(result.toString(), CholHistoryDate.class);
                        List<CholHistoryBean> list = date.getData();
                        if (page == 1 && mList.size() > 0) {
                            mList.clear();
                        }
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i));
                            }
                            mAadpter.setList(mList);

                            loadingFinished();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));
    }
    public void loadingFinished() {

        if (null != loadingAnimation && loadingAnimation.isRunning()) {
            loadingAnimation.stop();
        }
        progressBarView.setVisibility(View.INVISIBLE);
        progressBarTextView.setVisibility(View.INVISIBLE);
        isLoading = false;
        mAadpter.notifyDataSetChanged();
    }
}
