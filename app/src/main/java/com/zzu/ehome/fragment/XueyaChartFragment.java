package com.zzu.ehome.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.BloodPressResChatAdapter;
import com.zzu.ehome.bean.BloodPreessDate;
import com.zzu.ehome.bean.BloodPressRes;
import com.zzu.ehome.bean.TimeType;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.WeightDate;
import com.zzu.ehome.bean.WeightRes;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DoubleLineView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xclcharts.chart.PointD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;



/**
 * Created by Administrator on 2016/5/16.
 */
public class XueyaChartFragment extends BaseFragment implements View.OnClickListener,StickyListHeadersListView.OnHeaderClickListener {
    private View mView;
    private DoubleLineView mChart;
    private RequestMaker requestMaker;
    private String cardNo;
    private EHomeDao dao;
    private String userid;
    private User dbUser;
    private SupperBaseActivity activity;
    private TextView tvstart, tvend, tvnodata;
    private LinearLayout heardchat,layout_no_msg;
    private LayoutInflater inflater;
    private TimePickerView pvTime;
    private RelativeLayout rl_weight_time, rl_weight_endtime;
    private StickyListHeadersListView listview;
    private BloodPressResChatAdapter mAadpter;
    private Button searchBtn;
    private TextView tvtips;
    private LinkedList<String> labels = new LinkedList<String>();



    private TimeType typetime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requestMaker = RequestMaker.getInstance();
        dao = new EHomeDaoImpl(getActivity());
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dbUser = dao.findUserInfoById(userid);
        cardNo = dbUser.getUserno();
        mView = inflater.inflate(R.layout.layout_temp_chat, null);

        initViews();
        initTimePicker();
        initEvnets();
        if (!activity.isNetWork) {
            activity.showNetWorkErrorDialog();

        } else {
            getTop(7);
        }
        return mView;
    }

    @Override
    protected void lazyLoad() {

    }

    public static Fragment getInstance() {
        return new XueyaChartFragment();
    }

    public void initViews() {
        inflater = LayoutInflater.from(getActivity());
        mAadpter = new BloodPressResChatAdapter(getActivity());
        layout_no_msg=(LinearLayout)mView.findViewById(R.id.layout_no_msg);
        heardchat = (LinearLayout) inflater.inflate(R.layout.new_press_layout, null);
        listview = (StickyListHeadersListView) mView.findViewById(R.id.lv_temp);
        mChart = (DoubleLineView) heardchat.findViewById(R.id.chart);
        tvstart = (TextView) heardchat.findViewById(R.id.tv_start);
        tvend = (TextView) heardchat.findViewById(R.id.tv_end);
        tvtips=(TextView) heardchat.findViewById(R.id.tv_tips);
        tvnodata = (TextView) heardchat.findViewById(R.id.tvnodate);
        searchBtn=(Button) heardchat.findViewById(R.id.btn_search);
        rl_weight_time = (RelativeLayout) heardchat.findViewById(R.id.rl_weight_time);
        rl_weight_endtime = (RelativeLayout) heardchat.findViewById(R.id.rl_weight_endtime);
        listview.addHeaderView(heardchat);
        listview.setOnHeaderClickListener(this);
       listview.setAdapter(mAadpter);
    }

    private void getTop(int num) {
        requestMaker.BloodPressureInquiryType(cardNo, userid, "", "", "T", num + "", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue = result.toString();
                    JSONArray array = mySO
                            .getJSONArray("BloodPressureInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        layout_no_msg.setVisibility(View.VISIBLE);

                    } else {
                        layout_no_msg.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);
                        BloodPreessDate date = JsonTools.getData(result.toString(), BloodPreessDate.class);
                        List<BloodPressRes> list = date.getData();
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        if(array.length()>=6){
                            int i, j;

                            for (i = list.size() - 1, j = 0; i >= 0; i--, j++) {
                                Double xd = Double.valueOf(j);
                                Double ydH = Double.valueOf(list.get(i).getHigh());
                                Double ydL = Double.valueOf(list.get(i).getLow());
                                linePoint1.add(new PointD(xd, ydH));
                                linePoint2.add(new PointD(xd, ydL));
                                labels.add(DateUtils.StringPattern(list.get(i).getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "dd") + "日");
                            }
                            mChart.setX(labels,list.size()-2);

                        }
                        else {
                            int i, j;
                            labels.add("");
                            for (i = list.size() - 1, j = 1; i >= 0; i--, j++) {
                                Double xd = Double.valueOf(j);
                                Double ydH = Double.valueOf(list.get(i).getHigh());
                                Double ydL = Double.valueOf(list.get(i).getLow());
                                linePoint1.add(new PointD(xd, ydH));
                                linePoint2.add(new PointD(xd, ydL));
                                labels.add(DateUtils.StringPattern(list.get(i).getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "dd") + "日");
                            }

                            labels.add("");
                            mChart.setX(labels,list.size());
                        }
                        tvend.setText(DateUtils.StringPattern(list.get(0).getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd"));
                        tvstart.setText(DateUtils.StringPattern(list.get(list.size() - 1).getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd"));

                        mChart.refresh(linePoint1, linePoint2);
                        mAadpter.setList(list);
                        mAadpter.notifyDataSetChanged();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (SupperBaseActivity) context;
    }

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Date dtNow= new Date();
                Date dt = date;
                if(dtNow.getTime()>=dt.getTime())
                {
                    if (typetime == TimeType.Start) {
                        tvstart.setText(getTime(date));

                    } else {
                        Date dtStart= null;
                        Date dtend = date;
                        try {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            dtStart = df.parse(tvstart.getText().toString());
                            if(dtend.getTime()>=dtStart.getTime())
                            {
                                tvend.setText(getTime(date));
                            }
                            else
                            {

                                showDialog("开始时间不能晚于结束时间！");

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }
                else
                {
                    showDialog("请输入正确的时间！");

                }


            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY).setCancelText("取消").setSubmitText("确定").setContentSize(18)
                .setTitleSize(14).setCancelColor(getResources().getColor(R.color.actionbar_color)).setSubmitColor(getResources().getColor(R.color.actionbar_color))
                .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR))
                .build();

    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void initEvnets() {
        rl_weight_time.setOnClickListener(this);
        rl_weight_endtime.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_weight_time && pvTime != null) {
            typetime = TimeType.Start;
            pvTime.show();

        } else if (v.getId() == R.id.rl_weight_endtime && pvTime != null) {
            typetime = TimeType.End;
            pvTime.show();
        }else if(v.getId()==R.id.btn_search){
            if(TextUtils.isEmpty(tvend.getText())){
                showDialog("请选择结束时间");
                return;
            }
            getPressRecords(tvstart.getText().toString(),tvend.getText().toString());
        }

    }
    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }
    private void getPressRecords(String starttime,String endtime){
        tvtips.setText("请选择查询时间段");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dtStart;
        Date dtend ;
        try {
            dtStart = df.parse(starttime);
            dtend = df.parse(endtime);
            if (dtend.getTime() >= dtStart.getTime()) {
                requestMaker.BloodPressureInquiryType(cardNo, userid,starttime,endtime, "", "", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {

                        try {
                            JSONObject mySO = (JSONObject) result;
                            String resultValue = result.toString();
                            JSONArray array = mySO
                                    .getJSONArray("BloodPressureInquiry");
                            if (array.getJSONObject(0).has("MessageCode")) {
                                tvnodata.setVisibility(View.VISIBLE);
                                List<BloodPressRes> list = new ArrayList<BloodPressRes>();
                                List<PointD> linePoint1 = new ArrayList<PointD>();
                                List<PointD> linePoint2 = new ArrayList<PointD>();
                                labels.clear();
                                mChart.setX(labels,list.size());
                                mChart.refresh(linePoint1,linePoint2);
                                mAadpter.setList(list);
                                mAadpter.notifyDataSetChanged();
                            } else {
                                labels.clear();
                                tvnodata.setVisibility(View.GONE);
                                listview.setVisibility(View.VISIBLE);
                                BloodPreessDate date = JsonTools.getData(result.toString(), BloodPreessDate.class);
                                List<BloodPressRes> list = date.getData();
                                List<PointD> linePoint1 = new ArrayList<PointD>();
                                List<PointD> linePoint2 = new ArrayList<PointD>();
                                if(array.length()>=6){
                                    int i,j;

                                    for (i = list.size()-1,j=0; i>=0 ; i--,j++) {
                                        Double xd = Double.valueOf(j);
                                        Double ydH = Double.valueOf(list.get(i).getHigh());
                                        Double ydL = Double.valueOf(list.get(i).getLow());
                                        linePoint1.add(new PointD(xd, ydH));
                                        linePoint2.add(new PointD(xd, ydL));
                                        labels.add(DateUtils.StringPattern(list.get(i).getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "dd")+"日");
                                    }
                                    mChart.setX(labels,list.size()-2);
                                }else{
                                    int i,j;
                                    labels.add("");

                                    for (i = list.size()-1,j=1; i>=0 ; i--,j++) {
                                        Double xd = Double.valueOf(j);
                                        Double ydH = Double.valueOf(list.get(i).getHigh());
                                        Double ydL = Double.valueOf(list.get(i).getLow());
                                        linePoint1.add(new PointD(xd, ydH));
                                        linePoint2.add(new PointD(xd, ydL));
                                        labels.add(DateUtils.StringPattern(list.get(i).getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "dd")+"日");
                                    }
                                    labels.add("");
                                    mChart.setX(labels,list.size());
                                }


                                mChart.refresh(linePoint1, linePoint2);
                                mAadpter.setList(list);
                                mAadpter.notifyDataSetChanged();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                }));

            } else {
                showDialog("开始时间不能晚于结束时间！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        }

}
