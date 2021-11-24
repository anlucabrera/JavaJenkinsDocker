/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class RegistroEvidInstEvalMateriasRolDirector extends AbstractRol{
    
    /**
     * Representa la referencia hacia el director o directora de carrera
    */
    @Getter @NonNull private PersonalActivo director;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
    /**
     * Programa educativo seleccionado para registro
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;

    /**
     * Programas educativos para seleccionar
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
    /**
     * Plan de estudio seleccionado
     */
    @Getter @NonNull private PlanEstudio planEstudio;

    /**
     * Planes de estudio para seleccionar
     */
    @Getter @NonNull private List<PlanEstudio> planesEstudio;
    
     /**
     * Cuatrimestre seleccionado
     */
    @Getter @NonNull private Integer cuatrimestre ;

    /**
     * Cuatrimestre para filtrar búsqueda
     */
    @Getter @NonNull private List<Integer> cuatrimestres;
    
    /**
     * Tipo de búsqueda_ general o por grado
     */
    @Getter @NonNull private String tipoBusqueda ;
    
     /**
     * Lista de evidencias e instrumentos de evaluación sugeridos
     */
    @Getter private List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasInstrumentos;
    
    /**
     * Lista previa de evidencias e instrumentos de evaluación por registrar
     */
     @Getter private List<DtoRegistroPrevioEvidInstEval> listaPreviaEvidenciasInstrumentos;
     
      /**
     * Representa la ruta del archivo que se cargará a sistema
     */
    @Getter @NonNull private String rutaArchivo;
    
     /**
     * Lista registro guardados de evidencias e instrumentos de evaluación
     */
     @Getter private List<EvaluacionSugerida> listaEvaluacioneGuardadas;
    
     /**
     * Representa si se desea agregar más evidencias e instrumentos de evaluación
     */
    @Getter @NonNull private Boolean agregarEvidencia;
    
     /**
     * Representa si se agregará de manera masiva o individual la evidencia e instrumento de evaluación
     */
    @Getter @NonNull private String tipoAgregarEvid;
    
     /**
     * Lista de grados
     */
    @Getter @NonNull private List<Integer> grados;
    
     /**
     * Grado seleccionado
     */
    @Getter @NonNull private Integer grado;
    
    /**
     * Lista de materias
     */
    @Getter @NonNull private List<Materia> materias;
    
     /**
     * Materia seleccionada
     */
    @Getter @NonNull private Materia materia;
    
     /**
     * Lista de unidades de la materia seleccionada
     */
    @Getter @NonNull private List<UnidadMateria> unidadMaterias;
    
     /**
     * Unidad seleccionada
     */
    @Getter @NonNull private UnidadMateria unidadMateria;
    
   
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
    
    /**
     * Lista de registros con criterios incompletos 
     */
    @Getter private List<String> registrosIncompletos;
    
    public RegistroEvidInstEvalMateriasRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director) {
        super(filtro);
        this.director = director;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    public void setPlanesEstudio(List<PlanEstudio> planesEstudio) {
        this.planesEstudio = planesEstudio;
        if(planesEstudio != null && !planesEstudio.isEmpty()){
            this.setPlanEstudio(planesEstudio.get(0));
        }
    }

    public void setCuatrimestre(Integer cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public void setCuatrimestres(List<Integer> cuatrimestres) {
        this.cuatrimestres = cuatrimestres;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }
    
    public void setListaEvidenciasInstrumentos(List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasInstrumentos) {
        this.listaEvidenciasInstrumentos = listaEvidenciasInstrumentos;
    }

    public void setListaPreviaEvidenciasInstrumentos(List<DtoRegistroPrevioEvidInstEval> listaPreviaEvidenciasInstrumentos) {
        this.listaPreviaEvidenciasInstrumentos = listaPreviaEvidenciasInstrumentos;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void setListaEvaluacioneGuardadas(List<EvaluacionSugerida> listaEvaluacioneGuardadas) {
        this.listaEvaluacioneGuardadas = listaEvaluacioneGuardadas;
    }
        
    public void setAgregarEvidencia(Boolean agregarEvidencia) {
        this.agregarEvidencia = agregarEvidencia;
    }

    public void setTipoAgregarEvid(String tipoAgregarEvid) {
        this.tipoAgregarEvid = tipoAgregarEvid;
    }

    public void setGrados(List<Integer> grados) {
        this.grados = grados;
    }

    public void setGrado(Integer grado) {
        this.grado = grado;
    }
    
    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public void setUnidadMaterias(List<UnidadMateria> unidadMaterias) {
        this.unidadMaterias = unidadMaterias;
    }

    public void setUnidadMateria(UnidadMateria unidadMateria) {
        this.unidadMateria = unidadMateria;
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

    public void setRegistrosIncompletos(List<String> registrosIncompletos) {
        this.registrosIncompletos = registrosIncompletos;
    }
}
