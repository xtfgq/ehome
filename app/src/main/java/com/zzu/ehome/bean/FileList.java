package com.zzu.ehome.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/25.
 */

public class FileList implements Serializable {


    private String filename;
    private Long filelength;
    private Long filesize;

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFilelength() {
        return filelength;
    }

    public void setFilelength(Long filelength) {
        this.filelength = filelength;
    }

}
