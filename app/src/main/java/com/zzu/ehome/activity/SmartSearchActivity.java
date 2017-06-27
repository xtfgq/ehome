package com.zzu.ehome.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.TitleInquiryBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.GlideRoundTransform;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;





/**
 * Created by Administrator on 2017/1/4.
 */

public class SmartSearchActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_tips;
    private TextView tv_shengming;

    private Button btn_ok;
    private RelativeLayout layout_add_hosptial;
    public static final int ADD_HOSPITAL = 0x00;
    private RequestMaker requestMaker;
    private List<TitleInquiryBean> mList;
    private String hosptial, hospital_id;
    private TextView tv_hospital;
    private int index = 0;
    private TextView ed_name,edt_card_num;
    private EditText ed_code;
    private String userid;
    private String name,userno;
    private EHomeDao dao;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_smartsearch);
        mList = new ArrayList<TitleInquiryBean>();
        requestMaker = RequestMaker.getInstance();
        dao = new EHomeDaoImpl(SmartSearchActivity.this);
        userid= SharePreferenceUtil.getInstance(SmartSearchActivity.this).getUserId();

        initViews();
        if (!CommonUtils.isNotificationEnabled(SmartSearchActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }

        initEvnets();
        initData();

    }

    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "智能查询", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_shengming = (TextView) findViewById(R.id.tv_shengming);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        layout_add_hosptial = (RelativeLayout) findViewById(R.id.layout_add_hosptial);
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        ed_name=(TextView)findViewById(R.id.ed_name);
        edt_card_num=(TextView)findViewById(R.id.edt_card_num);
        name=dao.findUserInfoById(userid).getUsername();
        userno=dao.findUserInfoById(userid).getUserno();


        ed_name.setText(name);
        edt_card_num.setText(userno);
        ed_code=(EditText)findViewById(R.id.ed_code);
    }

    private void initEvnets() {
        tv_tips.setOnClickListener(this);
        tv_shengming.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        layout_add_hosptial.setOnClickListener(this);
    }

    /**
     * 提示示例
     */
    private void showTips() {
        LayoutInflater inflater = LayoutInflater.from(SmartSearchActivity.this);
        View layout = inflater.inflate(R.layout.dialog_tips, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SmartSearchActivity.this);
        builder.setView(layout);
        builder.setCancelable(false);
        final Dialog dialog = builder.show();
        TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
        ImageView iv = (ImageView) layout.findViewById(R.id.iv_tips);
        String url = Constants.EhomeURL + mList.get(index).getImg().replace("~", "").replace("\\", "/");
        Glide.with(SmartSearchActivity.this)
                .load(url)
                .centerCrop().error(R.drawable.pic_defaultads).transform(new GlideRoundTransform(SmartSearchActivity.this, 5))
                .into(iv);
        tvok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tips:
                if (mList.size() > 0) {
                    showTips();
                }
                break;
            case R.id.tv_shengming:
                startActivity(new Intent(SmartSearchActivity.this, DeclarationWebActivity.class));
                break;
            case R.id.btn_ok:

                String code=ed_code.getText().toString().replaceAll("\n","").replaceAll(" ","");

                if(TextUtils.isEmpty(code)&&hosptial.contains("郑州大学医院")){
                    show("请输入体检编号");
                    setTVEeable(true);
                    return;
                }
//                name="张三";
//                userno="410322198608051234";
//                code="0000000000";
                if(TextUtils.isEmpty(code)){
                    Intent i = new Intent(SmartSearchActivity.this, PlatformHospitalActivity.class);
                    i.putExtra("hosptial", hosptial);
                    i.putExtra("name", name);
                    i.putExtra("UserNo", userno);
                    startActivity(i);
                }else {
                    setTVEeable(false);
                    doSave(name, userno, hosptial, code);
                }
                break;
            case R.id.layout_add_hosptial:
                if (mList.size() > 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("HosList", (Serializable) mList);
                    intent.setClass(SmartSearchActivity.this, SelectPlatmHospitalActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ADD_HOSPITAL);
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_HOSPITAL && resultCode == ADD_HOSPITAL && data != null) {
            hosptial = data.getStringExtra("hospital");
            hospital_id = data.getStringExtra("hospital_id");
            tv_hospital.setText(hosptial);
            index = data.getIntExtra("index", 0);
            ed_code.setText("");
            if(hosptial.contains("郑州大学医院")){
                ed_code.setHint("请输入体检编号");
            }else{
                ed_code.setHint("请输入体检编号(非必填)");
            }
            String userIdWithCode=SharePreferenceUtil.getInstance(
                    SmartSearchActivity.this).getSmartSearchCode();
            if(userIdWithCode.contains(",")){
                String codes[]=userIdWithCode.split(",");
                for(String str:codes){
                    if(str.contains(":")){
                        String strs[]=str.split(":");
                        if(strs.length==3){
                            if(strs[0].equals(userid)&&strs[1].equals(hosptial)){
                                ed_code.setText(strs[2]);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取医院及实例数据
     */

    public void initData() {
        startProgressDialog();
        requestMaker.HospitalInquiryPMD(new JsonAsyncTask_Info(SmartSearchActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                stopProgressDialog();
                try {
                    JSONArray array = mySO.getJSONArray("HospitalInquiryPMD");
                    if (array.getJSONObject(0).has("MessageCode")) {

                    } else {
                        TitleInquiryBean tbf = new TitleInquiryBean();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            int ischk = Integer.valueOf(json.getString("IsCHK"));
                            if (ischk == 1) {
                                TitleInquiryBean tb = new TitleInquiryBean();
                                String id = json.getString("HospitalID");
                                tb.setCode(id);
                                String Value = json.getString("HospitalName");
                                tb.setValue(Value);
                                String img = json.getString("CHKImage");
                                tb.setImg(img);
                                mList.add(tb);
                            }
                        }
                        if (mList.size() > 0) {
                            hosptial = mList.get(0).getValue();
                            hospital_id = mList.get(0).getCode();
                            index = 0;
                            ed_code.setText("");
                            tv_hospital.setText(hosptial);
                            if(hosptial.contains("郑州大学医院")){
                                ed_code.setHint("请输入体检编号");
                            }else{
                                ed_code.setHint("请输入体检编号(非必填)");
                            }
                            String userIdWithCode=SharePreferenceUtil.getInstance(
                                    SmartSearchActivity.this).getSmartSearchCode();
                            if(userIdWithCode.contains(",")){
                                String codes[]=userIdWithCode.split(",");
                                for(String str:codes){
                                    if(str.contains(":")){
                                        String strs[]=str.split(":");
                                        if(strs.length==3){
                                            if(strs[0].equals(userid)&&strs[1].equals(hosptial)){
                                                ed_code.setText(strs[2]);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }

        }));


    }

    public void setTVEeable(boolean flag){
        if(flag){
            btn_ok.setEnabled(true);
            btn_ok.setBackgroundResource(R.color.actionbar_color);
        }else{
            btn_ok.setEnabled(false);
            btn_ok.setBackgroundResource(R.color.bottom_text_color_normal);
        }

    }

    /**
     * 查询相关平台数据
     * MessageCode:1，失败，2查不到数据含有表示已经保存过
     * 3.表示已经存过了
     */

    private void doSave(final String name,final String userno,final String hosname,final String code){

        requestMaker.ZDWFYUserRecordJudgeInquiry(code,userno,name,hosname,new JsonAsyncTask_Info(SmartSearchActivity.this,
                true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                setTVEeable(true);
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("ZDWFYUserRecordJudgeInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        int flag=Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                        if(flag==3){
                            StringBuffer sbf=new StringBuffer();
                            String string=userid+":"+hosname+":"+code+",";
                            String str= SharePreferenceUtil.getInstance(SmartSearchActivity.this).getSmartSearchCode();
                            if(!TextUtils.isEmpty(str)){
                                if(!str.equals(string)){
                                    sbf.append(str);

                                }

                            }
                            sbf.append(string);
                            SharePreferenceUtil.getInstance(SmartSearchActivity.this).setSmartSearchCode(sbf.toString());
                                Intent i = new Intent(SmartSearchActivity.this, WebPlatmActivity.class);
                                i.putExtra("flag", "info");
                                i.putExtra("name", name);
                                i.putExtra("UserNo", userno);
                                i.putExtra("code", code);
                                i.putExtra("time", array.getJSONObject(0).getString("RecordTime"));
                                i.putExtra("hosname", hosptial);
                                startActivity(i);
                        }else {
                            show(array.getJSONObject(0).getString("MessageContent"));
                        }
                    }else {
                        StringBuffer sbf=new StringBuffer();
                        String string=userid+":"+hosname+":"+code+",";
                        String str= SharePreferenceUtil.getInstance(SmartSearchActivity.this).getSmartSearchCode();
                        if(!TextUtils.isEmpty(str)){
                            if(!str.equals(string)){
                                sbf.append(str);

                            }

                        }
                        sbf.append(string);
                        SharePreferenceUtil.getInstance(SmartSearchActivity.this).setSmartSearchCode(sbf.toString());
                            String time= array.getJSONObject(0).getString("RecordTime");
                            Intent i = new Intent(SmartSearchActivity.this, WebPlatmActivity.class);
                            i.putExtra("flag", "add");
                            i.putExtra("name", name);
                            i.putExtra("code", code);
                            i.putExtra("UserNo", userno);
                            i.putExtra("time", time);
                            i.putExtra("hosname", hosptial);
                            i.putExtra("hospital_id", hospital_id);
                            startActivity(i);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                setTVEeable(true);
            }
        }));

    }

//    @Override
//    public void onSelect(String select) {
//        select_year.setText(select);
//    }
//    public void showAgePop(int year){
//        ypw=new YearPopWindow(SmartSearchActivity.this,layout_main,year);
//        ypw.setOnYearSelectListener(SmartSearchActivity.this);
//    }
}
