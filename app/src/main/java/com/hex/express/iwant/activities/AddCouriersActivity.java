package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.SwipeListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.CourierAdapter;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.CourierBean;
import com.hex.express.iwant.bean.addcoucorBean;
import com.hex.express.iwant.bean.CourierBean.Data;
import com.hex.express.iwant.bean.NearbyCourierBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MyListView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 添加快递员
 * 
 * @author Eric
 * 
 */
public class AddCouriersActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.et_name)
	EditText et_name;
	@Bind(R.id.iv_courier)
	ImageView iv_courier;
	@Bind(R.id.et_phone)
	EditText et_phone;
	@Bind(R.id.et_com)
	EditText et_com;
	@Bind(R.id.iv_com)
	ImageView iv_com;
	@Bind(R.id.lv)
	MyListView lv;
	private String name;
	private String phone_number;
	private LoadingProgressDialog dialog;
	private CourierAdapter adapter;
	private LocationClient mLocationClient;
	private double latitude;
	private double longitude;
	private String cityCode;
	private List list = new ArrayList<NearbyCourierBean.Data>();
	private boolean isLoc;
	private int courierId;
	public ViewGroup vg;
	private String my_cityCode;
	private double my_latitude;
	private double my_longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcouriers);
		vg = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.activity_addcouriers, null);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		dialog = new LoadingProgressDialog(AddCouriersActivity.this);
		initData();
	}

	//绑定点击事件；
	@OnClick({ R.id.btn_add, R.id.iv_courier, R.id.iv_com })
	public void myClick(View view) {
		switch (view.getId()) {
		case R.id.btn_add:// 选择联系人
			String string =et_phone.getText().toString();
			  String tmpstr=string.replace(" ","");
			if (tmpstr.length() != 11
					|| !StringUtil.isMobileNO(tmpstr)) {
				ToastUtil.shortToast(getApplicationContext(), "请输入正确的电话号码");
			} else if (!et_com.getText().toString().equals("")
					&& !et_name.getText().toString().equals("")) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("courierId", courierId);
					obj.put("userId", PreferencesUtils.getInt(
							getApplicationContext(), PreferenceConstants.UID));
					obj.put("userName", PreferencesUtils.getString(
							getApplicationContext(),
							PreferenceConstants.USERNAME));
					obj.put("userMobile", PreferencesUtils
							.getString(getApplicationContext(),
									PreferenceConstants.MOBILE));
					obj.put("courierName", et_name.getText().toString());
					obj.put("courierMobile", tmpstr);
					obj.put("expId", expId);
					obj.put("expName", et_com.getText().toString());
					obj.put("latitude", my_latitude);
					obj.put("longitude", my_longitude);
					obj.put("cityCode", my_cityCode);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dialog != null) {
					dialog.show();
				}
				AsyncHttpUtils.doPostJson(getApplicationContext(),
						MCUrl.Add_COURIER, obj.toString(),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								Log.e("&&&&&&&&", new String(arg2));
								// TODO Auto-generated method stub
								dialog.dismiss();
								addcoucorBean bean = new Gson().fromJson(
										new String(arg2), addcoucorBean.class);
								if (bean.data==null||bean.data.size()<1) {
									ToastUtil.shortToast(getApplicationContext(),
											bean.getMessage());
								}else {

									courierId = bean.data.get(0).courierId;
									Intent intent = new Intent();
									intent.putExtra("name", et_name.getText()
											.toString());
									intent.putExtra("phone", et_phone.getText()
											.toString());
									intent.putExtra("courierId", courierId);
									intent.putExtra("expName", et_com.getText()
											.toString());
									setResult(RESULT_OK, intent);
									ToastUtil.shortToast(getApplicationContext(),
											"添加成功");
									finish();
								}
								
							
								Log.e("faliure", "ddddd");
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								Log.e("art", arg0 + "");
							}
						});

			}
			break;
		case R.id.iv_courier:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		/*
		 * case R.id.iv_com: break;
		 */

		default:
			break;
		}
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	

	//初始化；
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		my_latitude = getIntent().getDoubleExtra("latitude", 0);
		my_longitude = getIntent().getDoubleExtra("longitude", 0);
		// 添加快递员用定位
		latitude = getIntent().getDoubleExtra("latitude1", 0);
		longitude = getIntent().getDoubleExtra("longitude1", 0);
		my_cityCode = getIntent().getStringExtra("cityCode");
	
		dialog.show();
		tbv_show.setTitleText("快递员库");
		et_com.setFocusable(false);
		et_com.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(AddCouriersActivity.this,
						SelectExpCompanyActivity.class), 1);
			}
		});
		// getData();
		getNetData(false);
		lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<SwipeListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<SwipeListView> refreshView) {
				// TODO Auto-generated method stub
				// getData();
				getNetData(true);
			}
		});
	}

	//获取到城市代码；
	private String getCityCode(String city) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(
					iWantApplication.getInstance());
		}
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city
						+ "'");

		return selectDataFromDb.get(0).city_code;
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	private int expId;

	//回传值；
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1:
			if (arg1 == RESULT_OK && arg2 != null) {
				et_com.setText(arg2.getStringExtra("exp"));
				expId = arg2.getIntExtra("expId", 0);
			}

			break;
		case 0:
			if (arg1 == RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = arg2.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_name.setText(name);
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phone.moveToNext()) {
					phone_number = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals(""))
						et_phone.setText(phone_number);
				}

			}
			break;
		default:
			break;
		}
	}

	
	//请求网络数据（从 接口）
	CourierBean bean;

	public void getNetData(final boolean isRefresh) {
		Log.e("my_latitude", "" + my_latitude);
		Log.e("my_longitude", "" + my_longitude);
		AsyncHttpUtils.doSimGet(UrlMap.getfour(
				MCUrl.CORUIERS_LIST,
				"userId",
				PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID) + "", "latitude", my_latitude
						+ "", "longitude", my_longitude + "", "cityCode",
				my_cityCode), new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				Log.e("data", new String(arg2));
				bean = new Gson().fromJson(new String(arg2), CourierBean.class);
				if (bean.data == null || bean.data.size() == 0) {
					Log.e("list1", "lsit1");
					return;
				}
				Log.e("data", "edddd");
				list = bean.data;
				if (adapter == null) {
					adapter = new CourierAdapter(AddCouriersActivity.this, list);
					lv.setAdapter(adapter);
				}
				if (isRefresh == true && adapter != null) {
					adapter.setData(list);
					adapter.notifyDataSetChanged();
					lv.onRefreshComplete();
				}
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						CourierBean.Data data = (Data) list.get(position - 1);
						Intent intent = new Intent();
						intent.putExtra("name", data.courierName);
						intent.putExtra("phone", data.courierMobile);
						intent.putExtra("courierId", data.courierId);
						intent.putExtra("expName", data.expName);
						setResult(RESULT_OK, intent);
						// ToastUtil.shortToast(getApplicationContext(),
						// "添加成功");
						finish();
					}

				});
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
			}
		});

	}

	@Override
	public void getData() {
	}
}
