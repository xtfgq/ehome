package com.zzu.ehome.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.utils.PermissionsChecker;

import java.io.File;
import java.util.Date;

/**
 * 相册类
 * Created by Administrator on 2016/4/7.
 */
public class PicPopu extends PopupWindow implements OnClickListener {


    private static final int PERMISSION_REQUEST_CODE = 0;        // 系统权限返回码
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private OnSelectPicFromAlbumListener listener;
    private Context mContext;
    private Activity activity;
    private String imageUrl;
    boolean crop = true;
    private static final int CROP = 2;
    int REQUEST_CODE;

    public File getmTempDir() {
        return mTempDir;
    }

    public void setmTempDir(File mTempDir) {
        this.mTempDir = mTempDir;
    }

    private File mTempDir;

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    private String mCurrentPhotoPath;

    public PicPopu(Context mContext, View parent, Activity activity) {
        super(mContext);
        this.activity = activity;
        this.mContext = mContext;
        listener=(OnSelectPicFromAlbumListener) activity;

        View view = View
                .inflate(mContext, R.layout.item_popubwindows, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));

        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
        mTempDir = new File(Environment.getExternalStorageDirectory(), "Temp");
        if (!mTempDir.exists()) {
            mTempDir.mkdirs();
        }
        mPermissionsChecker = new PermissionsChecker(mContext);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);

        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_popupwindows_camera:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {

                        showMissingPermissionDialog();
                        return;
                    }
                }
                Uri imageUri = null;
                String fileName = null;
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                REQUEST_CODE = Constants.REQUEST_CODE_CAPTURE_CAMEIA;
                long times=new Date().getTime();
                fileName = "image"+times+".jpg";
                imageUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), fileName));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        imageUri);
                imageUrl=Environment.getExternalStorageDirectory()+"/"+fileName;
                activity.startActivityForResult(openCameraIntent, REQUEST_CODE);
                dismiss();
                break;
            case R.id.item_popupwindows_Photo:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        showMissingPermissionDialog();
                        return;
                    }
                }
                if(listener!=null){
                    listener.onSelect();
                }
                break;
            case R.id.item_popupwindows_cancel:
                dismiss();
                break;

        }

    }

    public String getImageUrl(){
        return imageUrl;

    }

    public interface OnSelectPicFromAlbumListener {
        void onSelect();
    }

    public void setOnSelectPicFromAlbumListener(OnSelectPicFromAlbumListener listener){
        this.listener=listener;

    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(mContext, "请点击设置，打开所需存储权限",
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
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mContext.getPackageName()));
        mContext.startActivity(intent);
    }
}
