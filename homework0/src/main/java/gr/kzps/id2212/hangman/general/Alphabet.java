package gr.kzps.id2212.hangman.general;

/*
 * The english alphabet
 */
public enum Alphabet {
	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
	
	// Get position of a letter
	public static int getPosition(String letter) {
		return valueOf(letter.toUpperCase()).ordinal();
	}
	
	// Get the letter in a specific position
	public static String getLetter(Integer idx) {
		return Alphabet.values()[idx].toString();
	}
}
