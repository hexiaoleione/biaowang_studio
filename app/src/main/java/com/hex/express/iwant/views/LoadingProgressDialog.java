package com.hex.express.iwant.views;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hex.express.iwant.R;


public class LoadingProgressDialog extends Dialog {
	
	TextView msg;
	
	ImageView imageView;

	/*private static LoadingProgressDialog dialog=null;
	
	public static synchronized LoadingProgressDialog getInstance(Context context){
		if(dialog==null){
			dialog=new LoadingProgressDialog(context);
		}
		return dialog;
	}*/
	
	
	public LoadingProgressDialog(Context context) {
		super(context, R.style.MCTheme_Widget_Dialog);
	}

	/*public LoadingProgressDialog(Context context, int theme) {
		super(context, theme);
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_progress_loading);
		imageView = (ImageView) findViewById(R.id.iv);
		msg = (TextView) findViewById(R.id.tv);
	}
	@Override
	public void show() {
		super.show();
		if (imageView!=null) {
			startRotatAnimation(imageView);
		}
	}
	
	public void setMsg(String value){
		msg.setText(value);
	}
	
	AnimationSet animationSet;
	public void startRotatAnimation(View view) {
		if (animationSet == null) {
			animationSet = new AnimationSet(true);
			LinearInterpolator lin = new LinearInterpolator();
			animationSet.setInterpolator(lin);
			animationSet.setFillAfter(true);
			RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5F,
					Animation.RELATIVE_TO_SELF, 0.5F);
			rotateAnimation.setDuration(1000);
			rotateAnimation.setRepeatCount(-1);
			animationSet.addAnimation(rotateAnimation);
		}
		view.startAnimation(animationSet);
	}
	@Override
	public void cancel() {
		super.cancel();
		if (imageView != null) {
			imageView.clearAnimation();
		}
	}
	@Override
	public void dismiss() {
		super.dismiss();
		if (imageView != null) {
			imageView.clearAnimation();
		}
	}
	
}
