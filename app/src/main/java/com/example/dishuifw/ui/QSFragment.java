package com.example.dishuifw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;

public class QSFragment extends BaseFragment implements
		OnClickListener {

	private Button vOkBtn;


	public static QSFragment getInstance() {
		return new QSFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_qs,
				container, false);
		initComponents(mRootView);
		return mRootView;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void initComponents(View rootView) {
		MyApplication.setServiceStatusCode(ServerStatus.WORKING);

		vOkBtn = findView(R.id.business_button_ok);
		vOkBtn.setOnClickListener(this);
	}

	@Override
	public void handleMsgUpdateToView() {
		// 业务名称|业务说明|业务流程|所需材料|办理时限
		// updateView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.business_button_ok:
			MyApplication.setServiceStatusCode(ServerStatus.Online);
			showDefaultView();
			break;

		default:
			break;
		}
		

	}

}
