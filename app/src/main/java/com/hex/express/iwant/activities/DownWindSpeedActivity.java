package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.DownwindEscortAdapter;
import com.hex.express.iwant.adapters.DownwindOwnerAdapter;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DownSpecialBean.Data;
import com.hex.express.iwant.bean.DownStrokeBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【顺风速递】界面
 * 
 * @author han
 *
 */
public class DownWindSpeedActivity extends BaseActivity {
	@Bind(R.id.text_title)
	TextView text_title;
	@Bind(R.id.btnLeft)
	Button btnLeft;
	@Bind(R.id.ll_text_rank)
	LinearLayout ll_text_rank;
	@Bind(R.id.owner)
	Button owner;
	@Bind(R.id.escort)
	Button escort;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	// 我是货主显示menu
	@Bind(R.id.owner_listview)
	PullToRefreshListView owner_listview;
	@Bind(R.id.btn_postDownwindTask)
	Button btn_postDownwindTask;
	@Bind(R.id.ll_menu_owner)
	LinearLayout ll_menu_owner;
	@Bind(R.id.text_recently)
	TextView text_recently;
	@Bind(R.id.text_time)
	TextView text_time;
	@Bind(R.id.text_favorable)
	TextView text_favorable;
	// 我是镖师显示menu
	@Bind(R.id.btn_escort)
	Button btn_escort;
	@Bind(R.id.escort_listview)
	PullToRefreshListView escort_listview;
	@Bind(R.id.ll_menu_escort)
	LinearLayout ll_menu_escort;
	@Bind(R.id.text_recently_escort)
	TextView text_recently_escort;
	@Bind(R.id.text_time_escort)
	TextView text_time_escort;
	@Bind(R.id.text_favorable_escort)
	TextView text_favorable_escort;
	// @Bind(R.id.text_other_escort)
	// TextView text_other_escort;
	@Bind(R.id.iv_right)
	ImageView iv_right;
	@Bind(R.id.lout2)
	RelativeLayout lout2;
	boolean flag = false;
	boolean first = false;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private int escorePageNo = 1;// 请求页码
	private DownStrokeBean bean;
	private List<DownStrokeBean.Data> mList;
	private List<DownStrokeBean.Data> mList2;
	DownwindOwnerAdapter adapter;
	private ListView listView;
	private DownSpecialBean escoreBean;

	private List<DownSpecialBean.Data> escoreList;
	private List<DownSpecialBean.Data> escoreList2;
	DownwindEscortAdapter escoreAdapter;
	private ListView escoreListView;

	String sortType = "2";// 排序类型 1距离 2出发时间 3好评度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downwind);
		ButterKnife.bind(this);
		initData();
		setOnClick();
		getData();
	}
	@Override//重新打开页面时
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initData();
		getData();
	}
	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initData() {
		listView = owner_listview.getRefreshableView();
		escoreListView = escort_listview.getRefreshableView();
		escoreList = new ArrayList<DownSpecialBean.Data>();
		escoreList2= new ArrayList<DownSpecialBean.Data>();
		btn_postDownwindTask.setText("我要发件");
		if (getIntent().getStringExtra("downwind").equals("owner")) {
			text_title.setText("我是货主");
			owner_listview.setVisibility(View.VISIBLE);
			escoreListView.setVisibility(View.GONE);
			view_null_message.setVisibility(View.GONE);
			view_load_fail.setVisibility(View.GONE);
			btn_postDownwindTask.setVisibility(View.VISIBLE);
			btn_escort.setVisibility(View.INVISIBLE);
			lout2.setBackgroundResource(R.drawable.map_biao_bg);
			first = false;
			getHttpEscoreRequst(true, false, 1, false, sortType);
		}else {
			first = true;
			text_title.setText("顺风速递");
			lout2.setBackgroundResource(R.drawable.map_huo_bg); 
			escoreListView.setVisibility(View.VISIBLE);
			owner_listview.setVisibility(View.GONE);
			view_null_message.setVisibility(View.GONE);
			view_load_fail.setVisibility(View.GONE);
			btn_postDownwindTask.setVisibility(View.INVISIBLE);
			btn_escort.setVisibility(View.VISIBLE);
			getHttpOwnerRequst(true, false, 1, false, sortType);
		}
	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@OnClick({ R.id.owner, R.id.escort, R.id.ll_text_rank, R.id.btnLeft, R.id.text_time, R.id.text_recently,
			R.id.text_favorable, R.id.text_time_escort, R.id.text_recently_escort, R.id.text_favorable_escort, })
	// R.id.text_other_escort
	public void MyonClick(View view) {
		switch (view.getId()) {
		case R.id.owner:// 我是货主
			if (first) {
				lout2.setBackgroundResource(R.drawable.map_biao_bg);
				owner.setTextColor(getResources().getColor(R.color.orange1));
				escort.setTextColor(getResources().getColor(R.color.black));
			}
			ll_menu_owner.setVisibility(View.GONE);
			ll_menu_escort.setVisibility(View.GONE);
			owner_listview.setVisibility(View.VISIBLE);
			escoreListView.setVisibility(View.GONE);
			view_null_message.setVisibility(View.GONE);
			view_load_fail.setVisibility(View.GONE);
			btn_postDownwindTask.setVisibility(View.VISIBLE);
			btn_escort.setVisibility(View.INVISIBLE);
			first = false;

			sortType = "2";
			getHttpEscoreRequst(true, false, 1, false, sortType);
			break;
		case R.id.escort:// 我是镖师
			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")) {
				Builder ad = new Builder(DownWindSpeedActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("您未认证镖师，没权限查看");
				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						startActivity(new Intent(DownWindSpeedActivity.this, PrefectActivity.class).putExtra("IDENTIFY_CODE",
								"2"));
					}
				});
				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});
				ad.create().show();
			} else {
				if (!first) {
					lout2.setBackgroundResource(R.drawable.map_huo_bg);
					owner.setTextColor(getResources().getColor(R.color.black));
					escort.setTextColor(getResources().getColor(R.color.orange1));
				}
				first = true;
				ll_menu_owner.setVisibility(View.GONE);
				ll_menu_escort.setVisibility(View.GONE);
				escoreListView.setVisibility(View.VISIBLE);
				owner_listview.setVisibility(View.GONE);
				view_null_message.setVisibility(View.GONE);
				view_load_fail.setVisibility(View.GONE);
				btn_postDownwindTask.setVisibility(View.INVISIBLE);
				btn_escort.setVisibility(View.VISIBLE);

				sortType = "2";
				getHttpOwnerRequst(true, false, 1, false, sortType);
			}
			break;
		case R.id.ll_text_rank:// 智能排序
//			if (!flag) {
//				if (first) {
//					ll_menu_escort.setVisibility(View.VISIBLE);
//					ll_menu_owner.setVisibility(View.GONE);
//				} else {
//					ll_menu_owner.setVisibility(View.VISIBLE);
//					ll_menu_escort.setVisibility(View.GONE);
//				}
//				iv_right.setBackgroundResource(R.drawable.right_barb1);
//				flag = true;
//			} else {
//				ll_menu_escort.setVisibility(View.GONE);
//				ll_menu_owner.setVisibility(View.GONE);
//				iv_right.setBackgroundResource(R.drawable.right_barb);
//				flag = false;
//			}
			break;
		case R.id.btnLeft:// 返回按钮
			finish();
			break;
		case R.id.text_time:// 出发时间最早

			sortType = "2";
			Log.e("text", sortType);
			getHttpEscoreRequst(true, false, 1, false, sortType);
			break;
		case R.id.text_favorable:// 好评最高

			sortType = "3";
			getHttpEscoreRequst(true, false, 1, false, sortType);
			break;
		case R.id.text_recently:// 离我最近

			sortType = "1";
			getHttpEscoreRequst(true, false, 1, false, sortType);
			break;
		case R.id.text_time_escort:// 出发时间最早的货主

			sortType = "2";
			getHttpOwnerRequst(true, false, 1, false, sortType);
			break;
		case R.id.text_recently_escort:// 离我最近的货

			sortType = "1";
			getHttpOwnerRequst(true, false, 1, false, sortType);
			break;
		case R.id.text_favorable_escort:// 好评最高的货主

			sortType = "3";
			getHttpOwnerRequst(true, false, 1, false, sortType);
			break;
		default:
			break;

		}
	}

	private int num;

	/**
	 * 获取我是镖师发的行程记录
	 * 
	 * @param isFirst
	 * @param isRefresh
	 * @param pageNo
	 * @param isPull
	 * @param sortType
	 */
	@SuppressWarnings("unchecked")
	private void getHttpEscoreRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull,
			String sortType) {
		String url = UrlMap.getfour(MCUrl.DRIVERROUTELIST, "userId",
				String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)), "pageNo",
				String.valueOf(pageNo), "pageSize", String.valueOf(pageSize), "sortType", sortType);
		Log.e("url111111111", url);
		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				Log.e("pkdkdggggggggggggggggggjfjdkj", new String(arg2));
				mList = new ArrayList<DownStrokeBean.Data>();
				mList2= new ArrayList<DownStrokeBean.Data>();
				mList2.clear();
				mList.clear();
				bean = new Gson().fromJson(new String(arg2), DownStrokeBean.class);
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						adapter = new DownwindOwnerAdapter(DownWindSpeedActivity.this, mList);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();

					} else {
						view_null_message.setVisibility(View.VISIBLE);
						owner_listview.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						mList2.clear();
						mList2.addAll(mList);
						adapter.setData(mList2);
						adapter.notifyDataSetChanged();
						owner_listview.onRefreshComplete();
					} else if (!isRefresh && isPull) {
						num = mList.size();
						adapter.addData(mList);
						adapter.notifyDataSetChanged();
						owner_listview.onRefreshComplete();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
				view_load_fail.setVisibility(View.VISIBLE);
				owner_listview.setVisibility(View.GONE);

			}
		});
	}

	/**
	 * 我是货主发布行程记录在我是镖师按钮下显示
	 * 
	 * @param isFirst
	 * @param isRefresh
	 * @param pageNo
	 * @param isPull
	 */
	@SuppressWarnings("unchecked")
	private void getHttpOwnerRequst(final boolean isFirst, final boolean isRefresh, int escorePageNo,
			final boolean isPull, String sortType) {
		String url = UrlMap.getfour(MCUrl.DOWNWINDTASKLIST, "userId",
				String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)), "pageNo",
				String.valueOf(escorePageNo), "pageSize", String.valueOf(pageSize), "sortType", sortType);
		dialog.show();
		Log.e("111111dkj", url);
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				Log.e("pkdkoooooooodjfjdkj", new String(arg2));
				escoreList2.clear();
				escoreBean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
				escoreList = escoreBean.data;
				if (isFirst) {
					if (escoreList.size() != 0 && escoreList != null) {

						escoreAdapter = new DownwindEscortAdapter(DownWindSpeedActivity.this, escoreList);
						escoreListView.setAdapter(escoreAdapter);

					} else {
						view_null_message.setVisibility(View.VISIBLE);
						escoreListView.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						escoreList2.clear();
						escoreList2.addAll(escoreList);
						escoreAdapter.setData(escoreList2);
						escoreAdapter.notifyDataSetChanged();
						escort_listview.onRefreshComplete();
					} else if (!isRefresh && isPull) {
						num = escoreList.size();
						escoreAdapter.addData(escoreList);
						escoreAdapter.notifyDataSetChanged();
						escort_listview.onRefreshComplete();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
				view_load_fail.setVisibility(View.VISIBLE);
				escort_listview.setVisibility(View.GONE);
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		owner_listview.setMode(Mode.BOTH);
		owner_listview.setOnRefreshListener(new OnRefreshListener2() {

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttpEscoreRequst(false, false, 1, true, sortType);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpEscoreRequst(false, false, pageNo, true, sortType);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				// startActivity(new Intent(DownWindActivity.this,
				// EscoreDartEvaluteActivity.class));

			}
		});
		btn_postDownwindTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(DownWindSpeedActivity.this, PostDownWindTaskActivity.class));
				finish();
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getData() {
		escort_listview.setMode(Mode.BOTH);
		escort_listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttpOwnerRequst(false, true, 1, false, sortType);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				escorePageNo++;
				getHttpOwnerRequst(false, false, escorePageNo, true, sortType);
				if (num < pageSize && escorePageNo > 2) {
					Log.e("dd", num + "");
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		escoreListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1"))//如果为普通用户
				{
//					AlertDialog.Builder ad = new Builder(DownWindSpeedActivity.this);
//					TextView tv = new TextView(DownWindSpeedActivity.this);  
//	                tv.setText("非常抱歉！");    //内容  
//	                tv.setTextSize(22);//字体大小  
//	                tv.setPadding(30, 20, 10, 10);//位置  
//	                tv.setTextColor(Color.parseColor("#000000"));//颜色  
//					ad.setCustomTitle(tv);
//					ad.setTitle("温馨提示");
//					ad.setMessage("成为镖师就可以接单了，快去认证吧");
//					ad.setNegativeButton("去认证", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intent=new Intent();
//							intent.setClass(DownWindSpeedActivity.this, RoleAuthenticationActivity.class);
//							startActivity(intent);
//						}
//					});
//					ad.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							arg0.dismiss();
//							
//						}
//					});
//					ad.create().show();
			
					showPaywindow();
				}else {
					
				
				Data data = (Data) escoreAdapter.list.get(arg2 - 1);
				Intent intent = new Intent(DownWindSpeedActivity.this, DownEscortDetialsActivity.class);
				intent.putExtra("recId", String.valueOf(data.recId));
				intent.putExtra("publishTime", data.publishTime);
				intent.putExtra("mobile", data.mobile);
				intent.putExtra("address", data.address.replace("中国", ""));
				intent.putExtra("addressTo", data.addressTo.replace("中国", ""));
				intent.putExtra("matName", data.matName);
//				intent.putExtra("transferMoney", String.valueOf(data.transferMoney));
				intent.putExtra("transferMoney", data.transferMoney);
				intent.putExtra("matRemark", data.matRemark);
				intent.putExtra("matWeight", data.matWeight);
				intent.putExtra("matImageUrl", data.matImageUrl);
				intent.putExtra("fromLatitude", String.valueOf(data.fromLatitude));
				intent.putExtra("fromLongitude", String.valueOf(data.fromLongitude));
				intent.putExtra("toLatitude", String.valueOf(data.toLatitude));
				intent.putExtra("toLongitude", String.valueOf(data.toLongitude));
				intent.putExtra("length", data.length);
				intent.putExtra("wide", data.wide);
				intent.putExtra("high", data.high);
		 		Log.e("recId", data.recId + "");
		 		intent.putExtra("replaceMoney", data.replaceMoney);
				intent.putExtra("ifReplaceMoney", data.ifReplaceMoney);
				intent.putExtra("ifTackReplace", data.ifTackReplace);
				startActivity(intent);
				}
			}
		});
		btn_escort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(DownWindSpeedActivity.this, PostDownWindTaskActivity.class));
			}
		});

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}
	/**
	 * 显示提示信息
	 */
	private void showPaywindow() {
		final PopupWindow window02;
		TextView btnsaves_pan;
		Button btnsaves_que;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwind_tishi, null);
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
		window02.showAtLocation(DownWindSpeedActivity.this.findViewById(R.id.btn_escort), Gravity.CENTER, 0, 0);
	
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
		btnsaves_que=(Button) view.findViewById(R.id.btnsaves_que);
		btnsaves_pan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
			}
		});
		btnsaves_que.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(DownWindSpeedActivity.this, RoleAuthenticationActivity.class);
				startActivity(intent);
				finish();
				
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
