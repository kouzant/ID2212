package gr.kzps.id2212.project.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class AgentDB {
	private Map<UUID, AgentItem> db;
	
	public AgentDB() {
		db = new HashMap<>();
	}
	
	public void add(AgentItem item) {
		db.put(item.getId(), item);
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
	
	public void remove(String id) throws AgentNotFound {
		AgentItem agent = get(id);
		db.remove(agent.getId());
	}
}
