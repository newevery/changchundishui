package com.example.dishuifw.ui;

import java.lang.reflect.Field;

import org.apache.mina.core.session.IoSession;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant.CurrentFragmentName;
import com.example.dishuifw.core.constant.PublicExtrakey;

public class JiaoHaoFragment extends BaseFragment {

//	private JiaoHaoFragment mInstance;

	private TextView vJiaoHaoInfo;
	private String text;
	private Handler updateViewHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			vJiaoHaoInfo.setText(Html.fromHtml(text));
		};
	};

	public static JiaoHaoFragment getInstance() {
//		if (null == mInstance) {
//			mInstance = new JiaoHaoFragment();
//		}
//		return mInstance;
		return new JiaoHaoFragment();
	};
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_jiaohao, container, false);
		initComponents(v);
		return v;
	}

	@Override
	protected void initComponents(View rootView) {
		mRootView = rootView;
		vJiaoHaoInfo = findView(rootView, R.id.jiaohao_textView_info);

		String text = "<font  color='#ffffff'>请</font>" +
				"<font color='#FEF100' size='40dp'><b>" +		"Z1001" + "</b></font>" 
				+"<font  color='#ffffff'>到</font>" 
				+ "<font color='#FEF100' size='40dp'><b>" +	8 + "</b></font>" 
		+"<font  color='#ffffff'>" +	"号窗口办理</font>";
		vJiaoHaoInfo.setText(Html.fromHtml(text));
	}

	@Override
	public void handleMsgUpdateToView() {
		String jiaoHao = MyApplication.mPreUtil.getString(PublicExtrakey.JIAO_HAO, "0000");
		text = "<font  color='#ffffff'>请</font>" +
				"<font color='#FEF100' size='40dp'><b>" +		jiaoHao + "</b></font>" 
		        +"<font  color='#ffffff'>号到本窗口办理</font>";
		
		updateViewHandler.sendEmptyMessage(0);
	}
	
	@Override
		protected void setCurrentPagerName() {
			super.setCurrentPagerName();
			mCurrentViewName = CurrentFragmentName.jiaohao;
		}

}
