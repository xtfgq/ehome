package com.zzu.ehome.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.yiguo.toast.Toast;/**/

import com.zzu.ehome.R;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.CustomProgressDialog;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.SVProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.rong.imkit.RongIM;
import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Administrator on 2016/11/22.
 */

public abstract class SupperBaseActivity extends FragmentActivity {
    private int mScreenWidth;
    private int mScreenHeight;
    private Toast mToast;
    private Activity activity;
    private HeadView mHeadView;
    private RequestMaker requestMaker;
    private EHomeDao dao;
    public boolean isNetWork=true;
    public EHomeDao getDao() {
        return dao;
    }

    public void setDao(EHomeDao dao) {
        this.dao = dao;
    }


    private CustomProgressDialog progressDialog = null;
    private boolean isVisible,isshow=false;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stopProgressDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
        if (action.equals("rongyun") && isVisible) {
                confirmLogin();
            }else
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()) {
                    isNetWork=true;
                    if(isVisible){
                        SVProgressHUD.clean(SupperBaseActivity.this);
                    }
                    if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){

                    }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){

                    }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){

                    }


                } else {
                    isNetWork=false;
                    //无网络状态
                    if(isVisible){
                        showNetWorkErrorDialog();
                    }

                }
            }
        }
    };


    public void showNetWorkErrorDialog(){
      SVProgressHUD.showErrorWithStatus(SupperBaseActivity.this, "网络异常,请稍候重试!", SVProgressHUD.SVProgressHUDMaskType.Gradient);
    }
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestMaker = RequestMaker.getInstance();
        CustomApplcation.getInstance().addActivity(this);
        activity = this;
        //获取手机屏幕的高度和宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;
        dao = new EHomeDaoImpl(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.loginout");
        intentFilter.addAction("rongyun");
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
        isVisible=true;

    }


    /**
     * @param leftsrcid
     * @param title
     * @param rightsrcid
     * @param onleftclicklistener
     * @param onrightclicklistener
     * @author Mersens
     * setDefaultViewMethod--默认显示左侧按钮，标题和右侧按钮
     */
    public void setDefaultViewMethod(int leftsrcid, String title, int rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT);
        mHeadView.setDefaultViewMethod(leftsrcid, title, rightsrcid, onleftclicklistener, onrightclicklistener);
    }

    public void setDefaultTXViewMethod(int leftsrcid, String title, String rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT_TX);
        mHeadView.setDefaultTXViewMethod(leftsrcid, title, rightsrcid, onleftclicklistener, onrightclicklistener);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }

    /**
     * @param title
     * @param rightsrcid
     * @param onRightClickListener
     * @author Mersens
     * setRightAndTitleMethod--显示右侧按钮和标题
     */
    public void setRightAndTitleMethod(String title, int rightsrcid, HeadView.OnRightClickListener onRightClickListener) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.RIGHTANDTITLE);
        mHeadView.setRightAndTitleMethod(title, rightsrcid, onRightClickListener);
    }


    /**
     * @param leftsrcid
     * @param title
     * @param onleftclicklistener
     * @author Mersens
     * setLeftWithTitleViewMethod--显示左侧按钮和标题
     */
    public void setLeftWithTitleViewMethod(int leftsrcid, String title, HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFTANDTITLE);
        mHeadView.setLeftWithTitleViewMethod(leftsrcid, title, onleftclicklistener);
    }
    public void setHeadTitle(String title) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.setTitle(title);
    }

    public void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            //progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
        mHandler.sendEmptyMessageDelayed(0, 8000);
    }

    /**
     * @param title
     * @author Mersens
     * setOnlyTileViewMethod--只显示标题
     */
    public void setOnlyTileViewMethod(String title) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.ONLYTITLE);
        mHeadView.setOnlyTileViewMethod(title);
    }

    /**
     * @param leftsrcid
     * @param onleftclicklistener
     * @author Mersens
     * setLeftViewMethod--只显示左侧按钮
     */
    public void setLeftViewMethod(int leftsrcid, HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView = (HeadView) findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFT);
        mHeadView.setLeftViewMethod(leftsrcid, onleftclicklistener);
    }

    /**
     * @param text
     * @author Mersens
     * Toast显示以字符串作为显示内容
     */
    public void ShowToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * @param srcid
     * @author Mersens
     * Toast显示参数为资源id作为显示内容
     */
    public void ShowToast(int srcid) {
        if (mToast == null) {
            mToast = Toast.makeText(activity, srcid, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(srcid);
        }
        mToast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        if( CustomApplcation.getInstance().isOnLine==0){
            confirmLogin();
        }

    }
    public void startProgressDialogTitle(String title) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage(title);
        }

        progressDialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SVProgressHUD.clean(this);
    }



    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        isVisible = false;

    }

    /**
     * @return
     * @author Mersens
     * ͨ判断是否有网络连接
     */
    public boolean isNetworkAvailable() {
        NetworkInfo info = getNetworkInfo(activity);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    /**
     * @return mScreenWidth
     * @author Mersens
     * 获取屏幕的宽度
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * @return mScreenHeight
     * @author Mersens
     * 获取屏幕高度
     */
    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getHeadViewHeight() {
        return mHeadView.getHeadViewHeight();
    }

    /**
     * @param context
     * @param cls
     * @author Mersens
     */
    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }


    public void finishActivity() {
        finish();
    }

    /**
     * @param resid
     * @throws
     * @Title: setHeadViewBg
     * @Description: 设置HeadView的背景颜色
     * @author Mersens
     */
    public void setHeadViewBg(int resid) {
        mHeadView.setHeadViewBackground(resid);
    }

    /**
     * @param resid
     * @throws
     * @Title: setHeadViewTitleColor
     * @Description:设置HeadView的标题颜色
     * @author Mersens
     */
    public void setHeadViewTitleColor(int resid) {
        mHeadView.setHeadViewTitleColor(resid);
    }

    public int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = activity.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProgressDialog();
        try {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        } catch (Exception e) {
        }
        mHandler.removeCallbacksAndMessages(null);

    }

    private void UserClientBind() {

        String currUsr = SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).getUserId();
        requestMaker.loginOut(currUsr, new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String UserClientBind = result.toString();
                if (UserClientBind == null) {

                } else {
                    try {
                        JSONObject mySO = (JSONObject) result;
                        JSONArray array = mySO
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
                        SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).setUserId("");
                        SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).setIsRemeber(false);
                        Intent intenthealth = new Intent("userrefresh");
                        sendBroadcast(intenthealth);
                        Intent i = new Intent(CustomApplcation.getInstance(), LoginActivity.class);
                        i.putExtra("logout", "logout");
                        i.putExtra("Home","Home");
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    private void upload(String no) {
        double calories = 0;
        int step_length = 55;
        int minute_distance = 80;
        float weight;
        String timeCount;
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).getWeight())) {
            weight = 50.0f;
        } else {
            weight = Float.parseFloat(SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).getWeight());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01) / 1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        String currUsr = SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).getUserId();
        requestMaker.StepCounterInsert(no, currUsr, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f", calories), sdf.format(new Date()), new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("StepCounterInsert");
                    UserClientBind();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    public void confirmLogin() {
        if(!isshow) {

            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.dialog_default_ensure_click, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            dialog.show();
            isshow = true;
            TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
            tvok.setText("重新登陆");
            TextView tvCancle = (TextView) layout.findViewById(R.id.dialog_default_click_cancel);
            tvCancle.setVisibility(View.GONE);

            TextView tvtitel = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
            tvtitel.setText("提   示");
            TextView tvcontent = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
            tvcontent.setText("您的账号在别的设备上登录，您被迫下线");
            tvok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User dbUser = dao.findUserInfoById(SharePreferenceUtil.getInstance(CustomApplcation.getInstance()).getUserId());
                    dialog.dismiss();
                    isshow=false;
                    CustomApplcation.getInstance().isOnLine = -1;
                    if (TextUtils.isEmpty(dbUser.getUserno())) {
                        UserClientBind();
                    } else {
                        upload(dbUser.getUserno());
                    }


                }
            });
        }

    }
    public  void showTitleDialog(String message) {
        if(isVisible){
            DialogTips dialog = new DialogTips(SupperBaseActivity.this, message, "确定");
            dialog.show();
            dialog = null;
        }


    }
    public String getRightText() {

        return mHeadView.getRightText();
    }
        public void setRightText(String title) {
        mHeadView.setRightText(title);
    }
        public void getPermission(){
        PermissionGen.needPermission(SupperBaseActivity.this, 100,
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                }
        );
    }
        public void startTitleProgressDialog(String title){
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage(title);
        }

        progressDialog.show();
        mHandler.sendEmptyMessageDelayed(0, 30000);
    }
    public  void show(String message) {

        DialogTips dialog = new DialogTips(SupperBaseActivity.this, message, "确定");
        dialog.show();
        dialog = null;

    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }
    /**
     * 隐藏ActionBar
     */
    public void hideActionBar(){
        mHeadView.setVisibility(View.GONE);
    }

    /**
     * 显示ActionBar
     */
    public void showActionBar(){
        mHeadView.setVisibility(View.VISIBLE);
    }

}
