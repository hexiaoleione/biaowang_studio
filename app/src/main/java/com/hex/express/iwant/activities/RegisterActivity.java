package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DrawCardBean;
import com.hex.express.iwant.bean.ExerciseBean;
import com.hex.express.iwant.bean.RegistBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity{
	
	@Bind(R.id.imagesumit)
	ImageView imagesumit;//签到按钮
//	@Bind(R.id.tirle_text2)
//	TextView tirle_text2;//签到到第七天剩余的天数
	@Bind(R.id.te_qian)
	TextView te_qian;//签到到de天数
//	@Bind(R.id.tirle_text4)
//	TextView tirle_text4;//100E币数量
	@Bind(R.id.txi)
	TextView txi;//1规则1
//	@Bind(R.id.tirle_te)
//	TextView tirle_te;//姓名
	@Bind(R.id.tirle_text)
	TextView tirle_text;//今日已领取
	@Bind(R.id.txi2)
	TextView txi2;//每天签到可获得
	@Bind(R.id.tirle_leiji2)
	TextView tirle_leiji2;//累计获得几个
	
	
	
	
	@Bind(R.id.btn_Left)
	ImageView btn_Left;//
//	@Bind(R.id.imagetii)
//	ImageView imagetii;//
	
	@Bind(R.id.imagtitle1)
	ImageView imagtitle1;
	@Bind(R.id.imagtitle2)
	ImageView imagtitle2;
	@Bind(R.id.imagtitle3)
	ImageView imagtitle3;
	@Bind(R.id.imagtitle4)
	ImageView imagtitle4;
	@Bind(R.id.imagtitle5)
	ImageView imagtitle5;
	@Bind(R.id.imagtitle6)
	ImageView imagtitle6;
	@Bind(R.id.imagtitle7)
	ImageView imagtitle7;
	
	@Bind(R.id.imagtitle11)
	TextView imagtitle11;
	@Bind(R.id.imagtitle22)
	TextView imagtitle22;
	@Bind(R.id.imagtitle33)
	TextView imagtitle33;
	@Bind(R.id.imagtitle44)
	TextView imagtitle44;
	@Bind(R.id.imagtitle55)
	TextView imagtitle55;
	@Bind(R.id.imagtitle66)
	TextView imagtitle66;
	@Bind(R.id.imagtitle77)
	TextView imagtitle77;
	
	RegistBean regibean;
	List<RegistBean.Data> mList;
	int days;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rehisterse);//activity_rehister activity_rehisterse
		ButterKnife.bind(this);
		initView();
		initData();
		getData();
		setOnClick();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mList=new ArrayList<RegistBean.Data>();
		Getmea();
//		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH)!=null) {
////			new MyBitmapUtils().display(imagetii, PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USER_ICON));
////		Log.e("11111url",PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH));
////			loader.displayImage(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH),
////					imagetii, options);
//		}
		
//		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME)!=null) {
//			tirle_te.setText(""+PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME));
//		}
		
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btn_Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 获取签到
	 */
	public void Getmea(){
	
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.Singnlist, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null,
				null, null, new AsyncHttpResponseHandler() {
		

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111", "**********************"
								+ new String(arg2));
						regibean = new Gson().fromJson(new String(arg2),
								RegistBean.class);
// 
//						Log.e("1111111", "**********************"
//								+ drabean.getData().get(0).a);
						mList=regibean.data;
						if (mList.size() != 0 && mList != null) {
							setview();
						}
//					ToastUtil.shortToast(RegisterActivity.this, regibean.message);
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e("111111122", "**********************"
								+ arg0);
					}
				});

	
	}
	/**
	 * 签到
	 */
	public void Getmease(){
	
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.Singn, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null,
				null, null, new AsyncHttpResponseHandler() {
		
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111", "**********************"
								+ new String(arg2));
						BaseBean	drabean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						ToastUtil.shortToast(RegisterActivity.this, drabean.getMessage());
//						
//						setview();
						Getmea();
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
				});
	}
	
	public void setview(){
		if (regibean.getData().get(0).ifSign) {
			imagesumit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Getmease();
				}
			});
		}else {
			imagesumit.setBackgroundResource(R.drawable.bg_goldqian2);
		}
		//
		if (!regibean.getData().get(0).keepDay.equals("")) {
			txi.setText("2.连续签到"+regibean.getData().get(0).keepDay+"天即可得"+regibean.getData().get(0).ecoin+"积分。");
		}else {
			txi.setText("2.连续签到七天即可得100积分。");
		}
		
//		//100E币的100
//		if (!regibean.getData().get(0).ecoin.equals("")) {
//			tirle_text4.setText("距离："+regibean.getData().get(0).ecoin);
//		}else {
//			tirle_text4.setText("距离："+"100");
//		}
//		//签到剩余
//		if (!regibean.getData().get(0).needSignDays.equals("")) {
//			tirle_text2.setText(""+regibean.getData().get(0).needSignDays);
//		}else {
//			tirle_text2.setText("7");
//		}
			//连续签到
		if (regibean.getData().get(0).number!=0) {
			te_qian.setText(""+regibean.getData().get(0).number+"");
		}else {
			te_qian.setText("0");
		}
		//规则1
		if (regibean.getData().get(0).everyDayEcoin!=0) {
			txi2.setText("1.每天签到可得"+regibean.getData().get(0).everyDayEcoin+"积分");
		}else {
			txi2.setText("1.每天签到可得0积分");
		}
		//累计获得
		if (regibean.getData().get(0).allEcoin!=0) {
			tirle_leiji2.setText(""+regibean.getData().get(0).userEcoin+" ");
		}else {
			tirle_leiji2.setText("0");
		}	
		//今日
		if (regibean.getData().get(0).dayEcoin!=0) {
			tirle_text.setText(""+regibean.getData().get(0).dayEcoin+"");
//			tirle_text.setVisibility(View.VISIBLE);
		}else {
//			tirle_text.setVisibility(View.GONE);
			tirle_text.setText("0");
		}
		days=regibean.getData().get(0).days;
		if (days!=0 && days%7==0) {
			 days = 7;
		}else {
			 days = days%7;
		}
		if (days==1) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
		}else 
		if (days==2) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle2.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle22.setText(""+regibean.getData().get(0).everyDayEcoin);
		}else
		if (days==3) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle2.setBackgroundResource(R.drawable.dddqia);
			imagtitle3.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle22.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle33.setText(""+regibean.getData().get(0).everyDayEcoin);
		}else
		if (days==4) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle2.setBackgroundResource(R.drawable.dddqia);
			imagtitle3.setBackgroundResource(R.drawable.dddqia);
			imagtitle4.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle22.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle33.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle44.setText(""+regibean.getData().get(0).everyDayEcoin);
		}else
		if (days==5) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle2.setBackgroundResource(R.drawable.dddqia);
			imagtitle3.setBackgroundResource(R.drawable.dddqia);
			imagtitle4.setBackgroundResource(R.drawable.dddqia);
			imagtitle5.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle22.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle33.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle44.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle55.setText(""+regibean.getData().get(0).everyDayEcoin);
		}else
		if (days==6) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle2.setBackgroundResource(R.drawable.dddqia);
			imagtitle3.setBackgroundResource(R.drawable.dddqia);
			imagtitle4.setBackgroundResource(R.drawable.dddqia);
			imagtitle5.setBackgroundResource(R.drawable.dddqia);
			imagtitle6.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle22.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle33.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle44.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle55.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle66.setText(""+regibean.getData().get(0).everyDayEcoin);
		}else
		if (days==7) {
			imagtitle1.setBackgroundResource(R.drawable.dddqia);
			imagtitle2.setBackgroundResource(R.drawable.dddqia);
			imagtitle3.setBackgroundResource(R.drawable.dddqia);
			imagtitle4.setBackgroundResource(R.drawable.dddqia);
			imagtitle5.setBackgroundResource(R.drawable.dddqia);
			imagtitle6.setBackgroundResource(R.drawable.dddqia);
			imagtitle7.setBackgroundResource(R.drawable.dddqia);
			imagtitle11.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle22.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle33.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle44.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle55.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle66.setText(""+regibean.getData().get(0).everyDayEcoin);
			imagtitle77.setText(""+regibean.getData().get(0).ecoin);
		}
		
	}
}
