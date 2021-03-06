package com.zzu.ehome.fragment;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.MedicinalRecordAdapter;
import com.zzu.ehome.bean.MedicationDate;
import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/6/28.
 */
public class YYJLDataFragment extends BaseFragment implements StickyListHeadersListView.OnHeaderClickListener, AdapterView.OnItemClickListener, StickyListHeadersListView.OnLoadingMoreLinstener {
    private View view;
    private RequestMaker requestMaker;
    private StickyListHeadersListView listView;
    private String userid;
    private int page = 1;
    private MedicinalRecordAdapter mAdapter;

    private LinearLayout rlup;
    private ImageView ivupload;
    private RelativeLayout moredata;
    private LayoutInflater inflater;
    private View progressBarView;
    private TextView progressBarTextView;
    private boolean isLoading = false;
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private List<MedicationRecord> mlist = new ArrayList<MedicationRecord>();
    private SupperBaseActivity activity;
    private EHomeDao dao;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        view = inflater.inflate(R.layout.layout_yyjl_data, null);
        requestMaker = RequestMaker.getInstance();
        dao=new EHomeDaoImpl(getActivity());
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();

        initView();

        setListener();
//        initDate();
        return view;
    }

    private void setListener() {

    }

    private void initDate() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;

        }
        startProgressDialog();
        requestMaker.MedicationRecordInquiry(userid,dao.findUserInfoById(userid).getUserno(), 5 + "", page + "", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MedicationRecordInquiry");
                    stopProgressDialog();
                    if(view!=null){
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if (page == 1) {
                            rlup.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else
                            loadingFinished();
//                        tvcopyright.setVisibility(View.VISIBLE);
                    } else {
//                        tvcopyright.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            rlup.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                        MedicationDate date = JsonTools.getData(result.toString(), MedicationDate.class);
                        List<MedicationRecord> list = date.getData();
                        if (page == 1) {
                            if (mlist.size() > 0) mlist.clear();
                        }
                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                mlist.add(list.get(i));
                            }
                            mAdapter.setList(mlist);

                        }
                        loadingFinished();
                    }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingFinished();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));

    }

    public void initView() {
        inflater = LayoutInflater.from(getActivity());
        rlup = (LinearLayout) view.findViewById(R.id.ll_up);

        listView = (StickyListHeadersListView) view.findViewById(R.id.listView);
        mAdapter = new MedicinalRecordAdapter(getActivity());


        listView.setAdapter(mAdapter);
        moredata = (RelativeLayout) inflater.inflate(R.layout.moredata_date, null);
        progressBarView = (View) moredata.findViewById(R.id.loadmore_foot_progressbar);
        progressBarTextView = (TextView) moredata.findViewById(R.id.loadmore_foot_text);
        loadingAnimation = (AnimationDrawable) progressBarView.getBackground();
        listView.addFooterView(moredata);
        listView.setOnItemClickListener(this);
        listView.setOnHeaderClickListener(this);
        listView.setLoadingMoreListener(this);

//        tvcopyright= (TextView) moredata.findViewById(R.id.tvcopyright);
    }


    public static Fragment getInstance() {
        return new HealthFilesFragment();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public void loadingFinished() {

        if (null != loadingAnimation && loadingAnimation.isRunning()) {
            loadingAnimation.stop();
        }
        progressBarView.setVisibility(View.INVISIBLE);
        progressBarTextView.setVisibility(View.INVISIBLE);
        isLoading = false;

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnLoadingMore() {

        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            loadingFinished();
            return;

        }
        progressBarView.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);

        loadingAnimation.start();
        page++;
        if (!isLoading) {
            isLoading = true;
            initDate();
        }

    }

    @Override
    protected void lazyLoad() {

    }


}
