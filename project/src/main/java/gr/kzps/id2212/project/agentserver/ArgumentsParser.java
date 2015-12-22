package gr.kzps.id2212.project.agentserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Parse command line arguments
 * @author Antonis Kouzoupis
 *
 */
public class ArgumentsParser {
	private final String[] args;
	private final Options options;
	private final CommandLineParser parser;
	
	public ArgumentsParser(String[] args) {
		this.args = args;
		options = new Options();
		parser = new DefaultParser();
	}
	
	/**
	 * Parse command-line arguments
	 * @return Command-line arguments
	 * @throws ParseException
	 */
	public CommandLine parseArgs() throws ParseException {
		Option serverId = Option.builder("i").longOpt("id").hasArg().required(true)
				.desc("Agent server Identifier").build();
		
		Option searchPath = Option.builder("s").longOpt("searchPath").hasArg()
				.required(true).desc("Search path for the agent").build();
		
		Option serverPort = Option.builder("ap").longOpt("agentPort").hasArg()
				.desc("Inter-agent communicating port").build();
		
		Option basePort = Option.builder("bp").longOpt("basePort").hasArg()
				.desc("Service discovery port").build();
		
		Option bootStrapNode = Option.builder("bsn").longOpt("bootstrap").hasArg()
				.desc("Node to bootstrap from").build();
		
		Option bootStrapPort = Option.builder("bsp").longOpt("bootstrapPort").hasArg()
				.desc("Port of the bootstrap node").build();
		
		options.addOption(serverId);
		options.addOption(searchPath);
		options.addOption(serverPort);
		options.addOption(basePort);
		options.addOption(bootStrapNode);
		options.addOption(bootStrapPort);
		
		return parser.parse(options, args);
	}
	
	/**
	 * Get argument options
	 * @return Command-line options
	 */
	public Options getOptions() {
		return options;
	}
}
