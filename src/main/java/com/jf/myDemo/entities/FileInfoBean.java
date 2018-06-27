package com.jf.myDemo.entities;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 15:35
 * @Description: 文件信息实体类
 * To change this template use File | Settings | File and Templates.
 */

public class FileInfoBean implements Serializable{

    private String id;
    private String fileName;
    private String status;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FileInfoBean{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
