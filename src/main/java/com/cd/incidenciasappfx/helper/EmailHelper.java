
package com.cd.incidenciasappfx.helper;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.security.SecureRandom;
import java.util.Properties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;


/**
 * EmailHelper.java
 * 
 * @author CDAA
 */

public class EmailHelper {

    public static String genPassword() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder clave = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            clave.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return clave.toString();
    }

    public static boolean sendEmail(String destinatario, String asunto, String mensaje) {
        final String remitente = "carlos.informatico.28@gmail.com";
        final String password = "waaxnffygmygkitz";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            Message message = (Message) new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);

            // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado correctamente.");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo: " + e.getMessage());
            return false;
        }
    }
}
