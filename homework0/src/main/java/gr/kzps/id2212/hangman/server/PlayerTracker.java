package gr.kzps.id2212.hangman.server;

import java.util.ArrayList;
import java.util.List;

public class PlayerTracker {
	private List<Player> players;
	
	public PlayerTracker() {
		players = new ArrayList<>();
	}
	
	// I should take care of null values when I call it
	public Player removePlayer(String username) {
		int index = 0;
		Player tmpPlayer = null;
		
		for (Player player: players) {
			if (player.getUsername().equals(username)) {
				break;
			}
			
			index++;
		}
		
		if (index >= players.size()) {
			return null;
		} else {
			synchronized(this) {
				tmpPlayer = players.remove(index);
			}
			
			return tmpPlayer;
		}
	}
	
	// I should take care of null values when I call it
	public Player getPlayer(String username) {
		Player tmpPlayer = null;
		
		for (Player player: players) {
			if (player.getUsername().equals(username)) {
				tmpPlayer = player;
			}
		}
		
		return tmpPlayer;
	}
	
	public synchronized void addPlayer(Player player) {
		players.add(player);
	}
}
