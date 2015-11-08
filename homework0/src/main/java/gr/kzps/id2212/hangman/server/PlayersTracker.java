package gr.kzps.id2212.hangman.server;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Tracker for every player.
 * Currently NOT USED.
 */
public class PlayersTracker {
	private static final Logger LOG = LogManager
			.getLogger(PlayersTracker.class);

	private volatile List<Player> players;

	public PlayersTracker() {
		players = new ArrayList<>();
	}

	// Remove player reference for the tracker
	// Returns the reference for a given username
	// I should take care of null values when I call it
	public synchronized Player removePlayer(String username) {
		LOG.debug("Removing player");

		int index = 0;
		Player tmpPlayer = null;

		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				break;
			}

			index++;
		}

		if (index >= players.size()) {
			return null;
		} else {
			tmpPlayer = players.remove(index);

			return tmpPlayer;
		}
	}

	// For a specific username get player reference
	// I should take care of null values when I call it
	public Player getPlayer(String username) {
		LOG.debug("Quering for player");

		Player tmpPlayer = null;

		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				tmpPlayer = player;
			}
		}

		return tmpPlayer;
	}

	// Add player to the tracker
	public synchronized void addPlayer(Player player) {
		LOG.debug("Adding new player");

		players.add(player);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Player player : players) {
			sb.append("username: ").append(player.getUsername())
					.append("word: ").append(player.getWord()).append("loss: ")
					.append(player.getLoss()).append("score: ")
					.append(player.getScore()).append("\n");
		}

		return sb.toString();
	}
}
