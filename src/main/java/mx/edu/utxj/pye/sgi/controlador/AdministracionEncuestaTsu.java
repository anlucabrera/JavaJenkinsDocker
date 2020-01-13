/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

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
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaTsu;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
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
public class AdministracionEncuestaTsu implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;
    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEncuestaTsu ejbAdminEncTsu ;
    @EJB private EjbAdministracionEncuestaServicios ejbES;
    @Inject AdministracionEncuesta controlerAE;


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;



    @PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;

    }
    
    public void seguimientoEncuestaIyE() {
        try {
            dto.listaEvaCompleta = new ArrayList<>();
            dto.listaEvaIncompleta = new ArrayList<>();
            dto.listaEvaNA = new ArrayList<>();
            dto.alumnosEncuesta = ejbES.obtenerAlumnosNoAccedieron().stream().filter(x -> x.getGrado()== 6).collect(Collectors.toList());
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu listaCompletaRes = ejbAdminEncTsu.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if (listaCompletaRes != null) {
                        if (dto.comparadorEST.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparadorEST.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            dto.listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void seguimientoEncuestaDirector(){
        try {
            dto.listaEvaCompleta = new ArrayList<>();
            dto.listaEvaIncompleta = new ArrayList<>();
            dto.listaEvaNA = new ArrayList<>();
            dto.alumnosEncuesta = ejbES.obtenerResultadosXDirector(controlerAE.getDto().cveDirector).stream().filter(x -> x.getGrado() == 6).collect(Collectors.toList());
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu encuestasCompletas = ejbAdminEncTsu.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if(encuestasCompletas != null){
                        if(dto.comparadorEST.isCompleto(encuestasCompletas)){
                            ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            dto.listaEvaCompleta.add(encuestaCOmpleta);
                        }
                        if(!dto.comparadorEST.isCompleto(encuestasCompletas)){
                            ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            dto.listaEvaIncompleta.add(encuestaIncompleta);
                        }
                    }else{
                        ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                        dto.listaEvaNA.add(sinAccesar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
                }
            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaTutor(){
        try {
            dto.grado = 6;
            dto.listaEvaCompleta = new ArrayList<>();
            dto.listaEvaIncompleta = new ArrayList<>();
            dto.listaEvaNA = new ArrayList<>();
            List<AlumnosEncuestas> ae = ejbES.obtenerResultadosXTutor(controlerAE.getDto().cveTrabajador);
            dto.alumnosEncuesta = ae.stream().filter(x -> x.getGrado().equals(dto.grado)).collect(Collectors.toList());
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu encuestasCompletas = ejbAdminEncTsu.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if(encuestasCompletas != null){
                        if(dto.comparadorEST.isCompleto(encuestasCompletas)){
                            ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            dto.listaEvaCompleta.add(encuestaCOmpleta);
                        }
                        if(!dto.comparadorEST.isCompleto(encuestasCompletas)){
                            ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            dto.listaEvaIncompleta.add(encuestaIncompleta);
                        }
                    }else{
                        ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                        dto.listaEvaNA.add(sinAccesar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
                }
            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
