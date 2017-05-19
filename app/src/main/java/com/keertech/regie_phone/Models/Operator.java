package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/7/28.
 */
public class Operator implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String shopName = "";
    private String shopAddress = "";
    private String liceNo= "";
    private String chargerName= "";
    private String orderPhone= "";
    private String idCard= "";
    private String businessNo= "";
    private String deliverTime= "";
    private String avaiTime= "";
    private String contactphone= "";
    private String support_groups= "";
    private String addressState = "";

    /**国测局 经度 */
    private Double longitude = 0.0;

    /**国测局 纬度 */
    private Double latitude= 0.0;

    /**百度 经度 */
    private Double bd_longitude= 0.0;

    /**百度 纬度 */
    private Double bd_latitude= 0.0;

    private Integer mark = 0;

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getBd_longitude() {
        return bd_longitude;
    }

    public void setBd_longitude(Double bd_longitude) {
        this.bd_longitude = bd_longitude;
    }

    public Double getBd_latitude() {
        return bd_latitude;
    }

    public void setBd_latitude(Double bd_latitude) {
        this.bd_latitude = bd_latitude;
    }

    public String getSupport_groups() {
        return support_groups;
    }

    public void setSupport_groups(String support_groups) {
        this.support_groups = support_groups;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    private Corp corp;
    private Department department;
    private Community community;
    private Post post;
    private BizStatus bizStatus;

    private String customerManager = "";
    private int grade= 0;
    private String punishNum= "";
    private String real_operate_person= "";
    private String birthYear= "";
    private String business_type= "";
    private String addrProvince= "";
    private String addrCity= "";
    private String addrCounty= "";
    private String operating_life= "";
    private String market_type= "";
    private String porpertiy_code= "";
    private String store_nature= "";
    private String practitionerNum= "";

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getLiceNo() {
        return liceNo;
    }

    public void setLiceNo(String liceNo) {
        this.liceNo = liceNo;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getAvaiTime() {
        return avaiTime;
    }

    public void setAvaiTime(String avaiTime) {
        this.avaiTime = avaiTime;
    }

    public Corp getCorp() {
        return corp;
    }

    public void setCorp(Corp corp) {
        this.corp = corp;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public BizStatus getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(BizStatus bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(String customerManager) {
        this.customerManager = customerManager;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPunishNum() {
        return punishNum;
    }

    public void setPunishNum(String punishNum) {
        this.punishNum = punishNum;
    }

    public String getReal_operate_person() {
        return real_operate_person;
    }

    public void setReal_operate_person(String real_operate_person) {
        this.real_operate_person = real_operate_person;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getAddrProvince() {
        return addrProvince;
    }

    public void setAddrProvince(String addrProvince) {
        this.addrProvince = addrProvince;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrCounty() {
        return addrCounty;
    }

    public void setAddrCounty(String addrCounty) {
        this.addrCounty = addrCounty;
    }

    public String getOperating_life() {
        return operating_life;
    }

    public void setOperating_life(String operating_life) {
        this.operating_life = operating_life;
    }

    public String getMarket_type() {
        return market_type;
    }

    public void setMarket_type(String market_type) {
        this.market_type = market_type;
    }

    public String getPorpertiy_code() {
        return porpertiy_code;
    }

    public void setPorpertiy_code(String porpertiy_code) {
        this.porpertiy_code = porpertiy_code;
    }

    public String getStore_nature() {
        return store_nature;
    }

    public void setStore_nature(String store_nature) {
        this.store_nature = store_nature;
    }

    public String getPractitionerNum() {
        return practitionerNum;
    }

    public void setPractitionerNum(String practitionerNum) {
        this.practitionerNum = practitionerNum;
    }
}
