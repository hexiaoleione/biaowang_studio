package com.hex.express.iwant.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.NewSearchAdpa;
import com.hex.express.iwant.adapters.ProvinceAdapter;
import com.hex.express.iwant.adapters.ProvincetownAdapter;
import com.hex.express.iwant.bean.CitySelectBean;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CitySelectOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.views.TitleBarView;

public class NewSearchActivity extends Activity{

	private String aName;
	private String pCode;
	private String cCode;
	private String tCode;
	
	private ListView provList;
	private ListView cityList;
	private ListView townList;
	
	private NewSearchAdpa adapter;
	
	private List<CitySelectBean> resultPro;
	private List<CitySelectBean> resultCity;
	private List<CitySelectBean> resultTown;
	@Bind(R.id.btnLeft)
	Button btnLeft;
	@Bind(R.id.btnright)
	Button btnright;
	
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsearchcity);
		ButterKnife.bind(this);
		initProvince();
		initData();
	}
	
	private void initData() {
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});
		
	}

	private void initProvince(){
		boolean isCopySuccess = CheckDbUtils.checkDb();
		intent = new Intent();
		//成功的将数据库copy到data 中
		if(isCopySuccess){ 
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		cityList = null;
		provList = (ListView) findViewById(R.id.listProvince);
		provList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int index,
					long arg3) {
				CitySelectBean citySelectBean = resultPro.get(index);
				aName = citySelectBean.getName();
				pCode = citySelectBean.getCode();
				intent.putExtra("pro", aName);
//				btnright.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
						intent.putExtra("citytwo", aName);
						intent.putExtra("type", 2);
						NewSearchActivity.this.setResult(RESULT_OK, intent);
						finish();
//					}
//				});
//				initCity(pCode);
			}
		});
		resultPro = getProviceData();
		adapter = new NewSearchAdpa(NewSearchActivity.this,resultPro);
		NewSearchActivity.this.provList.setAdapter(adapter);
	}
	
	private void initCity(String provCode){
		boolean isCopySuccess = CheckDbUtils.checkDb();
		//成功的将数据库copy到data 中
		if(isCopySuccess){ 
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if(resultTown!=null){
			resultTown.clear();
		}
		cityList = (ListView) findViewById(R.id.listCity);
		cityList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int index,
					long arg3) {
				
				CitySelectBean citySelectBean = resultCity.get(index);
				aName = aName+citySelectBean.getName();
				cCode = citySelectBean.getCode();
				intent.putExtra("city", citySelectBean.getName());
				intent.putExtra("code", cCode);
//				btnright.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						intent.putExtra("type", 3);
						NewSearchActivity.this.setResult(RESULT_OK, intent);
						finish();
//					}
//				});
//				ProvCityTownAcrivity.this.setResult(RESULT_OK, intent);
//				finish();
//				initTown(cCode);
//				adapter.setSelectItem(index);
			}
		});
		resultCity = getCityData(provCode);
		adapter = new NewSearchAdpa(NewSearchActivity.this,resultCity);
		NewSearchActivity.this.cityList.setAdapter(adapter);
	}
	
	private void initTown(String cityCode){
		boolean isCopySuccess = CheckDbUtils.checkDb();
		//成功的将数据库copy到data 中
		if(isCopySuccess){ 
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		townList = (ListView) findViewById(R.id.listTown);
		townList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int index,
					long arg3) {
				CitySelectBean citySelectBean = resultTown.get(index);
				aName = aName+citySelectBean.getName();
				tCode = citySelectBean.getCode();
				//返回cityCode和aName
//				ArrayList<String> back = new ArrayList<String>();
//				back.add(cCode);
//				back.add(aName);
				String ba = cCode+"&"+aName;
//				intent.putStringArrayListExtra("area", back);
				intent.putExtra("distract",citySelectBean.getName());
				intent.putExtra("name", aName);
				intent.putExtra("code", cCode);
				NewSearchActivity.this.setResult(RESULT_OK, intent);
				finish();
			}
		});
		resultTown = getTownData(cityCode);
		adapter = new NewSearchAdpa(NewSearchActivity.this,resultTown);
		NewSearchActivity.this.townList.setAdapter(adapter);
	}
	
	private List<CitySelectBean> getProviceData(){
		CitySelectOperation selectOpeation = new CitySelectOperation();
		List<CitySelectBean>  mProvices = selectOpeation.selectDataFromDb("select * from provice","pro_name","pro_code");
		return mProvices;
	}
	
	private List<CitySelectBean> getCityData(String provCode){
		CitySelectOperation selectOpeation = new CitySelectOperation();
		List<CitySelectBean>  mProvices = selectOpeation.selectDataFromDb("select * from city where pro_code='"+provCode+"'","city_name","city_code");
		return mProvices;
	}
	
	private List<CitySelectBean> getTownData(String cityCode){
		CitySelectOperation selectOpeation = new CitySelectOperation();
		List<CitySelectBean>  mProvices = selectOpeation.selectDataFromDb("select * from area where pro_code='"+cityCode+"'","area_name","area_code");
		return mProvices;
	}
	/**
	 * 返回键关闭本页面
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}
