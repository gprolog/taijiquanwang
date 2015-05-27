package cn.fuhl.taijiquan.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.activity.MainActivity;
import cn.fuhl.taijiquan.app.BaseApplication;

/**
 * TaiJiQuan
 * Description:设备管理类
 * Created by Fu.H.L on
 * Date:2015-05-22
 * Time:上午10:57
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class TDevice {

    public static boolean GTE_HC;
    public static boolean GTE_ICS;
    public static boolean PRE_HC;
    private static Boolean _hasBigScreen = null;
    private static Boolean _hasCamera = null;
    private static Boolean _isTablet = null;
   
    private static int _pageSize = -1;
    public static float displayDensity = 0.0F;

    static {
        GTE_ICS = Build.VERSION.SDK_INT >= 14;
        GTE_HC = Build.VERSION.SDK_INT >= 11;
        PRE_HC = Build.VERSION.SDK_INT >= 11 ? false : true;
    }

    public TDevice() {
    }

    public static float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    private static Integer mloadFactor = null;
    public static int getDefaultLoadFactor() {
        if (mloadFactor == null) {
            Integer integer = Integer.valueOf(0xf & BaseApplication
                    .getResource().getConfiguration().screenLayout);
            mloadFactor = integer;
            mloadFactor = Integer.valueOf(Math.max(integer.intValue(), 1));
        }
        return mloadFactor.intValue();
    }

    public static float getDensity() {
        if (displayDensity == 0.0)
            displayDensity = getDisplayMetrics().density;
        return displayDensity;
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) BaseApplication.getApplication().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                screenWidth = (Integer) Display.class
                        .getMethod("getRawWidth")
                        .invoke(d);
                screenHeight = (Integer) Display.class
                        .getMethod("getRawHeight")
                        .invoke(d);
            } catch (Exception ignored) {
            }
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }

    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return BaseApplication.getApplication().getResources()
                    .getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getPageSize() {
        if (_pageSize == -1)
            if (TDevice.isTablet())
                _pageSize = 20;
            else if (TDevice.hasBigScreen())
                _pageSize = 15;
            else
                _pageSize = 12;
        return _pageSize;
    }

    public static String getUdid() {
        String udid = BaseApplication.getPreferences().getString("udid", "");
        if (udid.length() == 0) {
            SharedPreferences.Editor editor = BaseApplication.getPreferences()
                    .edit();
            udid = String.format("%s", UUID.randomUUID());
            editor.putString("udid", udid);
            editor.commit();
        }
        return udid;
    }

    public static boolean hasBigScreen() {
        boolean flag = true;
        if (_hasBigScreen == null) {
            boolean flag1;
            if ((0xf & BaseApplication.getApplication().getResources()
                    .getConfiguration().screenLayout) >= 3)
                flag1 = flag;
            else
                flag1 = false;
            Boolean boolean1 = Boolean.valueOf(flag1);
            _hasBigScreen = boolean1;
            if (!boolean1.booleanValue()) {
                if (getDensity() <= 1.5F)
                    flag = false;
                _hasBigScreen = Boolean.valueOf(flag);
            }
        }
        return _hasBigScreen.booleanValue();
    }

    public static final boolean hasCamera() {
        if (_hasCamera == null) {
            PackageManager pckMgr = BaseApplication.getApplication()
                    .getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            if (flag || flag1)
                flag2 = true;
            else
                flag2 = false;
            _hasCamera = Boolean.valueOf(flag2);
        }
        return _hasCamera.booleanValue();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasHardwareMenuKey(Context context) {
        boolean flag = false;
        if (PRE_HC)
            flag = true;
        else if (GTE_ICS) {
            flag = ViewConfiguration.get(context).hasPermanentMenuKey();
        } else
            flag = false;
        return flag;
    }

    public static boolean hasInternet() {
        boolean flag;
        if (((ConnectivityManager) BaseApplication.getApplication().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static boolean gotoGoogleMarket(Activity activity, String pck) {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.android.vending");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + pck));
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isPackageExist(String pckName) {
        try {
            PackageInfo pckInfo = BaseApplication.getApplication().getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            TLog.loge(e.getMessage());
        }
        return false;
    }

    public static void hideAnimatedView(View view) {
        if (PRE_HC && view != null)
            view.setPadding(view.getWidth(), 0, 0, 0);
    }

    public static void hideSoftKeyboard(View view) {
        if (view == null)
            return;
        ((InputMethodManager) BaseApplication.getApplication().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public static boolean isLandscape() {
        boolean flag;
        if (BaseApplication.getApplication().getResources().getConfiguration().orientation == 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static boolean isPortrait() {
        boolean flag = true;
        if (BaseApplication.getApplication().getResources().getConfiguration().orientation != 1)
            flag = false;
        return flag;
    }

    public static boolean isTablet() {
        if (_isTablet == null) {
            boolean flag;
            if ((0xf & BaseApplication.getApplication().getResources()
                    .getConfiguration().screenLayout) >= 3)
                flag = true;
            else
                flag = false;
            _isTablet = Boolean.valueOf(flag);
        }
        return _isTablet.booleanValue();
    }

    public static float pixelsToDp(float f) {
        return f / (getDisplayMetrics().densityDpi / 160F);
    }

    public static void showAnimatedView(View view) {
        if (PRE_HC && view != null)
            view.setPadding(0, 0, 0, 0);
    }

    public static void showSoftKeyboard(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(4);
    }

    public static void showSoftKeyboard(View view) {
        ((InputMethodManager) BaseApplication.getApplication().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
    }

    public static void toogleSoftKeyboard(View view) {
        ((InputMethodManager) BaseApplication.getApplication().getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isSdcardReady() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static String getCurCountryLan() {
        return BaseApplication.getApplication().getResources().getConfiguration().locale
                .getLanguage()
                + "-"
                + BaseApplication.getApplication().getResources().getConfiguration().locale
                .getCountry();
    }

    public static boolean isZhCN() {
        String lang = BaseApplication.getApplication().getResources()
                .getConfiguration().locale.getCountry();
        if (lang.equalsIgnoreCase("CN")) {
            return true;
        }
        return false;
    }

    public static String percent(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        str = nf.format(p3);
        return str;
    }

    public static String percent2(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
        str = nf.format(p3);
        return str;
    }

    public static void gotoMarket(Context context, String pck) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        context.startActivity(intent);
    }

    public static void openAppInMarket(Context context) {
        if (context != null) {
            String pckName = context.getPackageName();
            try {
                gotoMarket(context, pckName);
            } catch (Exception ex) {
                try {
                    String otherMarketUri = "http://market.android.com/details?id="
                            + pckName;
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(otherMarketUri));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = BaseApplication.getApplication().getPackageManager()
                    .getPackageInfo(BaseApplication.getApplication().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            TLog.loge(e.getMessage());
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = BaseApplication
                    .getApplication()
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.getApplication().getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    public static int getVersionCode(String packageName) {
        int versionCode = 0;
        try {
            versionCode = BaseApplication.getApplication().getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    public static String getVersionName() {
        String name = "";
        try {
            name = BaseApplication
                    .getApplication()
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.getApplication().getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

    public static boolean isScreenOn() {
        PowerManager pm = (PowerManager) BaseApplication.getApplication()
                .getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public static void installAPK(Context context, File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static Intent getInstallApkIntent(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    public static void openDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    public static void openDail(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openSendMsg(Context context) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openCamera(Context context) {
        Intent intent = new Intent(); // 调用照相机
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setFlags(0x34c40000);
        context.startActivity(intent);
    }

    public static String getIMEI() {
        TelephonyManager tel = (TelephonyManager) BaseApplication.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tel.getDeviceId();
    }

    public static String getPhoneType() {
        return Build.MODEL;
    }

    public static void openApp(Context context, String packageName) {
        Intent mainIntent = BaseApplication.getApplication().getPackageManager()
                .getLaunchIntentForPackage(packageName);
        // mainIntent.setAction(packageName);
        if (mainIntent == null) {
            mainIntent = new Intent(packageName);
        } else {
            TLog.logi("Action:" + mainIntent.getAction());
        }
        context.startActivity(mainIntent);
    }

    public static boolean isWifiOpen() {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) BaseApplication
                .getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        // check the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public static void copyTextToBoard(String string) {
        if (TextUtils.isEmpty(string))
            return;
        try {
            ClipboardManager clip = (ClipboardManager) BaseApplication.getApplication()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setText(string);
        }catch (Exception e){
            e.printStackTrace();;
        }
    }

    public static void sendEmail(Context context, String email, String content) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL,
                    new String[] { email });
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getStatuBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = BaseApplication.getApplication().getResources()
                    .getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

//    public static int getActionBarHeight(Context context) {
//        int actionBarHeight = (int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
//        TypedValue tv = new TypedValue();
//        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,
//                tv, true))
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
//                    context.getResources().getDisplayMetrics());
//
//        if (actionBarHeight == 0
//                && context.getTheme().resolveAttribute(R.attr.actionBarSize,
//                tv, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
//                    context.getResources().getDisplayMetrics());
//        }
//
//        return actionBarHeight;
//    }

    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断指定包名的进程是否运行
     *
     * @param context
     * @param packageName
     *            指定包名
     * @return 是否运行
     */
    public static boolean isRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo rapi : infos) {
            if (rapi.processName.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param
     * @param className    判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(300);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return isRunning;
    }

    public static void CreateShut(Activity activity) {
        // intent进行隐式跳转,到桌面创建快捷方式
        Intent addIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重建
        addIntent.putExtra("duplicate", false);
        // 得到应用的名称
        String title = activity.getResources().getString(R.string.app_name);
        // 将应用的图标设置为Parceable类型
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                activity, R.mipmap.ic_launcher);
        // 点击图标之后的意图操作
        Intent myIntent = new Intent(activity, MainActivity.class);
        // 设置快捷方式的名称
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 设置快捷方式的图标
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 设置快捷方式的意图
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
        // 发送广播
        activity.sendBroadcast(addIntent);
    }
}
