package vnc.jevon.socket.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vnc.jevon.protocol.impl.DefaultRfbProtocol;
import vnc.jevon.socket.Dispatcher;
import vnc.jevon.socket.SocketClient;
import vnc.jevon.util.LoggerUtil;

import com.example.dishuifw.MyApplication;

public class VncClient extends Thread implements SocketClient {

    private Socket socket;
    private Dispatcher dispatcher;
    private static final Logger logger = LoggerFactory.getLogger(VncClient.class);
    DefaultRfbProtocol rfbProtocol;

    public VncClient(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public VncClient() {
        logger.debug("init vnc client");

        rfbProtocol = new DefaultRfbProtocol();
        this.dispatcher = new VncDispatcher(rfbProtocol);
    }

    @Override
    public void connect() throws Exception {
        logger.debug("start server:" + MyApplication.getInstance().vncServer);
        socket = MyApplication.getInstance().vncServer.accept();
        logger.debug("accept:" + socket.toString());
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        dispatcher.dispatch(inputStream, outputStream);
    }

    @Override
    public void run() {
        try {
            connect();
        } catch (Exception e) {
            LoggerUtil.print(e, logger);
            try {
                VncClient.this.destory();
//                UiHandler.getInstance().switchTo(MainView.class, EmptyFragment.class);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void connect(String host, int port) throws Exception {
        socket = new Socket(host, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        dispatcher.dispatch(inputStream, outputStream);
    }

    @Override
    public void destory() throws IOException {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        if (dispatcher != null)
            dispatcher.destory();
        if (rfbProtocol != null) {
            rfbProtocol.destory();
            rfbProtocol = null;
        }
    }

}
