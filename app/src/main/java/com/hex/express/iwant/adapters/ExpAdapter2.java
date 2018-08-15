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

public class ExpAdapter2 extends BaseAdapter {
	private Context context;
	private List<kuaidiBean> data = new ArrayList<kuaidiBean>();
	public ExpAdapter2(SelectExpCompanyActivity context,
			List<kuaidiBean> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public kuaidiBean getItem(int position) {
		return (kuaidiBean)data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_expcompanyselect, null);
			//创建viewholder对象
			holder = new ViewHolder();
			holder.img_expimg = (ImageView) convertView.findViewById(R.id.img_expimg);
			holder.txt_expname = (TextView) convertView.findViewById(R.id.txt_expname);
			//设置标签
			convertView.setTag(holder);
		} else {
			//获取标签
			holder = (ViewHolder) convertView.getTag();
		}
		kuaidiBean bean = getItem(position);
		Picasso.with(context).load("http://www.efamax.com/images/expicon/Icon/"+data.get(position).expCode+".png").into(holder.img_expimg);
		holder.txt_expname.setText(bean.expName);
		return convertView;
	}
 static class ViewHolder{
	 ImageView img_expimg;
	 TextView txt_expname;
 }
}
