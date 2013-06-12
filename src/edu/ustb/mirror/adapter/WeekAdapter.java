package edu.ustb.mirror.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ustb.mirror.R;

public class WeekAdapter extends BaseAdapter {
	// private final ImageDownloader imageDownloader;
	private LayoutInflater inflater;
	private int[] beans;

	public WeekAdapter(Context context, int[] beans) {
		// imageDownloader = new ImageDownloader(context);
		this.inflater = LayoutInflater.from(context);
		this.beans = beans;
	}

	@Override
	public int getCount() {
		return beans.length;
	}

	@Override
	public Object getItem(int position) {
		return beans[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int[] icons = { R.drawable.week_1, R.drawable.week_2,
				R.drawable.week_3, R.drawable.week_4, R.drawable.week_5,
				R.drawable.week_6, R.drawable.week_7 };

		String[] names = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

		View v = inflater.inflate(R.layout.week_item, null);
		TextView tv = (TextView) v.findViewById(R.id.week_item);
		TextView name = (TextView) v.findViewById(R.id.week_name);
		name.setText(names[position % 7]);

		ImageView iv = (ImageView) v.findViewById(R.id.day_icon);
		iv.setImageResource(icons[position % 7]);
		if (position % 2 == 0) {
			v.findViewById(R.id.the_day_name).setVisibility(View.INVISIBLE);
		}
		int bean = beans[position];

		tv.setText("0" + bean);

		return v;

	}
}
