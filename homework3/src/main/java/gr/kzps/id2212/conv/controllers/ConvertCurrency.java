package gr.kzps.id2212.conv.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import gr.kzps.id2212.conv.entities.Currency;
import gr.kzps.id2212.conv.exceptions.ResultNotFound;
import gr.kzps.id2212.conv.sessionbeans.ConvertEJB;
import gr.kzps.id2212.conv.sessionbeans.CurrencyEJB;

@ManagedBean
@RequestScoped
public class ConvertCurrency {

	@EJB
	private CurrencyEJB currencyEJB;
	@EJB
	private ConvertEJB convertEJB;
	private List<Currency> storedCurrencies;
	private List<String> printableCurrencies;
	private String rawUserSelection;
	private UserSelection userSelection;
	
	
	public ConvertCurrency() {
		userSelection= new UserSelection();
		userSelection.setAmount(0F);
		printableCurrencies = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		try {
			storedCurrencies = currencyEJB.findAllCurrencies();
			
			for(Currency cur: storedCurrencies) {
				printableCurrencies.add(buildString(cur));
				printableCurrencies.add(buildReverseString(cur));
			}
			
		} catch (ResultNotFound ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No currency", "No currencies stored so far");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	private String buildReverseString(Currency cur) {
		return cur.getCurTo() + "-" + cur.getCurFrom();
	}
	
	private String buildString(Currency cur) {
		return cur.getCurFrom() + "-" + cur.getCurTo();
	}
	
	public String manageCurrencies() {
		return "manageCurrencies";
	}
	
	public String convert() {
		if (rawUserSelection == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No available currency", "There is no currency stored");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			return "success";
		}
		
		String[] tokens = rawUserSelection.split("-");
		userSelection.setCurFrom(tokens[0]);
		userSelection.setCurTo(tokens[1]);
		if(userSelection.getCurFrom().equals(userSelection.getCurTo())) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Same currency", "You have selected the same currency");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			try {
				Float result = convertEJB.convertCurrency(userSelection.getCurFrom(), userSelection.getCurTo(), userSelection.getAmount());
				userSelection.setConverted(result);
			} catch (ResultNotFound ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Not found in DB", "Could not find currency in DB");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		
		return "success";
	}

	
	public UserSelection getUserSelection() {
		return userSelection;
	}

	public void setUserSelection(UserSelection userSelection) {
		this.userSelection = userSelection;
	}

	public List<Currency> getStoredCurrencies() {
		return storedCurrencies;
	}

	public void setStoredCurrencies(List<Currency> storedCurrencies) {
		this.storedCurrencies = storedCurrencies;
	}

	public String getRawUserSelection() {
		return rawUserSelection;
	}

	public void setRawUserSelection(String rawUserSelection) {
		this.rawUserSelection = rawUserSelection;
	}

	public List<String> getPrintableCurrencies() {
		return printableCurrencies;
	}

	public void setPrintableCurrencies(List<String> printableCurrencies) {
		this.printableCurrencies = printableCurrencies;
	}
	
}
