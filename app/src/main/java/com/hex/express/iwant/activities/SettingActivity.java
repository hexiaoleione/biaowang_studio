package com.hex.express.iwant.activities;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.hex.express.iwant.R;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;

public class SettingActivity extends BaseActivity {
	@Bind(R.id.tv_about)
	TextView tv_about;
	@Bind(R.id.tv_share)
	TextView tv_share;
	@Bind(R.id.tv_feedback)
	TextView tv_feedback;
	@Bind(R.id.tv_unlogin)
	TextView tv_unlogin;
	@Bind(R.id.ll_unlogin)
	LinearLayout ll_unlogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.bind(this);
		initView();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.tv_feedback, R.id.tv_share, R.id.tv_about,R.id.tv_unlogin })
	public void onMyClick(View view) {
		switch (view.getId()) {
		case R.id.tv_feedback:
			startActivity(new Intent(SettingActivity.this,FeedBackActivity.class));
			break;
		case R.id.tv_share:
			startActivity(new Intent(SettingActivity.this,ShareActivity.class));
			//showShare();
			break;
		case R.id.tv_about:
			startActivity(new Intent(SettingActivity.this,AboutActivity.class));
			break;
		case R.id.tv_unlogin:
			//Intent intent=new Intent();
			PreferencesUtils.clear(getApplicationContext());
			fileDelete("/sdcard/myHead/head.png");
//			startActivity(new Intent(SettingActivity.this, MainActivity.class));
//			startActivity(new Intent(SettingActivity.this, MainTab.class));
			startActivity(new Intent(SettingActivity.this, NewMainActivity.class));
			
			ToastUtil.shortToast(getApplicationContext(), "退出成功");
			break;
		default:
			break;
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if(PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN)==true){
			ll_unlogin.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	private void fileDelete(String path){
		if(fileIsExist(path)){
			File file=new File(path);
			file.delete();
		}
	}
	private boolean fileIsExist(String path){
		File file = new File(path);
		if(file.exists()){
			return true;
		}
		return false;
	}
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 Log.e("ccc", "sss");
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("我推荐镖王app");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("来自镖王的分享");
		 
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImageUrl("http://www.efamax.com/images/5.png");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("https://itunes.apple.com/cn/app/e-fa/id1031426530?mt=8");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("https://itunes.apple.com/cn/app/e-fa/id1031426530?mt=8");
		 oks.setTitleUrl("https://itunes.apple.com/cn/app/e-fa/id1031426530?mt=8");
		// 启动分享GUI
		 oks.show(this);
		 }
}
