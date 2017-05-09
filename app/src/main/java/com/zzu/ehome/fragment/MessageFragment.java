package com.zzu.ehome.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.CampaignActivity;
import com.zzu.ehome.activity.ConversationListActivity;
import com.zzu.ehome.activity.LoginActivity;
import com.zzu.ehome.activity.MessageLiveActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.MessageAdapter;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.CapaingBean;
import com.zzu.ehome.bean.MessageBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.MD5Utils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by Mersens on 2016/8/27.
 */
public class MessageFragment extends BaseFragment {
    private View mView;
    private ListView listView;
    private View vTop;
    private MessageAdapter messageAdapter;
    private RequestMaker requestMaker;
    private SupperBaseActivity activity;
    List<MessageBean> mList = new ArrayList<>();
    private boolean isRefresh = false;
    private RefreshLayout refreshLayout;
    private BroadcastReceiver mDateOrFileBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("NumRefresh") && mList.size() > 0) {
                mList.get(1).setNum(CustomApplcation.getInstance().count);
                messageAdapter = null;
                listView.setAdapter(null);
                messageAdapter = new MessageAdapter(getActivity(), mList);
                listView.setAdapter(messageAdapter);
            }
            if (action.equals("userrefresh")) {
                userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if (!TextUtils.isEmpty(userid)) {
                    initDatas();
                } else {
                    mList.clear();
                    MessageBean bean2 = new MessageBean();
                    bean2.setContent("系统消息");
                    bean2.setNum(0);
                    bean2.setTips("");
                    mList.add(bean2);
                    MessageBean bean3 = new MessageBean();
                    bean3.setContent("家庭医生");
                    bean3.setNum(0);
                    bean3.setTips("在线问诊记录");
                    mList.add(bean3);
                    messageAdapter = null;
                    listView.setAdapter(null);
                    messageAdapter = new MessageAdapter(getActivity(), mList);
                    listView.setAdapter(messageAdapter);
                }
            }
        }


    };
    private String userid = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SupperBaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NumRefresh");
        intentFilter.addAction("userrefresh");
        getActivity().registerReceiver(mDateOrFileBroadcastReceiver, intentFilter);

        return inflater.inflate(R.layout.layout_message, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        requestMaker = RequestMaker.getInstance();
        initViews();
        initEvent();

    }

    public void initViews() {

        setOnlyTileViewMethod(mView, "消息");
        listView = (ListView) mView.findViewById(R.id.listView);
        refreshLayout = (RefreshLayout) mView.findViewById(R.id.refreshLayout);
        vTop = mView.findViewById(R.id.v_top);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int h = CommonUtils.getStatusHeight(getActivity());
            ViewGroup.LayoutParams params = vTop.getLayoutParams();
            params.height = h;
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            vTop.setLayoutParams(params);
            vTop.setBackgroundResource(R.color.actionbar_color);
        } else {
            vTop.setVisibility(View.GONE);
        }
    }


    public void initEvent() {
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        getData();
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {

                if (!activity.isNetWork) {
                    refreshLayout.refreshFinish(RefreshLayout.FAIL);
                    return;
                }
                isRefresh = true;
                getData();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!activity.isNetWork) {
                    activity.showNetWorkErrorDialog();
                    return;
                }
                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                switch (position){
                    case 0:
                        startActivity(new Intent(getActivity(), CampaignActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), ConversationListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), MessageLiveActivity.class));
                        break;
                }


            }
        });

    }


    public static Fragment getInstance() {
        return new MessageFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void initDatas() {
        if (!activity.isNetWork) {
            activity.showNetWorkErrorDialog();
            return;
        }
        startProgressDialog();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker.APPLogInquiry(userid, "01", 1 + "", 1 + "", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("APPLogInquiry");
                    mList.clear();
                    MessageBean bean2 = new MessageBean();
                    bean2.setContent("系统消息");
                    bean2.setNum(0);
                    stopProgressDialog();
                    if (mView != null) {
                        if (array.getJSONObject(0).has("MessageCode")) {

                            bean2.setTips("");
                            mList.add(bean2);
                            MessageBean bean3 = new MessageBean();
                            bean3.setContent("家庭医生");
                            if (CustomApplcation.getInstance().count > 0) {
                                bean3.setNum(CustomApplcation.getInstance().count);
                            } else {
                                bean3.setNum(0);
                            }

                            bean3.setTips("在线问诊记录");
                            mList.add(bean3);
                            addList(mList);
                            messageAdapter = null;
                            listView.setAdapter(null);
                            messageAdapter = new MessageAdapter(getActivity(), mList);
                            listView.setAdapter(messageAdapter);
                        } else {
                            CapaingBean bean = new CapaingBean();
                            bean.setCreateDate(array.getJSONObject(0).getString("CreatedDate"));
                            bean.setName(array.getJSONObject(0).getString("Log_Content"));
                            bean.setLog_ID(array.getJSONObject(0).getString("Log_ID"));
                            bean2.setTips(DateUtils.StringPattern(bean.getCreateDate(), "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm"));
                            mList.add(bean2);
                            MessageBean bean3 = new MessageBean();
                            bean3.setContent("家庭医生");
                            if (CustomApplcation.getInstance().count > 0) {
                                bean3.setNum(CustomApplcation.getInstance().count);
                            } else {
                                bean3.setNum(0);
                            }
                            bean3.setTips("在线问诊记录");
                            mList.add(bean3);
                            addList(mList);
                            messageAdapter = null;
                            listView.setAdapter(null);
                            messageAdapter = new MessageAdapter(getActivity(), mList);
                            listView.setAdapter(messageAdapter);
                        }
                    }

                } catch (Exception e) {
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if(hidden){
//
//        }else{
//            Log.e("vvv","hide.....");
//            userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
//            mList.clear();
//            if(!TextUtils.isEmpty(userid)) {
//                initDatas();
//            }else{
//                MessageBean bean2 = new MessageBean();
//                bean2.setContent("系统消息");
//                bean2.setNum(0);
//                bean2.setTips("");
//                mList.add(bean2);
//                MessageBean bean3 = new MessageBean();
//                bean3.setContent("私人医生");
//                bean3.setNum(0);
//                bean3.setTips("在线问诊记录");
//                mList.add(bean3);
//                messageAdapter = new MessageAdapter(getActivity(), mList);
//                listView.setAdapter(messageAdapter);
//            }
//        }
    }
//    private void getDoctorList(String CategoryID,String userid,String  cardno){
//
//            requestMaker.MSDoctorInquiry(CategoryID, userid,cardno,new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
//                @Override
//                public void processJsonObject(Object result) {
//                    JSONObject mySO = (JSONObject) result;
//                    try {
//                        JSONArray array = mySO.getJSONArray("MSDoctorInquiry");
//
//                        if (array.getJSONObject(0).has("MessageCode")) {
////                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
////                                Toast.LENGTH_SHORT).show();
//
//
//                        } else {
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }finally {
//
//                    }
//                }
//            }));


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void getData() {
        if (!TextUtils.isEmpty(userid)) {
            initDatas();
        } else {
            mList.clear();
            MessageBean bean2 = new MessageBean();
            bean2.setContent("系统消息");
            bean2.setNum(0);
            bean2.setTips("");
            mList.add(bean2);
            MessageBean bean3 = new MessageBean();
            bean3.setContent("家庭医生");
            bean3.setNum(0);
            bean3.setTips("在线问诊记录");
            mList.add(bean3);
            addList(mList);
            messageAdapter = null;
            listView.setAdapter(null);
            messageAdapter = new MessageAdapter(getActivity(), mList);
            listView.setAdapter(messageAdapter);
        }
    }
    public void addList(List<MessageBean> mList){
        MessageBean bean = new MessageBean();
        bean.setContent("视频教学");
        bean.setNum(0);
        bean.setTips("视频教学");
        mList.add(bean);
    }

}
