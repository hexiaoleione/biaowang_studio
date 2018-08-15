package com.hex.express.iwant.fragment;



import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownWindSpeedActivity;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.NewCenterActivity;
import com.hex.express.iwant.activities.NewExerciseActivity;
import com.hex.express.iwant.activities.UserCenterActivity;
import com.hex.express.iwant.adpter.MyPagerAdapter;
import com.hex.express.iwant.adpter.MyPagerAdapterse;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.PagerSlidingTabStrip;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newmain.TabFragmentHost;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class FragmentMessage extends Fragment {

	
	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	private TextView btnRight;
	private ImageView btnLeft;
	ViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {// 在前面执行

		super.onCreate(savedInstanceState);
		// 获取参数
		Bundle bundle = getArguments();
		if (null != bundle) {
			//
		}
	}
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view==null) {
			view = inflater.inflate(R.layout.fragment_message, null);
		}

		initView(view);

		return view;
	}

	private void initView(View view) {

		dm = getResources().getDisplayMetrics();
		 pager = (ViewPager) view.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		if (isLogin()) {
//		if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") || PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("")){
//			pager.setAdapter(new MyPagerAdapterse(getChildFragmentManager()));
//		}else {
//			pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
//		}
			pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
			tabs.setViewPager(pager);
			setTabsValue();
		}else {
			// 登录界面
			startActivity(new Intent(getActivity(), LoginWeixinActivity.class));
//			getActivity().finish();
			
		}
		
		
		btnRight=(TextView) view.findViewById(R.id.btnRight);
		btnLeft=(ImageView) view.findViewById(R.id.btnLeft);
      btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				MainTab activity=(MainTab) getActivity();
//				android.app.FragmentManager fm= activity.getFragmentManager();
//				android.app.FragmentTransaction ft=fm.beginTransaction();
//				 FragmentTransaction ft = fm.beginTransaction();
				if (isLogin()) {
					startActivity(new Intent(getActivity(), NewCenterActivity.class));
					
//					FragmentManager manager = getFragmentManager();  
//			        FragmentTransaction transation = manager.beginTransaction();  
//			        FragmentHome fra = new FragmentHome();  
//			        transation.replace(R.layout.tab_main_home, fra);  
//			        transation.addToBackStack(null);  
//			        transation.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
//			        transation.commit();  
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
		tabs.setIndicatorColor(Color.parseColor("#FF0000"));// #45c01a  43A44b FF730A
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
