package com.hex.express.iwant.adapters;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownEscoreDartActivity;
import com.hex.express.iwant.activities.DownEscortDetialsActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.NewCenterActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.DituActivity;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
/**
 * 【顺风速递】的【附近镖件】列表的适配器
 * @author han
 *
 */
public class DownwindEscortAdapter extends BaseListAdapter {

	private ImageLoader loader;
	private DisplayImageOptions options;
	Context mContext;

	public DownwindEscortAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new OwnerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_newdownwind;//  item_newdownwind     item_downwind_escort

	}

	class OwnerViewHolder extends ViewHolder {

		public OwnerViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.iv_imgdingwei)
		ImageView iv_imgdingwei;
		@Bind(R.id.iv_phone)
		ImageView iv_phone;
		@Bind(R.id.imgtv_wancheng)
		ImageView imgtv_wancheng;
		
		@Bind(R.id.tv_publishTime)
		TextView tv_publishTime;
//		@Bind(R.id.ll_arrivingTime)
//		LinearLayout ll_arrivingTime;
//		@Bind(R.id.tv_arrivingTime)
//		TextView tv_arrivingTime;
//		@Bind(R.id.tv_ownerName)
//		TextView tv_ownerName;
		@Bind(R.id.tv_nameOfGood)
		TextView tv_nameOfGood;
		@Bind(R.id.tv_address)
		MarqueeTextView tv_address;
		@Bind(R.id.tv_addressTo)
		MarqueeTextView tv_addressTo;
		@Bind(R.id.tv_distance)
		TextView tv_distance;
		@Bind(R.id.tv_transferMoney)
		TextView tv_transferMoney;
		@Bind(R.id.time_r)
		RelativeLayout  timr_e;
		@Bind(R.id.tv_collection)
		TextView tv_collection;
		@Bind(R.id.tv_wancheng)
		TextView tv_wancheng;
		@Bind(R.id.tv_guige)
		TextView tv_guige;
//		@Bind(R.id.tv_tiji)
//		TextView tv_tiji;
		@Bind(R.id.tv_ramker)
		TextView tv_ramker;
//		@Bind(R.id.tv_usetime)
//		TextView tv_usetime;
		@Bind(R.id.tv_zhong)
		TextView tv_zhong;
		@Bind(R.id.tv_jianshu)
		TextView tv_jianshu;
		@Bind(R.id.iv_jiedan)
		ImageView iv_jiedan;
		
		
		
		
		@Override
		public void setData(final int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			final DownSpecialBean bean = new DownSpecialBean();
			bean.data = list;

			//如果是限时专递：
//			if (bean.data.get(position).limitTime.length() != 0) {
//				ll_arrivingTime.setVisibility(View.VISIBLE);
//			}
			// 将图片设置成圆角；
//			if (!bean.data.get(position).limitTime.equals("")) {
////				new MyBitmapUtils().display(iv_imgOfGood, bean.data.get(position).matImageUrl);
//				iv_imgOfGood.setBackgroundResource(R.drawable.xianshim);
//			}else {
//				iv_imgOfGood.setBackgroundResource(R.drawable.shunfengm);
//			}      
			
			if (bean.data.get(position).ifReplaceMoney.equals("true") && !"".equals(bean.data.get(position).ifReplaceMoney)
					&& !bean.data.get(position).replaceMoney.equals("0.0")) {
				tv_collection.setVisibility(View.VISIBLE);
				tv_collection.setText("代收款："+bean.data.get(position).replaceMoney+" 元");
			}else {
				tv_collection.setVisibility(View.GONE);
			}
			if (bean.data.get(position).limitTime.equals("")) {
				tv_publishTime.setText("要求到达时间："+bean.data.get(position).useTime);// 发布时间；
			}else {
				tv_publishTime.setText("要求到达时间："+bean.data.get(position).limitTime);// 发布时间；
			}
			
			if (bean.data.get(position).matVolume.equals("")) {
				tv_guige.setText("要求车型：无要求");
			}else {
				tv_guige.setText("要求车型："+bean.data.get(position).matVolume);
			}
			
//			if (!bean.data.get(position).length.equals("0") && !bean.data.get(position).wide.equals("0") && !bean.data.get(position).high.equals("0")) {
//				tv_guige.setText("单件规格：长："+bean.data.get(position).length+"厘米  "+"  宽："+bean.data.get(position).wide+"厘米  "+"   高："+bean.data.get(position).high+"厘米   ");
//			}else if (!bean.data.get(position).length.equals("0")) {
//				if (!bean.data.get(position).wide.equals("0")) {
//					tv_guige.setText("单件规格：长："+bean.data.get(position).length+"厘米  "+"  宽："+bean.data.get(position).wide+"厘米  ");
//				}else if (!bean.data.get(position).high.equals("0")) {
//					tv_guige.setText("单件规格：长："+bean.data.get(position).length+"厘米  "+"  高："+bean.data.get(position).high+"厘米  ");
//				}else {
//					tv_guige.setText("单件规格：长："+bean.data.get(position).length+"厘米  ");
//				}
//			}else if (!bean.data.get(position).wide.equals("0")) {
//				if (!bean.data.get(position).high.equals("0")) {
//					tv_guige.setText("单件规格：宽："+bean.data.get(position).wide+"厘米  "+"  高："+bean.data.get(position).high+"厘米  ");
//				}else {
//					tv_guige.setText("单件规格：宽："+bean.data.get(position).wide+"厘米  ");
//				}
//			}else if (!bean.data.get(position).high.equals("0")) {
//				tv_guige.setText("单件规格：高："+bean.data.get(position).high+"厘米  ");
//			}else if (bean.data.get(position).length.equals("0") && !bean.data.get(position).wide.equals("0") &&bean.data.get(position).high.equals("0")) {
//				tv_guige.setText("单件规格：用户未填写 ");
//			}
				
			
			if (bean.data.get(position).matWeight.equals("5")) {
				tv_zhong.setText("总重量：≤"+bean.data.get(position).matWeight+" 公斤");
			}else {
				tv_zhong.setText("总重量："+bean.data.get(position).matWeight+" 公斤");
			}
		if (bean.data.get(position).cargoSize.length()>0) {
			tv_jianshu.setText("件数："+bean.data.get(position).cargoSize+" 件");
		}else {
			tv_jianshu.setVisibility(View.GONE);
		}
			
//			tv_arrivingTime.setText("送达:"+bean.data.get(position).limitTime);// 货主期望镖件的送达时间时间；
//			if (!"".equals(bean.data.get(position).useTime)) {
//				tv_usetime.setText("取货时间："+bean.data.get(position).useTime);
//				tv_usetime.setVisibility(View.VISIBLE);
//			}else {
//				tv_usetime.setVisibility(View.GONE);
//			}
//			if (!"".equals(bean.data.get(position).carLength)) {
//			if(bean.data.get(position).carLength.equals("1")){
//				tv_carchang.setText("车长需求：无");
//				iv_imgOfGood.setBackgroundResource(R.drawable.shunfengm);
//			}else if(bean.data.get(position).carLength.equals("2")){
//				tv_carchang.setText("车长需求：1.8米");
//				iv_imgOfGood.setBackgroundResource(R.drawable.yimi);
//			}else if (bean.data.get(position).carLength.equals("3")) {
//				tv_carchang.setText("车长需求：2.7米");
//				iv_imgOfGood.setBackgroundResource(R.drawable.liangmi);
//			}else if (bean.data.get(position).carLength.equals("4")) {
//				tv_carchang.setText("车长需求：4.2米");
//				iv_imgOfGood.setBackgroundResource(R.drawable.simi);
//			}
//			tv_carchang.setVisibility(View.VISIBLE);
//			}else {
//				tv_carchang.setVisibility(View.GONE);
//			}
//			if (!"".equals(bean.data.get(position).matVolume)) {
//				tv_tiji.setText("体积："+bean.data.get(position).matVolume);
//				tv_tiji.setVisibility(View.VISIBLE);;
//			}else {
//				tv_tiji.setVisibility(View.GONE);;
//			}
			if (!"".equals(bean.data.get(position).matRemark)) {
				tv_ramker.setText("备注："+bean.data.get(position).matRemark+"。若因此产生额外费用，请提前协商！");
				tv_ramker.setVisibility(View.VISIBLE);;
			}else {
				tv_ramker.setVisibility(View.GONE);;
			}
		
		
//			if (!bean.data.get(position).carType.equals("")) {
//				if (bean.data.get(position).carType.equals("smallTruck")) {
//					tv_ownerName.setText("要求车型：小货车");
//				}else if(bean.data.get(position).carType.equals("middleTruck")){
//					tv_ownerName.setText("要求车型：中货车");
//				}else if(bean.data.get(position).carType.equals("smallMinibus")){
//					tv_ownerName.setText("要求车型：小面包车");
//				}else if(bean.data.get(position).carType.equals("middleMinibus")){
//					tv_ownerName.setText("要求车型：中面包车");
//				}else {
//					tv_ownerName.setVisibility(View.GONE);
//				}
//			}else {
//				tv_ownerName.setVisibility(View.GONE);
//			}
			if (!bean.data.get(position).status.equals("1")) {
				tv_wancheng.setText("");
				tv_wancheng.setVisibility(View.VISIBLE);
//				imgtv_wancheng.setVisibility(View.VISIBLE);
				tv_collection.setVisibility(View.GONE);
				iv_phone.setVisibility(View.GONE);
				iv_imgdingwei.setClickable(false);
				iv_jiedan.setBackgroundResource(R.drawable.new_bg_yibutton);
			}else {
				iv_phone.setVisibility(View.VISIBLE);
				iv_imgdingwei.setClickable(true);
				tv_wancheng.setVisibility(View.GONE);
//				imgtv_wancheng.setVisibility(View.GONE);
				iv_jiedan.setBackgroundResource(R.drawable.new_jiedan_bg);
			}
			tv_nameOfGood.setText("物品名称：" + bean.data.get(position).matName);
			tv_address.setText("始发地：" + bean.data.get(position).address.replace("中国", ""));
			tv_addressTo.setText("目的地：" + bean.data.get(position).addressTo.replace("中国", ""));
//			if (Integer.parseInt(bean.data.get(position).distance)<1000) {
//				tv_distance.setText("距离:大约"+bean.data.get(position).distance+"  米");// 距离小于1000
//			}else {
			String disString=bean.data.get(position).distance;
//			text_juli.setText("运送距离："+(float)(Integer.parseInt(disString))/1000+"公里");
				tv_distance.setText("距离:大约" + (float)(Integer.parseInt(disString))/1000+"  公里");
//			}
			DecimalFormat df = new DecimalFormat("######0.00");
			tv_transferMoney.setText("运费" +bean.data.get(position).transferMoney+" 元");// 注意数据类型
      
			iv_phone.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AppUtils.intentDial(context, bean.data.get(position).mobileTo);
				}
			});
			
			
			iv_imgdingwei.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, DituActivity.class);
					intent.putExtra("recId", String.valueOf(bean.data.get(position).recId));
					intent.putExtra("publishTime", bean.data.get(position).publishTime);
					intent.putExtra("mobile", bean.data.get(position).mobile);
					intent.putExtra("address", bean.data.get(position).address.replace("中国", ""));
					intent.putExtra("addressTo", bean.data.get(position).addressTo.replace("中国", ""));
					intent.putExtra("matName", bean.data.get(position).matName);
//					intent.putExtra("transferMoney", String.valueOf(data.transferMoney));
					intent.putExtra("transferMoney", bean.data.get(position).transferMoney);
					intent.putExtra("matRemark", bean.data.get(position).matRemark);
					intent.putExtra("matImageUrl", bean.data.get(position).matImageUrl);
					intent.putExtra("fromLatitude", String.valueOf(bean.data.get(position).fromLatitude));
					intent.putExtra("fromLongitude", String.valueOf(bean.data.get(position).fromLongitude));
					intent.putExtra("toLatitude", String.valueOf(bean.data.get(position).toLatitude));
					intent.putExtra("toLongitude", String.valueOf(bean.data.get(position).toLongitude));
					intent.putExtra("length", bean.data.get(position).length);
					intent.putExtra("wide", bean.data.get(position).wide);
					intent.putExtra("high", bean.data.get(position).high);
					intent.putExtra("matWeight", bean.data.get(position).matWeight);
					intent.putExtra("replaceMoney", bean.data.get(position).replaceMoney);
					intent.putExtra("ifReplaceMoney", bean.data.get(position).ifReplaceMoney);
					intent.putExtra("ifTackReplace", bean.data.get(position).ifTackReplace);
					intent.putExtra("limitTime",  bean.data.get(position).limitTime);
			 		Log.e("recId", bean.data.get(position).recId + "");
					context.startActivity(intent);
//			 		context.getApplicationInfo().startActivityForResult(intent, 11);

				}
			});
		}
	}
}
