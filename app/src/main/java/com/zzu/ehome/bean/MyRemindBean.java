package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyRemindBean {
//    "ID": "9",
//            "UserID": "51064",
//            "Date": "",
//            "ClientID": "",
//            "Time": "07:47",
//            "Type": "02",
//            "Weekday": "周二,周三,周四",
//            "Enabled": "1"
    @SerializedName("Time")
    private String Time;
    @SerializedName("Type")
    private String Type;
    @SerializedName("Weekday")
    private String Weekday;
    @SerializedName("Enabled")
    private String Enabled;
    @SerializedName("ID")
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getWeekday() {
        return Weekday;
    }

    public void setWeekday(String weekday) {
        Weekday = weekday;
    }

    public String getEnabled() {
        return Enabled;
    }

    public void setEnabled(String enabled) {
        Enabled = enabled;
    }
}
