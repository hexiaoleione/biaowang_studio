package com.hex.express.iwant.adapters;

import java.util.List;
import java.util.Map;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.ExpressCheckActivity;
import com.hex.express.iwant.bean.ChildExpressBean.Data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PublicMoreAdapter extends BaseAdapter {
	private List<Data> mData;// 存储的EditText值
	public Context mContext;
	private Map<String, Object> map;
	private Handler mhandler;

	public PublicMoreAdapter(Context context, List<Data> mlist) {
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
			convertView = inflater.inflate(R.layout.item_public_more_detail, null);
			holder.et_kuaidinum = (TextView) convertView.findViewById(R.id.et_kuaidinum);
			holder.text_money = (TextView) convertView.findViewById(R.id.text_money);
			holder.btn_revise = (Button) convertView.findViewById(R.id.btn_DELETE);
			holder.tv_INCREASING_NO = (TextView) convertView.findViewById(R.id.tv_INCREASING_NO);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mData != null && mData.size() > 0) {
			final Data bean = mData.get(position);
			holder.et_kuaidinum.setText(bean.childExpNo);
			holder.text_money.setText(bean.childExpPrice);
			holder.tv_INCREASING_NO.setText(position + 1 + "");
			holder.btn_revise.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					if (!bean.childExpNo.equals("") && !bean.childExpNo.equals("")) {
						intent.setClass(mContext, ExpressCheckActivity.class);
						intent.putExtra("expName", bean.childExpNo);
						intent.putExtra("expNo", bean.childExpNo);
						mContext.startActivity(intent);

					}

				}
			});

		}
		return convertView;
	}


	public class ViewHolder {
		TextView et_kuaidinum, text_money;
		Button btn_revise;
		TextView tv_INCREASING_NO;
	}
}
