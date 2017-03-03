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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.AboutEhomeActivity;
import com.zzu.ehome.activity.AdviceActivity;
import com.zzu.ehome.activity.AppointmentActivity1;
import com.zzu.ehome.activity.HealthFilesActivity;
import com.zzu.ehome.activity.HealthFilesActivity1;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.activity.MyFocusActivity;
import com.zzu.ehome.activity.MyHome;
import com.zzu.ehome.activity.MyRemindActivity1;
import com.zzu.ehome.activity.MyReportActivity;
import com.zzu.ehome.activity.PersonalCenterInfo;
import com.zzu.ehome.activity.PingguTestActivity;
import com.zzu.ehome.activity.SettingActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.ShareModel;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CardUtil;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.SharePopupWindow;

import de.greenrobot.event.EventBus;

import static com.zzu.ehome.R.id.edt_age;

/**
 * Created by Mersens on 2016/8/5.
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private TextView tvGoMyFaimily;
    private String userid;
    private RequestMaker requestMaker;
    private ImageView icon_user;
    private TextView tv_name,tv_mydoctor;
    private SupperBaseActivity activity;
    private RelativeLayout layout_msg, layout_wdda, layout_wdbg,
            layout_wdyy, layout_wdtx,  layout_yqhy,
            layout_yjfk, layout_about, layout_setting,layout_wdpg;
    private EHomeDao dao;
    private TextView tv_msg,tv_edit;
    private View vTop;
    private String type=null;
    private SharePopupWindow share;
    private RelativeLayout layout_wdys,rlusertop;
    private BroadcastReceiver mRefreshReciver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("userrefresh")){
                userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
                initDatas();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("userrefresh");
        getActivity().registerReceiver(mRefreshReciver, intentFilter);
        return inflater.inflate(R.layout.layout_user_center, null);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        EventBus.getDefault().register(this);
        dao = new EHomeDaoImpl(getActivity());
        requestMaker = RequestMaker.getInstance();
        mView = view;
        initViews();
        initEvent();
        initDatas();
    }

    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_info) == event
                .getRefreshWhere()) {
            userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
            initDatas();
        }
    }


    public void initViews() {
        layout_wdda = (RelativeLayout) mView.findViewById(R.id.layout_wdda);
        layout_wdbg = (RelativeLayout) mView.findViewById(R.id.layout_wdbg);
        layout_wdyy = (RelativeLayout) mView.findViewById(R.id.layout_wdyy);
        layout_wdtx = (RelativeLayout) mView.findViewById(R.id.layout_wdtx);
        layout_yqhy = (RelativeLayout) mView.findViewById(R.id.layout_yqhy);
        layout_yjfk = (RelativeLayout) mView.findViewById(R.id.layout_yjfk);
        layout_about = (RelativeLayout) mView.findViewById(R.id.layout_about);
        layout_setting = (RelativeLayout) mView.findViewById(R.id.layout_setting);
        layout_wdpg=(RelativeLayout)mView.findViewById(R.id.layout_wdpg);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        layout_msg = (RelativeLayout) mView.findViewById(R.id.layout_msg);
        tvGoMyFaimily = (TextView) mView.findViewById(R.id.tvGoMyFaimily);
        icon_user = (ImageView) mView.findViewById(R.id.icon_user);
        tv_msg = (TextView) mView.findViewById(R.id.tv_msg);
        vTop = mView.findViewById(R.id.v_top);
        tv_mydoctor=(TextView)mView.findViewById(R.id.
                tv_mydoctor);
        layout_wdys=(RelativeLayout)mView.findViewById(R.id.layout_wdys);
        tv_edit=(TextView)mView.findViewById(R.id.tv_edit);
        rlusertop=(RelativeLayout)mView.findViewById(R.id.rlusertop);
        ViewGroup.LayoutParams para;
        para =  rlusertop.getLayoutParams();
        para.width = ScreenUtils.getScreenWidth(getActivity());
        para.height = para.width*20/75;
        rlusertop.setLayoutParams(para);
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
        layout_wdda.setOnClickListener(this);
        layout_wdbg.setOnClickListener(this);
        layout_wdyy.setOnClickListener(this);
        layout_wdtx.setOnClickListener(this);
//        layout_wdgz.setOnClickListener(this);
        layout_yqhy.setOnClickListener(this);
        layout_yjfk.setOnClickListener(this);
        layout_about.setOnClickListener(this);
        layout_setting.setOnClickListener(this);
        tvGoMyFaimily.setOnClickListener(this);
        icon_user.setOnClickListener(this);
        tv_mydoctor.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        layout_wdys.setOnClickListener(this);
        layout_wdpg.setOnClickListener(this);
    }


    public void initDatas() {
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        tv_msg.setText("");
        if (TextUtils.isEmpty(userid)) {
            layout_msg.setVisibility(View.INVISIBLE);
            tv_name.setText("");
            icon_user.setImageResource(R.drawable.icon_uesr_tx2);
        } else {
            User user = dao.findUserInfoById(userid);
            if (user != null) {
                if(TextUtils.isEmpty(user.getImgHead())) {
                    icon_user.setImageResource(R.drawable.icon_user_tx1);
                }else {
                    ImageLoader.getInstance().displayImage(user.getImgHead(), icon_user);
                }
                    tv_name.setText(user.getUsername());
                    StringBuffer sbf = new StringBuffer();
                    String sex = user.getSex();
                    String age = user.getAge();
                    String height = user.getUserHeight();
                    if (!TextUtils.isEmpty(sex)) {
                        if ("01".equals(sex)) {
                            sbf.append("男");
                        } else if ("02".equals(sex)) {
                            sbf.append("女");
                        }
                    }
                    if (!TextUtils.isEmpty(age)) {
                        String info=user.getUserno();
                        try {
                            if(info.length()==18){
                                age=CardUtil.getCarInfo(info);
                            }else if(info.length()==15){
                                age=CardUtil.getCarInfo15W(info);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            sbf.append(" | " + age + "岁");
                        }


                    }
                    if (!TextUtils.isEmpty(height)&& !"0".equals(height)) {
                        if(TextUtils.isEmpty(age)&&TextUtils.isEmpty(sex)){
                            sbf.append( height + "cm");
                        }else {
                            sbf.append(" | " + height + "cm");
                        }
                    }
                    String str = sbf.toString();
                    if (!TextUtils.isEmpty(str)) {
                        layout_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText(str);
                        tv_edit.setText("编辑");
                    }else{
                        layout_msg.setVisibility(View.VISIBLE);
                        tv_edit.setText("完善资料");

                    }
            }

        }
    }


    public static Fragment getInstance() {
        return new UserCenterFragment();
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {
        if(!activity.isNetWork){
            activity.showNetWorkErrorDialog();
            return;
        }
        switch (v.getId()) {
            case R.id.layout_wdpg:

                startActivity(new Intent(getActivity(), PingguTestActivity.class));
                break;
            case R.id.tvGoMyFaimily:
                doGoFamily();
                break;
            case R.id.layout_wdda:
                //我的档案
                userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if(TextUtils.isEmpty(userid)){
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                    return;
                }
                type=dao.findUserInfoById(userid).getType()+"";
                if (!TextUtils.isEmpty(userid) && type!=null) {
                    if(type.equals("2")){
                        Intent i=new Intent(getActivity(),HealthFilesActivity.class);
                        i.putExtra("UserId",SharePreferenceUtil.getInstance(getActivity()).getUserId());
                        i.putExtra("type",type);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(getActivity(),HealthFilesActivity1.class);
                        i.putExtra("UserId",SharePreferenceUtil.getInstance(getActivity()).getUserId());
                        i.putExtra("type",type);
                        startActivity(i);
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }


                break;
            case R.id.tv_mydoctor:
                //我的医生
                userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), MyFocusActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }

                break;
            case R.id.layout_wdbg:
                //我的报告
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), MyReportActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }

                break;
            case R.id.layout_wdyy:
                //我的预约
                if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getUserId())){
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }else
                startActivity(new Intent(getActivity(), AppointmentActivity1.class));
                break;
            case R.id.layout_wdtx:
                //我的提醒
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), MyRemindActivity1.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }



                break;

            case R.id.layout_yqhy:
                //邀请好友
                doInvite();
                break;
            case R.id.layout_yjfk:
                //意见反馈
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), AdviceActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }

                break;
            case R.id.layout_about:
                //关于App
                startActivity(new Intent(getActivity(), AboutEhomeActivity.class));
                break;
            case R.id.layout_setting:
                //设置
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }



                break;
            case R.id.icon_user:
                userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), PersonalCenterInfo.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }
                break;
            case R.id.tv_edit:
                userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), PersonalCenterInfo.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }
                break;
            case R.id.layout_wdys:
                userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
                if (!TextUtils.isEmpty(userid)) {
                    startActivity(new Intent(getActivity(), MyFocusActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity1.class));
                }
                break;
        }

    }

    public void doInvite() {
        share = new SharePopupWindow(getActivity());
        ShareModel model = new ShareModel();
        model.setImgPath(ImageUtil.saveResTolocal(getActivity().getResources(), R.drawable.share, "home_logo"));
        model.setText("跟我一起关注个人和家人健康吧，还可以预约网络视频问诊哦!");
        model.setTitle("个人健康数据管理专家");
        model.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.zzu.ehome.main.ehome");
        share.initShareParams(model);
        share.showShareWindow();
        share.showAtLocation(getActivity().findViewById(R.id.set),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void doGoFamily() {

        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();

        if (!TextUtils.isEmpty(userid)) {
            startActivity(new Intent(getActivity(), MyHome.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity1.class));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
//        if(!TextUtils.isEmpty(userid)) {
//            getBaseData();
//        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            getActivity().unregisterReceiver(mRefreshReciver);
            mRefreshReciver = null;
        } catch (Exception e) {
        }

    }
}
