package edu.ustb.mirror;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.ustb.mirror.adapter.CommentListAdapter;
import edu.ustb.mirror.bean.CommentBean;

public class DetailActivity extends Activity {
	private int CURRENT_DETAIL_TAB;
	private ImageView detail_tab_cursor; // 滑动游标图片
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private LinearLayout commentlist;
	ArrayList<CommentBean> beans = new ArrayList<CommentBean>();
	private CommentListAdapter commentlistadapter;

	private String[] des = {
			"1818年马克思，德国政治家，哲学家诞辰。卡尔 海因里希 马克思，早期在中国被译为麦喀士，马克思主义创始人。他的观点在社会科学和社会政治运动的发展中扮演的重要的角色，他是近代共产主义运动、无产阶级的精神领袖。\n 1818年马克思，德国政治家，哲学家诞辰。卡尔 海因里希 马克思，早期在中国被译为麦喀士，马克思主义创始人。他的观点在社会科学和社会政治运动的发展中扮演的重要的角色，他是近代共产主义运动、无产阶级的精神领袖。",
			"肯尼亚航空507号班机是肯尼亚航空公司（以下简称“肯尼亚航空”）一架编号为KQ507的班机，由喀麦隆杜阿拉的杜阿拉国际机场飞往肯尼亚内罗毕乔莫·肯雅塔国际机场。2007年5月5日，一架波音737-800型的客机执行此航班，飞机只有半年机龄，搭载来自27个不同国家，总共105名乘客及9名机组员，于杜阿拉国际机场起飞后不久即坠毁，机上人员全数罹难。" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.SCREEN_WIDTH = dm.widthPixels;// 获取分辨率宽度
		this.SCREEN_HEIGHT = dm.heightPixels;

		String from = "date";
		Intent intent = this.getIntent();
		if (intent.getExtras() != null) {
			from = intent.getExtras().getString("from");
		}

		//
		TextView tv = (TextView) this.findViewById(R.id.header_txt);
		TextView about = (TextView) this.findViewById(R.id.about_page);
		ImageView iv = (ImageView) this.findViewById(R.id.detail_img);

		android.view.ViewGroup.LayoutParams params = iv.getLayoutParams();
		params.width = this.SCREEN_WIDTH;
		params.height = this.SCREEN_WIDTH;
		iv.setLayoutParams(params);

		if (from.equals("date")) {
		} else if (from.equals("person")) {
			tv.setText("人物");
			about.setText(des[0]);
			iv.setBackgroundResource(R.drawable.person_img);
		} else {
			tv.setText("事件");
			about.setText(des[1]);
			iv.setBackgroundResource(R.drawable.event_img);
		}

		detail_tab_cursor = (ImageView) this.findViewById(R.id.detail_cursor);
		// 提前获取View 宽高
		final View vv = this.findViewById(R.id.detail_btn_group);
		ViewTreeObserver vto2 = vv.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				vv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int width = vv.getWidth();

				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) detail_tab_cursor
						.getLayoutParams();
				linearParams.width = width / 3;
				detail_tab_cursor.setLayoutParams(linearParams);

			}
		});

		View v = this.findViewById(R.id.about_btn);
		this.CURRENT_DETAIL_TAB = v.getId();

		// ////////////////////////
		commentlist = (LinearLayout) this.findViewById(R.id.comment_list);

		for (int i = 0; i < 15; i++) {
			View v1 = LayoutInflater.from(this).inflate(R.layout.comment_item,
					null);

			commentlist.addView(v1);
		}
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

	// Tab
	public void detailTabClick(View v) {
		if (this.CURRENT_DETAIL_TAB != v.getId()) {

			// 旧BTN
			TextView old = (TextView) this
					.findViewById(this.CURRENT_DETAIL_TAB);
			old.setTextColor(this.getResources().getColor(R.color.gray_txt));

			// Do something
			View g = this.findViewById(R.id.detail_btn_group);
			int i1 = this.getNumByDetailId(this.CURRENT_DETAIL_TAB);
			int i2 = this.getNumByDetailId(v.getId());

			int fromX = (2 * i1 - 1) * g.getWidth() / 6;
			int toX = fromX + (i2 - i1) * g.getWidth() / 3;
			int fix = this.detail_tab_cursor.getWidth() / 2;

			Animation animation = new TranslateAnimation(fromX - fix,
					toX - fix, 0, 0);

			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(250);
			detail_tab_cursor.startAnimation(animation);

			this.showDetailPage(i2);

			// New
			((TextView) v).setTextColor(this.getResources().getColor(
					R.color.yellow_txt));

			this.CURRENT_DETAIL_TAB = v.getId();
		}
	}

	// ///////////////////
	private int getNumByDetailId(int id) {
		switch (id) {
		case R.id.about_btn:
			return 1;
		case R.id.comment_btn:
			return 2;
		case R.id.relative_btn:
			return 3;
		default:
			return 1;
		}
	}

	private void showDetailPage(int page) {
		if (page == 1) {
			this.findViewById(R.id.about_page).setVisibility(View.VISIBLE);
			this.findViewById(R.id.comment_page).setVisibility(View.GONE);
			this.findViewById(R.id.relative_page).setVisibility(View.GONE);

		} else if (page == 2) {
			this.findViewById(R.id.about_page).setVisibility(View.GONE);
			this.findViewById(R.id.comment_page).setVisibility(View.VISIBLE);
			this.findViewById(R.id.relative_page).setVisibility(View.GONE);

		} else {
			this.findViewById(R.id.about_page).setVisibility(View.GONE);
			this.findViewById(R.id.comment_page).setVisibility(View.GONE);
			this.findViewById(R.id.relative_page).setVisibility(View.VISIBLE);

		}
	}

	// /////////////////////
	public void addComment(View view) {
		final Dialog dialog = new Dialog(this, R.style.comment_dlg);
		// 设置它的ContentView

		LinearLayout v1 = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.comment_dlg, null);

		ImageButton ok = (ImageButton) v1.findViewById(R.id.comment_ok_btn);
		ImageButton cancel = (ImageButton) v1
				.findViewById(R.id.comment_cancel_btn);
		final EditText content = (EditText) v1
				.findViewById(R.id.comment_content);

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		});

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!content.getText().toString().equals("")) {

					View comment = LayoutInflater.from(DetailActivity.this)
							.inflate(R.layout.comment_item, null);

					((TextView) comment.findViewById(R.id.content))
							.setText(content.getText().toString());

					DetailActivity.this.commentlist.addView(comment, 0);

				}

				dialog.dismiss();
			}

		});

		dialog.setContentView(v1);

		dialog.show();
	}

	// /////////////////////
	public void login(View view) {
		final Dialog dialog = new Dialog(this, R.style.comment_dlg);
		// 设置它的ContentView

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();

		dialogWindow.setGravity(Gravity.BOTTOM);

		LinearLayout v1 = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.login_dlg, null);

		ImageButton renren = (ImageButton) v1.findViewById(R.id.renren_btn);
		ImageButton weibo = (ImageButton) v1.findViewById(R.id.weibo_btn);

		renren.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		});

		weibo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}

		});

		dialog.setContentView(v1);

		dialog.show();
	}

}
