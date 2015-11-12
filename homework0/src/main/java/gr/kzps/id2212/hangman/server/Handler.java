package gr.kzps.id2212.hangman.server;

import gr.kzps.id2212.hangman.general.OpCodes;
import gr.kzps.id2212.hangman.general.Utilities;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Handle request from the client and provide the appropriate response
 */
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

			// Pick a random word from the dictionary
			Dictionary dictionary = Dictionary.getInstance();
			//String word = dictionary.getWord();
			String word = "Beowolf";
			LOG.debug("Word is {}", word);

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
			// Rest is the proposed letter or word
			LOG.debug("Recieved a guess");

			if (rest.length > 1) {
				// Player guessed the whole word,
				// either win or lose
				LOG.debug("Player made a guess for the whole word");
				String guessStr = new String(rest);
				if (currentPlayer.getWord().toLowerCase().equals(guessStr.toLowerCase())) {
					// Win
					LOG.debug("Player won by giving the whole word!");
					currentPlayer.incrementScore();
					byte[] response = new byte[] { OpCodes.WIN,
							(byte) currentPlayer.getScore().byteValue() };

					return response;
				} else {
					// Lose
					if (currentPlayer.getLoss() <= 0) {
						// Sorry you have lost the game
						LOG.debug("Player lost the game by giving the whole word");
						byte[] response = new byte[] { OpCodes.LOST,
								currentPlayer.getScore().byteValue() };

						return response;
					} else {
						// Don't worry, try again
						LOG.debug("Player made a wrong word guess, try again");
						currentPlayer.decrementLoss();
						byte[] response = new byte[] { OpCodes.W_GUESS,
								currentPlayer.getLoss().byteValue() };

						return response;
					}
				}
			} else {
				// Player proposed a single letter
				LOG.debug("Player made a guess for a letter");
				String letter = new String(rest);
				if (currentPlayer.getWord().toLowerCase().contains(letter.toLowerCase())) {
					// Good guess
					LOG.debug("Player made a good guess about a letter");
					// Update the pattern with the letter guessed
					String newPattern = Utilities.updatePattern(letter,
							currentPlayer.getWord(),
							currentPlayer.getLastPattern());

					if (newPattern.contains("*")) {
						// Player has not win yet
						LOG.debug("Player has not win yet though");
						currentPlayer.setLastPattern(newPattern);

						byte[] response = new byte[newPattern.length() + 1];
						response[0] = OpCodes.G_GUESS;

						System.arraycopy(newPattern.getBytes(), 0, response, 1,
								newPattern.getBytes().length);

						return response;
					} else {
						// We have a winner!
						LOG.debug("Player win by guessing the correct letter");
						currentPlayer.incrementScore();
						byte[] response = new byte[] { OpCodes.WIN,
								currentPlayer.getScore().byteValue() };

						return response;
					}
				} else {
					// Wrong guess
					LOG.debug("Player did not make a good guess about a letter");
					currentPlayer.decrementLoss();
					if (currentPlayer.getLoss() <= 0) {
						// Game over
						LOG.debug("Game over!");
						byte[] response = new byte[] { OpCodes.LOST,
								currentPlayer.getScore().byteValue() };

						return response;
					} else {
						// Try again
						LOG.debug("Player has not lost yet");
						byte[] response = new byte[] { OpCodes.W_GUESS,
								currentPlayer.getLoss().byteValue() };

						return response;
					}
				}
			}

		} else if (opCode == OpCodes.CLOSE) {
			// Player exit the program
			LOG.debug("Closing connection");
			playersTracker.removePlayer(currentPlayer.getUsername());

			// Acknowledge back
			byte[] response = new byte[] { OpCodes.CLOSE };

			return response;
		} else if (opCode == OpCodes.PL_AGAIN) {
			// Player requested to continue playing
			LOG.debug("Player will play again");
			// Pick a new word from the dictionary
			Dictionary dictionary = Dictionary.getInstance();
			String word = dictionary.getWord();
			LOG.debug("New word is: {}", word);

			currentPlayer.setWord(word);
			String pattern = Utilities.createPattern(word);
			currentPlayer.setLastPattern(pattern);
			currentPlayer.resetLifes();

			byte[] response = new byte[pattern.length() + 1];
			response[0] = OpCodes.PL_AGAIN;
			System.arraycopy(pattern.getBytes(), 0, response, 1,
					pattern.getBytes().length);

			return response;
		} else {
			// Unknown command
			LOG.debug("Unknown command");

			return new byte[] { OpCodes.UNKNOWN };
		}
	}
}
