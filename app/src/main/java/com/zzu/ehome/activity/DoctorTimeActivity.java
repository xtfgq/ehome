package com.zzu.ehome.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.DoctorTimeAdapter;
import com.zzu.ehome.bean.DoctorSchemaByTopmdBean;
import com.zzu.ehome.bean.DoctorSchemaByTopmdDate;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.zzu.ehome.R.attr.position;
import static com.zzu.ehome.R.id.ivmore;
import static com.zzu.ehome.R.id.tv;
import static com.zzu.ehome.R.id.tvdes;
import static com.zzu.ehome.R.id.tvjianjie;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorTimeActivity extends BaseActivity {
    private ListView listView;
    private DoctorTimeAdapter adapter;
    private ImageView icon_back;
    private ImageView icon_share;
    private TextView tv_title;
    private RequestMaker requestMaker;
    String hosid,depid,doctorid,DepartmentName,picUrl,name,HospitalName,GoodDisease;
    private TextView tvname,tv_msg;
    private ImageView icon_user;
    private ImageLoader mImageLoader;
    private RelativeLayout   rltop;
    private LinearLayout lldes,lljianjie;
    private int index = 0;
    private TextView tvjianjie,tvdes;
    private ImageView ivmore;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_doctor_time);
        requestMaker=RequestMaker.getInstance();
        hosid=this.getIntent().getStringExtra("HospitalID");
        depid=this.getIntent().getStringExtra("DepartmentID");
        doctorid=this.getIntent().getStringExtra("DoctorId");
        DepartmentName=this.getIntent().getStringExtra("DepartName");
        picUrl=this.getIntent().getStringExtra("picUrl");
        name=this.getIntent().getStringExtra("doctorName");
        HospitalName=this.getIntent().getStringExtra("HospitalName");
        GoodDisease=this.getIntent().getStringExtra("GoodDisease");
        mImageLoader=ImageLoader.getInstance();
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(DoctorTimeActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        icon_user=(ImageView) findViewById(R.id.icon_user);
        icon_back = (ImageView) findViewById(R.id.icon_back);
        icon_share = (ImageView) findViewById(R.id.icon_share);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvname=(TextView)findViewById(R.id.tv_name);
        tv_msg=(TextView)findViewById(R.id.tv_msg);
        rltop=(RelativeLayout)findViewById(R.id.rltop);
        lljianjie=(LinearLayout)findViewById(R.id.lljianjie);
        lldes = (LinearLayout) findViewById(R.id.lldes);
        tvjianjie = (TextView) findViewById(R.id.tvjianjie);
        tvdes = (TextView) findViewById(R.id.tvdes);
        ivmore = (ImageView) findViewById(R.id.ivmore);
        if(picUrl.contains("vine")){
            icon_user.setBackgroundResource(R.drawable.icon_doctor);
        }else {
            mImageLoader.displayImage(picUrl, icon_user);
        }
        tv_msg.setText(HospitalName);
        ViewGroup.LayoutParams para;
        para =  rltop.getLayoutParams();
        para.width = ScreenUtils.getScreenWidth(DoctorTimeActivity.this);
        para.height = para.width*20/75;
        rltop.setLayoutParams(para);
        if(TextUtils.isEmpty(GoodDisease)){
            lljianjie.setVisibility(View.GONE);
        }else{
            lljianjie.setVisibility(View.VISIBLE);
        }
    }

    public void initEvent() {
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        icon_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(DoctorTimeActivity.this, "点击分享");
            }
        });
        tv_title.setText(name);
        tvname.setText(DepartmentName);
        tvjianjie.setText(GoodDisease);
        tvjianjie.post(new Runnable() {
            @Override
            public void run() {
                int line=tvjianjie.getLineCount();
                if(tvjianjie.getLineCount()>2){
                    turnToDown(ivmore);
                    tvdes.setText("查看详情");
                    tvjianjie.setMaxLines(2);
                    lldes.setVisibility(View.VISIBLE);
                }else{
                    lldes.setVisibility(View.GONE);
                }
            }
        });
        lldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index % 2 == 0) {
                    tvjianjie.setMaxLines(100);
                    turnToUp(ivmore);
                    tvdes.setText("收起详情");
                } else {
                    tvjianjie.setMaxLines(2);
                    turnToDown(ivmore);
                    tvdes.setText("查看详情");
                }
                index++;
            }
        });
    }

    public void initDatas() {
       if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        requestMaker.DoctorSchemaByTopmd(hosid,depid,doctorid,new JsonAsyncTask_Info(
                DoctorTimeActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String str=result.toString();

                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("DoctorSchemaByTopmd");
                    if(listView!=null) {
                        if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(DoctorTimeActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                            show(array.getJSONObject(0).getString("MessageContent").toString());
                        } else {
                            DoctorSchemaByTopmdDate date = JsonTools.getData(result.toString(), DoctorSchemaByTopmdDate.class);
                            List<DoctorSchemaByTopmdBean> mList = date.getData();
                            adapter = new DoctorTimeAdapter(DoctorTimeActivity.this, mList, hosid, depid, doctorid, DepartmentName);
                            listView.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));

    }
    private void turnToUp(ImageView iv) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_arr_g_down)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(180f);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }
    private void turnToDown(ImageView iv) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_arr_g_down)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(360f);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }
}
