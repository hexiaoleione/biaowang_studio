package com.hex.express.iwant.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.ExpAdapter;
import com.hex.express.iwant.bean.kuaidiBean;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.helper.KuaiDiDbOperation;
import com.hex.express.iwant.utils.LogUtils;
import com.hex.express.iwant.views.TitleBarView;

/**
 * 选择快递公司页面
 * 
 * @author Eric
 * 
 */
public class SelectExpCompanyActivity extends BaseActivity {
	KuaiDiDbOperation mKuaiDiOperation = new KuaiDiDbOperation();
	private List<kuaidiBean> resultExp;
	private ExpAdapter adapter;
	private PullToRefreshListView lv_expshow1;
	TextView text_title;
	private ListView refreshView1;
	private List<kuaidiBean> kuaiDiData2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectexpcompany);
		iWantApplication.getInstance().addActivity(this);
		copyDbToData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {
		// 标题
		TitleBarView tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		tbv_show.setTitleText(R.string.expName);
		// 用来显示快递公司的的列表
		text_title = (TextView) findViewById(R.id.text_title);
		lv_expshow1 = (PullToRefreshListView) findViewById(R.id.lv_expshow1);
		refreshView1 = lv_expshow1.getRefreshableView();

	}

	/**
	 * 获取快递
	 * 
	 * @return
	 */
	private List<kuaidiBean> getKuaiDiData() {
		List<kuaidiBean> mKuaiDis = mKuaiDiOperation
				.selectDataFromDb("select * from kuaidi where showorder<94");
		return mKuaiDis;
	}

	private List<kuaidiBean> getKuaiDiData2() {
		List<kuaidiBean> mKuaiDis = mKuaiDiOperation
				.selectDataFromDb("select * from kuaidi where showorder>94");
		return mKuaiDis;
	}

	/**
	 * 把数据copy到data目录下
	 */
	private void copyDbToData() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(
					iWantApplication.getInstance());
		}
	}

	@Override
	public void initData() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(
					iWantApplication.getInstance());
		}
		resultExp = getKuaiDiData();
		kuaiDiData2 = getKuaiDiData2();
		adapter = new ExpAdapter(SelectExpCompanyActivity.this, resultExp,
				kuaiDiData2);// 前45个
		SelectExpCompanyActivity.this.refreshView1.setAdapter(adapter);

	}

	@Override
	public void setOnClick() {
		// 禁止下拉刷新
		lv_expshow1.setMode(Mode.DISABLED);
		// lv_expshow2.setMode(Mode.DISABLED);
		lv_expshow1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem <= 6) {
					text_title.setText("常用快递公司");
					if (firstVisibleItem == 7) {
						text_title.setVisibility(View.GONE);
					} else {
						text_title.setVisibility(View.VISIBLE);
					}
				} else {
					text_title.setText("其他快递公司");
				}

			}
		});
		// 列表刷新
		lv_expshow1.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				initData();
				 lv_expshow1.onRefreshComplete();
				
			}
	
		});
		refreshView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					final int index, long arg3) {
				RelativeLayout louLayout = (RelativeLayout) v
						.findViewById(R.id.refresh);
				RelativeLayout louLayout2 = (RelativeLayout) v
						.findViewById(R.id.refresh2);
				louLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						kuaidiBean bean = resultExp.get(index - 1);
						String expCode = bean.expCode;
						String expName = bean.expName;
						String favorite = bean.favorite;
						Log.e("&^%^^^^^^^^%$%^^%%%", bean.nameWord);
						// 存储
						Intent intent = new Intent();
						String ba = expCode + "&" + expName + "&" + favorite;
						LogUtils.info(ba);
						intent.putExtra("exp", expName);
						intent.putExtra("expId", bean.expId);
						SelectExpCompanyActivity.this.setResult(RESULT_OK,
								intent);
						SelectExpCompanyActivity.this.finish();

					}
				});
				louLayout2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						kuaidiBean bean = kuaiDiData2.get(index - 1);
						String expCode = bean.expCode;
						String expName = bean.expName;
						String favorite = bean.favorite;
						Log.e("&^%^^+++++++++++++++++++%", bean.nameWord);
						// 存储本地
						Intent intent = new Intent();
						String ba = expCode + "&" + expName + "&" + favorite;
						intent.putExtra("exp", expName);
						intent.putExtra("expId", bean.expId);
						LogUtils.info(ba);
						SelectExpCompanyActivity.this.setResult(RESULT_OK,
								intent);
						SelectExpCompanyActivity.this.finish();

					}
				});
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
}
