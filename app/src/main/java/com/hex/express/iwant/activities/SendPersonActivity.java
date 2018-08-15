package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.SendPersonAdapter;
import com.hex.express.iwant.bean.SendPersonBean;
import com.hex.express.iwant.bean.SendPersonBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;


public class SendPersonActivity extends BaseActivity{
	@Bind(R.id.lv) ListView lv;
	@Bind(R.id.tbv_show) TitleBarView tbv_show;
	@Bind(R.id.rl) LinearLayout rl;
	@Bind(R.id.tv_add) TextView tv_add;
	@Bind(R.id.rl_add)	RelativeLayout rl_add;
	private BaseListAdapter adapter;
	private List list=new ArrayList<SendPersonBean.Data>();
	LoadingProgressDialog dialog;
	Intent intent; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_person);
		ButterKnife.bind(this);
		dialog=new LoadingProgressDialog(this);
		dialog.show();
		initData();
	}
	@Override
	public void onWeightClick(View v) {
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}
	@Override
	public void initData() {
		tbv_show.setTitleText("历史地址");
		intent= new Intent();
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.SENDPERSON, "userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)+""), new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				SendPersonBean bean=new Gson().fromJson(new String(arg2),SendPersonBean.class);
			//	Log.e("list",bean.data.get(0).cityCode);
				list=bean.data;
				if(list!=null&&list.size()!=0){
					adapter=new SendPersonAdapter(SendPersonActivity.this, list,rl_add);
					rl.setVisibility(View.VISIBLE);
					rl_add.setVisibility(View.GONE);
					lv.setAdapter(adapter);
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							SendPersonBean.Data data = (Data) list.get(position);
							Intent intent=new Intent();
							intent.putExtra("name", data.personName);
							Log.e("name", data.personName);
							intent.putExtra("phone", data.mobile);
							intent.putExtra("location",data.areaName);
							intent.putExtra("address", data.address);
							intent.putExtra("latitude", data.latitude);
							intent.putExtra("longitude", data.longitude);
							intent.putExtra("city", data.cityName);
							setResult(RESULT_OK, intent);
							finish();
						}
					});
				}
				else{
					rl.setVisibility(View.GONE);
					rl_add.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("list", arg0+"");
				//di
				
			}
		});
	}

	@Override
	public void setOnClick() {
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	//点击增加寄信人
	/*@OnClick({R.id.btn_add,R.id.btn_add_no})
	public void add(View view){
		switch (view.getId()) {
		case R.id.btn_add:
			startActivity(new Intent(SendPersonActivity.this,AddCourierActivity.class));
			break;
		case R.id.btn_add_no:
			startActivity(new Intent(SendPersonActivity.this,AddCourierActivity.class));
			break;
		default:
			break;
		}
		
	}*/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//LoadingProgressDialog.getInstance(this).
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
