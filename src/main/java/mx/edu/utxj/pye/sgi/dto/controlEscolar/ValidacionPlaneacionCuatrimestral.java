/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaindicadoresporcriterioporconfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionDetalle;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.timeline.TimelineModel;

/**
 *
 * @author UTXJ
 */
public class ValidacionPlaneacionCuatrimestral extends AbstractRol{
    
    
    //Representa la referencia hacia personal director
    @Getter    @NonNull    private PersonalActivo director;
    // Representa la referencia del programa educativo seleccionado
    @Getter    @NonNull    private AreasUniversidad programa;
    // Representa la referencia al plan de estudios seleccionado
    @Getter    @NonNull    private PlanEstudio planEstudio;
    /**
     * Representa la referencia al evento activo de asignación academica
     */
    @Getter @NonNull private EventoEscolar eventoActivo;
    
    /**
     * Periodo seleccionado en el que se realizará la asignación
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
    /**
     * Representa la clave
     */
    @Getter @Setter private Date fechaInpresion;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Double porcentajeSer;
    
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Double porcentajeSaber;
    
    
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Double porcentajeSaberHacer;
   
     /**
     * Representa la clave
     */
    @Getter @Setter private List<DtoGraficaCronograma> cronograma;
    @Getter @Setter private Integer cuatrimestre;
    @Getter @Setter private Double porcIni;
    @Getter @Setter private Integer numDtotales;
    @Getter @Setter private Boolean render;
    
    @Getter @Setter private String mensajeV;
    @Getter    @NonNull    private Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap;
    // Representa el listado de programas educativos vigentes
    @Getter    @NonNull    private List<AreasUniversidad> programas;
    // Representa el listado de las áreas de conocimiento
    @Getter    @NonNull    private List<PlanEstudio> planesEstudios;
    @Getter    @Setter    private Grupo grupoSelec;
    @Getter    @Setter    private List<Grupo> grupos;
    
    /**
     * Carga académica seleccionada
     */
    @Getter @NonNull private DtoCargaAcademica carga;
    
    /**
     * Indicador por criterio que se va asignar
     */
    @Getter @NonNull private Listaindicadoresporcriterioporconfiguracion asignarIndicadoresCriterios;

  
    /**
     * Periodos escolares para seleccionar
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;

    /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<Listaindicadoresporcriterioporconfiguracion> listaAsignarIndicadoresCriterios;
    
    /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<Listaindicadoresporcriterioporconfiguracion> listaAsignarIndicadoresCriteriosOriginal;
    
    
    /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<Listaindicadoresporcriterioporconfiguracion> listaCriteriosSer;
    
     /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<Listaindicadoresporcriterioporconfiguracion> listaCriteriosSaber;
    
     /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<Listaindicadoresporcriterioporconfiguracion> listaCriteriosSaberHacer;
   
     /**
     * Lista de configuraciones realizadas
     */
    @Getter @NonNull private List<DtoCargaAcademica> cargas;
    
     /**
     * Existe o no configuración de unidad materia
     */
    @Getter @NonNull private Boolean existeConfiguracion;
    
     /**
     * Dto de configuración de unidad por materia
     */
    @Getter @NonNull private DtoConfiguracionUnidadMateria dtoConfUniMat;
    
     /**
     * Lista de dto de configuración de unidad por materia
     */
    @Getter @NonNull private List<DtoConfiguracionUnidadMateria> listaDtoConfUniMat;
    
     /**
     * Existe o no asignación de indicadores de la unidad seleccionada
     */
    @Getter @NonNull private Boolean existeAsignacionUnidad;
    
    /**
     * Lista de asignación de indicadores por unidad por materia
     */
    @Getter @NonNull private List<UnidadMateriaConfiguracionDetalle> listaConfiguracionDetalles;
    
     /**
     * Existe o no asignación de indicadores de la carga académica seleccionada
     */
    @Getter @NonNull private Boolean existeAsignacionIndicadores;
    
     /**
     * Lista de asignación de indicadores por carga académica
     */
    @Getter @NonNull private List<DtoInformePlaneaciones> informeplaneacioncuatrimestraldocenteprints;
    
    public ValidacionPlaneacionCuatrimestral(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }
   
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
         if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }
    
    public void setCarga(DtoCargaAcademica carga) {
        this.carga = carga;
    }

    public void setAsignarIndicadoresCriterios(Listaindicadoresporcriterioporconfiguracion asignarIndicadoresCriterios) {
        this.asignarIndicadoresCriterios = asignarIndicadoresCriterios;
    }
        
    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }
    /**
     * Sincroniza el periodo seleccionado al primer periodo en la lista
     * @param periodos
     */
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos != null && !periodos.isEmpty()){
            this.setPeriodo(periodos.get(0));
        }
    }

    /**
     * Sincroniza los grupos del programa seleccionado y el primer grupo como seleccionado
     * @param cargas
     */
    public void setCargas(List<DtoCargaAcademica> cargas) {
        this.cargas = cargas;
        if (cargas != null && !cargas.isEmpty()) {
            this.setCarga(cargas.get(0));
        }
    }

    public void setListaAsignarIndicadoresCriterios(List<Listaindicadoresporcriterioporconfiguracion> listaAsignarIndicadoresCriterios) {
        this.listaAsignarIndicadoresCriterios = listaAsignarIndicadoresCriterios;
    }

    public void setListaAsignarIndicadoresCriteriosOriginal(List<Listaindicadoresporcriterioporconfiguracion> listaAsignarIndicadoresCriteriosOriginal) {
        this.listaAsignarIndicadoresCriteriosOriginal = listaAsignarIndicadoresCriteriosOriginal;
    }
    
    public void setListaCriteriosSer(List<Listaindicadoresporcriterioporconfiguracion> listaCriteriosSer) {
        this.listaCriteriosSer = listaCriteriosSer;
    }

    public void setListaCriteriosSaber(List<Listaindicadoresporcriterioporconfiguracion> listaCriteriosSaber) {
        this.listaCriteriosSaber = listaCriteriosSaber;
    }

    public void setListaCriteriosSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaCriteriosSaberHacer) {
        this.listaCriteriosSaberHacer = listaCriteriosSaberHacer;
    }
    
     public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPorcentajeSer(Double porcentajeSer) {
        this.porcentajeSer = porcentajeSer;
    }

    public void setPorcentajeSaber(Double porcentajeSaber) {
        this.porcentajeSaber = porcentajeSaber;
    }

    public void setPorcentajeSaberHacer(Double porcentajeSaberHacer) {
        this.porcentajeSaberHacer = porcentajeSaberHacer;
    }

    public void setExisteConfiguracion(Boolean existeConfiguracion) {
        this.existeConfiguracion = existeConfiguracion;
    }

    public void setDtoConfUniMat(DtoConfiguracionUnidadMateria dtoConfUniMat) {
        this.dtoConfUniMat = dtoConfUniMat;
    }

    public void setListaDtoConfUniMat(List<DtoConfiguracionUnidadMateria> listaDtoConfUniMat) {
        this.listaDtoConfUniMat = listaDtoConfUniMat;
    }

    public void setExisteAsignacionUnidad(Boolean existeAsignacionUnidad) {
        this.existeAsignacionUnidad = existeAsignacionUnidad;
    }

    public void setListaConfiguracionDetalles(List<UnidadMateriaConfiguracionDetalle> listaConfiguracionDetalles) {
        this.listaConfiguracionDetalles = listaConfiguracionDetalles;
    }

    public void setExisteAsignacionIndicadores(Boolean existeAsignacionIndicadores) {
        this.existeAsignacionIndicadores = existeAsignacionIndicadores;
    }

    public void setInformeplaneacioncuatrimestraldocenteprints(List<DtoInformePlaneaciones> informeplaneacioncuatrimestraldocenteprints) {
        this.informeplaneacioncuatrimestraldocenteprints = informeplaneacioncuatrimestraldocenteprints;
    }    

    public void setAreaPlanEstudioMap(Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap) {
        this.areaPlanEstudioMap = areaPlanEstudioMap;
        this.planesEstudios = new ArrayList<>();
        if (areaPlanEstudioMap != null) {
            this.programas = areaPlanEstudioMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
            areaPlanEstudioMap.forEach((t, u) -> {
                this.planesEstudios.addAll(u);
            });
        }
        if (areaPlanEstudioMap != null && programa != null && areaPlanEstudioMap.containsKey(programa)) {
            this.planesEstudios = areaPlanEstudioMap.get(programa);
            if (planesEstudios != null) {
                planesEstudios.get(0);
            }
        }
    }
}
