package com.hex.express.iwant.subfragment;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.hex.express.iwant.R.id;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.ProvCityTownAcrivity;
import com.hex.express.iwant.activities.QuestionActivity;
import com.hex.express.iwant.activities.RoleAuthenticationActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.NearbyBean;
import com.hex.express.iwant.bean.NearbyBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubFragment3 extends Fragment {

    @Bind(id.freigh_listviewse)
    PullToRefreshListView freigh_listview;
    @Bind(id.null_message)
    View view_null_message;
    @Bind(id.subxuan)
    TextView subxuan;
    @Bind(id.edi_sou)
    EditText edi_sou;

    private ListView listView;
    protected int pageSize = 10;// 表示一页展示多少列
    protected int pageNo = 1;
    private List<Data> mList;
    private List<Data> mList2;
    private NearbyAdapter adapter;
    public NearbyBean bean;
    private int num;
    private String cityCode;
    private String entPlaceCityCode;
    private String search = "";
    private int type = 1;
    String uud;
    View view;
    //	public LoadingProgressDialog dialog;
    private double latitude;
    private double longitude;
    private String city;
    private String cityCode2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_subf3, container, false);
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        ButterKnife.bind(this, view);
//         dialog=new LoadingProgressDialog(getActivity());
//         inview();
        uud = String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID));
        cityCode2 = PreferencesUtils.getString(getActivity(), PreferenceConstants.CITYCODE);
//     	Log.e("111111   ", cityCode2);
        initData();
        getData();


        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//		getHttprequst(false, true, 1, false);
    }

    private LocationClient client;
    private BDLocation location;

    private void inview() {

        client = new LocationClient(getActivity());
        setLocationParams();
        client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation arg0) {
                // TODO Auto-generated method stub
                if (arg0 == null) {
                    ToastUtil.shortToast(getActivity(), "定位失败，请检查设置");
                    return;
                } else {
//					if (dialog != null)
//						dialog.dismiss();
                    city = arg0.getCity();
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
                    getCityCodes();

                }
            }
        });
        client.start();

//		ToastUtil.shortToast(getActivity(), "11111   "+city);
    }

    int i = 1;

    private void getCityCodes() {
        boolean isCopySuccess = CheckDbUtils.checkDb();
        // 成功的将数据库copy到data 中
        if (isCopySuccess) {
            iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
        }
        if (city == null || city.equals("")) {
            return;
        }
        Log.e("city", city);
        if (!city.contains("市")) {
            city = city + "市";
        }

//		List<CityBean> selectDataFromDb = new CityDbOperation()
//				.selectDataFromDb("select * from city where city_name='" + city + "'");
//			cityCode2 = selectDataFromDb.get(0).city_code;
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + city + "'");
        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
            cityCode2 = selectDataFromDb.get(0).city_code;
//				Log.e("citycode", cityCode2);
        }


        Log.e("11111citycode33", cityCode2);
        i = i + 1;
        Log.e("11111citycode33  s ", i + "");
//			ToastUtil.shortToast(getActivity(), "cityCode2 "+cityCode2);
        if (i < 3) {
            getHttprequst(true, false, 1, false, "");
//				i=i+1;
        } else {

        }

//			stoop();
    }

    private void stoop() {
        new Timer().schedule(new TimerTask() {
            public void run() {


            }
        }, 1800000);
    }

    private void setLocationParams() {
        // TODO Auto-generated method stub
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 2400000;
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
        cityCode = PreferencesUtils.getString(getActivity(), PreferenceConstants.CITYCODE, cityCode);
//		 ToastUtil.shortToast(getActivity(), "cityCode "+cityCode);
        mList = new ArrayList<Data>();
        mList2 = new ArrayList<Data>();
        listView = freigh_listview.getRefreshableView();
        getHttprequst(true, false, 1, false, "");
        subxuan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(getActivity(), ProvCityTownAcrivity.class), 1);

            }
        });
        edi_sou.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                doWhichOperation(actionId);

                return true;
            }
        });

        view_null_message.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getHttprequst(false, true, 1, false, "");
            }
        });
    }

    private void doWhichOperation(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                Log.e("BALLACK", edi_sou.getText().toString());
                getHttprequst(true, false, 1, false, edi_sou.getText().toString());
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

    /**
     * 获取网络数据
     */
    private void getHttprequst(final boolean isFirst, final boolean isRefresh,
                               int pageNo, final boolean isPull, String search) {
        String scotde;
//		if ("".equals(cityCode2)) {
//			scotde=cityCode;
//		}else {
//			scotde=cityCode2;
//		}

        String url = UrlMap.getfsix(MCUrl.NEARBY, "userId", uud,
                "startPlaceCityCode", cityCode2, "entPlaceCityCode", "", "search", "" + search, "pageNo", String.valueOf(pageNo), "pageSize", String
                        .valueOf(pageSize));
        Log.e("11111fjin", url);
//		dialog.show();
        mList.clear();
        AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				dialog.dismiss();
                mList2.clear();
                Log.e("11111fjin", new String(arg2));

                try {
                    bean = new Gson().fromJson(new String(arg2),
                            NearbyBean.class);
                    mList = bean.data;
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (isFirst) {
                    if (mList.size() != 0 && mList != null) {
//						if (adapter == null) {adapter = new NearbyAdapter(
//									getActivity(), mList);
                        adapter = new NearbyAdapter(getActivity(), mList);
                        listView.setAdapter(adapter);
                        view_null_message.setVisibility(View.GONE);
                        freigh_listview.setVisibility(View.VISIBLE);
//						}
                    } else {
                        view_null_message.setVisibility(View.VISIBLE);
                        freigh_listview.setVisibility(View.GONE);
                    }
                } else {
                    if (isRefresh && !isPull) {
                        mList2.clear();
//						mList2 = bean.data;
                        if (mList.size() != 0 && mList != null) {
//							mList2.addAll(mList);
//							Log.e("111111  ", mList2.size()+"");
                            adapter = new NearbyAdapter(
                                    getActivity(), mList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            freigh_listview.onRefreshComplete();
                            view_null_message.setVisibility(View.GONE);
                            freigh_listview.setVisibility(View.VISIBLE);
                        } else {
                            view_null_message.setVisibility(View.VISIBLE);
                            freigh_listview.setVisibility(View.GONE);
                        }
                    } else if (!isRefresh && isPull) {
                        num = mList.size();
                        adapter.addData(mList);
                        adapter.notifyDataSetChanged();
                        freigh_listview.onRefreshComplete();
                    }
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                freigh_listview.setVisibility(View.GONE);
                view_null_message.setVisibility(View.VISIBLE);
//				dialog.dismiss();
            }
        });
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getData() {
        freigh_listview.setMode(Mode.BOTH);
        freigh_listview.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpOwnerRequst(true, false, 1, false, sortType);
                getHttprequst(false, true, 1, false, "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                pageNo++;
                getHttprequst(false, false, pageNo, true, "");
                if (num < pageSize && pageNo > 2) {
                    Log.e("dd", num + "");
                    // refreshView.onRefreshComplete();
                    ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
                    proxy.setPullLabel("没有更多数据了");
                    proxy.setRefreshingLabel("没有更多数据了");
                    proxy.setReleaseLabel("没有更多数据了");
                }
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                // TODO Auto-generated method stub

                if (PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("1") ||
                        PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("2") ||
                        PreferencesUtils.getString(getActivity(), PreferenceConstants.WLID).equals("4")) {
                    String url = UrlMap.getUrl(MCUrl.getIfHaveAnswerRecord, "answerDriverId",
                            String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
                    Log.e("1111111ss", url);
                    AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

//                            不需要答题返回结果：{"errCode": 0, "success": true, "message": "已经完成答题!"}
//                            需要答题返回结果：{"errCode": -1, " success ": true, "message": "请先完成镖师培训题目!"}
//                            查询失败：{"errCode": -2, " success ": false, "message": "获取失败!"}
                            BaseBean beans = new Gson().fromJson(new String(arg2), BaseBean.class);
                            int errCode = beans.getErrCode();
                            String message = beans.getMessage();

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

                                Data data = (Data) adapter.list.get(position - 1);

                                Intent intent = new Intent(getActivity(), LogCompanyActivity.class);

                                //					SubFragment3.this.startActivityForResult(intent, 12);
                                intent.putExtra("recId", data.recId);//int recId;//镖件主键
                                intent.putExtra("userId", data.userId);//发件人id
                                intent.putExtra("cargoName", data.cargoName);//货物名称
                                intent.putExtra("startPlace", data.startPlace);//物品起发地址
                                intent.putExtra("entPlace", data.entPlace);//物品到达地址
                                intent.putExtra("cargoWeight", data.weight);//物品重量
                                intent.putExtra("cargoVolume", data.cargoVolume);//物品体积
                                intent.putExtra("takeTime", data.takeTime);//取货时间
                                intent.putExtra("arriveTime", data.arriveTime);//到达时间
                                intent.putExtra("takeName", data.takeName);//收货人姓名
                                intent.putExtra("takeMobile", data.takeMobile);//收货人地址
                                intent.putExtra("remark", data.remark);//备注
                                intent.putExtra("billCode", data.billCode);//单号
                                intent.putExtra("takeCargo", data.takeCargo);//是否要取货
                                intent.putExtra("sendCargo", data.sendCargo);//是否送取货
                                intent.putExtra("sendName", data.sendPerson);//发货人
                                intent.putExtra("sendMobile", data.sendPhone);//发货人手机号
                                intent.putExtra("sendNumber", data.sendNumber);//发货次数
                                intent.putExtra("publishTime", data.publishTime);//发布时间
                                intent.putExtra("transferMoney", data.transferMoney);// 总钱
                                intent.putExtra("takeCargoMoney", data.takeCargoMoney);// 取货费
                                intent.putExtra("sendCargoMoney", data.sendCargoMoney);// 送货上门
                                intent.putExtra("cargoTotal", data.cargoTotal);// 运费
                                intent.putExtra("ifQuotion", data.ifQuotion);//
                                intent.putExtra("cargoNumber", data.cargoSize);//
                                intent.putExtra("appontSpace", data.appontSpace);//

                                intent.putExtra("matWeight", data.matWeight);//
                                intent.putExtra("length", data.length);//
                                intent.putExtra("wide", data.wide);//
                                intent.putExtra("high", data.high);//
//					intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);// 货物价值
                                intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
                                intent.putExtra("status", bean.getData().get(position).status);//
                                intent.putExtra("yardAddress", bean.getData().get(position).yardAddress);//
                                intent.putExtra("carType", bean.getData().get(position).carType);//
                                intent.putExtra("carName", bean.getData().get(position).carName);//
                                intent.putExtra("tem", bean.getData().get(position).tem);//
//					startActivity(intent);
                                startActivityForResult(intent, 12);
                            }
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        }
                    });


                } else {
                    Builder ad = new Builder(getActivity());
                    ad.setTitle("温馨提示");
                    ad.setMessage("您还不是物流司机或者大货司机，请认证后报价！");
                    ad.setPositiveButton("立即认证", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intents = new Intent();
                            intents.putExtra("tiaoguo", "2");
                            intents.setClass(getActivity(), RoleAuthenticationActivity.class);
                            startActivity(intents);
                        }
                    });
                    ad.setNegativeButton("暂不认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
                    ad.create().show();
                }


            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("SubFragment3", "" + requestCode);
//		ToastUtil.shortToast(getActivity(), "的+"+data.getStringExtra("types"));
        switch (requestCode) {
            case 1:
                if (data == null) {
                    return;
                }
                if (data.getStringExtra("code") == null)
                    return;
//			area=data.getStringExtra("name");
                entPlaceCityCode = data.getStringExtra("code");
//			citys=data.getStringExtra("city");
                type = data.getIntExtra("type", 1);
                mList.clear();
                getHttprequst(true, false, 1, false, "");
                break;
            case 12:
//			if (data!=null) {
//				return;
//			}

                getHttprequst(false, true, 1, false, "");
//			Log.e("11111223333crequestCode  ", ""+requestCode);
//			if (!"".equals(data.getStringExtra("types"))) {
//				if ("1".equals(data.getStringExtra("types"))) {
//					getHttprequst(false, true, 1, false);
//					Log.e("11111223333crequestCode  ", ""+requestCode);
//					
//				}
//				
//			}
                break;
        }

    }

    class NearbyAdapter extends BaseListAdapter {

        public NearbyAdapter(Context context, List list) {
            super(context, list);
            // TODO Auto-generated constructor stub
        }

        @Override
        public ViewHolder onCreateViewHolder(View itemView) {
            // TODO Auto-generated method stub
            return new OwnerViewHolder(itemView);
        }

        @Override
        public int getLayoutResource() {
            // TODO Auto-generated method stub
            //informat_newitem   informat_item  新的改的
            return R.layout.informat_newitem;//informat_item  information_item
        }

        class OwnerViewHolder extends ViewHolder {

            public OwnerViewHolder(View itemView) {
                super(itemView);
            }

            @Bind(id.infor_juli)//距离
                    TextView infor_juli;
            @Bind(id.infor_time)//时间
                    TextView infor_time;
            @Bind(id.infor_wupin)//物品名字
                    TextView infor_wupin;
//			@Bind(R.id.infor_jiazhi)//物品价值
//			TextView infor_jiazhi;

            @Bind(id.infor_zhongliang)// 重量
                    TextView infor_zhongliang;
            @Bind(id.infor_tiji)//体积
                    TextView infor_tiji;
            @Bind(id.infor_quhuo)//取货操作
                    TextView infor_quhuo;
            @Bind(id.infor_daodadizhi)//达到地址
                    TextView infor_daodadizhi;
            @Bind(id.infor_songhuo)//送货操作
                    TextView infor_songhuo;
            @Bind(id.infor_baojia)//确认按钮
                    Button infor_baojia;


            @Bind(id.tem)//
                    TextView tem;
            @Bind(id.recoldcar)//冷链
                    RelativeLayout recoldcar;
            @Bind(id.resonghuo)//正常
                    RelativeLayout resonghuo;
            private NearbyBean bean;

            @Override
            public void setData(final int position) {
                super.setData(position);
                bean = new NearbyBean();
                bean.data = list;
//				Log.e("BEAM", bean.data.get(position).toString());
//				System.out.println("111111111钱2 "+bean.data.size()+"   dd "+bean.getData().get(position).cargoName);
//				infor_juli.setText("距离发货地："+bean.getData().get(position).distance+"km");
                if (bean.getData().get(position).carType.equals("cold")) {
                    infor_juli.setText("需求：" + bean.getData().get(position).carName);
                }

                infor_time.setText("要求到达时间：" + bean.getData().get(position).publishTime);
                infor_wupin.setText("物品名称：" + bean.getData().get(position).cargoName);
//				if (bean.getData().get(position).weight.equals("5")) {
//					infor_zhongliang.setText("总重量：≤"+bean.getData().get(position).weight+" 公斤");
//				}else {
                infor_zhongliang.setText("总重量：" + bean.getData().get(position).cargoWeight + " ");
//				}

                infor_tiji.setText("总体积：" + bean.getData().get(position).cargoVolume);
                if ("cold".equals(bean.getData().get(position).carType)) {
                    recoldcar.setVisibility(View.VISIBLE);
                    resonghuo.setVisibility(View.GONE);
                    tem.setText("温度要求：" + bean.getData().get(position).tem);
                } else {
                    recoldcar.setVisibility(View.GONE);
                    resonghuo.setVisibility(View.VISIBLE);
                }
//				infor_tiji.setText("单件体积： "+" 长"+bean.getData().get(position).length+"厘米"+" 宽"+bean.getData().get(position).wide+"厘米"+" 高"+bean.getData().get(position).high+"厘米");
//				String	ct=bean.getData().get(position).cargoCost;
//				String[] strs=ct.split("\\.");
//				infor_jiazhi.setText("价值："+strs[0]+"元");
//				infor_jiazhi.setText("价值："+bean.getData().get(position).cargoCost+"元");
//				infor_daodadizhi.setText(""+bean.getData().get(position).entPlace);
                infor_daodadizhi.setText("" + bean.getData().get(position).endPlaceName);
//				infor_zhongliang.setText("重量："+bean.getData().get(position).);

                if (bean.getData().get(position).takeCargo == false) {
                    infor_quhuo.setText("发货人送到货场");
                } else {
                    infor_quhuo.setText("物流公司上门取货");
                }
                if (bean.getData().get(position).sendCargo == false) {
                    infor_songhuo.setText("收件人自提");
                } else {
                    infor_songhuo.setText("物流公司送货上门");
                }
                if (bean.getData().get(position).ifQuotion) {
//					infor_baojia.setText("修改报价");
                    infor_baojia.setBackgroundResource(R.drawable.xiugaibaojia_new);
                } else {
                    infor_baojia.setBackgroundResource(R.drawable.new_baojia_bg);
                }
                infor_baojia.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        final Intent intent = new Intent(context, LogCompanyActivity.class);
                        intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
                        intent.putExtra("userId", bean.getData().get(position).userId);//发件人id
                        intent.putExtra("cargoName", bean.getData().get(position).cargoName);//货物名称
                        intent.putExtra("startPlace", bean.getData().get(position).startPlace);//物品起发地址
                        intent.putExtra("entPlace", bean.getData().get(position).entPlace);//物品到达地址
                        intent.putExtra("cargoWeight", bean.getData().get(position).cargoWeight);//物品重量
                        intent.putExtra("cargoVolume", bean.getData().get(position).cargoVolume);//物品体积
                        intent.putExtra("takeTime", bean.getData().get(position).takeTime);//取货时间
                        intent.putExtra("arriveTime", bean.getData().get(position).arriveTime);//到达时间
                        intent.putExtra("takeName", bean.getData().get(position).takeName);//收货人姓名
                        intent.putExtra("takeMobile", bean.getData().get(position).takeMobile);//收货人地址
                        intent.putExtra("remark", bean.getData().get(position).remark);//备注
                        intent.putExtra("billCode", bean.getData().get(position).billCode);//单号
                        intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
                        intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否送取货
                        intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
                        intent.putExtra("sendMobile", bean.getData().get(position).sendMobile);//发货人手机号
                        intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
                        intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
                        intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);// 总钱
                        intent.putExtra("takeCargoMoney", bean.getData().get(position).takeCargoMoney);// 取货费
                        intent.putExtra("sendCargoMoney", bean.getData().get(position).sendCargoMoney);// 送货上门
                        intent.putExtra("cargoTotal", bean.getData().get(position).cargoTotal);// 运费
                        intent.putExtra("cargoNumber", bean.getData().get(position).cargoSize);//
//						intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);// 货物价值
                        intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
                        intent.putExtra("status", bean.getData().get(position).status);//
                        intent.putExtra("appontSpace", bean.getData().get(position).appontSpace);//
                        intent.putExtra("yardAddress", bean.getData().get(position).yardAddress);//
                        intent.putExtra("ifQuotion", bean.getData().get(position).ifQuotion);// 是否报价
                        Log.e("11111", "" + bean.getData().get(position).recId);
                        intent.putExtra("matWeight", bean.getData().get(position).matWeight);//
                        intent.putExtra("length", bean.getData().get(position).length);//
                        intent.putExtra("wide", bean.getData().get(position).wide);//

                        intent.putExtra("high", bean.getData().get(position).high);//

                        intent.putExtra("carType", bean.getData().get(position).carType);//
                        intent.putExtra("carName", bean.getData().get(position).carName);//
                        intent.putExtra("tem", bean.getData().get(position).tem);//

//						intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
//						startActivityForResult(intent, 10);
                        if (PreferencesUtils.getString(context, PreferenceConstants.WLID).equals("1") ||
                                PreferencesUtils.getString(context, PreferenceConstants.WLID).equals("2")
                                || PreferencesUtils.getString(context, PreferenceConstants.WLID).equals("4")) {
//							context.startActivity(intent);
//							context.startActivity(intent);

                            String url = UrlMap.getUrl(MCUrl.getIfHaveAnswerRecord, "answerDriverId",
                                    String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
                            Log.e("1111111ss", url);
                            AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

//                            不需要答题返回结果：{"errCode": 0, "success": true, "message": "已经完成答题!"}
//                            需要答题返回结果：{"errCode": -1, " success ": true, "message": "请先完成镖师培训题目!"}
//                            查询失败：{"errCode": -2, " success ": false, "message": "获取失败!"}
                                    BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                                    int errCode = bean.getErrCode();
                                    String message = bean.getMessage();

                                    if (errCode == -1) {
                                        //todo 处理答题
                                        Builder ad = new Builder(context);
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
                                        startActivityForResult(intent, 12);
                                    }
                                }

                                @Override
                                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                }
                            });


                        } else {
                            Builder ad = new Builder(context);
                            ad.setTitle("温馨提示");
                            ad.setMessage("您还不是物流司机或者大货司机，请认证后报价！");
                            ad.setPositiveButton("立即认证", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intents = new Intent();
                                    intents.putExtra("tiaoguo", "2");
                                    intents.setClass(context, RoleAuthenticationActivity.class);
                                    startActivity(intents);
                                }
                            });
                            ad.setNegativeButton("暂不认证", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            ad.create().show();
                        }


                    }
                });
            }
        }
    }
}
