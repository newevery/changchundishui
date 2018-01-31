package vnc.jevon.protocol.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vnc.jevon.protocol.RfbProtocol;
import vnc.jevon.protocol.ServerInfo;
import vnc.jevon.protocol.support.canvas.DefaultDrawable;
import vnc.jevon.protocol.support.canvas.ImageRect;
import vnc.jevon.protocol.support.encoding.impl.RawDecoder;
import vnc.jevon.protocol.support.encoding.impl.ZrleDecoder;
import vnc.jevon.util.ArrayUtil;
import vnc.jevon.util.UI;

/**
 * RFB??????????????
 *
 * @author fujinjun
 *
 */
public class DefaultRfbProtocol implements RfbProtocol {
	private DataInputStream is;
	private OutputStream os;
	private boolean running = false;
	private byte encoding = Encoding.RAW;
	private static final Logger logger = LoggerFactory.getLogger(DefaultRfbProtocol.class);

	@Override
	synchronized public void setInputStream(InputStream inputStream) {
		this.is = new DataInputStream(new BufferedInputStream(inputStream,
				16384));
	}

	@Override
	synchronized public void setOutputStream(OutputStream outputStream) {
		this.os = outputStream;
	}

	@Override
	synchronized public void handShake() throws IOException {
		byte[] version = new byte[12];
		is.read(version);
		byte minorVersion = version[10];
		if (minorVersion >= 8) {
			os.write("RFB 003.008\n".getBytes());
		} else if (minorVersion == 7) {
			os.write("RFB 003.007\n".getBytes());
		} else {
			os.write("RFB 003.003\n".getBytes());
		}
	}

	@Override
	synchronized public void ensureSecurity() throws IOException {
		int securityTypeNumber = is.readUnsignedByte();
		logger.debug( "securityTypeLength:" + securityTypeNumber);
		byte[] securityTypes = new byte[securityTypeNumber];
		is.read(securityTypes);
		authenticate(securityTypes);
	}

	synchronized private void authenticate(byte[] securityTypes)
			throws IOException {
		if (ArrayUtil.contains(securityTypes, SecurityType.AuthenticateNone)) {
			// ????????
			os.write(SecurityType.AuthenticateNone);
		}
	}

	@Override
	synchronized public void initClient() throws IOException {
		int result = is.readInt();
		if (result == 0) {
			// ??????
			logger.info("authenticate success");
			os.write(0);
		} else {
			// ??????????????????
			int reasonLen = is.readInt();
			byte[] reason = new byte[reasonLen];
			is.read(reason);
			logger.info("authenticate fail,reason:" + new String(reason));
		}

	}

	@Override
	synchronized public void initServer() throws IOException {
		UI.serverinfo = new ServerInfo(is);
        DefaultDrawable.getInstance().init();
		logger.debug( UI.serverinfo.toString());
		setPixelFormat(UI.ClientInfo.C24bit);
		// UI.ClientInfo.palette = ColorModel256.colors;
		UI.ClientInfo.bytesPerPixel = 4;
		// setEncoding(new int[] { Encoding.Zrle });
		frameUpdateRequest(0, 0, UI.serverinfo.frameBufferWidth,
				UI.serverinfo.frameBufferHeight, true);
	}

	byte[] req = new byte[10];

	@Override
	synchronized public void frameUpdateRequest(int x, int y, short width,
			short height, boolean incremental) throws IOException {
		req[0] = Command.FrameBufferUpdateRequest;
		req[1] = (byte) (incremental ? 1 : 0);
		req[2] = (byte) ((x >> 8) & 0xff);
		req[3] = (byte) (x & 0xff);
		req[4] = (byte) ((y >> 8) & 0xff);
		req[5] = (byte) (y & 0xff);
		req[6] = (byte) ((width >> 8) & 0xff);
		req[7] = (byte) (width & 0xff);
		req[8] = (byte) ((height >> 8) & 0xff);
		req[9] = (byte) (height & 0xff);
		os.write(req);
		if (!running) {
			handleUpdate();
		}
	}

	@Override
	synchronized public void setPixelFormat(byte[] b) throws IOException {
		os.write(b);
	}

	@Override
	public void handleUpdate() throws IOException {
		int msgType = is.readUnsignedByte();
		is.readByte();
		switch (msgType) {
		case Command.FrameBufferUpdate:
			int rectNum = is.readUnsignedShort();
			if (encoding != Encoding.Zrle) {
				setEncoding(new int[] { Encoding.Zrle });
			}

			for (int i = 0; i < rectNum; i++) {
				ImageRect.from(is).handle();
			}

			break;

		default:
			break;
		}
//		System.gc();
		new Thread(){
			@Override
			public void run() {
				try {
					frameUpdateRequest(0, 0, UI.serverinfo.frameBufferWidth,
							UI.serverinfo.frameBufferHeight, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	synchronized public void setEncoding(int[] encodings) throws IOException {
		byte[] b = new byte[4 + 4 * encodings.length];
		encoding = (byte) encodings[0];

		b[0] = Command.SetEncoding;
		b[2] = (byte) ((encodings.length >> 8) & 0xff);
		b[3] = (byte) (encodings.length & 0xff);

		for (int i = 0; i < encodings.length; i++) {
			b[4 + 4 * i] = (byte) ((encodings[i] >> 24) & 0xff);
			b[5 + 4 * i] = (byte) ((encodings[i] >> 16) & 0xff);
			b[6 + 4 * i] = (byte) ((encodings[i] >> 8) & 0xff);
			b[7 + 4 * i] = (byte) (encodings[i] & 0xff);
		}

		os.write(b);
	}

	@Override
	public void destory() {
        ZrleDecoder.getInstance().destory();
        RawDecoder.getInstance().destory();
	}

}
