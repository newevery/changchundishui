package com.example.dishuifw.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 处理手势冲突的ViewPager 并且是慢速滚动的ViewPager
 * 
 * @author gaojianming
 * @date 2014年9月10日 下午2:40:33
 */
public class HeaderLoopViewPager extends YohoBuyViewPager {
	private int temp = 1;
	private float last_x;
	private static final int MIN_DISTANCE=5;
	
	
	public HeaderLoopViewPager(Context context) {
		super(context);
	}

	public HeaderLoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}	

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final float x = ev.getX();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);

			temp = 1;
			last_x = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (temp == 1) {
				if (x - last_x > MIN_DISTANCE &&getCurrentItem() == 0) {
					temp = 0;
					getParent().requestDisallowInterceptTouchEvent(false);
				}

				if (x - last_x < -MIN_DISTANCE
						&& getCurrentItem() == getAdapter().getCount() - 1) {
					temp = 0;
					getParent().requestDisallowInterceptTouchEvent(false);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
