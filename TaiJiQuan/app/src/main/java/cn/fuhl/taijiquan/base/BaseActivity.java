package cn.fuhl.taijiquan.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cn.fuhl.taijiquan.listener.BackGestureListener;
import cn.fuhl.taijiquan.utils.Constant;

public class BaseActivity extends Activity {

	GestureDetector mGestureDetector;
	private boolean mNeedBackGesture = false;
	private BroadcastReceiver mExistReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGestureDetector();
		IntentFilter filter = new IntentFilter(Constant.INTENT_ACTION_EXIT_APP);
		registerReceiver(mExistReceiver, filter);
	}

	private void initGestureDetector() {
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(getApplicationContext(),
					new BackGestureListener(this));
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(mNeedBackGesture){
			return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}
	
	public void setNeedBackGesture(boolean mNeedBackGesture){
		this.mNeedBackGesture = mNeedBackGesture;
	}
	
	public void doBack(View view) {
		onBackPressed();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mExistReceiver);
		mExistReceiver = null;
		super.onDestroy();
	}

}
