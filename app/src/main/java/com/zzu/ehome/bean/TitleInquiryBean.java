package com.zzu.ehome.bean;

/**
 * Created by Mersens on 2016/9/2.
 */
public class TitleInquiryBean {
    private String Id;
    private String CommCode;
    private String Code;
    private String Description;



    private String IsOcR;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

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

    private String Value;
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
    public String getIsOcR() {
        return IsOcR;
    }

    public void setIsOcR(String isOcR) {
        IsOcR = isOcR;
    }
}
