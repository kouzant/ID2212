package gr.kzps.id2212.hangman.server;

import gr.kzps.id2212.hangman.general.OpCodes;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler {
	
	private static final Logger LOG = LogManager.getLogger(Handler.class);
	public Handler() {
		super();
	}
	
	// TODO: Fix op codes later when I'll have a more solid idea of what the operations could be
	public void handle(byte[] request) {
		byte opCode = request[0];
		byte[] Rest = Arrays.copyOfRange(request, 1, request.length);
		
		LOG.debug("op Code: {}", opCode);
		LOG.debug("Rest: {}", new String(Rest));
		
		if (opCode == OpCodes.CREATE) {
			// Create new game
			// TODO Get players list, add the username
			// pick random word from dictionary and respond with pattern (001)
			LOG.debug("New game");
		} else if (opCode == OpCodes.GUESS) {
			// Receive a guess about a letter or the whole word.
			// If response is one character then repond back with 101 or 100
			// If response is > 1 char then repond 011 or 111
			LOG.debug("Recieved a guess");
		} else {
			// Unknown command
			LOG.debug("Unknown command");
		}
	}
}
