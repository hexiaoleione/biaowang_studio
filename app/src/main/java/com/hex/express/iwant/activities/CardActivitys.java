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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.Intents;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
public class CardActivitys extends BaseActivity {
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
	private CardAdaptersd adapter;
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
				startActivity(new Intent(CardActivitys.this, HModelActivity.class));
			}
		});
	}

	@Override
	public void initData() {
		mList = new ArrayList<CardBean.Data>();
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
						bean = new Gson().fromJson(new String(arg2),
								CardBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new CardAdaptersd(
											CardActivitys.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								ptrlv_card.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
								adapter.setData(mList);
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
						ToastUtil.shortToast(CardActivitys.this, "网络请求加载失败");

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
//		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		ptrlv_card.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			      	Data data = (Data) adapter.mData.get(arg2-1);
//				if (getIntent().getIntExtra("type", 0)==data.couponId) {
					if (data.money != 0 && data.userCouponId != 0) {
						intent.setClass(CardActivitys.this, DownWindPayActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("strResult", ""+data.userCouponId);
						bundle.putString("money", ""+data.money);
						intent.putExtra("bundle2", bundle);
						setResult(RESULT_OK, intent);
//						list.add(arg2,data.userCouponId);
						Log.e("111111111   ", ""+data.userCouponId);
						finish();
						adapter.notifyDataSetChanged();
				}
					 adapter.setSelectItem(arg2-1);  
					 adapter.notifyDataSetInvalidated();
//				}else {
//					ToastUtil.shortToast(CardActivitys.this, "此现金卷类型不支持");
//				}
					
//					
				

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
	class CardAdaptersd extends BaseAdapter {
		private List<Data> mData;// 存储的EditText值
		public Context mContext;
		private Map<String, Object> map;
		private Handler mhandler;
		CardBean cardBean;
		 int mSelect = 0; 
		public CardAdaptersd(Context context, List<Data> mlist) {
			this.mData = mlist;
			this.mContext = context;
		}

		public void addData(List<Data> mList) {
			// TODO Auto-generated method stub
			this.mData.addAll(mList);
		}

		public void setData(List<Data> mList) {
			for (int i = 0; i < mList.size(); i++) {
				this.mData.remove(mList.size() - i - 1);
			}
			this.mData.addAll(0, mList);
		}
		public void changeSelected(int positon){ //刷新方法
		     if(positon != mSelect){
		      mSelect = positon;
		     notifyDataSetChanged();
		     }
		    }
		@Override
		public int getCount() {
			if (mData == null || mData.size() == 0) {
				return 0;
			}
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(mContext);
				convertView = inflater.inflate(R.layout.item_activity_card, null);
//				holder.btn_use = (TextView) convertView.findViewById(R.id.btn_use);
				holder.moneyView = (TextView) convertView.findViewById(R.id.txt_cardmoney);
				holder.nameView = (TextView) convertView.findViewById(R.id.txt_cardexpname);
				holder.conditionView=(TextView) convertView.findViewById(R.id.txt_cardConditions);
				holder.timeView = (TextView) convertView.findViewById(R.id.txt_cardDuration);
				holder.textView = (TextView) convertView.findViewById(R.id.btn_usecard);
				holder.card_layout = (RelativeLayout) convertView.findViewById(R.id.card_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (mData != null && mData.size() > 0) {
				final Data bean = mData.get(position);
//				holder.tv_INCREASING_NO.setText(position + 1 + "");
				cardBean = new CardBean();
				
//				cardBean.data = list;
				if (mData.get(position).ifExpired) {
//					holder.textView.setText("已过期");
//					holder.textView.setTextColor(mContext.getResources()
//							.getColor(R.color.white));
//					holder.textView.setBackgroundColor(mContext.getResources().getColor(
//							R.color.gray));
//					if (mData.get(position).couponId==1) {
//						holder.btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//					}
//					if (mData.get(position).couponId==2) {
//						holder.btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//					}
//					if (mData.get(position).couponId==3) {
//						holder.btn_use.setBackgroundResource(R.drawable.xxianshimg_68);
//					}
//					if (mData.get(position).couponId==4) {
//						holder.btn_use.setBackgroundResource(R.drawable.xshubiaoimg_68);
//					}
					String s = String.valueOf(mData.get(position).money);
					String newD = s.substring(0,s.indexOf("."));
					holder.moneyView.setText( newD);
					holder.moneyView.setTextColor(mContext.getResources()
							.getColor(R.color.gray));
//					holder.txt_cardname.setTextColor(mContext.getResources().getColor(
//							R.color.gray));
					holder.timeView.setText("截止日期："
							+ mData.get(position).coupontime);
					holder.timeView.setTextColor(mContext.getResources().getColor(
							R.color.gray));
					holder.nameView.setText(mData.get(position).conditions);
//					holder.nameView.setTextColor(mContext.getResources().getColor(
//							R.color.gray));couponName
					holder.conditionView.setText(
							mData.get(position).couponName);
//					holder.conditionView.setTextColor(mContext.getResources().getColor(
//							R.color.gray));
					
				} else {
					if (mData.get(position).ifUsed) {
//						holder.textView.setText("已使用");
//						holder.textView.setTextColor(mContext.getResources()
//								.getColor(R.color.white));
//						holder.textView.setBackgroundColor(mContext.getResources().getColor(
//								R.color.gray));
//						if (mData.get(position).couponId==1) {
//							holder.btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//						}
//						if (mData.get(position).couponId==2) {
//							holder.btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//						}
//						if (mData.get(position).couponId==3) {
//							holder.btn_use.setBackgroundResource(R.drawable.xxianshimg_68);
//						}
//						if (mData.get(position).couponId==4) {
//							holder.btn_use.setBackgroundResource(R.drawable.xshubiaoimg_68);
//						}
						String s = String.valueOf(mData.get(position).money);
						String newD = s.substring(0,s.indexOf("."));
						holder.moneyView.setText( newD);
						holder.moneyView.setTextColor(mContext.getResources()
								.getColor(R.color.gray));
						holder.txt_cardname.setTextColor(mContext.getResources().getColor(
								R.color.gray));
						holder.timeView.setText("截止日期："
								+ mData.get(position).coupontime);
						holder.timeView.setTextColor(mContext.getResources().getColor(
								R.color.gray));
						holder.nameView.setText(mData.get(position).conditions);
//						holder.nameView.setTextColor(mContext.getResources().getColor(
//								R.color.gray));couponName
						holder.conditionView.setText(
								mData.get(position).couponName);
//						holder.conditionView.setTextColor(mContext.getResources().getColor(
//								R.color.gray));
						

					} else {
//						holder.textView.setText("未使用");
//						holder.moneyView.setTextColor(mContext.getResources().getColor(
//								R.color.orange1));
//						if (mData.get(position).couponId==1) {
//							holder.btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//						}
//						if (mData.get(position).couponId==2) {
//							holder.btn_use.setBackgroundResource(R.drawable.xfeijiimg_68);
//						}
//						if (mData.get(position).couponId==3) {
//							holder.btn_use.setBackgroundResource(R.drawable.xxianshimg_68);
//						}
//						if (mData.get(position).couponId==4) {
//							holder.btn_use.setBackgroundResource(R.drawable.xshubiaoimg_68);
//						}
						String s = String.valueOf(mData.get(position).money);
						String money = s.substring(0,s.indexOf("."));
						holder.moneyView.setText( money);
						holder.timeView.setText("截止日期："
								+mData.get(position).coupontime);
						holder.timeView.setTextColor(mContext.getResources().getColor(
								R.color.gray));
						holder.nameView.setText(mData.get(position).conditions);
//						holder.nameView.setTextColor(mContext.getResources().getColor(
//								R.color.gray));couponName
						holder.conditionView.setText(
								mData.get(position).couponName);
//						holder.conditionView.setTextColor(mContext.getResources().getColor(
//								R.color.gray));

					}

				}

			}
			  if (position == selectItem) {  
	                convertView.setBackgroundColor(Color.WHITE);   //选中项背景
	            }   
	            else {  
	                convertView.setBackgroundColor(Color.TRANSPARENT);  //其他项背景
	            } 
			return convertView;
		}
		public  void setSelectItem(int selectItem) {  
            this.selectItem = selectItem;  
       }  
       private int  selectItem=-1; 

		public class ViewHolder {
			TextView txt_cardname;
			TextView moneyView;
			TextView nameView;
			TextView conditionView;
			TextView timeView;
			TextView textView;
//			TextView btn_use;
			RelativeLayout card_layout;
		}
	}

}
