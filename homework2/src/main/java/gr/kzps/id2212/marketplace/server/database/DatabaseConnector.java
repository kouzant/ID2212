/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.kzps.id2212.marketplace.server.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Anestos
 */
public class DatabaseConnector {

    private SessionFactory sessionFactory;
    private Session session;

    public DatabaseConnector() {
        sessionFactory = null;
        session = null;

        Configuration conf = new Configuration();
        conf.configure("hibernate.cfg.xml");

        sessionFactory = conf.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public Session getSession() {
        return session;
    }
    
    public Session newSession(){
        if (session != null) {
            session.close();
        }
        session = sessionFactory.openSession();
        return session;
    }

    public void closeSession() {
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
