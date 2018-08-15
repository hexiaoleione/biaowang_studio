package com.hex.express.iwant.viewpager;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

public class FullScreenDlgFragment extends DialogFragment implements View.OnClickListener{
    private int mClickItem;//对应显示ViewPager子项的位置
    private List<String> mListImgUrls;
    private ViewPager mViewPager;
    private Integer[] mImgIds;//本地图片资源ID
    private Dialog mDialog;
//    public static final String TAG_NAME=AlertDlgFragment.class.getName();
    private Context mContext;

    //即学即用的工厂方法
    public static FullScreenDlgFragment newInstance(Context context, Integer[] imgIds, int clickItem) {
        Bundle args = new Bundle();
        FullScreenDlgFragment fragment = new FullScreenDlgFragment();
        fragment.setArguments(args);
        fragment.mContext = context;
        fragment.mImgIds = imgIds;
        fragment.mClickItem = clickItem;
        return fragment;
    }

      //由ViewPager来响应点击
    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //设置Dialog样式
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog_fill);
        mDialog=new Dialog(mContext);
        //去标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        return mDialog;
    }

    private void initView() {
        //将Dialog设置全屏！！！
        setDlgParams();

        mViewPager = new ViewPager(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(params);
        mViewPager.setBackgroundColor(0xFF000000);
        initViewPager();
        mDialog.setContentView(mViewPager);
    }

    private void setDlgParams() {
        ViewGroup.LayoutParams lay = mDialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        mDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = mDialog.getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }

    private void initViewPager() {
        if (mImgIds != null && mImgIds.length > 0) {
            List<View> listImgs = new ArrayList<View>();
            for (int i = 0; i < mImgIds.length; i++) {
                ImageView iv = new ImageView(mContext);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                listImgs.add(iv);
                iv.setOnClickListener(this);
                iv.setImageResource(mImgIds[i]);
                // 加载网络图片
                // BitmapHelper.getInstance(mContext).display(iv,
                // mListImgUrls.get(i));
            }
//            if (listImgs.size() > 0) {
//                MyPagerAdapter pageAdapter = new MyPagerAdapter(listImgs);
//                mViewPager.setAdapter(pageAdapter);
//                mViewPager.setCurrentItem(mClickItem);
//            }
        }
    }


	public void show(FragmentManager fragmentManager, int i) {
		// TODO Auto-generated method stub
		
	}
}