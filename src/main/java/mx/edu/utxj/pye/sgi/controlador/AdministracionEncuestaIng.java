
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
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestasTsu;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEncuestaIng implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;
    
    @Getter @Setter private Boolean esDeIyE,director,aperturas;
    @Getter @Setter Integer cveTrabajador,usuarioNomina;
    @Getter @Setter String cveDirector;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listEvaCompletaDir, listEvaIncompletaDir, listEvaNADir, listaFiltradoDir;
    @Getter @Setter private ListadoEvaluacionEgresados objListadoEvaEgre;
    @Getter @Setter private List<AlumnosEncuestasTsu> listAlumnos;
    @Getter @Setter private List<AlumnosEncuestas> alumnosEncuesta, alumnos11Ing;
    @Getter @Setter private List<DtoAlumnosEncuesta.DtoTutores> dtoTutores;
    @Getter @Setter private List<DtoAlumnosEncuesta> dtoAlumnos;
    @Getter @Setter private String nombreCompletoTutor;
    
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEncuestaIng ejbAdmEI;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    
    @PostConstruct
    public void init(){
        Long inicio= System.currentTimeMillis();
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)){
                usuarioNomina=Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
                cveTrabajador= logonMB.getListaUsuarioClaveNomina().getCvePersona();
                cveDirector = cveTrabajador.toString();
                Integer apertura = ejbAdmEI.getAperturaActiva().getApertura();
                if (apertura != null) {
                    aperturas = true;
                    if (!ejbAdmEncuesta.esDirectorDeCarrera(2, 2, 18, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        director = true;
                        //seguimientoEncuestaDirectores();
                    }
                    if (logonMB.getPersonal().getAreaOperativa() == 9 || usuarioNomina.equals(579)) {
                        esDeIyE = true;
                        //seguimientoEncuestaIyE();
                    }
                }
            }
        } catch (Throwable e) {
            director = false;
            esDeIyE = false;
            aperturas = false;
            Logger.getLogger(AdministracionEncuestaIng.class.getName()).log(Level.SEVERE, null, e);
        }
        
        Long fin= System.currentTimeMillis();
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuestaTsu.init() Fin de los componentes"+ fin);
        
        Long retardo = inicio-fin;
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuestaTsu.init() El retardo de carga de componentes es:"+ retardo);
    }
    
    public void seguimientoEncuestaIyE() {
        try {
            listaEvaCompleta = new ArrayList<>();
            listaEvaIncompleta = new ArrayList<>();
            listaEvaNA = new ArrayList<>();
            Short grado = 11;
            alumnos11Ing = ejbAdmEI.obtenerAlumnosOnceavo().parallelStream().filter(x -> x.getGrado().equals(grado)).collect(Collectors.toList());
            Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();
            alumnos11Ing.forEach(x -> {
                try {
                    
                    EncuestaSatisfaccionEgresadosIng listaCompletaRes = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompletaRes != null) {
                        if (comparador.isCompleto(listaCompletaRes)) {
                            nombreCompletoTutor = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), nombreCompletoTutor);
                            listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!comparador.isCompleto(listaCompletaRes)) {
                            nombreCompletoTutor = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), nombreCompletoTutor);
                            listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        nombreCompletoTutor = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), nombreCompletoTutor);
                        listaEvaNA.add(sinIngresar);
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
            listEvaCompletaDir = new ArrayList<>();
            listEvaIncompletaDir = new ArrayList<>();
            listEvaNADir = new ArrayList<>();
            Short grado = 11;
            alumnosEncuesta = ejbAdmEI.obtenerAlumnosOnceavo().parallelStream().filter(x -> x.getCveDirector().equals(cveDirector) && x.getGrado().equals(grado)).collect(Collectors.toList());
            //ejbAdmEI.obtenerResultadosXDirector(cveDirector);
                Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();
                alumnosEncuesta.forEach(x -> {
                    try {
                        nombreCompletoTutor = x.getNombreTutor()+" "+x.getApPatTutor()+" "+x.getApMatTutor();
                        EncuestaSatisfaccionEgresadosIng encuestasCompletas = ejbAdmEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                        if (encuestasCompletas != null) {
                            if (comparador.isCompleto(encuestasCompletas)) {
                                ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                        x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), nombreCompletoTutor);
                                listEvaCompletaDir.add(encuestaCOmpleta);
                            }
                            if (!comparador.isCompleto(encuestasCompletas)) {
                                ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()),
                                        x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), nombreCompletoTutor);
                                listEvaIncompletaDir.add(encuestaIncompleta);
                            }
                        } else {
                            ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), 
                                    x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), nombreCompletoTutor);
                            listEvaNADir.add(sinAccesar);
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
