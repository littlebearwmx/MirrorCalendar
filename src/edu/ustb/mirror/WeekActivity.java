package edu.ustb.mirror;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.ustb.mirror.adapter.WeekAdapter;
import edu.ustb.mirror.common.Global;
import edu.ustb.mirror.view.MyViewGroup;
import edu.ustb.mirror.view.MyViewGroup.IScrollListener;

public class WeekActivity extends Activity implements IScrollListener {
	private String TAG = "WeekActivity";
	private int MAX_WEEK = 20;
	private int MIN_WEEK = 16;
	private MyViewGroup viewGroup;

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.SCREEN_WIDTH = dm.widthPixels;// 获取分辨率宽度
		this.SCREEN_HEIGHT = dm.heightPixels;

		viewGroup = new MyViewGroup(this);
		viewGroup.setScrollListener(this);
		setContentView(viewGroup);

		for (int i = 0; i < 11; i++) {
			this.addFooterWeek();
		}

	}

	@Override
	public void onScrollToNextCompleted() {
		// TODO Auto-generated method stub
		this.addFooterWeek();

	}

	@Override
	public void onScrollToPrevCompleted() {
		// TODO Auto-generated method stub
		this.addHeaderWeek();

	}

	private void addHeaderWeek() {
		viewGroup.addHeaderView(this.addWeek());
	}

	private void addFooterWeek() {

		viewGroup.addFooterView(this.addWeek());
	}

	private View addWeek() {
		LinearLayout v1 = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.activity_week, null);
		((TextView) v1.findViewById(R.id.week_number)).setText("第"
				+ this.MAX_WEEK++ + "周 ");

		ListView g1 = (ListView) v1.findViewById(R.id.week_list);
		g1.setAdapter(new WeekAdapter(this, new int[] { 1, 2, 3, 4, 5, 6, 7 }));

		g1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Global.INTENAL_ACTION_DAY);
				intent.putExtra("day", arg2);
				sendBroadcast(intent);
			}

		});

		g1.setLayoutParams(new LinearLayout.LayoutParams(this.SCREEN_WIDTH,
				this.SCREEN_HEIGHT));

		return v1;
	}

}