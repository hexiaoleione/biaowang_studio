package com.hex.express.iwant.newactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.adpter.MyPagerAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.PagerSlidingTabStrip;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newsmain.LeftFragment;
import com.hex.express.iwant.slidingview.SlidingFragmentActivity;
import com.hex.express.iwant.slidingview.SlidingMenu;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author huyichuan
 * 接镖
 */
public class AnswerActivity extends SlidingFragmentActivity {

    /**
     * PagerSlidingTabStrip的实例
     */
    private PagerSlidingTabStrip tabs;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;
    private TextView btnRight;
    private ImageView btnLeft;
    ViewPager pager;

    private Fragment mContent;

    @Bind(R.id.topButton)
    ImageView topButton;
    @Bind(R.id.butfahuo)
    ImageView butfahuo;
    @Bind(R.id.btnjiehuo)
    ImageView btnjiehuo;
    @Bind(R.id.rigitButton)
    ImageView rigitButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        iWantApplication.getInstance().addActivity(this);
        ButterKnife.bind(AnswerActivity.this);
        initSlidingMenu(savedInstanceState);
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        initView();
    }

    public void initView() {
        // TODO Auto-generated method stub
//		if (isLogin()) {
//			if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("0") || PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("")){
//				pager.setAdapter(new MyPagerAdapterse(getChildFragmentManager()));
//			}else {
//				pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
//			}
        pager.setAdapter(new MyPagerAdapter(this.getSupportFragmentManager()));
        tabs.setViewPager(pager);

        setTabsValue();
//			}else {
//				// 登录界面
//				startActivity(new Intent(AnswerActivity.this, LoginWeixinActivity.class));
//				
//			}
        topButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                toggle();
            }
        });
        butfahuo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        rigitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isLogin()) {
                    startActivity(new Intent(AnswerActivity.this, MessageActivity.class));
                } else {
                    startActivity(new Intent(AnswerActivity.this, LoginWeixinActivity.class));
                }
            }
        });
        getMessageStutas();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // tabs.setDividerColor(Color.BLACK);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));// 4
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, dm)); // 16
        // 设置Tab Indicator的颜色 底部下划线颜色
        tabs.setIndicatorColor(Color.parseColor("#FF0000"));// #45c01a  43A44b FF730A
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#FF0000"));// #45c01a
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
        tabs.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    public boolean isLogin() {
        return PreferencesUtils.getBoolean(AnswerActivity.this, PreferenceConstants.ISLOGIN);
    }


    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        if (mContent == null) {
            mContent = new HomSubFragment1();
        }

        // 设置左侧滑动菜单
        setBehindContentView(R.layout.menu_frame_left);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new LeftFragment()).commit();

        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式,这里设置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//TOUCHMODE_NONE TOUCHMODE_FULLSCREEN
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);

    }

    /**
     * 信息是否有未读的状态
     */
    private void getMessageStutas() {
        RequestParams params = new RequestParams();
        Log.e("mdgread", UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
                String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))));
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
                        String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                null, null, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Log.e("message", "" + new String(arg2));
                        BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                        if (bean.getErrCode() > 0) {
                            rigitButton.setBackgroundResource(R.drawable.xiaoxihong);
//							rigitButton.setBackgroundDrawable(null);
                        } else if (bean.getErrCode() == -2) {
                            rigitButton.setBackgroundResource(R.drawable.xiaoxihei);
//							rigitButton.setBackgroundDrawable(null);
                        }
                    }

                });
    }


}
