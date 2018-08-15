package com.hex.express.iwant.activities;

import java.text.DecimalFormat;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.TranferBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.mypasspay.MypassActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请转账界面
 * 
 * @author SCHT-50
 * 
 */
public class TransferActivity extends BaseActivity {
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btnRight)
	ImageView btnRight;
	@Bind(R.id.iv_transfer)
	ImageView iv_transfer;
	@Bind(R.id.edit_phone)
	EditText edit_phone;
	@Bind(R.id.edit_money)
	EditText edit_money;
	@Bind(R.id.btn_submit)
	Button btn_submit;
	@Bind(R.id.transfer_money)
	TextView transferMoney;
	@Bind(R.id.text_waitmoney)
	TextView text_waitmoney;
	public String phone_number;
	public TranferBean bean;
	private LoadingProgressDialog dialog;
	private boolean isPayPasswordExits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
		setOnClick();
		initView();
	}

	// 这是注释：
	@OnClick({ R.id.iv_transfer, R.id.btn_submit })
	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.iv_transfer:
			// Uri uri = Uri.parse("content://contacts/people");
//			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			startActivityForResult(intent, 0);
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			startActivityForResult(intent, 0);
			break;
		case R.id.btn_submit:
			// ToastUtil.shortToast(TransferActivity.this, "dian");
			String string =edit_phone.getText().toString();
			  String tmpstr=string.replace(" ","");
			if (!StringUtil.isMobileNO(tmpstr)
					|| (tmpstr.length() != 11)) {
				ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号");
				return;
			} else {
				if (!edit_money.getText().toString().equals("0") && !edit_money.getText().toString().equals("")&& edit_money.getText().toString()!=null) {
					
					if (bean.data.get(0).transferableMoney >= Double.parseDouble(edit_money.getText().toString().trim())) {
						// dialog.show();
						// PostRequstMoney();
//						Log.e("++++++TAG+++++++", PreferenceConstants.PAYPASSWORD);
						
						if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD)!=null
								&&!PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD).toString().equals("")) {
//							if(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD)!=null){
							// 输入支付密码:
							startActivity(new Intent(getApplicationContext(), MypassActivity.class)
									.putExtra("money", edit_money.getText().toString())
									.putExtra("phoneTo", tmpstr));
						} else {
							//跳转到设定支付密码:
							startActivity(new Intent(getApplicationContext(), SetupPasswordActivity.class));
						}
					} else {
						ToastUtil.shortToast(TransferActivity.this, "您的余额不足");
					}

				} else {
					ToastUtil.shortToast(TransferActivity.this, "请输入转账金额");
				}
			}
			break;
		default:
			break;
		}

	}
//
//	/**
//	 * 网络上传转账数据
//	 */
//	private void PostRequstMoney() {
//		JSONObject obj = new JSONObject();
//		try {
//			obj.put("mobileTo", edit_phone.getText().toString());
//			obj.put("tradeMoney", Double.parseDouble(edit_money.getText().toString()));
//			obj.put("userId",
//					Integer.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
//			Log.e("json", "++++++" + obj.toString());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		Log.e("json", "_____" + MCUrl.TRANFERMONEY);
//		AsyncHttpUtils.doPostJson(TransferActivity.this, MCUrl.TRANFERMONEY, obj.toString(),
//				new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
////						dialog.dismiss();
//					}
//
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						Log.e("json22", new String(arg2));
////						dialog.dismiss();
//						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
//						if (bean.isSuccess()) {
	//							AlertDialog.Builder ad = new Builder(TransferActivity.this);
//							ad.setTitle("温馨提示");
//							ad.setMessage("转账成功");
//							ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									Intent intent = new Intent(TransferActivity.this, MainActivity.class);
//									startActivity(intent);
//									finish();
//
//									ToastUtil.shortToast(getApplicationContext(), "转账成功");
//								}
//							});
//							ad.create().show();
//
//						} else {
//							ToastUtil.shortToast(TransferActivity.this, bean.getMessage());
//						}
//
//					}
//
//				});
//
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		if (resultCode == Activity.RESULT_OK) {
			ContentResolver reContentResolverol = getContentResolver();
			Uri contactData = data.getData();
			// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			// 获得DATA表中的名字
			// name =
			// cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 条件为联系人ID
			// edt_sendpersonname.setText(name);
//			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//			Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//			while (phone.moveToNext()) {
//				phone_number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				if (!phone_number.equals(""))
//					edit_phone.setText(phone_number);
//			}
			 String[] contact = new String[2];
             // 查看联系人有多少个号码，如果没有号码，返回0
				int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				String[] strs = cursor.getColumnNames();
	            for (int i = 0; i < strs.length; i++) {
	                if (strs[i].equals("data1")) {
	                    ///手机号
	                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
	                    edit_phone.setText(""+contact[1]);
	                }
	            }
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
	      edit_money.addTextChangedListener(new TextWatcher() {
			
	    	  @Override
	          public void onTextChanged(CharSequence s, int start, int before,
	                  int count) {
	              String text = s.toString();
	              if (text.contains(".")) {
	                  int index = text.indexOf(".");
	                  if (index + 3 < text.length()) {
	                      text = text.substring(0, index + 3);
	                      edit_money.setText(text);
	                      edit_money.setSelection(text.length());
	                  }
	              }
	          }

	          @Override
	          public void beforeTextChanged(CharSequence s, int start, int count,
	                  int after) {
	              // TODO Auto-generated method stub

	          }

	          @Override
	          public void afterTextChanged(Editable s) {
	              // TODO Auto-generated method stub

	          }
	      });
		}

	@Override
	public void initData() {
		//判断状态:
		SharedPreferences settings;
//	    Editor editor;
	    //设置
	    settings = getSharedPreferences("payPassword", Context.MODE_PRIVATE);
	    isPayPasswordExits = settings.contains("payPassword");//判断是否存在;
	    
//	    String payPassword = settings.getString("payPassword", "null");
	     
		dialog = new LoadingProgressDialog(TransferActivity.this);
//		tvb_show.setTitleText("转账");
		getrequstMoney();

	}

	/**
	 * 获取转账余额
	 */
	private void getrequstMoney() {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getUrl(MCUrl.TRANFERMYLEFT, "userId",
				String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.TRANFERMYLEFT, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("json", "##############" + new String(arg2));
						bean = new Gson().fromJson(new String(arg2), TranferBean.class);
						DecimalFormat df = new DecimalFormat("######0.00");
						transferMoney.setText(df.format(bean.data.get(0).transferableMoney));
						text_waitmoney.setText(bean.data.get(0).waitMoney);
					}

				});
	}

	@Override
	public void setOnClick() {
    btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 String url="http://www.efamax.com/mobile/moneyPolicy.html";
				    Intent intent=new Intent();
					intent.putExtra("url", url);
					intent.setClass(TransferActivity.this, HAdvertActivity.class);//公司
					startActivity(intent);
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
