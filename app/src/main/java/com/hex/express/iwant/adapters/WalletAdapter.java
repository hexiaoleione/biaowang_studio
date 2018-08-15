package com.hex.express.iwant.adapters;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.WalletBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WalletAdapter extends BaseListAdapter {

	public WalletAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	class WalletViewHolder extends ViewHolder {
		public WalletViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		public void setData(int position) {
			WalletBean walletBean = new WalletBean();
			walletBean.data = list;
			DecimalFormat df = new DecimalFormat("######0.00");
//			if (!walletBean.data.get(position).recordMoney.equals("")||walletBean.data.get(position).recordMoney!=null) {
//				waller_num.setText(df.format(Double.parseDouble(walletBean.data.get(position).recordMoney)));
//			}else {
				waller_num.setText(walletBean.data.get(position).recordMoney);
//			}
//			waller_num.setText(walletBean.data.get(position).recordMoney);
			wallet_history.setText(walletBean.data.get(position).recordName);
			wallet_time.setText(walletBean.data.get(position).recordTime);
			wallet_desc.setText(walletBean.data.get(position).recordDesc);
		};

		@Bind(R.id.wallet_history)
		TextView wallet_history;
		@Bind(R.id.wallet_time)
		TextView wallet_time;
		@Bind(R.id.waller_num)
		TextView waller_num;
		@Bind(R.id.wallet_desc)
		TextView wallet_desc;
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_wallet;
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new WalletViewHolder(itemView);
	}
}
