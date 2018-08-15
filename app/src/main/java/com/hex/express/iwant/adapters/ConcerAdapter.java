package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.DownSpecialBean;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;

public class ConcerAdapter extends BaseListAdapter{

	public ConcerAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new ConcerViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.concer_item;
	}
	class ConcerViewHolder extends ViewHolder {

		public ConcerViewHolder(View itemView) {
			super(itemView);
		}
		@Bind(R.id.concer_juli)//距离
		TextView concer_juli;
		@Bind(R.id.concer_time)//时间
		TextView concer_time;
		@Bind(R.id.concer_wupin)//物品名字
		TextView concer_wupin;
		@Bind(R.id.concer_zhongliang)// 重量
		TextView concer_zhongliang;
		@Bind(R.id.concer_tiji)//体积
		TextView concer_tiji;
		@Bind(R.id.concer_songhuodizhi)//送货地址
		TextView concer_songhuodizhi;
		@Bind(R.id.concer_quhuo)//取货操作
		TextView concer_quhuo;
		@Bind(R.id.concer_daodadizhi)//达到地址
		TextView concer_daodadizhi;
		@Bind(R.id.concer_songhuo)//送货操作
		TextView concer_songhuo;
		@Bind(R.id.concer_baojia)//报价
		TextView concer_baojia;
	
		
		
		
		private DownSpecialBean bean;

		@Override
		public void setData(final int position) {
			super.setData(position);
			bean = new DownSpecialBean();
			bean.data = list;
			Log.e("BEAM", bean.data.get(position).toString());
		
		}
		}

}
