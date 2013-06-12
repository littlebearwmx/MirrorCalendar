package edu.ustb.mirror;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import edu.ustb.mirror.common.Global;

//新安装用户引导页面
public class GuideActivity extends Activity {

	private SharedPreferences preferences;
	private Intent intent;
	private boolean isGuest;

	private ImageView[] indicator_image_views; // 当前页面指示
	private View view_group_main;
	private ArrayList<View> imagesList;
	private ViewGroup view_group_indicator;
	private ViewPager viewPager;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		init();
		setContentView(this.view_group_main);
	}

	// 初始化
	public void init() {
		this.intent = this.getIntent();
		this.preferences = this.getSharedPreferences(Global.PREF_ACCOUNT,
				MODE_PRIVATE);
		LayoutInflater layout_inflator = getLayoutInflater();

		this.imagesList = new ArrayList<View>();

		View iv1 = layout_inflator.inflate(R.layout.guide_1, null);
		this.imagesList.add(iv1);

		View iv2 = layout_inflator.inflate(R.layout.guide_2, null);
		this.imagesList.add(iv2);

		View iv3 = layout_inflator.inflate(R.layout.guide_3, null);
		this.imagesList.add(iv3);

		View iv4 = layout_inflator.inflate(R.layout.guide_4, null);
		this.imagesList.add(iv4);

		iv4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				guideEnd(null);
			}
		});

		this.view_group_main = layout_inflator.inflate(R.layout.activity_guide,
				null);

		this.viewPager = ((ViewPager) this.view_group_main
				.findViewById(R.id.pager));
		this.viewPager.setAdapter(new GuidePageAdapter(this.imagesList));
		this.viewPager.setFocusable(true);
		this.viewPager.setOnPageChangeListener(new GuidePageChangeListener());

	}

	// 引导图片适配器
	private class GuidePageAdapter extends PagerAdapter {
		ArrayList<View> imagesList;

		private GuidePageAdapter(ArrayList<View> imagesList) {
			this.imagesList = imagesList;
		}

		public void destroyItem(View paramView, int paramInt, Object paramObject) {
			((ViewPager) paramView)
					.removeView((View) GuideActivity.this.imagesList
							.get(paramInt));
		}

		public int getCount() {
			return GuideActivity.this.imagesList.size();
		}

		public Object instantiateItem(View paramView, int paramInt) {
			((ViewPager) paramView)
					.addView((View) GuideActivity.this.imagesList.get(paramInt));
			return GuideActivity.this.imagesList.get(paramInt);
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
	class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
		GuidePageChangeListener() {
		}

		public void onPageScrollStateChanged(int paramInt) {
		}

		public void onPageScrolled(int paramInt1, float paramFloat,
				int paramInt2) {
		}

		public void onPageSelected(int paramInt) {
			// 当前页面提示
		}
	}

	// 结束引导
	public void guideEnd(View view) {
		if (this.intent.getStringExtra("from").equals("setting")) {
			
		} else {
			this.startActivity(new Intent(this, TabhostActivity.class));
		}
		this.finish();
		this.overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
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
