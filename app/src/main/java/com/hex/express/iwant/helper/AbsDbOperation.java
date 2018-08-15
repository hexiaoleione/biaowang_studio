package com.hex.express.iwant.helper;

import com.hex.express.iwant.iWantApplication;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public abstract class AbsDbOperation implements IDbOperation {

	protected DbManager getDbManager() {
		return iWantApplication.getInstance().mDbManager;
	}

	@Override
	public boolean saveData(EntityBase mEntity) {

		try {
			getDbManager().getContentDb().save(mEntity);
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	public abstract String getDbName();

	@Override
	public boolean deleteDataFromDb(String sql) {
		try {
			getDbManager().getContentDb().execNonQuery(sql);
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	@Override
	public boolean updateDataFromDb(String sql) {
		try {
			getDbManager().getContentDb().execNonQuery(sql);
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	public DbManager getDBDbManager() {
		return getDbManager();
	}

	public void clearDbData() {
		try {
			getDbManager().getContentDb().execNonQuery(
					"delete from " + getDbName());
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void insertOrUpdate(EntityBase mUser, WhereBuilder mWhereBuilder) {
		boolean isInsertSuccess = saveData(mUser);
		if (!isInsertSuccess) {
			try {
				getDBDbManager().getContentDb().update(mUser, mWhereBuilder);
			} catch (DbException e) {
			}
		}
	}
}
