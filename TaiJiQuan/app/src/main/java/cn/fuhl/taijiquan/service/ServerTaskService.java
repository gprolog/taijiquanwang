package cn.fuhl.taijiquan.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.app.AppContext;
import cn.fuhl.taijiquan.app.BaseApplication;
import cn.fuhl.taijiquan.helper.ToastHelper;
import cn.fuhl.taijiquan.http.ApiRequest;
import cn.fuhl.taijiquan.http.JsonHttpHandler;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-26
 * Time:上午10:30
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class ServerTaskService extends IntentService {
    private static final String SERVICE_NAME = "ServerTaskService";
    private static final String KEY_COMMENT = "comment_";

    private Handler mExitHandler = new Handler();

    private Handler mResultHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           ToastHelper.ShowToast("操作成功",getApplicationContext());
        }
    };

    public static List<String> penddingTasks = new ArrayList<String>();

    public ServerTaskService() {
        this(SERVICE_NAME);
    }

    private synchronized void tryToStopServie() {
        if (penddingTasks == null || penddingTasks.size() == 0) {
            mExitHandler.removeCallbacksAndMessages(null);
            mExitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopSelf();
                }
            }, 6000);
        }
    }

    private synchronized void addPenddingTask(String key) {
        mExitHandler.removeCallbacksAndMessages(null);
        penddingTasks.add(key);
    }

    private synchronized void removePenddingTask(String key) {
        penddingTasks.remove(key);
    }

    public ServerTaskService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final String BUNDLE_PUBLIC_COMMENT_TASK = "BUNDLE_PUBLIC_COMMENT_TASK";
    public static final String ACTION_PUBLIC_COMMENT = "CN.FUHL.TAIJIQUAN.ACTION_PUBLIC_COMMENT";

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
      if (ACTION_PUBLIC_COMMENT.equals(action)) {
            PublicCommentTask task = intent
                    .getParcelableExtra(BUNDLE_PUBLIC_COMMENT_TASK);
            if (task != null) {
                publicComment(task);
            }
        }
    }

    int postId;
    private void publicComment(final PublicCommentTask task) {
        int id = task.getId() * task.getUid();
        postId = id;
        addPenddingTask(KEY_COMMENT + id);

        notifySimpleNotifycation(id, getString(R.string.comment_publishing),
                getString(R.string.comment_publish),
                getString(R.string.comment_publishing), true, false);

        ApiRequest.publicComment(BaseApplication.getApplication(),task.getId(),task.getContent(),
                new PublicCommentJsonResponseHandler(BaseApplication.getApplication()));

    }

    class PublicCommentJsonResponseHandler extends JsonHttpHandler {
        Context mContext;
        PublicCommentJsonResponseHandler(Context context) {
            super(context);
            mContext=context;
        }

        @Override
        public void onDo(byte[] response) {

            notifySimpleNotifycation(postId,
                    getString(R.string.comment_publish_success),
                    getString(R.string.comment_publish),
                    getString(R.string.comment_publish_success), false,
                    true);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    cancellNotification(postId);
                }
            }, 3000);

            removePenddingTask(KEY_COMMENT + postId);
        }

        @Override
        public void onFail(String msg) {
            super.onFail(msg);
            notifySimpleNotifycation(postId,
                    getString(R.string.comment_publish_faile),
                    getString(R.string.comment_publish),
                    getString(R.string.comment_publish_faile),
                    false,
                    true);
            removePenddingTask(KEY_COMMENT + postId);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            tryToStopServie();
        }
    }

    /**
     *
     * @param id
     * @param ticker
     * @param title
     * @param content
     * @param ongoing
     * @param autoCancel
     */
    private void notifySimpleNotifycation(int id, String ticker, String title,
                                          String content, boolean ongoing, boolean autoCancel) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setSmallIcon(R.mipmap.ic_launcher);

        if (AppContext.isNotificationSoundEnable()) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }

        Notification notification = builder.build();

        NotificationManagerCompat.from(this).notify(id, notification);
    }

    private void cancellNotification(int id) {
        NotificationManagerCompat.from(this).cancel(id);
    }

}
