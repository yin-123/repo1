package com.xiaoshu.entity;

public class GroupByType {
    private String name;
    private Integer count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public GroupByType(String name, Integer count) {
        this.name = name;
        this.count = count;
    }

    public GroupByType() {
    }

    @Override
    public String toString() {
        return "GroupByType{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
