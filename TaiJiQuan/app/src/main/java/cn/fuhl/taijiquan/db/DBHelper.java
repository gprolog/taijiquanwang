package cn.fuhl.taijiquan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.fuhl.taijiquan.utils.TLog;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-23
 * Time:上午10:34
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String CACHE_TABLE = "app_cache";
    private static final String TAG = DBHelper.class.getSimpleName();
    private static String dbName = "app_cache.db";
    private static String initSQL;
    private static String upgradeSQL;

    static {
        initSQL = "CREATE TABLE IF NOT EXISTS app_cache (id INTEGER PRIMARY KEY, key NVARCHAR(255), file NVARCHAR(255), size NUMERIC, status INTEGER, time NUMERIC, expire NUMERIC);";
        upgradeSQL = initSQL;
    }

    public DBHelper(Context context, int version, String name, String ver,
                    String ver2) {
        super(context, name, null, version);
        if (name == null || name.trim().equals(""))
            name = dbName;
        if (ver != null && !ver.trim().equals("")) {
            initSQL = (new StringBuilder(String.valueOf(initSQL))).append(ver)
                    .toString();
            upgradeSQL = (new StringBuilder(String.valueOf(upgradeSQL)))
                    .append(ver).toString();
        }
        if (ver2 != null && !ver2.trim().equals(""))
            upgradeSQL = (new StringBuilder(String.valueOf(upgradeSQL)))
                    .append(ver2).toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TLog.logi(TAG, "Initialize database");
        String as[] = initSQL.split(";");
        for (int i = 0; i < as.length; i++) {
            String sql = as[i];
            TLog.logi(TAG, "execSQL: " + sql + ";");
            db.execSQL(String.valueOf(sql) + ";");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TLog.logi(TAG, "Upgrade database");
        String[] as = upgradeSQL.split(";");
        for (int l = 0; l < as.length; l++) {
            String sql = as[l];
            TLog.logi(TAG, "execSQL: " + sql + ";");
            db.execSQL(sql + ";");
        }
        onCreate(db);
    }
}
