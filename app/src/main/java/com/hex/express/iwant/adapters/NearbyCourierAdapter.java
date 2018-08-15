package com.hex.express.iwant.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.CourierBean;
import com.hex.express.iwant.bean.NearbyCourierBean;

public class NearbyCourierAdapter extends BaseListAdapter{
	public NearbyCourierAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MyViewHodler(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_courierlayout;
	}
	class MyViewHodler extends ViewHolder{
		@Bind(R.id.img_expimag) ImageView img_expimag;
		@Bind(R.id.txt_expname) TextView txt_expname;
		@Bind(R.id.rab_couriersscore)RatingBar rab_couriersscore;
		@Bind(R.id.txt_couriername)  TextView txt_couriername;
		@Bind(R.id.txt_couriermobile) TextView txt_couriermobile;
		public MyViewHodler(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			NearbyCourierBean bean = new NearbyCourierBean();
			bean.data=list;
			Log.e("ddddddd",bean.data.get(0).expName);
			txt_expname.setText(bean.data.get(position).expName);
			rab_couriersscore.setRating(bean.data.get(position).pickupScore);
			rab_couriersscore.setClickable(false);
			txt_couriername.setText(bean.data.get(position).courierName);
			txt_couriermobile.setText(bean.data.get(position).courierMobile);
		}
	}
}
