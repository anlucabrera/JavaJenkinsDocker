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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;


/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEncuestaServicios implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEncuestaServicios ejbAdminAES;
    @EJB private EjbAdministracionEncuesta ejbAE;
    @Inject AdministracionEncuesta ae;
    

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        //dto.grado = Short.parseShort("11");
        //obtenerListaPeriodosEscolaresEvaluacion();
    }

    public void seguimientoEncuestaServiciosIyE(){
        try {
                dto.listaEvaCompleta = new ArrayList<>();
                dto.listaEvaIncompleta = new ArrayList<>();
                dto.listaEvaNA = new ArrayList<>();
                dto.alumnosEncuestaUnion = ejbAdminAES.obtenerEstudiantesSaiiutyCE().getValor();
                dto.alumnosEncuestaUnion.forEach(x -> {
                    try {
                        EncuestaServiciosResultados listaCompletaRes = ejbAdminAES.obtenerResultadosEncServXMatricula(x.getMatricula());

                        if (listaCompletaRes != null) {
                            if (dto.comparador.isCompleto(listaCompletaRes)) {
                                ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                        x.getGrado(), x.getGrupo(), x.getTutor());
                                dto.listaEvaCompleta.add(encuestasCompletas);
                            }
                            if (!dto.comparador.isCompleto(listaCompletaRes)) {
                                ListadoEvaluacionEgresados encuestasInCompletas = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                        x.getGrado(), x.getGrupo(), x.getTutor());
                                dto.listaEvaIncompleta.add(encuestasInCompletas);
                            }
                        } else {
                            ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                    x.getGrado(), x.getGrupo(), x.getTutor());
                            dto.listaEvaNA.add(sinIngresar);
                        }
                    } catch (Throwable e) {
                        Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
                    }

                });
            } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaServiciosDirector(){
        try {
            dto.listaEvaCompleta = new ArrayList<>();
            dto.listaEvaIncompleta = new ArrayList<>();
            dto.listaEvaNA = new ArrayList<>();
            dto.alumnosEncuestaUnion = ejbAdminAES.obtenerResultadosXDirector(ae.getDto().cveDirector);
            dto.alumnosEncuestaUnion.forEach(x -> {
                try {
                    EncuestaServiciosResultados listaCompletaRes = ejbAdminAES.obtenerResultadosEncServXMatricula(x.getMatricula());

                    if (listaCompletaRes != null) {
                        if (dto.comparador.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                    x.getGrado(), x.getGrupo(), x.getTutor());
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparador.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasInCompletas = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                    x.getGrado(), x.getGrupo(), x.getTutor());
                            dto.listaEvaIncompleta.add(encuestasInCompletas);
                        }
                    } else {
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                x.getGrado(), x.getGrupo(), x.getTutor());
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
                }

            });

        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seguimientoEncuestaServiciosTutor(){
        try {
            Short grado = 11;
            dto.listaEvaCompleta = new ArrayList<>();
            dto.listaEvaIncompleta = new ArrayList<>();
            dto.listaEvaNA = new ArrayList<>();
            dto.alumnosEncuestaUnion = ejbAdminAES.obtenerResultadosXTutor(ae.getDto().cveTrabajador, ae.getDto().usuarioNomina);
            dto.alumnosEncuestaUnion.forEach(x -> {
                try {
                    EncuestaServiciosResultados listaCompletaRes = ejbAdminAES.obtenerResultadosEncServXMatricula(x.getMatricula());

                    if (listaCompletaRes != null) {
                        if (dto.comparador.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                    x.getGrado(), x.getGrupo(), x.getTutor());
                            dto.listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!dto.comparador.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasInCompletas = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                    x.getGrado(), x.getGrupo(), x.getTutor());
                            dto.listaEvaIncompleta.add(encuestasInCompletas);
                        }
                    } else {
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(x.getMatricula(), x.getNombre(), x.getSiglas(),
                                x.getGrado(), x.getGrupo(), x.getTutor());
                        dto.listaEvaNA.add(sinIngresar);
                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
                }

            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void obtenerListaPeriodosEscolaresEvaluacion() {
        dto.setListaEvaluaciones(ejbAE.obtenerListaEvaluaciones("Servicios", "Servicios (fusionada)"));
        //dto.setPeriodo(dto.getListaEvaluaciones().get(0).getPeriodo());
    }
    
    public void consultarEncuestasPasadas() {
        try {
                dto.alumnos = new ArrayList<>();
                dto.alumnosFilter = new ArrayList<>();
                List<EncuestaServiciosResultados> listaESR = new ArrayList<>();
                //System.out.println("Periodo:"+ dto.periodo);
                listaESR = ejbAdminAES.obtenerListaEncuestasPorPeriodo(dto.periodo).getValor();
                //System.out.println("Total:"+ listaESR.size());
                listaESR.forEach(esr -> {
                    try {
                        if(dto.comparador.isCompleto(esr)){
                            DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE dtoP = ejbAdminAES.obtenerAlumnosNoAccedieron(esr.getEncuestaServiciosResultadosPK().getEvaluador(), dto.grado).getValor();
                            ///System.out.println(dtoP);
                            //DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE dtoA = ejbAdminAES.obtenerEstudiantesSaiiutyCE(esr.getEncuestaServiciosResultadosPK().getEvaluador()).getValor();
                            if(dtoP.getMatricula() == null) return;
                            dto.alumnos.add(new DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE(dtoP.getMatricula(), dtoP.getNombre(), dtoP.getGrado(), dtoP.getSiglas(), 
                                    dtoP.getGrupo(), dtoP.getTutor(), dtoP.getCveMaestro(), dtoP.getCveDirector()));
                            //System.out.println("DTO:"+dto.alumnos);
                        }
                    } catch (Throwable e) {
                        Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
            } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }

}
