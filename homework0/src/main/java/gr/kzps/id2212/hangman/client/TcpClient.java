package gr.kzps.id2212.hangman.client;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {

	public static void main(String[] args) {
		Socket cSocket = null;
		
		try {
			
			byte[] op = new byte[] {(byte) 0x00, (byte) 0x11, (byte) 0x00};
			
			byte[] rest = "Hello".getBytes();
			
			byte[] msg = new byte[op.length + rest.length];
			
			System.arraycopy(op, 0, msg, 0, 3);
			System.arraycopy(rest, 0, msg, op.length, rest.length);
			
			System.out.println(new String(msg));
			
			cSocket = new Socket("localhost", 8080);
			InputStream input = cSocket.getInputStream();
			OutputStream output = cSocket.getOutputStream();
			output.write(msg);
			
			byte[] buffer = new byte[msg.length];
			
			Integer n = input.read(buffer, 0, buffer.length);
			
			if (n != msg.length) {
				System.out.println("Something is lost...");
			} else {
				System.out.println(new String(msg));
			}
			
			input.close();
			output.close();
			cSocket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
