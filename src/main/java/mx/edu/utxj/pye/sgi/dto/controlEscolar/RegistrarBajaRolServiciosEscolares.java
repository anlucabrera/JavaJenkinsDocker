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

import java.util.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
/**
 *
 * @author UTXJ
 */
public class RegistrarBajaRolServiciosEscolares extends AbstractRol {
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoDatosEstudiante datosEstudiante;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Lista de tipos de baja
     */
    @Getter @NonNull private List<BajasTipo> tiposBaja;
    
     /**
     * Tipo de baja seleccionada
     */
    @Getter @NonNull private BajasTipo tipoBaja;
    
     /**
     * Lista de causas baja
     */
    @Getter @NonNull private List<BajasCausa> causasBaja;
    
     /**
     * Causa de baja seleccionada
     */
    @Getter @NonNull private BajasCausa causaBaja;
    
     /**
     * Fecha de registro de baja
     */
    @Getter @NonNull private Date fechaBaja;
    
     /**
     * Fecha fin periodo escolar
     */
    @Getter @NonNull private DtoRangoFechasPermiso rangoFechas;
    
    /**
     * Fecha inicio periodo escolar
     */
    @Getter @NonNull private Date fechaInicio;
         
    /**
     * Fecha fin periodo escolar
     */
    @Getter @NonNull private Date fechaFin;
        
    /**
     * Baja registrada
     */
    @Getter @NonNull private Baja bajaRegistrada;
    
     /**
     * Información completa de la Baja registrada
     */
    @Getter @NonNull private DtoRegistroBajaEstudiante registroBajaEstudiante;
    
     /**
     * Lista de materias reprobadas registradas en caso de ser baja por reprobación
     */
    @Getter @NonNull private List<DtoMateriaReprobada> listaMateriasReprobadas;
    
     /**
     * Valor modal con materias reprobadas
     */
    @Getter @NonNull private Boolean forzarAperturaDialogo;
    
     /**
     * Información para generar formato de baja del estudiante
     */
    @Getter @NonNull private DtoFormatoBaja formatoBaja;
    
     /**
     * Información para generar formato de baja del estudiante
     */
    @Getter @NonNull private Date fechaImpresion;
   
     /**
     * Estudiante actualizado
     */
    @Getter @NonNull private Estudiante estudianteActualizado;
    
      /**
     * Representa valor para definir si se deshabilita o no el botón para registrar baja
     */
    @Getter @NonNull private Boolean deshabilitarRegistro;
    
    
      /**
     * Representa valor para definir si se deshabilita o no el botón para eliminar baja
     */
    @Getter @NonNull private Boolean deshabilitarEliminar;
    
    public RegistrarBajaRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }


    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setDatosEstudiante(DtoDatosEstudiante datosEstudiante) {
        this.datosEstudiante = datosEstudiante;
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setTiposBaja(List<BajasTipo> tiposBaja) {
        this.tiposBaja = tiposBaja;
    }

    public void setTipoBaja(BajasTipo tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    public void setCausasBaja(List<BajasCausa> causasBaja) {
        this.causasBaja = causasBaja;
    }

    public void setCausaBaja(BajasCausa causaBaja) {
        this.causaBaja = causaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public void setRangoFechas(DtoRangoFechasPermiso rangoFechas) {
        this.rangoFechas = rangoFechas;
    }
    
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setBajaRegistrada(Baja bajaRegistrada) {
        this.bajaRegistrada = bajaRegistrada;
    }

    public void setRegistroBajaEstudiante(DtoRegistroBajaEstudiante registroBajaEstudiante) {
        this.registroBajaEstudiante = registroBajaEstudiante;
    }

    public void setListaMateriasReprobadas(List<DtoMateriaReprobada> listaMateriasReprobadas) {
        this.listaMateriasReprobadas = listaMateriasReprobadas;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }

    public void setFormatoBaja(DtoFormatoBaja formatoBaja) {
        this.formatoBaja = formatoBaja;
    }

    public void setFechaImpresion(Date fechaImpresion) {
        this.fechaImpresion = fechaImpresion;
    }

    public void setEstudianteActualizado(Estudiante estudianteActualizado) {
        this.estudianteActualizado = estudianteActualizado;
    }

    public void setDeshabilitarRegistro(Boolean deshabilitarRegistro) {
        this.deshabilitarRegistro = deshabilitarRegistro;
    }

    public void setDeshabilitarEliminar(Boolean deshabilitarEliminar) {
        this.deshabilitarEliminar = deshabilitarEliminar;
    }
}
