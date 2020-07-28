/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupo;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.util.Encrypted;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServiceProcesoInscripcion implements EjbProcesoInscripcion {

    @EJB FacadeCE facadeCE;

    @EJB   EjbEventoEscolar ejbEventoEscolar;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = facadeCE.getEntityManager();
    }

    @Override
    public List<Aspirante> listaAspirantesTSU(Integer procesoInscripcion) {
        return  em.createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 AND a.datosAcademicos <> null ORDER BY a.folioAspirante ",Aspirante.class)
                .setParameter("idpi", procesoInscripcion)
                .getResultList();
    }

    @Override
    public List<Aspirante> lisAspirantesByPE(Short pe,Integer procesoInscripcion) {
        return em.createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 AND a.datosAcademicos.primeraOpcion = :po AND a.datosAcademicos <> null ORDER BY a.folioAspirante ",Aspirante.class)
                .setParameter("idpi", procesoInscripcion)
                .setParameter("po", pe)
                .getResultList();
    }

    @Override
    public Aspirante buscaAspiranteByFolio(Integer folio) {
        return em.createNamedQuery("Aspirante.findByFolioAspirante", Aspirante.class)
                .setParameter("folioAspirante", folio)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    @Override
    public Aspirante buscaAspiranteByFolioValido(Integer folio) {
        return em.createQuery("SELECT a FROM Aspirante a WHERE a.folioAspirante = :folioAspirante AND a.estatus = true", Aspirante.class)
                .setParameter("folioAspirante", folio)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    @Override
    public AreasUniversidad buscaAreaByClave(Short area) {
        return em.createNamedQuery("AreasUniversidad.findByArea", AreasUniversidad.class)
                .setParameter("area", area)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public ResultadoEJB<Estudiante> saveEstudiante(@NonNull Estudiante estudiante, @NonNull Boolean opcionIn, @NonNull DtoGrupo grupo, @NonNull Documentosentregadosestudiante documentos, @NonNull Operacion operacion,@NonNull EventoEscolar eventoEscolar) {
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new Estudiante(),"El estudiante no debe ser nulo");}
            if(opcionIn==null){return ResultadoEJB.crearErroneo(3,new Estudiante(),"La opci+on de inscripcón no debe ser nulo");}
            if(grupo==null){return ResultadoEJB.crearErroneo(4,new Estudiante(),"El grupo no debe ser nulo");}
            if(documentos==null){return ResultadoEJB.crearErroneo(5,new Estudiante(),"Los documentos no deben ser nulos");}
            switch (operacion){
                case PERSISTIR:
                    if(grupo.getLleno()==false){
                        // System.out.println("No esta lleno");
                        int matricula = 0;
                        String folio = "";
                        TipoEstudiante tipoEstudiante = new TipoEstudiante((short)1, "Regular", true);
                        //Se crea el Login
                        Login login = new Login();
                        String contrasena="";
                        int generador;
                        for(int i=0;i<6;i++){
                            generador = (int)(Math.random()*10);
                            contrasena+= generador;
                        }
                        //Asignar Matricula
                        String anyo2 = new SimpleDateFormat("yy").format(new Date());
                        folio = anyo2.concat("0000");
                        TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(e.matricula) FROM Estudiante e WHERE e.periodo = :idPeriodo")
                                .setParameter("idPeriodo", eventoEscolar.getPeriodo());

                        if(v.getSingleResult() == 0){
                            matricula = Integer.valueOf(folio);
                        }else{
                            matricula = v.getSingleResult() + 1;
                        }
                        //Guarda estudiante
                        estudiante.setGrupo(grupo.getGrupo());
                        estudiante.setCarrera(grupo.getGrupo().getIdPe());
                        estudiante.setMatricula(matricula);
                        estudiante.setTipoEstudiante(tipoEstudiante);
                        estudiante.setOpcionIncripcion(opcionIn);
                        estudiante.setTipoRegistro("Inscripción");
                        em.persist(estudiante);
                        facadeCE.flush();
                        login.setActivo(true);
                        login.setModificado(false);
                        login.setUsuario(String.valueOf(matricula));
                        login.setPassword(encriptaPassword(contrasena));
                        login.setPersona(estudiante.getAspirante().getIdPersona().getIdpersona());
                        em.persist(login);
                        facadeCE.flush();
                        documentos.setEstudiante(estudiante.getIdEstudiante());
                        em.persist(documentos);
                        facadeCE.flush();
                        em.merge(estudiante.getAspirante().getDatosAcademicos());
                        facadeCE.flush();
                        em.merge(estudiante.getAspirante().getIdPersona().getDatosMedicos());
                        facadeCE.flush();
                        return ResultadoEJB.crearCorrecto(estudiante,"Estudiante inscrito con éxito");
                    }else {
                        return ResultadoEJB.crearErroneo(6,new Estudiante(),"El grupo seleccionado está lleno. Seleccione otro.");
                    }

                case ACTUALIZAR:
                    if (grupo.getLleno()==false){
                        TipoEstudiante tipoEstudiante = new TipoEstudiante((short)1, "Regular", true);
                        //Guarda estudiante
                        estudiante.setGrupo(grupo.getGrupo());
                        estudiante.setCarrera(grupo.getGrupo().getIdPe());
                        estudiante.setTipoEstudiante(tipoEstudiante);
                        estudiante.setOpcionIncripcion(opcionIn);
                        em.merge(estudiante);
                        facadeCE.flush();
                        documentos.setEstudiante(estudiante.getIdEstudiante());
                        if(documentos.getEstudiante() == null){
                            em.persist(documentos);
                        }else{
                            em.merge(documentos);
                        }
                        em.merge(estudiante.getAspirante().getDatosAcademicos());
                        facadeCE.flush();
                        em.merge(estudiante.getAspirante().getIdPersona().getDatosMedicos());
                        facadeCE.flush();
                        return ResultadoEJB.crearCorrecto(estudiante,"Estudiante actualizado");
                    }else {
                        return ResultadoEJB.crearErroneo(7,new Estudiante(),"El grupo seleccionado está lleno. Seleccione otro.");
                    }
            }
            return ResultadoEJB.crearCorrecto(estudiante,"Estudiante inscrito");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al inscribir al estudiante(EJBProcesoInscripcion.saveEstudiante).", e, null);

        }
    }

    @Override
    public ResultadoEJB<Documentosentregadosestudiante> getDocEstudiante(@NonNull Estudiante estudiante) {
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new Documentosentregadosestudiante(),"El estudiante no debe ser nulo");}
            Documentosentregadosestudiante documentos = new Documentosentregadosestudiante();
            documentos = em.createQuery("select d from Documentosentregadosestudiante  d where d.estudiante=:id",Documentosentregadosestudiante.class)
                    .setParameter("id",estudiante.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(documentos==null){return  ResultadoEJB.crearErroneo(3,documentos,"No se encontraron documentos del estudiante");}
            else {return ResultadoEJB.crearCorrecto(documentos,"Documentos encontrados");}
        }catch ( Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener los documentos del estudiante(EJBProcesoInscripcion.saveEstudiante).", e, null); }
    }

    @Override
    public Estudiante guardaEstudiante(Estudiante estudiante, Documentosentregadosestudiante documentosentregadosestudiante, Boolean opcionIns) {
        List<Grupo> grupos = new ArrayList<>();
        List<Grupo> gruposElegibles = new ArrayList<>();
        Grupo gps = new Grupo();
        String folio = "";
        int matriculaUtilizable = 0;
        TipoEstudiante tipoEstudiante = new TipoEstudiante((short)1, "Regular", true);
        Short cve_pe = 0;
        Short cve_sistema = 0;

        if(estudiante.getIdEstudiante() == null){
            try {
                Login login = new Login();
                String contrasena="";
                int generador;
                for(int i=0;i<6;i++){
                    generador = (int)(Math.random()*10);
                    contrasena+= generador;
                }

                if(opcionIns == true){
                    cve_pe = estudiante.getAspirante().getDatosAcademicos().getPrimeraOpcion();
                    cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaPrimeraOpcion().getIdSistema();
                }else{
                    cve_pe = estudiante.getAspirante().getDatosAcademicos().getSegundaOpcion();
                    cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaSegundaOpcion().getIdSistema();
                }
                grupos = listaGruposXPeriodoByCarrera((short) estudiante.getAspirante().getIdProcesoInscripcion().getIdPeriodo(), cve_pe,cve_sistema,1);
                grupos.forEach(g ->{
                    if(g.getEstudianteList().size() != g.getCapMaxima()){
                        gruposElegibles.add(g);
                    }
                });
                //Asignar Matricula
                String anyo2 = new SimpleDateFormat("yy").format(new Date());
                folio = anyo2.concat("0000");

                TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(e.matricula) FROM Estudiante e WHERE e.periodo = :idPeriodo")
                        .setParameter("idPeriodo", estudiante.getAspirante().getIdProcesoInscripcion().getIdPeriodo());

                if(v.getSingleResult() == 0){
                    matriculaUtilizable = Integer.valueOf(folio);
                }else{
                    matriculaUtilizable = v.getSingleResult() + 1;
                }
                //Elige grupos aleatorio
                int numero = (int) (Math.random() * gruposElegibles.size());
                gps = gruposElegibles.get(numero);
                //Guarda estudiante
                estudiante.setGrupo(gps);
                estudiante.setCarrera(gps.getIdPe());
                estudiante.setMatricula(matriculaUtilizable);
                estudiante.setTipoEstudiante(tipoEstudiante);
                estudiante.setOpcionIncripcion(opcionIns);
                login.setActivo(true);
                login.setModificado(false);
                login.setUsuario(String.valueOf(matriculaUtilizable));
                login.setPassword(encriptaPassword(contrasena));
                login.setPersona(estudiante.getAspirante().getIdPersona().getIdpersona());
                em.persist(login);
                em.persist(estudiante);
                facadeCE.flush();
                documentosentregadosestudiante.setEstudiante(estudiante.getIdEstudiante());
                em.persist(documentosentregadosestudiante);
                facadeCE.flush();
            } catch (Exception ex) {
                Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            if(estudiante.getOpcionIncripcion() != opcionIns){
                if(opcionIns == true){
                    cve_pe = estudiante.getAspirante().getDatosAcademicos().getPrimeraOpcion();
                    cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaPrimeraOpcion().getIdSistema();
                }else{
                    cve_pe = estudiante.getAspirante().getDatosAcademicos().getSegundaOpcion();
                    cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaSegundaOpcion().getIdSistema();
                }
                grupos = listaGruposXPeriodoByCarrera((short) estudiante.getAspirante().getIdProcesoInscripcion().getIdPeriodo(), cve_pe,cve_sistema,1);

                grupos.forEach(g ->{
                    if(g.getEstudianteList().size() != g.getCapMaxima()){
                        gruposElegibles.add(g);
                    }
                });

                //Elige grupos aleatorio
                int numero = (int) (Math.random() * gruposElegibles.size());
                gps = gruposElegibles.get(numero);
                //Guarda estudiante
                estudiante.setGrupo(gps);
                estudiante.setCarrera(gps.getIdPe());
                estudiante.setTipoEstudiante(tipoEstudiante);
                estudiante.setOpcionIncripcion(opcionIns);
                em.merge(estudiante);
                facadeCE.flush();
                documentosentregadosestudiante.setEstudiante(estudiante.getIdEstudiante());
                if(documentosentregadosestudiante.getEstudiante() == null){
                    em.persist(documentosentregadosestudiante);
                }else{
                    em.merge(documentosentregadosestudiante);
                }
            }else{
                estudiante.setCarrera(gps.getIdPe());
                estudiante.setOpcionIncripcion(opcionIns);
                estudiante.setCarrera(estudiante.getGrupo().getIdPe());
                em.merge(estudiante);
                facadeCE.flush();
                documentosentregadosestudiante.setEstudiante(estudiante.getIdEstudiante());
                if(documentosentregadosestudiante.getEstudiante() == null){
                    em.persist(documentosentregadosestudiante);
                }else{
                    em.merge(documentosentregadosestudiante);
                }
            }
        }

        return estudiante;
    }

    @Override
    public Estudiante findByIdAspirante(Integer idAspirante) {
        return em.createQuery("SELECT e FROM Estudiante e WHERE e.aspirante.idAspirante = :idAspirante", Estudiante.class)
                .setParameter("idAspirante", idAspirante)
                .getResultList().stream().findFirst().orElse(null);

    }

    @Override
    public void generaComprobanteInscripcion(Estudiante estudiante) {
        try {
            String ruta = "C://archivos//plantillas//comprobanteInscripcion_nuevo.pdf";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
            Documentosentregadosestudiante documentosentregadosestudiante = new Documentosentregadosestudiante();
            Login login = new Login();
            documentosentregadosestudiante = em.find(Documentosentregadosestudiante.class, estudiante.getIdEstudiante());
            login = em.createQuery("SELECT l FROM Login l WHERE l.persona = :idPer", Login.class)
                    .setParameter("idPer", estudiante.getAspirante().getIdPersona().getIdpersona())
                    .getResultList().stream().findFirst().orElse(null);

            InputStream is = new FileInputStream(ruta);
            PdfReader pdfReader = new PdfReader(is,null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);

            BarcodePDF417 pdfcodigo = new BarcodePDF417();
            pdfcodigo.setText(String.valueOf(estudiante.getMatricula()));
            Image img = pdfcodigo.getImage();
            img.setAbsolutePosition(300f, 77f);
            img.scalePercent(200, 100 * pdfcodigo.getYHeight());
            PdfContentByte  content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
            content.addImage(img);

            AcroFields fields = pdfStamper.getAcroFields();
            AreasUniversidad areasUniversidad = new AreasUniversidad();
            Grupo grupo = new Grupo();
            grupo = estudiante.getGrupo();
            areasUniversidad = buscaAreaByClave((short) estudiante.getCarrera());
            String nombreCarrera = areasUniversidad.getNombre();
            String siglas = areasUniversidad.getSiglas();

            fields.setField("txtAP", estudiante.getAspirante().getIdPersona().getApellidoPaterno());
            fields.setField("txtAM", estudiante.getAspirante().getIdPersona().getApellidoMaterno());
            fields.setField("txtNombre", estudiante.getAspirante().getIdPersona().getNombre());
            fields.setField("txtCarrera", nombreCarrera);
            fields.setField("txtMatricula", String.valueOf(estudiante.getMatricula()));
            fields.setField("txtGrupo", String.valueOf(estudiante.getGrupo().getGrado()).concat("-").concat(String.valueOf(estudiante.getGrupo().getLiteral())));
            fields.setField("txtTurno", grupo.getIdSistema().getNombre());
            fields.setField("txtPassword", desencriptaPassword(login.getPassword()));
            fields.setField("txtCAbreviatura", siglas);
            fields.setField("txtFicha", String.valueOf(estudiante.getAspirante().getFolioAspirante()));
            fields.setField("txtNombreEstudiante", estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ").concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno().concat(" ").concat(estudiante.getAspirante().getIdPersona().getNombre())));
            fields.setField("txtValAN", documentosentregadosestudiante.getActaNacimiento() == true ? "Entregado" : "No Entregado");
            fields.setField("txtValCAN", documentosentregadosestudiante.getCopiaActaNacimiento() == true ? "Entregado" : "No Entregado");
            fields.setField("txtValCert", documentosentregadosestudiante.getCertificadoIems() == true ? "Entregado" : "No Entregado");
            fields.setField("txtValCCert", documentosentregadosestudiante.getCopiaCertificadoIems() == true ? "Entregado" : "No Entregado");
            fields.setField("txtValHist", documentosentregadosestudiante.getHistorial() == true ? "Entregado" : "No Entregado");
            fields.setField("txtValCURP", documentosentregadosestudiante.getCurp()== true ? "Entregado" : "No Entregado");
            fields.setField("txtValPC", documentosentregadosestudiante.getPagoColegiatura()== true ? "Entregado" : "No Entregado");
            fields.setField("txtValTS", documentosentregadosestudiante.getTipoSangre() == true ? "Entregado" : "No Entregado");

            pdfStamper.close();

            Object response = facesContext.getExternalContext().getResponse();
            if (response instanceof HttpServletResponse) {
                HttpServletResponse hsr = (HttpServletResponse) response;
                hsr.setContentType("application/pdf");
                hsr.setHeader("Content-disposition", "inline; filename=\""+ estudiante.getMatricula()+".pdf\"");
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

        } catch (BadElementException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void generaCartaCompromiso(Estudiante estudiante) {
        try {
            String ruta = "C://archivos//plantillas//cartaCompromiso.pdf";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            AreasUniversidad areasUniversidad = new AreasUniversidad();
            areasUniversidad = buscaAreaByClave((short) estudiante.getCarrera());
            String nombreCarrera = areasUniversidad.getNombre();

            InputStream is = new FileInputStream(ruta);
            PdfReader pdfReader = new PdfReader(is,null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);

            AcroFields fields = pdfStamper.getAcroFields();
            fields.setField("txtNombreCC", estudiante.getAspirante().getIdPersona().getApellidoPaterno()+" "+ estudiante.getAspirante().getIdPersona().getApellidoMaterno()+" "+ estudiante.getAspirante().getIdPersona().getNombre());
            fields.setField("txtMatriculaCC", String.valueOf(estudiante.getMatricula()));
            fields.setField("txtCarreraCC", nombreCarrera);

            if(estudiante.getDocumentosentregadosestudiante().getActaNacimiento() == false){
                fields.setField("txtActaCC", "Acta de Nacimiento (Original)");
            }

            if(estudiante.getDocumentosentregadosestudiante().getCertificadoIems() == false){
                fields.setField("txtCertCC", "Certificado de Bachillerato (Original)");
            }

            pdfStamper.close();

            Object response = facesContext.getExternalContext().getResponse();
            if (response instanceof HttpServletResponse) {
                HttpServletResponse hsr = (HttpServletResponse) response;
                hsr.setContentType("application/pdf");
                hsr.setHeader("Content-disposition", "inline; filename=\""+ estudiante.getMatricula()+".pdf\"");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Grupo> listaGruposXPeriodoByCarrera(Short periodo, Short carrera, Short sistema, Integer grado) {
        return em.createQuery("SELECT g FROM Grupo g WHERE g.grado = :grado AND g.idPe = :cvePe AND g.idSistema.idSistema = :idSistema AND g.periodo = :idPeriodo", Grupo.class)
                .setParameter("grado", 1)
                .setParameter("cvePe", carrera)
                .setParameter("idSistema", sistema)
                .setParameter("idPeriodo", periodo)
                .getResultList();
    }

    @Override
    public List<Estudiante> listaEstudiantesXPeriodo(Integer perido) {
        return em.createQuery("SELECT e FROM Estudiante e WHERE e.periodo = :idPeriodo", Estudiante.class)
                .setParameter("idPeriodo", perido)
                .getResultList();
    }

    public static String encriptaPassword(String password) throws Exception{
        String contraseñaEncriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaEncriptada = Encrypted.encrypt(key, iv, password);

        return contraseñaEncriptada;
    }

    public static String desencriptaPassword(String password) throws Exception{
        String contraseñaDesencriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaDesencriptada = Encrypted.decrypt(key, iv, password);

        return contraseñaDesencriptada;
    }

    @Override
    public void actualizaEstudiante(Estudiante estudiante) {
        facadeCE.setEntityClass(Estudiante.class);
        em.merge(estudiante);
        facadeCE.flush();
    }

    @Override
    public Iems buscaIemsByClave(Integer id) {
        return em.createQuery("SELECT i FROM Iems i WHERE i.iems = :idIems", Iems.class)
                .setParameter("idIems", id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public ResultadoEJB<EventoEscolar> verificarEventoIncipcion() {
        try{
            //Ultimo proceso de inscripcion
            /*EventoEscolar eventoEscolar = em.createQuery("select e from EventoEscolar  e where  e.evento=7",EventoEscolar.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            return ResultadoEJB.crearCorrecto(eventoEscolar,"Evento");

            */
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.INSCRIPCIONES);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar de inscripciones (EJBProcesoInscripcion.).", e, EventoEscolar.class);
        }

    }

    @Override
    public ResultadoEJB<EventoEscolar> verificarEventoRegistroFichas() {
        try{
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REGISTRO_FICHAS_ADMISION);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar de inscripciones (EJBProcesoInscripcion.).", e, EventoEscolar.class);
        }
    }

    /**
     * Busca los grupos por programa educativo y sistema
     * @param eventoEscolar Evento escolar activo para inscripcion
     * @param pe Programa educativo
     * @param sistema Sistema escolar
     * @return Resultado del proceso
     */

    @Override
    public ResultadoEJB<List<Grupo>> getGruposbyPe(@NonNull EventoEscolar eventoEscolar, @NonNull AreasUniversidad pe, @NonNull Sistema sistema) {
        try{
            List<Grupo> grupos = new ArrayList<>();
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2,grupos,"El evento no debe ser nulo");}
            if(pe==null){ return  ResultadoEJB.crearErroneo(3,grupos,"El programa educativo no debe ser nulo");}
            if(sistema==null){ return ResultadoEJB.crearErroneo(4,grupos,"El sistema no debe ser nulo");}
            grupos = em.createQuery("select g from Grupo g where g.periodo=:periodo and g.idPe=:pe and g.idSistema.idSistema=:sistema", Grupo.class)
                    .setParameter("periodo", eventoEscolar.getPeriodo())
                    .setParameter("pe",pe.getArea())
                    .setParameter("sistema",sistema.getIdSistema())
                    .getResultList()
            ;
            if(grupos==null || grupos.isEmpty()){ return ResultadoEJB.crearErroneo(5,grupos,"No existen grupos creados para el programa educativo");}
            else { return  ResultadoEJB.crearCorrecto(grupos,"Lista de grupos");}
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al obtener los grupos por programa educativo(EJBProcesoInscripcion.getGruposbyPe).", e, null);

        }
    }

    /**
     * Obtiene estudiantes regulares
     * @param grupo Grupo
     * @return Resultado del proceso
     */
    @Override
    public ResultadoEJB<List<Estudiante>> getEstudiantesbyGrupo(@NonNull Grupo grupo) {
        try{
            List<Estudiante> estudiantes= new ArrayList<>();
            if (grupo == null) { return ResultadoEJB.crearErroneo(2,estudiantes,"El grupo dno debe ser nulo");}
            estudiantes = em.createQuery("select e from Estudiante  e where e.grupo.idGrupo=:grupo and e.tipoEstudiante.idTipoEstudiante=1",Estudiante.class)
                    .setParameter("grupo",grupo.getIdGrupo())
                    .getResultList()
            ;
            if(estudiantes==null || estudiantes.isEmpty()){return ResultadoEJB.crearErroneo(3, estudiantes,"No existen estudiantes en ese grupo");}
            else { return ResultadoEJB.crearCorrecto(estudiantes,"Exiten estudiantes en el grupo"); }
        }catch (Exception e ){return  ResultadoEJB.crearErroneo(1, "Error al obtener los estudiantes por grupo(EJBProcesoInscripcion.getGruposbyPe).", e, null); }

    }

    /**
     * Empaqueta al grupo
     * @param grupo Grupo
     * @return Resultado del  proceso
     */
    @Override
    public ResultadoEJB<DtoGrupo> packGrupo(@NonNull Grupo grupo) {
        try{
            DtoGrupo dtoGrupo = new DtoGrupo();
            if(grupo==null){ return ResultadoEJB.crearErroneo(2,dtoGrupo,"El grupo no debe ser nulo");}
            dtoGrupo.setGrupo(grupo);
            // Obtiene el programa educativo del grupo
            AreasUniversidad pe =buscaAreaByClave(grupo.getIdPe());
            //Obtiene al tutor del grupo
            if(grupo.getTutor()!=null){
                ResultadoEJB<Personal> resTutor = getTutor(grupo.getTutor());
                if(resTutor.getCorrecto()){
                    dtoGrupo.setNombreTutor(resTutor.getValor().getNombre());
                    dtoGrupo.setTutor(resTutor.getValor());
                }else {return ResultadoEJB.crearErroneo(4,dtoGrupo,"Error al obtener al tutor del grupo");}
            }else {dtoGrupo.setNombreTutor("Sin asignar"); dtoGrupo.setTutor(new Personal());}
            //Obtiene la lista de estudiantes en ese grupo
            dtoGrupo.setCapMax(grupo.getCapMaxima());
            ResultadoEJB<List<Estudiante>> resEstudiantes= getEstudiantesbyGrupo(grupo);
            if(resEstudiantes.getCorrecto()==true){
                dtoGrupo.setEstudiantes(resEstudiantes.getValor());
                dtoGrupo.setTotalGrupo(resEstudiantes.getValor().size());
                dtoGrupo.setCapDispo(grupo.getCapMaxima()-dtoGrupo.getTotalGrupo());
            }else{
                //No hay estudiantes en ese grupo
                dtoGrupo.setEstudiantes(new ArrayList<>());
                dtoGrupo.setTotalGrupo(resEstudiantes.getValor().size());
                dtoGrupo.setCapDispo(grupo.getCapMaxima()- dtoGrupo.getTotalGrupo());
            }
            //Comprueba si el grupo ya está lleno
            if(dtoGrupo.getTotalGrupo()==grupo.getCapMaxima()){ dtoGrupo.setLleno(true);
            }else {dtoGrupo.setLleno(false);}
            return ResultadoEJB.crearCorrecto(dtoGrupo,"Grupo empaquetado");
        }catch (Exception e){ return  ResultadoEJB.crearErroneo(1, "Error al empaquetar al grupo(EJBProcesoInscripcion.packGrupo).", e, null); }

    }

    /**
     * Obtiene los possibes grupos por programa educativo y los empaqueta
     * @param eventoEscolar evento activo para inscripción
     * @param aspirante Aspirante validado
     * @param pe Programa educativo
     * @param datosAcademicos datos académicos del aspirante
     * @return Resultado del proceso (Lista de posibles grupos )
     */
    @Override
    public ResultadoEJB<List<DtoGrupo>> getGruposbyOpcion(@NonNull EventoEscolar eventoEscolar,@NonNull Aspirante aspirante,@NonNull AreasUniversidad pe,@NonNull DatosAcademicos datosAcademicos) {
        try{
            List<DtoGrupo> grupos= new ArrayList<>();
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2,grupos,"El evento no debe ser nulo");}
            if(aspirante==null){return ResultadoEJB.crearErroneo(3,grupos,"El aspirante no debe ser nulo");}
            if(pe==null){return ResultadoEJB.crearErroneo(4,grupos,"Los datos académicos no deben ser nulos"); }
            //Se obtienen grupos de la primera opción
            ResultadoEJB<List<Grupo>> resGruposPO = getGruposbyPe(eventoEscolar,pe,datosAcademicos.getSistemaPrimeraOpcion());
            if(resGruposPO.getCorrecto()==true){
                //Se recorren los grupos para poder empaquetarlos
                resGruposPO.getValor().forEach(g->{
                    //Se empaquetan los grupos
                    ResultadoEJB<DtoGrupo> resPack= packGrupo(g);
                    if(resPack.getCorrecto()==true){
                        grupos.add(resPack.getValor());
                    }
                });
                return ResultadoEJB.crearCorrecto(grupos,"Opcion de grupos");
            }else {return ResultadoEJB.crearErroneo(5,grupos,"No existen grupos creados del programa "+ pe.getNombre());}
        }catch (Exception e){return  ResultadoEJB.crearErroneo(1, "Error al obtener los posibles grupos(EJBProcesoInscripcion.getGruposbyPe).", e, null);}
    }

    /**
     * Obtiene al tutor del grupo
     * @param clave Clave del personal
     * @return Resultado del proceso
     */
    @Override
    public ResultadoEJB<Personal> getTutor(@NonNull Integer clave) {
        try{
            if(clave==null){ return ResultadoEJB.crearErroneo(2,new Personal(),"La clave del tutor no debe ser nula");}
            Personal tutor = new Personal();
            tutor =em.createQuery("select p from Personal p where p.clave=:clave",Personal.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(tutor ==null){ return ResultadoEJB.crearErroneo(3,tutor,"No tiene tutor asignado");}
            else {return ResultadoEJB.crearCorrecto(tutor,"Tutor de grupo");}
        }catch (Exception e){return  ResultadoEJB.crearErroneo(1, "Error al obtener al tutor de grupo(EJBProcesoInscripcion.getTutor).", e, null);}
    }





}
