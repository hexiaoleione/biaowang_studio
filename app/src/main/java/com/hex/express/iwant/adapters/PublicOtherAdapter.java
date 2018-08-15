package com.hex.express.iwant.adapters;

import java.util.List;
import java.util.Map;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.PublicOtherBean;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PublicOtherAdapter extends BaseAdapter {
	private List<PublicOtherBean> mData;// 存储的EditText值
	public Context mContext;
	private Handler mhandler;

	public PublicOtherAdapter(Context context, List<PublicOtherBean> data,Handler handler) {
		this.mData = data;
		this.mContext = context;
		this.mhandler=handler;
	}

	@Override
	public int getCount() {
		if (mData == null || mData.size() == 0){
			return 0;}
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
			convertView = inflater.inflate(R.layout.item_public_detail, null);
			holder.et_kuaidinum = (TextView) convertView.findViewById(R.id.et_kuaidinum);
			holder.text_money = (TextView) convertView.findViewById(R.id.text_money);
			holder.btn_revise = (ImageView) convertView.findViewById(R.id.btn_DELETE);
			holder.tv_INCREASING_NO=(TextView) convertView.findViewById(R.id.tv_INCREASING_NO);
			holder.btn_revise.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mData.remove(position);
					Log.e("mdata",mData.toString());
					mhandler.sendEmptyMessage(2);
					
					
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		for (int i = 0; i < mData.size(); i++) {
			Log.e("mliyuisdh", mData.get(i).getChildExpNo()+"dddd"+mData.get(i).getChildExpPrice());
		}
		if (mData != null&&mData.size()>0) {
			PublicOtherBean bean = mData.get(position);
				if (!mData.get(position).getChildExpNo().equals("")&&!mData.get(position).getChildExpPrice().equals("")) {
					holder.et_kuaidinum.setText(mData.get(position).getChildExpNo());
					Log.e("value", bean.getChildExpNo());
					Log.e("value", bean.getChildExpPrice()+"");
					holder.text_money.setText(mData.get(position).getChildExpPrice());			
					holder.tv_INCREASING_NO.setText(position+1+"");
				}
			
				
		} 
		return convertView;
	}

	public List<PublicOtherBean> Datalist() {
		return mData;
	}
    
	public class ViewHolder {
		TextView et_kuaidinum, text_money;
		ImageView btn_revise;
		TextView tv_INCREASING_NO;
	}
}
