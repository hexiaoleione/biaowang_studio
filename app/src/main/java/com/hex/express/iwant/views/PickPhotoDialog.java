package com.hex.express.iwant.views;

import android.app.Activity;
import android.view.View;

import com.hex.express.iwant.R;
import com.hex.express.iwant.utils.AppUtils;


/**
 * 版本更新对话框
 * 
 * @author lgd
 * @since 2015-2-2 20:52:57
 */
public class PickPhotoDialog extends CommonDialog {
	
	// 拍照的照片路径
	private String takePhotoPickPath;

	public PickPhotoDialog(Activity activity, int theme) {
		super(activity, theme);
		initView();
	}
	
	public PickPhotoDialog(Activity activity, String takePhotoPickPath) {
		super(activity);
		this.takePhotoPickPath = takePhotoPickPath;
		initView();
	}
	
	@Override
	protected void initView() {
		setContentView(R.layout.dialog_pick_photo);
		findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
				AppUtils.takePhoto(activity, 101, takePhotoPickPath);
			}
		});
		findViewById(R.id.pick).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
				AppUtils.pickPhoto(activity, 102);
			}
		});
		setWindowWidth(8);
		super.initView();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		/*switch (v.getId()) {
		case R.id.sure:
			AppUtils.exitAppication(getContext());
			if(mSureListener != null){
				mSureListener.onSure();
			}
			break;
			
		case R.id.cancel:
			dismiss();
			if(mCancelListener != null)
				mCancelListener.onCancel();
			break;

		default:
			break;
		}*/
	}
}
