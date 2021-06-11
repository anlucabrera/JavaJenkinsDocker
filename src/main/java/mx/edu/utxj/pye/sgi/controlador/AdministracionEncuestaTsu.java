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
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaTsu;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
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
public class AdministracionEncuestaTsu implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;
    @Getter
    @Setter
    private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEncuestaTsu ejbAdminEncTsu ;
    @EJB private EjbAdministracionEncuestaServicios ejbES;
    @Inject
    AdministracionEncuesta controlerAE;


@Inject
LogonMB logonMB;
@Getter
private Boolean cargado = false;



    @PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        dto.listaEvaCompleta = new ArrayList<>();
        dto.listaEvaIncompleta = new ArrayList<>();
        dto.listaEvaNA = new ArrayList<>();

    }

    public void seguimientoEncuestaIyE() {
        try {
            dto.alumnosEncuestaCE = ejbAdminEncTsu.obtenerAlumnosNoAccedieron().getValor();
            dto.alumnosEncuestaCE.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu listaCompletaRes = ejbAdminEncTsu.getResultadoEncPorEvaluador(x.getAlumnos().getMatricula());
                    if (listaCompletaRes != null) {
                        if (dto.comparadorEST.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if(x.getGrupos().getTutor()!= null){
                                tutorAsignado = x.getTutor().getNombre();
                            }
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                    x.getAreasUniversidad().getSiglas(), (short) x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorEST.isCompleto(listaCompletaRes)) {
                            String tutorAsginado = "No hay tutor asignado";
                            if(x.getGrupos().getTutor()!= null){
                                tutorAsginado = x.getTutor().getNombre();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                    x.getAreasUniversidad().getSiglas(), (short) x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), tutorAsginado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if(x.getGrupos().getTutor()!= null){
                            tutorAsignado = x.getTutor().getNombre();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                x.getAreasUniversidad().getSiglas(), (short)x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
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

    public void seguimientoEncuestaDirector(){
        try {
            dto.alumnosEncuestaCE = ejbAdminEncTsu.obtenerResultadosXDirector(controlerAE.getDto().cveDirector);
            dto.alumnosEncuestaCE.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu encuestasCompletas = ejbAdminEncTsu.getResultadoEncPorEvaluador(x.getAlumnos().getMatricula());
                    if (encuestasCompletas != null) {
                        if (dto.comparadorEST.isCompleto(encuestasCompletas)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if(x.getGrupos().getTutor()!= null){
                                tutorAsignado = x.getTutor().getNombre();
                            }
                            ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                x.getAreasUniversidad().getSiglas(), (short)x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestaCOmpleta);
                        }
                        if (!dto.comparadorEST.isCompleto(encuestasCompletas)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if(x.getGrupos().getTutor() != null){
                                tutorAsignado = x.getTutor().getNombre();
                            }
                            ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                x.getAreasUniversidad().getSiglas(), (short)x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
                            dto.listaEvaIncompleta.add(encuestaIncompleta);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if(x.getGrupos().getTutor() != null){
                            tutorAsignado = x.getTutor().getNombre();
                        }
                        ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                x.getAreasUniversidad().getSiglas(), (short)x.getAlumnos().getGrupo().getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
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

    public void seguimientoEncuestaTutor(){
        try {
            dto.alumnosEncuestaCE = ejbAdminEncTsu.obtenerResultadosXTutor(controlerAE.getDto().cveTrabajador);
            dto.alumnosEncuestaCE.forEach(x -> {
                try {

                    ResultadosEncuestaSatisfaccionTsu listaCompletaRes = ejbAdminEncTsu.getResultadoEncPorEvaluador(x.getAlumnos().getMatricula());
                    if (listaCompletaRes != null) {
                        if (dto.comparadorEST.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = x.getTutor().getNombre();
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                    x.getAreasUniversidad().getSiglas(), x.getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorEST.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = x.getTutor().getNombre();
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                    x.getAreasUniversidad().getSiglas(), x.getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = x.getTutor().getNombre();
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getAlumnos().getMatricula(),
                                    x.getPersonas().getNombre() + " " + x.getPersonas().getApellidoPaterno()+ " " + x.getPersonas().getApellidoMaterno(),
                                    x.getAreasUniversidad().getSiglas(), x.getGrado(), x.getGrupos().getLiteral().toString(), tutorAsignado);
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

}
