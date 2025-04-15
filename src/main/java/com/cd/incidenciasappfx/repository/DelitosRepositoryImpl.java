package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Delito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DelitosRepositoryImpl implements IDelitoRepository {

    @Override
    public Optional<Delito> save(Delito d) {

        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_delito");

            query.registerStoredProcedureParameter("p_nombre", String.class, jakarta.persistence.ParameterMode.IN);
            query.setParameter("p_nombre", d.getNombre());

            query.execute();
            return Optional.of(d);

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Delito> findById(Integer idDelito) {
        EntityManager em = JpaUtil.getEntityManager();
        Delito delito = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_delito_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, idDelito);

            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                delito = new Delito();
                delito.setIdDelito((Integer) row[0]);
                delito.setNombre((String) row[1]);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(delito);
    }

    @Override
    public List<Delito> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Delito> delitos = new ArrayList<>();

        try {
            // Llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_delitos");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                Delito delito = new Delito();
                delito.setIdDelito((Integer) row[0]);
                delito.setNombre((String) row[1]);

                delitos.add(delito);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return delitos;
    }

    @Override
    public Optional<Delito> update(Delito d) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_Delito");
            query.registerStoredProcedureParameter("p_idDelito", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);

            query.setParameter("p_idDelito", d.getIdDelito());
            query.setParameter("p_nombre", d.getNombre());

            query.execute();

            return Optional.of(d);

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
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_delito");

            query.registerStoredProcedureParameter("p_id_delito", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, jakarta.persistence.ParameterMode.OUT);

            query.setParameter("p_id_delito", id);

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
