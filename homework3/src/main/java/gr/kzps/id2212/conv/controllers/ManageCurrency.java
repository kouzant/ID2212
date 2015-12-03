package gr.kzps.id2212.conv.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.exceptions.ResultNotFound;
import gr.kzps.id2212.conv.sessionbeans.CurrencyEJB;

@ManagedBean
@RequestScoped
public class ManageCurrency {
	@EJB
	private CurrencyEJB currencyEJB;
	private Currency currency;
	private List<Currency> storedCurrencies;
	private String selectedCurrency;
	
	public ManageCurrency() {
		currency = new Currency();
	}
	
	@PostConstruct
	public void init() {
		try {
			storedCurrencies = currencyEJB.findAllCurrencies();
		} catch (ResultNotFound ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No currency", "No currencies stored so far");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	public String storeCurrency() {
		currencyEJB.storeCurrency(currency);
		
		return "success";
	}
	
	public String currencyConvertor() {
		return "currencyConvertor";
	}
	
	public String deleteCurrency() {
		System.out.println("TOKENS: " + selectedCurrency);
		String[] tokens = selectedCurrency.split("-");
		
		
		currencyEJB.removeCurrency(tokens[0], tokens[1]);
		
		return "success";
	}

	public List<Currency> getStoredCurrencies() {
		return storedCurrencies;
	}

	public void setStoredCurrencies(List<Currency> storedCurrencies) {
		this.storedCurrencies = storedCurrencies;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getSelectedCurrency() {
		return selectedCurrency;
	}

	public void setSelectedCurrency(String selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}
	
}
