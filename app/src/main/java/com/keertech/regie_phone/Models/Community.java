package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/7/28.
 */
public class Community implements Serializable {

    /**
     * valid : true
     * modifiedBy : {"position":null,"valid":true,"birthday":null,"phone":null,"post":null,"modifiedBy":null,"department":null,"entryday":null,"serialNumber":"80970","education":null,"password":"123","modifiedDate":"2010-05-18 16:11:48","creator":null,"version":2,"id":80970,"deletedFlag":false,"privilegeMap":null,"username":"80970","admin":false,"name":"周卫东","gender":null,"resigned":false,"createdDate":"2009-09-16 15:33:18"}
     * department : {"valid":true,"modifiedBy":null,"serialNumber":"100074","parent":null,"modifiedDate":"2009-12-18 16:23:11","version":1,"creator":null,"id":100074,"deletedFlag":false,"privilegeMap":null,"principal":null,"name":"洪山徐东管理所","shortName":"徐东管理所","createdDate":"2009-09-16 15:40:03"}
     * street : {"valid":true,"modifiedBy":null,"department":null,"streetName":"东湖风景区","communitySpecialist":null,"corp":null,"modifiedDate":"2009-12-31 15:36:29","version":0,"creator":null,"id":100634,"deletedFlag":false,"privilegeMap":null,"createdDate":"2009-09-16 16:21:34"}
     * modifiedDate : 2010-04-22 14:51:30
     * version : 0
     * creator : {"position":null,"valid":true,"birthday":"2011-08-10 00:00:00","phone":null,"post":null,"modifiedBy":null,"department":null,"entryday":"2011-08-10 00:00:00","serialNumber":"000001","education":null,"password":"228230","modifiedDate":"2011-08-11 20:50:28","creator":null,"version":2,"id":1,"deletedFlag":false,"privilegeMap":null,"username":"admin","admin":true,"name":"管理员","gender":2,"resigned":false,"createdDate":"2011-08-10 00:00:00"}
     * id : 105383
     * deletedFlag : false
     * privilegeMap : null
     * communityName : 东湖村
     * coordinator : null
     * createdDate : 2009-09-16 16:31:42
     */
    private boolean valid;
    private ModifiedByEntity modifiedBy;
    private DepartmentEntity department;
    private StreetEntity street;
    private String modifiedDate;
    private int version;
    private CreatorEntity creator;
    private int id;
    private boolean deletedFlag;
    private String privilegeMap;
    private String communityName;
    private String coordinator;
    private String createdDate;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setModifiedBy(ModifiedByEntity modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public void setStreet(StreetEntity street) {
        this.street = street;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setCreator(CreatorEntity creator) {
        this.creator = creator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeletedFlag(boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public void setPrivilegeMap(String privilegeMap) {
        this.privilegeMap = privilegeMap;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setCoordinator(String coordinator) {
        this.coordinator = coordinator;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isValid() {
        return valid;
    }

    public ModifiedByEntity getModifiedBy() {
        return modifiedBy;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public StreetEntity getStreet() {
        return street;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public int getVersion() {
        return version;
    }

    public CreatorEntity getCreator() {
        return creator;
    }

    public int getId() {
        return id;
    }

    public boolean isDeletedFlag() {
        return deletedFlag;
    }

    public String getPrivilegeMap() {
        return privilegeMap;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public static class ModifiedByEntity implements Serializable {
        /**
         * position : null
         * valid : true
         * birthday : null
         * phone : null
         * post : null
         * modifiedBy : null
         * department : null
         * entryday : null
         * serialNumber : 80970
         * education : null
         * password : 123
         * modifiedDate : 2010-05-18 16:11:48
         * creator : null
         * version : 2
         * id : 80970
         * deletedFlag : false
         * privilegeMap : null
         * username : 80970
         * admin : false
         * name : 周卫东
         * gender : null
         * resigned : false
         * createdDate : 2009-09-16 15:33:18
         */
        private String position;
        private boolean valid;
        private String birthday;
        private String phone;
        private String post;
        private String modifiedBy;
        private String department;
        private String entryday;
        private String serialNumber;
        private String education;
        private String password;
        private String modifiedDate;
        private String creator;
        private int version;
        private int id;
        private boolean deletedFlag;
        private String privilegeMap;
        private String username;
        private boolean admin;
        private String name;
        private String gender;
        private boolean resigned;
        private String createdDate;

        public void setPosition(String position) {
            this.position = position;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setEntryday(String entryday) {
            this.entryday = entryday;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDeletedFlag(boolean deletedFlag) {
            this.deletedFlag = deletedFlag;
        }

        public void setPrivilegeMap(String privilegeMap) {
            this.privilegeMap = privilegeMap;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setResigned(boolean resigned) {
            this.resigned = resigned;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getPosition() {
            return position;
        }

        public boolean isValid() {
            return valid;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getPhone() {
            return phone;
        }

        public String getPost() {
            return post;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public String getDepartment() {
            return department;
        }

        public String getEntryday() {
            return entryday;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public String getEducation() {
            return education;
        }

        public String getPassword() {
            return password;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public String getCreator() {
            return creator;
        }

        public int getVersion() {
            return version;
        }

        public int getId() {
            return id;
        }

        public boolean isDeletedFlag() {
            return deletedFlag;
        }

        public String getPrivilegeMap() {
            return privilegeMap;
        }

        public String getUsername() {
            return username;
        }

        public boolean isAdmin() {
            return admin;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public boolean isResigned() {
            return resigned;
        }

        public String getCreatedDate() {
            return createdDate;
        }
    }

    public static class DepartmentEntity implements Serializable {
        /**
         * valid : true
         * modifiedBy : null
         * serialNumber : 100074
         * parent : null
         * modifiedDate : 2009-12-18 16:23:11
         * version : 1
         * creator : null
         * id : 100074
         * deletedFlag : false
         * privilegeMap : null
         * principal : null
         * name : 洪山徐东管理所
         * shortName : 徐东管理所
         * createdDate : 2009-09-16 15:40:03
         */
        private boolean valid;
        private String modifiedBy;
        private String serialNumber;
        private String parent;
        private String modifiedDate;
        private int version;
        private String creator;
        private int id;
        private boolean deletedFlag;
        private String privilegeMap;
        private String principal;
        private String name;
        private String shortName;
        private String createdDate;

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDeletedFlag(boolean deletedFlag) {
            this.deletedFlag = deletedFlag;
        }

        public void setPrivilegeMap(String privilegeMap) {
            this.privilegeMap = privilegeMap;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public boolean isValid() {
            return valid;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public String getParent() {
            return parent;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public int getVersion() {
            return version;
        }

        public String getCreator() {
            return creator;
        }

        public int getId() {
            return id;
        }

        public boolean isDeletedFlag() {
            return deletedFlag;
        }

        public String getPrivilegeMap() {
            return privilegeMap;
        }

        public String getPrincipal() {
            return principal;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        public String getCreatedDate() {
            return createdDate;
        }
    }

    public static class StreetEntity implements Serializable {
        /**
         * valid : true
         * modifiedBy : null
         * department : null
         * streetName : 东湖风景区
         * communitySpecialist : null
         * corp : null
         * modifiedDate : 2009-12-31 15:36:29
         * version : 0
         * creator : null
         * id : 100634
         * deletedFlag : false
         * privilegeMap : null
         * createdDate : 2009-09-16 16:21:34
         */
        private boolean valid;
        private String modifiedBy;
        private String department;
        private String streetName;
        private String communitySpecialist;
        private String corp;
        private String modifiedDate;
        private int version;
        private String creator;
        private int id;
        private boolean deletedFlag;
        private String privilegeMap;
        private String createdDate;

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public void setCommunitySpecialist(String communitySpecialist) {
            this.communitySpecialist = communitySpecialist;
        }

        public void setCorp(String corp) {
            this.corp = corp;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDeletedFlag(boolean deletedFlag) {
            this.deletedFlag = deletedFlag;
        }

        public void setPrivilegeMap(String privilegeMap) {
            this.privilegeMap = privilegeMap;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public boolean isValid() {
            return valid;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public String getDepartment() {
            return department;
        }

        public String getStreetName() {
            return streetName;
        }

        public String getCommunitySpecialist() {
            return communitySpecialist;
        }

        public String getCorp() {
            return corp;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public int getVersion() {
            return version;
        }

        public String getCreator() {
            return creator;
        }

        public int getId() {
            return id;
        }

        public boolean isDeletedFlag() {
            return deletedFlag;
        }

        public String getPrivilegeMap() {
            return privilegeMap;
        }

        public String getCreatedDate() {
            return createdDate;
        }
    }

    public static class CreatorEntity implements Serializable {
        /**
         * position : null
         * valid : true
         * birthday : 2011-08-10 00:00:00
         * phone : null
         * post : null
         * modifiedBy : null
         * department : null
         * entryday : 2011-08-10 00:00:00
         * serialNumber : 000001
         * education : null
         * password : 228230
         * modifiedDate : 2011-08-11 20:50:28
         * creator : null
         * version : 2
         * id : 1
         * deletedFlag : false
         * privilegeMap : null
         * username : admin
         * admin : true
         * name : 管理员
         * gender : 2
         * resigned : false
         * createdDate : 2011-08-10 00:00:00
         */
        private String position;
        private boolean valid;
        private String birthday;
        private String phone;
        private String post;
        private String modifiedBy;
        private String department;
        private String entryday;
        private String serialNumber;
        private String education;
        private String password;
        private String modifiedDate;
        private String creator;
        private int version;
        private int id;
        private boolean deletedFlag;
        private String privilegeMap;
        private String username;
        private boolean admin;
        private String name;
        private int gender;
        private boolean resigned;
        private String createdDate;

        public void setPosition(String position) {
            this.position = position;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setEntryday(String entryday) {
            this.entryday = entryday;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDeletedFlag(boolean deletedFlag) {
            this.deletedFlag = deletedFlag;
        }

        public void setPrivilegeMap(String privilegeMap) {
            this.privilegeMap = privilegeMap;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setResigned(boolean resigned) {
            this.resigned = resigned;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getPosition() {
            return position;
        }

        public boolean isValid() {
            return valid;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getPhone() {
            return phone;
        }

        public String getPost() {
            return post;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public String getDepartment() {
            return department;
        }

        public String getEntryday() {
            return entryday;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public String getEducation() {
            return education;
        }

        public String getPassword() {
            return password;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public String getCreator() {
            return creator;
        }

        public int getVersion() {
            return version;
        }

        public int getId() {
            return id;
        }

        public boolean isDeletedFlag() {
            return deletedFlag;
        }

        public String getPrivilegeMap() {
            return privilegeMap;
        }

        public String getUsername() {
            return username;
        }

        public boolean isAdmin() {
            return admin;
        }

        public String getName() {
            return name;
        }

        public int getGender() {
            return gender;
        }

        public boolean isResigned() {
            return resigned;
        }

        public String getCreatedDate() {
            return createdDate;
        }
    }
}
