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
import edu.ustb.mirror.bean.FeedBean;
import edu.ustb.mirror.bean.LikeBean;

// 喜爱者列表
public class FeedListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<FeedBean> beans;
	private Context context;

	public FeedListAdapter(Context context, ArrayList<FeedBean> beans) {
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
			convertView = inflater.inflate(R.layout.feed_item, null);
			holder = new ViewHolder();

			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.emotion = (ImageView) convertView.findViewById(R.id.emotion);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FeedBean bean = beans.get(position);
		holder.content.setText(bean.getContent());
		holder.date.setText(bean.getDate());
		int e = bean.getEmotion();

		switch (e) {
		case 1:
			holder.emotion.setImageResource(R.drawable.sogood_pushed_big);
			break;
		case 2:
			holder.emotion.setImageResource(R.drawable.good_pushed_big);
			break;
		case 3:
			holder.emotion.setImageResource(R.drawable.notbad_pushed_big);
			break;
		case 4:
			holder.emotion.setImageResource(R.drawable.sad_pushed_big);
			break;
		case 5:
			holder.emotion.setImageResource(R.drawable.sosad_pushed_big);
			break;
		}

		return convertView;
	}

	private class ViewHolder {
		TextView content;
		TextView date;
		ImageView emotion;
	}
}