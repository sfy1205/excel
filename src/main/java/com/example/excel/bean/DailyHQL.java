package com.example.excel.bean;

import java.sql.Date;

public class DailyHQL {
    private Long Id;
    private Date UpdateTime;
    private String ZhanName;
    private Float HQL;

    public DailyHQL(Date updateTime, String zhanName, Float HQL) {
        UpdateTime = updateTime;
        ZhanName = zhanName;
        this.HQL = HQL;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Date getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(Date updateTime) {
        UpdateTime = updateTime;
    }

    public String getZhanName() {
        return ZhanName;
    }

    public void setZhanName(String zhanName) {
        ZhanName = zhanName;
    }

    public Float getHQL() {
        return HQL;
    }

    public void setHQL(Float HQL) {
        this.HQL = HQL;
    }

    @Override
    public String toString() {
        return "DailyHQL [Id=" + Id + ", UpdateTime=" + UpdateTime + ", ZhanName=" + ZhanName + ", HQL=" + HQL + "]";
    }
}
