package com.hex.express.iwant.helper;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.ProviceBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class CityDbOperation extends AbsDbOperation{

	@Override
	public  List<CityBean>  selectDataFromDb(String sql) {
		List<CityBean>  mLists = new ArrayList<CityBean>();
		/**/
		List<DbModel> dbModels = null;
		try{
			dbModels = getDbManager().getContentDb().findDbModelAll(sql);
			for(DbModel mDbModel:dbModels){
				CityBean mBeans = new CityBean();
				mBeans.city_name = mDbModel.getString(CityBean.CITY_NAME);
				mBeans.city_code = mDbModel.getString(CityBean.CITY_CODE);
				mBeans.param = mDbModel.getString(CityBean.PARAM);
				mBeans.pro_code = mDbModel.getString(CityBean.PRO_CODE);
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
		return ProviceBean.DBNAME;
	}

}
