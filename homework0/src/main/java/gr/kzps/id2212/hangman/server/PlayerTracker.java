package gr.kzps.id2212.hangman.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerTracker {
	private static final Logger LOG = LogManager.getLogger(PlayerTracker.class);
	
	private volatile List<Player> players;

	public PlayerTracker() {
		players = new ArrayList<>();
	}

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

	public synchronized void addPlayer(Player player) {
		LOG.debug("Adding new player");

		players.add(player);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Player player : players) {
			sb.append("username: ").append(player.getUsername())
					.append("word: ").append(player.getWord())
					.append("score: ").append(player.getScore()).append("\n");
		}

		return sb.toString();
	}
}
