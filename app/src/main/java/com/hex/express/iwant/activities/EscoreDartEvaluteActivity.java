package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.EscortInfoBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.TestMessage;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 镖师押镖完成后去评价
 * @author huyichuan
 *
 */
public class EscoreDartEvaluteActivity extends BaseActivity {
	@Bind(R.id.img_head)
	ImageView img_head;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.text_name)
	TextView text_name;
	@Bind(R.id.satisfaction)
	TextView satisfaction;
	@Bind(R.id.rab_couriersscore)
	RatingBar rab_couriersscore;
	@Bind(R.id.text1)
	TextView text1;
	@Bind(R.id.text2)
	TextView text2;
	@Bind(R.id.text3)
	TextView text3;
	@Bind(R.id.text4)
	TextView text4;
	@Bind(R.id.text5)
	TextView text5;
	@Bind(R.id.text6)
	TextView text6;

	@Bind(R.id.img_love1)
	ImageView img_love1;
	@Bind(R.id.img_love2)
	ImageView img_love2;
	@Bind(R.id.img_love3)
	ImageView img_love3;
	@Bind(R.id.img_love4)
	ImageView img_love4;
	@Bind(R.id.img_love5)
	ImageView img_love5;
	@Bind(R.id.img_love6)
	ImageView img_love6;
	@Bind(R.id.edit_text)
	EditText edit_text;
	@Bind(R.id.lout1)
	LinearLayout lout1;
	@Bind(R.id.lout2)
	LinearLayout lout2;
	@Bind(R.id.lout3)
	LinearLayout lout3;
	@Bind(R.id.lout4)
	LinearLayout lout4;
	@Bind(R.id.lout5)
	LinearLayout lout5;
	@Bind(R.id.lout6)
	LinearLayout lout6;
	int lou1=0;
	int lou2=0;
	int lou3=0;
	int lou4=0;
	int lou5=0;
	int lou6=0;
	boolean flag = false;

	List<String> mList = new ArrayList<String>();
	private String lout6String;
	private String lout5String;
	private String lout4String;
	private String lout3String;
	private String lout2String;
	private String lout1String;
	private String tagString;
	private String driverId;//镖师的ID；
	private String recId;//镖件的ID；
	//定义Action常量  
    protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_escoreevalute);
		ButterKnife.bind(EscoreDartEvaluteActivity.this);
		initData();
		getData();
	}

	@OnClick({ R.id.submit, R.id.lout1, R.id.lout2, R.id.lout3, R.id.lout4, R.id.lout5, R.id.lout6 })
	public void MyOnClick(View view) {
		switch (view.getId()) {
		case R.id.submit:
			Log.e("jkkkkkllllklk", rab_couriersscore.getRating() + "");
			AddBtnSubmit();
//			 EventBus.getDefault().post(new TestMessage(TestMessage.Event.header));
			
			
			break;
		case R.id.lout1:
			if (!flag) {
				lou1=1;
				lout1.setBackgroundResource(R.drawable.text_radius_whiteorangetwo);
				if (rab_couriersscore.getRating() >= 3) {	
					img_love1.setBackgroundResource(R.drawable.hearto);
				} else {
					img_love1.setBackgroundResource(R.drawable.xilihuala);

				}
				flag = true;
			} else {
				lou1=0;
				lout1String="";
				lout1.setBackgroundResource(R.drawable.text_radius_whitegraythree);
				img_love1.setBackgroundResource(R.drawable.none_heart);
				flag = false;
			}

			break;
		case R.id.lout2:
			if (!flag) {
				lou2=1;
				lout2.setBackgroundResource(R.drawable.text_radius_whiteorangetwo);
				if (rab_couriersscore.getRating() >= 3) {
					img_love2.setBackgroundResource(R.drawable.hearto);

				} else {
					img_love2.setBackgroundResource(R.drawable.xilihuala);

				}
				flag = true;
			} else {
				lou2=0;
				lout2String="";
				lout2.setBackgroundResource(R.drawable.text_radius_whitegraythree);
				img_love2.setBackgroundResource(R.drawable.none_heart);
				flag = false;
			}
			break;
		case R.id.lout3:
			if (!flag) {
				lou3=1;
				lout3.setBackgroundResource(R.drawable.text_radius_whiteorangetwo);
				if (rab_couriersscore.getRating() >= 3) {
					img_love3.setBackgroundResource(R.drawable.hearto);

				} else {
					img_love3.setBackgroundResource(R.drawable.xilihuala);

				}
				flag = true;
			} else {
				lou3=0;
				lout3String="";
				lout3.setBackgroundResource(R.drawable.text_radius_whitegraythree);
				img_love3.setBackgroundResource(R.drawable.none_heart);
				flag = false;
			}
			break;
		case R.id.lout4:
			if (!flag) {
				lou4=1;
				lout4.setBackgroundResource(R.drawable.text_radius_whiteorangetwo);
				if (rab_couriersscore.getRating() >= 3) {
					img_love4.setBackgroundResource(R.drawable.hearto);

				} else {
					img_love4.setBackgroundResource(R.drawable.xilihuala);

				}
				flag = true;
			} else {
				lou4=0;
				lout4String="";
				lout4.setBackgroundResource(R.drawable.text_radius_whitegraythree);
				img_love4.setBackgroundResource(R.drawable.none_heart);
				flag = false;
			}
			break;
//		case R.id.lout5:
//			if (!flag) {
//				lou5=1;
//				lout5.setBackgroundResource(R.drawable.text_radius_whiteorangetwo);
//				if (rab_couriersscore.getRating() >= 3) {
//					img_love5.setBackgroundResource(R.drawable.hearto);
//
//				} else {
//					img_love5.setBackgroundResource(R.drawable.xilihuala);
//
//				}
//				flag = true;
//			} else {
//				lou5=0;
//				lout5String="";
//				lout5.setBackgroundResource(R.drawable.text_radius_whitegraythree);
//				img_love5.setBackgroundResource(R.drawable.none_heart);
//				flag = false;
//
//			}
//
//			break;
//		case R.id.lout6:
//
//			if (!flag) {
//				lou6=1;
//				lout6.setBackgroundResource(R.drawable.text_radius_whiteorangetwo);
//				if (rab_couriersscore.getRating() >= 3) {
//					img_love6.setBackgroundResource(R.drawable.hearto);
//
//				} else {
//					img_love6.setBackgroundResource(R.drawable.xilihuala);
//
//				}
//				flag = true;
//			} else {
//				lou6=0;
//				lout6String="";
//				lout6.setBackgroundResource(R.drawable.text_radius_whitegraythree);
//				img_love6.setBackgroundResource(R.drawable.none_heart);
//				flag = false;
//			}
//			break;

		default:
			break;
		}
	}

	/**
	 * 提交评价
	 */
	private void AddBtnSubmit() {
		List<String >mList=new ArrayList<String>();
		if (lou1==1) {
			lout1String = text1.getText().toString();
			mList.add(lout1String);
		}
		if (lou2==1) {
			lout2String = text2.getText().toString();
			mList.add(lout2String);
		}
		if (lou3==1) {
			lout3String = text3.getText().toString();
			mList.add(lout3String);
		}
		if (lou4==1) {
			lout4String = text4.getText().toString();
			mList.add(lout4String);
		}
//		if (lou5==1) {
//			lout5String = text5.getText().toString();
//			mList.add(lout5String);
//		}
//		if (lou6==1) {
//			lout6String = text6.getText().toString();
//		}
		
		tagString = "  "+lout1String+"  "+lout2String+"  "+lout3String+"  "+lout4String+"\n";
		tagString=tagString.replace("null", "");
		JSONObject obj = new JSONObject();
		try {
			obj.put("recId", getIntent().getIntExtra("recId", 0));
			obj.put("driverId", driverId);
			obj.put("driverScore", rab_couriersscore.getRating()*2);
			obj.put("driverContent", edit_text.getText().toString());
			obj.put("driverTag", tagString);
			obj.put("userId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("obj", obj.toString());
		AsyncHttpUtils.doPostJson(EscoreDartEvaluteActivity.this, MCUrl.ESCOREEVALUATE, obj.toString(), new AsyncHttpResponseHandler() {			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			Logger.e("1111111  ", new String(arg2));
			BaseBean bean=new Gson().fromJson(new String(arg2), BaseBean.class);
			if (bean.getErrCode()==0) {
				ToastUtil.shortToast(EscoreDartEvaluteActivity.this, bean.getMessage());
				summit();
				finish();
			}else {
				ToastUtil.shortToast(EscoreDartEvaluteActivity.this, bean.getMessage());
			}
		
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	private void summit(){
		Intent broadcast = new Intent(ACTION);
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
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
		lout5.setVisibility(View.GONE);
		lout6.setVisibility(View.GONE);
		driverId = getIntent().getStringExtra("driverId");
		recId = getIntent().getStringExtra("recId");
		tbv_show.setTitleText("评价");
		rab_couriersscore.setOnRatingBarChangeListener(new RatingBarChangeListener());

	}

	class RatingBarChangeListener implements OnRatingBarChangeListener {

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
			if (rating == 1) {
				satisfaction.setText("非常不满意,各方面都很差");
				text1.setText("服务态度恶劣");
				text2.setText("效率低下");
				text3.setText("交通工具太破");
				text4.setText("道路不熟");
			} else if (rating == 2) {
				satisfaction.setText("不满意,比较差");
				text1.setText("服务态度恶劣");
				text2.setText("效率低下");
				text3.setText("交通工具太破");
				text4.setText("道路不熟");
			} else if (rating == 3) {
				satisfaction.setText("一般，需要改善");
				text1.setText("服务态度恶劣");
				text2.setText("效率低下");
				text3.setText("交通工具太破");
				text4.setText("道路不熟");

			} else if (rating == 4) {
				satisfaction.setText("比较满意，但仍可改善");
				text1.setText("活地图认路准");
				text2.setText("态度较好");
				text3.setText("交通工具不错");
				text4.setText("效率较高");
			} else if (rating == 5) {
				satisfaction.setText("非常满意，无可挑剔");
				text1.setText("活地图认路准");
				text2.setText("态度较好");
				text3.setText("交通工具不错");
				text4.setText("效率较高");
			}

			lout1.setBackgroundResource(R.drawable.text_radius_whitegraythree);
			img_love1.setBackgroundResource(R.drawable.none_heart);

			lout2.setBackgroundResource(R.drawable.text_radius_whitegraythree);
			img_love2.setBackgroundResource(R.drawable.none_heart);

			lout3.setBackgroundResource(R.drawable.text_radius_whitegraythree);
			img_love3.setBackgroundResource(R.drawable.none_heart);

			lout4.setBackgroundResource(R.drawable.text_radius_whitegraythree);
			img_love4.setBackgroundResource(R.drawable.none_heart);

//			lout5.setBackgroundResource(R.drawable.text_radius_whitegraythree);
//			img_love5.setBackgroundResource(R.drawable.none_heart);
//
//			lout6.setBackgroundResource(R.drawable.text_radius_whitegraythree);
//			img_love6.setBackgroundResource(R.drawable.none_heart);

		}
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * 表77 获取镖师信息 /driver/driverDetail
	 */
	@Override
	public void getData() {
		Log.e("-----------------", "-----" + UrlMap.getUrl(MCUrl.DRIVERDETAIL, "driverId", driverId));
		// GET方法获取数据(镖师的基本信息)
		dialog.show();
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.DRIVERDETAIL, "driverId", driverId),
				new AsyncHttpResponseHandler() {
					private EscortInfoBean eBean;
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("xinklsd", new String(arg2));
						dialog.dismiss();
						eBean = new Gson().fromJson(new String(arg2), EscortInfoBean.class);
						if (eBean.getErrCode() == 0) {// 如果获取成功
							if (!eBean.data.equals("") && eBean.data.size() > 0) {
								text_name.setText(eBean.getData().get(0).userName);// 设置镖师姓名			
								if (!eBean.data.get(0).userHeadPath.equals("")) {
									loader.displayImage(eBean.data.get(0).userHeadPath, img_head, options);
								} else {
									img_head.setBackgroundResource(R.drawable.moren);
								}
							} else {
								ToastUtil.shortToast(getApplicationContext(), eBean.getMessage());
							}

						} else if (eBean.getErrCode() == -1) {
							ToastUtil.shortToast(getApplicationContext(), eBean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}

}
