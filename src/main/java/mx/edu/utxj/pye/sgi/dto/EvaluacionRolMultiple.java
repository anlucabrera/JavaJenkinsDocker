package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDesempenioAmbientalUtxj;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionAmbiental;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

import javax.faces.model.SelectItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluacionRolMultiple{

    public static class EvaluacionRolPersonal extends AbstractRol {
        @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        @Getter @Setter private String fechaElaboracion;

        /**
         * Representa la evaluación activa
         */

        @Getter @NonNull private Evaluaciones evaluacion;

        @Getter @NonNull private PersonalActivo personal;

        /**
         * Representa el periodo activo de la evaluación
         */
        @Getter @NonNull private Integer periodoActivo, clavePersona;

        @Getter @NonNull private List<Apartado> apartados;
        @Getter @NonNull private List<SelectItem> respuestaPosibles;
        @Getter @NonNull private Map<String, String> respuestas = new HashMap<>();
        @Getter @NonNull private  String valor;
        @Getter @NonNull private EvaluacionDesempenioAmbientalUtxj resultados;
        @Getter @Setter private Comparador<EvaluacionDesempenioAmbientalUtxj> comparador = new ComparadorEvaluacionAmbiental();
        @Getter @Setter private List<DtoAlumnosEncuesta.DtoEvaluaciones> evaluacionesCompletas = new ArrayList<>();
        @Getter @Setter private List<DtoAlumnosEncuesta.DtoEvaluaciones> evaluacionesInCompletas = new ArrayList<>();
        @Getter @Setter private List<DtoAlumnosEncuesta.DtoEvaluaciones> evaluacionesCompletasPersonal = new ArrayList<>();
        @Getter @Setter private List<DtoAlumnosEncuesta.DtoEvaluaciones> evaluacionesCompletasEyP = new ArrayList<>();
        @Getter @Setter private List<DtoAlumnosEncuesta.DtoEvaluaciones> evaluacionesInCompletasPersonal = new ArrayList<>();

        public void setPersonal(PersonalActivo personal) {
            this.personal = personal;
        }

        public void setEvaluacion(Evaluaciones evaluacion) {
            this.evaluacion = evaluacion;
        }

        public void setPeriodoActivo(Integer periodoActivo) {
            this.periodoActivo = periodoActivo;
        }

        public void setApartados(List<Apartado> apartados) {
            this.apartados = apartados;
        }

        public void setRespuestaPosibles(List<SelectItem> respuestaPosibles) {
            this.respuestaPosibles = respuestaPosibles;
        }

        public void setRespuestas(Map<String, String> respuestas) {
            this.respuestas = respuestas;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public void setResultados(EvaluacionDesempenioAmbientalUtxj resultados) {
            this.resultados = resultados;
        }

        public void setClavePersona(Integer clavePersona) {
            this.clavePersona = clavePersona;
        }

        public EvaluacionRolPersonal(Filter<PersonalActivo> filtro, PersonalActivo personal) {
            super(filtro);
            this.personal = personal;
        }

    }

    public static class EvaluacionRolEstudiante19{
        @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;

        @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        @Getter @Setter private String fechaElaboracion;

        /**
         * Representa la evaluación activa
         */

        @Getter @NonNull private Evaluaciones evaluacion;

        @Getter @NonNull private DtoEstudiante estudiante;

        /**
         * Representa el periodo activo de la evaluación
         */
        @Getter @NonNull private Integer periodoActivo, clavePersona;

        @Getter @NonNull private List<Apartado> apartados;
        @Getter @NonNull private List<SelectItem> respuestaPosibles;
        @Getter @NonNull private Map<String, String> respuestas = new HashMap<>();
        @Getter @NonNull private  String valor;
        @Getter @NonNull private EvaluacionDesempenioAmbientalUtxj resultados;

        public Boolean tieneAcceso(DtoEstudiante estudiante, UsuarioTipo usuarioTipo){
            if(estudiante == null) return false;
            if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
            return true;
        }

        public void setEvaluacion(Evaluaciones evaluacion) {
            this.evaluacion = evaluacion;
        }

        public void setPeriodoActivo(Integer periodoActivo) {
            this.periodoActivo = periodoActivo;
        }

        public void setApartados(List<Apartado> apartados) {
            this.apartados = apartados;
        }

        public void setEstudiante(DtoEstudiante estudiante) {
            this.estudiante = estudiante;
        }

        public void setRespuestas(Map<String, String> respuestas) {
            this.respuestas = respuestas;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public void setResultados(EvaluacionDesempenioAmbientalUtxj resultados) {
            this.resultados = resultados;
        }

        public void setClavePersona(Integer clavePersona) {
            this.clavePersona = clavePersona;
        }

    }

    public static class EvaluacionRolEstudiante{
        @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;

        @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        @Getter @Setter private String fechaElaboracion;

        /**
         * Representa la evaluación activa
         */

        @Getter @NonNull private Evaluaciones evaluacion;

        @Getter @NonNull private Alumnos estudiante;

        @Getter @NonNull private Personas personas;

        /**
         * Representa el periodo activo de la evaluación
         */
        @Getter @NonNull private Integer periodoActivo, clavePersona;

        @Getter @NonNull private List<Apartado> apartados;
        @Getter @NonNull private Map<String, String> respuestas = new HashMap<>();
        @Getter @NonNull private  String valor;
        @Getter @NonNull private EvaluacionDesempenioAmbientalUtxj resultados;

        public Boolean tieneAcceso(Alumnos estudiante, UsuarioTipo usuarioTipo){
            if(estudiante == null) return false;
            if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE)) return false;
            return true;
        }

        public void setEvaluacion(Evaluaciones evaluacion) {
            this.evaluacion = evaluacion;
        }

        public void setPeriodoActivo(Integer periodoActivo) {
            this.periodoActivo = periodoActivo;
        }

        public void setApartados(List<Apartado> apartados) {
            this.apartados = apartados;
        }

        public void setEstudiante(Alumnos estudiante) {
            this.estudiante = estudiante;
        }

        public void setRespuestas(Map<String, String> respuestas) {
            this.respuestas = respuestas;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public void setResultados(EvaluacionDesempenioAmbientalUtxj resultados) {
            this.resultados = resultados;
        }

        public void setClavePersona(Integer clavePersona) {
            this.clavePersona = clavePersona;
        }

        public void setPersonas(Personas personas) {
            this.personas = personas;
        }
    }
}
