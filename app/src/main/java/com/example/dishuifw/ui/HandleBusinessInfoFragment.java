package com.example.dishuifw.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.PublicExtrakey;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;

public class HandleBusinessInfoFragment extends BaseFragment implements
		OnClickListener {
	public static boolean isConfirm = false;
	public static int confirmResult = 1; // 确认

	private final int RESPONSE_CODE_OK = 1;
	private final int RESPONSE_CODE_CANCEL = 0;
	private final int DEFAULT_TIME = 30 * 1000;

	// 业务名称|业务说明|业务流程|所需材料|办理时限
	private TextView vMingChenTv;
	private TextView vShuoMingTv;
	private TextView vLiuChenTv;
	private TextView vCaiLiaoTv;
	private TextView vShiJianTv;

	private Button vCanceBtn;
	private Button vOkBtn;

	private Handler mAutoJumpToDefaultView = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MyApplication.setServiceStatusCode(ServerStatus.Online);
			showDefaultView();
		};
	};

	public static HandleBusinessInfoFragment getInstance() {
		return new HandleBusinessInfoFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_handle_business_info,
				container, false);
		initComponents(mRootView);
		return mRootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		mAutoJumpToDefaultView.removeMessages(0);
	}

	@Override
	protected void initComponents(View rootView) {
		MyApplication.setServiceStatusCode(ServerStatus.WORKING);
		
		vMingChenTv = findView(R.id.business_textView_mc);
		vShuoMingTv = findView(R.id.business_textView_sm);
		vLiuChenTv = findView(R.id.business_textView_lc);
		vCaiLiaoTv = findView(R.id.business_textView_cl);
		vShiJianTv = findView(R.id.business_textView_sx);

		vCanceBtn = findView(R.id.business_button_cancel);
		vOkBtn = findView(R.id.business_button_ok);
		vCanceBtn.setOnClickListener(this);
		vOkBtn.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	public void handleMsgUpdateToView() {
		// 业务名称|业务说明|业务流程|所需材料|办理时限
		// updateView();
	}

	private void updateView() {
		String mc = MyApplication.mPreUtil.getString(
				PublicExtrakey.BUSSINESS_MC, null);
		String sm = MyApplication.mPreUtil.getString(
				PublicExtrakey.BUSSINESS_SM, null);
		String lc = MyApplication.mPreUtil.getString(
				PublicExtrakey.BUSSINESS_LC, null);
		String cl = MyApplication.mPreUtil.getString(
				PublicExtrakey.BUSSINESS_CL, null);
		String sx = MyApplication.mPreUtil.getString(
				PublicExtrakey.BUSSINESS_SX, null);

		vMingChenTv.setText(getString(R.string.handleBusiness_mc) + mc);
		vShuoMingTv.setText(getString(R.string.handleBusiness_need_sm) + sm);
		vLiuChenTv.setText(getString(R.string.handleBusiness_need_lc) + lc);
		vCaiLiaoTv.setText(getString(R.string.handleBusiness_need_sxcl) + cl);
		vShiJianTv.setText(getString(R.string.handleBusiness_need_blsx) + sx);

		mAutoJumpToDefaultView.sendEmptyMessageDelayed(0, DEFAULT_TIME);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.business_button_cancel:
			confirmResult = RESPONSE_CODE_CANCEL;
			break;

		case R.id.business_button_ok:
			confirmResult = RESPONSE_CODE_OK;
			break;

		default:
			break;
		}
		MyApplication.setServiceStatusCode(ServerStatus.Online);
		vCanceBtn.setEnabled(false);
		vOkBtn.setEnabled(false);
		isConfirm = true;
		showDefaultView();

	}

	// private void showDefaultView() {
	// if (null != mViewChangeListener) {
	// mViewChangeListener.handleMsg(null, FromClientCode.DefaultView,
	// null);
	// }
	// }

}
