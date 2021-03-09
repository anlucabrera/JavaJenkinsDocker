
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import org.primefaces.util.LangUtils;

/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEncuestaIng implements Serializable {

    private static final long serialVersionUID = 4320030263170989982L;

    @Getter
    @Setter
    private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB
    private EjbAdministracionEncuestaIng ejbAdmEI;
    @EJB
    private EjbAdministracionEncuesta ejbAE;
    @Inject
    LogonMB logonMB;
    @Inject
    AdministracionEncuesta ae;
    @Getter
    private Boolean cargado = false;

    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        dto.listaEvaCompleta = new ArrayList<>();
        dto.listaEvaIncompleta = new ArrayList<>();
        dto.listaEvaNA = new ArrayList<>();
        obtenerListaPeriodosEscolaresEvaluacion();
    }

    public void seguimientoEncuestaIyE() {
        try {
            dto.alumnosEncuesta = ejbAdmEI.obtenerAlumnosOnceavo().getValor();
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    EncuestaSatisfaccionEgresadosIng listaCompletaRes = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
                    if (listaCompletaRes != null) {
                        if (dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getGrupos().getCveMaestro() != null) {
                                tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            }
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsginado = "No hay tutor asignado";
                            if (x.getGrupos().getCveMaestro() != null) {
                                tutorAsginado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsginado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if (x.getGrupos().getCveMaestro() != null) {
                            tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsignado);
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaDirectores() {
        try {
            dto.alumnosEncuesta = ejbAdmEI.obtenerResultadosXDirector(ae.getDto().cveDirector);
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    EncuestaSatisfaccionEgresadosIng encuestasCompletas = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
                    if (encuestasCompletas != null) {
                        if (dto.comparadorESI.isCompleto(encuestasCompletas)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getGrupos().getCveMaestro() != null) {
                                tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            }
                            ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestaCOmpleta);
                        }
                        if (!dto.comparadorESI.isCompleto(encuestasCompletas)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getGrupos().getCveMaestro() != null) {
                                tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            }
                            ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsignado);
                            dto.listaEvaIncompleta.add(encuestaIncompleta);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if (x.getGrupos().getCveMaestro() != null) {
                            tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                        }
                        ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsignado);
                        dto.listaEvaNA.add(sinAccesar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
                }
            });

        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaTutor() {
        try {
            dto.alumnosEncuesta = ejbAdmEI.obtenerResultadosXTutor(ae.getDto().cveTrabajador);
            dto.alumnosEncuesta.forEach(x -> {
                try {

                    EncuestaSatisfaccionEgresadosIng listaCompletaRes = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
                    if (listaCompletaRes != null) {
                        if (dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getGrado(), x.getGrupos().getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getGrado(), x.getGrupos().getIdGrupo(), tutorAsignado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                x.getCarrerasCgut().getAbreviatura(), x.getGrado(), x.getGrupos().getIdGrupo(), tutorAsignado);
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void obtenerListaPeriodosEscolaresEvaluacion() {
        dto.setListaEvaluaciones(ejbAE.obtenerListaEvaluaciones("Satisfacción de egresados de ingenieria", "Satisfacción de egresados de ingenieria"));
        //dto.setPeriodo(dto.getListaEvaluaciones().get(0).getPeriodo());
    }

    public void consultarEncuestasPasadas() {
        dto.listaEvaluacionHitorico = new ArrayList<>();
        dto.listaEvaluacionFilter = new ArrayList<>();
        //System.out.println("Periodo:"+ dto.periodo);
        dto.alumnosEncuesta = ejbAdmEI.obtenerAlumnosOnceavo(dto.periodo).getValor();
        //System.out.println(dto.alumnosEncuesta);
        dto.alumnosEncuesta.forEach(dtoAE -> {
            EncuestaSatisfaccionEgresadosIng encuestaSEING = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(dtoAE.getAlumnos().getMatricula()), dtoAE.getGrupos().getGruposPK().getCvePeriodo());
            //System.out.println(encuestaSEING);
            if(encuestaSEING == null) return;
            if(dto.comparadorESI.isCompleto(encuestaSEING)) {
                String tutorAsignado = "No hay tutor asignado";
                if (dtoAE.getGrupos().getCveMaestro() != null) {
                    tutorAsignado = dtoAE.getTutor().getNombre() + " " + dtoAE.getTutor().getApellidoPat() + " " + dtoAE.getTutor().getApellidoMat();
                }
                ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(dtoAE.getAlumnos().getMatricula()),
                        dtoAE.getPersonas().getNombre() + " " + dtoAE.getPersonas().getApellidoPat() + " " + dtoAE.getPersonas().getApellidoMat(),
                        dtoAE.getCarrerasCgut().getAbreviatura(), dtoAE.getAlumnos().getGradoActual(), dtoAE.getGrupos().getIdGrupo(), tutorAsignado);
                dto.listaEvaluacionHitorico.add(encuestasCompletas);
            }
        });
    }

}
