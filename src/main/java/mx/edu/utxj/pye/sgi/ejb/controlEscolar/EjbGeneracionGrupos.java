package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConteoGrupos;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.GeneracionGruposRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Stateless
public class EjbGeneracionGrupos {
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    @Getter @Setter private Integer grupo;
    @Getter @Setter private Boolean activo;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
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
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReincorporacion.validarServiciosEscolares)", e, null);
        }
    }

    /**
     * Permite verificar si hay un periodo abierto para reincoporaciones
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(){
        try{
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.GENEREACION_GRUPOS);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para generacion de grupos (EjbGeneracionGrupos.).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite obtener la lista de grupos disponibles de acuerdo al periodo seleccionado
     * @param cvePeriodo Variable del periodo a seleccionar
     * @return Lista de grupos.
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposPorPeriodo(Integer cvePeriodo){
        try{
            List<Grupo> g = em.createQuery("select g from Grupo g where g.periodo =:cvePeriodo", Grupo.class)
                    .setParameter("cvePeriodo", cvePeriodo).getResultStream().collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(g, "Grupos encontrados con éxito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron grupos para este periodo. (EjbGeneracionGrupos.obtenerGruposPorPeriodo())", e, null);
        }
    }

    public ResultadoEJB<List<PlanEstudio>> obtenerPlanesEstudioPorCarrera(Short pe){
        try{
            List<PlanEstudio> planEstudio = em.createQuery("SELECT p FROM PlanEstudio p WHERE p.idPe = :area AND p.estatus = :estatus", PlanEstudio.class)
                    .setParameter("area", pe).setParameter("estatus", true).getResultStream().collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(planEstudio, "Planes de estudio encontrados con éxito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron planes de estudio. (EjbGeneracionGrupos.obtenerPlanesEstudioPorCarrera())", e, null);
        }
    }

    public ResultadoEJB<List<AreasUniversidad>> obtenerAreasUniversidad(){
        try {
            List<AreasUniversidad> au = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.categoria.categoria = 9 AND a.vigente = '1' ORDER BY a.nombre",
                    AreasUniversidad.class).getResultStream().collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(au, "Areas encontradas con éxito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron areas para este periodo. (EjbGeneracionGrupos.obtenerAreasUniversidad())", e, null);
        }
    }

    public ResultadoEJB<Grupo> guardarGrupo(Grupo grupo, Integer periodoActivo, Integer noGrupos, PlanEstudio planEstudio, Generaciones generacion, Operacion operacion){
        try{
            if(grupo == null) return ResultadoEJB.crearErroneo(2, "El grupo no puede ser nulo.", Grupo.class);
            if(periodoActivo == null) return ResultadoEJB.crearErroneo(3, "El periodo no puede ser nulo.", Grupo.class);
            if(operacion == null) return ResultadoEJB.crearErroneo(4, "La operación no debe ser nula.", Grupo.class);
            Grupo g = em.createQuery("select g from Grupo as g where g.idGrupo = :grupo", Grupo.class)
                    .setParameter("grupo", grupo.getIdGrupo()).getResultStream().findFirst().orElse(null);
            Integer gruposReg = em.createQuery("SELECT g FROM Grupo g WHERE g.idPe = :id_Pe AND g.periodo = :idPeriodo AND g.grado = :grado")
                    .setParameter("id_Pe", grupo.getIdPe()).setParameter("idPeriodo", periodoActivo).setParameter("grado", grupo.getGrado()).getResultList().size();
            switch (operacion){
                case PERSISTIR:
                    if(g == null){
                        Integer noAcumulado = gruposReg + noGrupos;
                        Character[] abecedario = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','U','V','W','X','Y','Z'};
                        for(int i = gruposReg; i < noAcumulado;i++) {
                            g = grupo;
                            g.setPeriodo(periodoActivo);
                            g.setLiteral((abecedario[i]));
                            g.setCapMaxima(noGrupos);
                            g.setPlan(planEstudio);
                            g.setGeneracion(generacion.getGeneracion());
                            em.persist(g);
                            return ResultadoEJB.crearCorrecto(g, "El grupo ha sido agregado correctamente");
                        }
                    }else{
                        return ResultadoEJB.crearErroneo(5, "No se pudo realizar el registro", Grupo.class);
                    }
                case ACTUALIZAR:
                    em.merge(grupo);
                    return ResultadoEJB.crearCorrecto(null, "Los datos de grupo se han actualizado correctamente.");
                case ELIMINAR:
                    List<CargaAcademica> cg = em.createQuery("select c from CargaAcademica as c where c.cveGrupo.idGrupo = :grupo", CargaAcademica.class)
                            .setParameter("grupo", grupo.getIdGrupo()).getResultStream().collect(Collectors.toList());
                    if(cg.isEmpty()){
                        em.remove(grupo);
                        return ResultadoEJB.crearCorrecto(null, "Los datos de grupo se han elminado correctamente");
                    }else {
                        return ResultadoEJB.crearErroneo(6, "No es posible eliminar la información seleccionada", Grupo.class);
                    }

                default:
                    return ResultadoEJB.crearErroneo(6, "Operación no autorizada.", Grupo.class);

            }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo. (EjbGeneracionGrupos.guardarGrupo())", e, null);
        }
    }

    public ResultadoEJB<Grupo> eliminarGrupo(Grupo g, Operacion o){
        try {
            if(g == null) return ResultadoEJB.crearErroneo(2, "El grupo no puede ser nulo.", Grupo.class);
            if(o == null) return ResultadoEJB.crearErroneo(3, "La operación no debe ser nula.", Grupo.class);
            List<CargaAcademica> cg = em.createQuery("select c from CargaAcademica as c where c.cveGrupo.idGrupo =:grupo", CargaAcademica.class)
                    .setParameter("grupo", g.getIdGrupo()).getResultStream().collect(Collectors.toList());

            switch (o){
                case ELIMINAR:
                    if(cg.isEmpty()){
                        em.remove(g);
                        return ResultadoEJB.crearCorrecto(null,"Se ha eliminado correctamente");
                    }else{
                        return ResultadoEJB.crearErroneo(4, "No se pudo eliminar la información", Grupo.class);
                    }
                default:
                    return ResultadoEJB.crearErroneo(5, "Operación no autorizada.", Grupo.class);
            }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el grupo. (EjbGeneracionGrupos.eliminarGrupo())", e, null);
        }
    }

    public ResultadoEJB<Generaciones> obtenerGeneraciones(){
        try {
            Generaciones g = em.createQuery("select g from Generaciones as g where :anio between g.inicio and g.fin order by g.generacion desc", Generaciones.class)
                    .setParameter("anio", 2019).getResultStream().findFirst().orElse(null);
            return ResultadoEJB.crearCorrecto(g, "Generaciones encontradas con éxito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron areas para este periodo. (EjbGeneracionGrupos.obtenerAreasUniversidad())", e, null);
        }
    }

    public ResultadoEJB<List<DtoAlerta>> identificarAlertas(GeneracionGruposRolServiciosEscolares rol){
        try {
            if (!rol.getGrupos().isEmpty()) {
                final List<DtoAlerta> mensajes = new ArrayList<>();
                List<Grupo> g = em.createQuery("select g from Grupo as g group by g.grado, g.literal, g.idPe having count(g.literal)>1",Grupo.class)
                        .getResultStream().collect(Collectors.toList());
                g.stream().forEach(x -> {
                    mensajes.add(new DtoAlerta(String.format("Existe un duplicado del grupo %s, en el programa educativo %s. Realizar las acciones correspondientes.",
                            x.getGrado() + "° " + x.getLiteral(), x.getPlan().getDescripcion()), AlertaTipo.ERROR));
                });
                /*List<Grupo> g1 = em.createQuery("select g from Grupo as g", Grupo.class).getResultStream().collect(Collectors.toList());
                List<Grupo> g2 = em.createQuery("select g from Grupo as g group by g.idPe, g.idSistema.idSistema", Grupo.class).getResultStream().collect(Collectors.toList());
                List<AreasUniversidad> au = em.createQuery("select a from AreasUniversidad as a", AreasUniversidad.class).getResultStream().collect(Collectors.toList());
                List<DatosAcademicos> da = em.createQuery("select d from DatosAcademicos as d", DatosAcademicos.class).getResultStream().collect(Collectors.toList());
                g2.stream().forEach(x -> {
                    au.stream().filter(c -> c.getArea().equals(x.getIdPe())).forEach(y -> {
                        Integer gruposExistentes = (int)g1.stream().filter(a -> a.getIdPe() == x.getIdPe() && a.getIdSistema().equals(x.getIdSistema())).count();
                        Integer gruposSugeridos  = (int)da.stream().filter(b -> b.getPrimeraOpcion() == x.getIdPe() && b.getSistemaPrimeraOpcion().equals(x.getIdSistema())).count();
                        if(gruposExistentes > gruposSugeridos){
                            mensajes.add(new DtoAlerta(
                                    String.format("Ha sobrepasado el número de grupos en el programa educativo %s, de acuerdo a los sugeridos.", x.getPlan().getDescripcion()), AlertaTipo.SUGERENCIA));
                        }
                    });
                });*/
                mensajes.stream().collect(Collectors.toSet());
                return ResultadoEJB.crearCorrecto(mensajes, "Lista de mensajes");
            } else {
                return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Sin mensajes");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron identificar mensajes de asignación (EjbAsignacionTutores.identificarMensajes)", e, null);
        }
    }

    public ResultadoEJB<List<DtoConteoGrupos>> obtenerSugerenciaGeneracionGrupos(){
        try {
            List<DtoConteoGrupos> listaSugerencias = new ArrayList<>();
            List<DatosAcademicos> da = em.createQuery("select d from DatosAcademicos as d", DatosAcademicos.class).getResultStream().collect(Collectors.toList());
            List<AreasUniversidad> au = em.createQuery("select a from AreasUniversidad as a", AreasUniversidad.class).getResultStream().collect(Collectors.toList());
            List<Grupo> g = em.createQuery("select g from Grupo as g", Grupo.class).getResultStream().collect(Collectors.toList());
            List<Grupo> g1 = em.createQuery("select g from Grupo as g group by g.idPe, g.idSistema", Grupo.class).getResultStream().collect(Collectors.toList());
            g1.stream().forEach(x -> {
                au.stream().filter(a -> a.getArea().equals(x.getIdPe())).forEach(y -> {
                    String siglas = y.getSiglas();
                    String pe = y.getNombre();
                    Integer totalAspirante = (int)da.stream().filter(b -> b.getPrimeraOpcion() == x.getIdPe() && b.getSistemaPrimeraOpcion().equals(x.getIdSistema())).count();
                    Integer grupos = (totalAspirante / 30)+1;
                    Integer gruposExistentes = (int)g.stream().filter(c -> c.getIdPe() == x.getIdPe() && c.getIdSistema().equals(x.getIdSistema())).count();
                    String sistema = x.getIdSistema().getNombre();
                    listaSugerencias.add(new DtoConteoGrupos(siglas, pe, totalAspirante, grupos, gruposExistentes, sistema));
                });
            });
            return ResultadoEJB.crearCorrecto(listaSugerencias, "Sugerencias realizadas con éxito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron areas para este periodo. (EjbGeneracionGrupos.obtenerSugerenciaGeneracionGrupos())", e, null);
        }
    }

}
