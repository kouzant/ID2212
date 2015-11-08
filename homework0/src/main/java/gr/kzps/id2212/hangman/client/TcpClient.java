package gr.kzps.id2212.hangman.client;

import gr.kzps.id2212.hangman.general.OpCodes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/*
 * Dummy client for testing purposes!
 */
public class TcpClient {

	private static boolean running = true;
	
	public static void main(String[] args) {
		Socket cSocket = null;

		try {
			cSocket = new Socket("localhost", 8080);

			InputStream input = cSocket.getInputStream();
			OutputStream output = cSocket.getOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			while (running) {
				
				System.out.print("Give OP code: ");
				Integer lala = Integer.parseInt(in.readLine());
				if (lala == 256) {
					running = false;
					break;
				}
				
				byte opCode = (byte) lala.byteValue();
				// byte opCode = (byte) 0x00;
				System.out.print("Give rest: ");
				byte[] rest = in.readLine().getBytes();

				byte[] msg = new byte[rest.length + 1];

				msg[0] = opCode;
				System.arraycopy(rest, 0, msg, 1, rest.length);

				
				output.write(msg);

				byte[] buffer = new byte[256];

				Integer n = input.read(buffer, 0, buffer.length);

				byte[] res = Arrays.copyOfRange(buffer, 0, n);
				
				System.out.println("Raw response: " + new String(res));
				
				if ((res[0] == OpCodes.WIN) || (res[0] == OpCodes.LOST)) {
					Byte scoreB = res[1];
					Integer score = scoreB.intValue();
					System.out.println("Score is: " + score);
				} else if (res[0] == OpCodes.W_GUESS) {
					Byte lossB = res[1];
					Integer loss = lossB.intValue();
					System.out.println("I still have " + loss + " lifes");
				} else if (res[0] == OpCodes.G_GUESS) {
					byte[] pat = Arrays.copyOfRange(res, 1, n);
					System.out.println("new pattern is: " + new String(pat));
				}
				
			}

			in.close();
			input.close();
			output.close();
			cSocket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
