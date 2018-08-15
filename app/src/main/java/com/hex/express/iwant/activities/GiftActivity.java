package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.MainActivity.MyTask;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.GiftBean;
import com.hex.express.iwant.bean.MapPointBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.kankan.wheel.widget.OnWheelChangedListener;
import com.kankan.wheel.widget.OnWheelScrollListener;
import com.kankan.wheel.widget.WheelView;
import com.kankan.wheel.widget.adapters.AbstractWheelAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GiftActivity extends BaseActivity {
	// 摇奖页面的东西
	/**
	 * show the luckitems while you have 显示请求下来的幸运数字
	 */
	private TextView resulttv;
	private TextView resulttv2;
	private TextView resulttv3;
	private TextView resulttv4;
	private TextView resulttv5;
	private TextView resulttv6;
	private TextView resulttv7;
	// private TextView resulttv8;
	// 开始按钮及状态显示布局
	private Button mix;
	// 帮助按钮
	private TextView tv_help;
	// 每个轮子的转动次数
	private int temp1 = 0;
	private int temp2 = 0;
	private int temp3 = 0;
	private int temp4 = 0;
	private int temp5 = 0;
	private int temp6 = 0;
	private int temp7 = 0;
	// 获取的消息
	private String message1;
	// 保存数据的请求
	// private String url1 = ConstantUtil.BASE_URL + "saveDailyDraw.action?";
	// 记录旋转次数
	int srollSize = 1;
	private String[] ss;
	// 获取到的幸运数字
	private String ln1;
	private String ln2;
	private String ln3;
	private String ln4;
	private String ln5;
	private String ln6;
	private String ln7;
	// 抽奖次数
	// 存储获取到的数据的集合
	private ArrayList<String[]> strs = new ArrayList<String[]>();
	// 摇动字符串数组
	private String items[] = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
			"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65",
			"66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83",
			"84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99" };
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
//				Toast.makeText(GiftActivity.this, message1, Toast.LENGTH_SHORT).show();
				showPaywindow(message1);
				break;
			case 1:
//				Toast.makeText(GiftActivity.this, "您本次抽奖机会已用完!", Toast.LENGTH_SHORT).show();
				String string="您本次抽奖机会已用完!";
				showPaywindow(string);
				break;
			}
		};
	};
	
	boolean completed = true;
	// private ImageView btnReLocate1;
	// private LinearLayout llayout_trake;
	private int userId;
	@Bind(R.id.tv_total)
	TextView tv_total;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.fashion)
	TextView fashion;
	PopupWindow window02;


	// 帮助的网址
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift);
		ButterKnife.bind(this);
		dialog = new LoadingProgressDialog(this);
		dialog.show();
		initGiftData();
		initData();
		completed = PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.COMPLETE);
		/*
		 * if (PreferencesUtils.getBoolean(getApplicationContext(),
		 * PreferenceConstants.COMPLETE)!= nu) { completed = true; }
		 */
		userId = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID);
		initWheel(R.id.slot_1);
		initWheel(R.id.slot_2);
		initWheel(R.id.slot_3);
		initWheel(R.id.slot_4);
		initWheel(R.id.slot_5);
		initWheel(R.id.slot_6);
		initWheel(R.id.slot_7);
		// 初始化UI

	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_04:
			Log.e("vv", ss.toString());
			// 设置旋转次数
			temp1 = Integer.valueOf(ss[0]);
			temp2 = Integer.valueOf(ss[1]);
			temp3 = Integer.valueOf(ss[2]);
			temp4 = Integer.valueOf(ss[3]);
			temp5 = Integer.valueOf(ss[4]);
			temp6 = Integer.valueOf(ss[5]);
			temp7 = Integer.valueOf(ss[6]);
			mixWheel(R.id.slot_1, temp1);
			mixWheel(R.id.slot_2, temp2);
			mixWheel(R.id.slot_3, temp3);
			mixWheel(R.id.slot_4, temp4);
			mixWheel(R.id.slot_5, temp5);
			mixWheel(R.id.slot_6, temp6);
			mixWheel(R.id.slot_7, temp7);
			chance--;
			// 次数自减
			sendEmptyUiMessageDelayed(MsgConstants.MSG_01, 4000);
			Log.e("vv", ss.toString());
			break;
		case MsgConstants.MSG_01:
			initGiftData();
			ToastUtil.longToast(getApplicationContext(), mess);
			break;
		default:
			break;
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		tbv_show.setTitleText("");
		fashion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GiftActivity.this, FashionActivity.class));

			}
		});

	}

	private String mess;

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		mix.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH).equals("Y")&&!PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH).equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(GiftActivity.this);
					builder.setTitle("友情提示");
					builder.setMessage("请完善信息，只有实名用户可以参与抽奖,虚假姓名无法领奖！");

					builder.setPositiveButton("取消", null);
					builder.setNegativeButton("确定", new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							startActivity(new Intent(GiftActivity.this, RegisterSetImageAndNameActivity.class)
									.putExtra("tiaoguo", "2"));
						}
					});
					builder.show();
				} else {
					if (chance > 0) {
						JSONObject obj = new JSONObject();
						try {
							obj.put("drawNum", list.get(chance - 1));
							obj.put("userId",
									PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.GIFT_NUM, obj.toString(),
								new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										if (arg2 == null)
											return;
										BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
										if (bean.getErrCode() == 0) {
											// 重新获取次数
											// 每次点击都初始化轮子
											initWheel(R.id.slot_1);
											initWheel(R.id.slot_2);
											initWheel(R.id.slot_3);
											initWheel(R.id.slot_4);
											initWheel(R.id.slot_5);
											initWheel(R.id.slot_6);
											initWheel(R.id.slot_7);
											ss = strs.get(chance - 1);
											mess = bean.getMessage();
											sendEmptyUiMessageDelayed(MsgConstants.MSG_04, 500);
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
//						ToastUtil.shortToast(getApplicationContext(), "您今天的抽奖次数已用完");
						String string="您今天的抽奖次数已用完";
						showPaywindow(string);
					}
				}
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	private LoadingProgressDialog dialog;
	private List<String> list;

	private void initGiftData() {
		/*
		 * SharedPreferences sp = getApplicationContext().getSharedPreferences(
		 * "cofig", Context.MODE_PRIVATE);
		 */
		AsyncHttpUtils.doSimGet(
				UrlMap.getUrl(MCUrl.GIFT_MESS, "userId",
						PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + ""),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						if (arg2 == null) {
							ToastUtil.shortToast(mActivity, "网络请求错误");
						} else {
							GiftBean bean = new Gson().fromJson(new String(arg2), GiftBean.class);

							if (dialog != null)
								dialog.dismiss();

							if (bean.data == null || bean.data.size() == 0)
								return;
							String s = String.valueOf(bean.data.get(0).leftMoney);
							String money = s.substring(0, s.indexOf("."));
							tv_total.setText("奖池当前还剩余" + money + "元");
							List<String> list_num = new ArrayList<String>();
							for (int i = 0; i < bean.data.get(0).luckyNum.length(); i = i + 2) {
								list_num.add(bean.data.get(0).luckyNum.substring(i, i + 2));
							}
							String[] luck = list_num.toArray(new String[7]);
							ln1 = luck[0];
							ln2 = luck[1];
							ln3 = luck[2];
							ln4 = luck[3];
							ln5 = luck[4];
							ln6 = luck[5];
							ln7 = luck[6];
							if (bean.getErrCode() != 0) {
								ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							} else {
								chance = bean.data.get(0).drawList.size();
								list = bean.data.get(0).drawList;
								List<String[]> numbers = new ArrayList<String[]>();
								for (int i = 0; i < list.size(); i++) {
									List<String> list_num_lucky = new ArrayList<String>();
									for (int j = 0; j < list.get(i).length(); j = j + 2) {
										list_num_lucky.add(list.get(i).substring(j, j + 2));
										Log.e("ss", list.get(i).substring(j, j + 2));
									}
									String[] str = new String[7];
									numbers.add((list_num_lucky.toArray(str)));
								}
								// 获取摇奖数据
								for (int i = 0; i < numbers.size(); i++) {
									String[] scroll = (String[]) numbers.get(i);
									strs.add(scroll);
								}
							}
						}

						updateStatus();
						setOnClick();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	// 抽奖程序
	/**
	 * 更新状态&获取控件
	 */
	private void updateStatus() {
		// 展示幸运号码
		resulttv = (TextView) findViewById(R.id.status);
		resulttv2 = (TextView) findViewById(R.id.status2);
		resulttv3 = (TextView) findViewById(R.id.status3);
		resulttv4 = (TextView) findViewById(R.id.status4);
		resulttv5 = (TextView) findViewById(R.id.status5);
		resulttv6 = (TextView) findViewById(R.id.status6);
		resulttv7 = (TextView) findViewById(R.id.status7);
		// 显示所摇到的号码那行字
		/*
		 * resulttv8 = (TextView) findViewById(R.id.status8);
		 * resulttv8.setVisibility(View.VISIBLE);
		 * resulttv8.setText("点击下方按钮开始抽奖");
		 */
		// 用户帮助
		// 抽奖按钮
		mix = (Button) findViewById(R.id.btn_mix);
		mix.setTextColor(Color.rgb(41, 36, 33));
		// 确定按钮
		// ok = (Button) findViewById(R.id.btn_ok);
		// 设置显示幸运号码
		resulttv.setText(ln1);
		resulttv2.setText(ln2);
		resulttv3.setText(ln3);
		resulttv4.setText(ln4);
		resulttv5.setText(ln5);
		resulttv6.setText(ln6);
		resulttv7.setText(ln7);
	}

	private int chance;
	// 车轮滚动标志
	private boolean wheelScrolled = true;
	// 车轮滚动的监听器
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			/*
			 * if (chance <= 0) { // resulttv8.setText("您本次抽奖机会已用完!");
			 * if(mix!=null) mix.setClickable(false); } else { //
			 * resulttv8.setText("正在滚动抽奖中,请稍候..."); }
			 */
		}

		public void onScrollingFinished(WheelView wheel) {
			// resulttv8.setText("抽奖结束!");
			// 保存次数
			/*
			 * if(mess==null||mess.equals("")) return;
			 */
			/*
			 * if (chance <= 0) { wheelScrolled = false; if(mix!=null)
			 * mix.setClickable(false); // resulttv8.setText("您本次抽奖机会已用完!"); }
			 */
		}
	};

	// 车轮item改变的监听器
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {

		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			/*
			 * if (wheelScrolled) { updateStatus(); }
			 */
		}
	};

	/**
	 * 初始化轮子
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	@SuppressWarnings("deprecation")
	private void initWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.scroll(0, 2000);
		wheel.setViewAdapter(new SlotMachineAdapter(this, id));
		// wheel.setCurrentItem((int)(Math.random() * 10));
		wheel.setCurrentItem(0);
		wheel.setVisibleItems(3);// 设置显示的条目
		wheel.setSoundEffectsEnabled(true);// 设置是否带有声响
		wheel.setBackgroundColor(getResources().getColor(R.color.orange1));
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
		// wheel.setCurrentItem(0);
	}

	/**
	 * 根据id获取轮子
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * 测试轮子转动位置
	 * 
	 * @return true
	 */
	private boolean test() {
		int value = getWheel(R.id.slot_1).getCurrentItem();
		return testWheelValue(R.id.slot_2, value) && testWheelValue(R.id.slot_3, value)
				&& testWheelValue(R.id.slot_4, value) && testWheelValue(R.id.slot_5, value)
				&& testWheelValue(R.id.slot_6, value) && testWheelValue(R.id.slot_7, value);
	}

	/**
	 * 根据轮子id获取当前item值
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {
		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * 转动轮子
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id, int count) {
		WheelView wheel = getWheel(id);
		// 设置转动轮子的次数跟时间
		wheel.scroll(count, 3000);
	}

	/**
	 * 老虎机适配器
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {

		// 布局膨胀器
		private Context context;
		private int num;

		// 构造函数
		public SlotMachineAdapter(Context context, int id) {
			this.context = context;
			this.num = id;
		}

		// 获取长度
		public int getItemsCount() {
			return items.length;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			ViewHolder holder = null;
			if (cachedView == null) {
				cachedView = View.inflate(context, R.layout.slot_item, null);
				holder = new ViewHolder();
				holder.item = (TextView) cachedView.findViewById(R.id.item);
				cachedView.setTag(holder);
			} else {
				holder = (ViewHolder) cachedView.getTag();
			}
			String i = items[index];
			// 更改第7个轮子的颜色
			if (num == R.id.slot_7) {
				holder.item.setTextColor(Color.rgb(255, 255, 0));
			} else {
				holder.item.setTextColor(Color.rgb(255, 255, 240));
			}

			holder.item.setTextSize(20);
			holder.item.setBackgroundColor(getBaseContext().getResources().getColor(R.color.transparent));
			holder.item.setGravity(Gravity.CENTER);
			holder.item.setText(i);
			return cachedView;
		}

		class ViewHolder {
			TextView item;
		}
	}
	/**
	 * 显示方式
	 */
	private void showPaywindow(String string) {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.gift_activity_powinds, null);
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
		window02.showAtLocation(GiftActivity.this.findViewById(R.id.btn_mix), Gravity.CENTER, 0, 0);
		RelativeLayout re;
		TextView textViewshowTextView;
		re = (RelativeLayout) view.findViewById(R.id.end_button);
		textViewshowTextView = (TextView) view.findViewById(R.id.gift_neirong);
		textViewshowTextView.setText(string);
		re.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
}
