package com.zzu.ehome.bean;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ResultContent {
    private String OCRType;
    private String rownumber;
    private String OCRTypeName;
    private String CreatedDate;
    private String ID;

    public String getFromto() {
        return Fromto;
    }

    public void setFromto(String fromto) {
        Fromto = fromto;
    }

    private String Fromto;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOCRType() {
        return OCRType;
    }

    public void setOCRType(String OCRType) {
        this.OCRType = OCRType;
    }

    public String getRownumber() {
        return rownumber;
    }

    public void setRownumber(String rownumber) {
        this.rownumber = rownumber;
    }

    public String getOCRTypeName() {
        return OCRTypeName;
    }

    public void setOCRTypeName(String OCRTypeName) {
        this.OCRTypeName = OCRTypeName;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
