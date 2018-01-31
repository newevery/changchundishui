package vnc.jevon.view;

import global.ConstDefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vnc.jevon.socket.impl.VncClient;
import vnc.jevon.util.UI;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.dishuifw.R;

public class VncView extends Fragment implements Callback {

	private static final Logger logger = LoggerFactory.getLogger(VncView.class);
	protected VncClient vncClient;
	public SurfaceView imageView;
	private SurfaceHolder sfh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		UI.view = this;
		View rootView = inflater.inflate(R.layout.activity_canvas, container,
				false);
		imageView = (SurfaceView) rootView.findViewById(R.id.vnc_canvas);
		sfh = imageView.getHolder();
		sfh.addCallback(this);
		return rootView;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Bitmap vnc_view_bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.qsd);
		Canvas canvas = sfh.lockCanvas();
		if (canvas != null) {
			canvas.drawBitmap(vnc_view_bg, null, new Rect(0, 0,
					ConstDefs.DisplayWidth, ConstDefs.DisplayHeight),
					new Paint());
			sfh.unlockCanvasAndPost(canvas);
			sfh.lockCanvas(new Rect(0, 0, 0, 0));
			sfh.unlockCanvasAndPost(canvas);
		}
	}

	public void startVnc() {
		logger.debug("vnc start");
		vncClient = new VncClient();
		vncClient.start();
	}

	public void stopVnc() {
		logger.debug("vnc stop");
		try {
			if (vncClient != null) {
				vncClient.destory();
				vncClient = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
