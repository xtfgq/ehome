package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class SuggarXueDate {
    @SerializedName("BiochemistryInquiryForLine")
    List<SuggarXueBean> data;

    public List<SuggarXueBean> getData() {
        return data;
    }
}
