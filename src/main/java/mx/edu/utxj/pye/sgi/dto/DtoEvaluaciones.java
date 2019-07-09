/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.funcional.*;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;

/**
 *
 * @author Planeacion
 */
public class DtoEvaluaciones implements Serializable{
    
    /////////////////////////General wrappers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Boolean cargada, finalizado, estSexto, estOnceavo ,esDeIyE,director, tutor, esSecretario, planeacion,esPsicopedagogia, ESActiva, ESTsuActiva,
            ESIngActiva, ESEActiva, EEActiva,ETutorActiva,EDocenteActiva;
    @Getter @Setter public Short grado;
    @Getter @Setter public String evaluador, valor, cveDirector, nombreCompletoTutor;
    @Getter @Setter public Integer evaluadorr,cveTrabajador,usuarioNomina;
    @Getter @Setter public Long total,total2;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ////////////////////////General entity's\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Evaluaciones evaluacion;
    @Getter @Setter public Alumnos alumno;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////Maps and lists generals\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Map<String, String> respuestas;
    @Getter @Setter public List<SelectItem> respuestasPosibles;
    @Getter @Setter public List<Apartado> apartados;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    @Getter @Setter public EncuestaServiciosResultados resultado;
    @Getter @Setter public ResultadosEncuestaSatisfaccionTsu resultadoREST;
    @Getter @Setter public EncuestaSatisfaccionEgresadosIng resultadoESEI;
    @Getter @Setter public List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado;
    @Getter @Setter public List<AlumnosEncuestas> alumnosEncuesta = new ArrayList<>();
    @Getter @Setter public List<ListaDatosAvanceEncuestaServicio> dtoLDAES, dtoLDAES1, dtoLDAES2, dtoLDAES3;
    @Getter @Setter public List<ViewEstudianteAsesorAcademico> alumnosEncuestas;
    @Getter @Setter public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> dtoAESPG, dtoAESPG1, dtoAESPGFilter;



    ////////////////////////////////////Comparadores\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
    @Getter @Setter public Comparador<ResultadosEncuestaSatisfaccionTsu> comparadorEST = new ComparadorEncuestaSatisfaccionEgresadosTsu();
    @Getter @Setter public Comparador<EvaluacionEstadiaResultados> comparadorEE = new ComparadorEvaluacionEstadia();
    @Getter @Setter public Comparador<EncuestaSatisfaccionEgresadosIng> comparadorESI = new ComparadorEncuestaSatisfaccionEgresadosIng();
    @Getter @Setter public Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparadorESR = new ComparadorEvaluacionEstudioSocioEconomico();

}
