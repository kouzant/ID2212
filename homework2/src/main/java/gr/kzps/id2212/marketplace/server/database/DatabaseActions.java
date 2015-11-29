/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.kzps.id2212.marketplace.server.database;

import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.ExtendedItem;
import gr.kzps.id2212.marketplace.server.database.entities.ClientEntity;
import gr.kzps.id2212.marketplace.server.database.entities.ItemEntity;
import gr.kzps.id2212.marketplace.server.database.entities.WishEntity;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Anestos
 */
public class DatabaseActions {

    public static void addClient(Session session, Client client) throws Exception {
        Transaction tx = session.beginTransaction();

        ClientEntity person = new ClientEntity(client.getName(), client.getEmail(), client.getPassword());

        session.save(person);
        tx.commit();
    }

    public static void removeClient(Session session, ClientEntity client) throws Exception {
        Transaction tx = session.beginTransaction();

        Long id = client.getId();

        Query q1 = session.createQuery("delete ItemEntity where clientEntity=" + id);
        q1.executeUpdate();
        Query q2 = session.createQuery("delete WishEntity where clientEntity =" + id);
        q2.executeUpdate();
        Query q3 = session.createQuery("delete ClientEntity where id =" + id);
        q3.executeUpdate();
        tx.commit();
    }

    public static ClientEntity selectClientByEmail(Session session, String email) throws Exception {
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from ClientEntity where email = :email");
        query.setString("email", email);

        List<?> result = query.list();
        tx.commit();

        if (result.size() > 0) {
            ClientEntity client = (ClientEntity) result.get(0);
            return client;
        }

        return null;
    }

    public static ClientEntity selectClientByItem(Session session, String itemName) throws Exception {
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ItemEntity where item_name=:itemName");
        query.setString("itemName", itemName);

        List<?> result = query.list();
        tx.commit();
        if (result.size() > 0) {
            ItemEntity item = (ItemEntity) result.get(0);

            return item.getClientEntity();
        }

        return null;
    }

    public static void addItem(Session session, ExtendedItem item, ClientEntity clientEntity) throws Exception {
        Transaction tx = session.beginTransaction();

        ItemEntity saveItem = new ItemEntity(item.getName(), item.getPrice(), item.getQuantity(), clientEntity);
        session.save(saveItem);
        tx.commit();
    }

    public static void removeItem(Session session, ExtendedItem item, int quantity, String sellerEmail, String buyerEmail) throws Exception {
        int newQuantity = item.getQuantity() - quantity;
        Transaction tx = session.beginTransaction();

        Query q1 = session.createQuery("Update ClientEntity set itemsSold=itemsSold+" + quantity + " where email='" + sellerEmail + "'");
        q1.executeUpdate();

        Query q2 = session.createQuery("Update ClientEntity set itemsBought=itemsBought+" + quantity + " where email='" + buyerEmail + "'");
        q2.executeUpdate();

        if (newQuantity == 0) {
            Query query = session.createQuery("delete ItemEntity where name='" + item.getName() + "'");
            int result = query.executeUpdate();
        } else {
            Query query = session.createQuery("update ItemEntity set quantity=" + newQuantity + " where name='" + item.getName() + "'");
            int result = query.executeUpdate();
        }
        tx.commit();

    }

    public static List<ItemEntity> getAllItems(Session session) {
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from ItemEntity where item_quantity > 0");
        List<ItemEntity> result = query.list();
        tx.commit();
        return result;
    }

    public static List<WishEntity> getWishes(Session session) {
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from WishEntity where wishFulfilled='"+false+"'");
        List<WishEntity> result = query.list();

        tx.commit();
        return result;
    }
    
    public static void removeWish(Session session, WishEntity wish){
        Transaction tx = session.beginTransaction();
        Long wishId = wish.getId();
        Query query = session.createQuery("update WishEntity set wishFulfilled='"+true+"' where id=" + wishId);
        query.executeUpdate();
        tx.commit();
    }

    public static ItemEntity selectItemByName(Session session, String itemName) throws Exception {
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from ItemEntity where name = :name");
        query.setString("name", itemName);

        List<?> result = query.list();
        tx.commit();
        if (result.size() > 0) {
            ItemEntity item = (ItemEntity) result.get(0);
            return item;
        }
        return null;
    }

    public static void addWish(Session session, String itemName, float price, ClientEntity clientEntity) throws Exception {
        Transaction tx = session.beginTransaction();

        WishEntity saveItem = new WishEntity(itemName, price, clientEntity);
        session.save(saveItem);
        tx.commit();
    }
    
    public static List<WishEntity> getUserInfo(Session session, ClientEntity client) throws Exception {
        String info = null;
        Transaction tx = session.beginTransaction();

        Long id = client.getId();

        Query query = session.createQuery("from WishEntity where clientEntity="+id);
        List<WishEntity> result = query.list();

        tx.commit();
        
        return result;
    }
}
