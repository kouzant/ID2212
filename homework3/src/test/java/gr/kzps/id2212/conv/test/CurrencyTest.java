package gr.kzps.id2212.conv.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.exceptions.ResultNotFound;
import gr.kzps.id2212.conv.sessionbeans.CurrencyEJB;

public class CurrencyTest {
	private static EJBContainer ec;
	private static Context ctx;
	private static CurrencyEJB currencyEJB;
	private static Float rate = 1.058427F;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void initContainer() throws Exception {
		ec = EJBContainer.createEJBContainer();
		ctx = ec.getContext();
		
		currencyEJB = (CurrencyEJB) ctx.lookup("java:global/classes/CurrencyEJBImpl!gr.kzps.id2212.conv.sessionbeans.CurrencyEJBImpl");
	}
	
	@AfterClass
	public static void closeContainer() throws Exception {
		ec.close();
	}
	
	@Before
	public void addCurrency() throws Exception {
		Currency currency = new Currency("EUR", "USD", rate);
		currency = currencyEJB.storeCurrency(currency);
	}
	
	@After
	public void removeCurrency() throws Exception {
		currencyEJB.removeCurrency("EUR", "USD");
	}
	
	@Test
	public void currencyFindAllTest() throws Exception {
		List<Currency> res = currencyEJB.findAllCurrencies();
		
		System.out.println(">>> Currencies stored:");
		res.stream().forEach(c -> System.out.println(c));
		
		assertNotEquals(0, res.size());
	}
	
	@Test
	public void currencyFindByNormalOrder() throws Exception {
		Currency res = currencyEJB.findByCur("EUR", "USD");
		
		assertNotNull("Currency EUR -> USD should not be null", res);
	}
	
	@Test
	public void currencyFindByNotNormalOrder() throws Exception {
		List<Currency> resList = currencyEJB.findAllCurrencies();
		System.out.println(">>> Number of rows: " + resList.size());
		
		Currency res = currencyEJB.findByCur("USD", "EUR");
		
		assertNotNull("Currency USD -> EUR should not be null", res);
	}
	
	@Test
	public void currencyFindByNotExist() throws Exception {
		thrown.expect(ResultNotFound.class);
		Currency res = currencyEJB.findByCur("XXX", "YYY");
		
		assertNull("Currency XXX -> YYY should be null", res);
	}
	
	@Test
	public void currencyInvertedRate() throws Exception {
		Currency res = currencyEJB.findByCur("EUR", "USD");
		
		assertEquals("Inverted rate should be " + 1/rate, 1/rate, res.getInvertedRate(), 0.00006);
	}
	
	@Test
	public void removeCurrencyTest() throws Exception {
		currencyEJB.removeCurrency("EUR", "USD");
		thrown.expect(ResultNotFound.class);
		currencyEJB.findByCur("EUR", "USD");
	}
}
