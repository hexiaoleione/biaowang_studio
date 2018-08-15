package com.hex.express.iwant.adpter;

import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.minfragment.MinFragment1;
import com.hex.express.iwant.minfragment.MinFragment2;
import com.hex.express.iwant.minfragment.MinFragment3;
import com.hex.express.iwant.minfragment.MinFragment4;
import com.hex.express.iwant.utils.PreferencesUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapterse extends FragmentStatePagerAdapter {
    private MinFragment1 minFragment1;
    private MinFragment2 minFragment2;
    private MinFragment3 minFragment3;
    private MinFragment4 minFragment4;

    public MyPagerAdapterse(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    private final String[] titles = {"限时镖", "物流镖"};

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
//		case 0:
//			if (null == minFragment2) {
//				minFragment2 = new MinFragment2();
//			}
//
//			return minFragment2;
//			

            case 0:
                if (null == minFragment1) {
                    minFragment1 = new MinFragment1();
                }

                return minFragment1;

            case 1:
                if (null == minFragment4) {
                    minFragment4 = new MinFragment4();
                }

                return minFragment4;
//			if (null == minFragment3) {
//				minFragment3 = new MinFragment3();
//			}
//
//			return minFragment3;
//		case 3:
//
//			if (null == minFragment4) {
//				minFragment4 = new MinFragment4();
//			}
//
//			return minFragment4;
            default:
                return null;
        }
    }

}

