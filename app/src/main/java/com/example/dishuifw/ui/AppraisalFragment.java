package com.example.dishuifw.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.AppraisalCode;
import com.example.dishuifw.core.constant.ProtocolConstant.AudioName;
import com.example.dishuifw.core.constant.ProtocolConstant.CurrentFragmentName;
import com.example.dishuifw.util.AudioUtil;

/***
 * 评价
 * **/
public class AppraisalFragment extends BaseFragment implements OnClickListener {

	public static int mCurrentLevel = AppraisalCode.FCMY.code;
	public static int mCurrentSubLevel = AppraisalCode.DEFAULT.code;
	public static boolean isCalled = false;
	public static boolean isClickComment = false;

	private static TextView vFcmyTv;
	private static TextView vMyTv;
	private static TextView vYbTv;
	private static TextView vBmyTv;

	private View vAppraisalTypeContainer;
	private View vBmyContainer;

	private static SparseIntArray appraisalCode = new SparseIntArray();

	static {
		/**
		 * 按钮的id，及其对应的评价返回码，便于点击按钮的时候直接获取其对应的值
		 * **/
		appraisalCode.put(R.id.appraisal_textView_fcmy,
				ProtocolConstant.AppraisalCode.FCMY.code);
		appraisalCode.put(R.id.appraisal_textView_my,
				ProtocolConstant.AppraisalCode.MY.code);
		appraisalCode.put(R.id.appraisal_textView_yb,
				ProtocolConstant.AppraisalCode.YB.code);
		appraisalCode.put(R.id.appraisal_textView_bmy,
				ProtocolConstant.AppraisalCode.BMY.code);
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			isCalled = true;
			isClickComment = true;
			showDefaultView();
		};
	};

	public static AppraisalFragment getInstance() {
		return new AppraisalFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyApplication.isAppraisalUI = true;
		View v = inflater
				.inflate(R.layout.fragment_appraisal, container, false);
		initComponents(v);
		// AudioUtil.play(getActivity(), R.raw.comment);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AudioUtil.playMatchAudio(getActivity(), AudioName.COMMENT);
			}
		});
		return v;
	}

	@Override
	protected void initComponents(View rootView) {
		vFcmyTv = findView(rootView, R.id.appraisal_textView_fcmy);
		vMyTv = findView(rootView, R.id.appraisal_textView_my);
		vYbTv = findView(rootView, R.id.appraisal_textView_yb);
		vBmyTv = findView(rootView, R.id.appraisal_textView_bmy);
		vAppraisalTypeContainer = findView(rootView,
				R.id.appraisal_layout_firstContainer);
		vBmyContainer = findView(rootView, R.id.appraisal_layout_bmyContainer);

		vFcmyTv.setOnClickListener(this);
		vMyTv.setOnClickListener(this);
		vYbTv.setOnClickListener(this);
		vBmyTv.setOnClickListener(this);

		mHandler.sendEmptyMessageDelayed(0,
				ProtocolConstant.DEFAULT_APPRAISAL_TIME);
	}

	@Override
	public void handleMsgUpdateToView() {
	}

	@Override
	public void onClick(View v) {
		mCurrentLevel = appraisalCode.get(v.getId());

		if (R.id.appraisal_textView_bmy != v.getId()) {
			mHandler.removeMessages(0);
			isCalled = true;
			isClickComment = true;
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AudioUtil.playMatchAudio(getActivity(), AudioName.THANKS);
				}
			});

			
			showDefaultView();
		} else {
			showBmyView();
		}
		setBtnEnable();
	}

	private void setBtnEnable() {
		vFcmyTv.setEnabled(false);
		vMyTv.setEnabled(false);
		vYbTv.setEnabled(false);
		vBmyTv.setEnabled(false);
	}

	private void showBmyView() {
		vAppraisalTypeContainer.setVisibility(View.INVISIBLE);
		vBmyContainer.setVisibility(View.VISIBLE);
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.appraisal_layout_bmyContainer,
						AppraisalBadQSelectFragment.getInstance()).commit();
	}


	@Override
	protected void setCurrentPagerName() {
		super.setCurrentPagerName();
		mCurrentViewName = CurrentFragmentName.comment;
	}

}
