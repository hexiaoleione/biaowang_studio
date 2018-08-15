package com.hex.express.iwant.adapters;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DrawCardActivity;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.adapters.SendOwnerAdapter.OwnerViewHolder;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DrawCardBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.GifView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

public class DrawCardAdapter extends BaseListAdapter{

	public DrawCardAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}


	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		return new OwnerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.item_newdrawcard;//item_newdrawcard

	}

	class OwnerViewHolder extends ViewHolder {

		public OwnerViewHolder(View itemView) {
			super(itemView);
		}

		@Bind(R.id.img_title)
		TextView img_title;
		@Bind(R.id.textView1)
		TextView textView1;
		@Bind(R.id.img_left)
		TextView img_left;
		@Bind(R.id.ll_a)
		RelativeLayout ll_a;

		
		private DrawCardBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new DrawCardBean();
			bean.data = list;
			Log.e("BEAM", bean.data.get(position).toString());
			img_title.setText(bean.getData().get(position).couponName);
			textView1.setText(bean.getData().get(position).couponFrom);
			img_left.setText("领取*"+bean.getData().get(position).couponCount);
			if ("0".equals(bean.getData().get(position).couponCount)) {
				img_left.setText("已领完");
				ll_a.setBackgroundDrawable(null);
				ll_a.setBackgroundResource(R.drawable.newdraw_bgs);
			}
//		}
			img_left.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.RecriveCoupon, "userId", String
							.valueOf(PreferencesUtils.getInt(context,
									PreferenceConstants.UID)), "conditionId", String
							.valueOf(bean.getData().get(position).conditionId)), null,
							null, null, new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.e("json", "*************************************"
											+ new String(arg2));
							BaseBean		drabsean = new Gson().fromJson(new String(arg2),
									BaseBean.class);
							if (drabsean.getErrCode()==0) {
								ToastUtil.shortToast(context, drabsean.getMessage());
								
							}
							ToastUtil.shortToast(context, drabsean.getMessage());
								}
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2,
										Throwable arg3) {
									// TODO Auto-generated method stub

								}
							});
				}
			});
		}
	}

}
