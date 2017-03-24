package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/7.
 */

public class RBCBean {
//    "MonitorTime": "2016/10/17 10:05:02",
//            "ItemMinValue": "3.5",
//            "ItemUnit": "10^12/L",
//            "ItemMinValue": "3.5",
//            "ItemMaxValue": "5.5"

@SerializedName("MonitorTime")
private String MonitorTime;
    @SerializedName("ItemValue")
    private String ItemValue;
    @SerializedName("ItemMinValue")
    private String ItemMinValue;
    @SerializedName("ItemMaxValue")
    private String ItemMaxValue;
    @SerializedName("ItemRange")
    private String ItemRange;
    @SerializedName("ItemUnit")
    private String ItemUnit;

    public String getCHK_ItemName_Z() {
        return CHK_ItemName_Z;
    }

    public void setCHK_ItemName_Z(String CHK_ItemName_Z) {
        this.CHK_ItemName_Z = CHK_ItemName_Z;
    }

    @SerializedName("CHK_ItemName_Z")
    private String CHK_ItemName_Z;
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

    public String getItemMinValue() {
        return ItemMinValue;
    }

    public void setItemMinValue(String itemMinValue) {
        ItemMinValue = itemMinValue;
    }

    public String getItemMaxValue() {
        return ItemMaxValue;
    }

    public void setItemMaxValue(String itemMaxValue) {
        ItemMaxValue = itemMaxValue;
    }

    public String getItemRange() {
        return ItemRange;
    }

    public void setItemRange(String itemRange) {
        ItemRange = itemRange;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(String itemUnit) {
        ItemUnit = itemUnit;
    }
}
