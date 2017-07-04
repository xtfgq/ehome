package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yiguo.toast.Toast;/**/

import com.zzu.ehome.R;
import com.zzu.ehome.activity.SelectDateAndTime;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealteTempData;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TempRes;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.OnSelectItemListener;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.ScaleMarkView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zzu on 2016/4/12.
 */
public class TempFragment extends BaseFragment {
    private View view;
    private ScaleMarkView mHeight;
    private int height;
    private TextView tvheight, tvcltime;
    private RelativeLayout rlchecktime;
    private float tw;
    private RequestMaker requestMaker;
    private Button btnsave;
    private String userid,cardNo;
    private String checktime;
    private OnSelectItemListener mListener;
    public static String postion = "";
    private EHomeDao dao;
    private User dbUser;
    private SupperBaseActivity activity;

    public static int p = -1;
    private CompositeSubscription compositeSubscription;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_height, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao = new EHomeDaoImpl(getActivity());
        dbUser=dao.findUserInfoById(userid);
        cardNo=dbUser.getUserno();
//        getTemperature();
        initViews();
        initEvents();
        getTemperature();
        if (!CommonUtils.isNotificationEnabled(getActivity())) {
            activity.showTitleDialog("请打开通知中心");
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSelectItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnArticleSelectedListener");
        }
    }

    public void initViews() {
        tw = 36f;

        mHeight = (ScaleMarkView) view.findViewById(R.id.scale_mark1);
        tvheight = (TextView) view.findViewById(R.id.tvheight);
        rlchecktime = (RelativeLayout) view.findViewById(R.id.rl_height_time);
        tvcltime = (TextView) view.findViewById(R.id.tv_cl_time);
        btnsave = (Button) view.findViewById(R.id.btn_savetemp);
        mHeight.setZOrderOnTop(true);
        mHeight.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mHeight.setPreMarkValue(1f);
        mHeight.setDefaultValue(tw);
        mHeight.setMinValue(35);
        mHeight.setMaxValue(45);
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        tvheight.setText(decimalFormat.format(tw)+"℃");

    }

    public void initEvents() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvcltime.setText(df.format(new Date()));
        checktime = df.format(new Date());
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
                            if("fail".equals(type.getType()))
                                btnsave.setEnabled(true);

                        }

                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
        mHeight.setOnValueChangedListener(new ScaleMarkView.OnValueChangedListener() {
            @Override
            public void onValueChanged(ScaleMarkView view, BigDecimal oldValue, BigDecimal newValue) {
                tw = newValue.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                tvheight.setText(tw + "℃");
            }
        });
        rlchecktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;

                }
                Intent intenttime = new Intent(getActivity(), SelectDateAndTime.class);
                startActivityForResult(intenttime, Constants.ADDTTIME);

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                }else {
                    addTemperature();
                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void addTemperature() {
        btnsave.setEnabled(false);
        requestMaker.TemperatureInsert(cardNo,userid, checktime, tw + "", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                String value = result.toString();
                btnsave.setEnabled(true);
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("TemperatureInsert");

                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        RxBus.getInstance().send(new EventType(Constants.HealthData));
//                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));

                        ToastUtils.showMessage(getActivity(), "保存成功！");
                        if (p == -1) {
                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_temp)));
                            getActivity().finish();
                        } else {
                            if (p <= 2) {
                                mListener.selectItem(p + 1);

                            } else {
                                Intent intentD = new Intent();
                                intentD.setAction("action.DateOrFile");
                                intentD.putExtra("msgContent", "Date");
                                getActivity().sendBroadcast(intentD);

//                                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                                getActivity().finish();
                            }
                        }

                    } else {
                        ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    btnsave.setEnabled(true);
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));

    }

    private void getTemperature() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;

        }
        requestMaker.HealthDataInquirywWithPageType(userid,cardNo, "1", "1", "Temperature", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue = result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");

                    if (array.getJSONObject(0).has("MessageCode")) {


                    } else {

                        HealteTempData date = JsonTools.getData(result.toString(), HealteTempData.class);
                        List<TempRes> list = date.getData();
                        tw = Float.parseFloat(list.get(0).getValue());
//                        mHeight.setValue(Double.valueOf(list.get(0).getValue1()));
                        mHeight.setValue(Double.valueOf(list.get(0).getValue()));
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


    public static Fragment getInstance() {
        return new TempFragment();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.REQUEST_CALENDAR && data != null) {
            String time = data.getStringExtra("time");

            if (!TextUtils.isEmpty(time)) {
                tvcltime.setText(time);
                checktime = time;
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    @Override
    protected void lazyLoad() {

    }
}
