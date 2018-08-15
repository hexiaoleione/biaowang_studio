package com.hex.express.iwant.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.ChildExpressListActivity;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.views.MarqueeTextView;

public class MyPickupAdapter extends BaseListAdapter {

	private int index = 0;

	public MyPickupAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new ReceiptViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_mypickup;
	}

	public void setIndex(int selected) {
		index = selected;
	}

	class ReceiptViewHolder extends ViewHolder {
		public ReceiptViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setData(final int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			final PublicCourierBean bean = new PublicCourierBean();
			bean.data = list;

			text_personName.setText(bean.data.get(position).personName);
			text_address.setText(bean.data.get(position).areaName + bean.data.get(position).address);
			Log.e("--Taggggg", bean.data.get(position).status.toString());
			if (index == position) {

				text_address.setSelected(true);

			} else {
				text_address.setSelected(false);
			}
			// [status] 状态：1-未取件，2-已取件(已支付)
			// “已取件”改为“已支付”
			// “未取件”改为“未支付”
			// public String payType;// 支付类型，1-现金支付，2-支付宝，3-微信支付，4-余额支付
			if (bean.data.get(position).status.equals("1")) {
				text_statusNam1.setText("未取件");// 快递员接单未取件
				// text_statusNam1.setTextColor(context.getResources().getColor(
				// R.color.red));//设置颜色
				text_picktime.setText("发件时间:" + bean.data.get(position).publishTime);
				tv_detail.setVisibility(View.GONE);
				text_money.setVisibility(View.INVISIBLE);
				text_statusNam1.setVisibility(View.VISIBLE);// 设置为可见
				text_expNo.setVisibility(View.VISIBLE);
//				text_expNo.setText("此单未使用线上支付，您没有补贴");
				text_expNo.setTextColor(Color.RED);

			} else if (bean.data.get(position).status.equals("2")) {

				text_picktime.setText("取件时间:" + bean.data.get(position).pickTime);
				text_statusNam1.setVisibility(View.GONE);
				tv_detail.setVisibility(View.VISIBLE);
				text_statusNam1.setText("已支付");// 快递员接单已支付
				text_expNo.setVisibility(View.INVISIBLE);
				text_money.setVisibility(View.VISIBLE);
				// text_expNo.setText("快递单号："+bean.data.get(position).expNo);
				text_money.setText(bean.data.get(position).needPayMoney + "元");
			} else if (bean.data.get(position).status.equals("3")) {
				text_picktime.setText("取件时间:" + bean.data.get(position).pickTime);
				text_statusNam1.setVisibility(View.GONE);
				tv_detail.setVisibility(View.VISIBLE);
				text_statusNam1.setText("已支付");// 快递员接单已支付
				text_expNo.setVisibility(View.INVISIBLE);
				text_money.setVisibility(View.VISIBLE);
				// text_expNo.setText("快递单号："+bean.data.get(position).expNo);
				text_money.setText(bean.data.get(position).needPayMoney + "元");				
			}else if (bean.data.get(position).status.equals("4")) {
				text_statusNam1.setText(bean.data.get(position).statusName);// 快递员已抢单
				text_picktime.setText("抢单时间:" + bean.data.get(position).contendTime);
				tv_detail.setVisibility(View.GONE);
				text_money.setVisibility(View.INVISIBLE);
				text_statusNam1.setVisibility(View.VISIBLE);// 设置为可见
				text_expNo.setVisibility(View.VISIBLE);
//				text_expNo.setText("使用线上支付，可享受补贴");
				text_expNo.setTextColor(Color.RED);
			}

			tv_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChildExpressListActivity.class);
					intent.putExtra("businessId", String.valueOf(bean.data.get(position).businessId));
					context.startActivity(intent);

				}
			});
			img_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					AppUtils.intentDial(context, bean.data.get(position).mobile);

				}
			});

		}

		@Bind(R.id.img_phone)
		ImageView img_phone;
		@Bind(R.id.text_picktime)
		TextView text_picktime;
		@Bind(R.id.text_expNo)
		TextView text_expNo;
		@Bind(R.id.text_personName)
		TextView text_personName;
		@Bind(R.id.text_statusNam1)
		TextView text_statusNam1;
		@Bind(R.id.text_address)
		MarqueeTextView text_address;
		@Bind(R.id.text_money)
		TextView text_money;
		@Bind(R.id.tv_detail)
		TextView tv_detail;
	}

}
