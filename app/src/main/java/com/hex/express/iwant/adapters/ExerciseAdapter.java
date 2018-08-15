package com.hex.express.iwant.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.adapters.MessageAdapter.MessageViewHolder;
import com.hex.express.iwant.bean.ExerciseBean;
import com.hex.express.iwant.bean.MessageBean;
import com.hex.express.iwant.utils.MyBitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

public class ExerciseAdapter extends BaseListAdapter{

	public ExerciseAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(View itemView) {
		// TODO Auto-generated method stub
		return new ExecViewHolder(itemView);
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.item_newexeci;//  item_execise  item_newexecise
	}
	class ExecViewHolder extends ViewHolder{

		public ExecViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void setData(int position) {
			// TODO Auto-generated method stub
			super.setData(position);
			ExerciseBean bean=new ExerciseBean();
			bean.data=list;
//			img_title.seti("   "+bean.getData().get(position).imagesUrl);
			if (!bean.data.get(position).imagesUrl.equals("")) {
			new MyBitmapUtils().display(img_title, bean.data.get(position).imagesUrl);
//				Bitmap bitmap = getHttpBitmap(bean.data.get(position).imagesUrl);
//				img_title.setImageBitmap(returnBitMap(bean.data.get(position).imagesUrl));
			img_title.setBackgroundDrawable(null);
		}else {
//			iv_imgOfGood.setBackgroundResource(R.drawable.shunfengm);
		}   
		}
//		@Bind(R.id.exec_substance)
//		TextView exec_substance;
//		@Bind(R.id.img_lefts)
//		ImageView img_lefts;
		@Bind(R.id.img_title)
		ImageView img_title;
		
	}
	/* * 获取网落图片资源 
	     * @param url
	     * @return
	     */
	    public static Bitmap getHttpBitmap(String url){
	    	URL myFileURL;
	    	Bitmap bitmap=null;
	    	try{
	    		myFileURL = new URL(url);
	    		//获得连接
	    		HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
	    		//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
	    		conn.setConnectTimeout(6000);
	    		//连接设置获得数据流
	    		conn.setDoInput(true);
	    		//不使用缓存
	    		conn.setUseCaches(false);
	    		//这句可有可无，没有影响
	    		//conn.connect();
	    		//得到数据流
	    		InputStream is = conn.getInputStream();
	    		//解析得到图片
	    		bitmap = BitmapFactory.decodeStream(is);
	    		//关闭数据流
	    		is.close();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	
			return bitmap;
	    	
	    }
	    public Bitmap returnBitMap(String url) { 
	    	URL myFileUrl = null; 
	    	Bitmap bitmap = null; 
	    	try { 
	    	myFileUrl = new URL(url); 
	    	} catch (MalformedURLException e) { 
	    	e.printStackTrace(); 
	    	} 
	    	try { 
	    	HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); 
	    	conn.setDoInput(true); 
	    	conn.connect(); 
	    	InputStream is = conn.getInputStream(); 
	    	bitmap = BitmapFactory.decodeStream(is); 
	    	is.close(); 
	    	} catch (IOException e) { 
	    	e.printStackTrace(); 
	    	} 
	    	return bitmap; 
	    	} 
}
