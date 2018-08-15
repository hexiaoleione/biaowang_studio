package com.hex.express.iwant.helper;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.ProviceBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class AreaDboperation extends AbsDbOperation{

	@Override
	public  List<AreaBean>  selectDataFromDb(String sql) {
		List<AreaBean>  mLists = new ArrayList<AreaBean>();
		/**/
		List<DbModel> dbModels = null;
		try{
			dbModels = getDbManager().getContentDb().findDbModelAll(sql);
			for(DbModel mDbModel:dbModels){
				AreaBean mBeans = new AreaBean();
				mBeans.area_code = mDbModel.getString(AreaBean.AREA_CODE);
//				mBeans.city_code = mDbModel.getString(CityBean.CITY_CODE);
//				mBeans.param = mDbModel.getString(CityBean.PARAM);
//				mBeans.pro_code = mDbModel.getString(CityBean.PRO_CODE);
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

