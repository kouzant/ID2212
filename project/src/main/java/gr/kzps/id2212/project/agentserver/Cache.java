package gr.kzps.id2212.project.agentserver;

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
	
	public Integer getAgentPort() {
		return agentPort;
	}
	
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
