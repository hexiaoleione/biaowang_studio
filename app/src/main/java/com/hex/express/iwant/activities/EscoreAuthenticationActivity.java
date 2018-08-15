package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.EscoreAuthenticateBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 镖师认证界面
 * 
 * @author huyichuan
 *
 */
public class EscoreAuthenticationActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tvb_show;
	@Bind(R.id.edt_registebankName)
	EditText edt_registebankName;
	@Bind(R.id.edt_bankCade)
	EditText edt_bankCade;
	private String checkName;
	private String checkIdCard;
	private String chenkPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_escore_authentication);
		ButterKnife.bind(EscoreAuthenticationActivity.this);
		initData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.btn_nextstep, R.id.img_camer, R.id.img_name })
	public void OnMyClick(View view) {
		switch (view.getId()) {
		case R.id.img_camer:

			break;
		case R.id.img_name:

			break;
		case R.id.btn_nextstep:// 下一步
			sendPostBank();
			break;

		default:
			break;
		}
	}

	@Override
	public void initData() {
		tvb_show.setTitleText("添加银行卡");
		checkName = getIntent().getStringExtra("checkName");
		checkIdCard = getIntent().getStringExtra("checkIdCard");
		chenkPath = getIntent().getStringExtra("chenkPath");
		Log.e("checkIdCard", checkIdCard);
		Log.e("checkName", checkName);
		if (!checkName.equals("") && !checkName.equals("null")) {
			edt_registebankName.setText(checkName);
		} else {
			edt_registebankName.setText("");
		}

	}

	public void sendPostBank() {
		// TODO 判断头像跟昵称是否为空 为空就不跳转 并提醒
		if (edt_bankCade.getText().toString().equals("") ||edt_registebankName.getText().toString().equals("")) {
			ToastUtil.shortToast(EscoreAuthenticationActivity.this, "请完善信息");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("checkName", checkName);
			obj.put("checkIdCard", checkIdCard);
			obj.put("checkPath", chenkPath);
			obj.put("checkType", "2");
			obj.put("bankCard", edt_bankCade.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("MSG", obj.toString());
		AsyncHttpUtils.doPostJson(EscoreAuthenticationActivity.this, MCUrl.CHECKADDCHECKINFO, obj.toString(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						// 返回代码
						// 0 您的信息已加入待审核列表，请耐心等待
						// -1 操作失败
						// -2 正在审核中，请耐心等待
						// -3 您已认证过快递员，镖师与快递员不可以同时认证

						dialog.dismiss();
						Log.e("msgkllnj", new String(arg2));
						RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getErrCode() == 0 || bean.getErrCode() == -2 || bean.getErrCode() == -3) {
							ToastUtil.shortToast(EscoreAuthenticationActivity.this, bean.getMessage());
							finish();
//							startActivity(new Intent(getApplicationContext(), MainActivity.class));
//							startActivity(new Intent(getApplicationContext(), MainTab.class));
							startActivity(new Intent(getApplicationContext(), NewMainActivity.class));
//							Intent intent = new Intent(CourierIdentificationActivity.this, NewMainActivity.class);
						} else {
							ToastUtil.shortToast(EscoreAuthenticationActivity.this, bean.getMessage());
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

						dialog.dismiss();

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
