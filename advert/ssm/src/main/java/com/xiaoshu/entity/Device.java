package com.xiaoshu.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.util.Date;

public class Device {
    private Integer id;
    private String name;
    private Integer tid;
    private String nc;
    private String color;
    private Integer price;
    private String statu;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    @Transient
    private Type type;
    @Transient
    private String tname;


    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Device(String name, Integer tid, String nc, String color, Integer price, String statu, Date time) {
        this.name = name;
        this.tid = tid;
        this.nc = nc;
        this.color = color;
        this.price = price;
        this.statu = statu;
        this.time = time;
    }

    public Device() {
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tid=" + tid +
                ", nc='" + nc + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", statu='" + statu + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", tname='" + tname + '\'' +
                '}';
    }
}
