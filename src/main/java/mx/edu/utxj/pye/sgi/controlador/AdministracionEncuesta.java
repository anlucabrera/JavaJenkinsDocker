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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaEgresados;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaServicio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class AdministracionEncuesta implements Serializable{
    
    private static final long serialVersionUID = 9051830636523223017L;
    
//    @Getter String cveDirector;
//    @Getter Integer claveTrabajador, cveMaestro;
//    @Getter Boolean tutor,esDeInformacionYEst;
//    
//    
//    
//    @Inject private LogonMB logonMB;
//    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
//    
    @Getter @Setter private Boolean director,esDeInformacionYEst;
    @Getter @Setter private Integer claveTrabajador, cveMaestro;
    @Getter @Setter private String cveDirector;
    @Getter @Setter private DatosGraficaEncuestaEgresados objDatosEncEgre;
    @Getter @Setter private List<DatosGraficaEncuestaEgresados> listDatosEncEgre=new ArrayList<>();
    @Getter @Setter private DatosGraficaEncuestaServicio objDatosEncServ;
    @Getter @Setter private List<DatosGraficaEncuestaServicio> listDatosEncServ= new ArrayList<>();
    @Getter @Setter private AlumnosEncuestas objAlumEncuesta;
    @Getter @Setter private List<AlumnosEncuestas> alumnosNoAccedieronEnc,alumnoNoAccedireonEncServ,listaAlumno,listaAlumnos, listAlumnoPorCar,listaEncuestaServiciosTotal;
    @Getter @Setter private PeriodosEscolares periodoEsc;
    //DTO
    @Getter @Setter private List<ListadoEvaluacionEgresados>listaEvNA, listadoEvaluacionCompleta, listadoEvaluacionIncompleta, listadoEvaluacionNa,listadoEvaluacionCompletaEstInf, listadoEvaluacionIncompletaEstInf;
    @Getter @Setter private ListadoEvaluacionEgresados objListaEgre;
    @Getter @Setter private List<ListadoEncuestaServicios> listaEncuestaServicios,listadoEncServiciosIncompleta, listaEncuestaIncompleta , listaEncuestaNA,listadoEncuestaServicios,listaEncuestaNAEstInf;
    @Getter @Setter private List<ListadoEncuestaServicios> listaFiltrado;
    @Getter @Setter private List<ListadoEvaluacionEgresados> listaSatFiltrado;
    @Getter @Setter private ListadoEncuestaServicios objListado;
    @Getter @Setter private ListadoGraficaEncuestaServicios objListGrafEncSer;
    @Getter @Setter private List<ListadoGraficaEncuestaServicios> listGrafEncServ;
    @Getter @Setter private Map<String,Long> collect = new HashMap<>();
    @Getter @Setter private Map<String,Long> collect2 = new HashMap<>();
    @Getter private UsuarioTipo usuarioTipo;
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    
    
    @PostConstruct
    public void init() {
        
        Long inicio=System.currentTimeMillis();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.init()"+ inicio);
        try {
//            if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
//                claveTrabajador = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
//                cveMaestro = logonMB.getListaUsuarioClaveNomina().getCvePersona();
//                cveDirector = cveMaestro.toString();
//                if (!ejbAdmEncuesta.esDirectorDeCarrera(28, 2, 18, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
//                    director = true;
//                    initSeguimientoEncuestasDirector();
//                    datosTablaAvance();
//                }
//                if (claveTrabajador.equals(579) || claveTrabajador != null && claveTrabajador.equals(148) || claveTrabajador.equals(240) || claveTrabajador.equals(28) ) {
//                    //|| claveTrabajador.equals(579) esta parte va dentro del if de arriba
//                    esDeInformacionYEst = true;
//                    //initSeguimientoEncuetasEstadistica();
//                    //datosTablaAvance();
//                }
//            }
//            datosTablaAvance();
////            contarAlumnosPorCarDeEncSer();
////            contarAlumnosPorCarrera();
        } catch (Throwable ex) {
            director = false;
            esDeInformacionYEst = false;
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long fin=System.currentTimeMillis();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.init()"+ fin);
        
        Long retardo=inicio-fin;
        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.init() EL retardo del init es:" + retardo);
    }
    
    public void initSeguimientoEncuestasDirector(){
        try {
                alumnosNoAccedieronVistaDirector();
                alumnosNoAccedieronEncServiciosVistaDirector();
            
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void initSeguimientoEncuetasEstadistica(){
        try {
                alumnosNoAccedieronEncuestaEgreVistaEstInf();
                alumnosNoAccedieronVistaEstInf();
            
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void alumnosNoAccedieronVistaDirector(){
        try {
            listadoEvaluacionCompleta = new ArrayList<>();
            listadoEvaluacionIncompleta = new ArrayList<>();
            listadoEvaluacionNa=new ArrayList<>();
            alumnosNoAccedieronEnc = ejbAdmEncuesta.obtenerAlumno(cveDirector).stream().filter(x->x.getGrado()==11).collect(Collectors.toList());
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.alumnosNoAccedieronVistaDirector()"+ alumnosNoAccedieronEnc);
            Comparador<EncuestaSatisfaccionEgresadosIng> comparador= new ComparadorEncuestaSatisfaccionEgresadosIng();
            alumnosNoAccedieronEnc.forEach(e -> {
                try {
                    //                ListaEncuestaSatisfaccionEgresadosIng listaCOmpleta=ejbAdmEncuesta.obtenerEvaluadorEncuesta(Integer.parseInt(e.getMatricula()));
                    EncuestaSatisfaccionEgresadosIng listaCOmpleta=ejbAdmEncuesta.getEncuestaEgreporEvaluador(Integer.parseInt(e.getMatricula()));
                    if (listaCOmpleta!= null) {
                        
                        if (comparador.isCompleto(listaCOmpleta)) {
                            ListadoEvaluacionEgresados completado = new ListadoEvaluacionEgresados(Integer.parseInt(e.getMatricula()), e.getNombre() + " " + e.getApellidoPat() + " " + e.getApellidoMat(), e.getAbreviatura(), e.getGrado(), e.getIdGrupo(), e.getNombreTutor()+" "+e.getApPatTutor()+" "+e.getApMatTutor());
                            listadoEvaluacionCompleta.add(completado);
                        }
                        if(!comparador.isCompleto(listaCOmpleta)) {
                            ListadoEvaluacionEgresados incompleto = new ListadoEvaluacionEgresados(Integer.parseInt(e.getMatricula()), e.getNombre() + " " + e.getApellidoPat() + " " + e.getApellidoMat(), e.getAbreviatura(), e.getGrado(), e.getIdGrupo(), e.getNombreTutor()+" "+e.getApPatTutor()+" "+e.getApMatTutor());
                            listadoEvaluacionIncompleta.add(incompleto);
                        }
                    } else {
                        ListadoEvaluacionEgresados Incompleto = new ListadoEvaluacionEgresados(Integer.parseInt(e.getMatricula()), e.getNombre() + " " + e.getApellidoPat() + " " + e.getApellidoMat(), e.getAbreviatura(), e.getGrado(), e.getIdGrupo(),e.getNombreTutor()+" "+e.getApPatTutor()+" "+e.getApMatTutor());
                        listadoEvaluacionNa.add(Incompleto);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            System.out.println("satisfaccion no accedio: " + listadoEvaluacionNa.size());
            System.out.println("satisfaccion  accedio: " + listadoEvaluacionIncompleta.size());
            System.out.println("satisfaccion  accedio y completo: " + listadoEvaluacionCompleta.size());
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void alumnosNoAccedieronEncServiciosVistaDirector(){
        try {
            listaEncuestaNA = new ArrayList<>();
            listaEncuestaIncompleta=new ArrayList<>();
            listaEncuestaServicios = new ArrayList<>();
            alumnoNoAccedireonEncServ = ejbAdmEncuesta.obtenerAlumno(cveDirector);
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.alumnosNoAccedieronEncServiciosVistaDirector() total alumnos director:"+alumnoNoAccedireonEncServ.size());
// se implementa la interfaz funcional comparador para poder validar la encuesta
            Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
            alumnoNoAccedireonEncServ.forEach(x -> {
                try {
                    EncuestaServiciosResultados estudianteTest = ejbAdmEncuesta.getEncuestaporevaluador(Integer.parseInt(x.getMatricula()));
                    
                    if (estudianteTest != null) {
                        if (comparador.isCompleto(estudianteTest)) {
                            ListadoEncuestaServicios completado = new ListadoEncuestaServicios(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listaEncuestaServicios.add(completado);
//                        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.alumnosNoAccedieronEncServiciosVistaDirector() Alumnos que completaron"+listaEncuestaServicios);
                        }
                        if(!comparador.isCompleto(estudianteTest)) {
                            ListadoEncuestaServicios incompleto = new ListadoEncuestaServicios(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listaEncuestaIncompleta.add(incompleto);
                        }
                    } else {
                        ListadoEncuestaServicios noAccedieron = new ListadoEncuestaServicios(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                        listaEncuestaNA.add(noAccedieron);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            System.out.println("serv no accedio: " + listaEncuestaNA.size());
            System.out.println("serv  accedio: " + listaEncuestaIncompleta.size());
            System.out.println("serv  accedio y completo: " + listaEncuestaServicios.size());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alumnosNoAccedieronEncuestaEgreVistaEstInf() {
        try {
            
                listadoEvaluacionIncompletaEstInf = new ArrayList<>();
                listadoEvaluacionCompletaEstInf = new ArrayList<>();
                listaEvNA = new ArrayList<>();
                listaAlumnos = ejbAdmEncuesta.obtenerListaAlumnosNoAccedieron().stream().filter(x -> x.getGrado() == 11).collect(Collectors.toList());
                Comparador<EncuestaSatisfaccionEgresadosIng> comparador= new ComparadorEncuestaSatisfaccionEgresadosIng();
                listaAlumnos.forEach(a -> {
                    try {
                        EncuestaSatisfaccionEgresadosIng listaEvCompleta=ejbAdmEncuesta.getEncuestaEgreporEvaluador(Integer.parseInt(a.getMatricula()));
                        if (listaEvCompleta != null) {
                            if (comparador.isCompleto(listaEvCompleta)) {
                                ListadoEvaluacionEgresados completado = new ListadoEvaluacionEgresados(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor() + " " + a.getApPatTutor() + " " + a.getApMatTutor());
                                listadoEvaluacionCompletaEstInf.add(completado);
                            }
                            if(!comparador.isCompleto(listaEvCompleta)) {
                                ListadoEvaluacionEgresados incompleto = new ListadoEvaluacionEgresados(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor() + " " + a.getApPatTutor() + " " + a.getApMatTutor());
                                listaEvNA.add(incompleto);
                            }
                        } else {
                            ListadoEvaluacionEgresados Incompleto = new ListadoEvaluacionEgresados(Integer.parseInt(a.getMatricula()), a.getNombre() + " " + a.getApellidoPat() + " " + a.getApellidoMat(), a.getAbreviatura(), a.getGrado(), a.getIdGrupo(), a.getNombreTutor() + " " + a.getApPatTutor() + " " + a.getApMatTutor());
                            listadoEvaluacionIncompletaEstInf.add(Incompleto);
                        }
                    } catch (Throwable ex) {
                        Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                System.out.println("Estudiantes que han finalizado encuesta: " + listadoEvaluacionCompletaEstInf.size());
            System.out.println("Estudiantes que no han accedido: " + listaEvNA.size());
            System.out.println("Estudiantes con encuesta incompleta: " + listadoEvaluacionIncompletaEstInf.size());
            

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void alumnosNoAccedieronVistaEstInf() {
        try {
                listaEncuestaNAEstInf = new ArrayList<>();
                listadoEncuestaServicios = new ArrayList<>();
                listadoEncServiciosIncompleta = new ArrayList<>();
                listaAlumno = ejbAdmEncuesta.obtenerListaAlumnosNoAccedieron();
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.alumnosNoAccedieronVistaEstInf()" + listaAlumno);
                Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
                listaAlumno.forEach(x -> {
                    try {
                        EncuestaServiciosResultados listaCompleta=ejbAdmEncuesta.getEncuestaporevaluador(Integer.parseInt(x.getMatricula()));
                        if (listaCompleta != null) {
                            if (comparador.isCompleto(listaCompleta)) {
                                ListadoEncuestaServicios completado = new ListadoEncuestaServicios(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                                listadoEncuestaServicios.add(completado);
                            }
                            if (!comparador.isCompleto(listaCompleta)) {
                                ListadoEncuestaServicios incompleto = new ListadoEncuestaServicios(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                                listadoEncServiciosIncompleta.add(incompleto);
                            }
                        } else {
                            ListadoEncuestaServicios noAccedieron = new ListadoEncuestaServicios(Integer.parseInt(x.getMatricula()), x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                            listaEncuestaNAEstInf.add(noAccedieron);
                        }
                    } catch (Throwable ex) {
                        Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                System.out.println("egresado no accedio: " + listadoEncuestaServicios.size());
            System.out.println("egresado  accedio: " + listadoEncServiciosIncompleta.size());
            System.out.println("egresado  accedio y completo: " + listaEncuestaNAEstInf.size());

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    /**
//     * Metodo que devuelve datos de avance de cada una de las encuestas, siendo utilizada para cuelaquier 
//     * director, para secretaría academica, y personal de informacion y estadistica
//     * 
//     */
    public void datosTablaAvance(){
        try {
            listDatosEncEgre=ejbAdmEncuesta.listaDatosAvanceEncEgresados();
            listDatosEncServ=ejbAdmEncuesta.listaAvanceEncServ();
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta.datosTablaAvance()"+ listDatosEncEgre);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
