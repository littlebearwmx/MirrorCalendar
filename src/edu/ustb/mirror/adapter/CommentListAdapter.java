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
import edu.ustb.mirror.bean.CommentBean;
import edu.ustb.mirror.bean.FeedBean;

// 喜爱者列表
public class CommentListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<CommentBean> beans;
	private Context context;

	public CommentListAdapter(Context context, ArrayList<CommentBean> beans) {
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
			convertView = inflater.inflate(R.layout.comment_item, null);
			holder = new ViewHolder();

			holder.username = (TextView) convertView
					.findViewById(R.id.username);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.date = (TextView) convertView.findViewById(R.id.dateline);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CommentBean bean = beans.get(position);
		holder.username.setText(bean.getUsername());
		holder.content.setText(bean.getContent());
		holder.date.setText(bean.getDate());

		return convertView;
	}

	private class ViewHolder {
		TextView username;
		TextView content;
		TextView date;
	}
}