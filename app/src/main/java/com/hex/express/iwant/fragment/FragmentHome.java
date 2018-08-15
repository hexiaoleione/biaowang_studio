package com.hex.express.iwant.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.AddressActivity;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.NewCenterActivity;
import com.hex.express.iwant.activities.NewExerciseActivity;
import com.hex.express.iwant.activities.UserCenterActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.ForResultNestedCompatFragment;
import com.hex.express.iwant.customer.MyViewpager;
import com.hex.express.iwant.customer.PagerSlidingTabStrip;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.homfragment.HomSubFragment2;
import com.hex.express.iwant.homfragment.HomSubFragment3;
import com.hex.express.iwant.homfragment.HomSubFragment4;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

public class FragmentHome extends Fragment {

	
	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;
//	TabPageIndicator  indicator;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	int first ;
	private ArrayList<Fragment> list;
	private ArrayList<Fragment> list2;
	private ArrayList<Fragment> list3;
	private ArrayList<Fragment> list4;
	private LinearLayout  xianshi,shunfeng,kuaidi,wuliul;
	private ImageView xianshi1,shunfeng1,kuaidi1,wuliul1;
	MyViewpager pager;
	private ImageView btnLeft,btnRightse;
	TextView btnRight;
	RegisterBean bean;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {// 在前面执行

		super.onCreate(savedInstanceState);
		// 获取参数
		Bundle bundle = getArguments();
		if (null != bundle) {
			//
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		
		initView(view);
//    ceshi();
		return view;
	}
	private void initviews(){
		 if ( bean.getData().size()>0) {
			 if (bean.data.get(0).userType.equals("2")){
					if (bean.data.get(0).wlid.equals("1") 
							|| bean.data.get(0).wlid.equals("2")) {
						myAdapter3 mAdapter3 = new myAdapter3(getChildFragmentManager());
						 wuliul.setVisibility(View.GONE);
						 kuaidi.setVisibility(View.GONE);
					     pager.setAdapter(mAdapter3);
					}else {
						myAdapter2 mAdapter2 = new myAdapter2(getChildFragmentManager());
						 kuaidi.setVisibility(View.GONE);
					     pager.setAdapter(mAdapter2);
					}
				}
				else{
					 if(!bean.data.get(0).wlid.equals("0") && !"".equals(bean.data.get(0).wlid)){
							myAdapter21 mAdapter21 = new myAdapter21(getChildFragmentManager());
							 wuliul.setVisibility(View.GONE);
							 pager.setAdapter(mAdapter21);
			         }else {
						myAdapter mAdapter = new myAdapter(getChildFragmentManager());
						pager.setAdapter(mAdapter);

					}
				}
		}else
	if (PreferencesUtils.getString(getActivity(), PreferenceConstants.USERTYPE).equals("2")){
		if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("1") 
				|| PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("2")) {
			myAdapter3 mAdapter3 = new myAdapter3(getChildFragmentManager());
			 wuliul.setVisibility(View.GONE);
			 kuaidi.setVisibility(View.GONE);
		     pager.setAdapter(mAdapter3);
		}else {
			myAdapter2 mAdapter2 = new myAdapter2(getChildFragmentManager());
			 kuaidi.setVisibility(View.GONE);
		     pager.setAdapter(mAdapter2);
		}
	}
	else{
		 if(!PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") && !"".equals(PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID))){
				myAdapter21 mAdapter21 = new myAdapter21(getChildFragmentManager());
				 wuliul.setVisibility(View.GONE);
				 pager.setAdapter(mAdapter21);
         }else {
			myAdapter mAdapter = new myAdapter(getChildFragmentManager());
			pager.setAdapter(mAdapter);

		}
	}
		
	}
	
	private void initView(View view) {

		dm = getResources().getDisplayMetrics();
		list = new ArrayList<Fragment>();
		list2= new ArrayList<Fragment>();
		list3= new ArrayList<Fragment>();
		list4= new ArrayList<Fragment>();
		HomSubFragment1 ownerFragment1 = new HomSubFragment1();//限时
		HomSubFragment2 escortFragment2 = new HomSubFragment2();//顺风
		HomSubFragment3 ownerFragment3 = new HomSubFragment3();//快递
		HomSubFragment4 escortFragment4 = new HomSubFragment4();//物流
		list.add(escortFragment2);
		list.add(ownerFragment1);
//		list.add(ownerFragment3);
		list.add(escortFragment4);
		
		list2.add(escortFragment2);
		list2.add(ownerFragment1);
		list2.add(escortFragment4);
		
		list3.add(escortFragment2);
		list3.add(ownerFragment1);
//		list3.add(ownerFragment3);
		
		list4.add(escortFragment2);
		list4.add(ownerFragment1);
		
//		myAdapter mAdapter = new myAdapter(getSupportFragmentManager());/
//		viewPager.setAdapter(mAdapter);/
		btnLeft=(ImageView) view.findViewById(R.id.btnLeft);
		btnRightse=(ImageView) view.findViewById(R.id.btnRightse);
		btnRight=(TextView) view.findViewById(R.id.btnRight);
		xianshi=(LinearLayout) view.findViewById(R.id.xianshi);
		shunfeng=(LinearLayout) view.findViewById(R.id.shunfeng);
		kuaidi=(LinearLayout) view.findViewById(R.id.kuaidi);
		wuliul=(LinearLayout) view.findViewById(R.id.wuliu);
		xianshi1=(ImageView) view.findViewById(R.id.xianshi1);
		shunfeng1=(ImageView) view.findViewById(R.id.shunfeng1);
		kuaidi1=(ImageView) view.findViewById(R.id.kuaidi1);
		wuliul1=(ImageView) view.findViewById(R.id.wuliu1);
		 pager = (MyViewpager) view.findViewById(R.id.pager);
		 
		 kuaidi.setVisibility(View.GONE);
		 if(isLogin()){
		//判断是否是快递员
			 getrequstBalance();
			 getMessageStutas();
		}else {
				myAdapter mAdapter = new myAdapter(getChildFragmentManager());
				pager.setAdapter(mAdapter);
		}
//		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
//		pager.setAdapter(new HomPagerAdapter(getChildFragmentManager()));
//		tabs.setViewPager(pager);
//		setTabsValue();
		 btnRightse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), MessageActivity.class));
			}
		});
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isLogin()) {
					startActivity(new Intent(getActivity(), NewCenterActivity.class));
				}else {
					// 登录界面
					startActivity(new Intent(getActivity(), LoginWeixinActivity.class));
//					getActivity().finish();
				}
				
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		  if (isLogin()) {
				startActivity(new Intent(getActivity(), NewExerciseActivity.class));
			}else {
				// 登录界面
				startActivity(new Intent(getActivity(), LoginWeixinActivity.class));
//				getActivity().finish();
			}
			}
		});
		
		xianshi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(1);
//				if (first==0) {
			
				    xianshi1.setBackgroundResource(R.drawable.xuanon);
					shunfeng1.setBackgroundResource(R.drawable.xuanoff);
					kuaidi1.setBackgroundResource(R.drawable.xuanoff);
					wuliul1.setBackgroundResource(R.drawable.xuanoff);
//				}
				first=0;
			}
		});
			shunfeng.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(0);
				first=1;
//				if (first==1) {
				xianshi1.setBackgroundResource(R.drawable.xuanoff);
				shunfeng1.setBackgroundResource(R.drawable.xuanon);
				kuaidi1.setBackgroundResource(R.drawable.xuanoff);
				wuliul1.setBackgroundResource(R.drawable.xuanoff);
//				}
			}
		});
			kuaidi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					first=2;
					pager.setCurrentItem(2);
//					if (first==2) {
					xianshi1.setBackgroundResource(R.drawable.xuanoff);
					shunfeng1.setBackgroundResource(R.drawable.xuanoff);
					kuaidi1.setBackgroundResource(R.drawable.xuanon);
					wuliul1.setBackgroundResource(R.drawable.xuanoff);
						
//					}
				}
			});
			wuliul.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					first=3;
//					if (first==3) {
					xianshi1.setBackgroundResource(R.drawable.xuanoff);
					shunfeng1.setBackgroundResource(R.drawable.xuanoff);
					kuaidi1.setBackgroundResource(R.drawable.xuanoff);
					wuliul1.setBackgroundResource(R.drawable.xuanon);
//					}
					pager.setCurrentItem(3);
					
				}
			});
	}
	
	
	class myAdapter extends FragmentPagerAdapter {

		public myAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

	}
	class myAdapter2 extends FragmentPagerAdapter {

		public myAdapter2(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list2.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list2.size();
		}

	}
	class myAdapter21 extends FragmentPagerAdapter {

		public myAdapter21(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list3.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list3.size();
		}

	}
	class myAdapter3 extends FragmentPagerAdapter {

		public myAdapter3(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list4.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list4.size();
		}

	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// tabs.setDividerColor(Color.BLACK);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));// 4
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 15, dm)); // 16
		// 设置Tab Indicator的颜色 底部下划线颜色
		tabs.setIndicatorColor(Color.parseColor("#FF730A"));// #45c01a  43A44b FF730A
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#FF730A"));// #45c01a
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
		tabs.setBackgroundColor(Color.WHITE);
		
	}


	public boolean isLogin(){
		return PreferencesUtils.getBoolean(getActivity(), PreferenceConstants.ISLOGIN);
		}
	/**
	 * 获取钱包余额
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("11111json", "" + new String(arg2));
						 bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getErrCode()==0) {
							initviews();
							PreferencesUtils.putString(getActivity(), PreferenceConstants.CITYCODE,bean.getData().get(0).cityCode);
						 PreferencesUtils.putString(getActivity(), PreferenceConstants.USERTYPE,bean.data.get(0).userType);
						 PreferencesUtils.putInt(getActivity(), PreferenceConstants.UID,bean.data.get(0).userId);
//						 PreferencesUtils.putString(getActivity(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
						 PreferencesUtils.putString(getActivity(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
						}		
					}

				});
	}
	/**
	 * 信息是否有未读的状态
	 */
	private void getMessageStutas() {
		RequestParams params = new RequestParams();
		Log.e("mdgread", UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("message", "" + new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() > 0) {
							btnRightse.setBackgroundResource(R.drawable.info_show);
						} else if (bean.getErrCode() == -2) {
							btnRightse.setBackgroundResource(R.drawable.mess);
						}
					}

				});
	}
}
