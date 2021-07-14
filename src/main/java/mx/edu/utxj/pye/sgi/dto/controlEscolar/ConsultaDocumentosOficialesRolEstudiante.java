/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class ConsultaDocumentosOficialesRolEstudiante {
    /**
     * Representa la referencia al estudiante logueado
     */
    @Getter @NonNull private Estudiante estudiante;
    /**
     * Representa la referencia al rol que hace uso del modulo
     */
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    
    /**
     * Clave del periodo activo
     */
    @Getter private Integer  periodoActivo;
   
    /**
     * Representa la referencia al estudiante logueado
     */
    @Getter @NonNull private DtoEstudianteComplete informacionEstudiante;
    
     /**
     * Generación del estudiante
     */
    @Getter @NonNull private String generacion;
    
    public Boolean tieneAccesoEs(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        setEstudiante(estudiante);
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante= estudiante;
    }

    public void setInformacionEstudiante(DtoEstudianteComplete informacionEstudiante) {
        this.informacionEstudiante = informacionEstudiante;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }
}
