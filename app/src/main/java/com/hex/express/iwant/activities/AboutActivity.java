package com.hex.express.iwant.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.VersionBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.UpdateChecker;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	@Bind(R.id.version)
	TextView version;
	@Bind(R.id.ll_down)
	LinearLayout ll_down;
	private VersionBean bean2;
	private String androidApkPath;
	private String androidVersion = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ButterKnife.bind(this);
		
		initData();
		getVersions();
		setOnClick();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}
	private String getCurrVersion() {
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = info.versionName;
		return version;
	}
	@OnClick({R.id.ll_function,R.id.ll_com,R.id.ll_service,R.id.ll_sfan,R.id.ll_slian})
	public void onMyClick(View view){
		switch (view.getId()) {
		case R.id.ll_function:
			startActivity(new Intent(AboutActivity.this,FunctionActivity.class));
			break;
		case R.id.ll_com:
			startActivity(new Intent(AboutActivity.this, ComActivity.class));
			break;
		case R.id.ll_service:
			startActivity(new Intent(AboutActivity.this,ServiceActivity.class));
			break;
		case R.id.ll_sfan:
			startActivity(new Intent(AboutActivity.this,FeedBackActivity.class));
			break;
		case R.id.ll_slian:
			AppUtils.intentDial(AboutActivity.this, "01052873062");
			break;
			
		default:
			break;
		}
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		version.setText("v."+getCurrVersion());
btn_Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		if (!getCurrVersion().equals(androidVersion)) {
//			ll_down.setVisibility(View.VISIBLE);
		}else {
			ll_down.setVisibility(View.GONE);
			
		}
			ll_down.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					upVersion(androidVersion);
					
				}
			});

			
	
		
	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	/**
	 * 获取当前服务器上最新版本号
	 */
	public String getVersions() {
		AsyncHttpUtils.doSimGet(MCUrl.VERSION, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg2 == null)
					return;
				bean2 = new Gson().fromJson(new String(arg2), VersionBean.class);
				Log.e("bean2", new String(arg2));
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.VERSION,
						bean2.data.get(0).androidVersion);
				Log.e("beanyyyy", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.VERSION));
				androidVersion = bean2.data.get(0).androidVersion;
			}
		});
		return androidVersion;
	}
	public void upVersion(String version) {
		Log.e("PPPP", version);
		if (!getCurrVersion().equals(version)) {
			AlertDialog.Builder ad = new AlertDialog.Builder(AboutActivity.this);
			ad.setTitle("版本更新");
			ad.setCancelable(false);
			ad.setMessage("有新的版本需要更新，安装最新版本才能正常使用");
			ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					new UpdateChecker(AboutActivity.this, MCUrl.VERSION).checkForUpdates();
				}
			});
			ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			});
			ad.create().show();
			
		}
	}
}
