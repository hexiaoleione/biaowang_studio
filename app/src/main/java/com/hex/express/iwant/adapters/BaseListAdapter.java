package com.hex.express.iwant.adapters;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.MessageBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import butterknife.ButterKnife;
import cn.sharesdk.framework.statistics.NewAppReceiver;

public abstract class BaseListAdapter extends BaseAdapter {
	public Context context;
	public List list;

	public BaseListAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null || list.size() == 0)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public void addData(List list) {
		this.list.addAll(list);

	}

	/**
	 * 删除item
	 * 
	 * @return
	 */
	public List deleteData() {
		List<Object> mList = new ArrayList<Object>();
		mList = list;
		return mList;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (list == null || list.size() == 0)
			return 0;
		return position;
	}

	public void setData(List list) {
		for (int i = 0; i < list.size(); i++) {
			this.list.remove(list.size() - i - 1);
		}
		this.list.addAll(0, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(getLayoutResource(), parent, false);
			holder = onCreateViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.setData(position);
		return convertView;
	}

	public class ViewHolder {
		private View itemView;

		public ViewHolder(View itemView) {
			this.itemView = itemView;
			ButterKnife.bind(this, itemView);
		}

		public void setData(int position) {
		}
	}

	public abstract ViewHolder onCreateViewHolder(View itemView);

	public abstract int getLayoutResource();
}
