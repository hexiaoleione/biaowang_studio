package com.hex.express.iwant.activities;

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
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.bean.MyloginBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
/**
 * 
 * @author huyichuan
 *  海南我的物流界面
 */

public class HaiLogisticalFreightActivity extends BaseActivity {

	@Bind(R.id.mylis_listview)
	PullToRefreshListView mylis_listview;
	@Bind(R.id.mylis_show)
	TitleBarView mylis_show;
	
	 ListView listView;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 20;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<MyloginBean.Data> mList;
	private List<MyloginBean.Data> mList2;
	private HaiMylisAdapter adapter;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_mylogistical);
		ButterKnife.bind(HaiLogisticalFreightActivity.this);
		initView();
		initData();
		setOnClick();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}
//	public void charse(){
//		ToastUtil.shortToast(MylogisticakActivity.this, "11111111");
//		adapter.notifyDataSetChanged();
//		getHttprequst(false, true, 1, false);
//		
//	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mylis_show.setTitleText("我的物流");
		listView = mylis_listview.getRefreshableView();
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<MyloginBean.Data>();
		mList2=new ArrayList<MyloginBean.Data>();
		dialog.show();
		getHttprequst(true, false, 1, false);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHttprequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("11111", UrlMap.getThreeUrl(MCUrl.FindHn, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.FindHn, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						mList2.clear();
						MyloginBean bean = new Gson().fromJson(new String(arg2),
								MyloginBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new HaiMylisAdapter(HaiLogisticalFreightActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								mylis_listview.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
//								mList.clear();
								mList = bean.data;
									if (mList.size() != 0 && mList != null) {
											mylis_listview.onRefreshComplete();
											adapter = new HaiMylisAdapter(HaiLogisticalFreightActivity.this, mList);
											listView.setAdapter(adapter);
											
									} else {
										view_null_message.setVisibility(View.VISIBLE);
										mylis_listview.setVisibility(View.GONE);
									}
//								mList2.clear();
//								mList2.addAll(mList);
//								adapter.setData(mList2);
//								adapter.notifyDataSetInvalidated();//这个方法会重新刷新listview的界面
//								adapter.notifyDataSetChanged();
//								mylis_listview.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								num = mList.size();
//								mList2.clear();
//								mList2.addAll(mList);
								adapter.setData(mList);
								adapter.notifyDataSetChanged();
								mylis_listview.onRefreshComplete();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						view_null_message.setVisibility(View.VISIBLE);
						mylis_listview.setVisibility(View.GONE);
						ToastUtil.shortToast(HaiLogisticalFreightActivity.this, "网络请求加载失败");

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		mylis_listview.setMode(Mode.BOTH);
		mylis_listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttprequst(false, true, 1, false);
//				getHttprequst(true, false, 1, false);
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
	}

	private boolean gift;
	//长按删除事件
	@Override
	public void getData() {
		
		gift = getIntent().getBooleanExtra("gift", false);
	}

	 class HaiMylisAdapter extends BaseListAdapter{

			public HaiMylisAdapter(Context context, List list) {
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
				return R.layout.hainanmylogist_itemes;  //mylogistical_item
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
//				@Bind(R.id.item_dizhi1)
//				TextView item_dizhi1;
//				@Bind(R.id.item_quhuo)
//				TextView item_quhuo;
				@Bind(R.id.item_dizhi2)
				MarqueeTextView item_dizhi2;
//				@Bind(R.id.item_songhuo)
//				TextView item_songhuo;
				@Bind(R.id.item_name)
				TextView item_name;
				@Bind(R.id.item_phone)
				TextView item_phone;
				@Bind(R.id.item_time)
				TextView item_time;
				@Bind(R.id.item_xin)//
				TextView item_xin;
				@Bind(R.id.item_pi)//
				TextView item_pi;
				
				@Bind(R.id.item_weizhifu)//未支付图片
				ImageView item_weizhifu;
				
				@Bind(R.id.button1)
				Button button1;
				@Bind(R.id.button2)
				Button button2;
				
				@Bind(R.id.butt_wan)//中间的
				Button butt_wan;
				@Bind(R.id.item_wupinliebie)// 
				TextView item_wupinliebie;
				@Bind(R.id.item_carnuber)// 
				TextView item_carnuber;
				@Bind(R.id.item_baohao)// 
				TextView item_baohao;
				
				@Bind(R.id.item_baoxian)
				TextView item_baoxian;
				@Bind(R.id.item_wupinjiaz)// 
				TextView item_wupinjiaz;
				@Bind(R.id.item_wupinbaofei)
				TextView item_wupinbaofei;
				@Bind(R.id.linearLayout1)
				LinearLayout linearLayout1;
				private MyloginBean bean;
       //status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
				//  7  9是分担保交易
				@Override
				public void setData(final int position) {
					// TODO Auto-generated method stub
					super.setData(position);
					bean = new MyloginBean();
					bean.data = list;
					item_wupin.setText("物品："+bean.data.get(position).cargoName);
					if (bean.data.get(position).cargoWeight.equals("吨") ||bean.data.get(position).cargoWeight.equals("千克") ) {
						item_zhongliang.setVisibility(View.GONE);
					}else {
						item_zhongliang.setText("重量："+bean.data.get(position).cargoWeight);
					}
					if ("".equals(bean.data.get(position).cargoVolume)) {
						item_tiji.setVisibility(View.INVISIBLE);
					}else {
						item_tiji.setText("货物件数："+bean.data.get(position).cargoVolume+"件");
					}
					if (bean.data.get(position).status.equals("6")) {
						item_pi.setText("审核中");
						item_pi.setVisibility(View.VISIBLE);
					}else {
						item_pi.setVisibility(View.GONE);
					}
					
//					item_dizhi1.setText(""+bean.getData().get(position).startPlace);
					item_dizhi2.setText(""+bean.data.get(position).entPlace);
					item_name.setText("收货人："+bean.data.get(position).takeName);
					item_phone.setText("手机号："+bean.data.get(position).takeMobile);
					item_time.setText(""+bean.data.get(position).publishTime);
					item_wupinliebie.setText("物品类别："+bean.data.get(position).category);
					item_baoxian.setText("保险种类："+bean.data.get(position).insurance);
					item_wupinjiaz.setText("保额："+bean.data.get(position).cargoCost+"元");
					item_carnuber.setText("车牌号："+bean.data.get(position).carNumImg);
					item_baohao.setText("保单号："+bean.data.get(position).remark);
					if (PreferencesUtils.getString(context, PreferenceConstants.AgreementType).equals("2")) {
						item_wupinbaofei.setVisibility(View.GONE);
					}else {
						item_wupinbaofei.setVisibility(View.GONE);
						item_wupinbaofei.setText("保费："+bean.data.get(position).insureCost+"元");
					}
					
//					item_weizhifu.setTag(tag);
					
					linearLayout1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent=new Intent();
							intent.setClass(context, HainanLogistioffActivity.class);//
							intent.putExtra("cargoName", bean.data.get(position).cargoName);
							intent.putExtra("recId", bean.data.get(position).recId);
							intent.putExtra("sendName", bean.data.get(position).sendName);
							intent.putExtra("sendMobile", bean.data.get(position).sendMobile);
							intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
							intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
							intent.putExtra("entPlace", bean.data.get(position).entPlace);
							intent.putExtra("startPlace", bean.data.get(position).startPlace);
							intent.putExtra("takeName", bean.data.get(position).takeName);
							intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
							intent.putExtra("category", bean.data.get(position).category);
							intent.putExtra("insurance", bean.data.get(position).insurance);
							intent.putExtra("cargoCost", bean.data.get(position).cargoCost);
							intent.putExtra("insureCost", bean.data.get(position).insureCost);
							intent.putExtra("sendName", bean.data.get(position).sendName);
							intent.putExtra("sendMobile", bean.data.get(position).sendMobile);
							intent.putExtra("carNumImg", bean.data.get(position).carNumImg);
							intent.putExtra("pdfURL", bean.data.get(position).pdfURL);
							intent.putExtra("remark", bean.data.get(position).remark);
							context.startActivity(intent);
							
						}
					});
				}

				}
		}
	

}
