package com.hex.express.iwant.viewpager;

import com.hex.express.iwant.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyPagerFragment extends Fragment {

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	public static final MyPagerFragment newInstance(DialogFragment dialogFragment, String message,
			int item) {
		MyPagerFragment f = new MyPagerFragment();
		Bundle bdl = new Bundle(item);
		f.item = item;
		f.dialogFragment = dialogFragment;
		bdl.putString(EXTRA_MESSAGE, message);
		f.setArguments(bdl);
		return f;
	}

	private int item;
	private TextView mTvMsg;
	private Button mBtnCancle;
	private DialogFragment dialogFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(
				R.layout.pager_fragment, container, false);

		String msg = getArguments().getString(EXTRA_MESSAGE);
		mTvMsg = (TextView) view.findViewById(R.id.tv_msg);
		mTvMsg.setText(msg);
		mBtnCancle = (Button) view.findViewById(R.id.btn_cancle);
		mBtnCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogFragment.dismiss();
			}
		});

		return view;
	}

}
