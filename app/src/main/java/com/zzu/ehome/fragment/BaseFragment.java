package com.zzu.ehome.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.BaseActivity;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.view.CustomProgressDialog;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by zzu on 2016/3/31.
 */
public abstract class BaseFragment extends Fragment {
    private HeadView mHeadView;
    private CustomProgressDialog progressDialog = null;
    protected boolean isVisible;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if (action.equals("action.loginout")) {
//                confirmExit2();
//            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.loginout");
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }



    public void reload() {

    }

    public void confirmExit2() {
        DialogTips dialog = new DialogTips(getActivity(), "", "是否退出软件？",
                "确定", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                CustomApplcation.getInstance().exit();
                getActivity().finish();
            }
        });

        dialog.show();
        dialog = null;
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
    public void setDefaultViewMethod(View v, int leftsrcid, String title, int rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView = (HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT);
        mHeadView.setDefaultViewMethod(leftsrcid, title, rightsrcid, onleftclicklistener, onrightclicklistener);
    }

    public void setDefaultTXViewMethod(View v, int leftsrcid, String title, String rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView = (HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT_TX);
        mHeadView.setDefaultTXViewMethod(leftsrcid, title, rightsrcid, onleftclicklistener, onrightclicklistener);
    }

    /**
     * @param title
     * @param rightsrcid
     * @param onRightClickListener
     * @author Mersens
     * setRightAndTitleMethod--显示右侧按钮和标题
     */
    public void setRightAndTitleMethod(View v, String title, int rightsrcid, HeadView.OnRightClickListener onRightClickListener) {
        mHeadView = (HeadView) v.findViewById(R.id.common_actionbar);
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
    public void setLeftWithTitleViewMethod(View v, int leftsrcid, String title, HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView = (HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFTANDTITLE);
        mHeadView.setLeftWithTitleViewMethod(leftsrcid, title, onleftclicklistener);
    }

    /**
     * @param title
     * @author Mersens
     * setOnlyTileViewMethod--只显示标题
     */
    public void setOnlyTileViewMethod(View v, String title) {
        mHeadView = (HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.ONLYTITLE);
        mHeadView.setOnlyTileViewMethod(title);
    }

    /**
     * @param leftsrcid
     * @param onleftclicklistener
     * @author Mersens
     * setLeftViewMethod--只显示左侧按钮
     */
    public void setLeftViewMethod(View v, int leftsrcid, HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView = (HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFT);
        mHeadView.setLeftViewMethod(leftsrcid, onleftclicklistener);
    }

    public void setHeadViewBackground(int resid) {
        if (resid != 0 && mHeadView != null) {
            mHeadView.setHeadViewBackground(resid);
        }
    }

    /**
     * @param resid
     * @throws
     * @Title: setHeadViewTitleColor
     * @Description:设置HeadView的标题颜色
     * @author Mersens
     */
    public void setTitleColor(int resid) {
        if (resid != 0 && mHeadView != null) {
            mHeadView.setHeadViewTitleColor(resid);
        }

    }

    public void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
            //progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
        mHandler.sendEmptyMessageDelayed(0, 5000);
    }
    public void startProgressDialogTitle(String title) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
            progressDialog.setMessage(title);
        }

        progressDialog.show();

    }

    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopProgressDialog();

        try {
            getActivity().unregisterReceiver(mBroadcastReceiver);

            mBroadcastReceiver = null;
        } catch (Exception e) {
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }

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
    public  void getPermission(){
        PermissionGen.needPermission(getActivity(), 100,
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                }
        );
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
