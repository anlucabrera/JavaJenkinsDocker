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
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
/**
 *
 * @author UTXJ
 */
public class ConsultaEmpresasEstadiaRolVinculacion extends AbstractRol{
    /**
     * Representa la referencia hacia al coordinador de estadía institucional pertenciente al área de vinculación
     */
    @Getter @NonNull private PersonalActivo coordinadorEstadia;
  
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
   
    /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Lista de empresas asignadas a seguimiento de estadía de la generación y nivel seleccionado
     */
    @Getter @NonNull private List<DtoEmpresaSeguimientosEstadia> empresasSeguimiento;
    
     /**
     * Empresas asignadas a seguimiento de estadía seleccionada
     */
    @Getter @NonNull private DtoEmpresaSeguimientosEstadia empresaSeguimiento;
    
    
    public ConsultaEmpresasEstadiaRolVinculacion(Filter<PersonalActivo> filtro, PersonalActivo coordinadorEstadia) {
        super(filtro);
        this.coordinadorEstadia = coordinadorEstadia;
    }

    public void setCoordinadorEstadia(PersonalActivo coordinadorEstadia) {
        this.coordinadorEstadia = coordinadorEstadia;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setEmpresasSeguimiento(List<DtoEmpresaSeguimientosEstadia> empresasSeguimiento) {
        this.empresasSeguimiento = empresasSeguimiento;
    }

    public void setEmpresaSeguimiento(DtoEmpresaSeguimientosEstadia empresaSeguimiento) {
        this.empresaSeguimiento = empresaSeguimiento;
    }
    
}
