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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NoAdeudoEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.Date;
import java.util.List;

public class CartaNoAdeudoRolEstudiante {

    @Getter @Setter @NonNull Boolean tieneAcceso = false;

    /**
     * Representa la referencia al estudiante logueado
     */
    @Getter @NonNull private Estudiante estudiante;
    /**
     * Representa la referencia al rol que hace uso del modulo
     */
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;

    @Getter @Setter @NonNull private DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral cartaGeneral;
   
    @Getter  @Setter @NonNull private List<NoAdeudoEstudiante> cartaNoAdeudo;
    @Getter  @Setter @NonNull private Date fechaImpresion;
    @Getter @Setter @NonNull private  String labelDireccion, classDireccion,labelBiblioteca, classBiblioteca, labelIye, classIye, labelEstadia,classEstadia,labelEgresados,classEgresados,
                                             labelRMateriales,classRMateriales,labelEscolares,classEscolares,labelTitulacion,classTitulacion,labelFinanzas,classFinanzas;

    /**
     * Generación del estudiante
     */
    @Getter @NonNull private Generaciones generacion;
    
    public Boolean tieneAccesoEs(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        setEstudiante(estudiante);
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante= estudiante;
    }


    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }
}
