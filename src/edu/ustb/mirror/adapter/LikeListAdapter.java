package edu.ustb.mirror.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ustb.mirror.R;
import edu.ustb.mirror.bean.LikeBean;

// 喜爱者列表
public class LikeListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<LikeBean> beans;
	private Context context;

	public LikeListAdapter(Context context, ArrayList<LikeBean> beans) {
		this.inflater = LayoutInflater.from(context);
		this.beans = beans;
		this.context = context;
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.like_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.description = (TextView) convertView
					.findViewById(R.id.description);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final LikeBean bean = beans.get(position);
		holder.title.setText(bean.getTitle());
		holder.description.setText(bean.getDescription());
		holder.date.setText(bean.getDate());
		holder.pic.setImageResource(bean.getPid());

		return convertView;
	}

	private class ViewHolder {
		TextView title;
		TextView description;
		TextView date;
		ImageView pic;
	}
}