package com.hex.express.iwant.adapters;

import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.utils.ToastUtil;

/**
 * 现金券Adapter
 * 
 * @author SCHT-50
 * 
 */
public class CardAdapter extends BaseListAdapter {
	public CardAdapter(Context context, List list) {
		super(context, list);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_activity_card;
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new CardViewHodler(itemView);
	}

	class CardViewHodler extends ViewHolder {
		public CardViewHodler(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.txt_cardname)
		TextView txt_cardname;
		@Bind(R.id.txt_cardmoney)
		TextView moneyView;
		@Bind(R.id.txt_cardexpname)
		TextView nameView;
		@Bind(R.id.txt_cardConditions)
		TextView conditionView;
		@Bind(R.id.txt_cardDuration)
		TextView timeView;
		@Bind(R.id.btn_usecard)
		TextView textView;
		@Bind(R.id.cb1)
		CheckBox cb;
		
		private CardBean cardBean;
		

		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			cardBean = new CardBean();
			cardBean.data = list;
			if (cardBean.data.get(position).ifExpired) {
				textView.setText("已过期");
				initData(position);
			} else {
				if (cardBean.data.get(position).ifUsed) {
					textView.setText("已使用");
					initData(position);

				} else {
					textView.setText("未使用");
					moneyView.setTextColor(context.getResources().getColor(
							R.color.orange1));
					String s = String.valueOf(cardBean.data.get(position).money);
					String money = s.substring(0,s.indexOf("."));
					moneyView.setText("¥" + money);
					timeView.setText("截止日期："
							+ cardBean.data.get(position).coupontime);
					timeView.setTextColor(context.getResources().getColor(
							R.color.gray));
					nameView.setText(cardBean.data.get(position).couponName);
					nameView.setTextColor(context.getResources().getColor(
							R.color.gray));
					conditionView.setText(
							 cardBean.data.get(position).conditions);
					conditionView.setTextColor(context.getResources().getColor(
							R.color.gray));

				}

			}

		}

		public void initData(final int position) {
			textView.setTextColor(context.getResources()
					.getColor(R.color.white));
			textView.setBackgroundColor(context.getResources().getColor(
					R.color.gray));
			String s = String.valueOf(cardBean.data.get(position).money);
			String newD = s.substring(0,s.indexOf("."));
			moneyView.setText("¥" + newD);
			moneyView.setTextColor(context.getResources()
					.getColor(R.color.gray));
			txt_cardname.setTextColor(context.getResources().getColor(
					R.color.gray));
			timeView.setText("截止日期："
					+ cardBean.data.get(position).coupontime);
			timeView.setTextColor(context.getResources().getColor(
					R.color.gray));
			nameView.setText(cardBean.data.get(position).couponName);
			nameView.setTextColor(context.getResources().getColor(
					R.color.gray));
			conditionView.setText(
					cardBean.data.get(position).conditions);
			conditionView.setTextColor(context.getResources().getColor(
					R.color.gray));
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				   
				   @Override
				   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					   Log.e("11111", "ggggg"+position);
				   }
				  });
		}
	}

}
