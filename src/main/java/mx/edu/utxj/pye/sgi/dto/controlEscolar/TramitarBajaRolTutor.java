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
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
/**
 *
 * @author UTXJ
 */
public class TramitarBajaRolTutor extends AbstractRol {
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo tutor;
    
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
    /**
     * Lista grupos tutorados
     */
    @Getter @NonNull private List<DtoListadoTutores> grupos;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    
    /**
     * Grupo tutorado seleccionado
     */
    @Getter @NonNull private DtoListadoTutores grupo;
    
   /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
    
     /**
     * Lista de estudiantes
     */
    @Getter @NonNull private List<DtoTramitarBajas> estudiantesGrupo;
    
     /**
     * Lista de estudiantes
     */
    @Getter @NonNull private DtoTramitarBajas estudiante;
    
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
     * Baja registrada
     */
    @Getter @NonNull private Baja bajaRegistrada;
    
     /**
     * Información completa de la Baja registrada
     */
    @Getter @NonNull private DtoRegistroBajaEstudiante registroBajaEstudiante;
    
     /**
     * Lista de bajas tramitadas
     */
    @Getter @NonNull private List<DtoRegistroBajaEstudiante> registrosBajaEstudiante;
    
     /**
     * Lista de materias reprobadas registradas en caso de ser baja por reprobación
     */
    @Getter @NonNull private List<DtoMateriaReprobada> listaMateriasReprobadas;
    
     /**
     * Valor modal con materias reprobadas
     */
    @Getter @NonNull private Boolean forzarAperturaDialogo;
    
      /**
     * Indica si el docente ha sido tutor
     */
    @Getter @NonNull private Boolean esTutor;
    
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
     * Existe o no registro de baja del estudiante
     */
    @Getter @NonNull private Boolean existeRegistroBaja;
    
     /**
     * Existe o no registro de baja del estudiante
     */
    @Getter @NonNull private String accionesTutor;
   
    public TramitarBajaRolTutor(Filter<PersonalActivo> filtro, PersonalActivo tutor) {
        super(filtro);
        this.tutor = tutor;
    }

    public void setTutor(PersonalActivo tutor) {
        this.tutor = tutor;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setGrupos(List<DtoListadoTutores> grupos) {
        this.grupos = grupos;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setGrupo(DtoListadoTutores grupo) {
        this.grupo = grupo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setEstudiantesGrupo(List<DtoTramitarBajas> estudiantesGrupo) {
        this.estudiantesGrupo = estudiantesGrupo;
    }

    public void setEstudiante(DtoTramitarBajas estudiante) {
        this.estudiante = estudiante;
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

    public void setBajaRegistrada(Baja bajaRegistrada) {
        this.bajaRegistrada = bajaRegistrada;
    }

    public void setRegistroBajaEstudiante(DtoRegistroBajaEstudiante registroBajaEstudiante) {
        this.registroBajaEstudiante = registroBajaEstudiante;
    }

    public void setRegistrosBajaEstudiante(List<DtoRegistroBajaEstudiante> registrosBajaEstudiante) {
        this.registrosBajaEstudiante = registrosBajaEstudiante;
    }

    public void setListaMateriasReprobadas(List<DtoMateriaReprobada> listaMateriasReprobadas) {
        this.listaMateriasReprobadas = listaMateriasReprobadas;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }

    public void setEsTutor(Boolean esTutor) {
        this.esTutor = esTutor;
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

    public void setExisteRegistroBaja(Boolean existeRegistroBaja) {
        this.existeRegistroBaja = existeRegistroBaja;
    }

    public void setAccionesTutor(String accionesTutor) {
        this.accionesTutor = accionesTutor;
    }
}
