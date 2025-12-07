package com.code058.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = buildEMF();

    private static EntityManagerFactory buildEMF() {
        return Persistence.createEntityManagerFactory("online-store-pu");
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf.isOpen()) emf.close();
    }
}
