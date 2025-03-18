package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Usuario;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author CDAA
 */
public interface IUsuarioService {
    Optional<Usuario> save(Usuario user);
    Optional<Usuario> findById(String dni);
    List<Usuario> findAll();
    Optional<Usuario> update(Usuario user);
    boolean delete(Usuario u);
    int getNextUserId();
    Optional<Usuario> validarUsuario(String username, String password);
    Optional<Usuario> findByEmail(String email);
}
