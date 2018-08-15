package com.hex.express.iwant.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.views.MarqueeTextView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class SendEscortAdapter extends BaseListAdapter {

	public SendEscortAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new OwnerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_newsendfreight;//item_sendfreight  item_newsendfreight
		
	}
	class OwnerViewHolder extends ViewHolder{

		public OwnerViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.text_time)
		TextView text_time;
		@Bind(R.id.iv_img)
		ImageView iv_img;
		@Bind(R.id.text_name)
		TextView text_name;
		@Bind(R.id.text_nameTo)
		TextView text_nameTo;
		@Bind(R.id.text_address)
		MarqueeTextView text_address;
		@Bind(R.id.text_range)
		TextView text_range;
		@Bind(R.id.replaceMoney)
		TextView replaceMoney;
		
		@Bind(R.id.text_state)
		TextView text_state;
		@Bind(R.id.text_quxiao)
		TextView text_quxiao;
		
		
		
		@Bind(R.id.iv_phone)
		ImageView iv_phone;
		@Bind(R.id.iv_details)
		ImageView iv_details;
		@Bind(R.id.iv_distance)
		ImageView iv_distance;
		@Bind(R.id.iv_Order_finish)//押镖成功
		ImageView iv_Order_finish;
		@Bind(R.id.iv_image)
		ImageView iv_image;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			DownSpecialBean bean=new DownSpecialBean();
			bean.data=list;
//			text_time.setTextColor(context.getResources().getColor(R.color.orange1));
			text_time.setText("要求到达时间："+bean.data.get(position).publishTime);
			text_nameTo.setText(bean.data.get(position).personNameTo);
			text_name.setText(bean.data.get(position).matName);
			text_address.setText(bean.data.get(position).addressTo.replace("中国", ""));
			if (bean.data.get(position).ifReplaceMoney.equals("true") && !bean.data.get(position).ifReplaceMoney.equals("")) {
				replaceMoney.setVisibility(View.VISIBLE);
				replaceMoney.setText("代收款："+bean.data.get(position).replaceMoney+"元");
			}else {
				replaceMoney.setVisibility(View.GONE);
			}
			
			if (bean.data.get(position).status.equals("5")||bean.data.get(position).status.equals("7")) {
				iv_Order_finish.setVisibility(View.VISIBLE);
				iv_image.setVisibility(View.GONE);
			}else {
				iv_image.setVisibility(View.VISIBLE);
				iv_Order_finish.setVisibility(View.GONE);
			}
			if (bean.data.get(position).status.equals("4") && !bean.data.get(position).ifAgree.equals("")) {
				iv_Order_finish.setVisibility(View.VISIBLE);
				iv_image.setVisibility(View.GONE);
//				iv_Order_finish.setBackgroundResource(R.drawable.huowuweigui);
			} else if(bean.data.get(position).status.equals("4") || bean.data.get(position).status.equals("8") ){
				
				iv_Order_finish.setVisibility(View.VISIBLE);
				iv_image.setVisibility(View.GONE);
//				iv_Order_finish.setBackgroundResource(R.drawable.bg_quexiao);
				
			}
			// /状态 0-预发布成功(未支付) 1-支付成功(已支付)  2(已抢单) 3 已取件(不需要代收款) 4 订单取消(镖师)  5 成功  6 系统删除 7 已评价 8订单取消(用户)
            //  9 货物违规状态(镖师点击货物违规按钮后,用户界面出现是否同意的按钮)
			if (bean.data.get(position).status.equals("1")) {
				text_state.setText("");
			}else if (bean.data.get(position).status.equals("2")) {
				text_state.setText("未取件");
			} else if (bean.data.get(position).status.equals("3")) {
				text_state.setText("未送达");
			} else if (bean.data.get(position).status.equals("4")) {
				if (!bean.data.get(position).ifAgree.equals(0)) {
					text_quxiao.setText("货物违规取消");
				}else {
					text_quxiao.setText("镖师取消取消");
				}
				
			}else if (bean.data.get(position).status.equals("5")) {
				text_quxiao.setText("已完成");
			}else if (bean.data.get(position).status.equals("7")) {
				text_quxiao.setText("已完成");
			}else if (bean.data.get(position).status.equals("8")) {
				text_quxiao.setText("用户取消");
			}
			else if (bean.data.get(position).status.equals("9")) {
				text_quxiao.setText("货物违规");
			}
			
			
			
//			if (!bean.data.get(position).matImageUrl.equals("")) {
//				new MyBitmapUtils().display(iv_img, bean.data.get(position).matImageUrl);
//			}else {
//				iv_img.setBackgroundResource(R.drawable.ren3);
//			}
			// 将图片设置成圆角；
			if (!bean.data.get(position).limitTime.equals("")) {
//				new MyBitmapUtils().display(iv_imgOfGood, bean.data.get(position).matImageUrl);
				iv_img.setBackgroundResource(R.drawable.xianshim);
			}else {
				iv_img.setBackgroundResource(R.drawable.shunfengm);
			} 
			if (!"".equals(bean.data.get(position).carLength)) {
				if(bean.data.get(position).carLength.equals("1")){
					iv_img.setBackgroundResource(R.drawable.shunfengm);
				}else if(bean.data.get(position).carLength.equals("2")){
					iv_img.setBackgroundResource(R.drawable.yimi);
				}else if (bean.data.get(position).carLength.equals("3")) {
					iv_img.setBackgroundResource(R.drawable.liangmi);
				}else if (bean.data.get(position).carLength.equals("4")) {
					iv_img.setBackgroundResource(R.drawable.simi);
				}
			}
				try
				{
				Date date=new Date();
				 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				 String time=format.format(date);
				  Date d1 = df.parse(bean.data.get(position).limitTime);
				  Date d2 = df.parse(time);
				  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
				  long days = diff / (1000 * 60 * 60 * 24);
				  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
				  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
				  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
	         if(minutes<0){
	        	 iv_Order_finish.setVisibility(View.VISIBLE);
	 	         iv_Order_finish.setBackgroundResource(R.drawable.chao_nosmill);
	              }
				}
				catch (Exception e)
				{
				}
			
		}
		
	}
	
	
}
