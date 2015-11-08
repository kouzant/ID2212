package gr.kzps.id2212.hangman.server;

import gr.kzps.id2212.hangman.general.OpCodes;
import gr.kzps.id2212.hangman.general.Utilities;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler {
	
	private static final Logger LOG = LogManager.getLogger(Handler.class);
	
	private PlayersTracker playersTracker;
	private Player currentPlayer;
	
	public Handler(PlayersTracker playersTracker) {
		this.playersTracker = playersTracker;
	}
	
	public byte[] handle(byte[] request) {
		LOG.debug("Request: {}", new String(request));
		byte opCode = request[0];
		byte[] rest = Arrays.copyOfRange(request, 1, request.length);
		
		LOG.debug("op Code: {}", opCode);
		LOG.debug("Rest: {}", new String(rest));
		
		if (opCode == OpCodes.CREATE) {
			// Create new game
			// Rest = username
			LOG.debug("New game");
			
			String username = new String(rest); 
			// Remove any previous reference of that username
			playersTracker.removePlayer(username);
			
			Dictionary dictionary = Dictionary.getInstance();
			String word = dictionary.getWord();
			LOG.debug("Word is {}", word);
			
			// Send back OpCodes.START pattern
			String pattern = Utilities.createPattern(word);
						
			currentPlayer = new Player(username, word, pattern);
			playersTracker.addPlayer(currentPlayer);

			byte[] patternBytes = pattern.getBytes();
			byte[] response = new byte[patternBytes.length + 1];
			response[0] = OpCodes.START;
			
			System.arraycopy(patternBytes, 0, response, 1, patternBytes.length);
			
			LOG.debug("Sending message {}", new String(response));
			
			return response;
		} else if (opCode == OpCodes.GUESS) {
			// Receive a guess about a letter or the whole word.
			// If response is one character then repond back with 101 or 100
			// If response is > 1 char then repond 011 or 111
			LOG.debug("Recieved a guess");
			
			if (rest.length > 1) {
				// Player guessed the whole word,
				// either win or lose
				LOG.debug("Player made a guess for the whole word");
				String guessStr = new String(rest);
				if (currentPlayer.getWord().equals(guessStr)) {
					// Win
					LOG.debug("Player won by giving the whole word!");
					currentPlayer.incrementScore();
					byte[] response = new byte[] {OpCodes.WIN, (byte) currentPlayer.getScore().byteValue()};
					
					return response;
				} else {
					// Lose
					if (currentPlayer.getLoss() <= 0) {
						// Sorry you have lost the game
						LOG.debug("Player lost the game by giving the whole word");
						byte[] response = new byte[] {OpCodes.LOST, currentPlayer.getScore().byteValue()};
						
						return response;
					} else {
						// Don't worry, try again
						LOG.debug("Player made a wrong word guess, try again");
						currentPlayer.decrementLoss();
						byte[] response = new byte[] {OpCodes.W_GUESS, currentPlayer.getLoss().byteValue()};
						
						return response;
					}
				}
			} else {
				// Check if letter part of the word and update pattern
				LOG.debug("Player made a guess for a letter");
				String letter = new String(rest);
				if (currentPlayer.getWord().contains(letter)) {
					// Good guess
					LOG.debug("Player made a good guess about a letter");
					String newPattern = Utilities.updatePattern(letter, currentPlayer.getWord(), currentPlayer.getLastPattern());
					
					if (newPattern.contains("*")) {
						// Has not won yet
						LOG.debug("Player has not won yet though");
						currentPlayer.setLastPattern(newPattern);
						
						byte[] response = new byte[newPattern.length() + 1];
						response[0] = OpCodes.G_GUESS;
						
						System.arraycopy(newPattern.getBytes(), 0, response, 1, newPattern.getBytes().length);
						
						return response;
					} else {
						// Won!
						LOG.debug("Player won by guessing the correct letter");
						currentPlayer.incrementScore();
						byte[] response = new byte[] {OpCodes.WIN, currentPlayer.getScore().byteValue()};
						
						return response;
					}
				} else {
					// Wrong guess
					LOG.debug("Player did not make a good guess about a letter");
					currentPlayer.decrementLoss();
					if (currentPlayer.getLoss() <= 0) {
						// Sorry you lost
						LOG.debug("Player lost!");
						byte[] response = new byte[] {OpCodes.LOST, currentPlayer.getScore().byteValue()};
						
						return response;
					} else {
						// Try again
						LOG.debug("Player has not lost yet");
						byte[] response = new byte[] {OpCodes.W_GUESS, currentPlayer.getLoss().byteValue()};
						
						return response;
					}
				}
			}
			
		} else if (opCode == OpCodes.CLOSE) {
			LOG.debug("Closing connection");
			playersTracker.removePlayer(currentPlayer.getUsername());

			byte[] response = new byte[] {OpCodes.CLOSE};
			
			return response;
		} else if (opCode == OpCodes.PL_AGAIN) {
			LOG.debug("Player will play again");
			Dictionary dictionary = Dictionary.getInstance();
			String word = dictionary.getWord();
			LOG.debug("New word is: {}", word);
			
			currentPlayer.setWord(word);
			String pattern = Utilities.createPattern(word);
			currentPlayer.setLastPattern(pattern);
			currentPlayer.resetLifes();
			
			byte[] response = new byte[pattern.length() + 1];
			response[0] = OpCodes.PL_AGAIN;
			System.arraycopy(pattern.getBytes(), 0, response, 1, pattern.getBytes().length);
			
			return response;
		} else {
			// Unknown command
			
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			LOG.debug("Unknown command");
			
			return new byte[] {OpCodes.UNKNOWN};
		}
	}
}
