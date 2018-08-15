package com.hex.express.iwant.helper;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CitySelectBean;
import com.hex.express.iwant.bean.ProviceBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class CitySelectOperation extends AbsDbOperation{

	public  List<CitySelectBean>  selectDataFromDb(String sql,String column1,String column2) {
		List<CitySelectBean>  mLists = new ArrayList<CitySelectBean>();
		/**/
		List<DbModel> dbModels = null;
		try{
			dbModels = getDbManager().getContentDb().findDbModelAll(sql);
			for(DbModel mDbModel:dbModels){
				CitySelectBean mBeans = new CitySelectBean();
				mBeans.setName(mDbModel.getString(column1));
				mBeans.setCode(mDbModel.getString(column2));
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

	@Override
	public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
