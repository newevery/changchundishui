package com.example.dishuifw.ui;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.AudioName;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.util.AudioUtil;

public class AppraisalBadQSelectFragment extends BaseFragment implements
		OnClickListener {

	private static AppraisalBadQSelectFragment mInstance;

	private View vHjItem;
	private View vFwtdItem;
	private View vYwnlItem;
	private View vQtItem;

	private static SparseIntArray appraisalCode = new SparseIntArray();

	static {
		/**
		 * 按钮的id，及其对应的评价返回码，便于点击按钮的时候直接获取其对应的值
		 * **/
		appraisalCode.put(R.id.appraisalBad_layout_hj,
				ProtocolConstant.AppraisalCode.HJ.code);
		appraisalCode.put(R.id.appraisalBad_layout_fwtd,
				ProtocolConstant.AppraisalCode.TD.code);
		appraisalCode.put(R.id.appraisalBad_layout_ywnl,
				ProtocolConstant.AppraisalCode.YW.code);
		appraisalCode.put(R.id.appraisalBad_layout_qt,
				ProtocolConstant.AppraisalCode.QT.code);
	}

	public static AppraisalBadQSelectFragment getInstance() {
		return new AppraisalBadQSelectFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_appraisal_bad_q_select,
				container, false);
		initComponents(mRootView);
		return mRootView;
	}

	@Override
	protected void initComponents(View rootView) {
		vHjItem = findView(R.id.appraisalBad_layout_hj);
		vFwtdItem = findView(R.id.appraisalBad_layout_fwtd);
		vYwnlItem = findView(R.id.appraisalBad_layout_ywnl);
		vQtItem = findView(R.id.appraisalBad_layout_qt);

		vHjItem.setOnClickListener(this);
		vFwtdItem.setOnClickListener(this);
		vYwnlItem.setOnClickListener(this);
		vQtItem.setOnClickListener(this);
	}

	@Override
	public void handleMsgUpdateToView() {

	}

	@Override
	public void onClick(View v) {
		AppraisalFragment.mCurrentSubLevel = appraisalCode.get(v.getId());
		AppraisalFragment.isCalled = true;
		AppraisalFragment.isClickComment = true;
		AudioUtil.playMatchAudio(getActivity(), AudioName.THANKS);
		mViewChangeListener.handleMsg(null, FromClientCode.DefaultView, null);
	}

}
