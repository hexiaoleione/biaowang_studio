package com.hex.express.iwant.adapters;

import java.util.List;

import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.ExpressCheckActivity;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.bean.PublicCourierBean.Data;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptCourierAdapter extends BaseListAdapter {

	Context context;

	public ReceiptCourierAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new ReceiptViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_receipt_fragment;
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
			text_matName.setText(bean.data.get(position).matName);
			if (bean.data.get(position).personNameTo.equals("")) {
				text_courierName.setText(bean.data.get(position).courierName);
				tv_rece.setText("快递员:");

			} else {
				if (bean.data.get(position).status.equals("4")) {
					tv_rece.setText("快递员: ");
					text_courierName.setText(bean.data.get(position).courierName);
				}else {
					text_courierName.setText(bean.data.get(position).personNameTo);
					tv_rece.setText("收件人: ");
				}
				
			}
		
			text_statusNam.setText(bean.data.get(position).statusName);
			// public boolean payed;// 是否支付；false 未支付；true，已支付
//			if (bean.data.get(position).payed) {
//				text_membership.setVisibility(View.INVISIBLE);
//			} else {
//				text_membership.setVisibility(View.VISIBLE);
//			}

			text_publishTime.setText(bean.data.get(position).publishTime);
			if (bean.data.get(position).status.equals("4")) {
				img_phone.setVisibility(View.VISIBLE);
			} else {
				img_phone.setVisibility(View.INVISIBLE);
			}
			img_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.e("msg", "打电话");
					AppUtils.intentDial(context, bean.data.get(position).courierMobile);
				}
			});
		}

		@Bind(R.id.text_matName)
		TextView text_matName;
		@Bind(R.id.text_courierName)
		TextView text_courierName;
		@Bind(R.id.text_statusNam)
		TextView text_statusNam;
		@Bind(R.id.text_publishTime)
		TextView text_publishTime;
		@Bind(R.id.tv_rece)
		TextView tv_rece;
		@Bind(R.id.text_membership) // 现金信息
		TextView text_membership;
		@Bind(R.id.img_phone)
		ImageView img_phone;
	}

}
