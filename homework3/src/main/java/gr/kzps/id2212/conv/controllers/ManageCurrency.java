package gr.kzps.id2212.conv.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.sessionbeans.CurrencyEJB;

@ManagedBean
@RequestScoped
public class ManageCurrency {
	@EJB
	private CurrencyEJB currencyEJB;
	private Currency currency;
	
	public ManageCurrency() {
		currency = new Currency();
	}
	
	public String storeCurrency() {
		currencyEJB.storeCurrency(currency);
		
		return null;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
