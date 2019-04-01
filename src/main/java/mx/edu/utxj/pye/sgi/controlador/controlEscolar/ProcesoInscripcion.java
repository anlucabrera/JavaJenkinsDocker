package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbProcesoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import org.omnifaces.util.Messages;

@Named(value = "procesoInscripcion")
@ViewScoped
public class ProcesoInscripcion implements Serializable{
    
    @Getter @Setter private ProcesosInscripcion procesosInscripcion;
    @Getter @Setter private Aspirante aspirante;
    @Getter @Setter private Persona persona;
    @Getter @Setter private List<Aspirante> listaAspirantesTSU;
    @Getter @Setter private List<Aspirante> listaAspirantesTSUXPE;
    @Getter @Setter private List<ProgramasEducativos> listaPe;
    @Getter @Setter private Integer folioFicha;
    @Getter @Setter private long totalRegistroSemanal,totalRegistroSabatino,totalRegistroSemanalValido,totalRegistroSabatinoValido;
    
    @EJB EjbFichaAdmision ejbFichaAdmision;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbSelectItemCE ejbSelectItemCE;
    
    @PostConstruct
    public void init(){
        aspirante = new Aspirante();
        persona = new Persona();
        procesosInscripcion = ejbFichaAdmision.getProcesoIncripcionTSU();
        listaAspirantesTSU = ejbProcesoInscripcion.listaAspirantesTSU(procesosInscripcion.getIdProcesosInscripcion());
        listaPe = ejbSelectItemCE.itemPEAll();
    }
    
    public void buscarFichaAdmision(){
        aspirante = ejbProcesoInscripcion.buscaAspiranteByFolio(folioFicha);
        if(aspirante != null){
            persona = aspirante.getIdPersona();
            Messages.addGlobalInfo("Registro encontrado exitosamente de "+persona.getNombre()+" !");
        }else{
            Messages.addGlobalError("No se encuentra registro de ficha de admisión con este folio !");
            folioFicha = null;
        }
    }
    
    public void validarFichaAdmision(){
        ejbFichaAdmision.actualizaAspirante(aspirante);
        String mensaje = "Estimado(a) "+persona.getNombre()+"\n\n Se le informa que su ficha de admisión ha sido validada correctamente, para continuar con el tu proceso de inscripción se le pide de favor que continúes con tu exámen institucional y ceneval.\n\n" +
                        "Datos de acceso a exámen Institucional: \n\n"
                        + "Url: http://escolar.utxj.edu.mx/utxj \n"
                        + "Usuario: "+aspirante.getFolioAspirante()+" \n" +
                          "Password: "+persona.getCurp()+"\n\n "+
                        "ATENTAMENTE \n" +
                        "Departamento de Servicios Escolares";
        enviarConfirmacionCorreoElectronico(aspirante, mensaje);
        init();
        folioFicha = null;
    }
    
    public void enviarConfirmacionCorreoElectronico(Aspirante aspirante,String msj){
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

        if(aspirante.getIdPersona().getMedioComunicacion().getEmail() != null && aspirante.getEstatus() == true){
            Session session = Session.getInstance(properties,null);
            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(correoEnvia,"Registro de Ficha de Admisión 2019 UTXJ"));

                InternetAddress internetAddress = new InternetAddress(aspirante.getIdPersona().getMedioComunicacion().getEmail());

                mimeMessage.setRecipient(Message.RecipientType.TO,internetAddress);
                mimeMessage.setSubject("Validación Ficha de Admisión");

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(msj);

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
        }
    }
    
    public void resetInput(){
        init();
        folioFicha = null;
    }
    
    public Long carlcularTotales(String clavePe){
        listaAspirantesTSUXPE = ejbProcesoInscripcion.lisAspirantesByPE(clavePe, procesosInscripcion.getIdProcesosInscripcion());
        
        totalRegistroSemanal = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal"))
                .count();
        
        totalRegistroSabatino = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino"))
                .count();
        
        totalRegistroSemanalValido = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal") && x.getEstatus() == true)
                .count();
        
        totalRegistroSabatinoValido = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino") && x.getEstatus() == true)
                .count();
        
        return totalRegistroSemanal;
    }
}
