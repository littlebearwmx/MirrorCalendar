package edu.ustb.mirror;

import java.util.HashMap;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import edu.ustb.mirror.common.Global;

public class TabhostActivity extends TabActivity {

	private TabHost mHost;
	protected static final int MSG_WHAT_NEW_VERSION = 0;
	private String DEBUG_TAG = "TabhostActivity";
	private HashMap<Integer, HashMap<String, Comparable>> tabs;
	private int CURRENT_TAB;
	private int FOOTER_HEIGHT;

	private TabSpec[] tabspecs = { null, null, null, null };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tabhost);

		HashMap<String, Comparable> tab1 = new HashMap<String, Comparable>();
		tab1.put("tag", "product_tab");
		tab1.put("nor_icon", R.drawable.day_nor);
		tab1.put("push_icon", R.drawable.day_pushed);

		HashMap<String, Comparable> tab2 = new HashMap<String, Comparable>();
		tab2.put("tag", "feed_tab");
		tab2.put("nor_icon", R.drawable.week_nor);
		tab2.put("push_icon", R.drawable.week_pushed);

		HashMap<String, Comparable> tab3 = new HashMap<String, Comparable>();
		tab3.put("tag", "message_tab");
		tab3.put("nor_icon", R.drawable.month_nor);
		tab3.put("push_icon", R.drawable.month_pushed);

		HashMap<String, Comparable> tab4 = new HashMap<String, Comparable>();
		tab4.put("tag", "more_tab");
		tab4.put("nor_icon", R.drawable.year_nor);
		tab4.put("push_icon", R.drawable.year_pushed);

		tabs = new HashMap<Integer, HashMap<String, Comparable>>();
		tabs.put(R.id.product_tab_btn, tab1);
		tabs.put(R.id.feed_tab_btn, tab2);
		tabs.put(R.id.message_tab_btn, tab3);
		tabs.put(R.id.more_tab_btn, tab4);

		// Init Tab
		if (savedInstanceState != null) {
			CURRENT_TAB = savedInstanceState.getInt("CURRENT_TAB");

			View product_tab_btn = this.findViewById(R.id.product_tab_btn);
			View feed_tab_btn = this.findViewById(R.id.feed_tab_btn);
			View message_tab_btn = this.findViewById(R.id.message_tab_btn);
			View more_tab_btn = this.findViewById(R.id.more_tab_btn);

			if (CURRENT_TAB == product_tab_btn.getId()) {
				product_tab_btn.setBackgroundResource((Integer) ((HashMap) tabs
						.get(product_tab_btn.getId())).get("push_icon"));

			} else if (CURRENT_TAB == feed_tab_btn.getId()) {
				feed_tab_btn.setBackgroundResource((Integer) ((HashMap) tabs
						.get(feed_tab_btn.getId())).get("push_icon"));

			} else if (CURRENT_TAB == message_tab_btn.getId()) {
				message_tab_btn.setBackgroundResource((Integer) ((HashMap) tabs
						.get(message_tab_btn.getId())).get("push_icon"));

			} else if (CURRENT_TAB == more_tab_btn.getId()) {
				more_tab_btn.setBackgroundResource((Integer) ((HashMap) tabs
						.get(more_tab_btn.getId())).get("push_icon"));

			}

		} else {
			View v = this.findViewById(R.id.product_tab_btn);
			v.setBackgroundResource((Integer) ((HashMap) tabs.get(v.getId()))
					.get("push_icon"));

			this.CURRENT_TAB = v.getId();
		}

		this.mHost = this.getTabHost();

		this.tabspecs[0] = mHost.newTabSpec("product_tab")
				.setIndicator("product_tab")
				.setContent(new Intent(this, DayActivity.class));

		this.tabspecs[1] = mHost.newTabSpec("feed_tab")
				.setIndicator("feed_tab")
				.setContent(new Intent(this, WeekActivity.class));

		this.tabspecs[2] = mHost.newTabSpec("message_tab")
				.setIndicator("message_tab")
				.setContent(new Intent(this, MonthActivity.class));

		this.tabspecs[3] = mHost.newTabSpec("more_tab")
				.setIndicator("more_tab")
				.setContent(new Intent(this, YearActivity.class));

		for (int i = 0; i < 4; i++) {
			mHost.addTab(this.tabspecs[i]);
		}

		this.getSharedPreferences(Global.PREF_ACCOUNT, MODE_PRIVATE);

		// 动态注册广播消息
		registerReceiver(month_broadcastreceiver, new IntentFilter(
				Global.INTENAL_ACTION_MONTH));
		registerReceiver(day_broadcastreceiver, new IntentFilter(
				Global.INTENAL_ACTION_DAY));
	}

	// Tab
	public void tab_click(View v) {

		if (this.CURRENT_TAB != v.getId()) {
			this.toggleBtn(v);
			// Tabhost
			mHost.setCurrentTabByTag((String) ((HashMap) tabs.get(v.getId()))
					.get("tag"));

		}
	}

	private void toggleBtn(View v) {
		// 旧BTN
		View old = this.findViewById(this.CURRENT_TAB);
		old.setBackgroundResource((Integer) ((HashMap) tabs.get(old.getId()))
				.get("nor_icon"));

		// New
		v.setBackgroundResource((Integer) ((HashMap) tabs.get(v.getId()))
				.get("push_icon"));

		this.CURRENT_TAB = v.getId();
	}

	public void toggleTabhost(View v) {
		View tab = this.findViewById(R.id.tab_btns);
		int h = tab.getHeight();
		LinearLayout.LayoutParams params = (LayoutParams) tab.getLayoutParams();
		if (h > 0) {
			this.FOOTER_HEIGHT = params.height;
			params.height = 0;
			tab.setLayoutParams(params);
		} else {
			params.height = this.FOOTER_HEIGHT;
			tab.setLayoutParams(params);
		}

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save away the original text, so we still have it if the activity
		// needs to be killed while paused.
		savedInstanceState.putInt("CURRENT_TAB", CURRENT_TAB);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// 用于解决activity中有ViewFlipper不响应
		getCurrentActivity().onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	private BroadcastReceiver month_broadcastreceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 如果捕捉到的action是INTENAL_ACTION_3
			if (Global.INTENAL_ACTION_MONTH.equals(action)) {
				// 当未知Intent包含的内容，则需要通过以下方法来列举
				View v = TabhostActivity.this
						.findViewById(R.id.message_tab_btn);
				toggleBtn(v);

				Intent i1 = new Intent(TabhostActivity.this,
						MonthActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.putExtra("month", intent.getExtras().getInt("month"));
				tabspecs[2].setContent(i1);
				mHost.setCurrentTabByTag((String) ((HashMap) tabs
						.get(R.id.message_tab_btn)).get("tag"));
			}
		}
	};

	private BroadcastReceiver day_broadcastreceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 如果捕捉到的action是INTENAL_ACTION_3
			if (Global.INTENAL_ACTION_DAY.equals(action)) {
				// 当未知Intent包含的内容，则需要通过以下方法来列举
				View v = TabhostActivity.this
						.findViewById(R.id.product_tab_btn);
				toggleBtn(v);

				Intent i1 = new Intent(TabhostActivity.this, DayActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.putExtra("day", intent.getExtras().getInt("day"));
				tabspecs[0].setContent(i1);
				mHost.setCurrentTabByTag((String) ((HashMap) tabs
						.get(R.id.product_tab_btn)).get("tag"));
			}
		}
	};

}