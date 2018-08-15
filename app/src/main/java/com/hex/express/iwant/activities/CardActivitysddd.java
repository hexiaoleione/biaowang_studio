package com.hex.express.iwant.activities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.CardActivitys.CardAdaptersd;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CardActivitysddd extends BaseActivity{
	private ListView lv_data;
	private Button bt;
	private boolean state = true;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<CardBean.Data> mList;
	public CardBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mult);
		lv_data = (ListView) findViewById(R.id.lv_data);
		bt = (Button) findViewById(R.id.bt);

		
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
		mList = new ArrayList<CardBean.Data>();
		dialog.show();
		getHttprequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.COUPON, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								CardBean.class);
						mList = bean.data;
						

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(CardActivitysddd.this, "网络请求加载失败");

					}
				});

	}
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> mult_data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("item", "mult_" + i);
			mult_data.add(temp);
		}
		lv_data.getCheckedItemIds();
		SimpleAdapter adapter = new SimpleAdapter(this, mult_data,
				R.layout.duoxuan_item, new String[] { "item" },
				new int[] { R.id.tv }) {
			@Override
			public boolean hasStableIds() {
				return true;
			}

			@Override
			public long getItemId(int position) {
				return position+100;
			}
		};
		lv_data.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv_data.setAdapter(adapter);

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < lv_data.getAdapter().getCount(); i++) {
					lv_data.setItemChecked(i, state);
				}
				state = !state;
			}
		});

		lv_data.addFooterView(new View(this));
		System.out.println(((HeaderViewListAdapter)lv_data.getAdapter()).getWrappedAdapter().getCount());
		lv_data.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (ListView.CHOICE_MODE_MULTIPLE == lv_data.getChoiceMode()) {
					HashMap<String, String> item = (HashMap<String, String>) lv_data
							.getItemAtPosition(position);
					Toast.makeText(
							CardActivitysddd.this,
							"呜呜呜呜" + lv_data.getCheckedItemCount()+ item.get("item"), 2)
							.show();
					long[] ids = lv_data.getCheckedItemIds();
					for (int i = 0; i < ids.length; i++) {
						Log.i("tag", ids[i]+"");//婵★拷锟斤拷锟斤拷娑�锟芥�锟斤拷锟斤拷锟斤拷��锟斤拷锟斤拷甯�锟�100��锟�101��锟�102��锟�103;锟藉��锟斤拷锟姐��锟斤拷adapter getItemId()�╋拷锟斤拷锟斤拷
					}
				}
			}
		});
		
	}

}
