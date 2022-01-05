/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.AsentamientoPK;
import mx.edu.utxj.pye.sgi.enums.SeguroFacultativoValidacion;
import mx.edu.utxj.pye.sgi.enums.SegurosFacultativosEstatus;
import mx.edu.utxj.pye.sgi.enums.converter.SeguroFacultativoValidacionConverter;
import mx.edu.utxj.pye.sgi.enums.converter.SegurosFacultativosEstatusConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbValidacionSeguroFacultativo")
public class EjbValidacionSeguroFacultativo {
    @EJB                EjbValidacionRol                                ejbValidacionRol;
    @EJB                EjbPropiedades                                  ep;
    @EJB                Facade                                          f;
    @EJB                EjbPacker                                       pack;
    @EJB                mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean         ejbPersonalBean;
    @EJB                EjbRegistroSeguroFacultativoEstudiante          ejbSeguroFacultativoEstudiante;
    @Inject             Caster                                          caster;
    
    private             EntityManager                                   em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite validar que solo los usuarios del área de servicios estudiantiles asignados a la categoría de enfermería puedan acceder al módulo de consulta y validación
     * @param clave
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEnfermeria(Integer clave){
        try{
           if(clave == null) return ResultadoEJB.crearErroneo(2, null, "No se puede comprobar un usuario si la clave de trabajador es nula");
           return ejbValidacionRol.validarEnfermeria(clave);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo comprobar como personal de Enfermería del área de Servicios Estudiantiles. (EjbValidacionSeguroFacultativo.validarEnfermeria)", e, null);
        }
    }
    
    /**
     * Permite que solo el jefe de departamento de servicios estudiantiles pueda acceder módulo de consulta y validación
     * @param clave
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarJefeDepartamentoEstudiantiles(Integer clave){
        try{
            if(clave == null) return ResultadoEJB.crearErroneo(2, null, "No se puede comprobar un usuario si la clave de trabajador es nula");
            return ejbValidacionRol.validarJefeDepartamentoEstudiantiles(clave);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo comprobar como Jefe de Departamento del Área de Servicios Estudiantiles. (EjbValidacionSeguroFacultativo.validarJefeDepartamentoEstudiantiles)", e, null);
        }
    }
    
    /**
     * Permite que solo el encargado de departamento servicios estudiantiles pueda acceder al módulo de consulta y validación
     * @param clave
     * @param areaOperativa
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDepartamentoEstudiantiles(Integer clave){
        try{
            if(clave == null) return ResultadoEJB.crearErroneo(2, null, "No se puede comprobar un usuario si la clave de trabajador es nula");
            return ejbValidacionRol.validarEncargadoDepartamentoEstudiantiles(clave);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo comprobar como Encargado del Departamento de Servicios Estudiantiles. (EjbValidacionSeguroFacultativo.validarEncargadoDepartamentoEstudiantiles)", e, null);
        }
    }
    
//    Permitir busquedas por Periodo, ProgramaEducativo, Grupo
    
    /**
     * Permite obtener una lista de periodos escolares en las cuales ha habido registros de tipo Seguro Facultativo
     * @return Lista de Tipo Periodos Escolares
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresConSF() {
        try {
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT DISTINCT sf FROM SegurosFacultativosEstudiante sf", SegurosFacultativosEstudiante.class)
                    .getResultStream()
                    .map(seguroFacultativo -> seguroFacultativo.getEstudiante().getPeriodo())
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if (periodosEscolares.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se pudo obtener la lista de periodos escolares, debido a que aún no hay registros de Seguros Facultativos en el sistema.");
            } else {
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares, en las que hay registros de tipo Seguro Facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en la que se han tenido registros de Seguros Facultativos. (EjbValidacionSeguroFacultativo.obtenerPeriodosEscolaresConSegurosFacultativos())", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de programas educativos en las cuales ha habido registros de tipo Seguro Facultativo con un solo filtro por periodo
     * @param periodo 
     * @return Lista de tipo Áreas Universidad
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativosPorPeriodoSF(PeriodosEscolares periodo){
        try {
            if(periodo == null) return ResultadoEJB.crearErroneo(3, null, "No se pueden consultar programas educativos si el periodo escolar es nulo");
            List<AreasUniversidad> programasEducativos = em.createQuery("SELECT DISTINCT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.periodo = :periodo ORDER BY e.carrera ASC", SegurosFacultativosEstudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(seguroFacultativo -> seguroFacultativo.getEstudiante().getCarrera())
                    .map(programaEducativo -> em.find(AreasUniversidad.class, programaEducativo))
                    .distinct()
                    .sorted((au1, au2) -> au1.getNivelEducativo().getNivel().compareTo(au2.getNivelEducativo().getNivel()))
                    .collect(Collectors.toList());
            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "No se pudo obtener la lista de peridos escolares, debido a que aún no hay registros de Seguros Facultativos en el sistema.");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos, en las que hay registros de tipo Seguro Facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos en la que se han tenido registros de Seguros Facultativos. (EjbValidacionSeguroFacultativo.obtenerProgramasEducativosPorPeriodoSF)", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de grupos en las cuales ha habido registros de tipo seguro facultativo con filtro de Periodo y Programa Educativo
     * @param periodo
     * @param programaEducativo Lista de Grupos
     * @return 
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposPorPeriodoProgramaEducativoSF(PeriodosEscolares periodo, AreasUniversidad programaEducativo){
        try {
            if(periodo == null) return ResultadoEJB.crearErroneo(2, null, "No se pueden consultar grupos si el periodo escolar es nulo");
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(3, null, "No se pueden consultar registros de Seguro Facultativo si el periodo escolar es nulo");
            List<Grupo> listaGruposSf = em.createQuery("SELECT DISTINCT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.periodo = :periodo AND e.carrera = :programaEducativo", SegurosFacultativosEstudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultStream()
                    .map(seguroFacultativo -> seguroFacultativo.getEstudiante().getGrupo().getIdGrupo())
                    .map(grupo -> em.find(Grupo.class, grupo))
                    .distinct()
                    .sorted((l1, l2) -> l1.getLiteral().compareTo(l2.getLiteral()))
                    .collect(Collectors.toList());
            if(listaGruposSf.isEmpty()){
                return ResultadoEJB.crearErroneo(4, null, "No se pudo obtener la lista de grupos debido a que aún no hay registtos de Seguros Facultativos en el sistema ó el estudiante no pertenece al programa educativo permitido consultar.");
            }else{
                return ResultadoEJB.crearCorrecto(listaGruposSf, "Lista de grupos en las que hay registros de tipo Seguro Facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos en la que se han tenido registros de Seguro Facultativo. (EjbValidacionSeguroFacultativo.obtenerGruposPorPeriodoProgramaEducativoSF)", e, null);
        }
    }
    
    /**
     * Permite obtener una lista de tipo DtoSeguroFacultativo,  filtrada por periodo escolar, programa educativo y grupo
     * Está lista se ocupará para filtrar en la interfaz los tipos de seguros facultativos (En espera de validación, Alta, Baja)
     * @param periodoEscolar
     * @param programaEducativo
     * @param grupo
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSFPorPeriodoProgramaEducativoGrupo(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo, Grupo grupo){
        try {
            if(periodoEscolar == null) return ResultadoEJB.crearErroneo(3, null, "No se pueden consultar registros de seguros facultativo si el periodo escoalar es nulo");
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(4, null, "No se pueden consultar registros de seguros facultativos si el programa educativo es nulo");
            if(grupo == null) return ResultadoEJB.crearErroneo(5, null, "No se pueden consultar registros de seguros facultativo si el grupo es nulo");
            List<DtoSeguroFacultativo> listaSegurosFacultatativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.grupo g WHERE e.periodo = :periodoEscolar AND e.carrera = :programaEducativo AND g.idGrupo = :grupo ORDER BY sf.seguroFacultativoEstudiante ASC", SegurosFacultativosEstudiante.class)
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> ejbSeguroFacultativoEstudiante.packSeguroFacultativo(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(listaSegurosFacultatativos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "No se pudo obtener la lista de Seguros Facultativos, debido a que aún no hay registros en el sistema");
            }else{
                return ResultadoEJB.crearCorrecto(listaSegurosFacultatativos, "Lista de seguros facultativos filtrada por periodo escolar, programa educativo y grupo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros de seguros facultativos filtrada por periodo y programa educativo. (EjbValidacionSeguroFacultativo.obtenerSegurosFacultativosPorPeriodoProgramaEducativo)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de Seguros Facultativos registrados por un estudiante.
     * Se utiliza para agilizar el proceso de búsqueda.
     * @param matricula
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSeguroFacultativoPorEstudiante(Integer matricula){
        try {
            if("".equals(matricula)) return ResultadoEJB.crearErroneo(2, null, "No se puede buscar un seguro facultatativo con una matricula vacía");
            if(matricula == null) return ResultadoEJB.crearErroneo(3, null, "No se puede buscar un seguro facultativo con una matricula nula");
            List<DtoSeguroFacultativo> listaSegurosFacultatativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.matricula = :matricula ORDER BY e.periodo DESC", SegurosFacultativosEstudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> ejbSeguroFacultativoEstudiante.packSeguroFacultativo(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(listaSegurosFacultatativos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "No se pudo obtener la lista de Seguros Facultativos, debido a que aún no hay registros en el sistema");
            }else{
                return ResultadoEJB.crearCorrecto(listaSegurosFacultatativos, "Lista de seguros facultativos filtrada por estudiante");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se puede obtener la lista de registros de seguro facultativo del estudiante solicitado (EjbValidacionSeguroFacultativo.obtenerSeguroFacultativoPorEstudiante", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de un estudiante, mediante una pista la cual incluye apellido paterno, apellido materno, nombre y matricula
     * Que permitirá la búsqueda del seguro facultativo.
     * @param pista
     * @return 
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante (String pista){
        try {
            if("".equals(pista) || pista == null) return ResultadoEJB.crearErroneo(2, null, "No se puede buscar un estudiante sin antes ingresar algún parametro");
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.segurosFacultativosEstudianteList sf INNER JOIN FETCH e.aspirante a JOIN FETCH a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("pista", pista)
                    .getResultList();
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno() + " " + estudiante.getAspirante().getIdPersona().getApellidoMaterno() + " " + estudiante.getAspirante().getIdPersona().getNombre() + " - " + estudiante.getMatricula() + " - " + caster.periodoToString(em.find(PeriodosEscolares.class, estudiante.getPeriodo()));
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            if(listaDtoEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "El estudiante aún no cuenta con un registro de seguro facultativo es por ello que no se encuentra en el listado, ó bien ha ingresado sus datos de manera erronéa");
            }else{
                return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Estudiante encontrados con los datos ingresados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se puede encontrar al estudiante solicitado, debido a un error interno. (EjbValidacionSeguroFacultativo.buscarEstudiante)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de un estudiante, mediante una pista la cual incluye apellido paterno, apellido materno, nombre y matricula
     * Que permitirá la búsqueda del seguro facultativo.
     * Filtrados por periodo y programas educativos
     * @param pista
     * @param periodosEscolares
     * @param programasEducativos
     * @return 
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiantePorPeriodosProgramaEducativos (String pista, List<Integer> periodosEscolares, List<Short> programasEducativos){
        try {
            if("".equals(pista) || pista == null) return ResultadoEJB.crearErroneo(2, null, "No se puede buscar un estudiante sin antes ingresar algún parametro");
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.segurosFacultativosEstudianteList sf INNER JOIN FETCH e.aspirante a JOIN FETCH a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') AND (e.periodo IN :periodosEscolares AND e.carrera IN :programasEducativos) ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("pista", pista)
                    .setParameter("periodosEscolares", periodosEscolares)
                    .setParameter("programasEducativos", programasEducativos)
                    .getResultList();
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno() + " " + estudiante.getAspirante().getIdPersona().getApellidoMaterno() + " " + estudiante.getAspirante().getIdPersona().getNombre() + " - " + estudiante.getMatricula() + " - " + caster.periodoToString(em.find(PeriodosEscolares.class, estudiante.getPeriodo()));
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            if(listaDtoEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "El estudiante aún no cuenta con un registro de seguro facultativo es por ello que no se encuentra en el listado, ó bien es perteneciente a un programa educativo el cual no tiene asignado");
            }else{
                return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Estudiante encontrados con los datos ingresados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se puede encontrar al estudiante solicitado, debido a un error interno. (EjbValidacionSeguroFacultativo.buscarEstudiantePorPeriodosProgramaEducativos)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de un estudiante, mediante una pista la cual incluye apellido paterno, apellido materno, nombre y matricula
     * Que permitirá la búsqueda del seguro facultativo.
     * Filtrados por periodo, programas educativos y grupos
     * @param pista
     * @param periodosEscolares
     * @param programasEducativos
     * @param grupos
     * @return 
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiantePorPeriodosProgramaEducativosGrupos (String pista, List<Integer> periodosEscolares, List<Short> programasEducativos, List<Integer> grupos){
        try {
            if("".equals(pista) || pista == null) return ResultadoEJB.crearErroneo(2, null, "No se puede buscar un estudiante sin antes ingresar algún parametro");
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.grupo g INNER JOIN FETCH e.segurosFacultativosEstudianteList sf INNER JOIN FETCH e.aspirante a JOIN FETCH a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') AND (e.periodo IN :periodosEscolares AND e.carrera IN :programasEducativos AND g.idGrupo IN :grupos) ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("pista", pista)
                    .setParameter("periodosEscolares", periodosEscolares)
                    .setParameter("programasEducativos", programasEducativos)
                    .setParameter("grupos", grupos)
                    .getResultList();
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno() + " " + estudiante.getAspirante().getIdPersona().getApellidoMaterno() + " " + estudiante.getAspirante().getIdPersona().getNombre() + " - " + estudiante.getMatricula() + " - " + caster.periodoToString(em.find(PeriodosEscolares.class, estudiante.getPeriodo()));
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            if(listaDtoEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "El estudiante aún no cuenta con un registro de seguro facultativo es por ello que no se encuentra en el listado, ó bien es perteneciente a un programa educativo el cual no tiene asignado");
            }else{
                return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Estudiante encontrados con los datos ingresados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se puede encontrar al estudiante solicitado, debido a un error interno. (EjbValidacionSeguroFacultativo.buscarEstudiantePorPeriodosProgramaEducativos)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de un estudiante, mediante una pista la cual incluye apellido paterno, apellido materno, nombre y matricula
     * Que permitirá la búsqueda del seguro facultativo.
     * Filtrados solo por los estudiantes que se le asignaron al asesor académico de estadía
     * @param pista
     * @param listaIdEstudiantesAsesoradosEstadia
     * @return 
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiantePorAsesoradosEstadia(String pista, List<Integer> listaIdEstudiantesAsesoradosEstadia){
        try {
            if("".equals(pista) || pista == null) return ResultadoEJB.crearErroneo(2, null, "No se puede buscar un estudiante sin antes ingresar algún parametro");
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.segurosFacultativosEstudianteList sf INNER JOIN FETCH e.aspirante a JOIN FETCH a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') AND (e.idEstudiante IN :listaIdEstudiantesAsesoradosEstadia) ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("pista", pista)
                    .setParameter("listaIdEstudiantesAsesoradosEstadia", listaIdEstudiantesAsesoradosEstadia)
                    .getResultList();
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno() + " " + estudiante.getAspirante().getIdPersona().getApellidoMaterno() + " " + estudiante.getAspirante().getIdPersona().getNombre() + " - " + estudiante.getMatricula() + " - " + caster.periodoToString(em.find(PeriodosEscolares.class, estudiante.getPeriodo()));
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            if(listaDtoEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "El estudiante aún no cuenta con un registro de seguro facultativo, es por ello que no se encuentra en el listado, ó bien es perteneciente a un programa educativo el cual no tiene asignado");
            }else{
                return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Estudiante encontrados con los datos ingresados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se puede encontrar al estudiante solicitado, debido a un error interno. (EjbValidacionSeguroFacultativo.buscarEstudiantePorAsesoradosEstadia)", e, null);
        }
    }
    
    
    
    /**
     * Método que permite registrar las observaciones detectadas por el área correspondiente
     * @param dtoSeguroFacultativo
     * @param personal
     * @return 
     */
    public ResultadoEJB<Boolean> registrarObservaciones(DtoSeguroFacultativo dtoSeguroFacultativo, PersonalActivo personal){
        try {
            if(dtoSeguroFacultativo.getSeguroFactultativo().getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se pueden registrar observaciones en un registro de seguro facultativo nulo");
            if(SegurosFacultativosEstatus.ENVIADO_PARA_VALIDACION.getLabel().equals(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusRegistro())){
                dtoSeguroFacultativo.getSeguroFactultativo().setEstatusRegistro(SegurosFacultativosEstatus.EN_OBSERVACIONES.getLabel());
                if(dtoSeguroFacultativo.getPersonalActivo() == null) {
                    dtoSeguroFacultativo.setPersonalActivo(personal);
                    dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());
                    ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(true, "Se han registrado las observaciones del registro del seguro facultativo del estudiante" + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
                }else{
//                    if(!Objects.equals(personal.getPersonal().getClave(), dtoSeguroFacultativo.getSeguroFactultativo().getUsuarioOperacion())){
//                        return ResultadoEJB.crearErroneo(3, null, "A este registro le está dando seguimiento el trabajador: " + dtoSeguroFacultativo.getPersonalActivo().getPersonal().getNombre());
//                    }else{
                    dtoSeguroFacultativo.setPersonalActivo(personal);
                    dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());
                    ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(true, "Se han registrado los comentarios del seguro facultativo del estudiante" + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
                }
//                }
            }else{
                return ResultadoEJB.crearCorrecto(false, "No se pueden registrar observaciones en el registro de Seguros Facultativo debido a que el estudiante aún no lo ha enviado a revisión, ó aún se encuentra en observaciones  y el estudiante aún no modifica su información");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron registrar las observaciones del estudiante: " + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula() + " (EjbValidacionSeguroFacultativo.registrarObservaciones)", e, null);
        }
    }
    
    /**
     * Método que permite la validación del archivo Tarjetón IMSS, recibe una variable de tipo Boolean para identificar si se validará o pasará al estatus de Observaciones para que el estudiante lo modifique
     * @param dtoSeguroFacultativo
     * @param validar
     * @param personal
     * @return 
     */
    public ResultadoEJB<Boolean> validarArchivoEstudiante(DtoSeguroFacultativo dtoSeguroFacultativo, PersonalActivo personal){
        try {
            if (dtoSeguroFacultativo.getSeguroFactultativo().getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se se puede validar el archivo en un registro de seguro facultativo nulo");
            if (SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() == 1.1D || SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() == 0D) {
                return ResultadoEJB.crearErroneo(3, null, "No se puede validar el archivo, debido a que el registro del seguro facultativo ya fué dado de baja o dado de alta");
            } else {
                if (dtoSeguroFacultativo.getPersonalActivo() == null) {
                    dtoSeguroFacultativo.setPersonalActivo(personal);
                    dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());    
                    ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha actualizado el estatus del archivo, si ha escogido la opción de 'En observaciones' favor de colocarlos en la sección de 'Comentarios para el estudiante'.");
                } else {
//                    if (!Objects.equals(personal.getPersonal().getClave(), dtoSeguroFacultativo.getSeguroFactultativo().getUsuarioOperacion())) {
//                        return ResultadoEJB.crearErroneo(3, null, "A este registro le está dando seguimiento el trabajador: " + dtoSeguroFacultativo.getPersonalActivo().getPersonal().getNombre());
//                    } else {
                    ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha actualizado el estatus del archivo, si ha escogido la opción de 'En observaciones' favor de colocarlos en la sección de 'Comentarios para el estudiante'.");
//                }
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo validar el archivo del estudiante: " + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula() + " (EjbValidacionSeguroFacultativo.validarArchivoEstudiante)", e, null);
        }
    }
    
    /**
     * Método que se utilizar para validar y dar de alta un seguro de un estudiante, se contemplan estatus de evidencias y registro en general, de igual manera se valida
     * si es la primera vez que un usuario le está dando seguimiento, o si ya tiene seguimiento por otro.
     * @param dtoSeguroFacultativo
     * @param personal
     * @return 
     */
    public ResultadoEJB<Boolean> altaSeguroFacultativoEstudiante(DtoSeguroFacultativo dtoSeguroFacultativo, PersonalActivo personal){
        try {
            if(dtoSeguroFacultativo.getSeguroFactultativo().getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se se puede validar el registro del seguro facultativo de un estudiante nulo");
            if(SegurosFacultativosEstatus.ENVIADO_PARA_VALIDACION.getLabel().equals(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusRegistro()) || SeguroFacultativoValidacion.BAJA.getLabel().equals(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria())){
                dtoSeguroFacultativo.getSeguroFactultativo().setEstatusRegistro(SegurosFacultativosEstatus.VALIDADO.getLabel());
                dtoSeguroFacultativo.getSeguroFactultativo().setValidacionEnfermeria(SeguroFacultativoValidacion.ALTA.getLabel());
                dtoSeguroFacultativo.getSeguroFactultativo().setComentariosEnfermeria("Registro validado y dado de alta");
                dtoSeguroFacultativo.getSeguroFactultativo().setFechaBaja(null);
                dtoSeguroFacultativo.getSeguroFactultativo().setFechaAlta(new Date());
                if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusTarjeton()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.VALIDADO.getLabel());
                if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteLocalizacion()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteLocalizacion(SegurosFacultativosEstatus.VALIDADO.getLabel());
                if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteVigenciaDerechos()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteVigenciaDerechos(SegurosFacultativosEstatus.VALIDADO.getLabel());
                if(dtoSeguroFacultativo.getPersonalActivo() == null) {
                    dtoSeguroFacultativo.setPersonalActivo(personal);
                    dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());
                    ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(true, "Se ha validado y dado de alta el registro del seguro facultativo del estudiante" + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
                }else{
//                    if(!Objects.equals(personal.getPersonal().getClave(), dtoSeguroFacultativo.getSeguroFactultativo().getUsuarioOperacion())){
//                        return ResultadoEJB.crearErroneo(3, null, "A este registro le está dando seguimiento el trabajador: " + dtoSeguroFacultativo.getPersonalActivo().getPersonal().getNombre());
//                    }else{
                        dtoSeguroFacultativo.setPersonalActivo(personal);
                        dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());
                        ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                        return ResultadoEJB.crearCorrecto(true, "Se ha validado y dado de alta el registro del seguro facultativo del estudiante" + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
//                    }
                }
            }else{
                return ResultadoEJB.crearCorrecto(false, "No se puede validar un registro de Seguro Facultativo debido a que el estudiante aún no lo ha enviado a revisión, ó aún se encuentra en observaciones  y el estudiante aún no modifica su información");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo validar el registro del Seguro Facultativo del estudiante: " + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula() + " (EjbValidacionSeguroFacultativo.validarSeguroFacultativoEstudiante)", e, null);
        }
    }
    
     public ResultadoEJB<Boolean> bajaSeguroFacultativoEstudiante(DtoSeguroFacultativo dtoSeguroFacultativo, PersonalActivo personal){
         try {
             if(dtoSeguroFacultativo.getSeguroFactultativo().getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se se puede dar de baja el registro del seguro facultativo de un estudiante nulo");
             if(SegurosFacultativosEstatus.VALIDADO.getLabel().equals(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusRegistro())){
                 dtoSeguroFacultativo.getSeguroFactultativo().setEstatusRegistro(SegurosFacultativosEstatus.VALIDADO.getLabel());
                 dtoSeguroFacultativo.getSeguroFactultativo().setValidacionEnfermeria(SeguroFacultativoValidacion.BAJA.getLabel());
                 dtoSeguroFacultativo.getSeguroFactultativo().setComentariosEnfermeria("Registro dado de baja");
                 dtoSeguroFacultativo.getSeguroFactultativo().setFechaBaja(new Date());
                 if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusTarjeton()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.VALIDADO.getLabel());
                 if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteLocalizacion()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteLocalizacion(SegurosFacultativosEstatus.VALIDADO.getLabel());
                 if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteVigenciaDerechos()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteVigenciaDerechos(SegurosFacultativosEstatus.VALIDADO.getLabel());
                 if(dtoSeguroFacultativo.getPersonalActivo() == null) {
                     dtoSeguroFacultativo.setPersonalActivo(personal);
                     dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());
                     ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                     return ResultadoEJB.crearCorrecto(true, "Se ha validado y dado de baja el registro del seguro facultativo del estudiante" + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
                 }else{
//                     if(!Objects.equals(personal.getPersonal().getClave(), dtoSeguroFacultativo.getSeguroFactultativo().getUsuarioOperacion())){
//                         return ResultadoEJB.crearErroneo(3, null, "A este registro le está dando seguimiento el trabajador: " + dtoSeguroFacultativo.getPersonalActivo().getPersonal().getNombre());
//                     }else{
                        dtoSeguroFacultativo.setPersonalActivo(personal);
                        dtoSeguroFacultativo.getSeguroFactultativo().setUsuarioOperacion(personal.getPersonal().getClave());
                         ejbSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                         return ResultadoEJB.crearCorrecto(true, "Se ha validado y dado de baja el registro del seguro facultativo del estudiante" + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
//                     }
                 }
             }else{
                 return ResultadoEJB.crearCorrecto(false, "No se puede dar de baja un registro de Seguro Facultativo debido a que no se dió de alta aún");
             }
         } catch (Exception e) {
             return ResultadoEJB.crearErroneo(1, "No se pudo dar de baja el registro del Seguro Facultativo del estudiante: " + dtoSeguroFacultativo.getEstudiante().getPersona().getNombre() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno() + " " + dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno() + " con matricula: " + dtoSeguroFacultativo.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula() + " (EjbValidacionSeguroFacultativo.bajaSeguroFacultativo)", e, null);
         }
     }
     
     /**
     * Empaqueta un registro de Seguro facultativo en su correspondiente DTO para uso de reportes
     * 
     * Uso para reportes, se intenta priorizar la velocidad de la consulta
     * @param seguroFacultativo Registro que se empaquetará
     * @return Regresa un Dto de tipo DtoSeguroFacultativo ya empaquetado
     */
    public ResultadoEJB<DtoSeguroFacultativo> packSeguroFacultativoReporte(SegurosFacultativosEstudiante seguroFacultativo){
        try {
            if(seguroFacultativo == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un registro de seguro facultativo nulo", DtoSeguroFacultativo.class);
            if(seguroFacultativo.getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un registro de seguro facultativo con clave nula", DtoSeguroFacultativo.class);
            
            DtoEstudiante estudiante = pack.packEstudianteGeneral(seguroFacultativo.getEstudiante()).getValor();
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, seguroFacultativo.getEstudiante().getCarrera());
            AsentamientoPK asentamientoPK = new AsentamientoPK(seguroFacultativo.getEstudiante().getAspirante().getDomicilio().getIdEstado(), seguroFacultativo.getEstudiante().getAspirante().getDomicilio().getIdMunicipio(), seguroFacultativo.getEstudiante().getAspirante().getDomicilio().getIdAsentamiento());
            Asentamiento asentamiento = em.find(Asentamiento.class, asentamientoPK);
            String asentamientoCompleto = asentamiento.getMunicipio1().getEstado().getNombre().concat(" - ").concat(asentamiento.getMunicipio1().getNombre()).concat(" - ").concat(asentamiento.getNombreAsentamiento());
            DtoSeguroFacultativo dtoSinUsuarioOperacion = new DtoSeguroFacultativo(seguroFacultativo, estudiante, programaEducativo, asentamientoCompleto);
            return ResultadoEJB.crearCorrecto(dtoSinUsuarioOperacion, "Seguro facultativo empaquetado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el registro de seguro facultativo (EjbValidacionSeguroFacultativo.packSeguroFacultativoReporte)", e, DtoSeguroFacultativo.class);
        }
    }
    
     /**
     * Permite obtener una lista de tipo seguro facultativo con filtro de Periodo
     * Para uso de reportes de excel
     * @param periodo
     * @param programaEducativo 
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSfPorPeriodo(PeriodosEscolares periodo){
        try {
            if(periodo == null) return ResultadoEJB.crearErroneo(2, null, "No se pueden consultar seguros facultativos si el periodo escolar es nulo");
            List<DtoSeguroFacultativo> listaSeguroFacultativo = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.periodo = :periodo", SegurosFacultativosEstudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> packSeguroFacultativoReporte(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(listaSeguroFacultativo.isEmpty()){
                return ResultadoEJB.crearErroneo(4, null, "No se pudo obtener la lista de seguros facultativos debido a que aún no hay registros de Seguros Facultativos en el sistema.");
            }else{
                return ResultadoEJB.crearCorrecto(listaSeguroFacultativo, "Listado de registros de tipo Seguro Facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista en la que se han tenido registros de Seguro Facultativo. (EjbValidacionSeguroFacultativo.obtenerSfPorPeriodoProgramaEducativo)", e, null);
        }
    }
    /**
     * Permite obtener una lista de tipo seguro facultativo con filtro de Periodo y Programa Educativo
     * Para uso de reportes de excel
     * @param periodo
     * @param programaEducativo 
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> obtenerSfPorPeriodoProgramaEducativo(PeriodosEscolares periodo, AreasUniversidad programaEducativo){
        try {
            if(periodo == null) return ResultadoEJB.crearErroneo(2, null, "No se pueden consultar seguros facultativos si el periodo escolar es nulo");
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(3, null, "No se pueden consultar registros de Seguro Facultativo si el programa educativo es nulo");
            List<DtoSeguroFacultativo> listaSeguroFacultativo = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE e.periodo = :periodo AND e.carrera = :programaEducativo", SegurosFacultativosEstudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> packSeguroFacultativoReporte(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(listaSeguroFacultativo.isEmpty()){
                return ResultadoEJB.crearErroneo(4, null, "No se pudo obtener la lista de seguros facultativos debido a que aún no hay registros de Seguros Facultativos en el sistema.");
            }else{
                return ResultadoEJB.crearCorrecto(listaSeguroFacultativo, "Lista de grupos en las que hay registros de tipo Seguro Facultativo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista en la que se han tenido registros de Seguro Facultativo. (EjbValidacionSeguroFacultativo.obtenerSfPorPeriodoProgramaEducativo)", e, null);
        }
    }
    
    /**
     * Método que permite obtener el asentamiento de residencia, para que el área de enfermería pueda seleccionar la clínica que le corresponde
     * @param estado
     * @param municipio
     * @param asentamiento
     * @return 
     */
    public ResultadoEJB<String> obtenerNombreAsentamiento(Integer estado, Integer municipio, Integer asentamiento){
        try {
            List<String> asentamientoNombre = em.createQuery("SELECT a.nombreAsentamiento FROM Asentamiento a WHERE a.asentamientoPK.estado = :estado AND a.asentamientoPK.municipio = :municipio AND a.asentamientoPK.asentamiento = :asentamiento", String.class)
                    .setParameter("estado", estado)
                    .setParameter("municipio", municipio)
                    .setParameter("asentamiento", asentamiento)
                    .getResultList();
            if(asentamientoNombre.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "No se encontró el nombre del asentamiento");
            }else{
                return ResultadoEJB.crearCorrecto(asentamientoNombre.get(0), "Nombre del asentamiento encontrado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el nombre del asentamiento (EjbValidacionSeguroFacultativo.obtenerNombreAsentamiento)", e, null);
        }
    }
    
}
