package cn.fuhl.taijiquan.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.dialog.DialogControl;
import cn.fuhl.taijiquan.dialog.WaitDialog;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-23
 * Time:下午4:28
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    protected static final int STATE_NONE = 0;
    protected static final int STATE_REFRESH = 1;
    protected static final int STATE_LOADMORE = 2;
    protected int mState = STATE_NONE;

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }

    }

    protected WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.message_loading);

    }

    protected WaitDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;

    }

    @Override
    public void onClick(View v) {

    }

    public boolean onBackPressed() {
        return false;
    }
}
