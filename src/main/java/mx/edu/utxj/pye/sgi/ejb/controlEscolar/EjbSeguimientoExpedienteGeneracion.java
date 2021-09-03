/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPagosEstudianteFinanzas;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Viewregalumnosnoadeudo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;

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
            
            List<ExpedienteTitulacion> expedientesReg = em.createQuery("SELECT e FROM ExpedienteTitulacion e",  ExpedienteTitulacion.class)
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
            
            List<EventoTitulacion> generacionesEventos = em.createQuery("SELECT e FROM EventoTitulacion e ORDER BY e.generacion DESC",  EventoTitulacion.class)
                    .getResultList();
          
            generacionesEventos.forEach(generacionEvento -> {
                Generaciones generacion = em.find(Generaciones.class, generacionEvento.getGeneracion());
                listaGeneraciones.add(generacion);
            });
            
             List<Generaciones> listaGeneracionesDistintas = listaGeneraciones.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas, "Lista de generaciones que tiene eventos de integración de expedientes de titulación.");
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
           
            List<Integer> eventos = em.createQuery("SELECT e FROM EventoTitulacion e WHERE e.generacion=:generacion AND e.nivel =:nivel ORDER BY e.nivel DESC",  EventoTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelE)
                    .getResultStream()
                    .map(e -> e.getEvento())
                    .collect(Collectors.toList());
           
            List<ExpedienteTitulacion> programasEventos = em.createQuery("SELECT er FROM ExpedienteTitulacion er WHERE er.evento.evento IN :eventos",  ExpedienteTitulacion.class)
                    .setParameter("eventos", eventos)
                    .getResultList();
          
            programasEventos.forEach(programaEvento -> {
                Estudiante ultRegEstudiante = em.createQuery("SELECT e FROM Estudiante e where e.matricula=:matricula AND e.grupo.generacion=:generacion ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula",  programaEvento.getMatricula().getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
                  
                AreasUniversidad programa = em.find(AreasUniversidad.class, ultRegEstudiante.getCarrera());
                listaProgramas.add(programa);
            });
            
             List<AreasUniversidad> listaProgramasDistintos = listaProgramas.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaProgramasDistintos, "Lista de programas educativos de la generación y nivel seleccionado se obtuvieron correctamente.");
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
            List<DtoExpedienteTitulacion> listaExpedientes = getExpedientesRegistrados().getValor();
            
            String nivelE = obtenerClaveNivelEducativo(nivel);
            
            List<DtoExpedienteTitulacion> listaExpedientePE = listaExpedientes.stream()
                    .filter(e-> e.getGeneracion().getGeneracion() == generacion.getGeneracion())
                    .filter(e-> nivelE.equals(e.getExpediente().getEvento().getNivel()))
                    .filter(e-> e.getProgramaEducativo().getArea() == programa.getArea())
                    .collect(Collectors.toList());
           
            return ResultadoEJB.crearCorrecto(listaExpedientePE, "La lista de expedientes registrados del programa educativo, generación y nivel seleccionado se obtuvieron correctamente.");
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
        
    public ResultadoEJB<List<DocumentoExpedienteTitulacion>> obtenerListaDocumentosExpediente(ExpedienteTitulacion expediente){
        try{
            
            List<DocumentoExpedienteTitulacion> listaDocExp = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.expediente.expediente =:expediente", DocumentoExpedienteTitulacion.class)
                    .setParameter("expediente", expediente.getExpediente())
                    .getResultList();
           
            
        return ResultadoEJB.crearCorrecto(listaDocExp, "Se realizó la búsqueda de la lista de documentos del expediente correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la lista de documentos. (EjbSeguimientoExpedienteGeneracion.obtenerListaDocumentosExpediente)", e, null);
        }
        
    }
}
