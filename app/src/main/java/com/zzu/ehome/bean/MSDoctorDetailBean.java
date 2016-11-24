package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MSDoctorDetailBean {
    //    "ImageURL": "~\\UpLoadFile\\Doctor\\2016-09-02/20160902104035.JPG",
//            "Description": "白蓉，女，主任医师、教授，1992年毕业于河南医科大学临床医学系。2000-2001年在复旦大学附属华山医院神经内科进修，师从著名神经科专家吕传真教授，期间参加国家九五重点攻关课题、卫生部自然科学基金资助项目的研究工作。2005年获郑州大学临床医学专业硕士学位。2006年晋升为神经内科专业副主任医师。2012年晋升为主任医师。对运动神经元玻多系统萎缩、中枢神经系统感染及脱髓鞘疾病有深入的认识。对脑卒中、脑外伤、脊髓损伤、共济失调等疾病有系统的治疗、康复方案，效果显著。",
//            "IsLine": "0",
//            "GoodCode": "03",
//            "DoctorName": "白蓉",
//            "Title": "主任医师",
//            "GoodAt": "高血压",
//            "HospitalName": "一附院",
//            "HospitalID": "3",
//            "Mobile": "0371-65328947",
//            "DoctorID": "2",
//            "DoctorFavors": "11",
//            "Department": "神经内科一",
//            "TitleCode": "02",
//            "DoctorNo": ""
    @SerializedName("ImageURL")
    private String ImageURL;
    @SerializedName("Description")
    private String Description;
    @SerializedName("IsLine")
    private String IsLine;
    @SerializedName("GoodCode")
    private String GoodCode;
    @SerializedName("DoctorName")
    private String DoctorName;
    @SerializedName("Title")
    private String Title;
    @SerializedName("GoodAt")
    private String GoodAt;
    @SerializedName("HospitalName")
    private String HospitalName;
    @SerializedName("HospitalID")
    private String HospitalID;
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("DoctorID")
    private String DoctorID;
    @SerializedName("SignCount")
    private String SignCount;
    @SerializedName("Department")
    private String Department;
    @SerializedName("TitleCode")
    private String TitleCode;
    @SerializedName("DoctorNo")
    private String DoctorNo;
    @SerializedName("Speciaty")
    private String Speciaty;
    @SerializedName("DiagnoseCount")
    private String DiagnoseCount;
    @SerializedName("IsSign")
    private String IsSign;
    @SerializedName("ApplyTo")
    private String ApplyTo;

    public String getDoctorOLexplain() {
        return DoctorOLexplain;
    }

    public void setDoctorOLexplain(String doctorOLexplain) {
        DoctorOLexplain = doctorOLexplain;
    }

    @SerializedName("DoctorOLexplain")
    private String DoctorOLexplain;

//    @SerializedName("StartOLTime")
//    private String StartOLTime;
//    @SerializedName("EndOLTime")
//    private String EndOLTime;


//    public String getStartOLTime() {
//        return StartOLTime;
//    }
//
//    public void setStartOLTime(String startOLTime) {
//        StartOLTime = startOLTime;
//    }
//
//    public String getEndOLTime() {
//        return EndOLTime;
//    }
//
//    public void setEndOLTime(String endOLTime) {
//        EndOLTime = endOLTime;
//    }

    public String getApplyTo() {
        return ApplyTo;
    }

    public void setApplyTo(String applyTo) {
        ApplyTo = applyTo;
    }



    public String getIsSign() {
        return IsSign;
    }

    public void setIsSign(String isSign) {
        IsSign = isSign;
    }
    public String getDiagnoseCount() {
        return DiagnoseCount;
    }

    public void setDiagnoseCount(String diagnoseCount) {
        DiagnoseCount = diagnoseCount;
    }



    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIsLine() {
        return IsLine;
    }

    public void setIsLine(String isLine) {
        IsLine = isLine;
    }

    public String getGoodCode() {
        return GoodCode;
    }

    public void setGoodCode(String goodCode) {
        GoodCode = goodCode;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getGoodAt() {
        return GoodAt;
    }

    public void setGoodAt(String goodAt) {
        GoodAt = goodAt;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public String getSignCount() {
        return SignCount;
    }

    public void setSignCount(String signCount) {
        SignCount = signCount;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getTitleCode() {
        return TitleCode;
    }

    public void setTitleCode(String titleCode) {
        TitleCode = titleCode;
    }

    public String getDoctorNo() {
        return DoctorNo;
    }

    public void setDoctorNo(String doctorNo) {
        DoctorNo = doctorNo;
    }

    public String getSpeciaty() {
        return Speciaty;
    }

    public void setSpeciaty(String speciaty) {
        Speciaty = speciaty;
    }
}
