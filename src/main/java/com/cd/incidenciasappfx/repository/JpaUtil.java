package com.cd.incidenciasappfx.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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

    public static void close() {
        emf.close();
    }
}
