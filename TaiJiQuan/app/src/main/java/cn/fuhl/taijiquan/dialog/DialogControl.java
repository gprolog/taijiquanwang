package cn.fuhl.taijiquan.dialog;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-23
 * Time:下午4:54
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public interface DialogControl {
    public abstract void hideWaitDialog();

    public abstract WaitDialog showWaitDialog();

    public abstract WaitDialog showWaitDialog(int resid);

    public abstract WaitDialog showWaitDialog(String text);
}
