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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FechaTerminacionTitulacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class FechasTerminacionRolTitulacion extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de titulación
     */
    @Getter @NonNull private PersonalActivo personal;
    
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Lista niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
   
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
  
     /**
     * Fechas de terminación registradas
     */
    @Getter private FechaTerminacionTitulacion fechaTerminacionTitulacion;
    
     
    public FechasTerminacionRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }
    
    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }
    
    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setFechaTerminacionTitulacion(FechaTerminacionTitulacion fechaTerminacionTitulacion) {
        this.fechaTerminacionTitulacion = fechaTerminacionTitulacion;
    }
}
