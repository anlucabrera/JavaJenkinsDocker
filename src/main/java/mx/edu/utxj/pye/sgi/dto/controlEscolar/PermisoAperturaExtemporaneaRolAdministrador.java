/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPermisoCapturaExtemporanea;

import java.util.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;

/**
 *
 * @author UTXJ
 */
public class PermisoAperturaExtemporaneaRolAdministrador extends AbstractRol{
    /**
     * Representa la referencia hacia el personal docente
     */
    @Getter @NonNull private PersonalActivo administrador;
    
     /**
     * Pista del docente asignar
     */
    @Getter @NonNull private String pistaDocente;
   
    /**
     * Docente seleccionado para asignar carga
     */
    @Getter @NonNull private PersonalActivo docente;
    
     /**
     * Lista de Cargas Académicas del docente
     */
    @Getter @NonNull private List<DtoCargaAcademica> cargasDocente;
    
     /**
     * Lista de Cargas Académicas del docente del periodo seleccionado
     */
    @Getter @NonNull private List<DtoCargaAcademica> cargasPeriodo;
    
    /**
     * Lista de Periodos en las que el docente tiene cargas académicas
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
    /**
     * Periodo seleccionado en el que se realizará la apertura
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Carga académica seleccionada
     */
    @Getter @NonNull private DtoCargaAcademica carga;
    
     /**
     * Lista de tipos de evaluación
     */
    @Getter @NonNull private List<String> tiposEvaluacion;
    
     /**
     * Tipos de evaluación seleccionada
     */
    @Getter @NonNull private String tipoEvaluacion;
        
    /**
     * Lista de unidades que tiene la materia seleccionada
     */
    @Getter @NonNull private List<UnidadMateria> unidadesMateria;
    
     /**
     * Unidad de la materia seleccionada
     */
    @Getter @NonNull private UnidadMateria unidadMateria;
    
    /**
     * Fecha de inicio del permiso de apertura
     */
    @Getter @NonNull private Date fechaInicio;

    /**
     * Fecha de fin del permiso de apertura
     */
    @Getter @NonNull private Date fechaFin;
    
     /**
     * Rango de fechas disponibles para inicio y fin del permiso
     */
    @Getter @NonNull private DtoRangoFechasPermiso dtoRangoFechasPermiso;
    
    /**
     * Fecha miníma para permiso de apertura
     */
    @Getter @NonNull private Date rangoFechaInicial;

    /**
     * Fecha máxima para permiso de apertura
     */
    @Getter @NonNull private Date rangoFechaFinal;
    
     /**
     * Lista de justificaciones para solicitud de apertura extemporanea
     */
    @Getter @NonNull private List<JustificacionPermisosExtemporaneos> listaJustificaciones;
    
    /**
     * Justificación de la solicitud de apertura extemporanea
     */
    @Getter @NonNull private JustificacionPermisosExtemporaneos justificacionPermisosExtemporaneos;
    
    /**
     * Permiso de captura extemporanea registrado
     */
    @Getter @NonNull private PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal;
    
    /**
     * Existen permiso(s) de captura extemporanea vigente
     */
    @Getter @NonNull private Boolean existePermisoCapturasExt;
    
    /**
     * Lista de permisos de captura extemporanea
     */
    @Getter @NonNull private List<DtoPermisoCapturaExtemporanea>  listaPermisosCapturasExtemporaneas;
    
    /**
     * Lista de estudiantes que integran el grupo seleccionado
     */
    @Getter @NonNull private List<Estudiante>  listaEstudiantes;
    
     /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private Estudiante estudiante;
    
     /**
     * Valor permisoActivo (Si/No)
     */
    @Getter @NonNull private String permisoActivo; 
    
    public PermisoAperturaExtemporaneaRolAdministrador(Filter<PersonalActivo> filtro, PersonalActivo administrador) {
        super(filtro);
        this.administrador = administrador;
    }

    public void setAdministrador(PersonalActivo administrador) {
        this.administrador = administrador;
    }

    public void setPistaDocente(String pistaDocente) {
        this.pistaDocente = pistaDocente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setCargasDocente(List<DtoCargaAcademica> cargasDocente) {
        this.cargasDocente = cargasDocente;
    }

    public void setCargasPeriodo(List<DtoCargaAcademica> cargasPeriodo) {
        this.cargasPeriodo = cargasPeriodo;
    }
    
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setCarga(DtoCargaAcademica carga) {
        this.carga = carga;
    }

    public void setTiposEvaluacion(List<String> tiposEvaluacion) {
        this.tiposEvaluacion = tiposEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public void setUnidadesMateria(List<UnidadMateria> unidadesMateria) {
        this.unidadesMateria = unidadesMateria;
    }

    public void setUnidadMateria(UnidadMateria unidadMateria) {
        this.unidadMateria = unidadMateria;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDtoRangoFechasPermiso(DtoRangoFechasPermiso dtoRangoFechasPermiso) {
        this.dtoRangoFechasPermiso = dtoRangoFechasPermiso;
    }

    public void setRangoFechaInicial(Date rangoFechaInicial) {
        this.rangoFechaInicial = rangoFechaInicial;
    }

    public void setRangoFechaFinal(Date rangoFechaFinal) {
        this.rangoFechaFinal = rangoFechaFinal;
    }
    
    public void setListaJustificaciones(List<JustificacionPermisosExtemporaneos> listaJustificaciones) {
        this.listaJustificaciones = listaJustificaciones;
    }
    
    public void setJustificacionPermisosExtemporaneos(JustificacionPermisosExtemporaneos justificacionPermisosExtemporaneos) {
        this.justificacionPermisosExtemporaneos = justificacionPermisosExtemporaneos;
    }

    public void setPermisosCapturaExtemporaneaGrupal(PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal) {
        this.permisosCapturaExtemporaneaGrupal = permisosCapturaExtemporaneaGrupal;
    }

    public void setExistePermisoCapturasExt(Boolean existePermisoCapturasExt) {
        this.existePermisoCapturasExt = existePermisoCapturasExt;
    }

    public void setListaPermisosCapturasExtemporaneas(List<DtoPermisoCapturaExtemporanea> listaPermisosCapturasExtemporaneas) {
        this.listaPermisosCapturasExtemporaneas = listaPermisosCapturasExtemporaneas;
    }

    public void setListaEstudiantes(List<Estudiante> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setPermisoActivo(String permisoActivo) {
        this.permisoActivo = permisoActivo;
    }
}
