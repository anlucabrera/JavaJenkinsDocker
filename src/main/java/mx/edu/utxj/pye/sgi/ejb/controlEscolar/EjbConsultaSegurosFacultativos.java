/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CoordinadorAreaEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.SegurosFacultativosEstatus;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbConsultaSegurosFacultativos")
public class EjbConsultaSegurosFacultativos {
    @EJB                EjbValidacionRol                                ejbValidacionRol;
    @EJB                EjbValidadorDocente                             ejbValidadorDocente;
    @EJB                EjbRegistroSeguroFacultativoEstudiante          ejbRegistroSeguroFacultativoEstudiante;
    @EJB                EjbValidacionSeguroFacultativo                  ejbValidacionSeguroFaculativo;
    @EJB                EjbPropiedades                                  ep;
    @EJB                Facade                                          f;
    @Inject             Caster                                          caster;
    
    private             EntityManager                                   em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Método que permite obtener los registros previos de la asignación del personal como Coordinador de Área Académica de Estadías
     * @param personal
     * @return 
     */
    public ResultadoEJB<List<CoordinadorAreaEstadia>> obtenerRegistrosCoordinadorAreaEstadia(Integer personal){
        try {
            if(personal == null) ResultadoEJB.crearErroneo(2, null, "No se pueden consultar registros de coordinador de área académica de estadía con un personal nulo");
            
            List<CoordinadorAreaEstadia> listaAreaEstadia = em.createQuery("SELECT cae FROM CoordinadorAreaEstadia cae WHERE cae.personal = :personal", CoordinadorAreaEstadia.class)
                    .setParameter("personal", personal)
                    .setMaxResults(1)
                    .getResultList();
            
            if(listaAreaEstadia.isEmpty()){
                return ResultadoEJB.crearErroneo(5, null, "No se han podido obtener registros previos donde el personal fué asignado como Coordinador de Área Académica de Estadías");
            }else{
                return ResultadoEJB.crearCorrecto(listaAreaEstadia, "Lista de registros encontrada con éxito");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Ha ocurrido un error mientras se obtenían los registros donde el personal fué asignado como coordinador de área ácademica de estadías (EjbConsultaSegurosFacultativos.obtenerRegistrosCoordinadorAreaEstadia)", e, null);
        }
    }
    
    /**
     * Método que permite obtener los registros previos de la asingación del personal como asesor académico de estadía
     * @param personal
     * @return 
     */
    public ResultadoEJB<List<AsesorAcademicoEstadia>> obtenerRegistrosAsesorAcademicoEstadia(Integer personal){
        try {
            if(personal == null) ResultadoEJB.crearErroneo(2, null, "No se pueden consultar registros de asesor académico de estadía con un personal nulo");
            
            List<AsesorAcademicoEstadia> listaAsesorAcademico = em.createQuery("SELECT aae FROM AsesorAcademicoEstadia aae WHERE aae.personal = :personal", AsesorAcademicoEstadia.class)
                    .setParameter("personal", personal)
                    .setMaxResults(1)
                    .getResultList();
            
            if(listaAsesorAcademico.isEmpty()){
                return ResultadoEJB.crearErroneo(5, null, "No se han podido obtener registros previos donde el personal fué asignado como asesor académico de estadía");
            }else{
                return ResultadoEJB.crearCorrecto(listaAsesorAcademico, "Lista de registros encontrada con éxito");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Ha ocurrido un error mientras se obtenían los registros donde el personal fué asignado como asesor académico de estadía (EjbConsultaSegurosFacultativos.obtenerRegistrosAsesorAcademicoEstadia)", e, null);
        }
    }
    
    /**
     * Hace referencia al método que permite la comprobación y validación de un coordinador de área académica de estadías.
     * @param personal
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinadorAreaAcademicaEstadia(Integer personal){
        try {
            return ejbValidacionRol.validarCoordinadorAreaAcademicaEstadia(personal, obtenerRegistrosCoordinadorAreaEstadia(personal).getValor());
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo validar el rol de Coordinador de Área Académica de Estadías (EjbConsultaSegurosFacultativos.validarCoordinadorAreaAcademicaEstadia)", e, null);
        }
    }
    
    /**
     * Hace referencia al método que permite la comprobación y validación de un Asesor Académico de Estadía
     * @param personal
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarAsesorAcademicoEstadia(Integer personal){
        try {
            return ejbValidacionRol.validarAsesorAcademicoEstadia(personal, obtenerRegistrosAsesorAcademicoEstadia(personal).getValor());
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo validar el rol de Asesor Académico de Estadías (EjbConsultaSegurosFacultativos.validarAsesorAcademicoEstadia)", e, null);
        }
    }
    
    /**
     * Hace referencia al método que permite la comprobación y validación de un trabajador que tiene acceso a  la consulta de Seguros Facultativos
     * @param personal
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validacionConsultaSegurosFacultativos(Integer personal){
        try {
            return ejbValidacionRol.validacionConsultaSegurosFacultativos(personal);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar correctamente la validación.  (EjbConsultaSegurosFacultativos.validacionConsultaSegurosFacultativos)", e, null);
        }
    }
    
    /**
     * Hace referencia al validador del rol como tutor en el ejbValidadorDocente 
     * @param clave
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarTutor(Integer clave){
        try {
            return ejbValidadorDocente.validarTutor(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar como tutor. (EjbConsultaSegurosFacultativos.validarTutor)", e, null);
        }
    }
    
    /**
     * Hace referencia al validador del rol como Coordinador Institucional de Estadías en el ejbValidarCoordinadorEstadia
     * @param clave
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinadorEstadia(Integer clave){
        try {
            return ejbValidacionRol.validarCoordinadorInsitucionalEstadia(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar como Asesor Académico de Estadía (EjbConsultaSegurosFacultativos.validarCoordinadorEstadia)", e, null);
        }
    }
    
/********************************************    Filtrados director de área académica *******************************************************************/
    
    /**
     * Método para obtener los programas educativos asignados al área académica de un director.
     * @param director
     * @return 
     */
    public ResultadoEJB<List<Short>> obtenerProgramasEducativosPorDirector(PersonalActivo director){
        try {
            List<AreasUniversidad> areaSuperior = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.responsable = :director", AreasUniversidad.class)
                    .setParameter("director", director.getPersonal().getClave())
                    .getResultList();
            if(areaSuperior.isEmpty()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "El usuario logueado no tiene ninguna área a su cargo");
            
            List<Short> programasEducativos = em.createQuery("SELECT a.area FROM AreasUniversidad a WHERE a.areaSuperior = :areaSuperior GROUP BY a.area ORDER BY a.area ASC", Short.class)
                    .setParameter("areaSuperior", areaSuperior.get(0).getArea())
                    .getResultList();
            if(programasEducativos.isEmpty()) return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "No se han podido encontrar programas educativos asignados al área académica del director");
            
            return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos asginada al área académica del director");
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido encontrar programas educativos asignados al área académica del director (EjbConsultaSegurosFacultativos.obtenerProgramasEducativosPorDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de periodos escolares en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de área académica y director
     * @param director
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresConSFPorDirector(PersonalActivo director) {
        try {
            ResultadoEJB<List<Short>> programasEducativos = obtenerProgramasEducativosPorDirector(director);
            if(!programasEducativos.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, programasEducativos.getMensaje());
            
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT e.periodo FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.carrera IN :programasEducativos GROUP BY e.periodo ORDER BY e.periodo DESC", Integer.class)
                    .setParameter("programasEducativos", programasEducativos.getValor())
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());

            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "El periodo escolar aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerPeriodosEscolaresConSFPorDirector).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de programas educativos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de área académica y periodo escolar
     * @param director
     * @param periodoEscolar
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativosConSFPorDirector(PersonalActivo director, PeriodosEscolares periodoEscolar) {
        try {
            ResultadoEJB<List<Short>> programasEducativos = obtenerProgramasEducativosPorDirector(director);
            if(!programasEducativos.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, programasEducativos.getMensaje());
            
            List<AreasUniversidad> programasEducativosPD = em.createQuery("SELECT e.carrera FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.carrera IN :programasEducativos AND e.periodo = :periodo GROUP BY e.carrera ORDER BY e.carrera ASC", Short.class)
                    .setParameter("programasEducativos", programasEducativos.getValor())
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .map(pe -> em.find(AreasUniversidad.class, pe))
                    .distinct()
                    .collect(Collectors.toList());
            
            if(programasEducativosPD.isEmpty()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "El programa educativo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativosPD, "Lista de programas educativos que cuentan con al menos un registro de seguro facultativo");
            }      
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerProgramasEducativosConSFPorDirector).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de grupos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de área académica, periodo escolar, programa educativo
     * @param director
     * @param periodoEscolar
     * @param programaEducativo
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposConSFPorDirector(PersonalActivo director, PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo) {
        try {
            ResultadoEJB<List<Short>> programasEducativos = obtenerProgramasEducativosPorDirector(director);
            if(!programasEducativos.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, programasEducativos.getMensaje());
            
            List<Grupo> gruposPD = em.createQuery("SELECT g.idGrupo FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.grupo g WHERE e.carrera IN :programasEducativos AND g.periodo = :periodo AND g.idPe = :programaEducativo GROUP BY g.idGrupo ORDER BY g.idGrupo ASC", Integer.class)
                    .setParameter("programasEducativos", programasEducativos.getValor())
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultStream()
                    .map(g -> em.find(Grupo.class, g))
                    .distinct()
                    .collect(Collectors.toList());
            if(gruposPD.isEmpty()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "El grupo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(gruposPD, "Lista de grupos que cuentan con al menos un registro de seguro facultativo");
            }      
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerGruposConSFPorDirector).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de periodos escolares en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de área académica y director
     * Uso para reportes de excel
     * @param director
     * @param periodoEscolar
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSfPorPeriodoDirector(PersonalActivo director, PeriodosEscolares periodoEscolar) {
        try {
            ResultadoEJB<List<Short>> programasEducativos = obtenerProgramasEducativosPorDirector(director);
            if(!programasEducativos.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, programasEducativos.getMensaje());
            
            List<DtoSeguroFacultativo> listaSegurosFacultativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.carrera IN :programasEducativos AND e.periodo = :periodoEscolar ORDER BY e.carrera, e.matricula ASC", SegurosFacultativosEstudiante.class)
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programasEducativos", programasEducativos.getValor())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> ejbValidacionSeguroFaculativo.packSeguroFacultativoReporte(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());

            if(listaSegurosFacultativos.isEmpty()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "El periodo escolar aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(listaSegurosFacultativos, "Lista de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerSfPorPeriodoDirector).", e, null);
        }
    }

    
//    La lista del Seguro Facultativo se obtiene del EjbValidacionSeguroFacultativo
    
    
//    Filtros por Coordinador de Área Académica de Estadías - En caso de que el docente cuente con asignación de coordinador en otra área académica - corregir alcance de filtros de periodo escolar
    
    public ResultadoEJB<List<Integer>> obtenerPeriodosEscolaresCoordinadorAreaEstadia(PersonalActivo personal){
        try {
            List<Short> generaciones = em.createQuery("SELECT e.generacion FROM CoordinadorAreaEstadia cae INNER JOIN FETCH cae.evento e WHERE cae.personal = :personal", Short.class)
                    .setParameter("personal", personal.getPersonal().getClave())
                    .getResultList();
            if(generaciones.isEmpty()) return ResultadoEJB.crearErroneo(2, null, "No se han encontrado registros de generaciones, aún no se cuenta con una asignación de coordinador de área académica de estadías");
            List<Integer> ciclosEscolares = em.createQuery("SELECT c.ciclo FROM CiclosescolaresGeneraciones ceg INNER JOIN FETCH ceg.generacion g INNER JOIN FETCH ceg.ciclo c WHERE g.generacion IN :generaciones", Integer.class)
                    .setParameter("generaciones", generaciones)
                    .getResultList();
            if(ciclosEscolares.isEmpty()) return ResultadoEJB.crearErroneo(3, null, "No se han encontrado registros de ciclos escolares, aún no se cuenta con una asignación de coordinador de área académica de estadías");
            List<Integer> periodosEscolares = em.createQuery("SELECT p.periodo FROM PeriodosEscolares p INNER JOIN FETCH p.ciclo c WHERE c.ciclo IN :ciclosEscolares GROUP BY p.periodo ORDER BY p.periodo DESC", Integer.class)
                    .setParameter("ciclosEscolares", ciclosEscolares)
                    .getResultList();
            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(4, null, "No se han encontrado registros de periodos escolares, aún no se cuenta con una asignación de coordinador de área académica de estadías");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares encontrada");
            }
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido obtener los periodos escolares que le correspoden al Coordinador de Área Académica de Estadía (EjbConsultaSegurosFacultativos.obtenerPeriodosEscolaresCoordinadorAreaEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener los programas educativos que le corresponden al filtrado de Coordinador de Área Académica de Estadía
     * @param personal
     * @return 
     */
    public ResultadoEJB<List<Short>> obtenerProgramasEducativosCoordinadorAreaEstadia(PersonalActivo personal){
        try {
            List<Short> areasSuperior = em.createQuery("SELECT cae.areaAcademica FROM CoordinadorAreaEstadia cae WHERE cae.personal = :personal", Short.class)
                    .setParameter("personal", personal.getPersonal().getClave())
                    .getResultList();

            if(areasSuperior.isEmpty()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "El usuario logueado no cuenta con ninguna asignación de Coordinador de Área Académica de Estadías");
            
            List<Short> programasEducativos = em.createQuery("SELECT a.area FROM AreasUniversidad a WHERE a.areaSuperior IN :areasSuperior GROUP BY a.area ORDER BY a.area ASC", Short.class)
                    .setParameter("areasSuperior", areasSuperior)
                    .getResultList();
            
            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han encontrado registros de programas educativos, aún no se cuenta con una asignación de coordinador de área académica de estadías");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos encontrada");
            }
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido obtener los programas educativos que le correspoden al Coordinador de Área Académica de Estadía (EjbConsultaSegurosFacultativos.obtenerProgramasEducativosCoordinadorAreaEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de periodos escolares en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de coordinador área académica de estadía
     * @param personal
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresConSFPorCoordinadorAreaAcademicaEstadia(PersonalActivo personal) {
        try {
            ResultadoEJB<List<Integer>> periodosEscolaresCAAE = obtenerPeriodosEscolaresCoordinadorAreaEstadia(personal);
            ResultadoEJB<List<Short>> programasEducativosCAAE = obtenerProgramasEducativosCoordinadorAreaEstadia(personal);
            
            if(!periodosEscolaresCAAE.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, periodosEscolaresCAAE.getMensaje());
            if(!programasEducativosCAAE.getCorrecto()) return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, programasEducativosCAAE.getMensaje());
            
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT e.periodo FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.carrera IN :programasEducativos AND e.periodo IN :periodosEscolares GROUP BY e.periodo ORDER BY e.periodo DESC", Integer.class)
                    .setParameter("programasEducativos", programasEducativosCAAE.getValor())
                    .setParameter("periodosEscolares", periodosEscolaresCAAE.getValor())
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());

            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El periodo escolar aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerPeriodosEscolaresConSFPorCoordinadorAreaAcademicaEstadia).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de programas educativos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de coordinador área académica de estadía
     * @param personal
     * @param periodoEscolar
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativosConSFPorCoordinadorAreaAcademicaEstadia(PersonalActivo personal, PeriodosEscolares periodoEscolar) {
        try {
            ResultadoEJB<List<Integer>> periodosEscolaresCAAE = obtenerPeriodosEscolaresCoordinadorAreaEstadia(personal);
            ResultadoEJB<List<Short>> programasEducativosCAAE = obtenerProgramasEducativosCoordinadorAreaEstadia(personal);
            
            if(!periodosEscolaresCAAE.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, periodosEscolaresCAAE.getMensaje());
            if(!programasEducativosCAAE.getCorrecto()) return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, programasEducativosCAAE.getMensaje());
            
            List<AreasUniversidad> programasEducativos = em.createQuery("SELECT e.carrera FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.periodo IN :periodosEscolares AND e.carrera IN :programasEducativos AND e.periodo = :periodoEscolar GROUP BY e.carrera ORDER BY e.carrera ASC", Short.class)
                    .setParameter("programasEducativos", programasEducativosCAAE.getValor())
                    .setParameter("periodosEscolares", periodosEscolaresCAAE.getValor())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .map(area -> em.find(AreasUniversidad.class, area))
                    .distinct()
                    .sorted(Comparator.comparingInt(AreasUniversidad::getArea).reversed())
                    .collect(Collectors.toList());

            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El programa educativo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerProgramasEducativosConSFPorCoordinadorAreaAcademicaEstadia).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de grupos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de coordinador área académica de estadía
     * @param personal
     * @param periodoEscolar
     * @param programaEducativo
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposConSFPorCoordinadorAreaAcademicaEstadia(PersonalActivo personal, PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo) {
        try {
            ResultadoEJB<List<Integer>> periodosEscolaresCAAE = obtenerPeriodosEscolaresCoordinadorAreaEstadia(personal);
            ResultadoEJB<List<Short>> programasEducativosCAAE = obtenerProgramasEducativosCoordinadorAreaEstadia(personal);
            
            if(!periodosEscolaresCAAE.getCorrecto()) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, periodosEscolaresCAAE.getMensaje());
            if(!programasEducativosCAAE.getCorrecto()) return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, programasEducativosCAAE.getMensaje());
            
            List<Grupo> grupos = em.createQuery("SELECT g.idGrupo FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.grupo g WHERE e.periodo IN :periodosEscolares AND e.carrera IN :programasEducativos AND g.periodo = :periodoEscolar AND g.idPe = :programaEducativo GROUP BY g.idGrupo ORDER BY g.idGrupo ASC", Integer.class)
                    .setParameter("programasEducativos", programasEducativosCAAE.getValor())
                    .setParameter("periodosEscolares", periodosEscolaresCAAE.getValor())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultStream()
                    .map(g -> em.find(Grupo.class, g))
                    .distinct()
                    .collect(Collectors.toList());

            if(grupos.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El grupo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(grupos, "Lista de grupos que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerGruposConSFPorCoordinadorAreaAcademicaEstadia).", e, null);
        }
    }
    
    //    La lista del Seguro Facultativo se obtiene del EjbValidacionSeguroFacultativo
    
    
    /********************************************    Filtrados tutor *******************************************************************/
    
    /**
     * Permite obtener una lista de periodos escolares en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de tutor
     * @param tutor
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresConSFPorTutor(PersonalActivo tutor) {
        try {
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT g.periodo FROM SegurosFacultativosEstudiante sfe INNER JOIN FETCH sfe.estudiante e INNER JOIN FETCH e.grupo g WHERE g.tutor = :tutor GROUP BY e.periodo ORDER BY e.periodo DESC", Integer.class)
                    .setParameter("tutor", tutor.getPersonal().getClave())
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());

            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El periodo escolar aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerPeriodosEscolaresConSFPorTutor).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de programas educativos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de tutor
     * @param personal
     * @param periodoEscolar
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativosConSFPorTutor(PersonalActivo tutor, PeriodosEscolares periodoEscolar) {
        try {
            List<AreasUniversidad> programasEducativos = em.createQuery("SELECT g.idPe FROM SegurosFacultativosEstudiante sfe INNER JOIN FETCH sfe.estudiante e INNER JOIN FETCH e.grupo g WHERE g.tutor = :tutor AND g.periodo = :periodoEscolar GROUP BY g.idPe ORDER BY g.idPe ASC", Short.class)
                    .setParameter("tutor", tutor.getPersonal().getClave())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .map(area -> em.find(AreasUniversidad.class, area))
                    .distinct()
                    .sorted(Comparator.comparingInt(AreasUniversidad::getArea).reversed())
                    .collect(Collectors.toList());

            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El programa educativo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerProgramasEducativosConSFPorTutor).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de grupos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de tutor
     * @param personal
     * @param periodoEscolar
     * @param programaEducativo
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposConSFPorTutor(PersonalActivo tutor, PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo) {
        try {
            List<Grupo> grupos = em.createQuery("SELECT g.idGrupo FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.grupo g WHERE g.tutor = :tutor AND g.periodo = :periodoEscolar AND g.idPe = :programaEducativo GROUP BY g.idGrupo ORDER BY g.idGrupo ASC", Integer.class)
                    .setParameter("tutor", tutor.getPersonal().getClave())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultStream()
                    .map(g -> em.find(Grupo.class, g))
                    .distinct()
                    .collect(Collectors.toList());

            if(grupos.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El grupo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(grupos, "Lista de grupos que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerGruposConSFPorTutor).", e, null);
        }
    }
    
    //    La lista del Seguro Facultativo se obtiene del EjbValidacionSeguroFacultativo
    
    /********************************************   Filtrados Asesor Académico Estadía *******************************************************************/
    
    /**
     * Permite consultar a los estudiantes asignados al Asesor Académico de Estadía
     * @param asesorAcademicoEstadia
     * @return 
     */
    public ResultadoEJB<List<Integer>> obtenerEstudiantesPorAsesorEstadia(PersonalActivo asesorAcademicoEstadia){
        try {
            List<Integer> estudiantes = em.createQuery("SELECT e.idEstudiante FROM SeguimientoEstadiaEstudiante see INNER JOIN FETCH see.estudiante e INNER JOIN FETCH see.asesor ase WHERE ase.personal = :asesorAcademicoEstadia GROUP BY e.idEstudiante ORDER BY e.idEstudiante ASC", Integer.class)
                    .setParameter("asesorAcademicoEstadia", asesorAcademicoEstadia.getPersonal().getClave())
                    .getResultList();
            if(estudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "El asesor académico de estadía aún no cuenta con estudiantes asignados");
            }else{
                return ResultadoEJB.crearCorrecto(estudiantes, "Lista de estudiantes asignados al Asesor Académico");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes asesorados de estadía (EjbConsultaSegurosFacultativos.obtenerEstudiantesPorAsesorEstadia)", e, null);
        }
    } 
    
    /**
     * Permite obtener una lista de periodos escolares en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de asesor académico de estadía
     * @param asesorAcademicoEstadia
     * @param estudiantesAsesoradosEstadia
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresConSFPorAsesorAcademicoEstadia(PersonalActivo asesorAcademicoEstadia, ResultadoEJB<List<Integer>> estudiantesAsesoradosEstadia) {
        try {
            if(!estudiantesAsesoradosEstadia.getCorrecto()) return ResultadoEJB.crearErroneo(2, null, estudiantesAsesoradosEstadia.getMensaje());
            
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT e.periodo FROM SegurosFacultativosEstudiante sfe INNER JOIN FETCH sfe.estudiante e WHERE e.idEstudiante IN :estudiantes GROUP BY e.periodo ORDER BY e.periodo DESC", Integer.class)
                    .setParameter("estudiantes", estudiantesAsesoradosEstadia.getValor())
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());

            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El periodo escolar aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerPeriodosEscolaresConSFPorAsesorAcademicoEstadia).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de programas educativos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de asesor académico de estadía
     * @param asesorAcademicoEstadia
     * @param estudiantesAsesoradosEstadia
     * @param periodoEscolar
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativosConSFPorAsesorAcademicoEstadia(PersonalActivo asesorAcademicoEstadia, ResultadoEJB<List<Integer>> estudiantesAsesoradosEstadia, PeriodosEscolares periodoEscolar) {
        try {
            List<AreasUniversidad> programasEducativos = em.createQuery("SELECT e.carrera FROM SegurosFacultativosEstudiante sfe INNER JOIN FETCH sfe.estudiante e WHERE e.idEstudiante IN :estudiantesAsesoradosEstadia AND e.periodo = :periodoEscolar GROUP BY e.carrera ORDER BY e.carrera DESC", Short.class)
                    .setParameter("estudiantesAsesoradosEstadia", estudiantesAsesoradosEstadia.getValor())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .map(area -> em.find(AreasUniversidad.class, area))
                    .distinct()
                    .sorted(Comparator.comparingInt(AreasUniversidad::getArea).reversed())
                    .collect(Collectors.toList());
            
            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El programa educativo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerProgramasEducativosConSFPorAsesorAcademicoEstadia).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de grupos en las cuales ha habido registros de tipo Seguro Facultativo - basada en el filtro de asesor académico de estadía
     * 
     * @param asesorAcademicoEstadia
     * @param estudiantesAsesoradosEstadia
     * @param periodoEscolar
     * @param programaEducativo
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposConSFPorAsesorAcademicoEstadia(PersonalActivo asesorAcademicoEstadia, ResultadoEJB<List<Integer>> estudiantesAsesoradosEstadia, PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo) {
        try {
            List<Grupo> grupos = em.createQuery("SELECT g.idGrupo FROM SegurosFacultativosEstudiante sfe INNER JOIN FETCH sfe.estudiante e INNER JOIN FETCH e.grupo g WHERE e.idEstudiante IN :estudiantesAsesoradosEstadia AND e.periodo = :periodoEscolar AND e.carrera = :programaEducativo GROUP BY g.idGrupo ORDER BY g.idGrupo ASC", Integer.class)
                    .setParameter("estudiantesAsesoradosEstadia", estudiantesAsesoradosEstadia.getValor())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultStream()
                    .map(g -> em.find(Grupo.class, g))
                    .distinct()
                    .collect(Collectors.toList());
            
            if(grupos.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "El grupo aún no cuenta con registro de seguros facultativos");
            }else{
                return ResultadoEJB.crearCorrecto(grupos, "Lista de grupos que cuentan con al menos un registro de seguro facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos con registros de seguro facultativo (EjbConsultaSegurosFacultativos.obtenerGruposConSFPorAsesorAcademicoEstadia).", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de tipo DtoSeguroFacultativo,  filtrada por periodo escolar, programa educativo y grupo  - basada en el filtro de asesor académico de estadía
     * @param estudiantesAsesoradosEstadia
     * @param periodoEscolar
     * @param programaEducativo
     * @param grupo
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSFPorPeriodoProgramaEducativoGrupo(ResultadoEJB<List<Integer>> estudiantesAsesoradosEstadia, PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo, Grupo grupo){
        try {
            if(periodoEscolar == null) return ResultadoEJB.crearErroneo(3, null, "No se pueden consultar registros de seguros facultativo si el periodo escolar es nulo");
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(4, null, "No se pueden consultar registros de seguros facultativos si el programa educativo es nulo");
            if(grupo == null) return ResultadoEJB.crearErroneo(5, null, "No se pueden consultar registros de seguros facultativo si el grupo es nulo");
            List<DtoSeguroFacultativo> listaSegurosFacultatativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.grupo g WHERE e.idEstudiante IN :estudiantesAsesoradosEstadia AND e.periodo = :periodoEscolar AND e.carrera = :programaEducativo AND g.idGrupo = :grupo ORDER BY sf.seguroFacultativoEstudiante DESC", SegurosFacultativosEstudiante.class)
                    .setParameter("estudiantesAsesoradosEstadia", estudiantesAsesoradosEstadia.getValor())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> ejbRegistroSeguroFacultativoEstudiante.packSeguroFacultativo(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(listaSegurosFacultatativos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "No se pudo obtener la lista de Seguros Facultativos, debido a que aún no hay registros en el sistema");
            }else{
                return ResultadoEJB.crearCorrecto(listaSegurosFacultatativos, "Lista de seguros facultativos filtrada por periodo escolar, programa educativo y grupo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros de seguros facultativos filtrada por periodo y programa educativo. (EjbConsultaSegurosFacultativos.obtenerSFPorPeriodoProgramaEducativoGrupo)", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de tipo DtoSeguroFacultativo,  filtrada por periodo escolar, programa educativo y grupo  - basada en el filtro de asesor académico de estadía
     * para uso en reportes de excel
     * @param estudiantesAsesoradosEstadia
     * @param periodoEscolar
     * @param programaEducativo
     * @param grupo
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSFCuatrimestral(ResultadoEJB<List<Integer>> estudiantesAsesoradosEstadia, PeriodosEscolares periodoEscolar){
        try {
            if(periodoEscolar == null) return ResultadoEJB.crearErroneo(3, null, "No se pueden consultar registros de seguros facultativo si el periodo escolar es nulo");
            List<DtoSeguroFacultativo> listaSegurosFacultatativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.grupo g WHERE e.idEstudiante IN :estudiantesAsesoradosEstadia AND e.periodo = :periodoEscolar ORDER BY sf.seguroFacultativoEstudiante DESC", SegurosFacultativosEstudiante.class)
                    .setParameter("estudiantesAsesoradosEstadia", estudiantesAsesoradosEstadia.getValor())
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> ejbRegistroSeguroFacultativoEstudiante.packSeguroFacultativo(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(listaSegurosFacultatativos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "No se pudo obtener la lista de Seguros Facultativos, debido a que aún no hay registros en el sistema");
            }else{
                return ResultadoEJB.crearCorrecto(listaSegurosFacultatativos, "Lista de seguros facultativos filtrada por periodo escolar");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros de seguros facultativos filtrada por periodo y programa educativo. (EjbConsultaSegurosFacultativos.obtenerSFCuatrimestral)", e, null);
        }
    }
    
}
