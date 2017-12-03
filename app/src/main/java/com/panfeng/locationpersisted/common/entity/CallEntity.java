package com.panfeng.locationpersisted.common.entity;

/**
 * Created by panfeng on 2017/11/30.
 */

public class CallEntity {


    //Location_x text,Location_y text,call_type integer
    /**
     * 纬度坐标
     */
    private double latitude;
    /**
     * 经度坐标
     */
    private double longgitude;
    //提醒距离
    private int call_range;
    //提醒类型
    private int call_type;

    /**
     * 提醒标题
     */
    private String title;
    /**
     * 提醒内容
     */

    private String content;
    /**
     * 提醒图标（暂时不用）
     */
    private String icon;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLonggitude() {
        return longgitude;
    }

    public void setLonggitude(double longgitude) {
        this.longgitude = longgitude;
    }

    public int getCall_range() {
        return call_range;
    }

    public void setCall_range(int call_range) {
        this.call_range = call_range;
    }

    public int getCall_type() {
        return call_type;
    }

    public void setCall_type(int call_type) {
        this.call_type = call_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
