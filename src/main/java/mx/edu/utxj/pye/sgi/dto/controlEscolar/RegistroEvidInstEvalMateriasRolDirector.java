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
     * Programa educativo seleccionado para registro
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;

    /**
     * Programas educativos para seleccionar
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;

    /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoEscolar;

    /**
     * Periodos escolares para seleccionar
     */
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    
     /**
     * Plan de estudio al que corresponde las materias registradas 
     */
    @Getter private PlanEstudio planEstudioRegistrado;

     /**
     * Lista de evidencias e instrumentos de evaluación sugeridos
     */
    @Getter private List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasInstrumentos;
    
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
    
    
   
    public RegistroEvidInstEvalMateriasRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director) {
        super(filtro);
        this.director = director;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setPeriodoEscolar(PeriodosEscolares periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    /**
     * Sincroniza el periodo seleccionado al primer periodo en la lista
     * @param periodosEscolares
     */
    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
        if(periodosEscolares != null && !periodosEscolares.isEmpty()){
            this.setPeriodoEscolar(periodosEscolares.get(0));
        }
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setPlanEstudioRegistrado(PlanEstudio planEstudioRegistrado) {
        this.planEstudioRegistrado = planEstudioRegistrado;
    }
    
    public void setListaEvidenciasInstrumentos(List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasInstrumentos) {
        this.listaEvidenciasInstrumentos = listaEvidenciasInstrumentos;
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
}
