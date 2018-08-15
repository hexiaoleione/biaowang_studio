package com.hex.express.iwant.newactivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.HjasActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.activities.TradingcertaintyActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.InsBean;
import com.hex.express.iwant.bean.InsuranBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class InsureActivity extends BaseActivity{
	
	@Bind(R.id.et_insure_my)
	EditText et_insure_my;
	@Bind(R.id.et_inumber)
	EditText et_inumber;
	
	@Bind(R.id.checkbox_chang)
	CheckBox checkbox_chang;
	@Bind(R.id.checkbox_shui)
	CheckBox checkbox_shui;
	@Bind(R.id.checkbox_leng)
	CheckBox checkbox_leng;
	@Bind(R.id.checkbox_sui)
	CheckBox checkbox_sui;

//	@Bind(R.id.imgzong)
//	ImageView imgzong;
	@Bind(R.id.imgbao)
	ImageView imgbao;
	
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	
	
	
	@Bind(R.id.toubao)
	TextView  toubao;

//	@Bind(R.id.tezong)
//	TextView  tezong;
	@Bind(R.id.checkbox_bao)
	CheckBox checkbox_bao;
	@Bind(R.id.checkbox_baojiben)
	CheckBox checkbox_baojiben;
	
	
	@Bind(R.id.checkbox_ming)
	CheckBox checkbox_ming;
	
	
	@Bind(R.id.butsumit)
	Button  butsumit;
	
	 String  wu="";
	private int  xian=0;
	private boolean ming=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lnsure_activity);
		iWantApplication.getInstance().addActivity(this);
		 ButterKnife.bind(this);
		 initView();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		butsumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getInsuran();
			}
		});
		toubao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub  http://www.efamax.com/mobile/manual/inSureRuleAndroid.html
				
				Intent intent=new Intent();
				intent.setClass(InsureActivity.this, HtmltouActivity.class);
				startActivity(intent);
			}
		});
	checkbox_chang.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			
 			@Override
 			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
 				// TODO Auto-generated method stub
 				if(arg1){
 					wu="1";
 					checkbox_shui.setChecked(false);
 	 				checkbox_leng.setChecked(false);
 	 				checkbox_sui.setChecked(false);
 	 				checkbox_bao.setVisibility(View.VISIBLE);
 	 				checkbox_baojiben.setVisibility(View.GONE);
 				}
// 				else {
// 					wu="0";
//				}
 				
// 				else {
// 					w=0;
// 				}
 				}
// 			}
 		});
	checkbox_shui.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
 					wu="2";
 					checkbox_chang.setChecked(false);
 	 				checkbox_leng.setChecked(false);
 	 				checkbox_sui.setChecked(false);
 	 				checkbox_bao.setVisibility(View.GONE);
 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
 				}
//				else {
// 					wu="0";
//				}
				
				}
//			}
		});
//	checkbox_leng.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if(arg1){
//					
// 					wu=3;
// 					checkbox_chang.setChecked(false);
// 	 				checkbox_shui.setChecked(false);
// 	 				checkbox_sui.setChecked(false);
// 	 				checkbox_bao.setVisibility(View.VISIBLE);
// 	 				checkbox_baojiben.setVisibility(View.GONE);
// 				}else {
// 					wu=0;
//				}
//				
//				}
////			}
//		});
	checkbox_sui.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					wu="4";
 					checkbox_chang.setChecked(false);
 	 				checkbox_shui.setChecked(false);
 	 				checkbox_leng.setChecked(false);
 	 				checkbox_bao.setVisibility(View.VISIBLE);
 	 				checkbox_baojiben.setVisibility(View.GONE);
 				}
//				else {
// 					wu="0";
//				}
				
				}
//			}
		});
		
//		if (wu==2) {
//			tezong.setText("基本险");
//		}
//		lyzong.setOnClickListener(new OnClickListener() {
//	
//			@Override
//		public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		imgzong.setBackgroundResource(R.drawable.xuanzhong);
//		
//			}
//		});
		imgbao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imgbao.setBackgroundResource(R.drawable.xuanzhong);
			}
		});
		checkbox_ming.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			
 			@Override
 			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
 				// TODO Auto-generated method stub
 				if(arg1){
 					ming=arg1;
 				}else {
 					ming=arg1;
 				}
 				}
// 			}
 		});
		checkbox_bao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					xian=2;
 				}else {
 					xian=0;
 				}
			}
		});
		checkbox_baojiben.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					xian=1;
 				}else {
 					xian=0;
 				}
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	public void getInsuran(){
		if ("0".equals(wu) || "".equals(wu)) {
			ToastUtil.shortToast(InsureActivity.this, "请选择物品种类");
			return;
		}
		if (et_insure_my.getText().toString().equals("") ||  et_inumber.getText().toString().equals("")) {
			ToastUtil.shortToast(InsureActivity.this, "请补充完信息");
			return;	
		}
		if (!ming) {
			ToastUtil.shortToast(InsureActivity.this, "请阅读并同意说明");
			return;	
		}
		if (xian==0) {
			ToastUtil.shortToast(InsureActivity.this, "请选择保险类别");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("recId", getIntent().getIntExtra("recId", 0));
			obj.put("userId",String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("cargoCost", et_insure_my.getText().toString());
			obj.put("carNumImg", et_inumber.getText().toString());
			obj.put("category", wu);
			
				obj.put("insurance", xian);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(InsureActivity.this, MCUrl.addInsurance, obj.toString(),
				new AsyncHttpResponseHandler() {
//		RequestParams params = new RequestParams();
//		AsyncHttpUtils.doGet(UrlMap.getfour(MCUrl.LogAddInsurance, "recId",getIntent().getIntExtra("WLBId", 0)+"",
//				"rate",adbean.getData().get(0).value, "cargoCost",jiazhi.getText().toString(), "carNumImg",icon),
//				null, null, params,
//				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						final InsBean	 inensb = new Gson().fromJson(new String(arg2),
								InsBean.class);
						if (inensb.errCode==0) {
//							Intent intent=new Intent();
//							intent.setClass(InsureActivity.this, NewMainActivity.class);
//							startActivity(intent);
//							finish();
							Builder ad = new Builder(InsureActivity.this);
							ad.setTitle("温馨提示");
							ad.setMessage("此单保费"+inensb.getData().get(0).insureCost+"元");
							
							ad.setPositiveButton("立即支付", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									getaddsurplus(inensb.getData().get(0).recId,inensb.getData().get(0).insureCost);
								}
							});
							ad.setNegativeButton("暂不支付", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
									Intent intent=new Intent();
									intent.setClass(InsureActivity.this, NewMainActivity.class);
									startActivity(intent);
									finish();
								}
							});
							ad.create().show();
						}else {
							ToastUtil.shortToast(InsureActivity.this, inensb.message);
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	/**
	 * 余额支付的接口
	 */
	public void getaddsurplus(int recId, String insureCost )   {
//		if (billCode.equals("") || money.equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
//			return;
//		}
//		Log.e("1111   余额obj", obj.toString());
		dialog.show();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.payInsure, "recId", ""+getIntent().getIntExtra("recId", 0), "","", "", ""), null,
				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
						if (arg2 == null)
							return;
						Log.e("msg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(InsureActivity.this, bean.getMessage());
							Intent intent=new Intent();
							intent.setClass(InsureActivity.this, NewMainActivity.class);
							startActivity(intent);
							finish();
						}
						if (bean.getErrCode() !=0 ) {
							ToastUtil.shortToast(InsureActivity.this, bean.getMessage());
							Intent intent=new Intent();
							intent.setClass(InsureActivity.this, NewMainActivity.class);
							startActivity(intent);
							finish();
//							DialogUtils.createAlertDialogTwo(InsureActivity.this, "", bean.getMessage(), 0, "去充值",
//									"更换支付方式", new DialogInterface.OnClickListener() {
//
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//
//											startActivity(new Intent(InsureActivity.this, RechargeActivity.class));
//										}
//
//									}).show();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				});
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			finish();// 销毁此页面,并去启动上一个页面的相应的Fragment
		}
		return super.dispatchKeyEvent(event);
	}

}
