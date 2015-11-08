package gr.kzps.id2212.hangman.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Load the words from the text file
 */
public class Dictionary {
	private static final Logger LOG = LogManager.getLogger(Dictionary.class);

	private static Dictionary self = null;
	private List<String> dict;
	private Random rand;

	private Dictionary() {
		dict = new ArrayList<String>();
		rand = new Random();
		loadWords(dict);
	}

	// Instantiate once
	public static Dictionary getInstance() {
		if (self == null) {
			self = new Dictionary();
		}

		return self;
	}

	// Get a random word from the dictionary
	public String getWord() {
		Integer index = rand.nextInt(dict.size());

		return dict.get(index);
	}

	// Load words from dictionary to memory only once
	private void loadWords(List<String> dict) {
		LOG.debug("Loading dictionary...");
		Path dicPath = Paths.get("src", "main", "resources", "words.txt");

		try (Stream<String> words = Files.lines(dicPath)) {
			words.forEach(word -> {
				dict.add(word);
			});
		} catch (IOException ex) {
			LOG.error("Error while loading dictionary");
			ex.printStackTrace();
		}
	}
}