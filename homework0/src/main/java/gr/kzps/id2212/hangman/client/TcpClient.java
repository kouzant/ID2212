package gr.kzps.id2212.hangman.client;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class TcpClient {

	public static void main(String[] args) {
		Socket cSocket = null;
		
		try {
			
			byte opCode = (byte) 0x00;
			
			byte[] rest = "antonis".getBytes();
			
			byte[] msg = new byte[rest.length + 1];
			
			msg[0] = opCode;
			System.arraycopy(rest, 0, msg, 1, rest.length);
						
			cSocket = new Socket("localhost", 8080);
			InputStream input = cSocket.getInputStream();
			OutputStream output = cSocket.getOutputStream();
			output.write(msg);
			
			byte[] buffer = new byte[msg.length];
			
			Integer n = input.read(buffer, 0, buffer.length);
			
			byte[] res = Arrays.copyOfRange(buffer, 0, n);
			System.out.println(new String(res));
			
			input.close();
			output.close();
			cSocket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
