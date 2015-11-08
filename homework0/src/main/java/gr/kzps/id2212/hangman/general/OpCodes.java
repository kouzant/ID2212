package gr.kzps.id2212.hangman.general;

/*
 * Operation codes for the communication protocol
 */
public class OpCodes {
	// Player requested to create a new game
	public static final byte CREATE = (byte) 0x00;
	// Player made a guess
	public static final byte GUESS = (byte) 0x01;
	// Server replies to start new game
	public static final byte START = (byte) 0x02;
	// Player win
	public static final byte WIN = (byte) 0x03;
	// Player lost
	public static final byte LOST = (byte) 0x04;
	// Player made a wrong guess
	public static final byte W_GUESS = (byte) 0x05;
	// Player made a good guess
	public static final byte G_GUESS = (byte) 0x06;
	// Close the connection
	public static final byte CLOSE = (byte) 0x07;
	// Player requested to play again
	public static final byte PL_AGAIN = (byte) 0x08;
	// Something weird received
	public static final byte UNKNOWN = (byte) 0xff;
}
