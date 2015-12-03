package gr.kzps.id2212.conv.sessionbeans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.exceptions.ResultNotFound;

@Stateless
@LocalBean
public class ConvertEJBImpl implements ConvertEJB {

	@EJB
	private CurrencyEJB currencyEJB;
	
	public ConvertEJBImpl() {
		super();
	}
	
	@Override
	public Float convertCurrency(String curFrom, String curTo, Float amount) throws ResultNotFound {
		Currency currency = currencyEJB.findByCur(curFrom, curTo);
		
		if ((curFrom.equals(currency.getCurFrom()) &&
				curTo.equals(currency.getCurTo()))) {
			return amount * currency.getRate(); 
		} else {
			return amount * currency.getInvertedRate();
		}
	}

}
