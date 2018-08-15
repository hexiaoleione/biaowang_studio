package com.hex.express.iwant.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 【自定义的TextView跑马灯效果】
 * @author han
 *
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context con) {
		  super(con);
		}

		public MarqueeTextView(Context context, AttributeSet attrs) {
		  super(context, attrs);
		}
		public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		  super(context, attrs, defStyle);
		}
		@Override
		public boolean isFocused() {
		return true;
		}
		@Override
		protected void onFocusChanged(boolean focused, int direction,
		   Rect previouslyFocusedRect) {  
		}
	
}
