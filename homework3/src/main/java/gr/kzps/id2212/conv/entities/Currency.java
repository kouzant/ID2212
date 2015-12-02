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
	@NamedQuery(name = "currency.findByCur", query = "SELECT c FROM Currency c WHERE (c.curFrom = :aFrom AND c.curTo = :aTo) OR (c.curFrom = :aTo AND c.curTo = :aFrom)")
})
public class Currency {
	@Id
	@GeneratedValue
	private Long id;
	private String curFrom;
	private String curTo;
	private Float rate;
	@Transient
	private Float invertedRate;
	
	public Currency() {
		
	}
	
	public Currency(String curFrom, String curTo, Float rate) {
		this.curFrom = curFrom;
		this.curTo = curTo;
		this.rate = rate;
	}
	
	@PostLoad
	@PostPersist
	@PostUpdate
	public void calculateInvertedRate() {
		invertedRate = 1 / rate;
	}

	@Override
	public String toString() {
		return curFrom + " -> " + curTo + " Rate: " + rate + " Inv. rate: " + invertedRate;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurFrom() {
		return curFrom;
	}

	public void setCurFrom(String curFrom) {
		this.curFrom = curFrom;
	}

	public String getCurTo() {
		return curTo;
	}

	public void setTo(String curTo) {
		this.curTo = curTo;
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
