package gr.kzps.id2212.project.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class AgentDB {
	private Map<UUID, AgentItem> db;
	private final Lock lock;
	
	public AgentDB() {
		db = new HashMap<>();
		lock = new ReentrantLock();
	}
	
	public void add(AgentItem item) {
		lock.lock();
		db.put(item.getId(), item);
		lock.unlock();
	}
	
	public AgentItem get(String id) throws AgentNotFound {
		Set<UUID> keySet = db.keySet();
		// Id might be a subset of the whole UUID
		List<UUID> found = keySet.stream().filter(k -> k.toString().contains(id))
				.collect(Collectors.toList());
		
		if (found.size() > 1) {
			throw new AgentNotFound("Found more than one entry with this ID.");
		} else if (found.size() == 0) {
			throw new AgentNotFound();
		} else {
			return db.get(found.get(0));
		}
	}
	
	public Collection<AgentItem> getValues() {
		return db.values();
	}
	
	public AgentItem remove(String id) throws AgentNotFound {
		AgentItem agent = get(id);
		lock.lock();
		AgentItem result = db.remove(agent.getId());
		lock.unlock();
		
		return result;
	}
}
