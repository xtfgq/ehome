package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/16.
 */

public class RelationshipInBean {
    @SerializedName("Code")
    private String Code;
    @SerializedName("Value")
    private String Value;
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    //    "CommCode": "05",
//            "Code": "01",
//            "Description": "",
//            "Value": "父亲"


}
