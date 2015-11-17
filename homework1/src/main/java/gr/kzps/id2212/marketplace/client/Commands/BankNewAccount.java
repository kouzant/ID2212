package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

public class BankNewAccount  extends BaseCommand {
	private final float initialBalance;
	
	public BankNewAccount(Client client, float initialBalance) {
		super(client);
		this.initialBalance = initialBalance;
	}
	
	public float getInitialBalance() {
		return initialBalance;
	}
}
