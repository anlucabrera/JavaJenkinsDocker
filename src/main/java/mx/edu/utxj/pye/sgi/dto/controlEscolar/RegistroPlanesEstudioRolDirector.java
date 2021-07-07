/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class RegistroPlanesEstudioRolDirector extends AbstractRol{

    
// Variables Basicas   
    //Representa la referencia hacia personal director
    @Getter    @Setter    @NonNull    private Boolean newCompetencia;
    @Getter    @Setter    private Boolean newRegiistroOe;
    @Getter    @Setter    private Boolean newRegiistroIn;
    @Getter    @Setter    private String tipoReg;
    @Getter    @Setter    private String clve;
    @Getter    @Setter    private String descripcion;
    @Getter    @Setter    private String nivelA;
    @Getter    @Setter    private Integer meta;
    
// Objetos de BD    
    //Representa la referencia hacia personal director
    @Getter    @NonNull    private PersonalActivo director;
    // Representa la referencia del programa educativo seleccionado
    @Getter    @NonNull    private AreasUniversidad programa;
    // Representa la referencia del área de conocimiento seleccionado
    @Getter    @NonNull    private AreaConocimiento conocimiento;
    // Representa la referencia de la Competencia seleccionada
    @Getter    @NonNull    private Competencia competencia;
    // Representa la referencia al plan de estudios seleccionado
    @Getter    @NonNull    private PlanEstudio planEstudio;
    // Representa la referencia al plan de estudios Materia seleccionado
    @Getter    @NonNull    private PlanEstudioMateria planEstudioMateria;
    // Representa la referencia de la materia seleccionada
    @Getter    @Setter    @NonNull    private Materia materia; 
    // Representa la referencia de la unidad de materia seleccionado
    @Getter    @NonNull    private UnidadMateria unidadMateria;
    
    @Getter    @NonNull    private MetasPropuestas metasPropuestas;
    
    @Getter    @Setter    @NonNull private String rutaArchivo;
  
// Variables de tipo de DTO
    // Representa la referencia entre un Plan de Estudio(control escolar) y un Área Universida(Prontuario)
    @Getter    @Setter    private DtoPlanEstudio planEstudio1;
    // Representa la referencia entre una Materia(control escolar) y un Área de Conocimiento(control escolar)
    @Getter    @Setter    private DtoMateriaRegistro materiaRegistro1;
    // Representa la referencia entre una Unidad Materia(control escolar) y una Materia(control escolar)
    @Getter    @Setter    private DtoMateriaUnidades materiaUnidades1;
    // Representa la referencia entre una Meta propuesta(control escolar) y una Materia(control escolar)
    @Getter    @Setter    private DtoMateriaMetasPropuestas materiaMetasPropuestas;
    // Representa la referencia entre un Plan de Estudio Materia(control escolar) con una Plan de Estudio(control escolar) y una Materia(control escolar)
    @Getter    @Setter    private DtoMateriaPlanEstudio materiaPlanEstudio1;
    // Representa la referencia entre una Competencia(control escolar) con una Plan de EstudioMateria(control escolar) y un Plan Estudio(control escolar)
    @Getter    @Setter    private DtoPlanEstudioMateriaCompetencias planEstudioMateriaCompetencias1;
    
    @Getter    @Setter    private DtoAlineacionAcedemica alineacionAcedemica;
    
// Variables de tipo de List
    // Representa el listado de programas educativos vigentes
    @Getter    @NonNull    private List<AreasUniversidad> programas;
    // Representa el listado de las áreas de conocimiento
    @Getter    @NonNull    private List<AreaConocimiento> conocimientos;
    // Representa el listado de los periodos escolares
    @Getter    @Setter     @NonNull    private List<PeriodosEscolares> periodosEscolareses;
    // Representa el listado de las Competencias
    @Getter    @NonNull    private List<Competencia> competencias;
    // Representa el listado de planes de estudios de un programa de estudio
    @Getter    @NonNull    private List<PlanEstudio> planesEstudio;
    // Representa la lista de materias de que contiene algun plan de estudios seleccionado
    @Getter    @NonNull    private List<Materia> materias;
    // Representa el listado de unidades de una materia
    @Getter    @NonNull    private List<UnidadMateria> unidadesMateria;
    // Representa el listado de Metas de una materia
    @Getter    @NonNull    private List<MetasPropuestas> metasPropuestases;
    @Getter    @Setter    private List<MetasPropuestas> propuestases;
    // Representa el listado de Plan Estudio Materia de un Plan de Estudio
    @Getter    @NonNull    private List<PlanEstudioMateria> planEstudioMaterias;    
    @Getter    @Setter    private List<PlanEstudioMateria> planestudioMateriasSelect;
// Variables de tipo de List DTO's
    @Getter    @Setter    private List<DtoPlanEstudioMateriaCompetencias> planEstudioMateriaCompetenciasesList;
    @Getter    @Setter    private List<DtoAlineacionAcedemica> acedemicas;
    @Getter    @Setter    private List<DtoAlineacionAcedemica> alineacionesDescripociones;
    @Setter    @Getter private List<DtoRegistroPrevioAlineacionEducativa> listaPreviaAlineacionEducativa;
// Variables de tipo de Map
    // Mapeo de materias con sus unidades de acuerdo al plan de estudios seleccionado
    @Getter    @NonNull    private Map<Materia, List<UnidadMateria>> materiasUnidadesMap;
    @Getter    @NonNull    private Map<Materia, List<MetasPropuestas>> materiasMetasMap;
    // Mapeo de Areas Universidad con sus Planes de Estudio de acuerdo al director logeado
    @Getter    @NonNull    private Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap;
    
    
    public RegistroPlanesEstudioRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }

// SET´s de Objetos
    public void setDirector(PersonalActivo director) {
        this.director = director;
    }
    
    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setConocimiento(AreaConocimiento conocimiento) {
        this.conocimiento = conocimiento;
    }
    
    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }
    
    /**
     * Sincroniza en caso de que el plan de estudio seleccionado ya cuenta con asignaturas cargadas
     * @param planEstudio 
     */
    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    public void setPlanEstudioMateria(PlanEstudioMateria planEstudioMateria) {
        this.planEstudioMateria = planEstudioMateria;
    }
  
    public void setUnidadMateria(UnidadMateria unidadMateria) {
        this.unidadMateria = unidadMateria;
    }
// SET's de Listas    
    public void setProgramas(List<AreasUniversidad> programas) {
        this.programas = programas;
    }
    public void setConocimientos(List<AreaConocimiento> conocimientos) {
        this.conocimientos = conocimientos;
    }
    public void setCompetencias(List<Competencia> competencias) {
        this.competencias = competencias;
    }
    public void setPlanesEstudio(List<PlanEstudio> planesEstudio) {
        this.planesEstudio = planesEstudio;
    }
    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }
    public void setUnidadesMateria(List<UnidadMateria> unidadesMateria) {
        this.unidadesMateria = unidadesMateria;
    }
    public void setPlanEstudioMaterias(List<PlanEstudioMateria> planEstudioMaterias) {
        this.planEstudioMaterias = planEstudioMaterias;
    }
    
// SET's de Map's    
    /**
     * Sincronoza la materia seleccionada con sus respectivas unidades
     * @param materiasUnidadesMap 
     */
    public void setMateriasUnidadesMap(Map<Materia, List<UnidadMateria>> materiasUnidadesMap) {
        this.materiasUnidadesMap = materiasUnidadesMap;
        if (materiasUnidadesMap != null) {
//            this.materias = materiasUnidadesMap.keySet().stream().sorted(Comparator.comparing(Materia::getNombre)).collect(Collectors.toList());
        }
        if (materiasUnidadesMap != null) {
            this.unidadesMateria = new ArrayList<>();
            materiasUnidadesMap.forEach((t, u) -> {
                if (Objects.equals(t.getIdMateria(), materiaUnidades1.getMateria().getIdMateria())) {
                    this.unidadesMateria.addAll(u);
                }
            });
        } else {
            this.unidadesMateria = new ArrayList<>();
        }
    }
    
    public void setmateriasMetasMap(Map<Materia, List<MetasPropuestas>> materiasMetasMap) {
        this.materiasMetasMap = materiasMetasMap;
        if (materiasMetasMap != null) {
//            this.materias = materiasUnidadesMap.keySet().stream().sorted(Comparator.comparing(Materia::getNombre)).collect(Collectors.toList());
        }
        if (materiasMetasMap != null) {
            this.metasPropuestases = new ArrayList<>();
            materiasMetasMap.forEach((t, u) -> {                
                if (Objects.equals(t.getIdMateria(), materiaMetasPropuestas.getMateria().getIdMateria())) {
                    this.metasPropuestases.addAll(u);
                }
            });
        } else {
            this.metasPropuestases = new ArrayList<>();
        }
    }

    public void setAreaPlanEstudioMap(Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap) {
        this.areaPlanEstudioMap = areaPlanEstudioMap;
        this.planesEstudio = new ArrayList<>();
        if (areaPlanEstudioMap != null) {
            this.programas = areaPlanEstudioMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
            areaPlanEstudioMap.forEach((t, u) -> {
                this.planesEstudio.addAll(u);
            });
        }
        if (areaPlanEstudioMap != null && programa != null && areaPlanEstudioMap.containsKey(programa)) {
            this.planesEstudio = areaPlanEstudioMap.get(programa);
            if (planesEstudio != null) {
                planesEstudio.get(0);
            }
        }
    }
}
