package com.hex.express.iwant.adapters;

import java.util.List;

import butterknife.Bind;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.SendPersonAdapter.MyViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class AddressAdapter extends BaseListAdapter {

	public AddressAdapter(Context context, List list) {
		super(context, list);
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		return new MyViewHolder(itemView) ;
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_addresstwo;
	}
	class MyViewHolder extends ViewHolder{
		@Bind(R.id.tv_name) TextView tv_name;
		@Bind(R.id.tv_address)TextView tv_address;
		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			SuggestionInfo info=(SuggestionInfo) list.get(position);
			//PoiAddrInfo info=(PoiAddrInfo)list.get(position);
			tv_name.setText(info.city+info.district);
			tv_address.setText(info.key);
		}
	}
}
