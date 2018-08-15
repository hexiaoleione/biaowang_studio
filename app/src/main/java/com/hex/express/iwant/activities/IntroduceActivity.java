package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
/**
 * 欢迎界面
 * @author SCHT-50
 *
 */
public class IntroduceActivity extends BaseActivity implements OnPageChangeListener,OnClickListener{

	private ViewPager viewpager;
	private ViewPagerAdapter adapter;
	private List<ViewHolder> views;
	private List<Bitmap> bitmaps;
	private View view1;
	private View view2;
	private View view3;
	private View view4;
	Bitmap bitmap1;
	Bitmap bitmap2;
	Bitmap bitmap3;
	Bitmap bitmap4;
	Context mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce);
		iWantApplication.getInstance().addActivity(this);
		mActivity=IntroduceActivity.this;
		
		initView();
		initData();
		setOnClick();
	}
	
	@Override
	public void initView() {
		viewpager=(ViewPager)findViewById(R.id.viewpager);
		view1=(View)findViewById(R.id.view_1);
		view2=(View)findViewById(R.id.view_2);
//		view3=(View)findViewById(R.id.view_3);
		view4=(View)findViewById(R.id.view_4);
		view1.setVisibility(View.GONE);
		view2.setVisibility(View.GONE);
		view4.setVisibility(View.GONE);
	}

	@Override
	public void initData() {// icon_introduce_1 
		bitmap1=BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.icon_intro1);
		bitmap2=BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.icon_intro2);
//		bitmap3=BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.icon_intro3);
		bitmap4=BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.icon_intro4);//icon_introduce_5
		views=new ArrayList<ViewHolder>();
		adapter=new ViewPagerAdapter();
		viewpager.setAdapter(adapter);
		bitmaps=new ArrayList<Bitmap>();
		bitmaps.add(bitmap1);
		bitmaps.add(bitmap2);
//		bitmaps.add(bitmap3);
		bitmaps.add(bitmap4);

		for(int i=0;i<3;i++){
			ViewHolder holder=new ViewHolder();
			holder.view=View.inflate(mActivity, R.layout.item_introduce_view, null);
			holder.button=(Button) holder.view.findViewById(R.id.button);
			holder.imageView=(ImageView) holder.view.findViewById(R.id.imageview);
			holder.button.setOnClickListener(this);
			views.add(holder);
		}
		adapter.notifyDataSetChanged();
	}


	
	class ViewPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			 return view==object;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position).view);
		}
		@Override
		public Object instantiateItem(View container, int position) {
			views.get(position).imageView.setImageBitmap(bitmaps.get(position));
			((ViewPager) container).addView(views.get(position).view);
			if(position==2){
				views.get(position).button.setVisibility(View.VISIBLE);
			}
			return views.get(position).view;
		}
	}
	
	class ViewHolder{
		View view;
		ImageView imageView;
		Button button;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPageSelected(int position) {
		view1.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_gray));
		view2.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_gray));
//		view3.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_gray));
		view4.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_gray));
		switch(position){
		case 0:
			view1.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_blue));
			break;
		case 1:
			view2.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_blue));
			break;
//		case 2:
//			view3.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_blue));
//			break;
		case 2:
			view4.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.shape_circle_blue));
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onClick(View v) {
		SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
		if(v==views.get(2).button){
			SharedPreferences.Editor edit = sp.edit();
			edit.putBoolean("isLogin", false);
			edit.commit();
//			Intent  intent=new Intent(mActivity,MainActivity.class);

//			Intent  intent=new Intent(mActivity,MainTab.class);
			Intent  intent=new Intent(mActivity,NewMainActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			bitmap1.recycle();
			bitmap2.recycle();
//			bitmap3.recycle();
			bitmap4.recycle();
			bitmaps.clear();
			
			finish();
		}
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		viewpager.setOnPageChangeListener(this);
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
