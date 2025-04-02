package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IncidenciaRepositoryImpl implements IIncidenciaRepository {

    @Override
    public Optional<Incidencia> save(Incidencia incidencia) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_insertar_incidencia");

            // Registrar los parámetros
            query.registerStoredProcedureParameter("p_direccion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_fecha_hora_incidencia", java.sql.Timestamp.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_descripcion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_user", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apoyo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_intervencion", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_delito", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_ocurrencia", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_servicio", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_urbanizacion", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_sector", Integer.class, ParameterMode.IN);

            // Parámetros del agraviado
            query.registerStoredProcedureParameter("p_nombre_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apellidos_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_telefono_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_doc_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_observacion_agr", String.class, ParameterMode.IN);

            // Parámetros del infractor
            query.registerStoredProcedureParameter("p_nombre_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apellidos_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_telefono_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_doc_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_observacion_inf", String.class, ParameterMode.IN);

            // Asignar valores
            query.setParameter("p_direccion", incidencia.getDireccion());
            query.setParameter("p_fecha_hora_incidencia", java.sql.Timestamp.valueOf(incidencia.getFecha_hora_incidencia()));
            query.setParameter("p_descripcion", incidencia.getDescripcion());
            query.setParameter("p_id_user", incidencia.getUser().getIdUsuario());
            query.setParameter("p_apoyo", incidencia.getApoyo_policial().name());

            query.setParameter("p_id_intervencion", !incidencia.getIntervenciones().isEmpty() ? incidencia.getIntervenciones().get(0).getIdIntervencion() : null);
            query.setParameter("p_id_delito", !incidencia.getDelitos().isEmpty() ? incidencia.getDelitos().get(0).getIdDelito() : null);
            query.setParameter("p_id_ocurrencia", !incidencia.getOcurrencias().isEmpty() ? incidencia.getOcurrencias().get(0).getIdOcurrencia() : null);
            query.setParameter("p_id_servicio", !incidencia.getServicios().isEmpty() ? incidencia.getServicios().get(0).getIdServicio() : null);
            query.setParameter("p_id_urbanizacion", !incidencia.getUrbanizaciones().isEmpty() ? incidencia.getUrbanizaciones().get(0).getId() : null);
            query.setParameter("p_id_sector", (!incidencia.getUrbanizaciones().isEmpty() && incidencia.getUrbanizaciones().get(0).getSector() != null) ? incidencia.getUrbanizaciones().get(0).getSector().getId() : null);


            query.setParameter("p_nombre_inf", incidencia.getInfractores().get(0).getPersona().getNombres());
            query.setParameter("p_apellidos_inf", incidencia.getInfractores().get(0).getPersona().getApellidos());
            query.setParameter("p_telefono_inf", incidencia.getInfractores().get(0).getPersona().getTelefono());
            query.setParameter("p_doc_inf", incidencia.getInfractores().get(0).getPersona().getDocumento());
            query.setParameter("p_observacion_inf", incidencia.getInfractores().get(0).getObservaciones());

            // Datos del Agraviado (si existen)
            if (incidencia.getAgraviados() != null) {
                query.setParameter("p_nombre_agr", incidencia.getAgraviados().get(0).getPersona().getNombres());
                query.setParameter("p_apellidos_agr", incidencia.getAgraviados().get(0).getPersona().getApellidos());
                query.setParameter("p_telefono_agr", incidencia.getAgraviados().get(0).getPersona().getTelefono());
                query.setParameter("p_doc_agr", incidencia.getAgraviados().get(0).getPersona().getDocumento());
                query.setParameter("p_observacion_agr", incidencia.getAgraviados().get(0).getObservaciones());
            } else {
                query.setParameter("p_nombre_agr", null);
                query.setParameter("p_apellidos_agr", null);
                query.setParameter("p_telefono_agr", null);
                query.setParameter("p_doc_agr", null);
                query.setParameter("p_observacion_agr", null);
            }


            query.execute();
            return Optional.of(incidencia);
        } catch (PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }


    @Override
    public Optional<Incidencia> findById(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_buscar_por_id");
            // Registrar y asignar el parámetro de entrada
            query.registerStoredProcedureParameter("p_id_incidencia", Integer.class, ParameterMode.IN);
            query.setParameter("p_id_incidencia", id);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Obtener resultados
            List<Object[]> resultados = query.getResultList();
            if (resultados.isEmpty()) {
                return Optional.empty();
            }

            // Procesar el primer resultado
            Object[] row = resultados.get(0);
            Incidencia incidencia = new Incidencia();

            incidencia.setIdIncidencia((Integer) row[0]);
            incidencia.setDireccion((String) row[1]);
            incidencia.setFecha_hora_incidencia(((Timestamp) row[2]).toLocalDateTime());
            incidencia.setDescripcion((String) row[3]);
            incidencia.setApoyo_policial((String) row[4]);

            // Usuario
            Usuario usuario = new Usuario();
            usuario.setIdUsuario((Integer) row[5]);
            usuario.setNombre((String) row[6]);
            usuario.setApellido((String) row[7]);
            incidencia.setUser(usuario);

            // Intervención
            TipoIntervencion tipoIntervencion = new TipoIntervencion();
            tipoIntervencion.setIdIntervencion((Integer) row[8]);
            tipoIntervencion.setNombre((String) row[9]);
            incidencia.setIntervenciones(tipoIntervencion);

            //Delito
            Delito delito = new Delito();
            delito.setIdDelito((Integer) row[10]);
            delito.setNombre((String) row[11]);
            incidencia.setDelitos(delito);


            // Ocurrencia
            TipoOcurrencia tipoOcurrencia = new TipoOcurrencia();
            tipoOcurrencia.setIdOcurrencia((Integer) row[12]);
            tipoOcurrencia.setNombre((String) row[13]);
            incidencia.setOcurrencias(tipoOcurrencia);

            // Servicio
            ServicioSerenazgo servicioSerenazgo = new ServicioSerenazgo();
            servicioSerenazgo.setIdServicio((Integer) row[14]);
            servicioSerenazgo.setNombre((String) row[15]);
            incidencia.setServicios(servicioSerenazgo);

            // Urbanización y sector
            Urbanizacion urbanizacion = new Urbanizacion();
            urbanizacion.setId((Integer) row[16]);
            urbanizacion.setNombre((String) row[17]);

            Sector sector = new Sector();
            sector.setId((Integer) row[18]);
            sector.setNombre((String) row[19]);

            urbanizacion.setSector(sector);
            incidencia.setUrbanizaciones(urbanizacion);


            // Agraviado
            Agraviado agraviado = new Agraviado();
            agraviado.getPersona().setIdPersona( (Integer) row[20]);
            agraviado.getPersona().setNombres((String) row[21]);
            agraviado.getPersona().setApellidos((String) row[22]);
            agraviado.getPersona().setTelefono((String) row[23]);
            agraviado.getPersona().setDocumento((String) row[24]);
            agraviado.setIdAgraviado((Integer) row[25]);
            agraviado.setObservaciones((String) row[26]);
            incidencia.setAgraviados(agraviado);



            Infractor infractor = new Infractor();
            infractor.getPersona().setIdPersona((Integer) row[27]);
            infractor.getPersona().setNombres((String) row[28]);
            infractor.getPersona().setApellidos((String) row[29]);
            infractor.getPersona().setTelefono((String) row[30]);
            infractor.getPersona().setDocumento((String) row[31]);
            infractor.setIdInfractor((Integer) row[32]);
            infractor.setObservaciones((String) row[33]);
            incidencia.setInfractores(infractor);


            return Optional.of(incidencia);
        } catch (PersistenceException e) {
            handleSQLException(e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Incidencia> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_incidencias");

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Obtener resultados
            List<Object[]> resultados = query.getResultList();
            List<Incidencia> incidencias = new ArrayList<>();

            for (Object[] row : resultados) {
                Incidencia incidencia = new Incidencia();

                incidencia.setIdIncidencia((Integer) row[0]);
                incidencia.setDireccion((String) row[1]);
                incidencia.setFecha_hora_incidencia(((Timestamp) row[2]).toLocalDateTime());
                incidencia.setDescripcion((String) row[3]);
                incidencia.setApoyo_policial((String) row[4]);

                // Usuario
                Usuario usuario = new Usuario();
                usuario.setIdUsuario((Integer) row[5]);
                usuario.setNombre((String) row[6]);
                usuario.setApellido((String) row[7]);
                incidencia.setUser(usuario);

                // Intervención
                TipoIntervencion tipoIntervencion = new TipoIntervencion();
                tipoIntervencion.setIdIntervencion((Integer) row[8]);
                tipoIntervencion.setNombre((String) row[9]);
                incidencia.setIntervenciones(tipoIntervencion);

                // Delito
                Delito delito = new Delito();
                delito.setIdDelito((Integer) row[10]);
                delito.setNombre((String) row[11]);
                incidencia.setDelitos(delito);

                // Ocurrencia
                TipoOcurrencia tipoOcurrencia = new TipoOcurrencia();
                tipoOcurrencia.setIdOcurrencia((Integer) row[12]);
                tipoOcurrencia.setNombre((String) row[13]);
                incidencia.setOcurrencias(tipoOcurrencia);

                // Servicio
                ServicioSerenazgo servicioSerenazgo = new ServicioSerenazgo();
                servicioSerenazgo.setIdServicio((Integer) row[14]);
                servicioSerenazgo.setNombre((String) row[15]);
                incidencia.setServicios(servicioSerenazgo);

                // Urbanización y sector
                Urbanizacion urbanizacion = new Urbanizacion();
                urbanizacion.setId((Integer) row[16]);
                urbanizacion.setNombre((String) row[17]);

                Sector sector = new Sector();
                sector.setId((Integer) row[18]);
                sector.setNombre((String) row[19]);

                urbanizacion.setSector(sector);
                incidencia.setUrbanizaciones(urbanizacion);

                // Agraviado
                Agraviado agraviado = new Agraviado();
                agraviado.getPersona().setIdPersona((Integer) row[20]);
                agraviado.getPersona().setNombres((String) row[21]);
                agraviado.getPersona().setApellidos((String) row[22]);
                agraviado.getPersona().setTelefono((String) row[23]);
                agraviado.getPersona().setDocumento((String) row[24]);
                agraviado.setIdAgraviado((Integer) row[25]);
                agraviado.setObservaciones((String) row[26]);
                incidencia.setAgraviados(agraviado);

                // Infractor
                Infractor infractor = new Infractor();
                infractor.getPersona().setIdPersona((Integer) row[27]);
                infractor.getPersona().setNombres((String) row[28]);
                infractor.getPersona().setApellidos((String) row[29]);
                infractor.getPersona().setTelefono((String) row[30]);
                infractor.getPersona().setDocumento((String) row[31]);
                infractor.setIdInfractor((Integer) row[32]);
                infractor.setObservaciones((String) row[33]);
                incidencia.setInfractores(infractor);

                incidencias.add(incidencia);
            }

            return incidencias;
        } catch (PersistenceException e) {
            handleSQLException(e);
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Incidencia> update(Incidencia incidencia) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_update_incidencia");

            // Registrar los parámetros
            query.registerStoredProcedureParameter("p_id_incidencia", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_direccion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_fecha_hora_incidencia", java.sql.Timestamp.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_descripcion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_user", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apoyo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_intervencion", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_delito", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_ocurrencia", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_servicio", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_urbanizacion", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_sector", Integer.class, ParameterMode.IN);

            // Parámetros del agraviado
            query.registerStoredProcedureParameter("p_nombre_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apellidos_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_telefono_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_doc_agr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_observacion_agr", String.class, ParameterMode.IN);

            // Parámetros del infractor
            query.registerStoredProcedureParameter("p_nombre_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apellidos_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_telefono_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_doc_inf", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_observacion_inf", String.class, ParameterMode.IN);

            // Asignar valores a los parámetros
            query.setParameter("p_id_incidencia", incidencia.getIdIncidencia());
            query.setParameter("p_direccion", incidencia.getDireccion());
            query.setParameter("p_fecha_hora_incidencia", java.sql.Timestamp.valueOf(incidencia.getFecha_hora_incidencia()));
            query.setParameter("p_descripcion", incidencia.getDescripcion());
            query.setParameter("p_id_user", incidencia.getUser().getIdUsuario());
            query.setParameter("p_apoyo", incidencia.getApoyo_policial().name());

            query.setParameter("p_id_intervencion", !incidencia.getIntervenciones().isEmpty() ? incidencia.getIntervenciones().get(0).getIdIntervencion() : null);
            query.setParameter("p_id_delito", !incidencia.getDelitos().isEmpty() ? incidencia.getDelitos().get(0).getIdDelito() : null);
            query.setParameter("p_id_ocurrencia", !incidencia.getOcurrencias().isEmpty() ? incidencia.getOcurrencias().get(0).getIdOcurrencia() : null);
            query.setParameter("p_id_servicio", !incidencia.getServicios().isEmpty() ? incidencia.getServicios().get(0).getIdServicio() : null);
            query.setParameter("p_id_urbanizacion", !incidencia.getUrbanizaciones().isEmpty() ? incidencia.getUrbanizaciones().get(0).getId() : null);
            query.setParameter("p_id_sector", (!incidencia.getUrbanizaciones().isEmpty() && incidencia.getUrbanizaciones().get(0).getSector() != null) ? incidencia.getUrbanizaciones().get(0).getSector().getId() : null);

            // Datos del Agraviado (si existen)
            if (incidencia.getAgraviados() != null) {
                query.setParameter("p_nombre_agr", incidencia.getAgraviados().get(0).getPersona().getNombres());
                query.setParameter("p_apellidos_agr", incidencia.getAgraviados().get(0).getPersona().getApellidos());
                query.setParameter("p_telefono_agr", incidencia.getAgraviados().get(0).getPersona().getTelefono());
                query.setParameter("p_doc_agr", incidencia.getAgraviados().get(0).getPersona().getDocumento());
                query.setParameter("p_observacion_agr", incidencia.getAgraviados().get(0).getObservaciones());
            } else {
                query.setParameter("p_nombre_agr", null);
                query.setParameter("p_apellidos_agr", null);
                query.setParameter("p_telefono_agr", null);
                query.setParameter("p_doc_agr", null);
                query.setParameter("p_observacion_agr", null);
            }

            // Datos del Infractor (si existen)
            if (incidencia.getInfractores() != null) {
                query.setParameter("p_nombre_inf", incidencia.getInfractores().get(0).getPersona().getNombres());
                query.setParameter("p_apellidos_inf", incidencia.getInfractores().get(0).getPersona().getApellidos());
                query.setParameter("p_telefono_inf", incidencia.getInfractores().get(0).getPersona().getTelefono());
                query.setParameter("p_doc_inf", incidencia.getInfractores().get(0).getPersona().getDocumento());
                query.setParameter("p_observacion_inf", incidencia.getInfractores().get(0).getObservaciones());
            } else {
                query.setParameter("p_nombre_inf", null);
                query.setParameter("p_apellidos_inf", null);
                query.setParameter("p_telefono_inf", null);
                query.setParameter("p_doc_inf", null);
                query.setParameter("p_observacion_inf", null);
            }

            query.execute();
            return Optional.of(incidencia);
        } catch (PersistenceException e) {
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
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_eliminar_incidencia_id");

            // Registrar el parámetro de entrada
            query.registerStoredProcedureParameter("p_id_incidencia", Integer.class, ParameterMode.IN);

            // Asignar el valor del parámetro
            query.setParameter("p_id_incidencia", id);

            // Ejecutar el procedimiento almacenado
            query.execute();

            return true; // Eliminación exitosa
        } catch (PersistenceException e) {
            handleSQLException(e);
            return false; // Eliminación fallida
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
