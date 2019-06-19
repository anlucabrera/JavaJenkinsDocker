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
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;

/**
 *
 * @author UTXJ
 */
public class AsignacionTutorRolDirector extends AbstractRol{
    
    /**
     * Representa la referencias hacia personal director
     */
    @Getter @NonNull private PersonalActivo director;
    /**
     * Representa la pista del docente a buscar y asignar
     */
    @Getter @NonNull private String pista;
    /**
     * Docente seleccionado para asignar grupo
     */
    @Getter @NonNull private PersonalActivo docente;
    /**
     * Periodo seleccionado en el cual se realizará la asignación del grupo
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programa;
    /**
     * Grupo con tutor seleccionado
     */
    @Getter @NonNull private DtoListadoTutores dtoListadoTutores;
    /**
     * Representa la operación a realizar
     */
    @Getter @NonNull private Operacion operacion;
    /**
     * Representa los grupos pertenecientes al programa educativo
     */
    @Getter @NonNull private List<Grupo> grupos;
    
    @Getter @NonNull private List<DtoListadoTutores> gruposTutores;
    /**
     * Lista de docentes mostrados de acuerdo a la pista ingresada
     */
    @Getter @NonNull private List<PersonalActivo> docentes;
    /**
     * Lista de periodos escolares a seleccionar
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    /**
     * Lista de programas educativos vigentes
     */
    @Getter @NonNull private List<AreasUniversidad> programas;
    /**
     * Mapeo de programas educativos con sus grupos
     */    
    @Getter @NonNull private Map<AreasUniversidad, List<DtoListadoTutores>> tutoresGruposMap;
        
    public AsignacionTutorRolDirector(Filter<PersonalActivo> filtro,PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }
    
    /**
     * Sincroniza la lista de grupos al programa seleccionado 
     * @param programa Programa Educativo seleccionado del cual se van a restablecer los grupos
     */
    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
        if(programa != null && tutoresGruposMap.containsKey(programa)){
            this.gruposTutores = tutoresGruposMap.get(programa);
            if(gruposTutores!=null && !gruposTutores.isEmpty()){
                this.dtoListadoTutores = gruposTutores.get(0);
            }
        }else {
            this.gruposTutores = Collections.EMPTY_LIST;
        }
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
     * Sincroniza el periodo seleccionado al primero
     * @param periodos 
     */
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos != null && !periodos.isEmpty()){
            this.periodo = periodos.get(0);
        }
    }

    public void setGruposTutores(List<DtoListadoTutores> gruposTutores) {
        this.gruposTutores = gruposTutores;
    }
    /**
     * Sincroniza el programa educativo seleccionado y los grupos de este
     * @param tutoresGruposMap 
     */
    public void setTutoresGruposMap(Map<AreasUniversidad, List<DtoListadoTutores>> tutoresGruposMap) {
        this.tutoresGruposMap = tutoresGruposMap;
        if(tutoresGruposMap != null){
            this.programas = tutoresGruposMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
        }
        if(tutoresGruposMap != null && programa != null && tutoresGruposMap.containsKey(programa)){
            this.gruposTutores = tutoresGruposMap.get(programa);
        }
    }

    public void setDtoListadoTutores(DtoListadoTutores dtoListadoTutores) {
        this.dtoListadoTutores = dtoListadoTutores;
    }
}
