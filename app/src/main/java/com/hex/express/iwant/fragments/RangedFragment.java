package com.hex.express.iwant.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.NearbyBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * 
 * @author huyichuan
 *  长途
 */
public class RangedFragment extends BaseItemFragment{
	public View rootView;
	//@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	//@Bind(R.id.nearby_listview)
	PullToRefreshListView ptrlv_card;
	//@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private NearbyBean bean;
	private List<NearbyBean.Data> mList;
		NearbyAdapter adapter;
	private ListView listView;

	//当前位置经纬度
	private double latitude;
	private double longitude;
	private String city;
	// 寄件人经纬度
	private double mylatitude;// 经度
	private double mylongitude;// 纬度
	private LocationClient client;
	private String cityCode;
	// 收件人经纬度
	private double receiver_longitude;
	private double receiver_latitude;
	private String receiver_citycode;
	private boolean receive;
	private String cityCodes;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
//		ButterKnife.bind(getActivity(), rootView);
		initView();
		initData();
		setOnClick();
		return rootView;
	}
	  private void initView() {
		// TODO Auto-generated method stub
		  cityCode=  PreferencesUtils.getString(getActivity(), PreferenceConstants.CITYCODE, cityCode);
		  view_load_fail=(LinearLayout) rootView.findViewById(R.id.view_load_fail);
		  ptrlv_card=(PullToRefreshListView) rootView.findViewById(R.id.nearby_listview);
		  view_null_message=(LinearLayout) rootView.findViewById(R.id.null_message);
		  Log.e("1111111111ss", PreferencesUtils.getString(getActivity(), PreferenceConstants.CITYCODE, cityCode));
		  }
		
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void initData() {
		listView = ptrlv_card.getRefreshableView();
//		getCityCode();
		getHttpRequst(true, false, 1, false);
		
	//	
	}


	private int num;

	//获取已接的镖件
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		String url = UrlMap.getfour(MCUrl.AREA, "userId",String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)),
				"cityCode", ""+cityCode,	"pageNo", String.valueOf(pageNo), "pageSize", String
						.valueOf(pageSize));
		
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("changtuuuu", new String(arg2));
				mList = new ArrayList<NearbyBean.Data>();
				try {
					bean = new Gson().fromJson(new String(arg2),
							NearbyBean.class);
					mList = bean.data;
					ToastUtil.shortToast(getActivity(), ""+bean.message);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new NearbyAdapter(getActivity(),
									mList);
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
					}
				else if (!isRefresh && isPull) {
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

			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttpRequst(false, true, 1, false);
			}

			@SuppressWarnings("rawtypes")
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				if(DownEscoreDartActivity.instance != null){}
//				DownEscoreDartActivity.instance.finish();
//				Intent intent = new Intent();
////				  intent.setClassName(com.hex.express.iwant.activities.this, DownEscoreDartActivity.class);
//					  intent.setClassName(getApplicationContext(), "DownEscoreDartActivity");
//					  List list = getPackageManager().queryIntentActivities(intent, 0);
//					  if (list.size() != 0) {   
//					       // 说明系统中存在这个activity
//						  DownEscoreDartActivity.instance.finish();
//					  }
				Log.e("111111111", ""+bean.getData().get(position).recId);
				Intent intent = new Intent(getActivity(), LogCompanyActivity.class);
//				intent.putExtra("recId", String.valueOf(data.recId));//int recId;//镖件主键
//				intent.putExtra("publishTime", data.publishTime);//String publishTime;//发布时间
				startActivity(intent);
//				getActivity().finish();//销毁掉承载它的FragmentActivity，使FragmentActivity在重新加载时能够重新从服务器请求本页面更新完的数据（在点击"取消订单"后）		
			}
		});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	  class NearbyAdapter extends BaseListAdapter {

		public NearbyAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new OwnerViewHolder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub
			return R.layout.information_item;
		}

		class OwnerViewHolder extends ViewHolder {

			public OwnerViewHolder(View itemView) {
				super(itemView);
			}
			@Bind(R.id.infor_juli)//距离
			TextView infor_juli;
			@Bind(R.id.infor_time)//时间
			TextView infor_time;
			@Bind(R.id.infor_wupin)//物品名字
			TextView infor_wupin;
			@Bind(R.id.infor_zhongliang)// 重量
			TextView infor_zhongliang;
			@Bind(R.id.infor_tiji)//体积
			TextView infor_tiji;
			@Bind(R.id.infor_songhuodizhi)//送货地址
			TextView infor_songhuodizhi;
			@Bind(R.id.infor_quhuo)//取货操作
			TextView infor_quhuo;
			@Bind(R.id.infor_daodadizhi)//达到地址
			TextView infor_daodadizhi;
			@Bind(R.id.infor_songhuo)//送货操作
			TextView infor_songhuo;
			@Bind(R.id.infor_baojia)//确认按钮
			Button infor_baojia;
			@Bind(R.id.infor_xin)//❤️数量
			TextView infor_xin;
			
			private NearbyBean bean;

			@Override
			public void setData(final int position) {
				super.setData(position);
				bean = new NearbyBean();
				bean.data = list;
				Log.e("BEAM", bean.data.get(position).toString());
//				System.out.println("111111111钱2 "+bean.data.size()+"   dd "+bean.getData().get(position).cargoName);
				infor_juli.setText("距离："+bean.getData().get(position).distance+"km");
				infor_time.setText(""+bean.getData().get(position).publishTime);
				infor_wupin.setText("物品："+bean.getData().get(position).cargoName);
				infor_zhongliang.setText("体重："+bean.getData().get(position).cargoWeight);
				infor_tiji.setText("体积："+bean.getData().get(position).cargoVolume);
				infor_songhuodizhi.setText(""+bean.getData().get(position).startPlace);
				infor_daodadizhi.setText(""+bean.getData().get(position).entPlace);
//				infor_xin.setText(""+bean.getData().get(position).evaluationScore);
				infor_baojia.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(), LogCompanyActivity.class);
						intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
						intent.putExtra("userId", bean.getData().get(position).userId);//发件人id
						intent.putExtra("cargoName", bean.getData().get(position).cargoName);//货物名称
						intent.putExtra("startPlace", bean.getData().get(position).startPlace);//物品起发地址
						intent.putExtra("entPlace", bean.getData().get(position).entPlace);//物品到达地址
						intent.putExtra("cargoWeight", bean.getData().get(position).cargoWeight);//物品重量
						intent.putExtra("cargoVolume", bean.getData().get(position).cargoVolume);//物品体积
						intent.putExtra("takeTime", bean.getData().get(position).takeTime);//取货时间
						intent.putExtra("arriveTime", bean.getData().get(position).arriveTime);//到达时间
						intent.putExtra("takeName", bean.getData().get(position).takeName);//收货人姓名
						intent.putExtra("takeMobile", bean.getData().get(position).takeMobile);//收货人地址
						intent.putExtra("remark", bean.getData().get(position).remark);//备注
						intent.putExtra("billCode", bean.getData().get(position).billCode);//单号
						intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
						intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
						intent.putExtra("sendMobile", bean.getData().get(position).sendMobile);//发货人手机号
						intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
						intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
						intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);//
						intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
						Log.e("11111transferMoney", ""+bean.getData().get(position).transferMoney);
//						intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
						
						startActivity(intent);
						
					}
				});
			}
		}
	}


}
