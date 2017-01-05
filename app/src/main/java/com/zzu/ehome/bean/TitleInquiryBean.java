package com.zzu.ehome.bean;

import java.io.Serializable;

/**
 * Created by Mersens on 2016/9/2.
 */
public class TitleInquiryBean implements Serializable {
    private String Id;
    private String CommCode;
    private String Code;
    private String Description;


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private String img;



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

}
