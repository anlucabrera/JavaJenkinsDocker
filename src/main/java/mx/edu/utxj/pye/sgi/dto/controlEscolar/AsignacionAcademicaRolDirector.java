package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.enums.Operacion;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AsignacionAcademicaRolDirector extends AbstractRol {

    /**
     * Representa la referencia hacia el personal director
     */
    @Getter @NonNull private PersonalActivo director;

    /**
     * Total de horas asignadas al docente seleccionado
     */
    @Getter @NonNull private Integer totalHorasAsignadas;

    /**
     * Pista del docente asignar
     */
    @Getter @NonNull private String pistaDocente;

    /**
     * Bandera que indica si se tiene la posibilidad de asignar una materia a un docente-grupo-periodo
     */
    @NonNull private Boolean asignable;

    /**
     * Docente seleccionado para asignar carga
     */
    @Getter @NonNull private PersonalActivo docente;

    /**
     * Periodo seleccionado en el que se realizará la asignación
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Programa educativo seleccionado para asignar
     */
    @Getter @NonNull private ProgramasEducativos programa;

    /**
     * Grupo seleccionado para asignar
     */
    @Getter @NonNull private Grupo grupo;

    /**
     * Materia seleccionada y que se va a asignar
     */
    @Getter @NonNull private Materia materia;

    /**
     * Carga académica registrada o seleccionada para eliminar
     */
    @Getter @NonNull private CargaAcademica carga;

    /**
     * Operacion a realizar, se acepta persistir y eliminar
     */
    @Getter @NonNull private Operacion operacion;

    /**
     * Grupos pertenecientes al programa seleccionado
     */
    @Getter @NonNull private List<Grupo> grupos;

    /**
     * Lista de docentes mostrados según la pista ingresada
     */
    @Getter @NonNull private List<PersonalActivo> docentes;

    /**
     * Periodos escolares para seleccionar
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;

    /**
     * Mapeo de programas educativos y sus grupos
     */
    @Getter @NonNull private Map<ProgramasEducativos, List<Grupo>> programasGruposMap;

    /**
     * Lista de materias sin asignar
     */
    @Getter @NonNull private List<Materia> materiasSinAsignar;

    /**
     * Lista de cargas académicas realizadas al docente seleccionado
     */
    @Getter @NonNull private List<CargaAcademica> cargas;

    public AsignacionAcademicaRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director, ProgramasEducativos programa) {
        super(filtro);
        this.director = director;
        this.totalHorasAsignadas = 0;
        this.programa = programa;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setTotalHorasAsignadas(Integer totalHorasAsignadas) {
        this.totalHorasAsignadas = totalHorasAsignadas;
    }

    public void setPistaDocente(String pistaDocente) {
        this.pistaDocente = pistaDocente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    /**
     * Sincroniza la lista de grupos del programa seleccionado y si la lista de grupos no es nula ni vacia, se sincroniza el grupo seleccionado
     * al primer grupo en la lista
     * @param programa Programa seleccionado del cual se van a establecer los grupos
     */
    public void setPrograma(ProgramasEducativos programa) {
        this.programa = programa;
        if(programa != null && programasGruposMap.containsKey(programa)){
            this.grupos = programasGruposMap.get(programa);
            if(grupos!=null && !grupos.isEmpty()){
                this.grupo = grupos.get(0);
            }
        }else {
            this.grupos = Collections.EMPTY_LIST;
        }
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public void setCarga(CargaAcademica carga) {
        this.carga = carga;
    }

    public void setOperacion(Operacion operacion) {
        this.operacion = operacion;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void setDocentes(List<PersonalActivo> docentes) {
        this.docentes = docentes;
    }

    /**
     * Sincroniza el periodo seleccionado al primer periodo en la lista
     * @param periodos
     */
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos != null && !periodos.isEmpty()){
            this.periodo = periodos.get(0);
        }
    }

    /**
     * Sincroniza los grupos del programa seleccionado y el primer grupo como seleccionado
     * @param programasGruposMap
     */
    public void setProgramasGruposMap(Map<ProgramasEducativos, List<Grupo>> programasGruposMap) {
        this.programasGruposMap = programasGruposMap;
        if(programasGruposMap != null && programa != null && programasGruposMap.containsKey(programa)) {
            this.grupos = programasGruposMap.get(programa);
            if(grupos!=null){
                grupo = grupos.get(0);
            }
        }
    }

    public void setMateriasSinAsignar(List<Materia> materiasSinAsignar) {
        this.materiasSinAsignar = materiasSinAsignar;
    }

    public void setCargas(List<CargaAcademica> cargas) {
        this.cargas = cargas;
    }

    public Boolean getAsignable() {
        asignable = periodo != null && grupo != null && docente != null && materia != null;
        return asignable;
    }
}
