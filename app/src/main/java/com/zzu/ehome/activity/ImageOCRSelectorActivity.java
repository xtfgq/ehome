package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yongchun.library.model.LocalMedia;
import com.yongchun.library.model.LocalMediaFolder;
import com.yongchun.library.utils.FileUtils;
import com.yongchun.library.utils.GridSpacingItemDecoration;
import com.yongchun.library.utils.LocalMediaLoader;
import com.yongchun.library.utils.ScreenUtils;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ImageFolderAdapter;
import com.zzu.ehome.adapter.ImageListAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.OCRForAndroidDate;
import com.zzu.ehome.bean.OcrBean;
import com.zzu.ehome.bean.OcrImage;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.OkHttpClientManager;
import com.zzu.ehome.utils.PictureUtil;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zzu.ehome.utils.PictureUtil.getSmallBitmap;
import static io.rong.imlib.statistics.UserData.name;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ImageOCRSelectorActivity extends BaseActivity {
    public final static int REQUEST_IMAGE = 66;
    public final static int REQUEST_CAMERA = 67;
    OkHttpClientManager okHttpClientManager;
    public final static String BUNDLE_CAMERA_PATH = "CameraPath";

    public final static String REQUEST_OUTPUT = "outputList";

    public final static String EXTRA_SELECT_MODE = "SelectMode";
    public final static String EXTRA_SHOW_CAMERA = "ShowCamera";
    public final static String EXTRA_ENABLE_PREVIEW = "EnablePreview";
    public final static String EXTRA_ENABLE_CROP = "EnableCrop";
    public final static String EXTRA_MAX_SELECT_NUM = "MaxSelectNum";

    public final static int MODE_MULTIPLE = 1;
    public final static int MODE_SINGLE = 2;

    private int maxSelectNum = 9;
    private int selectMode = MODE_MULTIPLE;
    private boolean showCamera = true;
    private boolean enablePreview = true;
    public static int numCu = 0;
    //裁剪需要引入新的第三方libSimpleCropView,永远false
    private boolean enableCrop = false;

    private int spanCount = 3;

    private RelativeLayout toolbar;
    private TextView doneText;

    private TextView previewText;

    private RecyclerView recyclerView;
    private ImageListAdapter imageAdapter;

    private LinearLayout folderLayout;
    private TextView folderName;
    private FolderWindow folderWindow;
    private ImageView ivback;

    private String cameraPath,userid;
    private RequestMaker requestMaker;
    public static  String hosname,hosid,typeid,typename;

    public static void start(Activity activity, int maxSelectNum, int mode, boolean isShow,
                             String name,String id,String type,String tame,boolean enablePreview, boolean enableCrop, int num) {
        Intent intent = new Intent(activity, ImageOCRSelectorActivity.class);
        intent.putExtra(EXTRA_MAX_SELECT_NUM, maxSelectNum);
        intent.putExtra(EXTRA_SELECT_MODE, mode);
        intent.putExtra(EXTRA_SHOW_CAMERA, isShow);
        intent.putExtra(EXTRA_ENABLE_PREVIEW, enablePreview);
        intent.putExtra(EXTRA_ENABLE_CROP, enableCrop);
        hosname=name;
        hosid=id;
        typeid=type;
        typename=tame;
        numCu = num;
        activity.startActivityForResult(intent, REQUEST_IMAGE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_upload);
        okHttpClientManager = OkHttpClientManager.getInstance();
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(ImageOCRSelectorActivity.this).getUserId();

        maxSelectNum = getIntent().getIntExtra(EXTRA_MAX_SELECT_NUM, 9);
        selectMode = getIntent().getIntExtra(EXTRA_SELECT_MODE, MODE_MULTIPLE);
        showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        enablePreview = getIntent().getBooleanExtra(EXTRA_ENABLE_PREVIEW, true);
        enableCrop = getIntent().getBooleanExtra(EXTRA_ENABLE_CROP, false);
        if (selectMode == MODE_MULTIPLE) {
            enableCrop = false;
        } else {
            enablePreview = false;
        }
        if (savedInstanceState != null) {
            cameraPath = savedInstanceState.getString(BUNDLE_CAMERA_PATH);
        }
        initView();
        registerListener();
        new LocalMediaLoader(this, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                folderWindow.bindFolder(folders);
                imageAdapter.bindImages(folders.get(0).getImages());
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_CAMERA_PATH, cameraPath);
    }

    public void initView() {
        folderWindow = new FolderWindow(this);

        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        ivback = (ImageView) findViewById(R.id.iv_back);


        doneText = (TextView) findViewById(R.id.done_text);
        doneText.setVisibility(selectMode == MODE_MULTIPLE ? View.VISIBLE : View.GONE);

//        previewText = (TextView) findViewById(R.id.preview_text);
//        previewText.setVisibility(enablePreview ? View.VISIBLE : View.GONE);

        folderLayout = (LinearLayout) findViewById(R.id.folder_layout);
        folderName = (TextView) findViewById(R.id.folder_name);

        recyclerView = (RecyclerView) findViewById(R.id.folder_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, ScreenUtils.dip2px(this, 2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        imageAdapter = new ImageListAdapter(this, maxSelectNum, selectMode, showCamera, enablePreview);
        recyclerView.setAdapter(imageAdapter);

    }

    public void registerListener() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        folderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderWindow.isShowing()) {
                    folderWindow.dismiss();
                } else {
                    folderWindow.showAsDropDown(toolbar);
                }
            }
        });
        imageAdapter.setOnImageSelectChangedListener(new ImageListAdapter.OnImageSelectChangedListener() {
            @Override
            public void onChange(List<LocalMedia> selectImages) {
                boolean enable = selectImages.size() != 0;
                doneText.setEnabled(enable ? true : false);
//                previewText.setEnabled(enable ? true : false);
                if (enable) {
                    doneText.setText(getString(R.string.done_num, selectImages.size() + numCu, maxSelectNum));
//                    previewText.setText(getString(R.string.preview_num, selectImages.size()));
                } else {
                    doneText.setText(R.string.done);
//                    previewText.setText(R.string.preview);
                }
            }

            @Override
            public void onTakePhoto() {
                startCamera();
            }

            @Override
            public void onPictureClick(LocalMedia media, int position) {
                if (enablePreview) {
                    startPreview(imageAdapter.getImages(), position);
                } else {
                    onSelectDone(media.getPath());
                }
            }
        });
        doneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectDone(imageAdapter.getSelectedImages());
            }
        });
        folderWindow.setOnItemClickListener(new ImageFolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, List<LocalMedia> images) {
                folderWindow.dismiss();
                imageAdapter.bindImages(images);
                folderName.setText(name);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // on take photo success
            if (requestCode == REQUEST_CAMERA) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(cameraPath))));
                onSelectDone(cameraPath);

            } else if (requestCode == ImagePreviewActivity.REQUEST_PREVIEW) {
                boolean isDone = data.getBooleanExtra(ImagePreviewActivity.OUTPUT_ISDONE, false);
                List<LocalMedia> images = (List<LocalMedia>) data.getSerializableExtra(ImagePreviewActivity.OUTPUT_LIST);
                if (isDone) {
                    onSelectDone(images);
                } else {
                    imageAdapter.bindSelectImages(images);
                }
            }


        }
    }

    public void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(this);
            cameraPath = cameraFile.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
//            File file= ImageUtil.saveBitmapFile(PictureUtil.getSmallBitmap(cameraPath));
//            UploadPicture(file,hosname);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }


    /**
     * on select done
     *
     * @param medias
     */
    public void onSelectDone(List<LocalMedia> medias) {
        ArrayList<String> images = new ArrayList<>();
        for (LocalMedia media : medias) {
            images.add(media.getPath());
        }

       onResult(images);
    }

    public void onSelectDone(String path) {
        ArrayList<String> images = new ArrayList<>();
        images.add(path);

        onResult(images);
    }

    public void onResult(ArrayList<String> images) {
//        setResult(RESULT_OK, new Intent().putStringArrayListExtra(REQUEST_OUTPUT, images));
        File file= ImageUtil.saveBitmapFile(PictureUtil.getSmallBitmap(images.get(0)));
        UploadPicture(file,hosname);
//        finish();
    }

    public void startPreview(List<LocalMedia> previewImages, int position) {
        ImagePreviewActivity.startPreview(this, previewImages, imageAdapter.getSelectedImages(), maxSelectNum, position);
    }

//    public void startCrop(String path) {
//        ImageCropActivity.startCrop(this, path);
//    }
private void UploadPicture(File file,String name) {
    final String typemingcheng=typename;
    startTitleProgressDialog("努力识别中...");
    okHttpClientManager.doUpload(Constants.OCR + "?UserID="+ userid+"&HospitalName="+hosname+"&HospitalID="+hosid+"&OCRType="+typeid+"&OCRTypeName="+typename, file, getPhotoFileName(), new OkHttpClientManager.RequestCallBack() {
        @Override
        public void onSueecss(String msg) {



            OCRForAndroidDate date = JsonTools.getData(msg, OCRForAndroidDate.class);
            List<OcrImage>list1=date.getData();
            String url=  list1.get(0).getImgUrl();
            List<OcrBean> list = list1.get(0).getData();
            for(OcrBean bean:list){
                if("-".equals(bean.getNum())){
                    bean.setNum("");
                }
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("OcrList", (Serializable) list);
            bundle.putString("typename",typemingcheng);
            bundle.putString("Url",url);
            bundle.putString("typeid",typeid);

            intent.setClass(ImageOCRSelectorActivity.this, OcrActivity.class);
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
    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss" + userid);
        return dateFormat.format(date) + ".png";
    }


}
