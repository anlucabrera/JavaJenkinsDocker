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

import java.util.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;

/**
 *
 * @author UTXJ
 */
public class SeguimientoEstadiaRolAsesor extends AbstractRol{
    /**
     * Representa la referencia hacia al asesor academico
     */
    @Getter @NonNull private PersonalActivo docente;
  
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
   
    /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Lista de estudiantes que tiene asignados par dar seguimiento
     */
    @Getter @NonNull private List<DtoSeguimientoEstadia> estudiantesSeguimiento;
    
     /**
     * Valor que determina si el personal tiene rol de asesor para el evento activo
     */
    @Getter @NonNull private Boolean rolAsesorActivo;
    
     /**
     * Seguimiento de un estudiante
     */
    @Getter @NonNull private DtoSeguimientoEstadia estudianteSeguimiento;
    
     /**
     * Representa el valor del autocomplete para buscar la empresa
     */
    @Getter @NonNull private DtoEmpresaComplete empresaSeleccionada;
    
     /**
     * Represente el valor para abrir o cerrar la ventana de registro de información de estadía
     */
    @Getter @NonNull private Boolean aperturaDialogoRegistroInfEst;
    
     /**
     * Represente el valor para abrir o cerrar la ventana de edición de información de estadía
     */
    @Getter @NonNull private Boolean aperturaDialogoEdicionInfEst;
    
     /**
     * Represente el valor para abrir o cerrar la ventana de edición de evaluación de estadía
     */
    @Getter @NonNull private Boolean aperturaDialogoEvalEst;
    
     /**
     * Represente el valor del input switch para habilitar la búsqueda de la empresa
     */
    @Getter @NonNull private Boolean editarEmpresa;
    
      /**
     * Número de semanas entre fecha de inicio y fin de la estadía
     */
    @Getter private Double semanasEstadia;
    
      /**
     * Representa el asesor empresarial de estadía
     */
    @Getter private AsesorEmpresarialEstadia asesorEmpresarialEstadia;
    
      /**
     * Lista de criterios de evaluación de estadía registrados
     */
    @Getter @NonNull private List<CalificacionCriterioEstadia> listaEvaluacionEstadiaRegistrada;
    
      /**
     * Lista de criterios de evaluación de estadía para registrar
     */
    @Getter @NonNull private List<DtoEvaluacionEstadiaEstudiante> listaEvaluacionEstadia;

      /**
     * Promedio evaluación proceso de estadía por asesor interno
     */
    @Getter @NonNull private Double promedioAsesorInterno;
    
      /**
     * Promedio evaluación proceso de estadía por asesor externo
     */
    @Getter @NonNull private Double promedioAsesorExterno;
    
      /**
     * Represente el valor para habilitar o deshabilitar el botón de guardar
     */
    @Getter @NonNull private Boolean habilitarGuardar;
   
     /**
     * Represente el valor si el estudiante tiene evaluación de estadía registarda
     */
    @Getter @NonNull private Boolean evaluacionRegistrada;
    
    public SeguimientoEstadiaRolAsesor(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.docente = docente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setEstudiantesSeguimiento(List<DtoSeguimientoEstadia> estudiantesSeguimiento) {
        this.estudiantesSeguimiento = estudiantesSeguimiento;
    }

    public void setRolAsesorActivo(Boolean rolAsesorActivo) {
        this.rolAsesorActivo = rolAsesorActivo;
    }
    
    public void setEstudianteSeguimiento(DtoSeguimientoEstadia estudianteSeguimiento) {
        this.estudianteSeguimiento = estudianteSeguimiento;
    }
    
     public void setEmpresaSeleccionada(DtoEmpresaComplete empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public void setAperturaDialogoRegistroInfEst(Boolean aperturaDialogoRegistroInfEst) {
        this.aperturaDialogoRegistroInfEst = aperturaDialogoRegistroInfEst;
    }
    
    public void setAperturaDialogoEdicionInfEst(Boolean aperturaDialogoEdicionInfEst) {
        this.aperturaDialogoEdicionInfEst = aperturaDialogoEdicionInfEst;
    }

    public void setAperturaDialogoEvalEst(Boolean aperturaDialogoEvalEst) {
        this.aperturaDialogoEvalEst = aperturaDialogoEvalEst;
    }
    
    public void setEditarEmpresa(Boolean editarEmpresa) {
        this.editarEmpresa = editarEmpresa;
    }

    public void setSemanasEstadia(Double semanasEstadia) {
        this.semanasEstadia = semanasEstadia;
    }

    public void setAsesorEmpresarialEstadia(AsesorEmpresarialEstadia asesorEmpresarialEstadia) {
        this.asesorEmpresarialEstadia = asesorEmpresarialEstadia;
    }
    
    public void setListaEvaluacionEstadiaRegistrada(List<CalificacionCriterioEstadia> listaEvaluacionEstadiaRegistrada) {
        this.listaEvaluacionEstadiaRegistrada = listaEvaluacionEstadiaRegistrada;
    }

    public void setListaEvaluacionEstadia(List<DtoEvaluacionEstadiaEstudiante> listaEvaluacionEstadia) {
        this.listaEvaluacionEstadia = listaEvaluacionEstadia;
    }

    public void setPromedioAsesorInterno(Double promedioAsesorInterno) {
        this.promedioAsesorInterno = promedioAsesorInterno;
    }

    public void setPromedioAsesorExterno(Double promedioAsesorExterno) {
        this.promedioAsesorExterno = promedioAsesorExterno;
    }
    
    public void setHabilitarGuardar(Boolean habilitarGuardar) {
        this.habilitarGuardar = habilitarGuardar;
    }

    public void setEvaluacionRegistrada(Boolean evaluacionRegistrada) {
        this.evaluacionRegistrada = evaluacionRegistrada;
    }
    
}
