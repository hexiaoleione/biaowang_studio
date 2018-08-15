package com.hex.express.iwant.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

public class IdCardActivity extends BaseActivity {
	@Bind(R.id.iv_show)
	ImageView iv_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idcard);
		//iv_show.setB
		ButterKnife.bind(this);
		if(!"".equals(getIntent().getStringExtra("iconpath"))){
		if (getIntent().getStringExtra("iconpath").equals("goodpath") ) {
			if(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.GOODPATH)!=null&&!PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.GOODPATH).equals("")){
				Picasso.with(getApplicationContext()).load(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.GOODPATH)).error(R.drawable.camre_img).into(iv_show);
			}else{
				Picasso.with(getApplicationContext()).load(R.drawable.camre_img).into(iv_show);
			}
		}
		}else {
			if(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH)!=null&&!PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH).equals("")){
				Picasso.with(getApplicationContext()).load(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH)).error(R.drawable.id_icon).into(iv_show);
			}else{
				Picasso.with(getApplicationContext()).load(R.drawable.id_icon).into(iv_show);
			}
		
		}
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			finish();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
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

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
