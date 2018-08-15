package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SetupPasswordActivity extends Activity{
	
	private EditText et_setupPayCode,et_confirmPayCode;
	private Button btn_DONE;
	private TitleBarView tbv_show;
	
	 private SharedPreferences preferences;
	 private Editor editor;
	 String payCode01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_setuppassword);
		initView();
	}
	
	     void initView(){
	    	// 标题
	 	tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
	 	tbv_show.setTitleText("设定支付密码");
		et_setupPayCode = (EditText) findViewById(R.id.edt_setupPayCode);
		et_confirmPayCode = (EditText) findViewById(R.id.et_confirmPayCode);
		btn_DONE = (Button) findViewById(R.id.btn_DONE);
		
		btn_DONE.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				payCode01 = et_setupPayCode.getText().toString();
				String payCode02 = et_confirmPayCode.getText().toString();
				String toastContent;
				if ("".equals(payCode01)&&"".equals(payCode02)) {
					toastContent = "请输入您要设定的6位支付密码!";
					Toast.makeText(getApplicationContext(), toastContent,Toast.LENGTH_SHORT).show();
				} else if(!payCode01.equals(payCode02)){
					toastContent = "两次输入的支付密码不一致!";
					Toast.makeText(getApplicationContext(), toastContent,Toast.LENGTH_SHORT).show();
				}else if(payCode01.equals(payCode02)){
					PostTransferMoney();
				}
			}
		});
	}
	     
	     
	     
	     /**
	 	 * 设置支付密码:
	 	 */
	 	private void PostTransferMoney() {
	 		JSONObject obj = new JSONObject();
	 		try {
	 			obj.put("userId", Integer.valueOf(PreferencesUtils.getInt(
	 					getApplicationContext(), PreferenceConstants.UID)));
	 			obj.put("payPassword", MD5Util.MD5Encode(payCode01));
	 			obj.put("deviceId",DataTools.getDeviceId(getBaseContext()));
	 			Log.e("json", "++++++" + obj.toString());
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
	 		Log.e("json", "_____" + MCUrl.MYPASSWORD);
	 		AsyncHttpUtils.doPostJson(SetupPasswordActivity.this, MCUrl.MYPASSWORD,
	 				obj.toString(), new AsyncHttpResponseHandler() {

	 					@Override
	 					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
	 							Throwable arg3) {
//	 						dialog.dismiss();
	 					}

	 					@Override
	 					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	 						Log.e("json22", new String(arg2));
//	 						dialog.dismiss();
	 						BaseBean bean = new Gson().fromJson(new String(arg2),
	 								BaseBean.class);
	 						if (bean.isSuccess()) {
//	 							AlertDialog.Builder ad = new Builder(MypassActivity.this);
//	 							ad.setTitle("温馨提示");
//	 							ad.setMessage("转账成功");
//	 							ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//	 								public void onClick(DialogInterface dialog, int which) {
//	 									Intent intent = new Intent(MypassActivity.this,
//	 											MainActivity.class);
//	 									startActivity(intent);
//	 									finish();
//	 									
//	 									ToastUtil.shortToast(getApplicationContext(), "转账成功");
	 							
//	 								}
//	 							}); 
//	 							ad.create().show();
	 							
//	 							PostRequstMoney();
	 							
	 							//设置的支付密码存储到本地;
	 							Log.e("mima", MD5Util.MD5Encode(payCode01));
	 							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PAYPASSWORD,MD5Util.MD5Encode(payCode01));
	 							
//	 							PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD).equals("");
	 							Toast.makeText(getApplicationContext(), "设置成功!",Toast.LENGTH_SHORT).show();
	 							
	 							finish();
//	 							startActivity(new Intent(getApplicationContext(), MypassActivity.class));
	 							
	 						} else {
	 							ToastUtil.shortToast(SetupPasswordActivity.this,
	 									bean.getMessage());
	 						}

	 					}

	 				});

	 	}
	     
	     
}