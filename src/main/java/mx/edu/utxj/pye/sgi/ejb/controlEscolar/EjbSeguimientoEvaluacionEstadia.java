/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbSeguimientoEvaluacionEstadia {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbAdministracionEncuestaServicios ejbAdm;
    private EntityManager em;
    private EntityManager em2;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }
    
    public ResultadoEJB<PersonalActivo> validarTutor(Integer clave){
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            return ResultadoEJB.crearCorrecto(p, "Personal activo");
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar como tutor en el periodo actual.", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> darAccesoSeguimiento(){
        AperturaVisualizacionEncuestas ave = em.createQuery("select a from AperturaVisualizacionEncuestas as a where a.encuesta = :encuesta and :fecha BETWEEN a.fechaInicial and a.fechaFinal", AperturaVisualizacionEncuestas.class)
                .setParameter("fecha", new Date())
                .setParameter("encuesta", "Evaluación Estadía")
                .getResultStream().findFirst().orElse(new AperturaVisualizacionEncuestas());
        if(ave.equals(new AperturaVisualizacionEncuestas())) return ResultadoEJB.crearCorrecto(Boolean.FALSE, "El seguimiento de este Test no esta activo");
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El seguimiento esta activo");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> obtenerGrupos(Integer clave){
        List<DtoAlumnosEncuesta.DtoGruposTutor> dtoT = em.createQuery("select g from Grupo as g where g.grado = :grado1 and g.tutor = :tutor and g.periodo = :periodo", Grupo.class)
                .setParameter("grado1", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .setParameter("tutor", clave)
                .setParameter("periodo", obtenerPeriodoTest().getValor())
                .getResultStream()
                .map(grupo -> packDto(grupo))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(dtoT.isEmpty()) return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "La lista se creo sin información");
        return ResultadoEJB.crearCorrecto(dtoT, "Lista completa");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoGruposTutor> packDto(Grupo grupo){
        if(grupo == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoGruposTutor.class);
        if(grupo.getIdGrupo() == null) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoGruposTutor.class);
        Grupo g = em.find(Grupo.class, grupo.getIdGrupo());
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, g.getPeriodo());
        DtoAlumnosEncuesta.DtoGruposTutor dto = new DtoAlumnosEncuesta.DtoGruposTutor(g.getIdGrupo(), pe, g.getGrado(), g.getLiteral().toString(), g.getTutor());
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<Integer> obtenerPeriodoTest(){
        VariablesProntuario vp = em.createQuery("SELECT v FROM VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoTEvaluacionE")
                .getResultStream().findFirst().orElse(new VariablesProntuario());
        if(vp.equals(new VariablesProntuario())) return ResultadoEJB.crearCorrecto(0, "No hay periodo encontrado");
        return ResultadoEJB.crearCorrecto(Integer.parseInt(vp.getValor()), "Periodo encontrado");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerVariable1(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "gradoEvaluacion")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 1");
    }
    
    public ResultadoEJB<Grupo> obtenerGrupoSeleccionado(Integer cveGrupo){
        return ResultadoEJB.crearCorrecto(em.find(Grupo.class, cveGrupo), "Grupo seleccionado");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerEstudiantes(Grupo grupo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto = em.createQuery("SELECT e from Estudiante as e where e.grupo.idGrupo = :cveGrupo and e.tipoEstudiante.idTipoEstudiante = :tipo", Estudiante.class)
                .setParameter("cveGrupo", grupo.getIdGrupo())
                .setParameter("tipo", 1)
                .getResultStream()
                .map(estudiante -> ejbAdm.packEstudiantesEncuesta(estudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> packEncuestasCompletas(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoEstudiante){
        Comparador<EvaluacionEstadiaResultados> comparador = new ComparadorEvaluacionEstadia();
        if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(1, "", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Evaluación Estadía")
                .setParameter("periodo", dtoEstudiante.getGrupos().getPeriodo())
                .getResultStream().findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "No hay evaluacion registrada para este periodo", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        EvaluacionEstadiaResultados tda = em.createQuery("SELECT e FROM EvaluacionEstadiaResultados as e where e.evaluacionEstadiaResultadosPK.evaluacion = :evaluacion and e.evaluacionEstadiaResultadosPK.evaluador = :evaluador", EvaluacionEstadiaResultados.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", String.valueOf(dtoEstudiante.getAlumnos().getMatricula()))
                .getResultStream().findFirst().orElse(new EvaluacionEstadiaResultados());
        if(!comparador.isCompleto(tda)) return ResultadoEJB.crearErroneo(2, "El alumno no ha ingresado o no ha concluido con su Test", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar(
                dtoEstudiante.getAlumnos(), dtoEstudiante.getPersonas(), dtoEstudiante.getGrupos(), dtoEstudiante.getAreasUniversidad(), 
                dtoEstudiante.getTutor(), dtoEstudiante.getDtoDirector(), dtoEstudiante.getSiglas(), dtoEstudiante.getGrupo(), dtoEstudiante.getGrado());
        return ResultadoEJB.crearCorrecto(dto, "Estudiante con Test Completo");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> packEncuestasIncompletas(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoEstudiante){
        Comparador<EvaluacionEstadiaResultados> comparador = new ComparadorEvaluacionEstadia();
        if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(1, "", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Evaluación Estadía")
                .setParameter("periodo", dtoEstudiante.getGrupos().getPeriodo())
                .getResultStream().findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "No hay evaluacion registrada para este periodo", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        EvaluacionEstadiaResultados tda = em.createQuery("SELECT e FROM EvaluacionEstadiaResultados as e where e.evaluacionEstadiaResultadosPK.evaluacion = :evaluacion and e.evaluacionEstadiaResultadosPK.evaluador = :evaluador", EvaluacionEstadiaResultados.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", String.valueOf(dtoEstudiante.getAlumnos().getMatricula()))
                .getResultStream().findFirst().orElse(new EvaluacionEstadiaResultados());
        if(comparador.isCompleto(tda)) return ResultadoEJB.crearErroneo(2, "El alumno no ha ingresado o no ha concluido con su Test", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar(
                dtoEstudiante.getAlumnos(), dtoEstudiante.getPersonas(), dtoEstudiante.getGrupos(), dtoEstudiante.getAreasUniversidad(), 
                dtoEstudiante.getTutor(), dtoEstudiante.getDtoDirector(), dtoEstudiante.getSiglas(), dtoEstudiante.getGrupo(), dtoEstudiante.getGrado());
        return ResultadoEJB.crearCorrecto(dto, "Estudiante con Test Completo");
    }
    
    
    
    //////////////////////////////////////////////EJBs para usuarios multiples\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public ResultadoEJB<Filter<PersonalActivo>> validarConsultaTest(Integer clave){   
        try {
            String pista = ",".concat(String.valueOf(clave).trim()).concat(",").trim();
            ConfiguracionPropiedades claveConfiguracionPropiedad = Objects.requireNonNull(em.createQuery("SELECT c FROM ConfiguracionPropiedades c WHERE c.tipo = :tipo AND c.clave = :clave AND c.valorCadena LIKE CONCAT('%',:pista,'%')", ConfiguracionPropiedades.class)
                    .setParameter("tipo", "Cadena")
                    .setParameter("pista", pista)
                    .setParameter("clave", "consultarEvaluacionEstadia")
                    .getResultStream()
                    .findFirst().orElse(new ConfiguracionPropiedades()));
            if (claveConfiguracionPropiedad.getClave() == null || claveConfiguracionPropiedad.getClave().equals("") || claveConfiguracionPropiedad.getClave().isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se ha encontrado la clave del trabajador, no tiene asignado el módulo");
            } else {
                PersonalActivo p = ejbPersonalBean.pack(clave);
                Filter<PersonalActivo> filtro = new Filter<>();
                filtro.setEntity(p);
                return ResultadoEJB.crearCorrecto(filtro, "Se ha encontrado la clave de trabajador asignada a este módulo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar correctamente la validación. (EjbValidacionRol.validarPsicopedagogia)", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerEstudiantes(Integer periodo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto = em.createQuery("SELECT e from Estudiante as e where e.tipoEstudiante.idTipoEstudiante = :tipo and e.periodo = :periodo and e.grupo.grado = :grado", Estudiante.class)
                .setParameter("tipo", 1)
                .setParameter("periodo", periodo)
                .setParameter("grado", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .getResultStream()
                .map(estudiante -> ejbAdm.packEstudiantesEncuesta(estudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> obtenerProgramasEducativo(Integer periodoEscolar){
        List<DtoAlumnosEncuesta.DtoProgramaEducativo> dtoPECE;
         dtoPECE = em.createQuery("select g from Grupo as g where g.periodo = :periodo GROUP BY g.idPe", Grupo.class)
                 .setParameter("periodo", periodoEscolar)
                 .getResultStream()
                 .map(au -> packProgramaEducativo(au))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
         
        return ResultadoEJB.crearCorrecto(dtoPECE, "Programas Educativos completos");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoProgramaEducativo> packProgramaEducativo(Grupo grupo){
        if(grupo == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        if(grupo.getIdGrupo() == null) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        //if(grupo.getGrado() != 6 || grupo.getGrado() != 11) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        AreasUniversidad au = em.find(AreasUniversidad.class, grupo.getIdPe());
        DtoAlumnosEncuesta.DtoProgramaEducativo dto = new DtoAlumnosEncuesta.DtoProgramaEducativo(au.getArea(), au.getNombre(), au.getSiglas(), au.getNivelEducativo().getNivel(), "Control Escolar");
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerEstudiantesCE(Short area, Integer periodo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto = em.createQuery("SELECT e from Estudiante as e where e.grupo.grado = :grado1 "
                        + "and e.carrera = :area and (e.tipoEstudiante.idTipoEstudiante = :tipo or e.tipoEstudiante.idTipoEstudiante = :tipo2) and e.periodo = :periodo", Estudiante.class)
                .setParameter("grado1", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .setParameter("area", area)
                .setParameter("tipo", 1)
                .setParameter("tipo2", 4)
                .setParameter("periodo", periodo)
                .getResultStream()
                .map(estudiante -> ejbAdm.packEstudiantesEncuesta(estudiante))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    
    /////////////////////////////////////////////////////EJBs de las opciones del Director\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        return ejbValidacionRol.validarDirector(clave);
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        return ejbValidacionRol.validarEncargadoDireccion(clave);
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerEncargadoMecaa(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "encargadoMECAA")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 1");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerArea(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "area")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 1");
    }
    
    public ResultadoEJB<List<AreasUniversidad>> obtenerAreaDirector(Integer clave){
        List<AreasUniversidad> lista = em.createQuery("select a from AreasUniversidad as a where a.responsable = :clave", AreasUniversidad.class)
                .setParameter("clave", clave)
                .getResultList();
        return ResultadoEJB.crearCorrecto(lista, "Areas academicas");
    }
    
    public ResultadoEJB<AreasUniversidad> obtenerAreaEspecifica(){
        AreasUniversidad au = em.createQuery("select a from AreasUniversidad as a where a.area = :area", AreasUniversidad.class)
                .setParameter("area", Short.parseShort(obtenerArea().getValor().getValor()))
                .getResultStream().findFirst().orElse(new AreasUniversidad());
        return ResultadoEJB.crearCorrecto(au, "");
    }
    
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativos(Short area){
        List<AreasUniversidad> au = em.createQuery("select a from AreasUniversidad as a where a.areaSuperior = :area and a.vigente = :vigente and (a.nivelEducativo.nivel = :nivel1 or a.nivelEducativo.nivel = :nivel2)", AreasUniversidad.class)
                .setParameter("area", area)
                .setParameter("vigente", "1")
                .setParameter("nivel1", obtenerNivel1().getValor().getValor())
                .setParameter("nivel2", obtenerNivel2().getValor().getValor())
                .getResultList();
        return ResultadoEJB.crearCorrecto(au, "");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerEstudiantes(Short area, Integer periodo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto = em.createQuery("SELECT e from Estudiante as e where e.carrera = :area and (e.tipoEstudiante.idTipoEstudiante = :tipo or e.tipoEstudiante.idTipoEstudiante = :tipo2) and e.periodo = :periodo and e.grupo.grado = :grado", Estudiante.class)
                .setParameter("area", area)
                .setParameter("tipo", 1)
                .setParameter("tipo2", 4)
                .setParameter("periodo", periodo)
                .setParameter("grado", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .getResultStream()
                .map(estudiante -> ejbAdm.packEstudiantesEncuesta(estudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerNivel1(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "nivel1")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 1");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerNivel2(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "nivel2")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 1");
    }
    
    public ResultadoEJB<AreasUniversidad> obtenerPrograma(Short area){
        AreasUniversidad au = em.createQuery("select a from AreasUniversidad as a where a.areaSuperior = :area and a.vigente = :vigente and (a.nivelEducativo.nivel = :nivel1 or a.nivelEducativo.nivel = :nivel2)", AreasUniversidad.class)
                .setParameter("area", area)
                .setParameter("vigente", "1")
                .setParameter("nivel1", obtenerNivel1().getValor().getValor())
                .setParameter("nivel2", obtenerNivel2().getValor().getValor())
                .getResultStream()
                .findFirst()
                .orElse(new AreasUniversidad());
        return ResultadoEJB.crearCorrecto(au, "Programa educativo.");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> packEstudiante(AreasUniversidad au, Integer periodo){
        try{
            if(au == null) return ResultadoEJB.crearErroneo(2, "No se encontro al estudiante enviado", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
            if(periodo == null) return ResultadoEJB.crearErroneo(2, "No se encontro al estudiante enviado", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
            AreasUniversidad areasUniversidad = em.find(AreasUniversidad.class, au.getArea());
            Estudiante estudianteBD = em.createQuery("SELECT e from Estudiante as e where e.carrera = :area and (e.tipoEstudiante.idTipoEstudiante = :tipo or e.tipoEstudiante.idTipoEstudiante = :tipo2) and e.periodo = :periodo and e.grupo.grado = :grado", Estudiante.class)
                .setParameter("area", areasUniversidad.getArea())
                .setParameter("tipo", 1)
                .setParameter("tipo2", 4)
                .setParameter("periodo", periodo)
                .setParameter("grado", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .getResultStream().findFirst().orElse(new Estudiante());
            Grupo grupo;
            Personal tutor;
            if(estudianteBD.getGrupo().getTutor() == null){
                grupo = new Grupo(estudianteBD.getGrupo().getIdGrupo(), estudianteBD.getGrupo().getLiteral(), estudianteBD.getGrupo().getGrado(), estudianteBD.getGrupo().getCapMaxima(), estudianteBD.getGrupo().getIdPe(), estudianteBD.getGrupo().getPeriodo(), estudianteBD.getGrupo().getGeneracion());
                tutor = new Personal();
            }else{
                grupo = em.find(Grupo.class, estudianteBD.getGrupo().getIdGrupo());
                tutor = em.find(Personal.class, grupo.getTutor());
            }
            
            
            
            DtoAlumnosEncuesta.DtoDirectores dtoDirector = ejbAdm.listaDirectores().getValor().stream()
                    .filter(siglas -> siglas.getSiglas().equals(areasUniversidad.getSiglas())).findFirst().orElse(null);
            assert dtoDirector != null;
            ListaUsuarioClaveNomina listaUsuarioClaveNomina = em2.createQuery("select l from ListaUsuarioClaveNomina as l where l.numeroNomina = :noNomima", ListaUsuarioClaveNomina.class)
                    .setParameter("noNomima", dtoDirector.getClaveDirector().toString()).getResultStream().findFirst().orElse(null);
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar(
                    estudianteBD, estudianteBD.getAspirante().getIdPersona(), grupo, areasUniversidad, tutor, listaUsuarioClaveNomina,
                    areasUniversidad.getSiglas(), grupo.getLiteral().toString(), (short) estudianteBD.getGrupo().getGrado());
            //System.out.println(dto);
            return ResultadoEJB.crearCorrecto( dto,"");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1,"No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        }
    }
}
