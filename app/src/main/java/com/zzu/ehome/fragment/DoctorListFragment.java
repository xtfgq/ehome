package com.zzu.ehome.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.DoctorDetialActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MSDoctorBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.zzu.ehome.R.id.tv_sign;

/**
 * Created by Mersens on 2016/8/16.
 */
public class DoctorListFragment extends BaseFragment {
    private View mView;
    private ListView mListView;
    private String hosptialId = "";
    private String title = "";
    private String goodAt = "";
    private String cardno="";
    public static final String HOSPTIALID = "hosptialId";
    public static final String TITLE = "title";
    public static final String GOODAT = "goodAt";
    private RequestMaker requestMaker;
    private List<MSDoctorBean> mList = null;
    private MyAdapter adapter;
    private LinearLayout layout_no_msg;
    private OnSearchResultListener listener;
    private RefreshLayout refreshLayout;
    private boolean isRefresh=false;
    private SupperBaseActivity activity;
    private EHomeDao dao;
    private CompositeSubscription compositeSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_list, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mList = new ArrayList<>();
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        dao = new EHomeDaoImpl(getActivity());
        refreshLayout=(RefreshLayout) mView.findViewById(R.id.refreshLayout);
        requestMaker = RequestMaker.getInstance();
        layout_no_msg = (LinearLayout) mView.findViewById(R.id.layout_no_msg);
        hosptialId = getArguments().getString(HOSPTIALID) == null ? "" : getArguments().getString(HOSPTIALID);
//       title = getArguments().getString(TITLE) == null ? "" : getArguments().getString(TITLE);

        title = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        if(!TextUtils.isEmpty(title)) {
            if (dao.findUserInfoById(title).getUserno() != null) {
                cardno = dao.findUserInfoById(title).getUserno();
            }
        }

//        goodAt = getArguments().getString(GOODAT) == null ? "" : getArguments().getString(GOODAT);
        mListView = (ListView) mView.findViewById(R.id.listView);

    }


    public void initEvent() {
        //创建compositeSubscription实例
        compositeSubscription = new CompositeSubscription();

        //监听订阅事件
        Subscription subscription = RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }

                        if (event instanceof EventType){
                            EventType type=(EventType)event;
                           if("su".equals(type.getType())){
                               title = SharePreferenceUtil.getInstance(getActivity()).getUserId();
                               if(!TextUtils.isEmpty(title)) {
                                   if (dao.findUserInfoById(title).getUserno() != null) {
                                       cardno = dao.findUserInfoById(title).getUserno();
                                   }else{
                                       cardno="";
                                   }
                               }
                               getDoctorListData(hosptialId, title,cardno);
                           }

                        }


                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }

            }
        });
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {

                    if(!activity.isNetWork){
                        refreshLayout.refreshFinish(RefreshLayout.FAIL);
                        return;
                    }

                isRefresh=true;
                getDoctorListData(hosptialId, title,cardno);
            }
        });
    }

    public void initDatas() {
        adapter = new MyAdapter(mList);
        mListView.setAdapter(adapter);
        getDoctorListData(hosptialId, title,cardno);
    }


//    @Override
//    public UserInfo getUserInfo(String s) {
//
////            for (int i = 0; i < mList.size(); i++) {
////
////                return new UserInfo(mList.get(i).getDoctorID(), mList.get(i).getDoctorName(), Uri.parse(Constants.EhomeURL + mList.get(i).getImageURL()));
////            }
//
//
//
//    }


    class MyAdapter extends BaseAdapter {
        private List<MSDoctorBean> list;

        public MyAdapter(List<MSDoctorBean> list) {
            this.list = list;
        }

        public void setList(List<MSDoctorBean> list) {
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_doctor, null);
                holder.icon_state = (ImageView) convertView.findViewById(R.id.icon_state);
                holder.user_img = (ImageView) convertView.findViewById(R.id.user_img);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_yqy = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tv_hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
                holder.tv_sc = (TextView) convertView.findViewById(R.id.tv_sc);
                holder.tv_sign=(Button)convertView.findViewById(tv_sign);
//                holder.tv_gooat = (TextView) convertView.findViewById(R.id.tv_gooat);
                holder.tv_qyl = (TextView) convertView.findViewById(R.id.tv_qyl);
//                holder.tv_wzl = (TextView) convertView.findViewById(R.id.tv_wzl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
           final  MSDoctorBean bean = list.get(position);
//            if(bean.getIsLine().equals("1")){
//                holder.icon_state.setImageResource(R.mipmap.icon_online_g);
//            }
            String imgurl = Constants.EhomeURL + bean.getImageURL().replace("~", "").replace("\\", "/");
            Glide.with(getActivity()).load(imgurl).error(R.drawable.icon_doctor).into(holder.user_img);

            holder.tv_name.setText(bean.getDoctorName());
//            holder.tv_zc.setText(bean.getTitle());
            holder.tv_hospital.setText(bean.getHospitalName());
            holder.tv_sc.setText("擅长：" + bean.getSpeciaty());
//            holder.tv_gooat.setText(bean.getGoodAt());
            int sign=Integer.valueOf(bean.getIsSign());
            if(sign==1){
                holder.tv_sign.setVisibility(View.VISIBLE);
                holder.tv_yqy.setText("已签约");
            }else{
                holder.tv_yqy.setText("");
                holder.tv_sign.setVisibility(View.INVISIBLE);
            }

            holder.tv_qyl.setText("签约量：" + bean.getSignCount());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), DoctorDetialActivity.class);
                    i.putExtra("doctorid", bean.getDoctorID());
                    i.putExtra("doctorname", bean.getDoctorName());
                    i.putExtra("UserSign",bean.getUserIsSign());
                    startActivity(i);
                }
            });
            holder.tv_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startProgressDialog();
                    requestMaker.MSDoctorConsultationTime(bean.getDoctorID(),new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
                        @Override
                        public void processJsonObject(Object result) {
                            try {
                                stopProgressDialog();

                                JSONObject mySO = (JSONObject) result;
                                JSONArray array = mySO.getJSONArray("MSDoctorConsultationTime");
//                                {"MSDoctorConsultationTime":[{"MessageCode":"3","MessageContent":"当前时间医生在线，可以咨询！"}]}
                                if(Integer.valueOf(array.getJSONObject(0).getString("MessageCode"))==3){
                                    RongIM.getInstance().startPrivateChat(getActivity(), bean.getDoctorID(), bean.getDoctorName());
                                }else {
                                    confirmConversation(array.getJSONObject(0).getString("MessageContent").toString(),bean.getDoctorID(), bean.getDoctorName());
                                }

                            }
                            catch (Exception e) {
                                e.printStackTrace();

                            }
                            finally {

                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    }));


                }
            });
//            holder.tv_wzl.setText("问诊量：" + bean.getDiagnoseCount());
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(bean.getDoctorID(), bean.getDoctorName(), Uri.parse(imgurl)));
            return convertView;
        }
    }

    public static class ViewHolder {
        public ImageView icon_state;
        public ImageView user_img;
        public TextView tv_name;
        public TextView tv_yqy;
        public TextView tv_hospital;
        public TextView tv_sc;
//        public TextView tv_gooat;
        public TextView tv_qyl;
        public Button tv_sign;
//        public TextView tv_wzl;

    }

    public void getDoctorListData(final String CategoryID, final String userid,final String cardno) {
        requestMaker.MSDoctorInquiry(CategoryID, userid,cardno,new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("MSDoctorInquiry");

                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                        if (TextUtils.isEmpty(hosptialId)) {
                            if(listener!=null){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onNoData(true);
                                    }
                                },100);
                            }
                        }
                        layout_no_msg.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    } else {
                        mList.clear();
                        layout_no_msg.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            mList.add(getDataFromJson(json));

                        }

                        adapter.setList(mList);
                        adapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(isRefresh){
                        refreshLayout.refreshFinish(RefreshLayout.SUCCEED);
                        isRefresh=false;
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));


//        requestMaker.MSDoctorInquiry(hosptialId, title, goodAt, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
//            @Override
//            public void processJsonObject(Object result) {
//                JSONObject mySO = (JSONObject) result;
//                try {
//                    JSONArray array = mySO.getJSONArray("MSDoctorInquiry");
//
//                    if (array.getJSONObject(0).has("MessageCode")) {
////                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
////                                Toast.LENGTH_SHORT).show();
//                        if (TextUtils.isEmpty(hosptialId) && TextUtils.isEmpty(title) && TextUtils.isEmpty(goodAt)) {
//                            if(listener!=null){
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        listener.onNoData(true);
//                                    }
//                                },100);
//                            }
//                        }
//                        layout_no_msg.setVisibility(View.VISIBLE);
//                        mListView.setVisibility(View.GONE);
//                    } else {
//
//                        layout_no_msg.setVisibility(View.GONE);
//                        mListView.setVisibility(View.VISIBLE);
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject json = array.getJSONObject(i);
//                            mList.add(getDataFromJson(json));
//
//                        }
//
//                        adapter.setList(mList);
//                        adapter.notifyDataSetChanged();
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    if(isRefresh){
//                        refreshLayout.refreshFinish(RefreshLayout.SUCCEED);
//                        isRefresh=false;
//                    }
//                }
//            }
//        }));
    }

    /**
     * 解析json数据
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public MSDoctorBean getDataFromJson(JSONObject json) throws JSONException {
        MSDoctorBean bean = new MSDoctorBean();
        String ImageURL = json.getString("ImageURL");
        bean.setImageURL(ImageURL);
        String Description = json.getString("Description");
        bean.setDescription(Description);
        String IsLine = json.getString("IsLine");
        bean.setIsLine(IsLine);
        String GoodCode = json.getString("GoodCode");
        bean.setGoodCode(GoodCode);
        String DoctorName = json.getString("DoctorName");
        bean.setDoctorName(DoctorName);
        String Title = json.getString("Title");
        bean.setTitle(Title);
        String GoodAt = json.getString("GoodAt");
        bean.setGoodAt(GoodAt);
        String HospitalName = json.getString("HospitalName");
        bean.setHospitalName(HospitalName);
        String HospitalID = json.getString("HospitalID");
        bean.setHospitalID(HospitalID);
        String Mobile = json.getString("Mobile");
        bean.setMobile(Mobile);
        String DoctorID = json.getString("DoctorID");
        bean.setDoctorID(DoctorID);
        String Department = json.getString("Department");
        bean.setDepartment(Department);
        String TitleCode = json.getString("TitleCode");
        bean.setTitleCode(TitleCode);
        String DoctorNo = json.getString("DoctorNo");
        bean.setDoctorNo(DoctorNo);
        String DiagnoseCount = json.getString("DiagnoseCount");//就诊量
        bean.setDiagnoseCount(DiagnoseCount);
        String SignCount = json.getString("SignCount");//签约量
        bean.setSignCount(SignCount);
        String Speciaty = json.getString("Speciaty");
        bean.setSpeciaty(Speciaty);
        String issign = json.getString("IsSign");
        bean.setIsSign(issign);
        String userissign=json.getString("UserIsSign");
        bean.setUserIsSign(userissign);
        String url = Constants.EhomeURL + ImageURL.replace("~", "").replace("\\", "/");
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(DoctorID, DoctorName, Uri.parse(url)));
        return bean;
    }

    public static Fragment getInstance(String hosptialId) {
        Bundle bd = new Bundle();
        bd.putString(HOSPTIALID, hosptialId);
//        bd.putString(TITLE, title);
//        bd.putString(GOODAT, goodAt);
        DoctorListFragment df = new DoctorListFragment();
        df.setArguments(bd);
        return df;
    }

    @Override
    protected void lazyLoad() {

    }

    public interface OnSearchResultListener {
        void onNoData(boolean ishas);
    }

    public void setOnSearchResultListener(OnSearchResultListener listener) {
        this.listener = listener;
    }
    public void confirmConversation(String str,final String uid,final String url) {
        DialogTips dialog = new DialogTips(getActivity(), "", str,
                "继续咨询", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
//                User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(DoctorDetialActivity.this).getUserId());
//                if (TextUtils.isEmpty(dbUser.getAge())) {
//                    startActivity(new Intent(DoctorDetialActivity.this, PersonalCenterInfo.class));
//                    return;
//                }
                RongIM.getInstance().startPrivateChat(getActivity(), uid,url);

            }
        });

        dialog.show();
        dialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
