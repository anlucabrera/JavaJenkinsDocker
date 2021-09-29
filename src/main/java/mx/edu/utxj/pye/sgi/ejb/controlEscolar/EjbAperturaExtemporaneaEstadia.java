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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAperturaExtemporaneaEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoEstadia;
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
@Stateless(name = "EjbAperturaExtemporaneaEstadia")
public class EjbAperturaExtemporaneaEstadia {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares, director(a) de carrera o encargado(a) de dirección
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarAcceso(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==18) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            }
            else if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==48) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            }
            else if (p.getPersonal().getAreaOperativa() == 10 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario tiene acceso a aperturas extemporáneas de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbAperturaExtemporaneaEstadia.validarAcceso)", e, null);
        }
    
    }
    
    /**
     * Permite identificar a una lista de posibles estudiante para asignar estadía, de la generación y nivel seleccionado, situación regular en el periodo actual
     * @param generacion
     * @param nivelEducativo
     * @param areaAcademica
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudianteAreaAcademica(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad areaAcademica, String pista){
        try{
            
            List<Short> programas = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior=:area AND a.nivelEducativo.nivel=:nivel AND a.vigente=:valor", AreasUniversidad.class)
                    .setParameter("area", areaAcademica.getArea())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .setParameter("valor", "1")
                    .getResultStream()
                    .map(p-> p.getArea())
                    .collect(Collectors.toList());
            
            Integer ultimoPeriodo = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.idPe IN :programas ORDER BY g.periodo DESC", Grupo.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("programas", programas)
                    .getResultStream()
                    .map(p-> p.getPeriodo())
                    .findFirst()
                    .orElse(null);
            
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p INNER JOIN e.grupo g WHERE g.generacion=:generacion AND e.carrera IN :programas AND e.tipoEstudiante.idTipoEstudiante=:tipo AND e.periodo=:periodo AND concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("programas", programas)
                    .setParameter("tipo", (short)1)
                    .setParameter("periodo", ultimoPeriodo)
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
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbAperturaExtemporaneaEstadia.buscarEstudianteAreaAcademica)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de actividades de eventos de estadía registrados de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventoEstadia>> getActividadesEventoEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            List<EventoEstadia> listaEventos = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.usuario=:usuario ORDER BY e.evento ASC",  EventoEstadia.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .setParameter("usuario", "Estudiante")
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaEventos, "Lista de actividades de eventos de estadía registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de actividades de eventos de estadía registrados. (EjbAperturaExtemporaneaEstadia.getActividadesEventoEstadia)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aperturas extemporáneas de eventos de estadía registradas
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAperturaExtemporaneaEstadia>> getListaAperturasExtemporaneas(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            List<AperturaExtemporaneaEventoEstadia> listaAperturas = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel ORDER BY a.evento.evento, a.fechaInicio ASC", AperturaExtemporaneaEventoEstadia.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<DtoAperturaExtemporaneaEstadia> listaAperturasExtemporaneas = new ArrayList<>();
            
            listaAperturas.forEach(apertura -> {
                Personal personalSolicito = em.find(Personal.class, apertura.getGrabaApertura());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, apertura.getSeguimiento().getEstudiante().getGrupo().getIdPe());
                
                DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia = new DtoAperturaExtemporaneaEstadia();
                dtoAperturaExtemporaneaEstadia.setAperturaExtemporanea(apertura);
                dtoAperturaExtemporaneaEstadia.setProgramaEducativo(programaEducativo);
                dtoAperturaExtemporaneaEstadia.setPersonalSolicita(personalSolicito);
                
                if(apertura.getPersonalValida()!= null){
                    Personal personalValido = em.find(Personal.class, apertura.getPersonalValida());
                    dtoAperturaExtemporaneaEstadia.setPersonalValido(personalValido);
                }
                listaAperturasExtemporaneas.add(dtoAperturaExtemporaneaEstadia);
            });
            
            return ResultadoEJB.crearCorrecto(listaAperturasExtemporaneas, "Lista de aperturas extemporáneas de eventos de estadía registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de aperturas extemporáneas de eventos de estadía registradas. (EjbAperturaExtemporaneaEstadia.getListaAperturasExtemporaneas)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aperturas extemporáneas de eventos de estadía registradas
     * @param generacion
     * @param nivelEducativo
     * @param areaAcademica
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAperturaExtemporaneaEstadia>> getListaAperturasExtemporaneasAreaAcademica(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad areaAcademica){
        try{
             List<Short> programas = em.createQuery("select a from AreasUniversidad a WHERE a.areaSuperior=:area AND a.nivelEducativo.nivel=:nivel", AreasUniversidad.class)
                    .setParameter("area", areaAcademica.getArea())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .map(p-> p.getArea())
                    .collect(Collectors.toList());
            
            List<AperturaExtemporaneaEventoEstadia> listaAperturas = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel AND a.seguimiento.estudiante.grupo.idPe IN :programas ORDER BY a.fechaInicio ASC", AperturaExtemporaneaEventoEstadia.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .setParameter("programas", programas)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
            
            List<DtoAperturaExtemporaneaEstadia> listaAperturasExtemporaneas = new ArrayList<>();
            
            listaAperturas.forEach(apertura -> {
                Personal personalSolicito = em.find(Personal.class, apertura.getGrabaApertura());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, apertura.getSeguimiento().getEstudiante().getGrupo().getIdPe());
                              
                DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia = new DtoAperturaExtemporaneaEstadia();
                dtoAperturaExtemporaneaEstadia.setAperturaExtemporanea(apertura);
                dtoAperturaExtemporaneaEstadia.setProgramaEducativo(programaEducativo);
                dtoAperturaExtemporaneaEstadia.setPersonalSolicita(personalSolicito);
                
                if(apertura.getPersonalValida()!= null){
                    Personal personalValido = em.find(Personal.class, apertura.getPersonalValida());
                    dtoAperturaExtemporaneaEstadia.setPersonalValido(personalValido);
                }
                listaAperturasExtemporaneas.add(dtoAperturaExtemporaneaEstadia);
            });
            
            return ResultadoEJB.crearCorrecto(listaAperturasExtemporaneas, "Lista de aperturas extemporáneas de eventos de estadía registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de aperturas extemporáneas de eventos de estadía registradas. (EjbAperturaExtemporaneaEstadia.getListaAperturasExtemporaneasAreaAcademica)", e, null);
        }
    }
    
     /**
     * Permite eliminar apertura extemporánea seleccionada
     * @param aperturaExtemporaneaEventoEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarAperturaExtemporanea(AperturaExtemporaneaEventoEstadia aperturaExtemporaneaEventoEstadia){
        try{
            
            Integer delete = em.createQuery("DELETE FROM AperturaExtemporaneaEventoEstadia a WHERE a.aperturaExtemporanea=:apertura", AperturaExtemporaneaEventoEstadia.class)
                .setParameter("apertura", aperturaExtemporaneaEventoEstadia.getAperturaExtemporanea())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se ha eliminado correctamente la apertura extemporánea seleccionada.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar correctamente la apertura extemporánea seleccionada. (EjbAperturaExtemporaneaEstadia.eliminarAperturaExtemporanea)", e, null);
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
    public ResultadoEJB<AperturaExtemporaneaEventoEstadia> guardarAperturaExtemporanea(EventoEstadia actividad, DtoDatosEstudiante estudiante, Date fechaInicio, Date fechaFin, Personal personal){
        try{
            SeguimientoEstadiaEstudiante seguimiento = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.estudiante.matricula=:matricula AND s.evento.generacion=:generacion AND s.evento.nivel=:nivel", SeguimientoEstadiaEstudiante.class)
                    .setParameter("matricula", estudiante.getEstudiante().getMatricula())
                    .setParameter("generacion", actividad.getGeneracion())
                    .setParameter("nivel", actividad.getNivel())
                    .getResultStream()
                    .findFirst().orElse(null);
           
            Date fechaFinCompleta = ejbPermisoAperturaExtemporanea.obtenerFechaFin(fechaFin);
            
            AperturaExtemporaneaEventoEstadia aperturaExtemporaneaEventoEstadia = new AperturaExtemporaneaEventoEstadia();
                aperturaExtemporaneaEventoEstadia.setEvento(actividad);
                aperturaExtemporaneaEventoEstadia.setSeguimiento(seguimiento);
                aperturaExtemporaneaEventoEstadia.setFechaInicio(fechaInicio);
                aperturaExtemporaneaEventoEstadia.setFechaFin(fechaFinCompleta);
                if(personal.getAreaOperativa()==10){
                    aperturaExtemporaneaEventoEstadia.setTipoApertura("Servicios Escolares");
                    aperturaExtemporaneaEventoEstadia.setGrabaApertura(personal.getClave());
                    aperturaExtemporaneaEventoEstadia.setPersonalValida(personal.getClave());
                    aperturaExtemporaneaEventoEstadia.setFechaValidacion(new Date());
                    aperturaExtemporaneaEventoEstadia.setValidada(Boolean.TRUE);
                }else{
                    aperturaExtemporaneaEventoEstadia.setTipoApertura("Dirección de carrera");
                    aperturaExtemporaneaEventoEstadia.setGrabaApertura(personal.getClave());
                    aperturaExtemporaneaEventoEstadia.setPersonalValida(null);
                    aperturaExtemporaneaEventoEstadia.setFechaValidacion(null);
                    aperturaExtemporaneaEventoEstadia.setValidada(Boolean.FALSE);
                }
                em.persist(aperturaExtemporaneaEventoEstadia);
                f.flush();
                
            return ResultadoEJB.crearCorrecto(aperturaExtemporaneaEventoEstadia, "Se ha registrado correctamente la apertura extemporánea del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la apertura extemporánea del estudiante seleccionado. (EjbAperturaExtemporaneaEstadia.guardarAperturaExtemporanea)", e, null);
        }
    }
   
     /**
     * Permite actualizar la apertura extemporánea de estadía seleccionada
     * @param dtoAperturaExtemporaneaEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<AperturaExtemporaneaEventoEstadia> actualizarAperturaExtemporanea(DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia){
        try{
            em.merge(dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea());
            em.flush();
           
            return ResultadoEJB.crearCorrecto(dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea(), "Se actualizó correctamente la apertura extemporánea de estadía seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la apertura extemporánea de estadía seleccionada. (EjbAperturaExtemporaneaEstadia.actualizarAperturaExtemporanea)", e, null);
        }
    }
    
     /**
     * Permite validar o invalidar apertura extemporánea seleccionada
     * @param dtoAperturaExtemporaneaEstadia
     * @param personal     
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> validarAperturaExtemporanea(DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia, Personal personal){
       try{
            String mensaje;
            Integer validar;
                if (dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea().getValidada()) {
                    validar = em.createQuery("UPDATE AperturaExtemporaneaEventoEstadia a set a.validada =:valor, a.fechaValidacion =:fecha, a.personalValida =:personal where a.aperturaExtemporanea =:apertura").setParameter("valor", Boolean.FALSE).setParameter("apertura", dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea().getAperturaExtemporanea()).setParameter("fecha", new Date()).setParameter("personal", personal.getClave())
                            .executeUpdate();
                    mensaje = "La apertura extemporánea se ha invalidado correctamente";
                } else {
                    validar = em.createQuery("UPDATE AperturaExtemporaneaEventoEstadia a set a.validada =:valor, a.fechaValidacion =:fecha, a.personalValida =:personal where a.aperturaExtemporanea =:apertura").setParameter("valor", Boolean.TRUE).setParameter("apertura", dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea().getAperturaExtemporanea()).setParameter("fecha", new Date()).setParameter("personal", personal.getClave())
                            .executeUpdate();
                    mensaje = "La apertura extemporánea se ha validado correctamente";
                }
                       
            return ResultadoEJB.crearCorrecto(validar, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar la apertura extemporánea seleccionada. (EjbAperturaExtemporaneaEstadia.validarAperturaExtemporanea)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aperturas extemporáneas registradas de una actividad del estudiante seleccionado
     * @param actividad
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> buscarAperturasRegistradasEvento(EventoEstadia actividad, DtoDatosEstudiante estudiante){
        try{
            List<AperturaExtemporaneaEventoEstadia> listaAperturaExtemporaneaEventoEstadia = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.seguimiento.estudiante.matricula=:matricula AND a.evento.evento=:evento", AperturaExtemporaneaEventoEstadia.class)
                    .setParameter("matricula", estudiante.getEstudiante().getMatricula())
                    .setParameter("evento", actividad.getEvento())
                    .getResultStream()
                    .collect(Collectors.toList());
                
            return ResultadoEJB.crearCorrecto(listaAperturaExtemporaneaEventoEstadia.size(), "Lista de aperturas extemporáneas registradas de una actividad del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener correctamente la lista de aperturas extemporáneas registradas de una actividad del estudiante seleccionado. (EjbAperturaExtemporaneaEstadia.buscarAperturasRegistradasEvento)", e, null);
        }
    }
    
}
