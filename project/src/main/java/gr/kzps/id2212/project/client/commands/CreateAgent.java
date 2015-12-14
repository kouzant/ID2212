package gr.kzps.id2212.project.client.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class CreateAgent implements Command {
	private final Logger LOG = LogManager.getLogger(CreateAgent.class);
	private final String queryClass;
	private final String targetIp;
	private final Integer targetPort;
	private Socket socket;
	private ObjectOutputStream outStream;

	public CreateAgent(String queryClass, String targetIp, Integer targetPort) {
		this.queryClass = queryClass;
		this.targetIp = targetIp;
		this.targetPort = targetPort;
	}

	@Override
	public void execute() {
		UUID agentId = UUID.randomUUID();
		LOG.debug("Creating agent with ID: {}", agentId);

		QueryPlanClassLoader loader = new QueryPlanClassLoader(this.getClass().getClassLoader());
		QueryPlan queryPlan = loader.loadPlan(queryClass);

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

			// TODO I should fix homeport
			Agent agent = new AgentImpl(agentId, InetAddress.getByName("localhost"), 5050, query);
			socket = new Socket(InetAddress.getByName(targetIp), targetPort);
			LOG.debug("Sending agent to {}:{}", new Object[] { targetIp, targetPort });
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(agent);
			outStream.flush();
			LOG.debug("Agent sent");
		} catch (ParseException ex) {
			System.out.println("> " + ex.getMessage());
			System.out.print("> ");
		} catch (UnknownHostException ex) {
			System.out.println("> " + ex.getMessage());
			System.out.print("> ");
		} catch (IOException ex) {
			System.out.println("> " + ex.getMessage());
			System.out.print("> ");
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
