package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.helper.EmailHelper;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.repository.IUsuarioRepository;
import com.cd.incidenciasappfx.repository.UsuarioRepositoryImpl;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * UsuarioServiceImpl.java
 *
 * @author CDAA
 */
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioServiceImpl() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    @Override
    @Transactional
    public Optional<Usuario> save(Usuario user) {
        String pass = EmailHelper.genPassword();
        String asunto = "Creación de cuenta IncidenciasAPP";
        String mensaje = "Su cuenta fue creada con éxito. Su contraseña para acceder es: " + pass;

        CompletableFuture<Boolean> emailFuture = CompletableFuture.supplyAsync(()
                -> EmailHelper.sendEmail(user.getCorreo(), asunto, mensaje)
        );

        try {
            boolean isSendEmail = emailFuture.get();

            if (!isSendEmail) {
                throw new RuntimeException("Error al enviar el correo. No se guardará el usuario.");
            }

            user.setPassword(pass);
            return usuarioRepository.save(user);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error en la ejecución del envío de correo.", e);
        }
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
    @Transactional
    public Optional<Usuario> update(Usuario user) {

        if ("ROLE_ADMIN".equals(user.getRol().getNombre())) {
            user.getRol().setIdRol(1);
        } else if ("ROLE_OPERADOR".equals(user.getRol().getNombre())) {
            user.getRol().setIdRol(2);
        }

        return usuarioRepository.update(user);
    }

    @Override
    public boolean delete(Usuario u) {
        String dni = u.getDni();
        return usuarioRepository.delete(dni);
    }

    @Override
    public int getNextUserId() {
        return usuarioRepository.getNextUserId();
    }

    @Override
    public Optional<Usuario> validarUsuario(String username, String password) {
        return usuarioRepository.validarUsuario(username, password);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        Optional<Usuario> user;
        user = usuarioRepository.findByEmail(email);
        if (user.isPresent()) {
            Usuario usuario = user.get();
            String pass = EmailHelper.genPassword();
            String asunto = "Reset Contraseña IncidenciasAPP";
            String mensaje = "User: " + usuario.getUsuario() + ", Su contraseña temporal es: " + pass;


            try {
                boolean isSendEmail = CompletableFuture.supplyAsync(() ->
                        EmailHelper.sendEmail(usuario.getCorreo(), asunto, mensaje)
                ).get(5, TimeUnit.SECONDS);

                if (!isSendEmail) {
                    return Optional.empty();
                }

                usuario.setPassword(pass);
                return usuarioRepository.update(usuario);

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Thread.currentThread().interrupt(); // 🔹 Restablecer el estado de interrupción
                throw new RuntimeException("Error al enviar el correo.", e);
            }
        }
        return Optional.empty();

    }

}
