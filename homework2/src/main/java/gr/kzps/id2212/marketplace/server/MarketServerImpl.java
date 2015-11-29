package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.database.DatabaseActions;
import gr.kzps.id2212.marketplace.server.database.DatabaseConnector;
import gr.kzps.id2212.marketplace.server.database.entities.ClientEntity;
import gr.kzps.id2212.marketplace.server.database.entities.ItemEntity;
import gr.kzps.id2212.marketplace.server.database.entities.WishEntity;
import gr.kzps.id2212.marketplace.server.exceptions.BankBalance;
import gr.kzps.id2212.marketplace.server.exceptions.DBConnectionException;
import gr.kzps.id2212.marketplace.server.exceptions.IncorrectPasswordException;
import gr.kzps.id2212.marketplace.server.exceptions.ItemDoesNotExists;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;
import gr.kzps.id2212.marketplace.server.exceptions.PasswordIsSmallException;
import gr.kzps.id2212.marketplace.server.exceptions.UserAlreadyExists;
import gr.kzps.id2212.marketplace.server.exceptions.UserNotRegistered;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;
import se.kth.id2212.ex2.bankrmi.RejectedException;

public class MarketServerImpl extends UnicastRemoteObject implements
        MarketServer {

    private static final long serialVersionUID = -7510739400260172856L;

    // Locks for different stores
    private final ReentrantLock loginLock = new ReentrantLock();

    private static final Logger LOG = LogManager
            .getLogger(MarketServerImpl.class);

    private final String marketName;

    // logged in users
    private final Map<String, MarketUsers> loggedInUsers;

    private final Bank bank;

    private final DatabaseConnector dbc;

    public MarketServerImpl(String marketName, Bank bank) throws RemoteException {
        super();
        this.marketName = marketName;
        loggedInUsers = new HashMap<>();
        this.bank = bank;
        this.dbc = new DatabaseConnector();
    }

    @Override
    public void unregister(String email, String password) throws RemoteException, NoUserException, DBConnectionException, IncorrectPasswordException {
        LOG.debug("Unregistering user");

        ClientEntity clientFromDb = null;
        try {
            // Put client in the list
            clientFromDb = DatabaseActions.selectClientByEmail(dbc.newSession(), email);
        } catch (Exception ex) {
            throw new DBConnectionException(ex.getMessage());
        }

        if (clientFromDb == null) {
            LOG.debug("User trying to unregister does not exist");
            throw new NoUserException("User does not exist");
        } else {
            if (!clientFromDb.getPassword().equals(password)) {
                throw new IncorrectPasswordException("The password is incorrect");
            } else {

                try {
                    DatabaseActions.removeClient(dbc.newSession(), clientFromDb);
                    loggedInUsers.remove(clientFromDb.getEmail());
                } catch (Exception ex) {
                    throw new DBConnectionException(ex.getMessage());
                }
            }
        }
    }

    @Override
    public void register(Client client) throws RemoteException, NoBankAccountException, UserAlreadyExists, DBConnectionException, PasswordIsSmallException {
        // Check if client has account in the bank
        Account bankAccount = bank.getAccount(client.getName());

        if (bankAccount == null) {
            // User does not have an account
            // Respond back
            LOG.warn("User trying to register does not have an account");
            throw new NoBankAccountException(
                    "User does NOT have a bank account");

        } else {
            // check if the user is already in the database
            ClientEntity clientFromDb = null;
            try {
                // Put client in the list
                clientFromDb = DatabaseActions.selectClientByEmail(dbc.newSession(), client.getEmail());
            } catch (Exception ex) {
                throw new DBConnectionException(ex.getMessage());
            }

            if (clientFromDb != null) {
                throw new UserAlreadyExists(
                        "A user with the same email already exists");
            } else {
                if (client.getPassword().length() < 8) {
                    throw new PasswordIsSmallException(
                            "The password must be at least 8 characters long");
                } else {

                    try {
                        DatabaseActions.addClient(dbc.newSession(), client);
                    } catch (Exception ex) {
                        throw new DBConnectionException(ex.getMessage());
                    }

                    LOG.info("Registered user: {}", client.getName());
                }
            }
        }

    }

    @Override
    public void login(Client client, Callbacks callbacks) throws RemoteException, DBConnectionException, IncorrectPasswordException, UserNotRegistered {
        // check if the user is already in the database
        ClientEntity clientFromDb = null;
        try {
            // Put client in the list
            clientFromDb = DatabaseActions.selectClientByEmail(dbc.newSession(), client.getEmail());
        } catch (Exception ex) {
            throw new DBConnectionException(ex.getMessage());
        }
        // Check if client is registered
        if (clientFromDb == null) {
            throw new UserNotRegistered("The user " + client.getName() + " does not exist");
        } else {
            Client user = new Client(clientFromDb);
            if (!user.getPassword().equals(client.getPassword())) {
                throw new IncorrectPasswordException("The password is incorrect");
            } else {
                loginLock.lock();
                try {
                    loggedInUsers.put(client.getEmail(), new MarketUsers(user, callbacks));
                } finally {
                    loginLock.unlock();
                }
                LOG.info("User login: {}", client.getEmail());
            }
        }

    }

    @Override
    public void logout(Client client) throws RemoteException, NoUserException {
        LOG.debug("Logging out user");
        MarketUsers user;
        loginLock.lock();
        try {
            user = loggedInUsers.remove(client.getEmail());
        } finally {
            loginLock.unlock();
        }

        if (user == null) {
            LOG.debug("User trying to loggout is not logged in");
            throw new NoUserException("User is not logged in");
        }

    }

    @Override
    public void sell(String email, String itemName, float price, int quantity) throws RemoteException, NoUserException, DBConnectionException {
        MarketUsers user = loggedInUsers.get(email);

        if (user == null) {
            LOG.debug("User trying to sell is NOT logged in.");
            throw new NoUserException("User trying to sell is NOT logged in.");
        } else {
            ExtendedItem newItem = new ExtendedItem(itemName, price, quantity, user);

            try {
                // Put client in the list
                ClientEntity clientFromDb = DatabaseActions.selectClientByEmail(dbc.newSession(), user.getClient().getEmail());
                DatabaseActions.addItem(dbc.newSession(), newItem, clientFromDb);
            } catch (Exception ex) {
                throw new DBConnectionException(ex.getMessage());
            }

            LOG.debug("Selling order stored");
            LOG.debug(newItem.toString());
            // Iterate the wishes to see if any of them in fulfilled now
            checkWishes();
        }
    }

    @Override
    public void buy(String buyersEmail, String itemName, int quantity) throws RemoteException, ItemDoesNotExists, NoUserException,
            BankBalance, DBConnectionException {
        ItemEntity itemFromDb = null;
        try {
            // Check if item exists
            itemFromDb = DatabaseActions.selectItemByName(dbc.newSession(), itemName);
        } catch (Exception ex) {
            throw new DBConnectionException(ex.getMessage());
        }

        if (itemFromDb == null) {
            throw new ItemDoesNotExists(
                    "The item you are trying to buy does not exist");
        } else {
            ClientEntity clt = null;
            try {
                clt = DatabaseActions.selectClientByItem(dbc.newSession(), itemName);
            } catch (Exception ex) {
                throw new DBConnectionException(ex.getMessage());
            }
            MarketUsers user;
            if (loggedInUsers.containsKey(clt.getEmail())) {
                user = loggedInUsers.get(clt.getEmail());
            } else {
                user = new MarketUsers(new Client(clt), null);
            }

            ExtendedItem item = new ExtendedItem(itemFromDb, user);
            // Check user exists
            // Find user in local db
            MarketUsers buyer = loggedInUsers.get(buyersEmail);
            if (buyer == null) {
                throw new NoUserException(
                        "You are not logged in in the marketplace");
            } else {

                if (item.getQuantity() < quantity) {
                    throw new NoUserException(
                            "Item quantity not enough");
                } else {
                    // Check user balance in bank
                    Account buyerBankAcc = bank.getAccount(buyer.getClient()
                            .getName());

                    try {
                        // Update buyer balance
                        buyerBankAcc.withdraw(item.getPrice() * quantity);
                        // Update seller balance
                        Account sellerBankAcc = bank.getAccount(item.getOwner()
                                .getClient().getName());
                        sellerBankAcc.deposit(item.getPrice() * quantity);

                        // Remove from sellingItems
                        DatabaseActions.removeItem(dbc.newSession(), item, quantity, item.getOwner().getClient().getEmail(), buyer.getClient().getEmail());

                        // Notify seller if he is logged in
                        // In the real world we would send an email instead
                        // now we do nothing
                        if (loggedInUsers.containsKey(item.getOwner().getClient().getEmail())) {
                            item.getOwner()
                                    .getCallbacks()
                                    .itemBought(item.getName(),
                                            buyer.getClient().getName());
                        }
                    } catch (RejectedException ex) {
                        throw new BankBalance(ex.getMessage());
                    } catch (Exception ex) {
                        throw new DBConnectionException(ex.getMessage());
                    }
                }

            }

        }

    }

    @Override
    public void wish(String userEmail, String itemName, float price) throws RemoteException, NoUserException, DBConnectionException {

        LOG.debug("Make a wish for item {} in price {}", new Object[]{
            itemName, price});

        MarketUsers user = loggedInUsers.get(userEmail);

        if (user == null) {
            throw new NoUserException(
                    "You are not logged in to the marketplace");
        } else {

            try {
                ClientEntity clientFromDb = DatabaseActions.selectClientByEmail(dbc.newSession(), userEmail);
                DatabaseActions.addWish(dbc.newSession(), itemName, price, clientFromDb);
            } catch (Exception ex) {
                throw new DBConnectionException(ex.getMessage());
            }
        }
    }

    @Override
    public List<BaseItem> listItems() throws RemoteException, DBConnectionException {
        List<ItemEntity> items = DatabaseActions.getAllItems(dbc.newSession());

        List<BaseItem> ret = items.stream()
                .map(I -> new BaseItem(I.getName(), I.getPrice(), I.getQuantity()))
                .collect(Collectors.toList());

        return ret;
    }

    @Override
    public String getName() {
        return marketName;
    }

    private void checkWishes() {
        LOG.debug("Checking wishes");

        List<WishEntity> fulfilledWishes = new ArrayList<>();

        List<WishEntity> wishez = DatabaseActions.getWishes(dbc.newSession());
        List<ItemEntity> items = DatabaseActions.getAllItems(dbc.newSession());

        for (WishEntity wish : wishez) {

            // Take the first item that matches the criteria
            Optional<ItemEntity> fitItemOpt = items
                    .stream()
                    .filter(I -> I.getName().equals(wish.getName())
                            && I.getPrice() <= wish.getPrice()).findFirst();

            if (fitItemOpt.isPresent()) {
                LOG.debug(
                        "Found fit for wish item: {} with price {} for user: {}",
                        new Object[]{wish.getName(), wish.getPrice(),
                            wish.getClientEntity().getName()});

                ItemEntity fitItem = fitItemOpt.get();
                fulfilledWishes.add(wish);
                // Notify user if he is logged in
                if (loggedInUsers.containsKey(wish.getClientEntity().getEmail())) {
                    MarketUsers usr = loggedInUsers.get(wish.getClientEntity().getEmail());
                    try {
                        usr.getCallbacks().wishFulfilled(fitItem.getName(), fitItem.getPrice());
                    } catch (RemoteException ex) {
                        java.util.logging.Logger.getLogger(MarketServerImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // user is not logged in
                    // send him email for the fullfiled wish
                }
            }
        }
        // Remove fulfilled wishes
        for (WishEntity wish : fulfilledWishes) {
            DatabaseActions.removeWish(dbc.newSession(), wish);
        }

        fulfilledWishes = null;
    }

    @Override
    public String info(String email) throws RemoteException, NoUserException, DBConnectionException {
        MarketUsers user = loggedInUsers.get(email);

        if (user == null) {
            LOG.debug("User trying to view info is NOT logged in.");
            throw new NoUserException("You are not logged in");
        } else {
            ClientEntity clientFromDb;
            String info;
            try {
                clientFromDb = DatabaseActions.selectClientByEmail(dbc.newSession(), email);
            } catch (Exception ex) {
                throw new DBConnectionException(ex.getMessage());
            }
            if (clientFromDb == null) {
                
                // that should never happen
                throw new NoUserException("User is not registered");
                
            } else {

                info = "> User: " + clientFromDb.getName() + "\n";
                info += "> Email: " + clientFromDb.getEmail() + "\n";
                info += "> Items Sold: " + clientFromDb.getItemsSold() + "\n";
                info += "> Items Bought: " + clientFromDb.getItemsBought() + "\n";

                try {
                    List<WishEntity> wishes = DatabaseActions.getUserInfo(dbc.newSession(), clientFromDb);
                    for (WishEntity wish : wishes) {
                        info += "> Wish item: " + wish.getName() + " price: " + wish.getPrice() + " fulfilled: " + wish.getWishFulfilled() + "\n";
                    }
                } catch (Exception ex) {
                    throw new DBConnectionException(ex.getMessage());
                }

                return info;
            }
        }
    }
}
