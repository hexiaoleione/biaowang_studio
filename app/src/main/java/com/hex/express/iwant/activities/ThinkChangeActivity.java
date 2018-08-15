package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.madmatrix.zxing.android.CaptureActivity;
import org.madmatrix.zxing.android.ViewfinderView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ThinkChangeActivity extends BaseActivity{

	@Bind(R.id.btn_cancel_scan)
	Button btn_cancel_scan;
	private ViewfinderView viewfinderView;
	public String expnum,codeId,phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thinchange);
		ButterKnife.bind(this);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		initData();
		initView();
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
		// TODO Auto-generated method stub
		btn_cancel_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(ThinkChangeActivity.this, CaptureActivity.class), 1);
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


@Override
protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	super.onActivityResult(arg0, arg1, arg2);
	switch (arg0) {
	
	case 4:
		if (arg1 == RESULT_OK && arg2 != null) {
			Bundle bundle = arg2.getExtras();
			codeId = bundle.getString("codeId");
			phone= bundle.getString("phone");
			Log.e("",expnum );
		}
		break;
	default:
		break;
	}
}
/**
 * 二维码登录
 */
private void getLlsurplus() {
//	if (!tv_needPayMoney.getText().toString().equals("")) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("codeId", codeId);
			obj.put("phone", phone);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("mlist", obj.toString());
		AsyncHttpUtils.doPut(ThinkChangeActivity.this, MCUrl.THINKCHANGE, obj.toString(), null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("josnnd", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
//						if (bean.getErrCode() == 0) {
//						} else {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());

//						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
//	} else {
//		ToastUtil.shortToast(ThinkChangeActivity.this, "");
//	}

}

}
