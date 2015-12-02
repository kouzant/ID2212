package gr.kzps.id2212.conv.sessionbeans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gr.kzps.id2212.conv.entities.Currency;

@Stateless
@LocalBean
public class CurrencyEJBImpl implements CurrencyEJB {
	@PersistenceContext(unitName = "id2212Hw3")
	private EntityManager em;
	
	public CurrencyEJBImpl() {
		super();
	}
	
	public Currency storeCurrency(Currency currency) {
		em.persist(currency);
		
		return currency;
	}
	
	public List<Currency> findAllCurrencies() {
		TypedQuery<Currency> query = em.createNamedQuery("currency.findAll", Currency.class);
		
		return query.getResultList();
	}
	
	public Currency findByCur(String from, String to) {
		TypedQuery<Currency> query = em.createNamedQuery("currency.findByCur", Currency.class);
		query.setParameter("from", from);
		query.setParameter("to", to);
		
		return query.getSingleResult();
	}
	
}
