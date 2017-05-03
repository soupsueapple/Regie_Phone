package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/7/17.
 */
public class Apps implements Serializable {


    /**
     * id : 1
     * apkName : Marketing.apk
     * name : 营销APK
     * apkUrl : http://192.168.9.125/mg/apkManage!download.action?apkName=Marketing.apk
     * apkEntry : com.keer.marketing.MainActivity.MainActivity
     * apkVer : 1.0
     * createdDate : 2015-07-22 13:49:08
     * apkSize : 1053195
     * modifiedDate : 2015-07-22 16:29:56
     * apkApps : null
     * version : 7
     */
    private int id;
    private String apkName;
    private String name;
    private String apkUrl;
    private String apkEntry;
    private String apkVer;
    private String createdDate;
    private int apkSize;
    private String modifiedDate;
    private int version;

    public Apps(int id, String apkName, String name, String apkUrl, String apkEntry, String apkVer, String createdDate, int apkSize, String modifiedDate, int version) {
        this.id = id;
        this.apkName = apkName;
        this.name = name;
        this.apkUrl = apkUrl;
        this.apkEntry = apkEntry;
        this.apkVer = apkVer;
        this.createdDate = createdDate;
        this.apkSize = apkSize;
        this.modifiedDate = modifiedDate;
        this.version = version;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public void setApkEntry(String apkEntry) {
        this.apkEntry = apkEntry;
    }

    public void setApkVer(String apkVer) {
        this.apkVer = apkVer;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setApkSize(int apkSize) {
        this.apkSize = apkSize;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public String getApkName() {
        return apkName;
    }

    public String getName() {
        return name;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public String getApkEntry() {
        return apkEntry;
    }

    public String getApkVer() {
        return apkVer;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getApkSize() {
        return apkSize;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public int getVersion() {
        return version;
    }
}
