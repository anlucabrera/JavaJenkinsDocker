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
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionCatEvidInstEvalRolEscolares extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;

    /**
     * Niveles educativos para seleccionar
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Lista de categorías y evidencias de evaluación del nivel educativo seleccionado
     */
    @Getter @NonNull private List<EvidenciaEvaluacion> listaCategoriasEvidencias;
    
     /**
     * Habilitar o deshabilitar opciones para agregar evidencia a la categoría
     */
    @Getter @NonNull private Boolean agregarEvidenciaCategoria;
    
     /**
     * Habilitar o deshabilitar opciones para agregar instrumento de evaluación
     */
    @Getter @NonNull private Boolean agregarInstrumentoEvaluacion;
    
    /**
     * Categoría seleccionada
     */
    @Getter @NonNull private Criterio categoria;

    /**
     * Lista de categorías
     */
    @Getter @NonNull private List<Criterio> categorias;
    
    /**
     * Evidencia seleccionada
     */
    @Getter @NonNull private String evidenciaSeleccionada;

    /**
     * Lista de evidencias
     */
    @Getter @NonNull private List<String> evidencias;  
    
     /**
     * Nueva evidencia de evaluación
     */
    @Getter private String nuevaEvidencia; 
    
    /**
     * Lista de instrumentos de evaluación
     */
    @Getter @NonNull private List<InstrumentoEvaluacion> instrumentosEvaluacion; 
    
    /**
     * Nuevo instrumento de evaluación
     */
    @Getter private String nuevoInstrumento; 
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionCatEvidInstEvalRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }
    
    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }
    
    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setListaCategoriasEvidencias(List<EvidenciaEvaluacion> listaCategoriasEvidencias) {
        this.listaCategoriasEvidencias = listaCategoriasEvidencias;
    }
    
    public void setAgregarEvidenciaCategoria(Boolean agregarEvidenciaCategoria) {
        this.agregarEvidenciaCategoria = agregarEvidenciaCategoria;
    }

    public void setAgregarInstrumentoEvaluacion(Boolean agregarInstrumentoEvaluacion) {
        this.agregarInstrumentoEvaluacion = agregarInstrumentoEvaluacion;
    }
    
    public void setCategoria(Criterio categoria) {
        this.categoria = categoria;
    }

    public void setCategorias(List<Criterio> categorias) {
        this.categorias = categorias;
    }

    public void setEvidenciaSeleccionada(String evidenciaSeleccionada) {
        this.evidenciaSeleccionada = evidenciaSeleccionada;
    }

    public void setEvidencias(List<String> evidencias) {
        this.evidencias = evidencias;
    }

    public void setNuevaEvidencia(String nuevaEvidencia) {
        this.nuevaEvidencia = nuevaEvidencia;
        if(nuevaEvidencia == null){
            this.setNuevaEvidencia("Ingresar nombre");
        }
    }

    public void setInstrumentosEvaluacion(List<InstrumentoEvaluacion> instrumentosEvaluacion) {
        this.instrumentosEvaluacion = instrumentosEvaluacion;
    }

    public void setNuevoInstrumento(String nuevoInstrumento) {
        this.nuevoInstrumento = nuevoInstrumento;
        if(nuevoInstrumento == null){
            this.setNuevoInstrumento("Ingresar nombre");
        }
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
    
}
