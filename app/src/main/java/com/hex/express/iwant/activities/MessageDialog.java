package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageDialog extends Dialog {
	    private Button positiveButton, negativeButton;
	    private TextView title;
		private TextView message;
		private LinearLayout content;
	 
	    public MessageDialog(Context context) {
	        super(context,R.style.dialog_tran);
	        setCustomDialog();
	    }
	 
	    private void setCustomDialog() {
	        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_message, null);
	        title = (TextView) mView.findViewById(R.id.title);
	        message = (TextView) mView.findViewById(R.id.message);
	        content = (LinearLayout) mView.findViewById(R.id.content);
	        positiveButton = (Button) mView.findViewById(R.id.positiveButton);
	        negativeButton = (Button) mView.findViewById(R.id.negativeButton);
	        super.setContentView(mView);
	    }
	     
	    public View getTextView(){
	        return message;
	    }
	    public View getTitle(){
	    	return title;
	    }
	    public View getLinearLayout(){
			return content;
	    }
	    public View getNoButton(){
	    	return negativeButton;
	    }
	    public View getPeButton(){
	    	return positiveButton;
	    }
	     @Override
	    public void setContentView(int layoutResID) {
	    }
	 
	    @Override
	    public void setContentView(View view, LayoutParams params) {
	    }
	 
	    @Override
	    public void setContentView(View view) {
	    }
	 
	    /**
	     * 确定键监听器
	     * @param listener
	     */ 
	    public void setOnPositiveListener(View.OnClickListener listener){ 
	        positiveButton.setOnClickListener(listener); 
	    } 
	    /**
	     * 取消键监听器
	     * @param listener
	     */ 
	    public void setOnNegativeListener(View.OnClickListener listener){ 
	        negativeButton.setOnClickListener(listener); 
	    }
	}