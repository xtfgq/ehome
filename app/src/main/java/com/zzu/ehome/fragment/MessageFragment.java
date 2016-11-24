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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.CampaignActivity;
import com.zzu.ehome.activity.ConversationListActivity;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.adapter.MessageAdapter;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.CapaingBean;
import com.zzu.ehome.bean.MessageBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.application.CustomApplcation.mList;


/**
 * Created by Mersens on 2016/8/27.
 */
public class MessageFragment extends BaseFragment {
    private View mView;
    private ListView listView;
    private View vTop;
    private MessageAdapter messageAdapter;
    private RequestMaker requestMaker;
    List<MessageBean> mList=new ArrayList<>();
    private BroadcastReceiver mDateOrFileBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("Num")&&mList.size()>0) {
                mList.get(1).setNum(CustomApplcation.getInstance().count);
                messageAdapter = new MessageAdapter(getActivity(), mList);
                listView.setAdapter(messageAdapter);
            }
        }


    };
    private String userid = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Num");
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

    @Override
    public void onResume() {
        super.onResume();
        userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
        mList.clear();
        if(!TextUtils.isEmpty(userid)) {
            initDatas();
        }else{
            MessageBean bean2 = new MessageBean();
            bean2.setContent("系统消息");
            bean2.setNum(0);
            bean2.setTips("");
            mList.add(bean2);
            MessageBean bean3 = new MessageBean();
            bean3.setContent("私人医生");
            bean3.setNum(0);
            bean3.setTips("在线问诊记录");
            mList.add(bean3);
//            if(messageAdapter==null) {
                messageAdapter = new MessageAdapter(getActivity(), mList);
                listView.setAdapter(messageAdapter);


        }
    }

    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                    return;
                }
                if (position == 0) {
                    startActivity(new Intent(getActivity(), CampaignActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), ConversationListActivity.class));
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
        messageAdapter=null;
    }

    public void initDatas() {
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker.APPLogInquiry(userid, "01", 1 + "", 1 + "", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("APPLogInquiry");
                    MessageBean bean2 = new MessageBean();
                    bean2.setContent("系统消息");
                    bean2.setNum(0);

                    if (array.getJSONObject(0).has("MessageCode")) {
                        bean2.setTips("");
                    } else {
                        CapaingBean bean = new CapaingBean();
                        bean.setCreateDate(array.getJSONObject(0).getString("CreatedDate"));
                        bean.setName(array.getJSONObject(0).getString("Log_Content"));
                        bean.setLog_ID(array.getJSONObject(0).getString("Log_ID"));
                        bean2.setTips(bean.getCreateDate());
                    }

                    mList.add(bean2);
                    MessageBean bean3 = new MessageBean();
                    bean3.setContent("私人医生");
                    if(CustomApplcation.getInstance().count>0){
                        bean3.setNum(CustomApplcation.getInstance().count);
                    }else{
                        bean3.setNum(0);
                    }

                    bean3.setTips("在线问诊记录");
                    mList.add(bean3);

                        messageAdapter = new MessageAdapter(getActivity(), mList);
                        listView.setAdapter(messageAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));


    }
}
