package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/10.
 */
public class TempRes {
    //    {
//        "height": "",
//            "weight": "42",
//            "UpdatedDate": "2016/5/10 17:27:09",
//            "DeleteFlag": "0",
//            "CreatedDate": "2016/5/10 17:27:09",
//            "CreatedBy": "",
//            "ID": "291",
//            "UserID": "51064",
//            "MonitorTime": "2016/5/10 14:27:00",
//            "UpdatedBy": ""
//    },


    @SerializedName("rownumber")
    String rownumber;
    @SerializedName("CardNO")
    String CardNo;
    @SerializedName("Value")
    String Value;

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

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
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
}
