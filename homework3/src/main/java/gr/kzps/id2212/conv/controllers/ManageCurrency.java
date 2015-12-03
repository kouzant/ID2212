package gr.kzps.id2212.conv.controllers;

import java.util.List;
import java.util.Optional;

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
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No currency",
					"No currencies stored so far");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public String storeCurrency() {
		Currency newCurrency = currencyEJB.storeCurrency(currency);
		
		storedCurrencies.add(newCurrency);

		return "success";
	}

	public String currencyConvertor() {
		return "currencyConvertor";
	}

	public String deleteCurrency() {

		if (selectedCurrency != null) {
			String[] tokens = selectedCurrency.split("-");

			currencyEJB.removeCurrency(tokens[0], tokens[1]);
			
			Optional<Currency> deletedCurrencyOpt = storedCurrencies.stream()
					.filter(c -> c.getCurFrom().equals(tokens[0])
							&& c.getCurTo().equals(tokens[1]))
					.findFirst();
			
			storedCurrencies.remove(deletedCurrencyOpt.get());
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"No currency selected", "You have not selected any currency to delete");
			FacesContext.getCurrentInstance().addMessage("listForm:list", msg);
		}
		
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
