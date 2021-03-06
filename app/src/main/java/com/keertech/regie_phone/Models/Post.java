package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/7/28.
 */
public class Post implements Serializable {

    /**
     * id : 244
     * deletedFlag : false
     * valid : true
     * privilegeMap : null
     * department : {"valid":true,"modifiedBy":null,"serialNumber":"100074","parent":null,"modifiedDate":"2009-12-18 16:23:11","version":1,"creator":null,"id":100074,"deletedFlag":false,"privilegeMap":null,"principal":null,"name":"洪山徐东管理所","shortName":"徐东管理所","createdDate":"2009-09-16 15:40:03"}
     * modifiedBy : {"position":null,"valid":true,"birthday":"2011-08-10 00:00:00","phone":null,"post":null,"modifiedBy":null,"department":null,"entryday":"2011-08-10 00:00:00","serialNumber":"000001","education":null,"password":"228230","modifiedDate":"2011-08-11 20:50:28","creator":null,"version":2,"id":1,"deletedFlag":false,"privilegeMap":null,"username":"admin","admin":true,"name":"管理员","gender":2,"resigned":false,"createdDate":"2011-08-10 00:00:00"}
     * name : SGY
     * createdDate : 2011-08-13 10:18:41
     * user : {"position":null,"valid":true,"birthday":null,"phone":null,"post":null,"modifiedBy":null,"department":null,"entryday":null,"serialNumber":"80971","education":null,"password":"123","modifiedDate":"2010-01-12 09:04:19","creator":null,"version":2,"id":80971,"deletedFlag":false,"privilegeMap":null,"username":"80971","admin":false,"name":"童锐","gender":null,"resigned":false,"createdDate":"2009-09-16 15:33:18"}
     * modifiedDate : 2011-08-13 10:18:41
     * creator : {"position":null,"valid":true,"birthday":"2011-08-10 00:00:00","phone":null,"post":null,"modifiedBy":null,"department":null,"entryday":"2011-08-10 00:00:00","serialNumber":"000001","education":null,"password":"228230","modifiedDate":"2011-08-11 20:50:28","creator":null,"version":2,"id":1,"deletedFlag":false,"privilegeMap":null,"username":"admin","admin":true,"name":"管理员","gender":2,"resigned":false,"createdDate":"2011-08-10 00:00:00"}
     * version : 0
     */
    private int id;
    private boolean deletedFlag;
    private boolean valid;
    private String privilegeMap;
    private DepartmentEntity department;
    private ModifiedByEntity modifiedBy;
    private String name;
    private String createdDate;
    private UserEntity user;
    private String modifiedDate;
    private CreatorEntity creator;
    private int version;

    public void setId(int id) {
        this.id = id;
    }

    public void setDeletedFlag(boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setPrivilegeMap(String privilegeMap) {
        this.privilegeMap = privilegeMap;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public void setModifiedBy(ModifiedByEntity modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setCreator(CreatorEntity creator) {
        this.creator = creator;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public boolean isDeletedFlag() {
        return deletedFlag;
    }

    public boolean isValid() {
        return valid;
    }

    public String getPrivilegeMap() {
        return privilegeMap;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public ModifiedByEntity getModifiedBy() {
        return modifiedBy;
    }

    public String getName() {
        return name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public CreatorEntity getCreator() {
        return creator;
    }

    public int getVersion() {
        return version;
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

    public static class ModifiedByEntity implements Serializable {
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

    public static class UserEntity implements Serializable {
        /**
         * position : null
         * valid : true
         * birthday : null
         * phone : null
         * post : null
         * modifiedBy : null
         * department : null
         * entryday : null
         * serialNumber : 80971
         * education : null
         * password : 123
         * modifiedDate : 2010-01-12 09:04:19
         * creator : null
         * version : 2
         * id : 80971
         * deletedFlag : false
         * privilegeMap : null
         * username : 80971
         * admin : false
         * name : 童锐
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
