/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServiceProcesoInscripcion implements EjbProcesoInscripcion {

    @EJB
    FacadeCE facadeCE;

    @Override
    public List<Aspirante> listaAspirantesTSU(Integer procesoInscripcion) {
        return  facadeCE.getEntityManager().createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 AND a.datosAcademicos <> null ORDER BY a.folioAspirante ",Aspirante.class)
                .setParameter("idpi", procesoInscripcion)
                .getResultList();
    }

    @Override
    public List<Aspirante> lisAspirantesByPE(Short pe,Integer procesoInscripcion) {
        return facadeCE.getEntityManager().createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 AND a.datosAcademicos.primeraOpcion = :po AND a.datosAcademicos <> null ORDER BY a.folioAspirante ",Aspirante.class)
                .setParameter("idpi", procesoInscripcion)
                .setParameter("po", pe)
                .getResultList();
    }

    @Override
    public Aspirante buscaAspiranteByFolio(Integer folio) {
        return facadeCE.getEntityManager().createNamedQuery("Aspirante.findByFolioAspirante", Aspirante.class)
                .setParameter("folioAspirante", folio)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    @Override
    public Aspirante buscaAspiranteByFolioValido(Integer folio) {
        return facadeCE.getEntityManager().createQuery("SELECT a FROM Aspirante a WHERE a.folioAspirante = :folioAspirante AND a.estatus = true", Aspirante.class)
                .setParameter("folioAspirante", folio)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    @Override
    public AreasUniversidad buscaAreaByClave(Short area) {
        return facadeCE.getEntityManager().createNamedQuery("AreasUniversidad.findByArea", AreasUniversidad.class)
                .setParameter("area", area)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public Estudiante guardaEstudiante(Estudiante estudiante,Documentosentregadosestudiante documentosentregadosestudiante,Boolean opcionIns) {
        List<Grupo> grupos = new ArrayList<>();
        List<Grupo> gruposElegibles = new ArrayList<>();
        Grupo gps = new Grupo();
        String folio = "";
        int matriculaUtilizable = 0;
        TipoEstudiante tipoEstudiante = new TipoEstudiante((short)1, "Regular", true);
        Short cve_pe = 0;
        Short cve_sistema = 0;
        
        if(estudiante.getIdEstudiante() == null){
            if(opcionIns == true){
                cve_pe = estudiante.getAspirante().getDatosAcademicos().getPrimeraOpcion();
                cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaPrimeraOpcion().getIdSistema();
            }else{
                cve_pe = estudiante.getAspirante().getDatosAcademicos().getSegundaOpcion();
                cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaSegundaOpcion().getIdSistema();
            }
            grupos = facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.grado = :grado AND g.idPe = :cvePe AND g.idSistema.idSistema = :idSistema AND g.periodo = :idPeriodo", Grupo.class)
                    .setParameter("grado", 1)
                    .setParameter("cvePe", cve_pe)
                    .setParameter("idSistema", cve_sistema)
                    .setParameter("idPeriodo", 50)
                    .getResultList();
                grupos.forEach(g ->{
                    if(g.getEstudianteList().size() != g.getCapMaxima()){
                        gruposElegibles.add(g);
                    }
                });
            //Asignar Matricula
            String anyo2 = new SimpleDateFormat("yy").format(new Date());
            folio = anyo2.concat("0000");

            TypedQuery<Integer> v = (TypedQuery<Integer>) facadeCE.getEntityManager().createQuery("SELECT MAX(e.matricula) FROM Estudiante e WHERE e.periodo = :idPeriodo")
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
            facadeCE.create(estudiante);
            facadeCE.flush();
            documentosentregadosestudiante.setEstudiante(estudiante.getIdEstudiante());
            facadeCE.create(documentosentregadosestudiante); 
        }else{
            if(estudiante.getOpcionIncripcion() != opcionIns){
                if(opcionIns == true){
                    cve_pe = estudiante.getAspirante().getDatosAcademicos().getPrimeraOpcion();
                    cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaPrimeraOpcion().getIdSistema();
                }else{
                    cve_pe = estudiante.getAspirante().getDatosAcademicos().getSegundaOpcion();
                    cve_sistema = estudiante.getAspirante().getDatosAcademicos().getSistemaSegundaOpcion().getIdSistema();
                }
                grupos = facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.grado = :grado AND g.idPe = :cvePe AND g.idSistema.idSistema = :idSistema AND g.periodo = :idPeriodo", Grupo.class)
                    .setParameter("grado", 1)
                    .setParameter("cvePe", cve_pe)
                    .setParameter("idSistema", cve_sistema)
                    .setParameter("idPeriodo", 50)
                    .getResultList();
               
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
                estudiante.setMatricula(matriculaUtilizable);
                estudiante.setTipoEstudiante(tipoEstudiante);
                estudiante.setOpcionIncripcion(opcionIns);
                facadeCE.edit(estudiante);
                facadeCE.flush();
                documentosentregadosestudiante.setEstudiante(estudiante.getIdEstudiante());
                if(documentosentregadosestudiante.getEstudiante() == null){
                    facadeCE.create(documentosentregadosestudiante);
                }else{
                    facadeCE.edit(documentosentregadosestudiante);
                } 
            }else{
                estudiante.setCarrera(gps.getIdPe());
                estudiante.setOpcionIncripcion(opcionIns);
                estudiante.setCarrera(estudiante.getGrupo().getIdPe());
                facadeCE.edit(estudiante);
                facadeCE.flush();
                documentosentregadosestudiante.setEstudiante(estudiante.getIdEstudiante());
                if(documentosentregadosestudiante.getEstudiante() == null){
                    facadeCE.create(documentosentregadosestudiante);
                }else{
                    facadeCE.edit(documentosentregadosestudiante);
                }
            }
        }
        
        return estudiante;
    }

    @Override
    public Estudiante findByIdAspirante(Integer idAspirante) {
        return facadeCE.getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.aspirante.idAspirante = :idAspirante", Estudiante.class)
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
            documentosentregadosestudiante = facadeCE.getEntityManager().find(Documentosentregadosestudiante.class, estudiante.getIdEstudiante());
                    
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
            areasUniversidad = buscaAreaByClave((short)estudiante.getCarrera());
            String nombreCarrera = areasUniversidad.getNombre();
            String siglas = areasUniversidad.getSiglas();
            fields.setField("txtAP", estudiante.getAspirante().getIdPersona().getApellidoPaterno());
            fields.setField("txtAM", estudiante.getAspirante().getIdPersona().getApellidoMaterno());
            fields.setField("txtNombre", estudiante.getAspirante().getIdPersona().getNombre());
            fields.setField("txtCarrera", nombreCarrera);
            fields.setField("txtMatricula", String.valueOf(estudiante.getMatricula()));
            fields.setField("txtGrupo", String.valueOf(estudiante.getGrupo().getGrado()).concat("-").concat(String.valueOf(estudiante.getGrupo().getLiteral())));
            fields.setField("txtTurno", grupo.getIdSistema().getNombre());
            fields.setField("txtPassword", "12345");
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
                hsr.setHeader("Content-disposition", "inline; filename=\""+estudiante.getMatricula()+".pdf\"");
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
        }
    }
}
