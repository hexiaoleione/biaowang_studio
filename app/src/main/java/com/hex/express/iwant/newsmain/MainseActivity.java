package com.hex.express.iwant.newsmain;


import com.hex.express.iwant.R;
import com.hex.express.iwant.newsmain.ContentFragment.MyPageChangeListener;
import com.hex.express.iwant.slidingview.Slidingmu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

@SuppressLint("NewApi")
public class MainseActivity extends BaseFragActivity {
	private Slidingmu mSlidingMenu;
	private LeftFragment leftFragment;
	private ContentFragment centerFragment;

	public static String TAG = "SlidingMenuActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_mainse);
		// String language =
		// Preferences.getInstance(this).getString("language");
		// Util.switchLanguage(SlidingMenuActivity.this,language);

		init();
		initListener();
	}

	private void init() {
		mSlidingMenu = (Slidingmu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));

		android.app.FragmentTransaction t = this.getFragmentManager().beginTransaction();
//		t.replace(R.id.left_frame,leftFragment );
//		t.replace(R.id.left_frame, leftFragment);
		centerFragment = ContentFragment.newInstance(mSlidingMenu);
//		t.replace(R.id.center_frame, centerFragment);
//		t.commit();
	}

	private void initListener() {
		centerFragment.setMyPageChangeListener(new MyPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (centerFragment.isFirst()) {
					mSlidingMenu.setCanSliding(true, false);
				} else if (centerFragment.isEnd()) {
					mSlidingMenu.setCanSliding(false, true);
				} else {
					mSlidingMenu.setCanSliding(false, false);
				}
			}
		});
	}

	// private long firstTime = 0;
	private int mBackKeyPressedTimes = 0;

	@Override
	public void onBackPressed() {
		if(mSlidingMenu.gethasClickLeft()){
			mSlidingMenu.showLeftView();
		}else{
			if (mBackKeyPressedTimes == 0) {
				Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
				mBackKeyPressedTimes = 1;
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							mBackKeyPressedTimes = 0;
						}
					}
				}.start();
				return;
			} else {
//				for (Activity activity : ActivityManager.instance().getActivities()) {
//					if(activity instanceof MainseActivity){
//						finish();
//					}
//				}
				finish();
				System.exit(0);  
			}
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("SlidingMenuActivity");
		// unregisterReceiver(mBroadcastReceiver);
		// mQavsdkControl.stopContext();
		// mQavsdkControl.setIsInStopContext(false);
	}

}
