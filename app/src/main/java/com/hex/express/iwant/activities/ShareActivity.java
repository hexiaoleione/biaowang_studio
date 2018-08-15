package com.hex.express.iwant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;
public class ShareActivity extends BaseActivity {
	@Bind(R.id.ll_weixin)
	LinearLayout ll_weixin;
	@Bind(R.id.ll_qq)
	LinearLayout ll_qq;
	@Bind(R.id.ll_zone)
	LinearLayout ll_zone;
	@Bind(R.id.btn_Lefts)
	ImageView btnLeft;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		ButterKnife.bind(this);
		initData();

	}
	private void showShare(String platform) {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 Log.e("ccc", "sss");
		 //关闭sso授权
		 oks.setSilent(false);
		 oks.setDialogMode();
		 oks.setTitle("我推荐镖王app"); 
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("来自镖王的分享");
		 
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImageUrl("http://www.efamax.com/images/logo3.png");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://www.efamax.com/user_dl.html");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://www.efamax.com/user_dl.html");
		 oks.setTitleUrl("http://www.efamax.com/user_dl.html");
		// 启动分享GUI
		 if(platform!=null){
			 oks.setPlatform(platform);
		 }
		// oks.setPlatform(platform)
		 oks.show(this);
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
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(ShareActivity.this, UserCenterActivity.class));
				finish();
			}
		});

	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.ll_qq, R.id.ll_zone, R.id.ll_weixin,R.id.ll_weixin_circle})
	public void onMyClick(View view) {
		switch (view.getId()) {
		case R.id.ll_qq:
			showShare(QQ.NAME);
			break;
		case R.id.ll_weixin:
			showShare(Wechat.NAME);
			break;
		case R.id.ll_weixin_circle:
			showShare(WechatMoments.NAME);
			break;
		case R.id.ll_zone:
			showShare(QZone.NAME);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
}
