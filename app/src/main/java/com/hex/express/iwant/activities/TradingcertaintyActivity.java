package com.hex.express.iwant.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.SelectListAdapter;
import com.hex.express.iwant.adapters.SelectListAdapter.OnDelBtnClickListener;
import com.hex.express.iwant.adapters.SelectListAdapter.OnItemClickListener;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DowInsuraBean;
import com.hex.express.iwant.bean.HaiBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.InsuranBean;
import com.hex.express.iwant.bean.LoOfferBean;
import com.hex.express.iwant.bean.MessageBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.PickActivity;
import com.hex.express.iwant.newactivity.PickToActivity;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  物流个人去支付的界面
 */
public class TradingcertaintyActivity  extends BaseActivity implements OnItemClickListener, OnDelBtnClickListener, OnDismissListener{

	@Bind(R.id.tradi_tog)//是否选择担保交易
	ToggleButton tradi_tog;
	@Bind(R.id.tradi_tog2)//是否投保
	ToggleButton tradi_tog2;
	@Bind(R.id.butsumit)
	Button butsumit;
	@Bind(R.id.but_nosumit)
	Button but_nosumit;
	@Bind(R.id.rela_jia)//价值的布局
	RelativeLayout rela_jia;
	@Bind(R.id.jiazhi)//价值
	EditText jiazhi;
	@Bind(R.id.rela_zhao)//汽车车牌号照片
	RelativeLayout rela_zhao;
	@Bind(R.id.textView12)//上传上门或者送货上门的文字改变
	TextView textView12;
	@Bind(R.id.baolv)//上传上门或者送货上门的文字改变
	TextView baolv;
	@Bind(R.id.textsheng2)//声明
	TextView textsheng2;
	@Bind(R.id.textsheng)//声明
	TextView textsheng;
	@Bind(R.id.rela_wupin)//物品类别
	RelativeLayout rela_wupin;
	@Bind(R.id.rela_baoxian)//保险类别
	RelativeLayout rela_baoxian;
	@Bind(R.id.check_chang)//常用物品
	RadioButton check_chang;
	@Bind(R.id.check_guoshu)//果蔬物品
	RadioButton check_guoshu;
	@Bind(R.id.check_lengcang)//冷藏物品
	RadioButton check_lengcang;
	@Bind(R.id.check_yisui)//易碎物品
	RadioButton check_yisui;
	@Bind(R.id.check_jiben)//基本保险
	RadioButton check_jiben;
	@Bind(R.id.check_zonghe)//综合保险
	RadioButton check_zonghe;
	@Bind(R.id.radioGroup_wupin)//wup
	RadioGroup radioGroup_wupin;
	@Bind(R.id.radioGroup_baoxian)//baoxian
	RadioGroup radioGroup_baoxian;
	@Bind(R.id.idcardr)//上传照片
	ImageView idcardr;
	@Bind(R.id.btnLeft)//返回
	ImageView btnLeft;
	@Bind(R.id.btnRight)//返回
	TextView btnRight;
	
	@Bind(R.id.checkbox_ming)//
	CheckBox checkbox_ming;
	
	@Bind(R.id.toubao)
	TextView toubao;
	@Bind(R.id.bbbao)
	LinearLayout bbbao;
	
	
	
	private Bitmap imghead;
	private String fileName = path + "cardimg.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String icon;
	private boolean flag = false;// false表示没有上传物品图片，true相反
	private IconBean bean;
	
	int WLBId;
	Double insureCost;
	String moneyString;
	private boolean top1=true;
	private boolean ming;
	boolean top2;
	DowInsuraBean	adbean;
	LoOfferBean  loff;
	boolean takeCargo;
	String wayde;
	int wu=1;
	int xian=1;
	private RelativeLayout mInputLayout;
	private ImageButton mArrow;
	private TextView mInput;
	private PopupWindow mSelectWindow;
	private LayoutInflater mInflater;
	private ArrayList<String> mAccounts;
	HaiBean haibean1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tradingcertain);
		ButterKnife.bind(TradingcertaintyActivity.this);
		WLBId=getIntent().getIntExtra("WLBId", 0);
//		insureCost=	getIntent().getDoubleExtra("insureCost", 0.0);//保费
		moneyString=	getIntent().getStringExtra("transferMoney");
		Log.e("11111111recId", ""+WLBId);
		wayde=getIntent().getStringExtra("wayde");
//		if (wayde.equals("1")) {
//			tradi_tog.setVisibility(View.GONE);
//		}
		takeCargo=getIntent().getBooleanExtra("takeCargo", false);
		if (takeCargo) {
			textView12.setText("请填写上门取货车辆的车牌号");
		}else {
			textView12.setText("请填写送到货场车辆的车牌号");
		}
		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			tradi_tog2.setChecked(false);
			tradi_tog2.setVisibility(View.GONE);
		}
		
		setOnClick();
		getmessage(1);
		initView();
		getHttprequst();
		setda();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
	}
	private void setda(){

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAccounts = new ArrayList<String>();
	}
	public void onDownArrowClicked(View view)
	{
		if (mAccounts.size() != 0)
		{
			mArrow.setBackgroundResource(R.drawable.arrow_up);
			showAccountChoiceWindow();
		}
	}

	private void showAccountChoiceWindow()
	{
		View view = mInflater.inflate(R.layout.input_selectlist, null);
		RelativeLayout contentview = (RelativeLayout) view.findViewById(R.id.input_select_listlayout);
		ListView userlist = (ListView) view.findViewById(R.id.input_select_list);
		userlist.setDividerHeight(0);

		SelectListAdapter adapter = new SelectListAdapter(this, mAccounts);
		adapter.setOnItemClickListener(this);
		adapter.setOnDelBtnClickListener(this);
		userlist.setAdapter(adapter);

		mSelectWindow = new PopupWindow(contentview, mInputLayout.getMeasuredWidth(), LayoutParams.WRAP_CONTENT, true);
		mSelectWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_input_bottom_normal));
		int heig = TradingcertaintyActivity.this.getWindowManager().getDefaultDisplay().getHeight();
		if (haibean1.data.size()>6) {
			mSelectWindow.setHeight(heig*3/5);
		}
		mSelectWindow.setOutsideTouchable(true);
		mSelectWindow.setFocusable(true);
		mSelectWindow.setOnDismissListener(this);
		// 设置popWindow的显示和消失动画
		mSelectWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//		mSelectWindow.showAsDropDown(mInputLayout, 0, 0);
		int[] location = new int[2];
		mInputLayout.getLocationOnScreen(location);
		mSelectWindow.showAtLocation(mInputLayout, Gravity.NO_GRAVITY, location[0], location[1]-mSelectWindow.getHeight());
		mArrow.setBackgroundResource(R.drawable.arrow_up);
	}

	@Override
	public void onDelBtnClicked(int position)
	{
		mSelectWindow.dismiss();
		String inputAccount = mInput.getText().toString();
		if (inputAccount == mAccounts.remove(position))
		{
			String nextAccount = "";

			if (mAccounts.size() != 0)
				nextAccount = mAccounts.get(0) + "";

//			mInput.setText(nextAccount);
		}
		
		detegetHttprequst(haibean1.getData().get(position).recId);
		
	}

	@Override
	public void onItemClicked(int position)
	{
		mSelectWindow.dismiss();
		mInput.setText(mAccounts.get(position) + "");
	}

	@Override
	public void onDismiss()
	{
		mArrow.setBackgroundResource(R.drawable.arrow_down);
	}
	
	/**
	 * 获取车牌
	 */
	private void getHttprequst() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.HaiCarNumber, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("111json", new String(arg2));
						haibean1 = new Gson().fromJson(new String(arg2),
								HaiBean.class);
						dialog.dismiss();
						for (int i = 0; i < haibean1.data.size(); i++)
						{
							mAccounts.add(haibean1.data.get(i).carNum);
						}
						if (mAccounts.size()>0) {
							mInput.setText(mAccounts.get(0) + "");
						}
						} 

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
//						ToastUtil.shortToast(HaiLogisticalActivity.this, "网络请求加载失败");

					}
				});

	}


	/**
	 * 删除车牌
	 */
	private void detegetHttprequst( int recId) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.HaiDeleteCarNum, "recId", ""+recId), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						BaseBean		bean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (bean.getErrCode()!=0) {
							ToastUtil.shortToast(TradingcertaintyActivity.this, bean.getMessage());
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
//						ToastUtil.shortToast(TradingcertaintyActivity.this, "网络请求加载失败");

					}
				});

	}

	
	
	@Override
	public void initView() {
		mInputLayout=(RelativeLayout) findViewById(R.id.input_layout);
		mArrow = (ImageButton) findViewById(R.id.input_arrow);
		mInput = (TextView) findViewById(R.id.input_et);
		mInput.setVisibility(View.GONE);
		// TODO Auto-generated method stub
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
				Intent intent=new Intent();
				intent.putExtra("url", "http://www.efamax.com/mobile/InsureWL.html");
				intent.putExtra("name", "2");
				intent.setClass(TradingcertaintyActivity.this, HWdvertActivity.class);
				startActivity(intent);
			}
		});
		textsheng.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("url", "http://www.efamax.com/mobile/InsureWL.html");
				intent.putExtra("name", "2");
				intent.setClass(TradingcertaintyActivity.this, HWdvertActivity.class);
				startActivity(intent);
			}
		});
		textsheng2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("url", "http://www.efamax.com/mobile/InsureWL.html");
				intent.putExtra("name", "2");
				intent.setClass(TradingcertaintyActivity.this, HWdvertActivity.class);
				startActivity(intent);
			}
		});
		check_zonghe.setVisibility(View.GONE);
		check_jiben.setVisibility(View.GONE);
		radioGroup_wupin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (check_chang.getId()==checkedId) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "常用物品");
					check_zonghe.setVisibility(View.VISIBLE);
					check_jiben.setVisibility(View.VISIBLE);
					wu=1;
				}
				if (check_guoshu.getId()==checkedId) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "蔬菜水果");
					check_zonghe.setVisibility(View.GONE);
					check_jiben.setVisibility(View.VISIBLE);
					wu=2;
				}
				if (check_lengcang.getId()==checkedId) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "水果");
					check_zonghe.setVisibility(View.GONE);
					check_jiben.setVisibility(View.VISIBLE);
					wu=3;
				}
				if (check_yisui.getId()==checkedId) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "易碎物品");
					check_zonghe.setVisibility(View.GONE);
					check_jiben.setVisibility(View.VISIBLE);
					wu=4;
				}
			}
		});
		radioGroup_baoxian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (check_jiben.getId()==checkedId) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "基本险");
					xian=1;
				}
				if (check_zonghe.getId()==checkedId) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "综合险");
					xian=2;
				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		tradi_tog.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					top1=isChecked;
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "1 "+isChecked);
					tradi_tog2.setVisibility(View.VISIBLE);
					
					  but_nosumit.setVisibility(View.VISIBLE);
				}else {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, "2 "+isChecked);
					    top1=isChecked;
					    tradi_tog2.setVisibility(View.GONE);
					    butsumit.setText("确 定");
					    but_nosumit.setVisibility(View.GONE);
					    top2=false;
				}
			}
		});
			tradi_tog2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, final boolean isChecked) {
					// TODO Auto-generated method stub
					RequestParams params = new RequestParams();
					AsyncHttpUtils.doGet(
							UrlMap.getUrl(MCUrl.Loin, "userId",
									String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
							null, null, params, new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						}
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("111111", new String(arg2));
								adbean = new Gson().fromJson(new String(arg2), DowInsuraBean.class);
							if (adbean.getErrCode()==0) {
								baolv.setText("（保险费=货物价值×"+adbean.getData().get(0).value+"‰）");
//								showPaywindow();
//								Log.e("11111111value  ", ""+adbean.getData().get(0).value);
//								if (isChecked) {
//									Intent intent=new Intent();
//									intent.setClass(TradingcertaintyActivity.this, HjasActivity.class);
//									startActivityForResult(intent, 4);
//									top2=isChecked;
//									rela_jia.setVisibility(View.VISIBLE);
//									rela_zhao.setVisibility(View.GONE);
//									rela_wupin.setVisibility(View.VISIBLE);
//									rela_baoxian.setVisibility(View.VISIBLE);
//								}else {
									top2=isChecked;
									rela_jia.setVisibility(View.VISIBLE);
									rela_wupin.setVisibility(View.VISIBLE);
									rela_baoxian.setVisibility(View.VISIBLE);
									bbbao.setVisibility(View.VISIBLE);
									
//								}
								
							}else {
								top2=false;
								tradi_tog2.setChecked(false);
								ToastUtil.shortToast(TradingcertaintyActivity.this, adbean.getMessage());
							}
						}
					});
				}
			});
		
			but_nosumit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					if (top1) {
//						
//					}else {
////						getaddsurplus();
//					}
					
					Intent intent=new Intent();
					intent.setClass(TradingcertaintyActivity.this, MylogisticakActivity.class);
					startActivity(intent);
					finish();
				}
			});
			
			butsumit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {//LogAddInsurance
					if (top2==true) {
					if (jiazhi.getText().toString().equals("") ) {
						ToastUtil.shortToast(TradingcertaintyActivity.this, "请输入货物价值");
					} else {
						if (ming) {
							getInsuran();
						}else {
							ToastUtil.shortToast(TradingcertaintyActivity.this, "请阅读并同意投保协议");
						}
					}
					}else {
						getmessage(2);
					}
					
				}
			});
			
//			idcardr.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					showPopwindow();
//				}
//			});
			
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
//	 			}
	 		});
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("11111111  ", resultCode+" ssss   "+data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				if (data != null) {
				cropPhoto(data.getData());// 裁剪图片
				}
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/cardimg.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				imghead = extras.getParcelable("data");
				if (imghead != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					setPicToView(imghead);
					map.put("recId",String.valueOf(loff.data.get(0).recId));
					map.put("fileName", "汽车车牌号");
					map_file.put("file", new File(fileName));
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
					idcardr.setBackgroundDrawable(null);// 切换图片前先置空
					idcardr.setImageBitmap(imghead);
				}
			}
			break;
			
		case 4:
			if (data!=null) {
				if (data.getStringExtra("type").equals("2")) {
//					ToastUtil.shortToast(TradingcertaintyActivity.this, data.getStringExtra("type"));
					tradi_tog2.setChecked(false);
				}else {
					tradi_tog2.setChecked(true);
				}
			}
			break;
		}
	}
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindow() {
		final PopupWindow window02;
		TextView btnsaves_pan,tet_tishi,tet_tishise;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_baojiase, null);
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
		window02.showAtLocation(TradingcertaintyActivity.this.findViewById(R.id.butsumit), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
		tet_tishi=(TextView) view.findViewById(R.id.tet_tishi);
		tet_tishise=(TextView) view.findViewById(R.id.tet_tishise);
		tet_tishise.setText("保费="+loff.data.get(0).cargoCost+"×"+adbean.getData().get(0).value+"‰"+"="+loff.data.get(0).insureCost+"元");
		tet_tishi.setText("(保费=物品价值×"+adbean.getData().get(0).value+"‰ )");
		btnsaves_pan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
	 * 非担保交易
	 */
	public void getaddsurplus() {
//		if (getIntent().getStringExtra("billCode").equals("") || getIntent().getStringExtra("transferMoney").equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
//			return;
//		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId",getIntent().getIntExtra("userId", 0));
			obj.put("billCode", getIntent().getStringExtra("billCode"));
			obj.put("whether", ""+0);
			obj.put("premium", getIntent().getStringExtra("premium"));
			obj.put("WLBId", getIntent().getIntExtra("recId", 0));
//			obj.put("WLBId", getIntent().getIntExtra("recId", 0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("obj", UrlMap.getfive(MCUrl.Balance, "userId",""+getIntent().getIntExtra("userId", 0),"WLBId",""+getIntent().getIntExtra("WLBId", 0), 
				"whether",""+false,"warrant",""+0,"transferMoney", getIntent().getStringExtra("transferMoney")));
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doGet(UrlMap.getfive(MCUrl.Balance, "userId",""+getIntent().getIntExtra("userId", 0),"WLBId",""+getIntent().getIntExtra("WLBId", 0), 
//				"whether",""+0,"warrant",""+0,"transferMoney", getIntent().getStringExtra("transferMoney")), null, null, params,
//				new AsyncHttpResponseHandler() {
			AsyncHttpUtils.doGet(UrlMap.getfive(MCUrl.Balance, "userId",""+loff.data.get(0).userToId,"WLBId",""+loff.data.get(0).recId, 
					"whether",""+0,"warrant",""+0,"transferMoney",loff.data.get(0).transferMoney), null, null, params,
					new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						dialog.dismiss();
						Intent intent=new Intent();
						if (arg2 == null)
							return;
						Log.e("msg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(TradingcertaintyActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
//							intent.setClass(TradingcertaintyActivity.this, MainActivity.class);//个人
//							intent.setClass(TradingcertaintyActivity.this, MainTab.class);//个人
							intent.setClass(TradingcertaintyActivity.this, PickActivity.class);//个人
							startActivity(intent);
							finish();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method
						// stub
						Log.e("1111111", ""+arg0);
						dialog.dismiss();
					}
				});
	}
	
	public void getInsuran(){
		JSONObject obj = new JSONObject();
		try {
			obj.put("recId", getIntent().getIntExtra("WLBId", 0));
			obj.put("userId",String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("cargoCost", jiazhi.getText().toString());
//			obj.put("carNumImg", mInput.getText().toString());
			obj.put("category", wu);
			obj.put("insurance", xian);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(TradingcertaintyActivity.this, MCUrl.LogAddInsurance, obj.toString(),
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
						InsuranBean	 inensb = new Gson().fromJson(new String(arg2),
								 InsuranBean.class);
						if (inensb.errCode==0) {
							getmessage(2);
						}else {
							ToastUtil.shortToast(TradingcertaintyActivity.this, inensb.message);
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	public void getmessage(final int i){
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.QuotationInfo, "recId",getIntent().getIntExtra("WLBId", 0)+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						 loff = new Gson().fromJson(new String(arg2),
								LoOfferBean.class);
						if (loff.errCode==0) {
							if (i==2) {
							if (top1) {
								// TODO Auto-generated method stub
//								AlertDialog.Builder ad = new Builder(TradingcertaintyActivity.this);
//								ad.setTitle("温馨提示");
//								ad.setMessage("是否确认支付");
//								ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface arg0, int arg1) {
									  Intent intent = new Intent(TradingcertaintyActivity.this, MydetailedPayActivity.class);
										intent.putExtra("userId", loff.data.get(0).userToId);//
										intent.putExtra("WLBId",  loff.data.get(0).recId);//
								 		intent.putExtra("billCode",  loff.data.get(0).billCode);//物流单号
//								 		Log.e("22211value  ", ""+adbean.getData().get(0).value);
								 		if (top2==true) {
//								 			intent.putExtra("way",    adbean.getData().get(0).value);
								 			intent.putExtra("whether", 1);
												String str= loff.data.get(0).transferMoney;
											    double d=Double.valueOf(str).doubleValue();
											    intent.putExtra("insureCost", loff.data.get(0).insureCost);//
												intent.putExtra("transferMoney", add(loff.data.get(0).insureCost, d));
												Log.e("111111w  ", ""+add(loff.data.get(0).insureCost, d));
										}else {
//											intent.putExtra("way", "0");
											intent.putExtra("whether", 0);
											String str=loff.data.get(0).transferMoney;
											   double d=Double.valueOf(str).doubleValue();
											  intent.putExtra("transferMoney", d);
//											  Log.e("22211333w  ", ""+add(insureCost, d));
										  }
								 		intent.putExtra("takeCargoMoney", loff.data.get(0).takeCargoMoney);//上门取货费
								 		intent.putExtra("sendCargoMoney",  loff.data.get(0).sendCargoMoney);//送货上门费
										intent.putExtra("cargoTotal",  loff.data.get(0).cargoTotal);//运费
										if (loff.data.get(0).status.equals("7") || loff.data.get(0).status.equals("8")) {
											startActivityForResult(intent, 10);
									 		finish();
										}else {
											ToastUtil.shortToast(TradingcertaintyActivity.this, "改单已经支付");
											finish();
										}
								 		
//									}
//								});
//								ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface arg0, int arg1) {
//										arg0.dismiss();
//									}
//								});
//								ad.create().show();
							}
							else {
								Builder ad = new Builder(TradingcertaintyActivity.this);
								ad.setTitle("温馨提示");
								ad.setMessage("是否选择非担保");
								ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										
										getaddsurplus();
									}
								});
								ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										arg0.dismiss();
										
									}
								});
								ad.create().show();
							}
						}else {
//							String str= loff.data.get(0).transferMoney;
//							  double d=Double.valueOf(str).doubleValue();
//								Log.e("22211w  ", ""+add(loff.data.get(0).insureCost, d));
							
						
						}
					}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	
	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.transparent);
		window.setBackgroundDrawable(dw);
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(TradingcertaintyActivity.this.findViewById(R.id.butsumit), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(TradingcertaintyActivity.this, IdCardActivity.class).putExtra("iconpath", "goodpath"));
				window.dismiss();
			}
		});
		tv_camera.setClickable(true);
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cardimg.png")));
				// intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent2, 2);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				// 相册选择
				Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent1, 1);

//				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				
				
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});
	}
	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				Log.e("logurl", MCUrl.IDCDHEAD);
				result = post(MCUrl.WlCarNum, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			Log.e("result", result);
			bean = new Gson().fromJson(result, IconBean.class);
			if (bean.data.size() != 0) {
				flag = true;
				icon = bean.data.get(0).filePath;
				Log.e("filePath", bean.data.get(0).filePath);
				setPicToView(imghead);
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				idcardr.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
				idcardr.setImageBitmap(imghead);
			}
			break;

		default:
			break;
		}
	}

	/***
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
		
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param head2
	 */

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 *            访问的服务器URL
	 * @param params
	 *            普通参数
	 * @param files
	 *            文件参数
	 * @return
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params, Map<String, File> files)
			throws IOException {

		StringBuilder sb2 = new StringBuilder();

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name是post中传参的键 filename是文件的名称
				sb1.append(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				int ch;

				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			}
			outStream.close();
			conn.disconnect();
		}
		return sb2.toString();
	}

	/* 显示Dialog的method */
	private void showDialog(String mess) {
	}

	public InputStream getStream(File file) {
		// 第2步、通过子类实例化父类对象
		// File f= new File("d:" + File.separator + "test.txt") ; // 声明File对象
		// 第2步、通过子类实例化父类对象
		InputStream input = null; // 准备好一个输入的对象
		try {
			input = new FileInputStream(file); // 通过对象多态性，进行实例化
			// 第3步、进行读操作
			// byte b[] = new byte[input..available()] ; 跟使用下面的代码是一样的
			byte b[] = new byte[(int) file.length()]; // 数组大小由文件决定
			int len = input.read(b); // 读取内容
			// 第4步、关闭输出流

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return input;
	}

	private boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void fileDelete(String path) {

		if (fileIsExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}

	// 点击空白区域关闭输入法
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputMethodManager.hideSoftInputFromWindow(TradingcertaintyActivity.this.getCurrentFocus().getWindowToken(),
//				InputMethodManager.HIDE_NOT_ALWAYS);
//		return super.onTouchEvent(event);
//	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	/** 
	* * 两个Double数相加 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double add(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   Double bDouble=	   b1.add(b2).doubleValue();
	   return bDouble;  
	} 


}
