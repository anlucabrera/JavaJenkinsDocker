package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionAcademicaRolDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateria;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.DateUtils;
import mx.edu.utxj.pye.sgi.util.NumberUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionAcademicaRolEscolares;

@Stateless(name = "EjbAsignacionAcademica")
public class EjbAsignacionAcademica {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbValidacionRol ejbValidacionRol;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        return ejbValidacionRol.validarDirector(clave);
    }

    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        return ejbValidacionRol.validarEncargadoDireccion(clave);
    }
    
    /**
     * Permite verificar si hay un periodo abierto para asignación docente
     * @param director Director que va a realizar asignación académica, permite funcionar como filtro en caso se un permiso especifico a su area o a su clave
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo director){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CARGA_ACADEMICA, director);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para asignación académica por parte del director (EjbAsignacionAcademica.).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite identificar a una lista de posibles docentes para asignar la materia
     * @param pista Contenido que la vista que puede incluir parte del nombre, nùmero de nómina o área operativa del docente que se busca
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<PersonalActivo>> buscarDocente(String pista){
        try{
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<PersonalActivo> docentes = em.createQuery("select p from Personal p where p.estado <> 'B' and concat(p.nombre, p.clave) like concat('%',:pista,'%')  ", Personal.class)
                    .setParameter("pista", pista)
                    .getResultStream()
                    .map(p -> ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(docentes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de docentes activos. (EjbAsignacionAcademica.buscarDocente)", e, null);
        }
    }

    /**
     * Permite obtener la lista de periodos escolares a elegir al realizar la asignación docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            //buscar lista de periodos escolares ordenados de forma descendente para que al elegir un periodo en la asignación docente aparezca primero el mas actual
            final List<PeriodosEscolares> periodos = em.createQuery("select ee from EventoEscolar ee where ee.tipo = :tipo order by ee.periodo desc", EventoEscolar.class)
                    .setParameter("tipo", EventoEscolarTipo.CARGA_ACADEMICA.getLabel())
                    .getResultStream()
                    .map(eventoEscolar -> em.find(PeriodosEscolares.class, eventoEscolar.getPeriodo()))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
//            System.out.println("periodos = " + periodos);
//            System.out.println("EventoEscolarTipo.CARGA_ACADEMICA.getLabel() = " + EventoEscolarTipo.CARGA_ACADEMICA.getLabel());
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionAcademica.getPeriodosDescendentes)", e, null);
        }
    }

    /**
     * Permite obtener la lista de programas educativos que tienen planes de estudio vigentes, los programas deben ordenarse por
     * área, nivel y nombre y los grupos por grado y letra
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> getProgramasActivos(PersonalActivo director, PeriodosEscolares periodo){
        try{
            //Map<AreasUniversidad, List<Grupo>> programasMap = Collections.EMPTY_MAP;
            // buscar lista de programas educativos con plan de estudios vigentes y despues mapear cada programa con su lista de grupos
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);
//            System.out.println("programaEducativoCategoria = " + programaEducativoCategoria);
            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' and a.nivelEducativo.nivel = :nivel order by a.nombre", AreasUniversidad.class)
                    .setParameter("areaPoa", director.getAreaPOA().getArea())
                    .setParameter("categoria", programaEducativoCategoria)
                    .setParameter("nivel", "TSU")
                    .getResultList();
//            System.out.println("programas = " + programas);
            Map<AreasUniversidad, List<Grupo>> programasMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarGrupos(programa, periodo)));
//            System.out.println("programasMap = " + programasMap);
            return ResultadoEJB.crearCorrecto(programasMap, "Mapa de programas y grupos");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbAsignacionAcademica.getProgramasActivos)", e, null);
        }
    }

    private List<Grupo> generarGrupos(AreasUniversidad programa, PeriodosEscolares periodo) {
//        System.out.println("programa = [" + programa + "], periodo = [" + periodo + "]");
        return em.createQuery("select g from Grupo g where g.idPe=:programa and g.periodo=:periodo", Grupo.class)
                .setParameter("programa", programa.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
    }

    /**
     * Permite obtener la lista de materias en un grupo correspondiente
     * @param programa Programa al que deben pertenecer las materias
     * @param grupo Grupo al que deben pertenecer las materias
     * @param periodoSeleccionado Periodo seleccionado en pantalla
     * @param periodoActivo Periodo activo segun el evento aperturado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoMateria>> getMaterias(AreasUniversidad programa, Grupo grupo, PeriodosEscolares periodoSeleccionado, Integer periodoActivo){
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            if(Objects.equals(periodoSeleccionado.getPeriodo(), periodoActivo)){
                //mostrar las materias que son candidatas  a asiganar segun el programa y el grado
                List<DtoMateria> materias = em.createQuery("select m from Materia m inner join m.planEstudioMateriaList pem where pem.idPlan.idPe=:programaEducativo and pem.grado=:grado", Materia.class)
                        .setParameter("programaEducativo", programa.getArea())
                        .setParameter("grado", grupo.getGrado())
                        .getResultStream()
                        .map(materia -> packMateria(grupo, materia, programa).getValor())
                        .filter(dtoMateria -> dtoMateria != null)
                        .filter(dtoMateria -> dtoMateria.getDtoCargaAcademica()!=null?dtoMateria.getDtoCargaAcademica().getGrupo().equals(grupo):true) //si tiene carga académica solo se filtrar las del grupo seleccionado
                        .collect(Collectors.toList());
                return ResultadoEJB.crearCorrecto(materias, "Lista de materias por grupo, grado, programa y periodo activo.");
            }else{
                //mostrar las materia que fueron asignadas en el periodo seleccionado, segun el programa y grado seleccionados
                List<DtoMateria> materias = em.createQuery("select m from Materia m inner join m.planEstudioMateriaList pem inner join pem.idPlan p inner join pem.cargaAcademicaList ca where p.idPe=:programaEducativo and pem.grado=:grado and ca.evento.periodo=:periodo", Materia.class)
                        .setParameter("programaEducativo", programa.getArea())
                        .setParameter("grado", grupo.getGrado())
                        .setParameter("periodo", periodoSeleccionado.getPeriodo())
                        .getResultStream()
                        .map(materia -> packMateria(grupo, materia, programa).getValor())
                        .filter(dtoMateria -> dtoMateria != null)
                        .filter(dtoMateria -> filtrarPeriodoGrupo(dtoMateria, periodoActivo, grupo)) //si tiene carga académica solo se filtrar las del grupo seleccionado
                        .collect(Collectors.toList());
                return ResultadoEJB.crearCorrecto(materias, "Lista de materias por grupo y programa");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias sin asignar por grupo y programa. (EjbAsignacionAcademica.getMaterias)", e, null);
        }
    }

    private Boolean filtrarPeriodoGrupo(DtoMateria dtoMateria, Integer periodoActivo, Grupo grupo){
        if(dtoMateria == null) return false;
        if(grupo == null) return false;
        if(dtoMateria.getDtoCargaAcademica() != null){
            if(!dtoMateria.getDtoCargaAcademica().getGrupo().equals(grupo) || !dtoMateria.getDtoCargaAcademica().getPeriodo().getPeriodo().equals(periodoActivo)) return false;
        }

        return true;
    }

    /**
     * Permite la asignación de carga académica a un docente para que imparta una materia a un grupo de un cierto periodo escolar
     * @param materia Materia que se va a asignar
     * @param docente Docente que impartirá la materia
     * @param grupo Grupo a quien se impartirá la materia
     * @param periodo Periodo en el que se impartirá la materia, si bien el grupo ya tiene la referencia necesaria, se utililizará como un regla de
     *                validación para asegurar que esta referencia y la del grupo coinciden
     * @param operacion Operación a realizar, se permite persistir y eliminar
     * @return Resultado del proceso generando la instancia de carga académica obtenida
     */
    public ResultadoEJB<CargaAcademica> asignarMateriaDocente(Materia materia, PersonalActivo docente, Grupo grupo, PeriodosEscolares periodo, AreasUniversidad programa, EventoEscolar evento, Operacion operacion){
        try{
            if(materia == null) return ResultadoEJB.crearErroneo(3, "La materia no debe ser nula.", CargaAcademica.class);
            if(grupo == null) return ResultadoEJB.crearErroneo(4, "El grupo no debe ser nulo.", CargaAcademica.class);
            if(periodo == null) return ResultadoEJB.crearErroneo(5, "El periodo no debe ser nulo.", CargaAcademica.class);
            if(docente == null) return ResultadoEJB.crearErroneo(6, "El docente no debe ser nulo.", CargaAcademica.class);

//            CargaAcademicaPK pk = new CargaAcademicaPK(grupo.getIdGrupo(), materia.getIdMateria(), docente.getPersonal().getClave());
            CargaAcademica cargaAcademica = em.createQuery("select ca from CargaAcademica ca inner join ca.idPlanMateria pm inner join pm.idMateria mat inner join pm.idPlan plan inner join ca.cveGrupo gru where gru.idGrupo=:grupo and mat.idMateria=:materia and plan.idPe=:programa and gru.grado=pm.grado", CargaAcademica.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .setParameter("materia", materia.getIdMateria())
                    .setParameter("programa", programa.getArea())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            //em.find(CargaAcademica.class, pk);

            switch (operacion){
                case PERSISTIR:
                    //crear la carga académica y persistirla
                    if(cargaAcademica == null){//comprobar si la asignación existe para impedir  duplicar
                        ResultadoEJB<PlanEstudioMateria> resPlanEstudioMateria = getPlanEstudioMateria(programa, materia, grupo.getGrado());
                        if(!resPlanEstudioMateria.getCorrecto()) return ResultadoEJB.crearErroneo(10, cargaAcademica, resPlanEstudioMateria.getMensaje());
                        cargaAcademica = new CargaAcademica();
//                        cargaAcademica .setCargaAcademicaPK(pk);
                        cargaAcademica.setCveGrupo(grupo);
                        cargaAcademica.setIdPlanMateria(resPlanEstudioMateria.getValor());
                        cargaAcademica.setEvento(evento);
                        cargaAcademica.setDocente(docente.getPersonal().getClave());
                        ResultadoEJB<Integer> resTotalHorasSugeridasPorSemana = getTotalHorasSugeridasPorSemana(materia, periodo);
//                        System.out.println("resTotalHorasSugeridasPorSemana = " + resTotalHorasSugeridasPorSemana);
                        if(resTotalHorasSugeridasPorSemana.getCorrecto()){
                            cargaAcademica.setHorasSemana(resTotalHorasSugeridasPorSemana.getValor());
                            em.persist(cargaAcademica);
                            return ResultadoEJB.crearCorrecto(cargaAcademica, "La asignación fué registrada correctamente.");
                        }else return ResultadoEJB.crearErroneo(8, "No se pudo calcular el total de horas por semana para la materia que se está asignando. ".concat(resTotalHorasSugeridasPorSemana.getMensaje()), CargaAcademica.class);
                    }else {//si ya existe se informa
                        PersonalActivo docente1 = ejbPersonalBean.pack(cargaAcademica.getDocente());
                        return ResultadoEJB.crearErroneo(7, String.format("La materia %s ya fue asignada al docente %s, en el grupo %s.",materia.getNombre(), docente1.getPersonal().getNombre(), "".concat(String.valueOf(grupo.getGrado())).concat(String.valueOf(grupo.getLiteral()))), CargaAcademica.class);
                    }
                case ELIMINAR:
                    //eliminar la carga académica
                    if(cargaAcademica != null){
                        f.remove(cargaAcademica);
                        return ResultadoEJB.crearCorrecto(null, "La asignación fue eliminada correctamente");
                    }else{
                        return ResultadoEJB.crearErroneo(9, "La asignación ya había sido eliminada.", CargaAcademica.class);
                    }
                    default:
                        return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", CargaAcademica.class);
            }

        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la carga académica. (EjbAsignacionAcademica.asignarMateriaDocente)", e, null);
        }
    }

    /**
     * Permite cambiar el docente de una asignación académica
     * @param dtoMateria DTO que se selecciona en pantalla y que contiene la asignación a cambiar el docente
     * @param docenteNuevo Docente seleccionado en pantalla y a quien será registrado en la asignación
     * @return Carga académica actualizada
     */
    public ResultadoEJB<CargaAcademica> actualizarDocenteEnAsignacion(DtoMateria dtoMateria, PersonalActivo docenteNuevo){
        try{
            if(dtoMateria.getDtoCargaAcademica() == null) return ResultadoEJB.crearErroneo(2, "La materia no ha sido asignada, por lo cual no puede ser cambiado el docente a una asignación que no existe.", CargaAcademica.class);
             Integer  t = em.createQuery("update CargaAcademica ca set ca.docente=:docente where ca.carga=:carga")
                    .setParameter("docente", docenteNuevo.getPersonal().getClave())
                    .setParameter("carga", dtoMateria.getDtoCargaAcademica().getCargaAcademica().getCarga())
                    .executeUpdate();
//            System.out.println("t = " + t);
            if(t > 0){
//                CargaAcademicaPK pk = new CargaAcademicaPK(dtoMateria.getDtoCargaAcademica().getCargaAcademica().getCargaAcademicaPK().getCveGrupo(), dtoMateria.getDtoCargaAcademica().getCargaAcademica().getCargaAcademicaPK().getCveMateria(), docenteNuevo.getPersonal().getClave());
                CargaAcademica cargaAcademica = em.find(CargaAcademica.class, dtoMateria.getDtoCargaAcademica().getCargaAcademica().getCarga());
//                System.out.println("cargaAcademica = " + cargaAcademica);
                return ResultadoEJB.crearCorrecto(cargaAcademica, "La asignación fue actualizada correctamente al docente seleccionado");
            }else{
                return ResultadoEJB.crearErroneo(3, "No se pudo realizar la actualización del docente en la asignación académica.", CargaAcademica.class);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el docente en la carga académica. (EjbAsignacionAcademica.actualizarDocenteEnAsignacion)", e, null);
        }
    }

    /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo Periodo seleccionado en pantalla
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicaPorDocente(PersonalActivo docente, PeriodosEscolares periodo){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<DtoCargaAcademica> cargas = em.createQuery("select c from CargaAcademica c inner join c.cveGrupo g inner join c.idPlanMateria pem inner join pem.idMateria mat inner join pem.idPlan plan where c.docente=:docente and g.periodo=:periodo order by g.idPe, g.grado, g.literal, mat.nombre", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbAsignacionAcademica.getCargaAcademicaPorDocente)", e, null);
        }
    }

    /**
     * Permite obtener el total de horas frente de a grupo de una lista de cargas académicas
     * @param cargas Lista de cargas académicas a utilizar para realizar el cálculo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> getTotalHorasFrenteAGrupo(List<DtoCargaAcademica> cargas){
        try{
            //calcular el total de horas frente a grupo asignadas
            Integer totalHorasPorSemana = cargas.stream()
                    .map(DtoCargaAcademica::getCargaAcademica)
                    .mapToInt(ca -> ca.getHorasSemana().intValue())
                    .sum();
            return ResultadoEJB.crearCorrecto(totalHorasPorSemana, "Horas frente a grupo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el total de horas frente a grupo. (EjbAsignacionAcademica.getTotalHorasFrenteAGrupo)", e, Integer.class);
        }
    }

    /**
     * Permite calcular el total de horas a la semana sugeridas de una materia al intentar asignarla a un docente en un grupo específico, se calcula en base al cociente
     * del total de horas de las asignatura y las semanas del cuatrimestre.
     * @param materia Materia a la que se le realiza el cáculo
     * @param periodo Periodos en el que se realiza el cálculo
     * @return Total de horas por semana sugeridas
     */
    public  ResultadoEJB<Integer> getTotalHorasSugeridasPorSemana(Materia materia, PeriodosEscolares periodo){
        try{
            //calcular el total de horas frente a grupo asignadas
            PeriodoEscolarFechas periodoEscolarFechas = periodo.getPeriodoEscolarFechas();
            if(periodoEscolarFechas == null)//comprobar si el periodo no está ligado a sus fechas, se intenta obtenerlo de la base de datos
                periodoEscolarFechas = em.find(PeriodoEscolarFechas.class, periodo.getPeriodo());

//            System.out.println("periodoEscolarFechas = " + periodoEscolarFechas);

            if(periodoEscolarFechas == null)//si aun despues de intentar ligar las fechas sigue siendo nulo, se informa del error para que el usuario reporte la inexistencia de la información
                return ResultadoEJB.crearErroneo(2, String.format("El periodo escolar %s no tiene fechas de inicio y fin asociadas.", String.valueOf(periodo.getPeriodo())), Integer.class);

            List<UnidadMateria> unidadMateriaList = em.createQuery("select um from UnidadMateria um where um.idMateria=:materia", UnidadMateria.class)
                    .setParameter("materia", materia)
                    .getResultList();
            if(unidadMateriaList.size() == 0) return ResultadoEJB.crearErroneo(2, "La materia que se intenta asignar no tiene unidades registradas.", Integer.class);
//            System.out.println("materia.getUnidadMateriaList() = " + materia.getUnidadMateriaList());
            Integer totalHorasAsignadas = unidadMateriaList.stream()
                    .mapToInt(um -> um.getHorasPracticas()+ um.getHorasTeoricas())
                    .sum();
//            System.out.println("totalHorasAsignadas = " + totalHorasAsignadas);
//            unidadMateriaList.forEach(unidadMateria -> {
//                System.out.println("unidadMateria.getHorasTeoricas() = " + unidadMateria.getHorasTeoricas());
//                System.out.println("unidadMateria.getHorasPracticas() = " + unidadMateria.getHorasPracticas());
//            });
            Long semanas = DateUtils.toWeeks(periodoEscolarFechas.getInicio(), periodoEscolarFechas.getFin()) + 1L;
//            System.out.println("semanas = " + semanas);

            BigDecimal semanasBD = BigDecimal.valueOf(semanas);
//            System.out.println("semanasBD = " + semanasBD);
            BigDecimal horasBD = BigDecimal.valueOf(totalHorasAsignadas);
//            System.out.println("horasBD = " + horasBD);
            BigDecimal cociente = horasBD.divide(semanasBD,8,RoundingMode.HALF_UP);
//            System.out.println("cociente = " + cociente);
            BigDecimal horas = NumberUtils.redondear(cociente, 0);
//            System.out.println("horas = " + horas);

            return ResultadoEJB.crearCorrecto(horas.intValue(), "Horas por semana sugeridas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el total de horas sugeridas por semana. (EjbAsignacionAcademica.getTotalHorasSugeridasPorSemana)", e, Integer.class);
        }
    }

    /**
     * Empaqueta una carga académica en su DTO Wrapper
     * @param cargaAcademica Carga académica que se va a empaquetar
     * @return Carga académica empaquetada
     */
    public ResultadoEJB<DtoCargaAcademica> pack(CargaAcademica cargaAcademica){
//        System.out.println("cargaAcademica = " + cargaAcademica);
        try{
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una carga académica nula.", DtoCargaAcademica.class);
            if(cargaAcademica.getCarga() == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar una carga académica con clave nula.", DtoCargaAcademica.class);

            CargaAcademica cargaAcademicaBD = em.find(CargaAcademica.class, cargaAcademica.getCarga());
            if(cargaAcademicaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar una carga académica no registrada previamente en base de datos.", DtoCargaAcademica.class);

            Grupo grupo = em.find(Grupo.class, cargaAcademicaBD.getCveGrupo().getIdGrupo());
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, grupo.getPeriodo());
            PlanEstudioMateria planEstudioMateria = cargaAcademicaBD.getIdPlanMateria();
            PlanEstudio planEstudio = planEstudioMateria.getIdPlan();
            Materia materia = em.find(Materia.class, planEstudioMateria.getIdMateria().getIdMateria());
            PersonalActivo docente = ejbPersonalBean.pack(cargaAcademicaBD.getDocente());
            AreasUniversidad programa = em.find(AreasUniversidad.class, planEstudio.getIdPe());
            DtoCargaAcademica dto = new DtoCargaAcademica(cargaAcademicaBD, periodo, docente, grupo, materia, programa, planEstudio, planEstudioMateria);

            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoCargaAcademica.class);
        }
    }

    /**
     * Permite engrapar una carga académica mediante los valores de su llave primaria.
     * @param grupo Valor de la clave del grupo.
     * @param materia Valor de la clave de la materia.
     * @param docente Valor de la clave del docente.
     * @return DTO wrapper de la carga académica o null si no existe.
     */
    public ResultadoEJB<DtoCargaAcademica> pack(Integer grupo, Integer materia, Integer docente){
        try{
            CargaAcademica cargaAcademica = em.createQuery("select ca from CargaAcademica  ca where ca.cveGrupo.idGrupo=:grupo and ca.idPlanMateria.idMateria.idMateria=:materia and ca.docente=:docente", CargaAcademica.class)
                    .setParameter("grupo", grupo)
                    .setParameter("materia", materia)
                    .setParameter("docente", docente)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
//            CargaAcademicaPK pk = new CargaAcademicaPK(grupo, materia, docente);
            return pack(cargaAcademica); //pack(em.find(CargaAcademica.class, pk));
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica con claves (EjbAsignacionAcademica. pack).", e, DtoCargaAcademica.class);
        }
    }

    /**
     * Permite engrapar en un DTO una materia a través de la clave del grupo y la instancia de la materia, agregando la asignación de materia en el grupo si es que ya existe.
     * @param grupo Clave del grupo del cual se busca la asignación de la materia.
     * @param materia Instancia del entity de la materia.
     * @return DTO wrapper de la materia con carga académica en el grupo correspondiente si es que existe.
     */
    public  ResultadoEJB<DtoMateria> packMateria(Grupo grupo, Materia materia, AreasUniversidad programa){
//        System.out.println("grupo = [" + grupo + "], materia = [" + materia + "], programa = [" + programa + "]");
        try{
            ResultadoEJB<PlanEstudioMateria> resPlanEstudioMateria = getPlanEstudioMateria(programa, materia, grupo.getGrado());
//            System.out.println("resPlanEstudioMateria = " + resPlanEstudioMateria);
            if(!resPlanEstudioMateria.getCorrecto()) return ResultadoEJB.crearCorrecto(new DtoMateria(materia, null, 0), resPlanEstudioMateria.getMensaje());
            CargaAcademica cargaAcademica = em.createQuery("select ca from CargaAcademica ca where ca.idPlanMateria=:planMateria and ca.cveGrupo=:grupo", CargaAcademica.class)
                    .setParameter("planMateria", resPlanEstudioMateria.getValor())
                    .setParameter("grupo", grupo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
//            System.out.println("cargaAcademica = " + cargaAcademica);
            if(cargaAcademica != null){
                ResultadoEJB<DtoCargaAcademica> resCarga = pack(cargaAcademica);
//                System.out.println("resCarga = " + resCarga);
                DtoMateria dto = new DtoMateria(materia, resCarga.getValor(), cargaAcademica.getHorasSemana());
//                System.out.println("dto correcto = " + dto);
//                System.out.println("dto.getDtoCargaAcademica() = " + dto.getDtoCargaAcademica());
                return ResultadoEJB.crearCorrecto(dto, "Materia empaquetada");
            }else{
                DtoMateria dto = new DtoMateria(materia, null, 0);
//                System.out.println("dto error = " + dto);
                return ResultadoEJB.crearCorrecto(dto, "Se empaquetó la materia pero no ha sido asignada.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la materia (EjbAsignacionAcademica.packMateria).", e, DtoMateria.class);
        }
    }

    /**
     * Permite detectar una lista de mensajes de posibles errores en la asignación docente, como es el caso de superar las horas máximas frente a grupo de acuerdo a si es PTC o PA el docente o si se han asignado
     * mas de una materia al mismo docente en un grupo detemrinado
     * @param rol DTO de la capa de sincronización con los datos seleccionados por el usuario
     * @return Lista de mensajes encontrados.
     */
    public ResultadoEJB<List<DtoAlerta>> identificarMensajes(AsignacionAcademicaRolDirector rol){
        try{
//            System.out.println("(rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getPrograma() != null && rol.getGrupo() != null) = " + (rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getPrograma() != null && rol.getGrupo() != null));
//            System.out.println("rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) = " + (rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo())));
//            System.out.println("rol.getPrograma() = " + rol.getPrograma());
//            System.out.println("rol.getGrupo() = " + rol.getGrupo());
            if(rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getPrograma() != null && rol.getGrupo() != null){
                final Short asignacionPTCCategoriaOficial = (short)ep.leerPropiedadEntera("asignacionPTCCategoriaOficial").orElse(32);
                final Short asignacionPACategoriaOficial = (short)ep.leerPropiedadEntera("asignacionPACategoriaOficial").orElse(30);
                final Integer asignacionPAHorasClaseMaximo = ep.leerPropiedadEntera("asignacionPAHorasClaseMaximo").orElse(30);
                final Integer asignacionPTCHorasClaseMaximo = ep.leerPropiedadEntera("asignacionPTCHorasClaseMaximo").orElse(22);
                final List<DtoAlerta> mensajes = new ArrayList<>();

                ResultadoEJB<List<DtoMateria>> resMaterias = getMaterias(rol.getPrograma(), rol.getGrupo(), rol.getPeriodo(), rol.getPeriodoActivo());
//                System.out.println("resMaterias = " + resMaterias);
                if(resMaterias.getCorrecto()){
                    List<PersonalActivo> docentes = resMaterias.getValor().stream()
                            .filter(dtoMateria -> dtoMateria.getDtoCargaAcademica() != null)
                            .map(DtoMateria::getDtoCargaAcademica)
                            .map(DtoCargaAcademica::getDocente)
                            .distinct()
                            .collect(Collectors.toList());

                    docentes.forEach(docente -> {
//                        System.out.println("docente = " + docente.getPersonal().getNombre());
                        ResultadoEJB<List<DtoCargaAcademica>> resCargaAcademicaPorDocente = getCargaAcademicaPorDocente(docente, rol.getPeriodo());
//                        System.out.println("resCargaAcademicaPorDocente = " + resCargaAcademicaPorDocente);
                        ResultadoEJB<Integer> resTotalHorasFrenteAGrupo = getTotalHorasFrenteAGrupo(resCargaAcademicaPorDocente.getCorrecto()?resCargaAcademicaPorDocente.getValor():Collections.EMPTY_LIST);
//                        System.out.println("resTotalHorasFrenteAGrupo = " + resTotalHorasFrenteAGrupo);
                        Integer totalHorasFrenteAGrupo = resTotalHorasFrenteAGrupo.getCorrecto()?resTotalHorasFrenteAGrupo.getValor():0;
                        if(docente.getPersonal().getCategoriaOficial().getCategoria().equals(asignacionPTCCategoriaOficial)){
//                            System.out.println("asignacionPTCCategoriaOficial = " + asignacionPTCCategoriaOficial);
                            if(totalHorasFrenteAGrupo > asignacionPTCHorasClaseMaximo) mensajes.add(new DtoAlerta(String.format("El PTC %s tiene %s horas frente a grupo y el máximo permitido es %s horas.", docente.getPersonal().getNombre(), String.valueOf(totalHorasFrenteAGrupo), String.valueOf(asignacionPTCHorasClaseMaximo)), AlertaTipo.SUGERENCIA));
                        }else if(docente.getPersonal().getCategoriaOficial().getCategoria().equals(asignacionPACategoriaOficial)){
//                            System.out.println("asignacionPACategoriaOficial = " + asignacionPACategoriaOficial);
                            if(totalHorasFrenteAGrupo > asignacionPAHorasClaseMaximo) mensajes.add(new DtoAlerta(String.format("El PA %s tiene %s horas frente a grupo y el máximo permitido es %s horas.", docente.getPersonal().getNombre(), String.valueOf(totalHorasFrenteAGrupo), String.valueOf(asignacionPAHorasClaseMaximo)), AlertaTipo.SUGERENCIA));
                        }
                        long count = rol.getMateriasPorGrupo().stream()
                                .filter(dtoMateria -> dtoMateria.getDtoCargaAcademica() != null)
                                .map(DtoMateria::getDtoCargaAcademica)
                                .map(DtoCargaAcademica::getDocente)
                                .filter(docente1 -> docente1.equals(docente))
                                .count();
//                    System.out.println("docente = " + docente);
//                    System.out.println("count = " + count);

                        if(count > 1l) mensajes.add(new DtoAlerta(String.format("El docente %s tiene mas de una materia asignada en el grupo %s", docente.getPersonal().getNombre(), String.valueOf(rol.getGrupo().getGrado()).concat(rol.getGrupo().getLiteral().toString())), AlertaTipo.SUGERENCIA));
                    });
                }
                return ResultadoEJB.crearCorrecto(mensajes, "Lista de mensajes");
            }else{
                return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Sin mensajes");
            }
        }catch (Exception e){
//            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudieron identificar mensajes de asignación (EjbAsignacionAcademica.identificarMensajes).", e, null);
        }
    }

    /**
     * Permite modificar las horas por semana de una materia
     * @param dtoMateria
     * @return
     */
    public ResultadoEJB<Integer> actualizarHorasPorSemana(DtoMateria dtoMateria){
        try{
            if(dtoMateria.getDtoCargaAcademica() == null) return ResultadoEJB.crearErroneo(2, "No se puede actualizar el valor de horas por semana, ya que la materia aún no ha sido asignada.", Integer.class);
            em.merge(dtoMateria.getDtoCargaAcademica().getCargaAcademica());
            return  ResultadoEJB.crearCorrecto(dtoMateria.getDtoCargaAcademica().getCargaAcademica().getHorasSemana(), "Se actualizó correctamente el valor de horas por semana.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el valor de horas por semana en la asignación (EjbAsignacionAcademica.actualizarHorasPorSemana).", e, null);
        }
    }

    /**
     * Permite identificar la relación de una materia con su plan de estudios vigente a través del programa, la materia y el grado.
     * @param programa Programa al que pertenece el plan de estudios vigente
     * @param materia Materia de la cual se requiere determinar su plan de estudios.
     * @param grado Grado de la materia, se recomienda que el dato venga directamente del grupo al cual se le va a asignar la materia
     * @return Regresa la relación de la materia con su plan de estudios o código de error de lo contrario
     */
    public ResultadoEJB<PlanEstudioMateria> getPlanEstudioMateria(AreasUniversidad programa, Materia materia, Integer grado){
//        System.out.println("programa = [" + programa + "], materia = [" + materia + "], grado = [" + grado + "]");
        try{
//            System.out.println("em = " + em);
            PlanEstudioMateria planEstudioMateria = em.createQuery("select pem from PlanEstudioMateria pem inner join pem.idMateria mat where mat.idMateria=:materia and pem.idPlan.idPe=:programa and pem.grado=:grado and pem.idPlan.estatus=:vigente", PlanEstudioMateria.class)
                    .setParameter("materia", materia.getIdMateria())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grado", grado)
                    .setParameter("vigente", true)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            if(planEstudioMateria == null) return ResultadoEJB.crearErroneo(2, planEstudioMateria, "La materia no ha sido asignada al plan de estudios correspondiente al programa indicado.");
            else return ResultadoEJB.crearCorrecto(planEstudioMateria, "Relación de materia con plan de estudios identificada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar a la entidad de la relación entre plan de estudios y materias (EjbAsignacionAcademica.getPlanEstudioMateria).", e, PlanEstudioMateria.class);
        }
    }
    
    
    /**
     * Permite crear el filtro para validar si el usuario autenticado es personal de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReincorporacion.validarServiciosEscolares)", e, null);
        }
    }

     /**
     * Permite obtener la lista de programas educativos que tienen planes de estudio vigentes, los programas deben ordenarse por
     * área, nivel y nombre y los grupos por grado y letra
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> getProgramasActivosEscolares(PeriodosEscolares periodo){
        try{
            //Map<AreasUniversidad, List<Grupo>> programasMap = Collections.EMPTY_MAP;
            // buscar lista de programas educativos con plan de estudios vigentes y despues mapear cada programa con su lista de grupos
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);
//            System.out.println("programaEducativoCategoria = " + programaEducativoCategoria);
            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad  a where a.categoria.categoria=:categoria and a.vigente = '1' and a.nivelEducativo.nivel = :nivel order by a.nombre", AreasUniversidad.class)
                    .setParameter("categoria", programaEducativoCategoria)
                    .setParameter("nivel", "TSU")
                    .getResultList();
//            System.out.println("programas = " + programas);
            Map<AreasUniversidad, List<Grupo>> programasMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarGrupos(programa, periodo)));
//            System.out.println("programasMap = " + programasMap);
            return ResultadoEJB.crearCorrecto(programasMap, "Mapa de programas y grupos");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbAsignacionAcademica.getProgramasActivosEscolares)", e, null);
        }
    }
    
     /**
     * Permite detectar una lista de mensajes de posibles errores en la asignación docente, como es el caso de superar las horas máximas frente a grupo de acuerdo a si es PTC o PA el docente o si se han asignado
     * mas de una materia al mismo docente en un grupo detemrinado
     * @param rol DTO de la capa de sincronización con los datos seleccionados por el usuario
     * @return Lista de mensajes encontrados.
     */
    public ResultadoEJB<List<DtoAlerta>> identificarMensajesEscolares(AsignacionAcademicaRolEscolares rol){
        try{
//            System.out.println("(rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getPrograma() != null && rol.getGrupo() != null) = " + (rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getPrograma() != null && rol.getGrupo() != null));
//            System.out.println("rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) = " + (rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo())));
//            System.out.println("rol.getPrograma() = " + rol.getPrograma());
//            System.out.println("rol.getGrupo() = " + rol.getGrupo());
            if(rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getPrograma() != null && rol.getGrupo() != null){
                final Short asignacionPTCCategoriaOficial = (short)ep.leerPropiedadEntera("asignacionPTCCategoriaOficial").orElse(32);
                final Short asignacionPACategoriaOficial = (short)ep.leerPropiedadEntera("asignacionPACategoriaOficial").orElse(30);
                final Integer asignacionPAHorasClaseMaximo = ep.leerPropiedadEntera("asignacionPAHorasClaseMaximo").orElse(30);
                final Integer asignacionPTCHorasClaseMaximo = ep.leerPropiedadEntera("asignacionPTCHorasClaseMaximo").orElse(22);
                final List<DtoAlerta> mensajes = new ArrayList<>();

                ResultadoEJB<List<DtoMateria>> resMaterias = getMaterias(rol.getPrograma(), rol.getGrupo(), rol.getPeriodo(), rol.getPeriodoActivo());
//                System.out.println("resMaterias = " + resMaterias);
                if(resMaterias.getCorrecto()){
                    List<PersonalActivo> docentes = resMaterias.getValor().stream()
                            .filter(dtoMateria -> dtoMateria.getDtoCargaAcademica() != null)
                            .map(DtoMateria::getDtoCargaAcademica)
                            .map(DtoCargaAcademica::getDocente)
                            .distinct()
                            .collect(Collectors.toList());

                    docentes.forEach(docente -> {
//                        System.out.println("docente = " + docente.getPersonal().getNombre());
                        ResultadoEJB<List<DtoCargaAcademica>> resCargaAcademicaPorDocente = getCargaAcademicaPorDocente(docente, rol.getPeriodo());
//                        System.out.println("resCargaAcademicaPorDocente = " + resCargaAcademicaPorDocente);
                        ResultadoEJB<Integer> resTotalHorasFrenteAGrupo = getTotalHorasFrenteAGrupo(resCargaAcademicaPorDocente.getCorrecto()?resCargaAcademicaPorDocente.getValor():Collections.EMPTY_LIST);
//                        System.out.println("resTotalHorasFrenteAGrupo = " + resTotalHorasFrenteAGrupo);
                        Integer totalHorasFrenteAGrupo = resTotalHorasFrenteAGrupo.getCorrecto()?resTotalHorasFrenteAGrupo.getValor():0;
                        if(docente.getPersonal().getCategoriaOficial().getCategoria().equals(asignacionPTCCategoriaOficial)){
//                            System.out.println("asignacionPTCCategoriaOficial = " + asignacionPTCCategoriaOficial);
                            if(totalHorasFrenteAGrupo > asignacionPTCHorasClaseMaximo) mensajes.add(new DtoAlerta(String.format("El PTC %s tiene %s horas frente a grupo y el máximo permitido es %s horas.", docente.getPersonal().getNombre(), String.valueOf(totalHorasFrenteAGrupo), String.valueOf(asignacionPTCHorasClaseMaximo)), AlertaTipo.SUGERENCIA));
                        }else if(docente.getPersonal().getCategoriaOficial().getCategoria().equals(asignacionPACategoriaOficial)){
//                            System.out.println("asignacionPACategoriaOficial = " + asignacionPACategoriaOficial);
                            if(totalHorasFrenteAGrupo > asignacionPAHorasClaseMaximo) mensajes.add(new DtoAlerta(String.format("El PA %s tiene %s horas frente a grupo y el máximo permitido es %s horas.", docente.getPersonal().getNombre(), String.valueOf(totalHorasFrenteAGrupo), String.valueOf(asignacionPAHorasClaseMaximo)), AlertaTipo.SUGERENCIA));
                        }
                        long count = rol.getMateriasPorGrupo().stream()
                                .filter(dtoMateria -> dtoMateria.getDtoCargaAcademica() != null)
                                .map(DtoMateria::getDtoCargaAcademica)
                                .map(DtoCargaAcademica::getDocente)
                                .filter(docente1 -> docente1.equals(docente))
                                .count();
//                    System.out.println("docente = " + docente);
//                    System.out.println("count = " + count);

                        if(count > 1l) mensajes.add(new DtoAlerta(String.format("El docente %s tiene mas de una materia asignada en el grupo %s", docente.getPersonal().getNombre(), String.valueOf(rol.getGrupo().getGrado()).concat(rol.getGrupo().getLiteral().toString())), AlertaTipo.SUGERENCIA));
                    });
                }
                return ResultadoEJB.crearCorrecto(mensajes, "Lista de mensajes");
            }else{
                return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Sin mensajes");
            }
        }catch (Exception e){
//            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudieron identificar mensajes de asignación (EjbAsignacionAcademica.identificarMensajes).", e, null);
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
}