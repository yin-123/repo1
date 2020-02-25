package com.xiaoshu.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.util.Date;

public class Advert {
    private Integer id;
    private String name;
    private Integer tid;
    private String img;
    private String path;
    private Integer price;
    private String statu;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @Transient
    private Type2 type2;

    public Advert(String name, Integer tid, String img, String path, Integer price, String statu, Date createtime) {
        this.name = name;
        this.tid = tid;
        this.img = img;
        this.path = path;
        this.price = price;
        this.statu = statu;
        this.createtime = createtime;
    }

    public Advert() {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Type2 getType2() {
        return type2;
    }

    public void setType2(Type2 type2) {
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "Advert{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tid=" + tid +
                ", img='" + img + '\'' +
                ", path='" + path + '\'' +
                ", price=" + price +
                ", statu='" + statu + '\'' +
                ", createtime=" + createtime +
                ", type2=" + type2 +
                '}';
    }
}
