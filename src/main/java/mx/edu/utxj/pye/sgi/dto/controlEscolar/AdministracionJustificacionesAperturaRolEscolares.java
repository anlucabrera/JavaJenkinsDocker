/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.JustificacionPermisosExtemporaneos;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionJustificacionesAperturaRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
     /**
     * Habilitar o deshabilitar opciones para agregar una justificación
     */
    @Getter @NonNull private Boolean agregarJustificacion;
    
    /**
     * Lista de generaciones
     */
    @Getter @NonNull private List<JustificacionPermisosExtemporaneos> justificaciones;
    
    /**
     * Descripción de la justificación a registrar
     */
    @Getter private String descripcion; 
    
    
    public AdministracionJustificacionesAperturaRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setAgregarJustificacion(Boolean agregarJustificacion) {
        this.agregarJustificacion = agregarJustificacion;
    }

    public void setJustificaciones(List<JustificacionPermisosExtemporaneos> justificaciones) {
        this.justificaciones = justificaciones;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        if(descripcion == null){
            this.setDescripcion("Ingresar descripción");
        }
    }
    
    
    
}
