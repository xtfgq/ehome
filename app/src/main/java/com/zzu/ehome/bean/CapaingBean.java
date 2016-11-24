package com.zzu.ehome.bean;

/**
 * Created by Administrator on 2016/9/24.
 */
public class CapaingBean {
    private String name;
    private String Id;
    private String CreateDate;
    private String Log_ID;
    public String getLog_ID() {
        return Log_ID;
    }

    public void setLog_ID(String log_ID) {
        Log_ID = log_ID;
    }


    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
