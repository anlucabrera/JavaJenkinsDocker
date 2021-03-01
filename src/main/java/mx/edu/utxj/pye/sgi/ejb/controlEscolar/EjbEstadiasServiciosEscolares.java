/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEntregaFotografiasEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPorcentajeEntregaFotografias;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EntregaFotografiasEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbEstadiasServiciosEscolares")
public class EjbEstadiasServiciosEscolares {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB EjbSeguimientoEstadia ejbSeguimientoEstadia;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /* MÓDULO REGISTRO DE EVENTOS DE ESTADÍA */
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de generaciones registradas a partir de la 2019 - 2021
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneraciones(){
        try{
            List<Generaciones> generaciones = em.createQuery("SELECT g FROM Generaciones g WHERE g.generacion >=:generacion ORDER BY g.generacion DESC", Generaciones.class)
                    .setParameter("generacion", (short) 30)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(generaciones, "Lista de generaciones para eventos de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones para eventos de estadía. (EjbEstadiasServiciosEscolares.getGeneraciones)", e, null);
        }
    }
    
    /**
     * Permite obtener la última generación registrada en eventos estadía
     * @return Resultado del proceso
     */
    public ResultadoEJB<Generaciones> getUltimaGeneracionRegistrada(){
        try{
            List<Short> listaClaves = em.createQuery("SELECT e FROM EventoEstadia e ORDER BY e.generacion DESC", EventoEstadia.class)
                    .getResultStream()
                    .map(p->p.getGeneracion())
                    .distinct()
                    .collect(Collectors.toList());
            
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            listaClaves.forEach(gen -> {
                Generaciones generacion = em.find(Generaciones.class, gen);
                listaGeneraciones.add(generacion);
            });
            
            return ResultadoEJB.crearCorrecto(listaGeneraciones.get(0), "Última generación registrada en eventos estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la última generación registrada en eventos estadía. (EjbEstadiasServiciosEscolares.getUltimaGeneracionRegistrada)", e, null);
        }
    }
    
    /**
     * Permite obtener el último nivel educativo registrado en eventos estadía
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProgramasEducativosNiveles> getUltimoNivelEducativoRegistrado(){
        try{
            List<String> listaClaves = em.createQuery("SELECT e FROM EventoEstadia e ORDER BY e.nivel DESC", EventoEstadia.class)
                    .getResultStream()
                    .map(p->p.getNivel())
                    .distinct()
                    .collect(Collectors.toList());
            
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            listaClaves.forEach(niv -> {
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, niv);
                listaNiveles.add(nivel);
            });
            
            return ResultadoEJB.crearCorrecto(listaNiveles.get(0), "Útimo nivel educativo registrado en eventos estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el último nivel educativo registrado en eventos estadía. (EjbEstadiasServiciosEscolares.getUltimoNivelEducativoRegistrado)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de niveles educativos registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesEducativos(){
        try{
            
            List<ProgramasEducativosNiveles> nivelesEducativos = em.createQuery("SELECT p FROM ProgramasEducativosNiveles p WHERE p.nivel<>:valor ORDER BY p.nombre ASC", ProgramasEducativosNiveles.class)
                    .setParameter("valor", "5B3")
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(nivelesEducativos, "Lista de niveles educativos para eventos de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos para eventos de estadía. (EjbEstadiasServiciosEscolares.getNivelesEducativos)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe eventos de estadía registrados de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarExistenEventos(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoRegistrado = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel", EventoEstadia.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream().findFirst().orElse(null);
            
            Boolean valor;
            if(eventoRegistrado==null)
            {
                valor = Boolean.FALSE;
            }else{
                valor = Boolean.TRUE;
            }
           return ResultadoEJB.crearCorrecto(valor, "Resultado de evento de estadía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron eventos de estadía", e, Boolean.TYPE);
        }
    }
    /**
     * Permite obtener la lista de eventos de estadía de la generación y nivel educativo seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEventosEstadia>> getEventosEstadia(){
        try{
            
            List<DtoEventosEstadia> listaActividades = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel ORDER BY e.evento ASC", EventoEstadia.class)
                    .setParameter("generacion", (short)30)
                    .setParameter("nivel", "TSU")
                    .getResultStream()
                    .map(actividad -> packEventoEstadia(actividad.getActividad(), actividad.getUsuario()).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaActividades, "Lista de eventos de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de estadía. (EjbEstadiasServiciosEscolares.getEventosEstadia)", e, null);
        }
    }
    
    /**
     * Empaqueta un evento de estadía del proceso en su DTO Wrapper
     * @param actividad
     * @param usuario
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoEventosEstadia> packEventoEstadia(String actividad, String usuario){
        try{
            DtoEventosEstadia dto = new DtoEventosEstadia(actividad, usuario, new Date(), new Date());
            return ResultadoEJB.crearCorrecto(dto, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbEstadiasServiciosEscolares.packEventoEstadia).", e, DtoEventosEstadia.class);
        }
    }
    
     /**
     * Permite actualizar las fechas de inicio y fin de un evento de estadía registrado previamente
     * @param dtoCalendarioEventosEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoEstadia> actualizarEventoRegistrado(DtoCalendarioEventosEstadia dtoCalendarioEventosEstadia){
        try{
            EventoEstadia evenEstBD  = em.find(EventoEstadia.class, dtoCalendarioEventosEstadia.getEventoEstadia().getEvento());
                evenEstBD.setFechaInicio(dtoCalendarioEventosEstadia.getEventoEstadia().getFechaInicio());
                evenEstBD.setFechaFin(dtoCalendarioEventosEstadia.getEventoEstadia().getFechaFin());
                em.merge(evenEstBD);
                em.flush();
            
            return ResultadoEJB.crearCorrecto(evenEstBD, "Se actualizó correctamente la información del evento de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del evento de estadía. (EjbEstadiasServiciosEscolares.actualizarEventoRegistrado)", e, null);
        }
    }
    
     /**
     * Permite guardar la lista de eventos de estadía de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param listaEventosEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCalendarioEventosEstadia>> guardarEventosEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, List<DtoEventosEstadia> listaEventosEstadia){
        try{
            
            listaEventosEstadia.forEach(evento -> {
                Date fechaFinCompleta = ejbPermisoAperturaExtemporanea.obtenerFechaFin(evento.getFechaFin());
                EventoEstadia eventoEstadia = new EventoEstadia();
                eventoEstadia.setGeneracion(generacion.getGeneracion());
                eventoEstadia.setNivel(nivelEducativo.getNivel());
                eventoEstadia.setActividad(evento.getActividad());
                eventoEstadia.setUsuario(evento.getUsuario());
                eventoEstadia.setFechaInicio(evento.getFechaInicio());
                eventoEstadia.setFechaFin(fechaFinCompleta);
                em.persist(eventoEstadia);
                em.flush();
            });
            
            List<DtoCalendarioEventosEstadia> lista = ejbSeguimientoEstadia.getCalendarioEventosEstadia(generacion, nivelEducativo).getValor();
            
            return ResultadoEJB.crearCorrecto(lista, "Se registró correctamente la lista de eventos de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la lista de eventos de estadía. (EjbEstadiasServiciosEscolares.guardarEventosEstadia)", e, null);
        }
    }
    
    /**
     * Permite eliminar la lista de eventos de estadía de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEventosEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            Integer delete = em.createQuery("DELETE FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel", EventoEstadia.class)
                .setParameter("generacion", generacion.getGeneracion())
                .setParameter("nivel", nivelEducativo.getNivel())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la lista de eventos de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la lista de eventos de estadía. (EjbEstadiasServiciosEscolares.eliminarEventosEstadia)", e, null);
        }
    }
    
    /* MÓDULO ENTREGA DE FOTOGRAFÍAS */
    
     /**
     * Permite identificar a una lista de posibles estudiante para asignar estadía, de la generación y nivel seleccionado, situación regular en el periodo actual
     * @param generacion
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(Generaciones generacion, String pista){
        try{
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p INNER JOIN e.grupo g WHERE g.generacion=:generacion AND e.tipoEstudiante.idTipoEstudiante=:tipo AND e.periodo=:periodo AND concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("tipo", (short)1)
                    .setParameter("periodo", ejbAsignacionRolesEstadia.getPeriodoActual().getPeriodo())
                    .setParameter("pista", pista)
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno()+" "+ estudiante.getAspirante().getIdPersona().getApellidoMaterno()+" "+ estudiante.getAspirante().getIdPersona().getNombre()+ " - " + estudiante.getMatricula();
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                String periodoEscolar = periodo.getMesInicio().getAbreviacion()+" - "+periodo.getMesFin().getAbreviacion()+" "+periodo.getAnio();
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete, periodoEscolar, programaEducativo);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbEstadiasServiciosEscolares.buscarEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de estudiantes que entregaron fotografías
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEntregaFotografiasEstadia>> getListaEstudiantesEntregaronFotografias(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoEntrega = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Entrega de fotografías").getValor();
         
            List<DtoEntregaFotografiasEstadia> entregaFotografias = em.createQuery("SELECT e FROM EntregaFotografiasEstudiante e WHERE e.evento.evento=:evento", EntregaFotografiasEstudiante.class)
                    .setParameter("evento", eventoEntrega.getEvento())
                    .getResultStream()
                    .map(entrega -> packEntregaFotografias(entrega).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoEntregaFotografiasEstadia::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(entregaFotografias, "Lista de estudiantes que entregaron fotografías.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes que entregaron fotografías. (EjbEstadiasServiciosEscolares.getListaEstudiantesEntregaronFotografias)", e, null);
        }
    }
    
    /**
     * Empaqueta la entrega de fotografías en su DTO Wrapper
     * @param entregaFotografiasEstudiante Entrega de fotografías a empaquetar
     * @return Entrega de fotografías empaquetada
     */
    public ResultadoEJB<DtoEntregaFotografiasEstadia> packEntregaFotografias(EntregaFotografiasEstudiante entregaFotografiasEstudiante){
        try{
            if(entregaFotografiasEstudiante == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar entrega de fotografías nulo.", DtoEntregaFotografiasEstadia.class);
            if(entregaFotografiasEstudiante.getEntrega()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar entrega de fotografías con clave nula.", DtoEntregaFotografiasEstadia.class);

            EntregaFotografiasEstudiante entregaFotografiasBD = em.find(EntregaFotografiasEstudiante.class, entregaFotografiasEstudiante.getEntrega());
            if(entregaFotografiasBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar entrega de fotografías no registrado previamente en base de datos.", DtoEntregaFotografiasEstadia.class);
          
            Estudiante estudiante = em.createQuery("SELECT e FROM Estudiante e where e.matricula=:matricula AND e.grupo.generacion=:generacion ORDER BY e.idEstudiante DESC", Estudiante.class)
                    .setParameter("matricula", entregaFotografiasBD.getMatricula().getMatricula())
                    .setParameter("generacion", entregaFotografiasBD.getEvento().getGeneracion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, entregaFotografiasBD.getMatricula().getCarrera());
            Personal personal = em.find(Personal.class, entregaFotografiasBD.getPersonalRecibio());
            
            DtoEntregaFotografiasEstadia dtoEntregaFotografiasEstadia = new DtoEntregaFotografiasEstadia(entregaFotografiasBD, estudiante, programaEducativo, personal);
            
            return ResultadoEJB.crearCorrecto(dtoEntregaFotografiasEstadia, "entrega de fotografías empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar entrega de fotografías. (EjbEstadiasServiciosEscolares.packEntregaFotografias).", e, DtoEntregaFotografiasEstadia.class);
        }
    }
    
    /**
     * Permite registrar entrega de fotografías del estudiante seleccionado
     * @param generacion
     * @param nivel
     * @param personal
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EntregaFotografiasEstudiante> registrarEntregaFotografias(Generaciones generacion, ProgramasEducativosNiveles nivel, Personal personal, DtoDatosEstudiante estudiante){
        try{
            EventoEstadia eventoEntrega = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivel, "Entrega de fotografías").getValor();
            
            EntregaFotografiasEstudiante entregaFotografiasEstudiante = new EntregaFotografiasEstudiante();
                entregaFotografiasEstudiante.setEvento(eventoEntrega);
                entregaFotografiasEstudiante.setMatricula(estudiante.getEstudiante());
                entregaFotografiasEstudiante.setFechaEntrega(new Date());
                entregaFotografiasEstudiante.setPersonalRecibio(personal.getClave());
                em.persist(entregaFotografiasEstudiante);
                f.flush();
                
            return ResultadoEJB.crearCorrecto(entregaFotografiasEstudiante, "Se ha registrado correctamente la entrega de fotografías del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la entrega de fotografías del estudiante seleccionado. (EjbEstadiasServiciosEscolares.registrarEntregaFotografias)", e, null);
        }
    }
    
     /**
     * Permite eliminar la entrega de fotografías del estudiante seleccionado
     * @param entregaFotografiasEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEntregaFotografias(EntregaFotografiasEstudiante entregaFotografiasEstudiante){
        try{
            
            Integer delete = em.createQuery("DELETE FROM EntregaFotografiasEstudiante e WHERE e.entrega=:entrega", EntregaFotografiasEstudiante.class)
                .setParameter("entrega", entregaFotografiasEstudiante.getEntrega())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se ha eliminado correctamente la entrega de fotografías del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar correctamente la entrega de fotografías del estudiante seleccionado. (EjbEstadiasServiciosEscolares.eliminarEntregaFotografias)", e, null);
        }
    }
    
    /**
     * Permite verificar si existe registro de entrega de fotografías del estudiante seleccionado
     * @param generacion
     * @param nivel
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EntregaFotografiasEstudiante> buscarRegistroEntregaFotografias(Generaciones generacion, ProgramasEducativosNiveles nivel, DtoDatosEstudiante estudiante){
        try{
            EventoEstadia eventoEntrega = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivel, "Entrega de fotografías").getValor();
            
            EntregaFotografiasEstudiante entregaFotografiasEstudiante = em.createQuery("SELECT e FROM EntregaFotografiasEstudiante e WHERE e.evento.evento = :evento AND e.matricula.matricula=:matricula", EntregaFotografiasEstudiante.class)
                    .setParameter("evento", eventoEntrega.getEvento())
                    .setParameter("matricula", estudiante.getEstudiante().getMatricula())
                    .getResultStream().findFirst().orElse(null);
                
            return ResultadoEJB.crearCorrecto(entregaFotografiasEstudiante, "Resultado de búsqueda de entrega de fotografías.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se btuvo resultado de búsqueda de entrega de fotografías. (EjbEstadiasServiciosEscolares.buscarRegistroEntregaFotografias)", e, null);
        }
    }
     /**
     * Permite obtener la lista de porcentaje de entrega de fotografías por programa educativo de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivel
     * @param listaEntregaFotografias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoPorcentajeEntregaFotografias>> getListaPorcentajeEntregaFotografias(Generaciones generacion, ProgramasEducativosNiveles nivel, List<DtoEntregaFotografiasEstadia> listaEntregaFotografias){
        try{
            List<Integer> listaPeriodos = listaEntregaFotografias.stream().map(p->p.getEstudiante().getPeriodo()).collect(Collectors.toList());
            
            Optional<Integer> maxNumber = listaPeriodos.stream().max((i, j) -> i.compareTo(j));
            
            List<AreasUniversidad> listaProgramasEducativos = listaEntregaFotografias.stream().map(p-> p.getProgramaEducativo()).collect(Collectors.toList());
            
            List<DtoPorcentajeEntregaFotografias> listaPorcentajeEntrega = new ArrayList<>();
            
            listaProgramasEducativos.forEach(programa -> {
                
                List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.grupo.idPe=:carrera AND e.grupo.periodo=:periodo AND e.grupo.generacion=:generacion", Estudiante.class)
                    .setParameter("carrera", programa.getArea())
                    .setParameter("periodo", maxNumber.get()) 
                    .setParameter("generacion", generacion.getGeneracion()) 
                    .getResultStream()
                    .collect(Collectors.toList());
                        
                
                Integer totalEstudiantes = listaEstudiantes.size();
                
                Long estudiantesPrograma = listaEntregaFotografias.stream().filter(p -> p.getProgramaEducativo()== programa).count();

                Integer estudiantesEntrega = (int) (long) estudiantesPrograma;

                Integer pendienteEntrega = totalEstudiantes - estudiantesEntrega;
                
                Double division = (double) estudiantesEntrega / listaEstudiantes.size();
                Double porcentajeEntrega = division * 100;

                DtoPorcentajeEntregaFotografias dtoPorcentajeEntregaFotografias = new DtoPorcentajeEntregaFotografias(programa, totalEstudiantes, estudiantesEntrega, pendienteEntrega, porcentajeEntrega);
                listaPorcentajeEntrega.add(dtoPorcentajeEntregaFotografias);
            });
            
            List<DtoPorcentajeEntregaFotografias> listaPorcentajeEntregaOrdenada = listaPorcentajeEntrega.stream().sorted(DtoPorcentajeEntregaFotografias::compareTo).collect(Collectors.toList());
                    
                    
            return ResultadoEJB.crearCorrecto(listaPorcentajeEntregaOrdenada, "Lista de porcentaje de entrega de fotografías por programa educativo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de porcentaje de entrega de fotografías por programa educativo. (EjbEstadiasServiciosEscolares.getListaPorcentajeEntregaFotografias)", e, null);
        }
    }
}
