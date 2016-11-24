package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/13.
 */
public class OrderInquiryByTopmd {
    @SerializedName("HospitalName")
    private String HospitalName;
    @SerializedName("DepartmentName")
    private String DepartmentName;
    @SerializedName("DoctorName")
    private String DoctorName;
    @SerializedName("GoTime")
    private String GoTime;
    @SerializedName("SchemaWeek")
    private String SchemaWeek;
    @SerializedName("BeginTime")
    private String BeginTime;
    @SerializedName("EndTime")
    private String EndTime;
    @SerializedName("OrderID")
    private String OrderID;
    @SerializedName("OrderStatus")
    private String OrderStatus;
    @SerializedName("PictureURL")
    private String PictureURL;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getGoTime() {
        return GoTime;
    }

    public void setGoTime(String goTime) {
        GoTime = goTime;
    }

    public String getSchemaWeek() {
        return SchemaWeek;
    }

    public void setSchemaWeek(String schemaWeek) {
        SchemaWeek = schemaWeek;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
    public String getPictureURL() {
        return PictureURL;
    }

    public void setPictureURL(String pictureURL) {
        PictureURL = pictureURL;
    }

    //    "UserName": "育7",
//            "HospitalName": "郑州大学第一附属医院",
//            "DepartmentName": "康复医学科",
//            "DoctorName": "何予工",
//            "GoTime": "2016/9/15 0:00:00",
//            "SchemaWeek": "星期四",
//            "BeginTime": "8:00",
//            "EndTime": "12:00",
//            "OrderStatusName": "成功预约",
//            "OrderStatus": "01",
//            "OrderID": "20160913112516-51064",
//            "HospitalID": "1"





}
