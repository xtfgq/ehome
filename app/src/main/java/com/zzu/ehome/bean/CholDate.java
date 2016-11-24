package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class CholDate {
    @SerializedName("CholestenoneInquiry")
    List<CholRes> data;

    public List<CholRes> getData() {
        return data;
    }
}
