package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.utils.DataCleanManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 
 * @author huyichuan
 *  个人信息
 */
public class RevisepersonActivity extends BaseActivity{
	
   @Bind(R.id.btnLeft)
   ImageView btnLeft;
   @Bind(R.id.detaclean)
   TextView detaclean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviseperson);
		ButterKnife.bind(this);
		getData();
		initView();
		initData();
		setOnClick();
	}
	@OnClick({R.id.ll_function,R.id.ll_com,R.id.ll_service})
	public void onMyClick(View view){
		switch (view.getId()) {
		case R.id.ll_function:
			//修改头像
			startActivity(new Intent(RevisepersonActivity.this,ReviseheadActivity.class));
			
			break;
		case R.id.ll_com://修改手机号
			startActivity(new Intent(RevisepersonActivity.this, RevisePhoneActivity.class));
			break;
		case R.id.ll_service:
//			startActivity(new Intent(RevisepersonActivity.this,ServiceActivity.class));
			break;
		case R.id.btnLeft:
//			startActivity(new Intent(RevisepersonActivity.this,ServiceActivity.class));
//			finish();
			break;
		default:
			break;
		}
		}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		try {
//			detaclean.setText(""+DataCleanManager.getTotalCacheSize(getApplicationContext()));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
//				try {
//					DataCleanManager.clearAllCache(RevisepersonActivity.this);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ImageLoader.getInstance().clearMemoryCache();//清除内存
//			      ImageLoader.getInstance().clearDiskCache();//清除sd卡
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
