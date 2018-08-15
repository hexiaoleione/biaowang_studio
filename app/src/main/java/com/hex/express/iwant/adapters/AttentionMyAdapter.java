package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.adapters.MylisOfferAdapter.MylisOfferViewHoder;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class AttentionMyAdapter extends BaseListAdapter{

	public AttentionMyAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MylisOfferViewHoder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_attent;
	}

	class MylisOfferViewHoder extends ViewHolder{

		public MylisOfferViewHoder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.attent_delete)//删除按钮
		ImageView attent_delete;
		@Bind(R.id.attent_end)//到达地址
		TextView attent_end;
		@Bind(R.id.attent_start)//起发地址
		TextView attent_start;
		private CardBean cardBean;

		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			cardBean = new CardBean();
			cardBean.data = list;
		final int string=	cardBean.getData().get(position).userId;
			attent_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					addPostResult(string);
					
				}
			});
		}
		private void addPostResult(int  messageId) {
			JSONObject obj = new JSONObject();
			
//			try {
//				obj.put("messageId",String.valueOf(messageId));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			Log.e("查看数据", obj.toString());//doPostJson     dosPut
			Log.e("111", MCUrl.LOGISTICSLIST_DEKETE + String.valueOf(messageId));
			AsyncHttpUtils.doPut(context,
					MCUrl.ATTENT_Delete +String.valueOf(messageId),
					obj.toString(), null,new AsyncHttpResponseHandler() {
//			AsyncHttpUtils.dosPut(context, MCUrl.LOGISTICSLIST_DEKETE, obj.toString(),
//					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("11111111111 wwww   ", new String(arg2));
							LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//							Log.e("oppop", bean.data.toString());
							ToastUtil.shortToast(context, bean.getMessage());
//							getHttprequst(false, true, 1, false);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						}
					});

		}
	}

		
}

