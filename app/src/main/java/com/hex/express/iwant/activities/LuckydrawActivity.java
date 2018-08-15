package com.hex.express.iwant.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DrawPageBean;
import com.hex.express.iwant.bean.LuckyBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.Rotate;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Timer;
import java.util.TimerTask;

public class LuckydrawActivity extends BaseActivity{
	private Rotate rotate;
	private ImageView start_btn,btn_Left;
	private TextView fashions,jiangnumber,time_dao;
	private LuckyBean LuckyBean;
	private PopupWindow window02;
	private int time=2;
	private long exitTime;
	private  int currentTime;
	private  int curtime;
	private Timer timer=null;
	private TimerTask task;
	private static  int  cutime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luckdraw);
		rotate = (Rotate) this.findViewById(R.id.rotate);
		start_btn = (ImageView) this.findViewById(R.id.start_btn);
		btn_Left = (ImageView) this.findViewById(R.id.btn_Left);
		fashions = (TextView) this.findViewById(R.id.fashions);
		jiangnumber= (TextView) this.findViewById(R.id.jiangnumber);
		time_dao= (TextView) this.findViewById(R.id.time_dao);
		
		timer = new Timer();

//		Getmea();
		DrawPage();
		setOnClick();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
		resetCurrentTime();
        start_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Getmea();
//				if(!rotate.isRuning()){
//					rotate.luckyStart(1);
//					start_btn.setImageResource(R.drawable.bg_draw_start);
//					if (!rotate.isShoundEnd()) {
//						rotate.luckyEnd();
//					}
//					}
//				}
			}
		});
	 
		btn_Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curtime=cutime;
				finish();
				
			}
		});
		fashions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LuckydrawActivity.this, FashionActivity.class));

			}
		});
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 获取抽奖的概率
	 */
	public void Getmea(){
		start_btn.setClickable(false);
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.LuckyDial, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null,
				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111", "**********************"+ new String(arg2));
						LuckyBean= new Gson().fromJson(new String(arg2), LuckyBean.class);	
					
						if (LuckyBean.errCode==1) {
							sendEmptyBackgroundMessage(MsgConstants.MSG_01);
							
						}else if (LuckyBean.errCode==-1) {
							start_btn.setClickable(true);
							ToastUtil.shortToast(LuckydrawActivity.this, LuckyBean.message);
//							ToastUtil.shortToast(LuckydrawActivity.this, "sssss");
						}else if (LuckyBean.errCode==-4) {
							ToastUtil.shortToast(LuckydrawActivity.this, LuckyBean.message);//EcoinExchangeNoteActivity
								startActivity(new Intent(LuckydrawActivity.this, EcoinActivity.class));
//								intent.setClass(LuckydrawActivity.this, IntegralActivity.class);
						} 
//					
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
//						Log.e("1111111223", "**********************"
//								+ arg0);
						start_btn.setClickable(true);
					}
				});

	
	}
	/**
	 * 获取积分数量界面信息
	 */
	public void DrawPage(){
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.DrawPage, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null,
				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111信息", "**********************"+ new String(arg2));
						DrawPageBean bean = new Gson().fromJson(new String(arg2), DrawPageBean.class);
							if (bean.getErrCode()==0) {
								if (bean.data != null || bean.data.size() != 0){
									jiangnumber.setText(""+bean.data.get(0).eCoin);
									int number=Integer.parseInt(bean.data.get(0).luckyTime);;
									cutime=number;
									times();
								}
//								sendEmptyBackgroundMessage(MsgConstants.MSG_02);
							}
							if (bean.getErrCode()!=0) {
								ToastUtil.shortToast(LuckydrawActivity.this, bean.getMessage());
//								ToastUtil.shortToast(LuckydrawActivity.this, ""+bean.getErrCode());
							}
							
//					
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
//						Log.e("111111122", "**********************"
//								+ arg0);
					}
				});
	}
	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			if (LuckyBean.data.get(0).position==0) {
				if(!rotate.isRuning()){
					rotate.luckyStart(7);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			if (LuckyBean.data.get(0).position==1) {
				if(!rotate.isRuning()){
					rotate.luckyStart(1);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			if (LuckyBean.data.get(0).position==2) {
				if(!rotate.isRuning()){
					rotate.luckyStart(9);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			if (LuckyBean.data.get(0).position==3) {
				if(!rotate.isRuning()){
					rotate.luckyStart(10);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			if (LuckyBean.data.get(0).position==4) {
				if(!rotate.isRuning()){
					rotate.luckyStart(1);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			if (LuckyBean.data.get(0).position==5) {
				if(!rotate.isRuning()){
					rotate.luckyStart(2);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			
			if (LuckyBean.data.get(0).position==6) {
				if(!rotate.isRuning()){
					rotate.luckyStart(3);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
			if (LuckyBean.data.get(0).position==7) {
				if(!rotate.isRuning()){
					rotate.luckyStart(4);
					if (!rotate.isShoundEnd()) {
						rotate.luckyEnd();
						Luky(LuckyBean.data.get(0).drawLevel);
					}
					}
			}
				if (LuckyBean.data.get(0).position==8) {
					if(!rotate.isRuning()){
						rotate.luckyStart(1);
						if (!rotate.isShoundEnd()) {
							rotate.luckyEnd();
							Luky(LuckyBean.data.get(0).drawLevel);
						}
						}
				}
					if (LuckyBean.data.get(0).position==9) {
						if(!rotate.isRuning()){
							rotate.luckyStart(6);
							if (!rotate.isShoundEnd()) {
								rotate.luckyEnd();
								Luky(LuckyBean.data.get(0).drawLevel);
							}
							}
					}
			break;
//			case MsgConstants.MSG_02:
//				break;
		default:
			break;
		}
	}
	public void Luky(int item){
		
		new Timer().schedule(new TimerTask(){
			  public void run() {
		 sendEmptyUiMessage(MsgConstants.MSG_01);
	
			}}, 4500);
	  }
	private void times(){
		if (cutime!=0) {
			   task = new TimerTask() {
					@Override
					public void run() {
					sendEmptyUiMessage(MsgConstants.MSG_02);
					}
				};
						timer.schedule(task, 0, 1000);
		}else {
//			ToastUtil.shortToast(LuckydrawActivity.this, "cutime111   "+cutime);
			sendEmptyUiMessage(MsgConstants.MSG_03);
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			start_btn.setClickable(true);
			  showwindow();
			  DrawPage();
			  start_btn.setClickable(false);
//			  start_btn.setVisibility(View.INVISIBLE);
			  start_btn.setImageResource(R.drawable.draw_stope);
			  time_dao.setVisibility(View.VISIBLE);
			break;
			case MsgConstants.MSG_02:
				if (cutime == 0) {
//					ToastUtil.shortToast(LuckydrawActivity.this, "cutime2 "+cutime);
//					resetCurrentTime();
					start_btn.setClickable(true);
					start_btn.setImageResource(R.drawable.bg_draw_start);
					time_dao.setVisibility(View.GONE);
					task.cancel();
				} else{
					 start_btn.setClickable(false);
					 start_btn.setImageResource(R.drawable.draw_stope);
					  time_dao.setVisibility(View.VISIBLE);
//					ToastUtil.shortToast(LuckydrawActivity.this, "cutime3 "+cutime);
					resetCurrentTime();
					time_dao.setText(cutime-- + "秒");
				}
			break;
			case MsgConstants.MSG_03:
				if (cutime == 0) {
//					ToastUtil.shortToast(LuckydrawActivity.this, "cutime2 "+cutime);
					start_btn.setClickable(true);
					start_btn.setImageResource(R.drawable.bg_draw_start);
					time_dao.setVisibility(View.GONE);
//					task.cancel();
				} 
			break;
		default:
			break;
		}
	}
	
	private void resetCurrentTime() {
		currentTime = cutime;
	}

	/**
	 * 现金卷弹窗
	 */
	public void showwindow() {
	
		TextView luck_con;
		Button btnsaves_pan;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.luckpopwind_draw, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window02.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window02.setBackgroundDrawable(dw);
		window02.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window02.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window02.showAtLocation(LuckydrawActivity.this.findViewById(R.id.fashions), Gravity.CENTER, 0, 0);
		btnsaves_pan=(Button) view.findViewById(R.id.btnsaves_pan);
		luck_con=(TextView) view.findViewById(R.id.luck_con);
		luck_con.setText(""+LuckyBean.getData().get(0).drawName);
		btnsaves_pan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Award();
				window02.dismiss();
							
			}
		});
		
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}
	/**
	 * 获取抽奖界面信息
	 */
	public void Award(){
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.Draw, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)),"drawLevel",""+LuckyBean.getData().get(0).drawLevel,"recId",""+LuckyBean.getData().get(0).recId), null,
				null, null, new AsyncHttpResponseHandler() {
//		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.Draw, "userId", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID)),"drawLevel",""+LuckyBean.getData().get(0).drawLevel), null,
//				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111领奖", "**********************"
								+ new String(arg2));
				BaseBean eBean= new Gson().fromJson(new String(arg2), BaseBean.class);
				if (LuckyBean.getData().get(0).drawLevel!=10) {
					ToastUtil.shortToast(LuckydrawActivity.this, eBean.getMessage());
					
				}
			
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
//						Log.e("111111122", "**********************"
//								+ arg0);
					}
				});
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		if (timer != null)
//			timer.cancel();
//		if (task != null)
//			task.cancel();
	}
//	protected void o 
//		// TODO Auto-generated method stub
//		super.onPause();
//		curtime=time;
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
		   timer=null;
		if (task != null)
			task.cancel();
		    task=null;
	}
	 @Override  
     public boolean onKeyDown(int keyCode, KeyEvent event) {  
         if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
            
              return false;  
         }else {  
             return super.onKeyDown(keyCode, event);  
         }  
           
     }  
       
}
