
package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;


/**
 * RolesRepositoryImpl.java
 * 
 * @author CDAA
 */
public class RolesRepositoryImpl implements IRolesRepository{
    private final EntityManager entityManager;

    public RolesRepositoryImpl() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    
    
@Override
public List<Rol> findAll() {
    EntityManager em = JpaUtil.getEntityManager();
    List<Rol> roles = new ArrayList<>();

    try {
        // Llamar al procedimiento almacenado
        StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_roles");
        List<Object[]> resultados = query.getResultList();

        for (Object[] row : resultados) {
            Rol rol = new Rol();
            rol.setIdRol((Integer) row[0]);
            rol.setNombre((String) row[1]);

            roles.add(rol);
        }

    } catch (jakarta.persistence.PersistenceException e) {
        Throwable cause = e.getCause();
        if (cause instanceof java.sql.SQLException sqlEx) {
            String sqlState = sqlEx.getSQLState();
            if ("45000".equals(sqlState)) {
                System.out.println("Error: " + sqlEx.getMessage());
            }
        }
        e.printStackTrace();
    } finally {
        em.close();
    }

    return roles;
}


}
