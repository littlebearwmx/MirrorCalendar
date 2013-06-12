package edu.ustb.mirror.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import edu.ustb.mirror.bean.FeedBean;

public class FeedModel extends Model {

	private static final String TB_NAME = "feed";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_DATELINE = "dateline";
	public static final String COLUMN_emotion = "emotion";

	public FeedModel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void add(String content, String dateline, int emotion) {
		db.execSQL("INSERT INTO feed VALUES(null, ?, ?, ?)", new Object[] {
				content, dateline, emotion });
		this.close();
	}

	public Cursor query(String limit) {
		return this.db.query(true, TB_NAME, null, null, null, null, null,
				COLUMN_DATELINE + " DESC", limit);
	}

	public ArrayList<FeedBean> findAll() {
		ArrayList<FeedBean> beans = new ArrayList<FeedBean>();

		Cursor c = this.db.query(true, TB_NAME, null, null, null, null, null,
				COLUMN_DATELINE + " DESC", null);

		c.moveToFirst(); // 重中之重，千万不能忘了
		while (!c.isAfterLast()) {

			FeedBean feed = new FeedBean();
			feed.setContent(c.getString(1));
			feed.setDate(c.getString(2));
			feed.setEmotion(c.getInt(3));

			beans.add(feed);

			c.moveToNext();
		}
		this.db.close();

		return beans;
	}

	public void close() {
		this.db.close();
	}
}
