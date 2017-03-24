package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/22.
 */
public class StaticBean {

    @SerializedName("CardNO")
    private String CardNO;
    @SerializedName("ReportURL")
    private String ReportURL;
    @SerializedName("ECGResult")
    private String ECGResult;
    @SerializedName("RealName")
    private String RealName;

    @SerializedName("ReportTime")
    private String ReportTime;

    public String getCardNO() {
        return CardNO;
    }

    public void setCardNO(String cardNO) {
        CardNO = cardNO;
    }

    public String getReportURL() {
        return ReportURL;
    }

    public void setReportURL(String reportURL) {
        ReportURL = reportURL;
    }

    public String getECGResult() {
        return ECGResult;
    }

    public void setECGResult(String ECGResult) {
        this.ECGResult = ECGResult;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getReportTime() {
        return ReportTime;
    }

    public void setReportTime(String reportTime) {
        ReportTime = reportTime;
    }
}
