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
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;

/**
 *
 * @author UTXJ
 */
public class EntregaFotografiasRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo usuario;
    
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
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Estudiante registrado
     */
    @Getter @NonNull private DtoDatosEstudiante estudianteRegistrado;
    
    /**
     * Estudiante entrega fotografias
     */
    @Getter @NonNull private DtoEntregaFotografiasEstadia estudianteFotografias;
    
    /**
     * Estudiantes entregaron fotografias
     */
    @Getter @NonNull private List<DtoEntregaFotografiasEstadia> estudiantesFotografias;
    
    
    /**
     * Habilita o deshabilita el botón para registrar entrega
     */
    @Getter @NonNull private Boolean desactivarRegistro;
    
    /**
     * Porcentaje de entrega por programa educativo de la generación y nivel seleccionado
     */
    @Getter @NonNull private List<DtoPorcentajeEntregaFotografias> porcentajesEntrega;
    
     /**
     * Número total de estudiantes
     */
    @Getter @NonNull private Integer totalEstudiantes;
    
     /**
     * Número total de estudiantes que entregaron fotografías
     */
    @Getter @NonNull private Integer totalEntregaronFotografias;
    
     /**
     * Número total de estudiantes pendientes de entregar fotografías
     */
    @Getter @NonNull private Integer totalPendienteEntrega;
    
     /**
     * Número total de porcentaje de entrega
     */
    @Getter @NonNull private String totalPorcentajeEntrega;
      
    public EntregaFotografiasRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
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

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setEstudianteRegistrado(DtoDatosEstudiante estudianteRegistrado) {
        this.estudianteRegistrado = estudianteRegistrado;
    }
    
    public void setEstudianteFotografias(DtoEntregaFotografiasEstadia estudianteFotografias) {
        this.estudianteFotografias = estudianteFotografias;
    }

    public void setEstudiantesFotografias(List<DtoEntregaFotografiasEstadia> estudiantesFotografias) {
        this.estudiantesFotografias = estudiantesFotografias;
    }

    public void setDesactivarRegistro(Boolean desactivarRegistro) {
        this.desactivarRegistro = desactivarRegistro;
    }

    public void setPorcentajesEntrega(List<DtoPorcentajeEntregaFotografias> porcentajesEntrega) {
        this.porcentajesEntrega = porcentajesEntrega;
    }

    public void setTotalEstudiantes(Integer totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public void setTotalEntregaronFotografias(Integer totalEntregaronFotografias) {
        this.totalEntregaronFotografias = totalEntregaronFotografias;
    }

    public void setTotalPendienteEntrega(Integer totalPendienteEntrega) {
        this.totalPendienteEntrega = totalPendienteEntrega;
    }

    public void setTotalPorcentajeEntrega(String totalPorcentajeEntrega) {
        this.totalPorcentajeEntrega = totalPorcentajeEntrega;
    }
    
}

