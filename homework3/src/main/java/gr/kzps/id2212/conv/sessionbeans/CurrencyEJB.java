package gr.kzps.id2212.conv.sessionbeans;

import java.util.List;

import javax.ejb.Local;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.exceptions.ResultNotFound;

@Local
public interface CurrencyEJB {
	public Currency storeCurrency(Currency currency);
	public List<Currency> findAllCurrencies() throws ResultNotFound;
	public Currency findByCur(String from, String to) throws ResultNotFound;
	public Currency removeCurrency(String from, String to);
}