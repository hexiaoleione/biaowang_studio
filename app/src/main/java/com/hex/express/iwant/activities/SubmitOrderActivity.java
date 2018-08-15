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
import android.widget.TextView;
/**
 * 提交订单
 * @author SCHT-40
 *
 */
public class SubmitOrderActivity extends BaseActivity {
	
	private TitleBarView tbv_show;
	private EditText edt_totalmoney;
	private EditText edt_distance;
	private EditText edt_totalweight;
	private TextView txt_sendaddress;
	private TextView txt_recaddress;
	private EditText edt_sendername;
	private EditText edt_sendermobile;
	private EditText edt_recname;
	private EditText edt_recmobile;
	private ImageView imgv_people2;
	private ImageView imgv_people1;
	private EditText edt_submitordermathname;
	private EditText edt_submitorderremarks;
	private EditText edit_submitorderpaymoney;
	private Button btn_submitorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitorder);
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
		case R.id.imgv_people1:
			Intent intent1 = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent1, 1);
			break;
		case R.id.imgv_people2:
			Intent intent2 = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent2, 2);
			break;
		case R.id.btn_submitorder:
			//TODO 实现页面控件的非空判断
//			intent.setClass(SubmitOrderActivity.this, MainActivity.class);
//			intent.setClass(SubmitOrderActivity.this, MainTab.class);
			intent.setClass(SubmitOrderActivity.this, NewMainActivity.class);
			startActivity(intent);
			SubmitOrderActivity.this.finish();
			break;
		}
	}

	@Override
	public void initView() {
		//标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.submitordertitle);
		//总重  总价  距离 
		edt_totalmoney = (EditText) findViewById(R.id.edt_totalmoney);
		edt_distance = (EditText) findViewById(R.id.edt_distance);
		edt_totalweight = (EditText) findViewById(R.id.edt_totalweight);
		//寄件人 收件人地址
		txt_sendaddress = (TextView) findViewById(R.id.txt_sendaddress);
		txt_recaddress = (TextView) findViewById(R.id.txt_recaddress);
		//寄件人 收件人 姓名与联系电话
		edt_sendername = (EditText) findViewById(R.id.edt_sendername);
		edt_sendermobile = (EditText) findViewById(R.id.edt_sendermobile);
		edt_recname = (EditText) findViewById(R.id.edt_recname);
		edt_recmobile = (EditText) findViewById(R.id.edt_recmobile);
		//获取联系人的图标
		imgv_people1 = (ImageView) findViewById(R.id.imgv_people1);
		imgv_people2 = (ImageView) findViewById(R.id.imgv_people2);
		//物品名称与备注  支付钱数
		edt_submitordermathname = (EditText) findViewById(R.id.edt_submitordermathname);
		edt_submitorderremarks = (EditText) findViewById(R.id.edt_submitorderremarks);
		edit_submitorderpaymoney = (EditText) findViewById(R.id.edit_submitorderpaymoney);
		//提交订单
		btn_submitorder = (Button) findViewById(R.id.btn_submitorder);
	}

	@Override
	public void initData() {

	}

	@Override
	public void setOnClick() {
		imgv_people1.setOnClickListener(this);
		imgv_people2.setOnClickListener(this);
		btn_submitorder.setOnClickListener(this);
	}

	@Override
	public void getData() {

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor c = SubmitOrderActivity.this.managedQuery(contactData,
						null, null, null, null);
				if (c != null) {
					c.moveToFirst();
					String phoneNum = this.getContactPhone(c);
					String[] arrayP = phoneNum.split("&");
					if (arrayP.length > 1) {
						edt_sendermobile.setText(arrayP[1]);
						edt_sendername.setText(arrayP[0]);
					}
				}
			}
			break;
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor c = SubmitOrderActivity.this.managedQuery(contactData,
						null, null, null, null);
				if (c != null) {
					c.moveToFirst();
					String phoneNum = this.getContactPhone(c);
					String[] arrayP = phoneNum.split("&");
					if (arrayP.length > 1) {
						edt_recmobile.setText(arrayP[1]);
						edt_recname.setText(arrayP[0]);
					}
				}
			}
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
			Cursor phones = SubmitOrderActivity.this.getContentResolver()
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
