package gr.kzps.id2212.project.client.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;
import gr.kzps.id2212.project.client.AgentServer;
import gr.kzps.id2212.project.client.AgentStatus;
import gr.kzps.id2212.project.client.agent.Agent;
import gr.kzps.id2212.project.client.agent.AgentImpl;
import gr.kzps.id2212.project.client.classloader.QueryPlanClassLoader;
import gr.kzps.id2212.project.client.query.DateParameter;
import gr.kzps.id2212.project.client.query.KeywordsParameter;
import gr.kzps.id2212.project.client.query.Query;
import gr.kzps.id2212.project.client.query.QueryParameter;
import gr.kzps.id2212.project.client.query.QueryPlan;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;
import gr.kzps.id2212.project.utils.Utilities;

public class CreateAgent extends CommandAbstr {
	private final String queryClass;
	private final String targetIp;
	private final Integer targetBasePort;
	private final AgentServer server;
	private Socket socket;
	private ObjectOutputStream outStream;

	public CreateAgent(String queryClass, AgentServer server, String targetIp, Integer targetBasePort) {
		this.queryClass = queryClass;
		this.targetIp = targetIp;
		this.targetBasePort = targetBasePort;
		this.server = server;
	}

	@Override
	public void execute(AgentDB db) {
		UUID agentId = UUID.randomUUID();
		//LOG.debug("Creating agent with ID: {}", agentId);

		QueryPlanClassLoader loader = new QueryPlanClassLoader(this.getClass().getClassLoader());
		QueryPlan queryPlan = loader.loadPlan(queryClass);
		
		if (queryPlan == null) {
			console.print("Fatal! Could not find query class file " + queryClass);
			
			return;
		}
		
		QueryParameter<String> author = queryPlan.getAuthor();
		QueryParameter<String> title = queryPlan.getTitle();
		KeywordsParameter<List<String>> keywords = queryPlan.getKeywords();
		DateParameter<String> date = queryPlan.getDate();

		Date parsedDate = new Date();
		try {
			if (date.getParameterSwitch().equals(ParameterSwitch.ON)) {
				Utilities utils = new Utilities();
				parsedDate = utils.parseDate(date.getParameter());
			}
			DateParameter<Date> queryDate = new DateParameter<Date>(parsedDate, date.getParameterSwitch(),
					date.getOperator());

			Query query = new Query(author, keywords, title, queryDate);

			Agent agent = new AgentImpl(agentId, InetAddress.getByName("localhost"),
					server.getHomeport(), query);
			Integer targetServicePort = server.getServicePort(targetIp, targetBasePort);
			
			// Connect to the service port
			socket = new Socket(InetAddress.getByName(targetIp), targetServicePort);
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(agent);
			outStream.flush();
			AgentItem item = new AgentItem(agentId, query, AgentStatus.SEARCHING);
			db.add(item);
			console.print("Agent ID: " + agentId.toString());
			console.printPrompt();
		} catch (ParseException | IOException | ClassNotFoundException ex) {
			console.print(ex.getMessage());
			console.printPrompt();
		} finally {
			try {
				if (outStream != null)
					outStream.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
