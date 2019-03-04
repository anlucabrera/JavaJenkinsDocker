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
import mx.edu.utxj.pye.sgi.dto.ListadoEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewAlumnos;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class administracionEncTutores implements Serializable {

    private static final long serialVersionUID = 4046927970605586753L;
    
    @Getter @Setter private Integer nominaTrabajadorTutor, claveTutor;
    @Getter @Setter private Integer grupo;
    @Getter @Setter private Short grado;
    @Getter @Setter private String claveTrabajador;
    @Getter @Setter Boolean tutor, tutorIngOnceavo;
    
    @Getter @Setter List<Grupos> listGrupo;
    @Getter @Setter List<Grupos> cuatrimestre;
    @Getter @Setter Grupos grupoTutor;
    @Getter @Setter List<Alumnos> listAlumnosTutor, alumnosTutor;
    @Getter @Setter ViewAlumnos objAlumno;
    
    @Getter @Setter List<AlumnosEncuestas> alumnosNoAccedieronEnc;
    
    @Getter @Setter List<ListadoEncuestaServicios> listaEncuestaServicios,listaEncuestaServCompleta,listEncuestaServIncompleta;
    @Getter @Setter List<ListadoEncuestaServicios> listFiltradoEncServicios;
    @Getter @Setter ListadoEncuestaServicios objListEncServ;
    @Getter @Setter List<ListadoEvaluacionEgresados> listaEncuestaEgresados,listadoEvaluacionCompleta,listadoEvaluacionInCompleta;
    @Getter @Setter List<ListadoEvaluacionEgresados> listFiltradoEncEgresados;
    @Getter @Setter ListadoEvaluacionEgresados objListEncEgre;
    
    @Inject
    @Getter @Setter LogonMB logonMB;
    
    @EJB
    @Getter @Setter private EjbAdministracionEncuesta ejbAdmEncuesta;
    
    @PostConstruct
    public void init() {
        try {
            if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            nominaTrabajadorTutor = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
            claveTutor = logonMB.getListaUsuarioClaveNomina().getCvePersona();
            if (ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor) != null) {
                tutor = true;
                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.init() Es tutor" + claveTutor);
                obtenerAlumnosTutorEncuestaServicios();
                if (ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado()==11) {
                    tutorIngOnceavo = true;
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.init() es tutor de ingenieria");
                    obtenerAlumnosTutor();
                } else {
                    tutorIngOnceavo = false;
                }
            } else {
                tutor = false;
                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.init() No es tutor");
            }

        }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.init() e: " + e.getMessage());
        }
        

    }
    
    public void obtenerAlumnosTutor() {
        try {
            if (ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado() == 11) {
                listaEncuestaEgresados = new ArrayList<>();
                listadoEvaluacionCompleta = new ArrayList<>();
                listadoEvaluacionInCompleta = new ArrayList<>();
//                alumnosNoAccedieronEnc = ejbAdmEncuesta.obtenerTodoAlumno(claveTutor);
                alumnosNoAccedieronEnc=ejbAdmEncuesta.obtenerListaAlumnosNoAccedieron().stream().filter(x->x.getCveMaestro().equals(claveTutor)).filter(x->x.getGrado()==11).collect(Collectors.toList());
                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutor()"+alumnosNoAccedieronEnc);
                Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();
                alumnosNoAccedieronEnc.forEach(a -> {
//                    ListaEncuestaSatisfaccionEgresadosIng alumnosComleto = ejbAdmEncuesta.getEncuestaEgreporEvaluador(Integer.parseInt(a.getMatricula()));
                    EncuestaSatisfaccionEgresadosIng alumnosCompleto=ejbAdmEncuesta.getEncuestaEgreporEvaluador(Integer.parseInt(a.getMatricula()));
                    if (alumnosCompleto != null) {
                        if (comparador.isCompleto(alumnosCompleto)) {
                            ListadoEvaluacionEgresados completado = new ListadoEvaluacionEgresados(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor());
                            listadoEvaluacionCompleta.add(completado);
                        }else if (!comparador.isCompleto(alumnosCompleto)) {
                            ListadoEvaluacionEgresados incompleto = new ListadoEvaluacionEgresados(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor());
                            listadoEvaluacionInCompleta.add(incompleto);
                        }
                    } else {
                        ListadoEvaluacionEgresados noAccedieron = new ListadoEvaluacionEgresados(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor());
                        listaEncuestaEgresados.add(noAccedieron);
                    }
                });
                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutor() Encuestas completas"+ listadoEvaluacionCompleta.size());
                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutor() Encuestas incompletas"+ listadoEvaluacionInCompleta.size());
                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutor() Alumnos que no accedieron"+ listaEncuestaEgresados.size());
            }
            
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(administracionEncTutores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void obtenerAlumnosTutorEncuestaServicios(){
        try {
            if( ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado()==11 || ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado()==10 || ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado()==8 || ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado()==5 || ejbAdmEncuesta.obtenerCuatriPorTutor(claveTutor).getGrado()==2)
            listaEncuestaServicios = new ArrayList<>();
            listaEncuestaServCompleta= new ArrayList<>();
            listEncuestaServIncompleta=new ArrayList<>();
//            alumnosNoAccedieronEnc = ejbAdmEncuesta.obtenerTodoAlumno(claveTutor);
            alumnosNoAccedieronEnc=ejbAdmEncuesta.obtenerListaAlumnosNoAccedieron().stream().filter(x->x.getCveMaestro().equals(claveTutor)).collect(Collectors.toList());
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutorEncuestaServicios()"+alumnosNoAccedieronEnc);
            Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
            alumnosNoAccedieronEnc.forEach(a -> {
//                ListaEncuestaServicios alumnosComleto=ejbAdmEncuesta.obtenerEvaluadorEncuestaServ(Integer.parseInt(a.getMatricula()));
                EncuestaServiciosResultados alumnosComleto=ejbAdmEncuesta.getEncuestaporevaluador(Integer.parseInt(a.getMatricula()));
                if(alumnosComleto!=null){
                    if (comparador.isCompleto(alumnosComleto)) {
                        ListadoEncuestaServicios completado = new ListadoEncuestaServicios(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor());
                        listaEncuestaServCompleta.add(completado);
                    }else if (!comparador.isCompleto(alumnosComleto)) {
                        ListadoEncuestaServicios incompleto = new ListadoEncuestaServicios(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor());
                        listEncuestaServIncompleta.add(incompleto);
                    } 
                } else {
                    ListadoEncuestaServicios noAccedieron = new ListadoEncuestaServicios(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor());
                    listaEncuestaServicios.add(noAccedieron);
                }
            });
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutorEncuestaServicios() Encuesta por servicio completado:"+listaEncuestaServCompleta.size());
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutorEncuestaServicios() Encuesta por servicio completado:"+listEncuestaServIncompleta.size());
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionEncTutores.obtenerAlumnosTutorEncuestaServicios() Encuesta por servicio completado:"+listaEncuestaServicios.size());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(administracionEncTutores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
