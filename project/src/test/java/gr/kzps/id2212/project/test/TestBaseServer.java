package gr.kzps.id2212.project.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import gr.kzps.id2212.project.messages.BaseMessage;

public class TestBaseServer {
	private static Socket cSocket;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		cSocket = new Socket("localhost", 9090);
		out = new ObjectOutputStream(cSocket.getOutputStream());
		in = new ObjectInputStream(cSocket.getInputStream());
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
		if (cSocket != null) {
			cSocket.close();
		}
	}
	
	@Test
	public void testAgentPort() throws IOException, ClassNotFoundException {
		BaseMessage bm = new BaseMessage();
		out.writeObject(bm);
		out.flush();
		Object reply = in.readObject();
		
		if (reply instanceof BaseMessage) {
			BaseMessage msg = (BaseMessage) reply;
			assertEquals("Agent port on server", (Integer) 8080, msg.getAgentPort());
		}
		
	}
}
