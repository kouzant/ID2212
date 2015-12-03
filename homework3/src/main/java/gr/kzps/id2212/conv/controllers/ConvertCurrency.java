package gr.kzps.id2212.conv.controllers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	private List<String> curList;
	private UserSelection userSelection;
	
	public ConvertCurrency() {
		userSelection = new UserSelection();
	}
	
	@PostConstruct
	public void init() {
		try {
			storedCurrencies = currencyEJB.findAllCurrencies();
			curList = storedCurrencies.stream()
					.map(c -> c.getCurFrom())
					.collect(Collectors.toList());
			List<String> tmpTo = storedCurrencies.stream()
					.map(c -> c.getCurTo())
					.collect(Collectors.toList());
			
			curList.addAll(tmpTo);
		} catch (ResultNotFound ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No currency", "No currencies stored so far");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	public String convert() {
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
	
	public List<String> getCurList() {
		return curList;
	}

	public void setCurList(List<String> curList) {
		this.curList = curList;
	}
}
