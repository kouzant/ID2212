package gr.kzps.id2212.marketplace.client;

import gr.kzps.id2212.marketplace.client.Commands.BankNewAccount;
import gr.kzps.id2212.marketplace.client.Commands.BaseCommand;
import gr.kzps.id2212.marketplace.client.Commands.BuyCommand;
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
import gr.kzps.id2212.marketplace.server.BaseItem;
import gr.kzps.id2212.marketplace.server.ExtendedItem;
import gr.kzps.id2212.marketplace.server.MarketServer;
import gr.kzps.id2212.marketplace.server.exceptions.BankBalance;
import gr.kzps.id2212.marketplace.server.exceptions.DBConnectionException;
import gr.kzps.id2212.marketplace.server.exceptions.IncorrectPasswordException;
import gr.kzps.id2212.marketplace.server.exceptions.ItemDoesNotExists;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;
import gr.kzps.id2212.marketplace.server.exceptions.PasswordIsSmallException;
import gr.kzps.id2212.marketplace.server.exceptions.UserAlreadyExists;
import gr.kzps.id2212.marketplace.server.exceptions.UserNotRegistered;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;
import se.kth.id2212.ex2.bankrmi.RejectedException;

public class Executor {

    private final static Logger LOG = LogManager.getLogger(Executor.class);
    private final Bank bank;
    private final MarketServer market;

    public Executor(Bank bank, MarketServer market) {
        this.bank = bank;
        this.market = market;
    }

    public Integer execute(BaseCommand command) throws RemoteException {
        if (command instanceof BankNewAccount) {
            // Create new bank account
            LOG.debug("Create new bank acount");
            try {
                Account account = bank
                        .newAccount(command.getClient().getName());
                account.deposit(((BankNewAccount) command).getInitialBalance());
                System.out.println("> Account created");
            } catch (RejectedException ex) {
                LOG.info("Could not create new account");
                System.out.println("> " + ex.getMessage());
            }
        } else if (command instanceof LoginMarketplace) {

            LOG.debug("Login user to marketplace");

            // Unexport previously exported callbacks
            try {
                UnicastRemoteObject.unexportObject(UserCache.getInstance()
                        .getCallbacks(), true);
            } catch (NoSuchObjectException ex) {
                LOG.debug("No previous callbacks exported");
            }

            // If this is inside the try-cache block we will have troubles
            // when quit the program. It will hang waiting for exported
            // callbacks
            UserCache.getInstance().setCallbacks(
                    ((LoginMarketplace) command).getCallbacks());

            try {
                String userName = market.login(command.getClient(),
                        ((LoginMarketplace) command).getCallbacks());
                UserCache.getInstance().setCurrentUser(command.getClient());
                UserCache.getInstance().getCurrentUser().setName(userName);;
                System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> User logged in");
                
            } catch (IncorrectPasswordException ex) {
                System.out.println("> Incorrect password. try again");
            } catch (UserNotRegistered ex) {
                System.out.println("> user not registered");
            } catch (DBConnectionException ex) {
                LOG.debug(ex.getMessage());
                System.out.println("> " + ex.getMessage());
            } catch (UserAlreadyExists ex) {
            	System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> " + ex.getMessage());
            }

        } else if (command instanceof LogoutMarketplace) {
            LOG.debug("Logout user from the marketplace");
            if (command.getClient() != null) {
                try {
                    market.logout(command.getClient().getEmail());
                    System.out.println("> Logout successful");
                } catch (NoUserException ex) {
                    LOG.debug("User is not logged in");
                    System.out.println("> " + ex.getMessage());
                }
            } else {
                System.out.println("> You are not logged in");
            }
        } else if (command instanceof InfoCommand) {
            if (command.getClient() != null) {
                try {
                    String info = market.info(command.getClient().getEmail());
                    System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> " + info);
                } catch (NoUserException | DBConnectionException ex) {
                    LOG.debug("User info error");
                    System.out.println("> " + ex.getMessage());
                }
            } else {
                System.out.println("> You are not logged in");
            }
        } else if (command instanceof RegisterMarketplace) {
            // Register ourself to the marketplace
            // We cannot register twice, before exit the client we should
            // unregister
            LOG.debug("Register user to marketplace");

            try {
                market.register(command.getClient());
                System.out.println("> User registered");

            } catch (NoBankAccountException ex) {
                LOG.debug("User does not have a bank account");
                System.out.println("> " + ex.getMessage());
            } catch (UserAlreadyExists | PasswordIsSmallException | DBConnectionException ex) {
                LOG.debug(ex.getMessage());
                System.out.println("> " + ex.getMessage());
            }

        } else if (command instanceof UnregisterMarketplace) {
            // Unregister from the marketplace
            LOG.debug("Unregistering user: {}", command.getClient().getEmail());

            try {
                market.unregister(command.getClient().getEmail(), command.getClient().getPassword());
                System.out.println("> User unregistered");
            } catch (NoUserException ex) {
                System.out
                        .println("> The user you are trying to unregister, does not exist");
            } catch (DBConnectionException | IncorrectPasswordException ex) {
                LOG.debug(ex.getMessage());
                System.out.println("> " + ex.getMessage());
            }

        } else if (command instanceof SellCommand) {
            // Place a sell order
            LOG.debug("Sell an item");
            if (command.getClient() != null) {
                try {
                    market.sell(command.getClient().getEmail(),
                            ((SellCommand) command).getItemName(),
                            ((SellCommand) command).getPrice(),
                            ((SellCommand) command).getQuantity());
                    System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> Item:" + ((SellCommand) command).getItemName() + " ready for sale");
                } catch (NoUserException | DBConnectionException ex) {
                    System.out.println("> " + ex.getMessage());
                }
            } else {
                System.out.println("> You are not logged in");
            }
        } else if (command instanceof ListCommand) {
            // List available items on the marketplace
            LOG.debug("List command");
            List<BaseItem> items = null;
            try {
                items = market.listItems();
            } catch (DBConnectionException ex) {
                System.out.println("> " + ex.getMessage());
            }

            if (items.size() == 0) {
                System.out.println("> No items are available for buying");
            } else {
                for (BaseItem item : items) {
                    System.out.println("> Name: " + item.getName() + " Price: "
                            + item.getPrice() + " Quantity: " + item.getQuantity());
                }
            }
        } else if (command instanceof BuyCommand) {
            // Buy a selected item
            LOG.debug("Buy command");
            if (command.getClient() != null) {
                try {
                    market.buy(command.getClient().getEmail(),
                            ((BuyCommand) command).getItemName(),
                            ((BuyCommand) command).getQuantity());
                    System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> Item bought!");
                } catch (ItemDoesNotExists | BankBalance | DBConnectionException ex) {
                    System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> " + ex.getMessage());
                } catch (NoUserException ex) {
                	System.out.println("> " + ex.getMessage());
                }
            } else {
                System.out.println("> You are not logged in");
            }
        } else if (command instanceof WishCommand) {
            // Make a wish for an item in a specific price
            LOG.debug("Make a wish for: {} at price: {}", new Object[]{
                ((WishCommand) command).getItemName(),
                ((WishCommand) command).getPrice()});

            try {
                market.wish(command.getClient().getEmail(),
                        ((WishCommand) command).getItemName(),
                        ((WishCommand) command).getPrice());
                System.out.println(UserCache.getInstance().getCurrentUser().getName() + "@Market> Wish placed");
            } catch (NoUserException | DBConnectionException ex) {
                System.out.println("> " + ex.getMessage());
            }
        } else if (command instanceof Help) {
            System.out.println(printHelp());
        } else if (command instanceof Exit) {
            System.out.println("> Bye");

            // Unexport callbacks
            try {
                UnicastRemoteObject.unexportObject(UserCache.getInstance()
                        .getCallbacks(), true);
            } catch (NoSuchObjectException ex) {
                LOG.debug("No exported object to unexport");
            }

            return -1;
        } else if (command == null) {
            LOG.error("Command is null");

            return -1;
        }

        return 0;
    }

    private String printHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== e-Marketplace help ===").append("\n");
        sb.append("> * Create bank account").append("\n");
        sb.append(">\tnewaccount NAME EMAIL INITIAL_BALANCE").append("\n");
        sb.append("> * Register user to marketplace").append("\n");
        sb.append(">\tregister NAME EMAIL PASSWORD").append("\n");
        sb.append("> * Unregister user from marketplace").append("\n");
        sb.append(">\tunregister EMAIL").append("\n");
        sb.append("> * Login to the marketplace").append("\n");
        sb.append(">\tlogin EMAIL PASSWORD").append("\n");
        sb.append("> * Logout from the marketplace").append("\n");
        sb.append(">\tlogout").append("\n");
        sb.append("> * Account information").append("\n");
        sb.append(">\tinfo").append("\n");
        sb.append("> * Put a sell order").append("\n");
        sb.append(">\tsell ITEM_NAME PRICE QUANTITY").append("\n");
        sb.append("> * Buy item").append("\n");
        sb.append(">\tbuy ITEM_NAME QUANTITY").append("\n");
        sb.append("> * List available items on marketplace").append("\n");
        sb.append(">\tlist").append("\n");
        sb.append("> * Place a wish for a specific order in a prefered price").append("\n");
        sb.append(">\twish ITEM_NAME PRICE").append("\n");
        sb.append("> * Print the current help menu").append("\n");
        sb.append(">\thelp").append("\n");
        sb.append("> * Exit marketplace client").append("\n");
        sb.append(">\texit").append("\n");

        return sb.toString();
    }
}
