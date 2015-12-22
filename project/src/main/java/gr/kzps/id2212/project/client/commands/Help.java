package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;

public class Help extends CommandAbstr {

	@Override
	public void execute(AgentDB db) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("+++ Moneypenny help +++\n");
		sb.append("> create QUERY_CLASS TARGET_IP TARGET_PORT\n");
		sb.append(">\t* Create and send an agent with the specified query\n");
		sb.append("> status\n");
		sb.append(">\t* Print the status of created agents\n");
		sb.append("> delete AGENT_ID\n");
		sb.append(">\t* Remove the agent with that (partial) ID from memory \n\t but keep the result file\n");
		sb.append("> purge AGENT_ID\n");
		sb.append(">\t* Remove both the agent from memory and the result file\n");
		sb.append("> whereis AGENT_ID\n");
		sb.append(">\t* Locates the specified agent in the network\n");
		sb.append("> cancel AGENT_ID\n");
		sb.append(">\t* Cancel a running agent and return home\n");
		sb.append("> help\n");
		sb.append(">\t* Print this menu\n");
		sb.append("> exit\n");
		sb.append(">\t* Terminate the client");
		
		console.print(sb.toString());
		console.printPrompt();
	}

}
