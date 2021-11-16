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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;

/**
 *
 * @author UTXJ
 */
public class SeguimientoExpedienteGeneracionRolTitulacion extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de la coordinación de titulación
     */
    @Getter @NonNull private PersonalActivo personal;
    
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Lista niveles educativos
     */
    @Getter @NonNull private List<String> nivelesEducativos;
    
    /**
     * Lista programas educativos
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
    /**
     * Lista expedientes registrados en la generación seleccionada
     */
    @Getter @NonNull private List<DtoExpedienteTitulacion> expedientesTitulacion;
    
     /**
     * Lista expedientes registrados en la generación y programa educativo seleccionado
     */
    @Getter @NonNull private List<DtoExpedienteTitulacion> expedientesTitulacionPE;
    
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private String nivelEducativo;
    
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;
    
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
    
    
    public SeguimientoExpedienteGeneracionRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }
    
    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setNivelesEducativos(List<String> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setExpedientesTitulacion(List<DtoExpedienteTitulacion> expedientesTitulacion) {
        this.expedientesTitulacion = expedientesTitulacion;
    }

    public void setExpedientesTitulacionPE(List<DtoExpedienteTitulacion> expedientesTitulacionPE) {
        this.expedientesTitulacionPE = expedientesTitulacionPE;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
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
