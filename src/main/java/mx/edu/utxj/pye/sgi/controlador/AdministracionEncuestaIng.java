
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEncuestaIng implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEncuestaIng ejbAdmEI;
    

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        dto.listaEvaCompleta = new ArrayList<>();
        dto.listaEvaIncompleta = new ArrayList<>();
        dto.listaEvaNA = new ArrayList<>();
    }
    
    public void seguimientoEncuestaIyE() {
        try {

            Short grado = 11;
            dto.alumnosEncuesta = ejbAdmEI.obtenerAlumnosOnceavo().parallelStream().filter(x -> x.getGrado().equals(grado)).collect(Collectors.toList());
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    
                    EncuestaSatisfaccionEgresadosIng listaCompletaRes = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompletaRes != null) {
                        if (dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = "No hay tutor asignado";
                            if(x.getNombreTutor() != null){
                                tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsginado = "No hay tutor asignado";
                            if(x.getNombreTutor() != null){
                                tutorAsginado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsginado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = "No hay tutor asignado";
                        if(x.getNombreTutor() != null){
                            tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                        }
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
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
    
    public void seguimientoEncuestaDirectores(){
        try {
            Short grado = 11;
            dto.alumnosEncuesta = ejbAdmEI.obtenerAlumnosOnceavo().parallelStream().filter(x -> x.getCveDirector().equals(dto.cveDirector) && x.getGrado().equals(grado)).collect(Collectors.toList());
                dto.alumnosEncuesta.forEach(x -> {
                    try {
                        EncuestaSatisfaccionEgresadosIng encuestasCompletas = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                        if (encuestasCompletas != null) {
                            if (dto.comparadorESI.isCompleto(encuestasCompletas)) {
                                String tutorAsignado = "No hay tutor asignado";
                                if(x.getNombreTutor() != null){
                                    tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                                }
                                ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                        x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                                dto.listaEvaCompleta.add(encuestaCOmpleta);
                            }
                            if (!dto.comparadorESI.isCompleto(encuestasCompletas)) {
                                String tutorAsignado = "No hay tutor asignado";
                                if(x.getNombreTutor() != null){
                                    tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                                }
                                ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                        x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                                dto.listaEvaIncompleta.add(encuestaIncompleta);
                            }
                        } else {
                            String tutorAsignado = "No hay tutor asignado";
                            if(x.getNombreTutor() != null){
                                tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            }
                            ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
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
            dto.alumnosEncuesta = ejbAdmEI.obtenerResultadosXTutor(dto.cveTrabajador);
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    
                    EncuestaSatisfaccionEgresadosIng listaCompletaRes = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompletaRes != null) {
                        if (dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorESI.isCompleto(listaCompletaRes)) {
                            String tutorAsginado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsginado);
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        String tutorAsignado = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), tutorAsignado);
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
