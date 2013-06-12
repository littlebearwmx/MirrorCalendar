package edu.ustb.mirror.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import edu.ustb.mirror.R;
import edu.ustb.mirror.common.Global;

public class YearAdapter extends BaseAdapter {
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	private Context context;
	private String TAG = "FansListAdapter";
	private LayoutInflater inflater;
	private int[] beans = { R.drawable.jan, R.drawable.feb, R.drawable.march,
			R.drawable.april, R.drawable.may, R.drawable.jun, R.drawable.july,
			R.drawable.august, R.drawable.sep, R.drawable.oct,
			R.drawable.november, R.drawable.december };

	public YearAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.beans.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.beans[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView iv = new ImageView(this.context);
		iv.setImageResource(beans[position]);
		iv.setPadding(0, 20, 0, 0);

		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Global.INTENAL_ACTION_MONTH);
				intent.putExtra("month", position+1);
				context.sendBroadcast(intent);

				Log.i(TAG, "sendBroadcast!!!!!!!!!");
			}

		});
		return iv;
	}
}