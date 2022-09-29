package com.achengovo.myfiles.entity;


import com.achengovo.myfiles.utils.Md5Util;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;//用户ID
    private String userName;//用户名
    private String userPass;//用户密码
    private String userAvatar;//用户头像
    private String userEmail;//用户邮箱
    private String userSex;//用户性别
    private String userType;//用户类型（admin:管理员、user:用户、disable:禁用）
    private String varCode;//验证码
    private String userToken;//用户token

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getVarCode() {
        return varCode;
    }

    public void setVarCode(String varCode) {
        this.varCode = varCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return Md5Util.remd5(userPass);
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userType='" + userType + '\'' +
                ", varCode='" + varCode + '\'' +
                ", userToken='" + userToken + '\'' +
                '}';
    }
}

