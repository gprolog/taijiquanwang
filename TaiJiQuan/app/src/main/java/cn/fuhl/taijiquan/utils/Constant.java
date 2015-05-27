package cn.fuhl.taijiquan.utils;

import android.os.Environment;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-20
 * Time:下午4:54
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class Constant {
    public static final String INTENT_ACTION_EXIT_APP = "INTENT_ACTION_EXIT_APP";
    public static final String USER = "CN_FUHL_TAIJIQUAN_USER";
    public static final String USERFAVORITE = "CN_FUHL_TAIJIQUAN_USERFAVORITE";
    public final static String BASE_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/TaiJiQuan/";
    public final static String CACHE_DIR = BASE_DIR + ".cache/";
}
