package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.helper.EmailHelper;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.repository.IUsuarioRepository;
import com.cd.incidenciasappfx.repository.UsuarioRepositoryImpl;
import java.util.HashSet;
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
        String mensaje = "Su cuenta fue creada con éxito.Su contraseña para acceder es: " + pass + "";

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
        
        if("ROLE_ADMIN".equals(user.getRol().getNombre())){
            user.getRol().setIdRol(1);  
        }else if("ROLE_OPERADOR".equals(user.getRol().getNombre())){
            user.getRol().setIdRol(2); 
        }
        
        return usuarioRepository.update(user);
    }

    @Override
    public void delete(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getNextUserId() {
        return usuarioRepository.getNextUserId();
    }

    
}
