package gr.kzps.id2212.conv.sessionbeans;

import java.util.List;

import javax.ejb.Local;

import gr.kzps.id2212.conv.entities.Currency;

@Local
public interface CurrencyEJB {
	public void storeCurrency(Currency currency);
	public List<Currency> findAllCurrencies();
	public Currency findByCur(String from, String to);
}
