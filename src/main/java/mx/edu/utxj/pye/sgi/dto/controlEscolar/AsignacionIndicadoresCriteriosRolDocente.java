/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaindicadoresporcriterioporconfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionDetalle;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionEvidenciaInstrumento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AsignacionIndicadoresCriteriosRolDocente extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal docente
    */
    @Getter @NonNull private PersonalActivo docente;

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
    @Getter @NonNull private List<DtoAsignadosIndicadoresCriterios> listaAsignacionCargaAcademica;
    
     /**
     * Tipo de asignación de indicadores que realizará el docente (masiva o individual)
     */
    @Getter @NonNull private String opcAsignacion;
    
     /**
     * Lista de unidades materia configuración para la asignación masiva de indicadores
     */
    @Getter @NonNull private List<UnidadMateriaConfiguracion> listaUnidadMateriaConfiguracion;
    
     /**
     * Parametro que guardar valor si la configuración se encuentra validada por el director
     */
    @Getter private Integer directorValido;
    
    /**
     * Lista de evidencias de evaluación que debe registrar obligatoriamente
     */
    @Getter @NonNull private List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas;
    
     /**
     * Lista de evidencias e instrumentos de evaluación sugeridos
     */
    @Getter @NonNull private List<UnidadMateriaConfiguracionEvidenciaInstrumento> listaEvidenciasInstrumentos;
    
     /**
     * Representa si se desea agregar más evidencias e instrumentos de evaluación
     */
    @Getter @NonNull private Boolean agregarEvidencia;
    
     /**
     * Representa valor componente para habilitar o deshabilitar opciones par agregar evidencias
     */
    @Getter @NonNull private String valorAgregarEvid;
    
     /**
     * Representa si se agregará de manera masiva o individual la evidencia e instrumento de evaluación
     */
    @Getter @NonNull private String tipoAgregarEvid;
   
     /**
     * Lista de categorías de evaluación
     */
    @Getter @NonNull private List<Criterio> categorias;
    
     /**
     * Categoría de evaluación seleccionada
     */
    @Getter @NonNull private Criterio categoria;
    
    /**
     * Lista de evidencias de evaluación
     */
    @Getter @NonNull private List<EvidenciaEvaluacion> evidencias;
    
    /**
     * Evidencias de evaluación seleccionada
     */
    @Getter @NonNull private EvidenciaEvaluacion evidencia;
    
    /**
     * Lista de instrumentos de evaluación
     */
    @Getter @NonNull private List<InstrumentoEvaluacion> instrumentos;
    
     /**
     * Instrumento de evaluación seleccionado
     */
    @Getter @NonNull private InstrumentoEvaluacion instrumento;
    
      /**
     * Representa valor de la meta del instrumento de evaluación
     */
    @Getter @NonNull private Integer metaInstrumento;
    
    
   
    public AsignacionIndicadoresCriteriosRolDocente(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.docente = docente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
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

    public void setListaAsignacionCargaAcademica(List<DtoAsignadosIndicadoresCriterios> listaAsignacionCargaAcademica) {
        this.listaAsignacionCargaAcademica = listaAsignacionCargaAcademica;
    }

    public void setOpcAsignacion(String opcAsignacion) {
        this.opcAsignacion = opcAsignacion;
    }

    public void setListaUnidadMateriaConfiguracion(List<UnidadMateriaConfiguracion> listaUnidadMateriaConfiguracion) {
        this.listaUnidadMateriaConfiguracion = listaUnidadMateriaConfiguracion;
    }
    
    public void setDirectorValido(Integer directorValido) {
        this.directorValido = directorValido;
    }

    public void setListaEvidenciasSugeridas(List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas) {
        this.listaEvidenciasSugeridas = listaEvidenciasSugeridas;
    }

    public void setListaEvidenciasInstrumentos(List<UnidadMateriaConfiguracionEvidenciaInstrumento> listaEvidenciasInstrumentos) {
        this.listaEvidenciasInstrumentos = listaEvidenciasInstrumentos;
    }

    public void setAgregarEvidencia(Boolean agregarEvidencia) {
        this.agregarEvidencia = agregarEvidencia;
    }

    public void setValorAgregarEvid(String valorAgregarEvid) {
        this.valorAgregarEvid = valorAgregarEvid;
    }
    
    public void setTipoAgregarEvid(String tipoAgregarEvid) {
        this.tipoAgregarEvid = tipoAgregarEvid;
    }

    public void setCategorias(List<Criterio> categorias) {
        this.categorias = categorias;
    }

    public void setCategoria(Criterio categoria) {
        this.categoria = categoria;
    }
    
    public void setEvidencias(List<EvidenciaEvaluacion> evidencias) {
        this.evidencias = evidencias;
    }

    public void setEvidencia(EvidenciaEvaluacion evidencia) {
        this.evidencia = evidencia;
    }

    public void setInstrumentos(List<InstrumentoEvaluacion> instrumentos) {
        this.instrumentos = instrumentos;
    }

    public void setInstrumento(InstrumentoEvaluacion instrumento) {
        this.instrumento = instrumento;
    }

    public void setMetaInstrumento(Integer metaInstrumento) {
        this.metaInstrumento = metaInstrumento;
    }

}
