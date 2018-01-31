package com.example.dishuifw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.R;

public class PauseServiceFragment extends BaseFragment {
	
	private static PauseServiceFragment mInstance;
	
	public static PauseServiceFragment getInstance(){
		if(null == mInstance){
			mInstance = new PauseServiceFragment();
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pause_service, container, false);
		return v;
	}

	@Override
	protected void initComponents(View rootView) {
		
	}

	@Override
	public void handleMsgUpdateToView() {
		// TODO Auto-generated method stub
		
	}

}
