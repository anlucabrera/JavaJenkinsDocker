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
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaTsu;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosTsu;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestasTsu;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeacion
 */
@Named
@SessionScoped
public class AdministracionEncuestaTsu implements Serializable{

    private static final long serialVersionUID = 4320030263170989982L;
    
    @Getter @Setter private Boolean esDeIyE,director;
    @Getter @Setter Integer cveTrabajador,usuarioNomina;
    @Getter @Setter String cveDirector;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listaEvaCompleta, listaEvaIncompleta, listaEvaNA, listaFiltrado;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listEvaCompletaDir, listEvaIncompletaDir, listEvaNADir, listaFiltradoDir;
    @Getter @Setter private ListadoEvaluacionEgresados objListadoEvaEgre;
    @Getter @Setter private List<AlumnosEncuestasTsu> listAlumnos;
    
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEncuestaTsu ejbAdminEncTsu ;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    
    @PostConstruct
    public void init() {
        try {
            Long inicio = System.currentTimeMillis();
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuestaTsu.init() inicio de componentes:" + inicio);
            String verdadero = "Verdadero";
            try {
                if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
                    usuarioNomina = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
                    cveTrabajador = logonMB.getListaUsuarioClaveNomina().getCvePersona();
                    cveDirector = cveTrabajador.toString();
                    if (!ejbAdmEncuesta.esDirectorDeCarrera(2, 2, 18, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
//                        System.out.println("Es director de carrera:" + verdadero);
                        director = true;
                        seguimientoEncuestaDirectores();
                    }
                    if (logonMB.getPersonal().getAreaOperativa() == 9 || usuarioNomina.equals(579)) {
                        //|| claveTrabajador.equals(579) esta parte va dentro del if de arriba
                        esDeIyE = true;
                        seguimientoEncuestaIyE();
                    }
                }
            } catch (Throwable e) {
                director = false;
                esDeIyE = false;
                Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
            }

            Long fin = System.currentTimeMillis();
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuestaTsu.init() Fin de los componentes" + fin);

            Long retardo = inicio - fin;
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuestaTsu.init() El retardo de carga de componentes es:" + retardo);

        } catch (Exception e) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionControlInterno.init() e: " + e.getMessage());
        }
    }
    
    public void seguimientoEncuestaIyE() {
        try {
            listaEvaCompleta = new ArrayList<>();
            listaEvaIncompleta = new ArrayList<>();
            listaEvaNA = new ArrayList<>();
            listAlumnos = ejbAdminEncTsu.obtenerListaAlumnosNoAccedieron().stream().filter(x -> x.getGrado()==6).collect(Collectors.toList());
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuestaTsu.seguimientoEncuestaIyE()"+ listAlumnos);
            Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
            listAlumnos.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu listaCompletaRes = ejbAdminEncTsu.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if (listaCompletaRes != null) {
                        if (comparador.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasCompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listaEvaCompleta.add(encuestasCompletas);
                        }
                        if (!comparador.isCompleto(listaCompletaRes)) {
                            ListadoEvaluacionEgresados encuestasIncompletas = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listaEvaIncompleta.add(encuestasIncompletas);
                        }
                    } else {
                        ListadoEvaluacionEgresados sinIngresar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                        listaEvaNA.add(sinIngresar);
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
    
    public void seguimientoEncuestaDirectores(){
        try {
            listEvaCompletaDir = new ArrayList<>();
            listEvaIncompletaDir = new ArrayList<>();
            listEvaNADir = new ArrayList<>();
            listAlumnos = ejbAdminEncTsu.obtenerAlumnosPorDirector(cveDirector).stream().filter(x -> x.getGrado() == 6).collect(Collectors.toList());
            Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
            listAlumnos.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu encuestasCompletas = ejbAdminEncTsu.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if(encuestasCompletas != null){
                        if(comparador.isCompleto(encuestasCompletas)){
                            ListadoEvaluacionEgresados encuestaCOmpleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listEvaCompletaDir.add(encuestaCOmpleta);
                        }
                        if(!comparador.isCompleto(encuestasCompletas)){
                            ListadoEvaluacionEgresados encuestaIncompleta = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listEvaIncompletaDir.add(encuestaIncompleta);
                        }
                    }else{
                        ListadoEvaluacionEgresados sinAccesar = new ListadoEvaluacionEgresados(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                        listEvaNADir.add(sinAccesar);
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
