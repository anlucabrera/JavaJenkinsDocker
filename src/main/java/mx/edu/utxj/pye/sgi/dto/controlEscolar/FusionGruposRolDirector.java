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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import org.primefaces.model.DualListModel;

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
     * Re´presenta las referencias al evento escolar activo
     */
    @Getter @NonNull private EventoEscolar eventoActivo;
    /**
     * Representa la referencias hacia el periodo activo
     */
    @Getter @NonNull private Integer periodoActivo;

    /**
     * Representa la referencias hacia personal director
     */
    @Getter @NonNull private PersonalActivo director;

    /**
     * Representa la referencias hacia personal director
     */
    @Getter @NonNull private PersonalActivo serviciiosEscolares;
    /**
     * Representa la referencia hacia el periodo seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    /**
     * Representa la referencias hacia el programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programa;
    /**
     * Representa la referencias hacia el programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad areaUniversidad;
    /**
     * Representa la referencia del grupo origen de donde se cambiaran los estudiantes
     */
    @Getter @NonNull private Grupo grupoOrigen;
    /**
     * Representa la refenecia del grupo destino de los estudiantes seleccionados
     */
    @Getter @NonNull private Grupo grupoDestino;
    /**
     * Representa la referencia del grupo seleccionado
     */
    @Getter @NonNull private Grupo grupoSeleccionado;
    /**
     * Representa el listado de periodos
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    /**
     * Representa el listado de programas educativos por área operativa
     */
    @Getter @NonNull private List<AreasUniversidad> programas, divisiones;
    /**
     * Representa el listado de grupos que pertenecen a un programa educativo
     */
    @Getter @NonNull private List<Grupo> grupos;
    /**
     * Representa el listado de estudiantes que pertenecen al grupo selececcionado
     */
    @Getter @NonNull private List<Estudiante> listaEstudiantes;

    /**
     * Representa el listado de estudiantes que pertenecen al grupo destino
     */
    @Getter @NonNull private List<Estudiante> listaEstudiantesDestino;
    /**
     * Representa el mapeo de programas educativos con sus respectivos grupos
     */
    @Getter @NonNull private Map<AreasUniversidad, List<Grupo>> programasGruposMap;
    /**
     * Representa el mapeo de grupos de un programa educativo con su listado de estudiantes inscritos
     */
    @Getter @NonNull private Map<Grupo, List<Estudiante>> grupoEstudianteMap;

    /**
     * Representa la referencia de la conversion de la lista estudiantes al dualListModel
     */
    @Getter @NonNull private DualListModel<Estudiante> dualListModelEstudent;

    @Getter @NonNull private Personal tutor;

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

    public void setListaEstudiantes(List<Estudiante> listaEstudiantes) {
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

    public void setGrupoEstudianteMap(Map<Grupo, List<Estudiante>> grupoEstudianteMap) {
        this.grupoEstudianteMap = grupoEstudianteMap;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setDivisiones(List<AreasUniversidad> divisiones) {
        this.divisiones = divisiones;
    }

    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public void setDualListModelEstudent(DualListModel<Estudiante> dualListModelEstudent) {
        this.dualListModelEstudent = dualListModelEstudent;
    }

    public void setListaEstudiantesDestino(List<Estudiante> listaEstudiantesDestino) {
        this.listaEstudiantesDestino = listaEstudiantesDestino;
    }

    public void setTutor(Personal tutor) {
        this.tutor = tutor;
    }

    public void setServiciiosEscolares(PersonalActivo serviciiosEscolares) {
        this.serviciiosEscolares = serviciiosEscolares;
    }

    public void setAreaUniversidad(AreasUniversidad areaUniversidad) {
        this.areaUniversidad = areaUniversidad;
    }
}
