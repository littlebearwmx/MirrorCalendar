package edu.ustb.mirror.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

public class MyViewGroup extends ViewGroup {

	private static final String TAG = "scroller";

	public static final int ORIENTATION_HORIZONTAL = 1;
	public static final int ORIENTATION_VERTICAL = 2;

	private int currentOrientation = 1;

	private IScrollListener mScrollListener;

	float scrollWidth = 0f;

	private Scroller scroller;

	private int currentScreenIndex = 0;

	public int getCurrentScreenIndex() {
		return currentScreenIndex;
	}

	public void setCurrentScreenIndex(int currentScreenIndex) {
		this.currentScreenIndex = currentScreenIndex;
	}

	private GestureDetector gestureDetector;

	private Context context;

	private float mStartY = 0; // save event y

	private boolean isFirstScroll = true;

	private boolean isInterceptEvent = false;

	// 设置一个标志位，防止底层的onTouch事件重复处理UP事件
	private boolean fling;

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);
	}

	public MyViewGroup(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	@SuppressWarnings("deprecation")
	private void initView(final Context context) {

		this.scroller = new Scroller(context);

		this.gestureDetector = new GestureDetector(new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {

				// 初次滑动，判断进入哪种状态，否则直接取当前状态
				if (isFirstScroll) {
					isFirstScroll = false;
					// 水平滑动
					if (Math.abs(distanceY) < Math.abs(distanceX)) {
						currentOrientation = MyViewGroup.ORIENTATION_HORIZONTAL;
					} else {// 垂直滑动
						currentOrientation = MyViewGroup.ORIENTATION_VERTICAL;
					}
				}
				if (currentScreenIndex >= 0
						&& currentScreenIndex <= getChildCount() - 1) {

					if (currentScreenIndex == getChildCount() - 1) {
						if (e2.getY() < e1.getY()) {
							return true;
						}
					}

					if (currentScreenIndex == 0) {
						if (e2.getY() > e1.getY()) {
							return true;
						}
					}

					if (currentOrientation == MyViewGroup.ORIENTATION_VERTICAL) {
						scrollBy(0, (int) distanceY);
						// Log.i(TAG, "getScrollY == " + getScrollY());
						isInterceptEvent = true;
					}
				}

				// Log.i(TAG, "onScroll");

				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				float distanceY = e2.getY() - e1.getY();
				float distanceX = e2.getX() - e1.getX();

				if (currentOrientation == MyViewGroup.ORIENTATION_VERTICAL) {
					isInterceptEvent = true;
					if (Math.abs(velocityY) > ViewConfiguration.get(context)
							.getScaledMinimumFlingVelocity()) {// 判断是否达到最小轻松速度，取绝对值的
						if (velocityY > 0) {
							scrollToPrevScreen();
						} else if (velocityY < 0) {
							scrollToNextScreen();
						}
						fling = true;
						Log.i(TAG, "onFling");
					}
				}

				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		Log.i(TAG, ">>left: " + left + " top: " + top + " right: " + right
				+ " bottom:" + bottom);

		int c = (this.getChildCount() - 1) / 2;
		/**
		 * 设置布局，将子视图顺序竖屏排列
		 */
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.setVisibility(View.VISIBLE);

			// ////默认奇数个子元素 显示中间那个
			child.measure(right - left, bottom - top);
			int l = 0;
			int t = (i - c) * getHeight();
			int r = getWidth();
			int b = getHeight() + (i - c) * getHeight();
			child.layout(l, t, r, b);

			Log.i(TAG, i + "-Child >>left: " + l + " top: " + t + " right: "
					+ r + " bottom:" + b);
		}

		this.setCurrentScreenIndex(c);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public Scroller getScroller() {
		return scroller;
	}

	public View getCurrentView() {
		return this.getChildAt(this.currentScreenIndex);
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(0, scroller.getCurrY());
			postInvalidate();
		}
	}

	public boolean onPreTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Log.i(TAG, "ACTION_DOWN");
			this.mStartY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			this.isFirstScroll = true;
			isInterceptEvent = false;
			scrollWidth = event.getRawY() - this.mStartY;

			if (!fling
					&& this.currentOrientation == MyViewGroup.ORIENTATION_VERTICAL) {
				if (scrollWidth > 0) {
					this.scrollToPrevScreen();
				} else if (scrollWidth < 0) {
					this.scrollToNextScreen();
				} else if (scrollWidth != 0) {
					// this.scrollToScreen(this.currentScreenIndex);
				}
			}
			fling = false;
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return this.isInterceptEvent;

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		this.onPreTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 切换到指定屏
	 * 
	 * @param whichScreen
	 */
	public void scrollToScreen(int whichScreen) {
		Log.i(TAG, "-------getScrollY == " + getScrollY());
		Log.i(TAG, "scrollToScreen : from " + this.currentScreenIndex + " to "
				+ whichScreen);
		int delta = 0;
		// Next
		if (whichScreen > this.currentScreenIndex) {
			// 下半部分
			if (whichScreen > (this.getChildCount() - 1) / 2) {

				delta = (Math.abs((this.currentScreenIndex - ((this
						.getChildCount() - 1) / 2))) + 1)
						* getHeight()
						- getScrollY();
			} else {// 上半部分
				delta = -(Math.abs((this.currentScreenIndex - ((this
						.getChildCount() - 1) / 2))) - 1)
						* getHeight()
						- getScrollY();
			}
			// Prev
		} else {
			// 下半部分
			if (whichScreen >= (this.getChildCount() - 1) / 2) {
				delta = (Math.abs((this.currentScreenIndex - ((this
						.getChildCount() - 1) / 2))) - 1)
						* getHeight()
						- getScrollY();
			} else {// 上半部分
				delta = -1
						* (Math.abs((this.currentScreenIndex - ((this
								.getChildCount() - 1) / 2))) + 1) * getHeight()
						- getScrollY();
			}
		}

		scroller.startScroll(0, getScrollY(), 0, delta,
				(int) (Math.abs(delta) * 1.2));
		invalidate();

		currentScreenIndex = whichScreen;

		Log.i(TAG, " delta " + delta);

	}

	/**
	 * 下一屏
	 * 
	 * @param whichScreen
	 */
	public void scrollToNextScreen() {
		if (this.currentScreenIndex < this.getChildCount() - 1) {
			Log.i(TAG, "scrollToNextScreen");

			this.scrollToScreen(this.currentScreenIndex + 1);

			//
			if (this.mScrollListener != null) {
				// this.mScrollListener.onScrollToNextCompleted();
				// Log.i(TAG, "onScrollToNextCompleted");
			}
		}

	}

	/**
	 * 上一屏
	 * 
	 * @param whichScreen
	 */
	public void scrollToPrevScreen() {
		if (this.currentScreenIndex > 0) {
			this.scrollToScreen(this.currentScreenIndex - 1);

			//
			if (this.mScrollListener != null) {
				// this.mScrollListener.onScrollToPrevCompleted();
				// Log.i(TAG, "onScrollToPrevCompleted");
			}
		}

	}

	/**
	 * 根据当前Y坐标位置确定切换到第几屏
	 */
	private void snapToDestination() {
		scrollToScreen((getScrollY() + (getHeight() / 2)) / getHeight());
	}

	// 监听事件接口
	public void setScrollListener(IScrollListener l) {
		this.mScrollListener = l;
	}

	public interface IScrollListener {
		public void onScrollToNextCompleted();

		public void onScrollToPrevCompleted();
	}

	// 插入第一张
	public void addHeaderView(View v) {
		this.addView(v, 0);
		Log.i(TAG, "add view index 0 childcount = " + this.getChildCount()
				+ " currentScreenIndex = " + this.currentScreenIndex);
	}

	// 插入最后一张
	public void addFooterView(View v) {
		this.addView(v);
		Log.i(TAG, "add view index " + (this.getChildCount() - 1)
				+ " childcount = " + this.getChildCount()
				+ " currentScreenIndex = " + this.currentScreenIndex);
	}

	// 删除第一张
	public void removeHeaderView() {
		this.removeViewAt(0);
		Log.i(TAG,
				"remove index 0" + " currentScreenIndex = "
						+ this.currentScreenIndex + " childcount = "
						+ this.getChildCount());
	}

	// 删除最后一张
	public void removeFooterView() {
		int i = this.getChildCount() - 1;
		this.removeViewAt(i);

		Log.i(TAG,
				"remove index " + i + " currentScreenIndex = "
						+ this.currentScreenIndex + " childcount = "
						+ this.getChildCount());
	}

}