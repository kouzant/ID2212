package gr.kzps.id2212.conv.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.sessionbeans.CurrencyEJB;

public class CurrencyEJBTest {
	private static EJBContainer ec;
	private static Context ctx;
	
	@BeforeClass
	public static void initContainer() throws Exception {
		ec = EJBContainer.createEJBContainer();
		ctx = ec.getContext();
	}
	
	@AfterClass
	public static void closeContainer() throws Exception {
		ec.close();
	}
	
	public void testCurrency() throws Exception {
		Currency currency = new Currency("EUR", "USD", 1.058427F);
		CurrencyEJB currencyEJB = (CurrencyEJB) ctx.lookup("java:global/homework3/CurrencyEJB");
		
		// Check persistence
		currency = currencyEJB.storeCurrency(currency);
		assertNotNull("Currency ID should not be null", currency.getId());
		
		// Check find all query
		List<Currency> currencies = currencyEJB.findAllCurrencies();
		assertNotEquals(0, currencies.size());
		
		// Check findBy query
		Currency ret = currencyEJB.findByCur("EUR", "USD");
		assertNotNull("Existing currerncy should not be null", ret);
	}
}
