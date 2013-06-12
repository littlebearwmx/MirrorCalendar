package edu.ustb.mirror.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Model {
	protected DbOpenHelper helper;
	protected SQLiteDatabase db;

	public Model(Context context) {
		helper = new DbOpenHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化Model的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	// 用户注销清空相关数据库
	public void clear() {

		db.execSQL(DbOpenHelper.FEED_TABLE_DROP);

		db.execSQL(DbOpenHelper.FEED_TABLE_CREATE);

		db.close();
	}
}
