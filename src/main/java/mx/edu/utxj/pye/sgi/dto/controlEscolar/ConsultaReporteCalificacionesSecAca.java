/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */

public class ConsultaReporteCalificacionesSecAca extends AbstractRol{

    
// Variables Basicas   
    @Getter @NonNull private PersonalActivo secAca;    
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    @Getter @NonNull private PeriodosEscolares periodo;
    @Getter @NonNull private Integer periodoActivo;
// Objetos de BD    
    // Representa la referencia del programa educativo seleccionado
    @Getter    @NonNull    private AreasUniversidad programa;
    // Representa la referencia al plan de estudios seleccionado
    @Getter    @NonNull    private PlanEstudio planEstudio;
    @Getter @Setter private Date fechaInpresion;
  
// Variables de tipo de DTO
    // Representa la referencia entre un Plan de Estudio(control escolar) y un Área Universida(Prontuario)
    @Getter    @Setter    private DtoPlanEstudio planEstudio1;
    
// Variables de tipo de List
    // Representa el listado de programas educativos vigentes
    @Getter    @NonNull    private List<AreasUniversidad> programas;
    // Representa el listado de las áreas de conocimiento
    @Getter    @NonNull    private List<PlanEstudio> planesEstudios;
    @Getter    @Setter    private List<Grupo> grupos;
    
    @Getter    @Setter    private List<Listaalumnosca> listaalumnoscas;
    @Getter    @Setter    private List<DtoVistaCalificacionestitulosTabla> titulos;
    @Getter    @Setter    private List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante;
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");
    
    @Getter    @Setter    private List<DtoPresentacionCalificacionesReporte> dvcs;
    
    @Getter    @Setter    private List<Estudiante> estudiantes;
    @Getter    @Setter    private List<DtoCalificacionesTutor> dcts;
    
    
    @Getter    @Setter    private List<DtoPaseListaReportes> dplrs;
    @Getter    @Setter    private List<DtoReportePaseLista> drpls;
    @Getter    @Setter    private Grupo grupoSelec;
// Variables de tipo de Map
    // Mapeo de materias con sus unidades de acuerdo al plan de estudios seleccionado
    @Getter    @NonNull    private Map<Materia, List<UnidadMateria>> materiasUnidadesMap;
    @Getter    @NonNull    private Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap;
    
    
    public ConsultaReporteCalificacionesSecAca(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        secAca = filtro.getEntity();
    }

// SET´s de Objetos
    public void setTutor(PersonalActivo secAca) {
        this.secAca = secAca;
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
    }

// SET's de Listas    
    public void setProgramas(List<AreasUniversidad> programas) {
        this.programas = programas;
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
    
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
         if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }

     public void setAreaPlanEstudioMap(Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap) {
        this.areaPlanEstudioMap = areaPlanEstudioMap;
        this.planesEstudios = new ArrayList<>();
        if (areaPlanEstudioMap != null) {
            this.programas = areaPlanEstudioMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
            areaPlanEstudioMap.forEach((t, u) -> {
                this.planesEstudios.addAll(u);
            });
        }
        if (areaPlanEstudioMap != null && programa != null && areaPlanEstudioMap.containsKey(programa)) {
            this.planesEstudios = areaPlanEstudioMap.get(programa);
            if (planesEstudios != null) {
                planesEstudios.get(0);
            }
        }
    }
}
