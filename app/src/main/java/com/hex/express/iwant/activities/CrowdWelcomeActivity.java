package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;


import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.CrowdfundingImageBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 【我要融资】非股东界面
 * 
 * @author SCHT-50
 * 
 */
public class CrowdWelcomeActivity extends BaseActivity {
	@Bind(R.id.btn_submit)
	Button btn_submit;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.img_welcome)
	ImageView img_welcome;
	@Bind(R.id.img_welcometwo)
	ImageView img_welcometwo;
	CrowdfundingImageBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowdwelcome);
		ButterKnife.bind(CrowdWelcomeActivity.this);
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		tbv_show.setTitleText("镖王融资");
		getImgeUrl();
		// SharedPreferences sp =getSharedPreferences(
		// "config", MODE_PRIVATE);
		// if (!sp.getString("topImageUrl", "").equals("")
		// && !sp.getString("bottomImageUrl", "").equals("")) {
		// new MyBitmapUtils().display(img_welcome,
		// sp.getString("topImageUrl", ""));
		// new MyBitmapUtils().display(img_welcometwo,
		// sp.getString("bottomImageUrl", ""));
		// } else {
		//
		// }

	}

	private void getImgeUrl() {
		AsyncHttpUtils.doGet(MCUrl.CROWDFUNDINGIMAGE, null, null, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						ToastUtil.shortToast(CrowdWelcomeActivity.this,
								bean.getMessage());
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("77777777777777", new String(arg2));
						bean = new Gson().fromJson(new String(arg2),
								CrowdfundingImageBean.class);
//						SharedPreferences sp = CrowdWelcomeActivity.this
//								.getSharedPreferences("config", MODE_PRIVATE);
//						Editor editor = sp.edit();
//						editor.putString("topImageUrl",
//								bean.data.get(0).topImageUrl);
//						editor.putString("bottomImageUrl",
//								bean.data.get(0).bottomImageUrl);
//						editor.commit();
						new MyBitmapUtils().display(img_welcome,
								bean.data.get(0).topImageUrl);
						new MyBitmapUtils().display(img_welcometwo,
								bean.data.get(0).bottomImageUrl);
					}

				});

	}

	@Override
	public void setOnClick() {
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtils.intentDial(CrowdWelcomeActivity.this, "01052873063");
//				startActivity(new Intent(CrowdWelcomeActivity.this,
//						CrowdConsultActivity.class));

			}
		});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
