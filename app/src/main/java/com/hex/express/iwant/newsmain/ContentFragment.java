package com.hex.express.iwant.newsmain;

import java.util.ArrayList;
import java.util.List;

import com.framework.base.BaseFragment;
import com.hex.express.iwant.R;
import com.hex.express.iwant.slidingview.Slidingmu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 主页
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class ContentFragment extends BaseFragment implements OnPageChangeListener {
	private ViewPager mViewPager;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();

//	private AdvanceFragment mAdvanceFragment;

	private static final int tabNum = 3;
	private static int selectedPage = 0;
	private static int preSelectedPage = 0;

	
	private static int scrollState;
	private static final int SCROLL_STATE_PRESS = 1;
	private static final int SCROLL_STATE_UP = 2;
	private static final int SCROLL_STATE_END = 0;
	private float unitWidth;
	private float currentPositionPix;
	private TextView tvTab0, tvTab1, tvTab2;
	private boolean isClick = false;
	private TextView ivCursor;
	
	private static Slidingmu mSlidingMenu;
	
	
	private List<TextView> mTextViewList = new ArrayList<TextView>();
	public static ContentFragment newInstance(Slidingmu mMenu) {
		ContentFragment mAtationFragment = new ContentFragment();
		mSlidingMenu = mMenu;
		return mAtationFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = LayoutInflater.from(null).inflate(R.layout.activity_main, null);
		mViewPager.setCurrentItem(1);
		return view;
	}

	
	
	

	public void onPageSelected(int position) {
//		System.out.println("onPageSelected");
		// System.out.println("onPageSelected------position" + position);
		selectedPage = position;
		if (myPageChangeListener != null)
			myPageChangeListener.onPageSelected(position);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//		System.out.println("onPageScrolled");
		if (!isClick) {
			if (positionOffsetPixels != 0) {
				if (scrollState == SCROLL_STATE_PRESS) {
					if (selectedPage == position) {
						ivCursor.setTranslationX(currentPositionPix + positionOffsetPixels / tabNum);
					} else {
						ivCursor.setTranslationX(currentPositionPix - (unitWidth - positionOffsetPixels / tabNum));
					}
				} else if (scrollState == SCROLL_STATE_UP) {
					if (preSelectedPage == position) {
						ivCursor.setTranslationX(currentPositionPix + positionOffsetPixels / tabNum);
					} else {
						ivCursor.setTranslationX(currentPositionPix - (unitWidth - positionOffsetPixels / tabNum));
					}
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
//		System.out.println("onPageScrollStateChanged");
		// System.out.println("onPageScrollStateChanged------state" + state);
		if (!isClick) {
			currentPositionPix = selectedPage * unitWidth;
			scrollState = state;
			preSelectedPage = selectedPage;
			for (int i = 0; i < mTextViewList.size(); i++) {
				if (preSelectedPage == i) {
					mTextViewList.get(preSelectedPage).setTextColor(Color.rgb(67, 142, 225));
				}else{
					mTextViewList.get(i).setTextColor(Color.rgb(170, 173, 173));
				}
			}
		}
	}
	
	
	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {

		myPageChangeListener = l;

	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}
	
	public boolean isFirst() {
		if (mViewPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mViewPager.getCurrentItem() == mFragmentList.size() - 1)
			return true;
		else
			return false;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("MainActivity");
	}
	
}
