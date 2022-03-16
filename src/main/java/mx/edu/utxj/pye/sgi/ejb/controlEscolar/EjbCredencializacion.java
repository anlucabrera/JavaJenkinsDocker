package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Registro;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import mx.edu.utxj.pye.sgi.dto.controlEscolar.CredencializacionRolServiciosE;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;

@Stateless (name = "EjbCredencializacion")
public class EjbCredencializacion {

    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;

    private EntityManager em;

    @PostConstruct
    public void init(){em = f.getEntityManager();}

    /**
     * Verifica que el personal logueado sea de Servicios Escolares
     * @param clave Clave del trabajador logueado
     * @return Resultado del Proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaSE(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbCredencializacion.validaSE)", e, null);
        }
    
    }

    /**
     * Busca un estudiante por matricula
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso (Estudiante)
     */

    public ResultadoEJB<Estudiante> getEstudiantebyMatricula(String matricula){
        try{
            Estudiante estudiante = new Estudiante();
            //Comprobar que la matricula no venga nula
            if(matricula ==null){return  ResultadoEJB.crearErroneo(2, estudiante,"La matricula no debe ser nula");}
            else{
                //Se hace la consulta
                estudiante = em.createQuery("select e from  Estudiante e where e.matricula=:matricula order by e.periodo desc ",Estudiante.class)
                .setParameter("matricula",Integer.parseInt(matricula))
                .getResultStream()
                .findAny()
                .orElse(null);
                if (estudiante!=null){return ResultadoEJB.crearCorrecto(estudiante,"Estudiante localizado con éxito");
                }else {return ResultadoEJB.crearErroneo(3,estudiante,"No se ha encontrado al estudiante");}
            }
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al buscar al estudiante (EjbCredencializacion.getEstudiantebyMatricula)", e, null); }
    }
    /**
     * Obtiene la carrera del estudiante por clave del area
     * @param rol
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreabyClave (CredencializacionRolServiciosE rol){
        try{
            AreasUniversidad area = new AreasUniversidad();
            if(rol==null){return ResultadoEJB.crearErroneo(2,area, "El estudiante no debe ser nulo");}
            else{
                //TODO: Se hace la consulta
                area = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area=:area",AreasUniversidad.class)
                        .setParameter("area",rol.getEstudiante().getGrupo().getIdPe())
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                if(area!=null){
                    return ResultadoEJB.crearCorrecto(area, "Se encontro el area del estudiante");
                }
                else{return ResultadoEJB.crearErroneo(3,area, "No se encontró el aréa");}
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al buscar el area del estudianate (EjbCredencializacion.getAreabyClave)", e, null);
        }
    }

    /**
     * Busca si existe registro de pago de credencial por matricula
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso(Resgitro)
     */

    public ResultadoEJB<Registro> getPagoCredencialbyMatricula(String matricula){
        try{
            //TODO:Comrpueba que la matricula no sea nulo
            Registro registro = new Registro();
            if(matricula==null){return ResultadoEJB.crearErroneo(3,registro,"La matricula no debe ser nula");}
            else {
                //TODO: Consulta en la base de finanzas si existe un pago de credencial
                registro = em.createQuery("select r from Registro r where r.alumnoFinanzas.alumnoFinanzasPK.matricula=:matricula and r.pago.concepto=:concepto order by r.fechaPago desc", Registro.class)
                        .setParameter("matricula", matricula)
                        .setParameter("concepto",65)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                ;
                if(registro!= null){return ResultadoEJB.crearCorrecto(registro,"Se ha encontrado registro de pago de credencial");
                }else {return ResultadoEJB.crearErroneo(3,registro,"No existe registro de pago de credencial");}
            }
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al buscar al pago de credencial (EjbCredencializacion.getPagoCredencialbyMatricula)", e, null);}
    }
    
    public ResultadoEJB getSituacionAcademicaEstudiante(String matricula){
        try{
            Estudiante estudiante = new Estudiante();
            //Comprobar que la matricula no venga nula
            if(matricula ==null){return  ResultadoEJB.crearErroneo(1, estudiante,"La matricula no debe ser nula");}
            else{
                //Se hace la consulta
                estudiante = em.createQuery("select e from  Estudiante e where e.matricula=:matricula order by e.periodo desc ",Estudiante.class)
                .setParameter("matricula",Integer.parseInt(matricula))
                .getResultStream()
                .findAny()
                .orElse(null);
                switch(estudiante.getTipoEstudiante().getDescripcion()){
                    case "Regular":
                        return ResultadoEJB.crearCorrecto(estudiante,"Situacion Academica: Regular");
                    case "Baja Temporal":
                        return ResultadoEJB.crearErroneo(1,estudiante,"Situacion Academica: Con baja temporal");
                    case "Baja Definitiva":
                        return ResultadoEJB.crearErroneo(1,estudiante,"Situacion Academica: Con baja definitiva");
                    case "Egresado No Titulado":
                        return ResultadoEJB.crearErroneo(1,estudiante,"Situacion Academica: Egresado no titulado");
                    case "Reincorporacion TSU":
                        return ResultadoEJB.crearCorrecto(estudiante,"Situacion Academica: Reincorporacion a TSU");
                    case "Reincorporacion ING":
                        return ResultadoEJB.crearCorrecto(estudiante,"Situacion Academica: Reincorporacion a ING");
                    case "Invalido por repetición de cuatrimestre":
                        return ResultadoEJB.crearErroneo(1,estudiante,"Situacion Academica: Invalido por repetición de cuatrimestre");
                    default:
                        return ResultadoEJB.crearErroneo(1,estudiante,"No se ha encontrado al estudiante");
                }
            }
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al buscar al estudiante (EjbCredencializacion.getEstudiantebyMatricula)", e, null); }
    }
    public ResultadoEJB getFotoAlumno(CredencializacionRolServiciosE rol){
        try{
            Image imageEstudiante = Image.getInstance("C://archivos//control_escolar//fotos//" + rol.getMatricula() + ".jpg");
            //Comprobar que la matricula no venga nula
            if (imageEstudiante != null) {
                return ResultadoEJB.crearCorrecto(imageEstudiante, "Estudiante localizado con éxito");
            } else {
                return ResultadoEJB.crearErroneo(3, imageEstudiante, "No se ha encontrado la foto");
            }
        }catch (BadElementException | IOException e){ return ResultadoEJB.crearErroneo(3, "No se ha encontrado la foto (EjbCredencializacion.getEstudiantebyMatricula)", e, null); }
    }
    
    public ResultadoEJB getFirmaAlumno(CredencializacionRolServiciosE rol){
        try{
            Image imageFirma = Image.getInstance("C://archivos//control_escolar//firmas//" + rol.getMatricula() + ".png");

            //Comprobar que la matricula no venga nula
            if (imageFirma != null) {
                return ResultadoEJB.crearCorrecto(imageFirma, "Firma localizado con éxito");
            } else {
                return ResultadoEJB.crearErroneo(3, imageFirma, "No se ha encontrado la firma");
            }

        }catch (BadElementException | IOException e){ return ResultadoEJB.crearErroneo(3, "No se ha encontrado la firma (EjbCredencializacion.getEstudiantebyMatricula)", e, null); }
    }
    
    /**
     * Identifica mensajes de alerta, en este caso sólo para identificar si el estudiante ha pagado la credencial
     * @param rol
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAlerta>> identificaMensajes(CredencializacionRolServiciosE rol){
        try {
            if(rol.getEstudiante()!=null){
                List<DtoAlerta> alertas = new ArrayList<>();
                String  matricula = String.valueOf(rol.getEstudiante().getMatricula());
                ResultadoEJB<Registro> resPago = getPagoCredencialbyMatricula(matricula);
                ResultadoEJB resFoto = getFotoAlumno(rol);
                ResultadoEJB resFirma = getFirmaAlumno(rol);
                ResultadoEJB resAcademica = getSituacionAcademicaEstudiante(matricula);
                
                if(resAcademica.getCorrecto()==true){
                    alertas.add(new DtoAlerta(resAcademica.getMensaje(),AlertaTipo.CORRECTO));
                }else{
                    alertas.add(new DtoAlerta(resAcademica.getMensaje(),AlertaTipo.SUGERENCIA));
                }
                
                if (resPago.getCorrecto() == true && resFoto.getCorrecto() == true && resFirma.getCorrecto() == true) {
                    alertas.add(new DtoAlerta("El estudiante con matricula " + matricula + " ha realizado el pago de credencial", AlertaTipo.CORRECTO));
                    alertas.add(new DtoAlerta("Corroborar que la firma y foto correspondan con el estudiante", AlertaTipo.SUGERENCIA));
                } else {
                    if (resPago.getCorrecto() == true) {
                        alertas.add(new DtoAlerta("El estudiante con matricula " + matricula + " ha realizado el pago de credencial", AlertaTipo.CORRECTO));
                    } else {
                        alertas.add(new DtoAlerta("El estudiante con matricula " + matricula + " no ha realizado el pago de credencial", AlertaTipo.SUGERENCIA));
                    }

                    if (resFoto.getCorrecto() == true) {
                        if (resFirma.getCorrecto() == true) {
                        } else {
                            alertas.add(new DtoAlerta("No se ha encontrado la firma del estudiante en el sistema", AlertaTipo.SUGERENCIA));
                        }
                    } else {
                        if (resFirma.getCorrecto() == true) {
                            alertas.add(new DtoAlerta("No se ha encontrado la foto del estudiante en el sistema", AlertaTipo.SUGERENCIA));
                        } else {
                            alertas.add(new DtoAlerta("No se ha encontrado la foto del estudiante en el sistema", AlertaTipo.SUGERENCIA));
                            alertas.add(new DtoAlerta("No se ha encontrado la firma del estudiante en el sistema", AlertaTipo.SUGERENCIA));
                        }
                    }
                }
                return ResultadoEJB.crearCorrecto(alertas, "Lista de mensajes");
            }
            return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Sin mensajes");
            
        } catch (Exception e) {return ResultadoEJB.crearErroneo(1, "No se pudieron identificar los mensajes(EjbCredencializacion.identificaMensajes) === Error :", e, null);}
    }

    /**
     * Genera la credencial del estudiante
     * @param rol
     */
    public void generaCredencial (CredencializacionRolServiciosE estudiante) {
        try {
            if(estudiante!=null){
                String ruta = "C://archivos//plantillas//credencial_estudiante.pdf";
                FacesContext facesContext = FacesContext.getCurrentInstance();
                SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");

                InputStream is = new FileInputStream(ruta);
                PdfReader pdfReader = new PdfReader(is,null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
                 //Foto del estudiante
                Image imageEstudiante = Image.getInstance("C://archivos//control_escolar//fotos//" + estudiante.getMatricula()+".jpg");
                imageEstudiante.setAbsolutePosition(46,120);
                imageEstudiante.scalePercent(46f,36f);
                PdfContentByte content = pdfStamper.getOverContent(1);
                content.addImage(imageEstudiante);
                
                //FIRMA DEL ESTUDIANTE
                Image imageFirma = Image.getInstance("C://archivos//control_escolar//firmas//" + estudiante.getMatricula()+".png");
                imageFirma.setAbsolutePosition(45,75);
                imageFirma.scalePercent(62f,53f);
                PdfContentByte contentFirma = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
                contentFirma.addImage(imageFirma);
                //CODIGO DE BARRAS
                PdfContentByte cb = pdfStamper.getOverContent(2);
                Barcode128 codigo = new Barcode128();
                codigo.setCodeType(Barcode128.CODE128);
                codigo.setCode(estudiante.getMatricula());
                Image image = codigo.createImageWithBarcode(cb, null, null);
                image.setAbsolutePosition(13f, 37f);
                image.scalePercent(120f,60f);
                cb.addImage(image);
                //datos
                AcroFields fields = pdfStamper.getAcroFields();
                fields.setField("txtNombre", estudiante.getEstudiante().getAspirante().getIdPersona().getNombre().concat(" ").concat(estudiante.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno()).concat(" ").concat(estudiante.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno()));
                fields.setField("txtCurp", estudiante.getEstudiante().getAspirante().getIdPersona().getCurp());
                fields.setField("txtGrupoS",estudiante.getEstudiante().getAspirante().getIdPersona().getDatosMedicos().getCveTipoSangre().getNombre());
                fields.setField("txtMatricula", String.valueOf(estudiante.getMatricula()));
                fields.setField("txtCarrera", String.valueOf(estudiante.getCarrera().getNombre()));
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
            }
        } catch (BadElementException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServiceProcesoInscripcion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            }
