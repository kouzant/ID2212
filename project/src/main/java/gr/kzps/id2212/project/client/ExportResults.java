package gr.kzps.id2212.project.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.query.DateParameter;
import gr.kzps.id2212.project.client.query.KeywordsParameter;
import gr.kzps.id2212.project.client.query.QueryParameter;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

public class ExportResults {
	private final Logger LOG = LogManager.getLogger(ExportResults.class);
	private final AgentItem agentItem;
	
	public ExportResults(AgentItem agentItem) {
		this.agentItem = agentItem;
		Path outDir = Paths.get("results");
		if (!outDir.toFile().exists()) {
			outDir.toFile().mkdir();
		}
	}
	
	// Return type is temporal!
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
		
		writeToFile(agentItem.getId().toString(), sb);
		
		return sb.toString();
	}
	
	private void writeToFile(String agentId, StringBuilder sb) {
		Path resultFile = Paths.get("results", agentId);
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile.toFile()))) {
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private <P extends QueryParameter<T>, T> String exportQueryParameter(P parameter) {
		if (parameter.getParameterSwitch().equals(ParameterSwitch.ON)) {
			T field = parameter.getParameter();
			
			if (field instanceof String) {
				return (String) field;
			} else if (field instanceof Date) {
				DateParameter<Date> date = (DateParameter<Date>) parameter;
				return date.getOperator().toString() + " " + (String) field.toString();
			} else if (field instanceof List<?>) {
				KeywordsParameter<List<String>> keywords = (KeywordsParameter<List<String>>) parameter;
 				return field.toString() + " Success threshold: " + keywords.getSuccessThreshold(); 
			} else {
				return "Unknown parameter";
			}
		} else {
			return "Inactive field";
		}
	}
}
