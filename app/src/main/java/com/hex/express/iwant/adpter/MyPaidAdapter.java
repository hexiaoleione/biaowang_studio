package com.hex.express.iwant.adpter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.a.a.c;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.LogistcaInforseActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.MyPaidBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.subfragment.SubFragment3;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class MyPaidAdapter extends BaseListAdapter {

	public MyPaidAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new OwnerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		//informat_newitem   informat_item  新的改的
		return R.layout.item_mypaid;//informat_item  information_item
	}

	class OwnerViewHolder extends ViewHolder {

		public OwnerViewHolder(View itemView) {
			super(itemView);
		}
		@Bind(R.id.tv_status)
		TextView tv_status;
		@Bind(R.id.text1)
		TextView text1;
		@Bind(R.id.text_name)
		TextView text_name;

		@Bind(R.id.text_time)
		TextView text_time;

		@Bind(R.id.text_peoplename)
		TextView text_peoplename;

		@Bind(R.id.iv_message)
		ImageView img_message;

		@Bind(R.id.iv_phone)
		ImageView img_phone;

		@Bind(R.id.img_fukuai)//代付款按钮
				Button img_fukuai;


		@Bind(R.id.img_status)
		ImageView img_status;

		@Bind(R.id.iv_address)
		ImageView img_address;

		@Bind(R.id.delete_order) // 取消订单
				TextView delete_order;
		@Bind(R.id.te_yifukuai) // 已付款
				TextView te_yifukuai;
		@Bind(R.id.tv_zhao) // title
				TextView tv_zhao;



		// 接镖的，和取件的才能显示
		@Bind(R.id.iv_imgview)
		ImageView iv_imgview;
		private MyPaidBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new MyPaidBean();
			bean.data = list;
//			Log.e("BEAM", bean.data.get(position).toString());
			text_time.setText("要求达到时间："+bean.data.get(position).publishTime);
			text_peoplename.setText(bean.data.get(position).driverName);
			text_name.setText(bean.data.get(position).matName);
//			img_status.setBackgroundResource(R.drawable.newfabu);
			delete_order.setText("代付金额："+bean.data.get(position).transferMoney+"元");
			tv_zhao.setText(""+bean.data.get(position).personName+"("+bean.data.get(position).mobile+")"+"找你代付");
//			iv_imgview.setVisibility(View.VISIBLE);

//			img_phone.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					AppUtils.intentDial(context, bean.data.get(position).mobile);
//
//				}
//			});
			img_fukuai.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					ToastUtil.shortToast(context, "即时通信");
					JSONObject obj = new JSONObject();
					try {
						obj.put("billCode", bean.data.get(position).billCode);
						obj.put("matName", null);
						obj.put("matType", null);
						obj.put("insuranceFee", null);
						obj.put("insureMoney", null);
						obj.put("shipMoney", null);
						obj.put("needPayMoney",  bean.data.get(position).transferMoney);
						obj.put("weight", null);
						obj.put("userCouponId", "");
						// if (cb_gift.isChecked()) {
						// obj.put("userCouponId", userCouponId);
						// Log.e("ic", userCouponId + "");
						// } else {
						// obj.put("userCouponId", 0);
						// Log.e("ic", "fff");
						// }

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.e("1111   余额obj", obj.toString());
					AsyncHttpUtils.doPut(context, MCUrl.BALANCE_MONEY, obj.toString(), null,
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method
									// stub
									if (arg2 == null)
										return;
									Log.e("msg", new String(arg2));
									BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
									if (bean.getErrCode() == 0) {
										ToastUtil.shortToast(context, bean.getMessage());
//										sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面
//										Intent intent=new Intent();
//										intent.putExtra("type", "2");
//										setResult(RESULT_OK, intent);
//										finish();

									}
									if (bean.getErrCode() == -2) {
										DialogUtils.createAlertDialogTwo(context, "", bean.getMessage(), 0, "去充值",
												"更换支付方式", new DialogInterface.OnClickListener() {

													@Override
													public void onClick(DialogInterface dialog, int which) {

														context.startActivity(new Intent(context, RechargeActivity.class));
													}

												}).show();
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method
									// stub
								}
							});

				}
			});

		}
	}
	/**
	 * 余额支付的接口
	 */
	public void getaddsurplus() {

	}

}
