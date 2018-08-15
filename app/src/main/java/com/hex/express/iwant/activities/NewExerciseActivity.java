package com.hex.express.iwant.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.ExerciseAdapter;
import com.hex.express.iwant.bean.ExerciseBean;
import com.hex.express.iwant.bean.ExerciseBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExerciseActivity extends BaseActivity{
	
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	@Bind(R.id.ptrl_exerc)
	PullToRefreshListView ptr_vd;
	@Bind(R.id.null_message)
	View view_null_message;
	ListView listView;
	@Bind(R.id.exe_xianjinjuan)
	ImageView exe_xianjinjuan;
	@Bind(R.id.exe_xianjinchong)
	ImageView exe_xianjinchong;
	
	@Bind(R.id.relativeLayout1se)
	RelativeLayout relativeLayout1;
	
	
	ExerciseAdapter excadapter;
	ExerciseBean exebean;
	List<ExerciseBean.Data> mList;
	List<ExerciseBean.Data> mList2;
	protected int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_exercises);//activity_exercise activity_exercises
		ButterKnife.bind(this);
		initData();
		getData();
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
		listView = ptr_vd.getRefreshableView();
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
  btn_Left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
  exe_xianjinjuan.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//现金卷
		Intent intent=new Intent();
		intent.setClass(NewExerciseActivity.this, NewDrawCardActivity.class);//NewDrawCardActivity  DrawCardActivity
		startActivity(intent);
	}
});
  exe_xianjinchong.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();//充值
		 intent.setClass(NewExerciseActivity.this, RechargeActivity.class);
			startActivity(intent);
	}
});
  listView.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		Data data = (Data) excadapter.list.get(arg2 - 1);
		int type=data.activityUrlStatus;
	   if (type==1) {//现金卷
		   intent.putExtra("gotoUrl", data.imagesUrl);
		   intent.setClass(NewExerciseActivity.this, NewDrawCardActivity.class);//NewDrawCardActivity DrawCardActivity
			startActivity(intent);
	    } 	
	   if (type==2) {//签到
		   
		   intent.setClass(NewExerciseActivity.this, RegisterActivity.class);
			startActivity(intent);
	    } //抽奖
	   if (type==3) {
		   //LuckydrawActivity GiftActivity
		   intent.setClass(NewExerciseActivity.this, LuckydrawActivity.class);
			startActivity(intent);
	    }
	   if (type==4) {
		   //LuckydrawActivity GiftActivity
		   intent.setClass(NewExerciseActivity.this, RechargeActivity.class);
			startActivity(intent);
	    }
	   String urlString=data.gotoUrl;
	   if (type==0) {
	   if (!urlString.equals("")) {
		   Intent intents = new Intent(NewExerciseActivity.this,
					HAdvertActivity.class);
			intents.putExtra("url",urlString);
				startActivity(intents);
		
	}
	   }
	   
	}
    });
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

		mList = new ArrayList<ExerciseBean.Data>();
		mList2= new ArrayList<ExerciseBean.Data>();
//		dialog.show();
		getHttpExece(true, false, 1, false);
//		read();
		
	}
  public void getHttpExece(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull){
		AsyncHttpUtils.doSimGet(MCUrl.validRulesNew, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				view_null_message.setVisibility(View.VISIBLE);	
				Log.e("json", "*************************************"
						+ arg0);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("json", "*************************************"
						+ new String(arg2));
				mList2.clear();
				dialog.dismiss();
				exebean = new Gson().fromJson(new String(arg2),
						ExerciseBean.class);
				// 00是都不开。01是充值将现金，10 现金卷 exebean.message
				
				if (exebean.errCode==0) {
				sendEmptyUiMessage(MsgConstants.MSG_01);
				mList = exebean.data;
				Log.e("mlist", mList.size()+"");
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (excadapter == null) {
							excadapter = new ExerciseAdapter(
									NewExerciseActivity.this, mList);
							listView.setAdapter(excadapter);
						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						ptr_vd.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						mList2.clear();
						mList2.addAll(mList);
						excadapter.setData(mList2);
						excadapter.notifyDataSetChanged();
						ptr_vd.onRefreshComplete();
					} else if (!isRefresh && isPull) {
						num = mList.size();
						excadapter.addData(mList);
						excadapter.notifyDataSetChanged();
						ptr_vd.onRefreshComplete();
					}
				}
				}else {
					ToastUtil.shortToast(NewExerciseActivity.this, "获取活动规则失败！");
				}
//
			}
		});
	}
  @Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
//			views();
			break;

		default:
			break;
		}
	}
  public void views(){
		// 00是都不开。01是充值将现金，10 现金卷 exebean.message
	  //现金卷领取
	  if (exebean.message.equals("10")) {
		  Log.e("1111", "11222222222   "+exebean.success);
		  relativeLayout1.setVisibility(View.VISIBLE);
	}
	  //充值将现金
	  if (exebean.message.equals("01")) {
		  exe_xianjinchong.setVisibility(View.VISIBLE);
	}
	  //充值将现金
	  if (exebean.message.equals("11")) {
		  relativeLayout1.setVisibility(View.VISIBLE);
		  exe_xianjinchong.setVisibility(View.VISIBLE);
	}
  }
}

