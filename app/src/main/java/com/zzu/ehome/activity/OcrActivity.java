package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.OcrAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.OcrBean;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.zzu.ehome.R.id.tv_fysj_time;

/**
 * Created by Administrator on 2016/9/14.
 */
public class OcrActivity extends BaseActivity implements View.OnClickListener{
    private ListView listView;
    private OcrAdapter mAdapter;
    private List<OcrBean> list=null;
    private Button btn_save;
    private Boolean isPost=true;

    private RequestMaker requestMaker;
    private String userid,typename,typeid;
    private EHomeDao dao;
    private User dbUser;
    private TextView tv_fysj_time;
    private RelativeLayout layout_fysj;
    String url;
    private ScrollView sv;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_ocr_edit);
        list= (List<OcrBean>)this.getIntent().getSerializableExtra("OcrList");
        url=this.getIntent().getStringExtra("Url");
        typename=this.getIntent().getStringExtra("typename");
        typeid=this.getIntent().getStringExtra("typeid");
        requestMaker=RequestMaker.getInstance();
        dao = new EHomeDaoImpl(this);
        userid= SharePreferenceUtil.getInstance(OcrActivity.this).getUserId();
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, typename, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        sv=(ScrollView)findViewById(R.id.svocr);
        listView=(ListView)findViewById(R.id.lilstView);
        btn_save=(Button)findViewById(R.id.btn_save);
        layout_fysj=(RelativeLayout)findViewById(R.id.layout_fysj);
        tv_fysj_time=(TextView)findViewById(R.id.tv_fysj_time);
    }


    public void initEvent() {

        btn_save.setOnClickListener(OcrActivity.this);
        layout_fysj.setOnClickListener(this);
        if(list.size()==0){
            showDialog("上传图片与模板不匹配，请上传正确图片！");
            btn_save.setVisibility(View.GONE);
        }else{
            btn_save.setVisibility(View.VISIBLE);
        }
    }


    public void initDatas() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tv_fysj_time.setText(sdf.format(new Date()));
        mAdapter=new OcrAdapter(OcrActivity.this,list);
        listView.setAdapter(mAdapter);
        sv.smoothScrollTo(0,0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                dbUser = dao.findUserInfoById(userid);
                if(dbUser.getUserno()==null||TextUtils.isEmpty(dbUser.getUserno())){
                    startActivity(new Intent(OcrActivity.this,PersonalCenterInfo.class));
                    return;
                }
                for (int i = 0; i < listView.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout)listView.getChildAt(i);
                    EditText et = (EditText) layout.findViewById(R.id.ednum);
                    if(!IOUtils.isRealNumber(et.getText().toString().trim())){
                        ToastUtils.showMessage(OcrActivity.this,"输入数字不合法！");
                        isPost=false;
                        break;
                    }else{
                        list.get(i).setNum(et.getText().toString().replaceAll("\\s*", ""));
                        isPost=true;
                    }
                }
                if(isPost){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    df.format(new Date());
                    btn_save.setEnabled(false);
                    requestMaker.BloodRoutineInsert(userid,dbUser.getUserno(),tv_fysj_time.getText().toString() ,url,typeid,list,new JsonAsyncTask_Info(OcrActivity.this, true, new JsonAsyncTaskOnComplete() {
                        @Override
                        public void processJsonObject(Object result) {
                            try {
                                btn_save.setEnabled(true);
                                JSONObject mySO = (JSONObject) result;
                                org.json.JSONArray array = mySO
                                        .getJSONArray("BloodRoutineInsert");
                                Toast.makeText(OcrActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                        Toast.LENGTH_SHORT).show();
                                if (array.getJSONObject(0).getString("MessageCode").toString().equals("0")) {
                                    CustomApplcation.getInstance().finishSingleActivityByClass(ImageOCRSelectorActivity.class);
                                    CustomApplcation.getInstance().finishSingleActivityByClass(SelectOCRHosActivity.class);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    }
                 ));
                }
            break;
            case R.id.layout_fysj:
                Intent intenttime = new Intent(OcrActivity.this, SelectDateAct.class);

                startActivityForResult(intenttime, Constants.ADDTTIME);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADDTTIME:
                if (data != null) {
                    String time = data.getStringExtra("time");
                    if (!TextUtils.isEmpty(time)) {
                        tv_fysj_time.setText(time);
                    }
                }
                break;
        }
    }
    private void showDialog(String message) {

        DialogTips dialog = new DialogTips(OcrActivity.this, message, "确定");

        dialog.show();
        dialog = null;

    }
}
