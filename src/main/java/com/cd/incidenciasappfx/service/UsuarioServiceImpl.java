package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.helper.EmailHelper;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.repository.IUsuarioRepository;
import com.cd.incidenciasappfx.repository.UsuarioRepositoryImpl;
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
    public UsuarioServiceImpl() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    @Override
    public Optional<Usuario> save(Usuario user) {
        String pass = EmailHelper.genPassword();

        String asunto = "Creación de cuenta IncidenciasAPP";
        String mensaje = "<h3>Su cuenta fue creada con éxito.</h3></br><p>Su contraseña para acceder es: " + pass + "</p>";

        boolean isSendEmail = EmailHelper.sendEmail(user.getCorreo(), asunto, mensaje);

        if (isSendEmail) {
            user.setPassword(pass);
            return usuarioRepository.save(user);
        }
        
        return usuarioRepository.save(user);
    }

    @Override
    public Optional<Usuario> findById(String dni) {
        return usuarioRepository.findById(dni);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
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
