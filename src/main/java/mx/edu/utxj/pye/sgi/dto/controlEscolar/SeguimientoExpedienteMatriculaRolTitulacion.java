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
public class SeguimientoExpedienteMatriculaRolTitulacion extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de la coordinación de titulación
     */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete dtoEstudianteComplete;
    
    /**
     * Lista expedientes registrados en la generación seleccionada
     */
    @Getter @NonNull private List<DtoExpedienteTitulacion> expedientesTitulacion;
    
    /**
     * Expediente seleccionado
     */
    @Getter @NonNull private DtoExpedienteTitulacion dtoExpedienteTitulacion;
    
     /**
     * Representa la clave del periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
  
     /**
     * Lista de pagos del estudiante
     */
    @Getter @NonNull private List<DtoPagosEstudianteFinanzas> listaPagosEstudianteFinanzas;
    
     /**
     * Valor modal del concentrado de pagos
     */
    @Getter @NonNull private Boolean aperturaPagos;
    
     /**
     * Lista de pasos registro
     */
    @Getter @NonNull private List<String> pasosRegistro;
    
    public SeguimientoExpedienteMatriculaRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }
    
    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setDtoEstudianteComplete(DtoEstudianteComplete dtoEstudianteComplete) {
        this.dtoEstudianteComplete = dtoEstudianteComplete;
    }
    
    public void setExpedientesTitulacion(List<DtoExpedienteTitulacion> expedientesTitulacion) {
        this.expedientesTitulacion = expedientesTitulacion;
    }

    public void setDtoExpedienteTitulacion(DtoExpedienteTitulacion dtoExpedienteTitulacion) {
        this.dtoExpedienteTitulacion = dtoExpedienteTitulacion;
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setListaPagosEstudianteFinanzas(List<DtoPagosEstudianteFinanzas> listaPagosEstudianteFinanzas) {
        this.listaPagosEstudianteFinanzas = listaPagosEstudianteFinanzas;
    }

    public void setAperturaPagos(Boolean aperturaPagos) {
        this.aperturaPagos = aperturaPagos;
    }

    public void setPasosRegistro(List<String> pasosRegistro) {
        this.pasosRegistro = pasosRegistro;
    }
    
}

