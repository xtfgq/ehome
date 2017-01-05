package com.zzu.ehome.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    String hosid,depid,doctorid,DepartmentName,picUrl,name,HospitalName;
    private TextView tvname,tv_msg;
    private ImageView icon_user;
    private ImageLoader mImageLoader;
    private RelativeLayout   rltop;


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
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(DoctorTimeActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                        show(array.getJSONObject(0).getString("MessageContent").toString());
                    } else {
                        DoctorSchemaByTopmdDate date = JsonTools.getData(result.toString(), DoctorSchemaByTopmdDate.class);
                        List<DoctorSchemaByTopmdBean> mList=date.getData();
                        adapter=new DoctorTimeAdapter(DoctorTimeActivity.this,mList,hosid,depid,doctorid,DepartmentName);
                        listView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

    }
}
