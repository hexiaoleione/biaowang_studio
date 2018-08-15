package com.hex.express.iwant.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogisPayActivity.MyReceiver;
import com.hex.express.iwant.bean.DiscountBean;
import com.hex.express.iwant.bean.LoOfferBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  物流公司报价详情
 */
public class LogCompanyFreActivity extends BaseActivity{

	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.spec_huoname)
	TextView spec_huoname;
	@Bind(R.id.spec_zhong)
	TextView spec_zhong;
	@Bind(R.id.spec_tiji)
	TextView spec_tiji;
	@Bind(R.id.spec_jiazhi)//价值
	TextView spec_jiazhi;
	@Bind(R.id.spec_quhuo)
	TextView spec_quhuo;
	@Bind(R.id.spec_songhuo)
	TextView spec_songhuo;
	@Bind(R.id.spec_fahuodizhi)
	TextView spec_fahuodizhi;
	@Bind(R.id.spec_dadaodizhi)
	TextView spec_dadaodizhi;
	@Bind(R.id.spec_shouhuoname)
	TextView spec_shouhuoname;
	@Bind(R.id.spec_lianxifangshi)//
	TextView spec_lianxifangshi;
	@Bind(R.id.spec_fahuoname)
	TextView spec_fahuoname;
	@Bind(R.id.spec_falianxifangshi)//
	TextView spec_falianxifangshi;
	@Bind(R.id.spec_fahuotime)//
	TextView spec_fahuotime;
	@Bind(R.id.spec_dadaotime)//
	TextView spec_dadaotime;
	@Bind(R.id.spec_beizhu)//
	TextView spec_beizhu;
	@Bind(R.id.spec_beizhus)//
	EditText spec_beizhus;
	@Bind(R.id.spec_edt_yuan)//报价多少元
	EditText spec_edt_yuan;
	@Bind(R.id.deta_but)//报价
	Button deta_but;
	@Bind(R.id.spec_huoaddse)//货场地址
	EditText spec_huoaddse;
	
	@Bind(R.id.editText1)//上门取货费
	EditText editText1;
	@Bind(R.id.editText2)//送货上门费
	EditText editText2;
	@Bind(R.id.text_total)//总价
	TextView text_total;
	@Bind(R.id.spec_jian)//
	TextView spec_jian;
	@Bind(R.id.huo)//
	RelativeLayout huo;
	@Bind(R.id.r1)
	RelativeLayout r1;// 上门取货费
	@Bind(R.id.r2)
	RelativeLayout r2;//送货上门费
	@Bind(R.id.r3)
	RelativeLayout r3;//运费
	
	private boolean qu;
	private boolean song;
	private LogisBean lobean;
	int  cargoTotal,takeCargoMoney,sendCargoMoney;
	
	PayReq req;
	private String orderInfo;
	private String result;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private MyReceiver reMyrecive;
	private Object needPayMoney;
	private DiscountBean bean;
	LoOfferBean loff;
	int recId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		//activity_logistical_xiangqing   activity_logistical_xiqing
		setContentView(R.layout.activity_logistifer_xiqing);
		ButterKnife.bind(LogCompanyFreActivity.this);
		req = new PayReq();
		msgApi.registerApp(CollectionKey.APP_ID);
		initView();
		setOnClick();
		
		
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		if (getIntent().getIntExtra("recId", 0)!=0) {
			recId=getIntent().getIntExtra("recId", 0);
		}
		spec_beizhus.setFocusable(false);
		spec_edt_yuan.setFocusable(false);
		spec_huoaddse.setFocusable(false);
		editText1.setFocusable(false);
		editText2.setFocusable(false);
		text_total.setFocusable(false);
		getDiscount();
		deta_but.setVisibility(View.GONE);
		// TODO Auto-generated method stub
//		spec__img_head.setImageURI("");
		
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		spec_fahuoname.setText("发件人："+loff.getData().get(0).sendPerson);
		spec_falianxifangshi.setText(""+loff.getData().get(0).sendPhone);
//		spec_logoffer_time.setText(getIntent().getStringExtra("publishTime"));
//		spec_logoffer_addes.setText("发货次数："+getIntent().getStringExtra("sendNumber"));
//		spec_logoffer_phone.setText(getIntent().getStringExtra(""));
//		spec_juli.setText(getIntent().getStringExtra(""));
		spec_huoname.setText("货物名称："+loff.getData().get(0).cargoName);
		spec_zhong.setText("总重量："+loff.getData().get(0).weight+" 公斤");
		spec_tiji.setText("单件体积："+loff.getData().get(0).cargoVolume);
//		spec_tiji.setText("单件规格："+" 长"+loff.getData().get(0).length+"  厘米"+" 宽"+loff.getData().get(0).wide+"  厘米"+" 高"+loff.getData().get(0).high+"  厘米");
//		spec_jiazhi.setText("货物价值："+loff.getData().get(0).cargoCost+"元");
//		spec_quhuo.setText(getIntent().getStringExtra(""));
//		spec_songhuo.setText(getIntent().getStringExtra(""));
		spec_fahuodizhi.setText("始发地："+loff.getData().get(0).startPlace);
		spec_dadaodizhi.setText("目的地："+loff.getData().get(0).entPlace);
		spec_shouhuoname.setText("收货人："+loff.getData().get(0).takeName);
		spec_lianxifangshi.setText(""+loff.getData().get(0).takeMobile);
		spec_fahuotime.setText("发货时间："+loff.getData().get(0).takeTime);
		spec_dadaotime.setText("送达时间："+loff.getData().get(0).arriveTime);
//		spec_jian.setText("件数："+getIntent().getStringExtra("cargoNumber")+"件");
		spec_jian.setText("件数："+loff.getData().get(0).cargoSize+"件");
		if(!"".equals(loff.getData().get(0).appontSpace)){
			spec_beizhu.setText("指定园区："+loff.getData().get(0).appontSpace);
		}else {
			spec_beizhu.setText("");
		}
		
		
		if(loff.getData().get(0).takeCargo.equals("true")){
			spec_quhuo.setText("物流公司上门取货");
		}else {
			spec_quhuo.setText("发货人送到货场");
			r1.setVisibility(View.GONE);
		}
		if(loff.getData().get(0).sendCargo.equals("true")){
			spec_songhuo.setText("物流公司送货上门");
		}else {
			spec_songhuo.setText("收件人自提");
			r2.setVisibility(View.GONE);
		}
			spec_beizhus.setText(loff.getData().get(0).luMessage);
			spec_huoaddse.setText(loff.getData().get(0).yardAddress);	
			String	cte=loff.getData().get(0).transferMoney;
			String[] stre=cte.split("\\.");
			text_total.setText(""+Integer.parseInt(stre[0]));
			deta_but.setText("修改报价");
			if (loff.getData().get(0).cargoTotal!=null && !loff.getData().get(0).cargoTotal.equals("0.0")) {
				String	ct=loff.getData().get(0).cargoTotal;
				String[] strs=ct.split("\\.");
				spec_edt_yuan.setText(""+Integer.parseInt(strs[0]));
			}
			if (loff.getData().get(0).takeCargoMoney!=null && !loff.getData().get(0).takeCargoMoney.equals("0.0")) {
				String	ct=loff.getData().get(0).takeCargoMoney;
				String[] strs=ct.split("\\.");
				editText1.setText(""+Integer.parseInt(strs[0]));
			}
			if (loff.getData().get(0).sendCargoMoney!=null && !loff.getData().get(0).sendCargoMoney.equals("0.0")) {
				String	ct=loff.getData().get(0).sendCargoMoney;
				String[] strs=ct.split("\\.");
				editText2.setText(""+Integer.parseInt(strs[0]));
			}
		
		
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		deta_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					//运费
					if (spec_edt_yuan.getText().toString().equals("")) {
						cargoTotal=0;
					}else {
						String ct= spec_edt_yuan.getText().toString();
						String[] strs=ct.split("\\.");
						cargoTotal=	Integer.parseInt(strs[0]);
					}
				//上门取货费
					if (editText1.getText().toString().equals("")) {
						takeCargoMoney=0;
					}else {
						String tm= editText1.getText().toString();
						String[] strs=tm.split("\\.");
						takeCargoMoney=	Integer.parseInt(strs[0]);
					}
				//送货上门费
					if (editText2.getText().toString().equals("")) {
						sendCargoMoney=0;
					}else {
						String sm=editText2.getText().toString();
						String[] strs=sm.split("\\.");
						sendCargoMoney=	Integer.parseInt(strs[0]);
					}
					text_total.setText(""+subadd(cargoTotal,takeCargoMoney,sendCargoMoney));
					//修改报价
//					getData();
			}
		});
		
		text_total.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	public void getDiscount(){
		
		Log.e("1111111recId","   "+recId);
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.QuotationInfo, "recId",recId+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("1111111json", new String(arg2));
						dialog.dismiss();
						 loff = new Gson().fromJson(new String(arg2),
								LoOfferBean.class);
						if (loff.errCode==0) {
							initData();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}


				});
	}


	public int  subduticon(int a,int b){
		int resultSub=a-b;
		return resultSub;
	}
	public int  subadd(int a,int b,int c){
		int resultSum=a+b+c;
		return resultSum;
	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}	
	

}
