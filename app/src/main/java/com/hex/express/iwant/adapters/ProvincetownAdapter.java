package com.hex.express.iwant.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.ProvCityActivity;
import com.hex.express.iwant.activities.ProvCityTownAcrivity;
import com.hex.express.iwant.bean.CitySelectBean;

public class ProvincetownAdapter extends BaseAdapter{

	private List<CitySelectBean> data = new ArrayList<CitySelectBean>();
	private Context context;
	public ProvincetownAdapter(ProvCityTownAcrivity context,
			List<CitySelectBean> result) {
		this.context = context;
		this.data = result;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CitySelectBean getItem(int position) {
		return (CitySelectBean)data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//定义一个ViewHolder
				ViewHolder holder;
				if (convertView == null) {
					//填充布局
					convertView = View.inflate(context, R.layout.city_select_item, null);
					//创建对象
					holder = new ViewHolder();
					//找到控件
					holder.selectName = (TextView) convertView.findViewById(R.id.txtSelectName);
					//设置标签
					convertView.setTag(holder);
				} else {
					//获取holder对象
					holder = (ViewHolder) convertView.getTag();
				}
				CitySelectBean address = getItem(position);
				String name = address.getName();
//				String code = address.getCode();
				holder.selectName.setText(name);
//				holder.selectName.setBackgroundColor(Color.BLUE);
//				 if (position == selectItem) {  
//					 holder.selectName.setTextColor(Color.RED);   //选中颜色
//		            }   
//		            else {  
//		            	holder.selectName.setTextColor(Color.BLACK);  //其他颜色
//		            } 
				return convertView;
	}
	 private int  selectItem; 
	public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
   } 
	static class ViewHolder{
		TextView selectName;
	}
}
