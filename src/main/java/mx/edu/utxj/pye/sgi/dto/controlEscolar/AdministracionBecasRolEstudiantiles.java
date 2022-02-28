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
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionBecasRolEstudiantiles extends AbstractRol{
     /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
    /**
     * Lista de tipos de beca registradas
     */
    @Getter @NonNull private List<BecaTipos> tiposBeca;

     /**
     * Habilitar o deshabilitar opciones para agregar tipo de beca
     */
    @Getter @NonNull private Boolean agregarTipoBeca;
    
    /**
     * Nuevo tipo de baja
     */
    @Getter @NonNull private String nuevoTipoBeca;

    public AdministracionBecasRolEstudiantiles(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setTiposBeca(List<BecaTipos> tiposBeca) {
        this.tiposBeca = tiposBeca;
    }

    public void setAgregarTipoBeca(Boolean agregarTipoBeca) {
        this.agregarTipoBeca = agregarTipoBeca;
    }

    public void setNuevoTipoBeca(String nuevoTipoBeca) {
        this.nuevoTipoBeca = nuevoTipoBeca;
        if(nuevoTipoBeca == null){
            this.setNuevoTipoBeca("Ingresar nombre");
        }
    }
    
}
