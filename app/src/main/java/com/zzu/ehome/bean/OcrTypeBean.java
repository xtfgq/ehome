package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/15.
 */

public class OcrTypeBean implements Serializable {
//    "CommCode": "13",
//            "Code": "01",
//            "Description": "",
//            "Value": "血常规报告单"
@SerializedName("HospitalID")
private String CommCode;
    @SerializedName("OCRType")
    private String Code;
    @SerializedName("ImageURL")
    private String Description;
    @SerializedName("OCRTypeName")
    private String Value;

    public String getCommCode() {
        return CommCode;
    }

    public void setCommCode(String commCode) {
        CommCode = commCode;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
