package com.zzu.ehome.utils;


import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.bean.OcrBean;
import com.zzu.ehome.view.crop.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xtfgq on 2016/4/5.
 * 统一请求地址
 */
public class RequestMaker {
    private static RequestMaker requestMaker = null;
    private final String SOAP_NAMESPACE = Constants.URL001;
    private final String SOAP_NAMESPACEECG = "http://www.topmd.cn/TopmdWeixin/";
    private final String SOAP_URL = Constants.URL002;

    public static RequestMaker getInstance() {
        if (requestMaker == null) {
            requestMaker = new RequestMaker();
            return requestMaker;
        } else {
            return requestMaker;
        }
    }

    /**
     * 广告位
     *
     * @param date
     * @param task
     */
    public void searchAds(String date, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";

        str = String.format(str, new Object[]{date, "01"});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE +
                        Constants.Ads, Constants.Ads,
                SOAP_URL, paramMap);
    }

    public void getDoctorsRequest(int userid, String IsPMD, String PageIndex, String PageSize, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><IsPMD>%s</IsPMD><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
        if (userid != 0) {
            str = String.format(str, new Object[]
                    {userid, "", "", IsPMD, PageIndex, PageSize});
        } else {
            str = String.format(str, new Object[]
                    {"", "", "", IsPMD, PageIndex, PageSize});
        }
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DoctorInquiry, Constants.DoctorInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param psd
     * @param ClientID
     * @param task
     */
    public void userLogin(String username, String psd, String ClientID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><PhoneType>%s</PhoneType></Request>";
        str = String.format(str, new Object[]
                {username, psd, ClientID, "0"});
        paramMap.put("str", str);
        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.LOGIN, Constants.LOGIN,
                SOAP_URL, paramMap);
    }

    /**
     * 上传头像
     *
     * @param pic
     * @param userid
     * @param filename
     * @param task
     */
    public void uploadPic(String pic, String userid, String filename, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("UserID", userid);
        paramMap.put("fileName", filename);
        paramMap.put("img", pic);
        task.execute(Constants.URL001Topmd, Constants.URL001Topmd + Constants.UploadUserPhoto, Constants.UploadUserPhoto,
                Constants.URL002Topmd, paramMap);
    }

    public void userRegister(String UserMobile, String password, String ClientID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><FromTo>%s</FromTo><PhoneType>%s</PhoneType></Request>";
        str = String.format(str, new Object[]
                {UserMobile, password, ClientID, "5", "0"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserRegister, Constants.UserRegister,
                SOAP_URL, paramMap);

    }

    public void sendCode(String mobile, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Flag>%s</Flag></Request>";
        str = String.format(str, new Object[]
                {mobile, "001"});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.SendAuthCode, Constants.SendAuthCode,
                SOAP_URL, paramMap);

    }
    public void sendAuthCode(String mobile, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Flag>%s</Flag></Request>";
        str = String.format(str, new Object[]
                {mobile, "002"});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.SendAuthCode, Constants.SendAuthCode,
                SOAP_URL, paramMap);

    }

    public void userInfo(String userid, String username, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><RealName>%s</RealName></Request>";
        str = String.format(str, new Object[]
                {userid, username});
        paramMap.put("str", str);
        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }


    public void userInfo(String userid, String username, String sex, String userno, String age, String height, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><RealName>%s</RealName><UserSex>%s</UserSex><UserNO>%s</UserNO><UserAge>%s</UserAge><UserHeight>%s</UserHeight></Request>";
        str = String.format(str, new Object[]
                {userid, username, sex, userno, age, height});
        paramMap.put("str", str);
        Log.e("str",str);
        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }

    public void userInfo2(String userid, String height, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><UserHeight>%s</UserHeight></Request>";
        str = String.format(str, new Object[]
                {userid, height});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }

    /**
     * 用户信息查询
     *
     * @param userid
     * @param task
     */
    public void UserInquiry(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInquiry, Constants.UserInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 健康数据
     *
     * @param userid
     * @param task
     */
    public void healthInfo(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataSearch, Constants.HealthDataSearch,
                SOAP_URL, paramMap);

    }

    public void TemperatureInsert(String CardNO,String userid, String time, String value, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><Value>%s</Value><MonitorTime>%s</MonitorTime><Fromto>%s</Fromto></Request>";
        str = String.format(str, new Object[]
                {CardNO,userid, value, time,"01"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TemperatureInsert, Constants.TemperatureInsert,
                SOAP_URL, paramMap);

    }

    public void WeightInsert(String cardNo, String userid,String height,String bmi,String value, String time, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><Height>%s</Height><BMI>%s</BMI><Weight>%s</Weight><MonitorTime>%s</MonitorTime><Fromto>%s</Fromto></Request>";
        str = String.format(str, new Object[]
                {cardNo,userid, height,bmi,value, time,"01"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeightInsert, Constants.WeightInsert,
                SOAP_URL, paramMap);

    }

    public void BloodPressureInsert(String cardNo,String userid, String High, String low, String xinlv, String time, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><High>%s</High><Low>%s</Low><Pulse>%s</Pulse><MonitorTime>%s</MonitorTime><Fromto>%s</Fromto></Request>";
        str = String.format(str, new Object[]
                {cardNo,userid, High, low, xinlv, time,"01"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodPressureInsert, Constants.BloodPressureInsert,
                SOAP_URL, paramMap);

    }

    public void BloodSugarInsert(String cardNo,String userid, String BloodSugarValue, String MonitorPoint, String MonitorTime, String Type, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><BloodSugarValue>%s</BloodSugarValue><HoursAfterMeal>%s</HoursAfterMeal><MonitorTime>%s</MonitorTime><Type>%s</Type><Fromto>%s</Fromto></Request>";
        str = String.format(str, new Object[]
                {cardNo,userid, BloodSugarValue, MonitorPoint, MonitorTime, Type,"01"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodSugarInsert, Constants.BloodSugarInsert,
                SOAP_URL, paramMap);

    }

    /**
     * 医院信息
     *
     * @param task
     */
    public void HospitalInquiry(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID></Request>";
        str = String.format(str, new Object[]
                {""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HOSPITALINQUIRY, Constants.HOSPITALINQUIRY,
                SOAP_URL, paramMap);

    }


    public void DepartmentInquiry(String hosptial_id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Department_Id>%s</Department_Id><Hospital_Id>%s</Hospital_Id></Request>";
        str = String.format(str, new Object[]
                {"", hosptial_id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DEPARTMENTINQUIRY, Constants.DEPARTMENTINQUIRY,
                SOAP_URL, paramMap);
    }

    public void DoctorInquiry(String department_id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Department_Id >%s</Department_Id ><Doctor_Id>%s</Doctor_Id></Request>";
        str = String.format(str, new Object[]
                {department_id, ""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DOCTORINQUIRY, Constants.DOCTORINQUIRY,
                SOAP_URL, paramMap);
    }

    /**
     * 新建病例
     *
     * @param userid
     * @param AppointmentTime
     * @param Hospital
     * @param Diagnosis
     * @param Opinion
     * @param list
     * @param task
     */
    public void OtherTreatmentInsert(String userid, String AppointmentTime, String Hospital, String Diagnosis, String Opinion, List<Images> list, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String images2 = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                images2 += "<Image>" + "<Code>" + list.get(i).getCode() + "</Code>" + "<FileName>" + list.get(i).getFileName() + "</FileName>" + "</Image>";
            }
        }
        String str = "<Request><User_Id>%s</User_Id><AppointmentTime>%s</AppointmentTime><Hospital>%s</Hospital>" +
                "<Diagnosis>%s</Diagnosis>" +
                "<Opinion>%s</Opinion>%s</Request>";
        str = String.format(str, new Object[]
                {userid, AppointmentTime, Hospital, Diagnosis, Opinion, images2});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.OtherTreatmentInsert, Constants.OtherTreatmentInsert,
                SOAP_URL, paramMap);
    }


    public void ScheduleInquiry(String doctor_id, String PatientId, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorId>%s</DoctorId ><PatientId>%s</PatientId></Request>";
        str = String.format(str, new Object[]
                {doctor_id, PatientId});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.SCHEDULEINQUIRY, Constants.SCHEDULEINQUIRY,
                SOAP_URL, paramMap);
    }


    public void TreatmentInsert(String user_id, String appointmentTime, String doctor_id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Patient_Id>%s</Patient_Id><AppointmentTime>%s</AppointmentTime><Doctor_Id>%s</Doctor_Id></Request>";
        str = String.format(str, new Object[]
                {user_id, appointmentTime, doctor_id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTINSERT, Constants.TREATMENTINSERT,
                SOAP_URL, paramMap);
    }

    public void TreatmentInquirywWithPage(String usrid, String pagesize, String pageindex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid, pagesize, pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TreatmentInquirywWithPage, Constants.TreatmentInquirywWithPage,
                SOAP_URL, paramMap);

    }

    public void DoctorInquiry(String department_id, String doctor_id, String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Department_Id >%s</Department_Id ><Doctor_Id>%s</Doctor_Id><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {department_id, doctor_id, userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DOCTORINQUIRY, Constants.DOCTORINQUIRY,
                SOAP_URL, paramMap);
    }

    public void FavorDoctorInsert(String UserID, String DoctorID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID >%s</UserID ><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {UserID, DoctorID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FAVORDOCTORINSERT, Constants.FAVORDOCTORINSERT,
                SOAP_URL, paramMap);
    }


    public void FavorDoctorInquiry(String UserID, String pasesize, String pageindex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID >%s</UserID ><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {UserID, pasesize, pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FAVORDOCTORINQUIRY, Constants.FAVORDOCTORINQUIRY,
                SOAP_URL, paramMap);
    }

    public void FeedBackInsert(String UserID, String Value, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID >%s</UserID ><Value>%s</Value></Request>";
        str = String.format(str, new Object[]
                {UserID, Value});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FEEDBACKINSERT, Constants.FEEDBACKINSERT,
                SOAP_URL, paramMap);
    }

    public void UserAuthChange(String UserMobile, String Password, String NewPassword, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><NewPassword>%s</NewPassword></Request>";
        str = String.format(str, new Object[]
                {UserMobile, Password, NewPassword,"002"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.USERAUTHCHANGE, Constants.USERAUTHCHANGE,
                SOAP_URL, paramMap);
    }

    public void loginOut(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserClientIDChange, Constants.UserClientIDChange,
                SOAP_URL, paramMap);

    }

    public void HealthDataInquirywWithPage(String usrid, String pagesize, String pageindex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid, pagesize, pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataInquirywWithPage, Constants.HealthDataInquirywWithPage,
                SOAP_URL, paramMap);
    }


    public void UserFindPass(String UserMobile, String Password, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile >%s</UserMobile ><Password>%s</Password></Request>";
        str = String.format(str, new Object[]
                {UserMobile, Password});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserFindPass, Constants.UserFindPass,
                SOAP_URL, paramMap);
    }

    public void TemperatureInquiry(String UserID, String StartTime, String EndTime, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID, StartTime, EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TemperatureInquiry, Constants.TemperatureInquiry,
                SOAP_URL, paramMap);


    }

    /**
     * 新温度查询
     *
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void TemperatureInquiry(String CardNO,String UserID, String StartTime, String EndTime, String type, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {CardNO,UserID, StartTime, EndTime, type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TemperatureInquiry, Constants.TemperatureInquiry,
                SOAP_URL, paramMap);

    }


    public void TreatmentInsert(String DoctorId, String PatientId, String DateStr, String TimeSpanStr, String PerTime, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorId>%s</DoctorId><PatientId>%s</PatientId><DateStr>%s</DateStr><TimeSpanStr>%s</TimeSpanStr><PerTime>%s</PerTime></Request>";
        str = String.format(str, new Object[]
                {DoctorId, PatientId, DateStr, TimeSpanStr, PerTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTINSERT, Constants.TREATMENTINSERT,
                SOAP_URL, paramMap);
    }

    public void TreatmentSearch(String PatientId, String start, String end, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><PatientId>%s</PatientId><IntStatus>%s</IntStatus><Start>%s</Start><End>%s</End></Request>";
        str = String.format(str, new Object[]
                {PatientId, "0", start, end});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTSEARCH, Constants.TREATMENTSEARCH,
                SOAP_URL, paramMap);
    }

    public void TreatmentCancel(String ReservationId, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ReservationId>%s</ReservationId></Request>";
        str = String.format(str, new Object[]
                {ReservationId});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTCANCEL, Constants.TREATMENTCANCEL,
                SOAP_URL, paramMap);
    }

    public void HealthDataInquirywWithPageType(String usrid, String CardNo,String pagesize, String pageindex, String type, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><CardNO>%s</CardNO><Type>%s</Type><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid,CardNo,type, pagesize, pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataInquirywWithPage, Constants.HealthDataInquirywWithPage,
                SOAP_URL, paramMap);
    }

    public void WeightInquiryType(String UserID, String StartTime, String EndTime, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID, StartTime, EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeightInquiry, Constants.WeightInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新体重查询
     *
     * @param CardNO
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void WeightInquiryType(String CardNO, String userid,String StartTime, String EndTime, String type, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {CardNO, userid,StartTime, EndTime, type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeightInquiry, Constants.WeightInquiry,
                SOAP_URL, paramMap);
    }

    public void FavorDoctorDelete(String UserID, String DoctorID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {UserID, DoctorID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FAVORDOCTORDELETE, Constants.FAVORDOCTORDELETE,
                SOAP_URL, paramMap);
    }


    public void BloodPressureInquiryType(String UserID, String StartTime, String EndTime, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID, StartTime, EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodPressureInquiry, Constants.BloodPressureInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新血压查询接口
     *
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void BloodPressureInquiryType(String cardNo,String UserID, String StartTime, String EndTime, String type, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {cardNo,UserID, StartTime, EndTime, type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodPressureInquiry, Constants.BloodPressureInquiry,
                SOAP_URL, paramMap);
    }



    /**
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void BloodSugarInquiryType(String CardNO,String UserID, String StartTime, String EndTime, String type, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {CardNO,UserID, StartTime, EndTime, type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodSugarInquiry, Constants.BloodSugarInquiry,
                SOAP_URL, paramMap);
    }

    public void updaateApk(JsonAsyncTask_Info task) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";

        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.VersionInquiry, Constants.VersionInquiry,
                SOAP_URL, paramMap);
    }

    public void BaseDataInsertInsert(String userid, String Marriage, String DrugAllergy, String GeneticHistory, String MedicalHistory, List<String> list, String Smoking, String Drinking,String bloodtype, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String familyHistory = "";
        if (list != null && list.size() > 0) {
            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    String[] ss = list.get(i).split(":");
                    familyHistory += "<FamilyHistory>" + "<Relation>" + list.get(i).split(":")[0] + "</Relation>" + "<Disease>" + list.get(i).split(":")[1] + "</Disease>" + "</FamilyHistory>";


                }

            }
        }
        String str = "<Request><UserID >%s</UserID><Marriage>%s</Marriage><DrugAllergy>%s</DrugAllergy>" + "<GeneticHistory>%s</GeneticHistory>" +
                "<MedicalHistory>%s</MedicalHistory>" + "%s" +

                "<Smoking>%s</Smoking><Drinking>%s</Drinking><BloodType>%s</BloodType></Request>";
        str = String.format(str, new Object[]
                {userid, Marriage, DrugAllergy, GeneticHistory, MedicalHistory, familyHistory, Smoking, Drinking,bloodtype});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BaseDataInsert, Constants.BaseDataInsert,
                SOAP_URL, paramMap);
    }

    public void BaseDataInquiry(String UserID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {UserID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BASEDATAINQUIRY, Constants.BASEDATAINQUIRY,
                SOAP_URL, paramMap);
    }

    public void BaseDataUpdate(String userid, String Marriage, String DrugAllergy, String GeneticHistory, String MedicalHistory, List<String> list, String Smoking, String Drinking,String bloodtype, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String familyHistory = "";
        if (list != null && list.size() > 0) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {

                    familyHistory += "<FamilyHistory>" + "<Relation>" + list.get(i).split(":")[0] + "</Relation>" + "<Disease>" + list.get(i).split(":")[1] + "</Disease>" + "</FamilyHistory>";

                }
            }
        }
        String str = "<Request><UserID >%s</UserID><Marriage>%s</Marriage><DrugAllergy>%s</DrugAllergy>" + "<GeneticHistory>%s</GeneticHistory>" +
                "<MedicalHistory>%s</MedicalHistory>" + "%s" +

                "<Smoking>%s</Smoking><Drinking>%s</Drinking><BloodType>%s</BloodType></Request>";
        str = String.format(str, new Object[]
                {userid, Marriage, DrugAllergy, GeneticHistory, MedicalHistory, familyHistory, Smoking, Drinking,bloodtype});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BASEDATAUPDATE, Constants.BASEDATAUPDATE,
                SOAP_URL, paramMap);
    }

    public void HolterPDFInquiry(String userid, String startTime, String endTime, JsonAsyncTask_ECGInfo task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartDate></StartDate><EndDate></EndDate></Request>";
        str = String.format(str, new Object[]
                {userid, startTime, endTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.HolterPDFInquiry, Constants.HolterPDFInquiry,
                Constants.URL003, paramMap);
    }

    public void BJResultInquiry(String userid, String startTime, String endTime, JsonAsyncTask_ECGInfo task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartDate></StartDate><EndDate></EndDate></Request>";
        str = String.format(str, new Object[]
                {userid, startTime, endTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.BJResultInquiry, Constants.BJResultInquiry,
                Constants.URL003, paramMap);
    }

    /**
     * 指定体检医院查询
     *
     * @param task
     */
    public void MedicalAgencyInquiry(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicalAgencyInquiry, Constants.MedicalAgencyInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新建体检
     */
    public void MedicalReportInsert(String userid, String username, String CheckTime, String InstituteName, List<Images> list, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String images2 = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                images2 += "<Image>" + "<Code>" + list.get(i).getCode() + "</Code>" + "<FileName>" + list.get(i).getFileName() + "</FileName>" + "</Image>";
            }
        }
        String str = "<Request><UserID>%s</UserID><UserName>%s</UserName><CheckTime>%s</CheckTime>" + "<InstituteName>%s</InstituteName>" +
                "%s</Request>";
        str = String.format(str, new Object[]
                {userid, username, CheckTime, InstituteName, images2});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicalReportInsert, Constants.MedicalReportInsert,
                SOAP_URL, paramMap);
    }

    /**
     *

     */
    public void MeidicalReportInquiry(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MeidicalReportInquiry, Constants.MeidicalReportInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 查询体检详情
     *
     * @param userid
     * @param id
     * @param task
     */
    public void MeidicalReportInquiry(String userid, String id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><ID>%s</ID></Request>";
        str = String.format(str, new Object[]
                {userid, id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MeidicalReportInquiry, Constants.MeidicalReportInquiry,
                SOAP_URL, paramMap);
    }

    public void RegionInquiry(String ParentID,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ParentID>%s</ParentID></Request>";
        str = String.format(str, new Object[]
                {ParentID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RegionInquiry, Constants.RegionInquiry,
                SOAP_URL, paramMap);
    }

    public void HospitalInquiryByRegion(String id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><RegionID>%s</RegionID></Request>";
        str = String.format(str, new Object[]
                {id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HospitalInquiryByRegion, Constants.HospitalInquiryByRegion,
                SOAP_URL, paramMap);
    }

    public void HealthDataSearchByDate(String cardNum,String usrid, String date, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><Date>%s</Date></Request>";
        str = String.format(str, new Object[]
                {cardNum,usrid, date});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataSearchByDate, Constants.HealthDataSearchByDate,
                SOAP_URL, paramMap);
    }

    public void MedicationRecordInquiry(String usrid, String cardNum,String PageSize, String PageIndex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><CardNO>%s</CardNO><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid, cardNum,PageSize, PageIndex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicationRecordInquiry, Constants.MedicationRecordInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新建用药记录
     */

    public void MedicationRecordInsert(String UserID, String MedicationTime, String DrugName, String Number, String Remarks, String cardNo,List<Images> list, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String images2 = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                images2 += "<Image>" + "<Code>" + list.get(i).getCode() + "</Code>" + "<FileName>" + list.get(i).getFileName() + "</FileName>" + "</Image>";
            }
        }
        String str = "<Request><UserID>%s</UserID><MedicationTime>%s</MedicationTime><DrugName>%s</DrugName><Number>%s</Number><Remarks>%s</Remarks><CardNO>%s</CardNO>" +
                "%s</Request>";
        str = String.format(str, new Object[]
                {UserID, MedicationTime, DrugName, Number, Remarks,cardNo,images2});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicationRecordInsert, Constants.MedicationRecordInsert,
                SOAP_URL, paramMap);
    }

    public void StepCounterInsert(String CardNO,String usrid, String TotalStep, String TotalDistance, String TotalTime, String TotalHeat, String MonitorTime, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><TotalStep>%s</TotalStep><TotalDistance>%s</TotalDistance><TotalTime>%s</TotalTime><TotalHeat>%s</TotalHeat><MonitorTime>%s</MonitorTime><Fromto>%s</Fromto></Request>";
        str = String.format(str, new Object[]
                {CardNO,usrid, TotalStep, TotalDistance, TotalTime, TotalHeat, MonitorTime,"01"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.StepCounterInsert, Constants.StepCounterInsert,
                SOAP_URL, paramMap);
    }

    public void StepCounterInquiry(String CardNO,String usrid, String MonitorTime, String PageSize, String PageIndex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><MonitorTime>%s</MonitorTime><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {CardNO,usrid, MonitorTime, PageSize, PageIndex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.StepCounterInquiry, Constants.StepCounterInquiry,
                SOAP_URL, paramMap);
    }

    public void StepCounterInquiry(String cardNo,String usrid, String MonitorTime, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><UserID>%s</UserID><MonitorTime>%s</MonitorTime></Request>";
        str = String.format(str, new Object[]
                {cardNo,usrid, MonitorTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.StepCounterInquiry, Constants.StepCounterInquiry,
                SOAP_URL, paramMap);
    }

    public void WeatherInquiry(String city, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><currentCity>%s</currentCity></Request>";
        str = String.format(str, new Object[]
                {city});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeatherInquiry, Constants.WeatherInquiry,
                SOAP_URL, paramMap);
    }

    public void UserRelationshipInsert(String userid, String Relationship, String RUserID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Relationship>%s</Relationship><RUserID>%s</RUserID></Request>";
        str = String.format(str, new Object[]
                {userid, Relationship, RUserID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserRelationshipInsert, Constants.UserRelationshipInsert,
                SOAP_URL, paramMap);
    }

    /*
    *录入身高体重 weight
     */
    public void userInfoWeight(String userid, String height, String weight, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><UserHeight>%s</UserHeight><UserWeight>%s</UserWeight></Request>";
        str = String.format(str, new Object[]
                {userid, height, weight});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }

    public void NewsInquiry(String Pagesize, String PageIndex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {Pagesize, PageIndex});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.NewsInquiry, Constants.NewsInquiry,
                SOAP_URL, paramMap);

    }

    public void HospitalInquiryByTopmd(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HospitalInquiryByTopmd, Constants.HospitalInquiryByTopmd,
                SOAP_URL, paramMap);

    }

    public void UserRelationshipInquiry(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);


        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserRelationshipInquiry, Constants.UserRelationshipInquiry,
                SOAP_URL, paramMap);

    }

    public void HospitalDepertByTopmd(String HospitalID, String ParentDeprtID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID><ParentDeprtID>%s</ParentDeprtID></Request>";
        str = String.format(str, new Object[]
                {HospitalID, ParentDeprtID});
        paramMap.put("str", str);


        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HospitalDepertByTopmd, Constants.HospitalDepertByTopmd,
                SOAP_URL, paramMap);

    }

    public void DepertDoctorByTopmd(String HospitalID, String DepartmentID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID><DepartmentID>%s</DepartmentID></Request>";
        str = String.format(str, new Object[]
                {HospitalID, DepartmentID});
        paramMap.put("str", str);


        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DepertDoctorByTopmd, Constants.DepertDoctorByTopmd,
                SOAP_URL, paramMap);

    }

    public void DoctorSchemaByTopmd(String HospitalID, String DepartmentID, String DoctorID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID><DepartmentID>%s</DepartmentID><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {HospitalID, DepartmentID, DoctorID});
        paramMap.put("str", str);


        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DoctorSchemaByTopmd, Constants.DoctorSchemaByTopmd,
                SOAP_URL, paramMap);

    }

    public void UserContactorInsert(String userid, String UserName, String UserNO, String UserAge, String UserSex, String UserMobile, String UserEmail, JsonAsyncTask_ECGInfo task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><UserName>%s</UserName><UserNO>%s</UserNO><UserAge>%s</UserAge><UserSex>%s</UserSex><UserMobile>%s</UserMobile><UserEmail>%s</UserEmail></Request>";
        str = String.format(str, new Object[]
                {userid, UserName, UserNO, UserAge, UserSex, UserMobile, UserEmail});
        paramMap.put("str", str);

        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.UserContactorInsert, Constants.UserContactorInsert,

                Constants.URL003, paramMap);
    }

    public void UserContactorInquiryByTopmd(String userid,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserContactorInquiryByTopmd, Constants.UserContactorInquiryByTopmd,
                SOAP_URL, paramMap);
    }


    public void GuahaoOrderInsert(String userid, String HospitalID, String DepartmentID, String DepartmentName, String DoctorName, String SchemaID, String DoctorID, String UserContactorID, String SchemaWeek, String NoonName, String BeginTime, String EndTime, String RegistType, String RMB, String Gotime,
                                  String RegistId,JsonAsyncTask_ECGInfo task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID>" +
                "<HospitalID>%s</HospitalID><DepartmentID>%s</DepartmentID><DepartmentName>%s</DepartmentName>" +
                "<DoctorName>%s</DoctorName><SchemaID>%s</SchemaID><DoctorID>%s</DoctorID><UserContactorID>%s</UserContactorID>" +
                "<SchemaWeek>%s</SchemaWeek><NoonName>%s</NoonName>" +
                "<BeginTime>%s</BeginTime><EndTime>%s</EndTime><RegistType>%s</RegistType>" +
                "<RMB>%s</RMB><Gotime>%s</Gotime><RegistId>%s</RegistId></Request>";
        str = String.format(str, new Object[]
                {userid,HospitalID,DepartmentID,DepartmentName,DoctorName,SchemaID,DoctorID,UserContactorID,SchemaWeek,NoonName,BeginTime,EndTime,RegistType,RMB,Gotime,RegistId});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.GuahaoOrderInsert, Constants.GuahaoOrderInsert,
                Constants.URL003, paramMap);
    }
    public void getToken(String userid, String nickname,String head,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><userId>%s</userId><name>%s</name><portraitUri>%s</portraitUri></Request>";
        str = String.format(str, new Object[]
                {userid,nickname,head});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.GetToken, Constants.GetToken,
                SOAP_URL, paramMap);
    }
    public void UserRefresh(String userid, String nickname,String head,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><userId>%s</userId><name>%s</name><portraitUri>%s</portraitUri></Request>";
        str = String.format(str, new Object[]
                {userid,nickname,head});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserRefresh, Constants.UserRefresh,
                SOAP_URL, paramMap);
    }
    public void PharmacyInquiry(String PageSize,String PageIndex,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {PageSize,PageIndex});
        paramMap.put("str", str);

        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.PharmacyInquiry, Constants.PharmacyInquiry,
                SOAP_URL, paramMap);

    }
    public void PharmacyDetailInquiry(String ID,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ID>%s</ID></Request>";
        str = String.format(str, new Object[]
                {ID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.PharmacyDetailInquiry, Constants.PharmacyDetailInquiry,
                SOAP_URL, paramMap);

    }
    public void TitleInquiry(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TitleInquiry, Constants.TitleInquiry,
                SOAP_URL, paramMap);

    }
    public void DiseaseInquiryForDoctor(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DiseaseInquiryForDoctor, Constants.DiseaseInquiryForDoctor,
                SOAP_URL, paramMap);

    }
    public void HospitalInquiryPMD(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HospitalInquiryPMD, Constants.HospitalInquiryPMD,
                SOAP_URL, paramMap);

    }
    public void CommonDiseaseInquiry(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.CommonDiseaseInquiry, Constants.CommonDiseaseInquiry,
                SOAP_URL, paramMap);

    }

    public void MSDoctorInquiry(String HospitalID,String Title,String GoodAt,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID><Title>%s</Title><GoodAt>%s</GoodAt></Request>";
        str = String.format(str, new Object[]
                {HospitalID,Title,GoodAt});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorInquiry, Constants.MSDoctorInquiry,
                SOAP_URL, paramMap);
    }

    public void MSDoctorDetailInquiry(String DoctorID,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {DoctorID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorDetailInquiry, Constants.MSDoctorDetailInquiry,
                SOAP_URL, paramMap);
    }
    public void MSDoctorDetailInquiry(String DoctorID,String usrid,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {DoctorID,usrid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorDetailInquiry, Constants.MSDoctorDetailInquiry,
                SOAP_URL, paramMap);
    }
    public void MSDoctorSignInsert(String DoctorID,String usrid,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {DoctorID,usrid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorSignInsert, Constants.MSDoctorSignInsert,
                SOAP_URL, paramMap);
    }
    public void MSDoctorSignDelete(String DoctorID,String usrid,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {DoctorID,usrid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorSignDelete, Constants.MSDoctorSignDelete,
                SOAP_URL, paramMap);
    }


    public void MSDoctorSignInquiry(String UserID,String PageSize, String PageIndex, String HospitalID,String Title,String GoodAt,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PageSize>%s</PageSize><PageIndex>%s</PageIndex><HospitalID>%s</HospitalID><Title>%s</Title><GoodAt>%s</GoodAt></Request>";
        str = String.format(str, new Object[]
                {UserID,PageSize,PageIndex,HospitalID,Title,GoodAt});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorSignInquiry, Constants.MSDoctorSignInquiry,
                SOAP_URL, paramMap);
    }

    public void MSDoctorDiagnoseInsert(String DoctorID,String usrid,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {DoctorID,usrid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorDiagnoseInsert, Constants.MSDoctorDiagnoseInsert,
                SOAP_URL, paramMap);
    }
    public void OCRForAndroid(String usrid,String code,String FileName
                              ,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Code>%s</Code><FileName>%s</FileName></Request>";
        str = String.format(str, new Object[]
                {usrid,code,FileName});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.OCRForAndroid, Constants.OCRForAndroid,
                SOAP_URL, paramMap);
    }
    public void OrderInquiryByTopmd(String usrid,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {usrid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.OrderInquiryByTopmd, Constants.OrderInquiryByTopmd,
                SOAP_URL, paramMap);
    }
    public void OrderCancel(String orderid, JsonAsyncTask_ECGInfo task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><OrderID>%s</OrderID></Request>";
        str = String.format(str, new Object[]
                {orderid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.OrderCancel, Constants.OrderCancel,
                Constants.URL003, paramMap);
    }
    public void TreatmentInquiryLatest(String UserID,String Date,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Date>%s</Date></Request>";
        str = String.format(str, new Object[]
                {UserID,Date});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TreatmentInquiryLatest, Constants.TreatmentInquiryLatest,
                SOAP_URL, paramMap);
    }
    public void RemindInsert(String UserID,String Time,String Type,String Weekday,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Time>%s</Time><Type>%s</Type><Weekday>%s</Weekday></Request>";
        str = String.format(str, new Object[]
                {UserID,Time,Type,Weekday});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RemindInsert, Constants.RemindInsert,
                SOAP_URL, paramMap);
    }
    public void RemindInquiry(String UserID,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {UserID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RemindInquiry, Constants.RemindInquiry,
                SOAP_URL, paramMap);
    }
    public void RemindUpdate(String ID,String Enabled ,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ID>%s</ID><Enabled>%s</Enabled></Request>";
        str = String.format(str, new Object[]
                {ID,Enabled});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RemindUpdate, Constants.RemindUpdate,
                SOAP_URL, paramMap);
    }
    public void RemindDelete(String id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ID>%s</ID></Request>";
        str = String.format(str, new Object[]
                {id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RemindDelete, Constants.RemindDelete,
                SOAP_URL, paramMap);
    }
    /**
     * ocr
     *

     */
    public void BloodRoutineInsert(String userid, String CardNO, String MonitorTime, String URL, String ocrtype,List<OcrBean> list, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String items = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                items += "<BloodRoutine>" + "<ItemName>" + list.get(i).getCHK_ItemName() + "</ItemName>" + "<ItemValue>" + list.get(i).getNum() + "</ItemValue>" + "</BloodRoutine>";
            }
        }
        String str = "<Request><UserID>%s</UserID><CardNO>%s</CardNO><MonitorTime>%s</MonitorTime>" +
                "<URL>%s</URL>" +
                "<Fromto>%s</Fromto><OCRType>%s</OCRType><BloodRoutines>%s</BloodRoutines></Request>";
        str = String.format(str, new Object[]
                {userid, CardNO, MonitorTime, URL, "02",ocrtype, items});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodRoutineInsert, Constants.BloodRoutineInsert,
                SOAP_URL, paramMap);
    }
    public void OCRRecordInquiry(String CardNO,String PageIndex,String PageSize,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
        str = String.format(str, new Object[]
                {CardNO,PageIndex,PageSize});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.OCRRecordInquiry, Constants.OCRRecordInquiry,
                SOAP_URL, paramMap);
    }
    public void BloodRoutineInquiry (String Type,String No,String id,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><OCRType>%s</OCRType><CardNO>%s</CardNO><RecordID>%s</RecordID><Fromto>%s</Fromto></Request>";
        str = String.format(str, new Object[]
                {Type,No,id,"02"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodRoutineInquiry, Constants.BloodRoutineInquiry,
                SOAP_URL, paramMap);
    }
   /* APPLogInquiry
    参数是：UserID，Log_Type（默认值：01），PageIndex，PageSize*/
   public void APPLogInquiry (String UserID,String Log_Type,String PageIndex,String PageSize,JsonAsyncTask_Info task) {
       Map<String, Object> paramMap = new HashMap<String, Object>();
       String str = "<Request><UserID>%s</UserID><Log_Type>%s</Log_Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
       str = String.format(str, new Object[]
               {UserID,Log_Type,PageIndex,PageSize});
       paramMap.put("str", str);
       task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.APPLogInquiry, Constants.APPLogInquiry,
               SOAP_URL, paramMap);
   }
    public void MSDoctorConsultationTime  (String id, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MSDoctorConsultationTime  , Constants.MSDoctorConsultationTime  ,
                SOAP_URL, paramMap);
    }
    public void LithicAcidInquiry(String CardNO,String StartTime,String EndTime,String Type,String Top,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type><Top>%s</Top></Request>";
        str = String.format(str, new Object[]
                {CardNO,StartTime,EndTime,Type,Top});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.LithicAcidInquiry, Constants.LithicAcidInquiry,
                SOAP_URL, paramMap);
    }
    public void CholestenoneInquiry(String CardNO,String StartTime,String EndTime,String Type,String Top,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><CardNO>%s</CardNO><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type><Top>%s</Top></Request>";
        str = String.format(str, new Object[]
                {CardNO,StartTime,EndTime,Type,Top});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.CholestenoneInquiry, Constants.CholestenoneInquiry,
                SOAP_URL, paramMap);
    }
    public void OCRTypeInquiry(String HospitalID,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID></Request>";
        str = String.format(str, new Object[]
                {HospitalID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.OCRTypeInquiry, Constants.OCRTypeInquiry,
                SOAP_URL, paramMap);
    }
    public void RelationshipInquiry(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RelationshipInquiry, Constants.RelationshipInquiry,
                SOAP_URL, paramMap);
    }




}
