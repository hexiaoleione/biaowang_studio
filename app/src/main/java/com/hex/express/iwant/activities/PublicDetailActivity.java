package com.hex.express.iwant.activities;

import java.text.DecimalFormat;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.madmatrix.zxing.android.CaptureActivity;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DiscountBean;
import com.hex.express.iwant.bean.WechatBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicDetailActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.tv_name)
	TextView tv_name;
	@Bind(R.id.tv_phone)
	TextView tv_phone;
	@Bind(R.id.tv_address)
	TextView tv_address;
	@Bind(R.id.ll_rece)
	LinearLayout ll_rece;
	@Bind(R.id.tv_re_name)
	TextView tv_re_name;
	@Bind(R.id.tv_re_phone)
	TextView tv_re_phone;
	@Bind(R.id.tv_re_address)
	TextView tv_re_address;
	@Bind(R.id.rb_warp)
	RadioButton rb_warp;
	@Bind(R.id.rb_file)
	RadioButton rb_file;
	@Bind(R.id.et_name)
	EditText et_name;
	@Bind(R.id.et_value)
	EditText et_value;
	@Bind(R.id.et_number)
	EditText et_number;
	@Bind(R.id.et_freight)
	EditText et_freight;
	@Bind(R.id.et_keep)
	EditText et_keep;
	@Bind(R.id.et_total)
	EditText et_total;
	@Bind(R.id.ll_surplus)
	LinearLayout ll_surplus;
	@Bind(R.id.ll_alipay)
	LinearLayout ll_alipay;
	@Bind(R.id.ll_weixin)
	LinearLayout ll_weixin;
	@Bind(R.id.cb_gift)
	RadioButton cb_gift;
	@Bind(R.id.rb_arrive)
	Button cb_arrive;
	@Bind(R.id.rb_month)
	Button cb_month;
	public String billCode;
	public String pointStatus;
	@Bind(R.id.et_weight)
	EditText et_weight;
	private String orderInfo;
	private int statu;
	private String result;
	private boolean save;
	private int userCouponId;
	private DecimalFormat df;
	private String money;
	public String businessId;
	private DiscountBean bean1;
	@Bind(R.id.ll_month)
	LinearLayout ll_month;
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private MyReceiver reMyrecive;
	public String PAYMENT=null;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_detail);
		ButterKnife.bind(this);
		initData();
		initView();
		req = new PayReq();
		msgApi.registerApp(CollectionKey.APP_ID);
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		handler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (PAYMENT.equals("1")) {
						Log.e("pay", "1");
						getLlsurplus();	
					}else if (PAYMENT.equals("2")) {
						Log.e("pay", "2");
						getWxchat();
					}else if (PAYMENT.equals("3")) {
						Log.e("pay", "3");
						getAlipay();
					}else if (PAYMENT.equals("4")) {
						Log.e("pay", "4");
						getMonth();
					}
					
					break;

				default:
					break;
				}
				}
			
		};

	}

	@OnClick({ R.id.ll_surplus, R.id.ll_alipay, R.id.ll_weixin, R.id.iv_scan,
			R.id.ll_month })
	public void onMyClick(View view) {
		switch (view.getId()) {
		case R.id.ll_surplus:
			Builder ad = new Builder(PublicDetailActivity.this);
			ad.setTitle("温馨提示");
			ad.setMessage("确认是否使用余额支付？");
			ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (save) {
						// 余额支付
						getLlsurplus();	
						Log.e("zhifu", "k");
					}else {
						PAYMENT="1";
						Log.e("zhifu", "9ok");
						getResultwo();
					}
				}
			});
			ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.create().show();
			break;
		case R.id.ll_weixin:
			 MessageUtils
			 .alertMessageCENTER(getApplicationContext(), "正在唤醒微信支付");
				if (save) {
					// 余额支付
					getWxchat();	
					Log.e("zhifu", "k");
				}else {
					PAYMENT="2";
					Log.e("zhifu", "2k");
					getResultwo();
				}
			
			break;
		case R.id.ll_alipay:
			 MessageUtils
			 .alertMessageCENTER(getApplicationContext(), "正在唤醒支付宝支付");
				if (save) {
					// 支付宝支付
					getAlipay();
					Log.e("zhifu", "k3");
				}else {
					PAYMENT="3";
					Log.e("zhifu", "3k");
					getResultwo();
				}
			
			break;
		case R.id.ll_month:
			// 月结
			if (save) {
				// 余额支付
				Log.e("zhifu", "k4");
				getMonth();
			}else {
				PAYMENT="4";
				Log.e("zhifu", "4k");
				getResultwo();
			}
			
			break;
		case R.id.iv_scan:
			startActivityForResult(new Intent(PublicDetailActivity.this,
					CaptureActivity.class), 1);
			break;
		default:
			break;
		}
	}

	private void getMonth() {
		if (!et_freight.getText().toString().equals("")
				&& !et_weight.getText().toString().equals("")
				&& !et_total.getText().toString().equals("")) {
			if (save == true) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("billCode", billCode);
					obj.put("matName", et_name.getText().toString());
					if (rb_warp.isChecked()) {
						obj.put("matType", "1");
					} else {
						obj.put("matType", "2");
					}
					obj.put("insuranceFee", et_keep.getText().toString());
					obj.put("insureMoney", et_value.getText().toString());
					obj.put("shipMoney", et_freight.getText().toString());
					obj.put("needPayMoney", et_total.getText().toString());
					obj.put("userCouponId", userCouponId);
					obj.put("weight", et_weight.getText().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.MONTH,
						obj.toString(), null, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								// TODO Auto-generated method stub
								if (arg2 == null)
									return;
								BaseBean bean = new Gson().fromJson(new String(
										arg2), BaseBean.class);
								Log.e("LLLL", bean.getMessage());
								ToastUtil.shortToast(getApplicationContext(),
										bean.getMessage());
								if (bean.getErrCode() == 0) {
									finish();
									Intent intent = new Intent(
											PublicDetailActivity.this,
											EvaluateActivity.class);
									intent.putExtra("businessId", businessId);
									startActivity(intent);
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				ToastUtil.shortToast(getApplicationContext(), "请重新验证快递单号");
			}
		} else {
			ToastUtil.shortToast(getApplicationContext(), "请先完善货物信息");
		}

	}

	// 获取支付宝和微信支付的结果
	public void getQueryResult(String billCode) {
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode),
				null, null, null, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("msdjkg", new String(arg2));
						BaseBean bean=new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.isSuccess()) {
							MessageUtils.alertMessageCENTER(
									getApplicationContext(), "支付成功");
							Intent intent = new Intent(PublicDetailActivity.this,
									EvaluateActivity.class);
							intent.putExtra("businessId", businessId);
							startActivity(intent);
							finish();							
						}else {
							MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						MessageUtils.alertMessageCENTER(
								getApplicationContext(), "支付失败");

					}
				});
	}

	// 余额支付
	private void getLlsurplus() {
		if (!pointStatus.equals("1")&&pointStatus.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该网点暂不支持线上支付");
		} else {
			if (!et_freight.getText().toString().equals("")
					&& !et_weight.getText().toString().equals("")
					&& !et_total.getText().toString().equals("")) {
				if (save == true) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("billCode", billCode);
						obj.put("matName", et_name.getText().toString());
						if (rb_warp.isChecked()) {
							obj.put("matType", "1");
						} else {
							obj.put("matType", "2");
						}
						obj.put("insuranceFee", et_keep.getText().toString());
						obj.put("insureMoney", et_value.getText().toString());
						obj.put("shipMoney", et_freight.getText().toString());
						obj.put("needPayMoney", et_total.getText().toString());
						if (cb_gift.isChecked()) {
							obj.put("userCouponId", userCouponId);
							Log.e("ic", userCouponId + "");
						} else {
							obj.put("userCouponId", 0);
							Log.e("ic", "fff");
						}
						obj.put("weight", et_weight.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AsyncHttpUtils.doPut(getApplicationContext(),
							MCUrl.BALANCE_MONEY, obj.toString(), null,
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									if (arg2 == null)
										return;
									BaseBean bean = new Gson().fromJson(
											new String(arg2), BaseBean.class);
									ToastUtil.shortToast(
											getApplicationContext(),
											bean.getMessage());
									if (bean.getErrCode() == 0) {
										finish();
										Intent intent = new Intent(
												PublicDetailActivity.this,
												EvaluateActivity.class);
										intent.putExtra("businessId",
												businessId);
										startActivity(intent);
									}
									if (bean.getErrCode() == -2) {
										DialogUtils
												.createAlertDialogTwo(
														PublicDetailActivity.this,
														"",
														bean.getMessage(),
														0,
														"去充值",
														"更换支付方式",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method
																// stub
																startActivity(new Intent(
																		PublicDetailActivity.this,
																		RechargeActivity.class));
															}

														}).show();
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub

								}
							});
				} else {
					ToastUtil.shortToast(getApplicationContext(), "请验证快递单号");
				}
			} else {
				ToastUtil.shortToast(getApplicationContext(), "请先完善货物信息");
			}
		}

	}

	// 支付宝支付
	private void getAlipay() {
		if (!pointStatus.equals("1")&&pointStatus.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该网点暂不支持线上支付");
		} else {
			if (!et_freight.getText().toString().equals("")
					&& !et_weight.getText().toString().equals("")
					&& !et_total.getText().toString().equals("")) {
				if (save == true) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("billCode", billCode);
						obj.put("matName", et_name.getText().toString());
						if (rb_warp.isChecked()) {
							obj.put("matType", "1");
						} else {
							obj.put("matType", "2");
						}					
						obj.put("insuranceFee", et_keep.getText().toString());
						obj.put("insureMoney", et_value.getText().toString());
						obj.put("shipMoney", et_freight.getText().toString());
						obj.put("needPayMoney", et_total.getText().toString());
						obj.put("weight", et_weight.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AsyncHttpUtils.doPut(getApplicationContext(),
							MCUrl.PAYPARAMS, obj.toString(), null,
							new AsyncHttpResponseHandler() {

								private Double needMoney;

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									if (arg2 == null)
										return;
									BaseBean bean = new Gson().fromJson(
											new String(arg2), BaseBean.class);
									if (bean1.data.get(0).discount != null) {
										needMoney = Double.parseDouble(et_total
												.getText().toString())
												* Double.parseDouble(bean1.data
														.get(0).discount);
									}
									if (bean.getErrCode() == 0) {
										orderInfo = AlipayUtils
												.getOrderInfo(
														"快件线上支付",
														et_name.getText()
																.toString()
																+ et_total
																		.getText()
																		.toString()
																+ "元",
														needMoney + "",
														"http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp",
														null, null, billCode);
										Log.e("pp", orderInfo);
										sendEmptyBackgroundMessage(MsgConstants.MSG_01);
									} else {
										ToastUtil.shortToast(
												getApplicationContext(),
												bean.getMessage());
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub

								}
							});

				} else {
					ToastUtil.shortToast(getApplicationContext(), "请重新验证快递单号");
				}
			} else {
				ToastUtil.shortToast(getApplicationContext(), "请先完善货物信息");
			}
		}

	}

	// 微信支付
	private void getWxchat() {
		if (!pointStatus.equals("1")&&pointStatus.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该网点暂不支持线上支付");
		} else {
			if (!et_freight.getText().toString().equals("")
					&& !et_weight.getText().toString().equals("")
					&& !et_total.getText().toString().equals("")) {
				if (save == true) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("billCode", billCode);
						obj.put("matName", et_name.getText().toString());
						if (rb_warp.isChecked()) {
							obj.put("matType", "1");
						} else {
							obj.put("matType", "2");
						}
						obj.put("insuranceFee", et_keep.getText().toString());
						obj.put("insureMoney", et_value.getText().toString());
						obj.put("shipMoney", et_freight.getText().toString());
						obj.put("needPayMoney", et_total.getText().toString());
						obj.put("weight", et_weight.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AsyncHttpUtils.doPut(getApplicationContext(),
							MCUrl.EXPRESSPAYWECHAT, obj.toString(), null,
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									if (arg2 == null)
										return;
									WechatBean bean = new Gson().fromJson(
											new String(arg2), WechatBean.class);
									Log.e("lllllllll}}}}}}]]]",
											new String(arg2));
									req.appId = bean.data.get(0).appId;
									req.partnerId = bean.data.get(0).partnerId;
									req.prepayId = bean.data.get(0).prepayid;
									req.nonceStr = bean.data.get(0).nonceStr;
									req.packageValue = bean.data.get(0).package_;
									req.sign = bean.data.get(0).sign;
									req.timeStamp = bean.data.get(0).timestamp;
									req.extData = "app data";
									msgApi.registerApp(CollectionKey.APP_ID);
									msgApi.sendReq(req);
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub

								}
							});
				} else {
					ToastUtil.shortToast(getApplicationContext(), "请验证快递单号");
				}
			} else {
				ToastUtil.shortToast(getApplicationContext(), "请先完善货物信息");
			}
		}

	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("recharge")) {
				getQueryResult(billCode);
			}

		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1:
			if (arg1 == RESULT_OK && arg2 != null) {
				Bundle bundle = arg2.getExtras();
				// 显示扫描到的内容
				et_number.setText(bundle.getString("result"));
				getResult();
			}
			break;
		case 2:
			if (arg1 == RESULT_OK && arg2 != null) {
				money = arg2.getStringExtra("money");
				userCouponId = arg2.getIntExtra("userCouponId", 0);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userCouponId == 0)
			cb_gift.setChecked(false);
	}

	private void getResult() {	
		if (!et_number.getText().toString().equals("")) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("billCode", billCode);
				obj.put("expNo", et_number.getText().toString());
				AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.EXPNO,
						obj.toString(), null, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								if (arg2 == null)
									return;
								BaseBean bean = new Gson().fromJson(new String(arg2),
										BaseBean.class);
								Log.e("vv", bean.getMessage());
								if (bean.getErrCode() == 0)
									save = true;
								Log.e("vv1", bean.getMessage());
								ToastUtil.shortToast(getApplicationContext(),
										bean.getMessage());
							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2,
									Throwable arg3) {
								// TODO Auto-generated method stub
								Log.e("cvv", arg0 + "");
							}
						});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}else {
			ToastUtil.shortToast(PublicDetailActivity.this, "请输入快递单号");
		}
		
	}
	private void getResultwo() {	
		if (!et_number.getText().toString().equals("")) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("billCode", billCode);
				obj.put("expNo", et_number.getText().toString());
				AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.EXPNO,
						obj.toString(), null, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								if (arg2 == null)
									return;
								BaseBean bean = new Gson().fromJson(new String(arg2),
										BaseBean.class);
								Log.e("vv", bean.getMessage());
								if (bean.getErrCode() == 0)
									save = true;
								Log.e("vv1", bean.getMessage());
								handler.sendEmptyMessage(1);
								ToastUtil.shortToast(getApplicationContext(),
										bean.getMessage());
							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2,
									Throwable arg3) {
								// TODO Auto-generated method stub
								Log.e("cvv", arg0 + "");
							}
						});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}else {
			ToastUtil.shortToast(PublicDetailActivity.this, "请输入快递单号");
		}
		
	}
	@Override
	public void initData() {
		tbv_show.setTitleText("快递详情");
		reMyrecive = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recharge");
		registerReceiver(reMyrecive, filter);
		// 获取支付宝打折现金折扣
		getDiscount();
		// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PublicDetailActivity.this.getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		// TODO Auto-generated method stub
		tv_name.setText(getIntent().getStringExtra("send_name"));
		tv_address.setText(getIntent().getStringExtra("send_adddress"));
		tv_phone.setText(getIntent().getStringExtra("send_phone"));
		tv_re_address.setText(getIntent().getStringExtra("rece_address"));
		if (tv_re_address.getText().equals("")) {
			ll_rece.setVisibility(View.GONE);
		}
		tv_re_name.setText(getIntent().getStringExtra("rece_name"));
		tv_re_phone.setText(getIntent().getStringExtra("rece_phone"));
		pointStatus = getIntent().getStringExtra("pointStatus");
		Log.e("pointStatus", pointStatus);
		billCode = getIntent().getStringExtra("billCode");
		businessId = getIntent().getStringExtra("businessId");
		et_number.setFocusable(true);
		et_total.setFocusable(false);
		df = new DecimalFormat("######0.00");
		setDataChange();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(reMyrecive);
	}

	private void setDataChange() {
		// TODO Auto-generated method stub
		et_freight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equals("")) {
					if (et_keep.getText().toString().equals("")
							&& !et_value.getText().toString().equals("")) {
						et_total.setText(df.format((Double.parseDouble(s
								.toString()))));
					}
					if (et_value.getText().toString().equals("")
							&& !et_keep.getText().toString().equals("")) {
						et_total.setText(df.format((Double.parseDouble(s
								.toString()) + Double.parseDouble(et_keep
								.getText().toString())))
								+ "");
					}
					if (et_value.getText().toString().equals("")
							&& et_keep.getText().toString().equals("")) {
						et_total.setText(df.format((Double.parseDouble(s
								.toString()))) + "");
					}
					if (!et_value.getText().toString().equals("")
							&& !et_keep.getText().toString().equals("")) {
						et_total.setText(df.format(Double.parseDouble(s
								.toString())
								+ Double.parseDouble(et_keep.getText()
										.toString()))
								+ "");

					}
				} else {
					et_total.setText("");
				}
			}
		});
		et_keep.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!s.toString().equals("")) {
					if (et_freight.getText().toString().equals("")) {
						et_total.setText(df.format((Double.parseDouble(s
								.toString()))));
					} else {
						if (!et_value.getText().toString().equals("")) {
							et_total.setText(df.format(Double.parseDouble(s
									.toString())
									+ Double.parseDouble(et_freight.getText()
											.toString()))
									+ "");
						} else {
							et_total.setText(df.format(Double.parseDouble(s
									.toString())
									+ Double.parseDouble(et_freight.getText()
											.toString()))
									+ "");
						}
					}
				} else {
					et_total.setText("");
				}
			}
		});
		et_value.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * if(!s.toString().equals("")){
				 * if(et_freight.getText().toString().equals("")){
				 * et_total.setText(""); } else{
				 * if(!et_keep.getText().toString().equals("")){
				 * et_total.setText(df.format(Double.parseDouble(s.toString()) +
				 * Double.parseDouble(et_keep.getText().toString()) +
				 * Double.parseDouble(et_freight.getText() .toString())) + "");
				 * } else{
				 * et_total.setText(df.format(Double.parseDouble(s.toString()) +
				 * Double.parseDouble(et_freight.getText() .toString())) + "");
				 * } } } else{ if(et_freight.getText().toString().equals("")){
				 * et_total.setText(""); }else{
				 * if(et_keep.getText().toString().equals("")){
				 * et_total.setText(Double.parseDouble(et_freight.getText()
				 * .toString())+ ""); }else{
				 * et_total.setText(df.format(Double.parseDouble
				 * (et_keep.getText().toString()) +
				 * Double.parseDouble(et_freight.getText() .toString())) + "");
				 * } } }
				 */
			}
		});


		cb_arrive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_freight.getText().toString().equals("")
						&& !et_keep.getText().toString().equals("")
						&& !et_value.getText().toString().equals("")
						&& !et_total.getText().toString().equals("")
						&& !et_weight.getText().toString().equals("")) {
					if (save == true) {
						JSONObject obj = new JSONObject();
						try {
							obj.put("billCode", billCode);
							obj.put("matName", et_name.getText().toString());
							if (rb_warp.isChecked()) {
								obj.put("matType", "1");
							} else {
								obj.put("matType", "2");
							}
							obj.put("insuranceFee", et_keep.getText()
									.toString());
							obj.put("insureMoney", et_value.getText()
									.toString());
							obj.put("shipMoney", et_freight.getText()
									.toString());
							obj.put("needPayMoney", et_total.getText()
									.toString());
							obj.put("weight", et_weight.getText().toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AsyncHttpUtils.doPut(getApplicationContext(),
								MCUrl.ARRIVED, obj.toString(), null,
								new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										if (arg2 == null)
											return;
										BaseBean bean = new Gson().fromJson(
												new String(arg2),
												BaseBean.class);
										ToastUtil.shortToast(
												getApplicationContext(),
												bean.getMessage());
										if (bean.getErrCode() == 0) {
											finish();
											Intent intent = new Intent(
													PublicDetailActivity.this,
													EvaluateActivity.class);
											intent.putExtra("businessId",
													businessId);
											startActivity(intent);
										}
									}

									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub

									}
								});
					} else {
						ToastUtil.shortToast(getApplicationContext(),
								"请重新验证快递单号");
					}
				} else {
					ToastUtil.shortToast(getApplicationContext(), "请先完善货物信息");
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

	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			PayTask task = new PayTask(this);
			result = task.pay(orderInfo);
			Log.e("mm", result);
			sendEmptyUiMessage(MsgConstants.MSG_01);
			break;

		default:
			break;
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			PayResult payResult = new PayResult(result);
			String resultStatus = payResult.getResultStatus();
			Log.e("su", resultStatus + payResult.getResult());
			if (TextUtils.equals(resultStatus, "9000")) {
				Toast.makeText(PublicDetailActivity.this, "支付成功",
						Toast.LENGTH_SHORT).show();
				getQueryResult(billCode);
			
			} else {
				// 判断resultStatus 为非“9000”则代表可能支付失败
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "8000")) {
					Toast.makeText(PublicDetailActivity.this, "支付结果确认中",
							Toast.LENGTH_SHORT).show();

				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(PublicDetailActivity.this, "支付失败",
							Toast.LENGTH_SHORT).show();

				}
			}
			break;

		default:
			break;
		}
	}

	// 获取支付宝支付折扣
	public void getDiscount() {
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ALIPAYDISCOUNT, "uid",
				PreferenceConstants.UID), null, null, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("disconut", new String(arg2));
						bean1 = new Gson().fromJson(new String(arg2),
								DiscountBean.class);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

}
