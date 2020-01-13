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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;

/**
 *
 * @author UTXJ
 */
public class HistorialMovEstRolServiciosEscolares extends AbstractRol {
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Datos Persona
     */
    @Getter @NonNull private Persona persona;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Lista de tipos de baja
     */
    @Getter @NonNull private List<DtoHistorialMovEstudiante> listaHistorialMovEst;
    
    
    public HistorialMovEstRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setListaHistorialMovEst(List<DtoHistorialMovEstudiante> listaHistorialMovEst) {
        this.listaHistorialMovEst = listaHistorialMovEst;
    }

    
}
