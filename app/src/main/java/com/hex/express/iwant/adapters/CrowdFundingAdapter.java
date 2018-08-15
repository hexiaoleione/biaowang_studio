package com.hex.express.iwant.adapters;

import java.util.List;

import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.CrowdNewBean;
import com.hex.express.iwant.bean.PublicCourierBean;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class CrowdFundingAdapter extends BaseListAdapter {

	public CrowdFundingAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MyViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_crowdfunding;
	}
	class MyViewHolder extends ViewHolder{

		public MyViewHolder(View itemView) {
			super(itemView);
		}
		@Override
		public void setData(int position) {
			super.setData(position);
			CrowdNewBean bean=new CrowdNewBean();
			bean.data=list;
			time.setText(bean.data.get(position).publishTime);
			message.setText(bean.data.get(position).newsContent);
		}
		@Bind(R.id.message)
		TextView message;
		@Bind(R.id.time)
		TextView time;
	}

}
