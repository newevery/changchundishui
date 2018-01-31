package com.example.dishuifw;

import java.lang.reflect.Field;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dishuifw.core.constant.ProtocolConstant.CurrentFragmentName;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 
 * 
 */
public abstract class BaseFragment extends Fragment {
	public static final Logger logger = LoggerFactory.getLogger(BaseFragment.class);
	public Context mContext;
	public View mRootView;
	public static CurrentFragmentName mCurrentViewName = CurrentFragmentName.defaultPager;
	public IViewChangeListener mViewChangeListener;
	
	public void initComponents() {

	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mViewChangeListener = (IViewChangeListener) activity;
		}catch(Exception e){
			System.out.println("activity no implement IViewChangeListener ");
		}
	}
	
	@Override
    public void onDetach() {
    	super.onDetach();
    	try {
    	    Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
    	    childFragmentManager.setAccessible(true);
    	    childFragmentManager.set(this, null);

    	} catch (NoSuchFieldException e) {
    	    throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e);
    	}
    
    }


	/**
	 * 找到view的资源
	 * 
	 * @param v
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends View> T findView(View v, int id) {
		T result = null;
		if (v != null) {
			result = (T) v.findViewById(id);
		}
		return result;
	}
	

	/**
	 * 找到view的资源
	 * 
	 * @param v
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends View> T findView( int id) {
		T result = null;
		if (mRootView != null) {
			result = (T) mRootView.findViewById(id);
		}
		return result;
	}

	public abstract void handleMsgUpdateToView();
	
	protected void setCurrentPagerName(){
		
	}

	@SuppressWarnings("unchecked")
	protected final <T extends Fragment> T findFragment(int id) {
		return (T) getFragmentManager().findFragmentById(id);
	}

	/**
	 * 初始化资源
	 * 
	 * @param rootView 布局view，可以用这个获取view的id
	 */
	protected abstract void initComponents(View rootView);

	protected void jumpToActivity(Class<?> className) {
		jumpToActivity(className, null, 0);
	}

	protected void jumpToActivity(Class<?> className, int flag) {
		jumpToActivity(className, null, flag);
	}

	protected void jumpToActivity(Class<?> className, Bundle b, int flag) {
		Intent intent = new Intent(getActivity(), className);
		if (null != b)
			intent.putExtras(b);
		if (flag != 0)
			intent.setFlags(flag);
		startActivity(intent);
	}
	
	public void showDefaultView() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (null != mViewChangeListener) {
					mViewChangeListener.handleMsg(null, FromClientCode.DefaultView,
							null);
				}
			}
		}, 500);
		
		
	}

}
