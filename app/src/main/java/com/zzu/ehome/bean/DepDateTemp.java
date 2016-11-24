package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DepDateTemp {
    @SerializedName("HospitalDepertByTopmd")
    List<DepTempBean> data;

    public List<DepTempBean> getData() {
        return data;
    }
}
