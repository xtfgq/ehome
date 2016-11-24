package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DortorlIst {
    @SerializedName("DepertDoctorByTopmd")
    List<DoctorBeanDes> data;

    public List<DoctorBeanDes> getData() {
        return data;
    }
}
