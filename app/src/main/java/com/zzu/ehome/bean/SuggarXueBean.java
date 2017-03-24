package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/7.
 */

public class SuggarXueBean {
//    "MonitorTime": "2016/10/21 13:00:00",
//            "ItemName": "GLU",
//            "ItemValue": "14.4",
//            "CHK_ItemName_Z": "血清葡萄糖",
//            "HoursAfterMeal": "2",
//            "Type": "1"
    @SerializedName("MonitorTime")
    private String MonitorTime;
    @SerializedName("ItemValue")
    private String ItemValue;
    @SerializedName("HoursAfterMeal")
    private String HoursAfterMeal;
    @SerializedName("Type")
    private String Type;
    @SerializedName("Fromto")
    private String FromTo;
    public String getFromTo() {
        return FromTo;
    }

    public void setFromTo(String fromTo) {
        FromTo = fromTo;
    }


    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
    }

    public String getHoursAfterMeal() {
        return HoursAfterMeal;
    }

    public void setHoursAfterMeal(String hoursAfterMeal) {
        HoursAfterMeal = hoursAfterMeal;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
