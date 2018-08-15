package com.hex.express.iwant.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.OfferBean;
import com.hex.express.iwant.bean.OfferBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  货物报价
 */
public class LogistiOfferActivity extends BaseActivity {
	@Bind(R.id.mylisoffer_listview)
	PullToRefreshListView mylisoffer_listview;
	@Bind(R.id.looffer_show)
	TitleBarView looffer_show;
	private ListView listView;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.li_chong)
	LinearLayout li_chong;
	@Bind(R.id.ll_shutdown)
	TextView ll_shutdown;
	
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<OfferBean.Data> mList;
	private MylisOfferAdapter adapter;
	public OfferBean bean;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistiofer);
		ButterKnife.bind(this);
		
		initView();
		initData();
		setOnClick();
		getData();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		looffer_show.setTitleText("货物报价");
		listView = mylisoffer_listview.getRefreshableView();
		if (getIntent().getStringExtra("whether").equals("2")) {
			li_chong.setVisibility(View.VISIBLE);
		}else {
			li_chong.setVisibility(View.GONE);
		}
		ll_shutdown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getprequst();
				
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<OfferBean.Data>();
		dialog.show();
		Log.e("11111  ", ""+getIntent().getIntExtra("recId", 0));
		getHttprequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.OFFer, "WLBId", ""+getIntent().getIntExtra("recId", 0), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								OfferBean.class);
//						ToastUtil.shortToast(LogistiOfferActivity.this, bean.message);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new MylisOfferAdapter(
											LogistiOfferActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								mylisoffer_listview.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
								adapter.setData(mList);
								adapter.notifyDataSetChanged();
								mylisoffer_listview.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								if (mList.size()!=0) {
								num = mList.size();
								adapter.addData(mList);
								adapter.notifyDataSetChanged();
//								mylisoffer_listview.onRefreshComplete();
								}
								
								mylisoffer_listview.onRefreshComplete();
							}
						}
//
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LogistiOfferActivity.this, "网络请求加载失败");

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		mylisoffer_listview.setMode(Mode.BOTH);
		mylisoffer_listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttprequst(false, true, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttprequst(false, false, pageNo, true);
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Data data = mList.get(arg2 - 1);
//				Intent intent = new Intent(LogistiOfferActivity.this, MydetailedActivity.class);
//				intent.putExtra("matImageUrl", data.matImageUrl);
//				startActivity(intent);
//				ToastUtil.shortToast(LogistiOfferActivity.this, "详情");

			}
		});

	}

	public void getprequst() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.RestartQuo, "recId", ""+getIntent().getIntExtra("recId", 0)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111json", new String(arg2));
						dialog.dismiss();
						BaseBean		sbean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (sbean.getErrCode()==0) {
									ToastUtil.shortToast(LogistiOfferActivity.this, sbean.getMessage());
									finish();
								}else {
									ToastUtil.shortToast(LogistiOfferActivity.this, sbean.getMessage());	
									finish();
								}
//
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LogistiOfferActivity.this, "网络请求加载失败");

					}
				});

		
	}
	private boolean gift;

	@Override
	public void getData() {
		gift = getIntent().getBooleanExtra("gift", false);
	}
	/**
	 * 
	 * @author huyichuan
	 *  内部类可改
	 */
	class MylisOfferAdapter extends BaseListAdapter{

		public MylisOfferAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new MylisOfferViewHoder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub   quote_item   quote_itemes//   mylogistical_offer_item
			return R.layout.quote_itemes;
		}
		class MylisOfferViewHoder extends ViewHolder{

			public MylisOfferViewHoder(View itemView) {
				super(itemView);
				// TODO Auto-generated constructor stub
			}

			@Bind(R.id.deta_img_heads)//头像
			ImageView deta_img_head;
			
			@Bind(R.id.deta_img_hea)//头像下实名认证
			ImageView deta_img_hea;
			@Bind(R.id.quote_juli)//距离
			TextView quote_juli;
//			@Bind(R.id.quote_time)//时间
//			TextView quote_time;
			@Bind(R.id.quote_name)//公司名
			TextView quote_name;
//			@Bind(R.id.quote_couriersscore)//用户评价
//			RatingBar quote_couriersscore;
//			@Bind(R.id.quote_cishu)//运货次数
//			TextView quote_cishu;
			@Bind(R.id.quote_addse3)//留言
			TextView quote_addse3;
			@Bind(R.id.quote_addse)//地址
			MarqueeTextView quote_addse;
			@Bind(R.id.quote_addse_dingwei)// 地址图标
			TextView quote_addse_dingwei;
			@Bind(R.id.quote_baojia)//报价
			TextView quote_baojia;
			@Bind(R.id.quote_btn)//确认按钮
			Button quote_btn;
			@Bind(R.id.quote_phone)//电话
			TextView quote_phone;
			@Bind(R.id.quote_phones)//电话
			TextView quote_phones;
			@Bind(R.id.re_phone)//电话控件
			RelativeLayout re_phone;
			@Bind(R.id.readdse)//电话控件
			RelativeLayout readdse;
			private OfferBean cardBean;

			@Override
			public void setData(final int position) {
				// TODO Auto-generated method stub
				super.setData(position);
				cardBean = new OfferBean();
				cardBean.data = list;
//				if (bean.data.get(position).matImageUrl!=null || !bean.data.get(position).matImageUrl.equals("")) {
//					new MyBitmapUtils().display(deta_img_head,
//							bean.data.get(position).matImageUrl);
//					deta_img_head.setBackgroundDrawable(null);
//					deta_img_head.setVisibility(View.VISIBLE);
//				}else {
//					deta_img_head.setBackgroundResource(R.drawable.tubgs);
//					deta_img_head.setVisibility(View.VISIBLE);
//				}
				quote_juli.setText("距我："+cardBean.getData().get(position).distance+"km");
				quote_name.setText("公司："+cardBean.getData().get(position).companyName);
//				quote_time.setText(cardBean.getData().get(position).publishTime);
				if ("".equals(cardBean.getData().get(position).yardAddress)) {
					readdse.setVisibility(View.GONE);
					}	else {
						
				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("2")) {
					readdse.setVisibility(View.GONE);
				}else {
					readdse.setVisibility(View.VISIBLE);
				}
				if ( getIntent().getBooleanExtra("sendCargo", false)==false) {
					readdse.setVisibility(View.GONE);
					quote_addse.setVisibility(View.GONE);
				}else {
					readdse.setVisibility(View.VISIBLE);
					quote_addse.setText(cardBean.getData().get(position).yardAddress);
				}
					}
//				quote_couriersscore.setRating(cardBean.getData().get(position).evaluation); 

				String	ct3=cardBean.getData().get(position).transferMoney;
				String[] strs3=ct3.split("\\.");
				quote_baojia.setText(""+strs3[0]+" 元"); 
//				quote_baojia.setText("报价："+cardBean.getData().get(position).transferMoney+" 元"); 
//				quote_cishu.setText("运货次数："+cardBean.getData().get(position).shipNumber); 
//				if(cardBean.getData().get(position).realManAuth.equals("Y")){
//					deta_img_hea.setVisibility(View.VISIBLE);
//				}
//				ToastUtil.shortToast(LogistiOfferActivity.this,cardBean.getData().get(position).transferMoney);
//				quote_liuyan.setText("留言："+cardBean.getData().get(position).luMessage); 
				
//				Log.e("1111111status    ", ""+getIntent().getStringExtra("status"));
				if (getIntent().getStringExtra("status").equals("2") || getIntent().getStringExtra("status").equals("8")) {
					re_phone.setVisibility(View.VISIBLE);	
					quote_phone.setText("公司电话："+cardBean.getData().get(position).mobile); 
				}else {
					re_phone.setVisibility(View.GONE);
				}
				quote_phones.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AppUtils.intentDial(LogistiOfferActivity.this, cardBean.getData().get(position).mobile);
					}
				});
				quote_btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, MydetailedActivity.class);
						intent.putExtra("cargoName",getIntent().getStringExtra("cargoName"));
						intent.putExtra("cargoWeight", getIntent().getStringExtra("cargoWeight"));
						intent.putExtra("cargoVolume", getIntent().getStringExtra("cargoVolume"));
						intent.putExtra("startPlace", getIntent().getStringExtra("startPlace"));
						intent.putExtra("entPlace", getIntent().getStringExtra("entPlace"));
						intent.putExtra("takeName", getIntent().getStringExtra("takeName"));
						intent.putExtra("takeMobile", getIntent().getStringExtra("takeMobile"));
						intent.putExtra("publishTimes", getIntent().getStringExtra("publishTime"));
						intent.putExtra("takeCargo", getIntent().getBooleanExtra("takeCargo", false));
						intent.putExtra("sendCargo", getIntent().getBooleanExtra("sendCargo", false));
						intent.putExtra("remark", getIntent().getStringExtra("remark"));
						intent.putExtra("takeTime", getIntent().getStringExtra("takeTime"));
						intent.putExtra("arriveTime", getIntent().getStringExtra("arriveTime"));
						intent.putExtra("billCode", getIntent().getStringExtra("billCode"));
						intent.putExtra("appontSpace",  getIntent().getStringExtra("appontSpace"));
//						intent.putExtra("whether", getIntent().getStringExtra("whether"));
//						intent.putExtra("premium", getIntent().getStringExtra("premium"));
						intent.putExtra("status", getIntent().getStringExtra("status"));
						intent.putExtra("recId", getIntent().getIntExtra("recId", 0));
						intent.putExtra("sendPerson", getIntent().getStringExtra("sendPerson"));
						intent.putExtra("sendPhone", getIntent().getStringExtra("sendPhone"));
						intent.putExtra("cargoNumber", getIntent().getStringExtra("cargoNumber"));
						
						intent.putExtra("length",getIntent().getStringExtra("length"));
						intent.putExtra("wide", getIntent().getStringExtra("wide"));
						intent.putExtra("high", getIntent().getStringExtra("high"));
						intent.putExtra("weight", getIntent().getStringExtra("weight"));
						intent.putExtra("cargoSize", getIntent().getStringExtra("cargoSize"));
						intent.putExtra("carType", getIntent().getStringExtra("carType"));
						intent.putExtra("carName", getIntent().getStringExtra("carName"));
						intent.putExtra("tem", getIntent().getStringExtra("tem"));
						
						
//						intent.putExtra("cargoCost", getIntent().getStringExtra("cargoCost"));
//						intent.putExtra("insureCost", getIntent().getDoubleExtra("insureCost", 0.0));//保费
//						Log.e("22  ", ""+getIntent().getIntExtra("recId", 0));
						intent.putExtra("userId", cardBean.getData().get(position).userId);
						intent.putExtra("distance", cardBean.getData().get(position).distance);
						intent.putExtra("companyName", cardBean.getData().get(position).companyName);
						intent.putExtra("publishTime", cardBean.getData().get(position).publishTime);
						intent.putExtra("matImageUrl", cardBean.getData().get(position).matImageUrl);
						intent.putExtra("address", cardBean.getData().get(position).yardAddress);
						intent.putExtra("mobile", cardBean.getData().get(position).mobile);
						intent.putExtra("luMessage", cardBean.getData().get(position).luMessage);
						intent.putExtra("evaluation", cardBean.getData().get(position).evaluation);
						intent.putExtra("shipNumber", cardBean.getData().get(position).shipNumber);
						intent.putExtra("transferMoney", cardBean.getData().get(position).transferMoney);
						intent.putExtra("takeCargoMoney", cardBean.getData().get(position).takeCargoMoney);
						intent.putExtra("sendCargoMoney", cardBean.getData().get(position).sendCargoMoney);
						intent.putExtra("cargoTotal", cardBean.getData().get(position).cargoTotal);
						
						startActivity(intent);
						finish();
					}
				});
				
			}
			}
		}
}
