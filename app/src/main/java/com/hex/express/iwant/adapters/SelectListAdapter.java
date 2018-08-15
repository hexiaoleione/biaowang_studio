package com.hex.express.iwant.adapters;

import java.util.ArrayList;

import com.hex.express.iwant.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 下拉列表适配器
 *
 * @author Jone
 */
public class SelectListAdapter extends BaseAdapter
{
	private ArrayList<String> mAccounts = new ArrayList<String>();
	private LayoutInflater mInflater;
	private OnItemClickListener mOnItemClickListener;
	private OnDelBtnClickListener mOnDelBtnClickListener;

	public SelectListAdapter(Context context, ArrayList<String> items)
	{
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAccounts = items;
	}

	@Override
	public int getCount()
	{
		return mAccounts.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mAccounts.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.input_selectitem, null);
			viewHolder.mItem = (RelativeLayout) convertView.findViewById(R.id.input_selectitem_item);
			viewHolder.mAvatar = (ImageView) convertView.findViewById(R.id.input_selectitem_avatar);
			viewHolder.mAccount = (TextView) convertView.findViewById(R.id.input_selectitem_account);
			viewHolder.mDelete = (ImageButton) convertView.findViewById(R.id.input_selectitem_delete);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == mAccounts.size() - 1)
			viewHolder.mItem.setBackgroundResource(R.drawable.input_bottom_bg);
		else
			viewHolder.mItem.setBackgroundResource(R.drawable.input_middle_bg);

		viewHolder.mAccount.setText(mAccounts.get(position) + "");
//		viewHolder.mAvatar.setImageResource(R.drawable.ic_luncher);

		viewHolder.mItem.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mOnItemClickListener.onItemClicked(position);
			}
		});

		viewHolder.mDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mOnDelBtnClickListener.onDelBtnClicked(position);
			}
		});

		return convertView;
	}

	private static class ViewHolder
	{
		RelativeLayout mItem;
		ImageView mAvatar;
		TextView mAccount;
		ImageButton mDelete;
	}

	/**
	 * 选择条目点击监听器接口
	 *
	 * @author Jone
	 */
	public interface OnItemClickListener
	{
		public void onItemClicked(int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
		mOnItemClickListener = onItemClickListener;
	}

	/**
	 * 删除按钮点击监听器接口
	 *
	 * @author Jone
	 */
	public interface OnDelBtnClickListener
	{
		public void onDelBtnClicked(int position);
	}

	public void setOnDelBtnClickListener(OnDelBtnClickListener onDeleteBtnClickListener)
	{
		mOnDelBtnClickListener = onDeleteBtnClickListener;
	}

}
