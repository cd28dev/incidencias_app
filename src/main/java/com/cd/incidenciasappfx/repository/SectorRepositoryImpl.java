package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Sector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SectorRepositoryImpl.java
 *
 * @author CDAA
 */
public class SectorRepositoryImpl implements ISectorRepository {

    @Override
    public List<Sector> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Sector> sectores = new ArrayList<>();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_sectores");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                Sector sector = new Sector();
                sector.setId((Integer) row[0]);
                sector.setNombre((String) row[1]);
                sectores.add(sector);
            }
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return sectores;
    }

    @Override
    public Optional<Sector> save(Sector sector) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_sector");
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
            query.setParameter("p_nombre", sector.getNombre());
            query.execute();
            return Optional.of(sector);
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Sector> findById(Integer idSector) {
        EntityManager em = JpaUtil.getEntityManager();
        Sector sector = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_sector_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, idSector);
            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                sector = new Sector();
                sector.setId((Integer) row[0]);
                sector.setNombre((String) row[1]);
            }
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(sector);
    }

    @Override
    public Optional<Sector> update(Sector sector) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_sector");
            query.registerStoredProcedureParameter("p_idSector", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
            query.setParameter("p_idSector", sector.getId());
            query.setParameter("p_nombre", sector.getNombre());
            query.execute();
            return Optional.of(sector);
        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int idSector) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_sector");
            query.registerStoredProcedureParameter("p_id_sector", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, ParameterMode.OUT);
            query.setParameter("p_id_sector", idSector);
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
