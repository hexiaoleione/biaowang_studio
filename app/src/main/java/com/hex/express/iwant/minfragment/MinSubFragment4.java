package com.hex.express.iwant.minfragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogistiOfferActivity;
import com.hex.express.iwant.activities.LogistioffActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.bean.MyloginBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.InsureActivity;
import com.hex.express.iwant.newactivity.LogoffActivity;
import com.hex.express.iwant.newactivity.LogoffActivitybao;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MinSubFragment4 extends Fragment {
	public View rootView;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private MyloginBean bean;
	private List<Data> mList;
	private List<Data> mList2;
	MylisAdapter adapter;
	private ListView listView;
	  protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";  
	   LocalBroadcastManager broadcastManager ;
	   BroadcastReceiver bordcastReceiver;
	   public LoadingProgressDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_min4, container, false);//fragment_sendowner

		 ViewGroup p = (ViewGroup) rootView.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
 		ButterKnife.bind(this, rootView);
 		dialog=new LoadingProgressDialog(getActivity());
 		initData();
 		setOnClick();
 		getData();
		return rootView;
	}

	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	public void initData() {
		listView = ptrlv_card.getRefreshableView();
		mList = new ArrayList<Data>();
		mList2 = new ArrayList<Data>();
		getHttpRequst(true, false, 1, false);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
//		
//	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	private int num;
//	private MyReceiver receiver;
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		dialog.show();
		RequestParams params = new RequestParams();
		Log.e("11111111", UrlMap.getThreeUrl(MCUrl.LOGISTICSLIST, "userId", String
				.valueOf(PreferencesUtils.getInt(getActivity(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.LOGISTICSLIST, "userId", String
				.valueOf(PreferencesUtils.getInt(getActivity(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("11111111", new String(arg2));
				dialog.dismiss();
				mList2.clear();
//				try {
					bean = new Gson().fromJson(new String(arg2), MyloginBean.class);
					mList = bean.data;
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new MylisAdapter(getActivity(), mList);
							listView.setAdapter(adapter);
						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						ptrlv_card.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						if (mList.size() != 0 && mList != null) {
								adapter = new MylisAdapter(getActivity(), mList);
								listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								ptrlv_card.onRefreshComplete();
								view_null_message.setVisibility(View.GONE);
								ptrlv_card.setVisibility(View.VISIBLE);
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								ptrlv_card.setVisibility(View.GONE);
							}
					} else if (!isRefresh && isPull) {
						num = mList.size();
						adapter.addData(mList);
						adapter.notifyDataSetChanged();
						ptrlv_card.onRefreshComplete();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				view_load_fail.setVisibility(View.VISIBLE);
				ptrlv_card.setVisibility(View.GONE);
				dialog.dismiss();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttpRequst(false, true, 1, false);
				listView.deferNotifyDataSetChanged();
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		
		
		

	}
	public void getData() {
		// TODO Auto-generated method stub
      listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			final Data data = (Data) adapter.list.get(arg2 - 1);
//			intent.setClass(context, LogistiOfferActivity.class);//
//			Intent intent=new Intent();
//			intent.setClass(getActivity(), LogistioffActivity.class);//
////			if (!bean.getData().get(position).status.equals("2")) {
//				startActivity(intent);
			
			
		}
	});
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHttpRequst(true, false, 1, false);
	}
	

	public void onStart() {
        super.onStart();
  
    }
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    class MylisAdapter extends BaseListAdapter{

		public MylisAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new MylisViewHoder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub   mylogist_item  mylogist_itemes
			return R.layout.mylogist_itemes;  //mylogistical_item
		}
		class MylisViewHoder extends ViewHolder{

			public MylisViewHoder(View itemView) {
				super(itemView);
				// TODO Auto-generated constructor stub
			}

			@Bind(R.id.item_wupin)
			TextView item_wupin;
			@Bind(R.id.item_zhongliang)
			TextView item_zhongliang;
			@Bind(R.id.item_tiji)
			TextView item_tiji;
			@Bind(R.id.item_dizhi2)
			MarqueeTextView item_dizhi2;
			@Bind(R.id.item_name)
			TextView item_name;
			@Bind(R.id.item_phone)
			TextView item_phone;
			@Bind(R.id.item_time)
			TextView item_time;
			@Bind(R.id.item_xin)//
			TextView item_xin;
			@Bind(R.id.item_weizhifu)//未支付图片
			ImageView item_weizhifu;
			
			@Bind(R.id.button1)
			Button button1;
			@Bind(R.id.button2)
			Button button2;
			
			@Bind(R.id.butt_wan)//中间的
			Button butt_wan;
			@Bind(R.id.textView1)//  报价图标
			TextView textView1;
			@Bind(R.id.item_time2)
			TextView item_time2;
			@Bind(R.id.linearLayout1)
			LinearLayout linearLayout1;
			private MyloginBean bean;
   //status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
			//  7  9是分担保交易  8就选它了
			
			@Override
			public void setData(final int position) {
				// TODO Auto-generated method stub
				super.setData(position);
				bean = new MyloginBean();
				bean.data = list;
				item_wupin.setText("物品名称："+bean.data.get(position).cargoName);
//				if (bean.data.get(position).weight.equals("5")) {
//					item_zhongliang.setText("总重量：≤"+bean.data.get(position).weight+"公斤");
//				}else {
//					item_zhongliang.setText("总重量："+bean.data.get(position).weight+"公斤");
//				}
				item_zhongliang.setText("总重量："+bean.data.get(position).cargoWeight+"");
				
			
				item_tiji.setText("件数："+bean.data.get(position).cargoSize+"件");
//				item_dizhi1.setText(""+bean.getData().get(position).startPlace);
				item_dizhi2.setText(""+bean.data.get(position).entPlace);
				item_name.setText("收货人："+bean.data.get(position).takeName);
				item_phone.setText("联系方式："+bean.data.get(position).takeMobile);
				item_time.setText(""+bean.data.get(position).publishTime);
				item_xin.setText("+"+bean.data.get(position).quotationNumber);
				
				//中间的按钮
//				butt_wan.setVisibility(View.GONE);
//				item_weizhifu.setTag(tag);
				if (bean.data.get(position).status.equals("1")) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_1sx);
					button1.setVisibility(View.VISIBLE);
					button2.setVisibility(View.VISIBLE);
					butt_wan.setVisibility(View.GONE);
					item_xin.setVisibility(View.VISIBLE);
					textView1.setVisibility(View.VISIBLE);
				}else 
					if(bean.data.get(position).status.equals("6")) {
					item_weizhifu.setVisibility(View.GONE);
					item_weizhifu.setBackgroundResource(R.drawable.status_1x);
					button1.setVisibility(View.VISIBLE);
					button2.setVisibility(View.VISIBLE);
					butt_wan.setVisibility(View.GONE);
					item_xin.setVisibility(View.VISIBLE);
					textView1.setVisibility(View.VISIBLE);
				}else
				if (bean.data.get(position).status.equals("2")) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_2x);
					button1.setVisibility(View.GONE);
					button2.setVisibility(View.GONE);
					item_xin.setVisibility(View.GONE);
					textView1.setVisibility(View.GONE);
//					butt_wan.setBackgroundResource(R.drawable.danbao);
//					item_time.setVisibility(View.GONE);
					butt_wan.setVisibility(View.VISIBLE);
				}else
				if (bean.data.get(position).status.equals("8")) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_8x);
					button1.setBackgroundResource(R.drawable.xianxiajiaoyi);
					button2.setBackgroundResource(R.drawable.danbaojiaoyi);
					button1.setVisibility(View.VISIBLE);
					button2.setVisibility(View.VISIBLE);
					item_xin.setVisibility(View.GONE);
					butt_wan.setVisibility(View.GONE);
					textView1.setVisibility(View.INVISIBLE);
					
				}else
				if (bean.data.get(position).status.equals("4") ) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_7xs);
					button1.setVisibility(View.GONE);
					button2.setVisibility(View.GONE);
					item_xin.setVisibility(View.GONE);
					textView1.setVisibility(View.INVISIBLE);
//					item_time.setVisibility(View.GONE);
					butt_wan.setVisibility(View.VISIBLE);
//					item_time2.setVisibility(View.VISIBLE);
//					item_time2.setText(""+bean.getData().get(position).publishTime);
				}else
				if (bean.data.get(position).status.equals("5")) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_7xs);
					button1.setVisibility(View.GONE);
					button2.setVisibility(View.GONE);
					item_xin.setVisibility(View.GONE);
					textView1.setVisibility(View.INVISIBLE);
//					item_time.setVisibility(View.GONE);
					butt_wan.setVisibility(View.VISIBLE);
//					item_time2.setVisibility(View.VISIBLE);
//					item_time2.setText(""+bean.getData().get(position).publishTime);
				}else
				if (bean.data.get(position).status.equals("7")) {
					item_weizhifu.setVisibility(View.VISIBLE);
					item_weizhifu.setBackgroundResource(R.drawable.status_7x);
					button1.setVisibility(View.GONE);
					button2.setVisibility(View.GONE);
					item_xin.setVisibility(View.GONE);
					textView1.setVisibility(View.INVISIBLE);
					butt_wan.setBackgroundResource(R.drawable.danbaojiaoyi);
//					item_time.setVisibility(View.GONE);
					butt_wan.setVisibility(View.VISIBLE);
//					item_time2.setVisibility(View.VISIBLE);
//					item_time2.setText(""+bean.getData().get(position).publishTime);
				}else
				if (bean.data.get(position).status.equals("3")) {
//					button1.setBackgroundResource(R.drawable.search_bar_dui);
					item_weizhifu.setVisibility(View.VISIBLE);
					button1.setVisibility(View.GONE);
					button2.setVisibility(View.GONE);
					item_xin.setVisibility(View.GONE);
					textView1.setVisibility(View.INVISIBLE);
					butt_wan.setBackgroundResource(R.drawable.fangdajing);//search_barkan
					item_weizhifu.setBackgroundResource(R.drawable.status_3x);
					butt_wan.setVisibility(View.VISIBLE);
//					item_time2.setVisibility(View.VISIBLE);
//					item_time2.setText(""+bean.getData().get(position).publishTime);
				}else
				if (bean.data.get(position).status.equals("9")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_7x);
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
						item_xin.setVisibility(View.GONE);
						butt_wan.setVisibility(View.VISIBLE);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
					}
				else
					if (bean.data.get(position).status.equals("11")) {
						item_weizhifu.setVisibility(View.GONE);
						item_weizhifu.setBackgroundResource(R.drawable.yitoubao);
						
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
						item_xin.setVisibility(View.GONE);
						butt_wan.setVisibility(View.VISIBLE);
						butt_wan.setBackgroundResource(R.drawable.fukuan);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
					}else
						if (bean.data.get(position).status.equals("12")) {
							item_weizhifu.setVisibility(View.VISIBLE);
							item_weizhifu.setBackgroundResource(R.drawable.yitoubao);
							button1.setVisibility(View.GONE);
							button2.setVisibility(View.GONE);
							textView1.setVisibility(View.INVISIBLE);
							item_xin.setVisibility(View.GONE);
							butt_wan.setVisibility(View.GONE);
//							item_time2.setVisibility(View.VISIBLE);
//							item_time2.setText(""+bean.getData().get(position).publishTime);
						}
				else {
						butt_wan.setVisibility(View.GONE);
					}
//				if (bean.getData().get(position).status.equals("4")) {
////					button1.setBackgroundResource(R.drawable.search_bar_dui);
//					button1.setVisibility(View.GONE);
//					button2.setVisibility(View.GONE);
//					textView1.setVisibility(View.INVISIBLE);
//					item_time.setVisibility(View.GONE);
//					item_xin.setVisibility(View.INVISIBLE);
//					butt_wan.setBackgroundResource(R.drawable.search_barwancheng);
//					butt_wan.setVisibility(View.VISIBLE);
//					item_time2.setVisibility(View.VISIBLE);
//					item_time2.setText(""+bean.getData().get(position).publishTime);
//				}
				button1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
						// TODO Auto-generated method stub
						
						if (bean.data.get(position).status.equals("8")) {
							Builder ad = new Builder(getActivity());
							ad.setTitle("温馨提示");
							ad.setMessage("确定选择线下交易吗？");
							ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
										if (!bean.data.get(position).userToId.equals("")) {
											int  iid=bean.data.get(position).recId;
											String  userToId=bean.data.get(position).userToId;
//											addPostResult(iid);
//											ToastUtil.shortToast(context, "查看报价");;
//											getaddsurplus(iid,userToId);
										}
									
								}
							});
							ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
									
								}
							});
							ad.create().show();
						}else {
						
						Builder ad = new Builder(getActivity());
						ad.setTitle("温馨提示");
						ad.setMessage("确定要取消发布该货源");
						
						ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								int  iid=bean.data.get(position).recId;
								addPostResult(iid ,3);
//								ToastUtil.shortToast(context, "查看报价");;
							}
						});
						ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
								
							}
						});
						ad.create().show();
						
						}
					}
				});
				linearLayout1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						logistics/task/info
						Intent intent=new Intent();
						intent.putExtra("recId", bean.data.get(position).recId);
						if (bean.data.get(position).status.equals("11") || bean.data.get(position).status.equals("12")) {
							intent.setClass(getActivity(), LogoffActivitybao.class);
							startActivity(intent);
						}else {
							intent.setClass(getActivity(), LogoffActivity.class);
							startActivity(intent);
						}
						
					}
				});
				
				linearLayout1.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
				if (!bean.data.get(position).status.equals("3") && !bean.data.get(position).status.equals("4") && 
						!bean.data.get(position).status.equals("5") && !bean.data.get(position).status.equals("9")
						&& !bean.data.get(position).status.equals("12")) {
//						
					final int  resd;
					Builder builder = new Builder(getActivity());
//	                builder.setIcon(R.drawable.ic_launcher);
	                builder.setTitle("请选择取消原因");
	                final String[] sex = {"自愿取消", "物流公司要求取消"};
	                //    设置一个单项选择下拉框
	                /**
	                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合
	                 * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'第二个' 会被勾选上
	                 * 第三个参数给每一个单选项绑定一个监听器
	                 */
	                builder.setSingleChoiceItems(sex, 3, new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	int re=which+1;
//	                    	ToastUtil.shortToast(getActivity(), "which"+re);
	                    	int  iid=bean.data.get(position).recId;
						     addPostResult(iid,re);
	                    	dialog.dismiss();
	                    }
	                });
	                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    }
	                });
	                builder.show();
						}else {
						
				if(bean.data.get(position).status.equals("4") || bean.data.get(position).status.equals("5") || bean.data.get(position).status.equals("9") || bean.data.get(position).status.equals("7")){
					
					Builder ad = new Builder(getActivity());
					ad.setTitle("温馨提示");
					ad.setMessage("是否删除此条记录");
					ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							int  iid=bean.data.get(position).recId;
							addPostResult(iid ,3);
//							ToastUtil.shortToast(context, "查看报价");;
						}
					});
					ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
						}
					});
					ad.create().show();
				}
						}
						return true;
					}
				});
				butt_wan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent=new Intent();
						// TODO Auto-generated method stub
//						ToastUtil.shortToast(context, "查看报价人员");
//						intent.putExtra("cityCode", cityCode);
//						intent.putExtra("latitude", latitude);
						intent.putExtra("cargoName", bean.data.get(position).cargoName);
						intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
						intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
						intent.putExtra("startPlace", bean.data.get(position).startPlace);
						intent.putExtra("entPlace", bean.data.get(position).entPlace);
						intent.putExtra("takeName", bean.data.get(position).takeName);
						intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
						intent.putExtra("publishTime", bean.data.get(position).publishTime);
						intent.putExtra("takeCargo", bean.data.get(position).takeCargo);
						intent.putExtra("sendCargo", bean.data.get(position).sendCargo);
						intent.putExtra("remark", bean.data.get(position).remark);
						intent.putExtra("takeTime", bean.data.get(position).takeTime);
						intent.putExtra("arriveTime", bean.data.get(position).arriveTime);
						intent.putExtra("recId", bean.data.get(position).recId);
						intent.putExtra("billCode", bean.data.get(position).billCode);
						intent.putExtra("whether", bean.data.get(position).whether);
						intent.putExtra("premium", bean.data.get(position).premium);
						intent.putExtra("status", bean.data.get(position).status);
						intent.putExtra("cargoCost", bean.data.get(position).cargoCost);
						intent.putExtra("sendPerson", bean.data.get(position).sendPerson);
						intent.putExtra("sendPhone", bean.data.get(position).sendPhone);
						intent.putExtra("length", bean.data.get(position).length);
						intent.putExtra("wide", bean.data.get(position).wide);
						intent.putExtra("high", bean.data.get(position).high);
						intent.putExtra("weight", bean.data.get(position).cargoWeight);
						intent.putExtra("cargoSize", bean.data.get(position).cargoSize);
						
						intent.putExtra("carType", bean.data.get(position).carType);
						intent.putExtra("carName", bean.data.get(position).carName);
						intent.putExtra("tem", bean.data.get(position).tem);
						
						
//						intent.setClass(MainActivity.this, MyDownwindActivity.class);
//						intent.setClass(context, LogistiOfferActivity.class);//
//						Intent intent=new Intent();
						intent.setClass(context, LogistioffActivity.class);//
						if (bean.getData().get(position).status.equals("11")) {
							Builder ad = new Builder(getActivity());
							ad.setTitle("温馨提示");
							ad.setMessage("此单保费"+bean.data.get(position).insureCost+"元");
							
							ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									getaddsurplus(bean.data.get(position).recId,bean.data.get(position).insureCost);
								}
							});
							ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
//									Intent intent=new Intent();
//									intent.setClass(getActivity(), NewMainActivity.class);
//									startActivity(intent);
//									finish();
								}
							});
							ad.create().show();
							
						}else {
							context.startActivity(intent);
						}
						
					}
				});
				button2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent=new Intent();
						// TODO Auto-generated method stub
//						ToastUtil.shortToast(context, "查看报价人员");
//						intent.putExtra("cityCode", cityCode);
//						intent.putExtra("latitude", latitude);
						intent.putExtra("cargoName", bean.data.get(position).cargoName);
						intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
						intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
						intent.putExtra("startPlace", bean.data.get(position).startPlace);
						intent.putExtra("entPlace", bean.data.get(position).entPlace);
						intent.putExtra("takeName", bean.data.get(position).takeName);
						intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
						intent.putExtra("publishTime", bean.data.get(position).publishTime);
						intent.putExtra("takeCargo", bean.data.get(position).takeCargo);
						intent.putExtra("sendCargo", bean.data.get(position).sendCargo);
						intent.putExtra("remark", bean.data.get(position).remark);
						intent.putExtra("takeTime", bean.data.get(position).takeTime);
						intent.putExtra("arriveTime", bean.data.get(position).arriveTime);
						intent.putExtra("recId", bean.data.get(position).recId);
						intent.putExtra("billCode", bean.data.get(position).billCode);
						intent.putExtra("whether", bean.data.get(position).whether);
						intent.putExtra("premium", bean.data.get(position).premium);
						intent.putExtra("status", bean.data.get(position).status);
						intent.putExtra("sendPerson", bean.data.get(position).sendPerson);
						intent.putExtra("sendPhone", bean.data.get(position).sendPhone);
						intent.putExtra("cargoNumber", bean.data.get(position).cargoNumber);
						intent.putExtra("appontSpace", bean.data.get(position).appontSpace);
						
						intent.putExtra("length", bean.data.get(position).length);
						intent.putExtra("wide", bean.data.get(position).wide);
						intent.putExtra("high", bean.data.get(position).high);
						intent.putExtra("weight", bean.data.get(position).cargoWeight);
						intent.putExtra("cargoSize", bean.data.get(position).cargoSize);
						intent.putExtra("carType", bean.data.get(position).carType);
						intent.putExtra("carName", bean.data.get(position).carName);
						intent.putExtra("tem", bean.data.get(position).tem);
//						intent.putExtra("cargoCost", bean.data.get(position).cargoCost);//价值
//						intent.putExtra("insureCost", bean.data.get(position).insureCost);//保费
//						intent.setClass(MainActivity.this, MyDownwindActivity.class);
						if (bean.data.get(position).status.equals("2") || bean.data.get(position).status.equals("8")) {
							intent.setClass(context, LogistioffActivity.class);
							context.startActivity(intent);
						}else {
							intent.setClass(context, LogistiOfferActivity.class);//用
							context.startActivity(intent);
//							finish();
						}
						
					}
				});
		
			}

			}
		private void addPostResult(int  messageId, int reason) {
			JSONObject obj = new JSONObject();
			
//			try {
//				obj.put("messageId",String.valueOf(messageId));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			Log.e("查看数据", obj.toString());//doPostJson     dosPut
			Log.e("111", MCUrl.LOGISTICSLIST_DEKETE + String.valueOf(messageId));
//			AsyncHttpUtils.doPut(context,
//					MCUrl.LOGISTICSLIST_DEKETE +String.valueOf(messageId),
//					obj.toString(), null,new AsyncHttpResponseHandler() {
			RequestParams params = new RequestParams();
			AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.DeleteLog, "recId",messageId+"","reason",reason+""), null, null, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("11111111111 wwww   ", new String(arg2));
							LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//							Log.e("oppop", bean.data.toString());
							ToastUtil.shortToast(context, bean.getMessage());
							adapter.notifyDataSetChanged();
							getHttpRequst(false, true, 1, false);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						}
					});

		}
	}
    /**
	 * 余额支付的接口
	 */
	public void getaddsurplus(int recId, double insureCost )   {
//		if (billCode.equals("") || money.equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
//			return;
//		}
//		Log.e("1111   余额obj", obj.toString());
		dialog.show();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.payInsure, "recId", ""+recId, "",""+"", "", ""), null,
				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
						if (arg2 == null)
							return;
						Log.e("msg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(getActivity(), bean.getMessage());
//							Intent intent=new Intent();
//							intent.setClass(getActivity(), NewMainActivity.class);
//							startActivity(intent);
//							finish();
							adapter.notifyDataSetChanged();
							getHttpRequst(false, true, 1, false);
						}
						if (bean.getErrCode() !=0 ) {
							DialogUtils.createAlertDialogTwo(getActivity(), "", bean.getMessage(), 0, "去充值",
									"更换支付方式", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											startActivity(new Intent(getActivity(), RechargeActivity.class));
										}

									}).show();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				});
	}


}

