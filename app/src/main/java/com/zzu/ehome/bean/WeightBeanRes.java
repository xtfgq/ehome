package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/20.
 */

public class WeightBeanRes {
    @SerializedName("rownumber")
    String rownumber;
    @SerializedName("CardNO")
    String CardNo;
    @SerializedName("Weight")
    String Weight;
    @SerializedName("BMI")
    String BMI;
    @SerializedName("Height")
    String Height;
    @SerializedName("MonitorTime")
    String MonitorTime;

    @SerializedName("DeleteFlag")
    String DeleteFlag;
    public String getRownumber() {
        return rownumber;
    }

    public void setRownumber(String rownumber) {
        this.rownumber = rownumber;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        DeleteFlag = deleteFlag;
    }

    //    "rownumber": "6",
//            "ID": "21",
//            "CardNO": "410102198201251513",
//            "Weight": "51.2",
//            "Height": "196",
//            "BMI": "13.32778",
//            "MonitorTime": "2016/10/19 10:14:00",
//            "Fromto": "",
//            "CreatedBy": "",
//            "CreatedDate": "2016/10/19 10:14:44",
//            "UpdatedBy": "",
//            "UpdatedDate": "2016/10/19 10:14:44",
//            "DeleteFlag": "0"

}
