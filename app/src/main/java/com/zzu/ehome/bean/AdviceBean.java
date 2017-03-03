package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/12.
 */

public class AdviceBean {


    //    "CardNO": "410102198201251510",
//            "Value1": "38",
//            "Value2": "",
//            "Value3": "0",
//            "MonitorTime": "2016/12/9 14:38:00",
//            "Description": "体温偏高",
//            "Advice": "1.生理性增高如月经前期或剧烈运动，可出现体温升高超过正常0.5℃。2.病理性增高，发热等体温中枢调节功能的障碍性疾病。建议结合临床进一步诊断。",
//            "Type": "Temperature",
//            "DeleteFlag": "0"
@SerializedName("Description")
String Description;
    @SerializedName("Advice")
    String Advice;

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    @SerializedName("MonitorTime")
    String MonitorTime;
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAdvice() {
        return Advice;
    }

    public void setAdvice(String advice) {
        Advice = advice;
    }

}
