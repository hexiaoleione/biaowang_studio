package com.hex.express.iwant.adpter;


import com.hex.express.iwant.subfragment.SubFragment1;
import com.hex.express.iwant.subfragment.SubFragment2;
import com.hex.express.iwant.subfragment.SubFragment3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private int startX;
    private int startY;
    private SubFragment1 subFragment1;
    private SubFragment2 subFragment2;
    private SubFragment3 subFragment3;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    private final String[] titles = {"顺路送", "专程送", "物流/冷链"};

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

                if (null == subFragment2) {
                    subFragment2 = new SubFragment2();
                }

                return subFragment2;
            case 1:

                if (null == subFragment1) {
                    subFragment1 = new SubFragment1();
                }

                return subFragment1;
            case 2:

                if (null == subFragment3) {
                    subFragment3 = new SubFragment3();
                }
                return subFragment3;
            default:
                return null;
        }
    }
}

