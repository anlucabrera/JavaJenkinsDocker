package mx.edu.utxj.pye.sgi.ejb;


import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class prueba {

    public static void main(String[] args) {
        // El correo gmail de envío
        String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";
        String claveCorreo = "Serv.Escolares";

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
            mimeMessage.setFrom(new InternetAddress(correoEnvia,"Registro de Ficha de Admisión 2019"));

            InternetAddress[] internetAddress = {
                    new InternetAddress("juan.sanchez@utxicotepec.edu.mx")
            };

            mimeMessage.setRecipients(Message.RecipientType.TO,internetAddress);
            mimeMessage.setSubject("Registro Exitoso");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText("Estimado(a) Aspirante \n\n Gracias por elegir a la Universidad Tecnologica de Xicotepec de Juarez como opción para continuar con tus estudios de nivel superior." +
                    "\n\n Debes de esperar el correo de confirmacion de que tu información es correcta y posteriormente continuar con " +
                    "tu examen CENEVAL Exanii II y con el examen institucional\n\n\n" +
                    "ATENTAMENTE \n" +
                    "Departamento de Servicios Escolares");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia,claveCorreo);
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Correo enviado satisfactoriamente");
    }
}
