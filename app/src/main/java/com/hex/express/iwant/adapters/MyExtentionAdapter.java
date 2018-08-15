package com.hex.express.iwant.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.AgentUBean;
import com.hex.express.iwant.bean.AgentUserBean;
import com.hex.express.iwant.bean.MyExtentionBean;
import com.hex.express.iwant.utils.MyBitmapUtils;

public class MyExtentionAdapter extends BaseListAdapter {
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public MyExtentionAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MyViewHodler(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_myextei;//item_myextentionlayout
	}

	@Override
	public int getItemViewType(int position) {
		if (position < 3)
			return TYPE_1;
		else
			return TYPE_2;

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	class MyViewHodler extends ViewHolder {
		@Bind(R.id.courierHeadPath)
		ImageView courierHeadPath;
		@Bind(R.id.iv_star)
		ImageView iv_star;
		@Bind(R.id.courierName)
		TextView courierName;
		@Bind(R.id.allCount) //接单方式
		TextView allCount;
		@Bind(R.id.todayCount)//时间
		TextView todayCount;
		@Bind(R.id.todaymoneyde)//金钱
		TextView todaymoneyde;
		

		public MyViewHodler(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			AgentUBean bean = new AgentUBean();
			bean.data = list;
			int type = getItemViewType(position);
//			switch (type) {
//			case TYPE_1:
//				iv_star.setVisibility(View.VISIBLE);
//				if (position == 0) {
//					iv_star.setBackgroundResource(R.drawable.top01);
//				} else if (position == 1) {
//					iv_star.setBackgroundResource(R.drawable.top02);
//				} else if (position == 2) {
//					iv_star.setBackgroundResource(R.drawable.top03);
//				}
//				break;
//
//			case TYPE_2:
//				iv_star.setVisibility(View.GONE);
//				break;
//			}
			new MyBitmapUtils().display(courierHeadPath,
					bean.data.get(position).headPath);
			courierName.setText(bean.data.get(position).userName);
			allCount.setText(""+bean.data.get(position).type);
			todayCount.setText(""+bean.data.get(position).pushime);
			todaymoneyde.setText(""+bean.data.get(position).money);
//			allCount.setText("累计单数:" + bean.data.get(position).totalNumber +"单"+ "/"
//					+ bean.data.get(position).totalMoney+"元");
//			todayCount.setText("今日单数:" + bean.data.get(position).dayNumber
//					+"单"+ "/" + bean.data.get(position).dayMoney+"元");
		}
	}
}
