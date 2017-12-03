package com.panfeng.locationpersisted.common.entity;

import java.util.Date;

/**
 * Created by panfeng on 2017/11/30.
 */

public class LocationPersistedEntity {
    /**
     * 详细地址
     */
    private String address;
    /**
     * 纬度坐标
     */
    private double latitude;
    /**
     * 经度坐标
     */
    private double longgitude;
    /**
     * 高度
     */
    private double altitude;
    /**
     * 定位所在城市
     */
    private String city;
    /**
     * 国家
     */
    private String country;
    /**
     * 区县
     */
    private String district;
    /**
     * 省份
     */
    private String province;
    /**
     * 街道信息
     */
    private String street;
    /**
     * 门牌号
     */
    private String streetNumber;

    /**
     * 运营商信息
     */

    private String operators;
    /**
     * 速度
     */

    private float speed;



    //时间
    private Date time;
    /**
     * 上一次定位的id
     */
    private LocationPersistedEntity last_info;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public LocationPersistedEntity getLast_info() {
        return last_info;
    }

    public void setLast_info(LocationPersistedEntity last_info) {
        this.last_info = last_info;
    }
}
