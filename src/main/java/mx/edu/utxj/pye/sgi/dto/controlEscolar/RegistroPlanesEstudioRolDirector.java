/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
public class RegistroPlanesEstudioRolDirector extends AbstractRol{
    
    /**
     * Representa la referencia hacia personal director
     */
    @Getter @NonNull private PersonalActivo director;
    /**
     * Representa la referencia del programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programa;
    /**
     * Representa la referencia al plan de estudios seleccionado
     */
    @Getter @NonNull private PlanEstudio planEstudio;
    /**
     * 
     */
    @Getter @Setter private String descripcion;
    @Getter @Setter private DtoPlanEstudio planEstudio1;
    /**
     * Representa la referencia de la materia seleccionada
     */
    @Getter @NonNull private Materia materia;
    /**
     * Representa la referencia de la unidad de materia
     */
    @Getter @NonNull private UnidadMateria unidadMateria;
    /**
     * Representa el listado de programas educativos vigentes
     */
    @Getter @NonNull private List<AreasUniversidad> programas;
    /**
     * Representa el listado de planes de estudios de un programa de estudio
     */
    @Getter @NonNull private List<PlanEstudio> planesEstudio;
    /**
     * Representa la lista de materias de que contiene algun plan de estudios seleccionado
     */
    @Getter @NonNull private List<Materia> materias;
    /**
     * Representa el listado de unidades de una materia
     */
    @Getter @NonNull private List<UnidadMateria> unidadesMateria;
    /**
     * Mapeo de materias con sus unidades de acuerdo al plan de estudios seleccionado
     */
    @Getter @NonNull private Map<Materia, List<UnidadMateria>> materiasUnidadesMap;
    /**
     * 
     */
    @Getter @NonNull private Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap;
    
    public RegistroPlanesEstudioRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }
    
    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }
    /**
     * Sincroniza en caso de que el plan de estudio seleccionado ya cuenta con asignaturas cargadas
     * @param planEstudio 
     */
    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
        if(planEstudio != null){
            //TODO: verificar lsita de materias
            //this.materias = planEstudio.getMateriaList();
        }else{
            this.materias = Collections.EMPTY_LIST;
        }
    }
    
    /**
     * Sincroniza el listado de unidades que contiene cierta materia seleccionada
     * @param materia 
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
        if(materia != null && materiasUnidadesMap.containsKey(materia)){
            this.unidadesMateria = materiasUnidadesMap.get(materia);
            if(unidadesMateria != null && !unidadesMateria.isEmpty()){
                this.unidadMateria = unidadesMateria.get(0);
            }else{
                this.unidadesMateria = Collections.EMPTY_LIST;
            }
        }
    }

    public void setUnidadMateria(UnidadMateria unidadMateria) {
        this.unidadMateria = unidadMateria;
    }

    public void setProgramas(List<AreasUniversidad> programas) {
        this.programas = programas;
    }

    public void setPlanesEstudio(List<PlanEstudio> planesEstudio) {
        this.planesEstudio = planesEstudio;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }
    
    /**
     * Sincronoza la materia seleccionada con sus respectivas unidades
     * @param materiasUnidadesMap 
     */
    public void setMateriasUnidades(Map<Materia, List<UnidadMateria>> materiasUnidadesMap) {
        this.materiasUnidadesMap = materiasUnidadesMap;
        if(materiasUnidadesMap != null){
            this.materias = materiasUnidadesMap.keySet().stream().sorted(Comparator.comparing(Materia::getNombre)).collect(Collectors.toList());
        }
        if(materiasUnidadesMap != null && materia != null && materiasUnidadesMap.containsKey(materia)){
            this.unidadesMateria = materiasUnidadesMap.get(materia);
            if(unidadesMateria != null){
                unidadesMateria.get(0);
            }
        }
    }

    public void setUnidadesMateria(List<UnidadMateria> unidadesMateria) {
        this.unidadesMateria = unidadesMateria;
    }

    public void setMateriasUnidadesMap(Map<Materia, List<UnidadMateria>> materiasUnidadesMap) {
        this.materiasUnidadesMap = materiasUnidadesMap;
    }

    public void setAreaPlanEstudioMap(Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap) {
        this.areaPlanEstudioMap = areaPlanEstudioMap;
        if(areaPlanEstudioMap != null){
            this.programas = areaPlanEstudioMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
        }
        if(areaPlanEstudioMap != null && programa != null && areaPlanEstudioMap.containsKey(programa)){
            this.planesEstudio = areaPlanEstudioMap.get(programa);
            if(planesEstudio != null){
                planesEstudio.get(0);
            }
        }
    }
}
