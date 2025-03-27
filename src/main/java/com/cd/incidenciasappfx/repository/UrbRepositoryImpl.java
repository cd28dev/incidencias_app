package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Sector;
import com.cd.incidenciasappfx.models.Urbanizacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UrbRepositoryImpl.java
 *
 * @author CDAA
 */
public class UrbRepositoryImpl implements IUrbRepository {

    @Override
    public Optional<Urbanizacion> save(Urbanizacion u) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_urbanizacion");
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre_sector", String.class, ParameterMode.IN);
            query.setParameter("p_nombre", u.getNombre());
            query.setParameter("p_nombre_sector", u.getSector().getNombre());
            query.execute();
            return Optional.of(u);
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Urbanizacion> findById(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        Urbanizacion urbanizacion = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_sector_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, id);
            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                urbanizacion = new Urbanizacion();
                Sector s = new Sector();
                urbanizacion.setId((Integer) row[0]);
                urbanizacion.setNombre((String) row[1]);
                s.setId((Integer) row[2]);
                s.setNombre((String) row[3]);
                urbanizacion.setSector(s);
            }
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(urbanizacion);
    }

    @Override
    public List<Urbanizacion> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Urbanizacion> urbanizaciones = new ArrayList<>();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_urbanizaciones");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                Urbanizacion u = new Urbanizacion();
                Sector s = new Sector();

                u.setId((Integer) row[0]);
                u.setNombre((String) row[1]);
                s.setId((Integer) row[2]);
                s.setNombre((String) row[3]);
                u.setSector(s);
                urbanizaciones.add(u);
            }
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return urbanizaciones;
    }

    @Override
    public Optional<Urbanizacion> update(Urbanizacion u) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_sector");
            query.registerStoredProcedureParameter("p_idSector", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_sector", Integer.class, ParameterMode.IN);
            query.setParameter("p_id_urbanizacion", u.getId());
            query.setParameter("p_nombre", u.getNombre());
            query.setParameter("p_id_sector", u.getSector().getId());
            query.execute();
            return Optional.of(u);
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_sector");
            query.registerStoredProcedureParameter("p_id_urbanizacion", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, ParameterMode.OUT);
            query.setParameter("p_id_urbanizacion", id);
            query.execute();
            Integer resultado = (Integer) query.getOutputParameterValue("p_resultado");
            return resultado != null && resultado == 1;
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Urbanizacion> findBySector(String nameSector) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Urbanizacion> urbanizaciones = new ArrayList<>();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_urbanizacion_por_sector");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, nameSector);

            List<String> resultados = query.getResultList();

            for (String nombreUrbanizacion : resultados) {
                Urbanizacion u = new Urbanizacion();
                u.setNombre(nombreUrbanizacion);
                urbanizaciones.add(u);
            }
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return urbanizaciones;
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
