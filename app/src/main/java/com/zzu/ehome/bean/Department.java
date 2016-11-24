package com.zzu.ehome.bean;

/**
 * Created by Administrator on 2016/8/19.
 */
public class Department {
    String DepartmentName;
    String DepartmentID;
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
