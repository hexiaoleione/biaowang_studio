package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.views.TitleBarView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 来抢单吧界面
 * 
 * @author Eric
 * 
 */
public class ComeToPickExpActivity extends BaseActivity {

	private ImageView imagv_addpeople1;
	private ImageView imagv_addpeople2;
	private ImageView imgv_people1;
	private ImageView imgv_people2;
	private ImageView imgv_location1;
	private ImageView imgv_location2;
	private Button btn_oksend;
	private EditText edt_cometopicksendername;
	private EditText edt_cometopicksendermobile;
	private EditText edt_senderaddress1;
	private EditText edt_senderaddress2;
	private EditText edt_cometopickaddresseename;
	private EditText edt_cometopickaddresseemobile;
	private EditText edt_addresseeAddress1;
	private EditText edt_addresseeAddress2;
	private TitleBarView tbv_show;
	private EditText edt_mathname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cometopick);
		iWantApplication.getInstance().addActivity(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.imagv_addpeople1:
			intent.setClass(ComeToPickExpActivity.this, AddressActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.imagv_addpeople2:
			intent.setClass(ComeToPickExpActivity.this, AddressActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.imgv_people1:
			Intent intent1 = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent1, 3);
			break;
		case R.id.imgv_people2:
			Intent intent2 = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent2, 4);
			break;
		case R.id.imgv_location1:
			intent.setClass(ComeToPickExpActivity.this, AddressActivity.class);
			startActivityForResult(intent, 5);
			break;
		case R.id.imgv_location2:
			intent.setClass(ComeToPickExpActivity.this, AddressActivity.class);
			startActivityForResult(intent, 6);
			break;
		case R.id.btn_oksend:
			String mathName = edt_mathname.getText().toString().trim();
			//TODO 在这里实现判断是否为空
			intent.setClass(ComeToPickExpActivity.this, NewMainActivity.class);
//			intent.setClass(ComeToPickExpActivity.this, MainTab.class);
//			intent.setClass(ComeToPickExpActivity.this, MainActivity.class);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 初始化UI
	 */
	@Override
	public void initView() {
		//标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.cometopick);
		// 选择地址
		imagv_addpeople1 = (ImageView) findViewById(R.id.imagv_addpeople1);
		imagv_addpeople2 = (ImageView) findViewById(R.id.imagv_addpeople2);
		// 选择联系人
		imgv_people1 = (ImageView) findViewById(R.id.imgv_people1);
		imgv_people2 = (ImageView) findViewById(R.id.imgv_people2);
		// 选择定位地址
		imgv_location1 = (ImageView) findViewById(R.id.imgv_location1);
		imgv_location2 = (ImageView) findViewById(R.id.imgv_location2);
		// 确认发布
		btn_oksend = (Button) findViewById(R.id.btn_oksend);
		// 发件人信息
		edt_cometopicksendername = (EditText) findViewById(R.id.edt_cometopicksendername);
		edt_cometopicksendermobile = (EditText) findViewById(R.id.edt_cometopicksendermobile);
		edt_senderaddress1 = (EditText) findViewById(R.id.edt_senderaddress1);
		edt_senderaddress2 = (EditText) findViewById(R.id.edt_senderaddress2);
		// 收件人信息
		edt_cometopickaddresseename = (EditText) findViewById(R.id.edt_cometopickaddresseename);
		edt_cometopickaddresseemobile = (EditText) findViewById(R.id.edt_cometopickaddresseemobile);
		edt_addresseeAddress1 = (EditText) findViewById(R.id.edt_addresseeAddress1);
		edt_addresseeAddress2 = (EditText) findViewById(R.id.edt_addresseeAddress2);
		//物品信息
		edt_mathname = (EditText) findViewById(R.id.edt_mathname);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {

	}

	/**
	 * 设置监听
	 */
	@Override
	public void setOnClick() {
		// 选择地址
		imagv_addpeople1.setOnClickListener(this);
		imagv_addpeople2.setOnClickListener(this);
		// 选择联系人
		imgv_people1.setOnClickListener(this);
		imgv_people2.setOnClickListener(this);
		// 选择定位地址
		imgv_location1.setOnClickListener(this);
		imgv_location2.setOnClickListener(this);
		// 确认发布
		btn_oksend.setOnClickListener(this);
	}

	@Override
	public void getData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:

			break;
		case 2:

			break;
		case 3:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor c = ComeToPickExpActivity.this.managedQuery(contactData,
						null, null, null, null);
				if (c != null) {
					c.moveToFirst();
					String phoneNum = this.getContactPhone(c);
					String[] arrayP = phoneNum.split("&");
					if (arrayP.length > 1) {
						edt_cometopicksendermobile.setText(arrayP[1]);
						edt_cometopicksendername.setText(arrayP[0]);
					}
				}
			}
			break;
		case 4:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor c = ComeToPickExpActivity.this.managedQuery(contactData,
						null, null, null, null);
				if (c != null) {
					c.moveToFirst();
					String phoneNum = this.getContactPhone(c);
					String[] arrayP = phoneNum.split("&");
					if (arrayP.length > 1) {
						edt_cometopickaddresseemobile.setText(arrayP[1]);
						edt_cometopickaddresseename.setText(arrayP[0]);
					}
				}
			}
			break;
		case 5:

			break;

		default:
			break;
		}
	}

	/**
	 * 获取联系人电话
	 * 
	 * @param cursor
	 * @return
	 */
	private String getContactPhone(Cursor cursor) {

		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		cursor.getString(phoneNum);
		String phoneResult = "";
		// System.out.print(phoneNum);
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int nameIndex = cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			String contactId = cursor.getString(idColumn);
			String name = cursor.getString(nameIndex);
			// 获得联系人的电话号码的cursor;
			Cursor phones = ComeToPickExpActivity.this.getContentResolver()
					.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
			// int phoneCount = phones.getCount();
			// allPhoneNum = new ArrayList<String>(phoneCount);
			if (phones.moveToFirst()) {
				// 遍历所有的电话号码
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);

					switch (phone_type) {
					case 2:
						phoneResult = phoneNumber;
						break;
					}
					// allPhoneNum.add(phoneNumber);
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
			phoneResult = name + "&" + phoneResult;
		}
		return phoneResult;
	}
}
