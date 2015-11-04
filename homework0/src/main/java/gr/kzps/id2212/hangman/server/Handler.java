package gr.kzps.id2212.hangman.server;

import gr.kzps.id2212.hangman.general.OpCodes;
import gr.kzps.id2212.hangman.general.Utilities;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler {
	
	private static final Logger LOG = LogManager.getLogger(Handler.class);
	
	private final Dictionary dictionary;
	private PlayersTracker playersTracker;
	
	public Handler(PlayersTracker playersTracker, Dictionary dictionary) {
		this.playersTracker = playersTracker;
		this.dictionary = dictionary;
	}
	
	public byte[] handle(byte[] request) {
		byte opCode = request[0];
		byte[] rest = Arrays.copyOfRange(request, 1, request.length);
		
		LOG.debug("op Code: {}", opCode);
		LOG.debug("Rest: {}", new String(rest));
		
		if (opCode == OpCodes.CREATE) {
			// Create new game
			// Rest = username
			// TODO Get players list, add the username
			// pick random word from dictionary and respond with pattern (001)
			LOG.debug("New game");
			
			String username = new String(rest); 
			// Remove any previous reference of that username
			playersTracker.removePlayer(username);
			
			String word = dictionary.getWord();
			LOG.debug("Word is {}", word);
			playersTracker.addPlayer(new Player(username, word));
			
			// Send back OpCodes.START pattern
			String pattern = Utilities.createPattern(word);

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
			
			return new byte[]{OpCodes.UNKNOWN};
			
		} else {
			// Unknown command
			LOG.debug("Unknown command");
			
			return new byte[] {OpCodes.UNKNOWN};
		}
	}
}
