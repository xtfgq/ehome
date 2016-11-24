package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class HospitalInquiryByTopmd {
    @SerializedName("HospitalInquiryByTopmd")
    private List<HospitalBean> date;

    public List<HospitalBean> getDate() {
        return date;
    }

    public void setDate(List<HospitalBean> date) {
        this.date = date;
    }
}
