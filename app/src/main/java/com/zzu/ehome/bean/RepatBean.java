package com.zzu.ehome.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/18.
 */
public class RepatBean implements Serializable{
    private String naem;
    private Boolean isSelct;

    public String getNaem() {
        return naem;
    }

    public void setNaem(String naem) {
        this.naem = naem;
    }

    public Boolean getSelct() {
        return isSelct;
    }

    public void setSelct(Boolean selct) {
        isSelct = selct;
    }


}
