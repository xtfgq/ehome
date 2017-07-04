package com.zzu.ehome.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yiguo.toast.Toast;/**/

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.bean.TreatmentInquirywWithPageDate;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.ContainsEmojiEditText;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.GridViewForScrollView;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.greenrobot.event.EventBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static u.aly.au.S;


/**
 * Created by zzu on 2016/4/12.
 */
public class CreateillnessActivity extends BaseActivity implements View.OnClickListener {
    private static final int TAKE_PICTURE = 0x000001;
    public static final int ADD_TIME = 0x11;
    public static final int ADD_TIMES = 0x35;

    private TextView edt_time;
    private TextView edt_jzdw;

    private static final String PACKAGE_URL_SCHEME = "package:";

    private ContainsEmojiEditText edt_jzjg;

    private EditText edt_yyjy;
    private String checktime = "";
    private GridAdapter mAdapter;

    private Button btn_save;
    private GridViewForScrollView noScrollgridview;

    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private RelativeLayout lljzhen;
    private RequestMaker requestMaker;
    private String userid, jzyy, zdjg, yyjy;
    public static final String EXTRA_IMAGES = "extraImages";
    private RecyclerView resultRecyclerView;
    private ImageView singleImageView;
    private RelativeLayout rlphoto;
    private String path;
    private ArrayList<String> images;
    private List<Images> mList = new ArrayList<Images>();
    private final String mPageName = "CreateillnessActivity";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int num = 500;
    private TextView tvWordNumber, tvyyjl;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        requestMaker = RequestMaker.getInstance();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        images = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_IMAGES);
        setContentView(R.layout.activity_illness);

        initViews();

        images = new ArrayList<String>();
        path = ImageUtil.saveResTolocal(CreateillnessActivity.this.getResources(), R.mipmap.icon_add_xd, "add_xd");
        images.add(path);
        mAdapter = new GridAdapter(images, CreateillnessActivity.this);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        resultRecyclerView.setAdapter(mAdapter);
        userid = SharePreferenceUtil.getInstance(CreateillnessActivity.this).getUserId();
        mPermissionsChecker = new PermissionsChecker(CreateillnessActivity.this);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        initEvent();
        if(!CommonUtils.isNotificationEnabled(CreateillnessActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews() {

        edt_time = (TextView) findViewById(R.id.edt_time);
        edt_jzdw = (TextView) findViewById(R.id.edt_jzdw);
        edt_jzjg = (ContainsEmojiEditText) findViewById(R.id.edt_jzjg);
        edt_yyjy = (EditText) findViewById(R.id.edt_yyjy);
        btn_save = (Button) findViewById(R.id.btn_save);
        lljzhen = (RelativeLayout) findViewById(R.id.lljzhen);
        tvWordNumber = (TextView) findViewById(R.id.tvnumber);
        tvyyjl = (TextView) findViewById(R.id.tvyyjl);
//        rlpic = (RelativeLayout) findViewById(R.id.rl_pic);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "添加就诊记录", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        rlphoto = (RelativeLayout) findViewById(R.id.rlphoto);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    public void initEvent() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
//        edt_time.setText(df.format(new Date()));
//        edt_time.setTextColor(getResources().getColor(R.color.actionbar_color));
        lljzhen.setOnClickListener(this);

        btn_save.setOnClickListener(this);
        rlphoto.setOnClickListener(this);
        edt_jzdw.setOnClickListener(this);
        rlphoto.setVisibility(View.VISIBLE);
        resultRecyclerView.setVisibility(View.INVISIBLE);

        edt_jzjg.addTextChangedListener(new TextWatcher() {
            CharSequence wordNum;//记录输入的字数
            int selectionStart;
            int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                //TextView显示剩余字数
                tvWordNumber.setText("还有" + number + "字");
                selectionStart = edt_jzjg.getSelectionStart();
                selectionEnd = edt_jzjg.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    edt_jzjg.setText(s);
                    edt_jzjg.setSelection(tempSelection);//设置光标在最后
                }
            }


        });
        edt_yyjy.addTextChangedListener(new TextWatcher() {
            CharSequence wordNum;//记录输入的字数
            int selectionStart;
            int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                tvyyjl.setText("还有" + number + "字");
                selectionStart = edt_jzjg.getSelectionStart();
                selectionEnd = edt_jzjg.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    edt_jzjg.setText(s);
                    edt_jzjg.setSelection(tempSelection);//设置光标在最后
                }
            }


        });
//        requsetHos();

    }

    private void requsetHos() {
        requestMaker.TreatmentInquirywWithPage(userid, 1 + "", "1", new JsonAsyncTask_Info(CreateillnessActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentInquirywWithPage");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode")) {

                    } else {

                        TreatmentInquirywWithPageDate date = JsonTools.getData(result.toString(), TreatmentInquirywWithPageDate.class);
                        List<TreatmentInquirywWithPage> list = date.getData();
                        edt_jzdw.setText(list.get(0).getHosname());
                        edt_jzdw.setTextColor(getResources().getColor(R.color.actionbar_color));

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onError(Exception e) {
                if(btn_save!=null)
                setEventEnable(true);
            }
        }));
    }

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mcontext;

        public List<String> getmList() {
            return mList;
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
        }

        private List<String> mList;

        public GridAdapter(List<String> list, Context context) {
            super();
            this.mList = list;
            this.mcontext = context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(CreateillnessActivity.this)
                    .load(new File(images.get(position)))
                    .centerCrop()
                    .into(holder.imageView);
            if (mList.get(position).equals(path)) {
                holder.imageClose.setVisibility(View.GONE);
            } else {
                holder.imageClose.setVisibility(View.VISIBLE);
            }
            holder.imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mList.remove(position);
                    if (mList.size() == 1) {
                        rlphoto.setVisibility(View.VISIBLE);
                        resultRecyclerView.setVisibility(View.INVISIBLE);

                    }
                    if (mList.size() == 4 && (!mList.get(mList.size() - 1).equals(path))) {

                        images.add(path);
                        setmList(images);
                    }

                    notifyDataSetChanged();

//                    rlpic.setVisibility(View.VISIBLE);
                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).equals(path)) {

                        ImageSelectorActivity.start((Activity) mcontext, 5, ImageSelectorActivity.MODE_MULTIPLE, true, false, false, images.size() - 1);
                    } else {
                        Intent intent = new Intent(mcontext, ImageAlbumManager.class);
                        if (mList.size() == 5) {
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                        } else {
                            List<String> imgs = new ArrayList<String>(mList);
                            //imgs=mList;
                            imgs.remove(mList.size() - 1);
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) imgs);
                        }
                        intent.putExtra("position", position);
                        intent.putExtra("type", 2);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView imageClose;


            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageClose = (ImageView) itemView.findViewById(R.id.ivclose);
            }
        }
    }

    @Override
    public void onClick(View v) {
       if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if(!CommonUtils.isNotificationEnabled(CreateillnessActivity.this)){
            showTitleDialog("请打开通知中心");
        }
        switch (v.getId()) {
            case R.id.rlphoto:
                ImageSelectorActivity.start(CreateillnessActivity.this, 5, ImageSelectorActivity.MODE_MULTIPLE, true, false, false, images.size() - 1);
                break;


            case R.id.lljzhen:
                Intent intenttime = new Intent(CreateillnessActivity.this, SelectDateAct.class);
                startActivityForResult(intenttime, Constants.ADDTTIME);
                break;
            case R.id.edt_jzdw:
                Intent intenttimes = new Intent(this, HosListActivity.class);
                startActivityForResult(intenttimes, ADD_TIMES);
                break;
            case R.id.btn_save:
                //保存按钮
             /*  if (CommonUtils.isFastClick())
                    return;*/

                jzyy = edt_jzdw.getText().toString();
                zdjg = StringFilter(edt_jzjg.getText().toString()) ;
                yyjy =StringFilter(edt_yyjy.getText().toString()) ;
                checktime = edt_time.getText().toString();
                if (jzyy.equals("")) {
                    showMessage("请填写就诊医院!");
                    setEventEnable(true);
                    return;
                } else if (TextUtils.isEmpty(checktime)) {
                    showMessage("请选择就诊时间!");
                    setEventEnable(true);
                    return;
                } else if (TextUtils.isEmpty(zdjg) || zdjg.length() < 10) {
                    showMessage("诊断结果不能低于10个字!");
                    setEventEnable(true);

                    return;
                } else if (TextUtils.isEmpty(yyjy) || yyjy.length() < 10) {
                    showMessage("用药建议不能低于10个字!");
                    setEventEnable(true);
                    return;
                }
                startProgressDialog();
                if (images != null && images.size() > 0) {
                    if (images.size() > 5) {
                        ToastUtils.showMessage(CreateillnessActivity.this, "你最多可以选择5张");
                        setEventEnable(true);
                        return;
                    }
                    List<String> imgs = new ArrayList<String>(images);

                    if (images.size() <=5&&imgs.get(imgs.size() - 1).equals(path)) {
                        imgs.remove(imgs.size() - 1);

                    }

                    for (int i = 0; i < imgs.size(); i++) {
                        Images imag = new Images();
                        Bitmap newBitmap = BitmapFactory.decodeFile(ImageUtil.doPicture(images.get(i)));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        newBitmap.compress(Bitmap.CompressFormat.JPEG,
                                100, baos);
                        imag.setCode(ImageUtil.Bitmap2StrByBase64(newBitmap));
                        imag.setFileName(getPhotoFileName(i));
                        mList.add(imag);
                    }
                }
                zdjg = zdjg.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;");
                yyjy = yyjy.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;");
                setEventEnable(false);
                requestMaker.OtherTreatmentInsert(userid, checktime, jzyy, zdjg, yyjy, mList, new JsonAsyncTask_Info(CreateillnessActivity.this, true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {
                        try {
                            stopProgressDialog();
                            String value = result.toString();
                            JSONObject mySO = (JSONObject) result;
                            org.json.JSONArray array = mySO
                                    .getJSONArray("OtherTreatmentInsert");
                            edt_time.setText("");
                            edt_jzdw.setText("");
                            edt_jzjg.setText("");
                            edt_yyjy.setText("");
                            if (mList.size() > 0) {
                                images.clear();
                                mAdapter.setmList(images);
                                mAdapter.notifyDataSetChanged();
                            }
                            RxBus.getInstance().send(new EventType(Constants.HealthData));
//                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_file)));


                            Toast.makeText(CreateillnessActivity.this, "保存成功!",
                                    Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                            finishActivity();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            setEventEnable(true);
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        if(btn_save!=null)
                        setEventEnable(true);
                    }

                }));


                break;

        }
    }
    public void setEventEnable(boolean flag){
        if(flag){
            btn_save.setEnabled(true);
            btn_save.setBackgroundResource(R.color.actionbar_color);
        }
        else{
            btn_save.setEnabled(false);
            btn_save.setBackgroundResource(R.color.bottom_text_color_normal);
        }
    }
    private String getPhotoFileName(int i) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        return dateFormat.format(date) + i + "_" + ".jpg";
    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageSelectorActivity.REQUEST_IMAGE:
                resultRecyclerView.setVisibility(View.VISIBLE);
                rlphoto.setVisibility(View.GONE);
                if (data != null) {
                    ArrayList<String> images1 = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    if (images1 != null) {
                        for (int i = 0; i < images1.size(); i++) {
                            images.add(0, images1.get(i));
                        }
                        if (images.size() > 6) {

                            ToastUtils.showMessage(CreateillnessActivity.this, "你最多可以选择5张");
                        }
                        if (images.size() == 6) {
                            images.remove(5);
                        }
                    }

                    mAdapter.setmList(images);
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case Constants.ADDTTIME:
                if (data != null) {
                    String time = data.getStringExtra("time");
                    if (!TextUtils.isEmpty(time)) {
                        edt_time.setText(time);
                        checktime = time;
                        edt_time.setTextColor(getResources().getColor(R.color.actionbar_color));
                    }
                }
                break;
            case ADD_TIMES:
                if (data != null) {
                    String times = data.getStringExtra("times");
                    if (!TextUtils.isEmpty(times)) {
                        edt_jzdw.setText(times);
                        edt_jzdw.setTextColor(getResources().getColor(R.color.actionbar_color));
                    }

                }

                break;

        }
    }
    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}''\\[\\].<>~！@#￥%……&*（）——+|{}【】‘”“’？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mList = null;
        edt_time.setText("");
        edt_jzdw.setText("");
        edt_jzjg.setText("");
        edt_yyjy.setText("");


    }


    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(this, "请点击设置，打开所需存储权限",
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
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    private void showMessage(String message) {
        DialogTips dialog = new DialogTips(CreateillnessActivity.this, "", message,
                "确定", true, true);

        dialog.show();
        dialog = null;
    }
}
