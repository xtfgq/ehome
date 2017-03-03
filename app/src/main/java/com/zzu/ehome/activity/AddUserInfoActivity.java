package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.CardUtil;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IDCardValidate;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_ECGInfo;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.SexPopupWindows;
import com.zzu.ehome.view.SexPopupWindows.OnGetData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mersens on 2016/8/10.
 */
public class AddUserInfoActivity extends BaseActivity implements View.OnClickListener,OnGetData {
    private RequestMaker requestMaker;
    private EditText edt_name,edt_card_num,edt_phone;
    private TextView edt_age;
    private SexPopupWindows sexPopupWindows;
    private String name,userNo,age,phone,sex="",userid;
    private RelativeLayout  rlsex,rlname,rlphone,rlage;

    private TextView tv_sex;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_adduser_info);
        requestMaker=RequestMaker.getInstance();
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(AddUserInfoActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "添加就诊人", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        edt_name=(EditText)findViewById(R.id.edt_name);
        edt_card_num=(EditText)findViewById(R.id.edt_card_num);

        edt_age=(TextView)findViewById(R.id.edt_age);
        edt_phone=(EditText)findViewById(R.id.edt_phone);
        rlsex=(RelativeLayout)findViewById(R.id.rlsex);
        btn_save=(Button)findViewById(R.id.btn_save);
        tv_sex=(TextView)findViewById(R.id.tv_sex);
        rlname=(RelativeLayout)findViewById(R.id.rlname);
        rlage=(RelativeLayout)findViewById(R.id.rlage);
        rlphone=(RelativeLayout)findViewById(R.id.rlphone);
    }

    public void initEvent(){
        btn_save.setOnClickListener(this);
        rlsex.setOnClickListener(this);
        rlage.setOnClickListener(this);
        rlname.setOnClickListener(this);
        rlphone.setOnClickListener(this);
        edt_card_num.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                  doAge();
                }

            }
        });
    }

    public void initDatas(){

    }
    private void doSex() {
        InputMethodManager imm = (InputMethodManager)getSystemService(AddUserInfoActivity.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_name.getWindowToken(),0);
        imm.hideSoftInputFromWindow(edt_card_num.getWindowToken(),0);
        imm.hideSoftInputFromWindow(edt_age.getWindowToken(),0);
        sexPopupWindows = new SexPopupWindows(AddUserInfoActivity.this, rlsex, AddUserInfoActivity.this);
        sexPopupWindows.setmOnGetData(this);

    }


    @Override
    public void onClick(View v) {
       if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        if(!doAge()){
            return;
        }
        switch (v.getId()){
            case R.id.btn_save:
                name=edt_name.getText().toString();
                userNo=edt_card_num.getText().toString();
                age=edt_age.getText().toString();
                phone=edt_phone.getText().toString();

                if(TextUtils.isEmpty(name.trim())){
                    show("请填写姓名！");
                    return;
                }
                if(name.length()>4){
                    show("姓名长度超长！");
                    return;
                }

                if (TextUtils.isEmpty(userNo.trim())) {
                    show("请填写身份证号码！");
                    return;
                }

                if (TextUtils.isEmpty(phone.trim())) {
                    show("请填写手机号码！");
                    return;
                }
                if( !IOUtils.isMobileNO(phone.trim())){
                    show( "手机号码格式不正确！");
                    return;
                }
                if (TextUtils.isEmpty(age.trim())) {
                    show("请填写年龄！");
                    return;
                }
//                int ageed=Integer.valueOf(edt_age.getText().toString());
//                if(ageed>150){
//                    show( "年龄过长！");
//                    return;
//                }

                if (TextUtils.isEmpty(sex)) {
                   show("请选择性别！");
                    return;
                }

                userid= SharePreferenceUtil.getInstance(AddUserInfoActivity.this).getUserId();
                requestMaker.UserContactorInsert(userid,name,userNo,age,sex,phone,"",  new JsonAsyncTask_ECGInfo(AddUserInfoActivity.this, true, new JsonAsyncTaskOnComplete() {
                    @Override
                    public void processJsonObject(Object result) {

                        try {
                            String value = result.toString();
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO.getJSONArray("Result");
                            int code=Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                            if(edt_name!=null&&edt_card_num!=null&&edt_age!=null&&edt_phone!=null&&tv_sex!=null) {
                                if (code == 1) {
                                    edt_name.setText("");
                                    edt_card_num.setText("");
                                    edt_age.setText("");
                                    edt_phone.setText("");
                                    tv_sex.setText("请选择性别");
                                    Intent data = new Intent();
                                    data.putExtra("NEW", "new");
                                    setResult(SelectPatientActivity.ADD_PATIENT, data);
                                    ToastUtils.showMessage(AddUserInfoActivity.this, "添加成功");
                                    finish();
                                } else {
                                    show(array.getJSONObject(0).getString("MessageContent").toString());

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



                break;
            case R.id.rlsex:
                doSex();
                break;
        }
    }


    @Override
    public void onDataCallBack(String s) {

        if(s.equals("01")){
            sex="01";
            tv_sex.setText("男");
        }else{
            sex="02";
            tv_sex.setText("女");
        }
    }
    private boolean  doAge() {
        boolean isCard=false;
        if (edt_card_num.getText().toString().length() > 14) {
            try {
                String info = IDCardValidate.IDCardValidateStr(edt_card_num.getText().toString().toLowerCase());
                if (!TextUtils.isEmpty(info)) {
                    show("身份证号格式不正确");
                    isCard=false;

                }else {
                    if (edt_card_num.getText().toString().length() == 18) {
                        edt_age.setText(CardUtil.getCarInfo(edt_card_num.getText().toString()));
                    } else if (edt_card_num.getText().toString().length() == 15) {
                        edt_age.setText(CardUtil.getCarInfo15W(edt_card_num.getText().toString()));
                    }
                    isCard=true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(edt_card_num.getText().toString().length() > 0) {
            show("身份证号格式不正确");
            edt_card_num.setFocusable(true);
            isCard=false;

        }
        return  isCard;
    }
}
