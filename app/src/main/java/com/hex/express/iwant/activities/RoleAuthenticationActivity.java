package com.hex.express.iwant.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.GifView;
import com.hex.express.iwant.views.TitleBarView;

/**
 * 角色认证界面
 * 
 * @author Eric
 * 
 */
public class RoleAuthenticationActivity extends BaseActivity implements OnClickListener {

	// 界面控件:
	private TitleBarView tbv_show;
	// ll_putongyonghu,ll_kuaidiyuan,tv_putongyonghu,iv_circle_putongyonghu,
	private RelativeLayout ll_shunfengbiaoshi, ll_kuaidiyuan, ll_wuliugongsi,ll_wuliugeren,ll_lenglianche,ll_shanghu;
	private TextView tv_shunfengbiaoshi, tv_kuaidiyuan, tv_wuliugongsi,tv_wuliugeren,tv_lenglian,tv_shanghu;
	private ImageView btn_Left,iv_circle_shunfengbiaoshi, iv_circle_kuaidiyuan, iv_circle_wuliugongsi,iv_circle_lenglian,iv_circle_wuliugeren,iv_circle_shanghu;
	private Button btn_nextStep,btn_zanbu;
	private GifView gif1;

	// 身份识别码:1-普通用户;2-顺丰镖师;3-快递员;4-物流公司 默认 为1.
	private String IDENTIFY_CODE = "0";
    private String ren;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roleauthentication);
		iWantApplication.getInstance().addActivity(this);
		ren=getIntent().getStringExtra("tiaoguo");
		initView();
		setListener();
		initData();
		
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		// 标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		tbv_show.setTitleText("角色认证");

		ll_shunfengbiaoshi = (RelativeLayout) findViewById(R.id.ll_shunfengbiaoshi);
		// ll_putongyonghu = (RelativeLayout)
		// findViewById(R.id.ll_putongyonghu);
		ll_kuaidiyuan = (RelativeLayout) findViewById(R.id.ll_kuaidiyuan);
		ll_wuliugongsi = (RelativeLayout) findViewById(R.id.ll_wuliugongsi);
		ll_wuliugeren= (RelativeLayout) findViewById(R.id.ll_wuliugeren);
		ll_lenglianche= (RelativeLayout) findViewById(R.id.ll_lenglianche);
		ll_shanghu= (RelativeLayout) findViewById(R.id.ll_shanghu);

		tv_shunfengbiaoshi = (TextView) findViewById(R.id.tv_shunfengbiaoshi);
		// tv_putongyonghu = (TextView) findViewById(R.id.tv_putongyonghu);
		tv_kuaidiyuan = (TextView) findViewById(R.id.tv_kuaidiyuan);
		tv_wuliugongsi = (TextView) findViewById(R.id.tv_wuliugongsi);
		tv_wuliugeren= (TextView) findViewById(R.id.tv_wuliugeren);
		tv_lenglian= (TextView) findViewById(R.id.tv_lenglian);
		tv_shanghu= (TextView) findViewById(R.id.tv_shanghu);

		iv_circle_shunfengbiaoshi = (ImageView) findViewById(R.id.iv_circle_shunfengbiaoshi);
		// iv_circle_putongyonghu = (ImageView)
		// findViewById(R.id.iv_circle_putongyonghu);
		iv_circle_kuaidiyuan = (ImageView) findViewById(R.id.iv_circle_kuaidiyuan);
		iv_circle_wuliugongsi = (ImageView) findViewById(R.id.iv_circle_wuliugongsi);
		iv_circle_wuliugeren= (ImageView) findViewById(R.id.iv_circle_wuliugeren);
		iv_circle_lenglian= (ImageView) findViewById(R.id.iv_circle_lenglian);
		iv_circle_shanghu= (ImageView) findViewById(R.id.iv_circle_shanghu);
		
		btn_Left= (ImageView) findViewById(R.id.btn_Left);
		
		btn_nextStep = (Button) findViewById(R.id.btn_nextStep);
		btn_zanbu= (Button) findViewById(R.id.btn_zanbu);
		gif1=(GifView)findViewById(R.id.gif1);
		gif1.setMovieResource(R.drawable.zhaunchexiaoche);  
		
		if (ren.equals("1")) {
			btn_zanbu.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		btn_Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_zanbu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				
				Intent intent = new Intent(RoleAuthenticationActivity.this,
						NewMainActivity.class);
//				Intent intent = new Intent(RoleAuthenticationActivity.this,
//						MainTab.class);
//				Intent intent = new Intent(RoleAuthenticationActivity.this,
//						MainActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
				finish();
			}
		});
	}

	public void setListener() {
		// TODO Auto-generated method stub

		ll_shunfengbiaoshi.setOnClickListener(this);
		// ll_putongyonghu.setOnClickListener(this);
		ll_kuaidiyuan.setOnClickListener(this);
		ll_wuliugongsi.setOnClickListener(this);
		ll_wuliugeren.setOnClickListener(this);
		ll_lenglianche.setOnClickListener(this);
		ll_shanghu.setOnClickListener(this);
		btn_nextStep.setOnClickListener(this);
		gif1.setOnClickListener(this);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	// 点击事件
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_shunfengbiaoshi:
			showReplaceMoney(2);
			// 身份识别码;
			IDENTIFY_CODE = "2";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.baseColor));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.ampm_text_color));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugeren.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_lenglian.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_shanghu.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// 选中状态
			// ToastUtil.shortToast(RoleAuthenticationActivity.this, "此功能暂未开放");
			// iv_circle_shunfengbiaoshi
			// .setBackgroundResource(R.drawable.circle_selected);
			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_ored);
			// iv_circle_putongyonghu
			// .setBackgroundDrawable(getApplicationContext()
			// .getResources().getDrawable(R.drawable.circle_gray));
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_gray);
		
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_gray);
			break;
		case R.id.ll_putongyonghu:
			showReplaceMoney(3);
			// 身份识别码;
			IDENTIFY_CODE = "3";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.baseColor));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.ampm_text_color));
            tv_wuliugeren.setTextColor(getResources().getColor(R.color.ampm_text_color));
        	tv_lenglian.setTextColor(getResources().getColor(R.color.ampm_text_color));
        	tv_shanghu.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// 选中状态

			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_gray);
			// iv_circle_putongyonghu
			// .setBackgroundDrawable(getApplicationContext()
			// .getResources().getDrawable(
			// R.drawable.circle_selected));
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_gray);
		
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_gray);

			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_gray);
			break;
		case R.id.ll_kuaidiyuan:
			showReplaceMoney(1);
			// 身份识别码;
			IDENTIFY_CODE = "1";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.ampm_text_color));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.baseColor));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			 tv_wuliugeren.setTextColor(getResources().getColor(R.color.ampm_text_color));

			// 选中状态
			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_gray);
			// iv_circle_putongyonghu
			// .setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_ored);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_gray);
			tv_lenglian.setTextColor(getResources().getColor(R.color.ampm_text_color));
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_gray);
			tv_shanghu.setTextColor(getResources().getColor(R.color.ampm_text_color));
			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_gray);
			break;
		case R.id.ll_wuliugongsi:
			// ToastUtil.shortToast(RoleAuthenticationActivity.this, "此功能暂未开放");
			showReplaceMoney(4);
			// 身份识别码;
			IDENTIFY_CODE = "4";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.ampm_text_color));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.baseColor));
			 tv_wuliugeren.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// 选中状态
			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_gray);
			// iv_circle_putongyonghu
			// .setBackgroundDrawable(getApplicationContext()
			// .getResources().getDrawable(R.drawable.circle_gray));
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_ored);//circle_selected
			tv_lenglian.setTextColor(getResources().getColor(R.color.ampm_text_color));
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_gray);
			tv_shanghu.setTextColor(getResources().getColor(R.color.ampm_text_color));
			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_gray);
			break;
		case R.id.ll_wuliugeren:
			// ToastUtil.shortToast(RoleAuthenticationActivity.this, "此功能暂未开放");
			// 身份识别码;
			showReplaceMoney(3);
			IDENTIFY_CODE = "3";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.ampm_text_color));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugeren.setTextColor(getResources().getColor(R.color.baseColor));
			tv_lenglian.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// 选中状态
			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_ored);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_gray);
			tv_shanghu.setTextColor(getResources().getColor(R.color.ampm_text_color));
			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_gray);
			break;
		case R.id.ll_lenglianche:
			// ToastUtil.shortToast(RoleAuthenticationActivity.this, "此功能暂未开放");
			// 身份识别码;
//			showReplaceMoney(5);
			IDENTIFY_CODE = "5";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.ampm_text_color));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugeren.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_lenglian.setTextColor(getResources().getColor(R.color.baseColor));
			// 选中状态
			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_ored);
			tv_shanghu.setTextColor(getResources().getColor(R.color.ampm_text_color));
			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_gray);
			break;
		case R.id.ll_shanghu:
			// ToastUtil.shortToast(RoleAuthenticationActivity.this, "此功能暂未开放");
			// 身份识别码;
//			showReplaceMoney(5);
			IDENTIFY_CODE = "6";
			// 字体颜色
			tv_shunfengbiaoshi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// tv_putongyonghu.setTextColor(getResources().getColor(
			// R.color.ampm_text_color));
			tv_kuaidiyuan.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugongsi.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_wuliugeren.setTextColor(getResources().getColor(R.color.ampm_text_color));
			tv_lenglian.setTextColor(getResources().getColor(R.color.ampm_text_color));
			// 选中状态
			iv_circle_shunfengbiaoshi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			
			iv_circle_wuliugeren.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_kuaidiyuan.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_wuliugongsi.setBackgroundResource(R.drawable.circle_gray);
			iv_circle_lenglian.setBackgroundResource(R.drawable.circle_gray);
			tv_shanghu.setTextColor(getResources().getColor(R.color.baseColor));
			iv_circle_shanghu.setBackgroundResource(R.drawable.circle_ored);
			break;
		case R.id.btn_nextStep:
			if ("0" == IDENTIFY_CODE) {
			  ToastUtil.shortToast(RoleAuthenticationActivity.this, "请先选择要认证的角色类型");	
			}else {
//			if (!"".equals(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE))) {
				
			Intent intent = new Intent();
			// if ("1" == IDENTIFY_CODE) {
			// if (PreferencesUtils.getBoolean(getApplicationContext(),
			// PreferenceConstants.COMPLETE)) {
			// ToastUtil.shortToast(RoleAuthenticationActivity.this,
			// "已经审核成功");
			// } else {
			// startActivity(intent.setClass(getApplicationContext(),
			// PrefectActivity.class));
			// }
			// // startActivity(intent.setClass(getApplicationContext(),
			// // PrefectActivity.class));
			// } else
			if ("2" == IDENTIFY_CODE) {// 镖师认证
				// ToastUtil.shortToast(RoleAuthenticationActivity.this,
				// "暂未开放");

				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")
						|| PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("3")) {
					ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已具备镖师资格");
				} else {
					intent.putExtra("IDENTIFY_CODE", IDENTIFY_CODE);
					startActivity(intent.setClass(getApplicationContext(), PrefectActivity.class));
					finish();
				}
			} else if ("1" == IDENTIFY_CODE) {// 认证类型 1 快递员 2 镖师
				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")) {
					ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了快递员");
				} else {
					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("3")) {
						startActivity(intent.setClass(getApplicationContext(), CourierIdentificationActivity.class));
						finish();
					} else {
						intent.putExtra("IDENTIFY_CODE", IDENTIFY_CODE);
						startActivity(intent.setClass(getApplicationContext(), PrefectActivity.class));
						finish();
					}

				}
			}
			 else if ("4" == IDENTIFY_CODE) {// 认证类型 公司物流
//					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")) {
//						ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了快递员");
//					} else {
				 if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("2") || 
							PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("1")) {
						ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了");
					} else {
						if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("3")) {
							ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已被禁用，请联系我们>Biaowang_app@163.com");
						
							return;
						}
							intent.putExtra("IDENTIFY_CODE", IDENTIFY_CODE);
//		             Log.e("111111", ""+PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID));					
//							startActivity(intent.setClass(getApplicationContext(), LogiCertificationActivity.class));
							startActivity(intent.setClass(getApplicationContext(), CompanyCerActivity.class));
							finish();

					}
				} else if ("3" == IDENTIFY_CODE) {// 认证类型 个人物流
//					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")) {
//					ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了快递员");
//				} else {

					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("2") || 
							PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("1")
						||	PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("4")) {
						ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了");
					} else {
						if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("3")) {
							ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已被禁用，请联系我们>Biaowang_app@163.com");
							return;
						}
						intent.putExtra("IDENTIFY_CODE", IDENTIFY_CODE);
						startActivity(intent.setClass(getApplicationContext(), PersonNewCerActivity.class));
//						startActivity(intent.setClass(getApplicationContext(), PersonCerActivity.class));//PersonNewCerActivity  LogiCertiActivity
						finish();

				}
				} else if ("5" == IDENTIFY_CODE) {// 认证类型 冷藏车
//					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")) {
//					ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了快递员");
//				} else {

					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("4") 
							) {
						ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了");
					} else {
						if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("3")) {
							ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已被禁用，请联系我们>Biaowang_app@163.com");
							return;
						}
						intent.putExtra("IDENTIFY_CODE", IDENTIFY_CODE);
						startActivity(intent.setClass(getApplicationContext(), PersonNewCerActivity.class));
//						startActivity(intent.setClass(getApplicationContext(), PersonCerActivity.class));//PersonNewCerActivity  LogiCertiActivity
						finish();

				}
					}else if ("6" == IDENTIFY_CODE) {// 认证类型 冷藏车
//						if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")) {
//						ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了快递员");
//					} else {

						if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.shopType).equals("1") 
								) {
							ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已经认证了");
						} else {
							if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.shopType).equals("2")) {
								ToastUtil.shortToast(RoleAuthenticationActivity.this, "您已被禁用，请联系我们>Biaowang_app@163.com");
								return;
							}
							intent.putExtra("IDENTIFY_CODE", IDENTIFY_CODE);
							startActivity(intent.setClass(getApplicationContext(), CommercialActivity.class));
//							startActivity(intent.setClass(getApplicationContext(), PersonCerActivity.class));//PersonNewCerActivity  LogiCertiActivity
							finish();

					}
						}
//			}
			}
			break;
			case R.id.gif1:
				startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
				break;
		default:
			break;
		}

	}
	/**
	 * 显示popowing
	 */
	private void showReplaceMoney(int nub) {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_windows, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(RoleAuthenticationActivity.this.findViewById(R.id.btn_nextStep), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		Button tv_show = (Button) view.findViewById(R.id.popbtnsaves_pan);
		TextView textView3= (TextView) view.findViewById(R.id.poptextView3);
		TextView tet_tishi= (TextView) view.findViewById(R.id.poptet_tishi);
		// 1快递员 2 镖师 3 物流个人 4 物流公司
		if (nub==1) {
			textView3.setText("快递员是做什么的？");
			tet_tishi.setText("接通知，上门取件，在线收款");
		}else if (nub==2) {
			textView3.setText("镖师是做什么的？");
			tet_tishi.setText("顺路捎货，赚大钱。");
		}else if (nub==3) {
			textView3.setText("大货车是做什么的？");
			tet_tishi.setText("随时随地，在线接单，增加业务量，避免空返。");
		}else if (nub==5) {
			textView3.setText("认证冷链车司机有什么好处？");
			tet_tishi.setText("针对您的车型，提供给您专门服务");
		}
		else {
			textView3.setText("物流公司是做什么的？");
			tet_tishi.setText("随时随地，在线接单，增加业务量，避免空返。");
		}
		tv_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				getReplace();
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

	}

}
