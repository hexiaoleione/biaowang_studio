package com.hex.express.iwant.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;

import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.EvaluationOfEscortBean;
import com.hex.express.iwant.bean.EvaluationOfEscortBean.Data;

public class EscortMessageAdapter extends BaseListAdapter {

	public EscortMessageAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MyViewHodler(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_escort_message;
	}

	class MyViewHodler extends ViewHolder {

		public MyViewHodler(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.text_message)
		TextView text_message;
		@Bind(R.id.rab_couriersscore)
		RatingBar rab_couriersscore;
		@Bind(R.id.text_time)
		TextView text_time;
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			EvaluationOfEscortBean bean = new EvaluationOfEscortBean();
			bean.data = list;
			text_time.setText(bean.data.get(position).getUpdateTime());// 评价时间；
			rab_couriersscore.setRating(Float.parseFloat(bean.data.get(position).getDriverScore()));// 评分；
			text_message.setText( bean.data.get(position).getDriverContent());// 设置评价内容
		}
	}

}
