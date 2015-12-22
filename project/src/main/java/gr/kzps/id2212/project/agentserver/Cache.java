package gr.kzps.id2212.project.agentserver;

/**
 * A place to cache some elements for the agent server
 * @author Antonis Kouzoupis
 *
 */
public class Cache {
	private static Cache instance;
	private Integer agentPort;
	private String searchPath;
	
	private Cache() {
		super();
	}
	
	public static Cache getInstance() {
		if (instance == null) {
			instance = new Cache();
		}
		
		return instance;
	}
	
	/**
	 * Get running port of the agent service
	 * @return Running port of the agent service
	 */
	public Integer getAgentPort() {
		return agentPort;
	}
	
	/**
	 * Get filesystem path for the agent to search
	 * @return Path with files to search
	 */
	public String getSearchPath() {
		return searchPath;
	}
	
	public void setAgentPort(Integer agentPort) {
		this.agentPort = agentPort;
	}
	
	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}
}
