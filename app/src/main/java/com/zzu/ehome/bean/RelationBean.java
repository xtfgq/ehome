package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class RelationBean {
    @SerializedName("UserRelationshipInquiry")
    List<RelationDes> data;

    public List<RelationDes> getData() {
        return data;
    }
}
