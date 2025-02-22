package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.StoredProcedureQuery;
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
            query.registerStoredProcedureParameter("p_id_rol", Integer.class, jakarta.persistence.ParameterMode.IN);

            query.setParameter("p_nombre", user.getNombre());
            query.setParameter("p_apellido", user.getApellido());
            query.setParameter("p_dni", user.getDni());
            query.setParameter("p_correo", user.getCorreo());
            query.setParameter("p_usuario", user.getUsuario());
            query.setParameter("p_password", user.getPassword());
            query.setParameter("p_id_rol", user.getRol().getIdRol());

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
    public Optional<Usuario> findById(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Usuario> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Usuario> update(Usuario user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
