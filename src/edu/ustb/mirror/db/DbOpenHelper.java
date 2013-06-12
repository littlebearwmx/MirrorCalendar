package edu.ustb.mirror.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {
	private String TAG = "DBOPENHELPER";
	private static final String DATABASE_NAME = "mirror.db";
	private static final int DATABASE_VERSION = 12;

	// 更新时间
	public static final String FEED_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS feed (_id INTEGER PRIMARY KEY AUTOINCREMENT,  content TEXT, dateline TEXT, emotion INTEGER)";
	public static final String FEED_TABLE_DROP = "DROP TABLE IF EXISTS feed";

	public DbOpenHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DbOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(TAG, "CREATE DB");
		db.execSQL(FEED_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(FEED_TABLE_DROP);
		this.onCreate(db);
	}

}
