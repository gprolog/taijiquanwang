package cn.fuhl.taijiquan.bean;

import java.io.Serializable;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-22
 * Time:上午9:23
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class UserBean implements Serializable {
    private String uid;
    private String username;
    private String token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
