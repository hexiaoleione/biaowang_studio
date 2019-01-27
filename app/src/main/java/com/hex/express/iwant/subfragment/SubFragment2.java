package com.hex.express.iwant.subfragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownEscoreDartActivity;
import com.hex.express.iwant.activities.DownEscortDetialsActivity;
import com.hex.express.iwant.activities.QuestionActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.activities.RoleAuthenticationActivity;
import com.hex.express.iwant.adapters.DownwindEscortAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DownSpecialBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.DepositsActivity;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubFragment2 extends Fragment {

    @Bind(R.id.escort_listview)
    PullToRefreshListView escort_listview;
    //	@Bind(R.id.ll_menu_escort)
//	LinearLayout ll_menu_escort;
    @Bind(R.id.null_message)
    View view_null_message;
    @Bind(R.id.subxuan)
    TextView subxuan;
    @Bind(R.id.lin_xuanze)
    LinearLayout lin_xuanze;
    //	@Bind(R.id.my_xiaomian)
//	TextView my_xiaomian;
//	@Bind(R.id.my_zhongmian)
//	TextView my_zhongmian;
//	@Bind(R.id.my_xiaohuo)
//	TextView my_xiaohuo;
//	@Bind(R.id.my_zhonghuo)
//	TextView my_zhonghuo;
//	@Bind(R.id.my_other)
//	TextView my_other;
    @Bind(R.id.edt_moneyon)
    EditText edt_moneyon;
    @Bind(R.id.edt_moneyout)
    EditText edt_moneyout;
    @Bind(R.id.edt_kmon)
    EditText edt_kmon;
    @Bind(R.id.edt_kmout)
    EditText edt_kmout;
    @Bind(R.id.edt_kgon)
    EditText edt_kgon;
    @Bind(R.id.edt_kgout)
    EditText edt_kgout;
    @Bind(R.id.edi_sou)
    EditText edi_sou;

    @Bind(R.id.btn_sunmit)
    Button btn_sunmit;

    String sortType = "";// 排序类型 1距离 2出发时间 3好评度
    private List<Data> escoreList;
    private List<Data> escoreList2;
    DownwindEscortAdapter escoreAdapter;
    private ListView escoreListView;
    private DownSpecialBean escoreBean;
    protected int pageSize = 10;// 表示一页展示多少列
    private int pageNo = 1;// 请求页码
    private int escorePageNo = 1;// 请求页码
    private int num;
    String carType = "else";// 排序类型 1距离 2出发时间 3好评度
    View view;
    public LoadingProgressDialog dialog;
    private String address, townaddress2;
    // 当前位置经纬度
    private double latitude;
    private double longitude;
    // 当前位置经纬度
    private double latitude2;
    private double longitude2;
    //发件城市
    private String city = "";
    //发件人经纬度
    private double myLatitude;// 纬度
    private double myLongitude;// 经度
    private LocationClient client;
    private Data tempData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_subf2, container, false);
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        ButterKnife.bind(this, view);
        dialog = new LoadingProgressDialog(getActivity());
//         inding();
        initData();
        getData();
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getHttpOwnerRequst(true, false, 1, false, sortType, carType);
    }

    private void inding() {
        client = new LocationClient(getActivity());
        setLocationParams();
        client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation arg0) {
                if (arg0 == null) {
                    ToastUtil.shortToast(getActivity(), "定位失败，请检查定位设置");
                    return;
                } else {
                    city = arg0.getCity();
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
//					Log.e("jpppp", latitude + ":::::::::" + longitude);
                    address = arg0.getAddrStr();
                    townaddress2 = arg0.getDistrict();
//					}
                }
            }
        });
        // 初始化定位
        // 打开GPS
        client.start();
    }

    /**
     * 百度地图定位设置
     */
    private void setLocationParams() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        client.setLocOption(option);
    }

    private void initData() {

//		final EditText input = new EditText(getActivity());   
//		input.setSingleLine(true); //android:singleLine=”true”  
//		   input.setImeOptions(EditorInfo.IME_ACTION_DONE);  
//		   input.setInputType(InputType.TYPE_CLASS_DATETIME |InputType.TYPE_CLASS_DATETIME);  
//		   input.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
//		       public boolean onEditorAction(TextView v, int actionId,    
//		               KeyEvent event)  {    
////		        Log.d(TAG, ""+actionId+","+event);  
//		           if (actionId==EditorInfo.IME_ACTION_DONE  
//		                ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {    
//		            //do something;  
//		            return true;  
//		           }    
//		           return false;    
//		       }    
//		   }); 
        escoreList = new ArrayList<Data>();
        escoreList2 = new ArrayList<Data>();
        escoreListView = escort_listview.getRefreshableView();
        getHttpOwnerRequst(true, false, 1, false, sortType, carType);
        subxuan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                lin_xuanze.setVisibility(View.VISIBLE);
            }
        });
        btn_sunmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                carType = "type";
                escoreList.clear();
                getHttpOwnerRequst(true, false, 1, false, sortType, carType);
//				getHttpOwnerRequst(false, true, 1, false, sortType,carType);
                lin_xuanze.setVisibility(View.GONE);
            }
        });
        view_null_message.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getHttpOwnerRequst(false, true, 1, false, sortType, carType);
            }
        });
//		my_xiaomian.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				lin_xuanze.setVisibility(View.GONE);
//				carType="smallMinibus";
//				getHttpOwnerRequst(true, false, 1, false, sortType,carType);
////				getHttpOwnerRequst(false, true, 1, false, sortType,carType);
//			}
//		});
//		my_zhongmian.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				lin_xuanze.setVisibility(View.GONE);
//				carType="middleMinibus";
//				getHttpOwnerRequst(true, false, 1, false, sortType,carType);
//			}
//		});
//			my_xiaohuo.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				lin_xuanze.setVisibility(View.GONE);
//				carType="smallTruck";
//				getHttpOwnerRequst(true, false, 1, false, sortType,carType);
//			}
//		});
//	  my_zhonghuo.setOnClickListener(new OnClickListener() {
//	
//		  	@Override
//		  	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		  		lin_xuanze.setVisibility(View.GONE);
//		  		carType="middleTruck";
//		  		getHttpOwnerRequst(true, false, 1, false, sortType,carType);
//		  	}
//	});
//	my_other.setOnClickListener(new OnClickListener() {
//	
//		@Override
//		public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//			lin_xuanze.setVisibility(View.GONE);
//			carType="else";
//			getHttpOwnerRequst(true, false, 1, false, sortType,carType);
//			}
//		});
        edi_sou.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                doWhichOperation(actionId);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                return true;
            }
        });
    }

    private void doWhichOperation(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                Log.e("BALLACK", edi_sou.getText().toString());
                getHttpOwnerRequst(true, false, 1, false, edi_sou.getText().toString(), carType);
                break;
            case EditorInfo.IME_ACTION_GO:
                Log.e("BALLACK", "IME_ACTION_GO");
                break;
            case EditorInfo.IME_ACTION_NEXT:
                Log.e("BALLACK", "IME_ACTION_NEXT");
                break;
            case EditorInfo.IME_ACTION_NONE:
                Log.e("BALLACK", "IME_ACTION_NONE");
                break;
            case EditorInfo.IME_ACTION_PREVIOUS:
                Log.e("BALLACK", "IME_ACTION_PREVIOUS");
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Log.e("BALLACK", "IME_ACTION_SEARCH");
                break;
            case EditorInfo.IME_ACTION_SEND:
                Log.e("BALLACK", "IME_ACTION_SEND");
                break;
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                Log.e("BALLACK", "IME_ACTION_UNSPECIFIED");
                break;
            default:
                break;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getData() {
        escort_listview.setMode(Mode.BOTH);
        escort_listview.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpOwnerRequst(true, false, 1, false, sortType);
                getHttpOwnerRequst(false, true, 1, false, sortType, carType);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                escorePageNo++;
                getHttpOwnerRequst(false, false, escorePageNo, true, sortType, carType);
                if (num < pageSize && escorePageNo > 2) {
                    Log.e("dd", num + "");
                    // refreshView.onRefreshComplete();
                    ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
                    proxy.setPullLabel("没有更多数据了");
                    proxy.setRefreshingLabel("没有更多数据了");
                    proxy.setReleaseLabel("没有更多数据了");
                }
            }
        });
        escoreListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final Data data = (Data) escoreAdapter.list.get(arg2 - 1);
                lin_xuanze.setVisibility(View.GONE);
                if (PreferencesUtils.getString(getActivity(), PreferenceConstants.USERTYPE).equals("1"))//如果为普通用户
                {
                    showPaywindow();
                } else {
                    String url = UrlMap.getUrl(MCUrl.getIfHaveAnswerRecord, "answerDriverId",
                            String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
                    dialog.show();
                    Log.e("1111111ss", url);
                    AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                            dialog.dismiss();

//                            不需要答题返回结果：{"errCode": 0, "success": true, "message": "已经完成答题!"}
//                            需要答题返回结果：{"errCode": -1, " success ": true, "message": "请先完成镖师培训题目!"}
//                            查询失败：{"errCode": -2, " success ": false, "message": "获取失败!"}

                            BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                            int errCode = bean.getErrCode();
                            String message = bean.getMessage();

                            if (errCode == -1) {
                                //todo 处理答题
                                Builder ad = new Builder(getActivity());
                                ad.setTitle("温馨提示");
                                ad.setMessage(message);
                                ad.setPositiveButton("立即培训", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intents = new Intent();
                                        intents.putExtra("url", "url");
                                        intents.setClass(getActivity(), QuestionActivity.class);
                                        startActivity(intents);
                                    }
                                });
                                ad.setNegativeButton("暂不培训", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                });
                                ad.create().show();
                            } else if (errCode == -2) {
                                ToastUtil.shortToast(getActivity(), message);
                            } else if (errCode == 0) {

                                Intent intent = new Intent(getActivity(), DownEscortDetialsActivity.class);
                                intent.putExtra("recId", String.valueOf(data.recId));
                                intent.putExtra("publishTime", data.publishTime);
                                intent.putExtra("mobile", data.mobile);
                                intent.putExtra("address", data.address.replace("中国", ""));
                                intent.putExtra("addressTo", data.addressTo.replace("中国", ""));
                                intent.putExtra("matName", data.matName);
//					intent.putExtra("transferMoney", String.valueOf(data.transferMoney));
                                intent.putExtra("transferMoney", data.transferMoney);
                                intent.putExtra("matRemark", data.matRemark);
                                intent.putExtra("matImageUrl", data.matImageUrl);
                                intent.putExtra("fromLatitude", String.valueOf(data.fromLatitude));
                                intent.putExtra("fromLongitude", String.valueOf(data.fromLongitude));
                                intent.putExtra("toLatitude", String.valueOf(data.toLatitude));
                                intent.putExtra("toLongitude", String.valueOf(data.toLongitude));
//					intent.putExtra("length", data.length);
//					intent.putExtra("wide", data.wide);
//					intent.putExtra("high", data.high);
                                intent.putExtra("matWeight", data.matWeight);
                                intent.putExtra("replaceMoney", data.replaceMoney);
                                intent.putExtra("ifReplaceMoney", data.ifReplaceMoney);
                                intent.putExtra("ifTackReplace", data.ifTackReplace);
                                intent.putExtra("limitTime", data.limitTime);
                                intent.putExtra("carLength", data.carLength);
                                intent.putExtra("useTime", data.useTime);
                                intent.putExtra("matVolume", data.matVolume);
//				startActivity(intent);
//					if (!data.status.equals("1")) {
//						ToastUtil.shortToast(getActivity(), "该单已被抢");
//					}else {
//						startActivityForResult(intent, 11);
//					}
                                if (!data.status.equals("1")) {
                                    ToastUtil.shortToast(getActivity(), "该单已被抢");
                                } else {
                                    if ("true".equals(data.ifReplaceMoney)) {
                                        Builder ad = new Builder(getActivity());
                                        ad.setTitle("温馨提示");
                                        ad.setMessage("此订单为代收款订单，接单后将冻结您账户相应金额，待收款成功后，冻结金额会支付给发货人。请务必在用户要求时间内到达");
                                        ad.setPositiveButton("确认接单", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
//										getHttpMessages(true, false, 1, false);
                                                addEscortreuslt(data.recId, data);
                                            }
                                        });
                                        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                arg0.dismiss();

                                            }
                                        });
                                        ad.create().show();
                                    } else {
                                        Builder ad = new Builder(getActivity());
                                        ad.setTitle("温馨提示");
                                        ad.setMessage("请务必在用户要求时间内到达");
                                        ad.setPositiveButton("确认接单", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
//										getHttpMessages(true, false, 1, false);
                                                addEscortreuslt(data.recId, data);
                                            }
                                        });
                                        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                arg0.dismiss();

                                            }
                                        });
                                        ad.create().show();
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                            dialog.dismiss();
                        }
                    });


                }

            }
        });

    }

    /**
     * 我是货主发布行程记录在我是镖师按钮下显示
     *
     * @param isFirst
     * @param isRefresh
     * @param isPull
     */
    @SuppressWarnings("unchecked")
    private void getHttpOwnerRequst(final boolean isFirst, final boolean isRefresh, int escorePageNo,
                                    final boolean isPull, String sortType, String carType) {
        String url;
//		if (carType.equals("type")) {
//			 url = UrlMap.getnine(MCUrl.FindDOWNWINDTASKLIST, "userId",
//					String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "pageNo",
//					String.valueOf(escorePageNo), "pageSize", String.valueOf(pageSize),
//					"distanceMin", edt_kmon.getText().toString(),"distanceMax",edt_kmout.getText().toString(),
//					"priceMin",edt_moneyon.getText().toString(),"priceMax",edt_moneyout.getText().toString(),
//					"weightMin",edt_kgon.getText().toString(),"weightMax",edt_kgout.getText().toString());
//		}else {
//			 url = UrlMap.getnine(MCUrl.FindDOWNWINDTASKLIST, "userId",
//						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "pageNo",
//						String.valueOf(escorePageNo), "pageSize", String.valueOf(pageSize),
//						"distanceMin", edt_kmon.getText().toString(),"distanceMax",edt_kmout.getText().toString(),
//						"priceMin",edt_moneyon.getText().toString(),"priceMax",edt_moneyout.getText().toString(),
//						"weightMin",edt_kgon.getText().toString(),"weightMax",edt_kgout.getText().toString());
//		}
        PreferencesUtils.getString(getActivity(), PreferenceConstants.LONGITUDE);
        PreferencesUtils.getString(getActivity(), PreferenceConstants.LATITUDE);
        url = UrlMap.getfsix(MCUrl.FindDOWNWINDTASKLIST, "userId",
                String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "pageNo",
                String.valueOf(escorePageNo), "pageSize", String.valueOf(pageSize), "search", sortType, "latitude", "" + PreferencesUtils.getString(getActivity(), PreferenceConstants.LATITUDE), "longitude", "" + PreferencesUtils.getString(getActivity(), PreferenceConstants.LONGITUDE));

        dialog.show();
        Log.e("1111111sse", url);
        AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				escoreList2.clear();
                dialog.dismiss();
                Log.e("pkdkoooooooodjfjdkj", new String(arg2));
                edi_sou.setText("");
                try {
                    escoreBean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
                    escoreList = escoreBean.data;
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (isFirst) {
//					Log.e("1111111", "222222222");
                    if (escoreList.size() != 0 && escoreList != null) {
//						Log.e("1111111", "222222222");
                        escoreAdapter = new DownwindEscortAdapter(getActivity(), escoreList);
                        escoreListView.setAdapter(escoreAdapter);
                        escoreListView.setVisibility(View.VISIBLE);
                        view_null_message.setVisibility(View.GONE);
                    } else {
                        view_null_message.setVisibility(View.VISIBLE);
                        escoreListView.setVisibility(View.GONE);
                    }
                } else if (isRefresh && !isPull) {
                    if (escoreList.size() != 0 && escoreList != null) {
                        escoreList2.clear();
                        escoreList2.addAll(escoreList);
//						Log.e("11111112", "isRefresh  "+isRefresh);
                        escoreAdapter.setData(escoreList2);
                        escoreAdapter.notifyDataSetChanged();
                        escoreAdapter.notifyDataSetInvalidated();
                        escort_listview.onRefreshComplete();
                    }
                } else if (!isRefresh && isPull) {
                    Log.e("11111113", "isRefresh  " + isRefresh);
                    num = escoreList.size();
                    escoreAdapter.addData(escoreList);
                    escoreAdapter.notifyDataSetChanged();
                    escort_listview.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                escort_listview.setVisibility(View.GONE);
                view_null_message.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示提示信息
     */
    private void showPaywindow() {
        final PopupWindow window02;
        TextView btnsaves_pan;
        Button btnsaves_que;
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwind_tishi, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window02.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(R.color.transparent01);
        @SuppressLint("ResourceAsColor")
        ColorDrawable dw = new ColorDrawable(android.R.color.white);
        window02.setBackgroundDrawable(dw);
        window02.setOutsideTouchable(false);// 这是点击外部不消失
        // 设置popWindow的显示和消失动画
        window02.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window02.showAtLocation(getActivity().findViewById(R.id.lin_xuanze), Gravity.CENTER, 0, 0);

        btnsaves_pan = (TextView) view.findViewById(R.id.btnsaves_pan);
        btnsaves_que = (Button) view.findViewById(R.id.btnsaves_que);
        btnsaves_pan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                window02.dismiss();
            }
        });
        btnsaves_que.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("tiaoguo", "3");
                intent.setClass(getActivity(), RoleAuthenticationActivity.class);
                startActivity(intent);
                window02.dismiss();
            }
        });
        // popWindow消失监听方法
        window02.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("11111s", "requestCode" + requestCode + "resultCode" + resultCode + "data" + data);
        switch (resultCode) {
            case 4:
                if (tempData != null){
                    addEscortreuslt(tempData.recId,tempData);
                }
                break;
            case 11:
                if (resultCode == -1) {
                    if (data != null) {
                        if (data.getStringExtra("type").equals("1")) {
//						finish();	
//						getHttpOwnerRequst(false, true, 1, false, sortType,carType);	
                            getHttpOwnerRequst(false, true, 1, false, sortType, carType);
//						getHttpOwnerRequst(true, false, 1, false, sortType,carType);

                        }
                    }
                }
                break;
        }

    }

    /**
     * 镖师接镖
     */
    private void addEscortreuslt(int reid, final Data data) {

        tempData = data;
        Log.e("url",
                UrlMap.getTwo(MCUrl.DOWNROBORDER, "userId",
                        String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)),
                        "recId", "" + reid));
//		if (getIntent().getStringExtra("whether").equals("A") || getIntent().getStringExtra("whether").equals("B")) {
//		}
        dialog.show();
        AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.DOWNROBORDER, "userId", String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "recId", "" + reid), null, null, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Log.e("msg", new String(arg2));
                        dialog.dismiss();
                        BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                        bean.setErrCode(-4);
                        if (bean.getErrCode() == 0) {
                            ToastUtil.shortToast(getActivity(), bean.getMessage());
//							AlertDialog.Builder ad = new Builder(getActivity());
                            Builder ad1 = new Builder(getActivity());
                            ad1.setTitle("温馨提示");
                            ad1.setMessage("请您选择继续接单或查看详情");
                            ad1.setNegativeButton("查看详情", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
//									getHttpMessages(true, false, 1, false);
//									startActivity(new Intent(getActivity(), PickToActivity.class));
                                    Intent intent = new Intent(getActivity(), DownEscoreDartActivity.class);
                                    intent.putExtra("recId", String.valueOf(data.recId));//int recId;//镖件主键
                                    intent.putExtra("toUserId", data.userId);//String publishTime;//发布时间
                                    intent.putExtra("publishTime", data.publishTime);//String publishTime;//发布时间
                                    intent.putExtra("personName", data.personName);//String personName;//发件人
                                    intent.putExtra("mobile", data.mobile);//String mobile;//发件人手机号
                                    intent.putExtra("address", data.address.replace("中国", ""));//String address;//发件地址
                                    intent.putExtra("personNameTo", data.personNameTo);//String personNameTo;//收件人
                                    intent.putExtra("matWeight", data.matWeight);//String personNameTo;//重量
                                    intent.putExtra("mobileTo", data.mobileTo);//String mobileTo;//收件人手机号
                                    intent.putExtra("addressTo", data.addressTo.replace("中国", ""));//String addressTo;//收件地址
                                    intent.putExtra("matRemark", data.matRemark);//String matRemark;//物品备注
                                    intent.putExtra("matName", data.matName);//String matRemark;//物品名字
                                    intent.putExtra("transferMoney", String.valueOf(data.transferMoney));//String matRemark;//物品价格
                                    intent.putExtra("readyTime", data.readyTime);//String readyTime
                                    intent.putExtra("replaceMoney", data.replaceMoney);//
                                    intent.putExtra("ifReplaceMoney", data.ifReplaceMoney);//
                                    intent.putExtra("ifTackReplace", data.ifTackReplace);//
                                    intent.putExtra("carType", data.carType);//
                                    intent.putExtra("carLength", data.carLength);
                                    intent.putExtra("matVolume", data.matVolume);
//									intent.putExtra("useTime",  data.useTime);

                                    intent.putExtra("fromLatitude", data.fromLatitude);
                                    intent.putExtra("fromLongitude", data.fromLongitude);
                                    intent.putExtra("toLatitude", data.toLatitude);
                                    intent.putExtra("toLongitude", data.toLongitude);
                                    intent.putExtra("cargoSize", data.cargoSize);//件数

                                    intent.putExtra("distance", data.distance);

                                    if (!data.length.equals("0") && !data.wide.equals("0") && !data.high.equals("0")) {
                                        intent.putExtra("length", "长：" + data.length + " 厘米" + "  宽：" + data.wide + " 厘米" + "  高：" + data.high + " 厘米");
                                    } else if (!data.length.equals("0")) {
                                        if (!data.wide.equals("0")) {
                                            intent.putExtra("length", "长：" + data.length + "厘米  " + "  宽：" + data.wide + "厘米  ");
                                        } else if (!data.high.equals("0")) {
                                            intent.putExtra("length", "长：" + data.length + "厘米  " + "  高：" + data.high + "厘米  ");
                                        } else {
                                            intent.putExtra("length", "长：" + data.length + "厘米  ");
                                        }
                                    } else if (!data.wide.equals("0")) {
                                        if (!data.high.equals("0")) {
                                            intent.putExtra("length", "宽：" + data.wide + "厘米  " + "  高：" + data.high + "厘米  ");
                                        } else {
                                            intent.putExtra("length", "宽：" + data.wide + "厘米  ");
                                        }
                                    } else if (!data.high.equals("0")) {
                                        intent.putExtra("length", "高：" + data.high + "厘米  ");
                                    } else if (data.length.equals("0") && !data.wide.equals("0") && data.high.equals("0")) {
                                        intent.putExtra("length", "用户未填写 ");
                                    }
//									intent.putExtra("length", "长："+data.length+"  宽："+data.wide+"  高："+data.high);//物品体积
                                    intent.putExtra("status", "2");//状态 0-预发布成功(未支付) 1-支付成功(已支付)  2(已抢单) 3 已取件 4 订单取消()镖师  5 成功  6 删除 7 已评价 8订单取消（用户）
                                    intent.putExtra("ifAgree", data.ifAgree);
//									if(!data.useTime.equals("")){
                                    intent.putExtra("useTime", data.useTime);//达到时间
//									}else {
//										intent.putExtra("useTime", data.limitTime);//期望到达时间
//									}
                                    startActivity(intent);
//									startActivity(intent1);

                                }
                            });
                            ad1.setPositiveButton("继续接单", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    getHttpOwnerRequst(false, true, 1, false, sortType, carType);
                                }
                            });
                            ad1.create().show();
                        } else if (bean.getErrCode() == 2 || bean.getErrCode() == -2) {
                            ToastUtil.shortToast(getActivity(), bean.getMessage());
                        } else if (bean.getErrCode() == 4) {

//							ToastUtil.shortToast(DownEscortDetialsActivity.this, bean.getMessage());
                            Builder ad = new Builder(getActivity());
                            ad.setTitle("温馨提示");
                            ad.setMessage(bean.getMessage());
                            ad.setNegativeButton("去充值", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
//									getHttpMessages(true, false, 1, false);
                                    Intent intent = new Intent();
                                    startActivity(intent.setClass(getActivity(), RechargeActivity.class));
                                }
                            });
                            ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();

                                }
                            });
                            ad.create().show();


                        } else if (bean.getErrCode() == -6) {
                            Builder ad = new Builder(getActivity());
                            ad.setTitle("温馨提示");
                            ad.setMessage(bean.getMessage());

                            ad.setPositiveButton("立即前往", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
//									getHttpMessages(true, false, 1, false);
                                    Intent intent = new Intent();
//									startActivity(intent.setClass(getActivity(), RechargeActivity.class));
                                    startActivity(intent.setClass(getActivity(), DepositsActivity.class));
                                }
                            });
                            ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();

                                }
                            });
                            ad.create().show();

                        } else if (bean.getErrCode() == -4) {

//							ToastUtil.shortToast(DownEscortDetialsActivity.this, bean.getMessage());
                            Builder ad = new Builder(getActivity());
                            ad.setTitle("温馨提示");
//                            ad.setMessage("镖师接单，必须已经在保险机构购买交通意外险。");
                            ad.setMessage(bean.getMessage());

                            ad.setPositiveButton("本人自己解决意外险问题", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
//									getHttpMessages(true, false, 1, false);
//                                    Intent intent = new Intent();
//                                    startActivityForResult(intent.setClass(getActivity(), InsuranceActivity.class),4);

                                    updateIfHaveBuyInsure();
                                }
                            });
//                            ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    arg0.dismiss();
//
//                                }
//                            });
                            ad.create().show();
                        } else if (bean.getErrCode() == -9) {
                            ToastUtil.shortToast(getActivity(), bean.getMessage());
                        } else {
                            ToastUtil.shortToast(getActivity(), bean.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        dialog.dismiss();
                    }
                });

    }

    private void updateIfHaveBuyInsure(){
        String url = UrlMap.getUrl(MCUrl.updateIfHaveBuyInsure, "userId",
                String.valueOf(PreferencesUtils.getInt(getContext(), PreferenceConstants.UID)));
        dialog.show();
        url += "&ifHaveBuyInsure=1";
        Log.e("1111111ss", url);
        AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dialog.dismiss();

                Log.e("oppo", new String(arg2));
                BaseBean beans = new Gson().fromJson(new String(arg2), BaseBean.class);
                int errCode = beans.getErrCode();
                String message = beans.getMessage();

                if (errCode == 0) {
                    //int resultCode, 结果码,用于区分到底是哪个的返回数据
                    if (tempData != null){
                        addEscortreuslt(tempData.recId,tempData);
                    }
                } else if (errCode == -1) {
                    ToastUtil.shortToast(getContext(), message);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Log.e("1111111ss", new String(arg2));
                dialog.dismiss();
            }
        });
    }
}
