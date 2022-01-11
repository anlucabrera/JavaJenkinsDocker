/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAprovechamientoEscolar;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAprovechamientoEscolarEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDistribucionMatricula;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteIrregular;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMatricula;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPromedioMateriaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReportePlaneacionDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReprobacionAsignatura;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
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
import net.sf.jxls.transformer.XLSTransformer;

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
    public static final String ACTUALIZADO_PROMEDIOS = "promediosEstudiante.xlsx";
    
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
            else if (p.getPersonal().getAreaOperativa() == 9 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getAreaOperativa() == 6 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getAreaOperativa() == 23 && p.getPersonal().getStatus()!='B') {
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
     * Permite obtener la lista de programas educativos en que existen eventos escolares registrados del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativos(PeriodosEscolares periodo, Personal personal){
        try{
            List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
            
            List<AreasUniversidad> listaProgramasEducativosFinal = new ArrayList<>();
            
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
            
            if(personal.getAreaOperativa()!=10 && personal.getAreaOperativa()!=6 && personal.getAreaOperativa()!=9 && personal.getAreaOperativa()!=23){
                listaProgramasEducativosFinal = listaProgramasEducativos.stream().filter(p->p.getAreaSuperior().equals(personal.getAreaOperativa())).collect(Collectors.toList());
            }else{
                listaProgramasEducativosFinal = listaProgramasEducativos;
            }
            
            return ResultadoEJB.crearCorrecto(listaProgramasEducativosFinal, "Lista de programas educativos del periodo seleccionado en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos del periodo seleccionado en los que existen eventos escolares registrados. (EjbReportesAcademicos.getProgramasEducativos)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoMatricula>> getMatriculaPrograma(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoMatricula> listaEstudiantes = new ArrayList<>();
            
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
                Generos genero = em.find(Generos.class, estudiante.getAspirante().getIdPersona().getGenero());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                DtoMatricula dtoMatricula = new DtoMatricula(estudiante, genero, programa, periodo);
                listaEstudiantes.add(dtoMatricula);
            });
            
            return ResultadoEJB.crearCorrecto(listaEstudiantes.stream().sorted(DtoMatricula::compareTo).collect(Collectors.toList()), "Lista de estudiantes del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getMatriculaPrograma)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoMatricula>> getMatricula(PeriodosEscolares periodo, Personal personal){
        try{
            List<Short> programasEducativos = getProgramasEducativos(periodo, personal).getValor().stream().map(p->p.getArea()).collect(Collectors.toList());
            
            List<DtoMatricula> listaEstudiantes = new ArrayList<>();
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(2); tiposEstudiante.add(3); tiposEstudiante.add(4);
            
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.periodo=:periodo AND e.carrera IN :programas AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            estudiantes.forEach(estudiante -> {
                Generos genero = em.find(Generos.class, estudiante.getAspirante().getIdPersona().getGenero());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                DtoMatricula dtoMatricula = new DtoMatricula(estudiante, genero, programaEducativo, periodo);
                listaEstudiantes.add(dtoMatricula);
            });
            
            return ResultadoEJB.crearCorrecto(listaEstudiantes.stream().sorted(DtoMatricula::compareTo).collect(Collectors.toList()), "Lista de estudiantes del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del periodo seleccionado. (EjbReportesAcademicos.getMatricula)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDistribucionMatricula>> getDistribucionMatriculaPrograma(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoDistribucionMatricula> listaDistribucionMatricula = new ArrayList<>();
            
            List<DtoMatricula> estudiantesPE = getMatriculaPrograma(periodo, programa).getValor();
            
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
                Integer matriculaInicial = getMatriculaInicial(periodo, programa, grado).getValor().size();
                
                Integer desercion = (int) bajasPE.stream().filter(p->p.getDtoEstudiante().getEstudiante().getGrupo().getGrado()==grado && p.getDtoEstudiante().getEstudiante().getCarrera()==programa.getArea()).count();
                Integer matriculaFinal = (int) estudiantesPE.stream().filter(p->p.getEstudiante().getGrupo().getGrado()==grado).count();
                
                Double porcentajeDesercion = (double)desercion/matriculaInicial*100;
                
                DtoDistribucionMatricula dtoDistribucionMatricula = new DtoDistribucionMatricula(programa, periodo, grado, matriculaInicial, desercion, matriculaFinal,porcentajeDesercion);
                listaDistribucionMatricula.add(dtoDistribucionMatricula);
            });
            
            return ResultadoEJB.crearCorrecto(listaDistribucionMatricula.stream().sorted(DtoDistribucionMatricula::compareTo).collect(Collectors.toList()), "Distribución de matricula del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la distrbución de matricula del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getDistribucionMatriculaPrograma)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de estudiantes del periodo y programa educativo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDistribucionMatricula>> getDistribucionMatricula(PeriodosEscolares periodo, Personal personal){
        try{
            List<DtoDistribucionMatricula> listaDistribucionMatricula = new ArrayList<>();
            
            List<AreasUniversidad> programasEducativos = getProgramasEducativos(periodo, personal).getValor();
           
            programasEducativos.forEach(programa -> {
                List<DtoMatricula> estudiantesPE = getMatriculaPrograma(periodo, programa).getValor();
            
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
                    Integer matriculaInicial = getMatriculaInicial(periodo, programa, grado).getValor().size();
                
                    Integer desercion = (int) bajasPE.stream().filter(p->p.getDtoEstudiante().getEstudiante().getGrupo().getGrado()==grado && p.getDtoEstudiante().getEstudiante().getCarrera()==programa.getArea()).count();
                    Integer matriculaFinal = (int) estudiantesPE.stream().filter(p->p.getEstudiante().getGrupo().getGrado()==grado).count();
                
                    Double porcentajeDesercion = (double)desercion/matriculaInicial*100;
                
                    DtoDistribucionMatricula dtoDistribucionMatricula = new DtoDistribucionMatricula(programa, periodo, grado, matriculaInicial, desercion, matriculaFinal,porcentajeDesercion);
                    listaDistribucionMatricula.add(dtoDistribucionMatricula);
                });
            });
            
            return ResultadoEJB.crearCorrecto(listaDistribucionMatricula.stream().sorted(DtoDistribucionMatricula::compareTo).collect(Collectors.toList()), "Distribución de matricula del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la distrbución de matricula del periodo seleccionado. (EjbReportesAcademicos.getDistribucionMatricula)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de planeacion docente del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReportePlaneacionDocente>> getPlaneacionesDocentePrograma(PeriodosEscolares periodo, AreasUniversidad programa){
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
                }else if(cantidadCeros==0){
                    porcentajeAsignacion= 0.0;
                }
                
                Double porcentajeConfiguracion=0.0, porcentajeValidacion = 0.0, porcentajePlaneacion = 0.0;
                
                if(!unidadesConfiguradas.isEmpty()){
                    porcentajeConfiguracion = (double)(unidadesConfiguradas.size()/unidadesMaterias.size())*100;
                    porcentajeValidacion = (double)(unidadesValidadas/unidadesConfiguradas.size())*100;
                }
                
                
                if(porcentajeAsignacion>0){
                    porcentajePlaneacion = (double)((porcentajeConfiguracion + porcentajeAsignacion + porcentajeValidacion)/3);
                }
                
                DtoReportePlaneacionDocente dtoReportePlaneacionDocente = new DtoReportePlaneacionDocente(carga, periodo, planEstudioMateria, programa, docente, unidadesMaterias.size(), unidadesConfiguradas.size(), unidadesValidadas, evidenciasSer, evidenciasSaber, evidenciasSaberHacer, asignacionCompleta, String.format("%.2f",porcentajeConfiguracion), String.format("%.2f",porcentajeAsignacion), String.format("%.2f",porcentajeValidacion), String.format("%.2f",porcentajePlaneacion));
                listaPlaneaciones.add(dtoReportePlaneacionDocente);
            });
            
            return ResultadoEJB.crearCorrecto(listaPlaneaciones.stream().sorted(DtoReportePlaneacionDocente::compareTo).collect(Collectors.toList()), "Lista de planeaciones docente del periodo y programa seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de planeaciones docente del periodo y programa seleccionado. (EjbReportesAcademicos.getPlaneacionDocentePrograma)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de planeacion docente del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReportePlaneacionDocente>> getPlaneacionesDocente(PeriodosEscolares periodo, Personal personal){
        try{
            List<DtoReportePlaneacionDocente> listaPlaneaciones = new ArrayList<>();
            
            List<Short> programasEducativos = getProgramasEducativos(periodo, personal).getValor().stream().map(p->p.getArea()).collect(Collectors.toList());
            
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            
            List<CargaAcademica> cargasAcademicas = new ArrayList<>();
            
            if(personal.getAreaOperativa()==23){
                cargasAcademicas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.cveGrupo.periodo=:periodo AND c.cveGrupo.idPe IN :programas AND c.cveGrupo.grado NOT IN :grados AND (c.idPlanMateria.idMateria.nombre like concat('%',:nombre1,'%') OR c.idPlanMateria.idMateria.nombre like concat('%',:nombre2,'%'))", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("grados", grados)
                    .setParameter("nombre1", "Inglés")
                    .setParameter("nombre2", "Francés")
                    .getResultStream()
                    .collect(Collectors.toList());
            
            }else{
                cargasAcademicas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.cveGrupo.periodo=:periodo AND c.cveGrupo.idPe IN :programas AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("grados", grados)
                    .getResultStream()
                    .collect(Collectors.toList());
            }
            
            cargasAcademicas.forEach(carga -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, carga.getCveGrupo().getIdPe());
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
                }else if(cantidadCeros==0){
                    porcentajeAsignacion= 0.0;
                }
                
                Double porcentajeConfiguracion=0.0, porcentajeValidacion = 0.0, porcentajePlaneacion = 0.0;
                
                if(!unidadesConfiguradas.isEmpty()){
                    porcentajeConfiguracion = (double)(unidadesConfiguradas.size()/unidadesMaterias.size())*100;
                    porcentajeValidacion = (double)(unidadesValidadas/unidadesConfiguradas.size())*100;
                }
                
                
                if(porcentajeAsignacion>0){
                    porcentajePlaneacion = (double)((porcentajeConfiguracion + porcentajeAsignacion + porcentajeValidacion)/3);
                }
                
                DtoReportePlaneacionDocente dtoReportePlaneacionDocente = new DtoReportePlaneacionDocente(carga, periodo, planEstudioMateria, programa, docente, unidadesMaterias.size(), unidadesConfiguradas.size(), unidadesValidadas, evidenciasSer, evidenciasSaber, evidenciasSaberHacer, asignacionCompleta, String.format("%.2f",porcentajeConfiguracion), String.format("%.2f",porcentajeAsignacion), String.format("%.2f",porcentajeValidacion), String.format("%.2f",porcentajePlaneacion));
                listaPlaneaciones.add(dtoReportePlaneacionDocente);
            });
            
            return ResultadoEJB.crearCorrecto(listaPlaneaciones.stream().sorted(DtoReportePlaneacionDocente::compareTo).collect(Collectors.toList()), "Lista de planeaciones docente del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de planeaciones docente del periodo. (EjbReportesAcademicos.getPlaneacionDocente)", e, null);
        }
    }
    
     /**
     * Permite obtener aprovechamiento escolar del periodo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAprovechamientoEscolar>> getAprovechamientoEscolarPrograma(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
           List<DtoAprovechamientoEscolar> listaAprovechamiento = new ArrayList<>();
           
           List<DtoMatricula> estudiantes = getMatriculaPrograma(periodo, programa).getValor();
           
           List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
            
           List<PlanEstudioMateria> planEstudiosMaterias = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.evento.periodo=:periodo AND c.cveGrupo.idPe=:programa AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .map(p->p.getIdPlanMateria())
                    .distinct()
                    .collect(Collectors.toList());
           
            planEstudiosMaterias.forEach(planEstudioMat -> {
                PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, planEstudioMat.getIdPlanMateria());
                
                List<DtoMatricula> estudiantesGrado = estudiantes.stream().filter(p->p.getEstudiante().getGrupo().getGrado()==planEstudioMateria.getGrado()).collect(Collectors.toList());
                
                Double promedio = getObtenerPromedioAsignatura(planEstudioMateria, estudiantesGrado).getValor();
                
                Double promedioHombres = getObtenerPromedioAsignaturaHombres(planEstudioMateria, estudiantesGrado).getValor();
                    
                Double promedioMujeres = getObtenerPromedioAsignaturaMujeres(planEstudioMateria, estudiantesGrado).getValor();
                
                DtoAprovechamientoEscolar dtoAprovechamientoEscolar = new DtoAprovechamientoEscolar(programa, periodo, planEstudioMateria, promedio, promedioHombres, promedioMujeres);
                listaAprovechamiento.add(dtoAprovechamientoEscolar);
            });
            
            return ResultadoEJB.crearCorrecto(listaAprovechamiento.stream().sorted(DtoAprovechamientoEscolar::compareTo).collect(Collectors.toList()), "Aprovechamiento escolar del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener aprovechamiento escolar del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getAprovechamientoEscolarPrograma)", e, null);
        }
    }
    
    /**
     * Permite obtener aprovechamiento escolar del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAprovechamientoEscolar>> getAprovechamientoEscolar(PeriodosEscolares periodo, Personal personal){
        try{
           List<DtoAprovechamientoEscolar> listaAprovechamiento = new ArrayList<>();
           
           List<AreasUniversidad> programasEducativos = getProgramasEducativos(periodo, personal).getValor();
           
           programasEducativos.forEach(programa -> {
               
            List<DtoMatricula> estudiantes = getMatriculaPrograma(periodo, programa).getValor();
           
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
            
            List<PlanEstudioMateria> planEstudiosMaterias = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.evento.periodo=:periodo AND c.cveGrupo.idPe=:programa AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .map(p->p.getIdPlanMateria())
                    .distinct()
                    .collect(Collectors.toList());
           
                planEstudiosMaterias.forEach(planEstudioMat -> {
                    PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, planEstudioMat.getIdPlanMateria());
                
                    List<DtoMatricula> estudiantesGrado = estudiantes.stream().filter(p->p.getEstudiante().getGrupo().getGrado()==planEstudioMateria.getGrado()).collect(Collectors.toList());
                
                    Double promedio = getObtenerPromedioAsignatura(planEstudioMateria, estudiantesGrado).getValor();
                    
                    Double promedioHombres = getObtenerPromedioAsignaturaHombres(planEstudioMateria, estudiantesGrado).getValor();
                    
                    Double promedioMujeres = getObtenerPromedioAsignaturaMujeres(planEstudioMateria, estudiantesGrado).getValor();
                
                    DtoAprovechamientoEscolar dtoAprovechamientoEscolar = new DtoAprovechamientoEscolar(programa, periodo, planEstudioMateria, promedio, promedioHombres, promedioMujeres);
                    listaAprovechamiento.add(dtoAprovechamientoEscolar);
                });
            
            });
            
            return ResultadoEJB.crearCorrecto(listaAprovechamiento.stream().sorted(DtoAprovechamientoEscolar::compareTo).collect(Collectors.toList()), "Aprovechamiento escolar del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener aprovechamiento escolar del periodo seleccionado. (EjbReportesAcademicos.getAprovechamientoEscolar)", e, null);
        }
    }
    
     /**
     * Permite obtener reprobación por asignatura del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReprobacionAsignatura>> getReprobacionAsignaturaPrograma(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
           List<DtoReprobacionAsignatura> listaReprobacionAsignatura = new ArrayList<>();
           
           List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
           
           List<PlanEstudioMateria> planEstudiosMaterias = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.evento.periodo=:periodo AND c.cveGrupo.idPe=:programa AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .map(p->p.getIdPlanMateria())
                    .distinct()
                    .collect(Collectors.toList());
           
            planEstudiosMaterias.forEach(planEstudioMat -> {
                PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, planEstudioMat.getIdPlanMateria());
                
                List<Estudiante> estudiantesGrado = getMatriculaInicial(periodo, programa, planEstudioMateria.getGrado()).getValor();
                
                List<CalificacionNivelacion> calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante IN :estudiantes ", CalificacionNivelacion.class)
                    .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                    .setParameter("estudiantes", estudiantesGrado.stream().map(p->p.getIdEstudiante()).collect(Collectors.toList()))
                    .getResultStream()
                    .collect(Collectors.toList());
                
                Integer aprobadosNivelacion = (int)calificacionNivelacion.stream().filter(p->p.getValor()>=8).count();
                
                Integer reprobadosNivelacion = (int)calificacionNivelacion.stream().filter(p->p.getValor()<8).count();
                
                Double porcentajeRepOrdinario=0.0, porcentajeRepNivelacion=0.0;
                
                if(!calificacionNivelacion.isEmpty() || calificacionNivelacion.size()!=0){
                    porcentajeRepOrdinario =  (double) calificacionNivelacion.size()/estudiantesGrado.size() * 100;
                    porcentajeRepNivelacion =  (double) reprobadosNivelacion/estudiantesGrado.size() * 100;
                }
                
                DtoReprobacionAsignatura dtoReprobacionAsignatura = new DtoReprobacionAsignatura(programa, periodo, planEstudioMateria, calificacionNivelacion.size(), aprobadosNivelacion, reprobadosNivelacion, estudiantesGrado.size(), porcentajeRepOrdinario, porcentajeRepNivelacion);
                listaReprobacionAsignatura.add(dtoReprobacionAsignatura);
            });
            
            return ResultadoEJB.crearCorrecto(listaReprobacionAsignatura.stream().sorted(DtoReprobacionAsignatura::compareTo).collect(Collectors.toList()), "Reprobación por asignatura del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener reprobación por asignatura del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getReprobacionAsignaturaPrograma)", e, null);
        }
    }
    
    /**
     * Permite obtener reprobación por asignatura del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReprobacionAsignatura>> getReprobacionAsignatura(PeriodosEscolares periodo, Personal personal){
        try{
           List<DtoReprobacionAsignatura> listaReprobacionAsignatura = new ArrayList<>();
           
           List<Short> programasEducativos = getProgramasEducativos(periodo, personal).getValor().stream().map(p->p.getArea()).collect(Collectors.toList());
           
           List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
           
           List<PlanEstudioMateria> planEstudiosMaterias = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.evento.periodo=:periodo AND c.cveGrupo.idPe IN :programas AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("grados", grados)
                    .getResultStream()
                    .map(p->p.getIdPlanMateria())
                    .distinct()
                    .collect(Collectors.toList());
           
            planEstudiosMaterias.forEach(planEstudioMat -> {
                PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, planEstudioMat.getIdPlanMateria());
                AreasUniversidad programa = em.find(AreasUniversidad.class, planEstudioMateria.getIdPlan().getIdPe());
                List<Estudiante> estudiantesGrado = getMatriculaInicial(periodo, programa, planEstudioMateria.getGrado()).getValor();
                
                List<CalificacionNivelacion> calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante IN :estudiantes ", CalificacionNivelacion.class)
                    .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                    .setParameter("estudiantes", estudiantesGrado.stream().map(p->p.getIdEstudiante()).collect(Collectors.toList()))
                    .getResultStream()
                    .collect(Collectors.toList());
                
                Integer aprobadosNivelacion = (int)calificacionNivelacion.stream().filter(p->p.getValor()>=8).count();
                
                Integer reprobadosNivelacion = (int)calificacionNivelacion.stream().filter(p->p.getValor()<8).count();
                
                Double porcentajeRepOrdinario=0.0, porcentajeRepNivelacion=0.0;
                
                if(!calificacionNivelacion.isEmpty() || calificacionNivelacion.size()!=0){
                    porcentajeRepOrdinario =  (double) calificacionNivelacion.size()/estudiantesGrado.size() * 100;
                    porcentajeRepNivelacion =  (double) reprobadosNivelacion/estudiantesGrado.size() * 100;
                }
                
                DtoReprobacionAsignatura dtoReprobacionAsignatura = new DtoReprobacionAsignatura(programa, periodo, planEstudioMateria, calificacionNivelacion.size(), aprobadosNivelacion, reprobadosNivelacion, estudiantesGrado.size(), porcentajeRepOrdinario, porcentajeRepNivelacion);
                listaReprobacionAsignatura.add(dtoReprobacionAsignatura);
            });
            
            return ResultadoEJB.crearCorrecto(listaReprobacionAsignatura.stream().sorted(DtoReprobacionAsignatura::compareTo).collect(Collectors.toList()), "Reprobación por asignatura del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener reprobación por asignatura escolar del periodo seleccionado. (EjbReportesAcademicos.getReprobacionAsignatura)", e, null);
        }
    }
    
     /**
     * Permite obtener el promedio del cuatrimestre del estudiante seleccionado
     * @param planEstudioMateria
     * @param estudiantes
     * @return Resultado del proceso
     */
     public ResultadoEJB<Double> getObtenerPromedioAsignatura(PlanEstudioMateria planEstudioMateria, List<DtoMatricula> estudiantes){
        try{
            Double promedio=0.0;
            
            List<Double> listaCalificaciones = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> { 
                Double calificacion = 0.0;
                
                CalificacionPromedio calificacionPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionPromedio.class)
                    .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                    .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null); 
                
                if(calificacionPromedio!=null){
                    if(calificacionPromedio.getValor()<8.0){
                        CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                            .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                            .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                            .getResultStream()
                            .findFirst()
                            .orElse(null); 
                        if(calificacionNivelacion!=null){ 
                            calificacion = calificacionNivelacion.getValor(); 
                        }
                    }else{
                        calificacion = calificacionPromedio.getValor();
                    }
                }
               listaCalificaciones.add(calificacion);
            });
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
     * Permite obtener el promedio del cuatrimestre del estudiante seleccionado
     * @param planEstudioMateria
     * @param estudiantes
     * @return Resultado del proceso
     */
     public ResultadoEJB<Double> getObtenerPromedioAsignaturaHombres(PlanEstudioMateria planEstudioMateria, List<DtoMatricula> estudiantes){
        try{
            Double promedio=0.0;
            
            List<Double> listaCalificaciones = new ArrayList<>();
            
            List<DtoMatricula> estudiantesHombres = estudiantes.stream().filter(p->p.getGenero().getGenero()==2).collect(Collectors.toList());
            
            if(!estudiantesHombres.isEmpty()){
            
                estudiantesHombres.forEach(estudiante -> { 
                    Double calificacion = 0.0;
                
                    CalificacionPromedio calificacionPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionPromedio.class)
                        .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                        .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                        .getResultStream()
                        .findFirst()
                        .orElse(null); 
                
                    if(calificacionPromedio!=null){
                        if(calificacionPromedio.getValor()<8.0){
                            CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                                .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                                .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                                .getResultStream()
                                .findFirst()
                                .orElse(null); 
                            if(calificacionNivelacion!=null){ 
                                calificacion = calificacionNivelacion.getValor(); 
                            }
                        }else{
                            calificacion = calificacionPromedio.getValor();
                        }
                    }
                listaCalificaciones.add(calificacion);
                });
                if(estudiantesHombres.size() == listaCalificaciones.size())
                {
                promedio = listaCalificaciones.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
                }
            }
            
            return ResultadoEJB.crearCorrecto(promedio, "Promedio de la asignatura en el periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el promedio de la asignatura en el periodo seleccionado. (EjbReportesAcademicos.getObtenerPromedioAsignaturaHombres)", e, null);
        }
    }
     
      /**
     * Permite obtener el promedio del cuatrimestre del estudiante seleccionado
     * @param planEstudioMateria
     * @param estudiantes
     * @return Resultado del proceso
     */
     public ResultadoEJB<Double> getObtenerPromedioAsignaturaMujeres(PlanEstudioMateria planEstudioMateria, List<DtoMatricula> estudiantes){
        try{
            Double promedio=0.0;
            
            List<Double> listaCalificaciones = new ArrayList<>();
            
            List<DtoMatricula> estudiantesMujeres = estudiantes.stream().filter(p->p.getGenero().getGenero()==1).collect(Collectors.toList());
            
            if(!estudiantesMujeres.isEmpty()){
                estudiantesMujeres.forEach(estudiante -> {
                    Double calificacion = 0.0;

                    CalificacionPromedio calificacionPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionPromedio.class)
                            .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                            .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    if (calificacionPromedio != null) {
                        if (calificacionPromedio.getValor() < 8.0) {
                            CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.idPlanMateria.idPlanMateria=:planMateria AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                                    .setParameter("planMateria", planEstudioMateria.getIdPlanMateria())
                                    .setParameter("estudiante", estudiante.getEstudiante().getIdEstudiante())
                                    .getResultStream()
                                    .findFirst()
                                    .orElse(null);
                            if (calificacionNivelacion != null) {
                                calificacion = calificacionNivelacion.getValor();
                            }
                        } else {
                            calificacion = calificacionPromedio.getValor();
                        }
                    }
                    listaCalificaciones.add(calificacion);
                });
                if (estudiantesMujeres.size() == listaCalificaciones.size()) {
                    promedio = listaCalificaciones.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
                }
            }
            return ResultadoEJB.crearCorrecto(promedio, "Promedio de la asignatura en el periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el promedio de la asignatura en el periodo seleccionado. (EjbReportesAcademicos.getObtenerPromedioAsignaturaMujeres)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aprovechamiento escolar del periodo y programa educativo seleccionado
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAprovechamientoEscolarEstudiante>> getListaAprovechamientoEscolarPrograma(PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            List<DtoAprovechamientoEscolarEstudiante> listaAprovechamiento = new ArrayList<>();
            
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(2); tiposEstudiante.add(3); tiposEstudiante.add(4);
            
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE e.periodo=:periodo AND e.carrera=:programa AND g.idPe=:programa AND g.grado NOT IN :grados AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grados", grados)
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
           
            estudiantes.forEach(estudiante -> {
                Generos genero = em.find(Generos.class, estudiante.getAspirante().getIdPersona().getGenero());
                String discapacidad = getTipoDiscapacidad(estudiante).getValor();
                String lenguaIndigena = getLenguaIndigena(estudiante).getValor();
                
                String promedio = String.format("%.2f",getObtenerPromedioEstudiante(estudiante).getValor());
                String promedioAcumulado = String.format("%.2f",getObtenerPromedioAcumuladoEstudiante(estudiante).getValor());
                
                DtoAprovechamientoEscolarEstudiante dtoAprovechamientoEscolar = new DtoAprovechamientoEscolarEstudiante(estudiante,programa,periodo,genero,discapacidad,lenguaIndigena, promedio, promedioAcumulado);
                listaAprovechamiento.add(dtoAprovechamientoEscolar);
            });
            return ResultadoEJB.crearCorrecto(listaAprovechamiento.stream().sorted(DtoAprovechamientoEscolarEstudiante::compareTo).collect(Collectors.toList()), "Lista de aprovechamiento escolar del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de aprovechamiento escolar del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getListaAprovechamientoEscolarPrograma)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de aprovechamiento escolar del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAprovechamientoEscolarEstudiante>> getListaAprovechamientoEscolar(PeriodosEscolares periodo, ProgramasEducativosNiveles nivel, Personal personal){
        try{
            List<DtoAprovechamientoEscolarEstudiante> listaAprovechamiento = new ArrayList<>();
            
            List<Short> programasEducativos = getProgramasEducativos(periodo, personal).getValor().stream().filter(p->p.getNivelEducativo().equals(nivel)).map(p->p.getArea()).collect(Collectors.toList());
            
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(2); tiposEstudiante.add(3); tiposEstudiante.add(4); tiposEstudiante.add(5);
            
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE e.periodo=:periodo AND e.carrera IN :programas AND g.idPe IN :programas AND g.grado NOT IN :grados AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("grados", grados)
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
           
            estudiantes.forEach(estudiante -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, estudiante.getCarrera());
                Generos genero = em.find(Generos.class, estudiante.getAspirante().getIdPersona().getGenero());
                String discapacidad = getTipoDiscapacidad(estudiante).getValor();
                String lenguaIndigena = getLenguaIndigena(estudiante).getValor();
                
                String promedio = String.format("%.2f",getObtenerPromedioEstudiante(estudiante).getValor());
                String promedioAcumulado = String.format("%.2f",getObtenerPromedioAcumuladoEstudiante(estudiante).getValor());
                
                DtoAprovechamientoEscolarEstudiante dtoAprovechamientoEscolar = new DtoAprovechamientoEscolarEstudiante(estudiante,programa,periodo,genero,discapacidad,lenguaIndigena, promedio,promedioAcumulado);
                listaAprovechamiento.add(dtoAprovechamientoEscolar);
            });
            return ResultadoEJB.crearCorrecto(listaAprovechamiento.stream().sorted(DtoAprovechamientoEscolarEstudiante::compareTo).collect(Collectors.toList()), "Lista de aprovechamiento escolar del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de aprovechamiento escolar del periodo seleccionado. (EjbReportesAcademicos.getListaAprovechamientoEscolar)", e, null);
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
     * Permite obtener el promedio del cuatrimestre del estudiante seleccionado
     * @param estudiante
     * @return Resultado del proceso
     */
     public ResultadoEJB<Double> getObtenerPromedioAcumuladoEstudiante(Estudiante estudiante){
        try{
            
            Double promedio=0.0;
            
            List<Double> promediosCuatrimestre = new ArrayList<>();
            
            List<Short> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add((short)2); tiposEstudiante.add((short)3);
            
             List<Estudiante> estudiantes = new ArrayList<>();
            
            
            if(estudiante.getGrupo().getGrado()>=7){
                List<Integer> gradosExcluir = new ArrayList<>(); gradosExcluir.add(6); gradosExcluir.add(11);
                
                estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.periodo<=:periodo AND e.grupo.grado NOT IN :grados", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("periodo", estudiante.getPeriodo())
                    .setParameter("grados", gradosExcluir)
                    .getResultStream()
                    .collect(Collectors.toList());
            }else{
                estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.periodo<=:periodo", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("periodo", estudiante.getPeriodo())
                    .getResultStream()
                    .collect(Collectors.toList());
            }
            
            estudiantes.forEach(est -> {
                Double promedioCuatrimestre = getObtenerPromedioEstudiante(est).getValor();
                promediosCuatrimestre.add(promedioCuatrimestre);
            });
            
            Integer parametro = estudiante.getGrupo().getGrado();
            
            if(estudiante.getGrupo().getGrado()>=7 && estudiante.getGrupo().getGrado()<=10){
            
                parametro = estudiante.getGrupo().getGrado() - 1;
            
            }else if(estudiante.getGrupo().getGrado()==11){
            
                 parametro = estudiante.getGrupo().getGrado() - 2;
            }
            
            if(parametro == promediosCuatrimestre.size()){
                promedio = promediosCuatrimestre.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            }
            
            return ResultadoEJB.crearCorrecto(promedio, "Promedio acumulado del estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el promedio acumulado del estudiante en el periodo seleccionado. (EjbReportesAcademicos.getObtenerPromedioAcumuladoEstudiante)", e, null);
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
                if (encuestaAspirante.getR1Lenguaindigena() == null) {
                    lenguaIndigena = "Sin información";
                } else if (encuestaAspirante.getR1Lenguaindigena().equals("Si") && encuestaAspirante.getR2tipoLenguaIndigena() != null) {
                    lenguaIndigena = encuestaAspirante.getR2tipoLenguaIndigena().getNombre();
                } else if (encuestaAspirante.getR1Lenguaindigena().equals("Si") && encuestaAspirante.getR2tipoLenguaIndigena() == null) {
                    lenguaIndigena = "No indicó lengua que habla";
                } else if (encuestaAspirante.getR1Lenguaindigena().equals("No") && encuestaAspirante.getR2tipoLenguaIndigena() != null) {
                    lenguaIndigena = encuestaAspirante.getR2tipoLenguaIndigena().getNombre();
                } else if (encuestaAspirante.getR1Lenguaindigena().equals("No") && encuestaAspirante.getR2tipoLenguaIndigena() == null) {
                    lenguaIndigena = "No aplica";
                } 
            }else{
                 lenguaIndigena = "No aplica";
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
    public ResultadoEJB<List<DtoEstudianteIrregular>> getEstudiantesIrregularesPrograma(PeriodosEscolares periodo, AreasUniversidad programa){
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
                    
                    if (promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 1 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==5 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==6) {
                        PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, promedio.getCargaAcademica().getIdPlanMateria().getIdPlanMateria());
                        Personal docente = em.find(Personal.class, promedio.getCargaAcademica().getDocente());
                        String tutor = "Sin tutor";
                        if(promedio.getEstudiante().getGrupo().getTutor()!= null){
                          Personal personalTutor = em.find(Personal.class, promedio.getEstudiante().getGrupo().getTutor());
                          tutor = personalTutor.getNombre();
                        }
                        DtoEstudianteIrregular dtoEstudianteIrregular = new DtoEstudianteIrregular(promedio.getEstudiante(), programa, periodo, planEstudioMateria, docente, String.format("%.2f", calificacion), tutor);
                        listaEstudiantesIrregulares.add(dtoEstudianteIrregular);
                    }
                }else if (calificacionNivelacion == null){
                    calificacion = promedio.getValor();
                    if (promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 1 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==5 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==6) {
                        PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, promedio.getCargaAcademica().getIdPlanMateria().getIdPlanMateria());
                        Personal docente = em.find(Personal.class, promedio.getCargaAcademica().getDocente());
                        String tutor = "Sin tutor";
                        if(promedio.getEstudiante().getGrupo().getTutor()!= null){
                          Personal personalTutor = em.find(Personal.class, promedio.getEstudiante().getGrupo().getTutor());
                          tutor = personalTutor.getNombre();
                        }
                        DtoEstudianteIrregular dtoEstudianteIrregular = new DtoEstudianteIrregular(promedio.getEstudiante(), programa, periodo, planEstudioMateria, docente, String.format("%.2f", calificacion), tutor);
                        listaEstudiantesIrregulares.add(dtoEstudianteIrregular);
                    }
                }
                }
            });
            
            return ResultadoEJB.crearCorrecto(listaEstudiantesIrregulares.stream().sorted(DtoEstudianteIrregular::compareTo).collect(Collectors.toList()), "Lista de estudiantes irregulares del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes irregulares del periodo y programa educativo seleccionado. (EjbReportesAcademicos.getEstudiantesIrregularesPrograma)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de aprovechamiento escolar del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEstudianteIrregular>> getEstudiantesIrregulares(PeriodosEscolares periodo, Personal personal){
        try{
            List<Short> programasEducativos = getProgramasEducativos(periodo, personal).getValor().stream().map(p->p.getArea()).collect(Collectors.toList());
            
            List<DtoEstudianteIrregular> listaEstudiantesIrregulares = new ArrayList<>();
            
            List<CalificacionPromedio> calificacionesPromedio = new ArrayList<>();
             
            if(personal.getAreaOperativa()==23){
                calificacionesPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.estudiante.periodo=:periodo AND c.estudiante.carrera IN :programas AND c.valor<:valor AND (c.cargaAcademica.idPlanMateria.idMateria.nombre like concat('%',:nombre1,'%') OR c.cargaAcademica.idPlanMateria.idMateria.nombre like concat('%',:nombre2,'%'))", CalificacionPromedio.class)
                        .setParameter("periodo", periodo.getPeriodo())
                        .setParameter("programas", programasEducativos)
                        .setParameter("valor", (double) 8)
                        .setParameter("nombre1", "Inglés")
                        .setParameter("nombre2", "Francés")
                        .getResultStream()
                        .collect(Collectors.toList());
            }else{
                calificacionesPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.estudiante.periodo=:periodo AND c.estudiante.carrera IN :programas AND c.valor<:valor", CalificacionPromedio.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("valor", (double)8)
                    .getResultStream()
                    .collect(Collectors.toList());
            }

            calificacionesPromedio.forEach(promedio -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, promedio.getCargaAcademica().getCveGrupo().getIdPe());
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
                        String tutor = "Sin tutor";
                        if(promedio.getEstudiante().getGrupo().getTutor()!= null){
                          Personal personalTutor = em.find(Personal.class, promedio.getEstudiante().getGrupo().getTutor());
                          tutor = personalTutor.getNombre();
                        }
                        DtoEstudianteIrregular dtoEstudianteIrregular = new DtoEstudianteIrregular(promedio.getEstudiante(), programa, periodo, planEstudioMateria, docente, String.format("%.2f", calificacion), tutor);
                        listaEstudiantesIrregulares.add(dtoEstudianteIrregular);
                    }
                }else if (calificacionNivelacion == null){
                    calificacion = promedio.getValor();
                    if (promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 1 || promedio.getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==5) {

                        PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, promedio.getCargaAcademica().getIdPlanMateria().getIdPlanMateria());
                        Personal docente = em.find(Personal.class, promedio.getCargaAcademica().getDocente());
                        String tutor = "Sin tutor";
                        if(promedio.getEstudiante().getGrupo().getTutor()!= null){
                          Personal personalTutor = em.find(Personal.class, promedio.getEstudiante().getGrupo().getTutor());
                          tutor = personalTutor.getNombre();
                        }
                        DtoEstudianteIrregular dtoEstudianteIrregular = new DtoEstudianteIrregular(promedio.getEstudiante(), programa, periodo, planEstudioMateria, docente, String.format("%.2f", calificacion), tutor);
                        listaEstudiantesIrregulares.add(dtoEstudianteIrregular);
                    }
                }
                }
            });
            
            return ResultadoEJB.crearCorrecto(listaEstudiantesIrregulares.stream().sorted(DtoEstudianteIrregular::compareTo).collect(Collectors.toList()), "Lista de estudiantes irregulares del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes irregulares del periodo seleccionado. (EjbReportesAcademicos.getEstudiantesIrregulares)", e, null);
        }
    }
    
     /**
     * Permite obtener la matricula inicial del periodo, programa educativo y grado seleccionado
     * @param periodo
     * @param programa
     * @param grado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Estudiante>> getMatriculaInicial(PeriodosEscolares periodo, AreasUniversidad programa, Integer grado){
        try{
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Estudiante> matriculaInicial = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE e.periodo=:periodo AND e.carrera=:programa AND g.idPe=:programa AND g.grado=:grado AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programa", programa.getArea())
                    .setParameter("grado", grado)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(matriculaInicial, "Matricula inicial del periodo, programa educativo y grado seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la matricula inicial del periodo, programa educativo y grado seleccionado. (EjbReportesAcademicos.getMatriculaInicial)", e, null);
        }
    }
    
      /**
     * Permite generar reporte de estudiantes irregulares para servicios escolares o dirección de carrera del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteEstIrregulares(PeriodosEscolares periodo, PersonalActivo personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, personal.getAreaOperativa().getSiglas());
       
        if(personal.getPersonal().getAreaOperativa()==10){
            rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, "ServEsc");
        }else if(personal.getPersonal().getAreaOperativa()==6){
            rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, "PlanEval");
        }else if(personal.getPersonal().getAreaOperativa()==9){
            rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, "InfEst");
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reporteEstIrreg.xlsx";
        
        String plantillaC = rutaPlantillaC.concat("reporteEstudiantesIrregulares.xlsx");
        
        Map beans = new HashMap();
        beans.put("estIrreg", getEstudiantesIrregulares(periodo, personal.getPersonal()).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de aprovechamiento escolar para servicios escolares o dirección de carrera del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteAprovEscolar(PeriodosEscolares periodo, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reporteAprovEscolar.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat("reporteAprovechamientoEscolar.xlsx");
        
        Map beans = new HashMap();
        beans.put("aprovEsc", getAprovechamientoEscolar(periodo, personal).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de aprovechamiento escolar por estudiante para servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param nivel Nivel educativo
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteAprovEstudiantes(PeriodosEscolares periodo, ProgramasEducativosNiveles nivel, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reportesAprovEst.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat("reporteAprovEscolarEstudiantes.xlsx");
        
        Map beans = new HashMap();
        beans.put("aprovEscEst", getListaAprovechamientoEscolar(periodo, nivel, personal).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de reprobación por asignatura para servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteRepAsignatura(PeriodosEscolares periodo, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reportesRepAsig.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat("reporteReprobacionAsignatura.xlsx");
        
        Map beans = new HashMap();
        beans.put("repAsig", getReprobacionAsignatura(periodo, personal).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de planeación docente para servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReportePlaneacionDoc(PeriodosEscolares periodo, PersonalActivo personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, personal.getAreaOperativa().getSiglas());
        
        if(personal.getPersonal().getAreaOperativa()==10){
            rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, "ServEsc");
        }else if(personal.getPersonal().getAreaOperativa()==6){
            rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, "PlanEval");
        }else if(personal.getPersonal().getAreaOperativa()==9){
            rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, "InfEst");
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reportePlanDoc.xlsx";

        String plantillaC = rutaPlantillaC.concat("reportePlaneacionDocente.xlsx");
        
        Map beans = new HashMap();
        beans.put("planDoc", getPlaneacionesDocente(periodo, personal.getPersonal()).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de matricula para servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteMatricula(PeriodosEscolares periodo, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reporteMatricula.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat("reporteMatricula.xlsx");
        
        Map beans = new HashMap();
        beans.put("matAct", getMatricula(periodo, personal).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de distribución de matricula para servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteDistMatricula(PeriodosEscolares periodo, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reporteDisMat.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat("reporteDistribucionMatricula.xlsx");
        
        Map beans = new HashMap();
        beans.put("distMat", getDistribucionMatricula(periodo, personal).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
      /**
     * Permite generar reporte de deserción académica servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteDesercionAcad(PeriodosEscolares periodo, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\reporteDesercionAcad.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat("reporteDesercionAcademica.xlsx");
        
        Map beans = new HashMap();
        beans.put("desAcad", ejbRegistroBajas.obtenerListaBajasPeriodo(periodo).getValor().stream().filter(p->p.getDtoRegistroBaja().getRegistroBaja().getValidada()==1).collect(Collectors.toList()));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
     /**
     * Permite obtener la lista de promedios por materia de cada estudiante del periodo seleccionado
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoPromedioMateriaEstudiante>> getListaPromedioMateriaEstudiante(PeriodosEscolares periodo, Personal personal){
        try{
            List<Short> programasEducativos = getProgramasEducativos(periodo, personal).getValor().stream().map(p->p.getArea()).collect(Collectors.toList());
            
            List<DtoPromedioMateriaEstudiante> listaPromedioMateriaEstudiante = new ArrayList<>();
            
            List<String> tiposRegistro = new ArrayList<>(); tiposRegistro.add("Regularización de calificaciones por reincoporación");
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(2); tiposEstudiante.add(3); tiposEstudiante.add(4);
            
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);;
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE e.periodo=:periodo AND e.carrera IN :programas AND g.idPe IN :programas AND g.grado NOT IN :grados AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND e.tipoRegistro NOT IN :tiposRegistro", Estudiante.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("programas", programasEducativos)
                    .setParameter("grados", grados)
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("tiposRegistro", tiposRegistro)
                    .getResultStream()
                    .collect(Collectors.toList());
           
            estudiantes.forEach(estudiante -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
                List<CargaAcademica> cargasAcademicas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.cveGrupo.idGrupo=:grupo ", CargaAcademica.class)
                    .setParameter("grupo", estudiante.getGrupo().getIdGrupo())
                    .getResultStream()
                    .collect(Collectors.toList()); 
            
                cargasAcademicas.forEach(carga -> {
                    DtoPromedioMateriaEstudiante dtoPromedioMateriaEstudiante = getObtenerPromedioMateriaEstudiante(estudiante, carga, periodo, programa).getValor();
                    listaPromedioMateriaEstudiante.add(dtoPromedioMateriaEstudiante);
                });
            });
            return ResultadoEJB.crearCorrecto(listaPromedioMateriaEstudiante.stream().sorted(DtoPromedioMateriaEstudiante::compareTo).collect(Collectors.toList()), "Lista de promedios por materia de cada estudiante del periodo y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de promedios por materia de cada estudiante del periodo seleccionado. (EjbReportesAcademicos.getListaPromedioMateriaEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener el promedio por materia del estudiante seleccionado
     * @param estudiante
     * @param carga
     * @param periodo
     * @param programa
     * @return Resultado del proceso
     */
     public ResultadoEJB<DtoPromedioMateriaEstudiante> getObtenerPromedioMateriaEstudiante(Estudiante estudiante, CargaAcademica carga, PeriodosEscolares periodo, AreasUniversidad programa){
        try{
            Generos genero = em.find(Generos.class, estudiante.getAspirante().getIdPersona().getGenero());
            String discapacidad = getTipoDiscapacidad(estudiante).getValor();
            String lenguaIndigena = getLenguaIndigena(estudiante).getValor(); 

            String promedioOrdinario = "0.0", promedioNivelacion = "0.0";
            
            PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, carga.getIdPlanMateria().getIdPlanMateria());
            
            Personal docente = em.find(Personal.class, carga.getDocente());

            CalificacionPromedio calificacionPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.cargaAcademica.carga=:carga AND c.estudiante.idEstudiante=:estudiante", CalificacionPromedio.class)
                    .setParameter("carga", carga.getCarga())
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (calificacionPromedio != null) {
                promedioOrdinario = String.format("%.2f", calificacionPromedio.getValor());
            }

            CalificacionNivelacion calificacionNivelacion = em.createQuery("SELECT c FROM CalificacionNivelacion c WHERE c.cargaAcademica.carga=:carga AND c.estudiante.idEstudiante=:estudiante", CalificacionNivelacion.class)
                    .setParameter("carga", carga.getCarga())
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (calificacionNivelacion != null) {
                promedioNivelacion = String.format("%.2f", calificacionNivelacion.getValor());
            }
            
            DtoPromedioMateriaEstudiante dtoPromedioMateriaEstudiante = new DtoPromedioMateriaEstudiante(estudiante, programa, periodo, genero, discapacidad, lenguaIndigena, planEstudioMateria, docente, promedioOrdinario, promedioNivelacion);
            
            return ResultadoEJB.crearCorrecto(dtoPromedioMateriaEstudiante, "Promedio por materia del estudiante en el periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el promedio por materia del estudiante en el periodo seleccionado. (EjbReportesAcademicos.getObtenerPromedioMateriaEstudiante)", e, null);
        }
    }
     
      /**
     * Permite generar reportes académicos para servicios escolares del periodo escolar seleccionado
     * @param periodo Periodo Escolar
     * @param personal Personal
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReportePromediosMateriaEstudiante(PeriodosEscolares periodo, Personal personal) throws Throwable {
        String periodoEscolar = periodo.getMesInicio().getMes()+ "-" + periodo.getMesFin().getMes()+" "+ periodo.getAnio();
        String areaGeneraReporte = "ServEsc";
        
        if(personal.getAreaOperativa()==6){
            areaGeneraReporte = "PlanEval";
        }else if(personal.getAreaOperativa()==9){
            areaGeneraReporte = "InfEst";
        }
        
        String rutaPlantilla = "C:\\archivos\\formatosEscolares\\baseDatosPromediosEstudiante.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesAcademicos(periodoEscolar, areaGeneraReporte);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_PROMEDIOS);
        
        Map beans = new HashMap();
        beans.put("promMatEst", getListaPromedioMateriaEstudiante(periodo, personal).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
}
