package mx.edu.utxj.pye.sgi.util;

import java.io.Serializable;
import java.util.Properties;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.omnifaces.cdi.ViewScoped;

@Named
@ViewScoped
public class UtilidadesCorreosElectronicos implements Serializable {

    public void enviarConfirmacionCorreoElectronico(String correoDestino, String titulo, String asunto, String mensaje, Integer tipo) {
        // El correo gmail de envío
        String correoEnvia = "zabdiel.perez@utxicotepec.edu.mx";//correo del arrea de desarrollo
        String claveCorreo = "fp6inrls3";//contraseña del correo del arrea de desarrollo
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);

        if (correoDestino != null) {
            Session session = Session.getInstance(properties, null);
            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(correoEnvia, titulo));

                switch (tipo) {
                    case 1:
                        InternetAddress internetAddress = new InternetAddress(correoDestino);
                        mimeMessage.setRecipient(Message.RecipientType.TO, internetAddress);
                        break;
                    case 2:
                        InternetAddress[] internetAddresses = {
                            new InternetAddress(correoDestino),//correo del área en proceso de POA
                            new InternetAddress("zabimg@gmail.com"),//correo del recuros financieros
                            new InternetAddress("marcelino.lopez@utxicotepec.edu.mx")};//correo de planeacion
                        // Agregar los destinatarios al mensaje
                        mimeMessage.setRecipients(Message.RecipientType.TO, internetAddresses);
                        break;
                }

                mimeMessage.setSubject(asunto);

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(mensaje);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                mimeMessage.setContent(multipart);

                Transport transport = session.getTransport("smtp");
                transport.connect(correoEnvia, claveCorreo);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
