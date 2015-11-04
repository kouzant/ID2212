package gr.kzps.id2212.hangman;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gr.kzps.id2212.hangman.server.Player;
import gr.kzps.id2212.hangman.server.PlayersTracker;

public class PlayerTrackerTest {
	public static void main(String[] args) {
		PlayersTracker players = new PlayersTracker();
		ExecutorService exec = Executors.newFixedThreadPool(2);
		PlayerTrackerTest ptt = new PlayerTrackerTest();
		exec.execute(ptt.new Worker1(players));
		exec.execute(ptt.new Worker2(players));

		exec.shutdown();
		
		System.out.println("sfdsf");

		try {
			if (exec.awaitTermination(30, TimeUnit.SECONDS)) {
				System.out.println("Threads are done");
				System.out.println(players);

			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	protected class Worker1 implements Runnable {
		private PlayersTracker pt;

		public Worker1(PlayersTracker pt) {
			this.pt = pt;
		}

		@Override
		public void run() {
			Player player = new Player("antonis", "skata");
			System.err.println("Worker 1");
			pt.addPlayer(player);
			Player ret = pt.removePlayer("antonis");
			if (ret != null) {
				System.err.println("Worker1 : " + ret.toString());
			} else {
				System.err.println("Worker1: NULL");
			}
		}
	}

	protected class Worker2 implements Runnable {
		private PlayersTracker pt;

		public Worker2(PlayersTracker pt) {
			this.pt = pt;
		}

		@Override
		public void run() {
			Player player = new Player("kouroufin", "morgoth");
			System.err.println("Worker 2");
			//pt.addPlayer(player);
			Player ret = pt.removePlayer("antonis");
			if (ret != null) {
				System.err.println("Worker2 : " + ret.toString());
			} else {
				System.err.println("Worker2: NULL");
			}
		}
	}
}
