package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xtfgq on 2016/4/5
 */
public class RBCDate {
    @SerializedName("BloodRoutineInquiryForLine")
    List<RBCBean> data;

    public List<RBCBean> getData() {
        return data;
    }


}
