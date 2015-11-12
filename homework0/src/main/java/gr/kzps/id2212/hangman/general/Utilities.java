package gr.kzps.id2212.hangman.general;

/*
 * Some utility functions
 */
public class Utilities {

	// Substitute letters with '*', initial response
	public static String createPattern(String word) {
		StringBuilder pattern = new StringBuilder();

		for (int i = 0; i < word.length(); i++) {
			pattern.append("*");
		}

		return pattern.toString();
	}

	// For a given letter, update the occurence in the pattern
	public static String updatePattern(String letter, String word,
			String lastPattern) {
		char[] newPattern = new char[word.length()];
		char[] lastPatternCh = new char[lastPattern.length()];
		lastPattern.getChars(0, lastPattern.length(), lastPatternCh, 0);

		for (int i = 0; i < word.length(); i++) {
			char idxLetter = word.charAt(i);
			char idxLetterLC = word.toLowerCase().charAt(i);

			// If the current letter in the word is the same as the letter
			// provided and previously was '*' in the pattern, update the letter
			if ((idxLetterLC == letter.toLowerCase().charAt(0)) && (lastPatternCh[i] == '*')) {
				newPattern[i] = idxLetter;
				// If the current letter in the word is not the same as the
				// letter
				// provided and has not been guessed yet, leave '*' in the
				// pattern
			} else if ((idxLetter != letter.charAt(0))
					&& (lastPatternCh[i] == '*')) {
				newPattern[i] = '*';
				// If the current letter in the word has been previously
				// guessed,
				// leave the guess as it is
			} else if (lastPatternCh[i] != '*') {
				newPattern[i] = lastPatternCh[i];
			}

		}

		return new String(newPattern);
	}
}
