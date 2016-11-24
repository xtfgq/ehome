package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class DoctorSchemaByTopmdDate {
    @SerializedName("DoctorSchemaByTopmd")
    List<DoctorSchemaByTopmdBean> data;

    public List<DoctorSchemaByTopmdBean> getData() {
        return data;
    }
}
