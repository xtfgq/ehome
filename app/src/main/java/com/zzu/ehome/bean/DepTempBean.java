package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DepTempBean {

    @SerializedName("DepartmentName")
    String DepartmentName;
    @SerializedName("DepartmentID")
    String DepartmentID;
    @SerializedName("ParentDeprtID")
    String ParentDeprtID;

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getParentDeprtID() {
        return ParentDeprtID;
    }

    public void setParentDeprtID(String parentDeprtID) {
        ParentDeprtID = parentDeprtID;
    }


}
