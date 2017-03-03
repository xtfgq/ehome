package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserInfoDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.ImageTools;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.PicPopupWindows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SecondActivity extends BaseSimpleActivity implements View.OnClickListener {
    private EditText edName;
    private PicPopupWindows picPop;
    private Button btnext;
    private ImageView iv_head, ivback;
    private byte[] mContent;
    private static final int SCALE = 5;// 照片缩小比例
    String parentId = "";
    private RequestMaker requestMaker;
    private ImageLoader mImageLoader;
    private EHomeDao dao;
    private Boolean isHead = false;
    private TextView tvGo;
    private String tag = "";
    private String userid = "";


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        setContentView(R.layout.activity_relation);
        setContentView(R.layout.activity_next_relation);
        requestMaker = RequestMaker.getInstance();
        mImageLoader = ImageLoader.getInstance();
        dao = new EHomeDaoImpl(this);
        if (this.getIntent() != null) {
            if (this.getIntent().getStringExtra("relation") != null) {
                tag = this.getIntent().getStringExtra("relation");
                parentId = SharePreferenceUtil.getInstance(SecondActivity.this).getPARENTID();

            }
        }
            userid = SharePreferenceUtil.getInstance(SecondActivity.this).getUserId();

        initViews();

        initEvent();
        if(!CommonUtils.isNotificationEnabled(SecondActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    private void initEvent() {

        iv_head.setOnClickListener(this);
        ivback.setOnClickListener(this);
        tvGo.setOnClickListener(this);
        btnext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if(!CommonUtils.isNotificationEnabled(SecondActivity.this)){
            showTitleDialog("请打开通知中心");
        }
        switch (v.getId()) {
            case R.id.iv_head:
                doAddHeadImage();
                break;
            case R.id.iv_back:
               Intent i=new Intent(SecondActivity.this,MainActivity.class);
                i.putExtra("Home","Home");
                startActivity(i);
                finishActivity();

                break;
            case R.id.tvGo:
                if (TextUtils.isEmpty(tag)) {
                    startActivity(new Intent(SecondActivity.this, SexActivity.class));
                } else {
                    Intent i2 = new Intent(SecondActivity.this, SexActivity.class);
                    i2.putExtra("relation", "rela");
                    startActivity(i2);
                }
                break;
            case R.id.btnext:
                if(!isHead){
                    ToastUtils.showMessage(SecondActivity.this, "请上传头像！");
                    return;
                }
                if (edName.getText().toString().trim().equals("")) {
                    ToastUtils.showMessage(SecondActivity.this, "姓名不允许为空");
                    return;
                } else if (edName.getText().toString().trim().length() > 4) {
                    ToastUtils.showMessage(SecondActivity.this, "姓名长度过长");
                    return;
                } else if (!IOUtils.isName(edName.getText().toString().trim())) {
                    ToastUtils.showMessage(SecondActivity.this, "姓名需要输入汉字");
                    return;
                } else {
                    if (TextUtils.isEmpty(tag)) {
                        doInfoChange(userid);
                    } else {
                        doInfoChange(parentId);
                    }

                }
                break;
        }

    }

    private void doInfoChange(String useid) {
        startProgressDialog();
        btnext.setEnabled(false);
        requestMaker.userInfo(useid, edName.getText().toString().trim(), new JsonAsyncTask_Info(SecondActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                try {
                    stopProgressDialog();
                    btnext.setEnabled(true);
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("UserInfoChange");

                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        if (TextUtils.isEmpty(tag)) {
                            startActivity(new Intent(SecondActivity.this, SexActivity.class));
                        } else {
                            Intent i = new Intent(SecondActivity.this, SexActivity.class);
                            i.putExtra("relation", "rela");
                            startActivity(i);
                        }
                    }
                    if(returnvalue.contains("RealName")){
                        User db=dao.findUserInfoById(array.getJSONObject(0).getString("UserID"));
                        db.setUsername(array.getJSONObject(0).getString("RealName"));
                        dao.updateUserInfo(db,array.getJSONObject(0).getString("UserID"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {
                btnext.setEnabled(true);
                iv_head.setEnabled(true);
                tvGo.setEnabled(true);
            }
        }));
    }

    private void initViews() {

        iv_head = (ImageView) findViewById(R.id.iv_head);
        edName = (EditText) findViewById(R.id.edt_setting_name);
        ivback = (ImageView) findViewById(R.id.iv_back);
        tvGo = (TextView) findViewById(R.id.tvGo);
        btnext = (Button) findViewById(R.id.btnext);
    }

    /**
     * 添加头像
     */
    private void doAddHeadImage() {
        InputMethodManager imm = (InputMethodManager) getSystemService(SecondActivity.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edName.getWindowToken(), 0);
        picPop = new PicPopupWindows(SecondActivity.this, iv_head, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SHOW_ALL_PICTURE) {
//               beginCrop(result.getData());
                try {
                    ContentResolver resolver = getContentResolver();
                    // 照片的原始资源地址
                    Uri originalUri = result.getData();
                    long mid = ImageUtil.getAutoFileOrFilesSize(ImageUtil.getImageAbsolutePath(SecondActivity.this, originalUri));
                    if (mid > 6 * 1024 * 1024) {
                        Toast.makeText(SecondActivity.this, "图片尺寸过大", 1).show();
                        return;
                    }
                    Bitmap photo = BitmapFactory.decodeFile(ImageUtil.doPicture(ImageUtil.getImageAbsolutePath(SecondActivity.this, originalUri)));

                    ExifInterface exif = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        exif = new ExifInterface(IOUtils.getPath(SecondActivity.this, originalUri));
                    } else {
                        exif = new ExifInterface(
                                getAbsoluteImagePath(originalUri));
                    }

                    int degree = ImageUtil.getDegree(exif);
                    String path = ImageUtil.doPicture(ImageUtil.getUriString(result.getData(), getContentResolver()));
                    startProgressDialog();
                    if (photo != null) {


                        if (degree != 0) {
                            photo = ImageUtil.rotateImage(photo, degree);
                        }
                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG,
                                100, baos2);
                        mContent = baos2.toByteArray();
                        String uploadBuffer1 = new String(
                                Base64.encode(mContent));
                        if (TextUtils.isEmpty(tag)) {
                            UploadPicture(uploadBuffer1, userid);
                        } else {
                            UploadPicture(uploadBuffer1, parentId);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == Constants.REQUEST_CODE_CAPTURE_CAMEIA) {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment
                        .getExternalStorageDirectory() + "/image.jpg");
                Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
                        bitmap.getWidth() / SCALE, bitmap.getHeight()
                                / SCALE);
                // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                bitmap.recycle();

                String url = Environment.getExternalStorageDirectory() + "/image.jpg";

                ExifInterface exif2 = null;
                try {
                    exif2 = new ExifInterface(url);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int degree2 = ImageUtil.getDegree(exif2);
                if (degree2 != 0) {
                    newBitmap = ImageUtil.rotateImage(newBitmap, degree2);
                }

                // 将处理过的图片显示在界面上，并保存到本地
                //iv_image.setImageBitmap(newBitmap);
                ImageTools.savePhotoToSDCard(newBitmap, Environment
                                .getExternalStorageDirectory().getAbsolutePath(),
                        String.valueOf(System.currentTimeMillis()));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG,
                        100, baos);
                mContent = baos.toByteArray();

                String uploadBuffer = new String(
                        Base64.encode(mContent));
                if (TextUtils.isEmpty(tag)) {
                    UploadPicture(uploadBuffer, userid);
                } else {
                    UploadPicture(uploadBuffer, parentId);
                }


            } else if (requestCode == Constants.CROP) {

                ContentResolver resolver2 = getContentResolver();
                Uri uri = null;

                if (result != null) {
                    uri = result.getData();
                    System.out.println("Data");
                } else {
                    System.out.println("File");
                    String fileName = getSharedPreferences("temp",
                            Context.MODE_WORLD_WRITEABLE).getString(
                            "tempName", "");
                    uri = Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), fileName));
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    cropImageUriAfterKikat(uri, 500, 500, Constants.CROP_PICTURE);
                } else {
                    cropImageUri(uri, 500, 500, Constants.CROP_PICTURE);
                }
            } else if (requestCode == Constants.CROP_PICTURE) {

                Bitmap photo = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //Uri photoUri = data.getData();
                    //Bundle extra = data.getExtras();

                    photo = decodeUriAsBitmap(Uri.fromFile(new File(Constants.IMGPATH, Constants.TMP_IMAGE_FILE_NAME)));
                    //photo = decodeUriAsBitmap(photoUri);//decode bitmap
                    //photo = data.getParcelableExtra("data");
                    //photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));


                    //photo = (Bitmap) extra.get("data");

                    if (photo != null) {
                        //photoUri = (Uri)extra.get("data");
                        //photo = BitmapFactory.decodeFile(photoUri.getPath());
                        //photo = (Bitmap) extra.get("data");
//							updateUserPortrait(photo);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100,
                                stream);

                        mContent = stream.toByteArray();

                        String uploadBuffer2 = new String(
                                Base64.encode(mContent));

                        if (TextUtils.isEmpty(tag)) {
                            UploadPicture(uploadBuffer2, userid);
                        } else {
                            UploadPicture(uploadBuffer2, parentId);
                        }
                        System.gc();
                    }

                } else {
                    if (result == null) {
                        return;
                    }//剪裁后的图片
                    Bundle extras = result.getExtras();
                    if (extras == null) {
                        return;
                    }
                    photo = extras.getParcelable("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100,
                            stream);
//						updateUserPortrait(photo);
                    mContent = stream.toByteArray();

                    String uploadBuffer2 = new String(
                            Base64.encode(mContent));
                    if (TextUtils.isEmpty(tag)) {
                        UploadPicture(uploadBuffer2, userid);
                    } else {
                        UploadPicture(uploadBuffer2, parentId);
                    }

                    System.gc();
                }
            }
        }


    }

    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj =
                {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void UploadPicture(String uploadBuffer, final String id) {
        startProgressDialog();
        requestMaker.uploadPic(uploadBuffer, id, getPhotoFileName(), new JsonAsyncTask_Info(SecondActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("UploadUserPhoto");


                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {


                        doInqueryHead(id);
                    } else {
                        ToastUtils.showMessage(SecondActivity.this, "头像上传失败，请重试！");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Exception e) {
                if(btnext!=null&&iv_head!=null&&tvGo!=null) {
                    btnext.setEnabled(true);
                    iv_head.setEnabled(true);
                    tvGo.setEnabled(true);
                }
            }

        }));
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss" + parentId);
        return dateFormat.format(date) + ".jpg";
    }

    private void doInqueryHead(final String id) {

        requestMaker.UserInquiry(id, new JsonAsyncTask_Info(SecondActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                UserInfoDate date = JsonTools.getData(result.toString(), UserInfoDate.class);
                List<User> list = date.getData();
                User user = list.get(0);
                String imgHead = user.getImgHead();
                stopProgressDialog();
                if (imgHead != null) {
                    if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                        imgHead = "";
                    } else {
                        imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");
                    }
                } else {
                    imgHead = "";
                }
                User userOld = dao.findUserInfoById(id);
                userOld.setImgHead(imgHead);
                dao.updateUserInfo(userOld, id);
                mImageLoader.displayImage(
                        imgHead, iv_head);
                isHead = true;


            }

            @Override
            public void onError(Exception e) {

            }

        }));
    }

    private void cropImageUriAfterKikat(Uri uri, int outputX, int outputY,
                                        int requestCode) {

        String url = ImageUtil.getPath(SecondActivity.this, uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false); // 返回数据bitmap

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Constants.IMGPATH, Constants.TMP_IMAGE_FILE_NAME)));

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY,
                              int requestCode) {


        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);

    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i=new Intent(SecondActivity.this,MainActivity.class);
            i.putExtra("Home","Home");
            startActivity(i);
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
