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
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbValidadorDocente")
public class EjbValidadorDocente {
    @EJB        EjbPersonalBean             ejbPersonalBean;
    @EJB        EjbPropiedades              ep;
    @EJB        EjbAsignacionAcademica      ejbAsignacionAcademica;
    @EJB        EjbPacker                   ejbPacker;
    @EJB        EjbModulos                  ejbModulos;
    @EJB        Facade                      f;
    private     EntityManager               em;
    
    @Inject     Caster                      caster;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite validar si el usuario logueado es un docente
     * @param clave Número de nómina del usuario logueado
     * @return Regresa la instancia del personal si es que cumple con ser docente o codigo de error de lo contrario
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar. (EjbValidadorDocente.validarDocente)", e, null);
        }
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarTutor(Integer clave){
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
//            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("tutorGrupo")));
            filtro.addParam(PersonalFiltro.TUTOR.getLabel(), String.valueOf(ep.leerPropiedad("tutorGrupo")));
            if(getPeriodosConCapturaCargaAcademicaTutor(p).getCorrecto()){
                return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un tutor.");
            }else{
                return ResultadoEJB.crearErroneo(2, null,"El docente no cuenta con grupos tutorados");
            }
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar como tutor. (EjbValidadorDocente.validarDocente)", e, null);
        }
    }
    
//    public ResultadoEJB<List<DtoUnidadConfiguracion>> getUnidadesEnEvaluacion(PersonalActivo docente){
//        try{
//            List<DtoUnidadConfiguracion> configuraciones = em.createQuery("select c from UnidadMateriaConfiguracion  c where current_date between c.fechaInicio and  c.fechaFin and c.carga.docente=:docente", UnidadMateriaConfiguracion.class)
//                    .setParameter("docente", docente.getPersonal().getClave())
//                    .getResultStream()
//                    .distinct()
//                    .map(unidadMateriaConfiguracion -> {
//                        CargaAcademica carga = unidadMateriaConfiguracion.getCarga();
//                        ResultadoEJB<DtoCargaAcademica> resCarga = ejbPacker.packCargaAcademica(carga);
//                        if (!resCarga.getCorrecto()) return null;
//                        return ejbPacker.packUnidadConfiguracion(unidadMateriaConfiguracion, resCarga.getValor());
//                    })
//                    .filter(ResultadoEJB::getCorrecto)
//                    .map(ResultadoEJB::getValor)
//                    .collect(Collectors.toList());
//            if(configuraciones.isEmpty()) return ResultadoEJB.crearErroneo(2, null,"No se encontraron configuraciones de unidades abiertas para captura.");
//            return ResultadoEJB.crearCorrecto(configuraciones, "Configuraciones abiertas para captura encontradas.");
//        }catch (Exception e){
//            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener las unidades con evaluación activa por docente según la fecha actual (EjbValidadorDocente.getUnidadesEnEvaluacion).", e, null);
//        }
//    }
//    
    
    /**
     * Permite obtener una lista de periodos escolares ordenados en forma descendente por fecha en los que el docente ha tenido asignaciones académicas
     * @param docente Docente del cual se van a obtener sus periodos con asignaciones
     * @return Lista de periodos o código de error de lo contrario
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCapturaCargaAcademica(PersonalActivo docente){
        try{
            //consultar los periodos en los que el docente tenga asignaciones registradas y ordenarlo en forma descentente
            List<PeriodosEscolares> periodos = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.docente=:docente", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .map(cargaAcademica -> cargaAcademica.getCveGrupo())
                    .map(grupo -> grupo.getPeriodo())
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if(periodos.isEmpty()){
                //en caso de no tener ningun periodo arrojar  un código de error
                return  ResultadoEJB.crearErroneo(2, periodos,"No se identificaron periodos escolares con carga académica para el docente.");
            }else{
//                System.out.println("periodos = " + periodos);
                return ResultadoEJB.crearCorrecto(periodos, "Periodos descendentes con asignación académica del docente");
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que el docente ha tenido carga académica (EjbValidadorDocente.getPeriodosConCapturaAsesorias).", e, null);
        }
    }

    /**
     * Permite obtener la lista de cargas académicas que un docente tiene en el periodo escolar seleccionado por el usuario
     * @param docente Docente logueado en sistema
     * @param periodo Periodo seleccionado en pantalla
     * @return Regresa lista de cargas académicas o código de error de lo contrario
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargasAcademicasPorPeriodo(PersonalActivo docente, PeriodosEscolares periodo){
        try {
            //obtener la lista de cargas académicas del docente
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.docente=:docente AND ca.evento.periodo = :periodo", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> ejbAsignacionAcademica.pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            if(cargas.isEmpty()) return  ResultadoEJB.crearErroneo(2, cargas, "Usted no tiene carga académica en el periodo seleccionado");
            else return ResultadoEJB.crearCorrecto(cargas, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas cadémicas por docente y periodo (EjbValidadorDocente.getCargasAcadémicasPorPeriodo).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoUnidadConfiguracion>> getConfiguraciones(DtoCargaAcademica dtoCargaAcademica){
        try {
            List<DtoUnidadConfiguracion> configuraciones = em.createQuery("select umc from UnidadMateriaConfiguracion umc where umc.carga=:carga", UnidadMateriaConfiguracion.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .getResultStream()
                    .map(unidadMateriaConfiguracion -> ejbPacker.packUnidadConfiguracion(unidadMateriaConfiguracion, dtoCargaAcademica))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(configuraciones.isEmpty()) return  ResultadoEJB.crearErroneo(2, configuraciones, "No se encontraron configuraciones en la carga académica seleccionada.");
            else return ResultadoEJB.crearCorrecto(configuraciones, "Configuraciones por carga académica");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de unidades y configuraciones (EjbValidadorDocente.getConfiguraciones).", e, null);
        }
    }
    
    
    /********************************************************* Herramientas tutor ****************************************************************/
    
//    TODO: Verificar el uso de este método y en su caso modificar a las necesidades
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCapturaCargaAcademicaTutor(PersonalActivo docente) {
        try {
            //consultar los periodos en los que el docente tenga asignaciones registradas y ordenarlo en forma descentente
            List<PeriodosEscolares> periodos = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.cveGrupo.tutor =:docente", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .map(cargaAcademica -> cargaAcademica.getCveGrupo())
                    .map(grupo -> grupo.getPeriodo())
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if (periodos.isEmpty()) {
                //en caso de no tener ningun periodo arrojar  un código de error
                return ResultadoEJB.crearErroneo(2, periodos, "No se identificaron periodos escolares con cargas académicas de su grupo tutorado.");
            } else {
//                System.out.println("periodos = " + periodos);
                return ResultadoEJB.crearCorrecto(periodos, "Periodos descendentes con asignación académica de su grupo tutorado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que su grupo ha tenido docentes con carga académica (EjbValidadorDocente.getPeriodosConCapturaCargaAcademicaTutor).", e, null);
        }
    }
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCasosCriticoRegistrados(PersonalActivo especialista) {
        try {
            List<PeriodosEscolares> periodos = em.createQuery("SELECT e FROM CasoCritico cc INNER JOIN cc.idEstudiante e WHERE cc.especialista = :especialista", Estudiante.class)
                    .setParameter("especialista", especialista.getPersonal().getClave())
                    .getResultStream()
                    .map(estudiante -> estudiante.getGrupo())
                    .map(grupo -> grupo.getPeriodo())
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if (periodos.isEmpty()) {
                //en caso de no tener ningun periodo arrojar  un código de error
                return ResultadoEJB.crearErroneo(2, periodos, "No se identificaron periodos escolares con casos críticos canalizados con especialista.");
            } else {
//                System.out.println("periodos = " + periodos);
                return ResultadoEJB.crearCorrecto(periodos, "Periodos descendentes con casos críticos canalizados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con casos críticos canalizados con especialista (EjbValidadorDocente.getPeriodosConCasosCriticoRegistrados).", e, null);
        }
    }
    
//    TODO: Verificar el uso de este método y en su caso modificar a las necesidades
    public ResultadoEJB<List<DtoCargaAcademica>> getCargasAcademicasPorPeriodoTutor(PersonalActivo docente, PeriodosEscolares periodo){
        try {
            //obtener la lista de cargas académicas del docente
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.cveGrupo.tutor =:docente AND ca.evento.periodo = :periodo", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> ejbAsignacionAcademica.pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            if(cargas.isEmpty()) return  ResultadoEJB.crearErroneo(2, cargas, "No se han encontrado cargas académicas en el periodo seleccionado");
            else return ResultadoEJB.crearCorrecto(cargas, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas cadémicas por docente y periodo (EjbValidadorDocente.getCargasAcademicasPorPeriodoTutor).", e, null);
        }
    }
    
//    TODO: Verificar el uso de este método y en su caso modificar a las necesidades
    public ResultadoEJB<List<DtoUnidadConfiguracion>> getConfiguracionesTutor(DtoCargaAcademica dtoCargaAcademica){
        try {
            List<DtoUnidadConfiguracion> configuraciones = em.createQuery("select umc from UnidadMateriaConfiguracion umc where umc.carga=:carga", UnidadMateriaConfiguracion.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .getResultStream()
                    .map(unidadMateriaConfiguracion -> ejbPacker.packUnidadConfiguracion(unidadMateriaConfiguracion, dtoCargaAcademica))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(configuraciones.isEmpty()) return  ResultadoEJB.crearErroneo(2, configuraciones, "No se encontraron configuraciones en la carga académica seleccionada.");
            else return ResultadoEJB.crearCorrecto(configuraciones, "Configuraciones por carga académica");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de unidades y configuraciones (EjbValidadorDocente.getConfiguracionesTutor).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoListadoTutores>> listarGruposTutor(PeriodosEscolares periodoEscolar, PersonalActivo tutor){
        try{
            List<DtoListadoTutores> listadoTutores = new ArrayList<>();
            em.createQuery("SELECT g FROM Grupo g WHERE g.periodo=:periodo AND g.tutor = :tutor ", Grupo.class)
                .setParameter("periodo", periodoEscolar.getPeriodo())
                .setParameter("tutor", tutor.getPersonal().getClave())
                .getResultStream().forEach(g ->{
                    DtoListadoTutores dlt = new DtoListadoTutores();
                        PersonalActivo personalActivo = ejbPersonalBean.pack(g.getTutor());
                        AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, g.getIdPe());
                        dlt.setGrupo(g);
                        dlt.setTutor(personalActivo);
                        dlt.setProgramaEducativo(programaEducativo);
                    listadoTutores.add(dlt);
                });
            List<DtoListadoTutores> listaDtoTutoresOrdenada = listadoTutores.stream()
                    .sorted(DtoListadoTutores::compareTo)
                    .collect(Collectors.toList());
            if(listaDtoTutoresOrdenada.isEmpty()){
                listaDtoTutoresOrdenada = Collections.EMPTY_LIST;
                return ResultadoEJB.crearErroneo(2, listaDtoTutoresOrdenada,"El docente no cuenta con grupos tutorados");
            }else{
                return ResultadoEJB.crearCorrecto(listaDtoTutoresOrdenada, "Listado de grupos del tutor");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el listado de grupos por tutor (EjbValidadorDocente.listarGruposTutor)", e, null);
        }
    }
    
}
