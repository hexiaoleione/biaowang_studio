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
import com.hex.express.iwant.activities.DepotFreigActivity;
import com.hex.express.iwant.activities.LogCompanyDeliveryActivity;
import com.hex.express.iwant.activities.LogCompanyFreActivity;
import com.hex.express.iwant.activities.LogistiOfferActivity;
import com.hex.express.iwant.activities.LogisticaPayActivity;
import com.hex.express.iwant.activities.LogisticalFreightActivity;
import com.hex.express.iwant.activities.LogistioffActivity;
import com.hex.express.iwant.activities.NotPermittedActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.adpter.MyPagerAdapterse;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.FreigBean;
import com.hex.express.iwant.bean.FreigBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MinFragment4 extends Fragment {
	public View rootView;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.weijiao)
	TextView weijiao;
	@Bind(R.id.huochang)
	TextView huochang;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private FreigBean bean;
	private List<FreigBean.Data> mList;
	private List<FreigBean.Data> mList2;
	MylositaAdapter adapter;
	private ListView listView;
	  protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";  
	   LocalBroadcastManager broadcastManager ;
	   BroadcastReceiver bordcastReceiver;
	   public LoadingProgressDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_my4, container, false);//fragment_sendowner

		 ViewGroup p = (ViewGroup) rootView.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
 		ButterKnife.bind(this, rootView);
 		dialog=new LoadingProgressDialog(getActivity());
 		initData();
 		setOnClick();
 		if (isLogin()) {
			
		if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") || PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("")){
			weijiao.setVisibility(View.GONE);
			huochang.setVisibility(View.GONE);
		}
 		}
		return rootView;
	}
	
  
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public boolean isLogin(){
		return PreferencesUtils.getBoolean(getActivity(), PreferenceConstants.ISLOGIN);
		}

	public void initData() {
		listView = ptrlv_card.getRefreshableView();
		mList = new ArrayList<FreigBean.Data>();
		mList2 = new ArrayList<FreigBean.Data>();
		getHttpRequst(true, false, 1, false);
		weijiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getActivity(),  NotPermittedActivity.class);
				startActivity(intent);
			}
		});
		huochang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getActivity(), DepotFreigActivity.class);
				startActivity(intent);
			}
		});
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
		RequestParams params = new RequestParams();
		dialog.show();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.TASKFIND, "userId", String
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
					bean = new Gson().fromJson(new String(arg2), FreigBean.class);
					mList = bean.data;
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new MylositaAdapter(getActivity(), mList);
							listView.setAdapter(adapter);
							view_null_message.setVisibility(View.GONE);
							ptrlv_card.setVisibility(View.VISIBLE);
						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						ptrlv_card.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						if (mList.size() != 0 && mList != null) {
								adapter = new MylositaAdapter(getActivity(), mList);
								listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								ptrlv_card.onRefreshComplete();
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
    /**
	 * 内部类 adapter
	 */
	
	class MylositaAdapter extends BaseListAdapter{

		public MylositaAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new MysViewHolder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub
			return R.layout.mylosit_itemse;//mylosita_itemse// mylosit_itemse
		}

		class MysViewHolder extends ViewHolder {

			public MysViewHolder(View itemView) {
				super(itemView);
			}
			@Bind(R.id.infor_title_juli)//距离、、、、未付款
			TextView infor_title_juli;
			@Bind(R.id.infor_title_time)//时间
			TextView infor_title_time;
			@Bind(R.id.infor_title_wupin)//物品名字
			TextView infor_title_wupin;
			@Bind(R.id.infor_title_zhongliang)// 重量
			TextView infor_title_zhongliang;
			@Bind(R.id.infor_title_tiji)//体积
			TextView infor_title_tiji;
			@Bind(R.id.infor_title_jiazhi)//价值
			TextView infor_title_jiazhi;
			@Bind(R.id.infor_title_songhuodizhi)//送货地址
			MarqueeTextView infor_title_songhuodizhi;
			@Bind(R.id.infor_title_quhuo)//取货操作
			TextView infor_title_quhuo;
//			@Bind(R.id.infor_title_quhuo_time)//取货时间
//			TextView infor_title_quhuo_time;
			
			@Bind(R.id.infor_title_daodadizhi)//达到地址
			MarqueeTextView infor_title_daodadizhi;
			@Bind(R.id.infor_title_songhuo)//送货操作
			TextView infor_title_songhuo;
			@Bind(R.id.infor_title_songhuo_time)//送货时间
			TextView infor_title_songhuo_time;
			@Bind(R.id.btn_zb)//是否中标
			Button btn_zb;
			@Bind(R.id.btn_zbdian)//中标后面的点
			Button btn_zbdian;
			@Bind(R.id.btn_fh)//已发货
			Button btn_fh;
			@Bind(R.id.btn_fhdian)//已发货后面的点
			Button btn_fhdian;
			@Bind(R.id.btn_dd)//已完成
			Button btn_dd;
			@Bind(R.id.infor_title)//
			LinearLayout infor_title;
			
			
			@Bind(R.id.coldcar)//
			TextView coldcar;
			
			@Bind(R.id.tem)//
			TextView tem;
			@Bind(R.id.recoldcar)//冷链
			RelativeLayout	recoldcar;
			@Bind(R.id.resonghuo)//正常
			RelativeLayout	resonghuo;
			
//		
//status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
			private FreigBean bean;

			@Override
			public void setData(final int position) {
				super.setData(position);
				bean = new FreigBean();
				bean.data = list;
				Log.e("BEAM", bean.data.get(position).toString());
			
				infor_title_time.setText("要求到达时间:"+bean.getData().get(position).arriveTime);
				infor_title_wupin.setText("物品："+bean.getData().get(position).cargoName);
				infor_title_zhongliang.setText("重量："+bean.getData().get(position).cargoWeight);
				infor_title_tiji.setText("总体积："+bean.getData().get(position).cargoVolume);
//				infor_title_jiazhi.setText("价值："+bean.getData().get(position).cargoCost+"元");
				infor_title_songhuodizhi.setText(bean.getData().get(position).startPlace);
//				infor_title_quhuo_time.setText(bean.getData().get(position).takeTime);
				infor_title_songhuo_time.setText(bean.getData().get(position).arriveTime);
//				infor_title_quhuo.setText(bean.getData().get(position).entPlace);
				
				if ("cold".equals(bean.getData().get(position).carType)) {
					recoldcar.setVisibility(View.VISIBLE);	
					resonghuo.setVisibility(View.GONE);
					tem.setText("温度要求："+bean.getData().get(position).tem);
				}else {
					recoldcar.setVisibility(View.GONE);
					resonghuo.setVisibility(View.VISIBLE);
				}
				if ("cold".equals(bean.getData().get(position).carType)) {
					coldcar.setText("需求："+bean.getData().get(position).carName);
				}else {
					coldcar.setVisibility(View.GONE);
				}
				if (bean.getData().get(position).takeCargo) {
					infor_title_quhuo.setText("物流公司上门取货");
				}else {
					infor_title_quhuo.setText("发货人送到货场");
////					infor_title_quhuo.setVisibility(View.GONE);
//					infor_title_quhuo.setTextColor(R.color.graywhite);
				}
				if (bean.getData().get(position).sendCargo) {
					infor_title_songhuo.setText("物流公司送货上门");
				}else {
					infor_title_songhuo.setText("收件人自提");
//					infor_title_songhuo.setTextColor(R.color.graywhite);
//					infor_title_songhuo.setVisibility(View.GONE);
				}
//				infor_title_daodadizhi.setText(bean.getData().get(position).entPlace);
				infor_title_daodadizhi.setText(bean.getData().get(position).endPlaceName);
				
//				infor_title_songhuo.setText(bean.getData().get(position).distance);
	//status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
//				if (bean.getData().get(position).status.equals("3")) {
//				}
				if (bean.getData().get(position).status==9 || bean.getData().get(position).status==7) {
					infor_title_juli.setVisibility(View.VISIBLE);
					infor_title_juli.setText("非担保交易");
//					btn_bj..setVisibility(View.INVISIBLE);
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_fh.setVisibility(View.INVISIBLE);
					btn_dd.setVisibility(View.INVISIBLE);
					btn_zbdian.setVisibility(View.INVISIBLE);
					btn_fhdian.setVisibility(View.INVISIBLE);
				}else {
					btn_fh.setVisibility(View.VISIBLE);
					btn_dd.setVisibility(View.VISIBLE);
					btn_zbdian.setVisibility(View.VISIBLE);
					btn_fhdian.setVisibility(View.VISIBLE);
				}
				if (bean.getData().get(position).status==2 ||
						bean.getData().get(position).status==3 ||
						bean.getData().get(position).status==4 ||
						bean.getData().get(position).status==6) {
					infor_title_juli.setVisibility(View.VISIBLE);
					infor_title_juli.setText("担保交易");
				}else {
					infor_title_juli.setVisibility(View.GONE);
				}
					
				if (bean.getData().get(position).status==2) {
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_dd.setBackgroundResource(R.drawable.newsongda);
					btn_fh.setBackgroundResource(R.drawable.newquhuo);
					btn_fhdian.setBackgroundResource(R.drawable.grayseach_13880);
				}else 
				if (bean.getData().get(position).status==3) {   
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_fh.setBackgroundResource(R.drawable.newyiquhuo);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_fhdian.setBackgroundResource(R.drawable.greensearchimg_13880);
//					btn_dd.setBackgroundResource(R.drawable.logiswdd);
				}else 
				if (bean.getData().get(position).status==4) {
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_fh.setBackgroundResource(R.drawable.newyiquhuo);
					btn_dd.setBackgroundResource(R.drawable.newyisongda);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_fhdian.setBackgroundResource(R.drawable.greensearchimg_13880);
				}else 
				if (bean.getData().get(position).status==8) {
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880); 
					btn_fh.setBackgroundResource(R.drawable.newyiquhuo);
					btn_dd.setBackgroundResource(R.drawable.newyisongda);
					btn_fhdian.setBackgroundResource(R.drawable.grayseach_13880);
				}
				
				//报价详情  infor_title  btn_zb
				infor_title.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent();
						intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
//						intent.setClass(context,	LogCompanyFreActivity.class);
//						intent.setClass(context,	LogistioffActivity.class);
//						startActivity(intent);
						
					}
				});
				
				//收货界面
				btn_fh.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (bean.getData().get(position).status!=3) {
							Intent intent=new Intent();
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
							intent.putExtra("status", bean.getData().get(position).status);//
							intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
							intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否要送货
							intent.putExtra("sendName", bean.getData().get(position).sendPerson);//发货人
							intent.putExtra("sendMobile", bean.getData().get(position).sendPhone);//发货人手机号
							intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
							intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
							intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);//
							intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
							intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);//价值
							intent.putExtra("distance", bean.getData().get(position).distance);//j距离
							intent.putExtra("firstPicture", bean.getData().get(position).firstPicture);//照片一
							intent.putExtra("secondPicture", bean.getData().get(position).secondPicture);//照片二
							intent.putExtra("thirdPicture", bean.getData().get(position).thirdPicture);//照片三
							intent.putExtra("carNumImg", bean.getData().get(position).carNumImg);//车牌号
							intent.putExtra("way", "1");//c照片
							intent.putExtra("length",  bean.getData().get(position).length);//长
							intent.putExtra("wide",  bean.getData().get(position).wide);//宽
							intent.putExtra("high",  bean.getData().get(position).high);//高
							intent.putExtra("cargoSize",  bean.getData().get(position).cargoSize);//件数
							intent.putExtra("appontSpace", bean.getData().get(position).appontSpace);//
							
							intent.putExtra("carType",  bean.getData().get(position).carType);//件数
							intent.putExtra("carName", bean.getData().get(position).carName);//
							intent.putExtra("tem", bean.getData().get(position).tem);//
							
							intent.setClass(context,LogCompanyDeliveryActivity.class);
							 startActivityForResult(intent,10);	
							}else {
						Intent intent=new Intent();
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
						intent.putExtra("status", bean.getData().get(position).status);//
						intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
						intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否要送货
						intent.putExtra("sendName", bean.getData().get(position).sendPerson);//发货人
						intent.putExtra("sendMobile", bean.getData().get(position).sendPhone);//发货人手机号
						intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
						intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
						intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);//
						intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
						intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);//价值
						intent.putExtra("distance", bean.getData().get(position).distance);//j距离
						intent.putExtra("firstPicture", bean.getData().get(position).firstPicture);//照片一
						intent.putExtra("secondPicture", bean.getData().get(position).secondPicture);//照片二
						intent.putExtra("thirdPicture", bean.getData().get(position).thirdPicture);//照片三
						intent.putExtra("carNumImg", bean.getData().get(position).carNumImg);//车牌号
						intent.putExtra("length",  bean.getData().get(position).length);//长
						intent.putExtra("wide",  bean.getData().get(position).wide);//宽
						intent.putExtra("high",  bean.getData().get(position).high);//高
						intent.putExtra("cargoSize",  bean.getData().get(position).cargoSize);//件数
						intent.putExtra("carType",  bean.getData().get(position).carType);//件数
						intent.putExtra("carName", bean.getData().get(position).carName);//
						intent.putExtra("tem", bean.getData().get(position).tem);//
						intent.setClass(context,LogCompanyDeliveryActivity.class);
						intent.putExtra("way", "2");//c照片
						intent.putExtra("appontSpace", bean.getData().get(position).appontSpace);//
//						 startActivity(intent);
						 startActivityForResult(intent,10);							}
					}
				});
				
				btn_dd.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (bean.getData().get(position).status==3) {
							Builder ad = new Builder(getActivity());
							ad.setTitle("温馨提示");
							ad.setMessage("是否确认货物已送达");
							//setPositiveButton
							ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
//									getMessages(bean.getData().get(position).recId);
									// 输入支付密码:LogisticaPayActivity   CompanyDeliveryPayActivity
									startActivity(new Intent(getActivity(), LogisticaPayActivity.class)
											.putExtra("recId",bean.getData().get(position).recId));
								}
							});
							ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
								}
							});
							ad.create().show();
						}else {
							if (bean.getData().get(position).status==4) {
								ToastUtil.shortToast(context, "您已完成");
								return;
							  }
							ToastUtil.shortToast(context, "您到达目的后才能使用");
						
						}
						
					}
				});
			}

		}
		}
	public void getMessages(final int recid){

		RequestParams params = new RequestParams();
		Log.e("数据", UrlMap.getUrl(MCUrl.ARRIVE, "recId", recid+""));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ARRIVE, "recId", recid+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111json", new String(arg2));
						dialog.dismiss();
						BaseBean		beans = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (beans.getErrCode()==0) {
							ToastUtil.shortToast(getActivity(), bean.message);
							// 输入支付密码:LogisticaPayActivity   CompanyDeliveryPayActivity
							startActivity(new Intent(getActivity(), LogisticaPayActivity.class)
									.putExtra("recId",recid));
						}else {
							ToastUtil.shortToast(getActivity(), bean.message);
						}
						

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(getActivity(), "网络请求加载失败");

					}
				});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Log.e("11111111  ", resultCode+" ssss   "+data);
		if (requestCode==10) {
			if (resultCode == Activity.RESULT_OK) {
				getHttpRequst(false, true, 1, false);
			}
		}
	}

}

