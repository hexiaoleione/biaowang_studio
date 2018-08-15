package com.hex.express.iwant.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.hex.express.iwant.R;
/**
 * 
 * @author Ran
 *
 * 时间: 2015年7月30日
 */
public class NoticeWithTwoButtonDialogCreater extends DialogCreater{
	
	public NoticeWithTwoButtonDialogCreater(Activity mActivity, int graty) {
		super(mActivity, graty);
	}

	@Override
	public void createDialog() {
		dialogHolder=new NoticeWithTwoButtonDialogHolder();
		dialogView=View.inflate(mActivity, R.layout.dialog_layoute, null);
		((NoticeWithTwoButtonDialogHolder)dialogHolder).tv_title=(TextView) dialogView.findViewById(R.id.dialog_title);
		((NoticeWithTwoButtonDialogHolder)dialogHolder).tv_detail=(TextView) dialogView.findViewById(R.id.dialog_message);
		((NoticeWithTwoButtonDialogHolder)dialogHolder).tv_sure=(TextView) dialogView.findViewById(R.id.commit_btn);
		((NoticeWithTwoButtonDialogHolder)dialogHolder).tv_cancel=(TextView) dialogView.findViewById(R.id.cancle_btn);
		dialog=DialogUtils.createDialog(mActivity, dialogView, R.style.Trans_Fullscreen,graty);
	}
	
	public class NoticeWithTwoButtonDialogHolder extends DialogHolder{
		public TextView tv_title;
		public TextView tv_detail;
		public TextView tv_sure;
		public TextView tv_cancel;
	}


}
