package com.zzu.ehome.bean;

/**
 * Created by Mersens on 2016/9/9.
 */
public class MSDoctorBean {
    /**
     * ImageURL : ~\UpLoadFile\Doctor\2016-09-02/20160902093349.JPG
     * Description : 蔡丙杰，医学硕士，副主任医师。2008年获郑州大学皮肤病与性病专业硕士学位，现任河南省医学会皮肤病与性病分会血管瘤与脉管畸形学组成员。擅长白癜风、银屑病、带状疱疹、皮炎、湿疹、荨麻疹、药疹、过敏性紫癜、痤疮、手足癣、血管炎、天疱疮、类天疱疮、尖锐湿疣、生殖器疱疹、红斑狼疮、皮肌炎、硬皮病、皮肤肿瘤等疾病的诊治。发表学术论文20余篇，获河南省科技厅，卫生厅科技成果奖2项。
     * IsLine : 0
     * GoodCode : 04
     * DoctorName : 蔡丙杰
     * Title : 副主任医师
     * GoodAt : 呼吸疾病
     * HospitalName : 一附院
     * HospitalID : 3
     * Mobile :
     * DoctorID : 1
     * DoctorFavors : 0
     * Department : 皮肤科
     * TitleCode : 03
     * DoctorNo :
     */


    private String ImageURL;
    private String Description;
    private String IsLine;
    private String GoodCode;
    private String DoctorName;
    private String Title;
    private String GoodAt;
    private String HospitalName;
    private String HospitalID;
    private String Mobile;
    private String DoctorID;
    private String DoctorFavors;
    private String Department;
    private String TitleCode;
    private String DoctorNo;
    private String DiagnoseCount;


    public String getSpeciaty() {
        return Speciaty;
    }

    public void setSpeciaty(String speciaty) {
        Speciaty = speciaty;
    }

    private String Speciaty;

    public String getSignCount() {
        return SignCount;
    }

    public void setSignCount(String signCount) {
        SignCount = signCount;
    }

    public String getDiagnoseCount() {
        return DiagnoseCount;
    }

    public void setDiagnoseCount(String diagnoseCount) {
        DiagnoseCount = diagnoseCount;
    }

    private String SignCount;


    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getIsLine() {
        return IsLine;
    }

    public void setIsLine(String IsLine) {
        this.IsLine = IsLine;
    }

    public String getGoodCode() {
        return GoodCode;
    }

    public void setGoodCode(String GoodCode) {
        this.GoodCode = GoodCode;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String DoctorName) {
        this.DoctorName = DoctorName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getGoodAt() {
        return GoodAt;
    }

    public void setGoodAt(String GoodAt) {
        this.GoodAt = GoodAt;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String HospitalName) {
        this.HospitalName = HospitalName;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String HospitalID) {
        this.HospitalID = HospitalID;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    public String getDoctorFavors() {
        return DoctorFavors;
    }

    public void setDoctorFavors(String DoctorFavors) {
        this.DoctorFavors = DoctorFavors;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getTitleCode() {
        return TitleCode;
    }

    public void setTitleCode(String TitleCode) {
        this.TitleCode = TitleCode;
    }

    public String getDoctorNo() {
        return DoctorNo;
    }

    public void setDoctorNo(String DoctorNo) {
        this.DoctorNo = DoctorNo;
    }
}

