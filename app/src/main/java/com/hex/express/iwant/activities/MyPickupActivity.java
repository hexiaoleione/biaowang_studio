package com.hex.express.iwant.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.sharesdk.framework.statistics.NewAppReceiver;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.MyPickupAdapter;
import com.hex.express.iwant.adapters.ReceiptCourierAdapter;
import com.hex.express.iwant.bean.MypickMoneyBean;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DateUtil;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TimePickerView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 我的接单
 * 
 * @author SCHT-50
 * 
 */

public class MyPickupActivity extends BaseActivity {
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.ptrlv_card)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.text_title)
	TextView text_title;
	@Bind(R.id.text_time)
	TextView text_time;
	@Bind(R.id.text_todaytime)
	TextView text_todaytime;
	@Bind(R.id.text_mouthnum)
	TextView text_mouthnum;
	@Bind(R.id.text_mouthmoney)
	TextView text_mouthmoney;

	@Bind(R.id.text_daynum)
	TextView text_daynum;
	@Bind(R.id.text_daymoney)
	TextView text_daymoney;

	@Bind(R.id.text_totalnum)
	TextView text_totalnum;
	@Bind(R.id.text_totalmoney)
	TextView text_totalmoney;
	protected int pageSize = 20;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private PublicCourierBean bean;
	private List<PublicCourierBean.Data> mList;
	private MypickMoneyBean beanmoney;
	private List<MypickMoneyBean.Data> list;
	MyPickupAdapter adapter;
	private ListView listView;
	TimePickerView pvTime;
	private String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypickup);
		ButterKnife.bind(MyPickupActivity.this);
		initData();
		setOnClick();
	}

	@Override
	public void initData() {
		text_title.setText("我的接单");
		text_todaytime.setText(new SimpleDateFormat("yyyy-MM-dd")
		.format(new Date()));
		listView = ptrlv_card.getRefreshableView();
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
		// Calendar calendar = Calendar.getInstance();
		// pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
		// calendar.get(Calendar.YEAR));
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);
		// 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				text_time.setText(getTime(date));
				text_todaytime.setText(getTime(date));
			}
		});
		dialog.show();
		getHttpRequst(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		getHttpRequstMoney(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		text_time
				.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		text_time.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				dialog.show();
				getHttpRequst(s.toString());
				getHttpRequstMoney(s.toString());

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
//		delete();
	}
	/**
	 * 删除
	 */
	public void delete() {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				PublicCourierBean.Data data = mList.get(arg2 - 1);
				final String billCode = data.billCode;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyPickupActivity.this);
				builder.setItems(R.array.thread_menu, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e("json", MCUrl.EXPRESSDELETECOURIER + billCode);
						JSONObject obj = new JSONObject();
						try {
							obj.put("billCode", billCode);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AsyncHttpUtils.doPut(MyPickupActivity.this,
								MCUrl.EXPRESSDELETE ,
								obj.toString(), null,
								new AsyncHttpResponseHandler() {

									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
									}

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										Log.e("jjkdlsks", new String(arg2));
									getHttpRequst(text_time.getText().toString());
									}

								});
					}
				});
				builder.show();
				return true;
			}
		});
	}

	@OnClick({ R.id.check_time, R.id.yesterday_time, R.id.tomorrow_time,  R.id.btnLeft, R.id.img_instructions})
	public void MyOnClick(View view) {
		switch (view.getId()) {
		case R.id.check_time:
			pvTime.show();
			break;
		case R.id.btnLeft:
			finish();
			break;
		case R.id.img_instructions://补贴说明
			Intent intent=new Intent(MyPickupActivity.this,CourierExtentionActivity.class);
			startActivity(intent);
			break;
		case R.id.yesterday_time:
			date = text_time.getText().toString();
			Log.e("time1", date);
			date = new DateUtil().getDateStrDaysBefore(date, -1);
			text_time.setText(date);
			text_todaytime.setText(date);

			break;
		case R.id.tomorrow_time:
			date = text_time.getText().toString();
			if (date.equals(new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date()))) {
			} else {
				Log.e("time1", date);
				date = new DateUtil().getDateStrDaysBefore(date, 1);
				text_time.setText(date);
			}
			text_todaytime.setText(date);
			break;
		default:
			break;
		}

	}

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	@SuppressWarnings("unchecked")
	private void getHttpRequst(String date) {
		String url = UrlMap.getTwo(MCUrl.MYPICKBYDATE, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "date", date);
		Log.e("jsondsd", url);
		AsyncHttpUtils.doGet(url, null, null, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						mList = new ArrayList<PublicCourierBean.Data>();
						bean = new Gson().fromJson(new String(arg2),
								PublicCourierBean.class);
						mList = bean.data;
						if (bean.getErrCode() == 0) {
							ptrlv_card.setVisibility(View.VISIBLE);
							view_null_message.setVisibility(View.GONE);
							adapter = new MyPickupAdapter(
									MyPickupActivity.this, mList);
							listView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							ptrlv_card.onRefreshComplete();
						}
						if (bean.getErrCode() == -2) {
							ptrlv_card.setVisibility(View.GONE);
							view_null_message.setVisibility(View.VISIBLE);
						}

						Log.e("jsondEWWsd", new String(arg2));

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ptrlv_card.setVisibility(View.GONE);
						view_load_fail.setVisibility(View.VISIBLE);
					}
				});
	}

	private void getHttpRequstMoney(String date) {
		String url = UrlMap.getTwo(MCUrl.INCOMEEXPRESSDAYWEEKMONTH, "userId",
				String.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "date", date);
		Log.e("jsondsd", url);
		AsyncHttpUtils.doGet(url, null, null, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						beanmoney = new Gson().fromJson(new String(arg2),
								MypickMoneyBean.class);
						Log.e("jshhhhhhhhhhsd", new String(arg2));
						if (!beanmoney.data.equals("")
								&& beanmoney.data.size() > 0) {
							text_daymoney.setText(beanmoney.data.get(0).dayMoney);
							text_daynum.setText(beanmoney.data.get(0).dayMount);
							text_mouthmoney.setText(beanmoney.data.get(0).monthMoney);
							text_mouthnum.setText(beanmoney.data.get(0).monthMount);
							text_totalmoney.setText(beanmoney.data.get(0).totalMoney);
							text_totalnum.setText(beanmoney.data.get(0).totalMount);
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(MyPickupActivity.this, "网络加载失败");
					}
				});
	}

	@Override
	public void onWeightClick(View v) {

	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.PULL_FROM_START);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				text_time.setText(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()));
				text_todaytime.setText(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()));
				getHttpRequst(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()));				

			}
		});
		ptrlv_card.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				  adapter.setIndex(arg2-1);  
	                adapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

}
