package com.hex.express.iwant.adapters;

import java.util.List;

import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.MessageBean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseListAdapter{

	public MessageAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MessageViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_message;
	}

	class MessageViewHolder extends ViewHolder{

		public MessageViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			MessageBean bean=new MessageBean();
			bean.data=list;
			messageTitle.setText(bean.data.get(position).messageTitle);
			tv_content.setText(bean.data.get(position).messageDesc);
			tv_time.setText(bean.data.get(position).sendTime);
			if (bean.data.get(position).ifReaded) {
				iv_icon.setBackgroundResource(R.drawable.mess_bg_new);
			}else {
				iv_icon.setBackgroundResource(R.drawable.mess_bg_newred);
			}
			
		}
		@Bind(R.id.textmessage)
		TextView messageTitle;
		@Bind(R.id.tv_time)
		TextView tv_time;
		@Bind(R.id.tv_content)
		TextView tv_content;
		@Bind(R.id.iv_icon)
		ImageView iv_icon;
	}
	
}
