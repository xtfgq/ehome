package com.zzu.ehome.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/10/25.
 */

public class CholRes {
    @SerializedName("MonitorTime")
  String MonitorTime;
    @SerializedName("CHOL")
    String CHOL;

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        MonitorTime = monitorTime;
    }

    public String getCHOL() {
        return CHOL;
    }

    public void setCHOL(String CHOL) {
        this.CHOL = CHOL;
    }
}
