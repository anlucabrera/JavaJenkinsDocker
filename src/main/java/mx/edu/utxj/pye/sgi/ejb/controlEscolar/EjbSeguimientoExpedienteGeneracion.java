/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoTitulacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbSeguimientoExpedienteGeneracion")
public class EjbSeguimientoExpedienteGeneracion {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbCarga ejbCarga;
    
    @EJB EjbIntegracionExpedienteTitulacion ejbIntegracionExpedienteTitulacion;
    
    public static final String ACTUALIZADO_TSU = "expTitulacion_tsu.xlsx", ACTUALIZADO_ING = "expTitulacion_ing.xlsx";

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        PeriodoEscolarFechas periodoFechas = em.createQuery("SELECT p FROM PeriodoEscolarFechas p WHERE current_timestamp between p.inicio and p.fin", PeriodoEscolarFechas.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        
        PeriodosEscolares periodo = new PeriodosEscolares();
        
        if(periodoFechas != null)
        {
            periodo = em.find(PeriodosEscolares.class, periodoFechas.getPeriodo());
        }
        return periodo;
    }
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito a la coordinación de titulación
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarTitulacion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalTitulacion").orElse(60)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbSeguimientoExpedienteGeneracion.validarTitulacion)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de generaciones que tienen evento de integración de expediente de titulación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoExpedienteTitulacion>> getExpedientesRegistrados(){
        try{
            
            List<ExpedienteTitulacion> expedientesReg = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.activo=:valor",  ExpedienteTitulacion.class)
                    .setParameter("valor", Boolean.TRUE)
                    .getResultList();
            
            List<DtoExpedienteTitulacion> listaExpedientes = new ArrayList<>();
          
            expedientesReg.forEach(expediente -> {
                
                DtoExpedienteTitulacion dto = ejbIntegracionExpedienteTitulacion.getDtoExpedienteTitulacion(expediente).getValor();
                listaExpedientes.add(dto);
            });
           
            
            return ResultadoEJB.crearCorrecto(listaExpedientes, "Lista de expedientes registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de expedientes registrados. (EjbSeguimientoExpedienteGeneracion.getExpedientesRegistrados)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de generaciones que tienen evento de integración de expediente de titulación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesExpedientes(){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            List<Short> generacionesEventos = em.createQuery("SELECT e FROM EventoTitulacion e ORDER BY e.generacion DESC",  EventoTitulacion.class)
                    .getResultStream()
                    .map(p->p.getGeneracion())
                    .distinct()
                    .collect(Collectors.toList());
          
            generacionesEventos.forEach(gen -> {
                Generaciones generacion = em.find(Generaciones.class, gen);
                listaGeneraciones.add(generacion);
            });
             
            return ResultadoEJB.crearCorrecto(listaGeneraciones, "Lista de generaciones que tiene eventos de integración de expedientes de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones en los que se han registrado expedientes. (EjbSeguimientoExpedienteGeneracion.getGeneracionesExpedientes)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de niveles educativos de la generación seleccionada que tienen evento de integración de expediente de titulación
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> obtenerListaNivelesGeneracion(Generaciones generacion){
        try{
            List<String> listaNiveles = new ArrayList<>();
            
            List<EventoTitulacion> nivelesEventos = em.createQuery("SELECT e FROM EventoTitulacion e WHERE e.generacion=:generacion ORDER BY e.nivel DESC",  EventoTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultList();
          
            nivelesEventos.forEach(nivelEvento -> {
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, nivelEvento.getNivel());
                listaNiveles.add(nivel.getNombre());
            });
            
             List<String> listaNivelesDistintos = listaNiveles.stream()
                    .distinct()
                    .collect(Collectors.toList());
          
            return ResultadoEJB.crearCorrecto(listaNivelesDistintos, "Lista de niveles educativos que tienen eventos de integración de expedientes de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos en los que se han registrado expedientes. (EjbSeguimientoExpedienteGeneracion.obtenerListaNivelesGeneracion)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos de la generación y nivel seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerListaProgramasEducativos(Generaciones generacion, String nivel){
        try{
            List<AreasUniversidad> listaProgramas = new ArrayList<>();
            
            String nivelE = obtenerClaveNivelEducativo(nivel);
            
            List<Short> programasGeneracionNivel = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.evento.generacion =:generacion AND e.evento.nivel =:nivel",  ExpedienteTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelE)
                    .getResultStream()
                    .map(p->p.getEstudiante().getGrupo().getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
           
            programasGeneracionNivel.forEach(programaGenNiv -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, programaGenNiv);
                listaProgramas.add(programa);
            });
            
            return ResultadoEJB.crearCorrecto(listaProgramas.stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList()), "Lista de programas educativos de la generación y nivel seleccionado se obtuvieron correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos de la generación y nivel seleccionado. (EjbSeguimientoExpedienteGeneracion.obtenerListaProgramasEducativos)", e, null);
        }
    }
    
     public String obtenerClaveNivelEducativo(String nivel) {
        //verificar que el parametro no sea nulo
        if (nivel == null) {
            return null;
        }
        
        String nivelE = "";
        
        switch (nivel) {
            case "Técnico Superior Universitario":
                nivelE = "TSU";
                break;
            case "Ingeniería":
                nivelE = "5A";
                break;
            case "Licenciatura":
                nivelE = "5B";
                break;
            case "Ingeniería Profesional":
                nivelE = "5B3";
                break;
            default:

                break;
        }
        
        return nivelE;
    }
     
     /**
     * Permite obtener la lista de expedientes registrados de la generación, nivel y programa educativo seleccionado
     * @param generacion
     * @param nivel
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoExpedienteTitulacion>> obtenerListaExpedientesProgramaEducativo(Generaciones generacion, String nivel, AreasUniversidad programa){
        try{
            String nivelE = obtenerClaveNivelEducativo(nivel);
            
            List<ExpedienteTitulacion> expedientesReg = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.evento.generacion=:generacion AND e.evento.nivel=:nivel AND e.estudiante.grupo.idPe=:programa",  ExpedienteTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelE)
                    .setParameter("programa", programa.getArea())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<DtoExpedienteTitulacion> listaExpedientes = new ArrayList<>();
          
            expedientesReg.forEach(expediente -> {
                DtoExpedienteTitulacion dto = ejbIntegracionExpedienteTitulacion.getDtoExpedienteTitulacion(expediente).getValor();
                listaExpedientes.add(dto);
            });
           
            return ResultadoEJB.crearCorrecto(listaExpedientes, "La lista de expedientes registrados del programa educativo, generación y nivel seleccionado se obtuvieron correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de expedientes registrado con los parametros selecionados. (EjbSeguimientoExpedienteGeneracion.obtenerListaExpedientesProgramaEducativo)", e, null);
        }
    }
    
      /**
     * Permite validar o invalidar un expediente de titulación
     * @param expedienteTitulacion
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> validarExpediente(ExpedienteTitulacion expedienteTitulacion, Personal personal){
        try{
           
            Boolean valor;
            Date fecha;
            Integer persona;
            
            if(expedienteTitulacion.getValidado()){
                valor = false;
                fecha = null;
                persona = null;
            }else{
                valor = true;
                fecha = new Date();
                persona = personal.getClave();
            }
            
            expedienteTitulacion.setValidado(valor);
            expedienteTitulacion.setFechaValidacion(fecha);
            expedienteTitulacion.setPersonalValido(persona);
            em.merge(expedienteTitulacion);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Se ha cambiadp el status del expediente correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del expediente. (EjbSeguimientoExpedienteGeneracion.validarExpediente)", e, null);
        }
    }
    
     public ResultadoEJB<String> buscarFotografia(DtoExpedienteTitulacion dtoExpedienteTitulacion){
        try{
            
            DocumentoExpedienteTitulacion docExp = new DocumentoExpedienteTitulacion();
            String ruta = "";
            
            if (dtoExpedienteTitulacion.getProgramaEducativo().getNivelEducativo().getNivel().equals("TSU")) {
                
                docExp = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.documento.documento =18 AND d.expediente.expediente =:expediente", DocumentoExpedienteTitulacion.class)
                    .setParameter("expediente", dtoExpedienteTitulacion.getExpediente().getExpediente())
                    .getResultStream().findFirst().orElse(null);
                
            } else if (!dtoExpedienteTitulacion.getProgramaEducativo().getNivelEducativo().getNivel().equals("TSU")) {
                
                docExp = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.documento.documento =25 AND d.expediente.expediente =:expediente", DocumentoExpedienteTitulacion.class)
                    .setParameter("expediente", dtoExpedienteTitulacion.getExpediente().getExpediente())
                    .getResultStream().findFirst().orElse(null);
                
            }
            
            if (docExp == null) {
                ruta = "C:\\archivos\\formatosTitulacion\\sinFotografia.png";
            } else {
                ruta = docExp.getRuta();
                File f = new File(ruta);
                if (!f.exists()) {
                    ruta = "C:\\archivos\\formatosTitulacion\\sinFotografia.png";
                }
            }
        return ResultadoEJB.crearCorrecto(ruta, "Se realizó la búsqueda de la fotografía correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la fotografía. (EjbSeguimientoExpedienteGeneracion.buscarFotografia)", e, null);
        }
     }
        
    public ResultadoEJB<DocumentoExpedienteTitulacion> buscarExtDocExpediente(Integer claveDoc, ExpedienteTitulacion expediente){
        try{
            
            DocumentoExpedienteTitulacion docExp = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.documento.documento =:claveDoc AND d.expediente.expediente =:expediente", DocumentoExpedienteTitulacion.class)
                    .setParameter("claveDoc", claveDoc)
                    .setParameter("expediente", expediente.getExpediente())
                    .getResultStream().findFirst().orElse(null);
           
            
        return ResultadoEJB.crearCorrecto(docExp, "Se realizó la búsqueda del documento correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda del documento. (EjbSeguimientoExpedienteGeneracion.buscarExtDocExpediente)", e, null);
        }
        
    }
        
    public ResultadoEJB<List<DtoDocumentoTitulacionEstudiante>> obtenerListaDocumentosExpediente(ExpedienteTitulacion expediente){
        try{
            String proceso = "TitulacionTSU";
            
            if(!expediente.getEvento().getNivel().equals("TSU")){
                proceso = "TitulacionIngLic";
            }
            
            List<DtoDocumentoTitulacionEstudiante> listaDocumentos = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso = :proceso AND d.obligatorio = :valor", DocumentoProceso.class)
                    .setParameter("proceso", proceso)
                    .setParameter("valor", true)
                    .getResultStream()
                    .map(doc -> packDocumento(doc, expediente).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            if(proceso.equals("TitulacionIngLic")){
                
                Estudiante e = em.createQuery("SELECT e FROM Estudiante as e WHERE e.matricula = :matricula AND e.grupo.generacion=:generacion AND e.grupo.idPe=:programa ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", expediente.getEstudiante().getMatricula())
                    .setParameter("generacion", expediente.getEvento().getGeneracion())
                    .setParameter("programa", (short)50)
                    .getResultStream().findFirst().orElse(null);
                
                if(e == null){
                      listaDocumentos = listaDocumentos.stream().filter(p-> p.getDocumentoProceso().getDocumento().getDocumento() != 32).collect(Collectors.toList());
                }
            }
               
        return ResultadoEJB.crearCorrecto(listaDocumentos, "Se realizó la búsqueda de la lista de documentos del expediente correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la lista de documentos. (EjbSeguimientoExpedienteGeneracion.obtenerListaDocumentosExpediente)", e, null);
        }
        
    }
    
    public ResultadoEJB<DtoDocumentoTitulacionEstudiante> packDocumento(DocumentoProceso documentoProceso, ExpedienteTitulacion expedienteTitulacion){
        try{
            if(documentoProceso == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un documento nulo.", DtoDocumentoTitulacionEstudiante.class);
            if(documentoProceso.getDocumentoProceso()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un documento con clave nula.", DtoDocumentoTitulacionEstudiante.class);

            DocumentoProceso documentoProcesoBD = em.find(DocumentoProceso.class, documentoProceso.getDocumentoProceso());
            if(documentoProcesoBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un documento no registrado previamente en base de datos.", DtoDocumentoTitulacionEstudiante.class);

            DocumentoExpedienteTitulacion documentoExpedienteTitulacion = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.expediente.expediente=:expediente AND d.documento =:documento", DocumentoExpedienteTitulacion.class)
                    .setParameter("expediente", expedienteTitulacion.getExpediente())
                    .setParameter("documento", documentoProcesoBD.getDocumento())
                    .getResultStream()
                    .findFirst()
                    .orElse(new DocumentoExpedienteTitulacion());
            
            DtoDocumentoTitulacionEstudiante dto = new DtoDocumentoTitulacionEstudiante(documentoProcesoBD, documentoExpedienteTitulacion);
            return ResultadoEJB.crearCorrecto(dto, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbSeguimientoExpedienteGeneracion.packDocumento).", e, DtoDocumentoTitulacionEstudiante.class);
        }
    }
    
     /**
     * Permite desactivar o activar un expediente de titulación de un estudiante con situación académica de baja temporal o definitiva durante el proceso de estadía
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> desactivarExpedienteTitulacion(ExpedienteTitulacion expedienteTitulacion){
        try{
            if(expedienteTitulacion.getExpediente()== null) return ResultadoEJB.crearErroneo(2, "La clave del expediente de titulación no puede ser nula.", ExpedienteTitulacion.class);

            String mensaje = "La situación del expediente de titulación es " + expedienteTitulacion.getActivo();
            if (expedienteTitulacion.getActivo()) {
                expedienteTitulacion.setActivo(false);
                em.merge(expedienteTitulacion);
                f.flush();
                mensaje="El expediente de titulación se ha desactivado correctamente.";
            } else if(!expedienteTitulacion.getActivo()){
                expedienteTitulacion.setActivo(true);
                em.merge(expedienteTitulacion);
                f.flush();
                mensaje="El expediente de titulación activado correctamente.";
            }

            return ResultadoEJB.crearCorrecto(expedienteTitulacion, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo desactivar o activar el expediente de titulación seleccionado. (EjbSeguimientoExpedienteGeneracion.desactivarExpedienteTitulacion)", e, null);
        }
    }
    
      /**
     * Permite obtener la lista de pasos registro de expediente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getPasosRegistro(){
        try{
            List<String> pasosRegistro = new ArrayList<>();
            pasosRegistro.add("Inicio Integración");
            pasosRegistro.add("Datos Personales");
            pasosRegistro.add("Domicilio y Comunicaciones");
            pasosRegistro.add("Antecedentes Académicos");
            pasosRegistro.add("Fotografía para Título");
            pasosRegistro.add("Fin Integración");
            
            return ResultadoEJB.crearCorrecto(pasosRegistro, "Lista de pasos de registro de integración de expediente de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de pasos de registro de integración de expediente de titulación. (EjbSeguimientoExpedienteGeneracion.getPasosRegistro)", e, null);
        }
    }
    
     /**
     * Permite actualizar el paso de registro del expediente de titulación seleccionado
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> actualizarPasoRegistro(ExpedienteTitulacion expedienteTitulacion){
        try{
            
            em.merge(expedienteTitulacion);
            f.flush();

            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Se ha actualizado el paso de registro del expediente de titulación seleccionado");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el paso de registro del expediente de titulación seleccionado. (EjbSeguimientoExpedienteGeneracion.actualizarPasoRegistro)", e, null);
        }
    }
    
     /**
     * Permite identificar a una lista de posibles expedientes de titulación que coincida con el nombre o matricula ingresada
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con expedientes de un estudiante ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarExpediente(String pista){
        try{
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<ExpedienteTitulacion> expedientesReg = em.createQuery("select e from ExpedienteTitulacion e INNER JOIN e.estudiante est INNER JOIN est.aspirante a INNER JOIN a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, est.matricula) like concat('%',:pista,'%') AND e.activo=:activo ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, est.periodo DESC", ExpedienteTitulacion.class)
                    .setParameter("pista", pista)
                    .setParameter("activo", true)
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            expedientesReg.forEach(expediente -> {
                String datosComplete = expediente.getEstudiante().getMatricula()+ " - " +expediente.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno()+" "+ expediente.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno()+" "+ expediente.getEstudiante().getAspirante().getIdPersona().getNombre();
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, expediente.getEstudiante().getPeriodo());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, expediente.getEstudiante().getGrupo().getIdPe());
                String periodoEscolar = periodo.getMesInicio().getAbreviacion()+" - "+periodo.getMesFin().getAbreviacion()+" "+periodo.getAnio();
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(expediente.getEstudiante(), datosComplete, periodoEscolar, programaEducativo);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
           
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de expedientes activos. (EjbSeguimientoExpedienteGeneracion.buscarExpediente)", e, null);
        }
    }
    
     /**
     * Permite obtener la base de expedientes registrados de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoExpedienteTitulacion>> obtenerBaseExpedientes(Generaciones generacion, String nivel){
        try{
            String nivelE = obtenerClaveNivelEducativo(nivel);
            
            List<ExpedienteTitulacion> expedientesReg = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.evento.generacion=:generacion AND e.evento.nivel=:nivel",  ExpedienteTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelE)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<DtoExpedienteTitulacion> listaExpedientes = new ArrayList<>();
          
            expedientesReg.forEach(expediente -> {
                DtoExpedienteTitulacion dto = ejbIntegracionExpedienteTitulacion.getDtoExpedienteTitulacion(expediente).getValor();
                listaExpedientes.add(dto);
            });
            
            return ResultadoEJB.crearCorrecto(listaExpedientes, "La base de expedientes registrados de la generación y nivel seleccionado se obtuvieron correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la base de expedientes registrado con los parametros selecionados. (EjbSeguimientoExpedienteGeneracion.obtenerBaseExpedientes)", e, null);
        }
    }
   
      /**
     * Permite generar reporte de base de expedientes registrados por generaicón y nivel educativo seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    public String getReporteBaseGeneracion(Generaciones generacion, String nivel) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\reporteTitulacionCE.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);
        
        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_ING);
        
        if(nivel.equals("Técnico Superior Universitario")){
            plantillaC = rutaPlantillaC.concat(ACTUALIZADO_TSU);
        }
       
        Map beans = new HashMap();
        beans.put("baseExpGen", obtenerBaseExpedientes(generacion, nivel).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
}
