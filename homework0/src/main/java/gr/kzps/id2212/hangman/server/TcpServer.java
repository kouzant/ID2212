package gr.kzps.id2212.hangman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpServer {
	private static final Logger LOG = LogManager.getLogger(TcpServer.class);
	private static final Integer PORT = 8080;
	private static ServerSocket sSocket;
	private static boolean running = true;
	
	public static void main(String[] args) {
		// Trap SIGINT
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				stop();
				if (sSocket != null) {
					try {
						sSocket.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				LOG.info("Ctrl-c pressed. Exit...");
			}
		}));
		
		sSocket = null;
		Socket cSocket = null;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		// Players tracker
		PlayersTracker playersTracker = new PlayersTracker();
		
		ArgumentsParser parser = new ArgumentsParser(args);
		try {
			CommandLine cmd = parser.parseArgs();
			
			if (cmd.hasOption("port")) {
				sSocket = new ServerSocket(Integer.parseInt(cmd.getOptionValue("port")));
			} else {
				sSocket = new ServerSocket(PORT);
			}
			
			LOG.info("Server started at port {}", sSocket.getLocalPort());
		} catch (Exception ex) {
			LOG.error("Something wrong happened!");
			ex.printStackTrace();
		}
		
		while (isRunning()) {
			try {
				cSocket = sSocket.accept();
				LOG.debug("Established connection");
				// Handle connection in separate thread
				threadPool.execute(new Acceptor(cSocket, playersTracker));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static boolean isRunning() {
		return running;
	}
	
	private static void stop() {
		running = false;
	}

}
