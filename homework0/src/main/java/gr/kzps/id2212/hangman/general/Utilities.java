package gr.kzps.id2212.hangman.general;

public class Utilities {
	
	// Substitute letters with *, initial response
	public static String createPattern(String word) {
		StringBuilder pattern = new StringBuilder();
		
		for (int i = 0; i < word.length(); i++) {
			pattern.append("*");
		}
		
		return pattern.toString();
	}
	
	// Update word pattern
		public static String updatePattern(String letter, String word, String lastPattern) {
			char[] newPattern = new char[word.length()];
			char[] lastPatternCh = new char[lastPattern.length()];
			lastPattern.getChars(0, lastPattern.length(), lastPatternCh, 0);
			
			for (int i = 0; i < word.length(); i++) {
				char idxLetter = word.charAt(i);
				
				if ((idxLetter == letter.charAt(0)) && (lastPatternCh[i] == '*')) {
					newPattern[i] = letter.charAt(0);
				} else if ((idxLetter != letter.charAt(0)) && (lastPatternCh[i] == '*')) {
					newPattern[i] = '*';
				} else if (lastPatternCh[i] != '*') {
					newPattern[i] = lastPatternCh[i];
				}
				
			}
			
			return new String(newPattern);
		}
}
