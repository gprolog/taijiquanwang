package cn.fuhl.taijiquan.dialog;

import android.app.Activity;
import android.content.Context;

import cn.fuhl.taijiquan.R;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-23
 * Time:下午4:55
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class DialogHelper {
    public static CommonDialog getPinterestDialog(Context context) {
        return new CommonDialog(context, R.style.dialog_common);
    }

    public static CommonDialog getPinterestDialogCancelable(Context context) {
        CommonDialog dialog = new CommonDialog(context,
                R.style.dialog_common);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static WaitDialog getWaitDialog(Activity activity, int message) {
        WaitDialog dialog = null;
        try {
            dialog = new WaitDialog(activity, R.style.dialog_waiting);
            dialog.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static WaitDialog getWaitDialog(Activity activity, String message) {
        WaitDialog dialog = null;
        try {
            dialog = new WaitDialog(activity, R.style.dialog_waiting);
            dialog.setMessage(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }

    public static WaitDialog getCancelableWaitDialog(Activity activity,
                                                     String message) {
        WaitDialog dialog = null;
        try {
            dialog = new WaitDialog(activity, R.style.dialog_waiting);
            dialog.setMessage(message);
            dialog.setCancelable(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }

}
