package com.hex.express.iwant.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.SelectExpCompanyActivity;
import com.hex.express.iwant.bean.kuaidiBean;
import com.squareup.picasso.Picasso;

public class ExpAdapter extends BaseAdapter {
	private Context context;
	private List<kuaidiBean> data = new ArrayList<kuaidiBean>();
	private List<kuaidiBean> data2 = new ArrayList<kuaidiBean>();
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public ExpAdapter(SelectExpCompanyActivity context, List<kuaidiBean> data,
			List<kuaidiBean> data2) {
		this.context = context;
		this.data = data;
		this.data2 = data2;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public kuaidiBean getItem(int position) {
		return (kuaidiBean) data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		if (position == 6)
			return TYPE_1;
		else
			return TYPE_2;

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int type = getItemViewType(position);
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_expcompanyselect,
					null);
			// 创建viewholder对象
			holder = new ViewHolder();
			holder.img_expimg = (ImageView) convertView
					.findViewById(R.id.img_expimg);
			holder.txt_expname = (TextView) convertView
					.findViewById(R.id.txt_expname);
			holder.img_expimg1 = (ImageView) convertView
					.findViewById(R.id.img_expimg1);
			holder.txt_expname1 = (TextView) convertView
					.findViewById(R.id.txt_expname1);
			holder.other = (TextView) convertView.findViewById(R.id.other);
			// 设置标签
			convertView.setTag(holder);
		} else {
			// 获取标签
			holder = (ViewHolder) convertView.getTag();
		}
		switch (type) {
		case TYPE_1:
			holder.other.setVisibility(View.VISIBLE);
			break;

		case TYPE_2:
			holder.other.setVisibility(View.GONE);
			break;
		}
		kuaidiBean bean = getItem(position);
		kuaidiBean bean1 = data2.get(position);
		Picasso.with(context)
				.load("http://www.efamax.com/images/expicon/Icon/"
						+ bean.nameWord + ".png")
				.into(holder.img_expimg);
		// holder.img_expimg.setBackgroundResource(R.drawable.courier);
		holder.txt_expname.setText(bean.expName);
		Picasso.with(context)
				.load("http://www.efamax.com/images/expicon/Icon/"
						+ data2.get(position).nameWord + ".png")
				.into(holder.img_expimg1);
		// holder.img_expimg.setBackgroundResource(R.drawable.courier);
		holder.txt_expname1.setText(bean1.expName);
		return convertView;
	}

	static class ViewHolder {
		ImageView img_expimg;
		TextView txt_expname;
		ImageView img_expimg1;
		TextView txt_expname1;
		TextView other;
	}
}
