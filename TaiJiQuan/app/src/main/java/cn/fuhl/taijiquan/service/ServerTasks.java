package cn.fuhl.taijiquan.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-26
 * Time:上午10:28
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class ServerTasks {

    public static void publicComment(Context context, PublicCommentTask task) {
        Intent intent = new Intent(ServerTaskService.ACTION_PUBLIC_COMMENT);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ServerTaskService.BUNDLE_PUBLIC_COMMENT_TASK, task);
        intent.putExtras(bundle);
        context.startService(intent);
    }

}
