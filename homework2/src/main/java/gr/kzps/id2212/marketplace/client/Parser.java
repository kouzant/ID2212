package gr.kzps.id2212.marketplace.client;

import gr.kzps.id2212.marketplace.client.Commands.BankNewAccount;
import gr.kzps.id2212.marketplace.client.Commands.BaseCommand;
import gr.kzps.id2212.marketplace.client.Commands.BuyCommand;
import gr.kzps.id2212.marketplace.client.Commands.Commands;
import gr.kzps.id2212.marketplace.client.Commands.Exit;
import gr.kzps.id2212.marketplace.client.Commands.Help;
import gr.kzps.id2212.marketplace.client.Commands.InfoCommand;
import gr.kzps.id2212.marketplace.client.Commands.ListCommand;
import gr.kzps.id2212.marketplace.client.Commands.LoginMarketplace;
import gr.kzps.id2212.marketplace.client.Commands.LogoutMarketplace;
import gr.kzps.id2212.marketplace.client.Commands.RegisterMarketplace;
import gr.kzps.id2212.marketplace.client.Commands.SellCommand;
import gr.kzps.id2212.marketplace.client.Commands.UnregisterMarketplace;
import gr.kzps.id2212.marketplace.client.Commands.WishCommand;
import gr.kzps.id2212.marketplace.client.exceptions.NotEnoughParams;
import gr.kzps.id2212.marketplace.client.exceptions.UnknownCommand;

import java.rmi.RemoteException;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parser {

    private final static Logger LOG = LogManager.getLogger(Parser.class);

    public Parser() {

    }

    public BaseCommand parse(String input) throws UnknownCommand, NotEnoughParams {
        if (input == null) {
            throw new UnknownCommand();
        }

        StringTokenizer inputTokens = new StringTokenizer(input);
        Commands command = null;

        if (!inputTokens.hasMoreTokens()) {
            throw new UnknownCommand();
        }

        String commandStr = inputTokens.nextToken();

        try {
            command = Commands.getCommand(commandStr);
        } catch (IllegalArgumentException ex) {
			// If command is not member of the Commands enum
            // the user has enter a wrong command
            LOG.debug("Unknown command");
            throw new UnknownCommand();
        }

        if (Commands.newaccount.equals(command)) {
            if (inputTokens.countTokens() != 3) {

                throw new NotEnoughParams();
            }
            String name = inputTokens.nextToken();
            String email = inputTokens.nextToken();
            String initialBalance = inputTokens.nextToken();

            return new BankNewAccount(new Client(name, email, null),
                    Float.parseFloat(initialBalance));

        } else if (Commands.register.equals(command)) {
            if (inputTokens.countTokens() != 3) {

                throw new NotEnoughParams();
            }
            String name = inputTokens.nextToken();
            String email = inputTokens.nextToken();
            String password = inputTokens.nextToken();

            return new RegisterMarketplace(new Client(name, email, password));

        } else if (Commands.unregister.equals(command)) {
            if (inputTokens.countTokens() != 2) {
                throw new NotEnoughParams();
            }
            String email = inputTokens.nextToken();
            String password = inputTokens.nextToken();

            return new UnregisterMarketplace(new Client(null, email, password));

        } else if (Commands.login.equals(command)) {
            if (inputTokens.countTokens() != 2) {

                throw new NotEnoughParams();
            }
            String email = inputTokens.nextToken();
            String password = inputTokens.nextToken();

            Callbacks newCallbacks = null;

            try {
                newCallbacks = new CallbacksImpl();
            } catch (RemoteException ex) {
                LOG.error("Error while creating user callback method");
                ex.printStackTrace();
            }
            return new LoginMarketplace(new Client(null, email, password), newCallbacks);

        } else if (Commands.logout.equals(command)) {
            

            return new LogoutMarketplace(UserCache.getInstance().getCurrentUser());

        } else if (Commands.info.equals(command)) {
            
            return new InfoCommand(UserCache.getInstance().getCurrentUser());
            
        } else if (Commands.sell.equals(command)) {
            if (inputTokens.countTokens() != 3) {
                throw new NotEnoughParams();
            }
            String itemName = inputTokens.nextToken();
            float price = Float.parseFloat(inputTokens.nextToken());
            int quantity = Integer.parseInt(inputTokens.nextToken());

            return new SellCommand(UserCache.getInstance().getCurrentUser(), itemName, price, quantity);
        } else if (Commands.list.equals(command)) {
            return new ListCommand(null);

        } else if (Commands.buy.equals(command)) {
            if (inputTokens.countTokens() != 2) {
                throw new NotEnoughParams();
            }
            String itemName = inputTokens.nextToken();
            int quantity = Integer.parseInt(inputTokens.nextToken());

            return new BuyCommand(UserCache.getInstance().getCurrentUser(), itemName, quantity);
        } else if (Commands.wish.equals(command)) {
            if (inputTokens.countTokens() != 2) {
                throw new NotEnoughParams();
            }

            String itemName = inputTokens.nextToken();
            float price = Float.parseFloat(inputTokens.nextToken());

            return new WishCommand(UserCache.getInstance().getCurrentUser(), itemName, price);
        } else if (Commands.exit.equals(command)) {

            return new Exit(null);
        } else if (Commands.help.equals(command)) {

            return new Help(null);
        } else {

            throw new UnknownCommand();
        }
    }
}
