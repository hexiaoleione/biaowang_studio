package com.hex.express.iwant.views;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.framework.base.BaseDialog;
import com.hex.express.iwant.R;

public class CommonDialog extends BaseDialog implements
		View.OnClickListener {

	protected OnSureListener mSureListener;

	protected OnCancelListener mCancelListener;

	protected Button mSureBtn, mCancelBtn;
	
	protected Activity activity;

	public CommonDialog(Activity activity, int theme) {
		super(activity, theme);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		this.activity = activity;
	}
	
	public CommonDialog(Activity activity) {
		this(activity, R.style.MCTheme_Widget_Dialog);
	}
	
	protected void initView(){
		/*mSureBtn = (Button) findViewById(R.id.sure);
		mCancelBtn = (Button) findViewById(R.id.cancel);
		if (mSureBtn != null)
			mSureBtn.setOnClickListener(this);

		if (mCancelBtn != null)
			mCancelBtn.setOnClickListener(this);*/
	}
	
	/**
	 * 设置对话框的宽度
	 * @param wid 范围值： 0-10
	 */
	protected void setWindowWidth(int wid){
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int parentWidth = dm.widthPixels;
		int width = parentWidth;
		lp.width = width * wid / 10;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;
		getWindow().setAttributes(lp);
	}

	public interface OnSureListener {
		void onSure();
	}

	public interface OnCancelListener {
		void onCancel();
	}

	public void setOnSureListener(OnSureListener listener) {
		this.mSureListener = listener;
	}

	public void setOnCancelListener(OnCancelListener listener) {
		this.mCancelListener = listener;
	}

	@Override
	public void onClick(View v) {

	}
}
