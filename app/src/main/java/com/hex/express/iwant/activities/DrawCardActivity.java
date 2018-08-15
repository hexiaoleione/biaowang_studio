package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.MessageAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DrawCardBean;
import com.hex.express.iwant.bean.ExerciseBean;
import com.hex.express.iwant.bean.MessageBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * 领卷现金卷界面
 */
public class DrawCardActivity extends BaseActivity{

	@Bind(R.id.img_left)
	Button img_left;
	@Bind(R.id.img_left2)
	Button img_left2;
	@Bind(R.id.img_left3)
	Button img_left3;
	@Bind(R.id.img_left4)
	Button img_left4;
	@Bind(R.id.img_left5)
	Button img_left5;
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	DrawCardBean drabean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawcard);
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
		   Getmea();
		
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
//		if (drabean.getData().get(0).a.equals("0")) {
//			img_left.setBackgroundColor(R.color.grayview);	
//		}else {
		img_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					receiveCoupon(0);
			}
		});
//		}
//		if (drabean.getData().get(0).b.equals("0")) {
//			img_left.setBackgroundColor(R.color.grayview);	
//		}else {
		img_left2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				receiveCoupon(1);
			}
		});
//		}
//		if (drabean.getData().get(0).c.equals("0")) {
//			img_left.setBackgroundColor(R.color.grayview);	
//		}else {
		img_left3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				receiveCoupon(2);
			}
		});
//		}
//		if (drabean.getData().get(0).d.equals("0")) {
//			img_left.setBackgroundColor(R.color.grayview);	
//		}else {
		img_left4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				receiveCoupon(3);
			}
		});
//		}
//		if (drabean.getData().get(0).e.equals("0")) {
//			img_left.setBackgroundColor(R.color.grayview);	
//		}else {
		img_left5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				receiveCoupon(4);
			}
		});
//		}
            
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 获取现金卷
	 */
	public void Getmea(){
	
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.Recrive, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null,
				null, null, new AsyncHttpResponseHandler() {
		

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111", "**********************"
								+ new String(arg2));
						drabean = new Gson().fromJson(new String(arg2),
								DrawCardBean.class);
                   if (drabean.data.size()>0) {
                		setview();
				}
//						Log.e("1111111", "**********************"
//								+ drabean.getData().get(0).a);
						if (drabean.errCode!=0) {
							ToastUtil.shortToast(DrawCardActivity.this, ""+drabean.message);
						}
					
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
				});

	
	}
	/**
	 * 领取现金卷
	 */
	public void receiveCoupon(int item){
		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.RecriveCoupon, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "conditionId", String
				.valueOf(item)), null,
				null, null, new AsyncHttpResponseHandler() {
//	AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.RecriveCoupon,obj.toString(),null,new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", "*************************************"
								+ new String(arg2));
				BaseBean		drabsean = new Gson().fromJson(new String(arg2),
						BaseBean.class);
				if (drabsean.getErrCode()==0) {
					ToastUtil.shortToast(DrawCardActivity.this, drabsean.getMessage());
					
					setcoo();
				}
				ToastUtil.shortToast(DrawCardActivity.this, drabsean.getMessage());
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						// TODO Auto-generated method stub

					}
				});
	}
	@SuppressWarnings("deprecation")
	public void setview(){
		img_left.setText("领取*"+drabean.getData().get(0).a);
		if (drabean.getData().get(0).a.equals("0")) {
			img_left.setText("已领取");
			img_left.setBackgroundColor(getResources().getColor(R.color.grayview));
		}
		img_left2.setText("领取*"+drabean.getData().get(0).b);
		if (drabean.getData().get(0).b.equals("0")) {
			img_left2.setText("已领取");
			img_left2.setBackgroundColor(getResources().getColor(R.color.grayview));
		}
		img_left3.setText("领取*"+drabean.getData().get(0).c);
		if (drabean.getData().get(0).c.equals("0")) {
			img_left3.setText("已领取");
			img_left3.setBackgroundColor(getResources().getColor(R.color.grayview));
		}
		img_left4.setText("领取*"+drabean.getData().get(0).d);
		if (drabean.getData().get(0).d.equals("0")) {
			img_left4.setText("已领取");
			img_left4.setBackgroundColor(getResources().getColor(R.color.grayview));
		}
		img_left5.setText("领取*"+drabean.getData().get(0).e);
		if (drabean.getData().get(0).e.equals("0")) {
			img_left5.setText("已领取");
			img_left5.setBackgroundColor(getResources().getColor(R.color.grayview));
		}
//		if (drabean.getData().get(0).a.equals("0")) {
////			img_left.setTextColor(color);
//		}
		
	}
	public void setcoo(){
		 Getmea();
	}

}
