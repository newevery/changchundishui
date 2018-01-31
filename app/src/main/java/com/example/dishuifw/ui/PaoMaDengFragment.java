package com.example.dishuifw.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.PublicExtrakey;

public class PaoMaDengFragment extends BaseFragment {

	private static TextView vPaoMaDeng;
//	private static TextView vPaoMaDeng1;
	private static PaoMaDengFragment mInstance;
	
	private String pmdInfo = null;
	
	private Handler mUpdateHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		
		};
	};

	public static PaoMaDengFragment getInstance() {
//		if (null == mInstance) {
//			mInstance =
					return new PaoMaDengFragment();
//		}
//		return mInstance;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_paomadeng, container, false);
		initComponents(mRootView);
		return mRootView;
	}

	@Override
	protected void initComponents(View rootView) {
		vPaoMaDeng = findView(R.id.paomadeng_textview_paomadeng);
//		vPaoMaDeng1 = findView(R.id.paomadeng_textview_paomadeng1);
		starPaoMaDengAnim();
	}

	@Override
	public void handleMsgUpdateToView() {
		pmdInfo = MyApplication.mPreUtil.getString(PublicExtrakey.MAIN_PAOMADENG, null);
		
		if (TextUtils.isEmpty(pmdInfo)) {
			vPaoMaDeng.setVisibility(View.GONE);
			vPaoMaDeng.setText("");
			vPaoMaDeng.clearAnimation();
			
//			vPaoMaDeng1.setVisibility(View.GONE);
//			vPaoMaDeng1.setText("");
//			vPaoMaDeng1.clearAnimation();
			
			
		} else {
			vPaoMaDeng.setVisibility(View.VISIBLE);
			vPaoMaDeng.setText(pmdInfo);
			
		
			starPaoMaDengAnim();
		}

	}

	private void starPaoMaDengAnim() {
		final Animation hyperspaceJump = AnimationUtils.loadAnimation(getActivity(), R.anim.pao_ma_deng);
		vPaoMaDeng.startAnimation(hyperspaceJump);
		
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				vPaoMaDeng1.setVisibility(View.VISIBLE);
//				vPaoMaDeng1.setText(pmdInfo);
//				vPaoMaDeng1.startAnimation(hyperspaceJump);
//			}
//		}, 6000);
	}

}
