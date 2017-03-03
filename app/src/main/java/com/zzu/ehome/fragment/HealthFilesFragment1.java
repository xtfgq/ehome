package com.zzu.ehome.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.RegisterFinishAct;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.HealthFiles;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesFragment1 extends BaseFragment {
    private View mView;
    private Button btn_save;
    private RadioButton weiHunCheck, jieHunCheck, liHunCheck, sangOuCheck;
    private static String tagfile="";

    private RadioGroup maritalStatus_group, medicineAllergy_group, pastMedicalHistory_group,
            familyMedicalhistory_father_group, familyMedicalhistory_mother_group,
            familyMedicalhistory_sister_group, familyMedicalhistory_children_group,
            geneticHistory_group, smokeState_group, drink_group;

    private RadioButton medicineAllergy_yesCheck, medicineAllergy_noCheck, pastMedicalHistory_yesCheck,
            pastMedicalHistory_noCheck, familyMedicalhistory_father_yesCheck,
            familyMedicalhistory_father_noCheck, familyMedicalhistory_mother_yesCheck,
            familyMedicalhistory_mother_noCheck, familyMedicalhistory_sister_yesCheck,
            familyMedicalhistory_sister_noCheck, familyMedicalhistory_children_yesCheck,
            familyMedicalhistory_children_noCheck, geneticHistory_yesCheck,
            geneticHistory_noCheck, smokeState_yesCheck, smokeState_noCheck,
            drinkState_yesCheck, drinkState_noCheck;

    private TableLayout medicineAllergy_type, pastMedicalHistory_type, familyMedicalhistory_father_type,
            familyMedicalhistory_mother_type, familyMedicalhistory_sister_type,
            familyMedicalhistory_children_type, geneticHistory_type;

    private LinearLayout smokeState_type, drinkState_type;

    private CheckBox medicineAllergy_checkbox_qingmeisu, medicineAllergy_checkbox_huangan,
            medicineAllergy_checkbox_lianmeisu,
            medicineAllergy_checkbox_qita;

    private CheckBox pastMedicalHistory_checkbox_gaoxueya, pastMedicalHistory_checkbox_tangniaobing,
            pastMedicalHistory_checkbox_guanxinbing, pastMedicalHistory_checkbox_naozuzhong,
            pastMedicalHistory_checkbox_jiehebing, pastMedicalHistory_checkbox_exingzhongliu,
            pastMedicalHistory_checkbox_ganyan, pastMedicalHistory_checkbox_chuanranbing,
            pastMedicalHistory_checkbox_zzjb, pastMedicalHistory_checkbox_mxfjb,
            pastMedicalHistory_checkbox_qita;

    private CheckBox fmh_father_checkbox_gxy, fmh_father_checkbox_tnb, fmh_father_checkbox_gxb,
            fmh_father_checkbox_gy, fmh_father_checkbox_jhb, fmh_father_checkbox_exzl,
            fmh_father_checkbox_xtjx, fmh_father_checkbox_zsxfjb, fmh_father_checkbox_qt;

    private CheckBox fmh_mother_checkbox_gxy, fmh_mother_checkbox_tnb, fmh_mother_checkbox_gxb,
            fmh_mother_checkbox_gy, fmh_mother_checkbox_jhb, fmh_mother_checkbox_exzl,
            fmh_mother_checkbox_xtjx, fmh_mother_checkbox_zsxfjb, fmh_mother_checkbox_qt;

    private CheckBox fmh_children_checkbox_gxy, fmh_children_checkbox_tnb, fmh_children_checkbox_gxb,
            fmh_children_checkbox_gy, fmh_children_checkbox_jhb, fmh_children_checkbox_exzl,
            fmh_children_checkbox_xtjx, fmh_children_checkbox_zsxfjb, fmh_children_checkbox_qt;

    private CheckBox fmh_sister_checkbox_gxy, fmh_sister_checkbox_tnb, fmh_sister_checkbox_gxb,
            fmh_sister_checkbox_gy, fmh_sister_checkbox_jhb, fmh_sister_checkbox_exzl,
            fmh_sister_checkbox_xtjx, fmh_sister_checkbox_zsxfjb, fmh_sister_checkbox_qt;

    private CheckBox gh_checkbox_gxy, gh_checkbox_tnb, gh_checkbox_gxb, gh_checkbox_nzu,
            gh_checkbox_jhb, gh_checkbox_exzl, gh_checkbox_gy, gh_checkbox_crb,
            gh_checkbox_zzjsjb, gh_checkbox_zsxfjb, gh_checkbox_qt;


    private CheckBox smokeState_checkbox_cb, smokeState_checkbox_oer, smokeState_checkbox_jc,
            smokeState_checkbox_mt, smokeState_checkbox_yjy;

    private CheckBox drinkState_checkbox_cb, drinkState_checkbox_oer, drinkState_checkbox_jc,
            drinkState_checkbox_mt, drinkState_checkbox_yjj;

    private EditText edt_ywgms, edt_jwbs, edt_father, edt_mother, edi_sister, edt_children, edt_ycbs;

    private RadioGroup blood_type_group;
    private RadioButton A_type_Check, B_type_Check, AB_type_Check, O_type_Check;

    private Map<String, String> map = null;
    public static final int MEDICINEALLERGY_CODE = 1;
    public static final int PASTMEDICALHISTORY_CODE = 2;
    public static final int FAMILYMEDICALHISTORY_FATHER_CODE = 3;
    public static final int FAMILYMEDICALHISTORY_MOTHER_CODE = 4;
    public static final int FAMILYMEDICALHISTORY_SISTER_CODE = 5;
    public static final int FAMILYMEDICALHISTORY_CHILDREN_CODE = 6;
    public static final int GENETICHISTORY_CODE = 7;
    public static final int SMOKESTATE_CODE = 8;
    public static final int DRINKSTATE_CODE = 9;
    public static final int MARITALSTATUS = 10;
    public static final int BLOODTYPE = 11;
    private String maritalStatusNames = "";//婚姻状况
    private String medicineAllergyNames = "";//药物过敏
    private String pastMedicalHistoryNames = "";//既往病史
    private String familyMedicalhistory_fatherNames = "";//家族病史（父亲）
    private String familyMedicalhistory_motherNames = "";//家族病史（母亲）
    private String familyMedicalhistory_sisterNames = "";//家族病史（兄弟姐妹）
    private String familyMedicalhistory_childrenNames = "";//家族病史（子女）
    private String geneticHistoryNmaes = "";//遗传病史
    private String drinkStateNames = "";//喝酒状况
    private String smokeStateNames = "";//吸烟状况
    private String bloodtype = "";
    private String familyNames = "";

    private String maritalStatusNamesEdit = "";//婚姻状况
    private String medicineAllergyNamesEdit = "";//药物过敏
    private String pastMedicalHistoryNamesEdit = "";//既往病史
    private String familyMedicalhistory_fatherNamesEdit = "";//家族病史（父亲）
    private String familyMedicalhistory_motherNamesEdit = "";//家族病史（母亲）
    private String familyMedicalhistory_sisterNamesEdit = "";//家族病史（兄弟姐妹）
    private String familyMedicalhistory_childrenNamesEdit = "";//家族病史（子女）
    private String geneticHistoryNmaesEdit = "";//遗传病史
    private String drinkStateNamesEdit = "";//喝酒状况
    private String smokeStateNamesEdit = "";//吸烟状况
    private String bloodtypeEdit = "";
    private String familyNamesEdit = "";
    public static String userid;
    private RequestMaker requestMaker;
    public static String stype = null;
    public  HealthFiles healthfiles = null;
    private EHomeDao dao;
    private SupperBaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_healthfiles2, null);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        healthfiles=(HealthFiles)getArguments().getSerializable("healthfiles");
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker = RequestMaker.getInstance();
        dao=new EHomeDaoImpl(getActivity());
        initViews();
        initEvent();
        setInitData();
    }


    public void initViews() {
        A_type_Check = (RadioButton) mView.findViewById(R.id.A_type_Check);
        B_type_Check = (RadioButton) mView.findViewById(R.id.B_type_Check);
        AB_type_Check = (RadioButton) mView.findViewById(R.id.AB_type_Check);
        O_type_Check = (RadioButton) mView.findViewById(R.id.O_type_Check);
        blood_type_group = (RadioGroup) mView.findViewById(R.id.blood_type_group);

        btn_save = (Button) mView.findViewById(R.id.btn_save);
        //婚姻状况
        maritalStatus_group = (RadioGroup) mView.findViewById(R.id.maritalStatus_group);
        weiHunCheck = (RadioButton) mView.findViewById(R.id.weiHunCheck);
        jieHunCheck = (RadioButton) mView.findViewById(R.id.jieHunCheck);
        liHunCheck = (RadioButton) mView.findViewById(R.id.liHunCheck);
        sangOuCheck = (RadioButton) mView.findViewById(R.id.sangOuCheck);
        //药物过敏
        medicineAllergy_group = (RadioGroup) mView.findViewById(R.id.medicineAllergy_group);
        medicineAllergy_yesCheck = (RadioButton) mView.findViewById(R.id.medicineAllergy_yesCheck);
        medicineAllergy_noCheck = (RadioButton) mView.findViewById(R.id.medicineAllergy_noCheck);
        medicineAllergy_type = (TableLayout) mView.findViewById(R.id.medicineAllergy_type);
        medicineAllergy_checkbox_qingmeisu = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_qingmeisu);
        medicineAllergy_checkbox_huangan = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_huangan);
        medicineAllergy_checkbox_lianmeisu = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_lianmeisu);
        medicineAllergy_checkbox_qita = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_qita);
        //既往病史
        pastMedicalHistory_group = (RadioGroup) mView.findViewById(R.id.pastMedicalHistory_group);
        pastMedicalHistory_yesCheck = (RadioButton) mView.findViewById(R.id.pastMedicalHistory_yesCheck);
        pastMedicalHistory_noCheck = (RadioButton) mView.findViewById(R.id.pastMedicalHistory_noCheck);
        pastMedicalHistory_type = (TableLayout) mView.findViewById(R.id.pastMedicalHistory_type);
        pastMedicalHistory_checkbox_gaoxueya = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_gaoxueya);
        pastMedicalHistory_checkbox_tangniaobing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_tangniaobing);
        pastMedicalHistory_checkbox_guanxinbing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_guanxinbing);
        pastMedicalHistory_checkbox_naozuzhong = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_naozuzhong);
        pastMedicalHistory_checkbox_jiehebing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_jiehebing);
        pastMedicalHistory_checkbox_exingzhongliu = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_exingzhongliu);
        pastMedicalHistory_checkbox_ganyan = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_ganyan);
        pastMedicalHistory_checkbox_chuanranbing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_chuanranbing);
        pastMedicalHistory_checkbox_zzjb = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_zzjb);
        pastMedicalHistory_checkbox_mxfjb = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_mxfjb);
        pastMedicalHistory_checkbox_qita = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_qita);
        //家族病史(父亲)
        familyMedicalhistory_father_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_father_group);
        familyMedicalhistory_father_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_father_yesCheck);
        familyMedicalhistory_father_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_father_noCheck);
        familyMedicalhistory_father_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_father_type);
        fmh_father_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_gxy);
        fmh_father_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_tnb);
        fmh_father_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_gxb);
        fmh_father_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_gy);
        fmh_father_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_jhb);
        fmh_father_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_exzl);
        fmh_father_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_xtjx);
        fmh_father_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_zsxfjb);
        fmh_father_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_qt);
        //家族病史(母亲)
        familyMedicalhistory_mother_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_mother_group);
        familyMedicalhistory_mother_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_mother_yesCheck);
        familyMedicalhistory_mother_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_mother_noCheck);
        familyMedicalhistory_mother_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_mother_type);
        fmh_mother_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_gxy);
        fmh_mother_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_tnb);
        fmh_mother_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_gxb);
        fmh_mother_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_gy);
        fmh_mother_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_jhb);
        fmh_mother_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_exzl);
        fmh_mother_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_xtjx);
        fmh_mother_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_zsxfjb);
        fmh_mother_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_qt);
        //家族病史(兄弟姐妹)
        familyMedicalhistory_sister_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_sister_group);
        familyMedicalhistory_sister_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_sister_yesCheck);
        familyMedicalhistory_sister_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_sister_noCheck);
        familyMedicalhistory_sister_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_sister_type);
        fmh_sister_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_gxy);
        fmh_sister_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_tnb);
        fmh_sister_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_gxb);
        fmh_sister_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_gy);
        fmh_sister_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_jhb);
        fmh_sister_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_exzl);
        fmh_sister_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_xtjx);
        fmh_sister_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_zsxfjb);
        fmh_sister_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_qt);
        //家族病史(子女)
        familyMedicalhistory_children_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_children_group);
        familyMedicalhistory_children_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_children_yesCheck);
        familyMedicalhistory_children_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_children_noCheck);
        familyMedicalhistory_children_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_children_type);
        fmh_children_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_gxy);
        fmh_children_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_tnb);
        fmh_children_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_gxb);
        fmh_children_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_gy);
        fmh_children_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_jhb);
        fmh_children_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_exzl);
        fmh_children_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_xtjx);
        fmh_children_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_zsxfjb);
        fmh_children_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_qt);
        //遗传病史
        geneticHistory_group = (RadioGroup) mView.findViewById(R.id.geneticHistory_group);
        geneticHistory_yesCheck = (RadioButton) mView.findViewById(R.id.geneticHistory_yesCheck);
        geneticHistory_noCheck = (RadioButton) mView.findViewById(R.id.geneticHistory_noCheck);
        geneticHistory_type = (TableLayout) mView.findViewById(R.id.geneticHistory_type);
        gh_checkbox_gxy = (CheckBox) mView.findViewById(R.id.gh_checkbox_gxy);
        gh_checkbox_tnb = (CheckBox) mView.findViewById(R.id.gh_checkbox_tnb);
        gh_checkbox_gxb = (CheckBox) mView.findViewById(R.id.gh_checkbox_gxb);
        gh_checkbox_nzu = (CheckBox) mView.findViewById(R.id.gh_checkbox_nzu);
        gh_checkbox_jhb = (CheckBox) mView.findViewById(R.id.gh_checkbox_jhb);
        gh_checkbox_exzl = (CheckBox) mView.findViewById(R.id.gh_checkbox_exzl);
        gh_checkbox_gy = (CheckBox) mView.findViewById(R.id.gh_checkbox_gy);
        gh_checkbox_crb = (CheckBox) mView.findViewById(R.id.gh_checkbox_crb);
        gh_checkbox_zzjsjb = (CheckBox) mView.findViewById(R.id.gh_checkbox_zzjsjb);
        gh_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.gh_checkbox_zsxfjb);
        gh_checkbox_qt = (CheckBox) mView.findViewById(R.id.gh_checkbox_qt);
        //吸烟状况
        smokeState_group = (RadioGroup) mView.findViewById(R.id.smokeState_group);
        smokeState_yesCheck = (RadioButton) mView.findViewById(R.id.smokeState_yesCheck);
        smokeState_noCheck = (RadioButton) mView.findViewById(R.id.smokeState_noCheck);
        smokeState_type = (LinearLayout) mView.findViewById(R.id.smokeState_type);
        smokeState_checkbox_cb = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_cb);
        smokeState_checkbox_oer = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_oer);
        smokeState_checkbox_jc = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_jc);
        smokeState_checkbox_mt = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_mt);
        smokeState_checkbox_yjy = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_yjy);

        //喝酒状况
        drink_group = (RadioGroup) mView.findViewById(R.id.drinkState_group);
        drinkState_yesCheck = (RadioButton) mView.findViewById(R.id.drinkState_yesCheck);
        drinkState_noCheck = (RadioButton) mView.findViewById(R.id.drinkState_noCheck);
        drinkState_type = (LinearLayout) mView.findViewById(R.id.drinkState_type);
        drinkState_checkbox_cb = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_cb);
        drinkState_checkbox_oer = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_oer);
        drinkState_checkbox_jc = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_jc);
        drinkState_checkbox_mt = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_mt);
        drinkState_checkbox_yjj = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_yjj);

        edt_ywgms = (EditText) mView.findViewById(R.id.edt_ywgms);
        edt_jwbs = (EditText) mView.findViewById(R.id.edt_jwbs);
        edt_father = (EditText) mView.findViewById(R.id.edt_father);
        edt_mother = (EditText) mView.findViewById(R.id.edt_mother);
        edi_sister = (EditText) mView.findViewById(R.id.edi_sister);
        edt_children = (EditText) mView.findViewById(R.id.edt_children);
        edt_ycbs = (EditText) mView.findViewById(R.id.edt_ycbs);

    }

    public void setInitData() {
        map = new HashMap<String, String>();
        if(healthfiles==null){
            return;
        }
        familyNames = healthfiles.familyNames;
        if (!TextUtils.isEmpty(familyNames) && familyNames.contains(",")) {
            String nametypes[] = familyNames.split(",");
            for (int i = 0; i < nametypes.length; i++) {
                if (nametypes[i].contains(":")) {
                    String names[] = nametypes[i].split(":");
                    if ("父亲".equals(names[0])) {
                        familyMedicalhistory_fatherNames = familyMedicalhistory_fatherNames + nametypes[i] + ",";
                    } else if ("母亲".equals(names[0])) {
                        familyMedicalhistory_motherNames = familyMedicalhistory_motherNames + nametypes[i] + ",";

                    } else if ("兄弟姐妹".equals(names[0])) {
                        familyMedicalhistory_sisterNames = familyMedicalhistory_sisterNames + nametypes[i] + ",";

                    } else if ("子女".equals(names[0])) {
                        familyMedicalhistory_childrenNames = familyMedicalhistory_childrenNames + nametypes[i] + ",";
                    }
                }
            }
        }

        maritalStatusNames = healthfiles.maritalStatusNames;
        medicineAllergyNames = healthfiles.medicineAllergyNames;
        pastMedicalHistoryNames = healthfiles.pastMedicalHistoryNames;
        geneticHistoryNmaes = healthfiles.geneticHistoryNmaes;
        drinkStateNames = healthfiles.drinkStateNames;
        smokeStateNames = healthfiles.smokeStateNames;
        bloodtype = healthfiles.bloodtype;
        if(TextUtils.isEmpty(maritalStatusNames)){
            map.put(MARITALSTATUS + ":", weiHunCheck.getText().toString());
        }
        if(TextUtils.isEmpty(bloodtype)){
            map.put(BLOODTYPE + ":", A_type_Check.getText().toString());
        }
        setInitMaritalStatus();
        setInitMedicineAllergy();
        setInitPastMedicalHistory();
        setInitGeneticHistory();
        setInitBloodType();
        setInitDrinkState();
        setInitSmokeState();
        setFathers();
        setMothers();
        setSister();
        setChildren();

    }

    public void setInitMaritalStatus() {
        if (!TextUtils.isEmpty(maritalStatusNames)) {
            if ("未婚".equals(maritalStatusNames)) {
                weiHunCheck.setChecked(true);
                maritalStatusNamesEdit="未婚";
            } else if ("结婚".equals(maritalStatusNames)) {
                maritalStatusNamesEdit="结婚";
                //map.put(MARITALSTATUS+":"+"结婚","结婚");
                jieHunCheck.setChecked(true);
            } else if ("离婚".equals(maritalStatusNames)) {
                maritalStatusNamesEdit="离婚";
               // map.put(MARITALSTATUS+":"+"离婚","离婚");
                liHunCheck.setChecked(true);
            } else if ("丧偶".equals(maritalStatusNames)) {
                maritalStatusNamesEdit="丧偶";
              //  map.put(MARITALSTATUS+":"+"丧偶","丧偶");
                sangOuCheck.setChecked(true);
            }
        }
    }

    public void setInitBloodType(){
        if (!TextUtils.isEmpty(bloodtype)) {
            if ("A型".equals(bloodtype)) {
                bloodtypeEdit="A型";
                //map.put(BLOODTYPE+":"+"A型","A型");
                A_type_Check.setChecked(true);
            } else if ("B型".equals(bloodtype)) {
                bloodtypeEdit="B型";
               // map.put(BLOODTYPE+":"+"B型","B型");
                B_type_Check.setChecked(true);
            } else if ("AB型".equals(bloodtype)) {
                bloodtypeEdit="AB型";
                //map.put(BLOODTYPE+":"+"AB型","AB型");
                AB_type_Check.setChecked(true);
            } else if ("O型".equals(bloodtype)) {
                bloodtypeEdit="A型";
               // map.put(BLOODTYPE+":"+"O型","O型");
                O_type_Check.setChecked(true);
            }
        }
    }

    public void setInitDrinkState(){
        if(!TextUtils.isEmpty(drinkStateNames)&&!"无".equals(drinkStateNames)){
            drinkState_yesCheck.setChecked(true);
            drinkState_type.setVisibility(View.VISIBLE);
            if ("从不".equals(drinkStateNames)) {
                map.put(DRINKSTATE_CODE+":"+"从不","从不");
                drinkState_checkbox_cb.setChecked(true);
            } else if ("偶尔".equals(drinkStateNames)) {
                map.put(DRINKSTATE_CODE+":"+"偶尔","偶尔");
                drinkState_checkbox_oer.setChecked(true);
            } else if ("经常".equals(drinkStateNames)) {
                map.put(DRINKSTATE_CODE+":"+"经常","经常");
                drinkState_checkbox_jc.setChecked(true);
            } else if ("每天".equals(drinkStateNames)) {
                map.put(DRINKSTATE_CODE+":"+"每天","每天");
                drinkState_checkbox_mt.setChecked(true);
            }else if ("已戒酒".equals(drinkStateNames)) {
                map.put(DRINKSTATE_CODE+":"+"已戒酒","已戒酒");
                drinkState_checkbox_yjj.setChecked(true);
            }
        }
    }

    public void  setInitSmokeState(){
        if(!TextUtils.isEmpty(smokeStateNames)&&!"无".equals(smokeStateNames)){
            smokeState_yesCheck.setChecked(true);
            smokeState_type.setVisibility(View.VISIBLE);
            if ("从不".equals(smokeStateNames)) {
                map.put(SMOKESTATE_CODE+":"+"从不","从不");
                smokeState_checkbox_cb.setChecked(true);
            } else if ("偶尔".equals(smokeStateNames)) {
                map.put(SMOKESTATE_CODE+":"+"偶尔","偶尔");
                smokeState_checkbox_oer.setChecked(true);
            } else if ("经常".equals(smokeStateNames)) {
                map.put(SMOKESTATE_CODE+":"+"经常","经常");
                smokeState_checkbox_jc.setChecked(true);
            } else if ("每天".equals(smokeStateNames)) {
                map.put(SMOKESTATE_CODE+":"+"每天","每天");
                smokeState_checkbox_mt.setChecked(true);
            }else if ("已戒烟".equals(smokeStateNames)) {
                map.put(SMOKESTATE_CODE+":"+"已戒烟","已戒烟");
                smokeState_checkbox_yjy.setChecked(true);
            }
        }
    }

    public void setFathers(){
        if(!TextUtils.isEmpty(familyMedicalhistory_fatherNames)  ){
            familyMedicalhistory_father_yesCheck.setChecked(true);
            familyMedicalhistory_father_type.setVisibility(View.VISIBLE);
            String names[] = familyMedicalhistory_fatherNames.split(",");
            for (int i = 0; i < names.length; i++) {
                String vals[]=names[i].split(":");
                if (!TextUtils.isEmpty(vals[1]) && !" ".equals(vals[1]) && "父亲".equals(vals[0])) {
                    if(vals[1].contains("-")){
                        String others[]=vals[1].split("-");
                        if(others.length>=2){
                        if(!TextUtils.isEmpty(others[1]) && others[0].equals("其他")){
                            if(!others[1].equals("无")) {
                                edt_father.setVisibility(View.VISIBLE);
                                edt_father.setText(others[1]);
                                fmh_father_checkbox_qt.setChecked(true);
                            }
                        }
                        }
                    }else {
                        if("高血压".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"高血压","高血压");
                            fmh_father_checkbox_gxy.setChecked(true);
                        }else if("糖尿病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"糖尿病","糖尿病");
                            fmh_father_checkbox_tnb.setChecked(true);
                        }else if("冠心病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"冠心病","冠心病");
                            fmh_father_checkbox_gxb.setChecked(true);
                        }else if("结核病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"结核病","结核病");
                            fmh_father_checkbox_jhb.setChecked(true);
                        }else if("恶性肿瘤".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"恶性肿瘤","恶性肿瘤");
                            fmh_father_checkbox_exzl.setChecked(true);
                        }else if("肝炎".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"肝炎","肝炎");
                            fmh_father_checkbox_gy.setChecked(true);
                        }else if("先天畸形".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"先天畸形","先天畸形");
                            fmh_father_checkbox_xtjx.setChecked(true);
                        }else if("先天性阻塞性肺疾病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_FATHER_CODE+":"+"先天性阻塞性肺疾病","先天性阻塞性肺疾病");
                            fmh_father_checkbox_zsxfjb.setChecked(true);
                        }
                    }
                }
            }
        }

    }

    public void setMothers(){

        if(!TextUtils.isEmpty(familyMedicalhistory_motherNames)){
            familyMedicalhistory_mother_yesCheck.setChecked(true);
            familyMedicalhistory_mother_type.setVisibility(View.VISIBLE);
            String names[] = familyMedicalhistory_motherNames.split(",");
            for (int i = 0; i < names.length; i++) {
                String vals[]=names[i].split(":");
                if (!TextUtils.isEmpty(vals[1]) && !" ".equals(vals[1]) && "母亲".equals(vals[0])) {
                    if(vals[1].contains("-")){
                        String others[]=vals[1].split("-");
                        if(others.length>=2) {
                            if (!TextUtils.isEmpty(others[1]) && others[0].equals("其他")) {
                                if(!others[1].equals("无")) {
                                    edt_mother.setVisibility(View.VISIBLE);
                                    edt_mother.setText(others[1]);
                                    fmh_mother_checkbox_qt.setChecked(true);
                                }
                            }
                        }
                    }else {
                        if("高血压".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"高血压","高血压");
                            fmh_mother_checkbox_gxy.setChecked(true);
                        }else if("糖尿病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"糖尿病","糖尿病");
                            fmh_mother_checkbox_tnb.setChecked(true);
                        }else if("冠心病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"冠心病","冠心病");
                            fmh_mother_checkbox_gxb.setChecked(true);
                        }else if("结核病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"结核病","结核病");
                            fmh_mother_checkbox_jhb.setChecked(true);
                        }else if("恶性肿瘤".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"恶性肿瘤","恶性肿瘤");
                            fmh_mother_checkbox_exzl.setChecked(true);
                        }else if("肝炎".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"肝炎","肝炎");
                            fmh_mother_checkbox_gy.setChecked(true);
                        }else if("先天畸形".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"先天畸形","先天畸形");
                            fmh_mother_checkbox_xtjx.setChecked(true);
                        }else if("先天性阻塞性肺疾病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_MOTHER_CODE+":"+"先天性阻塞性肺疾病","先天性阻塞性肺疾病");
                            fmh_mother_checkbox_zsxfjb.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    public void setSister(){
        if(!TextUtils.isEmpty(familyMedicalhistory_sisterNames) ){
            familyMedicalhistory_sister_yesCheck.setChecked(true);
            familyMedicalhistory_sister_type.setVisibility(View.VISIBLE);
            String names[] = familyMedicalhistory_sisterNames.split(",");
            for (int i = 0; i < names.length; i++) {
                String vals[]=names[i].split(":");
                if (!TextUtils.isEmpty(vals[1]) && !" ".equals(vals[1]) && "兄弟姐妹".equals(vals[0])) {
                    if(vals[1].contains("-")){
                        String others[]=vals[1].split("-");
                        if(others.length>=2) {
                            if (!TextUtils.isEmpty(others[1]) && others[0].equals("其他")) {
                                if(!others[1].equals("无")) {
                                    edi_sister.setVisibility(View.VISIBLE);
                                    edi_sister.setText(others[1]);
                                    fmh_sister_checkbox_qt.setChecked(true);
                                }
                            }
                        }
                    }else {
                        if("高血压".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"高血压","高血压");
                            fmh_sister_checkbox_gxy.setChecked(true);
                        }else if("糖尿病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"糖尿病","糖尿病");
                            fmh_sister_checkbox_tnb.setChecked(true);
                        }else if("冠心病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"冠心病","冠心病");
                            fmh_sister_checkbox_gxb.setChecked(true);
                        }else if("结核病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"结核病","结核病");
                            fmh_sister_checkbox_jhb.setChecked(true);
                        }else if("恶性肿瘤".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"恶性肿瘤","恶性肿瘤");
                            fmh_sister_checkbox_exzl.setChecked(true);
                        }else if("肝炎".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"肝炎","肝炎");
                            fmh_sister_checkbox_gy.setChecked(true);
                        }else if("先天畸形".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"先天畸形","先天畸形");
                            fmh_sister_checkbox_xtjx.setChecked(true);
                        }else if("先天性阻塞性肺疾病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_SISTER_CODE+":"+"先天性阻塞性肺疾病","先天性阻塞性肺疾病");
                            fmh_sister_checkbox_zsxfjb.setChecked(true);
                        }
                    }
                }
            }
        }
    }
    public  void setChildren(){
        if(!TextUtils.isEmpty(familyMedicalhistory_childrenNames)){
            familyMedicalhistory_children_yesCheck.setChecked(true);
            familyMedicalhistory_children_type.setVisibility(View.VISIBLE);
            String names[] = familyMedicalhistory_childrenNames.split(",");
            for (int i = 0; i < names.length; i++) {
                String vals[]=names[i].split(":");
                if (!TextUtils.isEmpty(vals[1]) && !" ".equals(vals[1]) && "子女".equals(vals[0])) {
                    if(vals[1].contains("-")){
                        String others[]=vals[1].split("-");
                        if(others.length>=2) {
                            if (!TextUtils.isEmpty(others[1]) && others[0].equals("其他")) {
                                if(!others[1].equals("无")) {

                                    edt_children.setVisibility(View.VISIBLE);
                                    edt_children.setText(others[1]);
                                    fmh_children_checkbox_qt.setChecked(true);
                                }
                            }
                        }
                    }else {
                        if("高血压".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"高血压","高血压");
                            fmh_children_checkbox_gxy.setChecked(true);
                        }else if("糖尿病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"糖尿病","糖尿病");
                            fmh_children_checkbox_tnb.setChecked(true);
                        }else if("冠心病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"冠心病","冠心病");
                            fmh_children_checkbox_gxb.setChecked(true);
                        }else if("结核病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"结核病","结核病");
                            fmh_children_checkbox_jhb.setChecked(true);
                        }else if("恶性肿瘤".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"恶性肿瘤","恶性肿瘤");
                            fmh_children_checkbox_exzl.setChecked(true);
                        }else if("肝炎".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"肝炎","肝炎");
                            fmh_children_checkbox_gy.setChecked(true);
                        }else if("先天畸形".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"先天畸形","先天畸形");
                            fmh_children_checkbox_xtjx.setChecked(true);
                        }else if("先天性阻塞性肺疾病".equals(vals[1])){
                            map.put(FAMILYMEDICALHISTORY_CHILDREN_CODE+":"+"先天性阻塞性肺疾病","先天性阻塞性肺疾病");
                            fmh_children_checkbox_zsxfjb.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    public void setInitMedicineAllergy(){
        if(!TextUtils.isEmpty(medicineAllergyNames) &&!"其他-无".equals(medicineAllergyNames)){
            medicineAllergy_yesCheck.setChecked(true);
            medicineAllergy_type.setVisibility(View.VISIBLE);
            String names[] = medicineAllergyNames.split(" ");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    if(names[i].contains("-")){
                        String others[]=names[i].split("-");
                        if(others.length>=2) {
                            if (!TextUtils.isEmpty(others[1]) && others[0].equals("其他")) {
                                if(!others[1].equals("无")){
                                    edt_ywgms.setVisibility(View.VISIBLE);
                                    edt_ywgms.setText(others[1]);
                                    medicineAllergy_checkbox_qita.setChecked(true);
                                }

                            }
                        }
                    }else {
                        if("青霉素".equals(names[i])){
                            map.put(MEDICINEALLERGY_CODE+":"+"青霉素","青霉素");
                            medicineAllergy_checkbox_qingmeisu.setChecked(true);
                        }else if("磺胺".equals(names[i])){
                            map.put(MEDICINEALLERGY_CODE+":"+"磺胺","磺胺");
                            medicineAllergy_checkbox_huangan.setChecked(true);
                        }else if("链霉素".equals(names[i])){
                            map.put(MEDICINEALLERGY_CODE+":"+"链霉素","链霉素");
                            medicineAllergy_checkbox_lianmeisu.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    public void setInitPastMedicalHistory(){
        if(!TextUtils.isEmpty(pastMedicalHistoryNames)&&!"其他-无".equals(pastMedicalHistoryNames) ){
            pastMedicalHistory_yesCheck.setChecked(true);
            pastMedicalHistory_type.setVisibility(View.VISIBLE);
            String names[] = pastMedicalHistoryNames.split(" ");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    if(names[i].contains("-")) {
                        String others[] = names[i].split("-");
                        if(others.length>=2) {
                            if (!TextUtils.isEmpty(others[1]) && others[0].equals("其他")) {
                                if(!others[1].equals("无")) {
                                    edt_jwbs.setVisibility(View.VISIBLE);
                                    edt_jwbs.setText(others[1]);
                                    pastMedicalHistory_checkbox_qita.setChecked(true);
                                }
                            }
                        }
                    }else{
                        if("高血压".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"高血压","高血压");
                            pastMedicalHistory_checkbox_gaoxueya.setChecked(true);
                        }else if("糖尿病".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"糖尿病","糖尿病");
                            pastMedicalHistory_checkbox_tangniaobing.setChecked(true);
                        }else if("冠心病".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"冠心病","冠心病");
                            pastMedicalHistory_checkbox_guanxinbing.setChecked(true);
                        }else if("脑卒中".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"脑卒中","脑卒中");
                            pastMedicalHistory_checkbox_naozuzhong.setChecked(true);
                        }else if("结核病".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"结核病","结核病");
                            pastMedicalHistory_checkbox_jiehebing.setChecked(true);
                        }else if("恶性肿瘤".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"恶性肿瘤","恶性肿瘤");
                            pastMedicalHistory_checkbox_exingzhongliu.setChecked(true);
                        }else if("肝炎".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"肝炎","肝炎");
                            pastMedicalHistory_checkbox_ganyan.setChecked(true);
                        }else if("传染病".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"传染病","传染病");
                            pastMedicalHistory_checkbox_chuanranbing.setChecked(true);
                        }else if("重症精神疾病".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"重症精神疾病","重症精神疾病");
                            pastMedicalHistory_checkbox_zzjb.setChecked(true);
                        }else if("慢性阻塞性肺疾病".equals(names[i])){
                            map.put(PASTMEDICALHISTORY_CODE+":"+"慢性阻塞性肺疾病","慢性阻塞性肺疾病");
                            pastMedicalHistory_checkbox_mxfjb.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    public void setInitGeneticHistory(){
        if(!TextUtils.isEmpty(geneticHistoryNmaes) &&!"其他-无".equals(geneticHistoryNmaes) ){
            geneticHistory_yesCheck.setChecked(true);
            geneticHistory_type.setVisibility(View.VISIBLE);
            String names[] = geneticHistoryNmaes.split(" ");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    if(names[i].contains("-")) {
                        String others[] = names[i].split("-");
                        if(others.length>=2) {
                            if (!TextUtils.isEmpty(others[1]) && others[0].equals("其他")) {
                                if(!others[1].equals("无")) {
                                    edt_ycbs.setVisibility(View.VISIBLE);
                                    edt_ycbs.setText(others[1]);
                                    gh_checkbox_qt.setChecked(true);
                                }
                            }
                        }
                    }else{
                        if("高血压".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"高血压","高血压");
                            gh_checkbox_gxy.setChecked(true);
                        }else if("糖尿病".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"糖尿病","糖尿病");
                            gh_checkbox_tnb.setChecked(true);
                        }else if("冠心病".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"冠心病","冠心病");
                            gh_checkbox_gxb.setChecked(true);
                        }else if("脑卒中".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"脑卒中","脑卒中");
                            gh_checkbox_nzu.setChecked(true);
                        }else if("结核病".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"结核病","结核病");
                            gh_checkbox_jhb.setChecked(true);
                        }else if("恶性肿瘤".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"恶性肿瘤","恶性肿瘤");
                            gh_checkbox_exzl.setChecked(true);
                        }else if("肝炎".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"肝炎","肝炎");
                            gh_checkbox_gy.setChecked(true);
                        }else if("传染病".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"传染病","传染病");
                            gh_checkbox_crb.setChecked(true);
                        }else if("重症精神疾病".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"重症精神疾病","重症精神疾病");
                            gh_checkbox_zzjsjb.setChecked(true);
                        }else if("慢性阻塞性肺疾病".equals(names[i])){
                            map.put(GENETICHISTORY_CODE+":"+"慢性阻塞性肺疾病","慢性阻塞性肺疾病");
                            gh_checkbox_zsxfjb.setChecked(true);
                        }
                    }
                }
            }
        }
    }
    public void initEvent() {

        maritalStatus_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == weiHunCheck.getId()) {
                    maritalStatusNamesEdit=weiHunCheck.getText().toString();
                   // map.put(MARITALSTATUS + ":"+weiHunCheck.getText().toString(), weiHunCheck.getText().toString());
                } else if (checkedId == jieHunCheck.getId()) {
                    maritalStatusNamesEdit=jieHunCheck.getText().toString();
                   // map.put(MARITALSTATUS + ":"+jieHunCheck.getText().toString(), jieHunCheck.getText().toString());
                } else if (checkedId == liHunCheck.getId()) {
                    maritalStatusNamesEdit=liHunCheck.getText().toString();
                   // map.put(MARITALSTATUS + ":"+liHunCheck.getText().toString(), liHunCheck.getText().toString());
                } else if (checkedId == sangOuCheck.getId()) {
                    maritalStatusNamesEdit=sangOuCheck.getText().toString();
                   // map.put(MARITALSTATUS + ":"+sangOuCheck.getText().toString(), sangOuCheck.getText().toString());
                }
            }
        });
        blood_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == A_type_Check.getId()) {
                    bloodtypeEdit=A_type_Check.getText().toString();
                   // map.put(BLOODTYPE + ":"+A_type_Check.getText().toString(), A_type_Check.getText().toString());
                } else if (checkedId == B_type_Check.getId()) {
                    bloodtypeEdit=B_type_Check.getText().toString();
                    //map.put(BLOODTYPE + ":"+B_type_Check.getText().toString(), B_type_Check.getText().toString());
                } else if (checkedId == AB_type_Check.getId()) {
                    bloodtypeEdit=AB_type_Check.getText().toString();
                    //map.put(BLOODTYPE + ":"+AB_type_Check.getText().toString(), AB_type_Check.getText().toString());
                } else if (checkedId == O_type_Check.getId()) {
                    bloodtypeEdit=O_type_Check.getText().toString();
                    //map.put(BLOODTYPE + ":"+O_type_Check.getText().toString(), O_type_Check.getText().toString());
                }
            }
        });

        medicineAllergy_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == medicineAllergy_yesCheck.getId()) {
                    medicineAllergy_type.setVisibility(View.VISIBLE);
                } else if (checkedId == medicineAllergy_noCheck.getId()) {
                    medicineAllergy_type.setVisibility(View.GONE);
                    medicineAllergy_checkbox_qingmeisu .setChecked(false);
                    medicineAllergy_checkbox_huangan .setChecked(false);
                    medicineAllergy_checkbox_lianmeisu.setChecked(false);
                    medicineAllergy_checkbox_qita.setChecked(false);
                    edt_ywgms.setText("");
                    edt_ywgms.setVisibility(View.GONE);
                }
            }
        });
        pastMedicalHistory_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == pastMedicalHistory_yesCheck.getId()) {
                    pastMedicalHistory_type.setVisibility(View.VISIBLE);
                } else if (checkedId == pastMedicalHistory_noCheck.getId()) {
                    pastMedicalHistory_type.setVisibility(View.GONE);
                    pastMedicalHistory_checkbox_gaoxueya.setChecked(false);
                    pastMedicalHistory_checkbox_tangniaobing.setChecked(false);
                    pastMedicalHistory_checkbox_guanxinbing.setChecked(false);
                    pastMedicalHistory_checkbox_naozuzhong .setChecked(false);
                    pastMedicalHistory_checkbox_jiehebing .setChecked(false);
                    pastMedicalHistory_checkbox_exingzhongliu .setChecked(false);
                    pastMedicalHistory_checkbox_ganyan .setChecked(false);
                    pastMedicalHistory_checkbox_chuanranbing .setChecked(false);
                    pastMedicalHistory_checkbox_zzjb.setChecked(false);
                    pastMedicalHistory_checkbox_mxfjb.setChecked(false);
                    pastMedicalHistory_checkbox_qita.setChecked(false);
                    edt_jwbs.setText("");
                    edt_jwbs.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_father_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_father_yesCheck.getId()) {
                    familyMedicalhistory_father_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_father_noCheck.getId()) {
                    familyMedicalhistory_father_type.setVisibility(View.GONE);
                    fmh_father_checkbox_gxy.setChecked(false);
                    fmh_father_checkbox_tnb .setChecked(false);
                    fmh_father_checkbox_gxb.setChecked(false);
                    fmh_father_checkbox_gy .setChecked(false);
                    fmh_father_checkbox_jhb .setChecked(false);
                    fmh_father_checkbox_exzl.setChecked(false);
                    fmh_father_checkbox_xtjx.setChecked(false);
                    fmh_father_checkbox_zsxfjb.setChecked(false);
                    fmh_father_checkbox_qt.setChecked(false);
                    edt_father.setText("");
                    edt_father.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_mother_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_mother_yesCheck.getId()) {
                    familyMedicalhistory_mother_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_mother_noCheck.getId()) {
                    familyMedicalhistory_mother_type.setVisibility(View.GONE);
                    fmh_mother_checkbox_gxy.setChecked(false);
                    fmh_mother_checkbox_tnb .setChecked(false);
                    fmh_mother_checkbox_gxb.setChecked(false);
                    fmh_mother_checkbox_gy .setChecked(false);
                    fmh_mother_checkbox_jhb .setChecked(false);
                    fmh_mother_checkbox_exzl.setChecked(false);
                    fmh_mother_checkbox_xtjx.setChecked(false);
                    fmh_mother_checkbox_zsxfjb.setChecked(false);
                    fmh_mother_checkbox_qt.setChecked(false);
                    edt_mother.setText("");
                    edt_mother.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_sister_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_sister_yesCheck.getId()) {
                    familyMedicalhistory_sister_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_sister_noCheck.getId()) {
                    familyMedicalhistory_sister_type.setVisibility(View.GONE);
                    fmh_sister_checkbox_gxy.setChecked(false);
                    fmh_sister_checkbox_tnb .setChecked(false);
                    fmh_sister_checkbox_gxb.setChecked(false);
                    fmh_sister_checkbox_gy .setChecked(false);
                    fmh_sister_checkbox_jhb .setChecked(false);
                    fmh_sister_checkbox_exzl.setChecked(false);
                    fmh_sister_checkbox_xtjx.setChecked(false);
                    fmh_sister_checkbox_zsxfjb.setChecked(false);
                    fmh_sister_checkbox_qt.setChecked(false);
                    edi_sister.setText("");
                    edi_sister.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_children_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_children_yesCheck.getId()) {
                    familyMedicalhistory_children_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_children_noCheck.getId()) {
                    familyMedicalhistory_children_type.setVisibility(View.GONE);
                    fmh_children_checkbox_gxy.setChecked(false);
                    fmh_children_checkbox_tnb .setChecked(false);
                    fmh_children_checkbox_gxb.setChecked(false);
                    fmh_children_checkbox_gy .setChecked(false);
                    fmh_children_checkbox_jhb .setChecked(false);
                    fmh_children_checkbox_exzl.setChecked(false);
                    fmh_children_checkbox_xtjx.setChecked(false);
                    fmh_children_checkbox_zsxfjb.setChecked(false);
                    fmh_children_checkbox_qt.setChecked(false);
                    edt_children.setText("");
                    edt_children.setVisibility(View.GONE);
                }
            }
        });
        geneticHistory_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == geneticHistory_yesCheck.getId()) {
                    geneticHistory_type.setVisibility(View.VISIBLE);
                } else if (checkedId == geneticHistory_noCheck.getId()) {
                    geneticHistory_type.setVisibility(View.GONE);
                    gh_checkbox_gxy.setChecked(false);
                    gh_checkbox_tnb .setChecked(false);
                    gh_checkbox_gxb .setChecked(false);
                    gh_checkbox_nzu .setChecked(false);
                    gh_checkbox_jhb .setChecked(false);
                    gh_checkbox_exzl.setChecked(false);
                    gh_checkbox_gy .setChecked(false);
                    gh_checkbox_crb .setChecked(false);
                    gh_checkbox_zzjsjb .setChecked(false);
                    gh_checkbox_zsxfjb .setChecked(false);
                    gh_checkbox_qt .setChecked(false);
                    edt_ycbs.setText("");
                    edt_ycbs.setVisibility(View.GONE);
                }
            }
        });
        smokeState_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == smokeState_yesCheck.getId()) {
                    smokeState_type.setVisibility(View.VISIBLE);
                } else if (checkedId == smokeState_noCheck.getId()) {
                    smokeState_type.setVisibility(View.GONE);
                    smokeState_checkbox_cb .setChecked(false);
                    smokeState_checkbox_oer .setChecked(false);
                    smokeState_checkbox_jc .setChecked(false);
                    smokeState_checkbox_mt .setChecked(false);
                    smokeState_checkbox_yjy .setChecked(false);
                }
            }
        });

        drink_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == drinkState_yesCheck.getId()) {
                    drinkState_type.setVisibility(View.VISIBLE);
                } else if (checkedId == drinkState_noCheck.getId()) {
                    drinkState_type.setVisibility(View.GONE);
                    drinkState_checkbox_cb.setChecked(false);
                    drinkState_checkbox_oer.setChecked(false);
                    drinkState_checkbox_jc .setChecked(false);
                    drinkState_checkbox_mt .setChecked(false);
                    drinkState_checkbox_yjj .setChecked(false);
                }
            }
        });

        medicineAllergy_checkbox_qingmeisu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));
        medicineAllergy_checkbox_huangan.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));
        medicineAllergy_checkbox_lianmeisu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));
        medicineAllergy_checkbox_qita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_ywgms.setVisibility(View.VISIBLE);
                } else {
                    edt_ywgms.setVisibility(View.GONE);
                }
            }
        });

        pastMedicalHistory_checkbox_gaoxueya.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_tangniaobing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_guanxinbing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_naozuzhong.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_jiehebing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_exingzhongliu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_ganyan.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_chuanranbing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_zzjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_mxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));

        pastMedicalHistory_checkbox_qita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_jwbs.setVisibility(View.VISIBLE);
                } else {
                    edt_jwbs.setVisibility(View.GONE);
                }

            }
        });

        fmh_father_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_qt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_father.setVisibility(View.VISIBLE);
                } else {
                    edt_father.setVisibility(View.GONE);
                }
            }
        });

        fmh_mother_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_qt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_mother.setVisibility(View.VISIBLE);
                } else {
                    edt_mother.setVisibility(View.GONE);
                }
            }
        });

        fmh_sister_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_qt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edi_sister.setVisibility(View.VISIBLE);
                } else {
                    edi_sister.setVisibility(View.GONE);
                }
            }
        });

        fmh_children_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_qt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_children.setVisibility(View.VISIBLE);
                } else {
                    edt_children.setVisibility(View.GONE);
                }
            }
        });

        gh_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_nzu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_crb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_zzjsjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_qt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_ycbs.setVisibility(View.VISIBLE);
                } else {
                    edt_ycbs.setVisibility(View.GONE);
                }
            }
        });
        smokeState_checkbox_cb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_oer.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_jc.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_mt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_yjy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));

        drinkState_checkbox_cb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_oer.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_jc.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_mt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_yjj.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;

                }
/*                maritalStatusNames = "";//婚姻状况
                medicineAllergyNames = "";//药物过敏
                pastMedicalHistoryNames = "";//既往病史
                familyMedicalhistory_fatherNames = "";//家族病史（父亲）
                familyMedicalhistory_motherNames = "";//家族病史（母亲）
                familyMedicalhistory_sisterNames = "";//家族病史（兄弟姐妹）
                familyMedicalhistory_childrenNames = "";//家族病史（子女）
                geneticHistoryNmaes = "";//遗传病史
                drinkStateNames = "";//喝酒状况
                smokeStateNames = "";//吸烟状况
                bloodtype = "";*/
                StringBuffer sbf=new StringBuffer();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    sbf.append(entry.getKey()+"===="+entry.getValue());
                    if (!TextUtils.isEmpty(key)) {
                        setDatas(key);
                    }
                }
                Log.d("TAG", "onClick() called with: v = [" + sbf.toString() + "]");
                if (stype.equals("2")) {
                    add();
                } else if (stype.equals("3")) {
                    upload();
                } else if (stype.equals("0")) {
                    add();
                }
            }
        });
    }

    public void add() {


        if (edt_father.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edt_father))) {
            familyMedicalhistory_fatherNamesEdit = familyMedicalhistory_fatherNamesEdit + "父亲:" + getEdtValue(edt_father);
        }
        if (edt_mother.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edt_mother))) {
            familyMedicalhistory_motherNamesEdit = familyMedicalhistory_motherNamesEdit + "母亲:" + getEdtValue(edt_mother);
        }
        if (edi_sister.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edi_sister))) {
            familyMedicalhistory_sisterNamesEdit = familyMedicalhistory_sisterNamesEdit + "兄弟姐妹:" + getEdtValue(edi_sister);
        }
        if (edt_children.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edt_children))) {
            familyMedicalhistory_childrenNamesEdit = familyMedicalhistory_childrenNamesEdit + "子女:" + getEdtValue(edt_children);
        }




        if(familyMedicalhistory_father_noCheck.isChecked()){
            familyMedicalhistory_fatherNamesEdit="";
        }
        if(familyMedicalhistory_mother_noCheck.isChecked()){
            familyMedicalhistory_motherNamesEdit="";
        }
        if(familyMedicalhistory_sister_noCheck.isChecked()){
            familyMedicalhistory_sisterNamesEdit="";

        }
        if(familyMedicalhistory_children_noCheck.isChecked()){
            familyMedicalhistory_childrenNamesEdit="";
        }

        String familyNames = familyMedicalhistory_fatherNamesEdit + familyMedicalhistory_motherNamesEdit + familyMedicalhistory_sisterNamesEdit + familyMedicalhistory_childrenNamesEdit;
        if (TextUtils.isEmpty(familyNames)) {
            familyNames = " " + ":" + " " + "," + " " + ":" + " ";
        }
        String names[] = familyNames.split(",");
        if(names.length==1){
            names[0]=names[0]+",";
        }
        List<String> list = Arrays.asList(names);
        startProgressDialog();
        if (TextUtils.isEmpty(smokeStateNamesEdit)) {
            smokeStateNamesEdit = "无";
        }

        if (TextUtils.isEmpty(drinkStateNames)) {
            drinkStateNamesEdit = "无";
        }
        if(smokeState_noCheck.isChecked()){
            smokeStateNamesEdit = "无";
        }
        if(drinkState_noCheck.isChecked()){
            drinkStateNamesEdit = "无";
        }


        if (edt_ywgms.getVisibility() == View.VISIBLE  && !TextUtils.isEmpty(medicineAllergyNamesEdit)) {
            medicineAllergyNamesEdit = medicineAllergyNamesEdit + getEdtValueOther(edt_ywgms);
        }


        if (edt_jwbs.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValueOther(edt_jwbs))) {
            pastMedicalHistoryNamesEdit = pastMedicalHistoryNamesEdit + getEdtValueOther(edt_jwbs);
        }

        if (edt_ycbs.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValueOther(edt_ycbs))) {
            geneticHistoryNmaesEdit = geneticHistoryNmaesEdit + getEdtValueOther(edt_ycbs);
        }


        if(medicineAllergy_noCheck.isChecked()){
            medicineAllergyNamesEdit="";
        }
        if(geneticHistory_noCheck.isChecked()){
            geneticHistoryNmaesEdit="";
        }
        if(pastMedicalHistory_noCheck.isChecked()){
            pastMedicalHistoryNamesEdit="";
        }

        if(TextUtils.isEmpty(bloodtypeEdit)){
            bloodtypeEdit="A型";
        }
        if(TextUtils.isEmpty(maritalStatusNamesEdit)){
            maritalStatusNamesEdit="未婚";
        }
        requestMaker.BaseDataInsertInsert(userid, maritalStatusNamesEdit, medicineAllergyNamesEdit, geneticHistoryNmaesEdit, pastMedicalHistoryNamesEdit, list, smokeStateNamesEdit, drinkStateNamesEdit, bloodtypeEdit, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                stopProgressDialog();
                String value = result.toString();
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BaseDataInsert");
                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        if(TextUtils.isEmpty(tagfile)) {
                            ToastUtils.showMessage(getActivity(),"保存成功");
                            getActivity().finish();
                            String id=SharePreferenceUtil.getInstance(getActivity()).getUserId();
                            User user=dao.findUserInfoById(id);
                            user.setType(3);
                            dao.updateUserInfo(user, id);
                        }else{
                            CustomApplcation.getInstance().finishSingleActivityByClass(RegisterFinishAct.class);
                            Intent i=new Intent(getActivity(), MainActivity.class);
                            i.putExtra("Home","Home");
                            getActivity().startActivity(i);

                            if(tagfile.equals("TagFile")) {
                                ToastUtils.showMessage(getActivity(),"保存成功");
                                String id = SharePreferenceUtil.getInstance(getActivity()).getUserId();
                                User user = dao.findUserInfoById(id);
                                user.setType(3);
                                dao.updateUserInfo(user, id);
                            }
                        }
                    } else
                        ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                if(btn_save!=null)
                btn_save.setEnabled(true);
            }
        }));
    }

    public void upload() {

        if (edt_father.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edt_father))) {
            familyMedicalhistory_fatherNamesEdit = familyMedicalhistory_fatherNamesEdit + "父亲:" + getEdtValue(edt_father);
        }
        if (edt_mother.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edt_mother))) {
            familyMedicalhistory_motherNamesEdit = familyMedicalhistory_motherNamesEdit + "母亲:" + getEdtValue(edt_mother);
        }
        if (edi_sister.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edi_sister))) {
            familyMedicalhistory_sisterNamesEdit = familyMedicalhistory_sisterNamesEdit + "兄弟姐妹:" + getEdtValue(edi_sister);
        }
        if (edt_children.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValue(edt_children))) {
            familyMedicalhistory_childrenNamesEdit = familyMedicalhistory_childrenNamesEdit + "子女:" + getEdtValue(edt_children);
        }
        if(familyMedicalhistory_father_noCheck.isChecked()){
            familyMedicalhistory_fatherNamesEdit="";
        }
        if(familyMedicalhistory_mother_noCheck.isChecked()){
            familyMedicalhistory_motherNamesEdit="";
        }
        if(familyMedicalhistory_sister_noCheck.isChecked()){
            familyMedicalhistory_sisterNamesEdit="";

        }
        if(familyMedicalhistory_children_noCheck.isChecked()){
            familyMedicalhistory_childrenNamesEdit="";
        }
        String familyNames = familyMedicalhistory_fatherNamesEdit + familyMedicalhistory_motherNamesEdit + familyMedicalhistory_sisterNamesEdit + familyMedicalhistory_childrenNamesEdit;
        if (TextUtils.isEmpty(familyNames)) {
            familyNames = " " + ":" + " " + "," + " " + ":" + " ";
        }
        String names[] = familyNames.split(",");
        if(names.length==1){
            names[0]=names[0]+",";
        }
        List<String> list = Arrays.asList(names);
        startProgressDialog();
        if (TextUtils.isEmpty(smokeStateNamesEdit)) {
            smokeStateNamesEdit = "无";
        }
        if (TextUtils.isEmpty(drinkStateNamesEdit)) {
            drinkStateNamesEdit = "无";
        }
        if(smokeState_noCheck.isChecked()){
            smokeStateNamesEdit = "无";
        }
        if(drinkState_noCheck.isChecked()){
            drinkStateNamesEdit = "无";
        }

        if (edt_ywgms.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValueOther(edt_ywgms))) {
            medicineAllergyNamesEdit = medicineAllergyNamesEdit + getEdtValueOther(edt_ywgms);
        }
        if (edt_jwbs.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValueOther(edt_jwbs))) {
            pastMedicalHistoryNamesEdit = pastMedicalHistoryNamesEdit + getEdtValueOther(edt_jwbs);
        }
        if (edt_ycbs.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getEdtValueOther(edt_ycbs))) {
            geneticHistoryNmaesEdit = geneticHistoryNmaesEdit + getEdtValueOther(edt_ycbs);
        }
        if(medicineAllergy_noCheck.isChecked()){
            medicineAllergyNamesEdit="";
        }
        if(geneticHistory_noCheck.isChecked()){
            geneticHistoryNmaesEdit="";
        }
        if(pastMedicalHistory_noCheck.isChecked()){
            pastMedicalHistoryNamesEdit="";
        }
        if(TextUtils.isEmpty(bloodtypeEdit)){
            bloodtypeEdit="A型";
        }
        if(TextUtils.isEmpty(maritalStatusNamesEdit)){
            maritalStatusNamesEdit="未婚";
        }
        requestMaker.BaseDataUpdate(userid, maritalStatusNamesEdit, medicineAllergyNamesEdit, geneticHistoryNmaesEdit, pastMedicalHistoryNamesEdit, list, smokeStateNamesEdit, drinkStateNamesEdit, bloodtypeEdit, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                stopProgressDialog();
                String value = result.toString();
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BaseDataUpdate");
                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        ToastUtils.showMessage(getActivity(), "保存成功");
                        getActivity().finish();
                    } else
                        ToastUtils.showMessage(getActivity(), array.getJSONObject(0).getString("MessageContent"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                if(btn_save!=null)
                btn_save.setEnabled(true);
            }
        }));
    }

    public void setDatas(String key) {
        String str[] = key.split(":");
        int code = Integer.parseInt(str[0]);
        switch (code) {
            case MEDICINEALLERGY_CODE:
                medicineAllergyNamesEdit = medicineAllergyNamesEdit + str[1] + " ";
                break;
            case PASTMEDICALHISTORY_CODE:
                pastMedicalHistoryNamesEdit = pastMedicalHistoryNamesEdit + str[1] + " ";
                break;
            case FAMILYMEDICALHISTORY_FATHER_CODE:
                familyMedicalhistory_fatherNamesEdit = familyMedicalhistory_fatherNamesEdit + "父亲:" + str[1] + ",";
                break;
            case FAMILYMEDICALHISTORY_MOTHER_CODE:
                familyMedicalhistory_motherNamesEdit = familyMedicalhistory_motherNamesEdit + "母亲:" + str[1] + ",";
                break;
            case FAMILYMEDICALHISTORY_SISTER_CODE:
                familyMedicalhistory_sisterNamesEdit = familyMedicalhistory_sisterNamesEdit + "兄弟姐妹:" + str[1] + ",";
                break;
            case FAMILYMEDICALHISTORY_CHILDREN_CODE:
                familyMedicalhistory_childrenNamesEdit = familyMedicalhistory_childrenNamesEdit + "子女:" + str[1] + ",";
                break;
            case GENETICHISTORY_CODE:
                geneticHistoryNmaesEdit = geneticHistoryNmaesEdit + str[1] + " ";
                break;
            case SMOKESTATE_CODE:
                smokeStateNamesEdit = str[1];
                break;
            case DRINKSTATE_CODE:
                drinkStateNamesEdit = str[1];
                break;
            case MARITALSTATUS:
                maritalStatusNamesEdit = map.get(key);
                break;
            case BLOODTYPE:
                bloodtypeEdit = map.get(key);
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }

    public void initDatas() {
       /* requestMaker.BaseDataInquiry(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;

                try {
                    JSONArray json = mySO.getJSONArray("BaseDataInquiry");
                    if (json.getJSONObject(0).has("MessageCode")) {
                        String MessageCode = json.getJSONObject(0).getString("MessageCode");
                        if ("2".equals(MessageCode)) {
                            ToastUtils.showMessage(getActivity(), "数据为空！");
                            return;
                        }
                        if ("1".equals(MessageCode)) {
                            ToastUtils.showMessage(getActivity(), "查询失败！");
                            return;
                        }
                    } else {
                        JSONObject jsonobject = json.getJSONObject(0);
                        maritalStatusNames = jsonobject.getString("Marriage");
                        smokeStateNames = jsonobject.getString("Smoking");
                        drinkStateNames = jsonobject.getString("Drinking");
                        medicineAllergyNames = jsonobject.getString("DrugAllergy");
                        pastMedicalHistoryNames = jsonobject.getString("MedicalHistory");
                        geneticHistoryNmaes = jsonobject.getString("GeneticHistory");
                        familyNames = jsonobject.getString("FamilyHistory");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));*/
    }


    public void setmView() {

    }

    public class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private Type type = null;

        public MyCheckedChangeListener(Type type) {
            this.type = type;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (type) {
                case MARITALSTATUS:
                    addData(MEDICINEALLERGY_CODE,buttonView,isChecked);
                    break;
                case MEDICINEALLERGY:
                    addData(MEDICINEALLERGY_CODE, buttonView, isChecked);
                    break;
                case PASTMEDICALHISTORY:
                    addData(PASTMEDICALHISTORY_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_FATHER:
                    addData(FAMILYMEDICALHISTORY_FATHER_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_MOTHER:
                    addData(FAMILYMEDICALHISTORY_MOTHER_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_SISTER:
                    addData(FAMILYMEDICALHISTORY_SISTER_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_CHILDREN:
                    addData(FAMILYMEDICALHISTORY_CHILDREN_CODE, buttonView, isChecked);
                    break;
                case GENETICHISTORY:
                    addData(GENETICHISTORY_CODE, buttonView, isChecked);
                    break;
                case DRINKSTATE:
                    resetCheck(Type.DRINKSTATE, buttonView, isChecked);
                    addData(DRINKSTATE_CODE, buttonView, isChecked);
                    break;
                case SMOKESTATE:
                    resetCheck(Type.SMOKESTATE, buttonView, isChecked);
                    addData(SMOKESTATE_CODE, buttonView, isChecked);
                    break;
            }
        }
    }

    public enum Type {
        MARITALSTATUS, MEDICINEALLERGY, PASTMEDICALHISTORY,
        FAMILYMEDICALHISTORY_FATHER, FAMILYMEDICALHISTORY_MOTHER,
        FAMILYMEDICALHISTORY_SISTER, FAMILYMEDICALHISTORY_CHILDREN,
        GENETICHISTORY, SMOKESTATE, DRINKSTATE;
    }

    public void addData(Integer type, CompoundButton buttonView, boolean isChecked) {
        CheckBox box = (CheckBox) buttonView;
        if (isChecked) {
            map.put(type + ":" + box.getText().toString(), box.getText().toString());
        } else {
            if (map.containsKey(type + ":" + box.getText().toString())) {
                map.remove(type + ":" + box.getText().toString());
            }
        }
    }

    public void resetCheck(Type type, CompoundButton buttonView, boolean isChecked) {
        CheckBox box = (CheckBox) buttonView;
        int id = box.getId();

        switch (type) {
            case SMOKESTATE:
                if (isChecked) {
                    if (id == smokeState_checkbox_cb.getId()) {
                        smokeState_checkbox_cb.setChecked(true);
                    } else {
                        smokeState_checkbox_cb.setChecked(false);
                    }
                    if (id == smokeState_checkbox_oer.getId()) {
                        smokeState_checkbox_oer.setChecked(true);
                    } else {
                        smokeState_checkbox_oer.setChecked(false);
                    }
                    if (id == smokeState_checkbox_jc.getId()) {
                        smokeState_checkbox_jc.setChecked(true);
                    } else {
                        smokeState_checkbox_jc.setChecked(false);
                    }
                    if (id == smokeState_checkbox_mt.getId()) {
                        smokeState_checkbox_mt.setChecked(true);
                    } else {
                        smokeState_checkbox_mt.setChecked(false);
                    }
                    if (id == smokeState_checkbox_yjy.getId()) {
                        smokeState_checkbox_yjy.setChecked(true);
                    } else {
                        smokeState_checkbox_yjy.setChecked(false);
                    }
                } else {
                    id = 0;
                }
                break;
            case DRINKSTATE:
                if (isChecked) {
                    if (id == drinkState_checkbox_cb.getId()) {
                        drinkState_checkbox_cb.setChecked(true);
                    } else {
                        drinkState_checkbox_cb.setChecked(false);
                    }
                    if (id == drinkState_checkbox_oer.getId()) {
                        drinkState_checkbox_oer.setChecked(true);
                    } else {
                        drinkState_checkbox_oer.setChecked(false);
                    }
                    if (id == drinkState_checkbox_jc.getId()) {
                        drinkState_checkbox_jc.setChecked(true);
                    } else {
                        drinkState_checkbox_jc.setChecked(false);
                    }
                    if (id == drinkState_checkbox_mt.getId()) {
                        drinkState_checkbox_mt.setChecked(true);
                    } else {
                        drinkState_checkbox_mt.setChecked(false);
                    }
                    if (id == drinkState_checkbox_yjj.getId()) {
                        drinkState_checkbox_yjj.setChecked(true);
                    } else {
                        drinkState_checkbox_yjj.setChecked(false);
                    }

                } else {
                    id = 0;
                }
                break;
        }
    }

    public static Fragment getInstance(String id, String type, HealthFiles hf,String tag) {
        userid = id;
        stype = type;
        HealthFilesFragment1 fragment=  new HealthFilesFragment1();
        tagfile=tag;
        Bundle bundle=new Bundle();

        bundle.putSerializable("healthfiles",hf);
        fragment.setArguments(bundle);
        return fragment;
    }
    public String getEdtValueOther(EditText edt) {
        String str=edt.getText().toString().trim();
        if(!TextUtils.isEmpty(str) ) {
            String regEx=StringFilter(str);
            if (regEx.contains(",")){
                regEx.replaceAll(",", "，");
            }

            return "其他-" +regEx ;
        }else {
            return "";
        }
    }
    public String getEdtValue(EditText edt) {
        String str=edt.getText().toString().trim();
        if(!TextUtils.isEmpty(str) ) {
            String regEx=StringFilter(str);
            if (regEx.contains(",")){
                regEx.replaceAll(",", "，");
            }

            return "其他-" +regEx+"," ;
        }else {
            return "" ;
        }
    }
    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}''\\[\\].<>~！@#￥%……&*（）——+|{}【】‘”“’？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
