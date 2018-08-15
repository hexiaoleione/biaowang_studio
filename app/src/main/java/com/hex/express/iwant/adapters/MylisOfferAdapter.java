package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LoginActivity;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.OfferBean;
import com.hex.express.iwant.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;

public class MylisOfferAdapter extends BaseListAdapter{

	public MylisOfferAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MylisOfferViewHoder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub   quote_item    mylogistical_offer_item
		return R.layout.quote_item;
	}
	class MylisOfferViewHoder extends ViewHolder{

		public MylisOfferViewHoder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.deta_img_heads)//头像
		ImageView deta_img_head;
		@Bind(R.id.deta_img_hea)//头像下实名认证
		ImageView deta_img_hea;
		@Bind(R.id.quote_juli)//距离
		TextView quote_juli;
		@Bind(R.id.quote_time)//时间
		TextView quote_time;
		@Bind(R.id.quote_name)//公司名
		TextView quote_name;
		@Bind(R.id.quote_couriersscore)//用户评价
		RatingBar quote_couriersscore;
		@Bind(R.id.quote_cishu)//运货次数
		TextView quote_cishu;
		@Bind(R.id.quote_liuyan)//留言
		TextView quote_liuyan;
		@Bind(R.id.quote_addse)//地址
		TextView quote_addse;
		@Bind(R.id.quote_addse_dingwei)// 地址图标
		TextView quote_addse_dingwei;
		@Bind(R.id.quote_baojia)//报价
		TextView quote_baojia;
		@Bind(R.id.quote_btn)//确认按钮
		Button quote_btn;
		@Bind(R.id.quote_phone)//电话
		TextView quote_phone;
		private OfferBean cardBean;

		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			cardBean = new OfferBean();
			cardBean.data = list;
			quote_juli.setText(cardBean.getData().get(position).distance);
			quote_name.setText(cardBean.getData().get(position).companyName);
			quote_time.setText(cardBean.getData().get(position).publishTime);
			quote_addse.setText("地址："+cardBean.getData().get(position).address);
			quote_couriersscore.setRating(cardBean.getData().get(position).evaluation); 
			quote_baojia.setText("报价："+cardBean.getData().get(position).transferMoney); 
			quote_cishu.setText("运货次数："+cardBean.getData().get(position).shipNumber); 
			if(cardBean.getData().get(position).realManAuth.equals("Y")){
				deta_img_hea.setVisibility(View.VISIBLE);
			}
			quote_liuyan.setText("留言："+cardBean.getData().get(position).luMessage); 
			
		}
		}
}
