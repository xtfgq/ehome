package com.zzu.ehome.bean;

/**
 * Created by Mersens on 2016/9/1.
 */
public class PharmacyBean {

    private String id;
    private String PharmacyPhone;
    private String PharmacyName;
    private String PharmacyAddress;
    private String YibaoType;
    private String Zhekou;
    private String Latitude;
    private String Longitude;
    private String PicURL;

    public String getPharmacyPicUrl() {
        return PharmacyPicUrl;
    }

    public void setPharmacyPicUrl(String pharmacyPicUrl) {
        PharmacyPicUrl = pharmacyPicUrl;
    }

    private String PharmacyPicUrl;


    public String getPicURL() {
        return PicURL;
    }

    public void setPicURL(String picURL) {
        PicURL = picURL;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPharmacyPhone() {
        return PharmacyPhone;
    }

    public void setPharmacyPhone(String pharmacyPhone) {
        PharmacyPhone = pharmacyPhone;
    }

    public String getPharmacyName() {
        return PharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        PharmacyName = pharmacyName;
    }

    public String getPharmacyAddress() {
        return PharmacyAddress;
    }

    public void setPharmacyAddress(String pharmacyAddress) {
        PharmacyAddress = pharmacyAddress;
    }

    public String getYibaoType() {
        return YibaoType;
    }

    public void setYibaoType(String yibaoType) {
        YibaoType = yibaoType;
    }

    public String getZhekou() {
        return Zhekou;
    }

    public void setZhekou(String zhekou) {
        Zhekou = zhekou;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    @Override
    public String toString() {
        return "PharmacyBean{" +
                "PharmacyPhone='" + PharmacyPhone + '\'' +
                ", PharmacyName='" + PharmacyName + '\'' +
                ", PharmacyAddress='" + PharmacyAddress + '\'' +
                ", YibaoType='" + YibaoType + '\'' +
                ", Zhekou='" + Zhekou + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                '}';
    }

}
