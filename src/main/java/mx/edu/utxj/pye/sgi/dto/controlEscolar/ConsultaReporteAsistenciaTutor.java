/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;

/**
 *
 * @author UTXJ
 */
public class ConsultaReporteAsistenciaTutor extends AbstractRol{

    
// Variables Basicas   
    @Getter @NonNull private PersonalActivo tutor;
    //Representa la referencia hacia personal director
    @Getter    @Setter    @NonNull    private Boolean newCompetencia;
    
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    /**
     * Periodo seleccionado en el que se realizará la asignación
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    /**
     * Representa la clave
     */
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
    @Getter    @Setter    private List<UnidadMateria> ums;
    @Getter    @Setter    private List<PersonalActivo> docentes;
    @Getter    @Setter    private List<DtoPaseListaReportes> dplrs;
    @Getter    @Setter    private List<DtoReportePaseLista> drpls;
    @Getter    @Setter    private Grupo grupoSelec;
// Variables de tipo de Map
    // Mapeo de materias con sus unidades de acuerdo al plan de estudios seleccionado
    @Getter    @NonNull    private Map<Materia, List<UnidadMateria>> materiasUnidadesMap;
    
    
    public ConsultaReporteAsistenciaTutor(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        tutor = filtro.getEntity();
    }

// SET´s de Objetos
    public void setTutor(PersonalActivo tutor) {
        this.tutor = tutor;
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

}
