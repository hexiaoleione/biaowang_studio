package com.hex.express.iwant.adapters;

import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LoginActivity;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.adapters.MylisAdapter.MylisViewHoder;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class MylisQuotAdapter extends BaseListAdapter{

	public MylisQuotAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new MylisViewHoder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.quote_item;
	}
	class MylisViewHoder extends ViewHolder{

		public MylisViewHoder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.item_wupin)
		TextView item_wupin;
		@Bind(R.id.item_zhongliang)
		TextView item_zhongliang;
		@Bind(R.id.item_tiji)
		TextView item_tiji;
		@Bind(R.id.item_dizhi1)
		TextView item_dizhi1;
		@Bind(R.id.item_quhuo)
		TextView item_quhuo;
		@Bind(R.id.item_dizhi2)
		TextView item_dizhi2;
		@Bind(R.id.item_songhuo)
		TextView item_songhuo;
		@Bind(R.id.item_name)
		TextView item_name;
		@Bind(R.id.item_phone)
		TextView item_phone;
		@Bind(R.id.item_time)
		TextView item_time;
		@Bind(R.id.item_xin)//查看报价公司列表
		TextView item_xin;
		@Bind(R.id.item_weizhifu)//未支付图片
		ImageView item_weizhifu;
		
		@Bind(R.id.button1)
		Button button1;
		@Bind(R.id.button2)
		Button button2;
		private CardBean cardBean;

		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			cardBean = new CardBean();
			cardBean.data = list;
		
		}
		}
}
