package com.hex.express.iwant.adapters;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.AddCourierActivity;
import com.hex.express.iwant.activities.AddCouriersActivity;
import com.hex.express.iwant.bean.CourierBean;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.squareup.picasso.Picasso;

public class CourierAdapter  extends BaseListAdapter {

	public CourierAdapter(Context context, List list) {
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
		@Bind(R.id.image_send) Button image_send;
		public MyViewHodler(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			CourierBean bean = new CourierBean();
			bean.data=list;
			Log.e("ddddddd",bean.data.get(0).expName);
			if(bean.data.get(position).userId!=0){
				image_send.setVisibility(View.VISIBLE);
				image_send.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {

						View view=LayoutInflater.from(context).inflate(R.layout.item_plane,null);
						final AlertDialog dialog = DialogUtils.createAlertDialog(context, "温馨提示", "快递员列表后面有纸飞机的，\n表示您添加过或者选择过的快递员", 0, null, true, true);
						//dialog.addContentView(view, new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
						dialog.show();
					}
				});
			}
			txt_expname.setText(bean.data.get(position).expName);
			rab_couriersscore.setRating(bean.data.get(position).pickupScore);
			rab_couriersscore.setClickable(false);
			txt_couriername.setText(bean.data.get(position).courierName);
			txt_couriermobile.setText(bean.data.get(position).courierMobile);
			Picasso.with(context).load("http://www.efamax.com/images/expicon/Icon/"+bean.data.get(position).expCode+".png").into(img_expimag);
		}
	}
}
