package com.hex.express.iwant.adapters;

import java.util.List;
import java.util.Map;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.ExpressCheckActivity;
import com.hex.express.iwant.adapters.PublicMoreAdapter.ViewHolder;
import com.hex.express.iwant.bean.ChildExpressBean.Data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

public class CardAdaptersd extends BaseAdapter {
	private List<Data> mData;// 存储的EditText值
	public Context mContext;
	private Map<String, Object> map;
	private Handler mhandler;

	public CardAdaptersd(Context context, List<Data> mlist) {
		this.mData = mlist;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if (mData == null || mData.size() == 0) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_activity_card, null);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mData != null && mData.size() > 0) {
			final Data bean = mData.get(position);
			

		}

		return convertView;
	}


	public class ViewHolder {
		TextView txt_cardname;
		TextView moneyView;
		TextView nameView;
		TextView conditionView;
		TextView timeView;
		TextView textView;
		RelativeLayout card_layout;
	}
}
