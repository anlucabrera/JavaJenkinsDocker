/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CordinadoresTutores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbSeguimientoTestDiagnosticoAprendizaje {
    
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbAdministracionEncuestaServicios ejbAdm;
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public ResultadoEJB<PersonalActivo> validarTutor(Integer clave){
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            return ResultadoEJB.crearCorrecto(p, "Personal activo");
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar como tutor en el periodo actual.", e, null);
        }
    }
    
    public ResultadoEJB<Personas> validarPersona(Integer clave){
        Personas p = f2.getEntityManager().createQuery("select p from Personas as p where p.personasPK.cvePersona = :clave", Personas.class)
                .setParameter("clave", clave)
                .getResultStream()
                .findFirst().orElse(new Personas());
        if(p.equals(new Personas())) ResultadoEJB.crearErroneo(1, "La clave ingresada no se encuentra", Personas.class);
        return ResultadoEJB.crearCorrecto(p, "Personas validada");
    }
    
    public ResultadoEJB<Boolean> esTutor(Integer clave){
        Integer periodoEscolar = obtenerPeriodoTest().getValor();
        List<Grupos> grupos = f2.getEntityManager().createQuery("select g from Grupos as g where g.cveMaestro = :clave and g.gruposPK.cvePeriodo = :periodo", Grupos.class)
                .setParameter("clave", clave)
                .setParameter("periodo", periodoEscolar)
                .getResultStream()
                .filter(g -> g.getGrado() != Short.parseShort("11") || g.getGrado() != Short.parseShort("6")).collect(Collectors.toList());
        //System.out.println("Grupo"+ g);
        if(grupos.size() <= 0) return ResultadoEJB.crearErroneo(2, "La persona ingresada no tiene grupos tutorados", Boolean.class);
        
        //System.out.println("Es tutor");
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Es tutor");
    }
    
    public ResultadoEJB<CordinadoresTutores> validarCoordinadorTutor(Integer clave, Integer periodo){
        try{
            CordinadoresTutores ct = em.createQuery("SELECT c FROM CordinadoresTutores c WHERE c.personal = :clave and c.periodo = :periodo", CordinadoresTutores.class)
                    .setParameter("clave", clave)
                    .setParameter("periodo", periodo)
                    .getResultStream().findFirst().orElse(new CordinadoresTutores());
            if(!ct.equals(new CordinadoresTutores())){
                return ResultadoEJB.crearCorrecto(ct, "El usuario ha sido identificado como Coordinador de Tutores");
            }else{
                return ResultadoEJB.crearErroneo(2, null,"El usuario no ha sido en ninguna ocasión Coordinador de Tutores");
            }
        }catch(Exception e){
            return ResultadoEJB.crearErroneo(1, "No se ha podido comprobar si el usuario es o ha sido Coordinador de Tutores. (EjbValidacionRol.validarCoordinadorTutor)", e, null);
        }
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarPsicopedagogia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalPsicopedagogia").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal del área de psicopedagogía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal del área no se pudo validar. (EjbValidacionRol.validarPsicopedagogia)", e, null);
        }    
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarConsultaTest(Integer clave){   
        try {
            String pista = ",".concat(String.valueOf(clave).trim()).concat(",").trim();
            ConfiguracionPropiedades claveConfiguracionPropiedad = Objects.requireNonNull(em.createQuery("SELECT c FROM ConfiguracionPropiedades c WHERE c.tipo = :tipo AND c.clave = :clave AND c.valorCadena LIKE CONCAT('%',:pista,'%')", ConfiguracionPropiedades.class)
                    .setParameter("tipo", "Cadena")
                    .setParameter("pista", pista)
                    .setParameter("clave", "consultarTest")
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
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> obtenerGrupos(Integer clave){
        List<DtoAlumnosEncuesta.DtoGruposTutor> dtoT = em.createQuery("select g from Grupo as g where (g.grado = :grado1 or g.grado = :grado2 or g.grado = :grado3 or g.grado = :grado4) and g.tutor = :tutor and g.periodo = :periodo", Grupo.class)
                .setParameter("grado1", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .setParameter("grado2", Integer.parseInt(obtenerVariable2().getValor().getValor()))
                .setParameter("grado3", Integer.parseInt(obtenerVariable3().getValor().getValor()))
                .setParameter("grado4", Integer.parseInt(obtenerVariable4().getValor().getValor()))
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
        if(grupo.getGrado() == 11 || grupo.getGrado() == 6) return ResultadoEJB.crearErroneo(3, "El grupo se encuentra en periodo de estadia", DtoAlumnosEncuesta.DtoGruposTutor.class);
        Grupo g = em.find(Grupo.class, grupo.getIdGrupo());
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, g.getPeriodo());
        DtoAlumnosEncuesta.DtoGruposTutor dto = new DtoAlumnosEncuesta.DtoGruposTutor(g.getIdGrupo(), pe, g.getGrado(), g.getLiteral().toString(), g.getTutor());
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> obtenerGruposSaiiut(Integer clave){
        List<DtoAlumnosEncuesta.DtoGruposTutor> g = f2.getEntityManager().createQuery("SELECT g FROM Grupos as g where (g.grado = :grado1 or g.grado = :grado2 or g.grado = :grado3 or g.grado = :grado4) and g.cveMaestro = :clave and g.gruposPK.cvePeriodo = :periodo", Grupos.class)
                .setParameter("grado1", Short.parseShort(obtenerVariable1().getValor().getValor()))
                .setParameter("grado2", Short.parseShort(obtenerVariable2().getValor().getValor()))
                .setParameter("grado3", Short.parseShort(obtenerVariable3().getValor().getValor()))
                .setParameter("grado4", Short.parseShort(obtenerVariable4().getValor().getValor()))
                .setParameter("clave", clave)
                .setParameter("periodo", obtenerPeriodoTest().getValor())
                .getResultStream()
                .map(grupo -> packDtoSaiiut(grupo))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(g.isEmpty()) return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "La lista se creo sin información");
        return ResultadoEJB.crearCorrecto(g, "DTO creado correctamente");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoGruposTutor> packDtoSaiiut(Grupos grupo){
        if(grupo == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoGruposTutor.class);
        if(grupo.getIdGrupo() == null) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoGruposTutor.class);
        if(grupo.getGrado() == Short.parseShort("11") || grupo.getGrado() == Short.parseShort("6")) return ResultadoEJB.crearErroneo(2, "El grupo se encuentra en periodo de estadía", DtoAlumnosEncuesta.DtoGruposTutor.class);
        Grupos g = f2.getEntityManager().createQuery("select g from Grupos as g where g.gruposPK.cveGrupo = :cveGrupo", Grupos.class)
                .setParameter("cveGrupo", grupo.getGruposPK().getCveGrupo())
                .getResultStream().findFirst().orElse(new Grupos());
        if(g.equals(new Grupos())) return ResultadoEJB.crearErroneo(3, "El id es nulo", DtoAlumnosEncuesta.DtoGruposTutor.class);
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, g.getGruposPK().getCvePeriodo());
        DtoAlumnosEncuesta.DtoGruposTutor dto = new DtoAlumnosEncuesta.DtoGruposTutor(g.getGruposPK().getCveGrupo(), pe, (int)g.getGrado(), g.getIdGrupo(), g.getCveMaestro());
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<Boolean> darAccesoSeguimiento(){
        AperturaVisualizacionEncuestas ave = em.createQuery("select a from AperturaVisualizacionEncuestas as a where a.encuesta = :encuesta and :fecha BETWEEN a.fechaInicial and a.fechaFinal", AperturaVisualizacionEncuestas.class)
                .setParameter("fecha", new Date())
                .setParameter("encuesta", "Test de Diagnostico de Estilos de Aprendizaje")
                .getResultStream().findFirst().orElse(new AperturaVisualizacionEncuestas());
        if(ave.equals(new AperturaVisualizacionEncuestas())) return ResultadoEJB.crearCorrecto(Boolean.FALSE, "El seguimiento de este Test no esta activo");
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El seguimiento esta activo");
    }
    
    public ResultadoEJB<Integer> obtenerPeriodoTest(){
        VariablesProntuario vp = em.createQuery("SELECT v FROM VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoTest")
                .getResultStream().findFirst().orElse(new VariablesProntuario());
        if(vp.equals(new VariablesProntuario())) return ResultadoEJB.crearCorrecto(0, "No hay periodo encontrado");
        return ResultadoEJB.crearCorrecto(Integer.parseInt(vp.getValor()), "Periodo encontrado");
    }
    
    public ResultadoEJB<Grupo> obtenerGrupoSeleccionado(Integer cveGrupo){
        return ResultadoEJB.crearCorrecto(em.find(Grupo.class, cveGrupo), "Grupo seleccionado");
    }
    
    public ResultadoEJB<Grupos> obtenerGrupoSeleccionadoS(Integer cveGrupo){
        Grupos g = f2.getEntityManager().createQuery("select g from Grupos as g where g.gruposPK.cveGrupo = :cveGrupo", Grupos.class)
                .setParameter("cveGrupo", cveGrupo)
                .getResultStream().findFirst().orElse(new Grupos());
        return ResultadoEJB.crearCorrecto(g, "Grupo seleccionado");
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
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerEstudiantesCE(Integer idGrupo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto = em
                .createQuery("SELECT e from Estudiante as e "
                        + "where e.grupo.idGrupo = :cveGrupo and "
                        + "e.tipoEstudiante.idTipoEstudiante = :tipo ", Estudiante.class)
                .setParameter("cveGrupo", idGrupo)
                .setParameter("tipo", 1)
                .getResultStream()
                .map(estudiante -> ejbAdm.packEstudiantesEncuesta(estudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerEstudiantesCE(Short area, Integer periodo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto = em
                .createQuery("SELECT e from Estudiante as e where (e.grupo.grado = :grado1 or e.grupo.grado = :grado2 or e.grupo.grado = :grado3 or e.grupo.grado = :grado4) "
                        + "and e.carrera = :area and e.tipoEstudiante.idTipoEstudiante = :tipo and e.periodo = :periodo", Estudiante.class)
                .setParameter("grado1", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                .setParameter("grado2", Integer.parseInt(obtenerVariable2().getValor().getValor()))
                .setParameter("grado3", Integer.parseInt(obtenerVariable3().getValor().getValor()))
                .setParameter("grado4", Integer.parseInt(obtenerVariable4().getValor().getValor()))
                .setParameter("area", area)
                .setParameter("tipo", 1)
                .setParameter("periodo", periodo)
                .getResultStream()
                .map(estudiante -> ejbAdm.packEstudiantesEncuesta(estudiante))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerEstudiantes(Grupos grupo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaDto = f2.getEntityManager()
                .createQuery("SELECT a from Alumnos as a where a.grupos.gruposPK.cveGrupo = :cveGrupo and a.cveStatus = :tipo", Alumnos.class)
                .setParameter("cveGrupo", grupo.getGruposPK().getCveGrupo())
                .setParameter("tipo", 1)
                .getResultStream()
                .map(alumno -> ejbAdm.packEstudiantesEncuesta(alumno))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerEstudiantesSaiiut(Integer grupo){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaDto = f2.getEntityManager()
                .createQuery("SELECT a from Alumnos as a "
                        + "where a.grupos.gruposPK.cveGrupo = :cveGrupo and "
                        + "a.cveStatus = :tipo", Alumnos.class)
                .setParameter("cveGrupo", grupo)
                .setParameter("tipo", 1)
                .getResultStream()
                .map(alumno -> ejbAdm.packEstudiantesEncuesta(alumno))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerEstudiantesSaiiut(Short area, Integer periodo){
         List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaDto = f2.getEntityManager()
                .createQuery("SELECT a from Alumnos as a where (a.gradoActual = :grado1 or a.gradoActual = :grado2 or a.gradoActual = :grado3 or a.gradoActual = :grado4) and a.grupos.gruposPK.cveCarrera = :area and a.cveStatus = :tipo and a.grupos.gruposPK.cvePeriodo = :periodo", Alumnos.class)
                .setParameter("grado1", Short.parseShort(obtenerVariable1().getValor().getValor()))
                .setParameter("grado2", Short.parseShort(obtenerVariable2().getValor().getValor()))
                 .setParameter("grado3", Short.parseShort(obtenerVariable3().getValor().getValor()))
                 .setParameter("grado4", Short.parseShort(obtenerVariable4().getValor().getValor()))
                .setParameter("area", area)
                .setParameter("tipo", 1)
                .setParameter("periodo", periodo)
                .getResultStream()
                .map(alumno -> ejbAdm.packEstudiantesEncuesta(alumno))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa de estudiantes");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> packEncuestasCompletas(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoEstudiante){
        Comparador<TestDiagnosticoAprendizaje> comparador = new ComparadorTestDiagnosticoAprendizaje();
        if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(1, "", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("periodo", dtoEstudiante.getGrupos().getPeriodo())
                .getResultStream().findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "No hay evaluacion registrada para este periodo", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        TestDiagnosticoAprendizaje tda = em.createQuery("SELECT t FROM TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", dtoEstudiante.getAlumnos().getMatricula())
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        if(!comparador.isCompleto(tda)) return ResultadoEJB.crearErroneo(2, "El alumno no ha ingresado o no ha concluido con su Test", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar(
                dtoEstudiante.getAlumnos(), dtoEstudiante.getPersonas(), dtoEstudiante.getGrupos(), dtoEstudiante.getAreasUniversidad(), 
                dtoEstudiante.getTutor(), dtoEstudiante.getDtoDirector(), dtoEstudiante.getSiglas(), dtoEstudiante.getGrupo(), dtoEstudiante.getGrado());
        return ResultadoEJB.crearCorrecto(dto, "Estudiante con Test Completo");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> packEncuestasCompletas(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dtoEstudiante){
        Comparador<TestDiagnosticoAprendizaje> comparador = new ComparadorTestDiagnosticoAprendizaje();
        if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(1, "", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("periodo", dtoEstudiante.getGrupos().getGruposPK().getCvePeriodo())
                .getResultStream().findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "No hay evaluacion registrada para este periodo", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        TestDiagnosticoAprendizaje tda = em.createQuery("SELECT t FROM TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", Integer.parseInt(dtoEstudiante.getAlumnos().getMatricula()))
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        if(!comparador.isCompleto(tda)) return ResultadoEJB.crearErroneo(2, "El alumno no ha ingresado o no ha concluido con su Test", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral(
                dtoEstudiante.getAlumnos(), dtoEstudiante.getPersonas(), dtoEstudiante.getGrupos(), dtoEstudiante.getCarrerasCgut(), 
                dtoEstudiante.getTutor(), dtoEstudiante.getDtoDirector(), dtoEstudiante.getSiglas(), dtoEstudiante.getGrupo(), dtoEstudiante.getGrado());
        return ResultadoEJB.crearCorrecto(dto, "Estudiante con Test Completo");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> packEncuestasIncompletas(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoEstudiante){
        Comparador<TestDiagnosticoAprendizaje> comparador = new ComparadorTestDiagnosticoAprendizaje();
        if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(1, "", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("periodo", dtoEstudiante.getGrupos().getPeriodo())
                .getResultStream().findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "No hay evaluacion registrada para este periodo", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        TestDiagnosticoAprendizaje tda = em.createQuery("SELECT t FROM TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", dtoEstudiante.getAlumnos().getMatricula())
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        if(comparador.isCompleto(tda)) return ResultadoEJB.crearErroneo(2, "El alumno ha concluido con su Test", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar(
                dtoEstudiante.getAlumnos(), dtoEstudiante.getPersonas(), dtoEstudiante.getGrupos(), dtoEstudiante.getAreasUniversidad(), 
                dtoEstudiante.getTutor(), dtoEstudiante.getDtoDirector(), dtoEstudiante.getSiglas(), dtoEstudiante.getGrupo(), dtoEstudiante.getGrado());
        return ResultadoEJB.crearCorrecto(dto, "Estudiante con Test Incompleto");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> packEncuestasIncompletas(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dtoEstudiante){
        Comparador<TestDiagnosticoAprendizaje> comparador = new ComparadorTestDiagnosticoAprendizaje();
        if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(1, "", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("periodo", dtoEstudiante.getGrupos().getGruposPK().getCvePeriodo())
                .getResultStream().findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "No hay evaluacion registrada para este periodo", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        TestDiagnosticoAprendizaje tda = em.createQuery("SELECT t FROM TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", Integer.parseInt(dtoEstudiante.getAlumnos().getMatricula()))
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        if(comparador.isCompleto(tda)) return ResultadoEJB.crearErroneo(2, "El alumno no ha ingresado o no ha concluido con su Test", DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral(
                dtoEstudiante.getAlumnos(), dtoEstudiante.getPersonas(), dtoEstudiante.getGrupos(), dtoEstudiante.getCarrerasCgut(), 
                dtoEstudiante.getTutor(), dtoEstudiante.getDtoDirector(), dtoEstudiante.getSiglas(), dtoEstudiante.getGrupo(), dtoEstudiante.getGrado());
        return ResultadoEJB.crearCorrecto(dto, "Estudiante con Test Completo");
    }
    
    public ResultadoEJB<TestDiagnosticoAprendizaje> obtenerTest(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoEstudiante){
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("periodo", dtoEstudiante.getGrupos().getPeriodo())
                .getResultStream().findFirst().get();
        TestDiagnosticoAprendizaje tda = em.createQuery("SELECT t FROM TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", dtoEstudiante.getAlumnos().getMatricula())
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        return ResultadoEJB.crearCorrecto(tda, "");
    }
    
    public ResultadoEJB<TestDiagnosticoAprendizaje> obtenerTest(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dtoEstudiante){
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("periodo", dtoEstudiante.getGrupos().getGruposPK().getCvePeriodo())
                .getResultStream().findFirst().get();
        TestDiagnosticoAprendizaje tda = em.createQuery("SELECT t FROM TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", e.getEvaluacion())
                .setParameter("evaluador", Integer.parseInt(dtoEstudiante.getAlumnos().getMatricula()))
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        return ResultadoEJB.crearCorrecto(tda, "");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> obtenerProgramasEducativo(Integer periodoEscolar){
        List<DtoAlumnosEncuesta.DtoProgramaEducativo> dtoPE;
        List<DtoAlumnosEncuesta.DtoProgramaEducativo> dtoPECE;
        List<DtoAlumnosEncuesta.DtoProgramaEducativo> dtoPESA;
        List<DtoAlumnosEncuesta.DtoProgramaEducativo> dtoPESA1;
         dtoPECE = em.createQuery("select g from Grupo as g where g.periodo = :periodo GROUP BY g.idPe", Grupo.class)
                 .setParameter("periodo", periodoEscolar)
                 .getResultStream()
                 .map(au -> packProgramaEducativo(au))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
         dtoPESA = f2.getEntityManager().createQuery("SELECT g from Grupos as g where g.gruposPK.cvePeriodo = :cvePeriodo", Grupos.class)
                .setParameter("cvePeriodo", periodoEscolar)
                .getResultStream()
                .map(carrera -> packProgramaEducativo(carrera))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
         dtoPESA1 = dtoPESA.stream().distinct().collect(Collectors.toList());
         dtoPE = Stream.concat(dtoPECE.stream(), dtoPESA1.stream()).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoPE, "Programas Educativos completos");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoProgramaEducativo> packProgramaEducativo(Grupo grupo){
        if(grupo == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        if(grupo.getIdGrupo() == null) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        AreasUniversidad au = em.find(AreasUniversidad.class, grupo.getIdPe());
        DtoAlumnosEncuesta.DtoProgramaEducativo dto = new DtoAlumnosEncuesta.DtoProgramaEducativo(au.getArea(), au.getNombre(), au.getSiglas(), au.getNivelEducativo().getNivel(), "Control Escolar");
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoProgramaEducativo> packProgramaEducativo(Grupos carrera){
        if(carrera == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        CarrerasCgut au = f2.getEntityManager().createQuery("select c from CarrerasCgut as c where c.cveCarrera = :carrera", CarrerasCgut.class)
                .setParameter("carrera", carrera.getGruposPK().getCveCarrera())
                .getResultStream().findFirst().orElse(new CarrerasCgut());
        DtoAlumnosEncuesta.DtoProgramaEducativo dto = new DtoAlumnosEncuesta.DtoProgramaEducativo(au.getCveCarrera().shortValue(), au.getNombre(), au.getAbreviatura(),
                "", "SAIIUT");
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoPeriodoEscolar>> obtenerPeriodosEscolares(){
        List<DtoAlumnosEncuesta.DtoPeriodoEscolar> listDtoPE = em.createQuery("select p from PeriodosEscolares as p where p.periodo >= :periodo ORDER BY p.periodo desc", PeriodosEscolares.class)
                .setParameter("periodo", 57)
                .getResultStream()
                .map(periodo -> packPeriodoEscolar(periodo))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(listDtoPE, "PeriodosEscolares completos");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoPeriodoEscolar> packPeriodoEscolar(PeriodosEscolares periodoEscolar){
        if(periodoEscolar == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoPeriodoEscolar.class);
        if(periodoEscolar.getPeriodo()== null) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoPeriodoEscolar.class);
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, periodoEscolar.getPeriodo());
        DtoAlumnosEncuesta.DtoPeriodoEscolar dto = new DtoAlumnosEncuesta.DtoPeriodoEscolar(pe.getPeriodo(), pe.getMesInicio().getMes()+"-"+pe.getMesFin().getMes()+" "+pe.getAnio(), 
        String.valueOf(pe.getAnio()), pe.getCiclo().getCiclo());
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> obtenerGrupos(Short programa, Integer periodo, String bd){
        List<DtoAlumnosEncuesta.DtoGruposTutor> dtoT = new ArrayList<>();
        if(bd.equals("Control Escolar")){
            dtoT = em.createQuery("select g from Grupo as g where (g.grado = :grado1 or g.grado = :grado2 or g.grado = :grado3 or g.grado = :grado4) and g.idPe = :idPE and g.periodo = :periodo", Grupo.class)
                    .setParameter("grado1", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                    .setParameter("grado2", Integer.parseInt(obtenerVariable2().getValor().getValor()))
                    .setParameter("grado3", Integer.parseInt(obtenerVariable3().getValor().getValor()))
                    .setParameter("grado4", Integer.parseInt(obtenerVariable4().getValor().getValor()))
                .setParameter("idPE", programa)
                .setParameter("periodo", periodo)
                .getResultStream()
                .map(grupo -> packDto(grupo))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        }
        if(bd.equals("SAIIUT")){
            dtoT = f2.getEntityManager().createQuery("select g from Grupos as g where (g.grado = :grado1 or g.grado = :grado2 or g.grado = :grado3 or g.grado = :grado4) and g.gruposPK.cveCarrera = :cveCarrera and g.gruposPK.cvePeriodo = :periodo", Grupos.class)
                    .setParameter("grado1", Integer.parseInt(obtenerVariable1().getValor().getValor()))
                    .setParameter("grado2", Integer.parseInt(obtenerVariable2().getValor().getValor()))
                    .setParameter("grado3", Integer.parseInt(obtenerVariable3().getValor().getValor()))
                    .setParameter("grado4", Integer.parseInt(obtenerVariable4().getValor().getValor()))
                    .setParameter("cveCarrera", programa.intValue())
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .map(grupo -> packDtoSaiiut(grupo))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
        }
        if(dtoT.isEmpty()) return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "La lista se creo sin información");
        return ResultadoEJB.crearCorrecto(dtoT, "Lista completa");
    }
    
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> obtenerProgramasEducativo(Short area ){
        List<DtoAlumnosEncuesta.DtoProgramaEducativo> dtoPE = em.createQuery("SELECT a from AreasUniversidad as a where a.areaSuperior = :area and a.vigente = :vigente", AreasUniversidad.class)
                .setParameter("area", area)
                .setParameter("vigente", "1")
                .getResultStream()
                .map(au -> packProgramaEducativo(au))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoPE, "Empaquetado correctamente");
    }
    
    public ResultadoEJB<DtoAlumnosEncuesta.DtoProgramaEducativo> packProgramaEducativo(AreasUniversidad area){
        if(area == null) return ResultadoEJB.crearErroneo(1, "Es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        if(area.getArea()== null) return ResultadoEJB.crearErroneo(2, "El id es nulo", DtoAlumnosEncuesta.DtoProgramaEducativo.class);
        AreasUniversidad au = em.find(AreasUniversidad.class, area.getArea());
        DtoAlumnosEncuesta.DtoProgramaEducativo dto = new DtoAlumnosEncuesta.DtoProgramaEducativo(au.getArea(), au.getNombre(), au.getSiglas(), au.getNivelEducativo().getNivel(), "Control Escolar");
        return ResultadoEJB.crearCorrecto(dto, "DTO creado correctamente");
    }
    
    public ResultadoEJB<Evaluaciones> guardarTest(Evaluaciones evaluacion, Integer periodo, Date fechaInicio, Date fechaFin, String tipo, Operacion operacion){
        try{
            //if(test == null) return ResultadoEJB.crearErroneo(2, "El grupo no puede ser nulo.", Evaluaciones.class);
            if(operacion == null) return ResultadoEJB.crearErroneo(3, "La operación no debe ser nula.", Evaluaciones.class);
            //System.out.println("Test"+test);
            Evaluaciones e = em.createQuery("select e from Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                    .setParameter("periodo", periodo)
                    .setParameter("tipo", tipo)
                    .getResultStream().findFirst().orElse(new Evaluaciones());
            //System.out.println("Evaluación disponible"+ e);
            //System.out.println("Grupos registrados:"+ gruposReg);
            switch (operacion){
                case PERSISTIR:
                    if(e.getEvaluacion() == null){
                         e.setPeriodo(periodo);
                        e.setFechaInicio(fechaInicio);
                        e.setFechaFin(fechaFin);
                        e.setTipo(tipo);
                        em.persist(e);
                        return ResultadoEJB.crearCorrecto(e, "El grupo ha sido agregado correctamente");
                    }else{
                        return ResultadoEJB.crearErroneo(4, "No se pudo agregar, ya hay un Test activo en el periodo seleccionado.", Evaluaciones.class);
                    }
                 case ACTUALIZAR:
                    em.merge(evaluacion);
                    return ResultadoEJB.crearCorrecto(null, "Los datos de grupo se han actualizado correctamente.");    
                    
                default:
                    return ResultadoEJB.crearErroneo(5, "Operación no autorizada.", Evaluaciones.class);

            }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo. (EjbGeneracionGrupos.guardarGrupo())", e, null);
        }
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerVariable1(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "gradoTest1")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 1");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerVariable2(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "gradoTest2")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 2");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerVariable3(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "gradoTest3")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 3");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerVariable4(){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "gradoTest4")
                .getResultStream()
                .findFirst()
                .get();
        return ResultadoEJB.crearCorrecto(vp, "Variable 4");
    }
    
    public ResultadoEJB<VariablesProntuario> actualizarVariable(String nombre, String valor, Operacion operacion){
        try {
            if(operacion == null) return ResultadoEJB.crearErroneo(3, "La operación no debe ser nula.", VariablesProntuario.class);
            //System.out.println("Test"+test);
            VariablesProntuario e = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                    .setParameter("nombre", nombre)
                    .getResultStream().findFirst().orElse(new VariablesProntuario());
            //System.out.println("Evaluación disponible"+ e);
            //System.out.println("Grupos registrados:"+ gruposReg);
            switch (operacion){
                case ACTUALIZAR:
                    e.setValor(valor);
                    em.merge(e);
                    return ResultadoEJB.crearCorrecto(null, "Los datos de grupo se han actualizado correctamente.");    
                    
                default:
                    return ResultadoEJB.crearErroneo(5, "Operación no autorizada.", VariablesProntuario.class);

            }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo. (EjbGeneracionGrupos.guardarGrupo())", e, null);
        }
    }
    
    public ResultadoEJB<List<Evaluaciones>> obtenerListaTest(){
        List<Evaluaciones> e = em.createQuery("select e from Evaluaciones as e where e.tipo = :tipo order by e.evaluacion desc", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .getResultStream().collect(Collectors.toList());
        if(e.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "No hay lista disponible");
        return ResultadoEJB.crearCorrecto(e, "Lista completa");
    }
    
}
