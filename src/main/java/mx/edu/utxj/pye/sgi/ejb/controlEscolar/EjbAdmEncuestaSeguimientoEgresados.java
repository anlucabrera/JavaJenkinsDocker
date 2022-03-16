/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteEncuesta;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaEmpleadores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaSeguimientoEgresados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaSeguimientoEgresadosPK;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
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
public class EjbAdmEncuestaSeguimientoEgresados {

    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    
    private EntityManager em;
    private EntityManager em2;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarResponsableAdministracion(Integer clave){   
        try {
            String pista = ",".concat(String.valueOf(clave).trim()).concat(",").trim();
            ConfiguracionPropiedades claveConfiguracionPropiedad = Objects.requireNonNull(em.createQuery("SELECT c FROM ConfiguracionPropiedades c WHERE c.tipo = :tipo AND c.clave = :clave AND c.valorCadena LIKE CONCAT('%',:pista,'%')", ConfiguracionPropiedades.class)
                    .setParameter("tipo", "Cadena")
                    .setParameter("pista", pista)
                    .setParameter("clave", "administrarEncuesta")
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
    
    public ResultadoEJB<Evaluaciones> obtenerEncuesta(Integer periodo, String tipo){
        try {
            if(periodo == null) return ResultadoEJB.crearErroneo(3, "El periodo no debe ser nulo", Evaluaciones.class);
            Evaluaciones e = em.createQuery("select e from Evaluaciones as e where e.tipo = :tipo and e.periodo = :periodo ORDER BY e.periodo desc", Evaluaciones.class)
                    .setParameter("periodo", periodo)
                    .setParameter("tipo", tipo)
                    .getResultStream()
                    .findFirst().orElse(new Evaluaciones());
            if (e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(2, "Evaluacion vacia", Evaluaciones.class);
            return ResultadoEJB.crearCorrecto(e, "Lista encontrada");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Información no encontrada.", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> verificarEncuestaActiva(Integer evaluacion, String tipo){
        Evaluaciones eventoEscolar = em.createQuery(
                    "SELECT e FROM Evaluaciones e WHERE e.evaluacion = :evaluacion and e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("tipo", tipo)
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(new Evaluaciones());
        if(eventoEscolar.equals(new Evaluaciones())) return ResultadoEJB.crearCorrecto(Boolean.FALSE, "Encuesta no activa");
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Encuesta activa");
    }
    
    public ResultadoEJB<Evaluaciones> actualizarFecha(Integer periodo,Integer indicador, Date fecha, Operacion operacion){
        try {
            if(periodo == null) return ResultadoEJB.crearErroneo(2, "El periodo es nulo", Evaluaciones.class);
            if(operacion == null) return ResultadoEJB.crearErroneo(3, "La operación no debe ser nula.", Evaluaciones.class);
            Evaluaciones e = em.createQuery("select e from Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                    .setParameter("periodo", periodo)
                    .setParameter("tipo", "Encuesta Seguimiento de Egresados")
                    .getResultStream().findFirst().orElse(new Evaluaciones());
            if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(4, "La evaluacion es nula", Evaluaciones.class);
            switch (operacion){
                case ACTUALIZAR:
                    if (indicador.equals(1)) {
                        e.setFechaInicio(fecha);
                        em.merge(e);
                        return ResultadoEJB.crearCorrecto(e, "Los datos de grupo se han actualizado correctamente.");
                    }
                    if (indicador.equals(2)) {
                        e.setFechaFin(fecha);
                        em.merge(e);
                        return ResultadoEJB.crearCorrecto(e, "Los datos de grupo se han actualizado correctamente.");
                    }
                default:
                    return ResultadoEJB.crearErroneo(5, "Operación no autorizada.", Evaluaciones.class);

            }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo. (EjbGeneracionGrupos.guardarGrupo())", e, null);
        }
    }
    
    public ResultadoEJB<DtoEstudianteEncuesta.DtoEstudianteControlEscolar> obtenerInformacionEstudiante(Integer matricula, Integer periodo){
        DtoEstudianteEncuesta.DtoEstudianteControlEscolar e = em.createQuery("select e from Estudiante as e where e.matricula = :matricula and e.periodo = :periodo and e.tipoEstudiante.idTipoEstudiante = :tipo and (e.grupo.grado = :grado1 or e.grupo.grado = :grado2) ORDER BY e.grupo.grado DESC", Estudiante.class)
                .setParameter("matricula", matricula)
                .setParameter("periodo", periodo)
                .setParameter("tipo", Short.parseShort(obtenerEstatus("estatusEstudianteCE").getValor()))
                .setParameter("grado1", 6)
                .setParameter("grado2", 11)
                .getResultStream()
                .map(estudiante -> pack(estudiante))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .findFirst()
                .orElse(new DtoEstudianteEncuesta.DtoEstudianteControlEscolar(new Estudiante()));
        if(e.getEstudiante().equals(new Estudiante())) return ResultadoEJB.crearErroneo(1, "No se encontro informacion del estudiante", DtoEstudianteEncuesta.DtoEstudianteControlEscolar.class);
        return ResultadoEJB.crearCorrecto(e, "Información encontrada");
    }
    
     public ResultadoEJB<DtoEstudianteEncuesta.DtoEstudianteControlEscolar> pack(Estudiante e){
         if(e.equals(new Estudiante())) return ResultadoEJB.crearErroneo(1, "No hay objeto de estudiante", DtoEstudianteEncuesta.DtoEstudianteControlEscolar.class);
         DtoEstudianteEncuesta.DtoEstudianteControlEscolar dto;
         Estudiante estudiante = em.find(Estudiante.class, e.getIdEstudiante());
         Grupo grupo = em.find(Grupo.class, estudiante.getGrupo().getIdGrupo());
         PeriodosEscolares periodo = em.find(PeriodosEscolares.class, grupo.getPeriodo());
         Generaciones generaciones = em.find(Generaciones.class, grupo.getGeneracion());
         Aspirante aspirante = em.find(Aspirante.class, estudiante.getAspirante().getIdAspirante());
         Persona persona = em.find(Persona.class, aspirante.getIdPersona().getIdpersona());
         
         dto = new DtoEstudianteEncuesta.DtoEstudianteControlEscolar(estudiante, grupo, periodo, generaciones, persona, aspirante);
         return ResultadoEJB.crearCorrecto(dto, "Se ha empaquetado correctamente");
     }
     
     public ResultadoEJB<DtoEstudianteEncuesta.DtoEstudianteSaiiut> obtenerInformacionEstudiante(String matricula, Integer periodo){
         
         DtoEstudianteEncuesta.DtoEstudianteSaiiut e = em2.createQuery("select a from Alumnos as a where a.matricula = :matricula and a.grupos.gruposPK.cvePeriodo = :periodo and a.cveStatus = :tipo and (a.gradoActual = :grado1 or a.gradoActual = :grado2) ORDER BY a.gradoActual DESC", Alumnos.class)
                .setParameter("matricula", matricula.length() < 6 ? "0".concat(matricula): matricula)
                 .setParameter("periodo", periodo)
                .setParameter("tipo", Integer.parseInt(obtenerEstatus("estatusEstudianteSA").getValor()))
                .setParameter("grado1", 6)
                .setParameter("grado2", 11)
                .getResultStream()
                .map(alumno -> pack(alumno))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .findFirst()
                .orElse(new DtoEstudianteEncuesta.DtoEstudianteSaiiut(new Alumnos()));
        if(e.getAlumno().equals(new Alumnos())) return ResultadoEJB.crearErroneo(1, "No se encontro informacion del estudiante", DtoEstudianteEncuesta.DtoEstudianteSaiiut.class);
        return ResultadoEJB.crearCorrecto(e, "Información encontrada");
     }
     
     public ResultadoEJB<DtoEstudianteEncuesta.DtoEstudianteSaiiut> pack(Alumnos a){
         if(a.equals(new Alumnos())) return ResultadoEJB.crearErroneo(1, "No hay objeto de estudiante", DtoEstudianteEncuesta.DtoEstudianteSaiiut.class);
         DtoEstudianteEncuesta.DtoEstudianteSaiiut dto;
         Alumnos alumnos = em2.createQuery("select a from Alumnos as a where a.alumnosPK.cveAlumno = :cveAlumno", Alumnos.class)
                 .setParameter("cveAlumno", a.getAlumnosPK().getCveAlumno())
                 .getResultStream().findFirst().orElse(new Alumnos());
         Grupos grupo = em2.createQuery("select g from Grupos as g where g.gruposPK.cveGrupo = :cveGrupo", Grupos.class)
                 .setParameter("cveGrupo", alumnos.getGrupos().getGruposPK().getCveGrupo())
                 .getResultStream().findFirst().orElse(new Grupos());
         CarrerasCgut carrera = em2.createQuery("select c from CarrerasCgut as c where c.cveCarrera = :cveCarrera", CarrerasCgut.class)
                 .setParameter("cveCarrera", alumnos.getGrupos().getGruposPK().getCveCarrera())
                 .getResultStream().findFirst().orElse(new CarrerasCgut());
         Personas persona = em2.createQuery("select p from Personas as p where p.personasPK.cvePersona = :cvePersona", Personas.class)
                 .setParameter("cvePersona", alumnos.getAlumnosPK().getCveAlumno())
                 .getResultStream().findFirst().orElse(new Personas());
         dto = new DtoEstudianteEncuesta.DtoEstudianteSaiiut(alumnos, grupo, carrera, persona);
         return ResultadoEJB.crearCorrecto(dto, "Se ha empaquetado correctamente");
     }
     
     public ResultadoEJB<EncuestaSeguimientoEgresados> realizarRegistro(Integer matricula, Integer evaluacion, Operacion operacion){
        try{
            if(matricula == null) return ResultadoEJB.crearErroneo(2, "La matrícula es nula.", EncuestaSeguimientoEgresados.class);
            if(evaluacion == null) return ResultadoEJB.crearErroneo(3, "No hay una evaluacion con el id especificado.", EncuestaSeguimientoEgresados.class);
            if(operacion == null) return ResultadoEJB.crearErroneo(4, "La operación no debe ser nula.", EncuestaSeguimientoEgresados.class);
            EncuestaSeguimientoEgresadosPK pk = new EncuestaSeguimientoEgresadosPK(evaluacion, matricula);
            f.setEntityClass(EncuestaSeguimientoEgresados.class);

            EncuestaSeguimientoEgresados r = (EncuestaSeguimientoEgresados) f.find(pk);

            
            switch (operacion){
                case PERSISTIR:
                    if (r == null) {
                        r = new EncuestaSeguimientoEgresados(pk);
                        em.persist(r);
                        return ResultadoEJB.crearCorrecto(r, "Registro realizado con éxito.");
                    }else{
                        return ResultadoEJB.crearErroneo(5, "Ya existe un registro con los datos especificados.", EncuestaSeguimientoEgresados.class);
                    }
                default:
                    return ResultadoEJB.crearErroneo(6, "Operación no autorizada.", EncuestaSeguimientoEgresados.class);

            }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo. (EjbGeneracionGrupos.guardarGrupo())", e, null);
        }
    }
     
    public ResultadoEJB<Evaluaciones> guardarEncuesta(Integer periodo,String tipo, Date fechaIn, Date fechaFin ,Operacion operacion){
        try{
            if(periodo == null) return ResultadoEJB.crearErroneo(1, "La variable periodo no puede ser nulo", Evaluaciones.class);
            if(fechaIn == null) return ResultadoEJB.crearErroneo(2, "La variable fecha inicio no puede se nulo", Evaluaciones.class);
            if(fechaFin == null) return ResultadoEJB.crearErroneo(3, "La variable fecha fin no puede se nulo", Evaluaciones.class);
            if(operacion == null) return ResultadoEJB.crearErroneo(4, "La operación no puede se nula", Evaluaciones.class);
            Evaluaciones evaluacion = em.createQuery("select e from Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                    .setParameter("periodo", periodo)
                    .setParameter("tipo", tipo)
                    .getResultStream()
                    .findFirst().orElse(new Evaluaciones());
            ////System.out.println("Evaluación buscada:" + evaluacion);
            switch (operacion){
                   case PERSISTIR:
                        if (evaluacion.equals(new Evaluaciones())) {
                            ////System.out.println("Periodo" + periodo + " Fecha inicio:" + fechaIn);
                            Evaluaciones e = evaluacion;
                            e.setPeriodo(periodo);
                            e.setFechaInicio(fechaIn);
                            e.setFechaFin(fechaFin);
                            e.setTipo(tipo);
                            ////System.out.println(e);
                            em.persist(e);
                            return ResultadoEJB.crearCorrecto(e, "Registro realizado con éxito.");
                        }else{
                            return ResultadoEJB.crearErroneo(5, "Ya existe un registro con los datos especificados.", Evaluaciones.class);
                        }
                   case ACTUALIZAR:
                       if(evaluacion.getEvaluacion() != null){
                           evaluacion.setFechaInicio(fechaIn);
                           evaluacion.setFechaFin(fechaFin);
                           em.merge(evaluacion);
                           return ResultadoEJB.crearCorrecto(evaluacion, "Actualización realizado con éxito.");
                       }else{
                           return ResultadoEJB.crearErroneo(7, "No se pudo realizar la actualización con los datos especificados.", Evaluaciones.class);
                       }
                    default:
                        return ResultadoEJB.crearErroneo(6, "Operación no autorizada.", Evaluaciones.class);

                }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo. (EjbGeneracionGrupos.guardarGrupo())", e, null);
        }
    }
    
    public ResultadoEJB<List<Estudiante>> obtenerLista(Evaluaciones evaluacion){
        List<Estudiante> lista = em.createQuery("select e from Estudiante as e where e.tipoEstudiante.idTipoEstudiante = :tipoEstudiante and (e.grupo.grado = :grado or e.grupo.grado = :grado2) and e.periodo = :periodo", Estudiante.class)
                .setParameter("tipoEstudiante", Short.parseShort(obtenerEstatus("estatusEstudianteCE").getValor()))
                .setParameter("grado", 6)
                .setParameter("grado2", 11)
                .setParameter("periodo", evaluacion.getPeriodo())
                .getResultStream().collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
     
    public ResultadoEJB<List<DtoEstudianteEncuesta.DtoGeneraciones>> obtenerListaGeneraciones(){
         List<DtoEstudianteEncuesta.DtoGeneraciones> listaDto = em.createQuery("select p from PeriodosEscolares as p "
                 + "where (p.mesInicio.numero = :mesInicio or p.mesInicio.numero = :mesInicio2) ORDER BY p.periodo desc", PeriodosEscolares.class)
                 .setParameter("mesInicio", Short.parseShort("1"))
                 .setParameter("mesInicio2", Short.parseShort("5"))
                 .getResultStream()
                 .map(periodo -> pack(periodo))
                 .distinct()
                 .filter(ResultadoEJB::getCorrecto)
                 .map(ResultadoEJB::getValor)
                 .collect(Collectors.toList());
         if(listaDto.isEmpty()) return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Lista vacia");
         return ResultadoEJB.crearCorrecto(listaDto, "Lista completa");
     }
     
    public ResultadoEJB<DtoEstudianteEncuesta.DtoGeneraciones> pack(PeriodosEscolares periodo){
         if(periodo.equals(new PeriodosEscolares())) return ResultadoEJB.crearErroneo(1, "El priodo es nula", DtoEstudianteEncuesta.DtoGeneraciones.class);
         DtoEstudianteEncuesta.DtoGeneraciones dto = new DtoEstudianteEncuesta.DtoGeneraciones();
         PeriodosEscolares periodoBD = em.find(PeriodosEscolares.class, periodo.getPeriodo());
         String nivel;
         if(periodoBD.getMesInicio().getNumero().equals(Short.parseShort("1"))){
             nivel = "ING";
             dto = new DtoEstudianteEncuesta.DtoGeneraciones(periodoBD.getPeriodo(), periodoBD.getAnio(), nivel);
         }
         if(periodoBD.getMesInicio().getNumero().equals(Short.parseShort("5"))){
             nivel = "TSU";
             dto = new DtoEstudianteEncuesta.DtoGeneraciones(periodoBD.getPeriodo(), periodoBD.getAnio(), nivel);
         }
         return ResultadoEJB.crearCorrecto(dto, "Empaquetado completo");
    }
    
    public ResultadoEJB<List<DtoEstudianteEncuesta.DtoEstudiante>> obtenerEstudiante(Integer periodo){
        List<DtoEstudianteEncuesta.DtoEstudiante> listaDto = new ArrayList<>();
        
        List<Estudiante> listaEstudiantes = em.createQuery("select e from Estudiante as e where (e.grupo.grado = :grado1 or e.grupo.grado = :grado2) and e.periodo = :periodo and e.tipoEstudiante.idTipoEstudiante = :tipo", Estudiante.class)
                .setParameter("grado1", 6)
                .setParameter("grado2", 11)
                .setParameter("periodo", periodo)
                .setParameter("tipo", Short.parseShort(obtenerEstatus("estatusEstudianteCE").getValor())) //Para prueba se coloca el estatus 1 ya que para este momento se encuentran activos, cambiar a 4 cuando ya vaya a ser liberado
                .getResultStream().collect(Collectors.toList());
        if(!listaEstudiantes.isEmpty()){
            listaDto = listaEstudiantes.stream()
                    .map(estudiante -> packEstudiante(estudiante))
                    .distinct()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
        }
        
        List<Alumnos> listaAlumnos = em2.createQuery("select a from Alumnos as a where (a.gradoActual = :grado1 or a.gradoActual = :grado2) and a.grupos.gruposPK.cvePeriodo = :periodo and a.cveStatus = :estatus", Alumnos.class)
                .setParameter("grado1", 6)
                .setParameter("grado2", 11)
                .setParameter("periodo", periodo)
                .setParameter("estatus", Integer.parseInt(obtenerEstatus("estatusEstudianteSA").getValor())) //Para prueba se coloca el estatus 1 ya que para este momento se encuentran activos, cambiar a 6 cuando ya vaya a ser liberado
                .getResultStream().collect(Collectors.toList());
        if(!listaAlumnos.isEmpty()){
            listaDto = listaAlumnos.stream()
                    .map(alumno -> packEstudiante(alumno))
                    .distinct()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
        }
        
        return ResultadoEJB.crearCorrecto(listaDto, "Lista completa");
    }
    
    public ResultadoEJB<DtoEstudianteEncuesta.DtoEstudiante> packEstudiante(Estudiante estudiante){
        if(estudiante.equals(new Estudiante())) return ResultadoEJB.crearErroneo(1, "El estudiante no puede ser enviado como objeto vacio", DtoEstudianteEncuesta.DtoEstudiante.class);
        Estudiante estudianteBD = em.find(Estudiante.class, estudiante.getIdEstudiante());
        return ResultadoEJB.crearCorrecto(new DtoEstudianteEncuesta.DtoEstudiante(
                estudianteBD.getMatricula(), 
                estudianteBD.getAspirante().getIdPersona().getNombre() +" "+ 
                        estudianteBD.getAspirante().getIdPersona().getApellidoPaterno() +" "+ 
                        estudianteBD.getAspirante().getIdPersona().getApellidoMaterno(),
                (short) estudianteBD.getGrupo().getGrado(),
                estudianteBD.getGrupo().getLiteral().toString(),
                estudianteBD.getGrupo().getPlan().getDescripcion(),
                estudianteBD.getPeriodo(), "Control Escolar"), "Se empaqueto con exit");
    }
    
    public ResultadoEJB<DtoEstudianteEncuesta.DtoEstudiante> packEstudiante(Alumnos alumno){
        if(alumno.equals(new Alumnos())) return ResultadoEJB.crearErroneo(1, "El estudiante no puede ser enviado como objeto vacio", DtoEstudianteEncuesta.DtoEstudiante.class);
        Alumnos alumnoBD = em2.createQuery("select a from Alumnos as a where a.alumnosPK.cveAlumno = :clave", Alumnos.class)
                .setParameter("clave", alumno.getAlumnosPK().getCveAlumno())
                .getResultStream().findFirst().orElse(new Alumnos());
        CarrerasCgut carrera = em2.createQuery("select c from CarrerasCgut as c where c.cveCarrera = :clave", CarrerasCgut.class)
                .setParameter("clave", alumnoBD.getGrupos().getGruposPK().getCveCarrera())
                .getResultStream()
                .findFirst().orElse(new CarrerasCgut());
        Personas persona = em2.createQuery("select p from Personas as p where p.personasPK.cvePersona = :clave", Personas.class)
                .setParameter("clave", alumnoBD.getAlumnosPK().getCveAlumno())
                .getResultStream().findFirst().orElse(new Personas());
        return ResultadoEJB.crearCorrecto(new DtoEstudianteEncuesta.DtoEstudiante(
                Integer.parseInt(alumnoBD.getMatricula()), 
                persona.getNombre()+" "+persona.getApellidoPat()+" "+persona.getApellidoMat(),
                alumnoBD.getGradoActual(),
                alumnoBD.getGrupos().getIdGrupo(),
                carrera.getNombre(),
                alumnoBD.getGrupos().getGruposPK().getCvePeriodo(), "Saiiut"), "Se empaqueto con exito");
    }
    
    public ResultadoEJB<List<DtoEstudianteEncuesta.DtoInformacionResultados>> obtenerListaResultadosEJB(Evaluaciones evaluacion){
        try {
            //////System.out.println("Primera parte - Evaluacion: "+ evaluacion);
            List<DtoEstudianteEncuesta.DtoInformacionResultados> lista = em.createQuery("select e from EncuestaSeguimientoEgresados as e where e.encuestaSeguimientoEgresadosPK.evaluacion = :evaluacion", EncuestaSeguimientoEgresados.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .getResultStream()
                    .map(encuesta -> pack(evaluacion, encuesta))
                    .distinct()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(lista, "Lista completa.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo.", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoEstudianteEncuesta.DtoInformacionResultados2>> obtenerListaResultadosEmpleadores(Evaluaciones evaluacion){
        try {
            //////System.out.println("Primera parte - Evaluacion: "+ evaluacion);
            List<DtoEstudianteEncuesta.DtoInformacionResultados2> lista = em.createQuery("select e from EncuestaEmpleadores as e where e.encuestaEmpleadoresPK.evaluacion = :evaluacion", EncuestaEmpleadores.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .getResultStream()
                    .map(encuesta -> pack(evaluacion, encuesta))
                    .distinct()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(lista, "Lista completa.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo.", e, null);
        }
    }
    
    public ResultadoEJB<DtoEstudianteEncuesta.DtoInformacionResultados2> pack(Evaluaciones evaluacion, EncuestaEmpleadores encuesta){
        try {
            if(encuesta.equals(new EncuestaEmpleadores())) return ResultadoEJB.crearErroneo(2, "La evaluación y el evaluador no pueden ser nulos", DtoEstudianteEncuesta.DtoInformacionResultados2.class);
            if(evaluacion.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "La evaluacion no puede ser nulo", DtoEstudianteEncuesta.DtoInformacionResultados2.class);
            EncuestaEmpleadores ese = em.createQuery("select e from EncuestaEmpleadores as e where e.encuestaEmpleadoresPK.evaluado = :evaluador and e.encuestaEmpleadoresPK.evaluacion = :evaluacion", EncuestaEmpleadores.class)
                    .setParameter("evaluador", encuesta.getEncuestaEmpleadoresPK().getEvaluado())
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .getResultStream().findFirst().orElse(new EncuestaEmpleadores());
            
            return ResultadoEJB.crearCorrecto(new DtoEstudianteEncuesta.DtoInformacionResultados2(ese.getEncuestaEmpleadoresPK().getEvaluado(), ese), "Empaquetado completo.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo.", e, null);
        }
    }
    
    public ResultadoEJB<DtoEstudianteEncuesta.DtoInformacionResultados> pack(Evaluaciones evaluacion, EncuestaSeguimientoEgresados encuesta){
        try {
            if(encuesta.equals(new EncuestaSeguimientoEgresados())) return ResultadoEJB.crearErroneo(2, "La evaluación y el evaluador no pueden ser nulos", DtoEstudianteEncuesta.DtoInformacionResultados.class);
            if(evaluacion.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(3, "La evaluacion no puede ser nulo", DtoEstudianteEncuesta.DtoInformacionResultados.class);
            EncuestaSeguimientoEgresados ese = em.createQuery("select e from EncuestaSeguimientoEgresados as e where e.encuestaSeguimientoEgresadosPK.evaluador = :evaluador and e.encuestaSeguimientoEgresadosPK.evaluacion = :evaluacion", EncuestaSeguimientoEgresados.class)
                    .setParameter("evaluador", encuesta.getEncuestaSeguimientoEgresadosPK().getEvaluador())
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .getResultStream().findFirst().orElse(new EncuestaSeguimientoEgresados());
            Estudiante estudiante = em.createQuery("select e from Estudiante as e where e.matricula = :matricula and e.periodo = :periodo", Estudiante.class)
                .setParameter("matricula", ese.getEncuestaSeguimientoEgresadosPK().getEvaluador())
                .setParameter("periodo", evaluacion.getPeriodo())
                .getResultStream().findFirst().orElse(new Estudiante());
            DtoEstudianteEncuesta.DtoEstudiante dto = new DtoEstudianteEncuesta.DtoEstudiante();
            if (!estudiante.equals(new Estudiante())) {
                //////System.out.println("Estudiante:" + estudiante.getIdEstudiante()+"-"+estudiante.getMatricula());
                dto = packEstudiante(estudiante).getValor();
            }
            String matricula;
            String evaluador = String.valueOf(ese.getEncuestaSeguimientoEgresadosPK().getEvaluador());
            if(evaluador.length() <= 5){
                matricula = "0".concat(evaluador);
            }else{
                matricula = evaluador;
            }
            Alumnos alumno = em2.createQuery("select a from Alumnos as a where a.matricula = :matricula and a.grupos.gruposPK.cvePeriodo = :periodo", Alumnos.class)
                    .setParameter("matricula", matricula)
                    .setParameter("periodo", evaluacion.getPeriodo())
                    .getResultStream()
                    .findFirst().orElse(new Alumnos());
            
            if (!alumno.equals(new Alumnos())) {
                //////System.out.println("Alumno:" + alumno.getAlumnosPK().getCveAlumno()+"-"+alumno.getMatricula());
                dto = packEstudiante(alumno).getValor();
            }
            return ResultadoEJB.crearCorrecto(new DtoEstudianteEncuesta.DtoInformacionResultados(ese.getEncuestaSeguimientoEgresadosPK().getEvaluador(), dto, ese), "Empaquetado completo.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el grupo.", e, null);
        }
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el nivel de escolaridad
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasNivelEscolaridad(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("4", "Técnico Superior Universitario", "TSU"));
        l.add(new SelectItem("3", "Licenciatura Profesional", "LIC"));
        l.add(new SelectItem("2", "Ingeniería", "ING"));
        l.add(new SelectItem("1", "Posgrado", "POS"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer si estudia actualmente
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasEstudias(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("3", "No estudias actualmente", "NO"));
        l.add(new SelectItem("2", "Universidad Tecnológica de Xicotepec de Juárez", "UTXJ"));
        l.add(new SelectItem("1", "Otra Institución de Educación Superior", "OTRA"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer su actividad actual
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasActActual(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "Busca empleo", "BUSCA"));
        l.add(new SelectItem("4", "Estudia", "ESTUDIA"));
        l.add(new SelectItem("3", "Labores del Hogar", "LAB. HOG"));
        l.add(new SelectItem("2", "Trabaja", "TRABAJA"));
        l.add(new SelectItem("1", "Trabaja y Estudia", "TRAB. y EST."));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer su actividad económica
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasActEconomico(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("11", "Agricultura, silvicultura o pezca", "Agricultura"));
        l.add(new SelectItem("10", "Comercio", "Comercio"));
        l.add(new SelectItem("9", "Educación", "Educación"));
        l.add(new SelectItem("8", "Industria automotriz, aeronáutica", "Industria auto."));
        l.add(new SelectItem("7", "Industria de la construcción", "Industria const."));
        l.add(new SelectItem("6", "Industria de transformación, manufactura", "Industria trans."));
        l.add(new SelectItem("5", "Industria extractiva (minería y petrolera)", "Industria ext."));
        l.add(new SelectItem("4", "Salud", "Salud"));
        l.add(new SelectItem("3", "Servicios (bancarios, turismo, transporte, etc.)", "Servicios"));
        l.add(new SelectItem("2", "Servicios del Estado (oficina de gobierno, aduanas, limpia, seguridad, y vigilacia, agua, alcantarillado, alumbrado, etc.)", "Serv. Est"));
        l.add(new SelectItem("1", "Tecnologías de la Información y la Comunicación", "TICs"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer su puesto
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasPuesto(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("7", "Actividades no profesionales (oficios o actividades ajenas a su profesión)", "Act. no Prof."));
        l.add(new SelectItem("6", "Director, gerente (mando superior)", "Director"));
        l.add(new SelectItem("5", "Operario", "Operario"));
        l.add(new SelectItem("4", "Personal Administrativo", "Personal"));
        l.add(new SelectItem("3", "Propietario", "Propietario"));
        l.add(new SelectItem("2", "Supervisor (mando medio)", "Supervisor"));
        l.add(new SelectItem("1", "Técnico especializado", "Técnico"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer la coincidencia de su profesión con su empleo
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasCoincidencia(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "Totalmente", "Totalmente"));
        l.add(new SelectItem("4", "Mucho", "Mucho"));
        l.add(new SelectItem("3", "Medianamente", "Medianamente"));
        l.add(new SelectItem("2", "Poco", "Poco"));
        l.add(new SelectItem("1", "Nada", "Nada"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer su Ingreso mensual
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasIngreso(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("8", "Menos de 2,500", "Menos 2,500"));
        l.add(new SelectItem("7", "2,500 a 3,999", "2,500-3,999"));
        l.add(new SelectItem("6", "4,000 a 5,999", "4,000-5,999"));
        l.add(new SelectItem("5", "6,000 a 7,999", "6,000-7,999"));
        l.add(new SelectItem("4", "8,000 a 9,999", "8,000-9,999"));
        l.add(new SelectItem("3", "10,000 a 11,999", "10,000-11,999"));
        l.add(new SelectItem("2", "Mas de 12,000", "Mas 12,000"));
        l.add(new SelectItem("1", "No especificó", "NE"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer su Regimen Jurídico
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasRegimen(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("2", "Público", "Público"));
        l.add(new SelectItem("1", "Privado", "Privado"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer el tamaño de la empresa donde trabaja
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasTamanioEmpresa(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("4", "Microempresa (de 1 a 20 empleados)", "Micro"));
        l.add(new SelectItem("3", "Pequeña (de 21 a 50 empleados)", "Pequeña"));
        l.add(new SelectItem("2", "Mediana (Entre 51 y 100 empleados)", "Mediana"));
        l.add(new SelectItem("1", "Grande (mas de 100 empleados)", "Grande"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer el tiempo que tardó enencontrar trabajo
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasTiempo(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("7", "Ya estabas trabajando y continuaste haciéndolo", "Ya trabajaba"));
        l.add(new SelectItem("6", "Te contrataron donde hiciste la estadía", "Te contrataron"));
        l.add(new SelectItem("5", "Menos de 3 meses", "Menos de 3 meses"));
        l.add(new SelectItem("4", "De 3 a 6 meses", "De 3 a 6 meses"));
        l.add(new SelectItem("3", "Mas de 6 meses a un año", "Mas de 6 meses"));
        l.add(new SelectItem("2", "Mas de un año", "Mas de un año"));
        l.add(new SelectItem("1", "No has empezado a trabajar", "No trabaja"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer el medio por el cual obtuvo su empleo
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasMedio(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("9", "Relación con la empresa en la que realizaste la estadía.", "Relación empresa"));
        l.add(new SelectItem("8", "Bolsa de trabajo de la UT.", "Bolsa de trabajo"));
        l.add(new SelectItem("7", "Creaste tu propio negocio con apoyo de la incubadora de empresas de la UT.", "Negocio propio"));
        l.add(new SelectItem("6", "Amigos o familiares.", "Amigos"));
        l.add(new SelectItem("5", "Invitación de una empresa o institución.", "Invitación"));
        l.add(new SelectItem("4", "Relaciones hechas en empresas anteriores.", "Relaciones"));
        l.add(new SelectItem("3", "Creaste tu propio negocio con tus medios propios.", "Medios propios"));
        l.add(new SelectItem("2", "Te integraste a un negocio familiar.", "Negocio familiar"));
        l.add(new SelectItem("1", "Buscaste por tu propia cuenta en periódicos, internet y/o agencias de empleo.", "Búsqueda propia"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer la cantidad de personal a su cargo
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasPersonalCargo(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "Ninguna", "Ninguna"));
        l.add(new SelectItem("4", "Dos o tres", "Dos o tres"));
        l.add(new SelectItem("3", "Cuatro o cinco", "Cuatro o cinco"));
        l.add(new SelectItem("2", "Entre seis y diez", "Seis a diez"));
        l.add(new SelectItem("1", "Mas de diez", "Mas de diez"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer su situación económica
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasSituacionEcon(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("3", "Ha mejorado", "Ha mejorado"));
        l.add(new SelectItem("2", "Sin cambio", "Sin cambio"));
        l.add(new SelectItem("1", "Ha empeorado", "Ha empeorado"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer la consideracion de algunos factores y como 
     * influreron en la obtención de su empleo
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasFactores(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "En gran medida", "En gran medida"));
        l.add(new SelectItem("4", "Bastante", "Bastante"));
        l.add(new SelectItem("3", "En alguna medida", "En alguna medida"));
        l.add(new SelectItem("2", "Casi nada", "Casi nada"));
        l.add(new SelectItem("1", "Nada", "Nada"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer la valoración de aspectos importantes en su formación
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasAspectos(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "Totalmente", "Totalmente"));
        l.add(new SelectItem("4", "Mucho", "Mucho"));
        l.add(new SelectItem("3", "Más o menos", "Más o menos"));
        l.add(new SelectItem("2", "Poco", "Poco"));
        l.add(new SelectItem("1", "Nada", "Nada"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    /**
     * Permite crear una lista de selección de las posibles respuestas para el conocer si cubrió sus expectativas en la formación recibida
     * @return Regresa la lista de Respuestas
     */
    public ResultadoEJB<List<SelectItem>> getRespuestasSiNo(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("2", "Si", "Si"));
        l.add(new SelectItem("1", "No", "No"));
        
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    
    public ResultadoEJB<List<SelectItem>> getNivelSatisfaccion(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "Muy Bien", "Muy Bien"));
        l.add(new SelectItem("4", "Bien", "Bien"));
        l.add(new SelectItem("3", "Regular", "Regular"));
        l.add(new SelectItem("2", "Mal", "Mal"));
        l.add(new SelectItem("1", "Pésimo", "Pésimo"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    public ResultadoEJB<List<SelectItem>> getNivelRequerido(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "Nivel de Conversación", "Nivel de Conversación"));
        l.add(new SelectItem("4", "Hablado y Escrito", "Hablado y Escrito"));
        l.add(new SelectItem("3", "Certificación", "Certificación"));
        l.add(new SelectItem("2", "Deseable", "Deseable"));
        l.add(new SelectItem("1", "No es necesario", "No es necesario"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    public ResultadoEJB<List<SelectItem>> getOportunidades(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("11", "Comunicación", "Comunicación"));
        l.add(new SelectItem("10", "Conocimientos Técnicos", "Conocimientos Técnicos"));
        l.add(new SelectItem("9", "Conocimientos en Microsoft Office", "Conocimientos en Microsoft Office"));
        l.add(new SelectItem("8", "Confianza y seguridad", "Confianza y seguridad"));
        l.add(new SelectItem("7", "Fortalecer el Liderazgo", "Fortalecer el Liderazgo"));
        l.add(new SelectItem("6", "Idioma Inglés", "Idioma Inglés"));
        l.add(new SelectItem("5", "Iniciativa y Toma de Desiciones", "Iniciativa y Toma de Desiciones"));
        l.add(new SelectItem("4", "Innovación", "Innovación"));
        l.add(new SelectItem("3", "Puntualidad", "Puntualidad"));
        l.add(new SelectItem("2", "Toma de Desiciones", "Toma de Desiciones"));
        l.add(new SelectItem("1", "Valores (Respeto, Responsabilidad, Voluntad, Honestidad, Solidaridad)", "Valores (Respeto, Responsabilidad, Voluntad, Honestidad, Solidaridad)"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    public ResultadoEJB<List<SelectItem>> getMejoras(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("7", "Agregar al programa académico herramientas de trabajo colaborativo", "Agregar al programa académico herramientas de trabajo colaborativo"));
        l.add(new SelectItem("6", "Calidad Educativa", "Calidad Educativa"));
        l.add(new SelectItem("5", "Mayor numero de practicas conforme a las materias cursadas", "Mayor numero de practicas conforme a las materias cursadas"));
        l.add(new SelectItem("4", "La logística para la asignación de estadías", "La logística para la asignación de estadías"));
        l.add(new SelectItem("3", "Mejor comunicación con nuestros estudiantes", "Mejor comunicación con nuestros estudiantes"));
        l.add(new SelectItem("2", "Mejorar el método de enseñanza con base en la materia de Inglés", "Mejorar el método de enseñanza con base en la materia de Inglés"));
        l.add(new SelectItem("1", "Mejorar la atención a los alumnos de manera individual", "Mejorar la atención a los alumnos de manera individual"));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    public ResultadoEJB<List<SelectItem>> getOpciones(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("3", "Si, los egresados están calificados.", "Si, los egresados están calificados."));
        l.add(new SelectItem("2", "No, existen egresados de otras universidades mejores preparados", "No, existen egresados de otras universidades mejores preparados"));
        l.add(new SelectItem("1", "Tal vez.", "Tal vez."));
        return ResultadoEJB.crearCorrecto(l, "Lista creada");
    }
    
    public ResultadoEJB<String> obtenerEstatus(String nombre){
        VariablesProntuario estatus = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", nombre)
                .getResultStream().findFirst().orElse(new VariablesProntuario());
        return ResultadoEJB.crearCorrecto(estatus.getValor(), "Estatus obtenido");
    }
}
