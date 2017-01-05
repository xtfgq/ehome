package com.zzu.ehome.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.CholesterolActivity;
import com.zzu.ehome.activity.JibuDataActivity;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.activity.MedicalRecordsActivity;
import com.zzu.ehome.activity.PersonalCenterInfo;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.activity.TiwenDataActivity;
import com.zzu.ehome.activity.TizhongDataActivity;
import com.zzu.ehome.activity.UricacidActivity;
import com.zzu.ehome.activity.XuetangDataActivity;
import com.zzu.ehome.activity.XueyaDataActivity;
import com.zzu.ehome.activity.YYJLDataActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealthBean;
import com.zzu.ehome.bean.HealthDataSearchByDate;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.bean.StepCounterDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.zzu.ehome.R.id.textView;

/**
 * Created by zzu on 2016/4/9.
 */
public class HealthDataFragment extends BaseFragment implements View.OnClickListener {
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View view;
    private RequestMaker requestMaker;
    private String userid, date, CurrnetDate;
    private TextView tz_num, tvbmi, tw_num, xt_num, xy_num, tvdate, tw_status, tv3, tv5, tv_yp, tv_xy, tv_xt, yp_num, tv_bimstatus, jibu_num, textView9;
    private ProgressBar pbweight, pbtw, pbBloodSuggar, pbPress1, pbPress2, pbDate,pbLs,pbCh;
    Map<String, HealthBean> map = new HashMap<String, HealthBean>();
    private LinearLayout layout_tz, layout_tw, layout_xt, layout_xy, layout_yp, layout_jibu, layout_jl;
    private ImageView imageView_lift, imageView_right;
    User dbUser;
    private EHomeDao dao;
    private RequestMaker requestmaker;
    private View vTop;
    private TextView tv_time, tv_address;
    private Boolean isToday=true;
    private Boolean isFirst=true;
    private Boolean isChange=false;
    private String cardNo="";
    private LinearLayout layout_ls;
    private LinearLayout layout_dgc;
    private TextView dgc_num,ls_num,textViewDgc2,textViewLs2;
    private CompositeSubscription compositeSubscription;
    private SupperBaseActivity activity;
    private BroadcastReceiver mRefrushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.Weight")) {
                dbUser = dao.findUserInfoById(userid);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                CurrnetDate = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
                date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
                tvdate.setText(DateUtils.getDateDetail(date) + DateUtils.StringPattern(date, "yyyy-MM-dd", "MM月dd日"));
                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                initData();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    private BroadcastReceiver mRefreshReciver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("userrefresh")){
                userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if(!TextUtils.isEmpty(userid)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    dbUser = dao.findUserInfoById(userid);
                    dbUser.getUserno();
                    CurrnetDate = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
                    date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
                    tvdate.setText(DateUtils.getDateDetail(date) + DateUtils.StringPattern(date, "yyyy-MM-dd", "MM月dd日"));
                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                    imageView_lift.setVisibility(View.VISIBLE);
                    imageView_right.setVisibility(View.INVISIBLE);
                    isChange = true;
                    initData();
                }else{
                    tz_num.setText("0");
                    tw_num.setText("0");
                    xt_num.setText("0");
                    xy_num.setText("0/0");
                    dgc_num.setText("0/0");
                    ls_num.setText("0/0");
                    pbweight.setProgress(0);
                    pbtw.setProgress(0);
                    pbBloodSuggar.setProgress(0);
                    pbweight.setProgress(0);
                    pbPress1.setProgress(0);
                    pbPress2.setProgress(0);
                    tw_status.setVisibility(View.GONE);
                    tv3.setVisibility(View.GONE);
                    tv5.setVisibility(View.GONE);
                    tvbmi.setText("BMI: 0");
                    tv_bimstatus.setVisibility(View.GONE);
                    yp_num.setText(0 + "");
                    tv_yp.setText("药品名");
                    jibu_num.setText("0");
                    tv_time.setText("暂无就诊数据");
                    tv_address.setText("");
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_healthdata, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao = new EHomeDaoImpl(getActivity());
        dbUser = dao.findUserInfoById(userid);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.Weight");
        intentFilter.addAction("userrefresh");
        getActivity().registerReceiver(mRefrushBroadcastReceiver, intentFilter);
        getActivity().registerReceiver(mRefreshReciver, intentFilter);
        mPermissionsChecker = new PermissionsChecker(getActivity());
        initView();
        initEvent();
        getData();
        return view;
    }

    public void initView() {

        setOnlyTileViewMethod(view, "日常记录");
        tz_num = (TextView) view.findViewById(R.id.tz_num);
        tvbmi = (TextView) view.findViewById(R.id.tv_bim);
        tw_num = (TextView) view.findViewById(R.id.tw_num);
        pbweight = (ProgressBar) view.findViewById(R.id.pb_progressbar);
        pbtw = (ProgressBar) view.findViewById(R.id.progressBar1);
        xt_num = (TextView) view.findViewById(R.id.xt_num);
        pbBloodSuggar = (ProgressBar) view.findViewById(R.id.progressBar2);
        xy_num = (TextView) view.findViewById(R.id.xy_num);
        pbPress1 = (ProgressBar) view.findViewById(R.id.progressBar3);
        pbPress2 = (ProgressBar) view.findViewById(R.id.progressBar4);
        layout_tz = (LinearLayout) view.findViewById(R.id.layout_tz);
        layout_jibu = (LinearLayout) view.findViewById(R.id.layout_jibu);
        layout_tw = (LinearLayout) view.findViewById(R.id.layout_tw);
        layout_xt = (LinearLayout) view.findViewById(R.id.layout_xt);
        layout_xy = (LinearLayout) view.findViewById(R.id.layout_xy);
        layout_yp = (LinearLayout) view.findViewById(R.id.layout_yp);
        imageView_lift = (ImageView) view.findViewById(R.id.imageView_lift);
        imageView_right = (ImageView) view.findViewById(R.id.imageView_right);
        tvdate = (TextView) view.findViewById(textView);
        jibu_num = (TextView) view.findViewById(R.id.jibu_num);
        textView9 = (TextView) view.findViewById(R.id.textView9);
        tw_status = (TextView) view.findViewById(R.id.textView2);
        tv_yp = (TextView) view.findViewById(R.id.tv_yp);
        tv_xy = (TextView) view.findViewById(R.id.tv_xy);
        tv_xt = (TextView) view.findViewById(R.id.tv_xt);
        yp_num = (TextView) view.findViewById(R.id.yp_num);
        tv_bimstatus = (TextView) view.findViewById(R.id.tv_bimstatus);
        pbDate = (ProgressBar) view.findViewById(R.id.progressBar8);
        layout_jl = (LinearLayout) view.findViewById(R.id.layout_jl);
        dgc_num=(TextView)view.findViewById(R.id.dgc_num);
        ls_num=(TextView)view.findViewById(R.id.ls_num);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv5 = (TextView) view.findViewById(R.id.tv5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
        tvdate.setText(DateUtils.getDateDetail(date) + DateUtils.StringPattern(date, "yyyy-MM-dd", "MM月dd日"));
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        layout_ls=(LinearLayout)view.findViewById(R.id.layout_ls);
        layout_dgc=(LinearLayout)view.findViewById(R.id.layout_dgc);
        pbLs= (ProgressBar) view.findViewById(R.id.progressLsBar1);
        pbCh=(ProgressBar) view.findViewById(R.id.progressDgcBar1);
        textViewLs2=(TextView)view.findViewById(R.id.textViewLs2);
        textViewDgc2=(TextView)view.findViewById(R.id.textViewDgc2);
        vTop = view.findViewById(R.id.v_top);

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
        layout_tz.setOnClickListener(this);
        layout_tw.setOnClickListener(this);
        layout_xy.setOnClickListener(this);
        layout_xt.setOnClickListener(this);
        layout_yp.setOnClickListener(this);
        imageView_lift.setOnClickListener(this);
        imageView_right.setOnClickListener(this);
        layout_jibu.setOnClickListener(this);
        layout_jl.setOnClickListener(this);
        layout_ls.setOnClickListener(this);
        layout_dgc.setOnClickListener(this);
        //创建compositeSubscription实例
        compositeSubscription = new CompositeSubscription();

        //监听订阅事件
        Subscription subscription = RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }

                        if (event instanceof EventType){
                            EventType type=(EventType)event;
                            if(Constants.HealthData.equals(type.getType())){
                                getData();
                            }
                        }

                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
    }
    private void getData(){
        if(!TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dbUser = dao.findUserInfoById(userid);
            CurrnetDate = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
            date = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
            tvdate.setText(DateUtils.getDateDetail(date)+"  " + DateUtils.StringPattern(date, "yyyy-MM-dd", "MM月dd日"));
            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
            imageView_lift.setVisibility(View.VISIBLE);
            imageView_right.setVisibility(View.INVISIBLE);

            initData();
        }
    }

    public void initData() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;
        }
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        if(!TextUtils.isEmpty(userid)) {
            dbUser = dao.findUserInfoById(userid);
            cardNo = dbUser.getUserno();
        }
        pbtw.setProgress(0);
        pbweight.setProgress(0);
        pbBloodSuggar.setProgress(0);
        pbPress1.setProgress(0);
        pbPress2.setProgress(0);
        startProgressDialog();

        requestMaker.HealthDataSearchByDate(cardNo,userid, date, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("HealthDataSearchByDate");
                    getJIbu();
                    getTreatmentInquiryLatest(userid, date);
                    stopProgressDialog();

                    if (array.getJSONObject(0).has("MessageCode")) {
                        tz_num.setText("0");
                        tw_num.setText("0");
                        xt_num.setText("0");
                        xy_num.setText("0/0");
                        dgc_num.setText("0/0");
                        ls_num.setText("0/0");
                        pbweight.setProgress(0);
                        pbtw.setProgress(0);
                        pbBloodSuggar.setProgress(0);
                        pbweight.setProgress(0);
                        pbPress1.setProgress(0);
                        pbPress2.setProgress(0);
                        tw_status.setVisibility(View.GONE);
                        tv3.setVisibility(View.GONE);
                        tv5.setVisibility(View.GONE);
                        tvbmi.setText("BMI: 0");
                        tv_bimstatus.setVisibility(View.GONE);
                        yp_num.setText(0 + "");
                        tv_yp.setText("药品名");

                    } else {
                        HealthDataSearchByDate date = JsonTools.getData(result.toString(), HealthDataSearchByDate.class);
                        List<HealthBean> list = date.getData();
                        map.clear();
                        for (HealthBean bean : list) {
                            map.put(bean.getType(), bean);
                        }
                        if (map.get("Weight") != null) {

                            tz_num.setText(map.get("Weight").getValue1());
                            if (!TextUtils.isEmpty(dbUser.getUserHeight())) {
                                tv_bimstatus.setVisibility(View.VISIBLE);

                                float bmi = Float.valueOf(map.get("Weight").getValue3());
                                BigDecimal b = new BigDecimal(bmi);
                                tv_bimstatus.setText(CommonUtils.showBMIDetail(b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue()));
                                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                                tvbmi.setText("BMI: " + decimalFormat.format(bmi));
                                String showBmi = CommonUtils.showBMIDetail(bmi);
                                if (showBmi.equals(CommonUtils.TINY)) {
                                    tv_bimstatus.setTextColor(getResources().getColor(R.color.normal_button));
                                } else if (showBmi.equals(CommonUtils.NORMAL)) {
                                    tv_bimstatus.setTextColor(getResources().getColor(R.color.actionbar_color));
                                } else if (showBmi.equals(CommonUtils.OVERLOAD)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#fa3b00"));
                                } else if (showBmi.equals(CommonUtils.SAMLL)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#97dee9"));
                                } else if (showBmi.equals(CommonUtils.MIDDLE)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#ea0b35"));
                                } else if (showBmi.equals(CommonUtils.BIG)) {
                                    tv_bimstatus.setTextColor(Color.parseColor("#fa5779"));
                                }
                            } else {
                                tv_bimstatus.setVisibility(View.GONE);
                            }
                            float num = Float.valueOf(map.get("Weight").getValue1()) / 150.0f;
                            pbStartAnima(pbweight, (int) (num * 100));

                        } else {
                            tz_num.setText("0");
                            pbweight.setProgress(0);
                            pbStartAnima(pbweight, 0);
                            tvbmi.setText("BMI: 0");
                            tv_bimstatus.setVisibility(View.GONE);
                            tv_bimstatus.setText("");

                        }
                        if (map.get("Temperature") != null) {

                            float num = (Float.valueOf(map.get("Temperature").getValue1()) - 35.0f) / (45.0f - 35.0f);
                            pbStartAnima(pbtw, (int) (num * 100));
                            tw_num.setText(map.get("Temperature").getValue1());

                            float tw = Float.parseFloat(map.get("Temperature").getValue1());
                            tw_status.setVisibility(View.VISIBLE);
                            if (Float.compare(tw, 37.1F) >= 0 && Float.compare(tw, 38F) <= 0) {
                                tw_status.setText("低热");
                                tw_status.setTextColor(Color.parseColor("#f9a116"));


                            } else if (Float.compare(tw, 38.1F) >= 0 && Float.compare(tw, 39F) <= 0) {
                                tw_status.setText("中等度热");
                                tw_status.setTextColor(Color.parseColor("#fb7701"));

                            } else if (Float.compare(tw, 39.1F) >= 0 && Float.compare(tw, 41) <= 0) {
                                tw_status.setText("高热");
                                tw_status.setTextColor(Color.parseColor("#fa3b00"));

                            } else if (Float.compare(tw, 41F) > 0) {

                                tw_status.setText("超高热");
                                tw_status.setTextColor(Color.parseColor("#ea0b35"));

                            } else {
                                tw_status.setText("正常");

                                tw_status.setTextColor(getResources().getColor(R.color.actionbar_color));
                            }

                        } else {
                            tw_num.setText("0");
                            tw_status.setVisibility(View.GONE);
                            pbStartAnima(pbtw, 0);
                        }
                        if (map.get("BloodSugar") != null) {
                            float bnum = Float.valueOf(map.get("BloodSugar").getValue1());
                            if (Integer.valueOf(map.get("BloodSugar").getValue3()) == 2) {
                                bnum = bnum / 18.0f;
                            }
                            float num = bnum / 33.0f;
                            DecimalFormat decimalFormat = new DecimalFormat("0.0");
                            xt_num.setText(decimalFormat.format(bnum));
                            tv3.setVisibility(View.VISIBLE);
                            String time="";
                            if (Integer.valueOf(map.get("BloodSugar").getValue2())==1) {

                                time="餐后";
                            } else if(Integer.valueOf(map.get("BloodSugar").getValue2())==0) {

                                time="空腹";
                            }else{

                                time="随机";
                            }
                            checkSuager(bnum, time);
                            pbStartAnima(pbBloodSuggar, (int) (num * 100));

                        } else {
                            xt_num.setText("0");
                            pbStartAnima(pbBloodSuggar, 0);
                            tv3.setVisibility(View.GONE);

                        }
                        if (map.get("BloodPressure") != null) {
                            xy_num.setText(map.get("BloodPressure").getValue1() + "/" + map.get("BloodPressure").getValue2());
                            float num1 = (Float.valueOf(map.get("BloodPressure").getValue1())) / 300f;
                            float num2 = (Float.valueOf(map.get("BloodPressure").getValue2())) / 300f;
                            pbStartAnima(pbPress1, (int) (num1 * 100));
                            pbStartAnima(pbPress2, (int) (num2 * 100));
                            tv5.setVisibility(View.VISIBLE);
                            changeImgeView(tv5, Integer.valueOf(map.get("BloodPressure").getValue1()), Integer.valueOf(map.get("BloodPressure").getValue2()));
                        } else {
                            tv5.setVisibility(View.GONE);
                            xy_num.setText("0/0");
                            pbStartAnima(pbPress1, 0);
                            pbStartAnima(pbPress2, 0);
                        }
                        if (map.get("MedicationRecord") != null) {

                            yp_num.setText(map.get("MedicationRecord").getValue2());
                            tv_yp.setText("药品名：" + map.get("MedicationRecord").getValue1());
                        } else {
                            yp_num.setText(0 + "");
                            tv_yp.setText("药品名 ");
                        }
                        if (map.get("Cholestenone") != null) {

                            dgc_num.setText(map.get("Cholestenone").getValue1());
                            float numCh = (Float.valueOf(map.get("Cholestenone").getValue1())) / 8f;
                            pbStartAnima(pbCh, (int) (numCh * 100));
                            Double ch=Double.valueOf(map.get("Cholestenone").getValue1());
                            if ( Double.compare(ch, 5.18d) <= 0) {
                                textViewDgc2.setText("正常");
                                textViewDgc2.setTextColor(getResources().getColor(R.color.actionbar_color));

                            } else  {
                                textViewDgc2.setText("偏高");
                                textViewDgc2.setTextColor(Color.parseColor("#fa3b00"));

                            }

                        } else {
                            dgc_num.setText("0/0");
                            pbStartAnima(pbCh, 0);
                        }
                        if (map.get("LithicAcid") != null) {

                            ls_num.setText(map.get("LithicAcid").getValue1());
                            float num1 = (Float.valueOf(map.get("LithicAcid").getValue1())) / 1f;
                            pbStartAnima(pbLs,  (int) (num1 * 100));
                            Double ls=Double.valueOf(map.get("LithicAcid").getValue1());
                            if ( Double.compare(ls, 0.089d) <= 0) {
                                textViewLs2.setText("偏低");
                                textViewLs2.setTextColor(Color.parseColor("#f9a116"));

                            } else if ( Double.compare(ls, 0.375d) >= 0) {
                                textViewLs2.setText("偏高");
                                textViewLs2.setTextColor(Color.parseColor("#fa3b00"));

                            }  else {
                                textViewLs2.setText("正常");
                                textViewLs2.setTextColor(getResources().getColor(R.color.actionbar_color));


                            }

                        } else {
                            ls_num.setText("0/0");
                            pbStartAnima(pbLs, 0);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }));
    }

    private void getTreatmentInquiryLatest(String usrid, String date) {
        requestMaker.TreatmentInquiryLatest(usrid, date, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentInquiryLatest");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        tv_time.setText("暂无就诊数据");
                        tv_address.setText("");
                    } else {
                        tv_time.setText(DateUtils.StringPattern(array.getJSONObject(0).getString("Treatment_AppointmentTime"), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd"));
                        tv_address.setText(array.getJSONObject(0).getString("Treatment_Unit"));


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    tv_time.setText("暂无就诊数据");
                    tv_address.setText("");
                }
            }
        }));
    }

    private void getJIbu() {
        /**
         * 查询历史数据
         */

        if(!isFirst) {
            if (!TextUtils.isEmpty(userid) && date.equals(CurrnetDate)) {
                jibu_num.setText(StepDetector.CURRENT_SETP + "");
                isToday = true;
            } else {
                isToday = false;
            }
        }
        dbUser=dao.findUserInfoById(userid);
        cardNo=dbUser.getUserno();

        requestMaker.StepCounterInquiry(cardNo,userid,date, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {

                        if(!isToday) {
                            jibu_num.setText("0");
                        }
                        if(isFirst){
                            jibu_num.setText("0");
                        }

                        textView9.setText("距离" + "0" + "公里");
                        pbStartAnima(pbDate, 0);
                        isFirst=false;

                    } else {

                        StepCounterDate date = JsonTools.getData(result.toString(), StepCounterDate.class);
                        List<StepCounterBean> list = date.getData();
                        if(!isToday||isFirst||isChange) {
                            jibu_num.setText(list.get(0).getTotalStep());
                        }

                        isChange=false;
                        isFirst=false;
                        textView9.setText("距离" + list.get(0).getTotalDistance() + "公里");
                        float num = Float.valueOf(list.get(0).getTotalStep()) / 10000f;
                        if (Float.compare(num, 1.00f) > 0) {
                            num = 1.00f;
                        }
                        pbStartAnima(pbDate, (int) (num * 100));
                        StepDetector.CURRENT_SETP = Integer.valueOf(list.get(0).getTotalStep());
                        StepBean step = new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(StepDetector.CURRENT_SETP);
                        step.setUserid(userid);
                        step.setUploadState(0);
                        dao.updateStep(step);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }));
    }

    private void changeImgeView(TextView tv, int ssz, int szy) {
        int lvssz = CommonUtils.computeSsz(ssz);
        int lvszy = CommonUtils.computeSzy(szy);
        int lv = CommonUtils.MaxInt(lvssz, lvszy);
        switch (lv) {
            case -1:
                tv.setText("低血压");
                tv.setTextColor(getResources().getColor(R.color.dxy_color));
                break;
            case 0:
                tv.setText("血压正常");
                tv.setTextColor(getResources().getColor(R.color.actionbar_color));
                break;
            case 1:

                tv.setText("高血压一期");
                tv.setTextColor(Color.parseColor("#fb7701"));
                break;
            case 2:

                tv.setText("高血压二期");
                tv.setTextColor(Color.parseColor("#fa3b00"));
                break;
            case 3:

                tv.setText("高血压三期");
                tv.setTextColor(Color.parseColor("#ea0b35"));
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void checkSuager(float value, String time) {
        if (time.contains("餐后") ) {
            if (Float.compare(value, 7.6F) >= 0 && Float.compare(value, 11.1F) <= 0) {

                tv3.setText("正常");
                tv3.setTextColor(getResources().getColor(R.color.actionbar_color));
            } else if (Float.compare(value, 11.1F) > 0) {
                tv3.setText("高血糖");
                tv3.setTextColor(Color.parseColor("#ea0b35"));
            } else {
                tv3.setText("低血糖");
                tv3.setTextColor(Color.parseColor("#fb7701"));
            }

        }else if(time.contains("空腹")){
            if (Float.compare(value, 3.9F) >= 0 && Float.compare(value, 6.1F) <= 0) {

                tv3.setText("正常");
                tv3.setTextColor(getResources().getColor(R.color.actionbar_color));
            } else if (Float.compare(value, 6.1F) > 0) {
                tv3.setText("高血糖");
                tv3.setTextColor(Color.parseColor("#ea0b35"));
            } else {
                tv3.setText("低血糖");
                tv3.setTextColor(Color.parseColor("#fb7701"));
            }
        }
        else{
            if (Float.compare(value, 3.9F) < 0) {
                tv3.setText("低血糖");
                tv3.setTextColor(Color.parseColor("#fb7701"));

            } else if (Float.compare(value, 3.9F) >= 0 && Float.compare(value, 11.1F) <= 0) {
                tv3.setText("正常");
                tv3.setTextColor(getResources().getColor(R.color.actionbar_color));

            } else {
                tv3.setText("高血糖");
                tv3.setTextColor(Color.parseColor("#ea0b35"));

            }

        }


    }

    public static Fragment getInstance() {
        return new HealthDataFragment();
    }

    @Override
    protected void lazyLoad() {

    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().unregisterReceiver(mRefrushBroadcastReceiver);

            mRefrushBroadcastReceiver = null;
            getActivity().unregisterReceiver(mRefreshReciver);
            compositeSubscription.unsubscribe();
            mRefreshReciver = null;
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;
        }
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())) {
            startActivity(new Intent(getActivity(), LoginActivity1.class));
            return;
        }

        switch (v.getId()) {
            case R.id.layout_tz:
                if(checkCardNo()){
                    intentAction(getActivity(), TizhongDataActivity.class, date, 1);
                };

                break;
            case R.id.layout_jibu:
//                if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getWeight())){
//                    intentAction(getActivity(), TizhongActivity.class,date,1);
//                }else {
                if(checkCardNo()) {
                    intentAction(getActivity(), JibuDataActivity.class, date, 5);
                }
//                }
                break;
            case R.id.layout_tw:
                if(checkCardNo()) {
                    intentAction(getActivity(), TiwenDataActivity.class, date, 0);
                }
                break;
            case R.id.layout_xt:
                if(checkCardNo()) {
                    intentAction(getActivity(), XuetangDataActivity.class, date, 3);
                }
                break;
            case R.id.layout_xy:
                if(checkCardNo()) {
                    intentAction(getActivity(), XueyaDataActivity.class, date, 2);
                }
                break;
            case R.id.layout_yp:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        showMissingPermissionDialog();
                        return;
                    }
                }
                if(checkCardNo()){
                    intentAction(getActivity(), YYJLDataActivity.class, date, 4);
                };

                break;
            case R.id.imageView_lift:
                try {
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date day = fmt.parse(date);
                    date = fmt.format(day.getTime() - 60 * 60 * 24 * 1000);
                    initData();
                    tvdate.setText(DateUtils.getDateDetail(date) + DateUtils.StringPattern(date, "yyyy-MM-dd", "MM月dd日"));
                    imageView_right.setVisibility(View.VISIBLE);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.imageView_right:
                try {
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date day = fmt.parse(date);
                    date = fmt.format(day.getTime() + 60 * 60 * 24 * 1000);
                    initData();
                    tvdate.setText(DateUtils.getDateDetail(date) + DateUtils.StringPattern(date, "yyyy-MM-dd", "MM月dd日"));
                    if (date.equals(CurrnetDate)) {
                        imageView_right.setVisibility(View.INVISIBLE);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.layout_jl:
                if(checkCardNo())
                    getActivity().startActivity(new Intent(getActivity(), MedicalRecordsActivity.class));
                break;
            case R.id.layout_ls:
                if(checkCardNo())
                    getActivity().startActivity(new Intent(getActivity(), UricacidActivity.class));
                break;
            case R.id.layout_dgc:
                if(checkCardNo())
                    getActivity().startActivity(new Intent(getActivity(), CholesterolActivity.class));
                break;
        }
    }

    public <T> void intentAction(Activity context, Class<T> cls, String time, int postion) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("position", postion);
        intent.putExtra("time", time);
        startActivity(intent);
    }

    public void pbStartAnima(final ProgressBar pb, final int progress) {
        new Thread() {
            public void run() {
                int v1 = 1;
                while (v1 <= progress) {
                    try {
                        if (progress > 50) {
                            v1 += 10;
                        } else
                            v1 += 5;
                        Thread.sleep(50);
                        pb.setProgress(v1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pb.setProgress(progress);
            }
        }.start();

    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(getActivity(), "请点击设置，打开所需存储权限",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startAppSettings();

            }
        });

        dialog.show();
        dialog = null;

    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getActivity().getPackageName()));
        startActivity(intent);
    }
    private Boolean  checkCardNo(){
        userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
        User dbUser=dao.findUserInfoById(userid);
        if (TextUtils.isEmpty(dbUser.getUserno())) {
            completeInfoTips();
            return false;
        }else{
            return true;
        }
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



}
