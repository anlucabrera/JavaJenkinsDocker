/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRolEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAsignacionRolesEstadia")
public class EjbAsignacionRolesEstadia {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /* MÓDULO DE ASIGNACIÓN DE ROLES POR DIRECTOR DE CARRERA */
    
     /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbAsignacionRolesEstadia.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un encargado de dirección.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El encargado de dirección de área académica no se pudo validar. (EjbAsignacionRolesEstadia.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite el área de la que está a cargo el director de carrera o encargado de dirección
     * @param clave Clave del responsable del área
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreaSuperior(Integer clave){
         try{
             
            AreasUniversidad areaSuperior = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.responsable =:clave",  AreasUniversidad.class)
                    .setParameter("clave", clave)
                    .getSingleResult();
          
            
            return ResultadoEJB.crearCorrecto(areaSuperior, "Área Superior de la que es responsable el director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el área superior de la que es responsable el director. (EjbAsignacionRolesEstadia.getAreaSuperior)", e, null);
        }
    }
   
     /**
     * Permite verificar si existe evento activo para el proceso de asignación de roles de estadía
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoEstadia> buscarEventoActivo(){
        try{
            Date fechaActual = new Date();
            EventoEstadia eventoEstadia = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.actividad=:actividad AND e.usuario=:usuario AND ((:fechaActual BETWEEN e.fechaInicio AND e.fechaFin) OR :fechaActual <= e.fechaInicio)", EventoEstadia.class)
                    .setParameter("actividad", "Asignacion coordinador asesor estadia")
                    .setParameter("usuario", "Director de carrera")
                    .setParameter("fechaActual", fechaActual)
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(eventoEstadia, "Evento activo para asignación de roles de estadía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe evento activo para asignación de roles de estadía (EjbAsignacionRolesEstadia.buscarEventoActivo).", e, EventoEstadia.class);
        }
    }
    
    /**
     * Permite buscar generación del evento activo de estadía
     * @param eventoEstadia
     * @return Devueleve la tarea integradora, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<Generaciones> buscarGeneracionEventoActivo(EventoEstadia eventoEstadia){
        try{
            Generaciones generacion = em.createQuery("SELECT g FROM Generaciones g WHERE g.generacion=:generacion", Generaciones.class)
                    .setParameter("generacion", eventoEstadia.getGeneracion())
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(generacion, "Generación del Evento activo para asignación de roles de estadía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la generación del evento activo para asignación de roles de estadía (EjbAsignacionRolesEstadia.buscarGeneracionEventoActivo).", e, Generaciones.class);
        }
    }
    
    /**
     * Permite obtener la lista de generaciones en la que existen eventos de estadía registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesEventosRegistrados(){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            List<EventoEstadia> listaEventos = em.createQuery("SELECT e FROM EventoEstadia e ORDER BY e.generacion DESC",  EventoEstadia.class)
                    .getResultList();
          
            listaEventos.forEach(evento -> {
                Generaciones generacion = em.find(Generaciones.class, evento.getGeneracion());
                listaGeneraciones.add(generacion);
            });
            
            List<Generaciones> listaGeneracionesDistintas = listaGeneraciones.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas, "Lista de generaciones en que existen eventos de estadía registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones en los que existen eventos de estadía registrados. (EjbAsignacionRolesEstadia.getGeneracionesEventosRegistrados)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de niveles educativo en la que existen eventos de estadía registrados de la generación seleccionada
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesGeneracionEventosRegistrados(Generaciones generacion){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
             List<EventoEstadia> listaEventos = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion ORDER BY e.nivel DESC",  EventoEstadia.class)
                        .setParameter("generacion", generacion.getGeneracion())
                    .getResultList();
                
            listaEventos.forEach(evento -> {
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, evento.getNivel());
                     listaNiveles.add(nivel);
            });
            
            List<ProgramasEducativosNiveles> listaNivelesDistintos = listaNiveles.stream()
                    .distinct()
                    .sorted(Comparator.comparing(ProgramasEducativosNiveles::getNombre))
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaNivelesDistintos, "Lista de niveles educativos de la generación seleccionada en que existen eventos de estadía registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos de la generación seleccionada en los que existen eventos de estadía registrados. (EjbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de niveles educativo en la que existen eventos de estadía registrados de la generación seleccionada del área académica correspondiente
     * @param generacion
     * @param area
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesGeneracionAreaEventosRegistrados(Generaciones generacion, AreasUniversidad area){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
            List<String> listaNivelesArea = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior=:area AND a.categoria.categoria=:categoria",  AreasUniversidad.class)
                    .setParameter("area", area.getArea())
                    .setParameter("categoria", (short)9)
                    .getResultStream()
                    .map(p->p.getNivelEducativo().getNivel())
                    .distinct()
                    .collect(Collectors.toList());
            
            List<EventoEstadia> listaEventos = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel IN :niveles AND e.actividad=:actividad ORDER BY e.evento ASC",  EventoEstadia.class)
                        .setParameter("generacion", generacion.getGeneracion())
                        .setParameter("niveles", listaNivelesArea)
                        .setParameter("actividad", "Asignacion coordinador asesor estadia")
                        .getResultStream().collect(Collectors.toList());
            
            
            listaEventos.forEach(evento -> {
               ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, evento.getNivel());
               listaNiveles.add(nivel);
            });
            
            return ResultadoEJB.crearCorrecto(listaNiveles.stream().sorted(Comparator.comparing(ProgramasEducativosNiveles::getNombre)).collect(Collectors.toList()), "Lista de niveles educativos de la generación seleccionada en que existen eventos de estadía registrados del área académica correspondiente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos de la generación seleccionada en los que existen eventos de estadía registrados del área académica correspondiente. (EjbAsignacionRolesEstadia.getNivelesGeneracionAreaEventosRegistrados)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de programas educativos en la que existen eventos de estadía registrados de la generación y nivel educativo seleccionados
     * @param generacion
     * @param nivelEducativo
     * @param areaSuperior
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasNivelesGeneracionEventosRegistrados(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Short areaSuperior){
        try{
            List<AreasUniversidad> listaProgramas = new ArrayList<>();
            
            List<SeguimientoEstadiaEstudiante> listaSeguimientos = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.generacion=:generacion AND s.evento.nivel=:nivel",  SeguimientoEstadiaEstudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultList();
          
            listaSeguimientos.forEach(seguimiento -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, seguimiento.getEstudiante().getGrupo().getIdPe());
                listaProgramas.add(programa);
            });
            
            List<AreasUniversidad> listaProgramasDistintos = new ArrayList<>();
            if(areaSuperior==5){
                listaProgramasDistintos = listaProgramas.stream()
                    .distinct()
                    .sorted(Comparator.comparing(AreasUniversidad::getNombre))
                    .collect(Collectors.toList());
            }else{
                listaProgramasDistintos= listaProgramas.stream()
                    .distinct()
                    .filter(x-> x.getAreaSuperior()== areaSuperior)
                    .sorted(Comparator.comparing(AreasUniversidad::getNombre))
                    .collect(Collectors.toList());
            }
            
            return ResultadoEJB.crearCorrecto(listaProgramasDistintos, "Lista de programas educativos de la generación y nivel seleccionado en que existen eventos de estadía registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos de la generación y nivel seleccionado en los que existen eventos de estadía registrados. (EjbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de personal activo del área seleccionada e información de rol asignada en caso de existir
     * @param areaSuperior área superior para realizar la búsqueda
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRolEstadia>> getDocentesPorAreaRolesEstadia(AreasUniversidad areaSuperior, Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<DtoRolEstadia> personal = em.createQuery("SELECT p FROM Personal p WHERE p.areaSuperior=:areaSuperior AND p.actividad.actividad=:actividad AND p.status<>:status ORDER BY p.nombre ASC", Personal.class)
                    .setParameter("areaSuperior", areaSuperior.getArea())
                    .setParameter("actividad", (short)3)
                    .setParameter("status", 'B')
                    .getResultStream()
                    .map(per -> pack(per, generacion, nivelEducativo).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(personal, "Lista de personal del área académica seleccionada y roles de estadía asignados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista del personal con asignación de roles de estadía. (EjbAsignacionRolesEstadia.getDocentesPorAreaRolesEstadia)", e, null);
        }
    }
    
    /**
     * Empaqueta personal en un DTO Wrapper para asignación de roles de estadía
     * @param personal Carga académica que se va a empaquetar
     * @param generacion
     * @param nivelEducativo
     * @return Carga académica empaquetada
     */
    public ResultadoEJB<DtoRolEstadia> pack(Personal personal, Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            if(personal == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar personal nulo.", DtoRolEstadia.class);
            if(personal.getClave()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar personal con clave nula.", DtoRolEstadia.class);

            Personal personalBD = em.find(Personal.class, personal.getClave());
            if(personalBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar personal no registrado previamente en base de datos.", DtoRolEstadia.class);

            String rolCoordinador = "";
            String rolAsesor = "";
            
            CoordinadorAreaEstadia coordinadorEstadia = em.createQuery("SELECT c FROM CoordinadorAreaEstadia c WHERE c.personal=:personal AND c.evento.generacion=:generacion AND c.evento.nivel=:nivel", CoordinadorAreaEstadia.class)
                    .setParameter("personal", personal.getClave())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if(coordinadorEstadia !=null){
                rolCoordinador = "Coordinador de estadía";
            }
            
            AsesorAcademicoEstadia asesorAcademico = em.createQuery("SELECT a FROM AsesorAcademicoEstadia a WHERE a.personal=:personal AND a.evento.generacion=:generacion AND a.evento.nivel=:nivel", AsesorAcademicoEstadia.class)
                    .setParameter("personal", personal.getClave())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if(asesorAcademico !=null){
                rolAsesor = "Asesor académico";
            }
           
            DtoRolEstadia dto = new DtoRolEstadia(personal, rolCoordinador, rolAsesor);
            
            return ResultadoEJB.crearCorrecto(dto, "Personal y rol de estadía empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el personal y rol de estadía (EjbAsignacionRolesEstadia.pack).", e, DtoRolEstadia.class);
        }
    }
    
     /**
     * Permite asignar o quitar el rol de coordinador de estadía
     * @param dtoRolEstadia
     * @param areaSuperior
     * @param eventoActivo
     * @return Resultado del proceso
     */
    public ResultadoEJB<CoordinadorAreaEstadia> asignarCoordinadorEstadia(DtoRolEstadia dtoRolEstadia, AreasUniversidad areaSuperior, EventoEstadia eventoActivo){
        try{
            String mensaje;
            CoordinadorAreaEstadia coordinadorAreaEstadia = new CoordinadorAreaEstadia();
            if (dtoRolEstadia.getRolCoordinador() == "") {
                coordinadorAreaEstadia.setAreaAcademica(areaSuperior.getArea());
                coordinadorAreaEstadia.setPersonal(dtoRolEstadia.getPersonal().getClave());
                coordinadorAreaEstadia.setEvento(eventoActivo);
                em.persist(coordinadorAreaEstadia);
                f.flush();
                mensaje="Asignación de coordinador de estadía registrado correctamente.";
            } else {
                Integer delete = em.createQuery("DELETE FROM CoordinadorAreaEstadia c WHERE c.personal=:personal AND c.evento.evento=:evento", CoordinadorAreaEstadia.class)
                .setParameter("personal", dtoRolEstadia.getPersonal().getClave())
                .setParameter("evento", eventoActivo.getEvento())
                .executeUpdate();
                 mensaje="Asignación de coordinador de estadía eliminado correctamente.";
            }
            
                       
            return ResultadoEJB.crearCorrecto(coordinadorAreaEstadia, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar o eliminar la asignación de coordinador de estadía. (EjbAsignacionRolesEstadia.asignarCoordinadorEstadia)", e, null);
        }
    }
    
     /**
     * Permite asignar o quitar el rol de coordinador de estadía
     * @param dtoRolEstadia
     * @param areaSuperior
     * @param eventoActivo
     * @return Resultado del proceso
     */
    public ResultadoEJB<AsesorAcademicoEstadia> asignarAsesorEstadia(DtoRolEstadia dtoRolEstadia, AreasUniversidad areaSuperior, EventoEstadia eventoActivo){
        try{
            String mensaje;
            AsesorAcademicoEstadia asesorAcademicoEstadia = new AsesorAcademicoEstadia();
            if (dtoRolEstadia.getRolAsesor()== "") {
                asesorAcademicoEstadia.setCoordinador(null);
                asesorAcademicoEstadia.setProgramaEducativo(areaSuperior.getArea());
                asesorAcademicoEstadia.setPersonal(dtoRolEstadia.getPersonal().getClave());
                asesorAcademicoEstadia.setEvento(eventoActivo);
                em.persist(asesorAcademicoEstadia);
                f.flush();
                mensaje="Asignación de asesor de estadía registrado correctamente.";
            } else {
                Integer delete = em.createQuery("DELETE FROM AsesorAcademicoEstadia a WHERE a.personal=:personal AND a.evento.evento=:evento", AsesorAcademicoEstadia.class)
                .setParameter("personal", dtoRolEstadia.getPersonal().getClave())
                .setParameter("evento", eventoActivo.getEvento())
                .executeUpdate();
                 mensaje="Asignación de asesor de estadía eliminado correctamente.";
            }
            
                       
            return ResultadoEJB.crearCorrecto(asesorAcademicoEstadia, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar o eliminar la asignación de asesor de estadía. (EjbAsignacionRolesEstadia.asignarAsesorEstadia)", e, null);
        }
    }
    
     /* MÓDULO DE ASIGNACIÓN DE ESTUDIANTES POR ASESOR ACADÉMICO */
    
     /**
     * Permite validar si el usuario autenticado es personal docente con rol de asesor de estadía
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocenteAsesorEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            List<AsesorAcademicoEstadia> listaAsignaciones = asignacionesEstadia(clave).getValor();
            if (!listaAsignaciones.isEmpty()) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente con asignaciones de asesor de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar con asignaciones de asesor de estadía. (EjbAsignacionRolesEstadia.validarDocenteAsesorEstadia)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de asignaciones como asesor de estadía del personal docente
     * @param clave
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AsesorAcademicoEstadia>> asignacionesEstadia(Integer clave){
        try{
            List<AsesorAcademicoEstadia> listaAsignaciones = em.createQuery("SELECT a FROM AsesorAcademicoEstadia a WHERE a.personal=:personal ORDER BY a.evento.evento DESC",  AsesorAcademicoEstadia.class)
                    .setParameter("personal", clave)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaAsignaciones, "Lista de asignaciones como asesor de estadía del personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de asignaciones como asesor de estadía del personal docente. (EjbAsignacionRolesEstadia.asignacionesEstadia)", e, null);
        }
    }
    
    
     /**
     * Permite verificar si existe evento de estadía activo para asignación de estudiantes
     * @return Devueleve el evento de estadía, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<EventoEstadia> buscarEventoActivoAsigEstudiantes(){
        try{
            EventoEstadia eventoEstadia = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.actividad=:actividad AND e.usuario=:usuario AND current_date between e.fechaInicio and e.fechaFin", EventoEstadia.class)
                    .setParameter("actividad", "Asignacion estudiantes")
                    .setParameter("usuario", "Asesor academico")
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(eventoEstadia, "Evento activo para asignación de estudiantes de estadía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe evento activo para asignación de estudiantes de estadía (EjbAsignacionRolesEstadia.buscarEventoActivoAsigEst).", e, EventoEstadia.class);
        }
    }
    
     /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos que pertenecen al área académica del docente
     * @param areaSuperior
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Short>> getProgramasEducativosArea(AreasUniversidad areaSuperior, ProgramasEducativosNiveles nivel){
        try{
            List<AreasUniversidad> listaProgramas = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior=:area AND a.nivelEducativo.nivel=:nivel AND a.vigente=:vigente ORDER BY a.nombre DESC",  AreasUniversidad.class)
                    .setParameter("area", areaSuperior.getArea())
                    .setParameter("nivel", nivel.getNivel())
                    .setParameter("vigente", "1")
                    .getResultList();
            
            List<Short> listaClaves = new ArrayList<>();
            if(!listaProgramas.isEmpty()){
                listaProgramas.forEach(pe -> {
                    listaClaves.add(pe.getArea());
                });
            }
            
            return ResultadoEJB.crearCorrecto(listaClaves, "Lista de programas educativos que pertenecen al área académica del docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos que pertenecen al área académica del docente. (EjbAsignacionRolesEstadia.getProgramasEducativosArea)", e, null);
        }
    }
    
     /**
     * Permite identificar a una lista de posibles estudiante para asignar estadía, de la generación y nivel seleccionado, situación regular en el periodo actual
     * @param generacion
     * @param programasEducativos
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(Generaciones generacion, List<Short> programasEducativos, String pista){
        try{
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p INNER JOIN e.grupo g WHERE g.generacion=:generacion AND e.carrera IN :programas AND e.tipoEstudiante.idTipoEstudiante=:tipo AND g.grado IN :grados AND e.periodo=:periodo AND concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("programas", programasEducativos)
                    .setParameter("tipo", (short)1)
                    .setParameter("periodo", getPeriodoActual().getPeriodo())
                    .setParameter("grados", grados)
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
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbAsignacionRolesEstadia.buscarEstudiante)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los datos del estudiante seleccionado. (EjbAsignacionRolesEstadia.buscarDatosEstudiante)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe registro de asignación del estudiante
     * @param eventoSeleccionado
     * @param estudiante
     * @return Resultado del proceso.
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> buscarEstudianteAsignado(EventoEstadia eventoSeleccionado, DtoDatosEstudiante estudiante){
        try{
            SeguimientoEstadiaEstudiante seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.matricula=:matricula", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("matricula", estudiante.getEstudiante().getMatricula())
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Registro de asignación del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe registro de asignación del estudiante (EjbAsignacionRolesEstadia.buscarEstudianteAsignado).", e, SeguimientoEstadiaEstudiante.class);
        }
    }
    
    /**
     * Permite verificar el event de estadía con los parametros seleccionados
     * @param generacion
     * @param nivel
     * @param actividad
     * @return Resultado del proceso.
     */
    public ResultadoEJB<EventoEstadia> buscarEventoSeleccionado(Generaciones generacion,  ProgramasEducativosNiveles nivel, String actividad){
        try{
            EventoEstadia eventoEstadia = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad", EventoEstadia.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .setParameter("actividad", actividad)
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(eventoEstadia, "Evento encontrado con los parametros seleccionados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe el evento con los parametros seleccionados (EjbAsignacionRolesEstadia.buscarEventoSeleccionado).", e, EventoEstadia.class);
        }
    }
    
     /**
     * Permite buscar la asignación del asesor académico en el evento seleccionado
     * @param eventoSeleccionado
     * @param personal
     * @return Resultado del proceso..
     */
    public ResultadoEJB<AsesorAcademicoEstadia> buscarAsesorAcademico(Personal personal, EventoEstadia eventoSeleccionado){
        try{
            AsesorAcademicoEstadia asesorAcademico = em.createQuery("SELECT a FROM AsesorAcademicoEstadia a WHERE a.personal=:personal AND a.evento.evento=:evento", AsesorAcademicoEstadia.class)
                    .setParameter("personal", personal.getClave())
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(asesorAcademico, "Registro de asignación del asesor académico");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe registro de asignación del asesor académico (EjbAsignacionRolesEstadia.buscarAsesorAcademico).", e, AsesorAcademicoEstadia.class);
        }
    }
    
     /**
     * Permite asignar o quitar asignación del estudiante
     * @param generacion
     * @param nivel
     * @param personal
     * @param estudiante
     * @param eventoActivo
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> asignarEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivel, Personal personal, EventoEstadia eventoActivo, DtoDatosEstudiante estudiante){
        try{
            String mensaje;
            EventoEstadia eventoAsignacion = buscarEventoSeleccionado(generacion, nivel, "Asignacion estudiantes").getValor();
            SeguimientoEstadiaEstudiante seguimientoEstadiaBD = buscarEstudianteAsignado(eventoAsignacion, estudiante).getValor();
            EventoEstadia eventoAsesor = buscarEventoSeleccionado(generacion, nivel, "Asignacion coordinador asesor estadia").getValor();
            AsesorAcademicoEstadia asesorAcademico = buscarAsesorAcademico(personal, eventoAsesor).getValor();
           
            SeguimientoEstadiaEstudiante seguimientoEstadia = new SeguimientoEstadiaEstudiante();
            if (seguimientoEstadiaBD == null) {
                seguimientoEstadia.setEvento(eventoActivo);
                seguimientoEstadia.setEstudiante(estudiante.getEstudiante());
                seguimientoEstadia.setAsesor(asesorAcademico);
                seguimientoEstadia.setFechaRegistro(new Date());
                seguimientoEstadia.setEmpresa(null);
                seguimientoEstadia.setProyecto(null);
                seguimientoEstadia.setFechaInicio(null);
                seguimientoEstadia.setFechaFin(null);
                seguimientoEstadia.setValidacionCoordinador(Boolean.FALSE);
                seguimientoEstadia.setFechaValidacionCoordinador(null);
                seguimientoEstadia.setComentariosCoordinador(null);
                seguimientoEstadia.setValidacionDirector(Boolean.FALSE);
                seguimientoEstadia.setFechaValidacionDirector(null);
                seguimientoEstadia.setPromedioAsesorExterno(0);
                seguimientoEstadia.setPromedioAsesorInterno(0);
                seguimientoEstadia.setValidacionVinculacion(Boolean.FALSE);
                seguimientoEstadia.setFechaValidacionVinculacion(null);
                em.persist(seguimientoEstadia);
                f.flush();
                mensaje="Asignación de estudiante de estadía registrado correctamente.";
            } else {
                if(seguimientoEstadiaBD.getAsesor()==asesorAcademico){
                    mensaje="Ya asignó previamente el estudiante seleccionado.";
                }else{
                    mensaje="El estudiante ha sido asignado por otro asesor académico.";
                }
            }      
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar o eliminar la asignación de coordinador de estadía. (EjbAsignacionRolesEstadia.asignarEstudiante)", e, null);
        }
    }
    
     /**
     * Permite crear registro de asesor empresarial para el seguimiento de estadía
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<AsesorEmpresarialEstadia> crearRegistroAsesorEmpresarial(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            String mensaje;
            SeguimientoEstadiaEstudiante seguimientoEstadiaBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
           
            AsesorEmpresarialEstadia asesorEmpresarialEstadia = new AsesorEmpresarialEstadia();
            if (seguimientoEstadiaBD != null) {
                asesorEmpresarialEstadia.setSeguimiento(seguimientoEstadiaBD);
                asesorEmpresarialEstadia.setNombre("Sin información");
                asesorEmpresarialEstadia.setApellidoPaterno("Sin información");
                asesorEmpresarialEstadia.setApellidoMaterno("Sin información");
                asesorEmpresarialEstadia.setPuesto("Sin información");
                asesorEmpresarialEstadia.setTelefono("0000000000");
                asesorEmpresarialEstadia.setEmail("correo@prueba.com");
                em.persist(asesorEmpresarialEstadia);
                f.flush();
                mensaje="Asesor empresarial registrado correctamente.";
            } else {
                mensaje="No se puede crear registro si no existe el seguimiento de estadía.";
            }      
            return ResultadoEJB.crearCorrecto(asesorEmpresarialEstadia, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el asesor empresarial. (EjbAsignacionRolesEstadia.crearRegistroAsesorEmpresarial)", e, null);
        }
    }
   
     /**
     * Permite obtener la lista de estudiantes asignados al asesor académico seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDatosEstudiante>> getListaEstudiantesEstadiaAsignados(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Personal personal, EventoEstadia eventoSeleccionado){
        try{
            EventoEstadia eventoAsesor = buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion coordinador asesor estadia").getValor();
            AsesorAcademicoEstadia asesorAcademico = buscarAsesorAcademico(personal, eventoAsesor).getValor();
         
            List<DtoDatosEstudiante> seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.asesor.asesorEstadia=:asesor", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("asesor", asesorAcademico.getAsesorEstadia())
                    .getResultStream()
                    .map(seg -> packSeguimiento(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoDatosEstudiante::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Lista de estudiantes asignados al asesor académico seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes asignados al asesor académico seleccionado. (EjbAsignacionRolesEstadia.getListaEstudiantesEstadiaAsignados)", e, null);
        }
    }
    
    /**
     * Empaqueta el seguimiento de estadía en un DTO Wrapper para obtener datos del estudiante
     * @param seguimientoEstadiaEstudiante Seguimiento de estadía a empaquetar
     * @param eventoSeleccionado
     * @return Seguimiento de estadía empaquetada
     */
    public ResultadoEJB<DtoDatosEstudiante> packSeguimiento(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, EventoEstadia eventoSeleccionado){
        try{
            if(seguimientoEstadiaEstudiante == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar seguimiento nulo.", DtoDatosEstudiante.class);
            if(seguimientoEstadiaEstudiante.getSeguimiento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar seguimiento con clave nula.", DtoDatosEstudiante.class);

            SeguimientoEstadiaEstudiante segEstBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
            if(segEstBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar seguimiento no registrado previamente en base de datos.", DtoDatosEstudiante.class);

            Estudiante estudianteBD = em.find(Estudiante.class, seguimientoEstadiaEstudiante.getEstudiante().getIdEstudiante());
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudianteBD.getGrupo().getIdPe());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudianteBD.getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudianteBD, programaEducativo,periodoEscolar);
            
            return ResultadoEJB.crearCorrecto(dtoDatosEstudiante, "Seguimiento de estadía empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el seguimiento de estadía. (EjbAsignacionRolesEstadia.packSeguimiento).", e, DtoDatosEstudiante.class);
        }
    }
    
     /**
     * Permite eliminar la asignación del estudiante
     * @param estudiante
     * @param eventoSeleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarAsignacion(EventoEstadia eventoSeleccionado, DtoDatosEstudiante estudiante){
        try{
            SeguimientoEstadiaEstudiante seguimientoEstadiaBD = buscarEstudianteAsignado(eventoSeleccionado, estudiante).getValor();
           
            Integer delete = em.createQuery("DELETE FROM SeguimientoEstadiaEstudiante s WHERE s.seguimiento=:seguimiento", SeguimientoEstadiaEstudiante.class)
                .setParameter("seguimiento", seguimientoEstadiaBD.getSeguimiento())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Asignación de estudiante de estadía eliminado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la asignación de estudiante de estadía. (EjbAsignacionRolesEstadia.eliminarAsignacion)", e, null);
        }
    }
}
