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
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class SeguimientoCartaResponsivaCursoIMSSRolVinculacion extends AbstractRol{
    /**
     * Representa la referencia hacia al coordinador de estadía institucional perteneciente al área de vinculación
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
     * Lista de programas educativos
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;
    
    /**
     * Lista de estudiantes que pertenecen a la generacion, nivel y programa educativo seleccionado
     */
    @Getter @NonNull private List<DtoCartaResponsivaCursoIMMSEstudiante> estudiantesSeguimiento;
    
     /**
     * Seguimiento de un estudiante
     */
    @Getter @NonNull private DtoCartaResponsivaCursoIMMSEstudiante estudianteSeguimiento;
    
     /**
     * Indica si se visualiza toda la información o solo la validada por dirección de carrera
     */
    @Getter @NonNull private Boolean mostrarSegValVinc;
    
     /**
     * Indica si se visualiza todas las columnas
     */
    @Getter @NonNull private Boolean ocultarColumnas;
    
    public SeguimientoCartaResponsivaCursoIMSSRolVinculacion(Filter<PersonalActivo> filtro, PersonalActivo coordinadorEstadia) {
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

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setEstudiantesSeguimiento(List<DtoCartaResponsivaCursoIMMSEstudiante> estudiantesSeguimiento) {
        this.estudiantesSeguimiento = estudiantesSeguimiento;
    }

    public void setEstudianteSeguimiento(DtoCartaResponsivaCursoIMMSEstudiante estudianteSeguimiento) {
        this.estudianteSeguimiento = estudianteSeguimiento;
    }

    public void setMostrarSegValVinc(Boolean mostrarSegValVinc) {
        this.mostrarSegValVinc = mostrarSegValVinc;
    }

    public void setOcultarColumnas(Boolean ocultarColumnas) {
        this.ocultarColumnas = ocultarColumnas;
    }
   
}
