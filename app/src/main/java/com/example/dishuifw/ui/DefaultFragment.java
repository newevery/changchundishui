package com.example.dishuifw.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.R;
import com.example.dishuifw.util.LoadSdCardBitmapUtil;

/***
 * 空闲默认显示的界面
 * **/
public class DefaultFragment extends BaseFragment implements OnClickListener {

	private static String TAG = "MainActivity";
	private ViewPager mViewPager;

	private ViewPagerAdapter mVpAdapter;

	private List<View> mViews;

	/**
	 * 记录当前自动滑动的状态，true就滑动，false停止滑动
	 */
	private boolean isContinue = true;

	private Handler mHandler;

	private Timer timer;

	private static boolean isSleep = true;

	/**
	 * 设置viewpager的初始页面
	 */
	private static final int initPositon = 50000;

	/**
	 * viewpager的当前页面
	 */
	private static int currentPosition = initPositon;

	public static DefaultFragment getInstance() {

		return new DefaultFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_default, container,
				false);
		mViews = new ArrayList<View>();

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		List<String> files = LoadSdCardBitmapUtil.getImgsInFoloder();

		for (int i = 0; i < files.size(); i++) {
			ImageView mImageView = new ImageView(getActivity());
			mImageView.setScaleType(ScaleType.FIT_XY);
			mImageView.setLayoutParams(mParams);
			Bitmap bitmap = LoadSdCardBitmapUtil.getLoacalBitmap(files.get(i));
			mImageView.setImageBitmap(bitmap);
			mViews.add(mImageView);
		}

		// Handler对象更新UI
		mHandler = new Handler() {

			public void handleMessage(Message msg) {
				mViewPager.setCurrentItem(currentPosition);
			}
		};

		// 启动线程，监控是否要自动滑动
		timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				while (true) {
					if (isContinue) {
						/*
						 * if (isSleep) { sleep(4000); }
						 */

						currentPosition++;
						mHandler.sendEmptyMessage(0);
						sleep(3000);
						/*
						 * if (!isSleep) { sleep(3000); } isSleep = false;
						 */
					}

				}
			}
		}, 4000);

		mViewPager = (ViewPager) findView(R.id.viewpager);
		mVpAdapter = new ViewPagerAdapter(mViews);
		mViewPager.setAdapter(mVpAdapter);
		mViewPager.setCurrentItem(initPositon);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new MyTouchListener());

		return mRootView;
	}

	/**
	 * viewpager页面变化的监听器
	 * 
	 * @author user
	 * 
	 */
	class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			currentPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	/**
	 * 监听手势监听器
	 * 
	 * @author user
	 * 
	 */
	class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				isContinue = false;
				break;
			case MotionEvent.ACTION_UP:
			default:
				isContinue = true;
				isSleep = true;
				break;
			}
			return false;
		}

	}

	/**
	 * 设置线程间隔
	 */
	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ViewPager适配器
	 * 
	 * @author user
	 * 
	 */
	public class ViewPagerAdapter extends PagerAdapter {

		// private static String TAG = "ViewPagerAdapter";
		/**
		 * 界面列表
		 */
		private List<View> mViews;

		public ViewPagerAdapter(List<View> mViews) {
			this.mViews = mViews;
		}

		/**
		 * 获取当前页面数
		 */
		@Override
		public int getCount() {
			// Log.v(TAG, "getCount" + mViews.size());
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			// Log.v(TAG, "isViewFromObject" + (view == object));
			return view == object;
		}

		/**
		 * 适配器给container容器添加视图
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Log.v(TAG, "instantiateItem" + position);

			position = position % mViews.size();
			container.addView(mViews.get(position), 0);
			return mViews.get(position);

		}

		/**
		 * 适配器移除container容器中的视图
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.v(TAG, "destroyItem" + position);
			position = position % mViews.size();
			container.removeView(mViews.get(position));
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMsgUpdateToView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initComponents(View rootView) {
		// TODO Auto-generated method stub

	}

}
