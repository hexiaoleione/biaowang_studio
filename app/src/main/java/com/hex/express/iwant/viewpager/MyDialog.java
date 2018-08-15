package com.hex.express.iwant.viewpager;

import com.hex.express.iwant.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class MyDialog extends DialogFragment implements OnClickListener{    
    private EditText et_title;    
    //自定义接口监听    
    public interface OnInputListener  {      
        void onInputComplete(String title);      
}    
    @Override    
    public Dialog onCreateDialog(Bundle savedInstanceState) {    
        View views=LayoutInflater.from(getActivity()).inflate(R.layout.mainpopwindow_logsitise, null);    
        ImageView iv_go = (ImageView) views.findViewById(R.id.adv_bg);    
//        iv_go.setOnClickListener(this);    
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());    
        AlertDialog show = builder.show();    
        show.getWindow().setContentView(views);//自定义布局    
        show.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//宽高    
        show.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);//位置  setLayout  
        show.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//支持输入法show.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);    
        return show;    
       }    
    
   @Override    
   public void onClick(View v) {    
      switch (v.getId()) {    
       case R.id.adv_bg:    
//          OnInputListener listener = (OnInputListener) getActivity();      
//          listener.onInputComplete(et_title.getText().toString());//传递值给监听接口    
           break;    
        }    
   }    
}  