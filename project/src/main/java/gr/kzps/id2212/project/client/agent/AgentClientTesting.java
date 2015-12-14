package gr.kzps.id2212.project.client.agent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.classloader.QueryPlanClassLoader;
import gr.kzps.id2212.project.client.query.DateParameter;
import gr.kzps.id2212.project.client.query.Query;
import gr.kzps.id2212.project.client.query.QueryParameter;
import gr.kzps.id2212.project.client.query.QueryPlan;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;
import gr.kzps.id2212.project.utils.Utilities;

/*
 * Just for TESTING purposes
 */
public class AgentClientTesting {
	private static final Logger LOG = LogManager.getLogger(AgentClientTesting.class);
	private static Socket socket;
	
	public static void main(String[] args) throws ParseException {
		try {
			InetAddress server = InetAddress.getByName("localhost");
			Integer port = 6060;
			UUID id = UUID.randomUUID();
			QueryPlanClassLoader loader = new QueryPlanClassLoader(AgentClientTesting.class.getClassLoader());
			QueryPlan queryPlan = loader.loadPlan("userplan.UserQueryPlan");
			
			QueryParameter<String> author = queryPlan.getAuthor();
			QueryParameter<String> title = queryPlan.getTitle();
			DateParameter<String> date = queryPlan.getDate();
			
			Date parsedDate = new Date();
			Utilities util = new Utilities();
			if (!date.getParameterSwitch().equals(ParameterSwitch.OFF)) {
				parsedDate = util.parseDate(date.getParameter());
			}
			DateParameter<Date> formDate = new DateParameter<Date>(parsedDate, date.getParameterSwitch(), date.getOperator());
			QueryParameter<List<String>> keywords = queryPlan.getKeywords();
			
			Query query = new Query(author, keywords, title, formDate);
			
			Agent agent = new AgentImpl(id, InetAddress.getByName("localhost"),
					5050, query);

			LOG.debug("Agent created and sending to server");
			socket = new Socket(server, port);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			
			outStream.writeObject(agent);
			
			outStream.flush();
			if (outStream != null)
				outStream.close();
			if (socket != null)
				socket.close();
			
			ServerSocket sSocket = new ServerSocket(5050);
			Socket cSocket = sSocket.accept();
			
			ObjectInputStream inStream = new ObjectInputStream(cSocket.getInputStream());
			Agent comingAgent = (Agent) inStream.readObject();
			
			if (inStream != null)
				inStream.close();
			if (cSocket != null)
				cSocket.close();
			if (sSocket != null)
				sSocket.close();
			
			LOG.debug(comingAgent.getResult());
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

}
