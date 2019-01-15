package com.hex.express.iwant.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.OrcModel;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.FileUtil;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 实名认证界面
 *
 * @author huyichuan
 */
public class PrefectActivity extends BaseActivity {

    private String userName;
    private String IDENTIFY_CODE = "0";
    private String smsCode;
    private TitleBarView tbv_show;
    private RoundCornerImageView img_headportrait;
    private TextView btn_complete;
    private Bitmap head;
    private String fileName = path + "head.png";
    private static String path = "/sdcard/myHead/";// sd路径
    private Map<String,String> map = new HashMap<>();
    private Map<String, File> map_file = new HashMap<>();
    private String result;
    private String icon = "";
    private boolean flag;
    @Bind(R.id.et_card)
    EditText et_card;
    @Bind(R.id.et_name)
    EditText et_name;
    private boolean frist = false;
    private IconBean bean;
    @Bind(R.id.pre_smallMinibus)
    LinearLayout pre_smallMinibus;
    @Bind(R.id.pre_middleMinibus)
    LinearLayout pre_middleMinibus;
    @Bind(R.id.pre_smallTruck)
    LinearLayout pre_smallTruck;
    @Bind(R.id.pre_middleTruck)
    LinearLayout pre_middleTruck;
    @Bind(R.id.pre_car)
    LinearLayout pre_car;
    @Bind(R.id.pre_other)
    LinearLayout pre_other;
    @Bind(R.id.lin_1)
    LinearLayout lin_1;
    @Bind(R.id.lin_2)
    LinearLayout lin_2;
    @Bind(R.id.iv_xiaomian)
    ImageView iv_xiaomian;
    @Bind(R.id.iv_xiaomian2)
    ImageView iv_xiaomian2;
    @Bind(R.id.iv_xiaomian3)
    ImageView iv_xiaomian3;
    @Bind(R.id.iv_xiaomian4)
    ImageView iv_xiaomian4;
    @Bind(R.id.iv_xiaomian5)
    ImageView iv_xiaomian5;
    @Bind(R.id.iv_xiaomian6)
    ImageView iv_xiaomian6;
    @Bind(R.id.pre_tishi)
    TextView pre_tishi;
    String type = "0";
    private static final int REQUEST_CODE_CAMERA = 102;
    private String imgBase46 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefect);
        ButterKnife.bind(this);
        IDENTIFY_CODE = getIntent().getStringExtra("IDENTIFY_CODE");
        initView();
        initData();
        setOnClick();
        initAccessTokenWithAkSk();
    }

    private Options getBitmapOption(int inSampleSize) {
        System.gc();
        Options options = new Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }


    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(
                new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {

                        // 本地自动识别需要初始化
                        initLicense();

                        Log.d("MainActivity", "onResult: " + result.toString());
//                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "初始化认证成功", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onError(OCRError error) {
                        error.printStackTrace();
                        Log.e("MainActivity", "onError: " + error.getMessage());
//                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "初始化认证失败,请检查 key", Toast.LENGTH_SHORT).show());
                    }
                }, getApplicationContext(),
                "Ot8BzGBPOrAGSBnZKVwef41G",
                "MKK2Ce9uzy2GGBmurNzx4Yp8CrBNHuAx");
    }

    private void initLicense() {
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                (errorCode, e) -> {
                    final String msg;
                    switch (errorCode) {
                        case CameraView.NATIVE_SOLOAD_FAIL:
                            msg = "加载so失败，请确保apk中存在ui部分的so";
                            break;
                        case CameraView.NATIVE_AUTH_FAIL:
                            msg = "授权本地质量控制token获取失败";
                            break;
                        case CameraView.NATIVE_INIT_FAIL:
                            msg = "本地质量控制";
                            break;
                        default:
                            msg = String.valueOf(errorCode);
                    }
//                    runOnUiThread(() -> Toast.makeText(MainActivity.this,
//                            "本地质量控制初始化错误，错误原因： " + msg, Toast.LENGTH_SHORT).show());
                });
    }

    // 身份证正面
    private void scanIdCardFront() {
        Intent intent = new Intent(PrefectActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    OrcModel orcModel = new OrcModel("00");
                    orcModel.setImageData(filePath);
                    if (IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)) {
                        orcModel.setType(1);
                        String name = "";
                        String sex = "";
                        String nation = "";
                        String num = "";
                        String address = "";
                        if (result.getName() != null) {
                            name = result.getName().toString();
                            orcModel.setName(name);
                        }
                        if (result.getGender() != null) {
                            sex = result.getGender().toString();
                            orcModel.setGender(sex);
                        }
                        if (result.getEthnic() != null) {
                            nation = result.getEthnic().toString();
                            orcModel.setNation(nation);
                        }
                        if (result.getIdNumber() != null) {
                            num = result.getIdNumber().toString();
                            orcModel.setCode(num);
                        }
                        if (result.getAddress() != null) {
                            address = result.getAddress().toString();
                            orcModel.setAddress(address);
                        }

//                        param.getImageFile().getAbsolutePath()
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
                        head = BitmapFactory.decodeFile(filePath);
                        /**
                         * 上传服务器代码 //TODO 实现头衔上传
                         */
                        setPicToView(head);
                        map.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "");
                        map.put("fileName", "蒙娜丽莎");
                        map_file.put("file", new File(fileName));
                        sendEmptyBackgroundMessage(MsgConstants.MSG_01);
                        img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
                        img_headportrait.setImageBitmap(head);

                        et_name.setText(orcModel.getName());
                        et_card.setText(orcModel.getCode());

                        Log.i("PrefectActivity","姓名: " + name + "\n" +
                                "性别: " + sex + "\n" +
                                "民族: " + nation + "\n" +
                                "身份证号码: " + num + "\n" +
                                "住址: " + address + "\n");
                    } else if (IDCardParams.ID_CARD_SIDE_BACK.equals(idCardSide)) {
                        orcModel.setType(2);
                        if (null != result.getIssueAuthority()) {
                            orcModel.setIssue(result.getIssueAuthority() + "");
                        }
                        if (null != result.getSignDate() && null != result.getExpiryDate()) {
                            orcModel.setValid(result.getSignDate() + "-" + result.getExpiryDate());
                        }
                        Log.i("PrefectActivity","签发机关: " + result.getIssueAuthority() + "\n" +
                                "签发日期：" + result.getSignDate() + "\n" +
                                "有效期：" + result.getExpiryDate());
                    }
                    Log.i("PrefectActivity","orcModel = " + orcModel.toString());

                }else {
                    ToastUtil.longToast(PrefectActivity.this, "身份证识别失败，请重新操作");
                }
            }

            @Override
            public void onError(OCRError error) {
                Log.d("PrefectActivity", "onError: " + error.getMessage());
                ToastUtil.longToast(PrefectActivity.this, "身份证识别失败，请重新操作");
            }
        });
    }
    /**
     * 解析银行卡
     *
     * @param filePath 图片路径
     */
    private void recCreditCard(String filePath) {
        // 银行卡识别参数设置
        BankCardParams param = new BankCardParams();
        param.setImageFile(new File(filePath));

        // 调用银行卡识别服务
        OCR.getInstance(this).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult result) {
                if (result != null) {
                    OrcModel orcModel = new OrcModel("00");
                    String type = "";
                    if (result.getBankCardType() == BankCardResult.BankCardType.Credit) {
                        type = "信用卡";
                    } else if (result.getBankCardType() == BankCardResult.BankCardType.Debit) {
                        type = "借记卡";
                    } else {
                        type = "未识别";
                    }
                    orcModel.setBankType(type);
                    if (null != result.getBankCardNumber()) {
                        orcModel.setBankNumber(result.getBankCardNumber());
                    }
                    if (null != result.getBankName()) {
                        orcModel.setBankName(result.getBankName());
                    }
                    if (null != imgBase46) {
                        orcModel.setImageData(imgBase46);
                    }
                    Log.i("PrefectActivity","银行卡号: " + (!TextUtils.isEmpty(result.getBankCardNumber()) ? result.getBankCardNumber() : "") + "\n" +
                            "银行名称: " + (!TextUtils.isEmpty(result.getBankName()) ? result.getBankName() : "") + "\n" +
                            "银行类型: " + type + "\n");
                    Log.i("PrefectActivity","orcModel = " + orcModel.toString());
                }
            }

            @Override
            public void onError(OCRError error) {
                Log.d("MainActivity", "onError: " + error.getMessage());
                ToastUtil.longToast(PrefectActivity.this, "身份证识别失败，请重新操作");
            }
        });
    }
    // 点击事件;
    @Override
    public void onWeightClick(View v) {
        switch (v.getId()) {
//		case R.id.btn_complete:// 下一步
//			addCourierPost(MCUrl.CHECKADDCHECKINFO);
//			break;
            case R.id.img_headportrait:// 上传头像
                scanIdCardFront();// 显示pupwindown
                break;
        }
    }

    /**
     * 快递员实名认证
     */
    public void addCourierPost(String url) {
        // TODO 判断头像跟昵称是否为空 为空就不跳转 并提醒
        String idcard = et_card.getText().toString();
        if (frist) {
            if (!et_name.getText().toString().equals("") && !et_name.getText().toString().equals("null")) {
                if (IDUtils.IDCardValidate(idcard.toUpperCase()).equals("true")) {
                    int id = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID);
                    JSONObject obj = new JSONObject();
                    try {
                        Log.e("icon", icon);
                        obj.put("userId", id);
                        obj.put("checkName", et_name.getText().toString());
                        obj.put("checkIdCard", et_card.getText().toString());
                        obj.put("chenkPath", icon);
                        obj.put("checkType", IDENTIFY_CODE);
                        if (IDENTIFY_CODE.equals("1")) {
                            obj.put("carType", "courier");
                        } else {
                            obj.put("carType", type);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    btn_complete.setClickable(false);
                    dialog.show();
                    Log.e("MSG", obj.toString());
                    AsyncHttpUtils.doPostJson(PrefectActivity.this, MCUrl.CHECKADDCHECKINFO, obj.toString(),
                            new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                    dialog.dismiss();
                                    btn_complete.setClickable(true);
                                    Log.e("msgkllnj", new String(arg2));
                                    RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
                                    if (bean.getErrCode() == 0) {
                                        ToastUtil.longToast(PrefectActivity.this, bean.getMessage());
                                        ;
                                        if (IDENTIFY_CODE.equals("1")) {
                                            startActivity(new Intent(PrefectActivity.this,
                                                    CourierIdentificationActivity.class));
                                        } else {
//											Intent intent = new Intent(PrefectActivity.this, MainTab.class);
                                            Intent intent = new Intent(PrefectActivity.this, NewMainActivity.class);
                                            intent.putExtra("type", "1");
                                            startActivity(intent);
                                            finish();
                                        }


                                    } else {
                                        ToastUtil.shortToast(PrefectActivity.this, bean.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                    fileDelete(path + "head.png");
                                    dialog.dismiss();
                                    btn_complete.setClickable(true);
                                }
                            });
                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请输入正确的身份证号");
                    btn_complete.setClickable(true);
                }

            } else {
                ToastUtil.shortToast(getApplicationContext(), "请输入真实姓名");
                btn_complete.setClickable(true);
            }
        } else {
            ToastUtil.shortToast(getApplicationContext(), "请上传身份证照图");
            btn_complete.setClickable(true);
        }
    }

    @Override
    public void initView() {
        // 标题
        tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
        // 头像
        img_headportrait = (RoundCornerImageView) findViewById(R.id.img_headportrait);
        // 完成按钮
        btn_complete = (TextView) findViewById(R.id.btn_complete);

        pre_smallTruck.setOnClickListener(this);
        pre_middleTruck.setOnClickListener(this);
        pre_smallMinibus.setOnClickListener(this);
        pre_middleMinibus.setOnClickListener(this);
        pre_car.setOnClickListener(this);
        pre_other.setOnClickListener(this);

        if (IDENTIFY_CODE.equals("1")) {
            lin_1.setVisibility(View.GONE);
            lin_2.setVisibility(View.GONE);
        } else {
            lin_1.setVisibility(View.VISIBLE);
            lin_2.setVisibility(View.VISIBLE);
        }

    }

    // 点击事件
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pre_smallMinibus:
                type = "smallMinibus";
                iv_xiaomian.setBackgroundResource(R.drawable.xuanzhongon);
                iv_xiaomian2.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian3.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian4.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian5.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian6.setBackgroundResource(R.drawable.xuanzhongoff);
//				pre_tishi.setText("请上传驾驶证");
                break;
            case R.id.pre_middleMinibus:
                type = "middleMinibus";
                iv_xiaomian.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian2.setBackgroundResource(R.drawable.xuanzhongon);
                iv_xiaomian3.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian4.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian5.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian6.setBackgroundResource(R.drawable.xuanzhongoff);
//				pre_tishi.setText("请上传驾驶证");
                break;
            case R.id.pre_smallTruck:
                type = "smallTruck";
                iv_xiaomian.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian2.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian3.setBackgroundResource(R.drawable.xuanzhongon);
                iv_xiaomian4.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian5.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian6.setBackgroundResource(R.drawable.xuanzhongoff);
//				pre_tishi.setText("请上传驾驶证");
                break;
            case R.id.pre_middleTruck:
                type = "middleTruck";
                iv_xiaomian.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian2.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian3.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian4.setBackgroundResource(R.drawable.xuanzhongon);
                iv_xiaomian5.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian6.setBackgroundResource(R.drawable.xuanzhongoff);
//				pre_tishi.setText("请上传驾驶证");
                break;
            case R.id.pre_car:
                type = "car";
                iv_xiaomian.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian2.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian3.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian4.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian5.setBackgroundResource(R.drawable.xuanzhongon);
                iv_xiaomian6.setBackgroundResource(R.drawable.xuanzhongoff);
//				pre_tishi.setText("请上传驾驶证");
                break;
            case R.id.pre_other:
                type = "other";
                iv_xiaomian.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian2.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian3.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian4.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian5.setBackgroundResource(R.drawable.xuanzhongoff);
                iv_xiaomian6.setBackgroundResource(R.drawable.xuanzhongon);
//                pre_tishi.setText("上传身份证或者驾照");
                pre_tishi.setText("上传身份证");

                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {

        if (IDENTIFY_CODE.equals("1")) {
            this.tbv_show.setTitleText("快递员认证");
            btn_complete.setText("下一步");
        } else if (IDENTIFY_CODE.equals("2")) {
            btn_complete.setText("完成");
            this.tbv_show.setTitleText("镖师认证");
        }

        if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME) != null
                && !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME).equals("")) {
            et_name.setText(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME));
        }
        if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD) != null
                && !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD).equals("")) {
            et_card.setText(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD));
        }

        if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH) != null
                && !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH).equals("")) {
//			loader.displayImage(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.HEARDER_PATH), img_headportrait,options);
            loader.displayImage(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH), img_headportrait);
//			Log.e("1111     ", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH));
            img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时
        }
    }

    @Override
    public void setOnClick() {
        img_headportrait.setOnClickListener(this);
        btn_complete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub  submitButton.setClickable(false);
                if (IDENTIFY_CODE.equals("1")) {
                    addCourierPost(MCUrl.CHECKADDCHECKINFO);
                } else {
                    if (type.equals("0")) {
                        ToastUtil.shortToast(PrefectActivity.this, "请选择车型");
                    } else {
                        addCourierPost(MCUrl.CHECKADDCHECKINFO);
                    }
                }
            }
        });

    }

    @Override
    public void getData() {
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        @SuppressLint("ResourceAsColor")
        ColorDrawable dw = new ColorDrawable(R.color.transparent);
        window.setBackgroundDrawable(dw);
        window.setOutsideTouchable(false);// 这是点击外部不消失
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(PrefectActivity.this.findViewById(R.id.img_headportrait), Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
        tv_show.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(PrefectActivity.this, IdCardActivity.class));
                window.dismiss();
            }
        });
        tv_camera.setClickable(true);
        // 点击是拍照
        tv_camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();
                /**
                 * android 6.0 拍照动态权限动态授予
                 */
                cameraPermissionTest();
            }
        });
        // 从相册弄
        TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
        tv_photofromalbum.setClickable(true);
        tv_photofromalbum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();
                /**
                 * android 6.0 相册权限动态授予
                 */
                storagePermissionTest();
            }
        });
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setClickable(true);
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        // popWindow消失监听方法
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

    }

    /**
     * 检查应用打开相册权限
     */
    private void storagePermissionTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
            } else {
                openPhoto();
            }
        } else {
            openPhoto();
        }
    }

    /**
     * 打开相册
     */
    private void openPhoto() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent1, 1);
    }

    /**
     * 检测应用拍照权限
     */
    private void cameraPermissionTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            } else {
                takePhoto();
            }
        } else {
            takePhoto();
        }
    }

    /**
     * 调用手机相机拍照
     */
    private void takePhoto() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "head.png");
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, "com.hex.express.iwant.fileprovider", file);
            intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent2, 2);// 采用ForResult打开
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] ==
                        PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "拍照上传功能需要授予应用'拍照'和'存储'权限,请在设置中授予",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openPhoto();
                } else {
                    Toast.makeText(this, "相册选择需要授予应用'存储'权限，请在设置中授予", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(contentURI, null, null, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.png");
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(PrefectActivity.this, "com.hex.express.iwant.fileprovider", temp);
                    } else {
                        uri = Uri.fromFile(temp);
                    }
                    cropPhoto(uri);// 裁剪图片
                }
                break;

            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras == null){
                        Uri uri = data.getData();
                        if (uri != null) {

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            head = BitmapFactory.decodeFile(getRealPathFromURI(uri),options);
                        }
                    }else {
                        head = extras.getParcelable("data");
                    }
                    if (head != null) {
                        /**
                         * 上传服务器代码 //TODO 实现头衔上传
                         */
                        setPicToView(head);
                        map.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "");
                        map.put("fileName", "蒙娜丽莎");
                        map_file.put("file", new File(fileName));
                        sendEmptyBackgroundMessage(MsgConstants.MSG_01);
                        img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
                        img_headportrait.setImageBitmap(head);
                    }
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (data != null) {
                    String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                    String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                    Log.d("PrefectActivity","############################## filepath :##############################" + filePath);
                    if (!TextUtils.isEmpty(contentType)) {
//                        try {
//                            imgBase46 = FileUtil.encodeBase64File(filePath);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("PrefectActivity","文件转base64失败");
//                        }
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                        } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                        } else if (CameraActivity.CONTENT_TYPE_BANK_CARD.equals(contentType)) {
                            recCreditCard(filePath);
                        }
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void handleBackgroundMessage(Message msg) {
        // TODO Auto-generated method stub
        super.handleBackgroundMessage(msg);
        switch (msg.what) {
            case MsgConstants.MSG_01:
                try {
                    Log.e("logurl", MCUrl.IDCDHEAD);
                    result = post(MCUrl.UPLOAD_ICON, map, map_file);
                    sendEmptyUiMessage(MsgConstants.MSG_01);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        // TODO Auto-generated method stub
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MsgConstants.MSG_01:
                Log.e("result", result);
                bean = new Gson().fromJson(result, IconBean.class);
                if (bean.getErrCode() == 0) {
                    if (bean.data.size() != 0) {
                        frist = true;
                        icon = bean.data.get(0).filePath;
                        Log.e("filePath", bean.data.get(0).filePath);
                        setPicToView(head);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
//				img_headportrait.setImageBitmap(head);
                    }
                } else {
                    ToastUtil.shortToast(PrefectActivity.this, bean.getMessage());
                }
                break;

            default:
                break;
        }
    }

    /***
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1.5);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);

    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param actionUrl 访问的服务器URL
     * @param params    普通参数
     * @param files     文件参数
     * @return
     * @throws IOException
     */
    public static String post(String actionUrl, Map<String, String> params, Map<String, File> files)
            throws IOException {

        StringBuilder sb2 = new StringBuilder();

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        InputStream in = null;
        // 发送文件数据
        if (files != null) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                // name是post中传参的键 filename是文件的名称
                sb1.append(
                        "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                in = conn.getInputStream();
                int ch;

                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
            }
            outStream.close();
            conn.disconnect();
        }
        return sb2.toString();
    }

    /* 显示Dialog的method */
    private void showDialog(String mess) {
    }

    public InputStream getStream(File file) {
        // 第2步、通过子类实例化父类对象
        // File f= new File("d:" + File.separator + "test.txt") ; // 声明File对象
        // 第2步、通过子类实例化父类对象
        InputStream input = null; // 准备好一个输入的对象
        try {
            input = new FileInputStream(file); // 通过对象多态性，进行实例化
            // 第3步、进行读操作
            // byte b[] = new byte[input..available()] ; 跟使用下面的代码是一样的
            byte b[] = new byte[(int) file.length()]; // 数组大小由文件决定
            int len = input.read(b); // 读取内容
            // 第4步、关闭输出流

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return input;
    }

    private boolean fileIsExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private void fileDelete(String path) {

        if (fileIsExist(path)) {
            File file = new File(path);
            file.delete();
        }
    }

    // 点击空白区域关闭输入法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(PrefectActivity.this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
