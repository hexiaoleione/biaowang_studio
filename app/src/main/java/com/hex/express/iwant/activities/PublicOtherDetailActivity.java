package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.madmatrix.zxing.android.CaptureActivity;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.PublicOtherAdapter;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.ChildExpressBean;
import com.hex.express.iwant.bean.ConsecutiveBean;
import com.hex.express.iwant.bean.DiscountBean;
import com.hex.express.iwant.bean.PublicOtherBean;
import com.hex.express.iwant.bean.WechatBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.MarqueeTextView;
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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicOtherDetailActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.tv_name)
	TextView tv_name;
	@Bind(R.id.tv_phone)
	TextView tv_phone;
	@Bind(R.id.tv_address)
	MarqueeTextView tv_address;
	@Bind(R.id.listview)
	PullToRefreshListView listview;
	@Bind(R.id.null_message)
	LinearLayout null_message;
	@Bind(R.id.tv_needPayMoney)
	TextView tv_needPayMoney;
	@Bind(R.id.automatically)
	TextView automatically;
	@Bind(R.id.iv_add)
	TextView iv_add;
	@Bind(R.id.lout_num)
	LinearLayout lout_num;
	// 添加连单号
	@Bind(R.id.lout_consecutive_number)
	LinearLayout lout_consecutive_number;
	@Bind(R.id.et_firstnumber)
	EditText et_firstnumber;
	@Bind(R.id.et_endnumber)
	EditText et_endnumber;
	@Bind(R.id.et_money)
	EditText et_money;
	@Bind(R.id.et_allnum)
	EditText et_allnum;
	LinearLayout ll_surplus;

	LinearLayout ll_alipay;

	LinearLayout ll_weixin;
	LinearLayout ll_month;
	RadioButton cb_gift;
	public String billCode;
	public String pointStatus;
	private String orderInfo;
	private String result;
	private int userCouponId;
	private DecimalFormat df;
	public String businessId;
	private DiscountBean bean1;
	double allmoney;
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private MyReceiver reMyrecive;
	private Handler handler;
	public String expnum;
	PublicOtherAdapter adapter;
	List<PublicOtherBean> mlist;
	List<ConsecutiveBean.Data> list;
	private EditText editExp;
	private String money;
	double sum = 0;
	private org.json.JSONArray array;
	private ImageView img_cancel01;
	PopupWindow window01;
	PopupWindow window02;
	private ImageView img_cancle02;
	PopupWindow window03;
	// private EditText et_firstnumber;
	// private EditText et_endnumber;

	private int firstPosition;
	private String substring;
	private String endsubstring;
	private int endPosition;
	// 用来区分连号和普通添加的标记，true是连号，false是普通
	boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_other_detail);
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
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Log.e("jsonuuuuu", mlist.get(0).toString());
					adapter = new PublicOtherAdapter(PublicOtherDetailActivity.this, mlist, handler);
					listview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					getSummation(mlist);
					break;
				case 2:
					if (adapter.Datalist().size() > 0) {
						adapter = new PublicOtherAdapter(PublicOtherDetailActivity.this, adapter.Datalist(), handler);
						listview.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						getSummation(adapter.Datalist());
					} else {
						iv_add.setVisibility(View.VISIBLE);
						automatically.setVisibility(View.VISIBLE);
						listview.setVisibility(View.GONE);
						null_message.setVisibility(View.VISIBLE);
						tv_needPayMoney.setText(0 + "");
					}
					break;
				default:
					break;
				}
			}

		};
	}

	/**
	 * 计算总价格
	 * 
	 * @param mList
	 */
	private void getSummation(List<PublicOtherBean> mList) {
		sum = 0;
		for (int i = 0; i < mlist.size(); i++) {
			allmoney = Double.parseDouble(mList.get(i).getChildExpPrice());
			Log.e("allmoney", allmoney + "");
			sum = sum + allmoney;
		}
		tv_needPayMoney.setText(df.format(sum) + "");

	}

	@OnClick({ R.id.iv_add, R.id.btn_submit, R.id.automatically, R.id.iv_eddscan, R.id.iv_scan })
	public void onMyClick(View view) {
		switch (view.getId()) {
		case R.id.automatically:
			lout_consecutive_number.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			lout_num.setVisibility(View.GONE);
			null_message.setVisibility(View.GONE);
			// showAllwindow();
			break;
		case R.id.iv_add:
			lout_consecutive_number.setVisibility(View.GONE);
			null_message.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			showPopwindow();
			lout_num.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_submit:
			//支付的时候如果没实名认证，先让用户去完善个人资料
//			if (!"Y".equals(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH))) {
//				AlertDialog.Builder ad = new Builder(PublicOtherDetailActivity.this);
//				ad.setTitle("温馨提示");
//				ad.setMessage("根据《中华人民共和国反恐怖主义法》规定，发快递须实名制，若您所填信息不真实或不完善，请尽快修改和完善，感谢您的配合。");
//				ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						startActivity(new Intent(PublicOtherDetailActivity.this,
//								RegisterSetImageAndNameActivity.class).putExtra("tiaoguo", "2"));
//					}
//				});
//				ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						arg0.dismiss();
//						
//					}
//				});
//				ad.create().show();
//			} else {
				if (flag) {
					if (substring.equals(endsubstring)) {
						getLlsurplus();
					} else {
						ToastUtil.shortToast(PublicOtherDetailActivity.this, "请输入连续的订单号");
					}
				} else {
					getLlsurplus();
				}
//			}
			break;
		case R.id.iv_eddscan:
			startActivityForResult(new Intent(PublicOtherDetailActivity.this, CaptureActivity.class), 4);
			break;
		case R.id.iv_scan:
			startActivityForResult(new Intent(PublicOtherDetailActivity.this, CaptureActivity.class), 3);
			break;
		default:
			break;
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

	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_public, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window01 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window01.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window01.setBackgroundDrawable(dw);
		window01.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window01.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window01.showAtLocation(PublicOtherDetailActivity.this.findViewById(R.id.iv_add), Gravity.CENTER, 0, 0);

		editExp = (EditText) view.findViewById(R.id.et_number);
		final EditText editMoney = (EditText) view.findViewById(R.id.et_freight);
		TextView iv_scan = (TextView) view.findViewById(R.id.iv_scan);
		Button btn_save = (Button) view.findViewById(R.id.btnsave);
		img_cancel01 = (ImageView) view.findViewById(R.id.img_cancel01);
		img_cancel01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				window01.dismiss();

			}
		});
		iv_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(PublicOtherDetailActivity.this, CaptureActivity.class), 1);

			}
		});
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 定义一个HashMap，用来存放EditText的值，Key是positio
				if (!editExp.getText().toString().equals("") && !editMoney.getText().toString().equals("")) {
					PublicOtherBean bean = new PublicOtherBean();
					if (mlist.size() > 0) {
						boolean first = false;
						for (int i = 0; i < mlist.size(); i++) {
							if (editExp.getText().toString().equals(mlist.get(i).getChildExpNo())) {
								first = true;
							}
						}
						if (first) {
							ToastUtil.shortToast(PublicOtherDetailActivity.this, "该订单号已存在");
						} else {
							bean.setChildExpNo(editExp.getText().toString());
							bean.setChildExpPrice(editMoney.getText().toString());
							mlist.add(bean);
							iv_add.setVisibility(View.VISIBLE);
							automatically.setVisibility(View.GONE);
							handler.sendEmptyMessage(1);
							flag = false;
							window01.dismiss();
						}
					} else {
						bean.setChildExpNo(editExp.getText().toString());
						bean.setChildExpPrice(editMoney.getText().toString());
						mlist.add(bean);
						iv_add.setVisibility(View.VISIBLE);
						automatically.setVisibility(View.GONE);
						handler.sendEmptyMessage(1);
						window01.dismiss();
					}
				} else {
					ToastUtil.shortToast(PublicOtherDetailActivity.this, "信息填写不完整");
				}

			}
		});
		// popWindow消失监听方法
		window01.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}

	/**
	 * 显示支付方式
	 */
	private void showPaywindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_piblic_pay, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window02.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window02.setBackgroundDrawable(dw);
		window02.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window02.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window02.showAtLocation(PublicOtherDetailActivity.this.findViewById(R.id.iv_add), Gravity.CENTER, 0, 0);
		ll_alipay = (LinearLayout) view.findViewById(R.id.ll_alipay);
		ll_month = (LinearLayout) view.findViewById(R.id.ll_month);
		ll_surplus = (LinearLayout) view.findViewById(R.id.ll_surplus);
		ll_weixin = (LinearLayout) view.findViewById(R.id.ll_weixin);
		cb_gift = (RadioButton) view.findViewById(R.id.cb_gift);
		img_cancle02 = (ImageView) view.findViewById(R.id.img_cancel02);
		img_cancle02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				window02.dismiss();

			}
		});
		cb_gift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Intent intent = new Intent(PublicOtherDetailActivity.this, CardActivity.class);
					intent.putExtra("gift", true);
					startActivityForResult(intent, 2);
				}
			}
		});
		ll_alipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MessageUtils.alertMessageCENTER(PublicOtherDetailActivity.this, "正在唤醒支付宝");
				getAlipay();
				window02.dismiss();
			}
		});
		ll_weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MessageUtils.alertMessageCENTER(PublicOtherDetailActivity.this, "正在唤醒微信");
				getWxChat();
				window02.dismiss();

			}

		});
		ll_surplus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Builder ad = new Builder(PublicOtherDetailActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("确认是否使用余额支付？");
				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						getaddsurplus();
						window02.dismiss();
					}
				});
				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.create().show();

			}

		});
		ll_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				getllMouth();
				window02.dismiss();

			}
		});
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}

	/**
	 * 月结
	 */
	private void getllMouth() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("billCode", billCode);
			obj.put("matName", null);
			obj.put("matType", null);
			obj.put("insuranceFee", null);
			obj.put("insureMoney", null);
			obj.put("shipMoney", null);
			obj.put("userCouponId", userCouponId);
			obj.put("needPayMoney", tv_needPayMoney.getText().toString());
			obj.put("weight", null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.MONTH, obj.toString(), null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						if (arg2 == null)
							return;
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						Log.e("LLLL", bean.getMessage());
						ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						if (bean.getErrCode() == 0) {
							finish();
							Intent intent = new Intent(PublicOtherDetailActivity.this, EvaluateActivity.class);
							intent.putExtra("businessId", businessId);
							startActivity(intent);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 提交订单
	 */
	private void getLlsurplus() {
		if (!tv_needPayMoney.getText().toString().equals("")) {
			JSONObject obj = new JSONObject();
			try {
				ListToArray();
				obj.put("billCode", billCode);
				obj.put("needPayMoney", tv_needPayMoney.getText().toString());
				obj.put("list", array);
				obj.put("useMoney", et_money.getText().toString());
				obj.put("startBillCode", et_firstnumber.getText().toString());
				obj.put("endBillCode", et_endnumber.getText().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("mlist", obj.toString());
			AsyncHttpUtils.doPut(PublicOtherDetailActivity.this, MCUrl.EXPRESSPAYPARMS, obj.toString(), null,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("josnnd", new String(arg2));
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
							if (bean.getErrCode() == 0) {
								showPaywindow();
							} else {
								ToastUtil.shortToast(getApplicationContext(), bean.getMessage());

							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub

						}
					});
		} else {
			ToastUtil.shortToast(PublicOtherDetailActivity.this, "您当前没有添加订单，快去添加订单");
		}

	}

	/**
	 * 把list转成json格式
	 */
	public void ListToArray() {
		array = new org.json.JSONArray();
		for (int i = 0; i < mlist.size(); i++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("childExpNo", mlist.get(i).getChildExpNo());
				obj1.put("childExpPrice", mlist.get(i).getChildExpPrice());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.put(obj1);
		}
	}

	/**
	 * 余额支付的接口
	 */
	public void getaddsurplus() {
		if (!pointStatus.equals("1")&&!pointStatus.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), pointStatus);

		} else {
			JSONObject obj = new JSONObject();
			try {
				obj.put("billCode", billCode);
				obj.put("matName", null);
				obj.put("matType", null);
				obj.put("insuranceFee", null);
				obj.put("insureMoney", null);
				obj.put("shipMoney", null);
				obj.put("fromCity", PreferencesUtils.getString(PublicOtherDetailActivity.this.getApplicationContext(), PreferenceConstants.CITYCODE));
				obj.put("needPayMoney", tv_needPayMoney.getText().toString());
				if (cb_gift.isChecked()) {
					obj.put("userCouponId", userCouponId);
					Log.e("ic", userCouponId + "");
				} else {
					obj.put("userCouponId", 0);
					Log.e("ic", "fff");
				}
				obj.put("weight", null);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.e("fff", obj.toString());
			AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCE_MONEY, obj.toString(), null,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method
							// stub
							if (arg2 == null)
								return;
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							if (bean.getErrCode() == 0) {
								finish();
								Intent intent = new Intent(PublicOtherDetailActivity.this, EvaluateActivity.class);
								intent.putExtra("businessId", businessId);
								startActivity(intent);
							}
							if (bean.getErrCode() == -2) {
								DialogUtils.createAlertDialogTwo(PublicOtherDetailActivity.this, "", bean.getMessage(),
										0, "去充值", "更换支付方式", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO
												// Auto-generated
												// method
												// stub
												startActivity(new Intent(PublicOtherDetailActivity.this,
														RechargeActivity.class));
											}

										}).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method
							// stub

						}
					});
		}

	}

	/**
	 * 微信支付
	 */
	private void getWxChat() {
		if (!pointStatus.equals("1") &&! pointStatus.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), pointStatus);

		} else {
			JSONObject obj = new JSONObject();
			try {
				obj.put("billCode", billCode);
				obj.put("matName", null);
				obj.put("matType", null);
				obj.put("insuranceFee", null);
				obj.put("insureMoney", null);
				obj.put("shipMoney", null);
				obj.put("userCouponId", userCouponId);
				obj.put("needPayMoney", tv_needPayMoney.getText().toString());
				obj.put("weight", null);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.EXPRESSPAYWECHAT, obj.toString(), null,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated
							// method stub
							if (arg2 == null)
								return;
							WechatBean bean = new Gson().fromJson(new String(arg2), WechatBean.class);
							Log.e("lllllllll}}}}}}]]]", new String(arg2));
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
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated
							// method stub

						}
					});

		}

	}

	/**
	 * 支付宝支付
	 */
	private void getAlipay() {
		if (!pointStatus.equals("1") && !pointStatus.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), pointStatus);
			Log.e("1111 ss", ""+pointStatus);
		} else {

			double needPayMoney = Double.parseDouble(tv_needPayMoney.getText().toString())
					* Double.parseDouble(bean1.data.get(0).discount);
			orderInfo = AlipayUtils.getOrderInfo("快件线上支付", null + tv_needPayMoney.getText().toString() + "元",
					needPayMoney + "",
					"http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp", null, null,
					billCode);
			Log.e("pp", orderInfo);
			sendEmptyBackgroundMessage(MsgConstants.MSG_01);

		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1:
			if (arg1 == RESULT_OK && arg2 != null) {
				Bundle bundle = arg2.getExtras();
				expnum = bundle.getString("result");
				editExp.setText(expnum);
			}
			break;
		case 2:
			if (arg1 == RESULT_OK && arg2 != null) {
				money = arg2.getStringExtra("money");
				userCouponId = arg2.getIntExtra("userCouponId", 0);
				Log.e("11111userCouponId", userCouponId+"");
			}
			break;
		case 3:
			if (arg1 == RESULT_OK && arg2 != null) {
				Bundle bundle = arg2.getExtras();
				expnum = bundle.getString("result");
				et_firstnumber.setText(expnum);
			}
			break;
		case 4:
			if (arg1 == RESULT_OK && arg2 != null) {
				Bundle bundle = arg2.getExtras();
				expnum = bundle.getString("result");
				et_endnumber.setText(expnum);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		 if (userCouponId == 0)
//		 cb_gift.setChecked(false);
	}

	@Override
	public void initData() {
		df = new DecimalFormat("######0.00");
		mlist = new ArrayList<PublicOtherBean>();
		list = new ArrayList<ConsecutiveBean.Data>();
		tbv_show.setTitleText("快递详情");
		reMyrecive = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recharge");
		registerReceiver(reMyrecive, filter);
		// 获取支付宝打折现金折扣
		getDiscount();
		tv_name.setText(getIntent().getStringExtra("send_name"));
		tv_address.setText(getIntent().getStringExtra("send_adddress"));
		tv_phone.setText(getIntent().getStringExtra("send_phone"));
		pointStatus = getIntent().getStringExtra("pointStatus");
		Log.e("pointStatus", pointStatus);
		billCode = getIntent().getStringExtra("billCode");
		businessId = getIntent().getStringExtra("businessId");
		getPaying();
		df = new DecimalFormat("######0.00");
		setDataChange();
	}

	/**
	 * 输入完尾单，和首单比较算出单数
	 */
	private void setDataChange() {
		et_allnum.setFocusable(false);
		et_firstnumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (et_endnumber.length() == et_firstnumber.length()) {
					String firstNum = arg0.toString();
					String endNum = et_endnumber.getText().toString();
					substring = firstNum.substring(1, firstNum.length() - 4);
					firstPosition = Integer.parseInt(firstNum.substring(firstNum.length() - 4, firstNum.length()));
					Log.e("firstPosition", firstPosition + "");
					endsubstring = endNum.substring(1, endNum.length() - 4);
					if (substring.equals(endsubstring)) {
						if (endPosition > firstPosition || endPosition == firstPosition) {
							et_allnum.setText((endPosition - firstPosition + 1) + "");
						} else {
							et_allnum.setText(0 + "");
						}
					} else {
						ToastUtil.shortToast(PublicOtherDetailActivity.this, "请输入连续的订单号");
					}
				}

			}
		});
		et_endnumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String endNum = arg0.toString();
				if (et_firstnumber.length() == et_endnumber.length()) {
					String firstNum = et_firstnumber.getText().toString();
					substring = firstNum.substring(1, firstNum.length() - 4);
					endsubstring = endNum.substring(1, endNum.length() - 4);
					firstPosition = Integer.parseInt(firstNum.substring(firstNum.length() - 4, firstNum.length()));
					Log.e("firstPosition", firstPosition + "");
					endPosition = Integer.parseInt(endNum.substring(endNum.length() - 4, endNum.length()));
					if (substring.equals(endsubstring)) {
						if (endPosition > firstPosition || endPosition == firstPosition) {
							et_allnum.setText((endPosition - firstPosition + 1) + "");
						} else {
							et_allnum.setText(0 + "");
						}
					} else {
						ToastUtil.shortToast(PublicOtherDetailActivity.this, "请输入连续的订单号");
					}
				}
				Log.e("endPosition", endPosition + "");
			}
		});
		et_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				Log.e("jsond", arg0.toString());

				if (!String.valueOf(endPosition).equals("") && !String.valueOf(firstPosition).equals("")
						&& !arg0.toString().equals("") && !et_allnum.getText().equals("")) {
					if (Double.parseDouble(et_allnum.getText().toString()) > 0) {
						tv_needPayMoney.setText(df.format(Double.parseDouble(et_allnum.getText().toString())
								* Double.parseDouble(et_money.getText().toString())));
						flag = true;
					}

				}

			}
		});
		et_allnum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (!et_money.getText().toString().equals("")) {
					tv_needPayMoney.setText(df.format(
							Double.parseDouble(arg0.toString()) * Double.parseDouble(et_money.getText().toString())));
				}

			}
		});

	}

	/**
	 * 获取未完成的订单
	 */
	private void getPaying() {
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.EXPRESSV2, "businessId", businessId), null, null, null,
				new AsyncHttpResponseHandler() {
					private Integer errCode;
					private String message;
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						try {
							JSONObject object = new JSONObject(new String(arg2));
							errCode = (Integer) object.opt("errCode");
							message = (String) object.opt("message");
							Log.e("errcode", errCode + "");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.e("mshhhhhg", new String(arg2));
						if (errCode == 0) {
							ChildExpressBean bean = new Gson().fromJson(new String(arg2), ChildExpressBean.class);
							for (int i = 0; i < bean.data.size(); i++) {
								PublicOtherBean bean2 = new PublicOtherBean();
								bean2.setChildExpNo(bean.data.get(0).childExpNo);
								bean2.setChildExpPrice(bean.data.get(i).childExpPrice);
								mlist.add(bean2);
							}
							if (mlist != null && mlist.size() > 0) {
								automatically.setVisibility(View.GONE);
								listview.setVisibility(View.VISIBLE);
								adapter = new PublicOtherAdapter(PublicOtherDetailActivity.this, mlist, handler);
								listview.setAdapter(adapter);
								getSummation(mlist);
							} else {
								null_message.setVisibility(View.VISIBLE);
								listview.setVisibility(View.GONE);
							}
						} else if (errCode == 1) {
							ConsecutiveBean bean1 = new Gson().fromJson(new String(arg2), ConsecutiveBean.class);
							list = bean1.data;
							getSummation(mlist);
							automatically.setVisibility(View.VISIBLE);
							iv_add.setVisibility(View.GONE);
							listview.setVisibility(View.GONE);
							lout_consecutive_number.setVisibility(View.VISIBLE);
							et_firstnumber.setText(list.get(0).startBillCode);
							et_endnumber.setText(list.get(0).endBillCode);
							et_money.setText(list.get(0).useMoney);
							String  star=bean1.data.get(0).startBillCode;
							String  end=bean1.data.get(0).endBillCode;
//							star.substring(1);
							double num = Double.parseDouble(end.substring(1))
									- Double.parseDouble(star.substring(1)) + 1;
							et_allnum.setText(num + "");
							tv_needPayMoney.setText(list.get(0).needPayMoney);

						} 
//						else {
//							ToastUtil.shortToast(PublicOtherDetailActivity.this, message);
//						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(reMyrecive);
		handler.removeCallbacksAndMessages(null);
		handler = null;
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
				Toast.makeText(PublicOtherDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				getQueryResult(billCode);
			} else {
				// 判断resultStatus 为非“9000”则代表可能支付失败
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "8000")) {
					Toast.makeText(PublicOtherDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(PublicOtherDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

				}
			}
			break;

		default:
			break;
		}
	}

	// 获取支付宝和微信支付的结果
	public void getQueryResult(String billCode) {
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode), null, null, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("msdjkg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.isSuccess()) {
							MessageUtils.alertMessageCENTER(getApplicationContext(), "支付成功");
							Intent intent = new Intent(PublicOtherDetailActivity.this, EvaluateActivity.class);
							intent.putExtra("businessId", businessId);
							startActivity(intent);
							finish();
						} else {
							MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						MessageUtils.alertMessageCENTER(getApplicationContext(), "支付失败");

					}
				});
	}

	// 获取支付宝支付折扣
	public void getDiscount() {
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ALIPAYDISCOUNT, "uid", PreferenceConstants.UID), null, null, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("disconut", new String(arg2));
						bean1 = new Gson().fromJson(new String(arg2), DiscountBean.class);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

}
