package particles;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class WsServer {

	private WebSocketServer server;
	private WebSocketMessageHandler messageHandler;
	
	public WsServer (WebSocketMessageHandler messageHandler) {
		try {
			this.messageHandler = messageHandler;
			server = new Server();
			server.start();
		} catch (Exception e) {
			System.out.println("Could not start web socket server");
			System.out.println(e.toString());
			System.exit(0);
		}
	}
	
	
	private class Server extends WebSocketServer{
		
		public Server(){
			super(new InetSocketAddress(8080));
		}

		@Override
		public void onClose(WebSocket conn, int code, String reason, boolean remote) {
			System.out.println("WS Connection Closed: IP address: " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ", reason: " + reason +  " boolean remote: " + remote + " connection.toString: " + conn.toString());
		}

		@Override
		public void onError(WebSocket conn, Exception e) {
			System.out.println("WS error from IP address: " + conn.getRemoteSocketAddress().getAddress().getHostAddress()  + " connection.toString: " + conn.toString() + "\n");
			e.printStackTrace();
		}

		@Override
		public void onMessage(WebSocket conn, String message) {
			String[] msgTokens = message.split("~");
			String msgKey = msgTokens[0];
			double msgValue;
			try {
				msgValue = Double.parseDouble(msgTokens[1]);
				messageHandler.handleMessage(msgKey, msgValue);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onOpen(WebSocket conn, ClientHandshake handshake) {
			System.out.println("WS Connection Opened: IP address: " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connection.toString: " + conn.toString());
		}
	}
	
}
