package com.hex.express.iwant.viewpager;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class DialogFragmentWindow extends DialogFragment {
	
	public static DialogFragmentWindow intance;
	private String first = "";
	private String second = "";
	private String from = "";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.window, container);
        intance = this;
        first = getArguments().getString("first");
        second = getArguments().getString("second");
        from = getArguments().getString("from");

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        List<Fragment> fragments = getFragments();
        AcoesMuscularesAdapter ama = new AcoesMuscularesAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(ama);
        if ("two".equals(from)) {//定位在第二个fragment
        	viewPager.setCurrentItem(1);
		}
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }
    
      //全屏显示
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//        }
//    }

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		fList.add(MyPagerFragment.newInstance(this, first, 1));
		fList.add(MyPagerFragment.newInstance(this, second, 2));
		return fList;
	}
    
    class AcoesMuscularesAdapter extends FragmentPagerAdapter{

        private List<Fragment> fragments;

        public AcoesMuscularesAdapter(FragmentManager fm, List<Fragment> fragments){
            super(fm);
            this.fragments = fragments;
        }

        @Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}
