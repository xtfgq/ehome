package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/30.
 */
public class UserContactor {
    //    "UserContactorID": "32003",
//            "UserID": "51064",
//            "UserName": "雨天",
//            "UserNO": "410102198201251510",
//            "UserAge": "23",
//            "UserSex": "01",
//            "UserMobile": "",
//            "UserEmail": "",
//            "IsDefault": "1",
//            "CardType": "",
//            "UserSexName": "男"
    @SerializedName("UserID")
    private String userid;
    @SerializedName("UserName")
    private String UserName;//用户名
    @SerializedName("UserNO")
    private String UserNO;//身份证
    @SerializedName("UserAge")
    private String UserAge;
    @SerializedName("UserSex")
    private String UserSex;
    @SerializedName("UserMobile")
    private String UserMobile;
    @SerializedName("UserContactorID")
    private String UserContactorID;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserNO() {
        return UserNO;
    }

    public void setUserNO(String userNO) {
        UserNO = userNO;
    }

    public String getUserAge() {
        return UserAge;
    }

    public void setUserAge(String userAge) {
        UserAge = userAge;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String userSex) {
        UserSex = userSex;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getUserContactorID() {
        return UserContactorID;
    }

    public void setUserContactorID(String userContactorID) {
        UserContactorID = userContactorID;
    }
}
