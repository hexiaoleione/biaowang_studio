package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.CardBean;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;

public class MylisFreighAdapter extends BaseListAdapter{

	public MylisFreighAdapter(Context context, List list) {
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
		// TODO Auto-generated method stub
		return R.layout.mylogistical_freigh_item;
	}
	class MylisOfferViewHoder extends ViewHolder{

		public MylisOfferViewHoder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		
		@Bind(R.id.freigh_juli)//距离
		TextView freigh_juli;
		@Bind(R.id.freigh_time)//时间
		TextView freigh_time;
		@Bind(R.id.freigh_wupin)//物品名字
		TextView freigh_wupin;
		@Bind(R.id.freigh_zhongliang)// 重量
		TextView freigh_zhongliang;
		@Bind(R.id.freigh_tiji)//体积
		TextView freigh_tiji;
		@Bind(R.id.freigh_songhuodizhi)//送货地址
		TextView freigh_songhuodizhi;
		@Bind(R.id.freigh_quhuo)//取货操作
		TextView freigh_quhuo;
		@Bind(R.id.freigh_daodadizhi)//达到地址
		TextView freigh_daodadizhi;
		@Bind(R.id.freigh_songhuo)//送货操作
		TextView freigh_songhuo;
		@Bind(R.id.freigh_baojia)//确认按钮
		Button freigh_baojia;
		@Bind(R.id.freigh_xin)//❤️数量
		TextView freigh_xin;
		private CardBean cardBean;

		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			cardBean = new CardBean();
			cardBean.data = list;
		
		}
		}
}

