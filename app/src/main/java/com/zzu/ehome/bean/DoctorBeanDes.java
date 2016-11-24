package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DoctorBeanDes {

    @SerializedName("DoctorName")
    private String DoctorName;
    @SerializedName("DepartmentID")
    private String DepartmentID;
    @SerializedName("PictureURL")
    private String PictureURL;
    @SerializedName("HospitalName")
    private String HospitalName;
    @SerializedName("HolterTypeName")
    private String HolterTypeName;
    @SerializedName("IsFull")
    private String IsFull;
    @SerializedName("DoctorID")
    private String DoctorID;
    @SerializedName("TitleName")
    private String TitleName;

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getPictureURL() {
        return PictureURL;
    }

    public void setPictureURL(String pictureURL) {
        PictureURL = pictureURL;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHolterTypeName() {
        return HolterTypeName;
    }

    public void setHolterTypeName(String holterTypeName) {
        HolterTypeName = holterTypeName;
    }

    public String getIsFull() {
        return IsFull;
    }

    public void setIsFull(String isFull) {
        IsFull = isFull;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public String getTitleName() {
        return TitleName;
    }

    public void setTitleName(String titleName) {
        TitleName = titleName;
    }
}
