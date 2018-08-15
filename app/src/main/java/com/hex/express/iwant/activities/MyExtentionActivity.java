package com.hex.express.iwant.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.MyExtentionAdapter;
import com.hex.express.iwant.bean.AgentBean;
import com.hex.express.iwant.bean.AgentUBean;
import com.hex.express.iwant.bean.AgentUserBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.viewpager.ADInfo;
import com.hex.express.iwant.viewpager.CycleViewPager;
import com.hex.express.iwant.viewpager.CycleViewPager.ImageCycleViewListener;
import com.hex.express.iwant.viewpager.ViewFactory;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的推广界面
 * 
 * @author SCHT-50
 * 推广界面
 */
public class MyExtentionActivity extends BaseActivity {
	private List<AgentUBean.Data> mList;
	private List<AgentUBean.Data> mList2;
	MyExtentionAdapter adapter;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.text_title)
	TextView text_title;
	@Bind(R.id.ptrl_wallet)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.ptrl_number)//推广人数
	TextView ptrl_number;
	@Bind(R.id.ptrl_money)//推广钱
	TextView ptrl_money;
	@Bind(R.id.mye_layout)//显示推广
	LinearLayout mye_layout;
	@Bind(R.id.mye_nolayout)//不显示推广
	LinearLayout mye_nolayout;
	
	public LoadingProgressDialog dialog;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPager cycleViewPager;
	int number;
	//http://www.efamax.com/images/manhua/shunfeng_02.png",
  //  @"http://www.efamax.com/images/manhua/shunfeng_02.png",
   // @"http://www.efamax.com/images/manhua/shunfeng_03.png",
    //@"http://www.efamax.com/images/manhua/shunfeng_04.png"
	private String[]  imageUrls = {"http://www.efamax.com/images/manhua/shunfeng_01.png",
			"http://www.efamax.com/images/manhua/shunfeng_02.png",
			"http://www.efamax.com/images/manhua/shunfeng_03.png",
			"http://www.efamax.com/images/manhua/shunfeng_04.png"};

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_extention);
		ButterKnife.bind(MyExtentionActivity.this);
		configImageLoader();
		initialize();
		initData();
		addPostResult();
		setOnClick();
		addPostResultmoney();
		
//		addPostResultlist();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}
	@SuppressLint("NewApi")
	private void initialize() {
		
		cycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.ptrl_viewpager_content);
		
		for(int i = 0; i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("图片-->" + i );
			infos.add(info);
		}
		
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));
		
		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		//设置轮播
		cycleViewPager.setWheel(true);

	    // 设置轮播时间，默认5000ms
		cycleViewPager.setTime(2000);
		//设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}
	
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
//				Toast.makeText(MyExtentionActivity.this,
//						"position-->" + info.getContent(), Toast.LENGTH_SHORT)
//						.show();
			}
			
		}

	};
	
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

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);		
	}

	// 获取推广数据
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		dialog.show();
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getThreeUrl(MCUrl.AgentUserListnew, "userId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)),
						"pageNo", pageNo + "",
						"pageSize", String.valueOf(pageSize)),
				 null, null, params,new AsyncHttpResponseHandler() {
				
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("111111111tuiguang1;;;", new String(arg2));
						dialog.dismiss();
						AgentUBean baseBean = new Gson().fromJson(new String(
								arg2), AgentUBean.class);
						mList=new ArrayList<AgentUBean.Data>();
						mList2=new ArrayList<AgentUBean.Data>();
						mList.clear();
						mList2.clear();
						mList=	baseBean.data;
						if (isFirst) {
						if (mList.size() != 0 && mList != null) {
							if (adapter == null) {
								adapter = new MyExtentionAdapter(
										MyExtentionActivity.this, mList);
								listView.setAdapter(adapter);
							}

						} else {
							view_null_message.setVisibility(View.VISIBLE);
							ptrlv_card.setVisibility(View.GONE);
						}
						}else {
							if (isRefresh && !isPull) {
								mList2.clear();
								mList2.addAll(mList);
								adapter.setData(mList2);
								adapter.notifyDataSetChanged();
								ptrlv_card.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								num = mList.size();
								adapter.addData(mList);
								adapter.notifyDataSetChanged();
								ptrlv_card.onRefreshComplete();
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						view_load_fail.setVisibility(View.VISIBLE);
						ptrlv_card.setVisibility(View.GONE);
						dialog.dismiss();
					}
				});

	}

	@Override
	public void initData() {
		text_title.setText("我的推广");
		listView = ptrlv_card.getRefreshableView();
		dialog = new LoadingProgressDialog(this);
//		getExtention();
		getHttpRequst(true, false, 1, false);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = sDateFormat.format(new java.util.Date());
		Log.e("json",
				"http://www.efamax.com/mobile/chart/line.html?"
						+ date
						+ "userId="
						+ PreferencesUtils.getInt(getApplicationContext(),
								PreferenceConstants.UID));
//		WebSettings webSettings = webview.getSettings();
//		webSettings.setSavePassword(false);
//		webSettings.setSaveFormData(false);
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setSupportZoom(false);
//		webview.loadUrl("http://www.efamax.com/mobile/chart/line_androd.html?"
//				+ date
//				+ "userId="
//				+ PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID));
//		webview.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//		});
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyExtentionActivity.this, CourierInstrucActivity.class));
//				startActivity(new Intent(RechargeActivity.this, PreferentialActivity.class));
			}
		});
	}

	private int num;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);
		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {

				getHttpRequst(false, true, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
							false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		ptrlv_card.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		try {
			
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		RequestParams params=new RequestParams();
		dialog.show();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.AgentCount, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						String string=new String(arg2);
						JSONObject jObject;
						try {
							jObject = new JSONObject(string);
							JSONArray jArray=jObject.optJSONArray("data");
							jArray.opt(0);
							number=(Integer) jArray.opt(0);
							ptrl_number.setText("推广人数: "+jArray.opt(0));
							Log.e("122222s   ",  ""+jArray.opt(0));
							if (number==0) {
								mye_layout.setVisibility(View.GONE);
								mye_nolayout.setVisibility(View.VISIBLE);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
//						AgentBean baseBean = new Gson().fromJson(new String(
//								arg2), AgentBean.class);
//						ToastUtil.shortToast(MyExtentionActivity.this, ""+baseBean.getMessage());
//						Log.e("122222s   ",  ""+baseBean.);
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	private void addPostResultmoney() {
		JSONObject obj = new JSONObject();
		try {
			
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("11111111qian   ",obj.toString());
//			AsyncHttpUtils.doPostJson(MyExtentionActivity.this, MCUrl.UserBalance, obj.toString(),
//					new AsyncHttpResponseHandler() {expressRecourageLeftMoney
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.UserBalance, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111qian   ", new String(arg2));
						dialog.dismiss();
//						String string=new String(arg2);
//						JSONObject jObject;
//						try {
//							jObject = new JSONObject(string);
//							JSONArray jArray=jObject.optJSONArray("data");
//							jArray.opt(0);
//							Log.e("122222s   ",  ""+jArray.opt(0));
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						
						AgentBean baseBean = new Gson().fromJson(new String(
								arg2), AgentBean.class);
						if (baseBean.getErrCode()==0) {
							
						}else {
							ToastUtil.shortToast(MyExtentionActivity.this, ""+baseBean.getMessage());
						}
						
						Log.e("122223s   ",  ""+baseBean.getData().get(0).perAgentSum);
						ptrl_money.setText("推广收入: "+baseBean.getData().get(0).perAgentSum+" 元");
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
//		AsyncHttpUtils.doSimGet(
//				UrlMap.getTwo(MCUrl.DRIVERTRUETASK, "recId", ""+1155,"dealPassword",""+156540),
//				new AsyncHttpResponseHandler(){
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						// TODO Auto-generated method stub
//						Log.e("11111111qian代理   ", new String(arg2));
////						BaseBean bean = new Gson().fromJson(new String(arg2),
////								BaseBean.class);
////						Log.e("JSON1", dealPassword.toString()+"============");
//						// Log.e("bean________",
//						// bean.getData().get(arg0).toString());
//					}
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//							Throwable arg3) {
//					}
//				});
		

	}
	private void addPostResultlist() {
		JSONObject obj = new JSONObject();
		try {
			
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("11111112223   ",UrlMap.getThreeUrl(MCUrl.AgentUserList, "userId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)),
				"pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)));
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getThreeUrl(MCUrl.AgentUserList, "userId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)),
						"pageNo", pageNo + "",
						"pageSize", String.valueOf(pageSize)),
				 null, null, params,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("22222qlist   ", new String(arg2));
						dialog.dismiss();
						AgentUserBean baseBean = new Gson().fromJson(new String(
								arg2), AgentUserBean.class);
						if (baseBean.getErrCode()==0) {
							
						}else {
							
							ToastUtil.shortToast(MyExtentionActivity.this, ""+baseBean.getMessage());
						}
						
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
//	

	}

}
