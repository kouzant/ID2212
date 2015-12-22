package gr.kzps.id2212.project.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


import gr.kzps.id2212.project.client.query.QueryParameter;

/**
 * Export agent's result set to a file
 * @author Antonis Kouzoupis
 *
 */
public class ExportResults {
	private final String RESULTS_DIR = "results";
	
	private final AgentItem agentItem;

	/**
	 * @param agentItem Item to export
	 */
	public ExportResults(AgentItem agentItem) {
		this.agentItem = agentItem;
		Path outDir = Paths.get(RESULTS_DIR);
		if (!outDir.toFile().exists()) {
			outDir.toFile().mkdir();
		}
	}

	/**
	 * Format the agent item
	 * @return A string representation of the result.
	 */
	public String export() {
		StringBuilder sb = new StringBuilder();
		sb.append("=== Results of ").append(agentItem.getId()).append(" agent ===\n");
		sb.append("-> Query:\n");
		sb.append("Title: ");
		sb.append(exportQueryParameter(agentItem.getQuery().getTitle()));
		sb.append("\n");

		sb.append("Author: ");
		sb.append(exportQueryParameter(agentItem.getQuery().getAuthor()));
		sb.append("\n");

		sb.append("Date: ");
		sb.append(exportQueryParameter(agentItem.getQuery().getDate()));
		sb.append("\n");

		sb.append("Keywords: ");
		sb.append(exportQueryParameter(agentItem.getQuery().getKeywords()));
		sb.append("\n");
		sb.append("\n");

		sb.append(compileVisitedServers());
		sb.append("\n");
		sb.append(compileResultSet());
		writeToFile(agentItem.getId().toString(), sb);

		return sb.toString();
	}

	/**
	 * Write string to file
	 * @param agentId UUID of the agent
	 * @param sb String builder containing the output
	 */
	private void writeToFile(String agentId, StringBuilder sb) {
		Path resultFile = Paths.get(RESULTS_DIR, agentId);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile.toFile()))) {
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Build the result set
	 * @return A string builder with the output string of the result filenames
	 */
	private StringBuilder compileResultSet() {
		StringBuilder sb = new StringBuilder();

		sb.append("-> Result set:").append("\n");
		sb.append("\t Server \t||\tFilename").append("\n");
		sb.append("-----------------------------------------------").append("\n");

		if (agentItem.getResultSet().isEmpty()) {
			sb.append("\tEmpty \t\t\t Empty\n");
		} else {
			agentItem.getResultSet().stream()
					.forEach(r -> sb.append(r.getServer()).append("\t|| ")
							.append(r.getFileName()).append("\n"));

		}
		
		return sb;
	}

	/**
	 * Build the visited servers output string
	 * @return String builder with the visited servers
	 */
	private StringBuilder compileVisitedServers() {
		StringBuilder sb = new StringBuilder();

		sb.append("-> Visited Servers:").append("\n");
		agentItem.getVisitedServers().stream()
				.forEach(s -> sb.append(s.getServer()).append(":").append(s.getPort()).append("\n"));

		return sb;
	}

	/**
	 * Build the query parameters string
	 * @param parameter A query parameter
	 * @return String representation of the query parameter
	 */
	private <P extends QueryParameter<T>, T> String exportQueryParameter(P parameter) {
		
		return parameter.export();
	}
}
