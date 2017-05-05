package com.keertech.regie_phone.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by soup on 15/8/5.
 */
public class MarketInspect implements Serializable {
    private Integer id;

    private CustomerInfo customer;

    /**
     * 进店时间
     */
    private Date inDate;

    /**
     * 出店时间
     */
    private Date outDate;

    /**
     * 证件异常信息-未亮证经营（1：是；0：否）
     */
    private Integer isLightCard;

    /**
     * 证件异常信息-人证不相符（1：是；0：否）
     */
    private Integer isRzConform;

    /**
     * 证件异常信息-经营地址异动（1：是；0：否）
     */
    private Integer isAddressChange;

    /**
     * 经营异常信息-违规经营(1：是；0：否 业务规则：如果是就必须填写非法卷烟信息)
     */
    private Integer illegalBusiness;

    private String rzConform = "";

    private String addressChange = "";

    /**
     * 经营异常信息-非法卷烟信息
     */
    private List<MarketInspectCig> cigList;

    /**
     * 照片(存储方式如：x1.jpg;x2.jpg;)
     */
    private String pictures = "";

    /**
     * 百度经度
     */
    private Double longitude_bd;

    /**
     * 百度纬度
     */
    private Double latitude_bd;

    /**
     * GPS经度
     */
    private Double longitude_gps;

    /**
     * GPS纬度
     */
    private Double latitude_gps;

    private String doorPhotos = "";

    private String evidencePhotos = "";

    private int isPropagandaLR = -1;

    private int isIntoCheck = 0;

    public int getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(int checkLevel) {
        this.checkLevel = checkLevel;
    }

    private int checkLevel = 0;

    private String intoCheckReason = "";

    private String apcdFeedback = "";

    private String otherAbnormal = "";

    public String getApcdFeedback() {
        return apcdFeedback;
    }

    public void setApcdFeedback(String apcdFeedback) {
        this.apcdFeedback = apcdFeedback;
    }

    public String getOtherAbnormal() {
        return otherAbnormal;
    }

    public void setOtherAbnormal(String otherAbnormal) {
        this.otherAbnormal = otherAbnormal;
    }

    public int getIsPropagandaLR() {
        return isPropagandaLR;
    }

    public void setIsPropagandaLR(int isPropagandaLR) {
        this.isPropagandaLR = isPropagandaLR;
    }

    public int getIsIntoCheck() {
        return isIntoCheck;
    }

    public void setIsIntoCheck(int isIntoCheck) {
        this.isIntoCheck = isIntoCheck;
    }

    public String getIntoCheckReason() {
        return intoCheckReason;
    }

    public void setIntoCheckReason(String intoCheckReason) {
        this.intoCheckReason = intoCheckReason;
    }

    public String getDoorPhotos() {
        return doorPhotos;
    }

    public void setDoorPhotos(String doorPhotos) {
        this.doorPhotos = doorPhotos;
    }

    public String getEvidencePhotos() {
        return evidencePhotos;
    }

    public void setEvidencePhotos(String evidencePhotos) {
        this.evidencePhotos = evidencePhotos;
    }

    public CustomerInfo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInfo customer) {
        this.customer = customer;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Integer getIsLightCard() {
        return isLightCard;
    }

    public void setIsLightCard(Integer isLightCard) {
        this.isLightCard = isLightCard;
    }

    public Integer getIsRzConform() {
        return isRzConform;
    }

    public void setIsRzConform(Integer isRzConform) {
        this.isRzConform = isRzConform;
    }

    public Integer getIsAddressChange() {
        return isAddressChange;
    }

    public void setIsAddressChange(Integer isAddressChange) {
        this.isAddressChange = isAddressChange;
    }

    public Integer getIllegalBusiness() {
        return illegalBusiness;
    }

    public void setIllegalBusiness(Integer illegalBusiness) {
        this.illegalBusiness = illegalBusiness;
    }

    public List<MarketInspectCig> getCigList() {
        return cigList;
    }

    public void setCigList(List<MarketInspectCig> cigList) {
        this.cigList = cigList;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Double getLongitude_bd() {
        return longitude_bd;
    }

    public void setLongitude_bd(Double longitude_bd) {
        this.longitude_bd = longitude_bd;
    }

    public Double getLatitude_bd() {
        return latitude_bd;
    }

    public void setLatitude_bd(Double latitude_bd) {
        this.latitude_bd = latitude_bd;
    }

    public Double getLongitude_gps() {
        return longitude_gps;
    }

    public void setLongitude_gps(Double longitude_gps) {
        this.longitude_gps = longitude_gps;
    }

    public Double getLatitude_gps() {
        return latitude_gps;
    }

    public void setLatitude_gps(Double latitude_gps) {
        this.latitude_gps = latitude_gps;
    }

    public String getRzConform() {
        return rzConform;
    }

    public void setRzConform(String rzConform) {
        this.rzConform = rzConform;
    }

    public String getAddressChange() {
        return addressChange;
    }

    public void setAddressChange(String addressChange) {
        this.addressChange = addressChange;
    }
}
