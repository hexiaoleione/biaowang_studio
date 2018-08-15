package com.hex.express.iwant.fragment;



import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.BduActivity;
import com.hex.express.iwant.activities.HjasActivity;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.NewCenterActivity;
import com.hex.express.iwant.activities.NewExerciseActivity;
import com.hex.express.iwant.activities.UserCenterActivity;
import com.hex.express.iwant.adpter.MinPagerAdapter;
import com.hex.express.iwant.adpter.MinPagerAdapterk;
import com.hex.express.iwant.adpter.MinPagerAdapterkw;
import com.hex.express.iwant.adpter.MinPagerAdapterse;
import com.hex.express.iwant.adpter.MinPagerAdaptersek;
import com.hex.express.iwant.adpter.MinPagerAdaptersew;
import com.hex.express.iwant.adpter.MinPagerAdaptersewk;
import com.hex.express.iwant.adpter.MinPagerAdapterw;
import com.hex.express.iwant.adpter.MyPagerAdapter;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.PagerSlidingTabStrip;
import com.hex.express.iwant.fragment.FragmentHome.myAdapter;
import com.hex.express.iwant.fragment.FragmentHome.myAdapter2;
import com.hex.express.iwant.fragment.FragmentHome.myAdapter21;
import com.hex.express.iwant.fragment.FragmentHome.myAdapter3;
import com.hex.express.iwant.newmain.BaidudingActivity;
import com.hex.express.iwant.newmain.MoreAddressActivity;
import com.hex.express.iwant.utils.PreferencesUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentMy extends Fragment {

	
	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	
	private LinearLayout my_xuanze;
	private TextView my_order,my_jiebiao,my_fabiao,btnRight,my_orderse;
	private ImageView btnLeft;
	ViewPager pager;
	boolean isfres=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {// 在前面执行

		super.onCreate(savedInstanceState);
		// 获取参数
		Bundle bundle = getArguments();
		if (null != bundle) {
			//
		}
	}
	View view ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (view==null) {
			 view = inflater.inflate(R.layout.fragment_my, null);

		}
		
		initView(view);

		return view;
	}

	private void initView(View view) {

		dm = getResources().getDisplayMetrics();
		 pager = (ViewPager) view.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		my_xuanze=(LinearLayout) view.findViewById(R.id.my_xuanze);
		my_order=(TextView) view.findViewById(R.id.my_order);
		my_orderse=(TextView) view.findViewById(R.id.my_orderse);
		my_jiebiao=(TextView) view.findViewById(R.id.my_jiebiao);
		my_fabiao=(TextView) view.findViewById(R.id.my_fabiao);
		btnRight=(TextView) view.findViewById(R.id.btnRight);
		btnLeft=(ImageView) view.findViewById(R.id.btnLeft);
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
//			  startActivity(new Intent(getActivity(), MoreAddressActivity.class));
//			  startActivity(new Intent(getActivity(), HjasActivity.class));
				startActivity(new Intent(getActivity(), NewExerciseActivity.class));
				
			}else {
				// 登录界面
				startActivity(new Intent(getActivity(), LoginWeixinActivity.class));
				
			}
			}
		});
//		my_order.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				my_xuanze.setVisibility(View.VISIBLE);
//			}
//		});
		my_order.setTextColor(getResources().getColor(R.color.white));
		my_orderse.setTextColor(getResources().getColor(R.color.graywhite));
		my_order.setTextSize(16);
		my_orderse.setTextSize(13);
		my_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isfres=true;
//				my_xuanze.setVisibility(View.GONE);
//				pager.setAdapter(new MinPagerAdapter(getChildFragmentManager()));
//				my_order.setText("我发的镖");
				my_order.setTextColor(getResources().getColor(R.color.white));
				my_orderse.setTextColor(getResources().getColor(R.color.graywhite));
				my_order.setTextSize(16);
				my_orderse.setTextSize(13);
				initView();
			}
		});
		my_orderse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isfres=false;
//				my_xuanze.setVisibility(View.GONE);
//				my_order.setText("我接的镖");
				my_order.setTextColor(getResources().getColor(R.color.graywhite));
				my_orderse.setTextColor(getResources().getColor(R.color.white));
				my_order.setTextSize(13);
				my_orderse.setTextSize(16);
				initView();
			}
		});
		
		 if (isLogin()) {
			 initView();
	       setTabsValue();
		 }else {
				startActivity(new Intent(getActivity(), LoginWeixinActivity.class));

		}
			

	}
  private void initView(){
	 
	  if (isfres) {
		  //2判断是不是快递员
		  if (PreferencesUtils.getString(getActivity(), PreferenceConstants.USERTYPE).equals("2")){
			  //0判断是不是物流用户
				if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("1") 
						|| PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("2")) {
					pager.setAdapter(new MinPagerAdapterkw(getChildFragmentManager()));
				}else {
					 pager.setAdapter(new MinPagerAdapterk(getChildFragmentManager()));
				}
			}//0判断是不是物流用户
			else {
				if(!PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") && !"".equals(PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID))){
					
					 pager.setAdapter(new MinPagerAdapterw(getChildFragmentManager()));
		         }else {
		        	 pager.setAdapter(new MinPagerAdapter(getChildFragmentManager()));
				}
			}
		     //我发的订单
//			 pager.setAdapter(new MinPagerAdapter(getChildFragmentManager()));
		}else {
			if (PreferencesUtils.getString(getActivity(), PreferenceConstants.USERTYPE).equals("2")){
				  //0判断是不是物流用户
					if (!PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0")) {
//						pager.setAdapter(new MinPagerAdaptersewk(getChildFragmentManager()));
						pager.setAdapter(new MinPagerAdapterse(getChildFragmentManager()));
						
					}else {
						 pager.setAdapter(new MinPagerAdaptersek(getChildFragmentManager()));
					}
				}//0判断是不是物流用户
				else {
					if(!PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") && !"".equals(PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID))){
						 pager.setAdapter(new MinPagerAdaptersew(getChildFragmentManager()));
			         }else {
			        	 pager.setAdapter(new MinPagerAdaptersewk(getChildFragmentManager()));
					}
				}
			//我接的订单
//			pager.setAdapter(new MinPagerAdapterse(getChildFragmentManager()));
		}
		  tabs.setViewPager(pager);
	  
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
//		tabs.setSelectedTextColor(Color.parseColor("#FF730A"));// #45c01a
		// 取消点击Tab时的背景色
		  tabs.setTabBackground(0);
		 tabs.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	
	public boolean isLogin(){
		return PreferencesUtils.getBoolean(getActivity(), PreferenceConstants.ISLOGIN);
		}
   
	
}
