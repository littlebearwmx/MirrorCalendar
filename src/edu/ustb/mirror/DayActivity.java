package edu.ustb.mirror;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import edu.ustb.mirror.adapter.FeedListAdapter;
import edu.ustb.mirror.bean.FeedBean;
import edu.ustb.mirror.db.FeedModel;
import edu.ustb.mirror.util.DateUtil;
import edu.ustb.mirror.view.MyViewGroup;
import edu.ustb.mirror.view.MyViewGroup.IScrollListener;

public class DayActivity extends Activity implements IScrollListener {
	private String TAG = "DayActivity";
	private int RESULT_FROM_SETTING = 0;
	private int NUMBER = 0;

	private ViewFlipper viewFlipper = null;

	private HashMap<Integer, HashMap<String, Comparable>> headertabs;
	private int CURRENT_HEADER_TAB;
	private int CURRENT_PAGER_TAB;// 处于激活的tab
	private ImageView header_cursor; // 滑动游标图片
	private MyViewGroup viewGroup;
	private View mood;
	private ListView feedlist;
	ArrayList<FeedBean> feedbeans = new ArrayList<FeedBean>();
	private FeedListAdapter feedadapter;
	private EditText feedet;

	private HashMap<Integer, HashMap<String, Comparable>> facetabs;
	private int CURRENT_FACE_TAB;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private ImageView face_tab_cursor; // 滑动游标图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day);

		Intent intent = this.getIntent();
		if (intent.getExtras() != null) {
			int day = intent.getExtras().getInt("day");
			// Toast.makeText(this, "Day = " + day, Toast.LENGTH_SHORT).show();
		}

		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		this.mood = inflater.inflate(R.layout.mood, null);
		// ////////////////////////////////////
		viewGroup = new MyViewGroup(this);
		// //////////
		viewFlipper.addView(viewGroup);
		viewFlipper.addView(mood);
		// FaceTab
		initFaceTab();

		face_tab_cursor = (ImageView) this.findViewById(R.id.face_tab_cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.SCREEN_WIDTH = dm.widthPixels;// 获取分辨率宽度
		this.SCREEN_HEIGHT = dm.heightPixels;

		Animation animation = new TranslateAnimation(0,
				this.SCREEN_WIDTH / 10 - 9, 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(0);
		face_tab_cursor.startAnimation(animation);

		viewGroup.setScrollListener(this);

		for (int i = 0; i < 21; i++) {
			viewGroup.addFooterView(this.addDay());
		}

		feedlist = (ListView) this.findViewById(R.id.feed_list);
		feedet = (EditText) this.findViewById(R.id.feed_content);
		feedbeans = new FeedModel(this).findAll();

		if (feedbeans.size() > 0) {
			FeedBean tmp = feedbeans.get(0);
			if (DateUtil.getCurrentDateline().substring(0, 10)
					.equals(tmp.getDate())) {
				this.feedet.setHint("今天已记录啦~");
				this.feedet.setEnabled(false);
			}
		}

		feedadapter = new FeedListAdapter(this, feedbeans);
		feedlist.setAdapter(feedadapter);

		// HeaderTab
		initHeaderTab();

		header_cursor = (ImageView) this.findViewById(R.id.header_cursor);

		// 提前获取View 宽高
		final View vv = this.findViewById(R.id.header_btn_group);
		ViewTreeObserver vto2 = vv.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				vv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int width = vv.getWidth();

				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) header_cursor
						.getLayoutParams();
				linearParams.width = width / 3;
				header_cursor.setLayoutParams(linearParams);

			}
		});

	}

	@Override
	public void onScrollToNextCompleted() {
		// TODO Auto-generated method stub
		viewGroup.removeHeaderView();
		viewGroup.addFooterView(this.addDay());
	}

	@Override
	public void onScrollToPrevCompleted() {
		// TODO Auto-generated method stub
		viewGroup.removeFooterView();
		viewGroup.addHeaderView(this.addDay());

	}

	// ////////////////
	private View addDay() {
		LinearLayout v1 = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.day, null);
		LinearLayout date = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.date, null);
		LinearLayout person = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.person, null);
		LinearLayout event = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.event, null);

		// TextView tv = (TextView)
		// this.viewGroup.getCurrentView().findViewById(R.id.day_num);
		// int num = Integer.parseInt(tv.getText().toString());

		TextView tv2 = (TextView) date.findViewById(R.id.day_num);
		tv2.setText("" + (NUMBER++ + 1));

		ViewPager pager = (ViewPager) v1.findViewById(R.id.pager);

		View[] views = new View[] { date, person, event };

		pager.setAdapter(new DayPageAdapter(views));
		pager.setFocusable(true);
		pager.setOnPageChangeListener(new PageChangeListener(this));

		LayoutParams params = pager.getLayoutParams();
		params.width = this.SCREEN_WIDTH;
		params.height = this.SCREEN_HEIGHT;
		pager.setLayoutParams(params);

		if (this.CURRENT_HEADER_TAB == R.id.day_btn) {
			// pager.setCurrentItem(0);
		} else if (this.CURRENT_HEADER_TAB == R.id.person_btn) {
			pager.setCurrentItem(1);
		} else if (this.CURRENT_HEADER_TAB == R.id.event_btn) {
			pager.setCurrentItem(2);
		}

		return v1;
	}

	public void start_activity(View view) {
		if (view.getId() == R.id.more_btn) {
			this.startActivityForResult(new Intent(this, MoreActivity.class),
					this.RESULT_FROM_SETTING);
			this.overridePendingTransition(R.anim.slide_in_from_bottom,
					android.R.anim.fade_out);
		} else if (view.getId() == R.id.date_icon) {
			Intent in = new Intent(this, DetailActivity.class);
			String from = "date";
			if (this.CURRENT_HEADER_TAB == R.id.day_btn) {
				from = "date";
			} else if (this.CURRENT_HEADER_TAB == R.id.person_btn) {
				from = "person";
			} else if (this.CURRENT_HEADER_TAB == R.id.event_btn) {
				from = "event";
			}

			in.putExtra("from", from);
			this.startActivity(in);
			this.overridePendingTransition(R.anim.slide_in_from_bottom,
					android.R.anim.fade_out);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 退出
		if (requestCode == this.RESULT_FROM_SETTING) {
			if (resultCode == Activity.RESULT_OK) {
				if (data.getExtras().getBoolean("exit")) {
					this.finish();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// Tab
	public void headerTabClick(View v) {
		if (this.CURRENT_HEADER_TAB != v.getId()) {

			// Do something
			if (v.getId() == R.id.face_btn) {

				this.findViewById(R.id.header_cursor).setVisibility(
						View.INVISIBLE);

				Animation rInAnim = AnimationUtils.loadAnimation(this,
						R.anim.slide_in_from_left);
				Animation rOutAnim = AnimationUtils.loadAnimation(this,
						R.anim.slide_out_to_right);

				viewFlipper.setInAnimation(rInAnim);
				viewFlipper.setOutAnimation(rOutAnim);
				viewFlipper.showNext();

			} else {
				this.findViewById(R.id.header_cursor).setVisibility(
						View.VISIBLE);

				int i1 = 0;
				int i2 = 0;

				// 从FACE 回到主页
				if (this.CURRENT_HEADER_TAB == R.id.face_btn) {
					// 回到原位置
					if (v.getId() == this.CURRENT_PAGER_TAB) {
						i1 = this.getNumByHeaderId(v.getId());
					} else {// 回到其他tab
						i1 = this.getNumByHeaderId(this.CURRENT_PAGER_TAB);
					}

					Animation rInAnim = AnimationUtils.loadAnimation(this,
							R.anim.slide_in_from_right);
					Animation rOutAnim = AnimationUtils.loadAnimation(this,
							R.anim.slide_out_to_left);

					viewFlipper.setInAnimation(rInAnim);
					viewFlipper.setOutAnimation(rOutAnim);
					viewFlipper.showPrevious();
				} else {
					i1 = this.getNumByHeaderId(this.CURRENT_HEADER_TAB);
				}

				// ///////////////////////CURSOR
				View g = this.findViewById(R.id.header_btn_group);
				i2 = this.getNumByHeaderId(v.getId());

				int fromX = (2 * i1 - 1) * g.getWidth() / 6;
				int toX = fromX + (i2 - i1) * g.getWidth() / 3;
				int fix = this.header_cursor.getWidth() / 2;

				Animation animation = new TranslateAnimation(fromX - fix, toX
						- fix, 0, 0);

				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(250);

				View view = this.viewGroup.getCurrentView();
				ViewPager pager = (ViewPager) view.findViewById(R.id.pager);

				int tab = 0;
				if (v.getId() == R.id.day_btn) {
					pager.setCurrentItem(0);
				} else if (v.getId() == R.id.person_btn) {
					pager.setCurrentItem(1);
					tab = 1;
				} else if (v.getId() == R.id.event_btn) {
					pager.setCurrentItem(2);
					tab = 2;
				}

				for (int i = 0; i < this.viewGroup.getChildCount(); i++) {
					pager = (ViewPager) this.viewGroup.getChildAt(i)
							.findViewById(R.id.pager);
					pager.setCurrentItem(tab);
				}
				header_cursor.startAnimation(animation);
				this.CURRENT_PAGER_TAB = v.getId();
			}

			// 旧BTN
			View old = this.findViewById(this.CURRENT_HEADER_TAB);
			old.setBackgroundResource((Integer) ((HashMap) headertabs.get(old
					.getId())).get("nor_icon"));

			// New
			v.setBackgroundResource((Integer) ((HashMap) headertabs.get(v
					.getId())).get("push_icon"));

			this.CURRENT_HEADER_TAB = v.getId();
		}
	}

	// ///////////////////
	private int getNumByHeaderId(int id) {
		switch (id) {
		case R.id.day_btn:
			return 1;
		case R.id.person_btn:
			return 2;
		case R.id.event_btn:
			return 3;
		default:
			return 1;
		}
	}

	// Tab
	@SuppressLint("NewApi")
	public void faceTabClick(View v) {
		int resId = 0;

		if (v.getId() == R.id.sogood) {
			resId = R.drawable.sogood_anim;
		} else if (v.getId() == R.id.good) {
			resId = R.drawable.good_anim;
		} else if (v.getId() == R.id.notbad) {
			resId = R.drawable.notbad_anim;
		} else if (v.getId() == R.id.sad) {
			resId = R.drawable.sad_anim;
		} else if (v.getId() == R.id.sosad) {
			resId = R.drawable.sosad_anim;
		}

		Resources resources = this.getResources();

		Drawable btnDrawable = resources.getDrawable(resId);
		v.setBackground(btnDrawable);

		AnimationDrawable animationDrawable = (AnimationDrawable) v
				.getBackground();

		// 动画是否正在运行
		if (animationDrawable.isRunning()) {
			// 停止动画播放
			// animationDrawable.stop();
		} else {
			animationDrawable.invalidateSelf();
			// 开始或者继续动画播放
			animationDrawable.start();
		}

		if (this.CURRENT_FACE_TAB != v.getId()) {

			int i1 = this.getNumByFaceId(this.CURRENT_FACE_TAB);
			int i2 = this.getNumByFaceId(v.getId());

			int fromX = (2 * i1 - 1) * this.SCREEN_WIDTH / 10;
			int toX = fromX + (i2 - i1) * this.SCREEN_WIDTH / 5;

			int fix = this.face_tab_cursor.getWidth() / 2;

			// 旧BTN
			// ImageView old = (ImageView)
			// this.findViewById(this.CURRENT_FACE_TAB);
			// old.setImageResource((Integer) ((HashMap)
			// facetabs.get(old.getId())).get("nor_icon"));

			// Do something
			Animation animation = new TranslateAnimation(fromX - fix,
					toX - fix, 0, 0);

			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(250);
			face_tab_cursor.startAnimation(animation);

			// New
			// ((ImageView) v).setImageResource((Integer) ((HashMap)
			// facetabs.get(v.getId())).get("push_icon"));

			this.CURRENT_FACE_TAB = v.getId();

		}
	}

	// ///////////////////
	private int getNumByFaceId(int id) {
		switch (id) {
		case R.id.sogood:
			return 1;
		case R.id.good:
			return 2;
		case R.id.notbad:
			return 3;
		case R.id.sad:
			return 4;
		case R.id.sosad:
			return 5;
		default:
			return 1;
		}
	}

	// ////////////////////
	private void initHeaderTab() {
		HashMap<String, Comparable> tab1 = new HashMap<String, Comparable>();
		tab1.put("tag", "face_tab");
		tab1.put("nor_icon", R.drawable.face_icon_nor);
		tab1.put("push_icon", R.drawable.face_icon_pushed);

		HashMap<String, Comparable> tab2 = new HashMap<String, Comparable>();
		tab2.put("tag", "day_tab");
		tab2.put("nor_icon", R.drawable.memorial_day_icon_nor);
		tab2.put("push_icon", R.drawable.memorial_day_icon_pushed);

		HashMap<String, Comparable> tab3 = new HashMap<String, Comparable>();
		tab3.put("tag", "person_tab");
		tab3.put("nor_icon", R.drawable.celebrity_icon_nor);
		tab3.put("push_icon", R.drawable.celebrity_icon_pushed);

		HashMap<String, Comparable> tab4 = new HashMap<String, Comparable>();
		tab4.put("tag", "event_tab");
		tab4.put("nor_icon", R.drawable.event_icon_nor);
		tab4.put("push_icon", R.drawable.event_icon_pushed);

		headertabs = new HashMap<Integer, HashMap<String, Comparable>>();
		headertabs.put(R.id.face_btn, tab1);
		headertabs.put(R.id.day_btn, tab2);
		headertabs.put(R.id.person_btn, tab3);
		headertabs.put(R.id.event_btn, tab4);

		View v = this.findViewById(R.id.day_btn);
		v.setBackgroundResource((Integer) ((HashMap) headertabs.get(v.getId()))
				.get("push_icon"));
		this.CURRENT_HEADER_TAB = v.getId();
		this.CURRENT_PAGER_TAB = v.getId();
	}

	// ////////////////////
	private void initFaceTab() {
		HashMap<String, Comparable> tab1 = new HashMap<String, Comparable>();
		tab1.put("tag", "1_tab");
		tab1.put("nor_icon", R.drawable.sogood);
		tab1.put("push_icon", R.drawable.sogood_pushed);

		HashMap<String, Comparable> tab2 = new HashMap<String, Comparable>();
		tab2.put("tag", "2_tab");
		tab2.put("nor_icon", R.drawable.good);
		tab2.put("push_icon", R.drawable.good_pushed);

		HashMap<String, Comparable> tab3 = new HashMap<String, Comparable>();
		tab3.put("tag", "3_tab");
		tab3.put("nor_icon", R.drawable.notbad);
		tab3.put("push_icon", R.drawable.notbad_pushed);

		HashMap<String, Comparable> tab4 = new HashMap<String, Comparable>();
		tab4.put("tag", "4_tab");
		tab4.put("nor_icon", R.drawable.sad);
		tab4.put("push_icon", R.drawable.sad_pushed);

		HashMap<String, Comparable> tab5 = new HashMap<String, Comparable>();
		tab5.put("tag", "5_tab");
		tab5.put("nor_icon", R.drawable.sosad);
		tab5.put("push_icon", R.drawable.sosad_pushed);

		facetabs = new HashMap<Integer, HashMap<String, Comparable>>();
		facetabs.put(R.id.sogood, tab1);
		facetabs.put(R.id.good, tab2);
		facetabs.put(R.id.notbad, tab3);
		facetabs.put(R.id.sad, tab4);
		facetabs.put(R.id.sosad, tab5);

		// ImageView v = (ImageView) this.findViewById(R.id.sogood);
		// v.setImageResource((Integer) ((HashMap)
		// facetabs.get(v.getId())).get("push_icon"));
		this.CURRENT_FACE_TAB = R.id.sogood;
	}

	// ////////////
	// 发布新鲜事
	public void addFeed(View v) {
		String content = feedet.getText().toString();
		if (content.equals("")) {
			return;
		}
		this.feedet.setText("");
		this.feedet.setHint("今天已记录啦~");
		this.feedet.setEnabled(false);

		FeedBean feed = new FeedBean();
		feed.setContent(content);
		feed.setEmotion(this.getNumByFaceId(this.CURRENT_FACE_TAB));
		feed.setDate(DateUtil.getCurrentDateline().substring(0, 10));

		this.feedbeans.add(0, feed);
		this.feedadapter.notifyDataSetChanged();

		new FeedModel(this).add(feed.getContent(), feed.getDate(),
				feed.getEmotion());
	}

	//
	// 引导图片适配器
	private class DayPageAdapter extends PagerAdapter {
		View[] views;

		private DayPageAdapter(View[] views) {
			this.views = views;
		}

		public void destroyItem(View paramView, int paramInt, Object paramObject) {
			((ViewPager) paramView).removeView(views[paramInt]);
		}

		public int getCount() {
			return views.length;
		}

		public Object instantiateItem(View paramView, int paramInt) {
			((ViewPager) paramView).addView(views[paramInt], 0);
			return views[paramInt];
		}

		public boolean isViewFromObject(View paramView, Object paramObject) {
			if (paramView == paramObject) {
				return true;
			}
			return false;
		}

		public void restoreState(Parcelable paramParcelable,
				ClassLoader paramClassLoader) {
		}

		public Parcelable saveState() {
			return null;
		}
	}

	// 页面切换时改变当前页指示灯
	class PageChangeListener implements ViewPager.OnPageChangeListener {
		private Context context;

		PageChangeListener(Context context) {
			this.context = context;
		}

		public void onPageScrollStateChanged(int paramInt) {
		}

		public void onPageScrolled(int paramInt1, float paramFloat,
				int paramInt2) {
		}

		public void onPageSelected(int paramInt) {
			View v;
			if (paramInt == 0) {
				v = ((Activity) context).findViewById(R.id.day_btn);
			} else if (paramInt == 1) {
				v = ((Activity) context).findViewById(R.id.person_btn);
			} else {
				v = ((Activity) context).findViewById(R.id.event_btn);
			}

			// ///////////////////////CURSOR
			View g = findViewById(R.id.header_btn_group);
			int i1 = getNumByHeaderId(CURRENT_PAGER_TAB);
			int i2 = getNumByHeaderId(v.getId());

			int fromX = (2 * i1 - 1) * g.getWidth() / 6;
			int toX = fromX + (i2 - i1) * g.getWidth() / 3;
			int fix = header_cursor.getWidth() / 2;

			Animation animation = new TranslateAnimation(fromX - fix,
					toX - fix, 0, 0);

			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(250);

			// //////////////////////////////
			// 旧BTN
			View old = findViewById(CURRENT_HEADER_TAB);
			old.setBackgroundResource((Integer) ((HashMap) headertabs.get(old
					.getId())).get("nor_icon"));

			// New
			v.setBackgroundResource((Integer) ((HashMap) headertabs.get(v
					.getId())).get("push_icon"));

			CURRENT_HEADER_TAB = v.getId();
			CURRENT_PAGER_TAB = v.getId();

			View view = viewGroup.getCurrentView();
			ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
			ViewPager pager2 = null;
			int tab = 0;
			if (v.getId() == R.id.day_btn) {
			} else if (v.getId() == R.id.person_btn) {
				tab = 1;
			} else if (v.getId() == R.id.event_btn) {
				tab = 2;
			}
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				pager2 = (ViewPager) viewGroup.getChildAt(i).findViewById(
						R.id.pager);

				if (!pager2.equals(pager)) {
					pager2.setCurrentItem(tab);
				}

			}

			header_cursor.startAnimation(animation);
		}
	}

}
