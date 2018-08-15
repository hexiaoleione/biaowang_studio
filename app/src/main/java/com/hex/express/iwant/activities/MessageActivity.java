package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.MessageAdapter;
import com.hex.express.iwant.bean.MessageBean;
import com.hex.express.iwant.bean.MessageBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.ptrl_message)
	PullToRefreshListView ptr_vd;
	@Bind(R.id.null_message)
	View view_null_message;
	ListView listView;
	@Bind(R.id.btn_Right)//一键删除
	TextView btn_Right;
	@Bind(R.id.btn_Left)//返回
	ImageView btn_Left;
	int pageNo = 1;
	int pageSize = 100;
	List<MessageBean.Data> mList;
	List<MessageBean.Data> mList2;
	public MessageBean bean;
	MessageAdapter adapter;
	protected int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
	
	}

	@Override
	public void initView() {
//		this.tbv_show.setTitleText(R.string.messagetitle);
		listView = ptr_vd.getRefreshableView();
	}
	

	@Override
	public void initData() {
		mList = new ArrayList<MessageBean.Data>();
		mList2= new ArrayList<MessageBean.Data>();
		dialog.show();
		getHttpMessage(true, false, 1, false);
		read();
		delete();

	}

	//长按删除事件
	public void delete() {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Data data = (Data) adapter.deleteData().get(arg2 - 1);
				final String messageId = data.messageId;
				Builder builder = new Builder(
						MessageActivity.this);
				builder.setItems(R.array.thread_menu, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e("json", MCUrl.MESSAGEDELETE + messageId);
						JSONObject obj = new JSONObject();
						AsyncHttpUtils.doPut(MessageActivity.this,
								MCUrl.MESSAGEDELETE + messageId,
								obj.toString(), null,
								new AsyncHttpResponseHandler() {

									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										Log.e("JSON", adapter.deleteData().size()+"");
										getHttpMessage(false, true, 1, false);
										Log.e("JSONtttttttt", adapter.deleteData().size()+"");
									}

								});
					}
				});
				builder.show();
				return true;
			}
		});
	}

	// 更改消息状态及点击事件
	public void read() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			private ImageView iv_icon;


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				Data data = (Data) adapter.list.get(arg2 - 1);
				String messageId = data.messageId;
				iv_icon = (ImageView) arg1.findViewById(R.id.iv_icon);

				if (data.messageUrl.toString().equals("")
						&& data.messageDesc.toString().equals("")) {

				} else {
					ReadHttp(messageId);
					intent.setClass(MessageActivity.this, MessageDetails.class);
					intent.putExtra("messageUrl", data.messageUrl);
					intent.putExtra("messageDesc", data.messageDesc);
					intent.putExtra("title", data.messageTitle);
					intent.putExtra("sendTime", data.sendTime);
					
					startActivity(intent);
				}
			}

			// 更改信息的状态
			private void ReadHttp(String messageId) {
				Log.e("json", MCUrl.MESSAGEREAD + messageId);
				JSONObject obj = new JSONObject();
				AsyncHttpUtils.doPut(MessageActivity.this, MCUrl.MESSAGEREAD
						+ messageId, obj.toString(), null,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								Log.e("json", "&&&&&&&&&&&&&&&&&");

								iv_icon.setBackgroundResource(R.drawable.read);
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub

							}
						});

			}
		});
	}

	private void getHttpMessage(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		Log.e("json", UrlMap.getThreeUrl(MCUrl.SYSTEM, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.SYSTEM, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)), null,
				null, null, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", "*************************************"
								+ new String(arg2));
						mList2.clear();
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								MessageBean.class);
						mList = bean.data;
						Log.e("mlist", mList.size()+"");
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new MessageAdapter(
											MessageActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								ptr_vd.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
								mList2.clear();
								mList2.addAll(mList);
								adapter.setData(mList2);
								adapter.notifyDataSetChanged();
								ptr_vd.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								num = mList.size();
								adapter.addData(mList);
								adapter.notifyDataSetChanged();
								ptr_vd.onRefreshComplete();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						// TODO Auto-generated method stub

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {
		ptr_vd.setMode(Mode.BOTH);
		ptr_vd.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				Log.e("vv", "vvv");
				// 刷新 重新加载数据
				getHttpMessage(false, true, 1, false);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpMessage(false, false, pageNo, true);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
							false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		
	}

	@Override
	public void getData() {
		btn_Left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		btn_Right.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Builder ad = new Builder(MessageActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("是否要删除所有");
				ad.setNegativeButton("确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						getHttpMessages(true, false, 1, false);
					}
				});
				ad.setPositiveButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						
					}
				});
				ad.create().show();
				
				
			}
		});

	}

	/**
	 * FragmentActivity的重写返回键；
	 */

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
//			 ToastUtil.shortToast(getApplicationContext(),
//			 "这是测试FragmentActivity的重写返回键");
			// 具体的操作代码
			finish();// 销毁此页面,并去启动上一个页面的相应的Fragment
//			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")
//					|| PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
//							.equals("3")) {
//				// 普通用户中心
//				startActivity(new Intent(MessageActivity.this, UserCenterActivity.class));
//			} else if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
//					.equals("2")) {
//				// 快递员用户中心
//				startActivity(new Intent(MessageActivity.this, CourierCenterActivity.class));
//			}
		}
		return super.dispatchKeyEvent(event);
	}
	//删除所有消息队列
	private void getHttpMessages(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		Log.e("json", UrlMap.getThreeUrl(MCUrl.SYSTEM, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)));
		JSONObject obj = new JSONObject();
		AsyncHttpUtils.doPut(MessageActivity.this,
				MCUrl.DeleteAll + String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID)),
				obj.toString(), null,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", "*************************************"
								+ new String(arg2));
//						ToastUtil.shortToast(MessageActivity.this, "22222");
						dialog.dismiss();
//						
						bean = new Gson().fromJson(new String(arg2),
								MessageBean.class);
						ToastUtil.shortToast(MessageActivity.this,bean.message);
//						mList = bean.data;
//						Log.e("mlist", mList.size()+"");
						getHttpMessage(true, false, 1, false);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
//						ToastUtil.shortToast(MessageActivity.this, "33333");
						// TODO Auto-generated method stub

					}
				});

	}
}
