package edu.ustb.mirror;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.ustb.mirror.adapter.YearAdapter;
import edu.ustb.mirror.common.Global;
import edu.ustb.mirror.view.MyViewGroup;
import edu.ustb.mirror.view.MyViewGroup.IScrollListener;

public class YearActivity extends Activity implements IScrollListener {
	private String TAG = "YearActivity";

	private MyViewGroup viewGroup;
	private int MAX_YEAR = 2015;
	private int MIN_YEAR = 2011;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewGroup = new MyViewGroup(this);
		viewGroup.setScrollListener(this);
		setContentView(viewGroup);

		for (int i = 0; i < 21; i++) {
			this.addFooterYear();
		}

	}

	@Override
	public void onScrollToNextCompleted() {
		// TODO Auto-generated method stub

		this.addFooterYear();

	}

	@Override
	public void onScrollToPrevCompleted() {
		// TODO Auto-generated method stub
		this.addHeaderYear();

	}

	private void addHeaderYear() {
		LinearLayout v1 = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.activity_year, null);
		((TextView) v1.findViewById(R.id.year)).setText("" + this.MIN_YEAR--);
		GridView g1 = (GridView) v1.findViewById(R.id.month_grid);
		g1.setAdapter(new YearAdapter(this));

		viewGroup.addHeaderView(v1);
	}

	private void addFooterYear() {
		LinearLayout v1 = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.activity_year, null);
		((TextView) v1.findViewById(R.id.year)).setText("" + this.MAX_YEAR++);
		GridView g1 = (GridView) v1.findViewById(R.id.month_grid);
		g1.setAdapter(new YearAdapter(this));

		viewGroup.addFooterView(v1);
	}

}
