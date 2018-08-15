package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.NearbyBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
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
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * 公司找货 新界面
 */
public class LogistcaInforseActivity extends BaseActivity{
	
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btnRight)
	ImageView btnRight;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.nearby_listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 20;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private NearbyBean bean;
	private List<NearbyBean.Data> mList;
	private List<NearbyBean.Data> mList2;
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
	private String area;
	private String code;
	private String citys;
	private String entPlaceCityCode;
	private int type=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informse);
		ButterKnife.bind(LogistcaInforseActivity.this);
//		initView();
//		initData();
//		setOnClick();
		
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
	}
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		initView();
//		initData();
//		setOnClick();
//	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		  cityCode=  PreferencesUtils.getString(LogistcaInforseActivity.this, PreferenceConstants.CITYCODE, cityCode);
//		  view_load_fail=(LinearLayout) .findViewById(R.id.view_load_fail);
//		  ptrlv_card=(PullToRefreshListView) .findViewById(R.id.nearby_listview);
//		  view_null_message=(LinearLayout) .findViewById(R.id.null_message);
		  Log.e("1111111111ss", PreferencesUtils.getString(LogistcaInforseActivity.this, PreferenceConstants.CITYCODE, cityCode));
		  }

	@Override
	public void initData() {
		listView = ptrlv_card.getRefreshableView();
//		getCityCode();
		String url = UrlMap.getfour(MCUrl.NEARBY, "userId",String.valueOf(PreferencesUtils.getInt(LogistcaInforseActivity.this, PreferenceConstants.UID)),
				"cityCode", ""+cityCode,	"pageNo", String.valueOf(pageNo), "pageSize", String
						.valueOf(pageSize));
		Log.e("111   ",url );
		mList2= new ArrayList<NearbyBean.Data>();
		mList = new ArrayList<NearbyBean.Data>();
		getHttpRequst(true, false, 1, false);
		
	//	
}


private int num;

//获取已接的镖件
@SuppressWarnings("unchecked")
private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
		int pageNo, final boolean isPull) {
	String url = UrlMap.getfsix(MCUrl.NEARBY, "userId",String.valueOf(PreferencesUtils.getInt(LogistcaInforseActivity.this, PreferenceConstants.UID)),
			"startPlaceCityCode", ""+cityCode,"entPlaceCityCode", ""+entPlaceCityCode,"type", ""+type,"pageNo", String.valueOf(pageNo), "pageSize", String
					.valueOf(pageSize));
	Log.e("fjinjeurl", url);
	AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			Log.e("fjinjjjj", new String(arg2));
		
			try {
				bean = new Gson().fromJson(new String(arg2),
						NearbyBean.class);
				mList = bean.data;
//				ToastUtil.shortToast(LogistcaInforseActivity.this, ""+bean.message);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if (isFirst) {
				if (mList.size() != 0 && mList != null) {
					if (adapter == null) {
						mList2.addAll(mList);
						adapter = new NearbyAdapter(LogistcaInforseActivity.this,
								mList2);
						listView.setAdapter(adapter);
					}
				} else {
					view_null_message.setVisibility(View.VISIBLE);
					ptrlv_card.setVisibility(View.GONE);
				}
			} else {
				if (isRefresh && !isPull) {
//					if (mList.size() != 0 && mList != null) {
					mList2.clear();
					mList2.addAll(mList);
					adapter.setData(mList);
//					adapter.notifyDataSetInvalidated();//这个方法会重新刷新listview的界面
					adapter.notifyDataSetChanged();//这个至更改界面
					ptrlv_card.onRefreshComplete();
					}
//				}
			else if (!isRefresh && isPull) {
//				Log.e("1111112", "33333");
					num = mList.size();
//					mList2.clear();
//					mList2.addAll(mList);
//					adapter.setData(mList2);
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
//			finish();
			startActivityForResult(new Intent(LogistcaInforseActivity.this,ProvCityTownAcrivity.class),1);
		}
	});
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
//				Log.e("dd", num + "");
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
//			if(DownEscoreDartActivity.instance != null){}
//			DownEscoreDartActivity.instance.finish();
//			Intent intent = new Intent();
////			  intent.setClassName(com.hex.express.iwant.activities.this, DownEscoreDartActivity.class);
//				  intent.setClassName(getApplicationContext(), "DownEscoreDartActivity");
//				  List list = getPackageManager().queryIntentActivities(intent, 0);
//				  if (list.size() != 0) {   
//				       // 说明系统中存在这个activity
//					  DownEscoreDartActivity.instance.finish();
//				  }
//			Log.e("111111111", ""+bean.getData().get(position).recId);
			Intent intent = new Intent(LogistcaInforseActivity.this, LogCompanyActivity.class);
//			intent.putExtra("recId", String.valueOf(data.recId));//int recId;//镖件主键
//			intent.putExtra("publishTime", data.publishTime);//String publishTime;//发布时间
			startActivity(intent);
//			getActivity().finish();//销毁掉承载它的FragmentActivity，使FragmentActivity在重新加载时能够重新从服务器请求本页面更新完的数据（在点击"取消订单"后）		
		}
	});

}

@Override
public void getData() {
	// TODO Auto-generated method stub

}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("1111122222crequestCode  ", ""+requestCode);
		if(requestCode==1&&resultCode==RESULT_OK){
			if(data.getStringExtra("code")==null)
				return;
//			area=data.getStringExtra("name");
			entPlaceCityCode=data.getStringExtra("code");
//			citys=data.getStringExtra("city");
			type=data.getIntExtra("type", 1);
//			getHttpRequst(true, false, 1, false);
			getHttpRequst(false, true, 1, false);
//			getHttpRequst(true, false, 1, false);
			
//			Log.e("1111122222code  ", entPlaceCityCode);
//			Log.e("111112222type  ", ""+type);
//			et_region.setText(arg2.getStringExtra("name"));
//			iv_loc.setVisibility(View.VISIBLE);
		}
		if (requestCode==10) {
			if (resultCode == Activity.RESULT_OK) {
			getHttpRequst(false, true, 1, false);
			}
		}

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
		return R.layout.informat_item;//informat_item  information_item
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
//		@Bind(R.id.infor_jiazhi)//物品价值
//		TextView infor_jiazhi;
		
		@Bind(R.id.infor_zhongliang)// 重量
		TextView infor_zhongliang;
		@Bind(R.id.infor_tiji)//体积
		TextView infor_tiji;
		@Bind(R.id.infor_quhuo)//取货操作
		TextView infor_quhuo;
		@Bind(R.id.infor_daodadizhi)//达到地址
		TextView infor_daodadizhi;
		@Bind(R.id.infor_songhuo)//送货操作
		TextView infor_songhuo;
		@Bind(R.id.infor_baojia)//确认按钮
		Button infor_baojia;
		
		private NearbyBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new NearbyBean();
			bean.data = list;
//			Log.e("BEAM", bean.data.get(position).toString());
//			System.out.println("111111111钱2 "+bean.data.size()+"   dd "+bean.getData().get(position).cargoName);
			infor_juli.setText("距离："+bean.getData().get(position).distance+"km");
			infor_time.setText(""+bean.getData().get(position).publishTime);
			infor_wupin.setText("物品："+bean.getData().get(position).cargoName);
			infor_zhongliang.setText("重量："+bean.getData().get(position).cargoWeight);
			infor_tiji.setText("体积："+bean.getData().get(position).cargoVolume+"方");
//			String	ct=bean.getData().get(position).cargoCost;
//			String[] strs=ct.split("\\.");
//			infor_jiazhi.setText("价值："+strs[0]+"元");
//			infor_jiazhi.setText("价值："+bean.getData().get(position).cargoCost+"元");
			infor_daodadizhi.setText(""+bean.getData().get(position).entPlace);
			if (bean.getData().get(position).takeCargo==false) {
				infor_quhuo.setText("上门取货:否");
			}else {
				infor_quhuo.setText("上门取货:是");
			}
			if (bean.getData().get(position).sendCargo==false) {
				infor_songhuo.setText("送货上门:否");
			}else {
				infor_songhuo.setText("送货上门:是");
			}//bean.getData().get(position).status.equals("1")
			if (bean.getData().get(position).ifQuotion) {//getIntent().getBooleanExtra("ifQuotion", false)
				infor_baojia.setText("修改报价");
			}
			infor_baojia.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(LogistcaInforseActivity.this, LogCompanyActivity.class);
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
					intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否送取货
					intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
					intent.putExtra("sendMobile", bean.getData().get(position).sendMobile);//发货人手机号
					intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
					intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
					intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);// 总钱
					intent.putExtra("takeCargoMoney", bean.getData().get(position).takeCargoMoney);// 取货费
					intent.putExtra("sendCargoMoney", bean.getData().get(position).sendCargoMoney);// 送货上门
					intent.putExtra("cargoTotal", bean.getData().get(position).cargoTotal);// 运费
					
//					intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);// 货物价值
					intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
					intent.putExtra("status", bean.getData().get(position).status);//
					intent.putExtra("yardAddress", bean.getData().get(position).yardAddress);//
					intent.putExtra("ifQuotion",bean.getData().get(position).ifQuotion);// 是否报价
					Log.e("11111", ""+bean.getData().get(position).recId);
//					intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
					startActivityForResult(intent, 10);
//					startActivity(intent);
					
				}
			});
		}
	}
}


}

