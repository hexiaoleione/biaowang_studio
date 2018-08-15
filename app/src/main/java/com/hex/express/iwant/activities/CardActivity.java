package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.Intents;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.CardAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.CardBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 现金券
 * 
 * @author SCHT-40
 * couponId;// 现金券类别id id为1是通用类型   2为顺丰速递  3为限时专递  加(4一键快递 		5-抢单发布)
 */
public class CardActivity extends BaseActivity {
	@Bind(R.id.ptrlv_card)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.btnRight)
	TextView btnRight;
	
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<CardBean.Data> mList;
	private List<CardBean.Data> mList2;
	private CardAdapter adapter;
	public CardBean bean;
	@Bind(R.id.null_message)
	View view_null_message;
	StringBuffer strb=new StringBuffer();
	Intent intent = new Intent();
	boolean change=false;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initView();
		initData();
		setOnClick();
		getData();
	}

	@Override
	public void onWeightClick(View v) {
	}

	@Override
	public void initView() {
//		tbv_show.setTitleText(R.string.cardtitle);
		listView = ptrlv_card.getRefreshableView();
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
				startActivity(new Intent(CardActivity.this, HModelActivity.class));
			}
		});
	}

	@Override
	public void initData() {
		mList = new ArrayList<CardBean.Data>();
		mList2 = new ArrayList<CardBean.Data>();
		dialog.show();
		getHttprequst(true, false, 1, false);
	}

	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.COUPON, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						mList2.clear();
						bean = new Gson().fromJson(new String(arg2),
								CardBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new CardAdapter(
											CardActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								ptrlv_card.setVisibility(View.GONE);
							}
						} else {
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
						dialog.dismiss();
						ToastUtil.shortToast(CardActivity.this, "网络请求加载失败");

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
		final List<Integer> list = new ArrayList();
		ptrlv_card.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Data data = mList.get(arg2 - 1);
				if(change=false){
					if (data.money != 0 && data.userCouponId != 0) {
						intent.setClass(CardActivity.this, DownWindPayActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("strResult", ""+data.userCouponId);
						intent.putExtra("bundle2", bundle);
						setResult(RESULT_OK, intent);
						list.add(arg2,data.userCouponId);
//						finish();
//					}
				}
				}else if (change=true) {
					if (data.money != 0 && data.userCouponId != 0) {
						intent.setClass(CardActivity.this, DownWindPayActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("strResult", ""+data.userCouponId);
						intent.putExtra("bundle2", bundle);
						setResult(RESULT_OK, intent);
						list.remove(arg2);
//						finish();
//					}
				}
					
				}
				

			}
		});

	}

	private boolean gift;

	@Override
	public void getData() {
		gift = getIntent().getBooleanExtra("gift", false);
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case 1:
//			if (resultCode == Activity.RESULT_OK) {
//				
//			}
//
//			break;
//
//		default:
//			break;
//		}
//	}
	class CardAdapter extends BaseListAdapter {
		public CardAdapter(Context context, List list) {
			super(context, list);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub
			return R.layout.item_activity_newcard;// item_activity_card  item_activity_newcard
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new CardViewHodler(itemView);
		}

		class CardViewHodler extends ViewHolder {
			public CardViewHodler(View itemView) {
				super(itemView);
				// TODO Auto-generated constructor stub
			}

//			@Bind(R.id.txt_cardname)
//			TextView txt_cardname;
			@Bind(R.id.txt_cardmoney)
			TextView moneyView;
			@Bind(R.id.txt_cardexpname)
			TextView nameView;
			@Bind(R.id.txt_cardConditions)
			TextView conditionView;
			@Bind(R.id.txt_cardDuration)
			TextView timeView;
			@Bind(R.id.btn_usecard)
			TextView textView;
//			@Bind(R.id.btn_use)
//			TextView btn_use;
			
			@Bind(R.id.card_layout)
			RelativeLayout card_layout;
			Map<Integer, Integer> isCheckMap =  new HashMap<Integer, Integer>();
			private CardBean cardBean;
			private boolean ins;
			
			Integer integer;

			

			@Override
			public void setData(final int position) {
				// TODO Auto-generated method stub
				super.setData(position);
				cardBean = new CardBean();
				cardBean.data = list;
				if (cardBean.data.get(position).ifExpired) {
//					textView.setText("已过期");
					initDatas(position);
				} else {
					if (cardBean.data.get(position).ifUsed) {
//						textView.setText("已使用");
						initDatas(position);

					} else {
//						textView.setText("未使用");
//						moneyView.setTextColor(context.getResources().getColor(
//								R.color.orange1));
//						if (cardBean.data.get(position).couponId==1) {
//							btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//						}
//						if (cardBean.data.get(position).couponId==2) {
//							btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//						}
//						if (cardBean.data.get(position).couponId==3) {
//							btn_use.setBackgroundResource(R.drawable.xxianshimg_68);
//						}
//						if (cardBean.data.get(position).couponId==4) {
//							btn_use.setBackgroundResource(R.drawable.xshubiaoimg_68);
//						}
						
						String s = String.valueOf(cardBean.data.get(position).money);
						String money = s.substring(0,s.indexOf("."));
						moneyView.setText( money);
						timeView.setText("截止日期："
								+ cardBean.data.get(position).coupontime);
						timeView.setTextColor(context.getResources().getColor(
								R.color.gray));
						nameView.setText(cardBean.data.get(position).conditions);
//						nameView.setTextColor(context.getResources().getColor(
//								R.color.gray));couponName
						conditionView.setText(
								 cardBean.data.get(position).couponName);
//						conditionView.setTextColor(context.getResources().getColor(
//								R.color.gray));

					}

				}
				card_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(change==true){
							 Log.e("11111", "ggggg  " );
							card_layout.setBackgroundColor(context.getResources().getColor(
								R.color.blue));
						}
					}
				});
//				cb.setTag(position);
//				cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//					   
//					   @Override
//					   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						   if(cb.isChecked()){
////							   ins=isChecked;
//							   isCheckMap.put(position, cardBean.data.get(position).userCouponId);
//						      
//						      strb.append(cardBean.data.get(position).userCouponId);
//						      Log.e("11111", "ggggg  "+strb.toString() );
//						      btnRight.setVisibility(View.VISIBLE);
//					   }else{
//						   ins=isChecked;
//						   //取消选中的则剔除
//	                        isCheckMap.remove(position);
//	                       
//	                       
//						   }
//					   }
//					  });
				
					
//				}
				
			}

			public void initDatas(final int position) {
//				textView.setTextColor(context.getResources()
//						.getColor(R.color.white));
//				textView.setBackgroundColor(context.getResources().getColor(
//						R.color.gray));
//				if (cardBean.data.get(position).couponId==1) {
//					btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//				}
//				if (cardBean.data.get(position).couponId==2) {
//					btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//				}
//				if (cardBean.data.get(position).couponId==3) {
//					btn_use.setBackgroundResource(R.drawable.xxianshimg_68);
//				}
//				if (cardBean.data.get(position).couponId==4) {
//					btn_use.setBackgroundResource(R.drawable.xshubiaoimg_68);
//				}
				String s = String.valueOf(cardBean.data.get(position).money);
				String newD = s.substring(0,s.indexOf("."));
				moneyView.setText( newD);
				moneyView.setTextColor(context.getResources()
						.getColor(R.color.gray));
//				txt_cardname.setTextColor(context.getResources().getColor(
//						R.color.gray));
				timeView.setText("截止日期："
						+ cardBean.data.get(position).coupontime);
				timeView.setTextColor(context.getResources().getColor(
						R.color.gray));
				nameView.setText(cardBean.data.get(position).conditions);
//				nameView.setTextColor(context.getResources().getColor(
//						R.color.gray));couponName
				conditionView.setText(
						cardBean.data.get(position).couponName);
//				conditionView.setTextColor(context.getResources().getColor(
//						R.color.gray));
				
			}
		}

	}
}
