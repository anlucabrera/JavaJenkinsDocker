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

/**
 *
 * @author UTXJ
 */
public class RegistroDocumentosOficialesRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo usuario;
    
    /**
     * Clave del periodo activo
     */
    @Getter private Integer  periodoActivo;
    
     /**
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
     /**
     * Generación del estudiante
     */
    @Getter @NonNull private String generacion;
    
    public RegistroDocumentosOficialesRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }
     
    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }
    
}
