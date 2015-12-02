package gr.kzps.id2212.conv.sessionbeans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.exceptions.ResultNotFound;

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
	
	public List<Currency> findAllCurrencies() throws ResultNotFound {
		TypedQuery<Currency> query = em.createNamedQuery("currency.findAll", Currency.class);
		
		List<Currency> result = query.getResultList();
		
		if (!result.isEmpty()) {
			return result;
		}
		
		throw new ResultNotFound();
	}
	
	public Currency findByCur(String from, String to) throws ResultNotFound {
		TypedQuery<Currency> query = em.createNamedQuery("currency.findByCur", Currency.class);
		query.setParameter("aFrom", from);
		query.setParameter("aTo", to);
		
		List<Currency> result = query.getResultList();
		
		if (!result.isEmpty()) {
			return result.get(0);
		}
		
		throw new ResultNotFound("No currency found matching your criteria");
	}
	
	public void removeCurrency(String from, String to) {
		try {
			Currency attached = findByCur(from, to);
			em.remove(attached);
		} catch (ResultNotFound ex) {
			
		}
	}
}
