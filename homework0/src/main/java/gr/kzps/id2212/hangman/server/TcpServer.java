package gr.kzps.id2212.hangman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpServer {
	private static final Logger LOG = LogManager.getLogger(TcpServer.class);
	private static final Integer PORT = 8080;
	
	@SuppressWarnings("resource")
	// Server socket will be closed when we exit...
	public static void main(String[] args) {
		ServerSocket sSocket = null;
		Socket cSocket = null;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		try {
			sSocket = new ServerSocket(PORT);
			LOG.info("Server started at port {}", PORT);
		} catch (Exception ex) {
			LOG.error("Something wrong happened!");
			ex.printStackTrace();
		}
		
		while (true) {
			try {
				cSocket = sSocket.accept();
				LOG.debug("Established connection");
				// Handle connection in separate thread
				threadPool.execute(new Handler(cSocket));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
