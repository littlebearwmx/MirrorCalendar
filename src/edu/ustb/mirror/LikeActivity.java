package edu.ustb.mirror;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ustb.mirror.adapter.LikeListAdapter;
import edu.ustb.mirror.bean.LikeBean;

public class LikeActivity extends Activity {

	private ListView list;
	ArrayList<LikeBean> beans = new ArrayList<LikeBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_like);

		list = (ListView) this.findViewById(R.id.like_list);

		String[] people = { "爱因斯坦", "居里夫人", "叔本华", "林肯", "邓小平" };
		String[] dates = { "爱牙日", "无烟日", "情人节", "元宵节", "端午节" };
		String[] event = { "香港回归", "二战结束", "原子弹爆炸", "克隆人出生", "人类登月" };

		for (int i = 0; i < 15; i++) {
			LikeBean bean = new LikeBean();

			if (i % 3 == 0) {
				bean.setTitle("人物");
				bean.setDescription(people[i / 3]);
				bean.setDate("2013/06/12");
			} else if (i % 3 == 1) {
				bean.setTitle("日子");
				bean.setDescription(dates[(i - 1) / 3]);
				bean.setDate("2012/05/13");
			} else {
				bean.setTitle("事件");
				bean.setDescription(event[(i - 2) / 3]);
				bean.setDate("2011/04/14");
			}

			bean.setPid(R.drawable.feb);

			beans.add(bean);
		}
		list.setAdapter(new LikeListAdapter(this, beans));

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LikeActivity.this.startActivity(new Intent(LikeActivity.this,
						DetailActivity.class));
				LikeActivity.this.overridePendingTransition(
						R.anim.slide_in_from_bottom, android.R.anim.fade_out);
			}

		});
	}

	public void start_activity(View view) {
		if (view.getId() == R.id.header_back_btn) {
			this.onBackPressed();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		this.overridePendingTransition(R.anim.slide_in_from_left,
				R.anim.slide_out_to_right);
	}

}
