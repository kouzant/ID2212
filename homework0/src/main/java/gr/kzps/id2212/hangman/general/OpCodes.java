package gr.kzps.id2212.hangman.general;

public class OpCodes {
	public static final byte CREATE = (byte) 0x00;
	public static final byte GUESS = (byte) 0x01;
	public static final byte START = (byte) 0x02;
	public static final byte WIN = (byte) 0x03;
	public static final byte LOST = (byte) 0x04;
	public static final byte W_GUESS = (byte) 0x05;
	public static final byte G_GUESS = (byte) 0x06;
	public static final byte CLOSE = (byte) 0x07;
	public static final byte PL_AGAIN = (byte) 0x08;
	public static final byte UNKNOWN = (byte) 0xff;
}
