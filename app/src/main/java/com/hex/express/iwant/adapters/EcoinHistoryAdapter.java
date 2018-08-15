package com.hex.express.iwant.adapters;

import java.util.List;

import butterknife.Bind;

import cn.jpush.android.api.b;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.EcoinBean;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class EcoinHistoryAdapter extends BaseListAdapter {

	public EcoinHistoryAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new ecoinViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		
		return R.layout.item_ecoin;
	}
	class ecoinViewHolder extends ViewHolder{

		public ecoinViewHolder(View itemView) {
			super(itemView);
			
		}
		@Override
		public void setData(int position) {
			EcoinBean bean=new EcoinBean();
			bean.data=list;
			super.setData(position);
			ecoinName.setText(bean.data.get(position).ecoinName);
			ecoin_time.setText(bean.data.get(position).ecoinTime);
			ecoinMoney.setText(bean.data.get(position).ecoinMoney);
		}
		@Bind(R.id.wallet_history)
		TextView ecoinName;
		@Bind(R.id.wallet_time)
		TextView ecoin_time;
		@Bind(R.id.waller_num)
		TextView ecoinMoney;
	}

}
