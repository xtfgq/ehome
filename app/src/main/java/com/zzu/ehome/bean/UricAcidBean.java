package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mersens on 2017/3/7 11:36
 * Email:626168564@qq.com
 */

public class UricAcidBean {
    @SerializedName("CardNO")
    private String CardNO;
    @SerializedName("MonitorTime")
    private String MonitorTime;
    @SerializedName("ItemName")
    private String ItemName;
    @SerializedName("ItemValue")
    private String ItemValue;
    @SerializedName("CHK_ItemName_Z")
    private String CHK_ItemName_Z;
    @SerializedName("HoursAfterMeal")
    private String HoursAfterMeal;
    @SerializedName("Fromto")
    private String FromTo;
    public String getFromTo() {
        return FromTo;
    }

    public void setFromTo(String fromTo) {
        FromTo = fromTo;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCardNO() {
        return CardNO;
    }

    public void setCardNO(String cardNO) {
        CardNO = cardNO;
    }

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
    }

    public String getCHK_ItemName_Z() {
        return CHK_ItemName_Z;
    }

    public void setCHK_ItemName_Z(String CHK_ItemName_Z) {
        this.CHK_ItemName_Z = CHK_ItemName_Z;
    }

    public String getHoursAfterMeal() {
        return HoursAfterMeal;
    }

    public void setHoursAfterMeal(String hoursAfterMeal) {
        HoursAfterMeal = hoursAfterMeal;
    }

    private String Type;

}
