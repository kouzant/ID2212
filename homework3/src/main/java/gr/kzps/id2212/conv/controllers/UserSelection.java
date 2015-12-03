package gr.kzps.id2212.conv.controllers;

public class UserSelection {
	private String curFrom;
	private String curTo;
	private Float amount;
	private Float converted;
	
	public UserSelection() {
		super();
	}

	public String getCurFrom() {
		return curFrom;
	}

	public void setCurFrom(String curFrom) {
		this.curFrom = curFrom;
	}

	public String getCurTo() {
		return curTo;
	}

	public void setCurTo(String curTo) {
		this.curTo = curTo;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getConverted() {
		return converted;
	}

	public void setConverted(Float converted) {
		this.converted = converted;
	}
	
}
