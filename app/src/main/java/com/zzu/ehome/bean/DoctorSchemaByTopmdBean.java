package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/**
 * Created by Administrator on 2016/8/23.
 */
public class DoctorSchemaByTopmdBean implements Serializable{

    @SerializedName("NoonName")
    private String NoonName;
    @SerializedName("RealName")
    private String RealName;
    @SerializedName("HospitalName")
    private String HospitalName;
    @SerializedName("ScheduleTimeContent")
    private String ScheduleTimeContent;
    @SerializedName("RegistType")
    private String RegistType;
    @SerializedName("SchemaID")
    private String SchemaID;
    @SerializedName("RegistNum")
    private String RegistNum;
    @SerializedName("IsYuyue")
    private String IsYuyue;
    @SerializedName("SchemaDate")
    private String SchemaDate;
    @SerializedName("BeginTime")
    private String BeginTime;
    @SerializedName("EndTime")
    private String EndTime;
    @SerializedName("RMB")
    private String RMB;

    public String getRegistId() {
        return RegistId;
    }

    public void setRegistId(String registId) {
        RegistId = registId;
    }

    @SerializedName("RegistId")
    private String RegistId;


    @SerializedName("SchemaWeek")
    private String SchemaWeek;


    public String getNoonName() {
        return NoonName;
    }

    public void setNoonName(String noonName) {
        NoonName = noonName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getScheduleTimeContent() {
        return ScheduleTimeContent;
    }

    public void setScheduleTimeContent(String scheduleTimeContent) {
        ScheduleTimeContent = scheduleTimeContent;
    }

    public String getRegistType() {
        return RegistType;
    }

    public void setRegistType(String registType) {
        RegistType = registType;
    }

    public String getSchemaID() {
        return SchemaID;
    }

    public void setSchemaID(String schemaID) {
        SchemaID = schemaID;
    }

    public String getRegistNum() {
        return RegistNum;
    }

    public void setRegistNum(String registNum) {
        RegistNum = registNum;
    }

    public String getIsYuyue() {
        return IsYuyue;
    }

    public void setIsYuyue(String isYuyue) {
        IsYuyue = isYuyue;
    }

    public String getSchemaDate() {
        return SchemaDate;
    }

    public void setSchemaDate(String schemaDate) {
        SchemaDate = schemaDate;
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

    public String getRMB() {
        return RMB;
    }

    public void setRMB(String RMB) {
        this.RMB = RMB;
    }
    public String getSchemaWeek() {
        return SchemaWeek;
    }

    public void setSchemaWeek(String schemaWeek) {
        SchemaWeek = schemaWeek;
    }

    //    {
//        "NoonName": "上午",
//            "BeginTime": "8:00",
//            "EndTime": "12:00",
//            "SchemaID": "1178184",
//            "RegistAllNum": "10",
//            "RegistNum": "0",
//            "RegistType": "省级知名专家",
//            "RMB": "20.50",
//            "SchemaDate": "2016-08-25",
//            "SchemaWeek": "星期四",
//            "ScheduleTimeContent": "2016-08-25星期四 8:00--12:00",
//            "SchemaStatu": "1",
//            "RealName": "方树友",
//            "HospitalName": "郑州大学第一附属医院",
//            "IsYuyue": "1",
//            "RegistId": "",
//            "pAppDate": "",
//            "pAppOrg": "",
//            "pNoon": "",
//            "PictureURL": "",
//            "Introduction": ""
//    },

}
