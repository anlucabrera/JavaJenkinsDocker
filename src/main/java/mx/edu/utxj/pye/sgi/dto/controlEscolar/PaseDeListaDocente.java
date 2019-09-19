/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaindicadoresporcriterioporconfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionDetalle;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class PaseDeListaDocente extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal docente
    */
    @Getter @NonNull private PersonalActivo tutor;

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
    @Getter @Setter private List<DtoPaseLista> dpls;
    @Getter @Setter private List<DtoPaseListaReporteConsulta> dtoPaseListaReporteConsultas;
    @Getter @Setter private List<DtoPaseListaReporte> dplsReportesMes;
    @Getter @Setter private List<Asistenciasacademicas> asistenciasacademicases;
    @Getter @Setter private DtoPaseListaReporteConsulta dplrc;
    @Getter @Setter private Boolean dplrcVisible= Boolean.FALSE;
    @Getter @Setter private List<String> asistencias;
    @Getter @Setter private List<Integer> diasPaseLista;
    @Getter @Setter private List<String> horasPaseLista;
    @Getter private DtoGrupoEstudiante estudiantesPorGrupo;
    @Getter @Setter private Map<Long, Double> calificacionMap = new HashMap<>();
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
    @Getter @NonNull private List<Listaalumnosca> listaalumnoscas;
    
    public PaseDeListaDocente(Filter<PersonalActivo> filtro, PersonalActivo tutor) {
        super(filtro);
        this.tutor = tutor;
    }

    public void setDocente(PersonalActivo tutor) {
        this.tutor = tutor;
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

    public void setListaalumnoscas(List<Listaalumnosca> listaalumnoscas) {
        this.listaalumnoscas = listaalumnoscas;
    }

    public void setEstudiantesPorGrupo(DtoGrupoEstudiante estudiantesPorGrupo) {
        this.estudiantesPorGrupo = estudiantesPorGrupo;
        if(estudiantesPorGrupo == null) return;
        calificacionMap.clear();
        estudiantesPorGrupo.getEstudiantes()
                .stream()
                .map(dtoCapturaCalificacion -> dtoCapturaCalificacion.getCapturas())
                .flatMap(capturas -> capturas.stream())
                .map(captura -> captura.getCalificacion())
                .forEach(calificacion -> {
                    Long clave = calificacion.getCalificacion();
                    Double valor = calificacion.getValor();
                    calificacionMap.put(clave, valor);
                });
//        System.out.println("calificacionMap = " + calificacionMap);
    }
}
