package com.zzu.ehome.bean;

/**
 * Created by Administrator on 2016/9/22.
 */
public class BloodRoutine {
    private String CHK_ItemName_Z;
    private String MonitorTime;
    private String ItemUnit;
    private String ItemRange;

    private String BioType;
    private String BioTypeName;

    public String getBioType() {
        return BioType;
    }

    public void setBioType(String bioType) {
        BioType = bioType;
    }

    public String getBioTypeName() {
        return BioTypeName;
    }

    public void setBioTypeName(String bioTypeName) {
        BioTypeName = bioTypeName;
    }


    private String ItemValue;

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
    }

    public String getCHK_ItemName_Z() {
        return CHK_ItemName_Z;
    }

    public void setCHK_ItemName_Z(String CHK_ItemName_Z) {
        this.CHK_ItemName_Z = CHK_ItemName_Z;
    }

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(String itemUnit) {
        ItemUnit = itemUnit;
    }

    public String getItemRange() {
        return ItemRange;
    }

    public void setItemRange(String itemRange) {
        ItemRange = itemRange;
    }
}
