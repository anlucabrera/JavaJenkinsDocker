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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    /////////////////////////General wrappers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Boolean completo, cargada, finalizado, estSexto, estOnceavo ,esDeIyE,director, tutor, tutorCe,tutor2,esSecretario, planeacion,esPsicopedagogia, ESActiva, ESTsuActiva, ESIngActiva, ESEActiva, EEActiva,ETutorActiva,EDocenteActiva, esServEst, esServEst2, eCEActiva;
    @Getter @Setter public Short grado;
    @Getter @Setter public String evaluador, valor, cveDirector, nombreCompletoTutor;
    @Getter @Setter public Integer evaluadorr,cveTrabajador,usuarioNomina;
    @Getter @Setter public Long total,total2;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ////////////////////////General entity's\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Evaluaciones evaluacion, evaluacionAnterior;
    @Getter @Setter public Alumnos alumno;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////Maps and lists generals\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Map<String, String> respuestas;
    @Getter @Setter public List<SelectItem> respuestasPosibles;
    @Getter @Setter public List<Apartado> apartados;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    @Getter @Setter public EncuestaServiciosResultados resultado;
    @Getter @Setter public ResultadoEJB<EncuestaServiciosResultados> resultadoEJB;
    @Getter @Setter public ResultadosEncuestaSatisfaccionTsu resultadoREST;
    @Getter @Setter public EncuestaSatisfaccionEgresadosIng resultadoESEI;
    @Getter @Setter public List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado;
    @Getter @Setter public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> alumnosEncuesta = new ArrayList<>();
    @Getter @Setter public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> alumnosEncuestaUnion = new ArrayList<>();
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
