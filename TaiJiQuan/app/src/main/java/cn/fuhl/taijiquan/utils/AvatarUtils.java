package cn.fuhl.taijiquan.utils;

/**
 * TaiJiQuan
 * Description:调整头像大小
 * Created by Fu.H.L on
 * Date:2015-05-23
 * Time:上午11:07
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class AvatarUtils {
    public static final String AVATAR_SIZE_REG = "_[0-9]{1,3}";
    public static final String MIDDLE_SIZE = "_100";
    public static final String LARGE_SIZE = "_200";

    // http://*****/uploads/user/63/127726_50.png?t=1390533116000
    public static String getSmallAvatar(String source) {
        return source;
    }

    // http://*****/uploads/user/63/127726_100.png?t=1390533116000
    public static String getMiddleAvatar(String source) {
        if (source == null)
            return null;
        return source.replaceAll(AVATAR_SIZE_REG, MIDDLE_SIZE);
    }

    public static String getLargeAvatar(String source) {
        if (source == null)
            return null;
        return source.replaceAll(AVATAR_SIZE_REG, LARGE_SIZE);
    }
}
