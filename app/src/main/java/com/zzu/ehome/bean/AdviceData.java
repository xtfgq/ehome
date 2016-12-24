package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class AdviceData {
    @SerializedName("HealthAdviceSearchByDate")
    List<AdviceBean> data;

    public List<AdviceBean> getData() {
        return data;
    }
}
