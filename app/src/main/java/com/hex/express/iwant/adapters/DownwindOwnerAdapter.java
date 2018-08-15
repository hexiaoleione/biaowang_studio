package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownDetialTrackActivity;
//import com.hex.express.iwant.activities.DownDetialTrackActivity;
import com.hex.express.iwant.activities.EscortMessageActivity;
import com.hex.express.iwant.bean.DownStrokeBean;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.views.MarqueeTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class DownwindOwnerAdapter extends BaseListAdapter {

	private ImageLoader loader;
	private DisplayImageOptions options;
	Context context;

	public DownwindOwnerAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new OwnerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_downwind;

	}

	class OwnerViewHolder extends ViewHolder {

		public OwnerViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.tv_publishTime)
		TextView tv_publishTime;
		@Bind(R.id.iv_img)
		ImageView iv_img;
		@Bind(R.id.iv_imgway)
		ImageView iv_imgway;
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_address)
		MarqueeTextView tv_address;
		@Bind(R.id.tv_addressTo)
		MarqueeTextView tv_addressTo;
		@Bind(R.id.tv_distance)
		TextView tv_distance;
		@Bind(R.id.img_phone)
		ImageView img_phone;
		@Override
		public void setData( final int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			 final DownStrokeBean bean = new DownStrokeBean();
			bean.data = list;
			// new MyBitmapUtils().display(iv_img,
			// bean.data.get(position).userHeadPath);

			loader = ImageLoader.getInstance();
			loader.init(ImageLoaderConfiguration.createDefault(context));
			options = new DisplayImageOptions.Builder().cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
					.displayer(new RoundedBitmapDisplayer(100))// 设置成圆角度
					.build();

			if (!bean.data.get(position).userHeadPath.equals("")) {
				loader.displayImage(bean.data.get(position).userHeadPath, iv_img, options);
			} else {
				 iv_img.setBackgroundResource(R.drawable.moren);
			}
			iv_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					context.startActivity(new Intent(context, EscortMessageActivity.class).putExtra("driverId", String.valueOf(bean.data.get(position).userId)));

				}
			});
			img_phone.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					AppUtils.intentDial(context, bean.data.get(position).driverMobile);
					
				}
			});
			if (bean.data.get(position).transportationId.equals("1")) {
				iv_imgway.setBackgroundResource(R.drawable.car_topright);
			} else if (bean.data.get(position).transportationId.equals("2")) {
				iv_imgway.setBackgroundResource(R.drawable.bicycle_topright);
			} else {
				iv_imgway.setBackgroundResource(R.drawable.feiway);
			}
			
			tv_publishTime.setText(bean.data.get(position).publishTime);// 发布时间
			tv_address.setText(bean.data.get(position).address);// 发布的位置
			tv_name.setText(bean.data.get(position).userName);// 镖师名字
			tv_address.setText(bean.data.get(position).address.replace("中国", ""));// 发布地址
			tv_addressTo.setText(bean.data.get(position).addressTo.replace("中国", ""));// 收货地址
			tv_distance.setText(bean.data.get(position).leaveTime);
//			tv_distance.setText(bean.data.get(position).distance/1000.00+" Km");// 距离
//			if (bean.data.get(position).distance<1000) {
//				tv_distance.setText(bean.data.get(position).distance+" m");// 距离小于1000
//			}else {
//		
//			tv_distance.setText(bean.data.get(position).distance/1000.00+" Km");// 距离
//			}		
		}
	}
}
