package gr.kzps.id2212.marketplace.server.database.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "T_CLIENT")
public class ClientEntity {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "EMAIL", unique=true)
	private String email;
        @Column(name = "PASSWORD")
	private String password;
        @Column(name = "ITEMS_BOUGHT")
        private int itemsBought;
        @Column(name = "ITEMS_SOLD")
        private int itemsSold;       
//	@OneToOne(cascade=CascadeType.ALL)
//	private IdCard idCard;
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
//	private List<Phone> phones;
        public ClientEntity(){
            
        }
        
	public ClientEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
                this.itemsBought = 0;
                this.itemsBought = 0;
	}
	
	public Long getId() {
		return id;
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
        
        public int getItemsSold(){
            return itemsSold;
        }
        
        public int getItemsBought(){
            return itemsBought;
        }
	public void setEmail(String email) {
		this.email = email;
	}
        
        public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Name: " + name + "  email: " + email + "  Passord: " + password;
	}
}
