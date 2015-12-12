package gr.kzps.id2212.project.client;

import java.util.ArrayList;
import java.util.List;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public class AgentImpl implements Agent, Runnable {

	private static final long serialVersionUID = 4772482720958169130L;
	
	private List<PeerAgent> serversVisited;
	
	public AgentImpl() {
		serversVisited = new ArrayList<>();
	}
	
	@Override
	public void run() {
		String agentName = Thread.currentThread().getName();
		System.out.println(agentName + " is doing something");
	}

	@Override
	public void agentArrived() {
		Thread agentThread = new Thread(this);
		agentThread.setName("Agent-Thread");
		agentThread.start();
	}
}
