/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.*;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

/**
 *
 * @author Planeacion
 */
public class DtoEvaluaciones implements Serializable{

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    @Getter @Setter @NonNull private DtoEstudiante estudiante;

    public Boolean tieneAcceso(DtoEstudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }
    
    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }
    
    public Boolean tieneAcceso(Alumnos estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE)) return false;
        return true;
    }
    /////////////////////////General wrappers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Getter @Setter public Boolean mostrarES, completo, testCompleto, cargada, finalizado, estSexto, estOnceavo ,
            esDeIyE,director, tutor, tutorCe,tutor2,esSecretario, planeacion,esPsicopedagogia,esRector,esFda, ESActiva, ESTsuActiva, ESIngActiva,
            ESEActiva, EEActiva,ETutorActiva,EDocenteActiva, esServEst, esServEst2, eCEActiva, encuestaTSUCompleto,evaluacionEstadiaCompleto;
    @Getter @Setter public Short grado;
    @Getter @Setter public String evaluador, valor, cveDirector, nombreCompletoTutor, fechaAplicacion;
    @Getter @Setter public Integer evaluadorr,cveTrabajador,usuarioNomina, periodo;
    @Getter @Setter public Long total,total2;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ////////////////////////General entity's\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Evaluaciones evaluacion, evaluacionAnterior;
    @Getter @Setter public Alumnos alumno;
    @Getter @Setter public Personas personas;
    @Getter @Setter public CarrerasCgut carrera;
    @Getter @Setter public Estudiante estudianteCE;
    @Getter @Setter public AreasUniversidad areasUniversidad;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////Maps and lists generals\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Map<String, String> respuestas;
    @Getter @Setter public List<SelectItem> respuestasPosibles;
    @Getter @Setter public List<SelectItem> respuestasPosibles1;
    @Getter @Setter public List<SelectItem> respuestasPosibles2;
    @Getter @Setter public List<SelectItem> respuestasPosibles3;
    @Getter @Setter public List<SelectItem> respuestasPosibles4;
    @Getter @Setter public List<SelectItem> respuestasPosibles5;
    @Getter @Setter public List<Apartado> apartados;
    @Getter @Setter public List<Apartado> apartados1;
    @Getter @Setter public List<Apartado> apartados2;
    @Getter @Setter public List<Apartado> apartados3;
    @Getter @Setter public List<Apartado> apartados4;
    @Getter @Setter public List<Apartado> apartados5;
    @Getter @Setter public List<Apartado> apartados6;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    @Getter @Setter public EncuestaServiciosResultados resultado;
    @Getter @Setter public TestDiagnosticoAprendizaje resultadoTest;
    @Getter @Setter public ResultadoEJB<EncuestaServiciosResultados> resultadoEJB;
    @Getter @Setter public ResultadosEncuestaSatisfaccionTsu resultadoREST;
    @Getter @Setter public EncuestaSatisfaccionEgresadosIng resultadoESEI;
    @Getter @Setter public SeguimientoEstadiaEstudiante seguimiento;
    @Getter @Setter public Personal persona;
    @Getter @Setter public EvaluacionEstadiaResultados resultadoEER;
    @Getter @Setter public List<PeriodosEscolares> listaEvaluaciones;
    @Getter @Setter public List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado, listaEvaluacionHitorico, listaEvaluacionFilter;
    @Getter @Setter public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> alumnosEncuesta = new ArrayList<>();
    @Getter @Setter public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> alumnosEncuestaUnion = new ArrayList<>(), alumnos, alumnosFilter;
    @Getter @Setter public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> alumnosEncuestaCE = new ArrayList<>();
    @Getter @Setter public List<ListaDatosAvanceEncuestaServicio> dtoLDAES, dtoLDAES1, dtoLDAES2, dtoLDAES3;
    @Getter @Setter public List<ViewEstudianteAsesorAcademico> alumnosEncuestas;
    @Getter @Setter public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> dtoAESPG, dtoAESPG1, dtoAESPG2, dtoAESPGFilter;



    ////////////////////////////////////Comparadores\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
    @Getter @Setter public Comparador<ResultadosEncuestaSatisfaccionTsu> comparadorEST = new ComparadorEncuestaSatisfaccionEgresadosTsu();
    @Getter @Setter public Comparador<EvaluacionEstadiaResultados> comparadorEE = new ComparadorEvaluacionEstadia();
    @Getter @Setter public Comparador<EncuestaSatisfaccionEgresadosIng> comparadorESI = new ComparadorEncuestaSatisfaccionEgresadosIng();
    @Getter @Setter public Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparadorESR = new ComparadorEvaluacionEstudioSocioEconomico();
    @Getter @Setter public Comparador<EncuestaCondicionesEstudio> comparadorECE = new ComparadorCondicionesEstudio();

    }
