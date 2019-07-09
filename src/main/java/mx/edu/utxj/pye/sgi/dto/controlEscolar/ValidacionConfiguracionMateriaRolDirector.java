//package mx.edu.utxj.pye.sgi.dto.controlEscolar;
//
//import com.github.adminfaces.starter.infra.model.Filter;
//import lombok.Getter;
//import lombok.NonNull;
//import mx.edu.utxj.pye.sgi.dto.AbstractRol;
//import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
//import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
//import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
//import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
//import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
//import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
//import mx.edu.utxj.pye.sgi.enums.Operacion;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//public class ValidacionConfiguracionMateriaRolDirector extends AbstractRol {
//
//    /**
//     * Representa la referencia hacia el personal director
//     */
//    @Getter @NonNull private PersonalActivo director;
//
//    /**
//     * Representa la referencia al evento activo de asignación academica
//     */
//    @Getter @NonNull private EventoEscolar eventoActivo;
//
//    /**
//     * Total de horas asignadas al docente seleccionado
//     */
//    @Getter @NonNull private Integer totalHorasAsignadas;
//
//    /**
//     * Pista del docente asignar
//     */
//    @Getter @NonNull private String pistaDocente;
//
//    /**
//     * Bandera que indica si se tiene la posibilidad de asignar una materia a un docente-grupo-periodo
//     */
//    @NonNull private Boolean asignable;
//
//    /**
//     * Docente seleccionado para asignar carga
//     */
//    @Getter @NonNull private PersonalActivo docente;
//
//    /**
//     * Periodo seleccionado en el que se realizará la asignación
//     */
//    @Getter @NonNull private PeriodosEscolares periodo;
//
//    /**
//     * Representa la clave
//     */
//    @Getter @NonNull private Integer periodoActivo;
//
//    /**
//     * Programa educativo seleccionado para asignar
//     */
//    @Getter @NonNull private AreasUniversidad programa;
//
//    /**
//     * Grupo seleccionado para asignar
//     */
//    @Getter @NonNull private Grupo grupo;
//
//    /**
//     * Materia seleccionada y que se va a asignar
//     */
//    @Getter @NonNull private DtoMateria materia;
//
//    /**
//     * Carga académica registrada o seleccionada para eliminar
//     */
//    @Getter @NonNull private CargaAcademica carga;
//
//    /**
//     * Operacion a realizar, se acepta persistir y eliminar
//     */
//    @Getter @NonNull private Operacion operacion;
//
//    /**
//     * Grupos pertenecientes al programa seleccionado
//     */
//    @Getter @NonNull private List<Grupo> grupos;
//
//    /**
//     * Lista de docentes mostrados según la pista ingresada
//     */
//    @Getter @NonNull private List<PersonalActivo> docentes;
//
//    /**
//     * Periodos escolares para seleccionar
//     */
//    @Getter @NonNull private List<PeriodosEscolares> periodos;
//
//    /**
//     * Lista de programas educativos vigentes y ordenados por nombre que dependen del area con POA del director identificado
//     */
//    @Getter @NonNull private List<AreasUniversidad> programas;
//
//    /**
//     * Mapeo de programas educativos y sus grupos
//     */
//    @Getter @NonNull private Map<AreasUniversidad, List<Grupo>> programasGruposMap;
//
//    @Getter @NonNull private List<Integer> grados = IntStream.rangeClosed(1, 11).boxed().collect(Collectors.toList());
//
//    /**
//     * Lista de materias sin asignar
//     */
//    @Getter @NonNull private List<DtoMateria> materiasPorGrupo;
//
//    /**
//     * Lista de cargas académicas realizadas al docente seleccionado
//     */
//    @Getter @NonNull private List<DtoCargaAcademica> cargas;
//
//    public ValidacionConfiguracionMateriaRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
//        super(filtro);
//        this.director = director;
//        this.totalHorasAsignadas = 0;
//        this.programa = programa;
//    }
//
//    public void setDirector(PersonalActivo director) {
//        this.director = director;
//    }
//
//    public void setEventoActivo(EventoEscolar eventoActivo) {
//        this.eventoActivo = eventoActivo;
//    }
//
//    public void setTotalHorasAsignadas(Integer totalHorasAsignadas) {
//        this.totalHorasAsignadas = totalHorasAsignadas;
//    }
//
//    public void setPistaDocente(String pistaDocente) {
//        this.pistaDocente = pistaDocente;
//    }
//
//    public void setDocente(PersonalActivo docente) {
//        this.docente = docente;
//    }
//
//    public void setPeriodo(PeriodosEscolares periodo) {
//        this.periodo = periodo;
//        if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
//    }
//
//    /**
//     * Sincroniza la lista de grupos del programa seleccionado y si la lista de grupos no es nula ni vacia, se sincroniza el grupo seleccionado
//     * al primer grupo en la lista
//     * @param programa Programa seleccionado del cual se van a establecer los grupos
//     */
//    public void setPrograma(AreasUniversidad programa) {
//        this.programa = programa;
//        if(programa != null && programasGruposMap.containsKey(programa)){
//            this.grupos = programasGruposMap.get(programa);
//            if(grupos!=null && !grupos.isEmpty()){
//                this.grupo = grupos.get(0);
//            }
//        }else {
//            this.grupos = Collections.EMPTY_LIST;
//        }
//    }
//
//    public void setGrupo(Grupo grupo) {
//        this.grupo = grupo;
//    }
//
//    public void setMateria(DtoMateria materia) {
//        this.materia = materia;
//    }
//
//    public void setCarga(CargaAcademica carga) {
//        this.carga = carga;
//    }
//
//    public void setOperacion(Operacion operacion) {
//        this.operacion = operacion;
//    }
//
//    public void setGrupos(List<Grupo> grupos) {
//        this.grupos = grupos;
//    }
//
//    public void setDocentes(List<PersonalActivo> docentes) {
//        this.docentes = docentes;
//    }
//
//    /**
//     * Sincroniza el periodo seleccionado al primer periodo en la lista
//     * @param periodos
//     */
//    public void setPeriodos(List<PeriodosEscolares> periodos) {
//        this.periodos = periodos;
//        if(periodos != null && !periodos.isEmpty()){
//            this.setPeriodo(periodos.get(0));
//        }
//    }
//
//    /**
//     * Sincroniza los grupos del programa seleccionado y el primer grupo como seleccionado
//     * @param programasGruposMap
//     */
//    public void setProgramasGruposMap(Map<AreasUniversidad, List<Grupo>> programasGruposMap) {
//        this.programasGruposMap = programasGruposMap;
//        if(programasGruposMap != null){
//            this.programas = programasGruposMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
//        }
//        if(programasGruposMap != null && programa != null && programasGruposMap.containsKey(programa)) {
//            this.grupos = programasGruposMap.get(programa);
//            if(grupos!=null){
//                grupo = grupos.get(0);
//            }
//        }
//    }
//
//    public void setMateriasPorGrupo(List<DtoMateria> materiasPorGrupo) {
//        this.materiasPorGrupo = materiasPorGrupo;
//    }
//
//    public void setCargas(List<DtoCargaAcademica> cargas) {
//        this.cargas = cargas;
//    }
//
//    public Boolean getAsignable() {
//        asignable = periodo != null && grupo != null && docente != null && materia != null;
//        return asignable;
//    }
//
//    public void setAsignable(Boolean asignable) {
//        this.asignable = asignable;
//    }
//
//    public void setPeriodoActivo(Integer periodoActivo) {
//        this.periodoActivo = periodoActivo;
//    }
//
//    public void setProgramas(List<AreasUniversidad> programas) {
//        this.programas = programas;
//    }
//}
