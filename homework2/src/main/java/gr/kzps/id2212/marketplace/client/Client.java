package gr.kzps.id2212.marketplace.client;

import gr.kzps.id2212.marketplace.server.database.entities.ClientEntity;
import java.io.Serializable;

public class Client implements Serializable {

    private static final long serialVersionUID = 8526692508359233604L;

    private String name;
    private final String email;
    private final String password;

    public Client(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Client(ClientEntity clientFromDb) {
       this.name = clientFromDb.getName();
       this.email = clientFromDb.getEmail();
       this.password = clientFromDb.getPassword();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	this.name = name;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
