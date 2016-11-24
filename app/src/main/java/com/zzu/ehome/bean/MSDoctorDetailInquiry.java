package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MSDoctorDetailInquiry {
    @SerializedName("MSDoctorDetailInquiry")
    List<MSDoctorDetailBean> data;

    public List<MSDoctorDetailBean> getData() {
        return data;
    }
}
