package com.hex.express.iwant.fragments;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.DownStrokeBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SendEscortWayFragment extends BaseItemFragment {
	
	// 获取镖师发布的一个行程
	private DownStrokeBean bean1;
	@Bind(R.id.loutEscore)
	LinearLayout loutEscore;
	// 取消行程按钮
	@Bind(R.id.cancel_order)
	TextView cancel_order;
	@Bind(R.id.today_time)
	TextView today_time;
	@Bind(R.id.today_boum)
	TextView today_boum;
	@Bind(R.id.today_redboum)
	TextView today_redboum;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_escort_way, container, false);
		ButterKnife.bind(this, rootView);
		initData();
		return rootView;
		
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getDriverToOne();
		cancel_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("msg", UrlMap.getUrl(MCUrl.CANCLEDRIVERROUTETOONE, "recId", bean1.data.get(0).recId));
				AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.CANCLEDRIVERROUTETOONE, "recId", bean1.data.get(0).recId),
						null, null, null, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								Log.e("cancle", new String(arg2));
								loutEscore.setVisibility(View.GONE);

							}
						});

			}
		});
	}

	/**
	 * 获取镖师自己发布的一条行程
	 */
	private void getDriverToOne() {
		RequestParams params = new RequestParams();
		Log.e("mdgread", UrlMap.getUrl(MCUrl.DRIVERROUTETOONE, "driverId", String
				.valueOf(PreferencesUtils.getInt(getActivity().getApplicationContext(), PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.DRIVERROUTETOONE, "driverId",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("ssdfffddffd", "" + new String(arg2));
						bean1 = new Gson().fromJson(new String(arg2), DownStrokeBean.class);
						if (bean1.getErrCode() == 0) {
							loutEscore.setVisibility(View.VISIBLE);
							view_null_message.setVisibility(View.GONE);
							today_time.setText(bean1.data.get(0).publishTime);
							today_boum.setText(bean1.data.get(0).address);
							today_redboum.setText(bean1.data.get(0).addressTo);
						}else if (bean1.getErrCode() == -2) {
							view_null_message.setVisibility(View.VISIBLE);
							loutEscore.setVisibility(View.GONE );
						}

					}

				});
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
