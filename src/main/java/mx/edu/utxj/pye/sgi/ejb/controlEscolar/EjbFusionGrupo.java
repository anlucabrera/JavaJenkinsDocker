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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbFusionGrupo")
public class EjbFusionGrupo {
    
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbEventoEscolar ejbEventoEscolar;

    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
    
    /**
     * Permite validar el usuario autenticado si es como director de área académica 
     * @param clave Número de nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        return ejbValidacionRol.validarDirector(clave);
    }

    /**
     * Permite validar el usuario autenticado si es como encargado de área académica
     * @param clave Número de nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        return ejbValidacionRol.validarEncargadoDireccion(clave);
    }

    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo director){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.FUSION_GRUPOS, director);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para asignación académica por parte del director (EjbAsignacionAcademica.).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite validar el usuario autenticado si es como director de área
     * @param clave Número de nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarJefeServiciosEscolares(Integer clave){
        return ejbValidacionRol.validarJefeDepartamento(clave, 10);
    }

    /**
     * Permite validar el usuario autenticado si es como director de área
     * @param clave Número de nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoServiciosEscolares(Integer clave){
        return ejbValidacionRol.validarencargadoDepartamento(clave, 10);
    }

    public ResultadoEJB<Personal> getPersonalTutorGrupo(Grupo grupo){
        try {
            Personal tutor = em.createQuery("select p from Personal as p where p.clave = :clave", Personal.class)
                    .setParameter("clave", grupo.getTutor())
                    .getResultStream().findFirst().orElse(null);
            return ResultadoEJB.crearCorrecto(tutor, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }

    /**
     * Mapea el listado de programas educativos vigentes y los grupos deben de ordenarse por grado y literal
     * @param director 
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> getProgramaEducativo(PersonalActivo director, PeriodosEscolares periodo){
        try {
            return ejbAsignacionAcademica.getProgramasActivos(director, periodo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbFusionGrupo)", e, null);
        }
    }

    public ResultadoEJB<List<AreasUniversidad>> getDivisionesAcademica(PersonalActivo personalActivo){
        try{
            List<AreasUniversidad> divisiones =
                    em.createQuery("select a from AreasUniversidad as a " +
                            "where a.areaSuperior = :areaSuperior " +
                            "and (a.categoria.categoria = :categoria1 or a.categoria.categoria = :categoria2)" +
                            "and a.responsable = :clave", AreasUniversidad.class)
                            .setParameter("clave", personalActivo.getPersonal().getClave())
                            .setParameter("areaSuperior", Short.parseShort("2"))
                            .setParameter("categoria1", Short.parseShort("7"))
                            .setParameter("categoria2", Short.parseShort("8"))
                            .getResultStream().collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(divisiones, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }

    public ResultadoEJB<List<AreasUniversidad>> getAreasAcademica(){
        try{
            List<AreasUniversidad> divisiones =
                    em.createQuery("select a from AreasUniversidad as a " +
                            "where a.areaSuperior = :areaSuperior " +
                            "and (a.categoria.categoria = :categoria1 or a.categoria.categoria = :categoria2) " +
                            "order by a.area desc", AreasUniversidad.class)
                            .setParameter("areaSuperior", Short.parseShort("2"))
                            .setParameter("categoria1", Short.parseShort("7"))
                            .setParameter("categoria2", Short.parseShort("8"))
                            .getResultStream().collect(Collectors.toList());
            System.out.println("Divisiones:"+ divisiones);
            return ResultadoEJB.crearCorrecto(divisiones, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }

    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativos(AreasUniversidad areaUniversidad){
        try {
            List<AreasUniversidad> programas =
                    em.createQuery("select a from AreasUniversidad as a " +
                            "where a.areaSuperior = :divsion " +
                            "and a.vigente = :vigente " +
                            "ORDER BY a.nivelEducativo.nombre desc", AreasUniversidad.class)
                            .setParameter("vigente", String.valueOf(1))
                            .setParameter("divsion", areaUniversidad.getArea())
                            .getResultStream().collect(Collectors.toList());
            System.out.println("Programas educativos:"+programas);
            return ResultadoEJB.crearCorrecto(programas, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }

    public ResultadoEJB<List<Grupo>> getGrupos(AreasUniversidad areasUniversidad){
        try{
            List<Grupo> grupos = em.createQuery("select g from Grupo as g where g.idPe = :programa", Grupo.class)
                    .setParameter("programa", Short.parseShort(areasUniversidad.getArea().toString()))
                    .getResultStream().collect(Collectors.toList());
            System.out.println("Grupos:"+grupos);
            return ResultadoEJB.crearCorrecto(grupos, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }

    public ResultadoEJB<List<Estudiante>> getEstudiantesGrupoSeleccionado(Grupo grupo, AreasUniversidad areasUniversidad){
        try{
            List<Estudiante> estudiantes =
                    em.createQuery("select e from Estudiante as e " +
                            "where e.grupo.idGrupo = :grupo and e.grupo.idPe = :programa", Estudiante.class)
                            .setParameter("grupo", grupo.getIdGrupo())
                            .setParameter("programa", Short.parseShort(areasUniversidad.getArea().toString()))
                            .getResultStream()
                            .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(estudiantes, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }


    /**
     * Permite realizar el cambio de grupo de los estudiantes seleccionados
     * @param estudiantesCambio Lista de los estudiantes a cambiar
     * @param grupoDestino Grupo al cual se asignaran los estudiantes
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> reasignacionGrupo(Estudiante estudiantesCambio, Grupo grupoDestino, Operacion o){
        try {
            if(estudiantesCambio == null) return ResultadoEJB.crearErroneo(2, "El grupo no puede ser nulo.", Estudiante.class);
            if(grupoDestino == null) return ResultadoEJB.crearErroneo(3, "La operación no debe ser nula.", Estudiante.class);
            Estudiante e = estudiantesCambio;
            switch (o){
                case ACTUALIZAR:
                    e.setGrupo(grupoDestino);
                    em.merge(estudiantesCambio);
                    return ResultadoEJB.crearCorrecto(e, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
                case ELIMINAR:
                    if(!e.getAsistenciasacademicasList().isEmpty()){
                        e.getAsistenciasacademicasList().stream().filter(a -> a.getEstudiante().getIdEstudiante().equals(e.getIdEstudiante())).forEach(x -> {
                            em.remove(x);

                        });
                        return ResultadoEJB.crearCorrecto(e, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
                    }
                default:
                    return ResultadoEJB.crearErroneo(4, "Operación no autorizada.", Estudiante.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }
}
