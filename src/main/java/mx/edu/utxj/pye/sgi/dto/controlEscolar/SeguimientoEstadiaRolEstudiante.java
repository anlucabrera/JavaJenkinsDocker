/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import java.util.*;
import lombok.Setter;

import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class SeguimientoEstadiaRolEstudiante{
    
    /**
     * Representa la referencia hacia al estudiante con seguimiento de estadía
     */
    @Getter @NonNull private DtoEstudiante  dtoEstudiante;
  
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
     * Representa los datos de seguimiento de estadía del estudiante
     */
    @Getter @NonNull private DtoSeguimientoEstadiaEstudiante  dtoSeguimientoEstadiaEstudiante;
    
     /**
     * Representa el nivel del rol
     */
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;

     /**
     * Lista de intruccions para utilizar el módulo
     */
    @Getter private List<String> instrucciones = new ArrayList<>();
    
    public Boolean tieneAcceso(DtoEstudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }

    public void setDtoEstudiante(DtoEstudiante dtoEstudiante) {
        this.dtoEstudiante = dtoEstudiante;
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

    public void setDtoSeguimientoEstadiaEstudiante(DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante) {
        this.dtoSeguimientoEstadiaEstudiante = dtoSeguimientoEstadiaEstudiante;
    }

    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }

    
}
