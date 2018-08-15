package com.hex.express.iwant.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.SelectListAdapter;
import com.hex.express.iwant.adapters.SelectListAdapter.OnDelBtnClickListener;
import com.hex.express.iwant.adapters.SelectListAdapter.OnItemClickListener;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.HaiBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
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
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *收货界面
 */
public class LogCompanyDeliveryActivity extends BaseActivity implements OnItemClickListener, OnDelBtnClickListener, OnDismissListener{

	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.delver_img_head)
	ImageView delver_img_head;
	@Bind(R.id.delver_logoffer_username)
	TextView delver_logoffer_username;
	@Bind(R.id.delver_logoffer_time)
	TextView delver_logoffer_time;
	@Bind(R.id.delver_huoname)
	TextView delver_huoname;
	@Bind(R.id.delver_zhong)
	TextView delver_zhong;
	@Bind(R.id.delver_tiji)
	TextView delver_tiji;
	@Bind(R.id.delver_quhuo)
	TextView delver_quhuo;
	@Bind(R.id.delver_songhuo)
	TextView delver_songhuo;
	@Bind(R.id.delver_fahuodizhi)
	MarqueeTextView delver_fahuodizhi;
	@Bind(R.id.delver_dadaodizhi)
	MarqueeTextView delver_dadaodizhi;
	@Bind(R.id.delver_shouhuoname)
	TextView delver_shouhuoname;
	@Bind(R.id.delver_lianxifangshi)//
	TextView delver_lianxifangshi;
	@Bind(R.id.delver_fahuotime)//
	TextView delver_fahuotime;
	@Bind(R.id.delver_dadaotime)//
	TextView delver_dadaotime;
	@Bind(R.id.delver_beizhu)//
	TextView delver_beizhu;
	@Bind(R.id.delver_logoffer_jiazhi)//
	TextView delver_logoffer_jiazhi;
	@Bind(R.id.guige)//
	TextView guige;
	
	@Bind(R.id.carcold)//
	TextView carcold;
	
	
	
	@Bind(R.id.delver_but)//报价
	Button delver_but;
	@Bind(R.id.delver_img1)//第一张图
	ImageView delver_img1;
	@Bind(R.id.delver_img2)//第二张图
	ImageView delver_img2;
	@Bind(R.id.delver_img3)//第三张图
	ImageView delver_img3;
	
	@Bind(R.id.tem)//
	TextView tem;
	@Bind(R.id.recoldcar)//冷链
	RelativeLayout	recoldcar;
	@Bind(R.id.resonghuo)//正常
	LinearLayout	resonghuo;
	
	private Bitmap head;
	private String fileName,fileNamet,fileNames;
	private static String path = "/sdcard/myHeads/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String result2;
	private String result3;
	private String icon = "";
	private String icon2 = "";
	private String icon3 = "";
	private boolean flag1,flag2,flag3;
	
	private RelativeLayout mInputLayout;
	private ImageButton mArrow;
	private TextView mInput;
	private PopupWindow mSelectWindow;
	private LayoutInflater mInflater;
	private ArrayList<String> mAccounts;
	HaiBean haibean1;
	LogisBean Logisbean;
	String num1;
	String num2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_locamdeliver);//activity_locamdeliver  activity_locamdeliverse
		ButterKnife.bind(LogCompanyDeliveryActivity.this);
		mInputLayout=(RelativeLayout) findViewById(R.id.input_layout);
		mArrow = (ImageButton) findViewById(R.id.input_arrow);
		mInput = (TextView) findViewById(R.id.input_et);
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
		if (getIntent().getStringExtra("way").equals("2")) {
			delver_but.setVisibility(View.GONE);	
			mInput.setText(""+getIntent().getStringExtra("carNumImg"));
		}else {
		
		if (getIntent().getIntExtra("status", 0)==1 || getIntent().getIntExtra("status", 0)==2 || getIntent().getIntExtra("status", 0)==8) {
			delver_but.setVisibility(View.VISIBLE);
			getHttprequst();
		}else {
			mInput.setText(""+getIntent().getStringExtra("carNumImg"));
			delver_but.setVisibility(View.GONE);	
		}
		}
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAccounts = new ArrayList<String>();
//		if (getIntent().getIntExtra("status", 0)==8) {
//			delver_but.setVisibility(View.VISIBLE);
//		}
		// TODO Auto-generated method stub
//		if (!"".equals(getIntent().getStringExtra("sendName"))) {
//			new MyBitmapUtils().display(delver_img_head,getIntent().getStringExtra("sendName"));
//		}
		guige.setText("总体积："+getIntent().getStringExtra("cargoVolume"));

		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			recoldcar.setVisibility(View.VISIBLE);	
			resonghuo.setVisibility(View.GONE);
			tem.setText("温度要求："+getIntent().getStringExtra("tem"));
		}else {
			recoldcar.setVisibility(View.GONE);
			resonghuo.setVisibility(View.VISIBLE);
		}
//		intent.putExtra("carType",  bean.getData().get(position).carType);//件数
//		intent.putExtra("carName", bean.getData().get(position).carName);//
		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			carcold.setText("需求："+getIntent().getStringExtra("carName"));
		}
//		guige.setText("单件规格："+"长："+getIntent().getStringExtra("length")+" 厘米"+" 宽："+getIntent().getStringExtra("wide")+" 厘米"+" 高："+getIntent().getStringExtra("high")+" 厘米");
		delver_logoffer_username.setText("发件人："+getIntent().getStringExtra("sendName"));
		delver_logoffer_time.setText("电话："+getIntent().getStringExtra("sendMobile"));
//		delver_juli.setText("距离:"+getIntent().getStringExtra("distance")+"km");
		delver_huoname.setText("物品名称："+getIntent().getStringExtra("cargoName"));
//		if (getIntent().getStringExtra("cargoWeight").equals("5")) {
//			delver_zhong.setText("总重量：≤"+getIntent().getStringExtra("cargoWeight")+" 公斤");
//		}else {
			delver_zhong.setText("总重量："+getIntent().getStringExtra("cargoWeight")+" ");
//		}
		
		delver_tiji.setText("件数："+getIntent().getStringExtra("cargoSize")+"  件");
		delver_logoffer_jiazhi.setText("货物价值："+getIntent().getStringExtra("cargoCost")+" ");
//		spec_quhuo.setText(getIntent().getStringExtra(""));
//		spec_songhuo.setText(getIntent().getStringExtra(""));
		delver_fahuodizhi.setText(getIntent().getStringExtra("startPlace"));
		delver_dadaodizhi.setText(getIntent().getStringExtra("entPlace"));
		delver_shouhuoname.setText("收货人："+getIntent().getStringExtra("takeName"));
		delver_lianxifangshi.setText("电话："+getIntent().getStringExtra("takeMobile"));
		delver_fahuotime.setText("发货时间："+getIntent().getStringExtra("takeTime"));
		delver_dadaotime.setText("要求到达时间："+getIntent().getStringExtra("arriveTime"));
		if (!"".equals(getIntent().getStringExtra("arriveTime"))) {
			delver_beizhu.setText("指定园区："+getIntent().getStringExtra("appontSpace"));
		}else {
			delver_beizhu.setVisibility(View.GONE);
		}
		if(getIntent().getBooleanExtra("takeCargo",true)){
			delver_quhuo.setText("物流公司上门取货");
		}else {
			delver_quhuo.setText("发件人送货");
		}
		if(getIntent().getBooleanExtra("sendCargo",true)){
			delver_songhuo.setText("物流公司送货上门");
		}else {
			delver_songhuo.setText("收件人自提");
		}
		
		if (getIntent().getStringExtra("firstPicture")!=null) {
			Log.e("111111P   ", getIntent().getStringExtra("firstPicture"));
			//得到可用的图片
			String urlString1=getIntent().getStringExtra("firstPicture");
			String urlString2=getIntent().getStringExtra("secondPicture");
			String urlString3=getIntent().getStringExtra("thirdPicture");
//	        Bitmap bitmap = MessageUtils.getHttpBitmap(urlString);
//			delver_img1.setImageBitmap(bitmap);
			if (!urlString1.equals("")) {
				new MyBitmapUtils().display(delver_img1,
						urlString1);
				delver_img1.setBackgroundDrawable(null);
			}else {
				delver_img1.setBackgroundResource(R.drawable.bg_newpai);
			}
			if (!urlString2.equals("")) {
				new MyBitmapUtils().display(delver_img2,
						urlString2);
				delver_img2.setBackgroundDrawable(null);
				
			}else {
				delver_img2.setBackgroundResource(R.drawable.bg_newpai);
			}
			if (!urlString3.equals("")) {
				new MyBitmapUtils().display(delver_img3,
						urlString3);
				delver_img3.setBackgroundDrawable(null);
			}else {
				delver_img3.setBackgroundResource(R.drawable.bg_newpai);
			}
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
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
		int heig = LogCompanyDeliveryActivity.this.getWindowManager().getDefaultDisplay().getHeight();
//		mSelectWindow.setHeight(heig*3/5);
		if (haibean1.data.size()>6) {
			mSelectWindow.setHeight(heig*3/5);
		}
		// 实例化一个ColorDrawable颜色为半透明
					 ColorDrawable dw = new ColorDrawable(R.color.transparent01);
					mSelectWindow.setBackgroundDrawable(dw);
		mSelectWindow.setOutsideTouchable(true);
		mSelectWindow.setFocusable(true);
		mSelectWindow.setOnDismissListener(this);
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

			mInput.setText(nextAccount);
		}
		
		detegetHttprequst(haibean1.getData().get(position).recId);
//		Toast.makeText(this, "删除账号成功", Toast.LENGTH_SHORT).show();
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
						Log.e("json", new String(arg2));
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
						BaseBean		bean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (bean.getErrCode()!=0) {
							ToastUtil.shortToast(LogCompanyDeliveryActivity.this, bean.getMessage());
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "网络请求加载失败");

					}
				});

	}

	
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
	btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				
			}
		});
		delver_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (getIntent().getIntExtra("status", 0)==8) {
					ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "请通知用户付款后再发货");
				}else {
				addPostResult();
				}
			}
		});
		delver_img1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (getIntent().getIntExtra("status", 0)==8) {
					ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "请通知用户付款后再发货");
				}else {
					if (getIntent().getIntExtra("status", 0)!=4) {
						showPopwindow();
					}
				
				}
			}
		});
	 delver_img2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (getIntent().getIntExtra("status", 0)==8) {
					ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "请通知用户付款后再发货");
				}else {
					if (getIntent().getIntExtra("status", 0)!=4) {
					showPopwindowtow();
					}
				}
				
			}
		});
	  delver_img3.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (getIntent().getIntExtra("status", 0)==8) {
				ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "请通知用户付款后再发货");
			}else {
				if (getIntent().getIntExtra("status", 0)!=4) {
			     showPopwindowsend();
				}
			}
		}
	});
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		if(!"".equals(icon) || !"".equals(icon2) || !"".equals(icon3)){
			
//		if(!flag2){
//			ToastUtil.shortToast(getApplicationContext(), "请上传物品照片");
//			return;
//		}
//		if(!flag3){
//			ToastUtil.shortToast(getApplicationContext(), "请上传物品照片");
//			return;
//		}
			if (mInput.getText().toString().equals("")) {
				ToastUtil.shortToast(getApplicationContext(), "请上传汽车牌号");
				return;
			}
		
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("recId", getIntent().getIntExtra("recId", 0));//图片1
			obj.put("firstPicture", icon);//图片1recId
			obj.put("secondPicture", icon2);//图片2
			obj.put("thirdPicture", icon3);//图片3
			obj.put("carNumImg", mInput.getText().toString());
//			obj.put("remark", spec_edt_yuan.getText().toString().trim());//报价元
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());//LOGISCORECIVE  LOGISTICS
		AsyncHttpUtils.doPostJson(LogCompanyDeliveryActivity.this, MCUrl.LOGISCORECIVE, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
//						ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						if (bean.getErrCode()==0) {
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
								finish();
						}else {
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
								finish();
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
		
		}else {
			ToastUtil.shortToast(getApplicationContext(), "请上传物品照片");
		}
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
		window.showAtLocation(LogCompanyDeliveryActivity.this.findViewById(R.id.delver_img1), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LogCompanyDeliveryActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "iconpath"));
				window.dismiss();
			}
		});
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "loghea.png")));
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
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.[_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 1);
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
	/**
	 * 显示2popupWindow
	 */
	private void showPopwindowtow() {
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
		window.showAtLocation(LogCompanyDeliveryActivity.this.findViewById(R.id.delver_img1), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LogCompanyDeliveryActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "iconpath"));
				window.dismiss();
			}
		});
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "logheas.png")));
				startActivityForResult(intent2, 22);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.[_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 11);
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
	/**
	 * 显示2popupWindow
	 */
	private void showPopwindowsend() {
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
		window.showAtLocation(LogCompanyDeliveryActivity.this.findViewById(R.id.delver_img1), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LogCompanyDeliveryActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "iconpath"));
				window.dismiss();
			}
		});
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "loghease.png")));
				startActivityForResult(intent2, 222);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.[_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 111);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/loghea.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
			Log.e("111111444    ", ""+data);
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				Logger.e("img.1111111"  +head);
				if (head != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					setPicToView(head);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName", "物流公司物品1");
					map_file.put("file", new File(fileName));
					// String result="";
//					delver_img1.setBackgroundDrawable(null);
//					delver_img1.setImageBitmap(head);
					
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				}
			}
			break;
		// 如果是直接从相册获取
			case 11:
						if (resultCode == RESULT_OK) {
							cropPhototow(data.getData());// 裁剪图片
						}
						break;
					// 如果是调用相机拍照时
					case 22:
						if (resultCode == RESULT_OK) {
							File temp = new File(Environment.getExternalStorageDirectory() + "/logheas.png");
							cropPhototow(Uri.fromFile(temp));// 裁剪图片
						}
						break;

					// 取得裁剪后的图片
					case 33:
						Log.e("1111112222    ", ""+data);
						if (data != null) {
							Bundle extras = data.getExtras();
							head = extras.getParcelable("data");
							Logger.e("img.11111112222"  +head);
							if (head != null) {
								/**
								 * 上传服务器代码 //TODO 实现头衔上传
								 */
								setPicToViewtwo(head);
								map.put("userId",
										String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
								map.put("fileName", "物流公司物品2");
								map_file.put("file", new File(fileNamet));
								// String result="";
								sendEmptyBackgroundMessage(MsgConstants.MSG_02);
							}
						}
						break;
						// 如果是直接从相册获取
					case 111:
								if (resultCode == RESULT_OK) {
									cropPhotosend(data.getData());// 裁剪图片
								}
								break;
							// 如果是调用相机拍照时
							case 222:
								if (resultCode == RESULT_OK) {
									File temp = new File(Environment.getExternalStorageDirectory() + "/loghease.png");
									cropPhotosend(Uri.fromFile(temp));// 裁剪图片
								}
								break;

							// 取得裁剪后的图片
							case 333:
								Log.e("1111113333    ", ""+data);
								if (data != null) {
									Bundle extras = data.getExtras();
									head = extras.getParcelable("data");
									Logger.e("img.11111113333"  +head);
									if (head != null) {
										/**
										 * 上传服务器代码 //TODO 实现头衔上传
										 */
										setPicToViewsen(head);
										map.put("userId",
												String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
										map.put("fileName", "物流公司物品3");
										map_file.put("file", new File(fileNames));
										// String result="";
										sendEmptyBackgroundMessage(MsgConstants.MSG_03);
									}
								}
								break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				result = post(MCUrl.LOGISCOMPANIMGFIRST, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_02:
			try {
				result2 = post(MCUrl.LOGISCOMPANIMGSECOND, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_02);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_03:
			try {
				result3 = post(MCUrl.LOGISCOMPANIMGTHIRD, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_03);
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			Log.e("11111result", result);
			IconBean bean = new Gson().fromJson(result, IconBean.class);
			if (bean.data.size() != 0) {
				flag1=true;
				Log.e("filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "第一张上传成功");
				icon = bean.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("11111touxiang", icon);
				delver_img1.setBackgroundDrawable(null);
				delver_img1.setImageBitmap(head);
				
			}
			break;
		case MsgConstants.MSG_02:
			Log.e("11111222result", result2);
			IconBean bean1 = new Gson().fromJson(result2, IconBean.class);
			Log.e("11111222result", ""+bean1.data.size());
			if (bean1.data.size() != 0) {
				flag2=true;
				Log.e("filePath", bean1.data.get(0).filePath);
				ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "第二张上传成功");
				icon2 = bean1.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("111112222touxiang", icon2);
				delver_img2.setBackgroundDrawable(null);
				delver_img2.setImageBitmap(head);
			}
			break;
		case MsgConstants.MSG_03:
			Log.e("11111333result", result3);
			IconBean bean11 = new Gson().fromJson(result3, IconBean.class);
			if (bean11.data.size() != 0) {
				flag3=true;
				Log.e("filePath", bean11.data.get(0).filePath);
				ToastUtil.shortToast(LogCompanyDeliveryActivity.this, "第三张上传成功");
				icon3 = bean11.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("11111333touxiang", icon3);
				delver_img3.setBackgroundDrawable(null);
				delver_img3.setImageBitmap(head);
			}
			break;
		default:
			break;
		}
	}
	/***
	 * 裁剪图片方法实现1
	 * 
	 * @param uri1
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	/***
	 * 裁剪图片方法实现2
	 * 
	 * @param uri
	 */
	public void cropPhototow(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}
	
	/***
	 * 裁剪图片方法实现2
	 * 
	 * @param uri
	 */
	public void cropPhotosend(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 333);
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
		fileName = path + "loghea.png";
		
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
	private void setPicToViewtwo(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		fileNamet = path + "logheas.png";
		try {
			b = new FileOutputStream(fileNamet);
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
	private void setPicToViewsen(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		fileNames = path + "loghease.png";
		try {
			b = new FileOutputStream(fileNames);
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
}
