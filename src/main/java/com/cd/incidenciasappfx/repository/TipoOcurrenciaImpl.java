package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.TipoIntervencion;
import com.cd.incidenciasappfx.models.TipoOcurrencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipoOcurrenciaImpl implements ITipoOcurrenciaRepository{

    @Override
    public Optional<TipoOcurrencia> save(TipoOcurrencia d) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_tOcurrencia");

            query.registerStoredProcedureParameter("p_nombre", String.class, jakarta.persistence.ParameterMode.IN);
            query.setParameter("p_nombre", d.getNombre());

            query.execute();
            return Optional.of(d);

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<TipoOcurrencia> findById(Integer idTipo) {
        EntityManager em = JpaUtil.getEntityManager();
        TipoOcurrencia tipoOcurrencia = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_tOcurrencia_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, idTipo);

            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                tipoOcurrencia = new TipoOcurrencia();
                tipoOcurrencia.setIdOcurrencia((Integer) row[0]);
                tipoOcurrencia.setNombre((String) row[1]);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(tipoOcurrencia);
    }

    @Override
    public List<TipoOcurrencia> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<TipoOcurrencia> tipoOcurrencias = new ArrayList<>();

        try {
            // Llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_tipos_ocurrencia");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                TipoOcurrencia to = new TipoOcurrencia();
                to.setIdOcurrencia((Integer) row[0]);
                to.setNombre((String) row[1]);

                tipoOcurrencias.add(to);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return tipoOcurrencias;
    }

    @Override
    public Optional<TipoOcurrencia> update(TipoOcurrencia d) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_tipo_ocurrencia");
            query.registerStoredProcedureParameter("p_idTipoOcurrencia", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);

            query.setParameter("p_idTipoOcurrencia", d.getIdOcurrencia());
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
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_tipo_ocurrencia");

            query.registerStoredProcedureParameter("p_id_tipo_ocurrencia", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, jakarta.persistence.ParameterMode.OUT);

            query.setParameter("p_id_tipo_ocurrencia", id);

            query.execute();

            Integer resultado = (Integer) query.getOutputParameterValue("p_resultado");

            return resultado != null && resultado == 1;

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return false;
        }finally {
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
