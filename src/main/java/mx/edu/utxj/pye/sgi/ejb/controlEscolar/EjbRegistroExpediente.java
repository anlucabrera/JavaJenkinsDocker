/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNuevoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FechaTerminacionTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.ListaEstudiantesGeneral;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.AsentamientoPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroExpediente")
public class EjbRegistroExpediente {
    @EJB EjbPersonalBean ejbPersonalBean;
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
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbRegistroExpediente.validarTitulacion)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de generaciones a elegir 
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesExpediente(){
        try{
             List<Short> claves = em.createQuery("SELECT e FROM EventoTitulacion e",  EventoTitulacion.class)
                .getResultStream()
                .map(a -> a.getGeneracion())
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, getPeriodoActual().getPeriodo().shortValue());
            }
            List<Generaciones> generaciones = em.createQuery("SELECT g FROM Generaciones g WHERE g.generacion IN :claves ORDER BY g.generacion DESC", Generaciones.class)
                    .setParameter("claves", claves)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(generaciones, "Generaciones ordenadas de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones. (EjbRegistroExpediente.getGeneracionesExpediente)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de niveles educativos a elegir 
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesExpedientes(){
        try{
             List<String> claves = em.createQuery("SELECT e FROM EventoTitulacion e", EventoTitulacion.class)
                .getResultStream()
                .map(a -> a.getNivel())
                .distinct()
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, "No existe niveles registrados");
            }
            List<ProgramasEducativosNiveles> niveles = em.createQuery("SELECT p FROM ProgramasEducativosNiveles p WHERE p.nivel IN :claves ORDER BY p.nivel ASC", ProgramasEducativosNiveles.class)
                    .setParameter("claves", claves)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(niveles, "Niveles educativos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos. (EjbRegistroExpediente.getNivelesExpedientes)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista procesos de integración a elegir depedniendo de la genación y nivel ducativo seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventoTitulacion>> getProcesosIntegracion(Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            List<EventoTitulacion> eventosTitulacion = em.createQuery("SELECT e FROM EventoTitulacion e WHERE e.generacion =:generacion AND e.nivel =:nivel ORDER BY e.numProceso ASC", EventoTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(eventosTitulacion, "Niveles educativos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos. (EjbRegistroExpediente.getProcesosIntegracion)", e, null);
        }
    }
    
   /**
     * Permite identificar a una lista de posibles estudiante para registrar expediente
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @param generacion
     * @param nivel
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(String pista, Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            List<ListaEstudiantesGeneral> listaEstudiantes = em.createQuery("select l from ListaEstudiantesGeneral l WHERE concat(l.apellidoPaterno, l.apellidoMaterno, l.nombre, l.matricula) like concat('%',:pista,'%') ORDER BY l.apellidoPaterno, l.apellidoMaterno, l.nombre DESC", ListaEstudiantesGeneral.class)
                    .setParameter("pista", pista)
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            listaEstudiantes.forEach(listaEst -> {
                
                Estudiante estudiante = em.createQuery("select e from Estudiante e WHERE e.matricula =:matricula ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", listaEst.getMatricula())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
                
                AreasUniversidad programa = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
                ProgramasEducativosNiveles programasEducativosNiveles = em.find(ProgramasEducativosNiveles.class, programa.getNivelEducativo().getNivel());
                
                    String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno()+" "+ estudiante.getAspirante().getIdPersona().getApellidoMaterno()+" "+ estudiante.getAspirante().getIdPersona().getNombre()+ " - " + estudiante.getMatricula();
                    PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                    String periodoEscolar = periodo.getMesInicio().getAbreviacion()+" - "+periodo.getMesFin().getAbreviacion()+" "+periodo.getAnio();
                    AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                    DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete, periodoEscolar, programaEducativo);
                    listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbRegistroExpediente.buscarEstudiante)", e, null);
        }
    }
    
     /**
     * Empaqueta DTO para registrar expediente nuevo
     * @param expedienteTitulacion
     * @return DTO 
     */
    public ResultadoEJB<DtoNuevoExpedienteTitulacion> getDtoNuevoExpedienteTitulacion(ExpedienteTitulacion expedienteTitulacion){
        try{
            if(expedienteTitulacion == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un expediente nulo.", DtoNuevoExpedienteTitulacion.class);
            if(expedienteTitulacion.getExpediente()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un expediente con clave nula.", DtoNuevoExpedienteTitulacion.class);
     
            SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
            String fechaNacimiento = sm.format(expedienteTitulacion.getEstudiante().getAspirante().getIdPersona().getFechaNacimiento());
            
            AreasUniversidad progEdu = em.find(AreasUniversidad.class,  expedienteTitulacion.getEstudiante().getGrupo().getIdPe());
            Generaciones generacion = em.find(Generaciones.class, expedienteTitulacion.getEvento().getGeneracion());
            ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, progEdu.getNivelEducativo().getNivel());
            
            Generos genero = em.find(Generos.class,  expedienteTitulacion.getEstudiante().getAspirante().getIdPersona().getGenero());
            Domicilio domicilio = em.find(Domicilio.class, expedienteTitulacion.getEstudiante().getAspirante().getIdAspirante());
            Estado estadoDom = em.find(Estado.class, domicilio.getIdEstado());
            MunicipioPK mpk = new MunicipioPK(estadoDom.getIdestado(), domicilio.getIdMunicipio());
            Municipio munDom = em.find(Municipio.class, mpk);
            AsentamientoPK apk = new AsentamientoPK(estadoDom.getIdestado(), munDom.getMunicipioPK().getClaveMunicipio(), domicilio.getIdAsentamiento());
            Asentamiento asentDom = em.find(Asentamiento.class, apk);
            MedioComunicacion comunicaciones = em.find(MedioComunicacion.class, expedienteTitulacion.getEstudiante().getAspirante().getIdPersona().getIdpersona());
            DatosAcademicos datosAcademicos = em.find(DatosAcademicos.class, expedienteTitulacion.getEstudiante().getAspirante().getIdAspirante());
            Iems iems = em.find(Iems.class, datosAcademicos.getInstitucionAcademica());
            Localidad locIems = em.find(Localidad.class, iems.getLocalidad().getLocalidadPK());
            
            FechaTerminacionTitulacion fechaTerminacionTitulacion = em.createQuery("SELECT f FROM FechaTerminacionTitulacion f WHERE f.generacion = :generacion AND f.nivel =:nivel", FechaTerminacionTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            DtoNuevoExpedienteTitulacion dto = new DtoNuevoExpedienteTitulacion(expedienteTitulacion, fechaNacimiento, progEdu, nivel, generacion, genero, domicilio, estadoDom, munDom, asentDom, comunicaciones, datosAcademicos, iems, locIems, fechaTerminacionTitulacion);
            return ResultadoEJB.crearCorrecto(dto, "Expediente de titulación empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el expediente de titulación (EjbRegistroExpediente.getDtoNuevoExpedienteTitulacion).", e, DtoNuevoExpedienteTitulacion.class);
        }
    }
    
     /**
     * Permite consultar si ya existe expediente de titulación registrado
     * @param estudiante
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> getExisteExpedienteTitulacion(Estudiante estudiante, Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            
            ExpedienteTitulacion expedienteTitulacion = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.estudiante.matricula=:matricula AND e.evento.generacion =:generacion AND e.evento.nivel =:nivel", ExpedienteTitulacion.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Expediente de titulación empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el expediente de titulación (EjbRegistroExpediente.getExisteExpedienteTitulacion).", e, ExpedienteTitulacion.class);
        }
    }
    
     /**
     * Permite guardar expediente de titulación
     * @param evento
     * @param fechaIntExp
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> guardarExpedienteTitulacion(EventoTitulacion evento, Date fechaIntExp, Estudiante estudiante){
        try{
            ExpedienteTitulacion expedienteTitulacion = new ExpedienteTitulacion();
            expedienteTitulacion.setEvento(evento);
            expedienteTitulacion.setFechaRegistro(fechaIntExp);
            expedienteTitulacion.setEstudiante(estudiante);
            expedienteTitulacion.setValidado(false);
            expedienteTitulacion.setFechaValidacion(null);
            expedienteTitulacion.setPersonalValido(null);
            expedienteTitulacion.setPasoRegistro("Inicio Integración");
            expedienteTitulacion.setFechaPaso(new Date());
            expedienteTitulacion.setPromedio((float)0.0);
            expedienteTitulacion.setServicioSocial(Boolean.FALSE);
            expedienteTitulacion.setActivo(true);
            em.persist(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "El expediente de titulación se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el expediente de titulación. (EjbRegistroExpediente.guardarExpedienteTitulacion)", e, null);
        }
    }
    
    /**
     * Permite actualizar el expediente de titulación
     * @param expedienteTitulacion
     * @param pasoRegistro
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> actualizarExpediente(ExpedienteTitulacion expedienteTitulacion, String pasoRegistro){
        try{
            expedienteTitulacion.setPasoRegistro(pasoRegistro);
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "El expediente de titulación se han actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el expédiente de titulación. (EjbIntegracionExpedienteTitulacion.actualizarExpediente)", e, null);
        }
    }
}
