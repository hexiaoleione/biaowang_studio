package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.LogCompanyDeliveryActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.MylogisticakActivity;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.FreigBean;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
/**
 * 
 * @author huyichuan
 * 公司 我的物流
 */
public class MylositaAdapter extends BaseListAdapter{

	public MylositaAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MysViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.mylosita_itemse;
	}

	class MysViewHolder extends ViewHolder {

		public MysViewHolder(View itemView) {
			super(itemView);
		}
		@Bind(R.id.infor_title_juli)//距离
		TextView infor_title_juli;
		@Bind(R.id.infor_title_time)//时间
		TextView infor_title_time;
		@Bind(R.id.infor_title_wupin)//物品名字
		TextView infor_title_wupin;
		@Bind(R.id.infor_title_zhongliang)// 重量
		TextView infor_title_zhongliang;
		@Bind(R.id.infor_title_tiji)//体积
		TextView infor_title_tiji;
		@Bind(R.id.infor_title_songhuodizhi)//送货地址
		TextView infor_title_songhuodizhi;
		@Bind(R.id.infor_title_quhuo)//取货操作
		TextView infor_title_quhuo;
		@Bind(R.id.infor_title_daodadizhi)//达到地址
		TextView infor_title_daodadizhi;
		@Bind(R.id.infor_title_songhuo)//送货操作
		TextView infor_title_songhuo;
		@Bind(R.id.infor_title_baojia)//确认按钮
		Button infor_title_baojia;
		@Bind(R.id.button3)//进度
		Button button3;
		@Bind(R.id.button4)//进度
		Button button4;
		@Bind(R.id.button5)//进度
		Button button5;
		
//		@Bind(R.id.my_infor_juli)//距离
//		TextView my_infor_juli;
//		@Bind(R.id.my_infor_time)//时间
//		TextView my_infor_time;
//		@Bind(R.id.my_infor_wupin)//物品名字
//		TextView my_infor_wupin;
//		@Bind(R.id.my_infor_zhongliang)// 重量
//		TextView my_infor_zhongliang;
//		@Bind(R.id.my_infor_tiji)//体积
//		TextView my_infor_tiji;
//		@Bind(R.id.my_infor_songhuodizhi)//送货地址
//		TextView my_infor_songhuodizhi;
//		@Bind(R.id.my_infor_quhuo)//取货操作
//		TextView my_infor_quhuo;
//		@Bind(R.id.my_infor_daodadizhi)//达到地址
//		TextView my_infor_daodadizhi;
//		@Bind(R.id.my_infor_songhuo)//送货操作
//		TextView my_infor_songhuo;
//		@Bind(R.id.my_infor_baojia)//确认按钮
//		Button my_infor_baojia;
//		@Bind(R.id.my_infor_xin)//❤️数量
//		TextView my_infor_xin;
		
		
		
		private FreigBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new FreigBean();
			bean.data = list;
			Log.e("BEAM", bean.data.get(position).toString());
//			infor_title_juli.setText(bean.getData().get(position).distance);
			infor_title_time.setText(bean.getData().get(position).distance);
			infor_title_wupin.setText(bean.getData().get(position).cargoName);
			infor_title_zhongliang.setText(bean.getData().get(position).weight);
			infor_title_tiji.setText(bean.getData().get(position).cargoVolume);
			infor_title_songhuodizhi.setText(bean.getData().get(position).startPlace);
//			infor_title_quhuo.setText(bean.getData().get(position).entPlace);
//			if (bean.getData().get(position).takeCargo) {
//				infor_title_quhuo.setVisibility(View.VISIBLE);
//			}else {
////				infor_title_quhuo.setVisibility(View.GONE);
////				infor_title_quhuo.setTextColor(R.color.graywhite);
//			}
//			if (bean.getData().get(position).sendCargo) {
//				infor_title_songhuo.setVisibility(View.VISIBLE);
//			}else {
////				infor_title_songhuo.setTextColor(R.color.graywhite);
////				infor_title_songhuo.setVisibility(View.GONE);
//			}
			infor_title_daodadizhi.setText(bean.getData().get(position).entPlace);
//			infor_title_songhuo.setText(bean.getData().get(position).distance);
			button3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(context,	LogCompanyActivity.class);//报价详情
					context. startActivity(intent);
					
				}
			});
			button4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(context,LogCompanyDeliveryActivity.class);//收货界面
					context. startActivity(intent);
				}
			});
			button5.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(context,	LogCompanyActivity.class);//密码支付
					context. startActivity(intent);
				}
			});
		}

		}
}
