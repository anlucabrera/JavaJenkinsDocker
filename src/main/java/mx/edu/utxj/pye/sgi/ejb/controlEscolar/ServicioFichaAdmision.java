/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.google.zxing.NotFoundException;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import nl.lcs.qrscan.core.QrPdf;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioFichaAdmision implements EjbFichaAdmision {

    @EJB FacadeCE facadeCE;
    @EJB EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;

    @Override
    public void GuardaPersona(Persona persona) {
        facadeCE.create(persona);
    }

    @Override
    public Persona actualizaPersona(Persona persona) {
        facadeCE.setEntityClass(Persona.class);
        facadeCE.edit(persona);
        facadeCE.flush();
        return persona;
    }

    @Override
    public Persona leerCurp(Part file) {
        Persona p = new Persona();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        String rutaRelativa = "";

        try{
            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioFichaAdmision");
            //Almacenamiento del archivo de la curp de la persona
            String ruta = ServicioArchivos.genRutaRelativa("curps");
            String rutaR = ServicioArchivos.carpetaRaiz.concat(ruta);
            String rutaArchivo = ruta.concat(file.getSubmittedFileName());
            ServicioArchivos.addCarpetaRelativa(rutaR);
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            rutaRelativa = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
            file.write(rutaArchivo);

            //Leer codigo QR de archivo de la Curp

            QrPdf pdf = new QrPdf(Paths.get(rutaR.concat(file.getSubmittedFileName())));
            String qrCode = pdf.getQRCode(1, true, true);
            String fecha_nacimiento = "";
            String[] parts = qrCode.split("\\|\\|");
            String continuacion = "";
            String[] parts1 = null;

            if(buscaPersonaByCurp(parts[0]) != null && parts.length > 1){
                p = buscaPersonaByCurp(parts[0]);
            }else{
                if((parts != null && parts.length > 1) || parts1 != null) {
                    continuacion = parts[1];
                    parts1 = continuacion.split("\\|");
                    if(parts1.length == 7){
                        p.setCurp(parts[0]);
                        p.setApellidoPaterno(ucFirst(parts1[0]).trim());
                        p.setApellidoMaterno(ucFirst(parts1[1]).trim());
                        p.setNombre(ucFirst(parts1[2]));
                        if(parts1[3].equals("HOMBRE"))
                            p.setGenero((short) 2);
                        if(parts1[3].equals("MUJER"))
                            p.setGenero((short) 1);
                        fecha_nacimiento = parts1[4].replace("/","-");
                        p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                        p.setEstado(Integer.valueOf(parts1[6]));
                        p.setUrlCurp(rutaRelativa);
                    }else if(parts1.length == 6){
                        p.setCurp(parts[0]);
                        p.setApellidoPaterno(ucFirst(parts1[0]));
                        p.setApellidoMaterno(ucFirst(parts1[1]));
                        p.setNombre(ucFirst(parts1[2]));
                        p.setGenero((short) 1);
                        if(parts1[3].equals("HOMBRE"))
                            p.setGenero((short) 2);
                        if(parts1[3].equals("MUJER"))
                            p.setGenero((short) 1);
                        fecha_nacimiento = parts1[4].replace("/","-");
                        p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                        p.setUrlCurp(rutaRelativa);
                    }else{
                        ServicioArchivos.eliminarArchivo(rutaRelativa);
                        p = new Persona();
                    }
                }
            }
        }catch (IOException ex){
            Logger.getLogger(ServicioFichaAdmision.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServicioFichaAdmision.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NotFoundException ex){
            ServicioArchivos.eliminarArchivo(rutaRelativa);
            Logger.getLogger(ServicioFichaAdmision.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;
    }

    @Override
    public ProcesosInscripcion getProcesoIncripcionTSU() {
        try {
            return facadeCE.getEntityManager().createQuery("SELECT pi FROM ProcesosInscripcion pi WHERE pi.activoNi = true AND pi.fechaInicio <= :fi AND pi.fechaFin >= :ff",ProcesosInscripcion.class)
                    .setParameter("fi", new Date())
                    .setParameter("ff", new Date())
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<SelectItem> listaGeneros() {
        List<SelectItem> lsg = new ArrayList<>();
        try {
            ejbDatosUsuarioLogeado.mostrarListaGeneros().stream()
                    .map((g)-> new SelectItem(g.getGenero(), g.getNombre()))
                    .forEachOrdered(selectItem -> lsg.add(selectItem));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return lsg;
    }

    @Override
    public Persona buscaPersonaByCurp(String curpBusqueda) {
        Persona  persona = new Persona();
        TypedQuery<Persona> p = facadeCE.getEntityManager().createNamedQuery("Persona.findByCurp",Persona.class)
                .setParameter("curp",curpBusqueda);
        List<Persona> personas = p.getResultList();

        if(personas.isEmpty()){
            persona = null;
        }else{
            persona = personas.get(0);
        }

        return  persona;
    }

    @Override
    public void guardaDatosMedicos(DatosMedicos datosMedicos) {
        facadeCE.create(datosMedicos);
    }

    @Override
    public void actualizaDatosMedicos(DatosMedicos datosMedicos) {
        facadeCE.setEntityClass(DatosMedicos.class);
        facadeCE.edit(datosMedicos);
        facadeCE.flush();
    }

    @Override
    public void guardaComunicacion(MedioComunicacion comunicacion) {
        facadeCE.create(comunicacion);
    }

    @Override
    public void actualizaCamunicacion(MedioComunicacion comunicacion) {
        facadeCE.setEntityClass(MedioComunicacion.class);
        facadeCE.edit(comunicacion);
        facadeCE.flush();
    }

    @Override
    public void guardaAspirante(Aspirante aspirante) {
        TipoAspirante tipoAspirante = new TipoAspirante((short)1, "Nuevo Ingreso");
        aspirante.setTipoAspirante(tipoAspirante);
        facadeCE.create(aspirante);
    }

    @Override
    public Aspirante buscaAspiranteByClave(Integer id) {
        return  facadeCE.getEntityManager().createQuery("SELECT a FROM Aspirante a WHERE a.idPersona.idpersona = :idP",Aspirante.class)
                .setParameter("idP",id)
                .getSingleResult();
    }

    @Override
    public void actualizaAspirante(Aspirante aspirante) {
        facadeCE.setEntityClass(Aspirante.class);
        facadeCE.edit(aspirante);
        facadeCE.flush();
    }

    @Override
    public Integer verificarFolio(ProcesosInscripcion procesosInscripcion) {
        String folio = "";
        int folioUlizable = 0;

        String anyo2 = new SimpleDateFormat("yy").format(new Date());
        folio = anyo2.concat(String.valueOf(procesosInscripcion.getIdPeriodo())).concat("0000");

        TypedQuery<Integer> v = (TypedQuery<Integer>) facadeCE.getEntityManager().createQuery("SELECT MAX(p.folioAspirante) FROM Aspirante AS p WHERE p.idProcesoInscripcion.idProcesosInscripcion =:idPE AND p.tipoAspirante.idTipoAspirante = 1")
                .setParameter("idPE", procesosInscripcion.getIdProcesosInscripcion());

        if(v.getSingleResult() == null){
            folioUlizable = Integer.valueOf(folio);
        }else{
            folioUlizable = v.getSingleResult()+1;
        }

        return folioUlizable;
    }

    @Override
    public void guardaDomicilo(Domicilio domicilio) {
        domicilio.setCalle(ucFirst(domicilio.getCalle()));
        domicilio.setCalleProcedencia(ucFirst(domicilio.getCalleProcedencia()));
        facadeCE.create(domicilio);
    }

    @Override
    public void actualizaDomicilio(Domicilio domicilio) {
        domicilio.setCalle(ucFirst(domicilio.getCalle()));
        domicilio.setCalleProcedencia(ucFirst(domicilio.getCalleProcedencia()));
        facadeCE.setEntityClass(Domicilio.class);
        facadeCE.edit(domicilio);
        facadeCE.flush();
    }

    @Override
    public void guardaTutorFamiliar(TutorFamiliar tutorFamiliar) {
        tutorFamiliar.setNombre(ucFirst(tutorFamiliar.getNombre()));
        tutorFamiliar.setApellidoPaterno(ucFirst(tutorFamiliar.getApellidoPaterno()));
        tutorFamiliar.setApellidoMaterno(ucFirst(tutorFamiliar.getApellidoMaterno()));
        tutorFamiliar.setCalle(ucFirst(tutorFamiliar.getCalle()));
        facadeCE.create(tutorFamiliar);
    }

    @Override
    public void actualizaTutorFamiliar(TutorFamiliar tutorFamiliar) {
        tutorFamiliar.setNombre(ucFirst(tutorFamiliar.getNombre()));
        tutorFamiliar.setApellidoPaterno(ucFirst(tutorFamiliar.getApellidoPaterno()));
        tutorFamiliar.setApellidoMaterno(ucFirst(tutorFamiliar.getApellidoMaterno()));
        tutorFamiliar.setCalle(ucFirst(tutorFamiliar.getCalle()));
        facadeCE.setEntityClass(TutorFamiliar.class);
        facadeCE.edit(tutorFamiliar);
        facadeCE.flush();
    }


    @Override
    public void guardaDatosFamiliares(DatosFamiliares datosFamiliares) {
        datosFamiliares.setNombrePadre(ucFirst(datosFamiliares.getNombrePadre()));
        datosFamiliares.setNombreMadre(ucFirst(datosFamiliares.getNombreMadre()));
        facadeCE.create(datosFamiliares);
    }

    @Override
    public void actualizaDatosFamiliares(DatosFamiliares datosFamiliares) {
        datosFamiliares.setNombrePadre(ucFirst(datosFamiliares.getNombrePadre()));
        datosFamiliares.setNombreMadre(ucFirst(datosFamiliares.getNombreMadre()));
        facadeCE.setEntityClass(DatosFamiliares.class);
        facadeCE.edit(datosFamiliares);
        facadeCE.flush();
    }

    @Override
    public void guardaDatosAcademicos(DatosAcademicos datosAcademicos) {
        facadeCE.create(datosAcademicos);
    }

    @Override
    public void actualizaDatosAcademicos(DatosAcademicos datosAcademicos) {
        facadeCE.setEntityClass(DatosAcademicos.class);
        facadeCE.edit(datosAcademicos);
        facadeCE.flush();
    }

    @Override
    public Iems buscaIemsByClave(Integer id) {
        return facadeCE.getEntityManager().find(Iems.class,id);
    }

    @Override
    public ProgramasEducativos buscaPEByClave(String clave) {
        return facadeCE.getEntityManager().createNamedQuery("ProgramasEducativos.findBySiglas",ProgramasEducativos.class)
                .setParameter("siglas",clave)
                .getSingleResult();
    }

    @Override
    public DocumentoAspirante guardaRequiitos(Part file, Aspirante aspirante, String tipoRequisito) throws IOException {
        String ruta = ServicioArchivos.genRutaRelativa("aspirantes");
        String rutaR = ServicioArchivos.carpetaRaiz.concat(ruta).concat("\\").concat(String.valueOf(aspirante.getFolioAspirante())).concat("\\").concat(tipoRequisito).concat("\\");
        String rutaArchivo = ruta.concat(String.valueOf(aspirante.getFolioAspirante())).concat("\\").concat(tipoRequisito).concat("\\").concat(file.getSubmittedFileName());
        ServicioArchivos.addCarpetaRelativa(rutaR);
        ServicioArchivos.eliminarArchivo(rutaArchivo);
        String rutaRelativa = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
        file.write(rutaArchivo);


        DocumentoAspirante documentoAspirante = new DocumentoAspirante();
        if(aspirante.getDocumentoAspirante() == null){
            documentoAspirante.setAspirante(aspirante.getIdAspirante());
            documentoAspirante.setEvidenciaCurp(aspirante.getIdPersona().getUrlCurp());
            if(tipoRequisito.equals("ActaNacimiento")){
                documentoAspirante.setEvidenciaActaNacimiento(rutaRelativa);
            }
            if(tipoRequisito.equals("HistorialAcademico")){
                documentoAspirante.setEvidenciaHistorialAcademico(rutaRelativa);
            }
            String mensaje = "Estimado(a) "+aspirante.getIdPersona().getNombre()+"\n\n Gracias por elegir a la Universidad Tecnologica de Xicotepec de Juárez como opción para continuar con tus estudios de nivel superior." +
                        "\n\n Debes de esperar el correo de confirmacion de que tu información es correcta y posteriormente continuar con " +
                        "tu examen CENEVAL Exanii II y con el examen institucional\n\n\n" +
                        "ATENTAMENTE \n" +
                        "Departamento de Servicios Escolares";
            if(documentoAspirante.getEvidenciaCurp() != null && documentoAspirante.getEvidenciaActaNacimiento() != null && documentoAspirante.getEvidenciaHistorialAcademico() != null){
                enviarConfirmacionCorreoElectronico(aspirante,mensaje);
            }
            facadeCE.create(documentoAspirante);
        }else{
            documentoAspirante = aspirante.getDocumentoAspirante();
            if(tipoRequisito.equals("ActaNacimiento")){
                documentoAspirante.setEvidenciaActaNacimiento(rutaRelativa);
            }
            if(tipoRequisito.equals("HistorialAcademico")){
                documentoAspirante.setEvidenciaHistorialAcademico(rutaRelativa);
            }
            String mensaje = "Estimado(a) "+aspirante.getIdPersona().getNombre()+"\n\n Has realizado cambios en los requisitos de ficha de admisión, espera a que estos sean validados por el área correspondiente\n\n\n" +
                        "ATENTAMENTE \n" +
                        "Departamento de Servicios Escolares";
            if(documentoAspirante.getEvidenciaCurp() != null && documentoAspirante.getEvidenciaActaNacimiento() != null && documentoAspirante.getEvidenciaHistorialAcademico() != null){
                enviarConfirmacionCorreoElectronico(aspirante,mensaje);
            }
            facadeCE.edit(documentoAspirante);
            facadeCE.flush();
        }

        
        return documentoAspirante;

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

        if(aspirante.getIdPersona().getMedioComunicacion().getEmail() != null){
            Session session = Session.getInstance(properties,null);
            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(correoEnvia,"Registro de Ficha de Admisión 2019 UTXJ"));

                InternetAddress internetAddress = new InternetAddress(aspirante.getIdPersona().getMedioComunicacion().getEmail());

                mimeMessage.setRecipient(Message.RecipientType.TO,internetAddress);
                mimeMessage.setSubject("Registro Exitoso");

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

    public static String ucFirst(String str){
        String strTemp = "";

        if (str == null || str.isEmpty()) {
            return str;
        } else {
            for (String palabra : str.split(" ")) {
                strTemp += palabra.substring(0, 1).toUpperCase() + palabra.substring(1, palabra.length()).toLowerCase() + " ";
                strTemp.trim();
            }
            return  strTemp;
        }
    }

    @Override
    public void actualizaDocumentosAspirante(DocumentoAspirante documentoAspirante) {
        facadeCE.setEntityClass(DocumentoAspirante.class);
        facadeCE.edit(documentoAspirante);
        facadeCE.flush();
    }
}
