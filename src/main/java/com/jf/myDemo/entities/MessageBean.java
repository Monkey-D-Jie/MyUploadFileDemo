package com.jf.myDemo.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-07-04 10:56
 * @Description: 自定义的消息类
 * To change this template use File | Settings | File and Templates.
 */

public class MessageBean implements Serializable {
    //发送者
    public Long from;
    //发送者名称
    public String fromName;
    //接收者
    public Long to;
    //发送的文本
    public String text;
    //发送日期
    public Date date;

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "from=" + from +
                ", fromName='" + fromName + '\'' +
                ", to=" + to +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
