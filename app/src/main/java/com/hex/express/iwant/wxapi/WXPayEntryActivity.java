package com.hex.express.iwant.wxapi;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Space;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.EvaluateActivity;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;
	private String businessId;
	private String billCodetwo;
	private String billCode;
	private SharedPreferences sp;
	private SharedPreferences sp1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, CollectionKey.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			String msg = "";
			if (resp.errCode == -2) {
				// 用户取消
				msg = "用户已取消";
				MessageUtils.alertMessageCENTER(getApplicationContext(), msg);
				finish();
			} else if (resp.errCode == -1) {
				// 支付错误
				msg = "充值失败";
				MessageUtils.alertMessageCENTER(getApplicationContext(), msg);
				finish();
			} else if (resp.errCode == 0) {
				msg = "充值成功";
				MessageUtils.alertMessageCENTER(getApplicationContext(), msg);
				sendBroadcast(new Intent().setAction("recharge"));
				finish();

			}

		}
	}

}