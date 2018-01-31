package vnc.jevon.util;

import vnc.jevon.protocol.ServerInfo;
import vnc.jevon.view.VncView;
import android.content.Context;

public class UI {
	public static VncView view;
	public static ServerInfo serverinfo;
	public static Context context;

	public static class ClientInfo {
		public static byte[] C24bit = new byte[] { 0, 0, 0, 0, 32, 24, 0, 1,
				(255 >> 8) & 0xff, (byte) (255 & 0xff), (255 >> 8) & 0xff,
				(byte) (255 & 0xff), (255 >> 8) & 0xff, (byte) (255 & 0xff),
				16, 8, 0, 0, 0, 0 };
		public static final byte[] C64 = new byte[] { 0, 0, 0, 0, 8, 6, 0, 1,
				(3 >> 8) & 0xff, (byte) (3 & 0xff), (3 >> 8) & 0xff,
				(byte) (3 & 0xff), (3 >> 8) & 0xff, (byte) (3 & 0xff), 4, 2, 0,
				0, 0, 0 };
		public static final byte[] C256 = new byte[] { 0, 0, 0, 0, 8, 8, 0, 1,
				(7 >> 8) & 0xff, (byte) (7 & 0xff), (7 >> 8) & 0xff,
				(byte) (7 & 0xff), (3 >> 8) & 0xff, (byte) (3 & 0xff), 0, 3, 6,
				0, 0, 0 };
		public static int bytesPerPixel = 1;
		public static int[] palette;
	}
}
