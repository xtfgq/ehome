package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/18.
 */
public class News {

    @SerializedName("Sort")
    private String Sort;
    @SerializedName("Hits")
    private String Hits;
    @SerializedName("Type")
    private String Type;
    @SerializedName("Zhaiyao")
    private String Zhaiyao;
    @SerializedName("Title")
    private String Title;
    @SerializedName("PicURL")
    private String Pic;
    @SerializedName("ID")
    private String ID;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    @SerializedName("CreatedDate")
    private String CreatedDate;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public String getHits() {
        return Hits;
    }

    public void setHits(String hits) {
        Hits = hits;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getZhaiyao() {
        return Zhaiyao;
    }

    public void setZhaiyao(String zhaiyao) {
        Zhaiyao = zhaiyao;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
