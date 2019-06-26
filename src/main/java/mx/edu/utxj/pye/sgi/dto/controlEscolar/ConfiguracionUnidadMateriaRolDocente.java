package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;

import java.util.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;

public class ConfiguracionUnidadMateriaRolDocente extends AbstractRol {

    /**
     * Representa la referencia hacia el personal docente
     */
    @Getter @NonNull private PersonalActivo docente;

    /**
     * Representa la referencia al evento activo de asignación academica
     */
    @Getter @NonNull private EventoEscolar eventoActivo;
    
    /**
     * Periodo seleccionado en el que se realizará la asignación
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
   
    /**
     * Carga académica seleccionada
     */
    @Getter @NonNull private DtoCargaAcademica carga;
  
    /**
     * Unidades materia seleccionada y que se va a configurar
     */
    @Getter @NonNull private UnidadMateria unidadMateria;
    
    /**
     * Unidades materia seleccionada y que se va a configurar
     */
    @Getter @NonNull private DtoConfiguracionUnidadMateria confUniMateria;

    /**
     * Operacion a realizar, se acepta persistir y eliminar
     */
    @Getter @NonNull private Operacion operacion;

    /**
     * Periodos escolares para seleccionar
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;

    /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<DtoConfiguracionUnidadMateria> confUniMateriasSug;
    
      /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<DtoConfiguracionUnidadMateria> confUniMateriasGuard;
    
     /**
     * Lista de configuraciones realizadas
     */
    @Getter @NonNull private List<DtoCargaAcademica> cargas;
  
    /**
     * Parametro que guarda valor si existe o no configuración registrada
     */
    @Getter @NonNull private  Boolean existe;
    
    public ConfiguracionUnidadMateriaRolDocente(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.docente = docente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }
   
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }
    
    public void setCarga(DtoCargaAcademica carga) {
        this.carga = carga;
    }

    public void setUnidadMateria(UnidadMateria unidadMateria) {
        this.unidadMateria = unidadMateria;
    }

    public void setConfUniMateria(DtoConfiguracionUnidadMateria confUniMateria) {
        this.confUniMateria = confUniMateria;
    }

    public void setOperacion(Operacion operacion) {
        this.operacion = operacion;
    }

    /**
     * Sincroniza el periodo seleccionado al primer periodo en la lista
     * @param periodos
     */
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos != null && !periodos.isEmpty()){
            this.setPeriodo(periodos.get(0));
        }
    }

    /**
     * Sincroniza los grupos del programa seleccionado y el primer grupo como seleccionado
     * @param cargas
     */
    public void setCargas(List<DtoCargaAcademica> cargas) {
        this.cargas = cargas;
        if (cargas != null && !cargas.isEmpty()) {
            this.setCarga(cargas.get(0));
        }
    }
    
    public void setConfUniMateriasSug(List<DtoConfiguracionUnidadMateria> confUniMateriasSug) {
        this.confUniMateriasSug = confUniMateriasSug;
    }
    
     public void setConfUniMateriasGuard(List<DtoConfiguracionUnidadMateria> confUniMateriasGuard) {
        this.confUniMateriasGuard = confUniMateriasGuard;
    }
    
     public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setExiste(Boolean existe) {
        this.existe = existe;
        if(existe == null){
         this.setExiste(false);
        }
    }

}
