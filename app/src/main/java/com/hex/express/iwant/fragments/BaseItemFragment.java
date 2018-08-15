package com.hex.express.iwant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.framework.base.BaseFragment;
import com.hex.express.iwant.R;

public abstract class BaseItemFragment extends BaseFragment {
	public View rootView;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	com.handmark.pulltorefresh.library.PullToRefreshListView listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, rootView);
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	/**
	 * 实现点击事件
	 * @param v
	 */
	public abstract void onWeightClick(View v);
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	/**
	 * 设置点击事件
	 */
	public abstract void setOnClick();
	/**
	 * 获取存储或者传递过来的数据
	 */
	public abstract void getData();
	
	public void onClick(View v){
		onWeightClick(v);
	}

	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
