package gr.kzps.id2212.hangman.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentsParser {
	private final String[] args;
	private Options options;

	public ArgumentsParser(String[] args) {
		this.args = args;
		options = new Options();
	}

	public CommandLine parseArgs() throws ParseException {
		Option port = Option.builder("p").longOpt("port").hasArg()
				.desc("Listen port").build();
		
		options.addOption(port);
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		return cmd;
	}
}
