package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;

import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownDetialTrackActivity;
import com.hex.express.iwant.activities.MyPickupActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.PickCenterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.GetChars;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class PickCenterAdapter extends BaseListAdapter {
	Handler mHandler;

	public PickCenterAdapter(Context context, List list, Handler handler) {
		super(context, list);
		this.mHandler = handler;
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MyViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.item_pickcenter;
	}

	public class MyViewHolder extends ViewHolder {
		@Bind(R.id.send_name)
		TextView send_name;
		@Bind(R.id.send_time)
		TextView send_time;
		@Bind(R.id.send_address)
		TextView send_address;
		@Bind(R.id.send_addressTo)
		TextView send_addressTo;
		@Bind(R.id.send_good)
		TextView send_good;
//		@Bind(R.id.send_range)
//		TextView send_range;
		@Bind(R.id.img_order)
		ImageView img_order;
		private PickCenterBean bean;

		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setData(final int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			img_order.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String userid = String.valueOf(PreferencesUtils.getInt(context.getApplicationContext(), PreferenceConstants.UID));
					Log.e("uid", userid);
					String businessId = bean.data.get(position).businessId;
					getHttpOrder(userid, businessId);

				}
			});
			bean = new PickCenterBean();
			bean.data = list;
			send_name.setText("发件人: " + bean.data.get(position).personName);// 发件人名字
			send_address.setText(bean.data.get(position).areaName + bean.data.get(position).address);// 发件人地址
			send_addressTo.setText(bean.data.get(position).areaNameTo);// 收件人地址
			send_good.setText("货物名称: " + bean.data.get(position).matName);// 货物名称
//			send_range.setText("距离: " + bean.data.get(position).distance + " Km");// 距离
			send_time.setText("时间: " + bean.data.get(position).publishTime);// 时间

		}

		/**
		 * 抢单
		 */
		public void getHttpOrder(String userId, String businessId) {
			AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.GRABEXPRESSLIST, "userId", userId, "businessId", businessId), null,
					null, null, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("msg", new String(arg2));
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
							if (bean.getErrCode() == 0) {
								mHandler.sendEmptyMessage(1);
								ToastUtil.shortToast(context, bean.getMessage());
								context.startActivity(new Intent(context, MyPickupActivity.class));
							} else {
								ToastUtil.shortToast(context, bean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub

						}
					});
		}
	}

}
