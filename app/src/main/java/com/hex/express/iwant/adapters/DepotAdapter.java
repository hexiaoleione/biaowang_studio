package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DepotActivity;
import com.hex.express.iwant.activities.DepotAddsActivity;
import com.hex.express.iwant.activities.EscortMessageActivity;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DepotBean;
import com.hex.express.iwant.bean.NearbyBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.Bind;

public class DepotAdapter extends BaseListAdapter{
	public DepotAdapter(Context context, List list) {
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
		return R.layout.depot_item;
	}
	class OwnerViewHolder extends ViewHolder {

		public OwnerViewHolder(View itemView) {
			super(itemView);
		}
		@Bind(R.id.depot_title)//市
		TextView depot_title;
		@Bind(R.id.depot_context)//具体地址
		TextView depot_context;
		@Bind(R.id.depot_compile)
		Button depot_compile;//修改
		@Bind(R.id.depot_delet)
		Button depot_delet;//删除
		@Bind(R.id.cb_xuan)//选择是否默认
		CheckBox cb_xuan;
		private DepotBean depobean;
	
	
	public void setData(final int position) {
		super.setData(position);
		depobean = new DepotBean();
		depobean.data = list;
		depot_title.setText(depobean.data.get(position).locationAddress);
		depot_context.setText(depobean.data.get(position).address);
		if (depobean.data.get(position).ifDefault) {
			cb_xuan.setChecked(true);
		}
		depot_compile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("locationAddress", depobean.data.get(position).locationAddress);
				intent.putExtra("address", depobean.data.get(position).address);
				intent.putExtra("cityCode", depobean.data.get(position).cityCode);
				intent.putExtra("way", "1");
				intent.setClass(context,	DepotAddsActivity.class);//密码支付
				context. startActivity(intent);
			}
		});
		depot_delet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				JSONObject obj = new JSONObject();
				try {
					obj.put("userId",
							String.valueOf(PreferencesUtils.getInt(context, PreferenceConstants.UID)));
					obj.put("recId", depobean.data.get(position).recId);
					obj.put("cityCode", depobean.data.get(position).cityCode);
					obj.put("locationAddress", depobean.data.get(position).locationAddress);
					obj.put("address", depobean.data.get(position).address);
					obj.put("latitude", depobean.data.get(position).latitude);
					obj.put("longitude", depobean.data.get(position).longitude);
					obj.put("ifDelete", true);
					obj.put("ifDefault", depobean.data.get(position).ifDefault);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				AsyncHttpUtils.doPostJson(context, MCUrl.UpdateComAdd, obj.toString(),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								Log.e("oppo", new String(arg2));
								BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
								if (bean.getErrCode() == 0) {
									
								}else {
									ToastUtil.shortToast(context, bean.getMessage());
								}
							}
							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							}
						});
			}
		});
	}
	 private void revisemesge(){
		   
	   }
	}
}
