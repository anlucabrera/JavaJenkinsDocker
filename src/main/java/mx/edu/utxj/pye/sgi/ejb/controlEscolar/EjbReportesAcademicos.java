/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAprovechamientoEscolar;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAprovechamientoEscolarEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDistribucionMatricula;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteIrregular;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReportePlaneacionDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionPromedio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionDetalle;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionEvidenciaInstrumento;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbReportesAcademicos")
public class EjbReportesAcademicos {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbCarga ejbCarga;
    public static final String ACTUALIZADO_ACADEMICO = "seguimientoEstadia.xlsx";
    public static final String ACTUALIZADO_ACADEMICO_DIRECCION = "seguimientoEstadiaAreaAcademica.xlsx";
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
      /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRolesReportesAcademicos(Integer clave){
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
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal con acceso a reportes académicos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReportesAcademicos.validarRolesReportesAcademicos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de periodos con escolar en la que existen eventos escolares registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEscolaresRegistro(){
        try{
            List<PeriodosEscolares> listaPeriodosEscolares = new ArrayList<>();
            
            List<Integer> listaPeriodosEventos = em.createQuery("SELECT e FROM EventoEscolar e ORDER BY e.periodo DESC",  EventoEscolar.class)
                    .getResultStream()
                    .map(p->p.getPeriodo())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaPeriodosEventos.forEach(periodo -> {
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, periodo);
                listaPeriodosEscolares.add(periodoEscolar);
            });
            
            
            return ResultadoEJB.crearCorrecto(listaPeriodosEscolares, "Lista de periodos escolares en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que existen eventos escolares registrados. (EjbReportesAcademicos.getPeriodosEscolaresRegistro)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de niveles educativo en los que existen eventos escolares registrados del periodo seleccionado
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesPeriodosEscolares(PeriodosEscolares periodo){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
            List<Short> listaProgramasGrupo = em.createQuery("SELECT g FROM Grupo g WHERE g.periodo=:periodo",  Grupo.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(p->p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaProgramasGrupo.forEach(programa -> {
                AreasUniversidad programaPK = em.find(AreasUniversidad.class, programa);
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, programaPK.getNivelEducativo().getNivel());
                listaNiveles.add(nivel);
            });
            
            return ResultadoEJB.crearCorrecto(listaNiveles.stream().distinct().sorted(Comparator.comparing(ProgramasEducativosNiveles::getNombre)).collect(Collectors.toList()), "Lista de niveles educativos del periodo escolar seleccionado en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos del periodo escolar seleccionado en los que existen eventos escolares registrados. (EjbReportesAcademicos.getNivelesPeriodosEscolares)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de programas educativos en que existen eventos escolares registrados del periodo y nivel educativo seleccionados
     * @param periodo
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosNivel(PeriodosEscolares periodo, ProgramasEducativosNiveles nivel){
        try{
            List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
            
            List<Short> listaProgramasGrupo = em.createQuery("SELECT g FROM Grupo g WHERE g.periodo=:periodo",  Grupo.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(p->p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaProgramasGrupo.forEach(programa -> {
                AreasUniversidad programaPK = em.find(AreasUniversidad.class, programa);
                listaProgramasEducativos.add(programaPK);
            });
            
            return ResultadoEJB.crearCorrecto(listaProgramasEducativos.stream().filter(p->p.getNivelEducativo().equals(nivel)).collect(Collectors.toList()), "Lista de programas educativos del periodo y nivel seleccionado en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos del periodo y nivel seleccionado en los que existen eventos escolares registrados. (EjbReportesAcademicos.getProgramasEducativosNivel)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDatosEstudiante>> getMatricula(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoDatosEstudiante> listaEstudiantes = new ArrayList<>();
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(2); tiposEstudiante.add(3); tiposEstudiante.add(4);
            
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.periodo=:periodo AND e.carrera=:programa AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            estudiantes.forEach(estudiante -> {
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudiante, programaEducativo, periodoEscolar);
                listaEstudiantes.add(dtoDatosEstudiante);
            });
            
            return ResultadoEJB.crearCorrecto(listaEstudiantes.stream().sorted(DtoDatosEstudiante::compareTo).collect(Collectors.toList()), "Lista de estudiantes del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getMatricula)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDistribucionMatricula>> getDistribucionMatricula(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoDistribucionMatricula> listaDistribucionMatricula = new ArrayList<>();
            
            List<DtoDatosEstudiante> estudiantesPE = getMatricula(periodo, programa).getValor();
            
            List<DtoTramitarBajas> bajas = ejbRegistroBajas.obtenerListaBajasPeriodo(periodo).getValor();
            
            List<DtoTramitarBajas> bajasPE = ejbRegistroBajas.obtenerListaBajasProgramaEducativo(bajas, programa).getValor();
            
            List<Integer> listaGrados = em.createQuery("SELECT g FROM Grupo g WHERE g.periodo=:periodo AND g.idPe=:programa", Grupo.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .getResultStream()
                    .map(p->p.getGrado())
                    .distinct()
                    .collect(Collectors.toList()); 
            
            
            listaGrados.forEach(grado -> {
                
                List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
                Integer matriculaInicial = (int) em.createQuery("SELECT e FROM Estudiante e WHERE e.periodo=:periodo AND e.carrera=:programa AND e.grupo.grado=:grado AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grado", grado)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .count();
                
                Integer desercion = (int) bajasPE.stream().filter(p->p.getDtoEstudiante().getEstudiante().getGrupo().getGrado()==grado).count();
                Integer matriculaFinal = (int) estudiantesPE.stream().filter(p->p.getEstudiante().getGrupo().getGrado()==grado).count();
                
                Double porcentajeDesercion = (double)desercion/matriculaInicial*100;
                
                DtoDistribucionMatricula dtoDistribucionMatricula = new DtoDistribucionMatricula(programa, grado, matriculaInicial, desercion, matriculaFinal,porcentajeDesercion);
                listaDistribucionMatricula.add(dtoDistribucionMatricula);
            });
            
            return ResultadoEJB.crearCorrecto(listaDistribucionMatricula.stream().sorted(DtoDistribucionMatricula::compareTo).collect(Collectors.toList()), "Distribución de matricula del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la distrbución de matricula del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getDistribucionMatricula)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de planeacion docente del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReportePlaneacionDocente>> getPlaneacionesDocente(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoReportePlaneacionDocente> listaPlaneaciones = new ArrayList<>();
            
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            
            List<CargaAcademica> cargasAcademicas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.cveGrupo.periodo=:periodo AND c.cveGrupo.idPe=:programa AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            cargasAcademicas.forEach(carga -> {
                PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, carga.getIdPlanMateria().getIdPlanMateria());
                Personal docente = em.find(Personal.class, carga.getDocente());
                
                List<UnidadMateria> unidadesMaterias = em.createQuery("SELECT u FROM UnidadMateria u WHERE u.idMateria.idMateria=:materia", UnidadMateria.class)
                    .setParameter("materia", carga.getIdPlanMateria().getIdMateria().getIdMateria())
                    .getResultStream()
                    .collect(Collectors.toList()); 
                
                List<UnidadMateriaConfiguracion> unidadesConfiguradas = em.createQuery("SELECT u FROM UnidadMateriaConfiguracion u WHERE u.carga.carga=:carga", UnidadMateriaConfiguracion.class)
                    .setParameter("carga", carga.getCarga())
                    .getResultStream()
                    .collect(Collectors.toList());
                
                Integer unidadesValidadas = (int) unidadesConfiguradas.stream().filter(p->p.getDirector()!=null).count();
                
                Integer evidenciasSer = 0, evidenciasSaber = 0, evidenciasSaberHacer = 0;
                Boolean asignacionCompleta = true;
                       
                if(periodo.getPeriodo()<57){
                    List<UnidadMateriaConfiguracionDetalle> indicadoresRegistrados = em.createQuery("SELECT u FROM UnidadMateriaConfiguracionDetalle u WHERE u.configuracion.carga.carga=:carga", UnidadMateriaConfiguracionDetalle.class)
                            .setParameter("carga", carga.getCarga())
                            .getResultStream()
                            .collect(Collectors.toList());
            
                    evidenciasSer = (int)indicadoresRegistrados.stream().filter(p->p.getCriterio().getTipo().equals("Ser")).count();
                    evidenciasSaber = (int)indicadoresRegistrados.stream().filter(p->p.getCriterio().getTipo().equals("Saber")).count();
                    evidenciasSaberHacer = (int)indicadoresRegistrados.stream().filter(p->p.getCriterio().getTipo().equals("Saber hacer")).count();
                    
                }
                else{
                    List<UnidadMateriaConfiguracionEvidenciaInstrumento> evidenciasRegistradas = em.createQuery("SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.configuracion.carga.carga=:carga", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                            .setParameter("carga", carga.getCarga())
                            .getResultStream()
                            .collect(Collectors.toList());
                    
                    evidenciasSer = (int)evidenciasRegistradas.stream().filter(p->p.getEvidencia().getCriterio().getTipo().equals("Ser")).count();
                    evidenciasSaber = (int)evidenciasRegistradas.stream().filter(p->p.getEvidencia().getCriterio().getTipo().equals("Saber")).count();
                    evidenciasSaberHacer = (int)evidenciasRegistradas.stream().filter(p->p.getEvidencia().getCriterio().getTipo().equals("Saber hacer")).count();
                }
                
                
                Double porcentajeAsignacion = 0.0;
                List<Integer> listaEvidencias = new ArrayList<>(); listaEvidencias.add(evidenciasSer); listaEvidencias.add(evidenciasSaber); listaEvidencias.add(evidenciasSaberHacer);
                Long cantidadCeros = listaEvidencias.stream().filter(p->p==0).count();
                
                if(cantidadCeros==0){
                    porcentajeAsignacion= 100.0;
                    asignacionCompleta = false;
                }else if(cantidadCeros==1){
                    porcentajeAsignacion= 33.3;
                }else if(cantidadCeros==2){
                    porcentajeAsignacion= 66.6;
                }
                
                Double porcentajeConfiguracion = (double)(unidadesConfiguradas.size()/unidadesMaterias.size())*100;
                Double porcentajeValidacion = (double)(unidadesValidadas/unidadesConfiguradas.size())*100;
                Double porcentajePlaneacion = (double)((porcentajeConfiguracion + porcentajeAsignacion + porcentajeValidacion)/3);
                
                DtoReportePlaneacionDocente dtoReportePlaneacionDocente = new DtoReportePlaneacionDocente(carga, planEstudioMateria, programa, docente, unidadesMaterias.size(), unidadesConfiguradas.size(), unidadesValidadas, evidenciasSer, evidenciasSaber, evidenciasSaberHacer, asignacionCompleta, String.format("%.2f",porcentajeConfiguracion), String.format("%.2f",porcentajeAsignacion), String.format("%.2f",porcentajeValidacion), String.format("%.2f",porcentajePlaneacion));
                listaPlaneaciones.add(dtoReportePlaneacionDocente);
            });
            
            return ResultadoEJB.crearCorrecto(listaPlaneaciones.stream().sorted(DtoReportePlaneacionDocente::compareTo).collect(Collectors.toList()), "Lista de planeaciones docente del periodo y programa seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de planeaciones docente del periodo y programa seleccionado. (EjbReportesAcademicos.getPlaneacionDocente)", e, null);
        }
    }
    
     /**
     * Permite obtener aprovechamiento escolar del periodo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAprovechamientoEscolar>> getAprovechamientoEscolar(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
           List<DtoAprovechamientoEscolar> listaAprovechamiento = new ArrayList<>();
           
           List<DtoDatosEstudiante> estudiantes = getMatricula(periodo, programa).getValor();
            
           List<PlanEstudioMateria> planEstudiosMaterias = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.evento.periodo=:periodo AND c.cveGrupo.idPe=:programa", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .getResultStream()
                    .map(p->p.getIdPlanMateria())
                    .distinct()
                    .collect(Collectors.toList());
           
            planEstudiosMaterias.forEach(planEstudioMat -> {
                PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, planEstudioMat.getIdPlanMateria());
                
                List<DtoDatosEstudiante> estudiantesGrado = estudiantes.stream().filter(p->p.getEstudiante().getGrupo().getGrado()==planEstudioMateria.getGrado()).collect(Collectors.toList());
                
                Double promedio = getObtenerPromedioAsignatura(planEstudioMateria, estudiantesGrado).getValor();
                
                DtoAprovechamientoEscolar dtoAprovechamientoEscolar = new DtoAprovechamientoEscolar(programa, planEstudioMateria, promedio);
                listaAprovechamiento.add(dtoAprovechamientoEscolar);
            });
            
            return ResultadoEJB.crearCorrecto(listaAprovechamiento.stream().sorted(DtoAprovechamientoEscolar::compareTo).collect(Collectors.toList()), "Aprovechamiento escolar del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener aprovechamiento escolar del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getAprovechamientoEscolar)", e, null);
        }
    }
    
     /**
     * Permite obtener el promedio del cuatrimestre del estudiante seleccionado
     * @param planEstudioMateria
     * @param estudiantes
     * @return Resultado del proceso
     */
     public ResultadoEJB<Double> getObtenerPromedioAsignatura(PlanEstudioMateria planEstudioMateria, List<DtoDatosEstudiante> estudiantes){
        try{
            System.out.println("getObtenerPromedioAsignatura1 - estudiantes " + estudiantes.size());
            
            Double promedio=0.0;
            
            List<Double> listaCalificaciones = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> { 
                System.out.println("getObtenerPromedioAsignatura2 - planEstMat " + planEstudioMateria.getClaveMateria());
                System.out.println("getObtenerPromedioAsignatura2 - estudiante " + estudiante.getEstudiante().getIdEstudiante());
                Double calificacion = 0.0;
                
                CalificacionPromedio calificacionPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionPromedio.class)
                    .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                    .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null); 
                
                System.out.println("getObtenerPromedioAsignatura3 - calificacionPromedio " + calificacionPromedio);
                
                if(calificacionPromedio!=null){
                    System.out.println("getObtenerPromedioAsignatura4 - calificacionPromedio " + calificacionPromedio.getValor());
                    if(calificacionPromedio.getValor()<8.0){
                        System.out.println("getObtenerPromedioAsignatura5 - menor a 8 ");
                        CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                            .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                            .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                            .getResultStream()
                            .findFirst()
                            .orElse(null); 
                        System.out.println("getObtenerPromedioAsignatura6 - calificacionNivelacion " + calificacionNivelacion);
                        if(calificacionNivelacion!=null){ 
                            System.out.println("getObtenerPromedioAsignatura7 - calificacionNivelacion " + calificacionNivelacion.getValor());
                            calificacion = calificacionNivelacion.getValor(); 
                        }
                    }else{
                        calificacion = calificacionPromedio.getValor();
                    }
                }
               System.out.println("getObtenerPromedioAsignatura8 - calificacion " + calificacion);
               listaCalificaciones.add(calificacion);
            });
            System.out.println("getObtenerPromedioAsignatura9 - listaCalificaciones " + listaCalificaciones.size());
            if(estudiantes.size() == listaCalificaciones.size())
            {
               promedio = listaCalificaciones.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            }
            
            return ResultadoEJB.crearCorrecto(promedio, "Promedio de la asignatura en el periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el promedio de la asignatura en el periodo seleccionado. (EjbReportesAcademicos.getObtenerPromedioAsignatura)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aprovechamiento escolar del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAprovechamientoEscolarEstudiante>> getListaAprovechamientoEscolar(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoAprovechamientoEscolarEstudiante> listaAprovechamiento = new ArrayList<>();
            
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(2); tiposEstudiante.add(3); tiposEstudiante.add(4);
            
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);;
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.periodo=:periodo AND e.carrera=:programa AND e.grupo.grado NOT IN :grados AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grados", grados)
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            estudiantes.forEach(estudiante -> {
                String discapacidad = getTipoDiscapacidad(estudiante).getValor();
                String lenguaIndigena = getLenguaIndigena(estudiante).getValor();
                
                String promedio = String.format("%.3f",getObtenerPromedioEstudiante(estudiante).getValor());
                
                DtoAprovechamientoEscolarEstudiante dtoAprovechamientoEscolar = new DtoAprovechamientoEscolarEstudiante(estudiante,programa,discapacidad,lenguaIndigena, promedio);
                listaAprovechamiento.add(dtoAprovechamientoEscolar);
            });
            
            return ResultadoEJB.crearCorrecto(listaAprovechamiento.stream().sorted(DtoAprovechamientoEscolarEstudiante::compareTo).collect(Collectors.toList()), "Lista de aprovechamiento escolar del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de aprovechamiento escolar del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getListaAprovechamientoEscolar)", e, null);
        }
    }
    
     /**
     * Permite obtener el promedio del cuatrimestre del estudiante seleccionado
     * @param estudiante
     * @return Resultado del proceso
     */
     public ResultadoEJB<Double> getObtenerPromedioEstudiante(Estudiante estudiante){
        try{
            
           Double promedio=0.0;
            
            List<Double> listaCalificaciones = new ArrayList<>();
            
            List<CargaAcademica> cargasAcademicas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.cveGrupo.idGrupo=:grupo ", CargaAcademica.class)
                    .setParameter("grupo", estudiante.getGrupo().getIdGrupo())
                    .getResultStream()
                    .collect(Collectors.toList()); 
            
            cargasAcademicas.forEach(carga -> { 
                Double calificacion = 0.0;
                
                CalificacionPromedio calificacionPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.cargaAcademica.carga=:carga AND c.estudiante.idEstudiante=:estudiante", CalificacionPromedio.class)
                    .setParameter("carga", carga.getCarga())
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null); 
                
                if(calificacionPromedio!=null){
                    if(calificacionPromedio.getValor()<8.0){
                        CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.carga=:carga AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                            .setParameter("carga", carga.getCarga())
                            .setParameter("estudiante", estudiante.getIdEstudiante())
                            .getResultStream()
                            .findFirst()
                            .orElse(null); 
                        if(calificacionNivelacion!=null){ calificacion = calificacionNivelacion.getValor(); }
                    }else{
                        calificacion = calificacionPromedio.getValor();
                    }
                }
               listaCalificaciones.add(calificacion);
            });
            
            if(cargasAcademicas.size() == listaCalificaciones.size())
            {
               promedio = listaCalificaciones.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            }
            
            return ResultadoEJB.crearCorrecto(promedio, "Promedio del estudiante en el periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el promedio del estudiante en el periodo seleccionado. (EjbReportesAcademicos.getObtenerPromedioEstudiante)", e, null);
        }
    }
    
     /**
     * Permite obtener tipo de discapacidad registrada en datos médicos por el estudiante
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> getTipoDiscapacidad(Estudiante estudiante){
        try{
            String discapacidad = "No aplica";
            
            DatosMedicos datosMedicos = em.createQuery("SELECT d FROM DatosMedicos d WHERE d.persona.idpersona=:persona", DatosMedicos.class)
                    .setParameter("persona",estudiante.getAspirante().getIdPersona().getIdpersona())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if(datosMedicos!=null){
                discapacidad = datosMedicos.getCveDiscapacidad().getNombre();
            }
            
            return ResultadoEJB.crearCorrecto(discapacidad, "Tipo de discapacidad registrada del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener tipo de discapacidad registrada por el estudiante. (EjbReportesAcademicos.getTipoDiscapacidad)", e, null);
        }
    }
    
     /**
     * Permite obtener tipo de lengua indígena registrada por el estudiante
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> getLenguaIndigena(Estudiante estudiante){
        try{
            String lenguaIndigena = "No aplica";
            
            EncuestaAspirante encuestaAspirante = em.createQuery("SELECT e FROM EncuestaAspirante e WHERE e.aspirante.idAspirante=:aspirante", EncuestaAspirante.class)
                    .setParameter("aspirante", estudiante.getAspirante().getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if(encuestaAspirante!=null){
                if (encuestaAspirante.getR1Lenguaindigena().equals("Sí") && encuestaAspirante.getR2tipoLenguaIndigena() != null) {
                    lenguaIndigena = encuestaAspirante.getR2tipoLenguaIndigena().getNombre();
                } else if (encuestaAspirante.getR1Lenguaindigena().equals("Sí") && encuestaAspirante.getR2tipoLenguaIndigena() == null) {
                    lenguaIndigena = "No indicó lengua que habla";
                }
            }
            
            return ResultadoEJB.crearCorrecto(lenguaIndigena, "Lengua indígena registrada por el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lengua indígena registrada por el estudiante. (EjbReportesAcademicos.getLenguaIndigena)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aprovechamiento escolar del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEstudianteIrregular>> getEstudiantesIrregulares(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            
            List<DtoEstudianteIrregular> listaEstudiantesIrregulares = new ArrayList<>();
             
            List<CalificacionPromedio> calificacionesPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.estudiante.periodo=:periodo AND c.estudiante.carrera=:programa AND c.valor<:valor", CalificacionPromedio.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("valor", (double)8)
                    .getResultStream()
                    .collect(Collectors.toList());

            calificacionesPromedio.forEach(promedio -> {
                if(promedio.getCargaAcademica().getCveGrupo().getIdGrupo().equals(promedio.getEstudiante().getGrupo().getIdGrupo())){
                Double calificacion = 0.0;
                
                CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.carga=:carga AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                        .setParameter("carga", promedio.getCargaAcademica().getCarga())
                        .setParameter("estudiante", promedio.getEstudiante().getIdEstudiante())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                
                if (calificacionNivelacion != null && calificacionNivelacion.getValor()<8.0) {
                    if(calificacionNivelacion.getValor()>=calificacion){
                        calificacion = calificacionNivelacion.getValor();
                    }else{
                        calificacion = promedio.getValor();
                    }
                    
                    if (promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 1 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==5) {

                        PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, promedio.getCargaAcademica().getIdPlanMateria().getIdPlanMateria());
                        Personal docente = em.find(Personal.class, promedio.getCargaAcademica().getDocente());
                        Personal tutor = em.find(Personal.class, promedio.getEstudiante().getGrupo().getTutor());
                        DtoEstudianteIrregular dtoEstudianteIrregular = new DtoEstudianteIrregular(promedio.getEstudiante(), programa, planEstudioMateria, docente, String.format("%.3f", calificacion), tutor);
                        listaEstudiantesIrregulares.add(dtoEstudianteIrregular);
                    }
                }else if (calificacionNivelacion == null){
                    calificacion = promedio.getValor();
                    if (promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 1 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==5) {

                        PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, promedio.getCargaAcademica().getIdPlanMateria().getIdPlanMateria());
                        Personal docente = em.find(Personal.class, promedio.getCargaAcademica().getDocente());
                        Personal tutor = em.find(Personal.class, promedio.getEstudiante().getGrupo().getTutor());
                        DtoEstudianteIrregular dtoEstudianteIrregular = new DtoEstudianteIrregular(promedio.getEstudiante(), programa, planEstudioMateria, docente, String.format("%.3f", calificacion), tutor);
                        listaEstudiantesIrregulares.add(dtoEstudianteIrregular);
                    }
                }
                }
            });
            
            return ResultadoEJB.crearCorrecto(listaEstudiantesIrregulares.stream().sorted(DtoEstudianteIrregular::compareTo).collect(Collectors.toList()), "Lista de estudiantes irregulares del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes irregulares del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getEstudiantesIrregulares)", e, null);
        }
    }
    
}
