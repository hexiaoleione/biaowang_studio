package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.CitySelectBean;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CitySelectOperation;
import com.hex.express.iwant.helper.DbManager;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 搜索城市的界面
 * @author huyichuan
 *
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener {
	@Bind(R.id.listview)
	ListView listView;
	@Bind(R.id.search_view)
	SearchView searchView;
	String[] names;
	ArrayAdapter<String> adapter;
	ArrayList<String> mAllList = new ArrayList<String>();
	private List<CitySelectBean> resultCity;
	private CitySelectBean citySelectBean;
	private ArrayList<String> mSearchList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchcity);
		ButterKnife.bind(SearchActivity.this);
		initData();
		initCity();

	}

	private void initCity() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		resultCity = getCityData();
		names = new String[resultCity.size()];
		for (int i = 0; i < resultCity.size(); i++) {
			names[i] = resultCity.get(i).getName();
		}
		Log.e("jjjjj", names + "");
		adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, names);
		listView.setAdapter(adapter);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				citySelectBean = resultCity.get(arg2);
				Intent intent = new Intent();
				intent.putExtra("citytwo", citySelectBean.getName());
				setResult(RESULT_OK, intent);
				Log.e("kkkakdl", citySelectBean.getName());
				finish();
			}
		});
	}

	private List<CitySelectBean> getCityData() {
		CitySelectOperation selectOpeation = new CitySelectOperation();
		List<CitySelectBean> mProvices = selectOpeation.selectDataFromDb("select * from city ", "city_name",
				"city_code");
		return mProvices;
	}


	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {

	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		Object[] obj = searchItem(newText);
		updateLayout(obj);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object[] searchItem(String name) {
		mSearchList = new ArrayList<String>();
		for (int i = 0; i < resultCity.size(); i++) {
			int index = resultCity.get(i).getName().indexOf(name);
			// 存在匹配的数据
			if (index != -1) {
				mSearchList.add(resultCity.get(i).getName());
			}
		}
		return mSearchList.toArray();
	}

	public void updateLayout(Object[] obj) {
		listView.setAdapter(
				new ArrayAdapter<Object>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, obj));
        listView.setOnItemClickListener(new OnItemClickListener() {

	    		@Override
	    		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	    			String citytwo=mSearchList.get(arg2);
	    			Intent intent=new Intent();
	    			intent.putExtra("citytwo", citytwo);
	    			setResult(RESULT_OK, intent);
	    			Log.e("kkkakdl", citytwo);
	    			finish();
	    		}
	    	});
	}

}
