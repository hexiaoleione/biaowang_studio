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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderCourierAdapter extends BaseListAdapter {

	public OrderCourierAdapter(Context context, List list) {
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
		return R.layout.item_receipt_fragment;
	}

	class ReceiptViewHolder extends ViewHolder {

		public ReceiptViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setData( int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			PublicCourierBean bean = new PublicCourierBean();
			bean.data = list;
			if (bean.data.get(position).userIdTo
					.equals(PreferencesUtils.getInt(context.getApplicationContext(), PreferenceConstants.UID))) {
				ll_check.setVisibility(View.VISIBLE);
				final String expName = bean.data.get(position).expName;
				final String expNo = bean.data.get(position).expNo;
				check.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, ExpressCheckActivity.class);
						intent.putExtra("expName", expName);
						intent.putExtra("expNo", expNo);
						context.startActivity(intent);

					}
				});
			}
			text_matName.setText(bean.data.get(position).matName);
			text_courierName.setText(bean.data.get(position).personName);
			tv_rece.setText("发件人:");
			text_statusNam.setText("快递单号:" + bean.data.get(position).expNo);
			text_publishTime.setText(bean.data.get(position).publishTime);

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
		@Bind(R.id.ll_check)
		View ll_check;
		@Bind(R.id.check)
		TextView check;
		@Bind(R.id.img_phone)
		ImageView img_phone;

	}

}
