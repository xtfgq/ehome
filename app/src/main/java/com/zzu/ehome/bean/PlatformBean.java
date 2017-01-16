package com.zzu.ehome.bean;

import static io.rong.imlib.statistics.UserData.name;

/**
 * Created by Administrator on 2017/1/3.
 */

public class PlatformBean {
    String time;
    String name;
    String CHKCODE;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCHKCODE() {
        return CHKCODE;
    }

    public void setCHKCODE(String CHKCODE) {
        this.CHKCODE = CHKCODE;
    }
}
