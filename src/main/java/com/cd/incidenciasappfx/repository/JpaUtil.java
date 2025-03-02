package com.cd.incidenciasappfx.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;

/**
 * JpaUtil.java
 *
 * @author CDAA
 */
public class JpaUtil {

    private static EntityManagerFactory emf;
    
    static {
    try {
        emf = Persistence.createEntityManagerFactory("incidenciasAppPU");
    } catch (Exception e) {
        e.printStackTrace();
        throw new ExceptionInInitializerError(e);
    }
}
    
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static Connection getConnection() throws SQLException {
        SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
        return sessionFactory.getSessionFactoryOptions().getServiceRegistry()
                .getService(ConnectionProvider.class).getConnection();
    }

    public static void close() {
        emf.close();
    }
}
