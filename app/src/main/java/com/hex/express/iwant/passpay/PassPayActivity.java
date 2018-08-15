package com.hex.express.iwant.passpay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PassPayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /************* 第一种用法————开始 ***************/
//        setContentView(R.layout.activity_main);
//
//        final PasswordView pwdView = (PasswordView) findViewById(R.id.pwd_view);
//        
//        //添加密码输入完成的响应
//        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
//            @Override
//            public void inputFinish() {
//            	//输入完成后我们简单显示一下输入的密码
//            	//也就是说——>实现你的交易逻辑什么的在这里写
//                Toast.makeText(MainActivity.this, pwdView.getStrPassword(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        /**
//         *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
//         *  如果写了，会覆盖我们在自定义控件中提供的响应
//         *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
//        pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Biu Biu Biu", Toast.LENGTH_SHORT).show();
//            }
//        });
        /************ 第一种用法————结束 ******************/

        /************* 第二种用法————开始 *****************/
        final PasswordView pwdView = new PasswordView(this);
        setContentView(pwdView);
        
        //6位输入密码完成后 调用：
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                Toast.makeText(PassPayActivity.this, pwdView.getStrPassword(), Toast.LENGTH_SHORT).show();
            }
        });
        /************** 第二种用法————结束 ****************/
    }
}
