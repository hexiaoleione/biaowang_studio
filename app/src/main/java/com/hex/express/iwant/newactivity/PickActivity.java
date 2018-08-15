package com.hex.express.iwant.newactivity;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.MyDownwindActivity;
import com.hex.express.iwant.adpter.MinPagerAdapter;
import com.hex.express.iwant.adpter.MyPagerAdapter;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.PagerSlidingTabStrip;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.newsmain.LeftFragment;
import com.hex.express.iwant.slidingview.SlidingFragmentActivity;
import com.hex.express.iwant.slidingview.SlidingMenu;
import com.hex.express.iwant.utils.PreferencesUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
/**
 * 
 * @author huyichuan
 *  接镖
 */
public class PickActivity extends SlidingFragmentActivity {
	
	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	private TextView btnRight,my_order;
	private ImageView btnLeft;
	ViewPager pager;
	
	private Fragment mContent;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick);
		ButterKnife.bind(PickActivity.this);
		initSlidingMenu(savedInstanceState);
		dm = getResources().getDisplayMetrics();
		 pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		my_order=(TextView) findViewById(R.id.my_order);
		my_order.setText("我的发单");
		initView();
	}

	public void initView() {
		// TODO Auto-generated method stub
//		if (isLogin()) {
//			if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") || PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("")){
//				pager.setAdapter(new MyPagerAdapterse(getChildFragmentManager()));
//			}else {
//				pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
//			}
				pager.setAdapter(new MinPagerAdapter(this.getSupportFragmentManager()));
				tabs.setViewPager(pager);
				setTabsValue();
//			}else {
//				// 登录界面
//				startActivity(new Intent(AnswerActivity.this, LoginWeixinActivity.class));
//				
//			}
				btnLeft.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
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
		tabs.setSelectedTextColor(Color.parseColor("#FF0000"));// #45c01a
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	   tabs.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	public boolean isLogin(){
		return PreferencesUtils.getBoolean(PickActivity.this, PreferenceConstants.ISLOGIN);
		}


	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new HomSubFragment1();
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//TOUCHMODE_NONE TOUCHMODE_FULLSCREEN
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}



}
