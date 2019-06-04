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
    @Getter @Setter public Boolean cargada, finalizado, estSexto, estOnceavo ,esDeIyE,director, tutor, esSecretario, planeacion, ESActiva, ESTsuActiva,
            ESIngActiva, ESEActiva, EEActiva;
    @Getter @Setter public String evaluador, valor, cveDirector, nombreCompletoTutor, tipoEncuesta, siglas, abreviatura;
    @Getter @Setter public Integer evaluadorr,cveTrabajador,usuarioNomina, cveMaestro;
    @Getter @Setter public Long total,total2;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ////////////////////////General entity's\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Evaluaciones evaluacion;
    @Getter @Setter public Alumnos alumno;
    @Getter @Setter public PeriodosEscolares periodoEsc;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////Maps and lists generals\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Map<String,Long> collect = new HashMap<>();
    @Getter @Setter public Map<String,Long> collect2 = new HashMap<>();
    @Getter @Setter public Map<String, String> respuestas;
    @Getter @Setter public List<SelectItem> respuestasPosibles;
    @Getter @Setter public List<Apartado> apartados;
    //////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    @Getter @Setter public EncuestaServiciosResultados resultado;
    @Getter @Setter public ResultadosEncuestaSatisfaccionTsu resultadoREST;
    @Getter @Setter public EncuestaSatisfaccionEgresadosIng resultadoESEI;
    @Getter @Setter public ListadoGraficaEncuestaServicios objListAlumnEnSer, objListGrafConcen;
    @Getter @Setter public ListaDatosAvanceEncuestaServicio objAvanceEncSer;
    @Getter @Setter public List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado,
            listEvaCompletaDir, listEvaIncompletaDir, listEvaNADir, listaFiltradoDir;
    @Getter @Setter public List<AlumnosEncuestas> alumnosEncuesta = new ArrayList<>();
    @Getter @Setter public List<ListadoGraficaEncuestaServicios> listGrafEncServ;
    @Getter @Setter public List<ListadoGraficaEncuestaServicios> listDatosGraf;
    @Getter @Setter public List<ListadoGraficaEncuestaServicios> listaIncompleta;
    @Getter @Setter public List<ListaDatosAvanceEncuestaServicio> avanceEncServ;
    @Getter @Setter public List<ListadoGraficaEncuestaServicios> listAlumnosEncSe;
    @Getter @Setter public List<ViewEstudianteAsesorAcademico> alumnosEncuestas;



    ////////////////////////////////////Comparadores\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Getter @Setter public Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
    @Getter @Setter public Comparador<ResultadosEncuestaSatisfaccionTsu> comparadorEST = new ComparadorEncuestaSatisfaccionEgresadosTsu();
    @Getter @Setter public Comparador<EvaluacionEstadiaResultados> comparadorEE = new ComparadorEvaluacionEstadia();
    @Getter @Setter public Comparador<EncuestaSatisfaccionEgresadosIng> comparadorESI = new ComparadorEncuestaSatisfaccionEgresadosIng();
    @Getter @Setter public Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparadorESR = new ComparadorEvaluacionEstudioSocioEconomico();

}
