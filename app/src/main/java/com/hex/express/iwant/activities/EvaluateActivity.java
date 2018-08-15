package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 评价界面
 * 
 * @author SCHT-50
 * 
 */

public class EvaluateActivity extends BaseActivity {
	@Bind(R.id.rab_couriersscore)
	RatingBar rab_couriersscore;
	@Bind(R.id.edit_text)
	EditText edit_text;
	@Bind(R.id.submit)
	Button btn_submit;
	private int businessId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
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
		businessId = Integer.parseInt(getIntent().getStringExtra("businessId"));
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
				JSONObject obj = new JSONObject();
				try {
					obj.put("businessId", businessId);
					obj.put("evaTypeId", 1);
					obj.put("score",
							(double) (rab_couriersscore.getRating() * 2));
					obj.put("evaContent", edit_text.getText().toString());
					Log.e("json", obj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AsyncHttpUtils.doPostJson(EvaluateActivity.this,
						MCUrl.EVALUATION, obj.toString(),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								dialog.dismiss();
//								startActivity(new Intent(EvaluateActivity.this,
//										MainActivity.class));
//								startActivity(new Intent(EvaluateActivity.this,
//										MainTab.class));
								startActivity(new Intent(EvaluateActivity.this,
										NewMainActivity.class));
								
								finish();
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub

							}
						});

			}
		});

	}

	@Override
	public void setOnClick() {

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
	}
}
