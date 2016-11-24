package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class LithicAcidDate {
    @SerializedName("LithicAcidInquiry")
    List<LithicAcidRes> data;

    public List<LithicAcidRes> getData() {
        return data;
    }
}
