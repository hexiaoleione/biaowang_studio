package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 
 * 
 * @author huyichuan
 *操作指导
 */
public class OperationActivity extends BaseActivity{
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_operation);//
		ButterKnife.bind(OperationActivity.this);
		initView();
		initData();
		setOnClick();
		
	}
	@OnClick({R.id.ll_yonghu,R.id.ll_biaoshi,R.id.ll_wulius,R.id.ll_caozuos,R.id.ll_qita,R.id.ll_gongneng})
	public void onMyClick(View view){
		Intent intent = new Intent(OperationActivity.this,OperationHtmlActivity.class);
		switch (view.getId()) {
		case R.id.ll_yonghu:
			
			intent.putExtra("url", "http://www.efamax.com/mobile/manual/userManual.html");
				startActivity(intent);
			break;
		case R.id.ll_biaoshi:
			intent.putExtra("url", "http://www.efamax.com/mobile/manual/diverManual.html");
				startActivity(intent);
			break;
		case R.id.ll_wulius:
			intent.putExtra("url", "http://www.efamax.com/mobile/manual/logDiverManual.html");
				startActivity(intent);
			break;
		case R.id.ll_caozuos:
			intent.putExtra("url", "http://www.efamax.com/mobile/manual/videoList.html");
				startActivity(intent);
			break;
		case R.id.ll_qita:
			intent.putExtra("url", "http://www.efamax.com/mobile/manual/else.html");
				startActivity(intent);
			break;
		case R.id.ll_gongneng:
			startActivity(new Intent(OperationActivity.this, FunctionActivity.class));
			break;
		default:
			break;
		}
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		btn_Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
