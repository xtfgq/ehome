package com.zzu.ehome.application;

import android.os.Environment;


import static com.zzu.ehome.application.MMloveConstants.NewsDetailInquiry;
import static java.lang.annotation.ElementType.PACKAGE;


/**
 * Created by Dell on 2016/3/14.
 */
public class Constants {
    //测试服务器地址
    public static String EhomeURL = "http://ehome.staging.topmd.cn:81";
    public static final String ICON = "http://p2d.staging.topmd.cn/Images/headImg/";
    public static final String URL002Topmd = "http://staging.topmd.cn/android/TopMD.asmx";
    public static final String JE_BASE_URL = "http://staging.topmd.cn/";
    public static final String JE_BASE_URL3 = "http://staging.topmd.cn";
    public static final String Download = "http://file.staging.topmd.cn/upload/";
    public static String PlatformURL = "http://hd.staging.topmd.cn:81";
    //    正式服务器
//        public static String EhomeURL = "https://ehome.topmd.cn";
//    public static final String ICON = "https://p2d.topmd.cn/Images/headImg/";
//    public static final String URL002Topmd = "https://www.topmd.cn/android/TopMD.asmx";
//    public static final String JE_BASE_URL = "https://www.topmd.cn/";
//    public static final String JE_BASE_URL3 = "https://www.topmd.cn";
//    public static final String Download = "https://file.topmd.cn/upload/";
//    public static String PlatformURL = "https://healthdata.topmd.cn";

    //视频直播测试=======
    public static String LiveURL = "http://lucifer5501.eicp.net/home/";
    public static final String AppCheckkey = "7407d5829e270dd92661ffcf06169bcf";
    public static final String Appkey = "1251175924";
    public static final int sdkAppId = 1400028799;

    public static final String UserSig = "eJx10F1PgzAUBuB7fkXTa*OolJV5BwQTpmNuOHXcNLUfo0NYw8q*jP-duC2RG8-tefK*J*fLAQDAl6f8lnG*6RpL7dFICO4BRGhIELz5A8ZoQZmlXisuALuue0eI11fyYHQrKVNWthflk2Hg-k5PaSEbq5W*GiZq3fRTtqKi577-i7Z6dV5OkmWczuJmt87qOfe7h7bC*yDJ520xUssqnD5qdVpX04Cwozfe7XmYluHz5*CtKrtJkS1MvEKlL*WAjxNZRCaK3jdpiD9es9lB5Yv*1VbX19fgEfI9jBGGzrfzA2laWOY_";
    public static final String groupId = "@TGS#aZONJBYES";
    //end 视频直播测试

    //相机常量
    public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1458;
    public static final int SHOW_ALL_PICTURE = 0x14;
    public static final int REQUEST_CALENDAR = 1460;
    public static final int ADDTTIME = 0x16;
    public static final int CROP = 2;
    public static final int CROP_PICTURE = 3;
    public static final String IMGPATH = ACCOUNT_DIR;

    public static final String IMAGE_FILE_NAME = "faceImage.jpeg";
    public static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";


    public static final String URLIMAGE = EhomeURL + "/WebServices/News.aspx?id=";

    //https校验用户名和密码
    public static final String UserName = "netlab";
    public static final String Identify = "n2#4%93142$23Ll";

    public static final String URL001 = "http://tempuri.org/";
    public static final String URL002 = EhomeURL + "/WebServices/EhomeWebservice.asmx";

    public static final String NewsIndexInquiry = "NewsInquiryIndex";
    public static final String DoctorInquiry = "DoctorInquiry";
    //主页小贴士接口
    private final String SOAP_NAMESPACE = Constants.URL001;

    private final String SOAP_NewsIndexInquiryURLMETHODNAME = Constants.NewsIndexInquiry;
    //广告接口
    public static final String Ads = "ADInquiry";
    //登陆接口
    public static final String LOGIN = "UserLogin";
    //名医网头像上传
    public static final String UploadUserPhoto = "UploadUserPhoto";
    //用户注册
    public static final String UserRegister = "UserRegister";
    public static final String SendAuthCode = "SendAuthCode";


    //头像地址 永远不换
    public static final String URL001Topmd = "http://www.topmd.cn/WebServices/";


    //心电报告
    public static final String URL003 = JE_BASE_URL3 + "/TopmdWeiXin.asmx";

    public static final String UserInfoChange = "UserInfoChange";
    public static final String UserInquiry = "UserInquiry";
    public static final String HealthDataSearch = "HealthDataSearch";
    public static final String TemperatureInsert = "TemperatureInsert";
    public static final String WeightInsert = "WeightInsert";
    public static final String BloodPressureInsert = "BloodPressureInsert";

    public static final String BloodSugarInsert = "BloodSugarInsert";

    public static final String HOSPITALINQUIRY = "HospitalInquiry";
    public static final String DEPARTMENTINQUIRY = "DepartmentInquiry";
    public static final String DOCTORINQUIRY = "DoctorInquiry";

    public static final String OtherTreatmentInsert = "OtherTreatmentInsert";

    public static final String SCHEDULEINQUIRY = "ScheduleInquiry";
    public static final String TREATMENTINSERT = "TreatmentInsert";
    public static final String TreatmentInquirywWithPage = "TreatmentInquirywWithPage";
    public static final String FAVORDOCTORINSERT = "FavorDoctorInsert";
    public static final String FAVORDOCTORINQUIRY = "FavorDoctorInquiry";
    public static final String FEEDBACKINSERT = "FeedBackInsert";

    public static final String UserClientIDChange = "UserClientIDChange";
    public static final String HealthDataInquirywWithPage = "HealthDataInquiryWithPage";

    public static final String USERAUTHCHANGE = "UserAuthChange";
    public static final String UserFindPass = "UserFindPass";

    public static final String TemperatureInquiry = "TemperatureInquiry";

    public static final String TREATMENTSEARCH = "TreatmentSearch";

    public static final String TREATMENTCANCEL = "TreatmentCancel";
    public static final String WeightInquiry = "WeightInquiry";

    public static final String FAVORDOCTORDELETE = "FavorDoctorDelete";
    public static final String BloodPressureInquiry = "BloodPressureInquiry";
    public static final String BloodSugarInquiry = "BloodSugarInquiry";
    public static final String DOWNLOADURL = EhomeURL + "/UpLoadFile/VersionDownload/ehome.apk";
    public static final String VersionInquiry = "VersionInquiry";
    public static final String BaseDataInsert = "BaseDataInsert";
    public static final String BASEDATAINQUIRY = "BaseDataInquiry";
    public static final String BASEDATAUPDATE = "BaseDataUpdate";
    public static final String HolterPDFInquiry = "HolterPDFInquiry";
    public static final String BJResultInquiry = "BJResultInquiry";
    public static final String MedicalAgencyInquiry = "MedicalAgencyInquiry";
    public static final String MedicalReportInsert = "MedicalReportInsert";
    public static final String MeidicalReportInquiry = "MeidicalReportInquiry";
    public static final String RegionInquiry = "RegionInquiry";
    public static final String HospitalInquiryByRegion = "HospitalInquiryByRegion";
    public static final String HealthDataSearchByDate = "HealthDataSearchByDate";
    public static final String MedicationRecordInquiry = "MedicationRecordInquiry";
    public static final String MedicationRecordInsert = "MedicationRecordInsert";
    public static final String StepCounterInsert = "StepCounterInsert";
    public static final String StepCounterInquiry = "StepCounterInquiry";
    public static final String WeatherInquiry = "WeatherInquiry";
    public static final String UserRelationshipInsert = "UserRelationshipInsert";
    public static final String NewsInquiry = "NewsInquiry";
    public static final String HospitalInquiryByTopmd = "HospitalInquiryByTopmd";
    public static final String UserRelationshipInquiry = "UserRelationshipInquiry";
    public static final String HospitalDepertByTopmd = "HospitalDepertByTopmd";
    public static final String DepertDoctorByTopmd = "DepertDoctorByTopmd";
    public static final String DoctorSchemaByTopmd = "DoctorSchemaByTopmd";
    public static final String UserContactorInsert = "UserContactorInsert";
    public static final String GetElectrocardio = "GetElectrocardio";
    public static final String UserContactorInquiryByTopmd = "UserContactorInquiryByTopmd";

    public static final String GuahaoOrderInsert = "GuahaoOrderInsert";
    public static final String GetToken = "GetToken";
    public static final String UserRefresh = "UserRefresh";

    public static final String PharmacyInquiry = "PharmacyInquiry";
    public static final String PharmacyDetailInquiry = "PharmacyDetailInquiry";
    public static final String TitleInquiry = "TitleInquiry";
    public static final String DiseaseInquiryForDoctor = "DiseaseInquiryForDoctor";
    public static final String HospitalInquiryPMD = "HospitalInquiryPMD";
    public static final String CommonDiseaseInquiry = "CommonDiseaseInquiry";
    public static final String MSDoctorInquiry = "MSDoctorInquiry";
    public static final String MSDoctorDetailInquiry = "MSDoctorDetailInquiry";
    public static final String MSDoctorSignInsert = "MSDoctorSignInsert";

    public static final String MSDoctorDiagnoseInsert = "MSDoctorDiagnoseInsert";
    public static final String OCRForAndroid = "OCRForAndroid";
    public static final String OrderInquiryByTopmd = "OrderInquiryByTopmd";
    public static final String OrderCancel = "OrderCancel";
    public static final String URLIMAGENEW = EhomeURL + "/WebServices/HealthInfo.aspx?id=";
    public static final String TreatmentInquiryLatest = "TreatmentInquiryLatest";
    //ocr
    public static final String OCR = Constants.EhomeURL + "/OCRForAndroid.aspx";
    public static final String RemindInsert = "RemindInsert";
    public static final String RemindInquiry = "RemindInquiry";
    public static final String RemindUpdate = "RemindUpdate";
    public static final String RemindDelete = "RemindDelete";
    public static final String BloodRoutineInsert = "BloodRoutineInsert";
    public static final String OCRRecordInquiry = "OCRRecordInquiry";
    public static final String BloodRoutineInquiry = "BloodRoutineInquiry";
    public static final String APPLogInquiry = "APPLogInquiry";
    public static final String MSDoctorSignDelete = "MSDoctorSignDelete";
    public static final String MSDoctorConsultationTime = "MSDoctorConsultationTime  ";
    public static final String OCRTypeInquiry = "OCRTypeInquiry";

    //尿酸
    public static final String LithicAcidInquiry = "LithicAcidInquiry";
    //胆固醇
    public static final String CholestenoneInquiry = "CholestenoneInquiry";
    public static final String RelationshipInquiry = "RelationshipInquiry";
    public static final String UserRelationshipExit = "UserRelationshipExit";

    //健康指导
    public static final String HealthAdviceSearchByDate = "HealthAdviceSearchByDate";

    //刷新常量

    public static final String HealthData = "healthdata";

    public static final String ZDWFYUserRecordJudgeInquiry = "ZDWFYUserRecordJudgeInquiry";
    public static final String CheckupInfoInsert = "CheckupInfoInsert";
    public static final String CheckupInfoInquiry = "CheckupInfoInquiry";

    public static final String CategoryInfoInquiry = "CategoryInfoInquiry";
    public static final String MSDoctorSignInquiry = "MSDoctorSignInquiry";

    public static final String BiochemistryInquiryForLine = "BiochemistryInquiryForLine";
    public static final String BloodRoutineInquiryForLine = "BloodRoutineInquiryForLine";
    public static final String NewsDetailInquiry = "NewsDetailInquiry";

    public static final String GetUserSig = "GetUserSig";
    public static final String ZDWFYRecordInfoInquiry = "ZDWFYRecordInfoInquiry";

    public static final String imageUrl = "shotapi";
    public static final int TEXT_TYPE = 0;


    public static final int IS_ALREADY_MEMBER = 10013;

    public static final int IS_ALREADY_IN_ROOM = 10025;
    public static final String CMD_KEY = "userAction";
    public static final String CMD_PARAM = "actionParam";
    public static final int AVIMCMD_TEXT = -1;         // 普通的聊天消息
    public static final int AVIMCMD_NONE = AVIMCMD_TEXT + 1;               // 无事件
    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_ENTERLIVE = AVIMCMD_NONE + 1;          // 用户加入直播, Group消息  1
    public static final int AVIMCMD_EXITLIVE = AVIMCMD_ENTERLIVE + 1;         // 用户退出直播, Group消息  2
    public static final int AVIMCMD_PRAISE = AVIMCMD_EXITLIVE + 1;           // 点赞消息, Demo中使用Group消息  3
    public static final int AVIMCMD_HOST_LEAVE = AVIMCMD_PRAISE + 1;         // 主播离开, Group消息 ： 4
    public static final int AVIMCMD_HOST_BACK = AVIMCMD_HOST_LEAVE + 1;         // 主播回来, Demo中使用Group消息 ： 5cons
    private static final String PACKAGE = "com.zzu.ehome.main.ehome";
    public static final String APPLY_CHATROOM = "申请加入";
    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE
            + ".ACTION_CLOSE_CONTEXT_COMPLETE";

}
