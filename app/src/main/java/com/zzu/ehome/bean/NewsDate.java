package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class NewsDate {
    @SerializedName("NewsInquiry")
    List<News> data;

    public List<News> getData() {
        return data;
    }
}
