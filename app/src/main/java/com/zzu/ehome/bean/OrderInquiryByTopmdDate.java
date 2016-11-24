package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class OrderInquiryByTopmdDate {
    @SerializedName("OrderInquiryByTopmd")
    List<OrderInquiryByTopmd> data;

    public List<OrderInquiryByTopmd> getData() {
        return data;
    }
}
