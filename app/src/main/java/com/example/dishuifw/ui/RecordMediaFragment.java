package com.example.dishuifw.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.recordMedia.RecordMediaThread;

public class RecordMediaFragment extends BaseFragment implements SurfaceHolder.Callback {
	private SurfaceView surfaceview;// 视频预览控件
	private LinearLayout lay; // 预揽控件的
	private Button start; // 
	private Button stop; // 
	private SurfaceHolder surfaceHolder; // 和surfaceView相关的
	private RecordMediaThread thread;
	
	private int currentStarTime;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			currentStarTime++;
			mHandler.sendEmptyMessageDelayed(0, 1000);
			
		};
	};

	private static RecordMediaFragment mInstance;


	public static RecordMediaFragment getInstance() {
		if (null == mInstance) {
			mInstance = new RecordMediaFragment();
			return new RecordMediaFragment();
		}
		return mInstance;
	};
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fragment_record_media);
//		// 初始化控件
//		init();
//	}
//	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_record_media, container, false);
		initComponents(mRootView);
		return mRootView;
	}
	
	@Override
	protected void initComponents(View rootView) {
		init();
	}
//	
	/**
	 * 初始化控件以及回调
	 */
	private void init() {
		surfaceview = findView(R.id.surfaceview);
		lay = findView(R.id.lay);
		start = findView(R.id.start);
		stop = findView(R.id.stop);
//		lay.setVisibility(LinearLayout.INVISIBLE);
		SurfaceHolder holder = this.surfaceview.getHolder();// 取得holder
		holder.addCallback(this); // holder加入回调接口
		// 设置setType
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startRecord();
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				stopRecord();
			}
		});

	}
	
	
	public void startRecord(){
		if (thread==null) {
			thread = new RecordMediaThread(ProtocolConstant.DEFAULT_RECORD_TIMER_MINTER*60*1000, surfaceview, surfaceHolder);
			thread.start();
			mHandler.sendEmptyMessage(0);
			Toast.makeText(getActivity(), "kkaishi 录制中……", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(getActivity(), "正在录制中……", Toast.LENGTH_SHORT).show();
		}
	}
	
    public void stopRecord(){
    	//TODO 再次判断是否
    	if(currentStarTime < 4){ //此处4为4秒
    		new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					stopMedia();
				}
			}, 2000);
    		return ;
    	}
    	
    	stopMedia();
    }
    
    private void stopMedia(){
    	if (thread!=null) {
    		currentStarTime = 0;
    		mHandler.removeMessages(0);
			thread.stopRecord();
			thread=null;
			Toast.makeText(getActivity(), "视频录制结束", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(getActivity(), "视频录制还没开始", Toast.LENGTH_SHORT).show();
		}
    }
    
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		// 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder 
		Log.i("SurfaceHolder", "surfaceChanged()");
        surfaceHolder = holder; 
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("SurfaceHolder", Thread.currentThread().getName());
		// 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
		surfaceHolder = holder;
		// 录像线程，当然也可以在别的地方启动，但是一定要在onCreate方法执行完成以及surfaceHolder被赋值以后启动
		thread = new RecordMediaThread(1*60*1000, surfaceview, surfaceHolder);
		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.i("SurfaceHolder", "surfaceDestroyed()");
		// surfaceDestroyed的时候同时对象设置为null
		surfaceview = null;
		surfaceHolder = null;
		/*释放资源 mediarecorder mCamera 否则会后果很严重*/
		if (thread!=null) {
			thread.stopRecord();
			thread=null;
		}
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("RecordDemoActivity", "onResume()");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("RecordDemoActivity", "onPause()");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("RecordDemoActivity", "onDestroy()");
	}

	@Override
	public void handleMsgUpdateToView() {
		// TODO Auto-generated method stub
		
	}
	

}
