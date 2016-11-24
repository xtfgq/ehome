package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.ContentResolver;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.OCRForAndroidDate;
import com.zzu.ehome.bean.OcrBean;
import com.zzu.ehome.bean.OcrImage;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.ImageTools;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.OkHttpClientManager;
import com.zzu.ehome.utils.PictureUtil;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.UploadPicOcr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/9/11.
 */
public class UploadOcrPicActivity extends BaseActivity {
    private ImageView ivupload;
    private UploadPicOcr picPop;
    private byte[] mContent;
    private static final int SCALE = 5;// 照片缩小比例
    private RequestMaker requestMaker;
    OkHttpClientManager okHttpClientManager;

    private String userid,name,id;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_upload_pic);
        userid = SharePreferenceUtil.getInstance(UploadOcrPicActivity.this).getUserId();
        requestMaker = RequestMaker.getInstance();
        okHttpClientManager = OkHttpClientManager.getInstance();
        userid = SharePreferenceUtil.getInstance(UploadOcrPicActivity.this).getUserId();
        name=this.getIntent().getStringExtra("Name");
        id=this.getIntent().getStringExtra("Id");
        initViews();
        initEvents();
    }

    private void initEvents() {
        ivupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddHeadImage();
            }
        });
    }

    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "OCR智能识别", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        ivupload = (ImageView) findViewById(R.id.ivupload);
    }

    /**
     * 添加头像
     */
    private void doAddHeadImage() {
        picPop = new UploadPicOcr(UploadOcrPicActivity.this, ivupload, this);
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
                    Bitmap photo = BitmapFactory.decodeFile(IOUtils.getPath(UploadOcrPicActivity.this, originalUri));
                    ExifInterface exif = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        exif = new ExifInterface(IOUtils.getPath(UploadOcrPicActivity.this, originalUri));
                    } else {
                        exif = new ExifInterface(
                                getAbsoluteImagePath(originalUri));
                    }
                    int degree = ImageUtil.getDegree(exif);
                    String path = IOUtils.getPath(UploadOcrPicActivity.this, originalUri);

                    File file =ImageUtil.saveBitmapFile(PictureUtil.getSmallBitmap(path));

//                    Glide.with(UploadOcrPicActivity.this)
//                            .load(file)
//                            .centerCrop()
//                            .into(ivupload);
                    startTitleProgressDialog("努力识别中...");
                    UploadPicture(path, file,name);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == Constants.REQUEST_CODE_CAPTURE_CAMEIA) {
                setImageView();
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
                ImageTools.savePhotoToSDCard(newBitmap, Environment
                                .getExternalStorageDirectory().getAbsolutePath(),
                        String.valueOf(System.currentTimeMillis()));
                File file =ImageUtil.saveBitmapFile(ImageUtil.imageZoom(newBitmap,1956));



                startTitleProgressDialog("努力识别中...");

                UploadPicture(url, file,name);

            }
        }

    }

    private void cropImageUriAfterKikat(Uri uri, int outputX, int outputY,
                                        int requestCode) {

        String url = ImageUtil.getPath(UploadOcrPicActivity.this, uri);
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

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss" + userid);
        return dateFormat.format(date) + ".png";
    }


    private void UploadPicture(String path, File file,String name) {
        okHttpClientManager.doUpload(Constants.OCR + "?UserID="+ userid+"&HospitalName="+name, file, getPhotoFileName(), new OkHttpClientManager.RequestCallBack() {
            @Override
            public void onSueecss(String msg) {

                stopProgressDialog();

                OCRForAndroidDate date = JsonTools.getData(msg, OCRForAndroidDate.class);
                List<OcrImage>list1=date.getData();
                String url=  list1.get(0).getImgUrl();
                List<OcrBean> list = list1.get(0).getData();
                for(OcrBean bean:list){
                    if(TextUtils.isEmpty(bean.getNum())||"-".equals(bean.getNum())){
                        bean.setNum("0");
                    }

                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("OcrList", (Serializable) list);
                bundle.putString("Url",url);
                intent.setClass(UploadOcrPicActivity.this, OcrActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String msg) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });

    }

    private void setImageView() {
        ViewGroup.LayoutParams para;
        para = ivupload.getLayoutParams();
        para.width =  ScreenUtils.getScreenWidth(UploadOcrPicActivity.this);
        para.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ivupload.setLayoutParams(para);
    }


}
