package com.example.dishuifw.ui;

import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.PublicExtrakey;
import com.example.dishuifw.util.BitmapTool;
import com.example.dishuifw.util.LoadSdCardBitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class UserInfoFragment extends BaseFragment {

	private static UserInfoFragment mInstance;
	private int imageMaxWidth;

	private ImageView vHeaderImg;
	private TextView vJobTv;
	private TextView vNameTv;
	private TextView vIdTv;
	private LinearLayout vStarContainer;

	public static UserInfoFragment getInstance() {
		return new UserInfoFragment();
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_user_info, container,
				false);
		initComponents(mRootView);
		return mRootView;
	}

	@Override
	protected void initComponents(View rootView) {

		imageMaxWidth = getResources().getDimensionPixelSize(
				R.dimen.userHeadSize);
		vHeaderImg = findView(R.id.userInfo_imageView_header);
		vJobTv = findView(R.id.userInfo_textView_job);
		vNameTv = findView(R.id.userInfo_textView_name);
		vIdTv = findView(R.id.userInfo_textView_id);
		vStarContainer = findView(R.id.userInfo_layout_starContainer);
	}

	@Override
	public void handleMsgUpdateToView() {
		updateView();
	}

	private void updateView() {
		vStarContainer.setVisibility(View.VISIBLE);
		String userName = MyApplication.mPreUtil.getString(
				PublicExtrakey.USER_NAME, null);
		String userId = MyApplication.mPreUtil.getString(
				PublicExtrakey.USER_ID, null);
		String rate = MyApplication.mPreUtil.getString(
				PublicExtrakey.USER_APPRISAL_RATE, null);
		if (!TextUtils.isEmpty(rate)) {
		}

		vHeaderImg.setImageBitmap(BitmapTool.getBitmap(getActivity(), userId,
				getHeadWidth(), getHeadHeight()));

		if (!TextUtils.isEmpty(userName)) {
			vNameTv.setText(getString(R.string.userInfo_tips_name) + userName);
		}

		if (!TextUtils.isEmpty(userId)) {
			vIdTv.setText(getString(R.string.userInfo_tips_No) + userId);
		}

		if (!TextUtils.isEmpty(rate)) {
			vStarContainer.removeAllViews();
			String[] points = rate.split("\\.");
			float point1 = 0;
			float point2 = 0;
			try {

				point1 = Float.valueOf(points[0]);

				for (int i = 0; i < point1; i++) {
					vStarContainer.addView(createStarView(R.drawable.star));
				}

				if (points.length > 1) {
					point2 = Float.valueOf(points[1]);
					vStarContainer
							.addView(createStarView(R.drawable.star_half));
				}

			} catch (Exception e) {

			}
		} else {

		}
	}

	private int getHeadWidth() {
		return getResources().getDimensionPixelSize(R.dimen.userHeadWidth);
	}

	private int getHeadHeight() {
		return getResources().getDimensionPixelSize(R.dimen.userHeadHeight);
	}

	private ImageView createStarView(int resId) {
		ImageView img = new ImageView(getActivity());
		img.setImageResource(resId);
		img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		img.setPadding(
				getResources().getDimensionPixelSize(
						R.dimen.userInfo_star_marginLeft), 0, 0, 0);
		return img;
	}

	public void outLogin() {
		vHeaderImg.setImageResource(R.drawable.user_default_header);
		vNameTv.setText(getString(R.string.userInfo_tips_name));
		vIdTv.setText(getString(R.string.userInfo_tips_No));
		vStarContainer.setVisibility(View.GONE);

	}

}
