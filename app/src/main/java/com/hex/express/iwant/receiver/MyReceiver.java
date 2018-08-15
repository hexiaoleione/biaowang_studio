package com.hex.express.iwant.receiver;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.base.BaseActivity;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.CardActivity;
import com.hex.express.iwant.activities.DownWindSpeedActivity;
import com.hex.express.iwant.activities.DrawCardActivity;
import com.hex.express.iwant.activities.EscoreDartEvaluteActivity;
import com.hex.express.iwant.activities.GuardActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.MyCourierActivity;
import com.hex.express.iwant.activities.MyPickupActivity;
import com.hex.express.iwant.activities.MyWalletActivity;
import com.hex.express.iwant.activities.NewDrawCardActivity;
import com.hex.express.iwant.activities.NewMyWalletActivity;
import com.hex.express.iwant.activities.PickCenterActivity;
import com.hex.express.iwant.activities.PickOrderActivity;
import com.hex.express.iwant.activities.SpreadActivity;
import com.hex.express.iwant.activities.MyDownwindActivity;
import com.hex.express.iwant.constance.JpushConstance;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.iflytek.TtsDemo;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.service.FloatuiService;
import com.hex.express.iwant.service.MediaplayService;
import com.hex.express.iwant.service.MediaplaytoService;
import com.hex.express.iwant.utils.BadgeUtil;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;

import android.R.integer;
import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	private JSONObject obj;
	//定义日志标签  
    private static final String TAG = "Test";  
    int i=0;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e("action", intent.getAction());
		Log.e("acti", intent.toString());
		Log.e("vvvv1", intent.getStringExtra("action") + "d");
		Bundle bundle = intent.getExtras();
		Log.e("bbbb", bundle.toString());
		Log.e("vvvv2", bundle.getString("action") + "d");
		// 极光推送注册
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.e("alias", regId);
			// PreferenceUtils.setString(context,
			// PreferenceConstants.REGISTRATIONID, regId);
		}
		// 接收到推送下来的自定义消息
		else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			/*
			 * Intent data = new Intent();
			 * data.setAction(HomeActivity.MESSAGE_RECEIVED_ACTION);
			 * data.putExtra(HomeActivity.KEY_MESSAGE,
			 * bundle.getString("cn.jpush.android.MESSAGE"));
			 * data.putExtra(HomeActivity.KEY_EXTRAS,
			 * bundle.getString("cn.jpush.android.EXTRA"));
			 * context.sendBroadcast(data);
			 */
//			Noticer(context);
//			ToastUtil.shortToast(context, "接收到推送下来的自定义通知");
		}
		// 接收到推送下来的通知
		else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			/*
			 * Intent msg_intent = new Intent(context,MyWalletActivity.class);
			 * msg_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * context.startActivity(msg_intent);
			 */
//			Noticer(context);
//			i++;
//			BadgeUtil.setBadgeCount(context, i, R.drawable.ic_launcher);
//		
			Intent msg_intent = new Intent();
			msg_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String extra = bundle.getString("cn.jpush.android.EXTRA");
			String value = "";
			String message="";
			try {
				obj = new JSONObject(extra);
				value = obj.optString("action");
				message= obj.optString("message");
				Log.e("111111falg", obj.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (value.equals(""))
				return;
			if (JpushConstance.afterPublish.equals(value) ) {//用户发布的抢单任务，给快递员推送跳到抢单大厅
			//	ToastUtil.shortToast(context, "接收到推送下来的通知");
				msg_intent.setClass(context, MediaplayService.class);
//				msg_intent.setClass(context, TtsDemo.class);
				
				msg_intent.putExtra("message", message);
//				context.startActivity(msg_intent);
				context.startService(msg_intent);
				stoop(context);
			}else 
			//用户收到订单已被接
			if (JpushConstance.afterOrder.equals(value) ) {
			//	ToastUtil.shortToast(context, "接收到推送下来的通知");
				msg_intent.setClass(context, MediaplaytoService.class);
//				msg_intent.setClass(context, TtsDemo.class);
				
				msg_intent.putExtra("message", message);
//				context.startActivity(msg_intent);
				context.startService(msg_intent);
				stoopto(context);
			}
			
		}
		// 用户点击打开了通知
		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Intent msg_intent = new Intent();
			msg_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String extra = bundle.getString("cn.jpush.android.EXTRA");
			String value = "";
//			BadgeUtil.resetBadgeCount(context,  R.drawable.ic_launcher);
			try {
				obj = new JSONObject(extra);
				value = obj.optString("action");
				Log.e("111111falg", obj.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (value.equals(""))
				return;
			if (JpushConstance.TRANSFER_ACTION.equals(value)) {// 钱包转账通知
//				msg_intent.setClass(context, MyWalletActivity.class);
				msg_intent.setClass(context, NewMyWalletActivity.class);
//				Intent intents = new Intent(context, FloatuiService.class);
//				Intent intents = new Intent(context, NewMyWalletActivity.class);
				context.startService(msg_intent);
//				msg_intent.setClass(context, FloatuiService.class);
//				context.startActivity(msg_intent);
//				msg_intent.setClass(context, MediaplayService.class);
//				context.stopService(msg_intent);
				
			}
			if (JpushConstance.COURIER_AUTH.equals(value)) {//快递员认证
				String flag = obj.optString("flag");
				String message = obj.optString("message");
				msg_intent.putExtra("flag", flag);
				msg_intent.putExtra("message", message);
				msg_intent.setAction("usertype");
				Log.e("flag1", flag);
				context.sendBroadcast(msg_intent);
			}
			if (JpushConstance.SYSTEM_MESSAGE_ACTION.equals(value)) {// 系统消息
				msg_intent.setClass(context, MessageActivity.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.COUPONVIEWCONTROLLER.equals(value)) {//现金卷
//				msg_intent.setClass(context, CardActivity.class);
//				msg_intent.setClass(context, DrawCardActivity.class);	
				msg_intent.setClass(context, NewDrawCardActivity.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.DRIVEWJUMP.equals(value)) {//镖师奖励
				msg_intent.setClass(context, GuardActivity.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.AFTER_GRABEXPRESS.equals(value)) {// 快递员抢单成功通知
//				msg_intent.setClass(context, MyCourierActivity.class);
				msg_intent.setClass(context, MainTab.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.DRIVER_AUTH.equals(value)) {//镖师认证通过的推送 flag是usertype字段
//				String flag = obj.optString("flag");
//				String message = obj.optString("message");
//				msg_intent.putExtra("flag", flag);
//				msg_intent.putExtra("message", message);
//				msg_intent.setAction("usertype");
//				context.sendBroadcast(msg_intent);
//				msg_intent.setClass(context, GuardActivity.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.TRUE_TAKE.equals(value)) {//镖师押镖完成的推送 flag里包含driverId和recId
				String flag = obj.optString("flag");
				Log.e("flag44", flag);
				String[] s = flag.split("_");
				String driverId=s[0];
				String recId=s[1];;
				Log.e("driverId", driverId);
				Log.e("recId", recId);
				msg_intent.putExtra("recId", recId);
				msg_intent.putExtra("driverId", driverId);
				msg_intent.setClass(context, EscoreDartEvaluteActivity.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.AFTER_GRABDOWNWINDTASK.equals(value)) {//镖师接镖以后用户收到的推送
//				msg_intent.setClass(context, MyDownwindActivity.class);
//				msg_intent.setClass(context, MainTab.class);
				msg_intent.setClass(context, NewMainActivity.class);
				
				context.startActivity(msg_intent);
			}
			if (JpushConstance.NEARBYAFTERPUBLISH.equals(value)) {//用户发布的抢单任务，给快递员推送跳到抢单大厅
//				msg_intent.setClass(context, MainTab.class);
				msg_intent.setClass(context, NewMainActivity.class);
				context.startActivity(msg_intent);
			}
			if (JpushConstance.NEARBYAFTERPUBLISHTODOWNWINDTASK.equals(value)) {//用户发了顺风任务，给附近镖师的推送
//				msg_intent.setClass(context, DownWindSpeedActivity.class).putExtra("downwind", "escort");
//				msg_intent.setClass(context, MainTab.class);
				msg_intent.setClass(context, NewMainActivity.class);
				context.startActivity(msg_intent);
//				msg_intent.setClass(context, MediaplayService.class);
//				context.stopService(msg_intent);
			}
		}
		// 用户收到到RICH PUSH CALLBACK
		// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
		else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			// Log.e("msg", intent.getAction()+"");
			Intent msg_intent = new Intent(context, NewMyWalletActivity.class);
			context.startActivity(msg_intent);
		}
		
		// connected state change to
		else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
		}
		// 未处理的通知
		else {
		}
		
	}
	public void Noticer(Context context){
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
	builder.statusBarDrawable = R.drawable.ic_luncher;
	builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
	        | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
	builder.notificationDefaults = Notification.DEFAULT_SOUND
	        | Notification.DEFAULT_VIBRATE
	        | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
	JPushInterface.setPushNotificationBuilder(1, builder);
	}
	//标签
//	public static void setTags(Context context, Set<String> tags, TagAliasCallback callback){
//		
//		
//	}
	//别名
//	public void gotResult(int responseCode, String alias, Set<String> tags){
//		
//	}
	
	private void showData(Bundle bundle) {
		// for(String key : bundle.keySet())
		// LogUtils.e(key + " = " + bundle.get(key).toString());
	}
	private void stoop(final Context  contexts){
	new Timer().schedule(new TimerTask(){
		  public void run() {
			  Intent msg_intent = new Intent();
				msg_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			  msg_intent.setClass(contexts, MediaplayService.class);
				contexts.stopService(msg_intent);

		}}, 3000);
}
	private void stoopto(final Context  contexts){
		new Timer().schedule(new TimerTask(){
			  public void run() {
				  Intent msg_intent = new Intent();
					msg_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				  msg_intent.setClass(contexts, MediaplaytoService.class);
					contexts.stopService(msg_intent);

			}}, 3000);
	}
}
