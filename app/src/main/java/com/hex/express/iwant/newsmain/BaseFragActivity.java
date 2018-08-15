package com.hex.express.iwant.newsmain;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;


public abstract class BaseFragActivity extends Activity{
	public BaseFragActivity activity;
	public Context context;
	public boolean mIsZh;
	/** mObserverGroup */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = BaseFragActivity.this;
		context = getApplicationContext();
	}
	
	
}
