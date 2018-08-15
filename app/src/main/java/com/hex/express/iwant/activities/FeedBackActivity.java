package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FeedBackActivity extends BaseActivity {
	@Bind(R.id.rb_advice)RadioButton rb_advice;
	@Bind(R.id.rb_else) RadioButton rb_else;
	@Bind(R.id.rb_feedback)RadioButton rb_feedback;
	@Bind(R.id.et_content) EditText et_content;
	@Bind(R.id.btn_finish) Button btn_finish;
	@Bind(R.id.tbv_show) TitleBarView tbv_show;
	@Bind(R.id.rg)RadioGroup rg;
	@Bind(R.id.btn_Left) ImageView btn_Left;
	
	int checkId;
	int ii;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		ButterKnife.bind(this);
		initData();
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				checkId=checkedId;
				Log.e("11111", checkedId+"");
			}
		});
		btn_Left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}
	@OnClick(R.id.btn_finish)
	public void onMyClick(View view){
		if(et_content.getText().toString().equals("")){
			ToastUtil.shortToast(getApplicationContext(), "请输入内容");
			return;
		}
		  String str = String.valueOf(checkId);
		   int i = str.length();
		   String lastchar = str.substring(i-1, i);
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
			obj.put("typeId",lastchar);
			obj.put("content", et_content.getText().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("11111", obj.toString());
		AsyncHttpUtils.doPostJson(getApplicationContext(),MCUrl.FEEDBACK, obj.toString(),new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if(arg2==null)
					return;
				BaseBean bean=new Gson().fromJson(new String(arg2),BaseBean.class);
				if(bean.getErrCode()==0)
					ToastUtil.shortToast(getApplicationContext(), "反馈成功");
//				startActivity(new Intent(FeedBackActivity.this, MainActivity.class));
//				startActivity(new Intent(FeedBackActivity.this, MainTab.class));
				startActivity(new Intent(FeedBackActivity.this, NewMainActivity.class));
				
				finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tbv_show.setTitleText("意见反馈");
		rb_feedback.setChecked(true);
		checkId=R.id.rb_feedback;
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
