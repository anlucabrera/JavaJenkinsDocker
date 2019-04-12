/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author UTXJ
 */
public class EnvioCorreos {
    
    
    public static void EnviarCorreoTxt(String correoEnvia,String claveCorreo,String descripcionCorreo,String asunto,String correo,String msj){
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);
        
        Session session = Session.getInstance(properties,null);
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correoEnvia,descripcionCorreo));
            InternetAddress internetAddress = new InternetAddress(correo);
            
            mimeMessage.setRecipient(Message.RecipientType.TO,internetAddress);
            mimeMessage.setSubject(asunto);
            
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(msj);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia,claveCorreo);
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void EnviarCorreoArchivos(String correoEnvia,String claveCorreo,String descripcionCorreo,String asunto,String correo,String msj,DataSource dataSource,String nombreArchivo){
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);
        
        Session session = Session.getInstance(properties,null);
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correoEnvia,descripcionCorreo));
            InternetAddress internetAddress = new InternetAddress(correo);
            
            mimeMessage.setRecipient(Message.RecipientType.TO,internetAddress);
            mimeMessage.setSubject(asunto);
            
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            MimeBodyPart documentoAdjunto = new MimeBodyPart();
            
            mimeBodyPart.setText(msj);
            documentoAdjunto.setDataHandler(new DataHandler(dataSource));
            documentoAdjunto.setFileName(nombreArchivo+".pdf");
            
            Multipart multipart = new MimeMultipart();
                
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(documentoAdjunto);
                
            mimeMessage.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia,claveCorreo);
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
