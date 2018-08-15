package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.AddReceiverActivity;
import com.hex.express.iwant.activities.ChangeAddressActivity;
import com.hex.express.iwant.activities.ChangeReceiverActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.SendPersonBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ReceiverAdapter extends BaseListAdapter {
	private RelativeLayout rl;
	public ReceiverAdapter(Context context, List list) {
		super(context, list);
	}
	public ReceiverAdapter(Context context,List list,RelativeLayout rl){
		super(context, list);
		this.rl=rl;
	}

	class MyViewHolder extends ViewHolder {
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_address)
		TextView tv_address;
		@Bind(R.id.tv_send_name)
		TextView tv_send_name;
		@Bind(R.id.tv_send_address)
		TextView tv_send_address;
		@Bind(R.id.tv_address_two)
		TextView tv_address_two;
		@Bind(R.id.tv_phone)
		TextView tv_phone;
//		@Bind(R.id.tv_mydefault) TextView tv_mydefault;
//		@Bind(R.id.tv_default)TextView tv_default;
		@Bind(R.id.tv_default_edit) LinearLayout tv_default_edit;
		@Bind(R.id.iv_delete) ImageView iv_delete;
		public MyViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void setData(final int position) {
			super.setData(position);
			final SendPersonBean bean=new SendPersonBean();
			bean.data=list;
			tv_name.setText(bean.data.get(position).personName);
			tv_address.setText(bean.data.get(position).cityName);
			tv_send_name.setText(bean.data.get(position).personName);
			tv_send_address.setText(bean.data.get(position).areaName);
			tv_address_two.setText(bean.data.get(position).address);
			tv_phone.setText(bean.data.get(position).mobile);
			iv_delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final LoadingProgressDialog dialog=new LoadingProgressDialog(context);
					dialog.show();
					//bean.data.remove(position);
					AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.REMOVE, "addressId",bean.data.get(position).addressId+""), new AsyncHttpResponseHandler() {
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							BaseBean bean=new Gson().fromJson(new String(arg2),BaseBean.class);
							if(bean.getErrCode()==0){
								ToastUtil.shortToast(context,"删除成功");
								list.remove(position);
								notifyDataSetChanged();
								if(list.size()==0){
									rl.setVisibility(View.VISIBLE);
								}
							}
							else{
								ToastUtil.shortToast(context, "删除失败");
							}
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							
						}
					});
					
				}
			});
			tv_default_edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(context, ChangeReceiverActivity.class);
					intent.putExtra("addressId", bean.data.get(position).addressId);
					intent.putExtra("personName", bean.data.get(position).personName);
					intent.putExtra("areaName", bean.data.get(position).areaName);
					intent.putExtra("address", bean.data.get(position).address);
					intent.putExtra("mobile", bean.data.get(position).mobile);
					intent.putExtra("cityName", bean.data.get(position).cityName);
					intent.putExtra("cityCode", bean.data.get(position).cityCode);
					context.startActivity(intent);
				}
			});
//			if(bean.data.get(position).ifDefault==true){
////				tv_mydefault.setVisibility(View.VISIBLE);
//			}
		}

	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MyViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_receiver;
	}

}

