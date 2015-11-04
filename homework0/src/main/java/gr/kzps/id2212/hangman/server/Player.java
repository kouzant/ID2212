package gr.kzps.id2212.hangman.server;

public class Player {
	
	private final String username;
	private String word;
	private Integer score;
	
	public Player(String username, String word) {
		this.username = username;
		this.word = word;
		this.score = 0;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getUsername() {
		return username;
	}

}
