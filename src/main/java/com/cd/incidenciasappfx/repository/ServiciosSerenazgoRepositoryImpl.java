package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Delito;
import com.cd.incidenciasappfx.models.ServicioSerenazgo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiciosSerenazgoRepositoryImpl implements IServiciosSerenazgoRepository{

    @Override
    public Optional<ServicioSerenazgo> save(ServicioSerenazgo servicioSerenazgo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_servicioserenazgo");

            query.registerStoredProcedureParameter("p_nombre", String.class, jakarta.persistence.ParameterMode.IN);
            query.setParameter("p_nombre", servicioSerenazgo.getNombre());

            query.execute();
            return Optional.of(servicioSerenazgo);

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<ServicioSerenazgo> findById(Integer idServicioSerenazgo) {
        EntityManager em = JpaUtil.getEntityManager();
        ServicioSerenazgo servicioSerenazgo = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_servicio_por_id");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, idServicioSerenazgo);

            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                servicioSerenazgo = new ServicioSerenazgo();
                servicioSerenazgo.setIdServicio((Integer) row[0]);
                servicioSerenazgo.setNombre((String) row[1]);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return Optional.ofNullable(servicioSerenazgo);
    }

    @Override
    public List<ServicioSerenazgo> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<ServicioSerenazgo> servicioSerenazgos = new ArrayList<>();

        try {
            // Llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_servicios");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                ServicioSerenazgo s = new ServicioSerenazgo();
                s.setIdServicio((Integer) row[0]);
                s.setNombre((String) row[1]);

                servicioSerenazgos.add(s);
            }

        } catch (jakarta.persistence.PersistenceException e) {
            handleSQLException(e);
        } finally {
            em.close();
        }

        return servicioSerenazgos;
    }

    @Override
    public Optional<ServicioSerenazgo> update(ServicioSerenazgo servicioSerenazgo) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_actualizar_servicios");
            query.registerStoredProcedureParameter("p_id_servicios", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);

            query.setParameter("p_id_servicios", servicioSerenazgo.getIdServicio());
            query.setParameter("p_nombre", servicioSerenazgo.getNombre());

            query.execute();

            return Optional.of(servicioSerenazgo);

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
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_servicio");

            query.registerStoredProcedureParameter("p_id_servicio", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, jakarta.persistence.ParameterMode.OUT);

            query.setParameter("p_id_servicio", id);

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
