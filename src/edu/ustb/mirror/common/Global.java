package edu.ustb.mirror.common;

import android.os.Environment;
import edu.ustb.mirror.R;

//全局常量
public abstract class Global {
	// DEBUG
	public static boolean DEBUG = false;

	// 后台获取消息的间隔
	public static int MESSAGE_SERVICE_DURATION = DEBUG ? 5000 : 300000;

	// Preferences
	public static String PREF_ACCOUNT = "account";

	// 举报类型
	public static int REPORT_USER = 1; // 举报用户

	// 内容来源
	public static int FROM_ANDROID = 1; // 安卓

	// Once Vibrator Pattern
	public static long[] CLICK_VIBRATOR_PATTERN = new long[] { 50, 30 };
	public static long[] LONG_CLICK_VIBRATOR_PATTERN = new long[] { 50, 30 };
	public static long[] MESSAGE_VIBRATOR_PATTERN = new long[] { 50, 100 };

	public static String INTENAL_ACTION_MONTH = "edu.ustb.mirror.month";
	public static String INTENAL_ACTION_WEEK = "edu.ustb.mirror.week";
	public static String INTENAL_ACTION_DAY = "edu.ustb.mirror.day";

}
