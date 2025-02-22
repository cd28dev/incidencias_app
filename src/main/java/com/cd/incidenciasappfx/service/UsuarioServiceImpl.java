
package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.repository.IUsuarioRepository;
import java.util.List;
import java.util.Optional;


/**
 * UsuarioServiceImpl.java
 * 
 * @author CDAA
 */
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;

    // Inyección manual del repositorio en el constructor
    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    
    @Override
    public Optional<Usuario> save(Usuario user) {
        
        return usuarioRepository.save(user);
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
