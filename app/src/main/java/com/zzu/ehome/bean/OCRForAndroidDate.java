package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class OCRForAndroidDate {
    @SerializedName("OCRForAndroid")
    List<OcrImage> data;

    public List<OcrImage> getData() {
        return data;
    }
}
