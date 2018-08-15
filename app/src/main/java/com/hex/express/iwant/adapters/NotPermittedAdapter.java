package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogisPayActivity;
import com.hex.express.iwant.activities.LogisticalActivity;
import com.hex.express.iwant.activities.NotPermittPayActivity;
import com.hex.express.iwant.activities.NotPermittedActivity;
import com.hex.express.iwant.bean.NotPermitted;
import com.hex.express.iwant.newactivity.NotPermitActivity;
import com.hex.express.iwant.views.MarqueeTextView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;

public class NotPermittedAdapter extends BaseListAdapter{

	public NotPermittedAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new WalletViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_permit;
	}
	class WalletViewHolder extends ViewHolder {
		public WalletViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.infor_juli)//距离
		TextView infor_juli;
		@Bind(R.id.infor_time)//时间
		TextView infor_time;
		@Bind(R.id.infor_wupin)//物品名字
		TextView infor_wupin;
		@Bind(R.id.infor_zhongliang)// 重量
		TextView infor_zhongliang;
		@Bind(R.id.infor_tiji)//体积
		TextView infor_tiji;
		@Bind(R.id.infor_quhuo)//取货操作
		TextView infor_quhuo;
		@Bind(R.id.infor_daodadizhi)//达到地址
		MarqueeTextView infor_daodadizhi;
		@Bind(R.id.infor_songhuo)//送货操作
		TextView infor_songhuo;
		@Bind(R.id.infor_baojia)//确认按钮
		Button infor_baojia;
		@Bind(R.id.infor_fadizhi)//达到地址
		MarqueeTextView infor_fadizhi;
		@Bind(R.id.lo)//确认按钮
		LinearLayout lo;
		
		
		public void setData(final int position) {
			final NotPermitted bean = new NotPermitted();
			bean.data = list;
			
			
			infor_time.setText("要求到达时间："+bean.data.get(position).publishTime);
			infor_wupin.setText("物品："+bean.data.get(position).cargoName);
//			infor_zhongliang.setText("重量："+bean.data.get(position).cargoWeight);
//			infor_tiji.setText("体积："+bean.data.get(position).cargoVolume+"方");
			infor_daodadizhi.setText(""+bean.data.get(position).entPlace);
			infor_fadizhi.setText(""+bean.data.get(position).startPlace);
			infor_baojia.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, NotPermittPayActivity.class);
				 	   intent.putExtra("playMoneyMin",bean.data.get(position).playMoneyMin);//余额支付 的价格	
//				 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
				 		intent.putExtra("playMoneyMax", bean.data.get(position).playMoneyMax);//其它支付的价格
				 		intent.putExtra("billCode", bean.data.get(position).billCode);//物流单号
				 		context.startActivity(intent);
				}
			});
			lo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, NotPermitActivity.class);
			 		intent.putExtra("recId",  bean.data.get(position).recId);//物流单号
			 	   intent.putExtra("playMoneyMin",bean.data.get(position).playMoneyMin);//余额支付 的价格	
//			 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
			 		intent.putExtra("playMoneyMax", bean.data.get(position).playMoneyMax);//其它支付的价格
			 		intent.putExtra("billCode", bean.data.get(position).billCode);//物流单号
			 		context.startActivity(intent);
				}
			});
			
		};

		
	}

}
