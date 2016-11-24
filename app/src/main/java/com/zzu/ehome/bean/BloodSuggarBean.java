package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodSuggarBean {
    @SerializedName("BloodSugarValue")
    private String BloodSugarValue;
    @SerializedName("CardNO")
    private String CardNO;
    @SerializedName("MonitorTime")
    private String MonitorTime;
    @SerializedName("Type")
    private String Type;
    @SerializedName("HoursAfterMeal")
    private String HoursAfterMeal;

    public String getBloodSugarValue() {
        return BloodSugarValue;
    }

    public void setBloodSugarValue(String bloodSugarValue) {
        BloodSugarValue = bloodSugarValue;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getHoursAfterMeal() {
        return HoursAfterMeal;
    }

    public void setHoursAfterMeal(String hoursAfterMeal) {
        HoursAfterMeal = hoursAfterMeal;
    }
}
