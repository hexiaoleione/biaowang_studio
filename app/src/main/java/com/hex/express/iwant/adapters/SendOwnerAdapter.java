package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.RegisterSetImageAndNameActivity;
import com.hex.express.iwant.activities.UserCenterActivity;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.GifView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import cn.jpush.android.api.c;

public class SendOwnerAdapter extends BaseListAdapter {
	
	 //自定义接口，用于回调按钮点击事件到Activity
	  public interface MyClickListener{
	      public void clickListener(View v);
	  }

	public SendOwnerAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		return new OwnerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.item_newsendownerd;//item_sendownerd  item_newsendownerd

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
		ImageView img_fukuai;
		

		@Bind(R.id.img_status)
		ImageView img_status;

		@Bind(R.id.iv_address)
		ImageView img_address;

		@Bind(R.id.delete_order) // 取消订单
		TextView delete_order;

		// 接镖的，和取件的才能显示
		@Bind(R.id.iv_imgview)
		ImageView iv_imgview;
		private DownSpecialBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new DownSpecialBean();
			bean.data = list;
			Log.e("BEAM", bean.data.get(position).toString());
			text_time.setText(bean.data.get(position).publishTime);
			text_peoplename.setText(bean.data.get(position).driverName);
			text_name.setText(bean.data.get(position).matName);
//			iv_imgview.setVisibility(View.VISIBLE);
			
			img_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AppUtils.intentDial(context, bean.data.get(position).mobile);

				}
			});
			img_fukuai.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					ToastUtil.shortToast(context, "即时通信");

				}
			});
			img_address.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ToastUtil.shortToast(context, "获取当前地理位置");

				}
			});
			delete_order.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Builder ad = new Builder(context);
					ad.setTitle("温馨提示");
					ad.setMessage("若镖师已接单，取消订单则罚款10元补偿给镖师");
					ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							deleteOrder();
						}
					});
					ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
						}
					});
					ad.create().show();

				}

				private void deleteOrder() {
					AsyncHttpUtils.doGet(
							UrlMap.getUrl(MCUrl.CANCELDOWNWINDTASK, "recId",
									String.valueOf(bean.data.get(position).recId)),
							null, null, null, new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.e("msg", new String(arg2));
//									context.sendBroadcast(new Intent().setAction("cancelOrder"));
//									getHttpRequst(false, true, 1, false);
									
								}

								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub

								}
							});

				}
			});
			img_status.setBackgroundResource(R.drawable.newfabu);
			delete_order.setVisibility(View.VISIBLE);
			tv_status.setText("");
			if (bean.data.get(position).status.equals("9")) {
				delete_order.setText("货物违规");
				tv_status.setText("");
				delete_order.setClickable(false);
			}else {
				delete_order.setClickable(true);
			}
			// public String status;//状态 0-预发布成功(未支付) 1-支付成功(已支付)-待接镖 2-待取镖
			// 3-押镖中 4-取消押镖 5-押镖完成（） 6-删除 7-已评价
			if (bean.data.get(position).status.equals("1")) {
				tv_status.setText("等待接单");
				tv_status.setTextColor(context.getResources().getColor(R.color.red));
				text1.setText("收件人:");
				text_peoplename.setText(bean.data.get(position).personNameTo);
				delete_order.setVisibility(View.VISIBLE);
				img_status.setBackgroundResource(R.drawable.newfabu);
			} else if (bean.data.get(position).status.equals("2")) {
				tv_status.setText("等待取单");
				text1.setText("镖师:");
				tv_status.setTextColor(context.getResources().getColor(R.color.red));
				delete_order.setVisibility(View.VISIBLE);
				img_status.setBackgroundResource(R.drawable.newfabu);
			} else if (bean.data.get(position).status.equals("3")) {
				tv_status.setText("送单中");
				text1.setText("镖师:");
				tv_status.setTextColor(context.getResources().getColor(R.color.red));
				delete_order.setVisibility(View.GONE);
				img_status.setBackgroundResource(R.drawable.fabuqujian);
			} else if (bean.data.get(position).status.equals("4")) {
				if (!bean.data.get(position).ifAgree.equals("")) {
					tv_status.setText("货物违规取消");
					delete_order.setVisibility(View.GONE);
					text1.setText("镖师:");
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					img_status.setBackgroundResource(R.drawable.fabuquxiao);
			    	}else {
			    		delete_order.setVisibility(View.GONE);
						tv_status.setText("镖师取消送单");
						text1.setText("镖师:");
						tv_status.setTextColor(context.getResources().getColor(R.color.red));
						img_status.setBackgroundResource(R.drawable.fabuquxiao);
					}
				
			} else if (bean.data.get(position).status.equals("5")) {
				tv_status.setText("  送单完成(待评价）");
				text1.setText("镖师:");
				delete_order.setVisibility(View.GONE);
				tv_status.setTextColor(context.getResources().getColor(R.color.red));
				img_status.setBackgroundResource(R.drawable.fabuwanchen);
			} else if (bean.data.get(position).status.equals("8")) {
				tv_status.setTextColor(context.getResources().getColor(R.color.red));
				tv_status.setText("已取消发布");
				text1.setText("收件人:");
				text_peoplename.setText(bean.data.get(position).personNameTo);
				delete_order.setVisibility(View.GONE);
				img_status.setBackgroundResource(R.drawable.fabuquxiao);
			}else if (bean.data.get(position).status.equals("7")) {
				tv_status.setTextColor(context.getResources().getColor(R.color.orange1));
				tv_status.setText("已评价");
				text1.setText("镖师:");
				delete_order.setVisibility(View.GONE);
				img_status.setBackgroundResource(R.drawable.fabuwanchen);
			}else if (bean.data.get(position).status.equals("6")) {
				tv_status.setTextColor(context.getResources().getColor(R.color.red));
				tv_status.setText("过期（已退款）");
				text1.setText("收件人:");
				img_status.setBackgroundResource(R.drawable.fabuquxiao);
//				setBackgroundResource(R.drawable.simi);
				delete_order.setVisibility(View.GONE);
			}
//			if(!bean.data.get(position).limitTime.equals("")){
//				img_message.setVisibility(View.VISIBLE);
//			}
//           if (img_message) {
//        	   img_message.setVisibility(View.VISIBLE);
//		}
		}
	}

}
