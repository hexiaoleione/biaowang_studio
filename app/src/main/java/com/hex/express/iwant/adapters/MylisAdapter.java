package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LoginActivity;
import com.hex.express.iwant.activities.LogistiOfferActivity;
import com.hex.express.iwant.activities.LogisticalActivity;
import com.hex.express.iwant.activities.LogistioffActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.MylogisticakActivity;
import com.hex.express.iwant.adapters.MylisAdapter.MylisViewHoder;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
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

public class MylisAdapter extends BaseListAdapter{

	public MylisAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MylisViewHoder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub   mylogist_item  mylogist_itemes
		return R.layout.mylogist_itemes;  //mylogistical_item
	}
	class MylisViewHoder extends ViewHolder{

		public MylisViewHoder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.item_wupin)
		TextView item_wupin;
		@Bind(R.id.item_zhongliang)
		TextView item_zhongliang;
		@Bind(R.id.item_tiji)
		TextView item_tiji;
//		@Bind(R.id.item_dizhi1)
//		TextView item_dizhi1;
//		@Bind(R.id.item_quhuo)
//		TextView item_quhuo;
		@Bind(R.id.item_dizhi2)
		MarqueeTextView item_dizhi2;
//		@Bind(R.id.item_songhuo)
//		TextView item_songhuo;
		@Bind(R.id.item_name)
		TextView item_name;
		@Bind(R.id.item_phone)
		TextView item_phone;
		@Bind(R.id.item_time)
		TextView item_time;
		@Bind(R.id.item_xin)//
		TextView item_xin;
		@Bind(R.id.item_weizhifu)//未支付图片
		ImageView item_weizhifu;
		
		@Bind(R.id.button1)
		Button button1;
		@Bind(R.id.button2)
		Button button2;
		
		@Bind(R.id.butt_wan)//中间的
		Button butt_wan;
		@Bind(R.id.textView1)//  报价图标
		TextView textView1;
		@Bind(R.id.item_time2)
		TextView item_time2;
		private MyloginBean bean;
//status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
		
		@Override
		public void setData(final int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			bean = new MyloginBean();
			bean.data = list;
			item_wupin.setText("物品："+bean.data.get(position).cargoName);
			item_zhongliang.setText("重量："+bean.data.get(position).cargoWeight);
			item_tiji.setText("体积："+bean.data.get(position).cargoVolume+"方");
//			item_dizhi1.setText(""+bean.getData().get(position).startPlace);
			item_dizhi2.setText(""+bean.data.get(position).entPlace);
			item_name.setText("收货人："+bean.data.get(position).takeName);
			item_phone.setText("手机号："+bean.data.get(position).takeMobile);
			item_time.setText(""+bean.data.get(position).publishTime);
			item_xin.setText(bean.data.get(position).quotationNumber);
//			if (bean.getData().get(position).takeCargo!=true) {
//				item_quhuo.setVisibility(View.GONE);
//			}
			if (bean.data.get(position).sendCargo!=true) {
//				item_songhuo.setTextColor(R.color.grayview);
			}
//			item_weizhifu.setTag(tag);
			if (bean.data.get(position).status.equals("1")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_1sx);
			}else 
				if(bean.data.get(position).status.equals("6")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_1x);
			}else
			if (bean.data.get(position).status.equals("2")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_2x);
			}else
			if (bean.data.get(position).status.equals("8")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_8x);
			}else
			if (bean.data.get(position).status.equals("4")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_6x);
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
				textView1.setVisibility(View.INVISIBLE);
//				item_time.setVisibility(View.GONE);
				item_xin.setVisibility(View.INVISIBLE);
				butt_wan.setVisibility(View.VISIBLE);
//				item_time2.setVisibility(View.VISIBLE);
//				item_time2.setText(""+bean.getData().get(position).publishTime);
			}else
			if (bean.data.get(position).status.equals("5")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_6x);
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
				textView1.setVisibility(View.INVISIBLE);
//				item_time.setVisibility(View.GONE);
				item_xin.setVisibility(View.INVISIBLE);
				butt_wan.setVisibility(View.VISIBLE);
//				item_time2.setVisibility(View.VISIBLE);
//				item_time2.setText(""+bean.getData().get(position).publishTime);
			}else
			if (bean.data.get(position).status.equals("7")) {
				item_weizhifu.setVisibility(View.VISIBLE);
				item_weizhifu.setBackgroundResource(R.drawable.status_7x);
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
				textView1.setVisibility(View.INVISIBLE);
//				item_time.setVisibility(View.GONE);
				item_xin.setVisibility(View.INVISIBLE);
				butt_wan.setVisibility(View.VISIBLE);
//				item_time2.setVisibility(View.VISIBLE);
//				item_time2.setText(""+bean.getData().get(position).publishTime);
			}else
			if (bean.data.get(position).status.equals("3")) {
//				button1.setBackgroundResource(R.drawable.search_bar_dui);
				item_weizhifu.setVisibility(View.VISIBLE);
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
				textView1.setVisibility(View.INVISIBLE);
//				item_time.setVisibility(View.GONE);
				item_xin.setVisibility(View.INVISIBLE);
				butt_wan.setBackgroundResource(R.drawable.search_barkan);
				item_weizhifu.setBackgroundResource(R.drawable.status_3x);
				butt_wan.setVisibility(View.VISIBLE);
//				item_time2.setVisibility(View.VISIBLE);
//				item_time2.setText(""+bean.getData().get(position).publishTime);
			}else
				if (bean.data.get(position).status.equals("9")) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_7x);
					button1.setVisibility(View.GONE);
					button2.setVisibility(View.GONE);
					textView1.setVisibility(View.INVISIBLE);
//					item_time.setVisibility(View.GONE);
					item_xin.setVisibility(View.INVISIBLE);
					butt_wan.setVisibility(View.VISIBLE);
//					item_time2.setVisibility(View.VISIBLE);
//					item_time2.setText(""+bean.getData().get(position).publishTime);
				}
//			if (bean.getData().get(position).status.equals("4")) {
////				button1.setBackgroundResource(R.drawable.search_bar_dui);
//				button1.setVisibility(View.GONE);
//				button2.setVisibility(View.GONE);
//				textView1.setVisibility(View.INVISIBLE);
//				item_time.setVisibility(View.GONE);
//				item_xin.setVisibility(View.INVISIBLE);
//				butt_wan.setBackgroundResource(R.drawable.search_barwancheng);
//				butt_wan.setVisibility(View.VISIBLE);
//				item_time2.setVisibility(View.VISIBLE);
//				item_time2.setText(""+bean.getData().get(position).publishTime);
//			}
//			button1.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					AlertDialog.Builder ad = new Builder(MylogisticakActivity.this);
//					ad.setTitle("温馨提示");
//					ad.setMessage("是否要删除");
//					ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							int  iid=bean.data.get(position).recId;
//							addPostResult(iid);
////							ToastUtil.shortToast(context, "查看报价");;
//						}
//					});
//					ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							arg0.dismiss();
//							
//						}
//					});
//					ad.create().show();
//					
//				
//				}
//			});
			butt_wan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent();
					// TODO Auto-generated method stub
//					ToastUtil.shortToast(context, "查看报价人员");
//					intent.putExtra("cityCode", cityCode);
//					intent.putExtra("latitude", latitude);
					intent.putExtra("cargoName", bean.data.get(position).cargoName);
					intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
					intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
					intent.putExtra("startPlace", bean.data.get(position).startPlace);
					intent.putExtra("entPlace", bean.data.get(position).entPlace);
					intent.putExtra("takeName", bean.data.get(position).takeName);
					intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
					intent.putExtra("publishTime", bean.data.get(position).publishTime);
					intent.putExtra("takeCargo", bean.data.get(position).takeCargo);
					intent.putExtra("sendCargo", bean.data.get(position).sendCargo);
					intent.putExtra("remark", bean.data.get(position).remark);
					intent.putExtra("takeTime", bean.data.get(position).takeTime);
					intent.putExtra("arriveTime", bean.data.get(position).arriveTime);
					intent.putExtra("recId", bean.data.get(position).recId);
					intent.putExtra("billCode", bean.data.get(position).billCode);
					intent.putExtra("whether", bean.data.get(position).whether);
					intent.putExtra("premium", bean.data.get(position).premium);
					intent.putExtra("status", bean.data.get(position).status);
					intent.putExtra("cargoCost", bean.data.get(position).cargoCost);
//					intent.setClass(MainActivity.this, MyDownwindActivity.class);
//					intent.setClass(context, LogistiOfferActivity.class);//
					intent.setClass(context, LogistioffActivity.class);//
//					if (!bean.getData().get(position).status.equals("2")) {
						context.startActivity(intent);
//					}else {
//						ToastUtil.shortToast(context, "此单已完成");
//					}
					
				}
			});
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent();
					// TODO Auto-generated method stub
//					ToastUtil.shortToast(context, "查看报价人员");
//					intent.putExtra("cityCode", cityCode);
//					intent.putExtra("latitude", latitude);
					intent.putExtra("cargoName", bean.data.get(position).cargoName);
					intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
					intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
					intent.putExtra("startPlace", bean.data.get(position).startPlace);
					intent.putExtra("entPlace", bean.data.get(position).entPlace);
					intent.putExtra("takeName", bean.data.get(position).takeName);
					intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
					intent.putExtra("publishTime", bean.data.get(position).publishTime);
					intent.putExtra("takeCargo", bean.data.get(position).takeCargo);
					intent.putExtra("sendCargo", bean.data.get(position).sendCargo);
					intent.putExtra("remark", bean.data.get(position).remark);
					intent.putExtra("takeTime", bean.data.get(position).takeTime);
					intent.putExtra("arriveTime", bean.data.get(position).arriveTime);
					intent.putExtra("recId", bean.data.get(position).recId);
					intent.putExtra("billCode", bean.data.get(position).billCode);
					intent.putExtra("whether", bean.data.get(position).whether);
					intent.putExtra("premium", bean.data.get(position).premium);
					intent.putExtra("status", bean.data.get(position).status);
					intent.putExtra("cargoCost", bean.data.get(position).cargoCost);//价值
					intent.putExtra("insureCost", bean.data.get(position).insureCost);//保费
//					intent.setClass(MainActivity.this, MyDownwindActivity.class);
					if (!bean.data.get(position).status.equals("2")) {
						intent.setClass(context, LogistiOfferActivity.class);//用
						context.startActivity(intent);
					}else {
						intent.setClass(context, LogistioffActivity.class);
						context.startActivity(intent);
//						finish();
					}
					
				}
			});
	
		}

		}
//	private void addPostResult(int  messageId) {
//		JSONObject obj = new JSONObject();
//		
////		try {
////			obj.put("messageId",String.valueOf(messageId));
////		} catch (JSONException e) {
////			e.printStackTrace();
////		}
//		Log.e("查看数据", obj.toString());//doPostJson     dosPut
//		Log.e("111", MCUrl.LOGISTICSLIST_DEKETE + String.valueOf(messageId));
//		AsyncHttpUtils.doPut(context,
//				MCUrl.LOGISTICSLIST_DEKETE +String.valueOf(messageId),
//				obj.toString(), null,new AsyncHttpResponseHandler() {
////		AsyncHttpUtils.dosPut(context, MCUrl.LOGISTICSLIST_DEKETE, obj.toString(),
////				new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						Log.e("11111111111 wwww   ", new String(arg2));
//						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
////						Log.e("oppop", bean.data.toString());
//						ToastUtil.shortToast(context, bean.getMessage());
//						adapter.notifyDataSetChanged();
//						getHttprequst(false, true, 1, false);
//					}
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//					}
//				});
//
//	}
}
