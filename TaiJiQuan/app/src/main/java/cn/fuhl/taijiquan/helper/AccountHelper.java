package cn.fuhl.taijiquan.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashSet;

import cn.fuhl.taijiquan.activity.LoginActivity;
import cn.fuhl.taijiquan.app.BaseApplication;
import cn.fuhl.taijiquan.bean.UserBean;
import cn.fuhl.taijiquan.utils.Constant;

/**
 * TaiJiQuan
 * Description:用户帮助类
 * Created by Fu.H.L on
 * Date:2015-05-22
 * Time:上午9:24
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class AccountHelper {

    public static UserBean getUser() {
        String s = BaseApplication.getPreferences().getString(Constant.USER, null);
        if (TextUtils.isEmpty(s)) {
            return null;
        } else {
            return new Gson().fromJson(s, UserBean.class);
        }
    }

    public static void setUser(UserBean user) {
        if (user == null) {
            SharedPreferences.Editor editor = BaseApplication.getPreferences().edit();
            editor.putString(Constant.USER, null);
            BaseApplication.apply(editor);
            editor.clear();
            return;
        } else {
            SharedPreferences.Editor editor = BaseApplication.getPreferences().edit();
            editor.putString(Constant.USER, new Gson().toJson(user));
            editor.commit();
            editor.clear();
        }
    }

    public static boolean isLogin() {
        UserBean user = getUser();
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isLoginAndOpenLogin(Context context) {
        UserBean user = getUser();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    public static final String ACTION_LOGIN = "CN.FUHL.TAIJIQUAN.ACTION.LOGIN";
    public static final String ACTION_LOGOUT = "CN.FUHL.TAIJIQUAN.ACTION.LOGOUT";
    public static void sendLoginBroadCast(Context context) {
        Intent intent = new Intent(ACTION_LOGIN);
        context.sendBroadcast(intent);
    }

    public static void sendLogoutBroadCast(Context context) {
        SharedPreferences.Editor editor = BaseApplication.getPreferences().edit();
        editor.putString(Constant.USER, null);
        BaseApplication.apply(editor);
        editor.clear();
        Intent intent = new Intent(ACTION_LOGOUT);
        context.sendBroadcast(intent);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static HashSet<String> getUserFavorite() {
        HashSet<String> s= new HashSet<String>();
        s = (HashSet<String>)BaseApplication.getPreferences().getStringSet(Constant.USERFAVORITE,null);
        if (s==null||s.isEmpty()) {
            return new HashSet<String>();
        } else {
            return s;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setUserFavorite(String postId) {
        if (postId == null) {
            return;
        } else {
            HashSet<String> s = getUserFavorite();
            s.add(postId);
            SharedPreferences.Editor editor = BaseApplication.getPreferences().edit();
            editor.putStringSet(Constant.USERFAVORITE, s);
            BaseApplication.apply(editor);
            editor.clear();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void delUserFavorite(String postId) {
        if (postId == null) {
            return;
        } else {
            HashSet<String> s = getUserFavorite();
            s.remove(postId);
            SharedPreferences.Editor editor = BaseApplication.getPreferences().edit();
            editor.putStringSet(Constant.USERFAVORITE, s);
            BaseApplication.apply(editor);
            editor.clear();
        }
    }
}
