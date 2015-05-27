package cn.fuhl.taijiquan.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.db.SQLHelper;
import cn.fuhl.taijiquan.utils.TLog;

public class BaseApplication extends Application {
    private static boolean sIsAtLeastGB;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }

    private static BaseApplication mBaseApplication;
    private static Resources mResource;
	@Override
	public void onCreate() {
		super.onCreate();
        mBaseApplication = this;
        mResource = mBaseApplication.getResources();
        initImageLoader(mBaseApplication);
	}
	
	public static BaseApplication getApplication() {
		return mBaseApplication;
	}

    public static Resources getResource() {
        return mResource;
    }

    private SQLHelper sqlHelper;
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mBaseApplication);
		return sqlHelper;
	}

    private static String PREF_NAME = "creativelocker.pref";
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        SharedPreferences pre = getApplication().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

	@Override
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
	}
	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "taijiquan/Cache");//获取到缓存的目录地址
		TLog.logd(cacheDir.getPath());
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.ic_full_image_failed)
                .showImageOnFail(R.drawable.ic_full_image_failed)
                .build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(displayOptions)
				.build();
		ImageLoader.getInstance().init(config);
	}


    public static void setPersistentObjectSet(String key, String o) {
        SharedPreferences store = getPreferences();
        synchronized (store) {
            SharedPreferences.Editor editor = store.edit();
            if (o == null) {
                editor.remove(key);
            } else {
                Set<String> vals = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    vals = store.getStringSet(key, null);
                } else {
                    String s = store.getString(key, null);
                    if (s != null)
                        vals = new HashSet<>(Arrays.asList(s.split(",")));
                }
                if (vals == null) vals = new HashSet<>();
                vals.add(o);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    editor.putStringSet(key, vals);
                } else {
                    editor.putString(key, join(vals, ","));
                }
            }
            editor.commit();
        }
    }

    public static Set<String> getPersistentObjectSet(String key) {
        SharedPreferences store = getPreferences();
        synchronized (store) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return store.getStringSet(key, null);
            } else {
                String s = store.getString(key, null);
                if (s != null) return new HashSet<>(Arrays.asList(s.split(",")));
                else return null;
            }
        }
    }

    public static String join(Set<String> set, String delim) {
        StringBuilder sb = new StringBuilder();
        String loopDelim = "";

        for (String s : set) {
            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }
        return sb.toString();
    }

    public static int[] getDisplaySize() {
        return new int[]{getPreferences().getInt("screen_width", 480),
                getPreferences().getInt("screen_height", 854)};
    }

    public static void saveDisplaySize(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt("screen_width", displaymetrics.widthPixels);
        editor.putInt("screen_height", displaymetrics.heightPixels);
        editor.putFloat("density", displaymetrics.density);
        editor.commit();
        TLog.logi("", "分辨率:" + displaymetrics.widthPixels + "x"
                + displaymetrics.heightPixels + " 密度:" + displaymetrics.density
                + " " + displaymetrics.densityDpi);
    }

}
