package com.zzu.ehome.bean;

/**
 * Created by Administrator on 2017/2/15.
 */

public class CityBean {
    String title;

    String id;
    String ParentID;
    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
