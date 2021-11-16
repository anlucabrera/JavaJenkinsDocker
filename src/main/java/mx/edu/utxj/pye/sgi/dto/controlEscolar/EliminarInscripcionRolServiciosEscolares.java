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

/**
 *
 * @author UTXJ
 */
public class EliminarInscripcionRolServiciosEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoDatosEstudiante datosEstudiante;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Representa la lista de tipos de registros que tiene el estudiante seleccionado
     */
    @Getter @NonNull private List<DtoEliminarRegistrosEstIns> registrosEstudiante;
    
      /**
     * Representa el valor si existen o no registros del estudiante
     */
    @Getter @NonNull private Boolean existenRegistros;
    
    
    public EliminarInscripcionRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setDatosEstudiante(DtoDatosEstudiante datosEstudiante) {
        this.datosEstudiante = datosEstudiante;
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setRegistrosEstudiante(List<DtoEliminarRegistrosEstIns> registrosEstudiante) {
        this.registrosEstudiante = registrosEstudiante;
    }

    public void setExistenRegistros(Boolean existenRegistros) {
        this.existenRegistros = existenRegistros;
    }
}
