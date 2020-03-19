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
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaCondicionesEstudio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
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


/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEncuestaCondicionesEstudio implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;

    @Getter
    @Setter
    private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbEncuestaCondicionesEstudio ejbECE;
    @Inject AdministracionEncuesta ae;
    

@Inject
LogonMB logonMB;
@Getter
private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;

    }

    public void seguimientoEncuestaServiciosIyE(){
        try {
                dto.listaEvaCompleta = new ArrayList<>();
                dto.listaEvaIncompleta = new ArrayList<>();
                dto.listaEvaNA = new ArrayList<>();
                dto.alumnosEncuesta = ejbECE.obtenerAlumnosNoAccedieron().getValor();
                dto.alumnosEncuesta.forEach(x -> {
                    try {
                        EncuestaCondicionesEstudio listaCompletaRes = ejbECE.obtenerResultadoEncuesta(Integer.parseInt(x.getAlumnos().getMatricula()));

                        if (listaCompletaRes != null) {
                            Integer matricula = Integer.parseInt(x.getAlumnos().getMatricula());
                            String nombreAlumno = x.getPersonas().getNombre() +" "+ x.getPersonas().getApellidoPat() +" "+ x.getPersonas().getApellidoMat();
                            String siglas = x.getCarrerasCgut().getAbreviatura();
                            Short grado = x.getAlumnos().getGradoActual();
                            String grupo = x.getGrupos().getIdGrupo();
                            if (dto.comparadorECE.isCompleto(listaCompletaRes)) {
                                dto.nombreCompletoTutor = "No hay tutor asignado";
                                if (x.getGrupos().getCveMaestro() != null) {
                                    dto.nombreCompletoTutor = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                                }

                                ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(matricula, nombreAlumno, siglas, grado, grupo, dto.nombreCompletoTutor);
                                dto.listaEvaCompleta.add(encuestasCompletas);
                            }
                            if (!dto.comparadorECE.isCompleto(listaCompletaRes)) {
                                dto.nombreCompletoTutor = "No hay tutor asignado";
                                if (x.getGrupos().getCveMaestro() != null) {
                                    dto.nombreCompletoTutor = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                                }
                                ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(matricula, nombreAlumno, siglas, grado, grupo, dto.nombreCompletoTutor);
                                dto.listaEvaIncompleta.add(encuestasIncompletas);
                            }
                        } else {
                            dto.nombreCompletoTutor = "No hay tutor asignado";
                            if (x.getGrupos().getCveMaestro() != null) {
                                dto.nombreCompletoTutor = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            }
                            ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), dto.nombreCompletoTutor);
                            dto.listaEvaNA.add(sinIngresar);
                        }
                    } catch (Throwable e) {
                        Logger.getLogger(AdministracionEncuestaCondicionesEstudio.class.getName()).log(Level.SEVERE, null, e);
                    }

                });

            dto.alumnosEncuestaCE = ejbECE.obtenerAlumnosNoAccedieronCE().getValor();
            dto.alumnosEncuestaCE.forEach(x -> {
                try {
                    EncuestaCondicionesEstudio listaCompletaRes = ejbECE.obtenerResultadoEncuesta(x.getAlumnos().getMatricula());

                    if (listaCompletaRes != null) {
                        Integer matricula = x.getAlumnos().getMatricula();
                        String nombreAlumno = x.getPersonas().getNombre() +" "+ x.getPersonas().getApellidoPaterno() +" "+ x.getPersonas().getApellidoMaterno();
                        String siglas = x.getAreasUniversidad().getSiglas();
                        Short grado = (short) x.getAlumnos().getGrupo().getGrado();
                        String grupo = x.getGrupos().getLiteral().toString();
                        if (dto.comparadorECE.isCompleto(listaCompletaRes)) {
                            dto.nombreCompletoTutor = "No hay tutor asignado";
                            if (x.getGrupos().getTutor() != null) {
                                dto.nombreCompletoTutor = x.getTutor().getNombre();
                            }

                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(matricula, nombreAlumno, siglas, grado, grupo, dto.nombreCompletoTutor);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorECE.isCompleto(listaCompletaRes)) {
                            dto.nombreCompletoTutor = "No hay tutor asignado";
                            if (x.getGrupos().getTutor() != null) {
                                dto.nombreCompletoTutor = x.getTutor().getNombre();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(matricula, nombreAlumno, siglas, grado, grupo, dto.nombreCompletoTutor);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        dto.nombreCompletoTutor = "No hay tutor asignado";
                        if (x.getGrupos().getTutor() != null) {
                            dto.nombreCompletoTutor = x.getTutor().getNombre();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno() + " " + x.getPersonas().getApellidoMaterno(),
                                x.getAreasUniversidad().getSiglas(), (short) x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), dto.nombreCompletoTutor);
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaCondicionesEstudio.class.getName()).log(Level.SEVERE, null, e);
                }

            });
            } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaCondicionesEstudio.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaServiciosDirector(){
        try {
            dto.listaEvaCompleta = new ArrayList<>();
            dto.listaEvaIncompleta = new ArrayList<>();
            dto.listaEvaNA = new ArrayList<>();
            //System.out.println("Director:"+ae.getDto().cveTrabajador +"-"+ ae.getDto().cveDirector);
            dto.alumnosEncuesta = ejbECE.obtenerResultadosXDirector(ae.getDto().cveDirector);
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    EncuestaCondicionesEstudio encuestasCompletas = ejbECE.obtenerResultadoEncuesta(Integer.parseInt(x.getAlumnos().getMatricula()));
                    if (encuestasCompletas != null) {
                        if (dto.comparadorECE.isCompleto(encuestasCompletas)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getGrupos().getCveMaestro() != null) {
                                tutorAsignado = x.getTutor().getNombre() + " " + x.getTutor().getApellidoPat() + " " + x.getTutor().getApellidoMat();
                            }
                            ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getAlumnos().getMatricula()),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPat() + " " + x.getPersonas().getApellidoMat(),
                                    x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestaCOmpleta);
                        }
                        if (!dto.comparadorECE.isCompleto(encuestasCompletas)) {
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
                    Logger.getLogger(AdministracionEncuestaCondicionesEstudio.class.getName()).log(Level.SEVERE, null, e);
                }
            });

            dto.alumnosEncuestaCE = ejbECE.obtenerResultadosXDirectorCE(ae.getDto().cveTrabajador);
            dto.alumnosEncuestaCE.forEach(x -> {
                try {
                    EncuestaCondicionesEstudio listaCompletaRes = ejbECE.obtenerResultadoEncuesta(x.getAlumnos().getMatricula());

                    if (listaCompletaRes != null) {
                        Integer matricula = x.getAlumnos().getMatricula();
                        String nombreAlumno = x.getPersonas().getNombre() +" "+ x.getPersonas().getApellidoPaterno() +" "+ x.getPersonas().getApellidoMaterno();
                        String siglas = x.getAreasUniversidad().getSiglas();
                        Short grado = (short) x.getAlumnos().getGrupo().getGrado();
                        String grupo = x.getGrupos().getLiteral().toString();
                        if (dto.comparadorECE.isCompleto(listaCompletaRes)) {
                            dto.nombreCompletoTutor = "No hay tutor asignado";
                            if (x.getGrupos().getTutor() != null) {
                                dto.nombreCompletoTutor = x.getTutor().getNombre();
                            }

                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(matricula, nombreAlumno, siglas, grado, grupo, dto.nombreCompletoTutor);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorECE.isCompleto(listaCompletaRes)) {
                            dto.nombreCompletoTutor = "No hay tutor asignado";
                            if (x.getGrupos().getTutor() != null) {
                                dto.nombreCompletoTutor = x.getTutor().getNombre();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(matricula, nombreAlumno, siglas, grado, grupo, dto.nombreCompletoTutor);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        dto.nombreCompletoTutor = "No hay tutor asignado";
                        if (x.getGrupos().getTutor() != null) {
                            dto.nombreCompletoTutor = x.getTutor().getNombre();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno() + " " + x.getPersonas().getApellidoMaterno(),
                                x.getAreasUniversidad().getSiglas(), (short) x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), dto.nombreCompletoTutor);
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaCondicionesEstudio.class.getName()).log(Level.SEVERE, null, e);
                }

            });

        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaCondicionesEstudio.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
