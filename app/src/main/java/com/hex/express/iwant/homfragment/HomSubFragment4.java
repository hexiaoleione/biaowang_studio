package com.hex.express.iwant.homfragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.AddressActivity;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newbaidu.LocationDemo;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomSubFragment4 extends Fragment {

    // 寄件人信息
    @Bind(R.id.et_name)
    EditText et_name;//寄件人姓名
    @Bind(R.id.et_tel)
    EditText et_tel;//寄件人电话
    @Bind(R.id.et_address)
    EditText et_address;//寄件人地址
    @Bind(R.id.tv_modifySenderAddress)
    TextView tv_modifySenderAddress;// 修改寄件人地址
    @Bind(R.id.iv_senderList)
    ImageView iv_senderList;// 发件人获取手机通讯录；
    //		 收件人信息
    @Bind(R.id.et_add_name)
    EditText et_add_name;
    @Bind(R.id.et_add_tel)
    EditText et_add_tel;
    @Bind(R.id.spinner1)//重量选择
            Spinner spinner1;
    @Bind(R.id.et_add_address)
    EditText et_add_address;
    @Bind(R.id.et_address_specific)
    EditText et_address_specific; //发件人具体地址
    @Bind(R.id.et_add_address_specific)
    EditText et_add_address_specific; //收件人具体地址
    @Bind(R.id.tv_modifyRecieverAddress)
    TextView tv_modifyRecieverAddress;// 修改收件人地址
    @Bind(R.id.iv_receiverList)
    ImageView iv_receiverList;// 收件人获取手机通讯录；
    //		// 物品信息
    @Bind(R.id.et_goodname)
    EditText et_goodname;
    @Bind(R.id.et_weight)
    EditText et_weight;
    @Bind(R.id.et_time)
    EditText et_time;
    @Bind(R.id.et_volume)
    EditText et_volume;
    @Bind(R.id.etjian)
    EditText etjian;


    @Bind(R.id.checkbox_wuliu)
    CheckBox checkbox_wuliu;
    @Bind(R.id.checkbox_wuliu2)
    CheckBox checkbox_wuliu2;
    // 当前位置经纬度
    private double latitude;
    private double longitude;
    private double latitude2;
    private double longitude2;
    private String city, city2;
    // 寄件人经纬度
    private double mylatitude;// 经度
    private double mylongitude;// 纬度
    private LocationClient client;
    private String cityCode;
    // 收件人经纬度
    //收件人经纬度
    private double receiverLongitude;
    private double receiverLatitude;
    //收件地址的城市代码
    private String receiver_citycode = "";
    private boolean receive;
    private String icon;
    private Calendar c = null;//时间选择
    private final static int DATE_DIALOG = 0;
    private final static int DATE = 1;
    private ArrayAdapter<String> adapter;

    private boolean up = false;//上门取货
    private boolean down = false;//送货上门
    private boolean weith;//重量  顿true
    private boolean frist = false;// 是否第一次获取位置成功
    private boolean chox = false;
    private boolean chox2 = false;
    PopupWindow window02;
    PopupWindow window03;
    TimePickerView pvTime;
    TimePickerView pvTimeto;
    String startPlaceTownCode, townaddressd, townaddressd2;
    //选择地址回调得到的地址
    private String return_address;
    private String townCode;
    private String townCode3, cityCode3;
    private String cityCode2, townCode2;
    private boolean namesb = false;
    private String namese, phonese, endPlaceName;
    @Bind(R.id.hlast_step_layout)
    LinearLayout hlast_step_layout;//第一个界面
    @Bind(R.id.hlast_steps)
    Button hlast_steps;//上一步er
    @Bind(R.id.hlast_stepse)
    Button hlast_stepse;//上一步san

    @Bind(R.id.next_layout)
    LinearLayout next_layout;//第二个界面
    @Bind(R.id.hnext_steps)
    Button hnext_steps;//下一步
    @Bind(R.id.zuihoulayout)
    LinearLayout end_layout;//第三个界面

    @Bind(R.id.hlast_step)
    Button hlast_step;//上一步
    @Bind(R.id.hnext_step)
    Button hnext_step;//下一步

    @Bind(R.id.hsuccess)
    Button hsuccess;//  完成
    @Bind(R.id.houaddse)
    LinearLayout houaddse;//指定货场地址
    @Bind(R.id.et_huodizhi)//输入框
            EditText et_huodizhi;


    @Bind(R.id.et_carchang)
    Spinner et_carchang;
    @Bind(R.id.et_tijisp)
    Spinner et_tijisp;
    //定位得到的地址
    private String address;
    View view;
    boolean iscode = false;
    public LoadingProgressDialog dialog;

    //	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_newhom4, container, false);
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        ButterKnife.bind(this, view);
        dialog = new LoadingProgressDialog(getActivity());
        intview();
        initData();
        // 时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        // 控制时间范围
// 		 Calendar calendar = Calendar.getInstance();
// 		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
// 		 calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");//日期格式
                String hehe = dateFormat.format(now);
                SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd ");
                Date begin = null;
                Date end = null;
                try {
                    begin = dfs.parse(hehe);
                    end = dfs.parse(getTimes(date));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("111shijian", "" + getTimes(date));
                long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒

                if (between / (60 * 60 * 24) >= 0) {
                    et_time.setText(getTimes(date));
                } else {
                    ToastUtil.shortToast(getActivity(), "请选择正确的到达日期");
                }


            }
        });
        return view;
    }

    public boolean isLogin() {
        return PreferencesUtils.getBoolean(getActivity(), PreferenceConstants.ISLOGIN);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isLogin()) {
//			 getrequstBalance();
        } else {
            et_name.setText("");
            et_tel.setText("");
        }
    }

    public void initData() {
        // TODO Auto-generated method stub

        client = new LocationClient(getActivity());
        initLocation();
        client.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                latitude = arg0.getLatitude();
                longitude = arg0.getLongitude();
                address = arg0.getAddrStr();
                townaddressd = arg0.getDistrict();
                if (frist) {

                    et_address.setText(return_address);
                } else {
                    et_address.setText(arg0.getAddrStr());
                }

//				getCityCode(true, city,1);
                getCityCode2(true, arg0.getCity());
//				Log.e("jpppp", latitude + ":::::::::" + longitude);
            }
        });
        // 初始化定位
        // 打开GPS
        client.start();
    }

    /**
     * 获取
     */
    private void getrequstBalance() {
        RequestParams params = new RequestParams();
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.BALANCE, "id",
                        String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
                null, null, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Logger.e("json", "" + new String(arg2));

                        RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
                        if (bean.getErrCode() == 0) {

                            PreferencesUtils.putString(getActivity(), PreferenceConstants.USERTYPE,
                                    bean.data.get(0).userType);
                            PreferencesUtils.putString(getActivity(), PreferenceConstants.REALMANAUTH,
                                    bean.data.get(0).realManAuth);
                            PreferencesUtils.putInt(getActivity(), PreferenceConstants.UID,
                                    bean.data.get(0).userId);
                            PreferencesUtils.putString(getActivity(), PreferenceConstants.AgreementType, bean.getData().get(0).agreementType);
                            PreferencesUtils.putString(getActivity(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
//						handler.sendEmptyMessage(0);
                            if (namesb) {
                                et_name.setText(namese);
                                et_tel.setText(phonese);
                            } else {
                                et_name.setText(bean.data.get(0).userName);
                                et_tel.setText(bean.data.get(0).mobile);
                            }
                            latitude = bean.data.get(0).latitude;
                            longitude = bean.data.get(0).longitude;
//						 try {
//								if (PreferencesUtils.getString(getActivity(), PreferenceConstants.USERNAME) != null
//										&& !PreferencesUtils.getString(getActivity(), PreferenceConstants.USERNAME).equals("")) {
//									et_name.setText(PreferencesUtils.getString(getActivity(), PreferenceConstants.USERNAME));
//								}
//								if (!PreferencesUtils.getString(getActivity(), PreferenceConstants.MOBILE).equals("")
//										&& PreferencesUtils.getString(getActivity(), PreferenceConstants.MOBILE) != null) {
//									et_tel.setText(PreferencesUtils.getString(getActivity(), PreferenceConstants.MOBILE));
//								}
//								
//									latitude=Double.valueOf( PreferencesUtils.getString(getActivity(), PreferenceConstants.LATITUDE)).doubleValue();
//									longitude=Double.valueOf( PreferencesUtils.getString(getActivity(), PreferenceConstants.LONGITUDE)).doubleValue();
//									 } catch (Exception e) {
                            // TODO: handle exception
//										}
                        }

                    }

                });
    }

    private void intview() {

        hnext_step.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isLogin()) {
                    hlast_step_layout.setVisibility(View.GONE);
                    next_layout.setVisibility(View.VISIBLE);
                    end_layout.setVisibility(View.GONE);

                    hlast_step.setVisibility(View.GONE);
                    hlast_steps.setVisibility(View.VISIBLE);
                    hnext_step.setVisibility(View.GONE);
                    hnext_steps.setVisibility(View.VISIBLE);
                    hsuccess.setVisibility(View.GONE);
                } else {
                    ToastUtil.shortToast(getActivity(), "请登录后发布");
                }
            }
        });
        hnext_steps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                addPostResult();
//				hlast_step_layout.setVisibility(View.GONE);
//				next_layout.setVisibility(View.GONE);
//				end_layout.setVisibility(View.VISIBLE);
//				
//				hlast_step.setVisibility(View.VISIBLE);
//				hlast_steps.setVisibility(View.GONE);
//				hnext_step.setVisibility(View.GONE);
//				hnext_steps.setVisibility(View.GONE);
//				hsuccess.setVisibility(View.VISIBLE);
            }
        });
        hlast_step.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                hlast_step_layout.setVisibility(View.GONE);
                next_layout.setVisibility(View.VISIBLE);
                end_layout.setVisibility(View.GONE);

                hlast_step.setVisibility(View.GONE);
                hlast_steps.setVisibility(View.VISIBLE);
                hnext_step.setVisibility(View.GONE);
                hnext_steps.setVisibility(View.VISIBLE);
                hsuccess.setVisibility(View.GONE);
            }
        });
        hlast_steps.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                hlast_step_layout.setVisibility(View.VISIBLE);
                next_layout.setVisibility(View.GONE);
                end_layout.setVisibility(View.GONE);

                hlast_step.setVisibility(View.GONE);
                hlast_steps.setVisibility(View.GONE);
                hnext_step.setVisibility(View.VISIBLE);
                hnext_steps.setVisibility(View.GONE);
                hsuccess.setVisibility(View.GONE);
            }
        });
        checkbox_wuliu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    chox = arg1;
                } else {
                    chox = arg1;
                }
            }
        });
        checkbox_wuliu2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    chox2 = arg1;
                } else {
                    chox2 = arg1;
                }
            }
        });
        final String[] myItems = getResources().getStringArray(
                R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, myItems);
        adapter.setDropDownViewResource(R.layout.dor_wu);
        //第四步：将适配器添加到下拉列表上    
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 1) {
                    weith = true;
//        			ToastUtil.shortToast(LogisticalActivity.this, "开 "+weith);
                } else {
                    weith = false;
//            	  ToastUtil.shortToast(LogisticalActivity.this, " guan  "+weith);

                }
//        		editText1.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] myItems1 = getResources().getStringArray(
                R.array.quhuo);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, myItems1);
        adapter1.setDropDownViewResource(R.layout.dor_wu);
        et_carchang.setAdapter(adapter1);
        et_carchang.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
//				kes=position;
                if (position == 1) {
                    chox = true;
//        			ToastUtil.shortToast(getActivity(), "开 "+chox);
                } else {
                    chox = false;
//            	  ToastUtil.shortToast(getActivity(), " guan  "+chox);
                }
//				Log.e("11111", "position"+sunmit(5, position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        houaddse.setVisibility(View.VISIBLE);
        final String[] myItems2 = getResources().getStringArray(
                R.array.songhuo);
        final ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, myItems2);
        adapter11.setDropDownViewResource(R.layout.dor_wu);
        et_tijisp.setAdapter(adapter11);
        et_tijisp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 1) {
                    chox2 = true;
//					ToastUtil.shortToast(getActivity(), "开 "+chox2);
                    houaddse.setVisibility(View.GONE);
                } else {
                    chox2 = false;
                    houaddse.setVisibility(View.VISIBLE);
//            		ToastUtil.shortToast(getActivity(), "false "+chox2);
                }
//				kes2=position;
//				kese=adapter1.getItem(position);
//				ToastUtil.shortToast(getActivity(), "position"+adapter1.getItem(position));
//				Log.e("11111", "position"+sunmit(5, position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @OnClick({R.id.et_time, R.id.hsuccess, R.id.et_address, R.id.iv_senderList, R.id.tv_modifySenderAddress, R.id.iv_receiverList,
            R.id.tv_modifyRecieverAddress})
    public void MyOnClick(View view) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.et_time:// 获取日期
                pvTime.show();
                break;
            case R.id.hsuccess:

                addPostResult();
//		ceshi();
                break;
//	case R.id.iv_selectSender:// 从寄件人列表中选择寄件人
//		startActivityForResult(new Intent(PostLimitedDownwindTaskActivity.this, SendPersonActivity.class), 5);
//		break;
//	case R.id.iv_selectReciever:// 从收件人列表中选择收件人
//		startActivityForResult(new Intent(PostLimitedDownwindTaskActivity.this, AddReceiverActivity.class), 6);
//		break;
            case R.id.et_address:// 获取寄件人定位的地点，或者修改
                intent = new Intent(getActivity(), AddressActivity.class);
//		startActivityForResult(intent, 7);
                break;
            case R.id.et_add_address:// 获取收件人定位的地点，或者修改
                intent = new Intent(getActivity(), AddressActivity.class);
//		startActivityForResult(intent, 8);
                break;
            case R.id.tv_modifySenderAddress:// 获取寄件人定位的地点，或者修改
                Log.e("et_address-02", et_address + "-----");
//		intent = new Intent(getActivity(), AddressActivity.class);
//		bundle.putString("address", address);
//		intent.putExtras(bundle);
                intent = new Intent(getActivity(), LocationDemo.class);
                startActivityForResult(intent, 7);
                break;
            case R.id.tv_modifyRecieverAddress:// 获取收件人定位的地点，或者修改
//		intent = new Intent(getActivity(), AddressReceiveActivity.class);
                intent = new Intent(getActivity(), LocationDemo.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 8);
                break;
            case R.id.iv_senderList:// 从手机通讯录里选取寄件人
//		intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//		startActivityForResult(intent, 9);
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent2, 9);
                break;
            case R.id.iv_receiverList:// 从手机通讯录里选取收件人
//		intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//		startActivityForResult(intent, 10);
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent3, 10);
                break;
            default:
                break;
        }
    }

    //选择时间
    public static String getTimes(Date date) {
//			SimpleDateFormat format = new SimpleDateFormat("MM-dd ");
//			SimpleDateFormat formats = new SimpleDateFormat("yyyy");
//			return "2017-"+format.format(date);
//			return format.format(date);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");//日期格式
        String hehe = dateFormat.format(date);
        return dateFormat.format(date);
    }

    private void addPostResult() {
        JSONObject obj = new JSONObject();
        String string = et_tel.getText().toString();
        String string2 = et_add_tel.getText().toString();
        String tmpstr = string.replace(" ", "");
        String tmpstr2 = string2.replace(" ", "");
        if (!StringUtil.isMobileNO(tmpstr)
                || (tmpstr.length() != 11)
                ) {
            ToastUtil.shortToast(getActivity(), "请输入正确发件人的手机号码");
            return;
        }
        if (!StringUtil.isMobileNO(tmpstr2)
                || (tmpstr2.length() != 11)
                ) {
            ToastUtil.shortToast(getActivity(), "请输入正确收件人的手机号码");
            return;
        }
        if (et_name.getText().toString().equals("")
                || et_address.getText().toString().equals("")
                ) {
            ToastUtil.shortToast(getActivity(), "请查看发货人信息是否完整");
            return;
        }
        if (et_add_address.getText().toString().equals("") || et_volume.getText().toString().equals("")
                || et_time.getText().toString().equals("") || et_add_tel.getText().toString().equals("")
                || et_add_name.getText().toString().equals("") || et_add_address.getText().toString().equals("")
                || et_goodname.getText().toString().equals("")) {
            ToastUtil.shortToast(getActivity(), "请查看信息是否完整");
            return;
        }

        try {
            obj.put("userId",
                    String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
            if (!iscode) {
                obj.put("latitude", latitude);
                obj.put("longitude", longitude);
                obj.put("startPlaceCityCode", cityCode3);
                obj.put("startPlaceTownCode", townCode3);
            } else {
                obj.put("latitude", mylatitude);
                obj.put("longitude", mylongitude);
                obj.put("startPlaceCityCode", cityCode2);
                obj.put("startPlaceTownCode", townCode2);
            }
            obj.put("startPlace", et_address.getText().toString() + et_address_specific.getText().toString());//货物起始地点  lo_chufa.getText().toString()
            obj.put("sendPerson", et_name.getText().toString());
//				obj.put("sendPhone", et_tel.getText().toString());
            obj.put("sendPhone", tmpstr);
            obj.put("cargoName", et_goodname.getText().toString());//货物名称  l
            obj.put("entPlace", et_add_address.getText().toString() + et_add_address_specific.getText().toString());//货物到达地点   lo_daoda.getText().toString()
            obj.put("takeCargo", chox);//是否需要取货  up
            obj.put("sendCargo", chox2);//是否需要送货  down
            obj.put("latitudeTo", receiverLatitude);
            obj.put("longitudeTo", receiverLongitude);
            obj.put("entPlaceCityCode", receiver_citycode);//
            if (weith == true) {
                obj.put("cargoWeight", et_weight.getText().toString() + "千克");//货物
            } else {
                obj.put("cargoWeight", et_weight.getText().toString() + "吨");//货物重量
            }
            obj.put("cargoVolume", et_volume.getText().toString());//货物体积
            obj.put("arriveTime", et_time.getText().toString());//到达时间
            obj.put("takeName", et_add_name.getText().toString());//收货人姓名
//				obj.put("takeMobile", et_add_tel.getText().toString());//收货电话 
            obj.put("takeMobile", tmpstr2);//收货电话
            obj.put("endPlaceName", endPlaceName);//
            obj.put("cargoNumber", etjian.getText().toString());//
            obj.put("appontSpace", et_huodizhi.getText().toString());//


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("查看数据", obj.toString());
        dialog.show();
        //PublishNew    LOGISTICS
        AsyncHttpUtils.doPostJson(getActivity(), MCUrl.PublishNew, obj.toString(),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Log.e("11111111111 wwww   ", new String(arg2));
                        dialog.dismiss();
                        LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//							Log.e("oppop", bean.data.toString());
                        if (bean.getErrCode() == 0) {
                            ToastUtil.longToast(getActivity(), bean.getMessage());
//								Intent intent = new Intent(LogisticalActivity.this, LogisPayActivity.class);
//							 	   intent.putExtra("evaluationStatus",bean.getData().get(0).evaluationStatus);//余额支付 的价格	
////							 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
//							 		intent.putExtra("evaluationScore", bean.getData().get(0).evaluationScore);//其它支付的价格
//							 		intent.putExtra("billCode", bean.getData().get(0).billCode);//物流单号
//							 		intent.putExtra("way", "1");
//							 		startActivityForResult(intent, 10);
                            remo();
//								
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

    private void remo() {
        hlast_step_layout.setVisibility(View.VISIBLE);
        next_layout.setVisibility(View.GONE);
        end_layout.setVisibility(View.GONE);

        hlast_step.setVisibility(View.GONE);
        hlast_steps.setVisibility(View.GONE);
        hnext_step.setVisibility(View.VISIBLE);
        hnext_steps.setVisibility(View.GONE);
        hsuccess.setVisibility(View.GONE);

        etjian.setText("");
        et_weight.setText("");
        et_volume.setText("");
        et_goodname.setText("");
        et_add_address_specific.setText("");
        et_address_specific.setText("");
        et_add_address.setText("");
        et_add_tel.setText("");
        et_add_name.setText("");
        et_time.setText("");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("11111s", "requestCode" + requestCode + "resultCode" + resultCode + "data" + data);
        switch (requestCode) {
            case 0:// 增加手机寄件人联系人的回调
                if (data == null) {
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    ContentResolver reContentResolverol = getActivity().getContentResolver();
                    Uri contactData = data.getData();
                    // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
                    Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    namesb = true;
                    if (cursor.moveToFirst()) {

                        // 获得DATA表中的名字
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // 条件为联系人ID
                        et_name.setText(name);
                        namese = name;
//			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//			Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//			 if (phone==null) {
//					return;
//				   }
//			while (phone.moveToNext()) {
//				String phone_number = phone
//						.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				if (!phone_number.equals("")){
//					et_tel.setText(phone_number);
//					phonese=phone_number;
//				}
//			}
                        String[] contact = new String[2];
                        // 查看联系人有多少个号码，如果没有号码，返回0
                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        String[] strs = cursor.getColumnNames();
                        for (int i = 0; i < strs.length; i++) {
                            if (strs[i].equals("data1")) {
                                ///手机号
                                contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86", "").replaceAll(" ", "").trim();
                                et_tel.setText("" + contact[1]);
                                phonese = contact[1];
                            }
                        }

                        if (Build.VERSION.SDK_INT < 14) {
                            cursor.close();
                        }
//			phone.close();
                    } else {
                        // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
                        new AlertDialog.Builder(getActivity())
                                .setMessage("app需要开启读取联系人权限")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();

                    }
//		 cursor.close();
                }

                break;
            case 7:// 选择/修改寄件人的寄件地址
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
//			Log.e("111111sss", data.getStringExtra("address").replace("中国", "")+"-----");
                        mylatitude = data.getDoubleExtra("latitude", 0);
                        mylongitude = data.getDoubleExtra("longitude", 0);
//			  city = data.getStringExtra("city");
//			    getCityCode(true, city);
//			  et_address.setText(data.getStringExtra("address").replace("中国", ""));
//			  Log.e("et_address-03", et_address+"-----");
                        return_address = data.getStringExtra("address").replace("中国", "");
                        et_address.setText(return_address);
                        city2 = data.getStringExtra("city");
                        townaddressd2 = data.getStringExtra("townaddressd");
//				Log.e("11111district", data.getStringExtra("district"));
                        getCityCode(true, city2, 2);
//				getCityCode(true, data.getStringExtra("district"));
                        frist = true;
                        return;
                    }
                }
                Log.e("et_address-04", et_address + "-----");
                break;
            case 8:// 修改收件人的收货地址
                if (data == null) {
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    receiverLatitude = data.getDoubleExtra("latitude", 0);
                    receiverLongitude = data.getDoubleExtra("longitude", 0);
                    city = data.getStringExtra("city");
                    Log.e("1111122address", data.getStringExtra("address"));
                    endPlaceName = data.getStringExtra("city") + data.getStringExtra("townaddressd");
//			ToastUtil.shortToast(getActivity(), endPlaceName);
                    receive = true;
//			getCityCode();
                    getCityCode(false, city, 2);
                    et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
                }
                break;
            case 9:// 从本地通讯录选取寄件人
                if (data == null) {
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    ContentResolver reContentResolverol = getActivity().getContentResolver();
                    Uri contactData = data.getData();
                    // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
                    Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
//			cursor.moveToFirst();
                    namesb = true;
                    if (cursor.moveToFirst()) {

                        // 获得DATA表中的名字
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // 条件为联系人ID
                        et_name.setText(name);
                        namese = name;
//			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//			Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//			while (phone.moveToNext()) {
//				String phone_number = phone
//						.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
//				if (!phone_number.equals("")){
//					et_tel.setText(phone_number);
//					phonese=phone_number;
//				}
//			}
                        String[] contact = new String[2];
                        // 查看联系人有多少个号码，如果没有号码，返回0
                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        String[] strs = cursor.getColumnNames();
                        for (int i = 0; i < strs.length; i++) {
                            if (strs[i].equals("data1")) {
                                ///手机号
                                contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86", "").replaceAll(" ", "").trim();
//	                    et_add_tel.setText(""+contact[1]);
                                et_tel.setText("" + contact[1]);
                                phonese = contact[1];
                            }
                        }

                        if (Build.VERSION.SDK_INT < 14) {
                            cursor.close();
                        }
//			phone.close();
                    } else {
                        // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
                        new AlertDialog.Builder(getActivity())
                                .setMessage("app需要开启读取联系人权限")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();

                    }
//		 cursor.close();

                }

                break;
            case 10:// 从本地通讯录选取收件人
                if (data == null) {
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    ContentResolver reContentResolverol = getActivity().getContentResolver();
                    Uri contactData = data.getData();
                    // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
                    Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
//			cursor.moveToFirst();
                    if (cursor.moveToFirst()) {
                        // 获得DATA表中的名字
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // 条件为联系人ID
                        et_add_name.setText(name);
//			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//			Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//			while (phone.moveToNext()) {
//				String phone_number = phone
//						.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
//				if (!phone_number.equals("")){
//					et_add_tel.setText(phone_number);
//				}
//			}
                        String[] contact = new String[2];
                        // 查看联系人有多少个号码，如果没有号码，返回0
                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        String[] strs = cursor.getColumnNames();
                        for (int i = 0; i < strs.length; i++) {
                            if (strs[i].equals("data1")) {
                                ///手机号
                                contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86", "").replaceAll(" ", "").trim();
                                et_add_tel.setText("" + contact[1]);
                            }
                        }

                        if (Build.VERSION.SDK_INT < 14) {
                            cursor.close();
                        }
//			phone.close();
                    } else {
                        // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
                        new AlertDialog.Builder(getActivity())
                                .setMessage("app需要开启读取联系人权限")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();

                    }
//			 cursor.close();
                }

                break;
            case 11:
//		remo();
                break;
        }
    }

    private void initLocation() {
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

    /**
     * 获取城市代码
     *
     * @param flag
     * @param city
     */
    private void getCityCode(boolean flag, String city, int i) {
        boolean isCopySuccess = CheckDbUtils.checkDb();
        // 成功的将数据库copy到data 中
        if (isCopySuccess) {
            iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
        }
        if (city == null || city.equals("")) {
//		ToastUtil.shortToast(getActivity(), "请输入完整信息");
            return;
        }
        Log.e("city", city);
        if (!city.contains("市")) {
            city = city + "市";
        }// area  area_name  取县的code
//	List<AreaBean> selectDataFromDb = new AreaDboperation()
//			.selectDataFromDb("select * from area where area_name='" + city + "'");
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + city + "'");

        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {

            if (i == 2) {
                if (flag) {
                    iscode = true;
                    cityCode2 = selectDataFromDb.get(0).city_code;
//			Log.e("11111city_codee", selectDataFromDb.get(0).city_code);
                    List<AreaBean> selectDataFromDbs = new AreaDboperation()
                            .selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
                    if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                        townCode2 = selectDataFromDbs.get(0).area_code;
//				Log.e("11111ptownCodee", townCode2);
                    }
                } else {
//			ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code23s "+selectDataFromDb.get(0).area_code);
                    receiver_citycode = selectDataFromDb.get(0).city_code;
                    Log.e("11111receiver_citycode", receiver_citycode);
                }

            }
        }
    }

    /**
     * 获取城市代码
     *
     * @param flag
     * @param city
     */
    private void getCityCode2(boolean flag, String city) {
        boolean isCopySuccess = CheckDbUtils.checkDb();
        // 成功的将数据库copy到data 中
        if (isCopySuccess) {
            iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
        }
        if (city == null || city.equals("")) {
//		ToastUtil.shortToast(getContext(), "请输入完整信息");
            return;
        }
        Log.e("city", city);
        if (!city.contains("市")) {
            city = city + "市";
        }// area  area_name  取县的code
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + city + "'");

        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
//		ToastUtil.longToast(PostLimitedDownwindTaskActivity.this, "11111area_name  "+selectDataFromDb.get(0).area_code);
            if (flag) {
//				iscode=false;

                cityCode3 = selectDataFromDb.get(0).city_code;
                Log.e("111112city_codee", cityCode3);
                List<AreaBean> selectDataFromDbs = new AreaDboperation()
                        .selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
                if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                    townCode3 = selectDataFromDbs.get(0).area_code;

                    Log.e("1111111townCode", townCode3);
                }
            }

        }
    }
}
