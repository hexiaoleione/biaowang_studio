/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *  
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hex.express.iwant.witget;

import java.util.LinkedList;
import java.util.List;

import com.hex.express.iwant.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Numeric wheel view.
 * 
 * @author Yuri Kanivets
 */
public class WheelView extends View {
	/** 
	 * Scrolling duration 
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷(锟斤拷锟斤拷) 
	 */
	private static final int SCROLLING_DURATION = 400;

	/** 
	 * Minimum delta for scrolling 
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷小锟斤拷值 
	 */
	private static final int MIN_DELTA_FOR_SCROLLING = 1;

	/** 
	 * Current value & label text color 
	 * 
	 *  </br>
	 * 锟斤拷前选锟斤拷锟斤拷  锟斤拷 锟斤拷锟斤拷 锟斤拷 锟斤拷色 
	 */
	private static final int VALUE_TEXT_COLOR = 0xF0000000;

	/** 
	 * ����涓��ㄥ����瀛�浣�棰���
	 * 
	 *  </br>
	 * 选锟斤拷 锟斤拷 锟斤拷锟斤拷 锟斤拷 锟斤拷色 
	 */
	private static final int ITEMS_TEXT_COLOR = 0xFFCFCFCF;

	/** 
	 * Top and bottom shadows colors 
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟酵底诧拷锟斤拷影 锟斤拷 锟斤拷色   </br>
	 * 选锟斤拷锟斤拷 锟斤拷锟斤拷锟酵底诧拷锟斤拷色锟角斤拷锟斤拷模锟斤拷锟斤拷锟街革拷锟揭伙拷锟斤拷锟斤拷锟缴�锟斤拷锟斤拷锟斤拷
	 */
	private static final int[] SHADOWS_COLORS = new int[] { 0x00FFFFFF,
			0x00FFFFFF, 0x00FFFFFF };

	/** 
	 * Additional items height (is added to standard text item height) 
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷母叨锟斤拷锟侥高讹拷  (锟斤拷位应锟斤拷锟斤拷dp) </br>
	 * 锟接猴拷锟斤拷getDesiredHeight() 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街涤�锟斤拷锟斤拷平锟街革拷每一锟斤拷选锟斤拷摹锟� </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫撅拷桑锟斤拷锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷芎停锟揭诧拷锟斤拷锟斤拷锟�5锟斤拷锟缴硷拷锟筋，锟斤拷么每锟斤拷锟缴硷拷锟斤拷母锟斤拷痈呔锟斤拷锟� ADDITIONAL_ITEM_HEIGHT/5
	 */
	private static final int ADDITIONAL_ITEM_HEIGHT = 50;

	/** 
	 * Text size 
	 * 
	 *  </br>
	 * 锟街猴拷
	 * 字体大小
	 */
	private static final int TEXT_SIZE = 41;

	/** 
	 * Top and bottom items offset (to hide that) 
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟窖★拷锟斤拷诙锟斤拷锟斤拷偷撞锟斤拷锟斤拷锟斤拷锟斤拷锟街碉拷锟� </br>
	 * 锟斤拷么锟斤拷锟斤拷锟斤拷~ 锟斤拷实锟斤拷一锟铰撅拷知锟斤拷锟剿ｏ拷 </br>
	 * &nbsp; 锟斤拷锟斤拷说锟斤拷picker锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷(锟叫硷拷锟角革拷锟斤拷选锟叫碉拷)锟斤拷剩锟斤拷4锟斤拷没选锟叫的★拷 </br>
	 * &nbsp; 锟斤拷没选锟叫碉拷锟斤拷4锟斤拷锟叫ｏ拷位锟节讹拷锟斤拷锟酵底诧拷锟斤拷锟筋，锟斤拷锟斤拷锟斤拷影锟节碉拷(锟节碉拷一锟斤拷锟街ｏ拷锟斤拷锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷要锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷) </br>
	 * &nbsp; 锟斤拷锟斤拷锟借定锟斤拷值锟斤拷锟斤拷锟斤拷指锟斤拷锟节碉拷锟斤拷size锟斤拷锟斤拷锟斤拷锟侥�锟斤拷值 TEXT_SIZE / 5 锟斤拷锟节碉拷锟斤拷1/5锟斤拷锟街猴拷 (锟斤拷么锟斤拷位也应锟斤拷锟斤拷sp锟斤拷)
	 */
	private static final int ITEM_OFFSET = TEXT_SIZE / 7;

	/** 
	 * Additional width for items layout 
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷占洌� 锟斤拷锟斤拷锟�~~锟斤拷锟斤拷锟斤拷锟皆帮拷 </br>
	 * 应锟斤拷锟斤拷锟斤拷目锟斤拷锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥ｏ拷 锟斤拷锟矫碉拷太锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟绞撅拷锟斤拷锟斤拷锟斤拷 </br>
	 * 锟斤拷锟斤拷影锟斤拷 锟叫达拷锟斤拷一锟斤拷实锟斤拷证锟斤拷
	 */
	private static final int ADDITIONAL_ITEMS_SPACE = 10;

	/** 
	 * Label offset
	 * 
	 *  </br>
	 * 锟斤拷签锟斤拷锟斤拷 锟斤拷锟斤拷锟斤拷未知锟斤拷 锟斤拷1,8锟斤拷锟斤拷800 锟斤拷锟斤拷值实锟介（8锟斤拷默锟斤拷值锟斤拷 效锟斤拷锟斤拷一锟斤拷锟侥★拷 
	 */
	private static final int LABEL_OFFSET = 8;

	/** 
	 * Left and right padding value
	 * 
	 *  </br>
	 * 锟斤拷锟�  </br>
	 * 锟斤拷锟窖★拷锟斤拷锟节诧拷锟斤拷洌�锟斤拷锟窖★拷锟斤拷歉锟�TextView锟侥伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫的硷拷锟斤拷TextView锟斤拷锟斤拷锟� </br>
	 * =锟斤拷=锟斤拷锟斤拷锟斤拷锟斤拷牖癸拷锟斤拷锌锟斤拷锟�item锟斤拷啥锟斤拷也锟斤拷知锟斤拷
	 */
	private static final int PADDING = 10;

	/** 
	 * Default count of visible items 
	 * 
	 *  </br>
	 * 默锟较可硷拷锟斤拷锟斤拷锟侥匡拷锟斤拷锟斤拷锟�picker锟斤拷锟斤拷示锟斤拷锟斤拷
	 */
	private static final int DEF_VISIBLE_ITEMS = 5;

	// Wheel Values
	/**
	 * Wheel Values
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷items锟斤拷通锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟结供锟侥帮拷
	 */
	private WheelAdapter adapter = null;
	/**
	 * Wheel Values
	 * 
	 *  </br>
	 * 锟斤拷前锟斤拷
	 */
	private int currentItem = 0;
	
	// Widths
	/**
	 * Widths
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷实锟斤拷 锟斤拷值锟斤拷为100 没锟斤拷锟轿何变化
	 */
	private int itemsWidth = 0;
	/**
	 * Widths
	 * 
	 *  </br>
	 * 也锟斤拷锟斤拷实锟斤拷 锟斤拷值锟斤拷为100 没锟斤拷锟轿何变化
	 */
	private int labelWidth = 0;

	// Count of visible items
	/**
	 * Count of visible items
	 * 
	 *  </br>
	 * 锟缴硷拷锟斤拷目锟斤拷锟斤拷锟斤拷
	 */
	private int visibleItems = DEF_VISIBLE_ITEMS;
	
	// Item height
	/**
	 * Item height
	 * 
	 *  </br>
	 * 锟斤拷item锟侥革拷
	 */
	private int itemHeight = 0;

	// Text paints
	/**
	 * Text paints
	 * 
	 *  </br>
	 * 选锟斤拷锟侥憋拷锟斤拷锟斤拷色
	 */
	private TextPaint itemsPaint;
	/**
	 * Text paints
	 * 
	 *  </br>
	 * 未选锟斤拷锟侥憋拷锟斤拷锟斤拷色
	 */
	private TextPaint valuePaint;

	// Layouts
	/**
	 * Layouts
	 * 
	 *  </br>
	 * 选锟斤拷  锟斤拷 锟斤拷锟斤拷
	 */
	private StaticLayout itemsLayout;
	/**
	 * Layouts
	 * 
	 *  </br>
	 * 锟斤拷签 锟斤拷 锟斤拷锟斤拷
	 */
	private StaticLayout labelLayout;
	/**
	 * Layouts
	 * 
	 *  </br>
	 * 选锟斤拷锟斤拷 锟斤拷 锟斤拷锟斤拷
	 */
	private StaticLayout valueLayout;

	// Label & background
	/**
	 * Label & background
	 * 
	 *  </br>
	 * 锟斤拷签
	 */
	private String label;
	/**
	 * Label & background
	 * 锟叫硷拷募锟斤拷锟斤拷锟� </br>
	 * 选锟斤拷锟斤拷锟斤拷谋锟斤拷锟�
	 */
	private Drawable centerDrawable;

	// Shadows drawables
	/**
	 * Shadows drawables
	 * 
	 *  </br>
	 * 锟较憋拷 锟斤拷 锟阶诧拷 锟斤拷锟斤拷影锟斤拷锟街的憋拷锟斤拷锟斤拷源
	 */
	private GradientDrawable topShadow;
	private GradientDrawable bottomShadow;

	// Scrolling
	/**
	 * Scrolling
	 * 
	 *  </br>
	 * 执锟叫癸拷锟斤拷锟斤拷
	 */
	private boolean isScrollingPerformed; 
	/**
	 * Scrolling
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	private int scrollingOffset;

	// Scrolling animation
	/**
	 * Scrolling animation
	 * 
	 *  </br>
	 * 锟斤拷锟狡硷拷锟斤拷锟�
	 */
	private GestureDetector gestureDetector;
	/**
	 * Scrolling animation
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷
	 */
	private Scroller scroller;
	/**
	 * Scrolling animation
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷 锟斤拷锟斤拷Y
	 */
	private int lastScrollY;

	// Cyclic
	/**
	 * Cyclic
	 * 
	 *  </br>
	 * 锟角凤拷循锟斤拷
	 */
	boolean isCyclic = false;
	
	// Listeners
	/**
	 * Listeners
	 * 
	 *  </br>
	 * 锟截硷拷锟侥憋拷锟斤拷锟斤拷锟斤拷锟� 锟斤拷锟斤拷
	 */
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	/**
	 * Listeners
	 * 
	 *  </br>
	 * 锟截硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷
	 */
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();

	/**
	 * Constructor
	 *
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷 锟斤拷实锟斤拷锟斤拷锟斤拷锟狡硷拷锟斤拷锟� 锟斤拷 锟斤拷锟斤拷
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	/**
	 * Constructor
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷 锟斤拷实锟斤拷锟斤拷锟斤拷锟狡硷拷锟斤拷锟� 锟斤拷 锟斤拷锟斤拷
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	/**
	 * Constructor
	 *
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷 锟斤拷实锟斤拷锟斤拷锟斤拷锟狡硷拷锟斤拷锟� 锟斤拷 锟斤拷锟斤拷
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}
	
	/**
	 * Initializes class data
	 * @param context the context
	 *
	 * </br>
	 * 锟斤拷锟捷筹拷始锟斤拷 </br>
	 * 锟斤拷锟角帮拷锟斤拷锟狡硷拷锟斤拷锟� 锟斤拷 锟斤拷锟斤拷锟斤拷 实锟斤拷锟斤拷 </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷械墓锟斤拷锟斤拷锟斤拷卸锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	private void initData(Context context) {
		gestureDetector = new GestureDetector(context, gestureListener);
		gestureDetector.setIsLongpressEnabled(false); //锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟绞裁达拷茫锟斤拷锟斤拷锟斤拷茫锟斤拷锟斤拷锟斤拷贸锟�true锟斤拷锟斤拷影锟斤拷效锟斤拷
		
		scroller = new Scroller(context);
	}
	
	/**
	 * Gets wheel adapter
	 * @return the adapter
	 *
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	public WheelAdapter getAdapter() {
		return adapter;
	}
	
	/**
	 * Sets wheel adapter
	 * @param adapter the new wheel adapter
	 *
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟截伙拷
	 */
	public void setAdapter(WheelAdapter adapter) {
		this.adapter = adapter;
		invalidateLayouts();
		invalidate();
	}
	
	/**
	 * Set the the specified scrolling interpolator
	 * @param interpolator the interpolator
	 *
	 * </br>
	 * 锟斤拷锟斤拷也锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷陌桑锟斤拷锟酵�锟斤拷锟斤拷锟斤拷锟藉补锟斤拷锟斤拷实锟斤拷 锟斤拷锟斤拷锟斤拷锟�
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.forceFinished(true);
		scroller = new Scroller(getContext(), interpolator);
	}
	
	/**
	 * Gets count of visible items
	 * 
	 * @return the count of visible items
	 *
	 * </br>
	 * 锟斤拷每杉锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets count of visible items
	 * 
	 * @param count
	 *            the new count
	 *
	 * </br>
	 * 锟斤拷锟矫可硷拷锟斤拷锟斤拷锟斤拷锟� 锟斤拷锟截伙拷view
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
		invalidate();
	}

	/**
	 * Gets label
	 * 
	 * @return the label
	 *
	 * </br>
	 * 锟斤拷帽锟角�
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets label
	 * 
	 * @param newLabel
	 *            the label to set
	 *
	 * </br>
	 * 锟斤拷锟矫憋拷签
	 */
	public void setLabel(String newLabel) {
		if (label == null || !label.equals(newLabel)) {
			label = newLabel;
			labelLayout = null;
			invalidate();
		}
	}
	
	/**
	 * Adds wheel changing listener
	 * @param listener the listener 
	 *
	 * </br>
	 * 锟斤拷涌丶锟斤拷谋锟斤拷锟斤拷锟斤拷
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * @param listener the listener
	 *
	 * </br>
	 * 锟狡筹拷锟截硷拷锟侥憋拷锟斤拷锟斤拷锟�
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}
	
	/**
	 * Notifies changing listeners
	 * @param oldValue the old wheel value
	 * @param newValue the new wheel value
	 *
	 * </br>
	 * 通知 锟侥憋拷锟斤拷锟斤拷锟斤拷锟� </br>
	 * 循锟斤拷  锟截硷拷锟侥憋拷锟斤拷锟斤拷锟斤拷锟斤拷希锟� 锟斤拷锟斤拷锟轿碉拷锟斤拷锟斤拷锟角碉拷onChenge锟斤拷锟斤拷
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * @param listener the listener 
	 *
	 * </br>
	 * 锟斤拷涌丶锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * @param listener the listener
	 *
	 * </br>
	 * 锟狡筹拷锟截硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}
	
	/**
	 * Notifies listeners about starting scrolling
	 *
	 * </br>
	 * 通知锟截硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫匡拷始锟斤拷锟斤拷锟侥凤拷锟斤拷
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 *
	 * </br>
	 * 通知锟截硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫斤拷锟斤拷锟斤拷锟斤拷锟侥凤拷锟斤拷
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Gets current value
	 * 
	 * @return the current value
	 *
	 * </br>
	 * 锟斤拷锟截碉拷前锟斤拷锟斤拷锟斤拷锟�
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * @param index the item index
	 * @param animated the animation flag
	 *
	 * </br>
	 * 锟斤拷锟矫碉拷前锟斤拷 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟截硷拷什么锟斤拷锟斤拷锟斤拷锟斤拷哟 </br>
	 * 锟节讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟角凤拷使锟矫癸拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return; // throw?
		}
		if (index < 0 || index >= adapter.getItemsCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += adapter.getItemsCount();
				}
				index %= adapter.getItemsCount();
			} else{
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				scroll(index - currentItem, SCROLLING_DURATION);
			} else {
				invalidateLayouts();
			
				int old = currentItem;
				currentItem = index;
			
				notifyChangingListeners(old, currentItem);
			
				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * @param index the item index
	 *
	 * </br>
	 * 锟斤拷锟矫碉拷前选锟斤拷锟筋，默锟斤拷锟角诧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}	
	
	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
	 * @return true if wheel is cyclic
	 *
	 * </br>
	 * 锟角凤拷循锟斤拷锟斤拷示
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * @param isCyclic the flag to set
	 *
	 * </br>
	 * 锟斤拷锟斤拷锟角凤拷循锟斤拷锟斤拷示
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		
		invalidate();
		invalidateLayouts();
	}

	/**
	 * Invalidates layouts
	 * 
	 *  </br>
	 * 锟截绘布锟斤拷 </br>
	 * 锟斤拷锟斤拷锟斤拷 选锟筋布锟斤拷itemsLayout 锟斤拷 选锟斤拷锟筋布锟斤拷 valueLayout 锟斤拷值为null </br>
	 * 同锟铰斤拷 锟斤拷锟斤拷锟斤拷锟斤拷?scrollingOffset 锟斤拷锟斤拷为0 </br>
	 * 锟斤拷锟斤拷锟�scrollingOffset 锟斤拷没锟斤拷锟斤拷锟斤拷锟斤拷什么锟矫ｏ拷
	 */
	private void invalidateLayouts() {
		itemsLayout = null;
		valueLayout = null;
		scrollingOffset = 0;
	}

	/**
	 * Initializes resources
	 *
	 * </br>
	 * 锟斤拷始锟斤拷源 </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷模锟� 锟叫讹拷前锟芥定锟斤拷幕锟斤拷省锟斤拷锟斤拷锟斤拷锟皆达拷锟剿斤拷锟斤拷锟斤拷缘锟街碉拷锟斤拷锟斤拷锟斤拷null锟斤拷锟斤拷锟铰从撅拷态锟斤拷锟斤拷锟斤拷取值锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷锟皆★拷 </br>
	 * 锟斤拷锟斤拷锟叫╋拷锟揭�锟斤拷锟斤拷为锟秸的伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟接�锟斤拷锟斤拷锟剿筹拷始锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	private void initResourcesIfNecessary() {
		if (itemsPaint == null) {
			itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
					| Paint.FAKE_BOLD_TEXT_FLAG);
			//itemsPaint.density = getResources().getDisplayMetrics().density;
			itemsPaint.setTextSize(TEXT_SIZE);
		}

		if (valuePaint == null) {
			valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
					| Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
			//valuePaint.density = getResources().getDisplayMetrics().density;
			valuePaint.setTextSize(TEXT_SIZE);
			valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
		}

		if (centerDrawable == null) {
			centerDrawable = getContext().getResources().getDrawable(R.drawable.wheel_newval);
		}

		if (topShadow == null) {
			topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
		}

		if (bottomShadow == null) {
			bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
		}

		setBackgroundResource(R.drawable.wheel_newbg);
	}

	/**
	 * Calculates desired height for layout
	 * 
	 * @param layout
	 *            the source layout
	 * @return the desired layout height
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟侥控硷拷锟竭度ｏ拷锟斤拷锟斤拷证锟戒不锟斤拷锟节斤拷锟斤拷锟斤拷锟叫★拷叨锟�
	 */
	private int getDesiredHeight(Layout layout) {
		if (layout == null) {
			return 0;
		}

		int desired = getItemHeight() * visibleItems - ITEM_OFFSET * 2
				- ADDITIONAL_ITEM_HEIGHT;

		// Check against our minimum height
		desired = Math.max(desired, getSuggestedMinimumHeight());

		return desired;
	}

	/**
	 * Returns text item by index
	 * @param index the item index
	 * @return the item or null
	 * 
	 * </br> 
	 * 指锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟窖★拷锟斤拷锟侥憋拷值(Sring) </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟轿э拷锟斤拷丶锟斤拷植锟斤拷锟窖�锟斤拷锟侥ｏ拷isCyclic锟斤拷锟斤拷锟津返伙拷null </br>
	 * 锟斤拷锟斤拷锟窖�锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷诓锟斤拷锟斤拷锟斤拷舜锟斤拷锟� </br>
	 * 为锟斤拷锟斤拷锟斤拷+count, 然锟斤拷取锟斤拷
	 */
	private String getTextItem(int index) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return null;
		}
		int count = adapter.getItemsCount();
		if ((index < 0 || index >= count) && !isCyclic) {
			return null;
		} else {
			while (index < 0) {
				index = count + index;
			}
		}
		
		index %= count;
		return adapter.getItem(index);
	}
	
	/**
	 * Builds text depending on current value
	 * 
	 * @param useCurrentValue
	 * @return the text
	 * 
	 *  </br>
	 * 锟斤拷锟斤拷锟斤拷前锟斤拷 锟斤拷锟斤拷锟侥憋拷 </br>
	 * 锟斤拷锟揭伙拷锟斤拷址锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷前锟斤拷目锟斤拷锟斤拷锟斤拷为5, 锟缴硷拷锟斤拷目锟斤拷为3  </br>
	 * 锟斤拷锟街凤拷锟斤拷锟斤拷值为 getTextItem(3).append("\n").getTextItem(4).append("\n").getTextItem(5).append("\n").getTextItem(6).append("\n").getTextItem(7) </br>
	 * 锟斤拷锟� 锟斤拷锟斤拷useCurrentValue为false </br>
	 * 锟津返回碉拷锟街凤拷锟斤拷为 getTextItem(3).append("\n").getTextItem(4).append("\n").getTextItem(6).append("\n").getTextItem(7) </br>
	 * 锟斤拷锟�getTextItem(i)锟斤拷锟斤拷null, 锟津不伙拷锟津返伙拷值锟斤拷追锟斤拷
	 */
	private String buildText(boolean useCurrentValue) {
		StringBuilder itemsText = new StringBuilder();
		int addItems = visibleItems / 2 + 1;

		for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
			if (useCurrentValue || i != currentItem) {
				String text = getTextItem(i);
				if (text != null) {
					itemsText.append(text);
				}
			}
			if (i < currentItem + addItems) {
				itemsText.append("\n");
			}
		}
		
		return itemsText.toString();
	}

	/**
	 * Returns the max item length that can be present
	 * @return the max length
	 * 
	 *  </br>
	 *  锟斤拷锟斤拷锟斤拷锟斤拷谋锟斤拷锟斤拷锟�</br>
	 *  锟斤拷锟斤拷锟斤拷锟截硷拷锟斤拷锟斤拷玫锟�
	 */
	private int getMaxTextLength() {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return 0;
		}
		
		int adapterLength = adapter.getMaximumLength();
		if (adapterLength > 0) {
			return adapterLength;
		}

		String maxText = null;
		int addItems = visibleItems / 2;
		for (int i = Math.max(currentItem - addItems, 0);
				i < Math.min(currentItem + visibleItems, adapter.getItemsCount()); i++) {
			String text = adapter.getItem(i);
			if (text != null && (maxText == null || maxText.length() < text.length())) {
				maxText = text;
			}
		}//锟斤拷锟窖�锟斤拷锟侥凤拷围没锟斤拷锟斤拷锟斤拷呀锟斤拷锟斤拷始值锟斤拷锟斤拷锟斤拷循锟斤拷锟斤拷 锟斤拷锟斤拷为什么锟斤拷 锟斤拷前锟斤拷锟斤拷锟斤拷+锟缴硷拷锟斤拷目锟斤拷锟斤拷

		return maxText != null ? maxText.length() : 0;
	}

	/**
	 * Returns height of wheel item
	 * @return the item height
	 * 
	 * </br>
	 * 锟斤拷锟窖★拷锟斤拷
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		} else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
			itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
			return itemHeight;
		}//锟斤拷锟斤拷锟�itemlayout 为什么要锟斤拷 锟斤拷锟斤拷锟叫碉拷top锟斤拷锟斤拷一锟叫碉拷top锟截ｏ拷锟斤拷锟斤拷应锟矫凤拷锟斤拷layout锟侥革拷锟斤？没锟斤拷锟斤拷锟斤拷
		
		return getHeight() / visibleItems;
	}

	/**
	 * Calculates control width and creates text layouts
	 * @param widthSize the input layout width
	 * @param mode the layout mode
	 * @return the calculated control width
	 * 
	 * </br>
	 * 锟斤拷锟姐布锟街匡拷
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		int width = widthSize;

		int maxLength = getMaxTextLength();
		if (maxLength > 0) {
			double textWidth = Math.ceil(Layout.getDesiredWidth("0", itemsPaint));
			itemsWidth = (int) (maxLength * textWidth);
		} else {
			itemsWidth = 0;
		}
		itemsWidth += ADDITIONAL_ITEMS_SPACE; // make it some more

		labelWidth = 0;
		if (label != null && label.length() > 0) {
			labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label, valuePaint));
		}

		boolean recalculate = false;
		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
			recalculate = true;
		} else {
			width = itemsWidth + labelWidth + 2 * PADDING;
			if (labelWidth > 0) {
				width += LABEL_OFFSET;
			}

			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
				recalculate = true;
			}
		}

		if (recalculate) {
			// recalculate width
			int pureWidth = width - LABEL_OFFSET - 2 * PADDING;
			if (pureWidth <= 0) {
				itemsWidth = labelWidth = 0;
			}
			if (labelWidth > 0) {
				double newWidthItems = (double) itemsWidth * pureWidth
						/ (itemsWidth + labelWidth);
				itemsWidth = (int) newWidthItems;
				labelWidth = pureWidth - itemsWidth;
			} else {
				itemsWidth = pureWidth + LABEL_OFFSET; // no label
			}
		}

		if (itemsWidth > 0) {
			createLayouts(itemsWidth, labelWidth);
		}

		return width;
	}

	/**
	 * Creates layouts
	 * @param widthItems width of items layout
	 * @param widthLabel width of label layout
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷</br>
	 */
	private void createLayouts(int widthItems, int widthLabel) {
		if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
			itemsLayout = new StaticLayout(buildText(isScrollingPerformed), itemsPaint, widthItems,
					widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER,
					1, ADDITIONAL_ITEM_HEIGHT, false);
		} else {
			itemsLayout.increaseWidthTo(widthItems);
		}

		if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
			String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
			valueLayout = new StaticLayout(text != null ? text : "",
					valuePaint, widthItems, widthLabel > 0 ?
							Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER,
							1, ADDITIONAL_ITEM_HEIGHT, false);
		} else if (isScrollingPerformed) {
			valueLayout = null;
		} else {
			valueLayout.increaseWidthTo(widthItems);
		}

		if (widthLabel > 0) {
			if (labelLayout == null || labelLayout.getWidth() > widthLabel) {
				labelLayout = new StaticLayout(label, valuePaint,
						widthLabel, Layout.Alignment.ALIGN_NORMAL, 1,
						ADDITIONAL_ITEM_HEIGHT, false);
			} else {
				labelLayout.increaseWidthTo(widthLabel);
			}
		}
	}

	/**
	 * 锟斤拷写onMeasure 锟斤拷锟矫尺达拷
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	/**
	 * 锟斤拷锟斤拷
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (itemsLayout == null) {
			if (itemsWidth == 0) {
				calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			} else {
				createLayouts(itemsWidth, labelWidth);
			}
		}

		if (itemsWidth > 0) {
			canvas.save();
			// Skip padding space and hide a part of top and bottom items
			canvas.translate(PADDING, -ITEM_OFFSET);
			drawItems(canvas);
			drawValue(canvas);
			canvas.restore();
		}

		drawCenterRect(canvas);
		drawShadows(canvas);
	}

	/**
	 * Draws shadows on top and bottom of control
	 * @param canvas the canvas for drawing
	 * 
	 * </br>
	 * 锟斤拷锟狡控硷拷锟斤拷锟斤拷 锟酵底诧拷锟斤拷 锟斤拷影锟斤拷锟斤拷 </br>
	 * 锟斤拷要锟斤拷锟诫画锟斤拷锟斤拷锟斤拷
	 */
	private void drawShadows(Canvas canvas) {
		topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
		topShadow.draw(canvas);

		bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems,
				getWidth(), getHeight());
		bottomShadow.draw(canvas);
	}

	/**
	 * Draws value and label layout
	 * @param canvas the canvas for drawing
	 * 
	 * </br>
	 * 锟斤拷锟斤拷选锟斤拷锟斤拷 锟斤拷 锟斤拷签
	 */
	private void drawValue(Canvas canvas) {
		valuePaint.setColor(VALUE_TEXT_COLOR);
		valuePaint.drawableState = getDrawableState();

		Rect bounds = new Rect();
		itemsLayout.getLineBounds(visibleItems / 2, bounds);

		// draw label
		if (labelLayout != null) {
			canvas.save();
			canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET, bounds.top);
			labelLayout.draw(canvas);
			canvas.restore();
		}

		// draw current value
		if (valueLayout != null) {
			canvas.save();
			canvas.translate(0, bounds.top + scrollingOffset);
			valueLayout.draw(canvas);
			canvas.restore();
		}
	}

	/**
	 * Draws items
	 * @param canvas the canvas for drawing
	 * 
	 * </br>
	 * 锟斤拷锟斤拷选锟斤拷
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();
		
		int top = itemsLayout.getLineTop(1);
		canvas.translate(0, - top + scrollingOffset);
		
		itemsPaint.setColor(ITEMS_TEXT_COLOR);
		itemsPaint.drawableState = getDrawableState();
		itemsLayout.draw(canvas);
		
		canvas.restore();
	}

	/**
	 * Draws rect for current value
	 * @param canvas the canvas for drawing
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟叫硷拷木锟斤拷锟斤拷锟斤拷锟�
	 */
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = getItemHeight() / 2;
		centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
		centerDrawable.draw(canvas);
	}

	/**
	 * 锟斤拷锟斤拷锟铰硷拷锟斤拷 锟截碉拷锟斤拷锟斤拷</br>
	 * 锟斤拷锟斤拷锟剿帮拷 锟斤拷锟斤拷锟斤拷锟斤拷锟� adapter锟斤拷null 锟侥伙拷锟斤拷锟斤拷锟斤拷锟斤拷什么锟斤拷锟斤拷锟斤拷锟斤拷锟侥★拷</br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟轿�锟秸ｏ拷锟斤拷MotionEvent锟斤拷锟捷革拷锟斤拷锟斤拷识锟斤拷锟斤拷锟斤拷 锟斤拷锟叫讹拷锟角凤拷锟斤拷ACTION_UP </br>
	 * 锟斤拷锟斤拷锟剿碉拷锟斤拷锟斤拷锟斤拷呀锟斤拷锟� 锟斤拷锟斤拷justify()锟斤拷锟斤拷锟斤拷</br>
	 * return true. 锟斤拷锟斤拷泄露
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return true;
		}
		
			if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
			justify();
		}
		return true;
	}
	
	/**
	 * Scrolls the wheel
	 * @param delta the scrolling value
	 * 
	 * </br>
	 * 锟斤拷锟斤拷</br>
	 * 锟斤拷锟斤拷只锟斤拷锟斤拷锟铰讹拷锟斤拷锟斤拷 scrollingOffset锟斤拷值锟斤拷</br>
	 * 执锟叫癸拷锟斤拷锟侥诧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷</br>
	 * 锟斤拷锟斤拷锟襟看吧★拷
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;
		
		int count = scrollingOffset / getItemHeight();
		int pos = currentItem - count;
		if (isCyclic && adapter.getItemsCount() > 0) {
			// fix position by rotating
			while (pos < 0) {
				pos += adapter.getItemsCount();
			}
			pos %= adapter.getItemsCount();
		} else if (isScrollingPerformed) {
			// 
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= adapter.getItemsCount()) {
				count = currentItem - adapter.getItemsCount() + 1;
				pos = adapter.getItemsCount() - 1;
			}
		} else {
			// fix position
			pos = Math.max(pos, 0);
			pos = Math.min(pos, adapter.getItemsCount() - 1);
		}
		
		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}
		
		// update offset
		scrollingOffset = offset - count * getItemHeight();
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}
	
	// gesture listener
	/**
	 * gesture listener
	 * 
	 * </br>
	 * 锟斤拷锟狡硷拷锟斤拷锟斤拷</br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷应锟矫伙拷fling 锟皆硷拷 scroll锟斤拷锟斤拷锟侥达拷锟斤拷
	 */
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		/**
		 * 锟斤拷锟斤拷前锟斤拷锟绞蓖Ｖ癸拷锟斤拷锟斤拷牟锟斤拷锟�
		 */
		public boolean onDown(MotionEvent e) {
			if (isScrollingPerformed) {
				scroller.forceFinished(true);
				clearMessages();
				return true;
			}
			return false;
		}
		
		/**
		 * scroll
		 */
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			startScrolling();
			doScroll((int)-distanceY);
			return true;
		}
		
		/**
		 * fling
		 */
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			lastScrollY = currentItem * getItemHeight() + scrollingOffset;
			int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
			int minY = isCyclic ? -maxY : 0;
			scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY, maxY);
			setNextMessage(MESSAGE_SCROLL);
			return true;
		}
	};

	// Messages
	/**
	 * Messages
	 * 
	 * </br>
	 * 锟津动伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟酵碉拷锟斤拷息 -锟斤拷锟斤拷
	 */
	private final int MESSAGE_SCROLL = 0;
	/**
	 * Messages
	 * 
	 * </br>
	 * 锟津动伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟酵碉拷锟斤拷息 -证锟斤拷
	 */
	private final int MESSAGE_JUSTIFY = 1;
	
	/**
	 * Set next message to queue. Clears queue before.
	 * 
	 * @param message the message to set
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�animationHandler锟叫碉拷原锟斤拷锟斤拷息锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷息
	 */
	private void setNextMessage(int message) {
		clearMessages();
		animationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟皆�锟叫碉拷锟斤拷息
	 */
	private void clearMessages() {
		animationHandler.removeMessages(MESSAGE_SCROLL);
		animationHandler.removeMessages(MESSAGE_JUSTIFY);
	}
	
	// animation handler
	/**
	 * animation handler
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	private Handler animationHandler = new Handler() {
		public void handleMessage(Message msg) {
			scroller.computeScrollOffset();
			int currY = scroller.getCurrY();
			int delta = lastScrollY - currY;
			lastScrollY = currY;
			if (delta != 0) {
				doScroll(delta);
			}
			
			// scrolling is not finished when it comes to final Y
			// so, finish it manually 
			if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
				currY = scroller.getFinalY();
				scroller.forceFinished(true);
			}
			if (!scroller.isFinished()) {
				animationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}
		}
	};
	
	/**
	 * Justifies wheel
	 * 
	 * </br>
	 * 锟斤拷证锟斤拷锟斤拷
	 */
	private void justify() {
		if (adapter == null) {
			return;
		}
		
		lastScrollY = 0;
		int offset = scrollingOffset;
		int itemHeight = getItemHeight();
		boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0; 
		if ((isCyclic || needToIncrease) && Math.abs((float) offset) > (float) itemHeight / 2) {
			if (offset < 0)
				offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
			else
				offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
		}
		if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
			scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
			setNextMessage(MESSAGE_JUSTIFY);
		} else {
			finishScrolling();
		}
	}
	
	/**
	 * Starts scrolling
	 * 
	 * </br>
	 * 锟斤拷始锟斤拷锟斤拷</br>
	 * 锟斤拷通知锟斤拷始锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	private void startScrolling() {
		if (!isScrollingPerformed) {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}
	}

	/**
	 * Finishes scrolling
	 * 
	 * </br>
	 * 锟斤拷锟斤拷锟斤拷锟斤拷</br>
	 * 锟斤拷通知锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	void finishScrolling() {
		if (isScrollingPerformed) {
			notifyScrollingListenersAboutEnd();
			isScrollingPerformed = false;
		}
		invalidateLayouts();
		invalidate();
	}
	
	
	/**
	 * Scroll the wheel
	 * @param itemsToSkip items to scroll
	 * @param time scrolling duration
	 * 
	 * 锟斤拷锟斤拷
	 */
	public void scroll(int itemsToScroll, int time) {
		scroller.forceFinished(true);

		lastScrollY = scrollingOffset;
		int offset = itemsToScroll * getItemHeight();
		
		scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
		setNextMessage(MESSAGE_SCROLL);
		
		startScrolling();
	}

}
