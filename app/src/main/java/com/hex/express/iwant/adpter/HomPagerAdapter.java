 package com.hex.express.iwant.adpter;


import com.hex.express.iwant.R;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.homfragment.HomSubFragment2;
import com.hex.express.iwant.homfragment.HomSubFragment3;
import com.hex.express.iwant.homfragment.HomSubFragment4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//FragmentPagerAdapter FragmentStatePagerAdapter //锟斤拷锟斤拷锟斤拷FragmentPagerAdapter
public class HomPagerAdapter extends FragmentStatePagerAdapter {
	private HomSubFragment1 homSubFragment1;
	private HomSubFragment2 homSubFragment2;
	private HomSubFragment3 homSubFragment3;
	private HomSubFragment4 homSubFragment4;
	public HomPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	private final int[] icons = {R.drawable.icon_1_n,R.drawable.icon_1_n,R.drawable.icon_1_n};
	private final String[] titles = { "限时镖", "顺风镖","快递", "物流" };
//	private final int[] title = { R.drawable.icon_1_n, R.drawable.icon_2_d,R.drawable.icon_3_d, R.drawable.icon_3_n };

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return titles.length;
	}
	
//	public int getPageIconResId(int position) {
//
//	    return icons[position];
//	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:

			if (null == homSubFragment1) {
				homSubFragment1 = new HomSubFragment1();
			}

			return homSubFragment1;

		case 1:

			if (null == homSubFragment2) {
				homSubFragment2 = new HomSubFragment2();
			}

			return homSubFragment2;
		case 2:

			if (null == homSubFragment3) {
				homSubFragment3 = new HomSubFragment3();
			}

			return homSubFragment3;
		case 3:

			if (null == homSubFragment4) {
				homSubFragment4 = new HomSubFragment4();
			}

			return homSubFragment4;
		default:
			return null;
		}
	}

}
