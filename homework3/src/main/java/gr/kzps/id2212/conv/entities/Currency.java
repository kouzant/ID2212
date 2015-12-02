package gr.kzps.id2212.conv.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_CURRENCY")
@NamedQueries({
	@NamedQuery(name = "currency.findAll", query = "SELECT c FROM Currency c"),
	@NamedQuery(name = "currency.findByCur", query = "SELECT c FROM Currency c "
			+ "WHERE (c.from = :ffrom AND c.to =: fto) OR (c.from = :fto AND c.to = :ffrom)")
})
public class Currency {
	@Id
	@GeneratedValue
	private Long id;
	private String from;
	private String to;
	private float rate;
	@Transient
	private float invertedRate;
	
	public Currency() {
		
	}
	
	@PostLoad
	@PostPersist
	@PostUpdate
	public void calculateInvertedRate() {
		invertedRate = 1 / rate;
	}

	@Override
	public String toString() {
		return from + " -> " + to + " Rate: " + rate + " Inv. rate: " + invertedRate;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getInvertedRate() {
		return invertedRate;
	}

	public void setInvertedRate(float invertedRate) {
		this.invertedRate = invertedRate;
	}
}
