package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.zzu.ehome.R;
import com.zzu.ehome.activity.InputHeightActivity;
import com.zzu.ehome.activity.SelectDateAndTime;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.WeightBeanRes;
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
public class WeightFragment extends BaseFragment {
    private View view;
    private ScaleMarkView mWeight, weight_mark2;
    private float weight, height;
    private TextView tv_weight, tvcltime, tv_height;
    private RelativeLayout rlchecktime;
    private String cardNo,userid,myheitht;
    private Button btnsave;
    private RequestMaker requestMaker;
    private String checktime;
    private OnSelectItemListener mListener;
    public static int p = -1;
    User dbUser;
    private EHomeDao dao;
    private SupperBaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_weight, null);
        requestMaker = RequestMaker.getInstance();
        dao = new EHomeDaoImpl(getActivity());


        initViews();
        userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dbUser = dao.findUserInfoById(userid);
        cardNo=dbUser.getUserno();
        myheitht=dbUser.getUserHeight();
//        if (TextUtils.isEmpty(dbUser.getUserHeight())) {
//            startActivity(new Intent(getActivity(), InputHeightActivity.class));
//        }
        initEvents();
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
        mWeight = (ScaleMarkView) view.findViewById(R.id.weight_mark1);
        mWeight.setMaxValue(150);
        if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getWeight())){
            weight = 50;
        }else{
            weight = Integer.valueOf(SharePreferenceUtil.getInstance(getActivity()).getWeight());
        }
        mWeight.setDefaultValue(weight);
        tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        tv_weight.setText(weight + "kg");
        tvcltime = (TextView) view.findViewById(R.id.tv_cl_time);
        rlchecktime = (RelativeLayout) view.findViewById(R.id.rl_weight_time);
        btnsave = (Button) view.findViewById(R.id.btn_saveweight);
        tv_height = (TextView) view.findViewById(R.id.tv_height);
//        weight_mark2=(ScaleMarkView)view.findViewById(R.id.weight_mark2);
//        weight_mark2.setMaxValue(250);
//        weight_mark2.setMinValue(40);
//        height=40;
//        weight_mark2.setDefaultValue(40);
    }

    public void initEvents() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvcltime.setText(df.format(new Date()));
        checktime = df.format(new Date());
        mWeight.setOnValueChangedListener(new ScaleMarkView.OnValueChangedListener() {
            @Override
            public void onValueChanged(ScaleMarkView view, BigDecimal oldValue, BigDecimal newValue) {
                weight = newValue.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                tv_weight.setText(weight + "kg");
            }
        });
/*        weight_mark2.setOnValueChangedListener(new ScaleMarkView.OnValueChangedListener() {
            @Override
            public void onValueChanged(ScaleMarkView view, BigDecimal oldValue, BigDecimal newValue) {
                height = newValue.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                tv_height.setText(height+"");
            }
        });*/

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
                if (!activity.isNetWork) {
                    activity.showNetWorkErrorDialog();
                    return;

                }else{

                if (CommonUtils.isFastClick()) return;
                btnsave.setEnabled(false);
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    float w = 0;
                    float h=0;
                    float bmi=0;
                    try {
                        w = Float.valueOf(weight);
                        h=Float.valueOf(myheitht) / 100;
                        bmi = w / (h * h);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                requestMaker.WeightInsert(cardNo, userid, myheitht, decimalFormat.format(bmi) + "", weight + "", checktime, new JsonAsyncTask_Info(
                        getActivity(), true, new JsonAsyncTaskOnComplete() {
                    public void processJsonObject(Object result) {
                        String value = result.toString();
                        btnsave.setEnabled(true);
                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO.getJSONArray("WeightInsert");
                            if (array.getJSONObject(0).getString("MessageCode")
                                    .equals("0")) {
//                                EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                                RxBus.getInstance().send(new EventType("healthdata"));
                                ToastUtils.showMessage(getActivity(), "保存成功！");
                                if (p == -1) {
                                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_weight)));
                                    getActivity().finish();
                                } else {
                                    if (p <= 2) {
                                        mListener.selectItem(p + 1);

                                    } else {
                                        Intent intentD = new Intent();
                                        intentD.setAction("action.DateOrFile");
                                        intentD.putExtra("msgContent", "Date");
                                        getActivity().sendBroadcast(intentD);

                                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                                        getActivity().finish();
                                    }
                                }

                            } else {
                                ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            btnsave.setEnabled(true);
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        if(btnsave!=null)
                        btnsave.setEnabled(true);
                    }
                }));
            }

            }
        });
        getWeight();
    }

    public static Fragment getInstance() {
        return new WeightFragment();
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

    }

    private void getWeight() {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;

        }
        requestMaker.HealthDataInquirywWithPageType(userid,cardNo, "1", "1", "Weight", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue = result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");
                    if (array.getJSONObject(0).has("MessageCode")) {

                    } else {

                        HealteData date = JsonTools.getData(result.toString(), HealteData.class);
                        List<WeightBeanRes> list = date.getData();
                        mWeight.setValue(Float.valueOf(list.get(0).getWeight()));

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

    @Override
    protected void lazyLoad() {

    }
}
