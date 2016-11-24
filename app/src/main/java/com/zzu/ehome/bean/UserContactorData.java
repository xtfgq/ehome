package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class UserContactorData {
    @SerializedName("UserContactorInquiryByTopmd")
    List<UserContactor> data;
    public List<UserContactor> getData() {
        return data;
    }

}
