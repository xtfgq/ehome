package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OcrImage {


    @SerializedName("ImgUrl")
    private String ImgUrl;
    @SerializedName("ResultContent")
    List<OcrBean> data;

    public List<OcrBean> getData() {
        return data;
    }
    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public void setData(List<OcrBean> data) {
        this.data = data;
    }

}
