package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/7/23.
 */
public class TerraceApp implements Serializable {

    /**
     * username : null
     * appname : 市场监管平台
     * appid : 1
     * code : 1001
     * password : null
     */
    private String username;
    private String appname;
    private int appid;
    private String code;
    private String password;

    public TerraceApp(String username, String appname, int appid, String code, String password) {
        this.username = username;
        this.appname = appname;
        this.appid = appid;
        this.code = code;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getAppname() {
        return appname;
    }

    public int getAppid() {
        return appid;
    }

    public String getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }
}
