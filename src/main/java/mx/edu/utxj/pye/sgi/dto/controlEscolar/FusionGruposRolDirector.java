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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Inscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author UTXJ
 */
public class FusionGruposRolDirector extends AbstractRol{
    
    /**
     * Representa la referencias hacia personal director
     */
    @Getter @NonNull private PersonalActivo director;
    /**
     * Representa la referencia hacia el periodo seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    /**
     * Representa la referencias hacia el programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programa;
    /**
     * Representa la referencia del grupo origen de donde se cambiaran los estudiantes
     */
    @Getter @NonNull private Grupo grupoOrigen;
    /**
     * Representa la refenecia del grupo destino de los estudiantes seleccionados
     */
    @Getter @NonNull private Grupo grupoDestino;
    /**
     * Representa el listado de periodos
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    /**
     * Representa el listado de programas educativos por área operativa
     */
    @Getter @NonNull private List<AreasUniversidad> programas;
    /**
     * Representa el listado de grupos que pertenecen a un programa educativo
     */
    @Getter @NonNull private List<Grupo> grupos;
    /**
     * Representa el listado de estudiantes que pertenecen al grupo selececcionado
     */
    @Getter @NonNull private List<Inscripcion> listaEstudiantes;
    /**
     * Representa el mapeo de programas educativos con sus respectivos grupos
     */
    @Getter @NonNull private Map<AreasUniversidad, List<Grupo>> programasGruposMap;
    /**
     * Representa el mapeo de grupos de un programa educativo con su listado de estudiantes inscritos
     */
    @Getter @NonNull private Map<Grupo, List<Inscripcion>> grupoEstudianteMap;
    
    public FusionGruposRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setGrupoOrigen(Grupo grupoOrigen) {
        this.grupoOrigen = grupoOrigen;
    }

    public void setGrupoDestino(Grupo grupoDestino) {
        this.grupoDestino = grupoDestino;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos != null && periodos.isEmpty()){
            this.periodo = periodos.get(0);
        }
    }

    public void setProgramas(List<AreasUniversidad> programas) {
        this.programas = programas;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void setListaEstudiantes(List<Inscripcion> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setProgramasGruposMap(Map<AreasUniversidad, List<Grupo>> programasGruposMap) {
        this.programasGruposMap = programasGruposMap;
        if(programasGruposMap != null){
            this.programas = programasGruposMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
        }
        if(programasGruposMap != null && programa != null && this.programasGruposMap.containsKey(programa)){
            this.grupos = programasGruposMap.get(programa);
            if(grupos != null){
                grupoOrigen = this.grupos.get(0);
            }
        }
    }

    public void setGrupoEstudianteMap(Map<Grupo, List<Inscripcion>> grupoEstudianteMap) {
        this.grupoEstudianteMap = grupoEstudianteMap;
    }
    
    
    
}
