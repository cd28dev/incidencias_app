package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RolesRepositoryImpl.java
 *
 * @author CDAA
 */
public class RolesRepositoryImpl implements IRolesRepository {

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
            handleSQLException(e);
        } finally {
            em.close();
        }

        return roles;
    }

    @Override
    public Optional<Rol> save(Rol rol) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_rol");

            query.registerStoredProcedureParameter("p_nombre", String.class, jakarta.persistence.ParameterMode.IN);
            query.setParameter("p_nombre", rol.getNombre());

            query.execute();
            return Optional.of(rol);

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Rol> findById(Integer idRol) {
        EntityManager em = JpaUtil.getEntityManager();
        Rol rol = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_rol_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, idRol);

            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                rol = new Rol();
                rol.setIdRol((Integer) row[0]);
                rol.setNombre((String) row[1]);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(rol);
    }

    @Override
    public Optional<Rol> update(Rol r) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_rol");
            query.registerStoredProcedureParameter("p_idRol", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);

            query.setParameter("p_idRol", r.getIdRol());
            query.setParameter("p_nombre", r.getNombre());

            query.execute();

            return Optional.of(r);

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int idRol) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_rol");

            // Registrar parámetros de entrada y salida
            query.registerStoredProcedureParameter("p_id_rol", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, jakarta.persistence.ParameterMode.OUT);

            // Establecer valor del parámetro de entrada
            query.setParameter("p_id_rol", idRol);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Obtener el valor del parámetro de salida
            Integer resultado = (Integer) query.getOutputParameterValue("p_resultado");

            // Retornar true si se eliminó correctamente, false en caso contrario
            return resultado != null && resultado == 1;

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return false;
        } finally {
            em.close();
        }
    }

    private void handleSQLException(jakarta.persistence.PersistenceException e) {
        Throwable cause = e.getCause();
        if (cause instanceof java.sql.SQLException sqlEx) {
            String sqlState = sqlEx.getSQLState();
            if ("45000".equals(sqlState)) {
                System.out.println("Error: " + sqlEx.getMessage());
            }
        }
        e.printStackTrace();
    }

}
