package com.zzu.ehome.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.DemoContext;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.EcgDesActivity;
import com.zzu.ehome.activity.ExaminationReportActivity;
import com.zzu.ehome.activity.ExaminationTestActivity;
import com.zzu.ehome.activity.FatherTestActivity;
import com.zzu.ehome.activity.HealthInstructionActivity;
import com.zzu.ehome.activity.HypertensionActivity;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.activity.NearPharmacyActivity;
import com.zzu.ehome.activity.NewsWebView;
import com.zzu.ehome.activity.OrdinaryYuYueActivity;
import com.zzu.ehome.activity.PersonalCenterInfo;
import com.zzu.ehome.activity.StaticWebView;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.activity.WebVideoActivity;
import com.zzu.ehome.adapter.HomeNewsAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.CacheBean;
import com.zzu.ehome.bean.MSDoctorBean;
import com.zzu.ehome.bean.News;
import com.zzu.ehome.bean.NewsDate;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.RelationBean;
import com.zzu.ehome.bean.RelationDes;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserDate;
import com.zzu.ehome.bean.UserInfoDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.service.DownloadServiceForAPK;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DecorViewGuideHelper;
import com.zzu.ehome.utils.DialogUtils;
import com.zzu.ehome.utils.ImageOptions;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogEnsureCancelView;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.ImageCycleView;
import com.zzu.ehome.view.MyHomeLayout;
import com.zzu.ehome.view.MyScrollView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static com.zzu.ehome.R.id.lltop;


/**
 * Created by Mersens on 2016/7/26.
 */
public class HomeFragment1 extends BaseFragment implements View.OnClickListener {
    private static final String PACKAGE_URL_SCHEME = "package:";
    //    private PermissionsChecker mPermissionsChecker; // 权限检测器
//    // 所需的全部权限
//    static final String[] PERMISSIONS = new String[]{
//
//            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
//    };
    private View mView;
    private ImageCycleView mViewPager;
    private ListView mListView;
    private PullToRefreshLayout pulltorefreshlayout;
    private RequestMaker requestMaker;
    private int page = 1;
    private View vTop;
    private String type=null;
    PackageManager pm ;
    boolean permission;
    private List<View> guid;
    private SupperBaseActivity activity;


    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
    }

    // private LinearLayout layout_health_files;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    private BDLocation location;
    private TextView tvcity, tvweather, tvpm, tvcurrent,tv_pmlv;
    private String city = "郑州市";
    private ImageView ivweather;
    //广告轮播 图片链接
    ArrayList<String> mImageUrl = new ArrayList<String>();
    //广告通用对象
    ArrayList<Map<String, Object>> mObject = new ArrayList<Map<String, Object>>();
    private LinearLayout llrecord;
    private LinearLayout layout_yygh, layout_free_consultation,
            layout_add, layout_fjyd, layout_srys, layout_jcbg,
            layout_tjbg, layout_xdbg;
    private LinearLayout layout_gxy, layout_xxg, layout_tnb, layout_gxb, layout_xzb;
    private HomeNewsAdapter mAadapter;
    private List<News> mList = new ArrayList<News>();
    private static BDLocation mLocation = null;
    private boolean isFirst = true;
    private boolean isReflash = false;
    private boolean isLoading = false;
    private double calories = 0;
    private int step_length = 55;
    private int minute_distance = 80;
    private float weight;
    private String timeCount;
    private EHomeDao dao;
    private int newCode;
    private int oldCode;
    private String versionlog;
    private String ClientID;
    private MyScrollView myScrollView;

    private MyHomeLayout linearLayout;
    RelationDes rsTemp = new RelationDes();
    RelationDes rsTarget = new RelationDes();
    private RelativeLayout rlhometitle;
    private LinearLayout nologin;
    private BroadcastReceiver mRefreshReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("userrefresh")) {
                if (mList.size() > 0) mList.clear();
                initDatas();
                linearLayout.removeAllViews();

                if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    nologin.setVisibility(View.GONE);
                    if (SharePreferenceUtil.getInstance(getActivity()).getUserId().equals(SharePreferenceUtil.getInstance(getActivity()).getHomeId())) {
                        initHomeFamily();
                    } else {
                        if(CustomApplcation.getInstance().isRead){
                            readCache();
                        }else {
                            initHomeNotUserId();
                        }
                    }
                }else{
                    nologin.setVisibility(View.VISIBLE);
                }
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ClientID = PushManager.getInstance().getClientid(getActivity());
        requestMaker = RequestMaker.getInstance();
//        mPermissionsChecker = new PermissionsChecker(getActivity());
        mView = inflater.inflate(R.layout.layout_home1, null);
        initViews();
        mAadapter = new HomeNewsAdapter(getActivity());
        mListView.setAdapter(mAadapter);
        dao = new EHomeDaoImpl(getActivity());
        location();
        initEvent();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("userrefresh");

        getActivity().registerReceiver(mRefreshReciver, intentFilter);
//        EventBus.getDefault().register(this);

        if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
            nologin.setVisibility(View.GONE);
            if (SharePreferenceUtil.getInstance(getActivity()).getUserId().equals(SharePreferenceUtil.getInstance(getActivity()).getHomeId())) {
                initHomeFamily();
            } else {
                if(CustomApplcation.getInstance().isRead) {
                    readCache();
                    ReConnetRong();
                }else {
                    initHomeNotUserId();
                }
            }
        }else{
            nologin.setVisibility(View.VISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }
        city = "郑州市";

        getWeather(city);
        versioninquiry();
        if(SharePreferenceUtil.getInstance(getActivity()).getGUIDId()!=4) {
            initGuid(inflater);
            showGuide();
        }
        mListView.setFocusable(false);

        return mView;

    }
    private void initGuid(LayoutInflater inflater){
        guid=new ArrayList<>();
        guid.add(inflater.inflate(R.layout.layout_guid1, null));
        View guid2=inflater.inflate(R.layout.layout_guid2, null);
        View lltop2=guid2.findViewById(lltop);
        ViewGroup.LayoutParams para2;
        para2 =  lltop2.getLayoutParams();
        para2.width = ScreenUtils.getScreenWidth(getActivity());
        para2.height = para2.width*10/22;
        lltop2.setLayoutParams(para2);
        guid.add(guid2);
        View guid3=inflater.inflate(R.layout.layout_guid3, null);
        View lltop3=guid3.findViewById(lltop);
        ViewGroup.LayoutParams para3;
        para3 =  lltop3.getLayoutParams();
        para3.width = ScreenUtils.getScreenWidth(getActivity());
        para3.height = para3.width*6/22;
        lltop3.setLayoutParams(para3);
        guid.add(guid3);
        View guid4=inflater.inflate(R.layout.layout_guid4, null);
        View lltop4=guid4.findViewById(lltop);
        ViewGroup.LayoutParams para4;
        para4 =  lltop4.getLayoutParams();
        para4.width = ScreenUtils.getScreenWidth(getActivity());
        para4.height = para4.width*12/22;
        lltop4.setLayoutParams(para4);
        guid.add(guid4);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(SupperBaseActivity)context;
    }

    public void initViews() {

        //layout_health_files=(LinearLayout) mView.findViewById(R.id.layout_health_files);

        mViewPager = (ImageCycleView) mView.findViewById(R.id.viewPager);
        mListView = (ListView) mView.findViewById(R.id.listView);
        tvcity = (TextView) mView.findViewById(R.id.tvcity);
        tvweather = (TextView) mView.findViewById(R.id.tvweather);
        pulltorefreshlayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);
        tvpm = (TextView) mView.findViewById(R.id.tvpm);
        ivweather = (ImageView) mView.findViewById(R.id.ivweather);
        tvcurrent = (TextView) mView.findViewById(R.id.tvcurrent);
        llrecord = (LinearLayout) mView.findViewById(R.id.llrecord);
        layout_yygh = (LinearLayout) mView.findViewById(R.id.layout_yygh);
        layout_free_consultation = (LinearLayout) mView.findViewById(R.id.layout_free_consultation);
        layout_add = (LinearLayout) mView.findViewById(R.id.layout_add);
        layout_fjyd = (LinearLayout) mView.findViewById(R.id.layout_fjyd);
        layout_srys = (LinearLayout) mView.findViewById(R.id.layout_srys);
        layout_jcbg = (LinearLayout) mView.findViewById(R.id.layout_jcbg);
        layout_tjbg = (LinearLayout) mView.findViewById(R.id.layout_tjbg);
        layout_xdbg = (LinearLayout) mView.findViewById(R.id.layout_xdbg);
        layout_gxy = (LinearLayout) mView.findViewById(R.id.layout_gxy);
        layout_xxg = (LinearLayout) mView.findViewById(R.id.layout_xxg);
        layout_tnb = (LinearLayout) mView.findViewById(R.id.layout_tnb);
        layout_gxb = (LinearLayout) mView.findViewById(R.id.layout_gxb);
        layout_xzb = (LinearLayout) mView.findViewById(R.id.layout_xzb);
        vTop = mView.findViewById(R.id.v_top);
        myScrollView = (MyScrollView) mView.findViewById(R.id.scrollView4);
        linearLayout = (MyHomeLayout) mView.findViewById(R.id.llhome);
        rlhometitle = (RelativeLayout) mView.findViewById(R.id.homehead);

        ViewGroup.LayoutParams para;
        para =  rlhometitle.getLayoutParams();
        para.width = ScreenUtils.getScreenWidth(getActivity());
        para.height = para.width*12/22;
        rlhometitle.setLayoutParams(para);

        nologin=(LinearLayout) mView.findViewById(R.id.nologin);
        tv_pmlv=(TextView)mView.findViewById(R.id.tv_pmlv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int h = CommonUtils.getStatusHeight(getActivity());
            ViewGroup.LayoutParams params = vTop.getLayoutParams();
            params.height = h;
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            vTop.setLayoutParams(params);
            vTop.setBackgroundResource(R.drawable.bg_header_lead);
        } else {
            vTop.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
//        getDefault().unregister(this);
        try {
            getActivity().unregisterReceiver(mRefreshReciver);
            mRefreshReciver = null;
        } catch (Exception e) {
        }
    }


    private void showADs() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());// new Date()为获取当前系统时间


        requestMaker.searchAds(date, new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                JSONObject mySO = (JSONObject) result;
                Map<String, Object> map;
                try {
                    JSONArray array = mySO.getJSONArray("ADInquiry");
                    myScrollView.smoothScrollTo(0, 0);
                    if (array.getJSONObject(0).has("MessageCode")) {
                        // Toast.makeText(Index.this, "数据为空", 1).show();
                        mViewPager.setVisibility(View.GONE);
                    } else {
                        if (mImageUrl != null && mImageUrl.size() > 0) {
                            mImageUrl.clear();
                        }
                        if (mObject != null && mObject.size() > 0) {
                            mObject.clear();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            map = new HashMap<String, Object>();
                            map.put("ImageUr",
                                    (Constants.EhomeURL + array
                                            .getJSONObject(i).getString(
                                                    "ImageUrl")).toString()
                                            .replace("~", "")
                                            .replace("\\", "/"));
                            map.put("ID",
                                    array.getJSONObject(i).getString("ID"));
                            map.put("Title",
                                    array.getJSONObject(i).getString("Title"));

                            mImageUrl
                                    .add((Constants.EhomeURL + array
                                            .getJSONObject(i).getString(
                                                    "ImageUrl")).toString()
                                            .replace("~", "")
                                            .replace("\\", "/"));
                            mObject.add(map);
                        }
                        mViewPager.setVisibility(View.VISIBLE);
                        mViewPager.setImageResources(mImageUrl, mObject, new ImageCycleView.ImageCycleViewListener() {
                            @Override
                            public void displayImage(String imageURL,
                                                     ImageView imageView) {
                                try {
                                    ImageLoader.getInstance().displayImage(imageURL,
                                            imageView, ImageOptions.getAdsOptions());
                                    ViewGroup.LayoutParams para;
                                    para = imageView.getLayoutParams();
                                    para.width = ScreenUtils.getScreenWidth(getActivity());
                                    para.height = para.width * 2 / 5;
                                    imageView.setLayoutParams(para);
                                } catch (OutOfMemoryError e) {
                                    // TODO Auto-generated catch block

                                }

                            }

                            @Override
                            public void onImageClick(int position, View imageView,
                                                     ArrayList<Map<String, Object>> mObject) {
                                String ID = mObject.get(position).get("ID")
                                        .toString();
                                String title = mObject.get(position).get("Title")
                                        .toString();
                                Intent i = new Intent(getActivity(),
                                        StaticWebView.class);
                                i.putExtra("ID", ID);
                                i.putExtra("Title", title);
                                startActivity(i);

                            }

                        });

                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }));

    }

//    public void onEventMainThread(RefreshEvent event) {
//
//        if (getResources().getInteger(R.integer.refresh_info) == event
//                .getRefreshWhere()) {
//
//        }
//
//    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);


    }


    @Override
    public void onStop() {
        super.onStop();
        hideFamily();
    }


//    private void showGps(){
//        DialogTips dialog = new DialogTips(getActivity(), "请打开GPS",
//                "确定");
//        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int userId) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                try
//                {
//                    getContext().startActivity(intent);
//
//
//                } catch(ActivityNotFoundException ex)
//                {
//                    intent.setAction(Settings.ACTION_SETTINGS);
//                    try {
//                        getContext().startActivity(intent);
//                    } catch (Exception e) {
//                    }
//
//            }
//            }
//        });
//
//        dialog.show();
//        dialog = null;
//
//    }




    public void initEvent() {

        llrecord.setOnClickListener(this);
        layout_yygh.setOnClickListener(this);
        layout_add.setOnClickListener(this);
        layout_fjyd.setOnClickListener(this);
        layout_srys.setOnClickListener(this);
        layout_jcbg.setOnClickListener(this);
        layout_tjbg.setOnClickListener(this);
        layout_xdbg.setOnClickListener(this);
        layout_gxy.setOnClickListener(this);
        layout_xxg.setOnClickListener(this);
        layout_tnb.setOnClickListener(this);
        layout_gxb.setOnClickListener(this);
        layout_xzb.setOnClickListener(this);
        rlhometitle.setOnClickListener(this);
        layout_free_consultation.setOnClickListener(this);
        nologin.setOnClickListener(this);
        initDatas();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
                String ID = mList.get(position).getID();
                String title = mList.get(position).getTitle();
                Intent i = new Intent(getActivity(),
                        NewsWebView.class);
                i.putExtra("ID", ID);
                i.putExtra("Title", title);
                startActivity(i);
            }
        });

        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    pulltorefreshlayout.refreshFinish(PullToRefreshLayout.FAIL);
                    return;
                }

                page = 1;
                isFirst = true;
                isReflash = true;
                isLoading = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPermission();
                }
                newsInqury();
                showADs();
                getWeather(city);
                // 下拉刷新操作

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(!activity.isNetWork){
                    pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    return;
                }
                page++;
                isLoading = true;
                isReflash = false;
                newsInqury();

            }
        });

    }


    public void initDatas() {
        showADs();
        newsInqury();
//        ReConnetRong();
    }


    private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

        @Override
        public void onChanged(ConnectionStatus connectionStatus) {

            switch (connectionStatus) {

                case CONNECTED://连接成功。
                    break;
                case DISCONNECTED://断开连接。

                    break;
                case CONNECTING://连接中。

                    break;
                case NETWORK_UNAVAILABLE://网络不可用。

                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线

//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            confirmExit();
//                        }
//                    });
                    CustomApplcation.getInstance().isOnLine=0;
                    Intent rongyun = new Intent("rongyun");
                    getActivity().sendBroadcast(rongyun);

                    break;
            }
        }
    }


    @Override
    protected void lazyLoad() {

    }

    public Fragment getInstance() {
        return new HomeFragment1();
    }

    @Override
    public void onClick(View v) {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;
        }
        hideFamily();
        switch (v.getId()) {

            case R.id.llrecord:
                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                    return;
                }
                Intent intenthealth = new Intent("Health");
                getActivity().sendBroadcast(intenthealth);
                break;
            case R.id.layout_yygh:
                startIntent(getActivity(), OrdinaryYuYueActivity.class);
                break;
            case R.id.layout_free_consultation:

//                startIntent(getActivity(), FreeConsultationActivity.class);
                startIntent(getActivity(), WebVideoActivity.class);
                break;
            case R.id.layout_add:
                String userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
//                if (!TextUtils.isEmpty(userid)) {
//                    if(type!=null){
//
//                        if("3".equals(type)){
//                            Intent i = new Intent(getActivity(), HealthFilesActivity1.class);
//                            i.putExtra("UserId", userid);
//                            i.putExtra("type",type);
//                            startActivity(i);
//                        }else if("2".equals(type)){
//                            Intent i = new Intent(getActivity(), HealthFilesActivity.class);
//                            i.putExtra("UserId", userid);
//                            i.putExtra("type",type);
//                            startActivity(i);
//                        }
//                    }else {
//                        return;
//                    }
//
//                } else {

//                    startActivity(new Intent(getActivity(), LoginActivity1.class));
//                }
                //                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
//                    startActivity(new Intent(getActivity(), LoginActivity1.class));
//                    return;
//                }
//                if(checkCardNo(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
//                    getActivity().startActivity(new Intent(getActivity(), MedicalRecordsActivity.class));
//                    return;
//                }
                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                    return;
                }
                if(checkCardNo(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startIntent(getActivity(), HealthInstructionActivity.class);
                    return;
                }

                break;
            case R.id.layout_fjyd:


                if (!permission) {
                    showDialog("请检查定位权限是否被第三方禁用");
                    return;
                }
                startIntent(getActivity(), NearPharmacyActivity.class);
                break;
            case R.id.layout_srys:

                Intent intent = new Intent("PrivateDoctor");
                getActivity().sendBroadcast(intent);
                break;
            case R.id.layout_jcbg:
                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startIntent(getActivity(), LoginActivity1.class);
                } else {
                    startIntent(getActivity(), ExaminationTestActivity.class);
                }
                break;
            case R.id.layout_tjbg:

                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                } else {
//                    startIntent(getActivity(), FatherTestActivity.class);
                    startIntent(getActivity(), ExaminationReportActivity.class);
                }
                break;
            case R.id.layout_xdbg:
                if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                } else {
                    startIntent(getActivity(), EcgDesActivity.class);
                }

                break;
            case R.id.layout_gxy:
                startIntent(getActivity(), HypertensionActivity.class, 1);
                break;
            case R.id.layout_xxg:
                startIntent(getActivity(), HypertensionActivity.class, 2);
                break;
            case R.id.layout_tnb:
                startIntent(getActivity(), HypertensionActivity.class, 3);
                break;
            case R.id.layout_gxb:
                startIntent(getActivity(), HypertensionActivity.class, 4);
                break;
            case R.id.layout_xzb:
                startIntent(getActivity(), HypertensionActivity.class, 5);
                break;
            case R.id.nologin:
                startActivity(new Intent(getActivity(), LoginActivity1.class));
                break;

        }

    }

    public <T> void startIntent(Activity context, Class<T> cls, int type) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    public <T> void startIntent(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    private void location() {
        mLocationClient = new LocationClient(getActivity());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();

    }

    // 初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为3000ms
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public void confirmExit() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_default_ensure_click, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setCancelable(false);
        builder.create().show();
        TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
        tvok.setText("重新登陆");
        TextView tvCancle = (TextView) layout.findViewById(R.id.dialog_default_click_cancel);
        tvCancle.setText("取消");
        TextView tvtitel = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
        tvtitel.setText("温馨提示");
        TextView tvcontent = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
        tvcontent.setText("你的账号在其他设备上登陆？");
        tvok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity1.class);
                i.putExtra("logout", "logout");
                startActivity(i);
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(getActivity()).getUserId());
                if (TextUtils.isEmpty(dbUser.getUserno())) {
                    UserClientBind();
                } else {
                    upload(dbUser.getUserno());
                }
            }
        });

    }
    private void showDialog(String message) {

        DialogTips dialog = new DialogTips(getActivity(), message, "确定");

        dialog.show();
        dialog = null;

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null) {

                return;

            }
            city = location.getCity();
            mLocation = location;

            if(Double.compare(location.getLatitude(),4.9e-324)==0){
                permission=false;
                return;
            }

            getWeather(city);
            permission=true;
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mMyLocationListener);

        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private void getWeather(final String strCity){
        /**
         * 天气接口查询
         */

        requestMaker.WeatherInquiry(strCity, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {

                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("WeatherInquiry");
                    String weather = array.getJSONObject(0).getString("weather");
                    CacheBean cacheBean=new CacheBean();
                    if(dao.findCacheIsExist("WeatherInquiry")){
                        cacheBean.setJson(mySO.toString());
                        cacheBean.setUrl("WeatherInquiry"+"_"+strCity);
                        dao.updateCacheInfo(cacheBean,"WeatherInquiry");
                    }else{
                        cacheBean.setJson(mySO.toString());
                        cacheBean.setUrl("WeatherInquiry"+"_"+strCity);
                        dao.addCacheInfo(cacheBean);
                    }
                    tvweather.setText(weather);
                    float pm2=Float.valueOf(array.getJSONObject(0).getString("pm"));
                    if (Float.compare(pm2, 300.5F) >=0) {
                        tv_pmlv.setText("严重污染");
                    } else if (Float.compare(pm2, 200.5F) >= 0 && Float.compare(pm2, 300.5F) < 0) {
                        tv_pmlv.setText("重度污染");
                    } else if (Float.compare(pm2, 150.5F) >= 0 && Float.compare(pm2, 200.5F) < 0) {
                        tv_pmlv.setText("中度污染");
                    } else if (Float.compare(pm2, 100.5F) >= 0 && Float.compare(pm2, 150.5F) < 0) {
                        tv_pmlv.setText("轻度污染");
                    }else if (Float.compare(pm2, 50.5F) >= 0 && Float.compare(pm2, 100.5F) < 0) {
                        tv_pmlv.setText("良");
                    }else{
                        tv_pmlv.setText("优");
                    }
                    tvpm.setText(array.getJSONObject(0).getString("pm"));
                    tvcurrent.setText(array.getJSONObject(0).getString("currenttemperature"));

                    if (weather.contains("转")) {
                        set(ivweather, weather.substring(0, weather.indexOf("转")));
                    } else {
                        set(ivweather, weather);
                    }
                    tvcity.setText(strCity);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }


    public static BDLocation getLocation() {
        return mLocation;
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getActivity().getPackageName()));
        startActivity(intent);
    }

    /**
     * 设置天气图标
     *
     * @param iv
     * @param str
     */

    private void set(ImageView iv, String str) {
        if (str.contains("雨")) {
            if (str.contains("小雨") || str.contains("阵雨")) {
                iv.setBackgroundResource(R.mipmap.icon_rain_s);
            } else if (str.contains("中雨")) {
                iv.setBackgroundResource(R.mipmap.icon_rain_m);
            } else {
                iv.setBackgroundResource(R.mipmap.icon_rain_b);
            }
        } else if (str.contains("雪")) {
            iv.setBackgroundResource(R.mipmap.icon_snow);
        } else if (str.contains("云")) {
            iv.setBackgroundResource(R.mipmap.icon_cloudy);
        } else if (str.contains("阴")) {
            iv.setBackgroundResource(R.mipmap.icon_overcast);
        } else {
            iv.setBackgroundResource(R.mipmap.icon_sunshine);
        }
    }

    private void newsInqury() {

        requestMaker.NewsInquiry(10 + "", page + "", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("NewsInquiry");

                    if (isReflash) {
                        mList.clear();
                    }
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if(page>1){
                            Toast.makeText(getActivity(), "全部加载完成！", Toast.LENGTH_SHORT).show();
                        }

                        if (isFirst) {
                            isFirst = false;
                        }
                        

                    } else {
                        NewsDate date = JsonTools.getData(result.toString(), NewsDate.class);
                        List<News> list = date.getData();
                        for (News n : list) {
                            mList.add(n);
                        }
                        mAadapter.setList(mList);
                        mAadapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if (isReflash) {
                        isReflash = false;
                        isFirst = false;
                        myScrollView.smoothScrollTo(0, 0);
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else if (isLoading) {
                        isLoading = false;
                        isFirst = false;
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                    } else {
                        myScrollView.smoothScrollTo(0, 0);
                    }
                }

            }
        }));
    }

    private void ReConnetRong() {
        if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
            if (DemoContext.getInstance() != null) {
                String token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
                if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {

                    CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                        @Override
                        public void OnSuccess(String userid) {

                            try {
                                User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(getActivity()).getUserId());
//                                RongIM.getInstance().refreshUserInfoCache(new UserInfo(dbUser.getUserid(), dbUser.getNick(), Uri.parse(dbUser.getImgHead())));

                                RongIM.getInstance().setCurrentUserInfo(new UserInfo(dbUser.getUserid(), dbUser.getNick(), Uri.parse(dbUser.getImgHead())));
                                RongIM.getInstance().setMessageAttachedUserInfo(true);
                                RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
                                initUnreadCountListener();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(getActivity()).getUserId());
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(dbUser.getUserid(), dbUser.getNick(), Uri.parse(dbUser.getImgHead())));
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                    RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
                    initUnreadCountListener();
                }
            }

        }
    }

    private void upload(String no) {
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getWeight())) {
            weight = 50.0f;
        } else {
            weight = Float.parseFloat(SharePreferenceUtil.getInstance(getActivity()).getWeight());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01) / 1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        String currUsr = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker.StepCounterInsert(no, currUsr, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f", calories), sdf.format(new Date()), new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInsert");
                    UserClientBind();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void UserClientBind() {

        String currUsr = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker.loginOut(currUsr, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String UserClientBind = result.toString();
                if (UserClientBind == null) {

                } else {


                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserClientIDChange");
                        StepDetector.CURRENT_SETP = 0;
//                        ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent").toString());
                        RongIM.getInstance().logout();
                        StepBean step = new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(0);
                        step.setUserid("");
                        step.setUploadState(0);
                        dao.updateStep(step);
                        SharePreferenceUtil.getInstance(getActivity()).setUserId("");
                        SharePreferenceUtil.getInstance(getActivity()).setIsRemeber(false);
                        CustomApplcation.getInstance().exit();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
    }

    private void versioninquiry() {
        requestMaker.updaateApk(new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                Map<String, Object> map;
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("VersionInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            newCode = Integer.valueOf(array
                                    .getJSONObject(i).getString(
                                            "VersionID"));
                            int VersionFlag = Integer.valueOf(array
                                    .getJSONObject(i).getString(
                                            "VersionFlag"));
                            PackageManager manager = getActivity()
                                    .getPackageManager();
                            PackageInfo info = manager.getPackageInfo(
                                    getActivity().getPackageName(), 0);
                            String appVersion = info.versionName; // 版本名
                            oldCode = info.versionCode; // 版本号
                            if (oldCode < newCode) {
                                versionlog = array.getJSONObject(i)
                                        .getString("VersionLog")
                                        .replace("@", "\n");
                                if (VersionFlag == 1) {
                                    showForceUpdate();
                                } else {
                                    showUpdateDialog();
                                }

                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));

    }

    private void showForceUpdate() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_default_ensure, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setCancelable(false);
        builder.create().show();
        TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
        TextView tvtitel = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
        tvtitel.setText("检测到新版本");
        TextView tvcontent = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
        tvcontent.setText(versionlog);
        tvok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent it = new Intent(getActivity(),
                        DownloadServiceForAPK.class);
                getActivity().startService(it);

            }
        });
    }

    private void showUpdateDialog() {
        DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
                getActivity()).setDialogMsg("检测到新版本", versionlog, "下载")
                .setOnClickListenerEnsure(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Intent it = new Intent(getActivity(),
                                DownloadServiceForAPK.class);
                        getActivity().startService(it);

                    }
                });
        DialogUtils.showSelfDialog(getActivity(), dialogEnsureCancelView);


    }

    private void initUnreadCountListener() {
//        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
//                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
//                Conversation.ConversationType.PUBLIC_SERVICE};
        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
            }
        }, 500);
    }

    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
            CustomApplcation.getInstance().count = count;

            Intent intenthealth = new Intent("NumRefresh");
            getActivity().sendBroadcast(intenthealth);

        }
    };

    private void initHomeFamily() {
        String userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        User dbUser = dao.findUserInfoById(userid);
        RelationDes rs = new RelationDes();
        rs.setUser_Name(dbUser.getUsername());
        rs.setUser_Icon(dbUser.getImgHead());
        rs.setRelationship("自己");
        rs.setRUserID(userid);
        linearLayout.addMainItem(rs);
        getToken(userid, dbUser.getUsername(), dbUser.getImgHead());

        requestMaker.UserRelationshipInquiry(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("UserRelationshipInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        List<RelationDes> list = new ArrayList<RelationDes>();
                        RelationDes res = new RelationDes();
                        res.setRelationship("添加亲人");
                        list.add(res);
                        final List<RelationDes> templist = list;
                        linearLayout.addItem(templist, new MyHomeLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos) {
                                rsTemp = linearLayout.getMainItem();
                                rsTarget = templist.get(pos);
                                UserClientBindChange(rsTemp.getRUserID(), rsTarget.getRUserID(), pos);
                            }
                        });

                    } else {
                        RelationBean date = JsonTools.getData(result.toString(), RelationBean.class);
                        List<RelationDes> list = date.getData();
                        for (RelationDes rs : list) {
                            if (dao.findRsIsExist(rs.getRUserID())) {
                                dao.updateResInfo(rs, rs.getRUserID());
                            } else
                                dao.addRelationInfo(rs);
                        }
                        RelationDes res = new RelationDes();
                        res.setRelationship("添加亲人");
                        list.add(res);
                        final List<RelationDes> templist = list;
                        linearLayout.addItem(list, new MyHomeLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos) {
                                rsTemp = linearLayout.getMainItem();
                                rsTarget = templist.get(pos);

                                UserClientBindChange(rsTemp.getRUserID(), rsTarget.getRUserID(), pos);
                            }
                        });

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    List<RelationDes> list = new ArrayList<RelationDes>();
                    RelationDes res = new RelationDes();
                    res.setRelationship("Add");
                    list.add(res);
                    final List<RelationDes> templist = list;
                    linearLayout.addItem(templist, new MyHomeLayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {
                            rsTemp = linearLayout.getMainItem();
                            rsTarget = templist.get(pos);
                            UserClientBindChange(rsTemp.getRUserID(), rsTarget.getRUserID(), pos);
                        }
                    });
                    ReConnetRong();
                }
            }
        }));


    }

    private void initHomeNotUserId() {
        final String homeid = SharePreferenceUtil.getInstance(getActivity()).getHomeId();
//        String userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
//        RelationDes rs=dao.findRelationInfoById(userid);
//        linearLayout.addMainItem(rs);
        requestMaker.UserRelationshipInquiry(homeid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("UserRelationshipInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        ReConnetRong();
                    } else {
                        RelationBean date = JsonTools.getData(result.toString(), RelationBean.class);
                        List<RelationDes> list = date.getData();
                        for(RelationDes rs:list){
                            if(dao.findRsIsExist(rs.getRUserID())){
                                dao.updateResInfo(rs,rs.getRUserID());
                            }else{
                                dao.addRelationInfo(rs);
                            }
                        }

                        readCache();
                        CustomApplcation.getInstance().isRead=true;

                    }
                    ReConnetRong();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    private void UserClientBindChange(String userid, final String loginid, final int pos) {
        startProgressDialogTitle("正在切换中");

        requestMaker.loginOut(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String UserClientBind = result.toString();
//               ToastUtils.showMessage(getActivity(),UserClientBind);
                if (UserClientBind == null) {


                } else {
                    getUser(loginid, pos);
                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserClientIDChange");
                        StepDetector.CURRENT_SETP = 0;
                        RongIM.getInstance().logout();
                        RongIM.getInstance().disconnect();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
    }

    private void getUser(String userid, final int pos) {
        requestMaker.UserInquiry(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                UserInfoDate date = JsonTools.getData(result.toString(), UserInfoDate.class);
//               ToastUtils.showMessage(getActivity(),result.toString());
                android.util.Log.e("psd", result.toString() + "<<<<----------->");
                List<User> list = date.getData();
                User user2 = list.get(0);
                login(user2.getMobile(), user2.getUserPassword(), pos);

            }

        }));
    }

    public void login(final String mobile, final String psd, final int pos) {
        requestMaker.userLogin(mobile, psd, ClientID, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                if (result != null) {
                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("UserLogin");

//                        ToastUtils.showMessage(getActivity(),result.toString());
                        if (array.getJSONObject(0).has("MessageCode")) {

                        } else {
                            UserDate date = JsonTools.getData(result.toString(), UserDate.class);
                            List<User> list = date.getData();
                            String imgHead = list.get(0).getImgHead();
                            if (imgHead != null) {
                                if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                                    imgHead = "";
                                } else {
                                    imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");
                                }
                            } else {
                                imgHead = "";
                            }
                            list.get(0).setImgHead(imgHead);
                            list.get(0).setPassword(psd);
                            list.get(0).setMobile(mobile);


                            if (!dao.findUserIsExist(list.get(0).getUserid())) {
                                User user=list.get(0);
                                user.setType(1);
                                dao.addUserInfo(list.get(0));
                            } else {
                                User dbUser = dao.findUserInfoById(list.get(0).getUserid());
                                dbUser.setImgHead(imgHead);
                                dbUser.setPassword(psd);
                                dbUser.setMobile(mobile);
                                dbUser.setPatientId(list.get(0).getPatientId());
                                if (list.get(0).getSex() != null) {
                                    dbUser.setSex(list.get(0).getSex());
                                }
                                if (list.get(0).getUserno() != null) {
                                    dbUser.setUserno(list.get(0).getUserno());
                                }
                                if (list.get(0).getAge() != null) {
                                    dbUser.setAge(list.get(0).getAge());
                                }
                                if (list.get(0).getUsername() != null) {
                                    dbUser.setUsername(list.get(0).getUsername());
                                }
                                if (list.get(0).getUserHeight() != null) {
                                    dbUser.setUserHeight(list.get(0).getUserHeight());
                                }
                                if (list.get(0).getUserno() != null) {
                                    dbUser.setUserno(list.get(0).getUserno());
                                }
                                dao.updateUserInfo(dbUser, list.get(0).getUserid());
                            }
                            SharePreferenceUtil.getInstance(getActivity()).setUserId(array.getJSONObject(0).getString("UserID"));
                            getToken(list.get(0).getUserid(), list.get(0).getUsername(), imgHead);
                            de.greenrobot.event.EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                            linearLayout.changePostion(pos);
                            CustomApplcation.getInstance().isRead=true;
                            Intent intenthealth = new Intent("userrefresh");
                            getActivity().sendBroadcast(intenthealth);
                            stopProgressDialog();
                            getBaseData();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }));
    }
    /**
     * type 2,空，3，更新
     */
    public void getBaseData(){
        final String id=SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker.BaseDataInquiry(id, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                Log.e("JSONObject",mySO.toString());
                int type=0;

                try {
                    JSONArray json = mySO.getJSONArray("BaseDataInquiry");
                    if (json.getJSONObject(0).has("MessageCode")) {
                        String MessageCode = json.getJSONObject(0).getString("MessageCode");
                        if ("2".equals(MessageCode)) {
                            type=2;
                        }
                        if ("1".equals(MessageCode)) {
                            type=1;
                        }
                    } else {
                        type=3;

                    }
                    User user=dao.findUserInfoById(id);
                    user.setType(type);
                    dao.updateUserInfo(user, id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));
    }
    /**
     * 获得token
     */
    private void getToken(final String userid, final String name, final String head) {

        requestMaker.getToken(userid, name, head, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("GetToken");
                    if (array.getJSONObject(0).getString("MessageCode").toString().equals("0")) {
                        String token = array.getJSONObject(0).getString("MessageContent").toString();
                        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                        edit.putString("DEMO_TOKEN", token);
                        edit.apply();
                        if(head.contains("vine.gif")){
                            RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, name, Uri.parse("")));
                        }else {
                            RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, name, Uri.parse(head)));
                        }
                        CustomApplcation.getInstance().isOnLine=1;
                        ReConnetRong();
                    } else {
                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CustomApplcation.getInstance().isOnLine=0;
                }

            }
        }));


    }

    public void hideFamily() {
        if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId()))
            linearLayout.hide();
    }
    private void readCache(){
        String homeid=SharePreferenceUtil.getInstance(getActivity()).getHomeId();
        User dbUser = dao.findUserInfoById(homeid);
        final RelationDes rsme = new RelationDes();
        rsme.setUser_Name(dbUser.getUsername());
        rsme.setUser_Icon(dbUser.getImgHead());
        rsme.setRUserID(homeid);
        rsme.setRelationship("自己");
        String userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
        RelationDes rs=dao.findRelationInfoById(userid);
        linearLayout.addMainItem(rs);
        User dbOrder=dao.findUserInfoById(homeid);
        String[] orders=dbOrder.getOrder().split(",");
        List<RelationDes> listTemp = new ArrayList<RelationDes>();
        for(int n=0;n<orders.length;n++){
            RelationDes rsDao=dao.findRelationInfoById(orders[n]);
            if(orders[n].equals(homeid)){
                listTemp.add(rsme);
            }else {
                listTemp.add(rsDao);
            }
        }
        RelationDes res = new RelationDes();
        res.setRelationship("添加亲人");
        listTemp.add(res);
        final List<RelationDes> listfinaltmp = listTemp;
        linearLayout.addItem(listfinaltmp, new MyHomeLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                rsTemp = linearLayout.getMainItem();
                rsTarget = listfinaltmp.get(pos);
                UserClientBindChange(rsTemp.getRUserID(),rsTarget.getRUserID(), pos);
            }
        });
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
        return bean;
    }
    private Boolean  checkCardNo(String  userid){
        User dbUser=dao.findUserInfoById(userid);
        if (TextUtils.isEmpty(dbUser.getUserno())) {
            completeInfoTips();
            return false;
        }else{
            return true;
        }
    }
    private void showGuide() {

        DecorViewGuideHelper helper = new DecorViewGuideHelper(getActivity(),guid);
        helper.display();
    }
    /**
     * 如果用户信息不完善，显示提示框
     */
    public void completeInfoTips() {
        DialogTips dialog = new DialogTips(getActivity(), "", "用户信息缺失，请先完善信息",
                "去完善", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startActivity(new Intent(getActivity(), PersonalCenterInfo.class));
            }
        });

        dialog.show();
        dialog = null;
    }

    public  void showTitleDialog(String message) {

        DialogTips dialog = new DialogTips(getActivity(), message, "确定");

        dialog.show();
        dialog = null;

    }


}
