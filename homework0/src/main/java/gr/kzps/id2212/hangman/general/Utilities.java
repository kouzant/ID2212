package gr.kzps.id2212.hangman.general;

public class Utilities {
	public static String createPattern(String word) {
		StringBuilder pattern = new StringBuilder();
		
		for (int i = 0; i < word.length(); i++) {
			pattern.append("*");
		}
		
		return pattern.toString();
	}
}
