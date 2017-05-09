package com.zzu.ehome.bean;

import static com.zzu.ehome.application.Constants.groupId;

/**
 * Created by guoqiang on 2017/3/31.
 */

public class StreamInfo {
//    "bandwidth": 0,
//            "client_ip": "125.46.217.127",
//            "flr": 1,
//            "fps": 0,
//            "online": 0,
//            "server_ip": "123.138.162.79",
//            "speed": 0,
//            "stream_name": "8575_1eede36861",
//            "time": "2017-03-31 13:38:55"
    String clientip;
    String server_ip;
    String stream_name;
    String time;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    String groupId;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    String imgUrl;

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
