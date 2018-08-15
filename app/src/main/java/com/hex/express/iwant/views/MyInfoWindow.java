package com.hex.express.iwant.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.CouriersCenterActivity;
import com.hex.express.iwant.bean.MapPointBean;
import com.hex.express.iwant.utils.AppUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

public class MyInfoWindow extends InfoWindow {
	ImageView iv_icon;
	TextView tv_address;
	TextView tv_name;
	TextView tv_name1;
	TextView tv_phone;
	TextView tv_phone1;
	TextView tv_distance;
	Button iv_phone;
	private MapPointBean.Data data;
	private View view;
	private double distance;
	private Context context;

	public MyInfoWindow(View arg0, LatLng arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public MyInfoWindow(Context context, View view, LatLng lat, int position,
			MapPointBean.Data data, double distance) {
		super(view, lat, position);
		this.data = data;
		this.view = view;
		this.distance = distance;
		this.context = context;
	}

	public void initData() {
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_address = (TextView) view.findViewById(R.id.tv_address_de);
		tv_name = (TextView) view.findViewById(R.id.tv_name_com);
		tv_name1 = (TextView) view.findViewById(R.id.tv_name1);
		tv_phone = (TextView) view.findViewById(R.id.tv_phone_de);
		tv_phone1 = (TextView) view.findViewById(R.id.tv_phone);
		tv_distance = (TextView) view.findViewById(R.id.tv_distance_de);
		iv_phone = (Button) view.findViewById(R.id.iv_phone);
		tv_address.setText(data.address);
		tv_name.setText(data.expName);
		tv_phone.setText(data.mobile);
		tv_name1.setText(data.expName);
		tv_distance.setText((int) distance + "米");
		if (!data.imagePath.equals("") && data.imagePath != null) {
			Picasso.with(context).load(data.imagePath).error(R.drawable.wu_cou)
					.into(iv_icon);
		}

		if (TextUtils.isEmpty(data.mobile)) {
			iv_phone.setVisibility(View.GONE);
			tv_phone.setVisibility(View.GONE);
			tv_phone1.setVisibility(View.GONE);
		} else {
			// iv_phone.setVisibility(View.VISIBLE);
			iv_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AppUtils.intentDial(context, data.mobile);
				}
			});
		}
	}
}
