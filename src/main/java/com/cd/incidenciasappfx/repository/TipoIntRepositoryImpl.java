package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.TipoIntervencion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipoIntRepositoryImpl implements ITipoIntRepository {

    @Override
    public Optional<TipoIntervencion> save(TipoIntervencion d) {

        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_tIntervencion");

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
    public Optional<TipoIntervencion> findById(Integer idTipo) {
        EntityManager em = JpaUtil.getEntityManager();
        TipoIntervencion tipoIntervencion = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_tIntervencion_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, idTipo);

            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                tipoIntervencion = new TipoIntervencion();
                tipoIntervencion.setIdIntervencion((Integer) row[0]);
                tipoIntervencion.setNombre((String) row[1]);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(tipoIntervencion);
    }

    @Override
    public List<TipoIntervencion> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<TipoIntervencion> tiposIntervencion = new ArrayList<>();

        try {
            // Llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_tipos_intervencion");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                TipoIntervencion ti = new TipoIntervencion();
                ti.setIdIntervencion((Integer) row[0]);
                ti.setNombre((String) row[1]);

                tiposIntervencion.add(ti);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return tiposIntervencion;
    }

    @Override
    public Optional<TipoIntervencion> update(TipoIntervencion d) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_tipo_intervencion");
            query.registerStoredProcedureParameter("p_idTipoIntervencion", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);

            query.setParameter("p_idTipoIntervencion", d.getIdIntervencion());
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
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_tipo_intervencion");

            query.registerStoredProcedureParameter("p_id_tipo_intervencion", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, jakarta.persistence.ParameterMode.OUT);

            query.setParameter("p_id_tipo_intervencion", id);

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
