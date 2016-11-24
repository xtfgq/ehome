package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyRemindDate {
    @SerializedName("RemindInquiry")
    List<MyRemindBean> data;

    public List<MyRemindBean> getData() {
        return data;
    }
}
