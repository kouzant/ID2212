package gr.kzps.id2212.marketplace.client;

import java.io.Serializable;

public class Client implements Serializable {
	
	private static final long serialVersionUID = 8526692508359233604L;
	
	private final String name;
	private final String email;
	
	public Client(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
}
