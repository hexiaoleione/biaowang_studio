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
public class NoticeDialogCreater extends DialogCreater{
	
	public NoticeDialogCreater(Activity mActivity, int graty) {
		super(mActivity, graty);
	}

	@Override
	public void createDialog() {
		dialogHolder=new NoticeDialogHolder();
		dialogView=View.inflate(mActivity, R.layout.dialog_notice_view, null);
		((NoticeDialogHolder)dialogHolder).tv_title=(TextView) dialogView.findViewById(R.id.tv_title);
		((NoticeDialogHolder)dialogHolder).tv_detail=(TextView) dialogView.findViewById(R.id.tv_detail);
		((NoticeDialogHolder)dialogHolder).tv_sure=(TextView) dialogView.findViewById(R.id.tv_sure);
		dialog=DialogUtils.createDialog(mActivity, dialogView, R.style.Trans_Fullscreen,graty);
	}
	
	public class NoticeDialogHolder extends DialogHolder{
		public TextView tv_title;
		public TextView tv_detail;
		public TextView tv_sure;
	}


}
