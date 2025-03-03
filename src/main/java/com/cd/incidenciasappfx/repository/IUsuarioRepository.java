
package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Usuario;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author CDAA
 */
public interface IUsuarioRepository {
    Optional<Usuario> save(Usuario user);
    Optional<Usuario> findById(String userId);
    List<Usuario> findAll();
    Optional<Usuario> update(Usuario user);
    void delete(int userId);
    Integer getNextUserId();
    Optional<Usuario> validarUsuario(String username,String password);
}
