package com.hex.express.iwant.helper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.hex.express.iwant.bean.kuaidiBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class KuaiDiDbOperation extends AbsDbOperation{

	@SuppressWarnings("unchecked")
	@Override
	public  List<kuaidiBean> selectDataFromDb(String sql) {
		List<kuaidiBean>  mLists = new ArrayList<kuaidiBean>();
		/***/
		List<DbModel> dbModels = null;
		try{
			dbModels = getDbManager().getContentDb().findDbModelAll(sql);
			for(DbModel mDbModel:dbModels){
				kuaidiBean mBeans = new kuaidiBean();
				mBeans.expCode = mDbModel.getString(kuaidiBean.EXPCODE);
				mBeans.expName = mDbModel.getString(kuaidiBean.EXPNAME);
				mBeans.nameWord = mDbModel.getString(kuaidiBean.NAMEWORD);
				mBeans.favorite = mDbModel.getString(kuaidiBean.FAVORITE);
				mBeans.expId = mDbModel.getInt(kuaidiBean.EXPID);
				Log.e("LLLLLLLLLLLLL", mDbModel.getInt(kuaidiBean.EXPID)+"");
				mBeans.orderId=mDbModel.getInt(kuaidiBean.SHOWORDER);
				mLists.add(mBeans);
			}
		}catch(DbException e){ 
		}finally{
			if(dbModels!=null){
				dbModels.clear();
				dbModels =null;
			}
		}
		return mLists;
	}

	@Override
	public String getDbName() {
		return kuaidiBean.DB_NAME;
	}
}
