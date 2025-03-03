package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author CDAA
 */
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private final EntityManager entityManager;

    public UsuarioRepositoryImpl() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    @Override
    public Optional<Usuario> save(Usuario user) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_insertar_usuario");
            query.registerStoredProcedureParameter("p_nombre", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apellido", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_dni", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_correo", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_usuario", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_password", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre_rol", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_foto", String.class, jakarta.persistence.ParameterMode.IN);

            query.setParameter("p_nombre", user.getNombre());
            query.setParameter("p_apellido", user.getApellido());
            query.setParameter("p_dni", user.getDni());
            query.setParameter("p_correo", user.getCorreo());
            query.setParameter("p_usuario", user.getUsuario());
            query.setParameter("p_password", user.getPassword());
            query.setParameter("p_nombre_rol", user.getRol().getNombre());
            query.setParameter("p_foto", user.getFoto());

            query.execute();
            return Optional.of(user);

        } catch (jakarta.persistence.PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                String sqlState = sqlEx.getSQLState();
                if ("45000".equals(sqlState)) {
                    System.out.println("Error: " + sqlEx.getMessage());
                }
            }

            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> findById(String dni) {
        EntityManager em = JpaUtil.getEntityManager();
        Usuario usuario = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("buscar_usuario_por_dni");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, dni);

            List<Object[]> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                Object[] row = resultados.get(0);
                usuario = new Usuario();
                usuario.setIdUsuario((Integer) row[0]);
                usuario.setUsuario((String) row[1]);
                usuario.setNombre((String) row[2]);
                usuario.setApellido((String) row[3]);
                usuario.setDni((String) row[4]);
                usuario.setCorreo((String) row[5]);
                Rol rol = new Rol();
                rol.setIdRol((Integer) row[6]);
                rol.setNombre((String) row[7]);
                usuario.setFoto((String) row[8]);
                usuario.setRol(rol);
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

        return Optional.ofNullable(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Usuario> usuarios = new ArrayList<>();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_listar_usuarios");
            List<Object[]> resultados = query.getResultList();

            for (Object[] row : resultados) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario((Integer) row[0]);
                usuario.setNombre((String) row[1]);
                usuario.setApellido((String) row[2]);
                usuario.setDni((String) row[3]);
                usuario.setCorreo((String) row[4]);
                usuario.setUsuario((String) row[5]);

                Rol rol = new Rol();
                rol.setIdRol((Integer) row[6]);
                rol.setNombre((String) row[7]);
                usuario.setRol(rol);

                usuarios.add(usuario);
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

        return usuarios;
    }

    @Override
    public Optional<Usuario> update(Usuario user) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_actualizar_usuario");

            // Registrar parámetros de entrada
            query.registerStoredProcedureParameter("p_id_usuario", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_apellido", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_dni", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_correo", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_usuario", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_rol", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_foto", String.class, jakarta.persistence.ParameterMode.IN);

            // Establecer valores de los parámetros
            query.setParameter("p_id_usuario", user.getIdUsuario());
            query.setParameter("p_nombre", user.getNombre());
            query.setParameter("p_apellido", user.getApellido());
            query.setParameter("p_dni", user.getDni());
            query.setParameter("p_correo", user.getCorreo());
            query.setParameter("p_usuario", user.getUsuario());
            query.setParameter("p_id_rol", user.getRol().getIdRol());
            query.setParameter("p_foto", user.getFoto());

            // Ejecutar el procedimiento almacenado
            query.execute();

            return Optional.of(user);

        } catch (jakarta.persistence.PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                String sqlState = sqlEx.getSQLState();
                if ("45000".equals(sqlState)) {
                    System.out.println("Error: " + sqlEx.getMessage());
                }
            }

            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(String dni) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("eliminar_usuario_por_dni");

            query.registerStoredProcedureParameter("p_dni", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", Integer.class, jakarta.persistence.ParameterMode.OUT);

            query.setParameter("p_dni", dni);

            query.execute();

            Integer resultado = (Integer) query.getOutputParameterValue("p_resultado");

            return resultado != null && resultado == 1; 

        } catch (jakarta.persistence.PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                String sqlState = sqlEx.getSQLState();
                if ("45000".equals(sqlState)) {
                    System.out.println("Error: " + sqlEx.getMessage());
                }
            }

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer getNextUserId() {
        EntityManager em = JpaUtil.getEntityManager();
        Integer nextId = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("GetNextUserId");
            nextId = (Integer) query.getSingleResult();
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

        return nextId;
    }

    @Override
    public Optional<Usuario> validarUsuario(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        Usuario usuario = null;

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_validar_usuario");

            // Registrar los parámetros del procedimiento almacenado
            query.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_existe", Boolean.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("p_rol", String.class, ParameterMode.OUT);

            // Asignar valores a los parámetros de entrada
            query.setParameter("p_username", username);
            query.setParameter("p_password", password);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Obtener valores de salida
            Boolean existe = (Boolean) query.getOutputParameterValue("p_existe");
            String rolNombre = (String) query.getOutputParameterValue("p_rol");

            if (Boolean.TRUE.equals(existe)) {
                usuario = new Usuario();
                usuario.setUsuario(username);

                // Crear y asignar el rol
                Rol rol = new Rol();
                rol.setNombre(rolNombre);
                usuario.setRol(rol);
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

        return Optional.ofNullable(usuario);
    }

}
