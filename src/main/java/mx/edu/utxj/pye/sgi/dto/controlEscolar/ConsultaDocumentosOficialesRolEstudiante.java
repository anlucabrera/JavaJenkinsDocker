/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.util.List;
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
     * Lista de instrucciones para utilizar el módulo
     */
    @Getter private List<String> instrucciones;
    
    /**
     * Representa la referencia al estudiante logueado
     */
    @Getter @NonNull private DtoEstudianteComplete informacionEstudiante;
    
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

    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }
}
