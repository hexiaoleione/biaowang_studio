package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.LogistcaInforseActivity;
import com.hex.express.iwant.activities.RoleAuthenticationActivity;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.NearbyBean;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.subfragment.SubFragment3;
import com.hex.express.iwant.utils.PreferencesUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

public class NearbyAdapter extends BaseListAdapter {

	public NearbyAdapter(Context context, List list) {
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
		return R.layout.informat_newitem;//informat_item  information_item  
	}

	class OwnerViewHolder extends ViewHolder {

		public OwnerViewHolder(View itemView) {
			super(itemView);
		}
		@Bind(R.id.infor_juli)//距离
		TextView infor_juli;
		@Bind(R.id.infor_time)//时间
		TextView infor_time;
		@Bind(R.id.infor_wupin)//物品名字
		TextView infor_wupin;
//		@Bind(R.id.infor_jiazhi)//物品价值
//		TextView infor_jiazhi;
		
		@Bind(R.id.infor_zhongliang)// 重量
		TextView infor_zhongliang;
		@Bind(R.id.infor_tiji)//体积
		TextView infor_tiji;
		@Bind(R.id.infor_quhuo)//取货操作
		TextView infor_quhuo;
		@Bind(R.id.infor_daodadizhi)//达到地址
		TextView infor_daodadizhi;
		@Bind(R.id.infor_songhuo)//送货操作
		TextView infor_songhuo;
		@Bind(R.id.infor_baojia)//确认按钮
		Button infor_baojia;
		
		
		@Bind(R.id.tem)//
		TextView tem;
		@Bind(R.id.recoldcar)//冷链
		RelativeLayout	recoldcar;
		@Bind(R.id.resonghuo)//正常
		RelativeLayout	resonghuo;
		private NearbyBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new NearbyBean();
			bean.data = list;
//			Log.e("BEAM", bean.data.get(position).toString());
//			System.out.println("111111111钱2 "+bean.data.size()+"   dd "+bean.getData().get(position).cargoName);
//			infor_juli.setText("距离发货地："+bean.getData().get(position).distance+"km");
			if (bean.getData().get(position).carType.equals("cold")) {
				infor_juli.setText("需求："+bean.getData().get(position).carName);	
			}
			
			infor_time.setText("要求到达时间："+bean.getData().get(position).publishTime);
			infor_wupin.setText("物品名称："+bean.getData().get(position).cargoName);
			if (bean.getData().get(position).weight.equals("5")) {
				infor_zhongliang.setText("总重量：≤"+bean.getData().get(position).weight+" 公斤");
			}else {
				infor_zhongliang.setText("总重量："+bean.getData().get(position).weight+" 公斤");
			}
			
			infor_tiji.setText("总体积："+bean.getData().get(position).cargoVolume);
			if ("cold".equals(bean.getData().get(position).carType)) {
				recoldcar.setVisibility(View.VISIBLE);
				resonghuo.setVisibility(View.GONE);
				tem.setText("温度要求："+bean.getData().get(position).tem);
			}else {
				recoldcar.setVisibility(View.GONE);
				resonghuo.setVisibility(View.VISIBLE);
			}
//			infor_tiji.setText("单件体积： "+" 长"+bean.getData().get(position).length+"厘米"+" 宽"+bean.getData().get(position).wide+"厘米"+" 高"+bean.getData().get(position).high+"厘米");
//			String	ct=bean.getData().get(position).cargoCost;
//			String[] strs=ct.split("\\.");
//			infor_jiazhi.setText("价值："+strs[0]+"元");
//			infor_jiazhi.setText("价值："+bean.getData().get(position).cargoCost+"元");
//			infor_daodadizhi.setText(""+bean.getData().get(position).entPlace);
			infor_daodadizhi.setText(""+bean.getData().get(position).endPlaceName);
//			infor_zhongliang.setText("重量："+bean.getData().get(position).);
			
			if (bean.getData().get(position).takeCargo==false) {
				infor_quhuo.setText("发货人送到货场");
			}else {
				infor_quhuo.setText("物流公司上门取货");
			}
			if (bean.getData().get(position).sendCargo==false) {
				infor_songhuo.setText("收件人自提");
			}else {
				infor_songhuo.setText("物流公司送货上门");
			}
			if (bean.getData().get(position).ifQuotion) {
//				infor_baojia.setText("修改报价");
				infor_baojia.setBackgroundResource(R.drawable.xiugaibaojia_new);
			}
			infor_baojia.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, LogCompanyActivity.class);
					intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
					intent.putExtra("userId", bean.getData().get(position).userId);//发件人id
					intent.putExtra("cargoName", bean.getData().get(position).cargoName);//货物名称
					intent.putExtra("startPlace", bean.getData().get(position).startPlace);//物品起发地址
					intent.putExtra("entPlace", bean.getData().get(position).entPlace);//物品到达地址
					intent.putExtra("cargoWeight", bean.getData().get(position).weight);//物品重量
					intent.putExtra("cargoVolume", bean.getData().get(position).cargoVolume);//物品体积
					intent.putExtra("takeTime", bean.getData().get(position).takeTime);//取货时间
					intent.putExtra("arriveTime", bean.getData().get(position).arriveTime);//到达时间
					intent.putExtra("takeName", bean.getData().get(position).takeName);//收货人姓名
					intent.putExtra("takeMobile", bean.getData().get(position).takeMobile);//收货人地址
					intent.putExtra("remark", bean.getData().get(position).remark);//备注
					intent.putExtra("billCode", bean.getData().get(position).billCode);//单号
					intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
					intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否送取货
					intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
					intent.putExtra("sendMobile", bean.getData().get(position).sendMobile);//发货人手机号
					intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
					intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
					intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);// 总钱
					intent.putExtra("takeCargoMoney", bean.getData().get(position).takeCargoMoney);// 取货费
					intent.putExtra("sendCargoMoney", bean.getData().get(position).sendCargoMoney);// 送货上门
					intent.putExtra("cargoTotal", bean.getData().get(position).cargoTotal);// 运费
					intent.putExtra("cargoNumber", bean.getData().get(position).cargoSize);// 
//					intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);// 货物价值
					intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
					intent.putExtra("status", bean.getData().get(position).status);//
					intent.putExtra("appontSpace", bean.getData().get(position).appontSpace);//
					intent.putExtra("yardAddress", bean.getData().get(position).yardAddress);//
					intent.putExtra("ifQuotion",bean.getData().get(position).ifQuotion);// 是否报价
					Log.e("11111", ""+bean.getData().get(position).recId);
					intent.putExtra("matWeight", bean.getData().get(position).matWeight);//
					intent.putExtra("length", bean.getData().get(position).length);//
					intent.putExtra("wide", bean.getData().get(position).wide);//
					
					intent.putExtra("high", bean.getData().get(position).high);//
					
					intent.putExtra("carType", bean.getData().get(position).carType);//
					intent.putExtra("carName", bean.getData().get(position).carName);//
					intent.putExtra("tem", bean.getData().get(position).tem);//
					
//					intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
//					startActivityForResult(intent, 10);
					if (PreferencesUtils.getString(context, PreferenceConstants.WLID).equals("1") ||
							PreferencesUtils.getString(context, PreferenceConstants.WLID).equals("2")
							||
							PreferencesUtils.getString(context, PreferenceConstants.WLID).equals("4")) {
//						context.startActivity(intent);
//						context.startActivity(intent);
					}else {
						Builder ad = new Builder(context);
						ad.setTitle("温馨提示");
						ad.setMessage("您还不是物流司机或者大货司机，请认证后报价！");
						ad.setPositiveButton("立即认证", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Intent intents=new Intent();
								intents.putExtra("tiaoguo", "2");
								intents.setClass(context, RoleAuthenticationActivity.class);
								context.startActivity(intents);
							}
						});
						ad.setNegativeButton("暂不认证", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						});
						ad.create().show();
					}
				
					
				}
			});
		}
	}

}

