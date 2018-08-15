package com.hex.express.iwant.homfragment;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.SelectableChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.framework.base.BaseFragment;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.AddressActivity;
import com.hex.express.iwant.activities.AddressReceiveActivity;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.DrawCardActivity;
import com.hex.express.iwant.activities.GuardActivity;
import com.hex.express.iwant.activities.H5ModelActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.HWdvertActivity;
import com.hex.express.iwant.activities.IdCardActivity;
import com.hex.express.iwant.activities.LogiNumberActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.MyExtentionActivity;
import com.hex.express.iwant.activities.NewExerciseActivity;
import com.hex.express.iwant.activities.NewMyWalletActivity;
import com.hex.express.iwant.activities.PostLimitedDownwindTaskActivity;
import com.hex.express.iwant.bean.AdvertBean;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DowInsuraBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.Loginuberbean;
import com.hex.express.iwant.bean.Pickers;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.ForResultNestedCompatFragment;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.homfragment.HomSubFragment2.NetImageHandler;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.imageview.ImageCycleView;
import com.hex.express.iwant.imageview.ImageCycleView.ImageCycleViewListener;
import com.hex.express.iwant.minfragment.MinSubFragment1;
import com.hex.express.iwant.newbaidu.LocationDemo;
import com.hex.express.iwant.newmain.PickerScrollViews;
import com.hex.express.iwant.newmain.PickerScrollViews.onSelectListener;
import com.hex.express.iwant.views.WheelView;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.NormalLoadPictrue;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.viewpager.ADInfo;
import com.hex.express.iwant.viewpager.CycleViewPager;
import com.hex.express.iwant.viewpager.DialogFragmentWindow;
import com.hex.express.iwant.viewpager.FullScreenDlgFragment;
import com.hex.express.iwant.viewpager.ViewFactory;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TimePickerView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class HomSubFragment1 extends BaseFragment {
	
	// 寄件人信息
	@Bind(R.id.et_name)
	EditText et_name;//寄件人姓名
	@Bind(R.id.et_tel)
	EditText et_tel;//寄件人电话
	@Bind(R.id.et_address)
	EditText et_address;//寄件人地址
	@Bind(R.id.tv_modifySenderAddress)
	TextView tv_modifySenderAddress;// 修改寄件人地址 
	@Bind(R.id.iv_senderList)
	ImageView iv_senderList;// 发件人获取手机通讯录；
//	 收件人信息
	@Bind(R.id.et_add_name)
	EditText et_add_name;
	@Bind(R.id.et_add_tel)
	EditText et_add_tel;
	@Bind(R.id.et_add_address)
	EditText et_add_address;
	@Bind(R.id.et_address_specific)
	EditText et_address_specific; //发件人具体地址
	@Bind(R.id.et_add_address_specific)
	EditText et_add_address_specific; //收件人具体地址
	@Bind(R.id.tv_modifyRecieverAddress)
	TextView tv_modifyRecieverAddress;// 修改收件人地址
	@Bind(R.id.iv_receiverList)
	ImageView iv_receiverList;// 收件人获取手机通讯录；
//	// 物品信息
	@Bind(R.id.et_goodname)
	EditText et_goodname;
//	@Bind(R.id.et_weight)
//	TextView et_weight;
	
	@Bind(R.id.et_weightpner)
	Spinner et_weightpner;
	@Bind(R.id.et_good_detail)
	EditText et_good_detail;
//	// 物品长宽高
	@Bind(R.id.et_chang)
	EditText et_chang;
	@Bind(R.id.et_kuan)
	EditText et_kuan;
	@Bind(R.id.et_height)
	EditText et_height;
//	//广告
//	@Bind(R.id.tv_tip)
//	TextView tv_tip;
	@Bind(R.id.rl_bannerTip)
	RelativeLayout rl_bannerTip;
//	
//	// 拍照
	@Bind(R.id.img_headportrait)
	RoundCornerImageView img_headportrait;
//	//时间选择
	@Bind(R.id.et_time)
	EditText et_time;
	@Bind(R.id.edt_daipric)
	EditText edt_daipric;
	
	TimePickerView pvTime;
//	// 确认发布
//	@Bind(R.id.btn_submit)
//	Button btn_submit;
	@Bind(R.id.checkbox_baoss)
	CheckBox checkbox_baoss;
	@Bind(R.id.checkbox_diashou)
	CheckBox checkbox_diashou;
//	@Bind(R.id.checkbox_detai)//是否需要大型车
//	CheckBox checkbox_detai;
	@Bind(R.id.ll03)
	LinearLayout ll03;
	@Bind(R.id.edt_prices)
	EditText edt_prices;
	@Bind(R.id.v_pps)
	View v_pps;
//	@Bind(R.id.tex)
//	TextView tex;
	@Bind(R.id.toubaoxuzhi)
	TextView toubaoxuzhi;
	
	
//	@Bind(R.id.hom1_togg)
//	ToggleButton hom1_togg;   //是否需要大货车
	private AdvertBean adbeans;
	private Bitmap imghead;
	private String fileName = path + "imghead.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	DowInsuraBean	adbean;
	
	//发件人的详细地址
	private String detailLocationOfSender;
	//收件人的详细地址
	private String detailLocationOfReceiver;
	//定位得到的地址
	private String address;
	//选择地址回调得到的地址
	private String return_address;
	// 当前位置经纬度
	private double latitude;
	private double longitude;
	// 当前位置经纬度
		private double latitude2;
		private double longitude2;
	//发件城市
	private String city = "";
	//发件人经纬度
	private double myLatitude;// 纬度
	private double myLongitude;// 经度
	private LocationClient client;
	//发件地址的城市代码
	private String cityCode = "";
	//发件地址的城市
		private String cityCode2 = "";
		private String	townCode2;
	//收件人经纬度
	private double receiverLongitude;
	private double receiverLatitude;
	//收件地址的城市代码
	private String receiver_citycode = "";
	private boolean receive;
	private String icon;
	private boolean flag = false;// false表示没有上传物品图片，true相反
	private boolean frist = false;// 是否第一次获取位置成功
	private boolean chox=false;
	private boolean choxdai=false; 
	private boolean choxde=false; 
	private boolean namesb=false; 
	private String namese,phonese;
	int kes=0;
	PopupWindow window02;
	DownSpecialBean bean;
	private String	townCode,townaddressd,townaddress2;
//	 private Context mContext;
	 private static final String[] CONTACTS_QUERY = {Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID};  
	 private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
     
	    /**电话号码**/  
	    private static final int PHONES_NUMBER_INDEX = 1;  
	      
	    /**头像ID**/  
	    private static final int PHONES_PHOTO_ID_INDEX = 2;  
	     
	    /**联系人的ID**/  
	    private static final int PHONES_CONTACT_ID_INDEX = 3;  
	    private static final int    RESULT_OK=-1;
	    
	    private boolean up;
    @Bind(R.id.hlast_step_layout)
    LinearLayout hlast_step_layout;//第一个界面
    @Bind(R.id.hlast_steps)
    Button hlast_steps;//上一步er
    @Bind(R.id.hlast_stepse)
    Button hlast_stepse;//上一步san
    
    @Bind(R.id.next_layout)
    LinearLayout next_layout;//第二个界面
    @Bind(R.id.hnext_steps)
    Button hnext_steps;//下一步
    @Bind(R.id.zuihoulayout)
    LinearLayout end_layout;//第三个界面
    
    @Bind(R.id.hlast_step)
     Button hlast_step;//上一步
    @Bind(R.id.hnext_step)
     Button hnext_step;//下一步
    
    @Bind(R.id.hsuccess)
     Button hsuccess;//  完成
    View view;
    Thread imageViewHander;
//    @Bind(R.id.che)
//    LinearLayout che;//
//    @Bind(R.id.xiaomian)
//    LinearLayout xiaomian;//
//    @Bind(R.id.zhongmian)
//    LinearLayout zhongmian;//
//    @Bind(R.id.xiaohuo)
//    LinearLayout xiaohuo;//
//    @Bind(R.id.zhonghuo)
//    LinearLayout zhonghuo;//
//    @Bind(R.id.xiaomian1)
//    ImageView xiaomian1;
//    @Bind(R.id.zhongmian1)
//    ImageView zhongmian1;
//    @Bind(R.id.xiaohuo1)
//    ImageView xiaohuo1;
//    @Bind(R.id.zhonghuo1)
//    ImageView zhonghuo1;
    
    private String cartype;
    private boolean dingwei=false; 
//    private Button bt_scrollchoose; // 滚动选择器按钮
	private PickerScrollViews pickerscrlllview; // 滚动选择器
	private List<Pickers> list; // 滚动选择器数据
	private String[] id;
	private String[] name;
	private Button bt_yes; // 确定按钮
	private RelativeLayout picker_rel; // 选择器布局
	public LoadingProgressDialog dialog;
	
	
	private CycleViewPager cycleViewPager;
	private String[] imageUrls = {"http://www.efamax.com/images/lunbo_0.jpg",
			"http://www.efamax.com/images/lunbo_1.jpg",
			"http://www.efamax.com/images/lunbo_2.jpg"};
	private List<ImageView> views = new ArrayList<ImageView>();
	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
	ImageCycleView  mAdView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.fragment_newhom1, container, false);
		}
		 ViewGroup p = (ViewGroup) view.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
         ButterKnife.bind(this, view);
//         mContext=view.getContext();
         dialog=new LoadingProgressDialog(getActivity());
         imageViewHander = new Thread(new NetImageHandler());
//     	picker_rel = (RelativeLayout)view. findViewById(R.id.picker_rel);
//		pickerscrlllview = (PickerScrollViews)view. findViewById(R.id.pickerscrlllview);
//		bt_yes = (Button) view.findViewById(R.id.picker_yes);
         closeInputMethod();
         initdata();
         intview();
         Selecta();
         setOnClick();
//        
//       
		return view;
	}
	public int sunmit(int a ,int b){
		int sum=a+b;
		return sum;
	}
	private void Selecta(){
		et_add_address.setFocusable(false);
		final String[] myItems = getResources().getStringArray(
				R.array.zhong);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, myItems);
		adapter.setDropDownViewResource(R.layout.drop_down_item); 
		et_weightpner.setAdapter(adapter);  
		et_weightpner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				kes=position;
//				Log.e("11111", "position"+sunmit(5, position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		// 时间选择器
		pvTime = new TimePickerView(getActivity(), TimePickerView.Type.MONTH_DAY_HOUR_MIN);
		// 控制时间范围
//		 Calendar calendar = Calendar.getInstance();
//		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//		 calendar.get(Calendar.YEAR));
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);
		// 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				   Date now = new Date(); 
				   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//日期格式
				   String hehe = dateFormat.format(now); 
				   SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");     
				   Date begin = null;
				   Date end = null ;
				try {
					begin = dfs.parse(hehe);
			        end = dfs.parse(getTime(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
//				Log.e("111shijian", ""+getTime(date));
				  long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒   
				  if (between>1800) {
					  et_time.setText(getTime(date));
				     }else {
					ToastUtil.shortToast(getActivity(), "请选择30分钟以后的时间！");
				}
				
			}
		});
	}
	private void closeInputMethod() { 
		InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		 
		    boolean isOpen = imm.isActive();     
		if (isOpen) { 
		        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示        
		     imm.hideSoftInputFromWindow(view.getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS); 
		    } 
		}
	private void initdata(){
//		  et_name.clearFocus();
		 if (isLogin()) {
			 Advert();
			 Advertse();
			 getrequstBalance();
			 try {
				   latitude=Double.valueOf( PreferencesUtils.getString(getActivity(), PreferenceConstants.LATITUDE)).doubleValue();
					longitude=Double.valueOf( PreferencesUtils.getString(getActivity(), PreferenceConstants.LONGITUDE)).doubleValue();
			} catch (Exception e) {
				// TODO: handle exception
			}
						
			 }
		client = new LocationClient(getActivity());
		setLocationParams();
		client.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					ToastUtil.shortToast(getActivity(), "定位失败，请检查定位设置");
					return;
				} else {
					city = arg0.getCity();
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
//					Log.e("jpppp", latitude + ":::::::::" + longitude);
					address = arg0.getAddrStr();
					townaddress2=arg0.getDistrict();
					if (isLogin()) {
							getCityCode2(arg0.getCity());
					}
						if (frist) {
							et_address.setText(return_address);
						} else {
							et_address.setText(address);
						}
//					}
				}
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
	}
@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if (isLogin()) {
//		 getrequstBalance();
		 }else {
			 et_name.setText("");
			 et_tel.setText("");
		}
	
}
/**
 * 获取钱
 */
private void getrequstBalance() {
	RequestParams params = new RequestParams();
	AsyncHttpUtils.doGet(
			UrlMap.getUrl(MCUrl.BALANCE, "id",
					String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
			null, null, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					Logger.e("json", "" + new String(arg2));
					
					RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
					if (bean.getErrCode()==0) {
//			       ToastUtil.shortToast(getActivity(), "UID"+bean.data.get(0).userId);
					String usertype = bean.data.get(0).userType;
					PreferencesUtils.putString(getActivity(), PreferenceConstants.USERTYPE,
							bean.data.get(0).userType);
					PreferencesUtils.putString(getActivity(), PreferenceConstants.REALMANAUTH,
							bean.data.get(0).realManAuth);
					PreferencesUtils.putInt(getActivity(), PreferenceConstants.UID,
							bean.data.get(0).userId);
					 PreferencesUtils.putString(getActivity(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
					 PreferencesUtils.putString(getActivity(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
//					handler.sendEmptyMessage(0);
					 if (namesb) {
						 et_name.setText(namese);
						 et_tel.setText(phonese);
					}else {
						 et_name.setText(bean.data.get(0).userName);
						 et_tel.setText(bean.data.get(0).mobile);
					}
					
					 latitude2=bean.data.get(0).latitude;
					 longitude2=bean.data.get(0).longitude;
					
				}
					
				}

			});
}
	@OnClick({R.id.et_time,R.id.hsuccess, R.id.img_headportrait, R.id.et_address, R.id.rl_bannerTip ,R.id.iv_senderList, R.id.tv_modifySenderAddress, R.id.iv_receiverList,
		R.id.tv_modifyRecieverAddress,R.id.checkbox_baoss,})
	public void MyOnClick(View view) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.et_time:// 获取日期
			pvTime.show();
			break;
		case R.id.rl_bannerTip://广告点击去镖师认证
//			startActivity(new Intent(getActivity(), H5ModelActivity.class));
			break;
		case R.id.hsuccess:
			
			addPostResult();
//			ceshi();
			
			break;
		case R.id.img_headportrait:// 添加物品照片
			showPopwindow();
			break;
//		case R.id.iv_selectSender:// 从寄件人列表中选择寄件人
//			startActivityForResult(new Intent(PostLimitedDownwindTaskActivity.this, SendPersonActivity.class), 5);
//			break;
//		case R.id.iv_selectReciever:// 从收件人列表中选择收件人
//			startActivityForResult(new Intent(PostLimitedDownwindTaskActivity.this, AddReceiverActivity.class), 6);
//			break;
		case R.id.et_address:// 获取寄件人定位的地点，或者修改
			intent = new Intent(getActivity(), AddressActivity.class);
//			startActivityForResult(intent, 7);
			break;
		case R.id.et_add_address:// 获取收件人定位的地点，或者修改
			intent = new Intent(getActivity(), AddressActivity.class);
//			startActivityForResult(intent, 8);
			break;
		case R.id.tv_modifySenderAddress:// 获取寄件人定位的地点，或者修改
			Logger.e("et_address-02", et_address+"-----");
//			intent = new Intent(getActivity(), AddressActivity.class);
			intent = new Intent(getActivity(), LocationDemo.class);
//			bundle.putString("address", address);
			intent.putExtras(bundle);
		HomSubFragment1.this.startActivityForResult(intent, 7);
			break;
		case R.id.tv_modifyRecieverAddress:// 获取收件人定位的地点，或者修改
//			intent = new Intent(getActivity(), AddressReceiveActivity.class);
			intent = new Intent(getActivity(), LocationDemo.class);
			intent.putExtras(bundle);
			HomSubFragment1.this.startActivityForResult(intent, 8);
			break;
		case R.id.iv_senderList:// 从手机通讯录里选取寄件人
//			intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			HomSubFragment1.this.	startActivityForResult(intent, 9);
			Intent intent2 = new Intent(Intent.ACTION_PICK);
			intent2.setType(Phone.CONTENT_TYPE);
			startActivityForResult(intent2, 9);
			break;
		case R.id.iv_receiverList:// 从手机通讯录里选取收件人
//			intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			HomSubFragment1.this.	startActivityForResult(intent, 10);
			Intent intent3 = new Intent(Intent.ACTION_PICK);
			intent3.setType(Phone.CONTENT_TYPE);
			startActivityForResult(intent3, 10);
			break;
		default:
			break;
		}
	}
//	private void ceshi(){
//		Intent intent = new Intent(getActivity(), DownWindPayActivity.class);
////		intent.putExtra("money", String.valueOf(df.format(bean.data.get(0).transferMoney)));
//		intent.putExtra("money", "11");
//		intent.putExtra("insureCost", "0.0");
//		intent.putExtra("billCode","33");
//		intent.putExtra("baofei", "");
//		intent.putExtra("distance", "1");
//		intent.putExtra("daishou","22");
//		intent.putExtra("tyy","限时镖");
//		startActivityForResult(intent, 11);
//	}
	private void intview(){
		
		hnext_step.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isLogin()) {
				hlast_step_layout.setVisibility(View.GONE);
				next_layout.setVisibility(View.VISIBLE);
				end_layout.setVisibility(View.GONE);
				
				hlast_step.setVisibility(View.GONE);
				hlast_steps.setVisibility(View.VISIBLE);
				hnext_step.setVisibility(View.GONE);
				hnext_steps.setVisibility(View.VISIBLE);
				hsuccess.setVisibility(View.GONE);
				}else {
					ToastUtil.shortToast(getActivity(), "请登录后发布");
				}
//					ToastUtil.shortToast(getActivity(), "请登入后发布"+PreferencesUtils.getString(getActivity(), PreferenceConstants.UID));
//				}
			}
		});
		hnext_steps.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				hlast_step_layout.setVisibility(View.GONE);
//				next_layout.setVisibility(View.GONE);
//				end_layout.setVisibility(View.VISIBLE);
//				
//				hlast_step.setVisibility(View.VISIBLE);
//				hlast_steps.setVisibility(View.GONE);
//				hnext_step.setVisibility(View.GONE);
//				hnext_steps.setVisibility(View.GONE);
//				hsuccess.setVisibility(View.VISIBLE);
				addPostResult();
			}
		});
		hlast_step.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				hlast_step_layout.setVisibility(View.GONE);
				next_layout.setVisibility(View.VISIBLE);
				end_layout.setVisibility(View.GONE);
				
				hlast_step.setVisibility(View.GONE);
				hlast_steps.setVisibility(View.VISIBLE);
				hnext_step.setVisibility(View.GONE);
				hnext_steps.setVisibility(View.VISIBLE);
				hsuccess.setVisibility(View.GONE);
			}
		});
		hlast_steps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				hlast_step_layout.setVisibility(View.VISIBLE);
				next_layout.setVisibility(View.GONE);
				end_layout.setVisibility(View.GONE);
				
				hlast_step.setVisibility(View.GONE);
				hlast_steps.setVisibility(View.GONE);
				hnext_step.setVisibility(View.VISIBLE);
				hnext_steps.setVisibility(View.GONE);
				hsuccess.setVisibility(View.GONE);
			}
		});
		edt_prices.setEnabled(false);
		edt_daipric.setEnabled(false);
         checkbox_baoss.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					chox=arg1;
					
					edt_prices.setEnabled(arg1);
//					edt_prices.setVisibility(View.VISIBLE);
//				   	v_pps.setVisibility(View.VISIBLE);
//					ToastUtil.shortToast(getActivity(), "12"+arg1);
					showPaywindow();
				}else {
					chox=arg1;
					edt_prices.setEnabled(arg1);
//					ToastUtil.shortToast(getActivity(), "13"+arg1);
//					edt_prices.setVisibility(View.GONE);
//				   	v_pps.setVisibility(View.GONE);
					edt_prices.setText("");
				}
				}
//			}
		});
 		
         checkbox_diashou.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			
 			@Override
 			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
 				// TODO Auto-generated method stub
 				if(arg1){
 					choxdai=arg1;
// 					edt_prices.setVisibility(View.VISIBLE);
// 				   	v_pps.setVisibility(View.VISIBLE);
 					edt_daipric.setEnabled(arg1);
 					showPaywindowdai();
// 					ToastUtil.shortToast(getActivity(), "21"+arg1);
 				}else {
 					choxdai=arg1;
// 					ToastUtil.shortToast(getActivity(), "23"+arg1);
 					edt_daipric.setEnabled(arg1);
 					edt_daipric.setText("");
// 					edt_prices.setVisibility(View.GONE);
// 				   	v_pps.setVisibility(View.GONE);
 				}
 				}
// 			}
 		});
         
//         hom1_togg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
// 			@Override
// 			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
// 				// TODO Auto-generated method stub
// 				if (isChecked) {  
// 					up=true;
//// 					che.setVisibility(View.VISIBLE);
// 				} else {  
//// 					che.setVisibility(View.GONE);
// 					up=false;
//                 }  
//// 				getFragmentManager()
//// 	   		   .beginTransaction()
////             .addToBackStack(null)  //将当前fragment加入到返回栈中
////             .replace(R.id.container, new MinSubFragment1())
////             .commit();
// 			}
// 		});
         toubaoxuzhi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("name", "1");
				intent.putExtra("url", "http://www.efamax.com/mobile/InsureSF.html");
				intent.setClass(getActivity(), HWdvertActivity.class);
				startActivity(intent);
			}
		});
//        checkbox_detai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if (arg1) {
//					choxde=arg1;
//					ll03.setVisibility(View.VISIBLE);
//					tex.setVisibility(View.VISIBLE);
//				}else {
//					choxde=arg1;
//					ll03.setVisibility(View.GONE);
//					tex.setVisibility(View.GONE);
//				}
//			}
//		});
      
         edt_daipric.clearFocus();
       
//         edt_daipric.addTextChangedListener(new TextWatcher() {
//        	 /** 
//        	     * 编辑框的内容发生改变之前的回调方法 
//        	     */
//        	 public void onTextChanged(CharSequence s, int start, int before, int count) {  
//                 if (edt_daipric.toString().equals("0") || s.toString().startsWith("0")  
//                     || edt_daipric.toString().equals("") || s.toString().trim().length() == 0) {  
//                	 edt_daipric.setText("1");  
//                 }  
//             }  
//        	 /** 
//        	     * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入 
//        	     * 我们可以在这里实时地 通过搜索匹配用户的输入 
//        	     */  
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			/** 
//		     * 编辑框的内容改变以后,用户没有继续输入时 的回调方法 
//		     */
//			@Override
//			public void afterTextChanged(Editable editable) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
         edt_prices.addTextChangedListener(new TextWatcher() {
        	 /** 
     	     * 编辑框的内容发生改变之前的回调方法 
     	     */
     	 public void onTextChanged(CharSequence s, int start, int before, int count) {  
     		temp=s;
     		 
          }  
     	 /** 
     	     * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入 
     	     * 我们可以在这里实时地 通过搜索匹配用户的输入 
     	     */  
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int afte) {
				// TODO Auto-generated method stub
				
			}
			/** 
		     * 编辑框的内容改变以后,用户没有继续输入时 的回调方法 
		     */
			@Override
			public void afterTextChanged(Editable editable) {
				// TODO Auto-generated method stub
				editStart = edt_prices.getSelectionStart(); 
				   editEnd = edt_prices.getSelectionEnd(); 
				if (temp.length()>4) {
//					ToastUtil.shortToast(getActivity(), "保额不能超过5000");
					showwind();
					editable.delete(editStart-1, editEnd); 
					    int tempSelection = editStart; 
					    edt_prices.setText(editable); 
					    edt_prices.setSelection(tempSelection);
				}
			}
		});
//         xiaomian.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				cartype="smallMinibus";
//				xiaomian1.setBackgroundResource(R.drawable.cheoff);
//				zhongmian1.setBackgroundResource(R.drawable.cheon);
//				xiaohuo1.setBackgroundResource(R.drawable.cheon);
//				zhonghuo1.setBackgroundResource(R.drawable.cheon);
//			}
//		});
//         zhongmian.setOnClickListener(new OnClickListener() {
// 			
// 			@Override
// 			public void onClick(View arg0) {
// 				// TODO Auto-generated method stub
// 				cartype="middleMinibus";
// 				xiaomian1.setBackgroundResource(R.drawable.cheon);
//				zhongmian1.setBackgroundResource(R.drawable.cheoff);
//				xiaohuo1.setBackgroundResource(R.drawable.cheon);
//				zhonghuo1.setBackgroundResource(R.drawable.cheon);
// 			}
// 		});
//         xiaohuo.setOnClickListener(new OnClickListener() {
// 			
// 			@Override
// 			public void onClick(View arg0) {
// 				// TODO Auto-generated method stub
// 				cartype="smallTruck";
// 				xiaomian1.setBackgroundResource(R.drawable.cheon);
//				zhongmian1.setBackgroundResource(R.drawable.cheon);
//				xiaohuo1.setBackgroundResource(R.drawable.cheoff);
//				zhonghuo1.setBackgroundResource(R.drawable.cheon);
// 			}
// 		});
//         zhonghuo.setOnClickListener(new OnClickListener() {
// 			
// 			@Override
// 			public void onClick(View arg0) {
// 				// TODO Auto-generated method stub
// 				cartype="middleTruck";
// 				xiaomian1.setBackgroundResource(R.drawable.cheon);
//				zhongmian1.setBackgroundResource(R.drawable.cheon);
//				xiaohuo1.setBackgroundResource(R.drawable.cheon);
//				zhonghuo1.setBackgroundResource(R.drawable.cheoff);
// 			}
// 		});
	}
	 private CharSequence temp;
	 private int editStart ; 
	  private int editEnd ;
	public void setOnClick() {
		// TODO Auto-generated method stub
		AsyncHttpUtils.doSimGet(MCUrl.DowInsura, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Logger.e("111111", new String(arg2));
					adbean = new Gson().fromJson(new String(arg2), DowInsuraBean.class);
				
				
			}
		});
	}
	/**
	  * 得到根Fragment
	  * 
	  * @return
	  */
//	 private Fragment getRootFragment() {
//	  Fragment fragment = getParentFragment();
//	  while (fragment.getParentFragment() != null) {
//	   fragment = fragment.getParentFragment();
//	  }
//	  return fragment;
//
//	 }
	 /**
	  * 启动Activity
	  */
//	 private void onClickTextViewRemindAdvancetime() {
//	  Intent intent = new Intent();
//	  intent.setClass(getActivity(), AddressActivity.class);
////	  intent.putExtra("TAG","TEST"); 
//	  HomSubFragment1.this.startActivityForResult(intent, 1000);
//	 }
	 
	//选择时间
		public static String getTime(Date date) {
			SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
//			SimpleDateFormat formats = new SimpleDateFormat("yyyy");
			Date now = new Date(); 
			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//日期格式
			   String hehe = dateFormat.format(now); 
			return hehe+"-"+format.format(date);
//			   return dateFormat.format(now);
		}
		/**
		 * 发布按钮提交数据
		 */
		private void addPostResult() {
			JSONObject obj = new JSONObject();
			Logger.e("111111et_address-03", "-----");
//			if (!flag) {
//				ToastUtil.shortToast(getActivity(), "请上传物品照片");
//				return;
//			}
			if (chox) {
				if (edt_prices.getText().toString().equals("")) {
					ToastUtil.shortToast(getActivity(), "请填写投保费用");
					return;
				}
				
			}
			if (choxdai) {
				if (edt_daipric.getText().toString().equals("")) {
					ToastUtil.shortToast(getActivity(), "请填写代收费用");
					return;
				}
				
			}
			String string =et_tel.getText().toString();
			String string2 =et_add_tel.getText().toString();
			  String tmpstr=string.replace(" ","");
			  String tmpstr2=string2.replace(" ","");
			  if (!StringUtil.isMobileNO(tmpstr)
						|| (tmpstr.length() != 11)
								&& !StringUtil.isMobileNO(tmpstr2)
						|| (tmpstr2.length() != 11)) {
				ToastUtil.shortToast(getActivity(), "请输入正确的手机号码");
				return;
			}
			if (et_add_name.getText().toString().equals("") || et_add_tel.getText().toString().equals("")
					|| et_name.getText().toString().equals("") || et_tel.getText().toString().equals("")
					|| et_goodname.getText().toString().equals("") ||et_time.getText().toString().equals("")
					) {//|| et_weight.getText().toString().equals("")
				ToastUtil.shortToast(getActivity(), "请完善信息");
				return;
			}
			if (choxde) {
				if ( et_chang.getText().toString().equals("") || et_kuan.getText().toString().equals("")
						|| et_height.getText().toString().equals("")) {
					ToastUtil.shortToast(getActivity(), "请完善信息");
					return;
				}
				
			}else {
				et_chang.setText("0");
				et_kuan.setText("0");
				et_height.setText("0");
			}
			
			
			Logger.e("et_add_address", et_add_address.getText().toString()+"-----");
			Logger.e("et_address", et_address.getText().toString()+"-----");
			Logger.e("city", city+"-----");
			Logger.e("cityCode", cityCode+"-----");
			Logger.e("receiver_citycode", receiver_citycode+"-----");
			if (et_address.getText().toString().equals("") ) {
				ToastUtil.shortToast(getActivity(), "请从新输入的发件地址信息");
				return;
			}
			if ( et_add_address.getText().toString().equals("")|| receiver_citycode.equals("")) {
				ToastUtil.shortToast(getActivity(), "请从新输入的收件地址信息");
				return;
			}
//			if ( et_add_address.getText().toString().equals("")|| receiver_citycode.equals("")) {
//				ToastUtil.shortToast(getActivity(), "请从新输入的收件地址信息");
//				return;
//			}
			try {
				obj.put("userId",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
			
				obj.put("latitude", latitude);
				obj.put("longitude", longitude);
//				obj.put("cityCode", cityCode);
				if (dingwei) {
					obj.put("cityCode", cityCode2);
					obj.put("townCode", townCode2);
					obj.put("fromLatitude", myLatitude);
					obj.put("fromLongitude", myLongitude);
				}else {
					if (latitude2==0.0) {
						obj.put("fromLatitude", latitude);
					}else {
						obj.put("fromLatitude", latitude2);
					}
					if (longitude2==0.0) {
						obj.put("fromLongitude", longitude);
					}else {
						obj.put("fromLongitude", longitude2);
					}
					obj.put("cityCode", cityCode);
					obj.put("townCode", townCode);
					
					
				}
				
				obj.put("personName", et_name.getText().toString());
//				obj.put("mobile", et_tel.getText().toString());
				obj.put("mobile", tmpstr);
				
				obj.put("address", et_address.getText().toString()+et_address_specific.getText().toString());
				obj.put("personNameTo", et_add_name.getText().toString());
//				obj.put("mobileTo", et_add_tel.getText().toString());
				obj.put("mobileTo", tmpstr2);
				obj.put("addressTo", et_add_address.getText().toString()+et_add_address_specific.getText().toString());
//				obj.put("addressTo", et_address.getText().toString());
				obj.put("cityCodeTo", receiver_citycode);
				obj.put("toLatitude", receiverLatitude);
				obj.put("toLongitude", receiverLongitude);
//				obj.put("cityCodeTo", cityCode);
//				obj.put("toLatitude", myLatitude);
//				obj.put("toLongitude", myLongitude);
				obj.put("matName", et_goodname.getText().toString());
				obj.put("matWeight", sunmit(5, kes));
				obj.put("matImageUrl", icon);
				obj.put("matRemark", et_good_detail.getText().toString());
				obj.put("high", "0");
				obj.put("wide", "0");
//				if (up) {
//					obj.put("carType", cartype);
//					obj.put("length", "135");
//				}else {
					obj.put("carType", "else");
					obj.put("length", "10");
//				}
				
				obj.put("limitTime", et_time.getText().toString());
				if(chox){
					obj.put("whether", "Y");
					obj.put("premium", edt_prices.getText().toString().trim());
				}else {
					obj.put("whether", "N");
					obj.put("premium", "");
				}
				if (choxdai) {
					obj.put("ifReplaceMoney", choxdai);
					obj.put("replaceMoney", edt_daipric.getText().toString().trim());
				}else {
					obj.put("ifReplaceMoney", choxdai);
					obj.put("replaceMoney", "");
				}
				obj.put("publshDeviceId", DataTools.getDeviceId(getActivity()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dialog.show();
			Logger.e("查看数据", obj.toString());
			AsyncHttpUtils.doPostJson(getActivity(), MCUrl.DOWNWINDTASKPUBLISHS, obj.toString(),
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Logger.e("oppo", new String(arg2));
							dialog.dismiss();
							 bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
							Logger.e("oppop", bean.data.toString());
							if (bean.getErrCode() == 0) {
								DecimalFormat df = new DecimalFormat("######0.00");

									ToastUtil.shortToast(getActivity(), bean.getMessage());
									Intent intent = new Intent(getActivity(), DownWindPayActivity.class);
//									intent.putExtra("money", String.valueOf(df.format(bean.data.get(0).transferMoney)));
									intent.putExtra("money", bean.data.get(0).transferMoney);
									intent.putExtra("insureCost", bean.data.get(0).insureCost);
									intent.putExtra("billCode", bean.data.get(0).billCode);
									intent.putExtra("distance", bean.data.get(0).distance);
									intent.putExtra("baofei", edt_prices.getText().toString());
									intent.putExtra("daishou", edt_daipric.getText().toString());
									intent.putExtra("tyy","限时镖");
									Logger.e("billCode数据",""+ bean.data.get(0).insureCost);
									startActivityForResult(intent, 11);
//								}

							}else {
								ToastUtil.shortToast(getActivity(), bean.getMessage());
							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							dialog.dismiss();
						}
					});

		}
		/**
		 * 
		 */
		private void getrequst(int   resid) {
			RequestParams params = new RequestParams();
			AsyncHttpUtils.doGet(
					UrlMap.getUrl(MCUrl.afterPublish, "recId",
							""+resid),
					null, null, params, new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						}
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							Log.e("11111json", "" + new String(arg2));
							BaseBean beans = new Gson().fromJson(new String(arg2), BaseBean.class);
							if (beans.getErrCode()==-2) {
								ToastUtil.longToast(getActivity(), beans.getMessage());
							}
						}

					});
		}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Logger.e("11111s", "requestCode"+requestCode+"resultCode"+resultCode+"data"+data);
		switch (requestCode) {
		case 0:// 增加手机寄件人联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol =  getActivity().getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor =  getActivity().managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				namesb=true;
				if (cursor.moveToFirst()) {
					
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_name.setText(name);
				namese=name;
//				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//				 if (phone==null) {
//						return;
//					   }
//				while (phone.moveToNext()) {
//					String phone_number = phone
//							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//					if (!phone_number.equals("")){
//						et_tel.setText(phone_number);
//						phonese=phone_number;
//					}
//				}
				 String[] contact = new String[2];
	                // 查看联系人有多少个号码，如果没有号码，返回0
					int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String[] strs = cursor.getColumnNames();
		            for (int i = 0; i < strs.length; i++) {
		                if (strs[i].equals("data1")) {
		                    ///手机号
		                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
		                    et_tel.setText(""+contact[1]);
		                    phonese=contact[1];
		                }
		            }
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
				}else{  
					ToastUtil.shortToast(getActivity(), "需要开启读取联系人权限");
                    // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
//                    new AlertDialog.Builder( getActivity())  
//                    .setMessage("app需要开启读取联系人权限")  
//                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
//                        @Override  
//                        public void onClick(DialogInterface dialogInterface, int i) {  
//                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
//                            intent.setData(Uri.parse("package:" +   getActivity(). getPackageName()));  
//                            startActivity(intent);  
//                        }  
//                    })  
//                    .setNegativeButton("取消", null)  
//                    .create()  
//                    .show();  
                      
                }   
			}

			break;
		// 如果是直接从相册获取
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/head.png");
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
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
					map.put("fileName", "顺风物品");
					map_file.put("file", new File(fileName));
//					Message msg = new Message(); 
//		               msg.what = 1; 
//					 handler.sendMessage(msg); 
					   img_headportrait.setBackgroundDrawable(null);// 切换图片前先置空
						img_headportrait.setImageBitmap(imghead);
						imageViewHander.start();
//					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				}
			}
			break;
			
//		case 5:// 从寄件人列表中选择寄件人
//			if (resultCode == Activity.RESULT_OK) {
//				// receiver_city = data.getStringExtra("city");
//				myLatitude = data.getDoubleExtra("latitude", 0);
//				myLongitude = data.getDoubleExtra("longitude", 0);
//				return_address = data.getStringExtra("location").replace("中国", "");
//				et_address.setText(return_address);
//				detailLocationOfSender = data.getStringExtra("address");
//				et_name.setText(data.getStringExtra("name"));
//				et_tel.setText(data.getStringExtra("phone").replaceAll(" ",""));// 选择，并去掉手机号码中的空格；
//				getCityCode(true, data.getStringExtra("city"));
////				city = data.getStringExtra("city");
//				frist = true;
//			}
//
//			break;
//		case 6:// 从收件人列表中选择收件人
//			if (resultCode == Activity.RESULT_OK) {
//				receiverLatitude = data.getDoubleExtra("latitude", 0.0);
//				receiverLongitude = data.getDoubleExtra("longitude", 0.0);
//				et_add_name.setText(data.getStringExtra("name"));
//				et_add_tel.setText(data.getStringExtra("phone"));
//				et_add_address.setText(data.getStringExtra("location"));
//				detailLocationOfReceiver = data.getStringExtra("address");
//				receiver_citycode = data.getStringExtra("citycode");
//				ToastUtil.longToast(getApplicationContext(), receiverLatitude+"/////"+receiverLongitude);
//			}
//			break;
		case 7:// 选择/修改寄件人的寄件地址
			if (resultCode == RESULT_OK) {
				if(data!=null){
//				Log.e("111111sss", data.getStringExtra("address").replace("中国", "")+"-----");
				myLatitude = data.getDoubleExtra("latitude", 0);
				myLongitude = data.getDoubleExtra("longitude", 0);
//				ToastUtil.shortToast(getActivity(), "city"+ data.getStringExtra("city")+"+"+ data.getDoubleExtra("latitude", 0)+"+"+ data.getStringExtra("townaddressd"));
//				city = data.getStringExtra("city");
//				getCityCode(true, city);
//				et_address.setText(data.getStringExtra("address").replace("中国", ""));
//				Log.e("et_address-03", et_address+"-----");
			     	return_address = data.getStringExtra("address").replace("中国", "");
			     	et_address.setText(return_address);
					city = data.getStringExtra("city");
					townaddressd= data.getStringExtra("townaddressd");
//					Log.e("11111district", data.getStringExtra("district"));
					getCityCode(true, city,false);
//					getCityCode(true, data.getStringExtra("district"));
					frist = true;
					return;
				}
			}
			Log.e("et_address-04", et_address+"-----");
			break;
		case 8:// 修改收件人的收货地址
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
			
				receiverLatitude = data.getDoubleExtra("latitude", 0);
				receiverLongitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
//				Log.e("1111122address", data.getStringExtra("address"));
				receive = true;
				getCityCode(false, city,true);
				et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
			}
			break;
		case 9:// 从本地通讯录选取寄件人
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol =getActivity().getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
				
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_name.setText(name);
				
//				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//				while (phone.moveToNext()) {
//					String phone_number = phone
//							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
//					if (!phone_number.equals("")){
//						et_tel.setText(phone_number);
//					}
//				
//				}
				 String[] contact = new String[2];
	                // 查看联系人有多少个号码，如果没有号码，返回0
					int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String[] strs = cursor.getColumnNames();
		            for (int i = 0; i < strs.length; i++) {
		                if (strs[i].equals("data1")) {
		                    ///手机号
		                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
		                    et_tel.setText(""+contact[1]);
		                	phonese=contact[1];
		                }
		            }
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
				}else{  
                    // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
                    new AlertDialog.Builder(getActivity())  
                    .setMessage("app需要开启读取联系人权限")  
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
                        @Override  
                        public void onClick(DialogInterface dialogInterface, int i) {  
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));  
                            startActivity(intent);  
                        }  
                    })  
                    .setNegativeButton("取消", null)  
                    .create()  
                    .show();  
                      
                }   

			}

			break;
		case 10:// 从本地通讯录选取收件人
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getActivity().getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
				 if (cursor.moveToFirst()) {
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_add_name.setText(name);
				
//				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//				while (phone.moveToNext()) {
//					String phone_number = phone
//							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
//					if (!phone_number.equals("")){
//						et_add_tel.setText(phone_number);
//					}
//				}
				 String[] contact = new String[2];
	                // 查看联系人有多少个号码，如果没有号码，返回0
					int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String[] strs = cursor.getColumnNames();
		            for (int i = 0; i < strs.length; i++) {
		                if (strs[i].equals("data1")) {
		                    ///手机号
		                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
		                    et_add_tel.setText(""+contact[1]);
		                }
		            }
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//			    	phone.close();
				 }else{  
	                    // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
	                    new AlertDialog.Builder(getActivity())  
	                    .setMessage("app需要开启读取联系人权限")  
	                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
	                        @Override  
	                        public void onClick(DialogInterface dialogInterface, int i) {  
	                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
	                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));  
	                            startActivity(intent);  
	                        }  
	                    })  
	                    .setNegativeButton("取消", null)  
	                    .create()  
	                    .show();  
	                      
	                }   
				
			}
			
			break;
		case 11:
			if (resultCode == RESULT_OK) {
				if (data!=null) {
					if (!data.getStringExtra("type").equals("1")) {
//						finish();	
						remo();
						getrequst(bean.data.get(0).recId);
					}
				}
				}
			
			break;
		}
	}
	private void remo(){
		hlast_step_layout.setVisibility(View.VISIBLE);
		next_layout.setVisibility(View.GONE);
		end_layout.setVisibility(View.GONE);
		
		hlast_step.setVisibility(View.GONE);
		hlast_steps.setVisibility(View.GONE);
		hnext_step.setVisibility(View.VISIBLE);
		hnext_steps.setVisibility(View.GONE);
		hsuccess.setVisibility(View.GONE);
		edt_prices.setText("");
//		setChecked
		checkbox_diashou.setChecked(false);
		checkbox_baoss.setChecked(false);
		cartype="else";
//		che.setVisibility(View.GONE);
		choxdai=false;
		chox=false;
		cartype="";
		img_headportrait.setBackgroundDrawable(null);// 切换图片前先置空
		img_headportrait.setBackgroundResource(R.drawable.camre_img);
		et_good_detail.setText("");
//		et_weight.setText("");
		et_goodname.setText("");
		et_add_address_specific.setText("");
		et_address_specific.setText("");
		et_add_address.setText("");
		et_add_tel.setText("");
		et_add_name.setText("");
		et_time.setText("");
		
	}
	/**
	 * 百度地图定位设置
	 */
	private void setLocationParams() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 0;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(false);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		client.setLocOption(option);
	}
	/**
	 * 获取城市代码
	 * @param flag
	 * @param city
	 */
	private void getCityCode(boolean flag, String city,boolean ding) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getContext(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}// area  area_name  取县的code
//		List<AreaBean> selectDataFromDb = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + city + "'");
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
//			ToastUtil.longToast(PostLimitedDownwindTaskActivity.this, "11111area_name  "+selectDataFromDb.get(0).area_code);
		
			if (flag) {
				if (ding) {
//					dingwei=false;
				 cityCode = selectDataFromDb.get(0).city_code;
			  	Logger.e("11111city_codee", selectDataFromDb.get(0).city_code);
//			  	ToastUtil.shortToast(getActivity(), "cityCode "+cityCode);
				List<AreaBean> selectDataFromDbs = new AreaDboperation()
						.selectDataFromDb("select * from area where area_name='" + townaddress2 + "'");
				if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
					townCode=selectDataFromDbs.get(0).area_code;
					Logger.e("11111ptownCodee", townCode);
//					ToastUtil.shortToast(getActivity(), "cityCode "+cityCode+"townCode"+townCode);
				}
				}
			} else {
//				ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code23s "+selectDataFromDb.get(0).area_code);
				receiver_citycode = selectDataFromDb.get(0).city_code;
				Logger.e("11111receiver_citycode", receiver_citycode);
			}
			if (flag) {
				if (!ding) {
					dingwei=true;
				   cityCode2 = selectDataFromDb.get(0).city_code;
				  	Logger.e("11111city_codee", selectDataFromDb.get(0).city_code);
					
					List<AreaBean> selectDataFromDbs = new AreaDboperation()
							.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
					if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
						townCode2=selectDataFromDbs.get(0).area_code;
						Logger.e("11111ptownCodee", townCode);
						}
		     	}
				}
		}
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
//		}
//		 if (!townaddressd.equals("")) {
//		List<AreaBean> selectDataFromDbs = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
//		if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
//			townCode=selectDataFromDbs.get(0).area_code;
//			Log.e("11111ptownCodee", townCode);
//		}
//		 }
		 
	}
	/**
	 * 获取城市代码
	 * @param flag
	 * @param city
	 */
	private void getCityCode2(String city) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getContext(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}// area  area_name  取县的code
//		List<AreaBean> selectDataFromDb = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + city + "'");
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
					
				 cityCode = selectDataFromDb.get(0).city_code;
			  	Logger.e("11111city_codee", selectDataFromDb.get(0).city_code);
				List<AreaBean> selectDataFromDbs = new AreaDboperation()
						.selectDataFromDb("select * from area where area_name='" + townaddress2 + "'");
				if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
					townCode=selectDataFromDbs.get(0).area_code;
					Logger.e("11111ptownCodee", townCode);
//					ToastUtil.shortToast(getActivity(), "cityCode "+cityCode+"townCode"+townCode);
//			
				}
		}
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
//		}
//		 if (!townaddressd.equals("")) {
//		List<AreaBean> selectDataFromDbs = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
//		if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
//			townCode=selectDataFromDbs.get(0).area_code;
//			Log.e("11111ptownCodee", townCode);
//		}
//		 }
		 
	}
	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
//		List<AreaBean> selectDataFromDb = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + city + "'");
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
	
		if (receive) {
			receiver_citycode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycode);
//			ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code "+selectDataFromDb.get(0).area_code);
		} else {
//			ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code "+selectDataFromDb.get(0).area_code);
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
		}
	}
	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater)getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		window.showAtLocation(getActivity().findViewById(R.id.img_headportrait), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), IdCardActivity.class).putExtra("iconpath", "goodpath"));
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.png")));
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
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	 class NetImageHandler implements Runnable {
	        @Override
	        public void run() {
	            try {
	                //发送消息，通知UI组件显示图片
	            	result =HomSubFragment1. post(MCUrl.FILEDOWNWIND, map, map_file);
	                handler.sendEmptyMessage(0);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    Handler handler = new Handler(){
	        @Override
	        public void handleMessage(Message msg) {
	            if(msg.what == 0){
	            	sendEmptyUiMessage(MsgConstants.MSG_01);
	            }
	        }
	    };
	
	
	
//	public void handleBackgroundMessage(Message msg) {
//		// TODO Auto-generated method stub
//		super.handleBackgroundMessage(msg);
//		switch (msg.what) {
//		case MsgConstants.MSG_01:
//			try {
////				result = post(MCUrl.FILEDOWNWIND, map, map_file);
//				sendEmptyUiMessage(MsgConstants.MSG_01);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			break;
//
//		default:
//			break;
//		}
//	}

	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			Log.e("result", result);
			IconBean bean = new Gson().fromJson(result, IconBean.class);
			if (bean.data.size() != 0) {
				flag = true;
				icon = bean.data.get(0).filePath;
				PreferencesUtils.putString(getActivity(), PreferenceConstants.GOODPATH, icon);
				setPicToView(imghead);
				img_headportrait.setBackgroundDrawable(null);// 切换图片前先置空
				img_headportrait.setImageBitmap(imghead);
			}
			break;

		default:
			break;
		}
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
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager inputMethodManager = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return super.getActivity().onTouchEvent(event);
	}
	
	
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindow() {
		
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_baojia, null);
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
		window02.showAtLocation(HomSubFragment1.this.view.findViewById(R.id.checkbox_baoss), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
		tet_tishi=(TextView) view.findViewById(R.id.tet_tishi);
		tet_tishi.setText(adbean.getMessage());
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
	 * 显示投保提示信息
	 */

	/**
	 * 显示投保提示信息
	 */
	private void showPaywindowdai() {
		
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_daishou, null);
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
		window02.showAtLocation(HomSubFragment1.this.view.findViewById(R.id.checkbox_baoss), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
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
	 * 提示信息
	 */
	private void showwind() {
		
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_toubao, null);
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
		window02.showAtLocation(HomSubFragment1.this.view.findViewById(R.id.checkbox_baoss), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_panb);
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
	public void Advert(){
		AsyncHttpUtils.doSimGet(MCUrl.getAdvert, new AsyncHttpResponseHandler() {//getAdvert Advertise
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				Log.e("111111se", new String(arg2));
				adbeans = new Gson().fromJson(new String(arg2), AdvertBean.class);
//				String yString=adbeans.getData().get(0).advertiseName;
//				if (yString.length()>0) {
//					String string2=yString.substring(yString.length()-1,yString.length());
//					if (string2.equals("Y")) {
//						handler.sendEmptyMessage(4);
						showwindow();
//					}
//				}
				
				
			}
		});
	}
	/**
	 * 登录次数
	 */
	public void Advertse(){//MCUrl.LoginCoun
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LoginCoun, "userId", String
				.valueOf(PreferencesUtils.getInt(getActivity(),
						PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("111111se", new String(arg2));
				Loginuberbean lonmber = new Gson().fromJson(new String(arg2), Loginuberbean.class);
				if(lonmber.data.size()>0 || lonmber.data!=null){
				if (lonmber.getData().get(0).jumpPage.equals(""+1)) {
//					ToastUtil.shortToast(getActivity(), "jumpPage"+lonmber.getData().get(0).jumpPage);
					Intent intents = new Intent(getActivity(),
							LogiNumberActivity.class);
					       startActivity(intents);
				}
				
				}
			}
		});
	}
	/**
	 * 现金卷弹窗
	 */
	
	public void showwindow() {
		final TextView btnsaves_pan;
		TextView pay_ext;
		ImageView adv_bg;
		ViewPager iewPager;
		  configImageLoader();
	 	
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.mainpopwindow_logsitise, null);// mainpopwindow_logsiti  mainpopwindow_logsitise
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		//放置位置
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
		window02.showAtLocation(HomSubFragment1.this.view.findViewById(R.id.edt_daipric), Gravity.CENTER, 0, 0);
		adv_bg=(ImageView) view.findViewById(R.id.adv_bg);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_panse);
//		iewPager=(ViewPager) view.findViewById(R.id.viewPager);
		
//		cycleViewPager = (CycleViewPager)  findViewById(R.id.fragment_cycle);
		
		mAdView =  (ImageCycleView) view.findViewById(R.id.ad_view);
//		mAdView.startImageCycle();
		pay_ext=(TextView) view.findViewById(R.id.pay_ext);
		adv_bg.setVisibility(View.GONE);
		initialize();
//		relativeLayout1.setim
//		Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
//		if (!adbeans.getData().get(0).advertiseImageUrl.equals("")) {
//			String string="http://images.csdn.net/20130609/zhuanti.jpg";
//			new NormalLoadPictrue().getPicture(adbeans.getData().get(0).advertiseImageUrl,adv_bg);
////			new MyBitmapUtils().display(adv_bg,adbean.getData().get(0).advertiseImageUrl);
//		}else {
//			adv_bg.setBackgroundResource(R.drawable.dandandans);
//		}
//		
		adv_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(),
//						HAdvertActivity.class);
//				intent.putExtra("url", adbeans.getData().get(0).advertiseHtmlUrl);
//				Log.e("111url", ""+adbeans.getData().get(0).advertiseHtmlUrl);
//				if(!adbeans.getData().get(0).advertiseHtmlUrl.equals("")){
//					startActivity(intent);
//					window02.dismiss();	
//				}else {
					btnsaves_pan.setText("立即前往");
					Intent intents = new Intent(getActivity(),
							NewExerciseActivity.class);
					startActivity(intents);
					window02.dismiss();	
//				}
				
							
			}
		});
		pay_ext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
				mAdView.pushImageCycle();
			}
		});
		
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				window02.dismiss();
				mAdView.pushImageCycle();
			}
		});

	}
	
	@SuppressLint("NewApi")
	private void initialize() {
		
//		cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle);
		
		for(int i = 0; i < adbeans.getData().size(); i ++){
//		for(int i = 0; i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(adbeans.getData().get(i).pointName);
			info.setContent("图片-->" + i );
			infos.add(info);
		}
//		mAdView.setImageResources(infos, mAdCycleViewListener);
		mAdView.setImageResources(infos, mAdCycleViewListener);
		
//		// 将最后一个ImageView添加进来
//		views.add(ViewFactory.getImageView(getActivity(), infos.get(infos.size() - 1).getUrl()));
//		for (int i = 0; i < infos.size(); i++) {
//			views.add(ViewFactory.getImageView(getActivity(), infos.get(i).getUrl()));
//		}
//		// 将第一个ImageView添加进来
//		views.add(ViewFactory.getImageView(getActivity(), infos.get(0).getUrl()));
//		
//		// 设置循环，在调用setData方法前调用
//		cycleViewPager.setCycle(true);
//		// 在加载数据前设置是否循环
//		cycleViewPager.setData(views, infos, mAdCycleViewListener);
//		//设置轮播
//		cycleViewPager.setWheel(true);
//	    // 设置轮播时间，默认5000ms
//		cycleViewPager.setTime(2000);
//		//设置圆点指示图标组居中显示，默认靠右
//		cycleViewPager.setIndicatorCenter();
	}
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
//			Toast.makeText(getActivity(), "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
//			btnsaves_pan.setText("立即前往");
			Intent intents = new Intent(getActivity(),
					NewExerciseActivity.class);
			startActivity(intents);
			window02.dismiss();	
			mAdView.pushImageCycle();
		}
		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
		}
	};
//	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
//
//		@Override
//		public void onImageClick(ADInfo info, int position, View imageView) {
//			if (cycleViewPager.isCycle()) {
//				position = position - 1;
////				Toast.makeText(getActivity(),
////						"position-->" + info.getContent(), Toast.LENGTH_SHORT)
////						.show();
//			}
//			
//		}
//
//	};
//	
	/**
	 * 配置ImageLoder
	 */
	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.huandeng_wu700) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.huandeng_shun700) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.huandeng_kuai700) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);		
	}


}
