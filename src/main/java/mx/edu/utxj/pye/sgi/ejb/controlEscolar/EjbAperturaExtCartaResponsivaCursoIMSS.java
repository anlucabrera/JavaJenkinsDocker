/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoApertExtCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCartaResponsivaCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAperturaExtCartaResponsivaCursoIMSS")
public class EjbAperturaExtCartaResponsivaCursoIMSS {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite obtener la lista de actividades de eventos de vinculación registrados de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventoVinculacion>> getActividadesEventoVinculacion(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            List<EventoVinculacion> listaEventos = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel ORDER BY e.evento ASC",  EventoVinculacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaEventos, "Lista de actividades de eventos de vinculación registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de actividades de eventos de vinculación registrados. (EjbAperturaExtCartaResponsivaCursoIMSS.getActividadesEventoVinculacion)", e, null);
        }
    }
    
     /**
     * Permite identificar a una lista de posibles estudiante para registrar apertura extemporánea, de la generación y nivel seleccionado, situación regular en el periodo actual
     * @param generacion
     * @param nivelEducativo
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, String pista){
        try{
            List<Integer> listaGrados = new ArrayList<>(); 
            if("TSU".equals(nivelEducativo.getNivel())){
                listaGrados.add(5);
                listaGrados.add(6);
            }else{
                listaGrados.add(10);
                listaGrados.add(11);
            }
            
            Integer ultimoPeriodo = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.grado IN :listaGrados ORDER BY g.idGrupo DESC", Grupo.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("listaGrados", listaGrados)
                    .getResultStream()
                    .map(p-> p.getPeriodo())
                    .findFirst()
                    .orElse(null);
            

            List<Short> listaTipos = new ArrayList<>(); listaTipos.add((short)1); listaTipos.add((short)4);
            
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p INNER JOIN e.grupo g WHERE g.generacion=:generacion AND e.tipoEstudiante.idTipoEstudiante IN :tipos AND e.periodo=:periodo AND concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("tipos", listaTipos)
                    .setParameter("periodo", ultimoPeriodo)
                    .setParameter("pista", pista)
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno()+" "+ estudiante.getAspirante().getIdPersona().getApellidoMaterno()+" "+ estudiante.getAspirante().getIdPersona().getNombre()+ " - " + estudiante.getMatricula();
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                String periodoEscolar = periodo.getMesInicio().getAbreviacion()+" - "+periodo.getMesFin().getAbreviacion()+" "+periodo.getAnio();
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete, periodoEscolar, programaEducativo);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes activos. (EjbAperturaExtCartaResponsivaCursoIMSS.buscarEstudiante)", e, null);
        }
    }
    
     /**
     * Permite buscar la información del estudiante seleccionado
     * @param claveEstudiante Clave del estudiante
     * @return Resultado del proceso 
     */
    public ResultadoEJB<DtoDatosEstudiante> buscarDatosEstudiante(Integer claveEstudiante){
        try{
            Estudiante estudiante = em.createQuery("select e from Estudiante e where e.idEstudiante =:estudiante", Estudiante.class)
                    .setParameter("estudiante", claveEstudiante)
                    .getSingleResult();
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudiante, programaEducativo,periodoEscolar);
           
            return ResultadoEJB.crearCorrecto(dtoDatosEstudiante, "Datos del estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los datos del estudiante seleccionado. (EjbAperturaExtCartaResponsivaCursoIMSS.buscarDatosEstudiante)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aperturas extemporáneas de eventos de vinculación registradas
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoApertExtCartaResponsivaCursoIMSS>> getListaAperturasExtemporaneas(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            List<AperturaExtemporaneaEventoVinculacion> listaAperturas = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel ORDER BY a.evento.evento, a.fechaInicio ASC", AperturaExtemporaneaEventoVinculacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<DtoApertExtCartaResponsivaCursoIMSS> listaAperturasExtemporaneas = new ArrayList<>();
            
            listaAperturas.forEach(apertura -> {
                Personal personalRegistra = em.find(Personal.class, apertura.getGrabaApertura());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, apertura.getSeguimiento().getEstudiante().getGrupo().getIdPe());
                
                DtoApertExtCartaResponsivaCursoIMSS dtoApertExtCartaResponsivaCursoIMSS = new DtoApertExtCartaResponsivaCursoIMSS();
                dtoApertExtCartaResponsivaCursoIMSS.setAperturaExtemporanea(apertura);
                dtoApertExtCartaResponsivaCursoIMSS.setProgramaEducativo(programaEducativo);
                dtoApertExtCartaResponsivaCursoIMSS.setPersonalRegistra(personalRegistra);
                listaAperturasExtemporaneas.add(dtoApertExtCartaResponsivaCursoIMSS);
            });
            
            return ResultadoEJB.crearCorrecto(listaAperturasExtemporaneas, "Lista de aperturas extemporáneas de eventos de vinculación registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de aperturas extemporáneas de eventos de vinculación registradas. (EjbAperturaExtCartaResponsivaCursoIMSS.getListaAperturasExtemporaneas)", e, null);
        }
    }
    
     /**
     * Permite eliminar apertura extemporánea seleccionada
     * @param aperturaExtemporaneaEventoVinculacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarAperturaExtemporanea(AperturaExtemporaneaEventoVinculacion aperturaExtemporaneaEventoVinculacion){
        try{
            
            Integer delete = em.createQuery("DELETE FROM AperturaExtemporaneaEventoVinculacion a WHERE a.aperturaExtemporanea=:apertura", AperturaExtemporaneaEventoVinculacion.class)
                .setParameter("apertura", aperturaExtemporaneaEventoVinculacion.getAperturaExtemporanea())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se ha eliminado correctamente la apertura extemporánea seleccionada.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar correctamente la apertura extemporánea seleccionada. (EjbAperturaExtCartaResponsivaCursoIMSS.eliminarAperturaExtemporanea)", e, null);
        }
    }
    
     /**
     * Permite guardar apertura extemporánea de la actividad y estudiante seleccionado
     * @param actividad
     * @param estudiante     
     * @param fechaInicio     
     * @param fechaFin     
     * @param personal     
     * @return Resultado del proceso
     */
    public ResultadoEJB<AperturaExtemporaneaEventoVinculacion> guardarAperturaExtemporanea(EventoVinculacion actividad, DtoDatosEstudiante estudiante, Date fechaInicio, Date fechaFin, Personal personal){
        try{
            SeguimientoVinculacionEstudiante seguimiento = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.matricula=:matricula AND s.estudiante.grupo.generacion=:generacion AND s.nivel=:nivel", SeguimientoVinculacionEstudiante.class)
                    .setParameter("matricula", estudiante.getEstudiante().getMatricula())
                    .setParameter("generacion", actividad.getGeneracion())
                    .setParameter("nivel", actividad.getNivel())
                    .getResultStream()
                    .findFirst().orElse(null);
            
            if(seguimiento == null) return ResultadoEJB.crearErroneo(2, "No se puede guardar apertura de estudiante que no tiene seguimiento de vinculación registrado previamente en base de datos.", AperturaExtemporaneaEventoVinculacion.class);
            
             Date fechaFinCompleta = ejbPermisoAperturaExtemporanea.obtenerFechaFin(fechaFin);
            
            AperturaExtemporaneaEventoVinculacion aperturaExtemporaneaEventoVinculacion = new AperturaExtemporaneaEventoVinculacion();
            aperturaExtemporaneaEventoVinculacion.setEvento(actividad);
            aperturaExtemporaneaEventoVinculacion.setSeguimiento(seguimiento);
            aperturaExtemporaneaEventoVinculacion.setFechaInicio(fechaInicio);
            aperturaExtemporaneaEventoVinculacion.setFechaFin(fechaFinCompleta);
            aperturaExtemporaneaEventoVinculacion.setGrabaApertura(personal.getClave());
            em.persist(aperturaExtemporaneaEventoVinculacion);
            em.flush();
                
            return ResultadoEJB.crearCorrecto(aperturaExtemporaneaEventoVinculacion, "Se ha registrado correctamente la apertura extemporánea del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la apertura extemporánea del estudiante seleccionado. (EjbAperturaExtCartaResponsivaCursoIMSS.guardarAperturaExtemporanea)", e, null);
        }
    }
   
     /**
     * Permite actualizar la apertura extemporánea de vinculación seleccionada
     * @param dtoApertExtCartaResponsivaCursoIMSS
     * @return Resultado del proceso
     */
    public ResultadoEJB<AperturaExtemporaneaEventoVinculacion> actualizarAperturaExtemporanea(DtoApertExtCartaResponsivaCursoIMSS dtoApertExtCartaResponsivaCursoIMSS){
        try{
            em.merge(dtoApertExtCartaResponsivaCursoIMSS.getAperturaExtemporanea());
            em.flush();
           
            return ResultadoEJB.crearCorrecto(dtoApertExtCartaResponsivaCursoIMSS.getAperturaExtemporanea(), "Se actualizó correctamente la apertura extemporánea de vinculación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la apertura extemporánea de vinculación seleccionada. (EjbAperturaExtCartaResponsivaCursoIMSS.actualizarAperturaExtemporanea)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de aperturas extemporáneas registradas de una actividad del estudiante seleccionado
     * @param actividad
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> buscarAperturasRegistradasEvento(EventoVinculacion actividad, DtoDatosEstudiante estudiante){
        try{
            List<AperturaExtemporaneaEventoVinculacion> listaAperturaExtemporaneaEvento = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.seguimiento.estudiante.matricula=:matricula AND a.evento.evento=:evento", AperturaExtemporaneaEventoVinculacion.class)
                    .setParameter("matricula", estudiante.getEstudiante().getMatricula())
                    .setParameter("evento", actividad.getEvento())
                    .getResultStream()
                    .collect(Collectors.toList());
                
            return ResultadoEJB.crearCorrecto(listaAperturaExtemporaneaEvento.size(), "Lista de aperturas extemporáneas registradas de una actividad del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener correctamente la lista de aperturas extemporáneas registradas de una actividad del estudiante seleccionado. (EjbAperturaExtCartaResponsivaCursoIMSS.buscarAperturasRegistradasEvento)", e, null);
        }
    }
    
    /**
     * Permite buscar si existe seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoVinculacionEstudiante> buscarSeguimientoEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Estudiante estudiante){
        try{
            SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.matricula=:matricula AND s.estudiante.grupo.generacion=:generacion AND s.nivel=:nivel AND s.activo=:activo", SeguimientoVinculacionEstudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .setParameter("activo", true)
                    .getResultStream()
                    .findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(seguimientoVinculacionEstudiante, "Seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado. (EjbAperturaExtCartaResponsivaCursoIMSS.buscarSeguimientoEstudiante)", e, null);
        }
    }
}
