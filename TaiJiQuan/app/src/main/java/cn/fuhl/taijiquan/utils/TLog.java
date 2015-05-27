package cn.fuhl.taijiquan.utils;

import android.util.Log;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-22
 * Time:上午9:31
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class TLog {
    public static final String LOG_TAG = "taijiquan";
    public static boolean DEBUG = true;

    public TLog() {
    }

    public static final void logd(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void loge(String log) {
        if (DEBUG)
            Log.e(LOG_TAG, "" + log);
    }

    public static final void logi(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void logi(String tag, String log) {
        if (DEBUG)
            Log.i(tag, log);
    }

    public static final void logv(String log) {
        if (DEBUG)
            Log.v(LOG_TAG, log);
    }

    public static final void logw(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }
}
