package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/19.
 */
public class RelationDes implements Serializable {

    @SerializedName("Relationship")
    private String Relationship;
    @SerializedName("RUserID")
    private String RUserID;
    @SerializedName("User_Name")
    private String User_Name;
    @SerializedName("User_FullName")
    private String User_FullName;
    @SerializedName("User_Icon")
    private String User_Icon;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getRelationship() {
        return Relationship;
    }

    public void setRelationship(String relationship) {
        Relationship = relationship;
    }

    public String getRUserID() {
        return RUserID;
    }

    public void setRUserID(String RUserID) {
        this.RUserID = RUserID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_FullName() {
        return User_FullName;
    }

    public void setUser_FullName(String user_FullName) {
        User_FullName = user_FullName;
    }

    public String getUser_Icon() {
        return User_Icon;
    }

    public void setUser_Icon(String user_Icon) {
        User_Icon = user_Icon;
    }
}
