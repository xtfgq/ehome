package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14.
 */
public class OcrBean implements Serializable {
    @SerializedName("CHK_ItemName_Z")
    private String name;
    @SerializedName("ItemValue")
    private String num;
    @SerializedName("ItemUnit")
    private String AToB;
    @SerializedName("ItemRange")
    private String range;
    @SerializedName("CHK_ItemName")
    private String CHK_ItemName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAToB() {
        return AToB;
    }

    public void setAToB(String AToB) {
        this.AToB = AToB;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCHK_ItemName() {
        return CHK_ItemName;
    }

    public void setCHK_ItemName(String CHK_ItemName) {
        this.CHK_ItemName = CHK_ItemName;
    }
}
