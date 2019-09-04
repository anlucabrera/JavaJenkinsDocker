/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.google.zxing.NotFoundException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import nl.lcs.qrscan.core.QrPdf;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioFichaAdmision implements EjbFichaAdmision {

    @EJB FacadeCE facadeCE;
    @EJB EjbPersonal ejbPersonal;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
//    @EJB Facade2 f;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = facadeCE.getEntityManager();
    }
    
    @Override
    public void GuardaPersona(Persona persona) {
        em.persist(persona);
    }

    @Override
    public Persona actualizaPersona(Persona persona) {
        facadeCE.setEntityClass(Persona.class);
        em.merge(persona);
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
            String[] parts = qrCode.split("\\|");
            if(buscaPersonaByCurp(parts[0]) != null){
                p = buscaPersonaByCurp(parts[0]);
            }else{
                if((parts != null && (parts.length == 8 || parts.length == 9))) {
                    if(parts.length == 9){
                        p.setCurp(parts[0]);
                        p.setApellidoPaterno(ucFirst(parts[2]).trim());
                        p.setApellidoMaterno(ucFirst(parts[3]).trim());
                        p.setNombre(ucFirst(parts[4]));
                        if(parts[5].equals("HOMBRE"))
                            p.setGenero((short) 2);
                        if(parts[5].equals("MUJER"))
                            p.setGenero((short) 1);
                        fecha_nacimiento = parts[6].replace("/","-");
                        p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                        String claveEstado = parts[0].substring(11, 13);
                        
                        Estado estado = em.createNamedQuery("Estado.findByClave", Estado.class)
                                .setParameter("clave",claveEstado)
                                .getResultList()
                                .stream().findFirst().orElse(null);
                        
                        p.setEstado(estado.getIdestado());
                        p.setUrlCurp(rutaRelativa);
                    }else if(parts.length == 8){
                        p.setCurp(parts[0]);
                        p.setApellidoPaterno(ucFirst(parts[2]));
                        p.setApellidoMaterno(ucFirst(parts[3]));
                        p.setNombre(ucFirst(parts[4]));
                        p.setGenero((short) 1);
                        if(parts[5].equals("HOMBRE"))
                            p.setGenero((short) 2);
                        if(parts[5].equals("MUJER"))
                            p.setGenero((short) 1);
                        fecha_nacimiento = parts[6].replace("/","-");
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
            return em.createQuery("SELECT pi FROM ProcesosInscripcion pi WHERE pi.activoNi = true AND pi.fechaInicio <= :fi AND pi.fechaFin >= :ff",ProcesosInscripcion.class)
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
            ejbPersonal.mostrarListaGeneros().stream()
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
        TypedQuery<Persona> p = em.createNamedQuery("Persona.findByCurp",Persona.class)
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
        em.persist(datosMedicos);
    }

    @Override
    public void actualizaDatosMedicos(DatosMedicos datosMedicos) {
        facadeCE.setEntityClass(DatosMedicos.class);
        em.merge(datosMedicos);
        facadeCE.flush();
    }

    @Override
    public void guardaComunicacion(MedioComunicacion comunicacion) {
        em.persist(comunicacion);
    }

    @Override
    public void actualizaComunicacion(MedioComunicacion comunicacion) {
        facadeCE.setEntityClass(MedioComunicacion.class);
        em.merge(comunicacion);
        facadeCE.flush();
    }

    @Override
    public Aspirante guardaAspirante(Aspirante aspirante) {
        TipoAspirante tipoAspirante = new TipoAspirante((short)1, "Nuevo Ingreso");
        aspirante.setTipoAspirante(tipoAspirante);
        em.persist(aspirante);
        facadeCE.flush();
        return aspirante;
    }

    @Override
    public Aspirante buscaAspiranteByClave(Integer id) {
        return  em.createQuery("SELECT a FROM Aspirante a WHERE a.idPersona.idpersona = :idP",Aspirante.class)
                .setParameter("idP",id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public void actualizaAspirante(Aspirante aspirante) {
        facadeCE.setEntityClass(Aspirante.class);
        em.merge(aspirante);
        facadeCE.flush();
    }

    @Override
    public Integer verificarFolio(ProcesosInscripcion procesosInscripcion) {
        String folio = "";
        int folioUlizable = 0;

        String anyo2 = new SimpleDateFormat("yy").format(new Date());
        folio = anyo2.concat(String.valueOf(procesosInscripcion.getIdPeriodo())).concat("0000");

        TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(p.folioAspirante) FROM Aspirante AS p WHERE p.idProcesoInscripcion.idProcesosInscripcion =:idPE AND p.tipoAspirante.idTipoAspirante = 1")
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
        domicilio.setCalle(ucFirst(domicilio.getCalle().trim()));
        domicilio.setCalleProcedencia(ucFirst(domicilio.getCalleProcedencia().trim()));
        em.persist(domicilio);
    }

    @Override
    public void actualizaDomicilio(Domicilio domicilio) {
        domicilio.setCalle(ucFirst(domicilio.getCalle().trim()));
        domicilio.setCalleProcedencia(ucFirst(domicilio.getCalleProcedencia().trim()));
        facadeCE.setEntityClass(Domicilio.class);
        em.merge(domicilio);
        facadeCE.flush();
    }

    @Override
    public void guardaTutorFamiliar(TutorFamiliar tutorFamiliar) {
        tutorFamiliar.setNombre(ucFirst(tutorFamiliar.getNombre().trim()));
        tutorFamiliar.setApellidoPaterno(ucFirst(tutorFamiliar.getApellidoPaterno().trim()));
        tutorFamiliar.setApellidoMaterno(ucFirst(tutorFamiliar.getApellidoMaterno().trim()));
        tutorFamiliar.setCalle(ucFirst(tutorFamiliar.getCalle().trim()));
        em.persist(tutorFamiliar);
    }

    @Override
    public void actualizaTutorFamiliar(TutorFamiliar tutorFamiliar) {
        tutorFamiliar.setNombre(ucFirst(tutorFamiliar.getNombre().trim()));
        tutorFamiliar.setApellidoPaterno(ucFirst(tutorFamiliar.getApellidoPaterno().trim()));
        tutorFamiliar.setApellidoMaterno(ucFirst(tutorFamiliar.getApellidoMaterno().trim()));
        tutorFamiliar.setCalle(ucFirst(tutorFamiliar.getCalle().trim()));
        tutorFamiliar.setParentesco(ucFirst(tutorFamiliar.getParentesco().trim()));
        facadeCE.setEntityClass(TutorFamiliar.class);
        em.merge(tutorFamiliar);
        facadeCE.flush();
    }


    @Override
    public void guardaDatosFamiliares(DatosFamiliares datosFamiliares) {
        datosFamiliares.setNombrePadre(ucFirst(datosFamiliares.getNombrePadre().trim()));
        datosFamiliares.setNombreMadre(ucFirst(datosFamiliares.getNombreMadre().trim()));
        em.persist(datosFamiliares);
    }

    @Override
    public void actualizaDatosFamiliares(DatosFamiliares datosFamiliares) {
        datosFamiliares.setNombrePadre(ucFirst(datosFamiliares.getNombrePadre().trim()));
        datosFamiliares.setNombreMadre(ucFirst(datosFamiliares.getNombreMadre().trim()));
        facadeCE.setEntityClass(DatosFamiliares.class);
        em.merge(datosFamiliares);
        facadeCE.flush();
    }

    @Override
    public void guardaDatosAcademicos(DatosAcademicos datosAcademicos) {
        em.persist(datosAcademicos);
    }

    @Override
    public void actualizaDatosAcademicos(DatosAcademicos datosAcademicos) {
        facadeCE.setEntityClass(DatosAcademicos.class);
        em.merge(datosAcademicos);
        facadeCE.flush();
    }

    @Override
    public Iems buscaIemsByClave(Integer id) {
        return em.find(Iems.class,id);
    }

    @Override
    public AreasUniversidad buscaPEByClave(Short clave) {
        return em.createNamedQuery("AreasUniversidad.findByArea",AreasUniversidad.class)
                .setParameter("area",clave)
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
            em.persist(documentoAspirante);
        }else{
            documentoAspirante = aspirante.getDocumentoAspirante();
            if(tipoRequisito.equals("ActaNacimiento")){
                documentoAspirante.setEvidenciaActaNacimiento(rutaRelativa);
            }
            if(tipoRequisito.equals("HistorialAcademico")){
                documentoAspirante.setEvidenciaHistorialAcademico(rutaRelativa);
            }
            em.merge(documentoAspirante);
            facadeCE.flush();
        }

        
        return documentoAspirante;

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
        em.merge(documentoAspirante);
        facadeCE.flush();
    }
    
    @Override
    public void generaFichaAdmin(Persona persona, DatosAcademicos academicos,Domicilio domicilio,Aspirante aspirante,MedioComunicacion medioComunicacion, String uso) throws IOException, DocumentException{
        String ruta = "C://archivos//plantillas//formato_ficha_admision.pdf";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        
        Generos generos = new Generos();
        generos = em.find(Generos.class, persona.getGenero());
        Iems iems = new Iems();
        iems = em.find(Iems.class,academicos.getInstitucionAcademica());
        Asentamiento asentamiento = new Asentamiento();
        asentamiento = em.createQuery("SELECT a FROM Asentamiento a WHERE a.asentamientoPK.asentamiento = :idA AND a.asentamientoPK.municipio = :idMun AND a.asentamientoPK.estado = :idEst", Asentamiento.class)
                .setParameter("idA", domicilio.getIdAsentamiento())
                .setParameter("idMun", domicilio.getIdMunicipio())
                .setParameter("idEst", domicilio.getIdEstado())
                .getSingleResult();
        
        InputStream is = new FileInputStream(ruta);
        PdfReader pdfReader = new PdfReader(is,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
        
        BarcodeQRCode qrcode = new BarcodeQRCode(String.valueOf(aspirante.getFolioAspirante()), 60, 60, null);
        Image image = qrcode.getImage();
        image.setAbsolutePosition(465f, 92f);
        PdfContentByte  content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
        content.addImage(image);

        BarcodeQRCode qrcode2 = new BarcodeQRCode(String.valueOf(aspirante.getFolioAspirante()), 60, 60, null);
        Image image2 = qrcode2.getImage();
        image2.setAbsolutePosition(465f, 442f);
        content.addImage(image2);
        
        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("folio", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("fechaA", sm.format(aspirante.getFechaRegistro()));
        fields.setField("carreraA", ejbProcesoInscripcion.buscaAreaByClave(academicos.getPrimeraOpcion()).getNombre());
        fields.setField("apellidoPatA", persona.getApellidoPaterno());
        fields.setField("apellidoMatA", persona.getApellidoMaterno());
        fields.setField("nombreAlumnoA", persona.getNombre());
        fields.setField("fechaNacimientoA", sm.format(persona.getFechaNacimiento()));
        fields.setField("generoA",generos.getNombre());
        fields.setField("estadoCivilA", persona.getEstadoCivil());
        fields.setField("emailA", medioComunicacion.getEmail());
        fields.setField("iemsA",iems.getNombre());
        fields.setField("estadoIemsA", iems.getLocalidad().getMunicipio().getEstado().getNombre());
        fields.setField("municipioIemsA", iems.getLocalidad().getMunicipio().getNombre());
        fields.setField("localidadIemsA", iems.getLocalidad().getNombre());
        fields.setField("calleA", domicilio.getCalle());
        fields.setField("numeroA", domicilio.getNumero());
        fields.setField("coloniaA", asentamiento.getNombreAsentamiento());
        fields.setField("localidadA", asentamiento.getNombreAsentamiento());
        fields.setField("municipioA", asentamiento.getMunicipio1().getNombre());
        fields.setField("estadoA", asentamiento.getMunicipio1().getEstado().getNombre());
        fields.setField("movilA", medioComunicacion.getTelefonoMovil());
        fields.setField("fijoA", medioComunicacion.getTelefonoFijo());
        fields.setField("folioA", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("usuarioA", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("passA", persona.getCurp());
        fields.setField("turnoA", academicos.getSistemaPrimeraOpcion().getNombre());

        fields.setField("fechaC", sm.format(aspirante.getFechaRegistro()));
        fields.setField("carreraC",ejbProcesoInscripcion.buscaAreaByClave(academicos.getPrimeraOpcion()).getNombre());
        fields.setField("apellidoPatC", persona.getApellidoPaterno());
        fields.setField("apellidoMatC", persona.getApellidoMaterno());
        fields.setField("nombreAlumnoC", persona.getNombre());
        fields.setField("fechaNacimientoC", sm.format(persona.getFechaNacimiento()));
        fields.setField("generoC", generos.getNombre());
        fields.setField("estadoCivilC",  persona.getEstadoCivil());
        fields.setField("emailC", medioComunicacion.getEmail());
        fields.setField("iemsC", iems.getNombre());
        fields.setField("estadoIemsC", iems.getLocalidad().getMunicipio().getEstado().getNombre());
        fields.setField("municipioIemsC", iems.getLocalidad().getMunicipio().getNombre());
        fields.setField("localidadIemsC", iems.getLocalidad().getNombre());
        fields.setField("calleC", domicilio.getCalle());
        fields.setField("numeroC", domicilio.getNumero());
        fields.setField("coloniaC", asentamiento.getNombreAsentamiento());
        fields.setField("localidadC", asentamiento.getNombreAsentamiento());
        fields.setField("municipioC", asentamiento.getMunicipio1().getNombre());
        fields.setField("estadoC", asentamiento.getMunicipio1().getEstado().getNombre());
        fields.setField("movilC", medioComunicacion.getTelefonoMovil());
        fields.setField("fijoC", medioComunicacion.getTelefonoFijo());
        fields.setField("folioC", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("usuarioC", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("passC", persona.getCurp());
        fields.setField("turnoC", academicos.getSistemaSegundaOpcion().getNombre());
        
        pdfStamper.close();
        pdfStamper.close();
        
        Object response = facesContext.getExternalContext().getResponse();
        if (response instanceof HttpServletResponse) {
              HttpServletResponse hsr = (HttpServletResponse) response;
              hsr.setContentType("application/pdf");
              hsr.setHeader("Content-disposition", "attachment; filename=\""+aspirante.getFolioAspirante()+".pdf\"");
              hsr.setContentLength(baos.size());
              try {
                    ServletOutputStream out = hsr.getOutputStream();
                    baos.writeTo(out);
                    out.flush();
                    out.close();
              } catch (IOException ex) {
                    System.out.println("Error:  " + ex.getMessage());
              }
              facesContext.responseComplete();
        }
        
        if(uso.equals("Alumno")){
            // El correo gmail de envío
            String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";
            String claveCorreo = "DServiciosEscolares19";
            String mensaje = "Estimado(a) "+persona.getNombre()+"\n\n Gracias por elegir a la Universidad Tecnologica de Xicotepec de Juárez como opción para continuar con tus estudios de nivel superior." +
                            "\n\n Para continuar descarga la ficha la admisión y asiste a las instalaciones de la UTXJ y entregar la documentación necesaria\n\n"
                            + "* Formato de Ficha de Admisión.\n"
                            + "* Copia de CURP (nuevo formato).\n"
                            + "* Copia de Acta de Nacimiento.\n"
                            + "* Copia de Certificado de Nivel Medio Superior o Constancia de Estudios Original reciente con tira de materias y calificaciones (aprobatorias) del 1o. al 5o. semestre; indicando el promedio general y firmado por el titular de la Institución\n"
                            + "* Referencia bancaria del pago de ficha y examen de admisión.\n\n" +
                            "ATENTAMENTE \n" +
                            "Departamento de Servicios Escolares";

            String identificador = "Registro de Ficha de Admisión 2019 UTXJ";
            String asunto = "Registro Exitoso";
            if(medioComunicacion.getEmail() != null){
                try {
                    DataSource source = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
                    EnvioCorreos.EnviarCorreoArchivos(correoEnvia, claveCorreo,identificador,asunto,persona.getMedioComunicacion().getEmail(),mensaje,source,String.valueOf(aspirante.getFolioAspirante()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
