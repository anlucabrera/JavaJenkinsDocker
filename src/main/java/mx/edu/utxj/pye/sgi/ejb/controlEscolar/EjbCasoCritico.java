package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import edu.mx.utxj.pye.seut.util.dto.Dto;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.apache.poi.hssf.record.TabIdRecord;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

@Stateless
public class EjbCasoCritico implements Serializable {
    @EJB EjbPacker packer;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    /**
     * Permite identificar si existen casos críticos abiertos para un estudiante en unidad de materia actual o anteriores
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCritico>> identificar(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion){
        try{
            //lista de unidades previas  a la unidad que se muestra en pantalla para buscar casos criticos tanto en la unidad actual como en las anteriores
            List<UnidadMateriaConfiguracion> unidades = em.createQuery("select umc from UnidadMateriaConfiguracion umc inner join umc.idUnidadMateria um where um.idMateria=:materia and umc.carga=:carga and um.noUnidad <= :unidad", UnidadMateriaConfiguracion.class)
                    .setParameter("materia", dtoCargaAcademica.getMateria())
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .setParameter("unidad", dtoUnidadConfiguracion.getUnidadMateria().getNoUnidad())
                    .getResultStream()
                    .collect(Collectors.toList());

            //lista de estados de casos críticos que indican que estén abiertos
            List<CasoCriticoEstado> estadosAbiertos = Arrays.stream(CasoCriticoEstado.values())
                    .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() > 0D)
                    .collect(Collectors.toList());

            //lista de casos críticos abiertos del estudiante correspondientes a la carga académica
            List<CasoCritico> casosCriticos= em.createQuery("select cc from CasoCritico cc where cc.carga=:carga and cc.configuracion in :unidades and cc.idEstudiante=:estudiante and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .setParameter("unidades", unidades)
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion())
                    .setParameter("estadosAbiertos", estadosAbiertos)
                    .getResultStream()
                    .collect(Collectors.toList());

            List<ResultadoEJB<DtoCasoCritico>> resultadoEJBS = casosCriticos.stream()
                    .map(casoCritico -> packer.packCasoCritico(casoCritico, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion))
                    .collect(Collectors.toList());
            ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCritico.class);

            List<@NonNull DtoCasoCritico> dtos = resultadoEJBS
                    .stream()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(dtos.isEmpty()) {
                List<DtoCasoCritico> l = Collections.EMPTY_LIST;
                return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
            }
            return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante, carga académica y configuración de la unidad (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    /**
     * Permite identificar si existen casos críticos abiertos para un estudiante en unidad de materia actual o anteriores
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificarAlineacion(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion){
        try{
            //lista de unidades previas  a la unidad que se muestra en pantalla para buscar casos criticos tanto en la unidad actual como en las anteriores
            List<UnidadMateriaConfiguracion> unidades = em.createQuery("select umc from UnidadMateriaConfiguracion umc inner join umc.idUnidadMateria um where um.idMateria=:materia and umc.carga=:carga and um.noUnidad <= :unidad", UnidadMateriaConfiguracion.class)
                    .setParameter("materia", dtoCargaAcademica.getMateria())
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .setParameter("unidad", dtoUnidadConfiguracion.getUnidadMateria().getNoUnidad())
                    .getResultStream()
                    .collect(Collectors.toList());

            //lista de estados de casos críticos que indican que estén abiertos
            List<CasoCriticoEstado> estadosAbiertos = Arrays.stream(CasoCriticoEstado.values())
                    .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() > 0D)
                    .collect(Collectors.toList());

            //lista de casos críticos abiertos del estudiante correspondientes a la carga académica
            List<CasoCritico> casosCriticos= em.createQuery("select cc from CasoCritico cc where cc.carga=:carga and cc.configuracion in :unidades and cc.idEstudiante=:estudiante and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .setParameter("unidades", unidades)
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion())
                    .setParameter("estadosAbiertos", estadosAbiertos)
                    .getResultStream()
                    .collect(Collectors.toList());

            List<ResultadoEJB<DtoCasoCriticoAlineacion>> resultadoEJBS = casosCriticos.stream()
                    .map(casoCritico -> packer.packCasoCriticoAlineacion(casoCritico, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion))
                    .collect(Collectors.toList());
            ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCriticoAlineacion.class);

            List<@NonNull DtoCasoCriticoAlineacion> dtos = resultadoEJBS
                    .stream()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(dtos.isEmpty()) {
                List<DtoCasoCriticoAlineacion> l = Collections.EMPTY_LIST;
                return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
            }
            return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante, carga académica y configuración de la unidad (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    /**
     * Permite identificar si existen casos críticos para un estudiante 
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCritico>> identificarPorEsdudiante(Estudiante estudiante){
        try{
//            Empaquetado de estudiantes:
            ResultadoEJB<DtoEstudiante> dtoEstudiante = packer.packEstudianteGeneral(estudiante);
            if (dtoEstudiante.getCorrecto()) {
//                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante.matricula = :matricula AND cc.idEstudiante.periodo = :periodo order by cc.fechaRegistro desc", CasoCritico.class)
                        .setParameter("matricula", estudiante.getMatricula())
                        .setParameter("periodo", estudiante.getPeriodo())
                        .getResultStream()
                        .collect(Collectors.toList());

                List<ResultadoEJB<DtoCasoCritico>> resultadoEJBS = casosCriticos.stream()
                        .map(casoCritico -> packer.packCasoCriticoEstudiante(casoCritico, dtoEstudiante.getValor()))
                        .collect(Collectors.toList());
                ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCritico.class);

                List<@NonNull DtoCasoCritico> dtos = resultadoEJBS
                        .stream()
                        .filter(ResultadoEJB::getCorrecto)
                        .map(ResultadoEJB::getValor)
                        .collect(Collectors.toList());
                if (dtos.isEmpty()) {
                    List<DtoCasoCritico> l = Collections.EMPTY_LIST;
                    return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
                }
                return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
            } else {
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "No se encontraron casos críticos.");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    /**
     * Permite identificar si existen casos críticos para un estudiante 
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificarPorEsdudianteAlineacion(Estudiante estudiante){
        try{
//            Empaquetado de estudiantes:
            ResultadoEJB<DtoEstudiante> dtoEstudiante = packer.packEstudianteGeneral(estudiante);
            if (dtoEstudiante.getCorrecto()) {
//                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante.matricula = :matricula AND cc.idEstudiante.periodo = :periodo order by cc.fechaRegistro desc", CasoCritico.class)
                        .setParameter("matricula", estudiante.getMatricula())
                        .setParameter("periodo", estudiante.getPeriodo())
                        .getResultStream()
                        .collect(Collectors.toList());

                List<ResultadoEJB<DtoCasoCriticoAlineacion>> resultadoEJBS = casosCriticos.stream()
                        .map(casoCritico -> packer.packCasoCriticoEstudianteAlineacion(casoCritico, dtoEstudiante.getValor()))
                        .collect(Collectors.toList());
                ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCriticoAlineacion.class);

                List<@NonNull DtoCasoCriticoAlineacion> dtos = resultadoEJBS
                        .stream()
                        .filter(ResultadoEJB::getCorrecto)
                        .map(ResultadoEJB::getValor)
                        .collect(Collectors.toList());
                if (dtos.isEmpty()) {
                    List<DtoCasoCriticoAlineacion> l = Collections.EMPTY_LIST;
                    return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
                }
                return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
            } else {
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "No se encontraron casos críticos.");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    /**
     * Permite identificar si existen casos críticos abiertos para un estudiante 
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCritico>> identificarPorEsdudianteCaso(Estudiante estudiante, Integer caso){
        try{
//            Empaquetado de estudiantes:
            ResultadoEJB<DtoEstudiante> dtoEstudiante = packer.packEstudianteGeneral(estudiante);
            if (dtoEstudiante.getCorrecto()) {
                //lista de casos críticos del estudiante
//                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante AND cc.caso = :caso and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                  List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante.matricula = :matricula AND cc.idEstudiante.periodo = :periodo AND cc.caso = :caso order by cc.fechaRegistro desc", CasoCritico.class)
                        .setParameter("matricula", estudiante.getMatricula())
                        .setParameter("periodo", estudiante.getPeriodo())
                        .setParameter("caso", caso)
                        .getResultStream()
                        .collect(Collectors.toList());

                List<ResultadoEJB<DtoCasoCritico>> resultadoEJBS = casosCriticos.stream()
                        .map(casoCritico -> packer.packCasoCriticoEstudiante(casoCritico, dtoEstudiante.getValor()))
                        .collect(Collectors.toList());
                ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCritico.class);

                List<@NonNull DtoCasoCritico> dtos = resultadoEJBS
                        .stream()
                        .filter(ResultadoEJB::getCorrecto)
                        .map(ResultadoEJB::getValor)
                        .collect(Collectors.toList());
                if (dtos.isEmpty()) {
                    List<DtoCasoCritico> l = Collections.EMPTY_LIST;
                    return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
                }
                return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
            } else {
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "No se encontraron casos críticos.");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    /**
     * Permite identificar si existen casos críticos abiertos para un estudiante 
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificarPorEsdudianteCasoAlineacion(Estudiante estudiante, Integer caso){
        try{
//            Empaquetado de estudiantes:
            ResultadoEJB<DtoEstudiante> dtoEstudiante = packer.packEstudianteGeneral(estudiante);
            if (dtoEstudiante.getCorrecto()) {
                //lista de casos críticos del estudiante
//                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante AND cc.caso = :caso and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                  List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante.matricula = :matricula AND cc.idEstudiante.periodo = :periodo AND cc.caso = :caso order by cc.fechaRegistro desc", CasoCritico.class)
                        .setParameter("matricula", estudiante.getMatricula())
                        .setParameter("periodo", estudiante.getPeriodo())
                        .setParameter("caso", caso)
                        .getResultStream()
                        .collect(Collectors.toList());

                List<ResultadoEJB<DtoCasoCriticoAlineacion>> resultadoEJBS = casosCriticos.stream()
                        .map(casoCritico -> packer.packCasoCriticoEstudianteAlineacion(casoCritico, dtoEstudiante.getValor()))
                        .collect(Collectors.toList());
                ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCriticoAlineacion.class);

                List<@NonNull DtoCasoCriticoAlineacion> dtos = resultadoEJBS
                        .stream()
                        .filter(ResultadoEJB::getCorrecto)
                        .map(ResultadoEJB::getValor)
                        .collect(Collectors.toList());
                if (dtos.isEmpty()) {
                    List<DtoCasoCriticoAlineacion> l = Collections.EMPTY_LIST;
                    return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
                }
                return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
            } else {
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "No se encontraron casos críticos.");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCasoCritico>> identificarPorGrupo(Integer grupo, String tipo, String estado) {
        try {
            //lista de estados de casos críticos que indican que estén abiertos
            List<CasoCriticoEstado> estadosCerrados = Arrays.stream(CasoCriticoEstado.values())
                    .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() < 0D)
                    .collect(Collectors.toList());

            //lista de casos críticos abiertos del estudiante correspondientes a la carga académica
//                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante AND cc.caso = :caso and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
            List<CasoCritico> casosCriticos = em.createQuery("SELECT cc FROM CasoCritico cc WHERE cc.idEstudiante.grupo.idGrupo = :grupo AND cc.estado NOT IN :estadosCerrados AND cc.tipo = :tipo AND cc.estado = :estado ORDER BY cc.idEstudiante.matricula,cc.tipo, cc.estado", CasoCritico.class)
                    .setParameter("grupo", grupo)
                    .setParameter("estadosCerrados", estadosCerrados)
                    .setParameter("tipo", tipo)
                    .setParameter("estado", estado)
                    .getResultStream()
                    .collect(Collectors.toList());

            List<ResultadoEJB<DtoCasoCritico>> resultadoEJBS = casosCriticos.stream()
                    .map(casoCritico -> packer.packCasoCriticoEstudiante(casoCritico, packer.packEstudianteGeneral(casoCritico.getIdEstudiante()).getValor()))
                    .collect(Collectors.toList());
            ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCritico.class);

            List<@NonNull DtoCasoCritico> dtos = resultadoEJBS
                    .stream()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if (dtos.isEmpty()) {
                List<DtoCasoCritico> l = Collections.EMPTY_LIST;
                return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos del grupo seleccionado.");
            }
            return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudieron identificar casos críticos por estudinte (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificarPorGrupoAlineacion(Integer grupo, String tipo, String estado) {
        try {
            //lista de estados de casos críticos que indican que estén abiertos
            List<CasoCriticoEstado> estadosCerrados = Arrays.stream(CasoCriticoEstado.values())
                    .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() < 0D)
                    .collect(Collectors.toList());

            //lista de casos críticos abiertos del estudiante correspondientes a la carga académica
//                List<CasoCritico> casosCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante AND cc.caso = :caso and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
            List<CasoCritico> casosCriticos = em.createQuery("SELECT cc FROM CasoCritico cc WHERE cc.idEstudiante.grupo.idGrupo = :grupo AND cc.estado NOT IN :estadosCerrados AND cc.tipo = :tipo AND cc.estado = :estado ORDER BY cc.idEstudiante.matricula,cc.tipo, cc.estado", CasoCritico.class)
                    .setParameter("grupo", grupo)
                    .setParameter("estadosCerrados", estadosCerrados)
                    .setParameter("tipo", tipo)
                    .setParameter("estado", estado)
                    .getResultStream()
                    .collect(Collectors.toList());

            List<ResultadoEJB<DtoCasoCriticoAlineacion>> resultadoEJBS = casosCriticos.stream()
                    .map(casoCritico -> packer.packCasoCriticoEstudianteAlineacion(casoCritico, packer.packEstudianteGeneral(casoCritico.getIdEstudiante()).getValor()))
                    .collect(Collectors.toList());
            ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCriticoAlineacion.class);

            List<@NonNull DtoCasoCriticoAlineacion> dtos = resultadoEJBS
                    .stream()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if (dtos.isEmpty()) {
                List<DtoCasoCriticoAlineacion> l = Collections.EMPTY_LIST;
                return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos del grupo seleccionado.");
            }
            return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudieron identificar casos críticos por estudinte (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }

    /**
     * Permite crear una nueva instancia de caso crítico y empaquertarla asignando cun zero como clave primaria del caso crítico para que al intentar registrar se detecte
     * como no persistida a la instancia, en caso de existir un caso critico abierto para el mismo estudiante, carga y unidad se devuelve el caso existente
     * para evitar abrir mas de un critico bajo las mismas condiciones
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @param casoCriticoTipo Define el tipo de caso critico, si el nivel del tipo es de sistema solo devolverá un caso crítico existente que coincida con la unidad y el tipo, en caso de ser de tipo registrado por usuario, devolverá el primer caso crítico que encuentre
     * @return
     */
    public ResultadoEJB<DtoCasoCritico> generarNuevo(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion, CasoCriticoTipo casoCriticoTipo){
        try{
//            System.out.println("EjbCasoCritico.generarNuevo");
//            System.out.println("dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            ResultadoEJB<List<DtoCasoCritico>> identificar = identificar(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
//            System.out.println("identificar = " + identificar);
            if(identificar.getCorrecto()) {
                if(casoCriticoTipo.getNivel() > 0d){//si el tipo caso crítico es registrado por el usuario se regresa como existente el primer caso crítico localizado
                    DtoCasoCritico dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().getNivel() > 0d)//solo contemplar casos críticos registrados por el usuario
                            .findFirst()
                            .orElse(null);
                    if(dtoCasoCritico1 != null) return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico existente.");
                }else{
                    DtoCasoCritico dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().equals(dtoUnidadConfiguracion.getUnidadMateriaConfiguracion()))//solo contemplar casos criticos de la unidad actual
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().equals(casoCriticoTipo))//solo contemplar casos criticos del tipo generado por el sistema
                            .findFirst()
                            .orElse(null);
                    if(dtoCasoCritico1 != null) return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico lanzado por sistema existente.");
                }

            }
            //
            CasoCritico casoCritico = new CasoCritico(0);
            casoCritico.setCarga(dtoCargaAcademica.getCargaAcademica());
            casoCritico.setComentariosEspecialista(null);
            casoCritico.setComentariosTutor(null);
            casoCritico.setConfiguracion(dtoUnidadConfiguracion.getUnidadMateriaConfiguracion());
            casoCritico.setDescripcion(null);
            casoCritico.setEspecialista(null);
            casoCritico.setEstado(CasoCriticoEstado.SIN_REGISTRO.getLabel());
            casoCritico.setEvidenciaEspecialista(null);
            casoCritico.setEvidenciaTutor(null);
            casoCritico.setFechaCierre(null);
            casoCritico.setFechaRegistro(new Date());
            casoCritico.setIdEstudiante(dtoEstudiante.getInscripcionActiva().getInscripcion());
            casoCritico.setTipo(casoCriticoTipo.getLabel());
            casoCritico.setTutor(dtoCargaAcademica.getGrupo().getTutor());

//            System.out.println("casoCritico = " + casoCritico.getTipo());
            ResultadoEJB<DtoCasoCritico> dtoCasoCriticoResultadoEJB = packer.packCasoCritico(casoCritico, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
//            System.out.println("dtoCasoCriticoResultadoEJB = " + dtoCasoCriticoResultadoEJB);

            if(!dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCritico.class);
            else return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Nuevo caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo generar un nuevo caso critico (EjbCasoCritico.generarNuevo).", DtoCasoCritico.class);
        }
    }
    
    /**
     * Permite crear una nueva instancia de caso crítico y empaquertarla asignando cun zero como clave primaria del caso crítico para que al intentar registrar se detecte
     * como no persistida a la instancia, en caso de existir un caso critico abierto para el mismo estudiante, carga y unidad se devuelve el caso existente
     * para evitar abrir mas de un critico bajo las mismas condiciones
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @param casoCriticoTipo Define el tipo de caso critico, si el nivel del tipo es de sistema solo devolverá un caso crítico existente que coincida con la unidad y el tipo, en caso de ser de tipo registrado por usuario, devolverá el primer caso crítico que encuentre
     * @return
     */
    public ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevoAlineacion(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion, CasoCriticoTipo casoCriticoTipo){
        try{
//            System.out.println("EjbCasoCritico.generarNuevo");
//            System.out.println("dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificar = identificarAlineacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
//            System.out.println("identificar = " + identificar);
            if(identificar.getCorrecto()) {
                if(casoCriticoTipo.getNivel() > 0d){//si el tipo caso crítico es registrado por el usuario se regresa como existente el primer caso crítico localizado
                    DtoCasoCriticoAlineacion dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().getNivel() > 0d)//solo contemplar casos críticos registrados por el usuario
                            .findFirst()
                            .orElse(null);
                    if(dtoCasoCritico1 != null) return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico existente.");
                }else{
                    DtoCasoCriticoAlineacion dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().equals(dtoUnidadConfiguracion.getUnidadMateriaConfiguracion()))//solo contemplar casos criticos de la unidad actual
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().equals(casoCriticoTipo))//solo contemplar casos criticos del tipo generado por el sistema
                            .findFirst()
                            .orElse(null);
                    if(dtoCasoCritico1 != null) return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico lanzado por sistema existente.");
                }

            }
            //
            CasoCritico casoCritico = new CasoCritico(0);
            casoCritico.setCarga(dtoCargaAcademica.getCargaAcademica());
            casoCritico.setComentariosEspecialista(null);
            casoCritico.setComentariosTutor(null);
            casoCritico.setConfiguracion(dtoUnidadConfiguracion.getUnidadMateriaConfiguracion());
            casoCritico.setDescripcion(null);
            casoCritico.setEspecialista(null);
            casoCritico.setEstado(CasoCriticoEstado.SIN_REGISTRO.getLabel());
            casoCritico.setEvidenciaEspecialista(null);
            casoCritico.setEvidenciaTutor(null);
            casoCritico.setFechaCierre(null);
            casoCritico.setFechaRegistro(new Date());
            casoCritico.setIdEstudiante(dtoEstudiante.getInscripcionActiva().getInscripcion());
            casoCritico.setTipo(casoCriticoTipo.getLabel());
            casoCritico.setTutor(dtoCargaAcademica.getGrupo().getTutor());

//            System.out.println("casoCritico = " + casoCritico.getTipo());
            ResultadoEJB<DtoCasoCriticoAlineacion> dtoCasoCriticoResultadoEJB = packer.packCasoCriticoAlineacion(casoCritico, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
//            System.out.println("dtoCasoCriticoResultadoEJB = " + dtoCasoCriticoResultadoEJB);

            if(!dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCriticoAlineacion.class);
            else return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Nuevo caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo generar un nuevo caso critico (EjbCasoCritico.generarNuevo).", DtoCasoCriticoAlineacion.class);
        }
    }
    
    /**
     * Permite crear una nueva instancia de caso crítico y empaquertarla asignando con cero como clave primaria del caso crítico para que al intentar registrar se detecte
     * como no persistida a la instancia, en caso de existir un caso critico abierto para el mismo estudiante, carga y unidad se devuelve el caso existente
     * para evitar abrir mas de un critico bajo las mismas condiciones
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param casoCriticoTipo Define el tipo de caso critico, si el nivel del tipo es de sistema solo devolverá un caso crítico existente que coincida con la unidad y el tipo, en caso de ser de tipo registrado por usuario, devolverá el primer caso crítico que encuentre
     * @return
     */
    public ResultadoEJB<DtoCasoCritico> generarNuevoDesdeTutoriaGrupal(DtoEstudiante dtoEstudiante, CasoCriticoTipo casoCriticoTipo, Integer tutor){
        try{
//            System.out.println("EjbCasoCritico.generarNuevo");
//            System.out.println("dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            ResultadoEJB<List<DtoCasoCritico>> identificar = identificarPorEsdudiante(dtoEstudiante.getInscripcionActiva().getInscripcion());
//            System.out.println("identificar = " + identificar);
            if(identificar.getCorrecto()) {
                if(casoCriticoTipo.getNivel() > 0d){//si el tipo caso crítico es registrado por el usuario se regresa como existente el primer caso crítico localizado
                    DtoCasoCritico dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().getNivel() > 0d)//solo contemplar casos críticos registrados por el usuario
                            .findFirst()
                            .orElse(null);
                    if(dtoCasoCritico1 != null) return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico existente.");
                }else{
                    DtoCasoCritico dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().equals(casoCriticoTipo))//solo contemplar casos criticos del tipo generado por el sistema
                            .findFirst()
                            .orElse(null);
                    if (dtoCasoCritico1 != null)
                        if(!dtoCasoCritico1.getCasoCritico().getEstado().equals(CasoCriticoEstado.CERRADO_ESPECIALISTA.getLabel()) && !dtoCasoCritico1.getCasoCritico().getEstado().equals(CasoCriticoEstado.CERRADO_TUTOR.getLabel()))
                            return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico lanzado por sistema existente.");
                }

            }
            //
            CasoCritico casoCritico = new CasoCritico(0);
            casoCritico.setCarga(null);
            casoCritico.setComentariosEspecialista(null);
            casoCritico.setComentariosTutor(null);
            casoCritico.setConfiguracion(null);
            casoCritico.setDescripcion(null);
            casoCritico.setEspecialista(null);
            casoCritico.setEstado(CasoCriticoEstado.SIN_REGISTRO.getLabel());
            casoCritico.setEvidenciaEspecialista(null);
            casoCritico.setEvidenciaTutor(null);
            casoCritico.setFechaCierre(null);
            casoCritico.setFechaRegistro(new Date());
            casoCritico.setIdEstudiante(dtoEstudiante.getInscripcionActiva().getInscripcion());
            casoCritico.setTipo(casoCriticoTipo.getLabel());
            casoCritico.setTutor(tutor);

//            System.out.println("casoCritico = " + casoCritico.getTipo());
            ResultadoEJB<DtoCasoCritico> dtoCasoCriticoResultadoEJB = packer.packCasoCriticoEstudiante(casoCritico, dtoEstudiante);
//            System.out.println("dtoCasoCriticoResultadoEJB = " + dtoCasoCriticoResultadoEJB);

            if(!dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCritico.class);
            else return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Nuevo caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo generar un nuevo caso critico (EjbCasoCritico.generarNuevoDesdeTutoriaGrupal).", DtoCasoCritico.class);
        }
    }
    
    /**
     * Permite crear una nueva instancia de caso crítico y empaquertarla asignando con cero como clave primaria del caso crítico para que al intentar registrar se detecte
     * como no persistida a la instancia, en caso de existir un caso critico abierto para el mismo estudiante, carga y unidad se devuelve el caso existente
     * para evitar abrir mas de un critico bajo las mismas condiciones
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param casoCriticoTipo Define el tipo de caso critico, si el nivel del tipo es de sistema solo devolverá un caso crítico existente que coincida con la unidad y el tipo, en caso de ser de tipo registrado por usuario, devolverá el primer caso crítico que encuentre
     * @return
     */
    public ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevoDesdeTutoriaGrupalAlineacion(DtoEstudiante dtoEstudiante, CasoCriticoTipo casoCriticoTipo, Integer tutor){
        try{
//            System.out.println("EjbCasoCritico.generarNuevo");
//            System.out.println("dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificar = identificarPorEsdudianteAlineacion(dtoEstudiante.getInscripcionActiva().getInscripcion());
//            System.out.println("identificar = " + identificar);
            if(identificar.getCorrecto()) {
                if(casoCriticoTipo.getNivel() > 0d){//si el tipo caso crítico es registrado por el usuario se regresa como existente el primer caso crítico localizado
                    DtoCasoCriticoAlineacion dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().getNivel() > 0d)//solo contemplar casos críticos registrados por el usuario
                            .findFirst()
                            .orElse(null);
                    if(dtoCasoCritico1 != null) return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico existente.");
                }else{
                    DtoCasoCriticoAlineacion dtoCasoCritico1 = identificar.getValor()
                            .stream()
                            .filter(dtoCasoCritico -> dtoCasoCritico.getTipo().equals(casoCriticoTipo))//solo contemplar casos criticos del tipo generado por el sistema
                            .findFirst()
                            .orElse(null);
                    if (dtoCasoCritico1 != null)
                        if(!dtoCasoCritico1.getCasoCritico().getEstado().equals(CasoCriticoEstado.CERRADO_ESPECIALISTA.getLabel()) && !dtoCasoCritico1.getCasoCritico().getEstado().equals(CasoCriticoEstado.CERRADO_TUTOR.getLabel()))
                            return ResultadoEJB.crearCorrecto(dtoCasoCritico1, "Caso crítico lanzado por sistema existente.");
                }

            }
            //
            CasoCritico casoCritico = new CasoCritico(0);
            casoCritico.setCarga(null);
            casoCritico.setComentariosEspecialista(null);
            casoCritico.setComentariosTutor(null);
            casoCritico.setConfiguracion(null);
            casoCritico.setDescripcion(null);
            casoCritico.setEspecialista(null);
            casoCritico.setEstado(CasoCriticoEstado.SIN_REGISTRO.getLabel());
            casoCritico.setEvidenciaEspecialista(null);
            casoCritico.setEvidenciaTutor(null);
            casoCritico.setFechaCierre(null);
            casoCritico.setFechaRegistro(new Date());
            casoCritico.setIdEstudiante(dtoEstudiante.getInscripcionActiva().getInscripcion());
            casoCritico.setTipo(casoCriticoTipo.getLabel());
            casoCritico.setTutor(tutor);

//            System.out.println("casoCritico = " + casoCritico.getTipo());
            ResultadoEJB<DtoCasoCriticoAlineacion> dtoCasoCriticoResultadoEJB = packer.packCasoCriticoEstudianteAlineacion(casoCritico, dtoEstudiante);
//            System.out.println("dtoCasoCriticoResultadoEJB = " + dtoCasoCriticoResultadoEJB);

            if(!dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCriticoAlineacion.class);
            else return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Nuevo caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo generar un nuevo caso critico (EjbCasoCritico.generarNuevoDesdeTutoriaGrupal).", DtoCasoCriticoAlineacion.class);
        }
    }

    /**
     * Crea o actualiza el caso critico
     * @param dtoCasoCritico Empaquetado del caso critico
     * @return Regresa el empaquetado del caso critico actualizado
     */
    public  ResultadoEJB<DtoCasoCritico> registrar(DtoCasoCritico dtoCasoCritico){
        try{
            //
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
//            System.out.println("dtoCasoCritico.getCasoCritico().getTipo() = " + dtoCasoCritico.getCasoCritico().getTipo());
//            System.out.println("dtoCasoCritico.getCasoCritico().getEstado() = " + dtoCasoCritico.getCasoCritico().getEstado());
            if(!existe) {
                dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.REGISTRADO.getLabel());
                em.persist(dtoCasoCritico.getCasoCritico());
            }
            else em.merge(dtoCasoCritico.getCasoCritico());

            ResultadoEJB<DtoCasoCritico> dtoCasoCriticoResultadoEJB = packer.packCasoCritico(dtoCasoCritico.getCasoCritico(), dtoCasoCritico.getDtoEstudiante(), dtoCasoCritico.getDtoCargaAcademica(), dtoCasoCritico.getDtoUnidadConfiguracion());

            if(dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Caso crítico registrado correctamente.");
            else return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCritico.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el caso crítico (EjbCasoCritico.registrarCasoCritico).", e, DtoCasoCritico.class);
        }
    }
    
    /**
     * Crea o actualiza el caso critico
     * @param dtoCasoCritico Empaquetado del caso critico
     * @return Regresa el empaquetado del caso critico actualizado
     */
    public  ResultadoEJB<DtoCasoCriticoAlineacion> registrarAlineacion(DtoCasoCriticoAlineacion dtoCasoCritico){
        try{
            //
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
//            System.out.println("dtoCasoCritico.getCasoCritico().getTipo() = " + dtoCasoCritico.getCasoCritico().getTipo());
//            System.out.println("dtoCasoCritico.getCasoCritico().getEstado() = " + dtoCasoCritico.getCasoCritico().getEstado());
            if(!existe) {
                dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.REGISTRADO.getLabel());
                em.persist(dtoCasoCritico.getCasoCritico());
            }
            else em.merge(dtoCasoCritico.getCasoCritico());

            ResultadoEJB<DtoCasoCriticoAlineacion> dtoCasoCriticoResultadoEJB = packer.packCasoCriticoAlineacion(dtoCasoCritico.getCasoCritico(), dtoCasoCritico.getDtoEstudiante(), dtoCasoCritico.getDtoCargaAcademica(), dtoCasoCritico.getDtoUnidadConfiguracion());

            if(dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Caso crítico registrado correctamente.");
            else return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCriticoAlineacion.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el caso crítico (EjbCasoCritico.registrarCasoCritico).", e, DtoCasoCriticoAlineacion.class);
        }
    }
    
    public  ResultadoEJB<DtoCasoCritico> registrarTG(DtoCasoCritico dtoCasoCritico){
        try{
            //
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
//            System.out.println("dtoCasoCritico.getCasoCritico().getTipo() = " + dtoCasoCritico.getCasoCritico().getTipo());
//            System.out.println("dtoCasoCritico.getCasoCritico().getEstado() = " + dtoCasoCritico.getCasoCritico().getEstado());
            if(!existe) {
                dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.REGISTRADO.getLabel());
                em.persist(dtoCasoCritico.getCasoCritico());
            }
            else em.merge(dtoCasoCritico.getCasoCritico());

            ResultadoEJB<DtoCasoCritico> dtoCasoCriticoResultadoEJB = packer.packCasoCriticoEstudiante(dtoCasoCritico.getCasoCritico(), dtoCasoCritico.getDtoEstudiante());

            if(dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Caso crítico registrado correctamente.");
            else return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCritico.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el caso crítico (EjbCasoCritico.registrarCasoCritico).", e, DtoCasoCritico.class);
        }
    }
    
     public  ResultadoEJB<DtoCasoCriticoAlineacion> registrarTG(DtoCasoCriticoAlineacion dtoCasoCritico){
        try{
            //
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
//            System.out.println("dtoCasoCritico.getCasoCritico().getTipo() = " + dtoCasoCritico.getCasoCritico().getTipo());
//            System.out.println("dtoCasoCritico.getCasoCritico().getEstado() = " + dtoCasoCritico.getCasoCritico().getEstado());
            if(!existe) {
                dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.REGISTRADO.getLabel());
                em.persist(dtoCasoCritico.getCasoCritico());
            }
            else em.merge(dtoCasoCritico.getCasoCritico());

            ResultadoEJB<DtoCasoCriticoAlineacion> dtoCasoCriticoResultadoEJB = packer.packCasoCriticoEstudianteAlineacion(dtoCasoCritico.getCasoCritico(), dtoCasoCritico.getDtoEstudiante());

            if(dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Caso crítico registrado correctamente.");
            else return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCriticoAlineacion.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el caso crítico (EjbCasoCritico.registrarCasoCritico).", e, DtoCasoCriticoAlineacion.class);
        }
    }
    
    /**
     * Actualiza el caso critico - Aplica para el seguimiento del tutor o especialista
     * @param dtoCasoCritico    Empaquetado del caso critico
     * @return  Regresa el empaquetado del caso critico actulizado
     */
    public ResultadoEJB<DtoCasoCritico> actualizarCasoCritico(DtoCasoCritico dtoCasoCritico){
        try {
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
            if(!existe){
                return ResultadoEJB.crearErroneo(2, "No se pudo actualizar el caso crítico debido a que no esta registrado en la base de datos", DtoCasoCritico.class);
            }else{
                if (dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_ESPECIALISTA.getNivel() && dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_TUTOR.getNivel()) {
                    em.merge(dtoCasoCritico.getCasoCritico());
                    return  ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico actualizado correctamente");
                }else{
                    return ResultadoEJB.crearErroneo(3, "No se puede actualizar un caso crítico debido a que ya esta cerrado", DtoCasoCritico.class);
                }
            } 
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el caso crítico (EjbCasoCritico.actualizarCasoCritico).", e, DtoCasoCritico.class);
        }
    }
    
    /**
     * Actualiza el caso critico - Aplica para el seguimiento del tutor o especialista
     * @param dtoCasoCritico    Empaquetado del caso critico
     * @return  Regresa el empaquetado del caso critico actulizado
     */
    public ResultadoEJB<DtoCasoCriticoAlineacion> actualizarCasoCriticoAlineacion(DtoCasoCriticoAlineacion dtoCasoCritico){
        try {
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
            if(!existe){
                return ResultadoEJB.crearErroneo(2, "No se pudo actualizar el caso crítico debido a que no esta registrado en la base de datos", DtoCasoCriticoAlineacion.class);
            }else{
                if (dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_ESPECIALISTA.getNivel() && dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_TUTOR.getNivel()) {
                    em.merge(dtoCasoCritico.getCasoCritico());
                    return  ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico actualizado correctamente");
                }else{
                    return ResultadoEJB.crearErroneo(3, "No se puede actualizar un caso crítico debido a que ya esta cerrado", DtoCasoCriticoAlineacion.class);
                }
            } 
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el caso crítico (EjbCasoCritico.actualizarCasoCritico).", e, DtoCasoCriticoAlineacion.class);
        }
    }
    
//    public ResultadoEJB<DtoCasoCritico> abrirCasoCriticoEspecialista(DtoCasoCritico dtoCasoCritico){
//        dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.EN_SEGUIMIENTO_ESPECIALISTA.getLabel());
//        dtoCasoCritico.getCasoCritico().setFechaCierre(null);
//        return abrirCasoCritico(dtoCasoCritico);
//    }
//    
//    public ResultadoEJB<DtoCasoCritico> abrirCasoCriticoTutor(DtoCasoCritico dtoCasoCritico){
//        dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.EN_SEGUMIENTO_TUTOR.getLabel());
//        dtoCasoCritico.getCasoCritico().setFechaCierre(null);
//        return abrirCasoCritico(dtoCasoCritico);
//    }
    
    public ResultadoEJB<DtoCasoCritico> abrirCasoCritico(DtoCasoCritico dtoCasoCritico){
        try {
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
            if(!existe){
                return ResultadoEJB.crearErroneo(2, "No se pudo actualizar el caso crítico debido a que no esta registrado en la base de datos", DtoCasoCritico.class);
            }else{
                em.merge(dtoCasoCritico.getCasoCritico());    
                return  ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico actualizado correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el caso crítico (EjbCasoCritico.actualizarCasoCritico).", e, DtoCasoCritico.class);
        }
    }
    
    public ResultadoEJB<DtoCasoCriticoAlineacion> abrirCasoCriticoAlineacion(DtoCasoCriticoAlineacion dtoCasoCritico){
        try {
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
            if(!existe){
                return ResultadoEJB.crearErroneo(2, "No se pudo actualizar el caso crítico debido a que no esta registrado en la base de datos", DtoCasoCriticoAlineacion.class);
            }else{
                em.merge(dtoCasoCritico.getCasoCritico());    
                return  ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico actualizado correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el caso crítico (EjbCasoCritico.actualizarCasoCritico).", e, DtoCasoCriticoAlineacion.class);
        }
    }

    /**
     * Permite eliminar un caso crítico validando si es que aun no se encuentra en seguimiento.
     * @param dtoCasoCritico Empaquetado del caso crítico que se pretende eliminar
     * @return Regresa TRUE si se elimina correctamente, código de error 2 cuando el caso crítico ya se encuentra en seguimiento y código 1 para error inesperado
     */
    public ResultadoEJB<Boolean> eliminar(DtoCasoCritico dtoCasoCritico){
//        System.out.println("EjbCasoCritico.eliminar");
//        System.out.println("dtoCasoCritico = [" + dtoCasoCritico + "]");
        try{
            if(dtoCasoCritico.getEstado().getNivel() < CasoCriticoEstado.EN_SEGUMIENTO_TUTOR.getNivel()) {
                CasoCritico casoCritico = em.find(CasoCritico.class, dtoCasoCritico.getCasoCritico().getCaso());
//                System.out.println("casoCritico = " + casoCritico);
                em.remove(casoCritico);
                em.flush();
                casoCritico = em.find(CasoCritico.class, dtoCasoCritico.getCasoCritico().getCaso());
//                System.out.println("casoCritico = " + casoCritico);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Caso crítico eliminado.");
            }
            else return ResultadoEJB.crearErroneo(2, "El caso crítico ya no se puede eliminar debido a que ya se encuentra en seguimiento por el tutor o por un especialista", Boolean.TYPE);
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el caso crítico (EjbCasoCritico.eliminar).", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite eliminar un caso crítico validando si es que aun no se encuentra en seguimiento.
     * @param dtoCasoCritico Empaquetado del caso crítico que se pretende eliminar
     * @return Regresa TRUE si se elimina correctamente, código de error 2 cuando el caso crítico ya se encuentra en seguimiento y código 1 para error inesperado
     */
    public ResultadoEJB<Boolean> eliminarAlineacion(DtoCasoCriticoAlineacion dtoCasoCritico){
//        System.out.println("EjbCasoCritico.eliminar");
//        System.out.println("dtoCasoCritico = [" + dtoCasoCritico + "]");
        try{
            if(dtoCasoCritico.getEstado().getNivel() < CasoCriticoEstado.EN_SEGUMIENTO_TUTOR.getNivel()) {
                CasoCritico casoCritico = em.find(CasoCritico.class, dtoCasoCritico.getCasoCritico().getCaso());
//                System.out.println("casoCritico = " + casoCritico);
                em.remove(casoCritico);
                em.flush();
                casoCritico = em.find(CasoCritico.class, dtoCasoCritico.getCasoCritico().getCaso());
//                System.out.println("casoCritico = " + casoCritico);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Caso crítico eliminado.");
            }
            else return ResultadoEJB.crearErroneo(2, "El caso crítico ya no se puede eliminar debido a que ya se encuentra en seguimiento por el tutor o por un especialista", Boolean.TYPE);
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el caso crítico (EjbCasoCritico.eliminar).", e, Boolean.TYPE);
        }
    }

    /**
     * Permite disparar un caso crítico automático al generar un promedio reprobatorio por unidad
     * @param dtoCapturaCalificacion Empaquetado de la captura de calificación para detectar si es reprobatorio o no.
     * @return Regresa el empaquetado del caso crítico posterior a comprobar si es calificación reprobatoria o no. <br/>
     * El código 1 es un error inesperado.<br/>
     * El código 2 es que no se pudo regostrar o crear un nuevo caso crítico por medio del método generarNuevo.<br/>
     * El  código 3 implica que no se pudo eliminar un caso crítico registrado previamente como reprobatorio y ahora se tiene una calificación aprobatoria.<br/>
     * El código 4 implica que el promedio es aprobatorio, por lo que no se puede registrar el caso crítico por reprobación.
     */
    public ResultadoEJB<DtoCasoCritico> registrarPorReprobacion(DtoCapturaCalificacion dtoCapturaCalificacion){
        try{
//            System.out.println("EjbCasoCritico.registrarPorReprobacion");
            if(dtoCapturaCalificacion.getPromedio().compareTo(ejbCapturaCalificaciones.leerCalificacionMínimaAprobatoria()) < 0){
//                System.out.println("Es reprobatorio");
                ResultadoEJB<Boolean> verificarCapturaCompleta = ejbCapturaCalificaciones.verificarCapturaCompleta(dtoCapturaCalificacion);
                if(!verificarCapturaCompleta.getCorrecto()){
                    return ResultadoEJB.crearErroneo(5, "La captura de la calificaciones de la unidad aún no está completa.", DtoCasoCritico.class);
                }

                ResultadoEJB<DtoCasoCritico> generarNuevo = generarNuevo(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA);
                if(generarNuevo.getCorrecto()){
                    DtoCasoCritico dtoCasoCritico = generarNuevo.getValor();
                    String unidad = String.valueOf(dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateria().getNoUnidad());
                    String materia = dtoCapturaCalificacion.getDtoUnidadConfiguracion().getDtoCargaAcademica().getMateria().getNombre();
                    String calificacion = dtoCapturaCalificacion.getPromedio().toString();
                    dtoCasoCritico.getCasoCritico().setDescripcion(String.format("Sistema: unidad -%s- de la materia -%s- con la calificación reprobatoria -%s-", unidad, materia, calificacion));
                    dtoCasoCritico.getCasoCritico().setTipo(CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA.getLabel());
                    ResultadoEJB<DtoCasoCritico> registrar = registrar(dtoCasoCritico);
                    if(registrar.getCorrecto()) cargarCasosCriticos(dtoCapturaCalificacion);
//                        System.out.println("dtoCapturaCalificacion.getTieneCasoCriticoSistema() = " + dtoCapturaCalificacion.getTieneCasoCriticoSistema());
//                        System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
                    return registrar;
                }else return ResultadoEJB.crearErroneo(2, "No se pudo registrar de forma automática el caso crítico correspondiente a una calificación reprobatoria por unidad.", DtoCasoCritico.class);
            }else {
//                System.out.println("No es reprobatorio");
                if(dtoCapturaCalificacion.getTieneCasoCriticoSistema()){
                    /*System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
                    DtoCasoCritico dtoCasoCritico = dtoCapturaCalificacion.getCasosCriticosSistema().get(CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA);
                    System.out.println("dtoCasoCritico = " + dtoCasoCritico);
                    if(dtoCasoCritico != null){
                        ResultadoEJB<Boolean> eliminar = eliminar(dtoCasoCritico);
                        if(!eliminar.getCorrecto()) return ResultadoEJB.crearErroneo(3, eliminar.getMensaje(), DtoCasoCritico.class);
                    }*/
                    ResultadoEJB<List<DtoCasoCritico>> identificar = identificar(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion());
                    if(identificar.getCorrecto()){
                        List<DtoCasoCritico> dtoCasoCriticos = identificar.getValor()
                                .stream()
                                .filter(dtoCasoCritico1 -> dtoCasoCritico1.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().equals(dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion()))
                                .filter(dtoCasoCritico1 -> dtoCasoCritico1.getTipo().equals(CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA))
                                .collect(Collectors.toList());

                        dtoCasoCriticos
                                .forEach(dtoCasoCritico1 -> {
                                    ResultadoEJB<Boolean> eliminar = eliminar(dtoCasoCritico1);
                                    if(eliminar.getValor()){
                                        cargarCasosCriticos(dtoCapturaCalificacion);
                                    }
                                });
//                        System.out.println("dtoCapturaCalificacion.getTieneCasoCriticoSistema() = " + dtoCapturaCalificacion.getTieneCasoCriticoSistema());
//                        System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
                    }else return ResultadoEJB.crearErroneo(5, "No se encontró caso crítico por reprobación a eliminar.", DtoCasoCritico.class);
                }
                return ResultadoEJB.crearErroneo(4, "No se puede registrar caso crítico por reprobación ya que el promedio es aprobatorio.", DtoCasoCritico.class);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar de forma automática el caso crítico correspondiente a una calificación reprobatoria por unidad (EjbCasoCritico.registrarPorReprobacion).", e, DtoCasoCritico.class);
        }
    }
    
    /**
     * Permite disparar un caso crítico automático al generar un promedio reprobatorio por unidad
     * @param dtoCapturaCalificacion Empaquetado de la captura de calificación para detectar si es reprobatorio o no.
     * @return Regresa el empaquetado del caso crítico posterior a comprobar si es calificación reprobatoria o no. <br/>
     * El código 1 es un error inesperado.<br/>
     * El código 2 es que no se pudo regostrar o crear un nuevo caso crítico por medio del método generarNuevo.<br/>
     * El  código 3 implica que no se pudo eliminar un caso crítico registrado previamente como reprobatorio y ahora se tiene una calificación aprobatoria.<br/>
     * El código 4 implica que el promedio es aprobatorio, por lo que no se puede registrar el caso crítico por reprobación.
     */
    public ResultadoEJB<DtoCasoCriticoAlineacion> registrarPorReprobacionAlineacion(DtoCapturaCalificacionAlineacion dtoCapturaCalificacion){
        try{
//            System.out.println("EjbCasoCritico.registrarPorReprobacion");
            if(dtoCapturaCalificacion.getPromedio().compareTo(ejbCapturaCalificaciones.leerCalificacionMínimaAprobatoria()) < 0 && dtoCapturaCalificacion.getPromedio().compareTo(BigDecimal.ZERO) != 0){
//                System.out.println("Es reprobatorio");
                ResultadoEJB<Boolean> verificarCapturaCompleta = ejbCapturaCalificaciones.verificarCapturaCompletaAlineacion(dtoCapturaCalificacion);
                if(!verificarCapturaCompleta.getCorrecto()){
                    return ResultadoEJB.crearErroneo(5, "La captura de la calificaciones de la unidad aún no está completa.", DtoCasoCriticoAlineacion.class);
                }

                ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevo = generarNuevoAlineacion(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA);
                if(generarNuevo.getCorrecto()){
                    DtoCasoCriticoAlineacion dtoCasoCritico = generarNuevo.getValor();
                    String unidad = String.valueOf(dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateria().getNoUnidad());
                    String materia = dtoCapturaCalificacion.getDtoUnidadConfiguracion().getDtoCargaAcademica().getMateria().getNombre();
                    String calificacion = dtoCapturaCalificacion.getPromedio().toString();
                    dtoCasoCritico.getCasoCritico().setDescripcion(String.format("Sistema: unidad -%s- de la materia -%s- con la calificación reprobatoria -%s-", unidad, materia, calificacion));
                    dtoCasoCritico.getCasoCritico().setTipo(CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA.getLabel());
                    ResultadoEJB<DtoCasoCriticoAlineacion> registrar = registrarAlineacion(dtoCasoCritico);
                    if(registrar.getCorrecto()) cargarCasosCriticosAlineacion(dtoCapturaCalificacion);
//                        System.out.println("dtoCapturaCalificacion.getTieneCasoCriticoSistema() = " + dtoCapturaCalificacion.getTieneCasoCriticoSistema());
//                        System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
                    return registrar;
                }else return ResultadoEJB.crearErroneo(2, "No se pudo registrar de forma automática el caso crítico correspondiente a una calificación reprobatoria por unidad.", DtoCasoCriticoAlineacion.class);
            }else {
//                System.out.println("No es reprobatorio");
                if(dtoCapturaCalificacion.getTieneCasoCriticoSistema()){
                    
                    ResultadoEJB<List<DtoCasoCriticoAlineacion>> identificar = identificarAlineacion(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion());
                    if(identificar.getCorrecto()){
                        List<DtoCasoCriticoAlineacion> dtoCasoCriticos = identificar.getValor()
                                .stream()
                                .filter(dtoCasoCritico1 -> dtoCasoCritico1.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().equals(dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion()))
                                .filter(dtoCasoCritico1 -> dtoCasoCritico1.getTipo().equals(CasoCriticoTipo.SISTEMA_UNIDAD_REPROBADA))
                                .collect(Collectors.toList());

                        dtoCasoCriticos
                                .forEach(dtoCasoCritico1 -> {
                                    ResultadoEJB<Boolean> eliminar = eliminarAlineacion(dtoCasoCritico1);
                                    if(eliminar.getValor()){
                                        cargarCasosCriticosAlineacion(dtoCapturaCalificacion);
                                    }
                                });
//                        System.out.println("dtoCapturaCalificacion.getTieneCasoCriticoSistema() = " + dtoCapturaCalificacion.getTieneCasoCriticoSistema());
//                        System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
                    }else return ResultadoEJB.crearErroneo(5, "No se encontró caso crítico por reprobación a eliminar.", DtoCasoCriticoAlineacion.class);
                }
                return ResultadoEJB.crearErroneo(4, "No se puede registrar caso crítico por reprobación ya que el promedio es aprobatorio.", DtoCasoCriticoAlineacion.class);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar de forma automática el caso crítico correspondiente a una calificación reprobatoria por unidad (EjbCasoCritico.registrarPorReprobacion).", e, DtoCasoCriticoAlineacion.class);
        }
    }

    public ResultadoEJB<Boolean> cargarCasosCriticos(DtoCapturaCalificacion dtoCapturaCalificacion){
//        System.out.println("EjbCasoCritico.cargarCasosCriticos");
        try{
            //limpiar casos críticos
            dtoCapturaCalificacion.setTieneCasoCritico(false);
            dtoCapturaCalificacion.setDtoCasoCritico(null);
            dtoCapturaCalificacion.setTieneCasoCriticoSistema(false);
            dtoCapturaCalificacion.setCasosCriticosSistema(new HashMap<>());

            //identificar caso crítico abierto mas reciente registrado por el usuario
            ResultadoEJB<DtoCasoCritico> generarNuevo = generarNuevo(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.ASISTENCIA_IRREGURLAR);
//            System.out.println("generarNuevo = " + generarNuevo);
            if(generarNuevo.getCorrecto()) {
                DtoCasoCritico dtoCasoCritico = generarNuevo.getValor();
                if(dtoCasoCritico.getEstado().getNivel() > 0){
                    dtoCapturaCalificacion.setDtoCasoCritico(generarNuevo.getValor());
                    dtoCapturaCalificacion.setTieneCasoCritico(true);
                }
            }

            //identificar casos críticos  generados por sistema
            CasoCriticoTipo.ListaSistema().forEach(casoCriticoTipo -> {
//                System.out.println("casoCriticoTipo = " + casoCriticoTipo);
                ResultadoEJB<DtoCasoCritico> generarNuevo1 = generarNuevo(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), casoCriticoTipo);
//                System.out.println("generarNuevo1 = " + generarNuevo1);
                if(generarNuevo1.getCorrecto() && !Objects.equals(0, generarNuevo1.getValor().getCasoCritico().getCaso())){
                    DtoCasoCritico dtoCasoCritico = generarNuevo1.getValor();
//                    System.out.println("dtoCasoCritico = " + dtoCasoCritico);
                    if(dtoCasoCritico.getEstado().getNivel() > 0){
//                        System.out.println("dtoCasoCritico.getCasoCritico() = " + dtoCasoCritico.getCasoCritico());
                        dtoCapturaCalificacion.getCasosCriticosSistema().put(casoCriticoTipo, generarNuevo1.getValor());
                        dtoCapturaCalificacion.setTieneCasoCriticoSistema(true);
                    }
                }
            });
//            System.out.println("dtoCapturaCalificacion.getTieneCasoCriticoSistema() = " + dtoCapturaCalificacion.getTieneCasoCriticoSistema());
//            System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
            return ResultadoEJB.crearCorrecto(true, "Casos críticos cargados.");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> cargarCasosCriticosAlineacion(DtoCapturaCalificacionAlineacion dtoCapturaCalificacion){
//        System.out.println("EjbCasoCritico.cargarCasosCriticos");
        try{
            //limpiar casos críticos
            dtoCapturaCalificacion.setTieneCasoCritico(false);
            dtoCapturaCalificacion.setDtoCasoCritico(null);
            dtoCapturaCalificacion.setTieneCasoCriticoSistema(false);
            dtoCapturaCalificacion.setCasosCriticosSistema(new HashMap<>());

            //identificar caso crítico abierto mas reciente registrado por el usuario
            ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevo = generarNuevoAlineacion(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.ASISTENCIA_IRREGURLAR);
//            System.out.println("generarNuevo = " + generarNuevo);
            if(generarNuevo.getCorrecto()) {
                DtoCasoCriticoAlineacion dtoCasoCritico = generarNuevo.getValor();
                if(dtoCasoCritico.getEstado().getNivel() > 0){
                    dtoCapturaCalificacion.setDtoCasoCritico(generarNuevo.getValor());
                    dtoCapturaCalificacion.setTieneCasoCritico(true);
                }
            }

            //identificar casos críticos  generados por sistema
            CasoCriticoTipo.ListaSistema().forEach(casoCriticoTipo -> {
//                System.out.println("casoCriticoTipo = " + casoCriticoTipo);
                ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevo1 = generarNuevoAlineacion(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), casoCriticoTipo);
//                System.out.println("generarNuevo1 = " + generarNuevo1);
                if(generarNuevo1.getCorrecto() && !Objects.equals(0, generarNuevo1.getValor().getCasoCritico().getCaso())){
                    DtoCasoCriticoAlineacion dtoCasoCritico = generarNuevo1.getValor();
//                    System.out.println("dtoCasoCritico = " + dtoCasoCritico);
                    if(dtoCasoCritico.getEstado().getNivel() > 0){
//                        System.out.println("dtoCasoCritico.getCasoCritico() = " + dtoCasoCritico.getCasoCritico());
                        dtoCapturaCalificacion.getCasosCriticosSistema().put(casoCriticoTipo, generarNuevo1.getValor());
                        dtoCapturaCalificacion.setTieneCasoCriticoSistema(true);
                    }
                }
            });
//            System.out.println("dtoCapturaCalificacion.getTieneCasoCriticoSistema() = " + dtoCapturaCalificacion.getTieneCasoCriticoSistema());
//            System.out.println("dtoCapturaCalificacion.getCasosCriticosSistema() = " + dtoCapturaCalificacion.getCasosCriticosSistema());
            return ResultadoEJB.crearCorrecto(true, "Casos críticos cargados.");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<DtoCasoCritico> registrarPorAsistenciaIrregular(DtoCapturaCalificacion dtoCapturaCalificacion, Double porcentDouble) {
        try {
            List<CasoCritico> ccs = em.createQuery("SELECT cc FROM CasoCritico cc WHERE cc.idEstudiante.idEstudiante = :idEstudiante AND cc.tipo=:tipo", CasoCritico.class)
                    .setParameter("idEstudiante", dtoCapturaCalificacion.getDtoEstudiante().getInscripcionActiva().getInscripcion().getIdEstudiante())
                    .setParameter("tipo", CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR.getLabel()).getResultList();
            List<CasoCritico> ccs2 = new ArrayList<>();
            if (!ccs.isEmpty()) {
                ccs2 = ccs.stream().filter(t -> Objects.equals(t.getCarga().getCarga(), dtoCapturaCalificacion.getDtoCargaAcademica().getCargaAcademica().getCarga())
                        && Objects.equals(t.getConfiguracion().getConfiguracion(), dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().getConfiguracion())).collect(Collectors.toList());
            }

            if ((ccs2.isEmpty()) && (porcentDouble < 80.0)) {
                ResultadoEJB<DtoCasoCritico> generarNuevo = generarNuevo(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR);
                if (generarNuevo.getCorrecto()) {
                    DtoCasoCritico dtoCasoCritico = generarNuevo.getValor();
                    String alumno = dtoCapturaCalificacion.getDtoEstudiante().getPersona().getApellidoPaterno() + " " + dtoCapturaCalificacion.getDtoEstudiante().getPersona().getApellidoMaterno() + " " + dtoCapturaCalificacion.getDtoEstudiante().getPersona().getNombre();
                    String grupo = dtoCapturaCalificacion.getDtoEstudiante().getInscripcionActiva().getGrupo().getGrado() + "° " + dtoCapturaCalificacion.getDtoEstudiante().getInscripcionActiva().getGrupo().getLiteral();
                    String carrera = dtoCapturaCalificacion.getDtoCargaAcademica().getPlanEstudio().getDescripcion();
                    dtoCasoCritico.getCasoCritico().setDescripcion(String.format("Sistema: El Alumno -%s- del grupo -%s- de la carrera -%s- no asiste a clases", alumno, grupo, carrera));
                    dtoCasoCritico.getCasoCritico().setTipo(CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR.getLabel());
                    ResultadoEJB<DtoCasoCritico> registrar = registrar(dtoCasoCritico);
                    return registrar;
                } else {
                    return ResultadoEJB.crearErroneo(2, "No se pudo registrar de forma automática el caso crítico correspondiente a una Asistencia irregular.", DtoCasoCritico.class);
                }
            } else if ((!ccs2.isEmpty()) && (porcentDouble > 80.0)) {
                DtoCasoCritico dtoCasoCritico = dtoCapturaCalificacion.getCasosCriticosSistema().get(CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR);
                if (dtoCasoCritico != null) {
                    ResultadoEJB<Boolean> eliminar = eliminar(dtoCasoCritico);
                    if (!eliminar.getCorrecto()) {
                        return ResultadoEJB.crearErroneo(3, eliminar.getMensaje(), DtoCasoCritico.class);
                    }
                }
                return ResultadoEJB.crearErroneo(4, "No se puede registrar caso crítico por Asistencia irregular ya que el Alumno asistio a clases.", DtoCasoCritico.class);
            } else {
                return ResultadoEJB.crearErroneo(4, "Ya existe un registro de caso crítico por Asistencia irregular", DtoCasoCritico.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar de forma automática el caso crítico correspondiente a una Asistencia irregular (EjbCasoCritico.registrarPorReprobacion).", e, DtoCasoCritico.class);
        }
    }
    
    public ResultadoEJB<DtoCasoCriticoAlineacion> registrarPorAsistenciaIrregularAlineacion(DtoCapturaCalificacionAlineacion dtoCapturaCalificacion, Double porcentDouble) {
        try {
            List<CasoCritico> ccs = em.createQuery("SELECT cc FROM CasoCritico cc WHERE cc.idEstudiante.idEstudiante = :idEstudiante AND cc.tipo=:tipo", CasoCritico.class)
                    .setParameter("idEstudiante", dtoCapturaCalificacion.getDtoEstudiante().getInscripcionActiva().getInscripcion().getIdEstudiante())
                    .setParameter("tipo", CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR.getLabel()).getResultList();
            List<CasoCritico> ccs2 = new ArrayList<>();
            if (!ccs.isEmpty()) {
                ccs2 = ccs.stream().filter(t -> Objects.equals(t.getCarga().getCarga(), dtoCapturaCalificacion.getDtoCargaAcademica().getCargaAcademica().getCarga())
                        && Objects.equals(t.getConfiguracion().getConfiguracion(), dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().getConfiguracion())).collect(Collectors.toList());
            }

            if ((ccs2.isEmpty()) && (porcentDouble < 80.0)) {
                ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevo = generarNuevoAlineacion(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR);
                if (generarNuevo.getCorrecto()) {
                    DtoCasoCriticoAlineacion dtoCasoCritico = generarNuevo.getValor();
                    String alumno = dtoCapturaCalificacion.getDtoEstudiante().getPersona().getApellidoPaterno() + " " + dtoCapturaCalificacion.getDtoEstudiante().getPersona().getApellidoMaterno() + " " + dtoCapturaCalificacion.getDtoEstudiante().getPersona().getNombre();
                    String grupo = dtoCapturaCalificacion.getDtoEstudiante().getInscripcionActiva().getGrupo().getGrado() + "° " + dtoCapturaCalificacion.getDtoEstudiante().getInscripcionActiva().getGrupo().getLiteral();
                    String carrera = dtoCapturaCalificacion.getDtoCargaAcademica().getPlanEstudio().getDescripcion();
                    dtoCasoCritico.getCasoCritico().setDescripcion(String.format("Sistema: El Alumno -%s- del grupo -%s- de la carrera -%s- no asiste a clases", alumno, grupo, carrera));
                    dtoCasoCritico.getCasoCritico().setTipo(CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR.getLabel());
                    ResultadoEJB<DtoCasoCriticoAlineacion> registrar = registrarAlineacion(dtoCasoCritico);
                    return registrar;
                } else {
                    return ResultadoEJB.crearErroneo(2, "No se pudo registrar de forma automática el caso crítico correspondiente a una Asistencia irregular.", DtoCasoCriticoAlineacion.class);
                }
            } else if ((!ccs2.isEmpty()) && (porcentDouble > 80.0)) {
                DtoCasoCriticoAlineacion dtoCasoCritico = dtoCapturaCalificacion.getCasosCriticosSistema().get(CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR);
                if (dtoCasoCritico != null) {
                    ResultadoEJB<Boolean> eliminar = eliminarAlineacion(dtoCasoCritico);
                    if (!eliminar.getCorrecto()) {
                        return ResultadoEJB.crearErroneo(3, eliminar.getMensaje(), DtoCasoCriticoAlineacion.class);
                    }
                }
                return ResultadoEJB.crearErroneo(4, "No se puede registrar caso crítico por Asistencia irregular ya que el Alumno asistio a clases.", DtoCasoCriticoAlineacion.class);
            } else {
                return ResultadoEJB.crearErroneo(4, "Ya existe un registro de caso crítico por Asistencia irregular", DtoCasoCriticoAlineacion.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar de forma automática el caso crítico correspondiente a una Asistencia irregular (EjbCasoCritico.registrarPorReprobacion).", e, DtoCasoCriticoAlineacion.class);
        }
    }

    public ResultadoEJB<List<PeriodosEscolares>> consultaPeriodosEscolaresCasoCriticoEstudiante(Integer matricula) {
        try {
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT cc.idEstudiante.periodo FROM CasoCritico cc WHERE cc.idEstudiante.matricula = :matricula", Integer.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if (periodosEscolares.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han podido recuperar periodos escolars debido a que el estudiante no cuenta con casos críticos");
            } else {
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares en donde el estudiante ha tenido caso críticos");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares (EjbCasoCritico.consultaPeriodosEscolares)", e, null);
        }
    }
}
