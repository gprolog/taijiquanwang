package cn.fuhl.taijiquan.app;

import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.cache.CacheManager;
import cn.fuhl.taijiquan.db.DBHelper;
import cn.fuhl.taijiquan.emoji.EmojiHelper;
import cn.fuhl.taijiquan.utils.Constant;
import cn.fuhl.taijiquan.utils.MethodsCompat;
import cn.fuhl.taijiquan.utils.StringUtils;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-25
 * Time:下午5:41
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class AppContext extends BaseApplication {

    private static final String KEY_SOFTKEYBOARD_HEIGHT = "KEY_SOFTKEYBOARD_HEIGHT";
    private static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    private static final String KEY_NOTIFICATION_SOUND = "KEY_NOTIFICATION_SOUND";
    private static final String LAST_QUESTION_CATEGORY_IDX = "LAST_QUESTION_CATEGORY_IDX";
    private static final String KEY_DAILY_ENGLISH = "KEY_DAILY_ENGLISH";
    private static final String KEY_GET_LAST_DAILY_ENG = "KEY_GET_LAST_DAILY_ENG";
    private static final String KEY_NOTIFICATION_DISABLE_WHEN_EXIT = "KEY_NOTIFICATION_DISABLE_WHEN_EXIT";
    private static final String KEY_TWEET_DRAFT = "key_tweet_draft";
    private static final String KEY_QUESTION_TITLE_DRAFT = "key_question_title_draft";
    private static final String KEY_QUESTION_CONTENT_DRAFT = "key_question_content_draft";
    private static final String KEY_QUESTION_TYPE_DRAFT = "key_question_type_draft";
    private static final String KEY_QUESTION_LMK_DRAFT = "key_question_lmk_draft";
    private static final String KEY_NEWS_READED = "key_readed_news";
    private static final String KEY_QUESTION_READED = "key_readed_question";
    private static final String KEY_BLOG_READED = "key_readed_blog";
    private static final String KEY_NOTICE_ATME_COUNT = "key_notice_atme_count";
    private static final String KEY_NOTICE_MESSAGE_COUNT = "key_notice_message_count";
    private static final String KEY_NOTICE_REVIEW_COUNT = "key_notice_review_count";
    private static final String KEY_NOTICE_NEWFANS_COUNT = "key_notice_newfans_count";
    private static final String KEY_LOGIN_ID = "key_login_id";
    private static final String KEY_COOKIE = "key_cookie";
    private static final String KEY_APP_ID = "key_app_id";
    private static final String KEY_DETAIL_FONT_SIZE = "key_font_size";

    private static Set<String> mReadedNewsIds, mReadedQuestionIds, mReadedBlogIds; //已读IDS

    private static AppContext instance;


    @Override
    public void onCreate() {
        super.onCreate();
        // 注册App异常崩溃处理器
        // Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());

        CacheManager.initCacheDir(Constant.CACHE_DIR, getApplicationContext(),
                new DBHelper(getApplicationContext(), 1, "app_cache", null, null));
        instance = this;
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        EmojiHelper.initEmojis();
    }


    public static void setSoftKeyboardHeight(int height) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_SOFTKEYBOARD_HEIGHT, height);
        apply(editor);
    }

    public static int getSoftKeyboardHeight() {
        return getPreferences().getInt(KEY_SOFTKEYBOARD_HEIGHT, 0);
    }

    public static boolean shouldLoadImage() {
        return getPreferences().getBoolean(KEY_LOAD_IMAGE, true);
    }

    public static void setLoadImage(boolean flag) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_LOAD_IMAGE, flag);
        apply(editor);
    }

    /**
     * 设置字体大小可选值[0,1,2]
     * @param size
     */
    public static void setDetailFontSize(int size) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_DETAIL_FONT_SIZE, size);
        apply(editor);
    }

    /**
     * 获取字体大小[0,1,2]
     * @return
     */
    public static int getDetailFontSize() {
        return getPreferences().getInt(KEY_DETAIL_FONT_SIZE, 1);
    }

    public static String getDetailFontSizeStr() {
        return getResource().getStringArray(R.array.font_size)[getDetailFontSize()];
    }

    public static int getDetailFontSizePx() {
        return getResource().getIntArray(R.array.font_size_value)[getDetailFontSize()];
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }


    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(KEY_APP_ID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(KEY_APP_ID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return getLoginUid() != 0;
    }

    /**
     * 获取登录用户id
     *
     * @return
     */
    public static int getLoginUid() {
        return getPreferences().getInt(KEY_LOGIN_ID, 0);
    }

    public static void setLoginUid(int uid) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_LOGIN_ID, uid);
        apply(editor);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        deleteDatabase("webview.db");
        deleteDatabase("webview.db-shm");
        deleteDatabase("webview.db-wal");
        deleteDatabase("webviewCache.db");
        deleteDatabase("webviewCache.db-shm");
        deleteDatabase("webviewCache.db-wal");
        // 清除数据缓存
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getCacheDir(), System.currentTimeMillis());
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(MethodsCompat.getExternalCacheDir(this),
                    System.currentTimeMillis());
        }
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    public static void setProperty(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static String getProperty(String key) {
        return getPreferences().getString(key, null);
    }

    public static void removeProperty(String... keys) {
        for (String key : keys) {
            SharedPreferences.Editor editor = getPreferences().edit();
            editor.putString(key, null);
            apply(editor);
        }
    }

    public static AppContext instance() {
        return instance;
    }

    public static int getLastQuestionCategoryIdx() {
        return getPreferences().getInt(LAST_QUESTION_CATEGORY_IDX, 0);
    }


    public static boolean isNotificationSoundEnable() {
        return getPreferences().getBoolean(KEY_NOTIFICATION_SOUND, true);
    }

    public static void setNotificationSoundEnable(boolean enable) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_NOTIFICATION_SOUND, enable);
        apply(editor);
    }

    public static boolean isNotificationDisableWhenExit() {
        return getPreferences().getBoolean(KEY_NOTIFICATION_DISABLE_WHEN_EXIT,
                false);
    }

    public static void setNotificationDisableWhenExit(boolean enable) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_NOTIFICATION_DISABLE_WHEN_EXIT, enable);
        apply(editor);
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                KEY_TWEET_DRAFT + instance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(KEY_TWEET_DRAFT + instance().getLoginUid(), draft);
        apply(editor);
    }

    public static String getQuestionTitleDraft() {
        return getPreferences().getString(
                KEY_QUESTION_TITLE_DRAFT + instance().getLoginUid(), "");
    }

    public static void setQuestionTitleDraft(String draft) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(KEY_QUESTION_TITLE_DRAFT + instance().getLoginUid(),
                draft);
        apply(editor);
    }

    public static String getQuestionContentDraft() {
        return getPreferences().getString(
                KEY_QUESTION_CONTENT_DRAFT + instance().getLoginUid(), "");
    }

    public static void setQuestionContentDraft(String draft) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(KEY_QUESTION_CONTENT_DRAFT + instance().getLoginUid(),
                draft);
        apply(editor);
    }

    public static int getQuestionTypeDraft() {
        return getPreferences().getInt(
                KEY_QUESTION_TYPE_DRAFT + instance().getLoginUid(), 0);
    }

    public static void setQuestionTypeDraft(int draft) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_QUESTION_TYPE_DRAFT + instance().getLoginUid(), draft);
        apply(editor);
    }

    public static boolean getQuestionLetMeKnowDraft() {
        return getPreferences().getBoolean(
                KEY_QUESTION_LMK_DRAFT + instance().getLoginUid(), false);
    }

    public static void setQuestionLetMeKnowDraft(boolean draft) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_QUESTION_LMK_DRAFT + instance().getLoginUid(),
                draft);
        apply(editor);
    }

    public static void setRefreshTime(String cacheKey, long time) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(cacheKey, time);
        apply(editor);
    }

    public static long getRefreshTime(String cacheKey) {
        return getPreferences().getLong(cacheKey, 0);
    }

    public static void addReadedNews(int id) {
        setPersistentObjectSet(KEY_NEWS_READED, id + "");
    }

    public static boolean isReadedNews(int id) {
        if (mReadedNewsIds == null) {
            Set<String> ids = getPersistentObjectSet(KEY_NEWS_READED);
            mReadedNewsIds = ids;
        }
        if (mReadedNewsIds != null && mReadedNewsIds.contains(id + ""))
            return true;
        return false;
    }

    public static void setNoticeAtMeCount(int noticeAtMeCount) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_NOTICE_ATME_COUNT + instance().getLoginUid(),
                noticeAtMeCount);
        apply(editor);
    }

    public static int getNoticeAtMeCount() {
        return getPreferences().getInt(KEY_NOTICE_ATME_COUNT + instance().getLoginUid(), 0);
    }

    public static void setNoticeMessageCount(int noticeMessageCount) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_NOTICE_MESSAGE_COUNT + instance().getLoginUid(),
                noticeMessageCount);
        apply(editor);
    }

    public static int getNoticeMessageCount() {
        return getPreferences().getInt(KEY_NOTICE_MESSAGE_COUNT + instance().getLoginUid(), 0);
    }

    public static void setNoticeReviewCount(int reviewCount) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_NOTICE_REVIEW_COUNT + instance().getLoginUid(),
                reviewCount);
        apply(editor);
    }

    public static int getNoticeReviewCount() {
        return getPreferences().getInt(KEY_NOTICE_REVIEW_COUNT + instance().getLoginUid(), 0);
    }

    public static void setNoticeNewFansCount(int newFansCount) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(KEY_NOTICE_NEWFANS_COUNT + instance().getLoginUid(),
                newFansCount);
        apply(editor);
    }

    public static int getNoticeNewFansCount() {
        return getPreferences().getInt(KEY_NOTICE_NEWFANS_COUNT + instance().getLoginUid(), 0);
    }
}
