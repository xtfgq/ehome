package com.zzu.ehome.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.MedicalExaminationAdapter;
import com.zzu.ehome.bean.MedicalBean;
import com.zzu.ehome.bean.MedicalDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MyExaminationFragment extends BaseFragment{
    private View mView;
    private ListView listView;
    private RequestMaker requestMaker;
    private String userid;
    private List<MedicalBean> mList;
    private MedicalExaminationAdapter adapter;
    private LinearLayout layout_none;
    private SupperBaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_appointment, null);
        requestMaker = RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();
        initViews();
        initEvent();
        initDatas();
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    public void initViews() {
        listView = (ListView) mView.findViewById(R.id.listView);
        layout_none = (LinearLayout) mView.findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);

    }

    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void initDatas() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;

        }
        requestMaker.MeidicalReportInquiry(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {


                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MeidicalReportInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        layout_none.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {

                        if (mList != null && mList.size() > 0)
                            mList.clear();
                        MedicalDate date = JsonTools.getData(result.toString(), MedicalDate.class);
                        mList = date.getData();
                        if (adapter == null) {

                            adapter = new MedicalExaminationAdapter(getActivity(), mList);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.setmList(mList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }));


    }

    public static Fragment getInstance() {
        return new MyExaminationFragment();

    }

    @Override
    protected void lazyLoad() {

    }

}
