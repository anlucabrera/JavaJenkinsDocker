/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEstudioSocioeconomico;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionTutores;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioSocioEconomico;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEstudioSocioeconomico implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;

    @Getter @Setter private Boolean esDeIyE,director,aperturas, tutor;
    @Getter @Setter Integer cveTrabajador,usuarioNomina;
    @Getter @Setter String cveDirector, nombreCompletoTutor;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listEvaCompletaDir, listEvaIncompletaDir, listEvaNADir, listaFiltradoDir;
    @Getter @Setter private List<AlumnosEncuestas> alumnosEncuesta;
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEstudioSocioeconomico ejbAES;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    @EJB private EjbAdministracionTutores ejbAdmTutor;


@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
    }

    public void seguimientoEncuestaIyE() {
        try {
            listaEvaCompleta = new ArrayList<>();
            listaEvaIncompleta = new ArrayList<>();
            listaEvaNA = new ArrayList<>();
            alumnosEncuesta = ejbAES.alumnosEncuestas().parallelStream().collect(Collectors.toList());
            Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparador = new ComparadorEvaluacionEstudioSocioEconomico();
            alumnosEncuesta.forEach(x -> {
                try {
                    EvaluacionesEstudioSocioeconomicoResultados listaCompletaRes = ejbAES.evaluacionesEstudioSocioeconomicoResultadoXMatricula(Integer.parseInt(x.getMatricula()));

                    if (listaCompletaRes != null) {
                        if (comparador.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!comparador.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if (x.getNombreTutor() != null) {
                            tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                        listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaDirectores(){
        try {
            listEvaCompletaDir = new ArrayList<>();
            listEvaIncompletaDir = new ArrayList<>();
            listEvaNADir = new ArrayList<>();
            alumnosEncuesta = ejbAES.alumnosEncuestasPorDirector(cveDirector).stream().collect(Collectors.toList());
                Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparador = new ComparadorEvaluacionEstudioSocioEconomico();
                alumnosEncuesta.forEach(x -> {
                    try {
                        EvaluacionesEstudioSocioeconomicoResultados encuestasCompletas = ejbAES.evaluacionesEstudioSocioeconomicoResultadoXMatricula(Integer.parseInt(x.getMatricula()));
                        if (encuestasCompletas != null) {
                            if (comparador.isCompleto(encuestasCompletas)) {
                                String tutorAsignado = "No hay tutor asignado";
                                if (x.getNombreTutor() != null) {
                                    tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                                }
                                ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                        x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                                listEvaCompletaDir.add(encuestaCOmpleta);
                            }
                            if (!comparador.isCompleto(encuestasCompletas)) {
                                String tutorAsignado = "No hay tutor asignado";
                                if (x.getNombreTutor() != null) {
                                    tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                                }
                                ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                        x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                                listEvaIncompletaDir.add(encuestaIncompleta);
                            }
                        } else {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            listEvaNADir.add(sinAccesar);
                        }
                    } catch (Throwable e) {
                        Logger.getLogger(AdministracionEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, e);
                    }
                });

        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaTutores(){
        try {
            Short grado = 11;
            listaEvaCompleta = new ArrayList<>();
            listaEvaIncompleta = new ArrayList<>();
            listaEvaNA = new ArrayList<>();
            alumnosEncuesta = ejbAES.alumnosEncuestasPorTutor(cveTrabajador);
            Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparador = new ComparadorEvaluacionEstudioSocioEconomico();
            alumnosEncuesta.forEach(x -> {
                try {
                    EvaluacionesEstudioSocioeconomicoResultados listaCompletaRes = ejbAES.evaluacionesEstudioSocioeconomicoResultadoXMatricula(Integer.parseInt(x.getMatricula()));

                    if (listaCompletaRes != null) {
                        if (comparador.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!comparador.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if (x.getNombreTutor() != null) {
                            tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                        listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
