package gr.kzps.id2212.conv.sessionbeans;

import javax.ejb.Local;

import gr.kzps.id2212.conv.exceptions.ResultNotFound;

@Local
public interface ConvertEJB {
	public Float convertCurrency(String curFrom, String curTo, Float amount) throws ResultNotFound;
}
