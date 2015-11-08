package gr.kzps.id2212.hangman.server;

public class Player {
	
	private final String username;
	private String word;
	private Integer loss;
	private Integer score;
	private String lastPattern;
	
	public Player(String username, String word, String lastPattern) {
		this.username = username;
		this.word = word;
		this.loss = 10;
		this.score = 0;
		this.lastPattern = lastPattern;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getLoss() {
		return loss;
	}

	public void incrementLoss() {
		this.loss++;
	}
	
	public void decrementLoss() {
		this.loss--;
	}

	public Integer getScore() {
		return score;
	}
	
	public void incrementScore() {
		this.score++;
	}
	
	public void decrementScore() {
		this.score--;
	}
	
	public String getUsername() {
		return username;
	}

	public String getLastPattern() {
		return lastPattern;
	}
	
	public void setLastPattern(String pattern) {
		this.lastPattern = pattern;
	}
	
	public void resetLifes() {
		this.loss = 10;
	}
}
