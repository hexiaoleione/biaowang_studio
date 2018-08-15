package com.hex.express.iwant.newsmain;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.madmatrix.zxing.android.CaptureActivity;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.AboutActivity;
import com.hex.express.iwant.activities.FeedBackActivity;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.NewCenterActivity;
import com.hex.express.iwant.activities.NewExerciseActivity;
import com.hex.express.iwant.activities.NewMyWalletActivity;
import com.hex.express.iwant.activities.OperationActivity;
import com.hex.express.iwant.activities.RegisterSetImageAndNameActivity;
import com.hex.express.iwant.activities.RoleAuthenticationActivity;
import com.hex.express.iwant.activities.ShareActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.MyPaidActivity;
import com.hex.express.iwant.newactivity.PickActivity;
import com.hex.express.iwant.newactivity.PickToActivity;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import android.view.ViewGroup;

/**
 * @date 2017
 * @author zwy
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View todayView;
	private View lastListView;
	private View discussView,tvDiscussto;
	private View favoritesView;
	private View commentsView;
	private View settingsView;
	private View 	ll_phone,ll_feedback,ll_caozuo,ll_about,ll_tui,tvdaifu;
	private RegisterBean bean;
	private NetworkInfo info;
	private ImageView img_head,imageView1;
	public ImageLoader loader;
	public DisplayImageOptions options;
	private TextView txt_username,txt_fen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		todayView = view.findViewById(R.id.saomiao);
		lastListView = view.findViewById(R.id.ll_wallet);
		discussView = view.findViewById(R.id.tvDiscussMeeting);
		tvDiscussto = view.findViewById(R.id.tvDiscussto);
		favoritesView = view.findViewById(R.id.tvMyFavorites);
		commentsView = view.findViewById(R.id.tvMyComments);
		settingsView = view.findViewById(R.id.ll_realname);
		ll_phone= view.findViewById(R.id.ll_phone);
		ll_feedback= view.findViewById(R.id.ll_feedback);
		ll_caozuo= view.findViewById(R.id.ll_caozuo);
		ll_about= view.findViewById(R.id.ll_about);
		ll_tui= view.findViewById(R.id.ll_tui);
		tvdaifu= view.findViewById(R.id.tvdaifu);
		img_head= (ImageView) view.findViewById(R.id.img_head);
		imageView1= (ImageView) view.findViewById(R.id.imageView1);
		txt_username= (TextView) view.findViewById(R.id.txt_username);
		txt_fen= (TextView) view.findViewById(R.id.txt_fen);
		
		
		todayView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		favoritesView.setOnClickListener(this);
		commentsView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
		tvDiscussto.setOnClickListener(this);
		tvdaifu.setOnClickListener(this);
		
		ll_phone.setOnClickListener(this);
		ll_feedback.setOnClickListener(this);
		ll_caozuo.setOnClickListener(this);
		ll_about.setOnClickListener(this);
		ll_tui.setOnClickListener(this);
		img_head.setOnClickListener(this);
		txt_fen.setOnClickListener(this);
		
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(getContext()));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ren4) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ren4) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ren4) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(300))// 设置成圆角图片
				.build();
		if (PreferencesUtils.getBoolean(getActivity(), PreferenceConstants.ISLOGIN)) {
			getrequstBalance();
			getMessageStutas();
		}
		
	}
	 
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (PreferencesUtils.getBoolean(getActivity(), PreferenceConstants.ISLOGIN)) {
			getrequstBalance();
			getMessageStutas();
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		
		case R.id.img_head: // 头像
			startActivity(new Intent(getActivity(),
					RegisterSetImageAndNameActivity.class).putExtra("tiaoguo", "2"));
			break;
		case R.id.txt_fen: // 
			startActivity(new Intent(getActivity(), ShareActivity.class));
			break;
			
		case R.id.saomiao: // 扫描登录
//			newContent = new HomSubFragment1();
//			title = getString(R.string.today);
//			startActivity(new Intent(getActivity(), NewCenterActivity.class));
			startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 1);	
			break;
		case R.id.ll_wallet:// 钱包
//			newContent = new LastListFragment();
//			title = getString(R.string.lastList);
			startActivity(new Intent(getActivity(), NewMyWalletActivity.class));
			break;
		case R.id.tvDiscussMeeting: // 我发的单
//			newContent = new DiscussFragment();
//			title = getString(R.string.discussMeetting);
			startActivity(new Intent(getActivity(), PickActivity.class));
			break;
		case R.id.tvDiscussto: // 我接的单
//			newContent = new DiscussFragment();
//			title = getString(R.string.discussMeetting);
			startActivity(new Intent(getActivity(), PickToActivity.class));
			break;
		case R.id.tvdaifu: // 我的代付
//			newContent = new DiscussFragment();
//			title = getString(R.string.discussMeetting);
			startActivity(new Intent(getActivity(), MyPaidActivity.class));
			break;
			
		case R.id.tvMyFavorites: // 活动
//			newContent = new MyFavoritesFragment();
//			title = getString(R.string.myFavorities);
			startActivity(new Intent(getActivity(), NewExerciseActivity.class));
			break;
		case R.id.tvMyComments: // 消息中心
//			newContent = new MyCommentsFragment();
//			title = getString(R.string.myComments);
			startActivity(new Intent(getActivity(), MessageActivity.class));
			break;
		case R.id.ll_realname: // 认证
//			newContent = new MySettingsFragment();
			Intent intents=new Intent();
			intents.putExtra("tiaoguo", "2");
			intents.setClass(getActivity(), RoleAuthenticationActivity.class);
			startActivity(intents);
			break;
		case R.id.ll_phone: // 理赔
//			newContent = new MyCommentsFragment();
//			title = getString(R.string.myComments);
			AppUtils.intentDial(getActivity(), "4006095509");//95509
			break;
		case R.id.ll_feedback: // 反馈意见
//			newContent = new MyCommentsFragment();
//			title = getString(R.string.myComments);
			startActivity(new Intent(getActivity(), FeedBackActivity.class));
			break;
		case R.id.ll_caozuo: // 操作指南
//			newContent = new MyCommentsFragment();
//			title = getString(R.string.myComments);
			startActivity(new Intent(getActivity(), OperationActivity.class));
			break;
		case R.id.ll_about: // 关于
//			newContent = new MyCommentsFragment();
//			title = getString(R.string.myComments);
			startActivity(new Intent(getActivity(), AboutActivity.class));
			break;
		case R.id.ll_tui: // 退出
			Builder ad = new Builder(getActivity());
			ad.setTitle("退出登录");
			ad.setMessage("确认是否退出登录？");
			ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					PreferencesUtils.clear(getContext());
//					PreferencesUtils.clearuid(getApplicationContext());
					fileDelete("/sdcard/myHead/head.png");
					txt_username.setText("");
					img_head.setBackgroundDrawable(null);
					img_head.setBackgroundResource(R.drawable.ren4);
					startActivity(new Intent(getActivity(), LoginWeixinActivity.class));
					ToastUtil.shortToast(getContext(), "退出成功");
					JPushInterface.setAliasAndTags(getContext(), "", null,new TagAliasCallback() {
						@Override
						public void gotResult(int i, String s, Set<String> set) {
							if (i == 0) {
								Log.d("Jpush", s);
							} else {
								Log.d("Jpush", "error code is " + i);
							}
						}
					});
			
				}
			});
			ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.create().show();
			break;
		default:
			break;
		}
		if (newContent != null) {
//			switchFragment(newContent, null);
		}
	}

	
	/**
	 * 切换fragment
	 * @param fragment
	 */
//	private void switchFragment(Fragment fragment, String title) {
//		if (getActivity() == null) {
//			return;
//		}
//		if (getActivity() instanceof NewMainActivity) {
//			NewMainActivity fca = (NewMainActivity) getActivity();
//			fca.switchConent(fragment, title);
//		}
//	}
	
	/**
	 * 获取钱包余额
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("11json", "" + new String(arg2));
						bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getData() == null || bean.getData().size() == 0)
							return;
						// double类型保留小数点位数
						DecimalFormat df = new DecimalFormat("######0.00");
//						txt_balance.setText(df.format(bean.data.get(0).balance));
						if (!bean.data.get(0).userName.equals("")&&!bean.data.get(0).userName.equals("null")) {
							txt_username.setText(bean.data.get(0).userName);
						} else {
							txt_username.setText(bean.data.get(0).mobile);
						}
						try {
							PreferencesUtils.putInt(getActivity(), PreferenceConstants.UID, bean.data.get(0).userId);	
						} catch (Exception e) {
							// TODO: handle exception
						}
//						try {
//						if (!"".equals(bean.data.get(0).headPath)) {
//						PreferencesUtils.putString(getActivity(), PreferenceConstants.HeadPath, bean.getData().get(0).headPath);
//						}
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//						if (!"".equals(bean.data.get(0).idCardPath)) {
//							PreferencesUtils.putString(getActivity(), PreferenceConstants.ICON_PATH,bean.data.get(0).idCardPath);
//						}
						try {
						if (!"".equals(bean.data.get(0).userType)) {
							  PreferencesUtils.putString(getActivity(), PreferenceConstants.USERTYPE,bean.getData().get(0).userType);
						}
						} catch (Exception e) {
							// TODO: handle exception
						}
						try{
						if (!"".equals(bean.data.get(0).wlid)) {
					    PreferencesUtils.putString(getActivity(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
						try{
						if (!"".equals(bean.data.get(0).agreementType)) {
						 PreferencesUtils.putString(getActivity(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

					    if (!bean.data.get(0).headPath.equals("") && bean.data.get(0).headPath!=null) {
								loader.displayImage(bean.data.get(0).headPath, img_head, options);
								// new MyBitmapUtils().display(img_head,
								// bean.data.get(0).headPath);
							} else {
								img_head.setBackgroundResource(R.drawable.ren4);
							}

					}

				});
	}
	private void fileDelete(String path) {
		if (fileIsExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}

	private boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("requestCode     ",""+requestCode);
		Log.e("resultCode     ",""+resultCode);
		Log.e("data     ",""+data);
		switch (requestCode) {
		
		case 1:
			if (requestCode == 1 && data != null) {
				Bundle bundle = data.getExtras();
				
				Log.e("111111111     ",""+bundle.getString("result"));
				Log.e("111111111     ",""+bundle.getString("errCode"));
				Log.e("111111111     ",""+bundle.getBundle("success"));
//				codeId = bundle.getString("codeId");
//				phone= bundle.getString("phone");
			     if(!bundle.getString("result").equals("") && bundle.getString("result")!=null){
			    	 getLlsurplus(bundle.getString("result"));
			     }
				
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 信息是否有未读的状态
	 */
	private void getMessageStutas() {
		RequestParams params = new RequestParams();
		Log.e("mdgread", UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("message", "" + new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() > 0) {
							imageView1.setBackgroundResource(R.drawable.ce_newciaoxion);
						} else if (bean.getErrCode() == -2) {
							imageView1.setBackgroundResource(R.drawable.ce_newxiaoxi);
						}
					}

				});
	}
	/**
	 * 二维码登录
	 */
	private void getLlsurplus(String codeid) {
//		if (!tv_needPayMoney.getText().toString().equals("")) {
		String str = codeid;   
		boolean isNum = str.matches("[0-9]+");  
		if (isNum) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("codeId", codeid);
				obj.put("idCard", PreferencesUtils.getString(getActivity(), PreferenceConstants.IDCARD));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("1111111 mlist    ", obj.toString());
			AsyncHttpUtils.doPostJson(getActivity(), MCUrl.THINKCHANGE, obj.toString(),
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("josnnd", new String(arg2));
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
								ToastUtil.shortToast(getActivity(), bean.getMessage());
						}
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
						}
					});
		} else {
			ToastUtil.shortToast(getActivity(), "二维码不正确");
		}

	}
}
