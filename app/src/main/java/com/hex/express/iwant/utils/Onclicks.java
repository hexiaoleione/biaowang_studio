package com.hex.express.iwant.utils;

import java.util.Calendar;

import android.view.View;
import android.view.View.OnClickListener;

public class Onclicks {

	  public abstract class NoDoubleClickListener implements OnClickListener {

          public static final int MIN_CLICK_DELAY_TIME = 1000;
          private long lastClickTime = 0;

          @Override
          public void onClick(View v) {
              long currentTime = Calendar.getInstance().getTimeInMillis();
              if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                  lastClickTime = currentTime;
//                  onNoDoubleClick(v);
                  onNoDoubleClick(v);
              } 
          }

		private void onNoDoubleClick(View v) {
			// TODO Auto-generated method stub
			
		}   
      }

}
