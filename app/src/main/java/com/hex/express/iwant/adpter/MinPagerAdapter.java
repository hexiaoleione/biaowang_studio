package com.hex.express.iwant.adpter;

import com.hex.express.iwant.minfragment.MinSubFragment1;
import com.hex.express.iwant.minfragment.MinSubFragment2;
import com.hex.express.iwant.minfragment.MinSubFragment3;
import com.hex.express.iwant.minfragment.MinSubFragment4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 *
 * @author huyichuan
 * 我发的订单
 */
//FragmentPagerAdapter FragmentStatePagerAdapter //锟斤拷锟斤拷锟斤拷FragmentPagerAdapter
public class MinPagerAdapter extends FragmentStatePagerAdapter {
	private MinSubFragment1 minSubFragment1;
	private MinSubFragment2 minSubFragment2;
	private MinSubFragment3 minSubFragment3;
	private MinSubFragment4 minSubFragment4;
	public MinPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	private final String[] titles = { "顺路送", "专程送",  "物流/冷链" };//"快递",物流/冷链
//	private final String[] titles = {  "专程送",  "物流/冷链" };//"快递",物流/冷链

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:


				if (null == minSubFragment2) {
					minSubFragment2 = new MinSubFragment2();
				}

				return minSubFragment2;

			case 1:

				if (null == minSubFragment1) {
					minSubFragment1 = new MinSubFragment1();
				}

				return minSubFragment1;
			case 2:

				if (null == minSubFragment4) {
					minSubFragment4 = new MinSubFragment4();
				}

				return minSubFragment4;
//			if (null == minSubFragment3) {
//				minSubFragment3 = new MinSubFragment3();
//			}
//
//			return minSubFragment3;
//		case 3:
//
//			if (null == minSubFragment4) {
//				minSubFragment4 = new MinSubFragment4();
//			}
//
//			return minSubFragment4;
			default:
				return null;
		}
	}

}
