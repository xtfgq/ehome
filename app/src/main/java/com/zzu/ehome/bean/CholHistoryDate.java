package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class CholHistoryDate {
    @SerializedName("HealthDataInquiryWithPage")
    List<CholHistoryBean> data;

    public List<CholHistoryBean> getData() {
        return data;
    }
}
