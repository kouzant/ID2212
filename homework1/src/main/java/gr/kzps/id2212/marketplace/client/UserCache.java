package gr.kzps.id2212.marketplace.client;

public class UserCache {
	private static UserCache self = null;
	private Client currentUser;
	private Callbacks callbacks;
	
	private UserCache() {
		currentUser = null;
		callbacks = null;
	}
	
	public static UserCache getInstance() {
		if (self == null) {
			self = new UserCache();
		}
		
		return self;
	}
	
	public Client getCurrentUser() {
		return currentUser;
	}
	
	public Callbacks getCallbacks() {
		return callbacks;
	}
	
	public void setCurrentUser(Client currentUser) {
		this.currentUser = currentUser;
	}
	
	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
	}
}
