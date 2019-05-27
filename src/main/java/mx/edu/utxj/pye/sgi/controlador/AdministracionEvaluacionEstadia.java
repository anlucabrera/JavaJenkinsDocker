/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionTutores;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEvaluacionEstadia implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;
    
    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEvaluacionEstadia ejbAdminAES;
    @Inject AdministracionEncuesta controlerAE;
    
    @PostConstruct
    public void init(){
        dto.listaEvaCompleta = new ArrayList<>();
        dto.listaEvaIncompleta = new ArrayList<>();
        dto.listaEvaNA = new ArrayList<>();
    }
    
    public void seguimientoEncuestaIyE() {
        try {
            dto.alumnosEncuestas = ejbAdminAES.obtenerEstudiantesAsesorAcademico();
            dto.alumnosEncuestas.forEach(x -> {
                try {
                    EvaluacionEstadiaResultados listaCompletaRes = ejbAdminAES.obtenerResultadosEvaluacionPorAlumno(x.getMatricula());

                    if (listaCompletaRes != null) {
                        if (dto.comparadorEE.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApellidoPatTutor()+ " " + x.getApellidoMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombreAlumno()+ " " + x.getApellidoPaternoAlumno()+ " " + x.getApellidoMaternoAlumno(), 
                                    x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorEE.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApellidoPatTutor()+ " " + x.getApellidoMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombreAlumno()+ " " + x.getApellidoPaternoAlumno()+ " " + x.getApellidoMaternoAlumno(), 
                                    x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if (x.getNombreTutor() != null) {
                            tutorAsignado = x.getNombreTutor() + " " + x.getApellidoPatTutor()+ " " + x.getApellidoMatTutor();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombreAlumno()+ " " + x.getApellidoPaternoAlumno()+ " " + x.getApellidoMaternoAlumno(), 
                                    x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEvaluacionEstadia.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEvaluacionEstadia.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void seguimientoEncuestaDirectores(){
        try {
            dto.alumnosEncuestas = ejbAdminAES.obtenerEstudiantesPorDirector(controlerAE.getDto().cveDirector);
                dto.alumnosEncuestas.forEach(x -> {
                    try {
                        EvaluacionEstadiaResultados encuestasCompletas = ejbAdminAES.obtenerResultadosEvaluacionPorAlumno(x.getMatricula());
                        if (encuestasCompletas != null) {
                            if (dto.comparadorEE.isCompleto(encuestasCompletas)) {
                                String tutorAsignado = "No hay tutor asignado";
                                if (x.getNombreTutor() != null) {
                                    tutorAsignado = x.getNombreTutor() + " " + x.getApellidoPatTutor()+ " " + x.getApellidoMatTutor();
                                }
                                ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombreAlumno()+ " " + x.getApellidoPaternoAlumno()+ " " + x.getApellidoMaternoAlumno(), 
                                    x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                                dto.listaEvaCompleta.add(encuestaCOmpleta);
                            }
                            if (!dto.comparadorEE.isCompleto(encuestasCompletas)) {
                                String tutorAsignado = "No hay tutor asignado";
                                if (x.getNombreTutor() != null) {
                                    tutorAsignado = x.getNombreTutor() + " " + x.getApellidoPatTutor()+ " " + x.getApellidoMatTutor();
                                }
                                ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombreAlumno()+ " " + x.getApellidoPaternoAlumno()+ " " + x.getApellidoMaternoAlumno(), 
                                    x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                                dto.listaEvaIncompleta.add(encuestaIncompleta);
                            }
                        } else {
                            String tutorAsignado = "No hay tutor asignado";
                            if (x.getNombreTutor() != null) {
                                tutorAsignado = x.getNombreTutor() + " " + x.getApellidoPatTutor()+ " " + x.getApellidoMatTutor();
                            }
                            ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                    x.getNombreAlumno()+ " " + x.getApellidoPaternoAlumno()+ " " + x.getApellidoMaternoAlumno(), 
                                    x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            dto.listaEvaNA.add(sinAccesar);
                        }
                    } catch (Throwable e) {
                        Logger.getLogger(AdministracionEvaluacionEstadia.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
            
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEvaluacionEstadia.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
//    public void seguimientoEncuestaTutores(){
//        try {
//            Short grado = 11;
//            listaEvaCompleta = new ArrayList<>();
//            listaEvaIncompleta = new ArrayList<>();
//            listaEvaNA = new ArrayList<>();
//            alumnosEncuesta = ejbAdminAES.obtenerResultadosXTutor(cveTrabajador);
//            Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
//            alumnosEncuesta.forEach(x -> {
//                try {
//                    EncuestaServiciosResultados listaCompletaRes = ejbAdminAES.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
//
//                    if (listaCompletaRes != null) {
//                        if (comparador.isCompleto(listaCompletaRes)) {
//                            String tutorAsignado = "No hay tutor asignado";
//                            if (x.getNombreTutor() != null) {
//                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
//                            }
//                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
//                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
//                            listaEvaCompleta.add(encuestasCompletas);
//                        }
//                        if (!comparador.isCompleto(listaCompletaRes)) {
//                            String tutorAsignado = "No hay tutor asignado";
//                            if (x.getNombreTutor() != null) {
//                                tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
//                            }
//                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
//                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
//                            listaEvaIncompleta.add(encuestasIncompletas);
//                        }
//                    } else {
//                        String tutorAsignado = "No hay tutor asignado";
//                        if (x.getNombreTutor() != null) {
//                            tutorAsignado = x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor();
//                        }
//                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
//                                x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
//                        listaEvaNA.add(sinIngresar);
//                    }
//                } catch (Throwable e) {
//                    Logger.getLogger(AdministracionEvaluacionEstadia.class.getName()).log(Level.SEVERE, null, e);
//                }
//
//            });
//        } catch (Throwable e) {
//            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
//            Logger.getLogger(AdministracionEvaluacionEstadia.class.getName()).log(Level.SEVERE, null, e);
//        }
//    }
}
