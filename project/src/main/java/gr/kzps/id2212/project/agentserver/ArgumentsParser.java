package gr.kzps.id2212.project.agentserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/*
 * Parse command line arguments
 */
public class ArgumentsParser {
	private final String[] args;
	private final Options options;
	
	public ArgumentsParser(String[] args) {
		this.args = args;
		options = new Options();
	}
	
	public CommandLine parseArgs() throws ParseException {
		Option serverId = Option.builder("i").longOpt("id").hasArg().required(true)
				.desc("Agent server Identifier").build();
		
		Option serverPort = Option.builder("sp").longOpt("serverPort").hasArg()
				.desc("Inter-agent communicating port").build();
		
		Option basePort = Option.builder("bs").longOpt("basePort").hasArg()
				.desc("Service discovery port").build();
		
		options.addOption(serverId);
		options.addOption(serverPort);
		options.addOption(basePort);
		
		CommandLineParser parser = new DefaultParser();
		
		return parser.parse(options, args);
	}
}
