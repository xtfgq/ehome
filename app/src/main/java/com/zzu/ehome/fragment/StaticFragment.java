package com.zzu.ehome.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.EcgDesActivity;
import com.zzu.ehome.activity.InternetHospitalActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.ECGStaticAadapter;
import com.zzu.ehome.bean.StaticBean;
import com.zzu.ehome.bean.StaticDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_ECGInfo;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dell on 2016/6/20.
 */
public class StaticFragment extends BaseFragment {
    private View mView;
    private ListView listView;
    private LinearLayout layout_no_msg;
    private TextView tv_hlwyy;
    private RequestMaker requestMaker;
    private String userid;
    private ECGStaticAadapter adapter;
    private LinearLayout heardchat;
    private SupperBaseActivity activity;
    private String cardNo;
    private EHomeDao dao;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_ect, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao=new EHomeDaoImpl(getActivity());
        cardNo=dao.findUserInfoById(userid).getUserno();
        initViews();
        initEvent();
        initDatas();
        return mView;
    }

    public void initViews() {
        heardchat = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_head, null);
        listView = (ListView) mView.findViewById(R.id.listview);
        layout_no_msg = (LinearLayout) mView.findViewById(R.id.layout_no_msg);
        tv_hlwyy = (TextView) mView.findViewById(R.id.tv_hlwyy);
        listView.addHeaderView(heardchat);

    }

    public void initEvent() {
        tv_hlwyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
                Intent intent = new Intent(getActivity(), InternetHospitalActivity.class);
                startActivity(intent);
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //String name=mList.get(position);
//                Intent intent = new Intent(getActivity(), ECGDetailsActivity.class);
//                //intent.putExtra("name",name);
//                startActivity(intent);
//            }
//        });
    }

    public void initDatas() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;
        }

        startProgressDialog();
//      userid = "113397";
//        cardNo="4101051964020252737";
        requestMaker.GetElectrocardio(userid, cardNo,new JsonAsyncTask_ECGInfo(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                stopProgressDialog();
                try {

                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("GetElectrocardio");

                    if(layout_no_msg!=null&&listView!=null){
                        if (array.getJSONObject(0).has("MessageCode")) {
                        layout_no_msg.setVisibility(View.VISIBLE);
                    } else {
                        layout_no_msg.setVisibility(View.GONE);
                        StaticDate date = JsonTools.getData(result.toString(), StaticDate.class);
                        List<StaticBean> list = date.getData();
                        adapter = new ECGStaticAadapter(getActivity(), list);
                        listView.setAdapter(adapter);
                    }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }

            @Override
            public void onError(Exception e) {

            }

        }));

    }


    public static Fragment getInstance() {
        return new StaticFragment();
    }

    @Override
    protected void lazyLoad() {

    }
}
