package edu.ustb.mirror;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import edu.ustb.mirror.common.Global;
import edu.ustb.mirror.view.MyViewGroup;
import edu.ustb.mirror.view.MyViewGroup.IScrollListener;

public class MonthActivity extends Activity implements IScrollListener {
	private String TAG = "MonthActivity";
	private int CURRENT_MONTH = 0;
	private MyViewGroup viewGroup;

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	private int[] month_bgs = { R.drawable.jan_1, R.drawable.feb_1,
			R.drawable.march_1, R.drawable.april_1, R.drawable.may_1,
			R.drawable.june_1, R.drawable.july_1, R.drawable.august_1,
			R.drawable.sep_1, R.drawable.ouct_1, R.drawable.november_1,
			R.drawable.december_1, R.drawable.jan_1 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		if (intent.getExtras() != null) {
			int month = intent.getExtras().getInt("month");
			// Toast.makeText(this, "Month = " + month,
			// Toast.LENGTH_SHORT).show();
		}

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.SCREEN_WIDTH = dm.widthPixels;// 获取分辨率宽度
		this.SCREEN_HEIGHT = dm.heightPixels;

		viewGroup = new MyViewGroup(this);
		viewGroup.setScrollListener(this);
		setContentView(viewGroup);

		for (int i = 0; i < 13; i++) {
			this.addFooterMonth();
		}
	}

	@Override
	public void onScrollToNextCompleted() {
		// TODO Auto-generated method stub

		this.addFooterMonth();

	}

	@Override
	public void onScrollToPrevCompleted() {
		// TODO Auto-generated method stub
		this.addHeaderMonth();

	}

	private void addHeaderMonth() {
		viewGroup.addHeaderView(this.addMonth());
	}

	private void addFooterMonth() {
		viewGroup.addFooterView(this.addMonth());
	}

	private View addMonth() {
		View v1 = LayoutInflater.from(this).inflate(R.layout.activity_month,
				null);

		ImageView iv = (ImageView) v1.findViewById(R.id.month_bg);
		iv.setBackgroundResource(this.month_bgs[this.CURRENT_MONTH % 13]);

		this.CURRENT_MONTH++;
		return v1;
	}

	public void start_activity(View v) {
		if (v.getId() == R.id.month_bg) {
			Intent intent = new Intent(Global.INTENAL_ACTION_DAY);
			intent.putExtra("day", 1);
			sendBroadcast(intent);
		}

	}
}
