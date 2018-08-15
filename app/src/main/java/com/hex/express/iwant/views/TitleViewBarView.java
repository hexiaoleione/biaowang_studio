package com.hex.express.iwant.views;

import com.hex.express.iwant.R;
import com.hex.express.iwant.utils.DataTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 快递查询界面专用的titlebar
 * @author SCHT-50
 *
 */
public class TitleViewBarView extends RelativeLayout {
	private static final String TAG = TitleViewBarView.class.getSimpleName();
	private Button btnCenterLeft;
	private Button btnCenterRight;
	private Button btnLeft;
	private Button btnRight;
	private LinearLayout centerButtons,ll_titlebar;
	private Context mContext;
	private TextView txtTitle;
	private ImageView img_title;
	private View view;

	public TitleViewBarView(Context paramContext) {
		super(paramContext);
		this.mContext = paramContext;
		initView();
	}

	public TitleViewBarView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mContext = paramContext;
		initView();
		init();
	}

	private void init() {
		this.btnLeft.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				try {
					((Activity) TitleViewBarView.this.mContext).finish();
					return;
				} catch (Exception localException) {
					localException.printStackTrace();
				}
			}
		});
	}

	private void initView() {
		view = LayoutInflater.from(this.mContext).inflate(R.layout.common_titleview_bar,
				this);
		this.btnLeft = ((Button) findViewById(R.id.btnLeft));
		this.btnRight = ((Button) findViewById(R.id.btnRight));
		this.btnCenterLeft = ((Button) findViewById(R.id.btnCenterLeft));
		this.btnCenterRight = ((Button) findViewById(R.id.btnCenterRight));
		this.txtTitle = ((TextView) findViewById(R.id.txtTitle));
		this.img_title = ((ImageView) findViewById(R.id.img_title));
		this.centerButtons = ((LinearLayout) findViewById(R.id.center_buttons));
		this.ll_titlebar = ((LinearLayout) findViewById(R.id.ll_titlebar));
	}

	public LinearLayout getCenterButtons() {
		return this.centerButtons;
	}

	public Button getCenterLeftBtn() {
		return this.btnCenterLeft;
	}

	public LinearLayout getLl_titlebar() {
		return ll_titlebar;
	}

	public void setLl_titlebar(LinearLayout ll_titlebar) {
		this.ll_titlebar = ll_titlebar;
	}

	public ImageView getImg_title() {
		return img_title;
	}

	public void setImg_title(ImageView img_title) {
		this.img_title = img_title;
	}

	public Button getCenterRightBtn() {
		return this.btnCenterRight;
	}

	public Button getLeftBtn() {
		return this.btnLeft;
	}

	public Button getRightBtn() {
		return this.btnRight;
	}

	public void setCenterButtons(LinearLayout paramLinearLayout) {
		this.centerButtons = paramLinearLayout;
	}

	public void setCenterLeftBtnOnClickListener(
			OnClickListener paramOnClickListener) {
		this.btnCenterLeft.setOnClickListener(paramOnClickListener);
	}

	public void setCenterLeftBtnText(int paramInt) {
		this.btnCenterLeft.setText(paramInt);
	}

	public void setCenterLeftBtnText(String paramString) {
		this.btnCenterLeft.setText(paramString);
	}

	public void setCenterRightBtnOnClickListener(
			OnClickListener paramOnClickListener) {
		this.btnCenterRight.setOnClickListener(paramOnClickListener);
	}

	public void setCenterRightBtnText(int paramInt) {
		this.btnCenterRight.setText(paramInt);
	}

	public void setCenterRightBtnText(String paramString) {
		this.btnCenterRight.setText(paramString);
	}

	public void setCommonVisible(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
		this.btnLeft.setVisibility(paramInt1);
		this.btnRight.setVisibility(paramInt4);
		this.txtTitle.setVisibility(paramInt2);
		this.centerButtons.setVisibility(paramInt3);
	}

	public void setLeftBtnIcon(int paramInt) {
		Drawable localDrawable = this.mContext.getResources().getDrawable(
				paramInt);
		int i = DataTools.dip2px(this.mContext, 30);
		localDrawable.setBounds(0, 0, i * localDrawable.getIntrinsicWidth()
				/ localDrawable.getIntrinsicHeight(), i);
		this.btnLeft.setCompoundDrawables(localDrawable, null, null, null);
	}

	public void setLeftBtnOnclickListener(
			OnClickListener paramOnClickListener) {
		this.btnLeft.setOnClickListener(paramOnClickListener);
	}

	public void setLeftBtnText(int paramInt) {
		this.btnLeft.setText(paramInt);
	}

	public void setLeftBtnText(String paramString) {
		this.btnLeft.setText(paramString);
	}

	public void setRightBtnIcon(int paramInt) {
		Drawable localDrawable = this.mContext.getResources().getDrawable(
				paramInt);
		int i = DataTools.dip2px(this.mContext, 30);
		localDrawable.setBounds(0, 0, i * localDrawable.getIntrinsicWidth()
				/ localDrawable.getIntrinsicHeight(), i);
		this.btnRight.setCompoundDrawables(null, null, localDrawable, null);
	}

	public void setRightBtnOnclickListener(
			OnClickListener paramOnClickListener) {
		this.btnRight.setOnClickListener(paramOnClickListener);
	}

	public void setRightBtnPadding() {
		this.btnRight.setPadding(20, 0, 20, 0);
	}

	public void setRightBtnStyle() {
		GradientDrawable localGradientDrawable = new GradientDrawable();
		localGradientDrawable.setShape(0);
		localGradientDrawable.setColor(255);
		localGradientDrawable.setStroke(1, Color.rgb(73, 155, 247));
		this.btnRight.setBackgroundDrawable(localGradientDrawable);
	}

	public void setRightBtnText(int paramInt) {
		this.btnRight.setText(paramInt);
	}

	public void setRightBtnText(String paramString) {
		this.btnRight.setText(paramString);
	}

	public void setTitleText(int paramInt) {
		this.txtTitle.setText(paramInt);
	}
	public View getTitleTextView(){
		return txtTitle;
	}
	public View getTitleimView(){
		return img_title;
	}
	public void setTitleText(String paramString) {
		this.txtTitle.setText(paramString);
	}
	public void setBackGroundColor(int color){
		this.view.setBackgroundColor(color);
	}
	@SuppressLint({ "NewApi" })
	public void showPopWindow(PopupWindow paramPopupWindow,
			TitleViewBarView paramTitleBarView) {
		// paramPopupWindow.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#E9E9E9")));
		// paramPopupWindow.showAsDropDown(paramTitleBarView, 0, -15);
		// paramPopupWindow.setAnimationStyle(2131427332);
		// paramPopupWindow.setFocusable(true);
		// paramPopupWindow.setOutsideTouchable(true);
		// paramPopupWindow.update();
	}
}